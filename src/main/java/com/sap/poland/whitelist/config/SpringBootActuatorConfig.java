package com.sap.poland.whitelist.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties
//@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class/*, MongoDataAutoConfiguration.class /*, RabbitAutoConfiguration.class*/ })
public class SpringBootActuatorConfig {

}
