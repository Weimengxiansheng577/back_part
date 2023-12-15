package com.azhai.service;


import com.azhai.dto.EmployeeLoginDTO;
import com.azhai.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
