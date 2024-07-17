package code.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
	@Bean
	public RequestCachingFilter requestCachingFilter() {
		return new RequestCachingFilter();
	}

	@Bean
	public FilterRegistrationBean<RequestCachingFilter> requestCachingFilterRegistration(RequestCachingFilter requestCachingFilter) {
		FilterRegistrationBean<RequestCachingFilter> bean = new FilterRegistrationBean<>(requestCachingFilter);
		bean.setOrder(1);
		return bean;
	}
}
