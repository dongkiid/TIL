package com.example.board.persistence;

import com.example.board.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Long> {


    //글번호를 이용해서 댓글을 삭제하는 메서드를 선언
    // -> BoardService 클래스에 메서드를 선언
    // -> BoardServiceImpl 클래스에 메서드를 구현
    @Modifying
    @Query("delete from Reply r where r.board.bno =:bno")
    void deleteByBno(@Param("bno") Long bno);

}

