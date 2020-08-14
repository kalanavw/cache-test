package com.example.cachetest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kalana.w on 8/13/2020.
 */
@RestController
public class IndexController {
    @GetMapping
    public String index(){
        return "Welcome";
    }
}
