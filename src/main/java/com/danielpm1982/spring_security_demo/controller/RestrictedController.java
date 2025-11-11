package com.danielpm1982.spring_security_demo.controller;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restricted")
public class RestrictedController {
    @GetMapping(value="",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> restrictedMethod() {
        return ResponseEntity.ok("{\"msg\":\"This is a restricted area ! If you're seeing this message, you've been " +
                "authenticated and authorized to access this resource !\"}");
    }
}
