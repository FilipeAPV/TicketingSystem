package com.mycompany.ticketingsystem.service;

import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean saveUser(User user) {
        boolean isSaved = false;

        user = userRepository.save(user);
        if (user != null && user.getId() > 0) {
            isSaved = true;
        }

        return isSaved;
    }


}
