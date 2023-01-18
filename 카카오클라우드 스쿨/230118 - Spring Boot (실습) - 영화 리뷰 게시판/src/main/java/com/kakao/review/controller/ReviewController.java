package com.kakao.review.controller;

import com.kakao.review.dto.ReviewDTO;
import com.kakao.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/reviews")
    @RequiredArgsConstructor
    @Log4j2
    public class ReviewController {

        private final ReviewService reviewService;

        @GetMapping("/{mno}/list")
        public ResponseEntity<List<ReviewDTO>> list(@PathVariable("mno") Long mno) {
            List<ReviewDTO> reviewDTOList = reviewService.getList(mno);
            return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
        }

        @PostMapping("/{mno}")
        public ResponseEntity<Long> addReview(@RequestBody ReviewDTO movieReviewDTO) {
            log.info("----------------------add MovieReview------------------------");
            log.info("reviewDTO: " + movieReviewDTO);
            Long reviewnum = reviewService.register(movieReviewDTO);
            return new ResponseEntity<>(reviewnum, HttpStatus.OK);
        }

        @PutMapping("/{mno}/{reviewnum}")
        public ResponseEntity<Long> modifyReview(@PathVariable Long reviewnum, @RequestBody ReviewDTO movieReviewDTO){
            log.info("------------------------modify MovieReview-----------------------" + reviewnum);
            log.info("reviewDTO: " + movieReviewDTO);
            reviewService.modify(movieReviewDTO);
            return new ResponseEntity<>(reviewnum, HttpStatus.OK);
        }

        @DeleteMapping("/{mno}/{reviewnum}")
        public ResponseEntity<Long> removeReview( @PathVariable Long reviewnum){
            log.info("-----------------modify removeReview-----------------------");
            log.info("reviewnum: " + reviewnum);
            reviewService.remove(reviewnum);
            return new ResponseEntity<>(reviewnum, HttpStatus.OK);
        }

    }

