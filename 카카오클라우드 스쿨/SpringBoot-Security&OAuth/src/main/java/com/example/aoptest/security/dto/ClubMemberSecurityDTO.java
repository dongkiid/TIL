package com.example.aoptest.security.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class ClubMemberSecurityDTO extends User implements OAuth2User {
    private String mid;
    private String mpw;
    private String email;
    private String name;
    private boolean del;
    private boolean social;


    public ClubMemberSecurityDTO(String username,
                                 String password,
                                 String email,
                                 String name,
                                 boolean del,
                                 boolean social,
                                 Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.mid = username;
        this.mpw = password;
        this.email = email;
        this.name = name;
        this.del = del;
        this.social = social;
    }

    //소셜 로그인을 위한 코드
    private Map<String, Object> props;

    @Override
    public Map<String,Object> getAttributes(){
        return this.getProps();
    }
    @Override
    public String getName(){
        return this.mid;
    }
}
