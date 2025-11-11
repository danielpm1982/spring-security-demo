package com.danielpm1982.spring_security_demo.controller;
import com.danielpm1982.spring_security_demo.dto.LoginDTOInput;
import com.danielpm1982.spring_security_demo.dto.UserDTOInput;
import com.danielpm1982.spring_security_demo.dto.UserDTOOutput;
import com.danielpm1982.spring_security_demo.exception.MyInternalServerErrorException;
import com.danielpm1982.spring_security_demo.exception.MyNotFoundErrorException;
import com.danielpm1982.spring_security_demo.exception.MyUnauthorizedErrorException;
import com.danielpm1982.spring_security_demo.security.MyToken;
import com.danielpm1982.spring_security_demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        userDTOInput.setPassword(new BCryptPasswordEncoder().encode(userDTOInput.getPassword()));
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
    @PostMapping(value = "/login")
    public ResponseEntity<MyToken> login(@RequestBody LoginDTOInput loginDTOInput) {
        return ResponseEntity.ok(userService.userLogin(loginDTOInput).orElseThrow(()->new MyUnauthorizedErrorException("Authentication " +
                "failed ! No User found with email: "+loginDTOInput.getEmail()+", or the used login password is wrong when compared to the " +
                "User stored password, registered before ! Review your login data and try logging in again !")));
    }
}

// We only manage user and login DTOs here, for inputs and outputs, not the User or other entity instances themselves. Every
// instance we send or receive to/from the UserService, is a DTO, not the entity instance. For example, UserDTOInput instances
// don't have a Long id attribute (it must be auto-generated at the DBMS), while UserDTOOutput instances don't have the String
// password attribute (the password never should be returned to the user, and should be kept encrypted at the DB). Also, as the
// DTO is not a reference to the JPA entity object (User), there's no way to persist any change at the DB by using the DTOs
// returned from the UserService. Entities are only accessible from inside the UserRepository and UserService classes, and there
// are no references to them from outside. All entity instances are encapsulated inside the Service and Persistence layers. There's
// no direct manipulation of them from the Controller or from any View we created using the Service layer.

// All password input data is encrypted at this very class. Password data that the Service and Repository layers eventually receive,
// are already encrypted, as well as the data when persisted at the DB. Except at login, 'cause two hashed passwords (even from same
// raw data) can't be compared using BCrypt, so we couldn't send a hashed password from the login input to be compared with another
// already hashed password of an existing user at the DB. We must send the login input as plain-text (uncoded). The only way we would
// get comparable hashes from one same data would be if we stored the same salt used when hashing both values, but that would destroy
// the security of the hashing BCrypt algorithm, which includes exactly having a random - and not a fixed - salt each time any value is
// encrypted, so that two hashed values, even from one same original data, will never be equal (and therefore will never match). We can
// only compare plain-text passwords (login input unencrypted data) with hashed passwords from the DB, not two already hashed passwords.
// The main thing is that any password never is persisted without being encrypted before. And, when the user tries to log in, at least
// a secure SSL connection is established, so that the password he sends to this app server is not intercepted unencrypted before
// arriving here. Each endpoint should be evaluated regarding authentication and authorization - and configured accordingly, at the
// WebSecurityConfig bean class.

// For more on BCrypt and cryptography when using Spring, check out the Spring BCrypt documentation:
// https://docs.spring.io/spring-security/reference/features/integrations/cryptography.html .

// Regarding JWT tokens, which are another security strategy for securing endpoints' access, see the security package classes of this
// project. Have in mind that one thing is the signup and authentication of the user at this server (DB), with encrypted passwords,
// always, another thing is the user being able to, after being authenticated, have received a token, valid for a time, in order to access
// secured endpoints without having to log in again, each time he sends a request. The two security strategies are combined, but unique on
// their own. Including the Encryption algorithms may differ, according to how we configure them. We may have a better level of encryption for
// the passwords, and a lower level of encryption for the tokens.
