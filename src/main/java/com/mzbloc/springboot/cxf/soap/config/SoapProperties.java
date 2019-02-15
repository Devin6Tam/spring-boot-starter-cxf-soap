package com.mzbloc.springboot.cxf.soap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by tanxw on 2019/1/31.
 */
@ConfigurationProperties(prefix = "mzbloc.soap")
public class SoapProperties {

    /**
     * 扫描路径
     */
    private String scan;
    /**
     * 服务访问地址
     */
    private String serverUrl;

    /**
     * 拦截器是否启动日志功能
     */
    private boolean interceptorLogEnable;

    public String getScan() {
        return scan;
    }

    public void setScan(String scan) {
        this.scan = scan;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public boolean isInterceptorLogEnable() {
        return interceptorLogEnable;
    }

    public void setInterceptorLogEnable(boolean interceptorLogEnable) {
        this.interceptorLogEnable = interceptorLogEnable;
    }
}
