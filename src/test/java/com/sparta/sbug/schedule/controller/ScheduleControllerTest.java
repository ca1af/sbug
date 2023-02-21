package com.sparta.sbug.schedule.controller;

import com.sparta.sbug.schedule.service.ScheduleService;
import com.sparta.sbug.schedule.dto.ScheduleRequestDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.user.entity.User;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;


import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.any;

import java.time.LocalDateTime;

import java.util.Map;
import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest {
/*
    @Autowired
    private MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new ScheduleController())
            .setCustomArgumentResolvers(new PrincipalDetailsArgumentResolver())
            .build();

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScheduleService scheduleService;


    //일정 등록
    @Test
    @DisplayName("@PostMapping('') schedule() Test: registerSchedule")
    public void registerSchedule() {
        //given
        willDoNothing().given(scheduleService).registerSchedule(any(),any());

        Map<String, String> input = new HashMap<>();
        input.put("content", "Project meeting");
        input.put("date", "2023-02-20 14:00:00");


        //when & then
        mvc.perform(post("")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isOk());

    }
    
    //일정 수정
    @Test
    @DisplayName("@PutMapping('/{id}') schedule() Test: updateSchedule")
    public void updateSchedule() {
        //given

        //when


    }

    //일정 삭제
    @Test
    @DisplayName("@DeleteMapping('/{id}') schedule() Test: deleteSchedule")
    public void deleteSchedule() {
        //given

        //when


    }

    //내 일정 조회
    @Test
    @DisplayName("@GetMapping('') mySchedules() Test: getMySchedules")
    public void getMySchedules() {
        //given

        //when


    }

    //일정 상세 조회
    @Test
    @DisplayName("@GetMapping('/{id}') schedule() Test: getSchedule")
    public void getSchedule() {
        //given

        //when


    }

    //기간내 일정 조회
    @Test
    @DisplayName("@GetMapping('/period') schedule() Test: getPeriodSchedule")
    public void getPeriodSchedules() {
        //given

        //when


    }
*/
}

