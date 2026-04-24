package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {

        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //将password进行MD5加密后与数据库中进行比较
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //进行密码比较
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public PageResult empList(EmployeePageQueryDTO employeePageQueryDTO) {

        //设置分页参数
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        //执行查询
       List<Employee> empList = employeeMapper.selectEmpByPage(employeePageQueryDTO.getName());
       //数据转换
        Page<Employee> pageEmp =(Page<Employee>) empList;

        return new PageResult(pageEmp.getTotal(),pageEmp.getResult());
    }

    @Override
    public void addEmp(EmployeeDTO employeeDTO) {

        Employee employee = Employee.builder()
                .username(employeeDTO.getUsername())
                .name(employeeDTO.getName())
                .phone(employeeDTO.getPhone())
                .sex(employeeDTO.getSex())
                .idNumber(employeeDTO.getIdNumber())
                .build();
        //设置账号状态，默认为1，启用状态
        employee.setStatus(StatusConstant.ENABLE);
        //设置默认密码，MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //设置创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //设置创建人，修改人
        //TODO 获取当前登录用户的ID
        employee.setCreateUser(10086L);
        employee.setUpdateUser(10086L);

        employeeMapper.insert(employee);
    }

    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        //比对就旧密码是否正确
       Employee employee = employeeMapper.selectEmpById(passwordEditDTO.getEmpId());
       //获取旧密码的MD5加密值
       String oldPassword = DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes());
        if(!employee.getPassword().equals(oldPassword)){
            //如果旧密码不一致
            throw new PasswordErrorException(MessageConstant.PASSWORD_EDIT_FAILED);
        }
        //如果到这一步，说明旧密码正确，就可以修改密码
        String newPassword = DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes());

        //操作数据库更新密码
        employeeMapper.updatePassword(passwordEditDTO.getEmpId(),newPassword);
    }

    @Override
    public void updateStatus(String status, String id) {
        employeeMapper.updateStatusById(status,id);
    }

    @Override
    public Employee getEmpById(Long id) {

        //根据id查询员工信息
        return employeeMapper.selectEmpById(id);
    }


}
