package com.example.demo.config;

import com.example.demo.dao.MenuDao;
import com.example.demo.entity.Role;
import com.example.demo.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by T011689 on 2018/12/24.
 */
@Component
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private MenuDao menuDao;
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        MenuVo menuVo = menuDao.selectMenuWithRole(requestUrl);
        if(menuVo==null){
            return  null;
        }
        String[] roles=new String[menuVo.getRoles().size()];
        for(int i=0;i<roles.length;i++){
            roles[i]=menuVo.getRoles().get(i).getName();
        }
        return SecurityConfig.createList(roles);

    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
