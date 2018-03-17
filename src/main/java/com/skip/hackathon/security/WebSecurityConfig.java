package com.skip.hackathon.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.csrf().disable().authorizeRequests().antMatchers("/").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/customers").permitAll().and()
				// filtra requisições de login
				.addFilterBefore(new JWTLoginFilter("/api/v1/customers/auth", authenticationManager()),
						UsernamePasswordAuthenticationFilter.class)

				// filtra outras requisições para verificar a presença do JWT no header
				.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception { // cria uma conta default
		auth.inMemoryAuthentication().withUser("admin").password("password").roles("ADMIN");
		/*
		 * auth.jdbcAuthentication().dataSource(dataSource) .usersByUsernameQuery(
		 * "select email as username,password, status as enable  from customer where email=?"
		 * )
		 * 
		 * .authoritiesByUsernameQuery(
		 * "select user.user_email as username, user.password as password, user.user_status as enable from user where user.user_login=?"
		 * );
		 */
	}

}
