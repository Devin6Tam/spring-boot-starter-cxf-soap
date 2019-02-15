package com.mzbloc.springboot.cxf.soap.helper;

import com.mzbloc.springboot.cxf.soap.util.PropsUtil;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * SOAP 助手类
 *
 * @author huangyong
 * @since 1.0.0
 */
public class SoapHelper {

    private static final List<Interceptor<? extends Message>> inInterceptorList = new ArrayList<Interceptor<? extends Message>>();
    private static final List<Interceptor<? extends Message>> outInterceptorList = new ArrayList<Interceptor<? extends Message>>();

    static {
        // 添加 Logging Interceptor
        Boolean isLog = PropsUtil.getBoolean(PropsUtil.loadProps("application.properties"),"mzbloc.soap.interceptorLogEnable");
        if (isLog) {
            LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
            inInterceptorList.add(loggingInInterceptor);
            LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
            outInterceptorList.add(loggingOutInterceptor);
        }
    }

    /**
     * 发布 SOAP 服务
     */
    private static void publishService(String wsdl, Class<?> interfaceClass, Object implementInstance) {
        ServerFactoryBean factory = new ServerFactoryBean();
        factory.setAddress(wsdl);
        factory.setServiceClass(interfaceClass);
        factory.setServiceBean(implementInstance);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(outInterceptorList);
        factory.create();
    }

    /**
     * 创建 SOAP 客户端
     */
    public static <T> T createClient(String wsdl, Class<? extends T> interfaceClass,
                                     Interceptor<? extends Message> outInterceptor) {
        ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
        factory.setAddress(wsdl);
        factory.setServiceClass(interfaceClass);
        factory.setInInterceptors(inInterceptorList);
        if(outInterceptor!=null) {
            outInterceptorList.add(outInterceptor);
        }
        factory.setOutInterceptors(outInterceptorList);

        return factory.create(interfaceClass);
    }
}
