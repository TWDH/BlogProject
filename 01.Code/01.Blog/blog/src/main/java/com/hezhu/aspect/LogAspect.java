package com.hezhu.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect //切面注解
@Component //组件扫描
public class LogAspect {

    //拦截范围
    @Pointcut("execution(* com.hezhu.controller.*.*(..))") // 所有范围（public） 类包.所有类.所有方法
    public void log() {

    }

    @Before("log()") //在切面log前执行
    public void deBefore() {
        log.info("----------- do Before ----------");
    }

    @After("log()") //在切面log后执行
    public void deAfter() {
        log.info("----------- do After ----------");
    }

    //获取返回内容
    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturn(Object result) {
        log.info("Result : {}", result);
    }
}
