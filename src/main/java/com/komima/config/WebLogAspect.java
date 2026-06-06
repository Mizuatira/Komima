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

/**
 * Web请求日志切面
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Aspect
@Component
public class WebLogAspect {

    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    /**
     * 定义切入点，拦截controller包下的所有方法
     */
    @Pointcut("execution(* com.komima.controller..*(..))")
    public void controllerLog() {}

    /**
     * 方法执行前记录请求日志
     * @param joinPoint 连接点
     */
    @Before("controllerLog()")
    public void before(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("[请求] {}.{} | 参数: {}", className, methodName, args);
    }

    /**
     * 方法执行后记录响应日志
     * @param joinPoint 连接点
     * @param result 返回结果
     */
    @AfterReturning(pointcut = "controllerLog()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("[响应] {} | 返回: {}", methodName, result);
    }

    /**
     * 方法抛出异常时记录异常日志
     * @param joinPoint 连接点
     * @param ex 异常
     */
    @AfterThrowing(pointcut = "controllerLog()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        log.error("[异常] {} | 错误: {}", methodName, ex.getMessage());
    }
}
