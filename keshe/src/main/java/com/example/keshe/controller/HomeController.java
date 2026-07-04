package com.example.keshe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/") public String index() { return "Index"; }
    @GetMapping("/welcome") public String welcome() { return "Welcome"; }
}
