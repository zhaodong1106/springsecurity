package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Created by T011689 on 2018/10/25.
 */
@Configuration
@EnableResourceServer
public class AuthResource extends ResourceServerConfigurerAdapter {
//    static final String RESOURCE_ID = "my_resource_id";
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.resourceId(RESOURCE_ID);
//    }

    //    @Override
//    public void configure(HttpSecurity http) throws Exception {
//           http.authorizeRequests().antMatchers("/user/me").permitAll().anyRequest().authenticated();
//        http.csrf().disable();
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/user/**").access("#oauth2.hasScope('read')");
//    }
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.antMatcher("/api/**")
////                .antMatcher("/oauth/authorize")
//                .authorizeRequests()
////                .antMatchers("/login", "/loginForm", "/oauth/authorize").permitAll()
//                .anyRequest().authenticated().and()
//                .httpBasic();
////        http.csrf().disable();
//
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/api/**").and().
//                http.
                authorizeRequests()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
