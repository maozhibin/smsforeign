package com.cunyun.smsforeign;

import org.apache.coyote.http11.Http11Nio2Protocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

/**
 * @author liuke1@geely.com
 * @version 1.0
 * @since v1.0 2017/11/8 14:10
 */
@Configuration
public class MyEmbeddedServletContainerFactory extends TomcatEmbeddedServletContainerFactory {

    @Override
    public EmbeddedServletContainer getEmbeddedServletContainer(
            ServletContextInitializer... initializers) {
        this.setProtocol(Http11Nio2Protocol.class.getName());
        this.setUriEncoding(Charset.forName("UTF-8"));
        return super.getEmbeddedServletContainer(initializers);
    }
}
