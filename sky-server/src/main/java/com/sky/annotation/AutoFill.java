package com.sky.annotation;

//自定义注解，用于自动填充公共字段

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//表示这个注解只能放在方法上
@Target(ElementType.METHOD)

//表示这个注解在运行时
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {

    //数据库操作类型
    /**
     * 插入操作
     * 更新操作
     */
    OperationType value();
}
