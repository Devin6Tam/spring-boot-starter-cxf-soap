package com.mzbloc.springboot.cxf.soap.config;

import com.mzbloc.springboot.cxf.soap.annotation.Soap;
import com.mzbloc.springboot.cxf.soap.param.ServerConfigParamVo;
import com.mzbloc.springboot.cxf.soap.util.ClassUtil;
import com.mzbloc.springboot.cxf.soap.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by tanxw on 2019/1/31.
 */
public class SoapServlet extends CXFServlet{

    private SoapProperties soapProperties;
    private List<Interceptor<? extends Message>> inInterceptors;
    private List<Interceptor<? extends Message>> outInterceptors;

    public void setSoapProperties(SoapProperties soapProperties, List<Interceptor<? extends Message>> inInterceptors,
                                  List<Interceptor<? extends Message>> outInterceptors) {
        this.soapProperties = soapProperties;
        this.inInterceptors = inInterceptors;
        this.outInterceptors = outInterceptors;
    }

    public SoapServlet() {
    }

    @Override
    protected void loadBus(ServletConfig servletConfig) {
        // 初始化 CXF 总线
        super.loadBus(servletConfig);
        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
        // 发布 SOAP 服务
        publishSoapService();
    }

    private void publishSoapService() {
        // 遍历所有标注了 SOAP 注解的类
        Set<Class<?>> soapClassSet = ClassUtil.getClassSet(soapProperties.getScan(),Soap.class);
        if (!CollectionUtils.isEmpty(soapClassSet)) {
            for (Class<?> soapClass : soapClassSet) {
                // 获取 SOAP 地址
                String address = getAddress(soapClass);
                // 获取 SOAP 类的接口
                Class<?> soapInterfaceClass = getSoapInterfaceClass(soapClass);
                // 获取 SOAP 类的实例
                Object soapInstance = ReflectionUtil.newInstance(soapClass);

                // 发布 SOAP 服务
                ServerConfigParamVo serverConfigParam = getServerConfigParam(address, soapInterfaceClass, soapInstance);
                /**输入过滤器设置*/
                if(!CollectionUtils.isEmpty(inInterceptors)){
                    List<Interceptor<? extends Message>> inInterceptorList = serverConfigParam.getInInterceptorList();
                    for (Interceptor<? extends Message> interceptor:inInterceptors){
                        inInterceptorList.add(interceptor);
                    }
                    serverConfigParam.setInInterceptorList(inInterceptorList);
                }
                /**输出过滤器设置*/
                if(!CollectionUtils.isEmpty(outInterceptors)){
                    List<Interceptor<? extends Message>> outInterceptorList = serverConfigParam.getOutInterceptorList();
                    for (Interceptor<? extends Message> interceptor:outInterceptors){
                        outInterceptorList.add(interceptor);
                    }
                    serverConfigParam.setInInterceptorList(outInterceptorList);
                }
                publishService(serverConfigParam);
            }
        }
    }

    /**
     * 组装服务配置参数
     * @param address
     * @param interfaceClass
     * @param implementInstance
     * @return
     */
    public ServerConfigParamVo getServerConfigParam(String address, Class<?> interfaceClass, Object implementInstance){
        ServerConfigParamVo serverConfigParamVo = new ServerConfigParamVo();
        serverConfigParamVo.setAddress(address);
        serverConfigParamVo.setInterfaceClass(interfaceClass);
        serverConfigParamVo.setImplementInstance(implementInstance);
        serverConfigParamVo.setInInterceptorList(getInInterceptorList());
        serverConfigParamVo.setOutInterceptorList(getOutInterceptorList());
        return serverConfigParamVo;
    }


    public List<Interceptor<? extends Message>> getInInterceptorList(){
        List<Interceptor<? extends Message>> inInterceptorList = new ArrayList<>();
        if(soapProperties.isInterceptorLogEnable()) {
            inInterceptorList.add(new LoggingInInterceptor());
        }
        return inInterceptorList;
    }

    public List<Interceptor<? extends Message>> getOutInterceptorList(){
        List<Interceptor<? extends Message>> outInterceptorList = new ArrayList<>();
        if(soapProperties.isInterceptorLogEnable()) {
            outInterceptorList.add(new LoggingOutInterceptor());
        }
        return outInterceptorList;
    }

    /**
     * 发布 SOAP 服务
     */
    private void publishService(ServerConfigParamVo serverConfig) {
        ServerFactoryBean factory = new ServerFactoryBean();
        factory.setAddress(serverConfig.getAddress());
        factory.setServiceClass(serverConfig.getInterfaceClass());
        factory.setServiceBean(serverConfig.getImplementInstance());
        factory.setInInterceptors(serverConfig.getInInterceptorList());
        factory.setOutInterceptors(serverConfig.getOutInterceptorList());
        factory.create();
    }

    private Class<?> getSoapInterfaceClass(Class<?> soapClass) {
        // 获取 SOAP 实现类的第一个接口作为 SOAP 服务接口
        return soapClass.getInterfaces()[0];
    }

    private String getAddress(Class<?> soapClass) {
        String address;
        // 若 SOAP 注解的 value 属性不为空，则获取当前值，否则获取类名
        String soapValue = soapClass.getAnnotation(Soap.class).value();
        if (StringUtils.isNotEmpty(soapValue)) {
            address = soapValue;
        } else {
            address = getSoapInterfaceClass(soapClass).getSimpleName();
        }
        // 确保最前面只有一个 /
        if (!address.startsWith("/")) {
            address = "/" + address;
        }
        address = address.replaceAll("\\/+", "/");
        return address;
    }
}
