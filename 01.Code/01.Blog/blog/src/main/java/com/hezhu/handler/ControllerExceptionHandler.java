package com.hezhu.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 拦截异常
 */

//拦截所有有@controller的
@ControllerAdvice
public class ControllerExceptionHandler {

    //日志记录
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 统一拦截exception，并做记录
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class) //标识做异常处理
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("Request URL: {}, Exception : {}", request.getRequestURL(), e);

        //如果指定状态（不为空），抛出异常。交给springboot处理
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("url", request.getRequestURL());
        mv.addObject("exception", e);
        mv.setViewName("error/error");

        return mv;
    }
}
