package com.example.secstudent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class HelloController {

    @GetMapping("/helloWorld")
    public List<String> helloWorld() {

        return Collections.singletonList("Spring Security simple demo");
    }

}
