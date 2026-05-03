package com.sky.aspect;

//自定义切面类，实现了公共字段的自动填充

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

//表明是个切面类
@Aspect
//交给ioc容器管理
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    // 表示在mapper包下的所有方法上添加切点,并且是带AutoFill注解的方法
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * Before: 前置通知
     * JoinPoint: 连接点
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){

        log.info("开始进行公共字段填充....");

        //获取到当前被拦截到方法的类型：update，insert
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();    //获取到方法签名
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);  //获取到方法中的AutoFill注解
        OperationType operationType = autoFill.value();         //获取注解的操作类型
        //获取到方法的参数---实体对象
        Object[] args = joinPoint.getArgs();    //获取到方法的参数，约定第一个参数是实体对象
        //判空，避免空指针异常
        if(args == null || args.length == 0){
            //如果为空，直接返回
            return;
        }

        //获取第一个实体对象
        Object entity = args[0];

        //准备需要赋值的数据
        LocalDateTime now = LocalDateTime.now();    //获取当前时间
        long currentId = BaseContext.getCurrentId();//获取当前登录用户的ID

        //根据不同的操作类型，为对应的属性复制
        if(operationType ==OperationType.INSERT){
            //插入操作，需要填充4个字段

            try{
                //通过反射获取set方法
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射调用方法
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            }catch (Exception e){
                e.printStackTrace();
            }


        }


        if(operationType ==OperationType.UPDATE){
            //更新操作，需要填充2个字段

            try{
                //通过反射获取set方法
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射调用方法
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
