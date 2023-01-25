package com.example.aoptest;

import com.example.aoptest.model.ClubMember;
import com.example.aoptest.model.ClubMemberRole;
import com.example.aoptest.persistence.ClubMemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.IntStream;
@SpringBootTest
public class RepositoryTest {

@Autowired
private ClubMemberRepository clubMemberRepository;

@Autowired
private PasswordEncoder passwordEncoder;


    //샘플 데이터 삽입
    @Test
    public void insertMembers(){
        IntStream.rangeClosed(1,100).forEach(i->{
            ClubMember clubMember = ClubMember.builder()
                    .mid("member" + i)
                    .mpw(passwordEncoder.encode("1111"))
                    .email("user"+i+"@gamil.com")
                    .name("사용자"+i)
                    .social(false)
                    .roleSet(new HashSet<ClubMemberRole>())
                    .build();
            clubMember.addRole(ClubMemberRole.USER);
            if(i>90){
                clubMember.addRole(ClubMemberRole.ADMIN);
            }
            clubMemberRepository.save(clubMember);
        });
    }

    //mid를 이용해서 조회하는 메서드
    @Test
    public void testRead(){
        Optional<ClubMember> result =
        clubMemberRepository.getWithRoles("member70");
        if(result.isPresent()){
            System.out.println(result);
            System.out.println(result.get().getRoleSet());
        }else{
            System.out.println("존재하지 않는 아이디");
        }
    }

    @Test
    public void testReadEmail(){
        Optional<ClubMember> clubMember =
                clubMemberRepository.findByEmail("user95@gamil.com");
        System.out.println(clubMember.get().getRoleSet());
    }


}
