package com.example.demo.controller;

import com.example.demo.vo.StudentVo;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by T011689 on 2018/10/24.
 */
@Controller
public class TestController {
    @RequestMapping("/testPrinciple")
    @ResponseBody
    public Object testPrinciple(Authentication authentication){
        StudentVo studentVo= (StudentVo)authentication.getPrincipal();
        return studentVo;
    }
    @RequestMapping("/login")
    public ModelAndView index(){
        return  new ModelAndView("index");
    }
    @RequestMapping("/center")
    public ModelAndView center(HttpSession session){
        System.out.println("forward 经过了控制器");
        System.out.println("session 过期时间:"+session.getMaxInactiveInterval());
        return  new ModelAndView("center");
    }
    @RequestMapping("/admin/test")
    @ResponseBody
    public Object adminTest(Principal principal){
        return  "只有管理员才能访问："+principal.getName();
    }


    @RequestMapping("/user/me")
    @ResponseBody
    public Principal user(Principal principal) {
        return principal;
    }
    @RequestMapping("/api/admin/me")
    @ResponseBody
    public Principal apiAdmin(Principal principal) {
        return principal;
    }
    @RequestMapping("/smsValidateCode")
    @ResponseBody
    public Object apiAdmin(String username) {
        if(!isPhone(username)){
            return  "用户名必须传一个手机号码";
        }
        if(valueOperations.get("code:"+username)!=null){
            return "60s以后再发送验证码";
        }
        int code=new Random().nextInt(900000)+100000;
        System.out.println("验证码是:"+code);
        valueOperations.set("code:"+username,String.valueOf(code),60, TimeUnit.SECONDS);
        return "发送成功";
    }
    public  boolean isPhone(final String str) {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    @Resource(name="redisTemplate")
    private ValueOperations<String,String> valueOperations;
    @RequestMapping("/api/user/me")
    @ResponseBody
    public Principal apiUser(Principal principal) {
        return principal;
    }
    @RequestMapping("/exit")
    public void exit(HttpServletRequest request, HttpServletResponse response) {
        // token can be revoked here if needed
        new SecurityContextLogoutHandler().logout(request, null, null);
        try {
            //sending back to client app
            response.sendRedirect(request.getHeader("referer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
