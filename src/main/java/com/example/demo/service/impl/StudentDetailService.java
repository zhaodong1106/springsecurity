package com.example.demo.service.impl;

import com.example.demo.dao.Student.StudentDao;
import com.example.demo.entity.Student;
import com.example.demo.vo.StudentVo;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

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
    public static  void main(String[] args){
        for(int i=0;i<10;i++) {
            int code = ThreadLocalRandom.current().nextInt(900000) + 100000;
            System.out.println("验证码是:" + code);
        }
    }
}
