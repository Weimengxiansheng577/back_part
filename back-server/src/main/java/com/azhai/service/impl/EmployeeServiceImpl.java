package com.azhai.service.impl;

import com.azhai.constant.MessageConstant;
import com.azhai.constant.PasswordConstant;
import com.azhai.constant.StatusConstant;
import com.azhai.context.BaseContext;
import com.azhai.dto.EmployeeDTO;
import com.azhai.dto.EmployeeLoginDTO;
import com.azhai.dto.EmployeePageQueryDTO;
import com.azhai.entity.Employee;
import com.azhai.exception.AccountLockedException;
import com.azhai.exception.AccountNotFoundException;
import com.azhai.exception.PasswordErrorException;
import com.azhai.mapper.EmployeeMapper;
import com.azhai.result.PageResult;
import com.azhai.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
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
        // 密码加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
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

    /**
     * @description: 新增员工
     * @author azhai
     * @date 2023/12/17 16:29
     * @version 1.0
     */
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //把前面的属性赋值给后边
        BeanUtils.copyProperties(employeeDTO,employee);

        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //设置当前记录创建人和修改人id
        // 改为当前用户的id
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);

    }


    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        long total = page.getTotal();
        List<Employee> result = page.getResult();
        return new PageResult(total,result);
    }

}
