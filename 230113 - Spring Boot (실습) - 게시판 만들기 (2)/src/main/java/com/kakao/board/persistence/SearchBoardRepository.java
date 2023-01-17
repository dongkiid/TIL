package com.kakao.board.persistence;

import com.kakao.board.domain.Board;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBoardRepository {

    Board search1();


    //Pageable을 전송하고 Page<Object[]>로 리턴받는 메서드를 선언
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);

}
