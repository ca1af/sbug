package com.sparta.sbug.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Controller
public class ChatViewController {

    /**
     * 임시: 로그인 뷰를 반환합니다.
     * [GET] /login
     *
     * @return String : login.index를 의미합니다.
     */
    @RequestMapping(value = "/login")
    public String loginView() {
        return "login";
    }

    /**
     * 채팅 상대 유저 ID와 함께 채팅방 뷰를 반환합니다.
     * [GET] /chats/users
     *
     * @param id 상대 유저 ID
     * @return ModelAndView : chat.index와 함께 상대 유저의 id를 반환합니다.
     */
    @GetMapping("/chats/users")
    public ModelAndView chatView(@RequestParam Long id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("chat");
        mv.addObject("data", id);

        return mv;
    }
}
