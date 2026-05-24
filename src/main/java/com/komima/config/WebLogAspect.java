package com.komima.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WebLogAspect {

    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(* com.komima.controller..*(..))")
    public void controllerLog() {}

    @Before("controllerLog()")
    public void before(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("[请求] {}.{} | 参数: {}", className, methodName, args);
    }

    @AfterReturning(pointcut = "controllerLog()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("[响应] {} | 返回: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "controllerLog()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        log.error("[异常] {} | 错误: {}", methodName, ex.getMessage());
    }
}
