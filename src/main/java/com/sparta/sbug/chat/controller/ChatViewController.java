package com.sparta.sbug.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// lombok
@RequiredArgsConstructor

// springframework
@Controller
public class ChatViewController {

    @RequestMapping(value = "/login")
    public String loginView() {
        return "login";
    }

    @RequestMapping(value = "/chats")
    public String chatView() {
        return "chat";
    }
}
