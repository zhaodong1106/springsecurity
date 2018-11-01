package com.example.demo.controller;

import com.example.demo.entity.Student;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by T011689 on 2018/11/1.
 */
public class Test {
    public static void main(String[] args){
        /**
         * java对于普通变量参数都是值传递
         */
        /*int i=1111;
        System.out.println("i之前:"+i);
        fun(i);
        System.out.println("i之后:"+i);*/

        /**
         * java对于对象参数是引用传递
         */
        /*Student student=new Student();
        student.setName("11111");
        System.out.println("函数之前student:"+student.getName());
        fun(student);
        System.out.println("函数之后student:"+student.getName());*/

        if(isMobile("15214358494")){
            System.out.println("匹配");
        }else{
            System.out.println("不匹配");
        }
    }
    public static void fun(int i){
        i=2222;
        System.out.println("在函数中的i:"+i);
    }
    public static void fun(Student student){
        student.setName("22222");
        System.out.println("在函数中的student:"+student.getName());
    }
    /**
            * 判断是否是手机号
    *
            * @param mobile
    * @return
            */
    public static boolean isMobile(String mobile) {
        String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }
}
