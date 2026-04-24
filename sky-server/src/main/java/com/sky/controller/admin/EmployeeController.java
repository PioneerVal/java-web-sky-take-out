package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 员工的分页查询
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> empList(EmployeePageQueryDTO employeePageQueryDTO){
       PageResult empList = employeeService.empList(employeePageQueryDTO);

        return Result.success(empList);
    }

    /**
     * 添加员工
     * @param employeeDTO
     * @return
     */
    @PostMapping
    public Result<T> addEmp(@RequestBody EmployeeDTO employeeDTO){
        employeeService.addEmp(employeeDTO);
        return Result.success();
    }

    /**
     * 修改密码
     * @param passwordEditDTO
     * @return
     */
    @PutMapping("/editPassword")
    public Result<T> editPassword(@RequestBody PasswordEditDTO passwordEditDTO){

        log.info("修改密码：{}",passwordEditDTO);
        //调用service层服务
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }

    /**
     * 修改员工状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable String status,String id){

        log.info("修改员工状态，被修改人ID：{}，状态改为：{}",id,status);
        employeeService.updateStatus(status,id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Employee> getEmpById(@PathVariable Long id){
        log.info("根据ID查询员工信息：{}",id);
        //调用service层服务
       Employee employee = employeeService.getEmpById(id);
       return Result.success(employee);
    }
}
