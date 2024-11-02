package com.mcgill.ecse321.GameShop.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class CreateAccountController {

    @GetMapping("/hello")
    public String printHello() {
        return "Hello";
    }

    @GetMapping("/hello/{myName}")
    public String printHelloArgs(@PathVariable String myName, @RequestParam String yourName) {
        return String.format("hello there! My name is %s.", yourName, myName);
    }

    // @GetMapping("/hello/{myName}")
    // public String printHelloArgs(@PathVariable String myName) {
    //     return String.format("hello there! My name is %s.", myName);
    // }
    
    
    
}
