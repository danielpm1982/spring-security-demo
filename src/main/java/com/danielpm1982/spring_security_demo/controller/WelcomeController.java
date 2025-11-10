package com.danielpm1982.spring_security_demo.controller;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class WelcomeController {
    @GetMapping(value={"/","/hello","/welcome","/index"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("{\"msg\":\"Hello World !\"}");
    }
}
