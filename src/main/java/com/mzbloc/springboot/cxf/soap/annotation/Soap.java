package com.mzbloc.springboot.cxf.soap.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SOAP 服务注解
 * Created by tanxw on 2019/1/30.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Soap {

    /**
     * 服务名
     */
    String value() default "";
}
