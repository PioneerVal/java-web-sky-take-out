package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    //分页查询员工信息，使用动态sql
    List<Employee> selectEmpByPage(String name);

    @Insert("insert into employee (name, username, password, phone, sex,status, id_number, create_time, update_time, create_user, update_user)" +
            "values(#{name},#{username},#{password},#{phone},#{sex},#{status},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser}) ")
    void insert(Employee employee);
}
