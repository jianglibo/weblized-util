package com.go2wheel.weblizedutil;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import com.go2wheel.weblizedutil.convert.mvc.Id2Model;

@Configuration
@EnableWebMvc
public class WeblizedUtilWebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private MyAppSettings myAppSettings;
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**");
			}
			
			@Override
			public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
                ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
                t.setCorePoolSize(10);
                t.setMaxPoolSize(100);
                t.setQueueCapacity(50);
                t.setAllowCoreThreadTimeOut(true);
                t.setKeepAliveSeconds(120);
                t.initialize();
                configurer.setTaskExecutor(t);
			}
		};
	}
	
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
        	.setUseSuffixPatternMatch(false);
    }
	
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);

    }

    @Bean
    public Filter hiddenHttpMethodFilter() {
        HiddenHttpMethodFilter filter = new HiddenHttpMethodFilter();
        return filter;
    }
    
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.CHINESE);
        return slr;
    }
    
    @Bean
    public Id2Model id2Model() {
    	return new Id2Model();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(id2Model());
    }
    

    /**
     * This classpath template resolver has higher priority, when finding a template this resolver get hit first, If not found the normal routine follows.
     * @return
     */
	@Bean
	public SpringResourceTemplateResolver cpTemplateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(this.applicationContext);
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		resolver.setCheckExistence(true);
		resolver.setTemplateMode("HTML");
		resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		resolver.setCacheable(false);
		resolver.setOrder(100);
		return resolver;
	}
    
//    @Bean
//    @Description("Thymeleaf Template Resolver")
//    public ServletContextTemplateResolver templateResolver(final ServletContext servletContext) {
//        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
//        templateResolver.setPrefix("/WEB-INF/views/");
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode("HTML5");
//        return templateResolver;
//    }
	
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.enableContentNegotiation(new MappingJackson2JsonView());
//        registry.freeMarker().cache(false);
    }

//    @Bean
//    public FreeMarkerConfigurer freeMarkerConfigurer() {
//        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
//        configurer.setTemplateLoaderPath("/freemarker");
//        return configurer;
//    }
	
//	@Bean(name="localeResolver")
//	public LocaleResolver localMissingEndeResolver() {
//		CookieLocaleResolver clr = new CookieLocaleResolver();
//		clr.setDefaultLocale(Locale.ENGLISH); //Locale.US result en_US.properties.
//		return clr;
//	}
//	
//	@Bean
//	public LocaleChangeInterceptor localeChangeInterceptor() {
//		return new LocaleChangeInterceptor();
//	}
//
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
        .addResourceLocations("file:static/");
        
        registry.addResourceHandler("/img/**")
        .addResourceLocations("classpath:/public/img/")
        .setCachePeriod(31556926);
        
        registry.addResourceHandler("/pure/**")
        .addResourceLocations("classpath:/public/pure/")
        .setCachePeriod(31536000);

        registry.addResourceHandler("/jquery/**")
        .addResourceLocations("classpath:/public/jquery/")
        .setCachePeriod(31536000);

        registry.addResourceHandler("/cache-forever/**")
        .addResourceLocations("classpath:/cache-forever/")
        .setCachePeriod(myAppSettings.getCache().getCombo());
	}
	
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(localeChangeInterceptor());
//	}
	
	
}
