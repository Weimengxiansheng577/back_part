package com.azhai.service;


import com.azhai.dto.EmployeeDTO;
import com.azhai.dto.EmployeeLoginDTO;
import com.azhai.dto.EmployeePageQueryDTO;
import com.azhai.entity.Employee;
import com.azhai.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * @description: 新增员工
     * @author zhai
     * @date 2023/12/17 16:28
     * @version 1.0
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * @description: 扩展分页查询方法
     * @author Administrator
     * @date 2023/12/18 3:21
     * @version 1.0
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
}
