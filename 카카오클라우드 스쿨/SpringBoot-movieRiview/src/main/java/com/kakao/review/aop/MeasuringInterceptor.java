package com.kakao.review.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
public class MeasuringInterceptor implements HandlerInterceptor {

    //컨트롤러에게 요청을 하기 전에 호출되는 메서드
    //유효성검사, 로그인 검증 등을 수행할 수 있음
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        log.warn("처리하기 전에 호출");
        return true; //false를 리턴할 경우, 컨트롤러로 가지 않는데 대신 갈 곳을 리다이렉트 해줘야함
    }

    //컨트롤러가 요청을 정상적으로 처리한 후 호출되는 메서드
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        //로그 기록
        log.warn("요청을 정상적으로 처리한 후 호출");
    }

    //컨트롤러가 요청을 처리한 후 무조건 호출되는 메서드
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception e) {
        //로그 기록
        log.warn("비정상적으로 처리되도 호출");

    }
}
