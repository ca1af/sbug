package com.sparta.sbug.schedule.controller;

import com.sparta.sbug.schedule.service.ScheduleService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.any;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ScheduleService scheduleService;


    //일정 등록
    @Test
    @DisplayName("@PostMapping('') schedule() Test: registerSchedule")
    public void registerSchedule() {
        //given
        LinkedMultiValueMap<String, String> requestParams =
            new LinkedMultiValueMap<>();


        willDoNothing().given(scheduleService).registerSchedule(any(),any());


        //when & then
        mvc.perform(post("").param())

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
}

