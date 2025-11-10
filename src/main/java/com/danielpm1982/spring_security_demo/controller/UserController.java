package com.danielpm1982.spring_security_demo.controller;
import com.danielpm1982.spring_security_demo.dto.UserDTOInput;
import com.danielpm1982.spring_security_demo.dto.UserDTOOutput;
import com.danielpm1982.spring_security_demo.exception.MyInternalServerErrorException;
import com.danielpm1982.spring_security_demo.exception.MyNotFoundErrorException;
import com.danielpm1982.spring_security_demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTOOutput> addUser(@RequestBody UserDTOInput userDTOInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDTOInput).orElseThrow(
                ()->new MyInternalServerErrorException("User not added ! Adding User failed due to a Server error ! " +
                        "Try again later !")));
    }
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTOOutput>> getAllUsers() {
        List<UserDTOOutput> userDTOOutputList = userService.findAll();
        if(userDTOOutputList.isEmpty())
            throw new MyNotFoundErrorException("No User found !");
        return ResponseEntity.ok(userDTOOutputList);
    }
    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTOOutput> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username).orElseThrow(()->new MyNotFoundErrorException("User " +
                "not found for username: "+username+" !")));
    }
}

// we only manage userDTOs here, for inputs and outputs, not the User instances themselves. Every instance we send or receive
// from the UserService, is a DTO, not the User instance itself. UserDTOInput instances don't have a Long id attribute (it must
// be auto-generated at the DBMS), while UserDTOOutput instances don't have the String password attribute (the password never
// should be returned to the user, and should be kept encrypted at the DB). Also, as the DTO is not a reference to the JPA
// entity object (User), there's no way to persist any change at the DB by using the DTOs returned from the UserService. User
// entities are only accessible from inside the UserRepository and UserService classes, and there are no references to them from
// outside. All User instances are encapsulated inside the Service and Persistence layers. There's no direct manipulation of
// entities from the Controller or from any View we created using the Service.
