package com.sparta.sbug.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class ExecutionTimer {

    // 조인포인트를 어노테이션으로 설정
    @Pointcut("@annotation(com.sparta.sbug.aop.ExeTimer)")
    private void timer(){};

    // 메서드 실행 전,후로 시간을 공유해야 하기 때문에 Around로 설정.
    // 또한 Around 는 강력한 만큼 관리가 필요하기 때문에 어노테이션 타입으로 AOP 사용하도록 조정.

    // 단점 -> 리소스 관리 차원에서 단점 존재한다.
    // AOP 단점 검색해보기 (코드 확장에 닫혀있다 -> 내부 코드 영향을 받는 AOP 코드의 경우(기능을 확장해주는 친구들))

    @Around("timer()")
    public Object AssumeExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object proceed = joinPoint.proceed();// 조인포인트의 메서드 실행
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.info("실행 메서드: {}, 실행시간 = {}ms", methodName, totalTimeMillis);
        return proceed;
    }
}