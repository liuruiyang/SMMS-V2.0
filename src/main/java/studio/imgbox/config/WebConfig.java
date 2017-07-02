package studio.imgbox.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class WebConfig {

	/**
	 * Tomcat优化配置
	 * 全局强制HTTPS访问
	 * @return
	 */
	@Bean
	public TomcatEmbeddedServletContainerFactory containerFactory() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
			@Override
			protected void customizeConnector(Connector connector) {
				super.customizeConnector(connector);
				if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
					((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(50*1024*1024);
					((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxThreads(100);
					((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMinSpareThreads(20);
					((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setConnectionTimeout(12000);
					((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxConnections(1000);
				}
			}

			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
		return tomcat;
	}

	
	private Connector initiateHttpConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(80);
		connector.setSecure(false);
		connector.setRedirectPort(443);
		return connector;
	}

	/**
	 * 自定义SpringBoot的错误页面
	 * @return
	 */
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {

			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/assets/ep/403.html"));
				container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/assets/ep/404.html"));
				container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/assets/ep/50X.html"));
				container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/assets/ep/50X.html"));
				container.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/assets/ep/403.html"));
			}
		};
	}
}
