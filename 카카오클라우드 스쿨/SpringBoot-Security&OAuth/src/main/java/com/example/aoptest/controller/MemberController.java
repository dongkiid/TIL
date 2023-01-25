package com.example.aoptest.controller;

import com.example.aoptest.dto.ClubMemberJoinDTO;
import com.example.aoptest.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/login")
    //error는 로그인 실패했을 때의 파라미터
    //logout은 로그아웃 한 후 로그인으로 이동했을 때의 파라미터
    public void login(String error, String logout){
        if(logout != null){
            log.info("로그아웃");
        }
    }

    private final MemberService memberService;

    //회원 가입 페이지로 이동 - get
    @GetMapping("/join")
    public void join(){
        log.info("회원가입 페이지로 이동");

    }

    //회원가입 처리 - post
    @PostMapping("/join")
    public String join(ClubMemberJoinDTO memberJoinDTO, RedirectAttributes rattr){
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
            //성공
        }catch (Exception e){
            rattr.addFlashAttribute("error","mid");
                return "redirect:/member/join";
        }
            rattr.addFlashAttribute("result","seccess");
                return "redirect:/member/login";
    }
}

