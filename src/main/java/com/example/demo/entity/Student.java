package com.example.demo.entity;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by T011689 on 2018/10/24.
 */
public class Student {
    private int id;
    @NotBlank
    @Length(min = 5,max = 10)
    private String name;
    @Pattern(regexp="(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{5,10}",message="密码必须是5~10位数字和字母的组合")
    private String password;
    private String email;
/*    @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$",message = "请填写正确的手机号码🙆‍")
    private String mobile;*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    /*public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }*/
    public static  void main(String[] args){
        int max = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        System.out.println("max:"+max);
    }
}
