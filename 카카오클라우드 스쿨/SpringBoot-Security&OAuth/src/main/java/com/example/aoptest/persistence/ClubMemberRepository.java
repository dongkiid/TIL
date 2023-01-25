package com.example.aoptest.persistence;

import com.example.aoptest.model.ClubMember;
import org.hibernate.boot.model.source.spi.AttributePath;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {
    //mid를 매개변수로 받아서
    //social 값이 false인 데이터를 전부 찾아오는 메서드
    //SQL
    //select * from club_member m, club_member_rol_set s
    //where m.mid = s.mod and m.mind=? and m.social=false


    @EntityGraph(attributePaths ="roleSet")
    @Query("select m from ClubMember m " +
            "where m.mid = :mid and m.social = false")
    Optional<ClubMember> getWithRoles(String mid);


//소셜 로그인시 email 가지고 로그인 여부 판단하도록 하는 메서드 생성 및 확인
@EntityGraph(attributePaths = "roleSet",type = EntityGraph.EntityGraphType.LOAD)
@Query("select m from ClubMember m where m.email = :email")
Optional<ClubMember> findByEmail(@Param("email") String email);
}