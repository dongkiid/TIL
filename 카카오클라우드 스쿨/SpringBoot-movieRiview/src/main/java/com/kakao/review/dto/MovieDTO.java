package com.kakao.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {
    private Long mno;
    private String title;

    //review의 grade 평균
    private double avg;

    //리뷰 개수
    private Long reviewCnt;

    //등록일 과 수정
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    //builder()라는 메서드를 이용해서 생성할 때 기본으로 사용
    @Builder.Default
    private List<MovieImageDTO> imageDTOList = new ArrayList<>();
}
