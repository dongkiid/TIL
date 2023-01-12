package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.domain.Member;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.persistence.BoardRepository;
import com.example.board.persistence.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{
    private final BoardRepository repository;

    @Override
    public Long register(BoardDTO dto) {
        log.info(dto);
        Board board  = dtoToEntity(dto);
        repository.save(board);
        return board.getBno();
    }
    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0],(Member)en[1],(Long)en[2]));

        Page<Object[]> result = repository.getBoardWithReplyCount(
                pageRequestDTO.getPageable(Sort.by("bno").descending())  );

        return new PageResultDTO<>(result, fn);
    }
    @Override
    public BoardDTO get(Long bno) {
        Object result = repository.getBoardByBno(bno);
        Object[] arr = (Object[])result;
        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }


    private final ReplyRepository replyRepository;

    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        //댓글 부터 삭제
        replyRepository.deleteByBno(bno);
        repository.deleteById(bno);
    }


    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> board = repository.findById(boardDTO.getBno());

        if(board.isPresent()) {
            board.get().changeTitle(boardDTO.getTitle());
            board.get().changeContent(boardDTO.getContent());

            repository.save(board.get());
        }
    }

}