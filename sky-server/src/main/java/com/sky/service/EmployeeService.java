package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 分页查找员工信息
     * @param employeePageQueryDTO
     * @return PageResult
     */
    PageResult empList(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 添加员工信息
     * @param employeeDTO
     */
    void addEmp(EmployeeDTO employeeDTO);

    /**
     * 修改密码
     * @param passwordEditDTO
     */
    void editPassword(PasswordEditDTO passwordEditDTO);

    /**
     * 修改员工状态
     * @param status
     * @param id
     */
    void updateStatus(String status, String id);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    Employee getEmpById(Long id);
}
