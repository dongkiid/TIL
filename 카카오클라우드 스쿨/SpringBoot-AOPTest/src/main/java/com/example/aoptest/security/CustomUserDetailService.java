package com.example.aoptest.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CustomUserDetailService implements UserDetailsService {
    private PasswordEncoder passwordEncoder;

    public CustomUserDetailService (){
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    //아이디를 입력하고 로그인 요청을 하게되면 아이디에 해당하는 데이터를 찾아오는 메서드
    //로그인 처리를 해줘야함
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        log.info("loadUserByUsername:" +username);

        //로그인에 성공한 경우 생성
        //실제로는 데이터베이스에서 읽어서 설정
        UserDetails userDetails = User.builder()
                .username("user1")
                .password
        (passwordEncoder.encode("1111"))
                .authorities("ROLE_USER")
                .build();

        return userDetails;
    }
}
