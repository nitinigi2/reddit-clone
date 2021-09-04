package com.reddit.clone.service;

import com.reddit.clone.model.User;
import com.reddit.clone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
