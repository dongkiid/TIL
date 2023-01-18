package com.kakao.review;

import com.kakao.review.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTests {
    @Autowired
    private MovieService movieService;

    @Test
    public void getMovie(){
        System.out.println(movieService.getMovie(118L));
    }
}
