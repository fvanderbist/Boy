package com.javabunga.springbootexample.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String sayHello(@RequestParam(value = "name") String name) {
        return "Hello " + name + "!";
    }

    @RequestMapping("/uppercase")
    public String uppercase(@RequestParam(value = "name") String name) { return name.toUpperCase(); }

    @GetMapping("/random")
    public int uppercase(@RequestParam(value = "min") Integer min, @RequestParam(value = "max") Integer max) {
        final Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }
}
