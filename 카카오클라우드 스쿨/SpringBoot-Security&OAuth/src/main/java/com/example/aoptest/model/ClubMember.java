package com.example.aoptest.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
//Setter를 안만드는 이유는, 수정 가능성이 있기 때문이다.
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude="rolSet")
public class ClubMember extends  BaseEntity{

    @Id
    private String mid;
    private String mpw;
    private String email;
    private String name;
    private boolean del;
    private boolean social;

    //권한 - 여러개의 권한 소유
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ClubMemberRole> roleSet = new HashSet<>();

    public void changePassword(String mpw){
        this.mpw=mpw;
    }
    public void changeEmail(String email){
        this.email=email;
    }
    public void changeDel(boolean del){
        this.del= del;
    }

    //권한 추가
    public void addRole(ClubMemberRole memberRole){
        this.roleSet.add(memberRole);
    }
    //권한 전부 삭제
    public void clearRoles(){
        this.roleSet.clear();
    }
    public void changeSocial (boolean social) {
        this.social = social;
    }
}

