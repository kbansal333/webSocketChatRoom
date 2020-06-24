package com.example.chatRoom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController {
    @ResponseBody
    @RequestMapping(value = "/")
    public ModelAndView login(){
        ModelAndView model = new ModelAndView("login");
        return model;
    }

    /*@ResponseBody
    @RequestMapping(value = "/index")
    public ModelAndView chatRoom(HttpServletRequest request){
        ModelAndView model = new ModelAndView("chat");
        return model;
    }*/

    @RequestMapping(value = "/index",method = RequestMethod.POST)
    public void chatRoomPost(HttpServletRequest request, HttpServletResponse response, @RequestParam String username) throws IOException {
        request.getSession().setAttribute("username",username);
        //response.sendRedirect("chatroom");
    }

    @ResponseBody
    @RequestMapping(value = "/chatroom")
    public ModelAndView chatRoom(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("chat");
        String username = (String)request.getSession().getAttribute("username");
        modelAndView.addObject("username",username);
        return modelAndView;
    }
}
