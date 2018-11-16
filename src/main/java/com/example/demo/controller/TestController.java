package com.example.demo.controller;

import com.example.demo.dao.Student.StudentDao;
import com.example.demo.entity.Student;
import com.example.demo.vo.StudentVo;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;
import org.thymeleaf.util.ArrayUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
@Controller
public class TestController {
    @RequestMapping("/testPrinciple")
    @ResponseBody
    public Object testPrinciple(Authentication authentication){
        StudentVo studentVo= (StudentVo)authentication.getPrincipal();
        return studentVo;
    }
    @RequestMapping("/testJessionId")
    @ResponseBody
    public Object testJessionId(){
        return "2222222";
    }
    @RequestMapping("/login")
    public ModelAndView index(){
        return  new ModelAndView("index").addObject("student",new Student());
    }
    @RequestMapping("/center")
    public ModelAndView center(HttpServletRequest request){
        String name = (String)WebUtils.getSessionAttribute(request, "name");
        return  new ModelAndView("center");
    }
    @RequestMapping("/admin/test")
    @ResponseBody
    public Object adminTest(Principal principal){
        return  "只有管理员才能访问："+principal.getName();
    }

//    @Autowired
//    FindByIndexNameSessionRepository<? extends Session> sessions;
    @RequestMapping("/users")
    @ResponseBody
    public Object user(Principal principal) {
//        sessions.findByIndexNameAndIndexValue()
        List<Object> allPrincipals = registry.getAllPrincipals();
        return allPrincipals;
    }
    @Autowired
    private SessionRegistry registry;
    @RequestMapping("/user/delete")
    @ResponseBody
    public Object userDelete(String name) {
//        sessions.findByIndexNameAndIndexValue()
        Collection<? extends Session> usersSessions = sessionRepository.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, name).values();
        usersSessions.forEach((temp) -> {
            String sessionId = temp.getId();
// sessionRegistry.removeSessionInformation(sessionId);
            SessionInformation info = sessionRegistry.getSessionInformation(sessionId);
            info.expireNow();
        });
         return "success";
    }
    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    @Autowired
    private SessionRegistry sessionRegistry;
    @RequestMapping("/api/admin/me")
    @ResponseBody
    public Principal apiAdmin(Principal principal) {
        return principal;
    }

    /**
     * 通常要生成随机数，我们要么创建一个java.util.Random或Math.random（）的实例 - 它在第一次调用时在内部创建java.util.Random的实例。但是，在并发应用程序中，上述使用会导致争用问题
     *Random是线程安全的，可供多个线程使用。但是如果多个线程使用相同的Random实例，则多个线程共享相同的种子。它会导致多个线程之间的争用，从而导致性能下降。
     *ThreadLocalRandom是上述问题的解决方案。 ThreadLocalRandom每个线程都有一个Random实例，可以防止争用。
     *因此，基本上，每个线程使用一个随机实例允许您停止同步所有线程必须使用的种子。
     * @param username
     * @return
     */
    @RequestMapping("/smsValidateCode")
    @ResponseBody
    public Object apiAdmin(String username) {
        if(!isPhone(username)){
            return  "用户名必须传一个手机号码";
        }
        if(valueOperations.get("code:"+username)!=null){
            return "60s以后再发送验证码";
        }
        int code=ThreadLocalRandom.current().nextInt(900000)+100000;
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

    @RequestMapping("/testMobilePattern")
    @ResponseBody
    public Object testMobilePattern(@Valid Student student, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            Map<String,String> map=new HashMap<>();
            for(FieldError fieldError:bindingResult.getFieldErrors()){
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
            }
            return map;
        }
        return "success";
    }
    @RequestMapping("/register")
    public ModelAndView register( @Valid Student student,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            student.setPassword(null);
           return new ModelAndView("index");
        }
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        if(studentDao.selectByName(student.getName())==null) {
            Integer count = studentDao.insert(student);
        }else {
            System.out.println("已经存在");
        }
        return new ModelAndView(new RedirectView("index"));
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StudentDao studentDao;

}
