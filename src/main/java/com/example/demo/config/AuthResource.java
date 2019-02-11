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

    /**
     * @EnableResourceServer这个注解会@configuration注入一个Bean:ResourceServerConfiguration
     * ResourceServerConfiguration这个Bean也是继承WebSecurityConfigurerAdapter这个抽象类,
     * ResourceServerConfiguration默认Order是3
     * 而WebSecurityConfigurerAdapter的默认Order是100
     * 数字越小的拦截器越在前面，所以@EnableResourceServer资源服务器会先拦截/api/**的所有请求，
     * 剩下的请求会走formlogin的WebSecurityConfigurerAdapter拦截器那里
     * @EnableResourceServer资源服务器默认((HttpSecurity)((HttpSecurity)http.authenticationProvider(new AnonymousAuthenticationProvider("default")).exceptionHandling().accessDeniedHandler(resources.getAccessDeniedHandler()).and()).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()).csrf().disable()
     * @EnableResourceServer资源服务器如果不配置HttpSecurity默认((AuthorizedUrl)http.authorizeRequests().anyRequest()).authenticated();
     *  @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/api/**").and()
//                http.
                .authorizeRequests()
                .antMatchers("/api/haiwan","/api/haiwanjson").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();
//        http.httpBasic()
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
