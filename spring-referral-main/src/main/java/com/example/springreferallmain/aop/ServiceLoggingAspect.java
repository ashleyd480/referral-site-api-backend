package com.example.springreferallmain.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLoggingAspect {


    @Pointcut("execution(* com.example.springreferallmain.service..*.*(..))")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object logAroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Before method: " + proceedingJoinPoint.getSignature().toShortString() + "; Trigger time: " + System.currentTimeMillis());

        Object result = proceedingJoinPoint.proceed();

        log.info("After method: " + proceedingJoinPoint.getSignature().toShortString() + "; Trigger time: " + System.currentTimeMillis());

        return result;
    }

}
