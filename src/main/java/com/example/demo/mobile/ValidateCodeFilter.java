package com.example.demo.mobile;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by T011689 on 2018/10/29.
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter{
    @Autowired
    private ObjectMapper objectMapper;
    @Resource(name = "redisTemplate")
    private ValueOperations<String,String> valueOperations;
    private String url="/oauth/token";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String uri=request.getRequestURI();
        if("/server/oauth/token".equals(uri)){
            String code = request.getParameter("code");
            String username=request.getParameter("username");
            if(username!=null&&!"".equals(username.trim())) {
                String useCodeInRedis = valueOperations.get("code:" +username);
                if(useCodeInRedis==null){
                    String mes = objectMapper.writeValueAsString(new ResponseApi(402, null, "code已过期"));
                    responseOutWithJson(response, mes);
                    return;
                }
                if(useCodeInRedis.equals(code)){
                    chain.doFilter(request, response);
                }else {
                    String mes = objectMapper.writeValueAsString(new ResponseApi(400, null, "验证码错误"));
                    responseOutWithJson(response, mes);
                    return;
                }
            }else {
                String mes = objectMapper.writeValueAsString(new ResponseApi(401, null, "用户名不存在"));
                responseOutWithJson(response, mes);
                return;
            }
        }else {
            chain.doFilter(request, response);
        }
    }
    private void responseOutWithJson(HttpServletResponse response,
                                       String responseObject) {
        //将实体对象转换为JSON Object转换
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(responseObject);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
