package com.example.aoptest.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Log4j2
@RequestMapping("/member")
public class SampleController {
    @GetMapping("/")
    public String index() {
        log.info("메인");
        return "/sample/index";
    }
        @GetMapping("/sample/all")
        public void main(){
            log.info("모두 허용");
        }

        //로그인 한 유저만 섭속 가능하도록 설정
        @PreAuthorize("hasRole('USER')")
        @GetMapping("/sample/member")
        public void member(){
            log.info("멤버만 허용");
        }

        //관리자만 접속 가능하도록 설정
        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/sample/admin")
        public void admin(){
            log.info("관리자만 허용");
        }

        @GetMapping("/login")
        public void login(String error, String logout) {
            if (error != null) {

            }
            if (logout != null) {

            }
        }
    }

