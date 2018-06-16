package com.realsnake.sample.config;

import java.util.Collections;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    /** HTTPS를 사용하기 때문에 HTTP를 위해 별도 포트 사용 */
    @Value("${server.http.port}")
    private int serverHttpPort;

    @Value("${server.session.timeout}")
    private Integer maxInactiveIntervalInSeconds;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();

        TomcatConnectorCustomizer connectorCustomizer = connector -> {
            connector.setAttribute("maxThreads", 1024);
            connector.setAttribute("acceptCount", 10);
            connector.setAttribute("maxConnections", 8192);
            connector.setAttribute("minSpareThreads", 512);
            connector.setAttribute("URIEncoding", "UTF-8");
            connector.setAttribute("connectionTimeout", 10000);
            connector.setAttribute("connectionUploadTimeout", 180000);
            connector.setAttribute("keepAliveTimeout", 5000);
            connector.setMaxParameterCount(40000);

            // HTTP 압축설정(HTTPS 사용 시에는 주의해야 함)
            // connector.setAttribute("compression", "on");
            // connector.setAttribute("compressableMimeType", "text/html,text/xml,text/plain,application/json,application/xml");
            // connector.setAttribute("compressionMinSize", "2048"); // 2KB 이하는 압축안함
        };

        factory.setTomcatConnectorCustomizers(Collections.singletonList(connectorCustomizer));

        factory.setSessionTimeout(this.maxInactiveIntervalInSeconds);

        Connector connector = new Connector(TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL);
        connector.setPort(this.serverHttpPort);
        factory.addAdditionalTomcatConnectors(connector);

        return factory;
    }

    /* @formatter:off */
    /* @formatter:on */

}
