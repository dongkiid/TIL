package com.kakao.board.board;

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
    //페이징 처리를 위한 속성
    private int page;
    private int size;

    //검색 관련 속성
    private String type;
    private String keyword;

    //page 와 size 값이 없을 때 사용할 기본값 설정을 위한 생성자
    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    //page 와 size를 가지고 Pageable 객체를 생성해주는 메서드
    public Pageable getPageable(Sort sort){
        return PageRequest.of(page -1, size, sort);
    }
}

