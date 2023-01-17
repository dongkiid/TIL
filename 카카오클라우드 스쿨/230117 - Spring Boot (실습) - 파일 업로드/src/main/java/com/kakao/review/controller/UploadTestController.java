package com.kakao.review.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UploadTestController {
    @GetMapping("/uploadajax")
    public void uploadAjax(){}
}
