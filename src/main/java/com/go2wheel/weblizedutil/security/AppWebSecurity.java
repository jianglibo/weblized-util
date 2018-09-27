package com.go2wheel.weblizedutil.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.go2wheel.weblizedutil.value.CommonRoleNames;

@EnableWebSecurity
public class AppWebSecurity {
	
	@Bean
	public BCryptPasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public AuthenticationProviderImpl springAuthenticationProvider() {
		return new AuthenticationProviderImpl();
	}

	
	@Configuration
	@Order(1)                                                        
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http
				.antMatcher("/api/**")
				.authorizeRequests()
					.anyRequest().hasRole("ADMIN")
					.and()
				.anonymous()
				.and()
				.httpBasic();
		}
	}
	
//	.logoutUrl("/my/logout")                                                 2
//	.logoutSuccessUrl("/my/index")                                           3
//	.logoutSuccessHandler(logoutSuccessHandler)                              4
//	.invalidateHttpSession(true)                                             5
//	.addLogoutHandler(logoutHandler)                                         6
//	.deleteCookies(cookieNamesToClear)                                       7
	

	@Configuration
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.csrf().disable()
				.formLogin()
				.and()
				.logout()
				.and()
				.anonymous().authorities(CommonRoleNames.ROLE_ANON)
				.and()
				.authorizeRequests()
					.antMatchers("/app/polling")
					.permitAll()
					.antMatchers("/curdir")
					.permitAll()
					.anyRequest()
					.permitAll();
//					.authenticated();
		}
	}
}
