package br.gov.sp.cps.produto_soap.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.web.servlet.ServletRegistrationBean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurer;
import org.springframework.ws.server.EndpointInterceptor;

import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.SimplePasswordValidationCallbackHandler;

import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;

import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig implements WsConfigurer {

	@Bean
	public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();

		servlet.setApplicationContext(context);

		servlet.setTransformWsdlLocations(true);

		return new ServletRegistrationBean<>(servlet, "/ws/*");
	}

	@Bean(name = "produto")
	public DefaultWsdl11Definition produtoWsdl(XsdSchema produtoSchema) {

		DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();

		wsdl.setPortTypeName("ProdutoPort");

		wsdl.setLocationUri("/ws");

		wsdl.setTargetNamespace("http://cps.sp.gov.br/produto");

		wsdl.setSchema(produtoSchema);

		return wsdl;
	}

	@Bean
	public XsdSchema produtoSchema() {
		return new SimpleXsdSchema(new ClassPathResource("produto.xsd"));
	}



	@Bean
	public SimplePasswordValidationCallbackHandler passwordValidationCallbackHandler() {

		SimplePasswordValidationCallbackHandler handler = new SimplePasswordValidationCallbackHandler();

		// Credenciais apenas para ambiente de desenvolvimento
		handler.setUsersMap(Map.of("admin", "123456"));

		return handler;
	}

	@Bean
	public Wss4jSecurityInterceptor securityInterceptor(
			SimplePasswordValidationCallbackHandler passwordValidationCallbackHandler) {

		Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();

		interceptor.setValidationActions("UsernameToken");
		interceptor.setValidationCallbackHandler(passwordValidationCallbackHandler);
		interceptor.setSecureResponse(false);

		return interceptor;
	}

	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		interceptors.add(securityInterceptor(passwordValidationCallbackHandler()));
	}
}