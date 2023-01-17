package com.kakao.review.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"movie", "member"})

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewnum;

    @ManyToOne(fetch= FetchType.LAZY)
    private Movie movie;
    @ManyToOne(fetch= FetchType.LAZY)
    private Member member;

    private int grade;
    private String text;
}
