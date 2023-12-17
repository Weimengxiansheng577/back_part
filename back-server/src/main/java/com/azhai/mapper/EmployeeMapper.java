package com.azhai.mapper;

import com.azhai.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * @description: 插入员工数据
     * @author Administrator
     * @date 2023/12/17 16:45
     * @version 1.0
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "VALUES " +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Employee employee);
}
