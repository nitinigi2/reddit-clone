package com.reddit.clone.service;

import com.reddit.clone.exception.UserNotFoundException;
import com.reddit.clone.model.User;
import com.reddit.clone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with given username: " + username));
    }
}
