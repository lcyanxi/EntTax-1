package com.douguo.dc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PermissionsController {

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("auth");
        request.getSession().setMaxInactiveInterval(1);
        return "redirect:/test_overview.do";
    }
    
    @RequestMapping("/index")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/overview.do";
    }
}
