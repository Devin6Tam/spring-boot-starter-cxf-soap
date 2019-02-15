package com.mzbloc.springboot.cxf.soap.config;

/**
 * Created by tanxw on 2019/2/15.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tanxw on 2019/1/31.
 */
@Configuration
@EnableConfigurationProperties(SoapProperties.class)
public class SoapConfig {

    @Autowired
    private SoapProperties soapProperties;

}
