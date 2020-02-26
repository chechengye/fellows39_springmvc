package com.weichuang.exception;
/**
 * 自定义异常处理器
 */

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        System.out.println("页面走丢了!");
        ModelAndView mav = new ModelAndView();
        mav.addObject("msg" ,"页面走丢了!" );
        mav.setViewName("error");
        return mav;
    }
}
