package com.example.demo.controller;

import com.example.demo.dao.Student.StudentDao;
import com.example.demo.entity.CouponActivity;
import com.example.demo.entity.Student;
import com.example.demo.mobile.ResponseApi;
import com.example.demo.vo.StudentVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.corba.se.spi.ior.ObjectKey;
import io.swagger.annotations.ApiResponse;
import io.swagger.models.auth.In;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisScriptingCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;
import org.thymeleaf.util.ArrayUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.nio.charset.Charset;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private DefaultRedisScript rs;
    private RedisScript<Long> stockScript;

    @PostConstruct
    public void init() throws IOException {
        String script = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("lua/1122.lua"), "utf-8");
        rs=new DefaultRedisScript(script,Long.class);
        String stockLua=IOUtils.toString(getClass().getClassLoader().getResourceAsStream("lua/stock.lua"), "utf-8");
        stockScript=new DefaultRedisScript<>(stockLua,Long.class);
    }
    @RequestMapping("/testPrinciple")
    @ResponseBody
    public Object testPrinciple(Authentication authentication){
        System.out.println(22);
        StudentVo studentVo= (StudentVo)authentication.getPrincipal();
        return studentVo;
    }
    @RequestMapping("/test/testStock")
    @ResponseBody
    public Object testStock(Integer userId,Integer count){
        System.out.println(stockScript.getSha1());
        Long result = (Long) redisTemplate.execute(stockScript, Arrays.asList(userId+""), count);
        return result;
    }
    @RequestMapping("/test/testHash")
    @ResponseBody
    public Object testHash(CouponActivity couponActivity){
            couponActivity.setEndDate(couponActivity.getEndTime().getTime());
            couponActivity.setStartDate(couponActivity.getStartTime().getTime());
           hashOperations.getOperations().execute(new RedisCallback<Object>() {

               @Override
               public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                   try {
                       return redisConnection.hSet("couponActivityDuration".getBytes(),"shopcoupon:1111".getBytes(),objectMapper.writeValueAsBytes(couponActivity));
                   } catch (JsonProcessingException e) {
                       e.printStackTrace();
                   }
                   return null;
               }
           });

        return 1;
    }
    @RequestMapping("/test/testBitMap")
    @ResponseBody
    public Object testBitMap(Long userId){
        LocalDate now = LocalDate.now();
        String format = now.format(dtf);
        Boolean aBoolean = valueOperations.setBit("sign:" + format, userId, true);
        return  aBoolean;
    }

    @RequestMapping("/test/testGetBitMap")
    @ResponseBody
    public Object testGetBitMap(Long userId){
        LocalDate now = LocalDate.now().minusDays(1);
        String format = now.format(dtf);
        Boolean aBoolean = valueOperations.getBit("sign:" + format, userId);
        return  aBoolean;
    }
    public  BitSet fromByteArrayReverse(final byte[] bytes) {
        final BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[i / 8] & (1 << (7 - (i % 8)))) != 0) {
                bits.set(i);
            }
        }
        return bits;
    }
    private DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private ObjectMapper objectMapper;


    @RequestMapping("/test/testLua")
    @ResponseBody
    public Object testLua(int number){
        return redisTemplate.execute(rs,Collections.emptyList(),number);
    }
    public static void main(String[] args) {
        try(InputStream is=new FileInputStream(new File("C:\\Users\\T011689\\stock.lua"));
                InputStream is2=new FileInputStream(new File("C:\\Users\\T011689\\stock2.lua"))
        ) {
            String s = IOUtils.toString(is, "utf-8");
            String s2 = IOUtils.toString(is2, "utf-8");
            RedisScript<Long> rs=new DefaultRedisScript(s,Long.class);
            RedisScript<Long> rs2=new DefaultRedisScript(s2,Long.class);
            System.out.println(rs.getSha1());
            System.out.println(rs2.getSha1());
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    @Autowired
    private RedisTemplate redisTemplate;
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
    public ModelAndView center(HttpServletRequest request,Authentication authentication){
        String name = (String)WebUtils.getSessionAttribute(request, "name");
        return  new ModelAndView("center");
    }
    @RequestMapping("/center2")
    public ModelAndView center2(HttpServletRequest request,Authentication authentication){
        String name = (String)WebUtils.getSessionAttribute(request, "name");
        return  new ModelAndView("center2");
    }
    @RequestMapping("/api/haiwan")
    public ModelAndView haiwan(){
        return new ModelAndView("haiwan");
    }
    @RequestMapping(value = "/api/haiwanjson",method = RequestMethod.GET)
    @ResponseBody
    public Object haiwanjson(){
        return new ResponseApi(200,"i am sucess","success");
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
    @RequestMapping(value = "/api/admin/me",method = RequestMethod.GET)
    @ResponseBody
    public Principal apiAdmin(Principal principal) {
        return principal;
    }

    /**
     * 通常要生成随机数，我们要么创建一个java.util.Random或Math.random（）的实例 - 它在第一次调用时在内部创建java.util.Random的实例。但是，在并发应用程序中，上述使用会导致争用问题
     *Random是线程安全的，可供多个线程使用。但是如果多个线程使用相同的Random实例，则多个线程共享相同的种子。它会导致多个线程之间的争用，从而导致性能下降。
     *ThreadLocalRandom是上述问题的解决方案。 ThreadLocalRandom每个线程都有一个Random实例，可以防止争用。
     *因此，基本上，每个线程使用一个随机实例允许您停止同步所有线程必须使用的种子。
     * @param
     * @return
     */
    @RequestMapping("/smsValidateCode")
    @ResponseBody
    public Object apiAdmin() {
        String username="15214358494";
        if(!isPhone(username)){
            return  "用户名必须传一个手机号码";
        }
//        if(valueOperations.get("code:"+username)!=null){
//            return "60s以后再发送验证码";
//        }
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
    @RequestMapping("/testRedisList")
    @ResponseBody
    public Object testRedisList(){
        List<Integer> ids=Arrays.asList(222,333,333,444,555);
        Long goodsIds = listOperations.leftPushAll("goodsIds", ids);
        return goodsIds;
    }
    @RequestMapping("/testRedisListPop")
    @ResponseBody
    public Object testRedisListPop(){
        Integer count = listOperations.rightPop("goodsIds");
        return count;
    }
    @Resource(name="redisTemplate")
    private HashOperations<String,String,Object> hashOperations;
    @RequestMapping("/test/zsetQuery")
    @ResponseBody
    public Object zsetQuery(Integer userId){
        if(userId==null){
            return 0;
        }
        String key="favorit:"+userId;
        Set<Integer> set = zSetOperations.reverseRange(key, 0, -1);
        return  set;
    }
    private List<Student> students;

    @RequestMapping("/test/ZsetIncr")
    @ResponseBody
    public Object ZsetIncr(@RequestParam(value = "number",required = false,defaultValue = "1.00") double number){
        Double aDouble = zSetOperations.incrementScore("2018:12:25:001", 322222, number);


        return aDouble;
    }

    @Resource(name = "redisTemplate")
    private ZSetOperations<String,Integer> zSetOperations;
    @RequestMapping("/testRedisSet")
    @ResponseBody
    public Object testRedisSet(){
        Boolean member = setOperations.isMember("info_1106", 200907090025L);
        Long size = setOperations.size("info_1106");
        return  member+"_"+size;
    }
    @Resource(name="redisTemplate")
    private SetOperations<String,Long> setOperations;
    @Resource(name="redisTemplate")
    private ListOperations<String,Integer> listOperations;
    @Resource(name="redisTemplate")
    private ValueOperations<String,String> valueOperations;
    @RequestMapping(value = "/api/user/me",method = RequestMethod.GET)
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
