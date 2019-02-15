package com.mzbloc.springboot.cxf.soap.param;

import lombok.Data;

/**
 * Created by tanxw on 2019/2/1.
 */
@Data
public class ServerConfigParamVo extends AbstractInterceptorParamVo{

    /**
     * 接口地址
     */
    private String address;

    /**
     * 服务接口
     */
    private Class<?> interfaceClass;

    /**
     * 服务接口实现类
     */
    private Object implementInstance;
}
