package com.sparta.sbug.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

// lombok
@RequiredArgsConstructor

// springframework
@Controller
public class ChatViewController {

    @RequestMapping(value = "/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/chats/users")
    public ModelAndView chatView(@RequestParam Long id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("chat");
        mv.addObject("data", id);

        return mv;
    }
}
