package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.sql.DataSource;

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
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * http.authorizeRequests()匹配所有请求，在@EnableResourceServer拦截器之后
     * 如果不继承WebSecurityConfigurerAdapter，
     * 那么@EnableWebSecurity会默认new WebSecurityConfigurerAdapter()生成一个对象
     * 默认参数是((HttpSecurity)((HttpSecurity)((AuthorizedUrl)http.authorizeRequests().anyRequest()).authenticated().and()).formLogin().and()).httpBasic();
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login","/loginForm","/smsValidateCode","/register","/testJessionId").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/loginForm").failureForwardUrl("/500").successForwardUrl("/center").and()
                .logout().invalidateHttpSession(true).clearAuthentication(true).logoutSuccessUrl("/login").deleteCookies("JSESSIONID");
//                .and()
//                .rememberMe().rememberMeParameter("remember-me").tokenRepository(tokenRepository()).userDetailsService(userDetailsService);
//        http.csrf().disable();
        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false).sessionRegistry(sessionRegistry()).expiredUrl("/login?expired");
// http.requestMatchers()
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
    @Autowired
    private DataSource dataSource;
    @Bean
    public PersistentTokenRepository  tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl=new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        return jdbcTokenRepositoryImpl;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/js/**");
//        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/upload/**");
    }
    /**
        每次登陆前使用redis存放在redis里面的session信息,接着Iterator挨个遍历比较
     */
     @Bean
    SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }/**

        内存中初始化一个ConcurrentMap存放principals,接着挨个比较
    /*@Bean
    SessionRegistry sessionRegistry(){
        return  new SessionRegistryImpl();
    }*/

    /**
     * 注意：一定要写成FindByIndexNameSessionRepository<? extends Session> 其中？不能少,不然注入就是null值
     */
    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;


}
