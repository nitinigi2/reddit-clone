package com.reddit.clone.service;

import com.reddit.clone.exception.SpringRedditException;
import com.reddit.clone.exception.UserNotFoundException;
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

    public User findByUserName(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with given username: " + username));
        return user;
    }
}
