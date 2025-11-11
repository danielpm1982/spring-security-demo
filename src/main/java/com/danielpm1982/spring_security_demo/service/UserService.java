package com.danielpm1982.spring_security_demo.service;
import com.danielpm1982.spring_security_demo.dto.LoginDTOInput;
import com.danielpm1982.spring_security_demo.dto.UserDTOInput;
import com.danielpm1982.spring_security_demo.dto.UserDTOOutput;
import com.danielpm1982.spring_security_demo.security.MyToken;
import java.util.List;
import java.util.Optional;

public interface UserService {
    public Optional<UserDTOOutput> findByUsername(String username);
    public Optional<UserDTOOutput> addUser(UserDTOInput userDTOInput);
    public List<UserDTOOutput> findAll();
    public Optional<MyToken> userLogin(LoginDTOInput loginDTOInput);
}
