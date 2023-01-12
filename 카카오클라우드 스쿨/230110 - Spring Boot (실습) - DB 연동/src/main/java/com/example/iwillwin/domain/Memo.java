package com.example.iwillwin.domain;
import lombok.ToString;

import javax.persistence.*;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name= "tbl_memo")
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Memo {
    @Id
    //auto_increament 사용
    //@GeneratedValue(strategy = GenerationType.IDENTITY)

    //Sequence 생성
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGeneratorName")
    //@SequenceGenerator(sequenceName = "SequenceName", name = "SequenceGeneratorName", allocationSize = 1)

    //생성 방법을 Hibernate 가 결정
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;
}
