package com.example.aoptest.service;

import com.example.aoptest.dto.ClubMemberJoinDTO;
import com.example.aoptest.security.dto.ClubMemberSecurityDTO;

public interface MemberService {
    //회원이 존재하는 경우 발생 시킬 예외 클래스
    static class MidExistException extends  Exception{

    }
    void join(ClubMemberJoinDTO memberJoinDTO) throws MidExistException;

}
