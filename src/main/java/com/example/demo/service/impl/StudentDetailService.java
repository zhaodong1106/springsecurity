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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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

    /**
     * 实验list删除其中一个对象
     * ①list.remove并不能用户筛选删除其中匹配的数据，因为remove方法删除当前索引数据的同时，后面的列会向前移动一位，导致接下来的列会漏掉
     * ②使用iterater来筛选删除匹配的数据就不会有这个问题
     * @param args
     */
    public static  void main(String[] args){
        List<String> strings= Arrays.asList(new String[]{"dad","dadadas","1120","1120","1532","1120"});
        List<String> strings2=new ArrayList<>();
        for(String str:strings){
            strings2.add(str);
        }
        strings2.stream().forEach(System.out::println);
//        for (int i=0;i<strings2.size();i++) {
//            if("1120".equals(strings2.get(i))){
//               strings2.remove(i);
//            }
//        }
        Iterator<String> iterator=strings2.iterator();
        while(iterator.hasNext()){
            String next = iterator.next();
            if("1120".equals(next)){
                iterator.remove();
            }
        }
        System.out.println(strings2.size());
        strings2.stream().forEach(System.out::println);

    }
}
