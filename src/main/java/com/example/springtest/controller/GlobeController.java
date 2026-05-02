package com.example.springtest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobeController {

    @GetMapping("/globe")
    public String globe() {
        return "globe";
    }
}
