package com.sparta.sbug.schedule.controller;

import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.user.entity.UserRole;
import com.sparta.sbug.user.entity.User;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

/*
public class PrincipalDetailsArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserDetailsImpl.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        User user = User.builder()
            .email("some@somemail.com")
            .password("1234qwer")
            .nickname("bob")
            .build();
        ReflectionTestUtils.setField(user, "id", 4878L);

        UserDetailsImpl userDetails = new UserDetailsImpl(user, user.getEmail());

        return userDetails;
    }



}
*/
