package com.azhai.aspect;

import com.azhai.annotation.AutoFill;
import com.azhai.constant.AutoFillConstant;
import com.azhai.context.BaseContext;
import com.azhai.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author 自定义切面，实现公共字段自动填充处理
 * @version 1.0
 * @description: TODO
 * @date 2023/12/18 17:18
 */
@Aspect
@Component
@Slf4j
public class AutoFileAspect {


    /**
     * @description: 定义切入点
     * @author Administrator
     * @date 2023/12/18 17:22
     * @version 1.0
     */
    @Pointcut("execution(* com.azhai.mapper.*.*(..)) && @annotation(com.azhai.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * @description: 前置通知，再通知中进行公共字段的赋值
     * @author Administrator
     * @date 2023/12/18 17:31
     * @version 1.0
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("前置通知，再通知中进行公共字段的赋值");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);

        OperationType op = autoFill.value();

        Object[] args = joinPoint.getArgs();

        if (args == null || args.length == 0) {
            return;
        }

        Object entity = args[0];

        LocalDateTime localDateTime = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        if (op == OperationType.INSERT) {
            //给四个字段分配数据
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //通过反射为对象属性赋值
                setCreateTime.invoke(entity, localDateTime);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, localDateTime);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (op == OperationType.UPDATE) {
            //给两个字段分配数据
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity, localDateTime);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
