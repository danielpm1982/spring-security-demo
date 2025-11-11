package com.danielpm1982.spring_security_demo.service;
import com.danielpm1982.spring_security_demo.dto.LoginDTOInput;
import com.danielpm1982.spring_security_demo.dto.UserDTOInput;
import com.danielpm1982.spring_security_demo.dto.UserDTOOutput;
import com.danielpm1982.spring_security_demo.entity.User;
import com.danielpm1982.spring_security_demo.repository.UserRepository;
import com.danielpm1982.spring_security_demo.security.MyToken;
import com.danielpm1982.spring_security_demo.security.TokenManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public Optional<UserDTOOutput> findByUsername(String username) {
        Optional<User> foundUserOptional = userRepository.findByUsername(username);
        Optional<UserDTOOutput> userDTOOutputOptional;
        if(foundUserOptional.isPresent()){
            User foundUser = foundUserOptional.get();
            userDTOOutputOptional = Optional.of(new UserDTOOutput(foundUser.getId(), foundUser.getUsername(), foundUser.getEmail()));
        }else{
            userDTOOutputOptional = Optional.empty();
        }
        return userDTOOutputOptional;
    }
    @Transactional
    @Override
    public Optional<UserDTOOutput> addUser(UserDTOInput userDTOInput) {
        User newUser = new User(null, userDTOInput.getUsername(), userDTOInput.getPassword(), userDTOInput.getEmail());
        User addeduser = userRepository.save(newUser);
        UserDTOOutput userDTOOutput = new UserDTOOutput(addeduser.getId(), addeduser.getUsername(), addeduser.getEmail());
        return Optional.of(userDTOOutput);
    }
    @Override
    public List<UserDTOOutput> findAll() {
        List<User> foundUserList =  userRepository.findAll();
        List<UserDTOOutput> userDTOOutputList = new ArrayList<>();
        foundUserList.forEach(x->userDTOOutputList.add(new UserDTOOutput(x.getId(), x.getUsername(), x.getEmail())));
        return userDTOOutputList;
    }
    @Override
    public Optional<MyToken> userLogin(LoginDTOInput loginDTOInput) {
        Optional<User> storedUserOptional = userRepository.findByEmail(loginDTOInput.getEmail());
        if(storedUserOptional.isPresent()){
            User storedUser = storedUserOptional.get();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if(bCryptPasswordEncoder.matches(loginDTOInput.getPassword(), storedUser.getPassword())){
                // if the user gets authenticated, user data is sent for TokenManager to create the JWT token and return it to the user...
                // while also registering this just-created user-customized JWT token at the SecurityContextHolder context, through the
                // MyAuthenticationFilter, as an allowed valid token, so that the user can proceed with further requests - then using that
                // pre-approved token at the Authorization header as a bearer token. With a valid token, the same user won't need to log in
                // anymore... only when the token expires
                UserDTOInput userDTOInput = new UserDTOInput(storedUser.getUsername(), storedUser.getPassword(), storedUser.getEmail());
                return Optional.of(TokenManager.encodeToken(userDTOInput));
                //returns JWT token, encoded with custom data from the issuer and the user, cryptographed with the encoding key (at TokenManager class)
            }
        }
        return Optional.empty();
        //returns an empty Optional either if user (by email) not found or login input raw password and DB stored encoded password do not match
    }
}
