package com.example.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;
    private String title;
    private String content;
    private String writerEmail;//작성자의 이메일(id)
    private String writerName;//작성자의 이름
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int replyCount; //해당 게시물의 댓글 수

}
