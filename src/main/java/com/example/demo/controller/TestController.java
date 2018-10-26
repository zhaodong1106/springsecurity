package com.example.demo.controller;

import com.example.demo.vo.StudentVo;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

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
