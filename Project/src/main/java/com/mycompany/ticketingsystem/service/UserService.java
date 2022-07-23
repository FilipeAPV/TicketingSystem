package com.mycompany.ticketingsystem.service;

import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean saveUser(User user) {
        boolean isSaved = false;

        //Hash the plain text password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user = userRepository.save(user);
        if (user != null && user.getId() > 0) {
            isSaved = true;
        }

        return isSaved;
    }

    public int totalNumberOfUsers() {
        List<User> userList = userRepository.findAll();
        return userList.size();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
