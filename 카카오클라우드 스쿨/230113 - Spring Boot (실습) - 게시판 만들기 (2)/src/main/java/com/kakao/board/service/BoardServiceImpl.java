package com.kakao.board.service;

import com.kakao.board.board.BoardDTO;
import com.kakao.board.board.PageRequestDTO;
import com.kakao.board.board.PageResponseDTO;
import com.kakao.board.domain.Board;
import com.kakao.board.domain.Member;
import com.kakao.board.persistence.BoardRepository;
import com.kakao.board.persistence.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;


@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;

    public Long register(BoardDTO dto){
        log.info("Service:" + dto);
        Board board = dtoToEntity(dto);
        boardRepository.save(board);
        return board.getBno();
    }


    public PageResponseDTO<BoardDTO, Object[]> getList(
            PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        //Entity를 DTO로 변경하는 람다 인스턴스 생성
        Function<Object[], BoardDTO> fn = (
                en -> entityToDTO(
                        (Board) en[0], (Member) en[1], (Long) en[2]));
        //목록 보기 요청 처리 - 검색 적용 안됨
        /*
        Page<Object []> result =
                boardRepository.getBoardWithReplyCount(
                        pageRequestDTO.getPageable(
                                Sort.by("bno").descending()));

         */

        //검색이 적용된 메서드 호출
        Page<Object[]> result =
                boardRepository.searchPage(
                        pageRequestDTO.getType(),
                        pageRequestDTO.getKeyword(),
                        pageRequestDTO.getPageable(
                                Sort.by("bno").descending())
                );
        return new PageResponseDTO<>(result, fn);
    }

    public BoardDTO get(Long bno){
        Object result = boardRepository.getBoardByBno(bno);
        Object [] arr = (Object []) result;
        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    private final ReplyRepository replyRepository;
    //2가지가 연쇄적으로 삭제 되기 때문에 트랜잭션을 적용해야 한다.
    @Transactional
    public void removeWithReplies(Long bno){
        replyRepository.deleteByBno(bno); //댓글 삭제
        boardRepository.deleteById(bno); //게시글 삭제
    }

    @Transactional
    public Long modify(BoardDTO dto){
        //JPA에서는 삽입과 수정 메서드가 동일
        //upsert(있으면 수정 없으면 삽입) 를 하고자 하는 경우는 save를 호출하면 되지만
        //update를 하고자 하면 데이터가 있는지 확인한 후 수행하는 것이 좋습니다.
        if(dto.getBno() == null){
            return 0L;
        }

        //데이터 존재 여부를 확인
        Optional<Board> board = boardRepository.findById(dto.getBno());
        //존재하는 경우
        if(board.isPresent()){
            board.get().changeTitle(dto.getTitle());
            board.get().changeContent(dto.getContent());
            boardRepository.save(board.get());
            return board.get().getBno();
        }else{
            return 0L;
        }
    }
}
