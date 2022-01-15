package com.miaosha.config;

import org.apache.coyote.http11.Http11Nio2Protocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * @author yiminren
 * 定制化内嵌Tomcat
 * 当Spring容器内没有TomcatEmbeddedServletContainerFactory时，会将这个Bean加载进spring容器
 */
@Component
public class WebServerConfiguration implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // 使用工厂类提供的接口定制化tomcat connector
        TomcatServletWebServerFactory tomcatServletWebServerFactory = (TomcatServletWebServerFactory) factory;
        tomcatServletWebServerFactory.addConnectorCustomizers(connector -> {
            Http11NioProtocol protocolHandler = (Http11NioProtocol) connector.getProtocolHandler();
            // 定制化keepAliveTimeout
            protocolHandler.setKeepAliveTimeout(30_000);
            protocolHandler.setMaxKeepAliveRequests(10_000);
        });

    }
}
