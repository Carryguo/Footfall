package com.carry.wifi_monitor.controller;


import com.carry.wifi_monitor.WebSecurityConfig;
import com.carry.wifi_monitor.entity.User;
import com.carry.wifi_monitor.mapper.UserDao;
import com.carry.wifi_monitor.service.impl.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * Created by huangds on 2017/10/24.
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserDao userDao;

    @GetMapping("/index")
    public String index(@SessionAttribute(WebSecurityConfig.SESSION_KEY)String account, Model model){
        return "index";
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping("/set")
    public String set(@SessionAttribute(WebSecurityConfig.SESSION_KEY)String account, Model model){
        return "set";
    }

    @GetMapping("/detail")
    public String detail(){
        return "detail";
    }

    @GetMapping("/machineList")
    public String machineList(){
        return "machineList";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/map")
    public String map(){
        return "map";
    }

    @PostMapping("/login")
    public String loginVerify(String username,String password,HttpSession session){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        boolean verify = loginService.verifyLogin(user);
        if (verify) {
            session.setAttribute(WebSecurityConfig.SESSION_KEY, username);
            return "index";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute(WebSecurityConfig.SESSION_KEY);
        return "redirect:/login";
    }

    //注册页面
    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    //注册方法
    @RequestMapping("/addregister")
    public String register(HttpServletRequest request){
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String password2 = request.getParameter("password2");

            if (password.equals(password2)){
                User userEntity = new User();
                userEntity.setUsername(username);
                userEntity.setPassword(password);
                loginService.save(userEntity);
                return "login";
            }else {
                return "register";
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("注册失败");
        }
        return "register";
    }
}
