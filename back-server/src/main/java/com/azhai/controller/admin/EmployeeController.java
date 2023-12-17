package com.azhai.controller.admin;


import com.azhai.constant.JwtClaimsConstant;
import com.azhai.dto.EmployeeDTO;
import com.azhai.dto.EmployeeLoginDTO;
import com.azhai.dto.EmployeePageQueryDTO;
import com.azhai.entity.Employee;
import com.azhai.properties.JwtProperties;
import com.azhai.result.PageResult;
import com.azhai.result.Result;
import com.azhai.service.EmployeeService;
import com.azhai.utils.JwtUtil;
import com.azhai.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关的接口")
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
    @ApiOperation(value = "员工的登录方法")
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
    @ApiOperation(value = "退出登录的方法")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * @description: 新增员工
     * @author azhai
     * @date 2023/12/17 16:22
     * @version 1.0
     */
    @PostMapping
    @ApiOperation(value = "新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info(employeePageQueryDTO.toString());
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

}
