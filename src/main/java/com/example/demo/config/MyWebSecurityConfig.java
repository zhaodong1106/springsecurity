package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by T011689 on 2018/10/24.
 */
@EnableWebSecurity
//@EnableResourceServer
@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login","/loginForm","/smsValidateCode").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/loginForm").failureForwardUrl("/500").successForwardUrl("/center").and()
                .logout().invalidateHttpSession(true).clearAuthentication(true).logoutSuccessUrl("/login").deleteCookies("JSESSIONID");
        http.csrf().disable();
//        http.requestMatchers()
//                .antMatchers("/login", "/oauth/authorize","/exit")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/exit").hasAnyRole("ADMIN","USER")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll();

    }
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
}
