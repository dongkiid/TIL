package com.example.board.service;


import com.example.board.dto.BoardDTO;
import com.example.board.domain.Board;
import com.example.board.domain.Member;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;

import static org.hibernate.boot.model.process.spi.MetadataBuildingProcess.build;

public interface BoardService {
    default Board dtoToEntity(BoardDTO dto){

        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return board;
    }
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) //int로 처리하도록
                .build();

        return boardDTO;
    }

    Long register(BoardDTO dto);

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    public BoardDTO get(Long bno);

    void removeWithReplies(Long bno);

    //수정을 위한 메서드를 선언 ->Impl에서 구현
    void modify(BoardDTO boardDTO);



}

