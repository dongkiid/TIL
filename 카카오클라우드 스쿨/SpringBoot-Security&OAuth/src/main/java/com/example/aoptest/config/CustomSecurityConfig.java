package com.example.aoptest.config;

import com.example.aoptest.security.CustomUserDetailService;
import com.example.aoptest.security.handler.Custom403Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableGlobalMethodSecurity(prePostEnabled = true)
    @Configuration
    @Log4j2
    @RequiredArgsConstructor
    public class CustomSecurityConfig {

        private final DataSource dataSource;
        private final CustomUserDetailService customUserDetailService;

        @Bean
        public PersistentTokenRepository persistentTockenRepository() {
            JdbcTokenRepositoryImpl repo =
                    new JdbcTokenRepositoryImpl();
            repo.setDataSource(dataSource);
            return repo;

        }

        @Bean
        public AccessDeniedHandler accessDeniedHandler(){
            return new Custom403Handler();
        }
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http)
                throws Exception{
            log.info("필터 환경 설정");
            //인증이나 인가에 문제가 발생하면 로그인 폼 출력
            http.formLogin().loginPage("/member/login");

            //OAuth2가 사용할 로그인 URL설정
            http.oauth2Login().loginPage("/member/login/");

            http.csrf().disable();

            http.rememberMe()
                    .key("123456")
                    .tokenRepository(persistentTockenRepository())
                    .userDetailsService(customUserDetailService)
                    .tokenValiditySeconds(60*60*24*30);

           http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        return http.build();
        }

    @Bean
    PasswordEncoder passwordEncoder(){

            return new BCryptPasswordEncoder();
    }

    //정정 파일 요청은 동작하지 않도록 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources()
                        .atCommonLocations());
    }
    }

