package com.kakao.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    //페이지 번호
    private int page;
    //페이지 당 데이터 개수
    private int size;
    //검색 항목
    private String type;
    //검색 내용
    private String keyword;

    public PageRequestDTO(){
        page = 1;
        size = 10;
    }

    //page 와 size를 가지고 Pageable 객체를 생성해주는 메서드
    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1, size, sort);
    }
}
