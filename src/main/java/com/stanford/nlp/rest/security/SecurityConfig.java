package com.stanford.nlp.rest.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/rest/api/**").anonymous()
                .antMatchers(HttpMethod.PUT, "/rest/api/**").anonymous()
                .antMatchers(HttpMethod.POST, "/rest/api/**").anonymous()
                .antMatchers(HttpMethod.DELETE, "/rest/api/**").anonymous();
                /*.anyRequest().permitAll()
                .and()
                .formLogin().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);*/
    }
}
