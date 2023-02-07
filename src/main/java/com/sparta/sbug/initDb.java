package com.sparta.sbug;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class initDb {
    @PostConstruct
    public void init(){

    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class initService{
        private final EntityManager em;
    }
}
