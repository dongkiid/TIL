package com.example.board.controller;

import com.example.board.dto.PageRequestDTO;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    @GetMapping({"/", "/board/list"})
    public String list(PageRequestDTO pageRequestDTO, Model domain){
        log.info("list............." + pageRequestDTO);
        domain.addAttribute("result", boardService.getList(pageRequestDTO));
        return "board/list";
    }

}
