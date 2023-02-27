package com.sparta.sbug.thread.repository.query;

import lombok.Builder;
import lombok.Data;

@Data
public class ThreadSearchCond {
    private String content;
    private String email;
    @Builder
    public ThreadSearchCond(String content, String email){
        this.content =content;
        this.email = email;
    }
}
