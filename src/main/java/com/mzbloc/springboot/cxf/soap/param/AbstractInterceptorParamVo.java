package com.mzbloc.springboot.cxf.soap.param;

import lombok.Data;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanxw on 2019/2/1.
 */
@Data
public abstract class AbstractInterceptorParamVo extends AbstractJsonSeriable{

    private List<Interceptor<? extends Message>> inInterceptorList = new ArrayList<Interceptor<? extends Message>>();
    private List<Interceptor<? extends Message>> outInterceptorList = new ArrayList<Interceptor<? extends Message>>();

}
