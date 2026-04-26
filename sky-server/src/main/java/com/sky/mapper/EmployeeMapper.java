package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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


    //添加员工
    @Insert("insert into employee (name, username, password, phone, sex,status, id_number, create_time, update_time, create_user, update_user)" +
            "values(#{name},#{username},#{password},#{phone},#{sex},#{status},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser}) ")
    void insert(Employee employee);

    //根据ID查询员工信息
    @Select("select * from employee where id = #{empId}")
    Employee selectEmpById(Long empId);

    //更新密码
    @Update("update employee set password = #{newPassword} where id = #{empId} ")
    void updatePassword(Long empId,String newPassword);

    //修改员工状态
    @Update("update employee set status = #{status} where id = #{id}")
    void updateStatusById(String status, String id);

    //根据ID修改员工信息，需要用到动态sql,使用xml方式
    void updateEmpById(Employee employee);
}
