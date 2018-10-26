package com.example.demo.service.impl;

import com.example.demo.dao.Student.StudentDao;
import com.example.demo.vo.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by T011689 on 2018/10/24.
 */
@Service
public class StudentDetailService implements UserDetailsService {
    @Autowired
    private StudentDao studentDao;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        StudentVo studentVo = studentDao.selectByName(name);
//        studentVo.setGrantedAuthorityList(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        return studentVo;
    }
}
