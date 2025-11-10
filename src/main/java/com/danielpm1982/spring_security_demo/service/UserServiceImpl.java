package com.danielpm1982.spring_security_demo.service;
import com.danielpm1982.spring_security_demo.dto.UserDTOInput;
import com.danielpm1982.spring_security_demo.dto.UserDTOOutput;
import com.danielpm1982.spring_security_demo.entity.User;
import com.danielpm1982.spring_security_demo.repository.UserRepository;
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
}
