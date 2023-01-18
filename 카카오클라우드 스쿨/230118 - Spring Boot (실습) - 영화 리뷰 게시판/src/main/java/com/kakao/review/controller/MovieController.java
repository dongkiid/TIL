package com.kakao.review.controller;

import com.kakao.review.dto.MovieDTO;
import com.kakao.review.dto.PageRequestDTO;
import com.kakao.review.dto.PageResponseDTO;
import com.kakao.review.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String register(MovieDTO movieDTO,
                           RedirectAttributes redirectAttributes){
        log.info("movieDTO:" + movieDTO);
        Long mno = movieService.register(movieDTO);
        redirectAttributes.addFlashAttribute("msg",
                mno + "삽입 성공");
        return "redirect:/movie/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO,
                     Model model){
        PageResponseDTO pageResponseDTO =
                movieService.getList(pageRequestDTO);
        model.addAttribute("result",
                pageResponseDTO);
    }

    @GetMapping("/read")
    public void read(Long mno,
                     @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                     Model model){
        MovieDTO movieDTO = movieService.getMovie(mno);
        model.addAttribute("dto", movieDTO);
    }


    
}