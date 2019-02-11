package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.Filter;
import java.util.concurrent.TimeUnit;

/**
 * Created by T011689 on 2018/10/30.
 */
//@EnableWebMvc
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/upload/**").addResourceLocations("file:C:\\images\\");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/").setCacheControl(CacheControl.maxAge(60, TimeUnit.MINUTES));
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/").setCacheControl(CacheControl.maxAge(60,TimeUnit.MINUTES));
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/").setCacheControl(CacheControl.maxAge(60,TimeUnit.MINUTES));
    }
//    @Bean
//    public Filter shallowEtagHeaderFilter() {
//        return new ShallowEtagHeaderFilter();
//    }
}
