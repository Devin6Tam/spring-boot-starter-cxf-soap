package com.mzbloc.springboot.cxf.soap.param;

import lombok.Data;

/**
 * Created by tanxw on 2019/2/1.
 */
@Data
public class ClientRequestParamVo extends AbstractInterceptorParamVo{
    /**
     * 接口地址
     */
    private String address;

    /**
     * 服务接口
     */
    private Class<?> interfaceClass;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
