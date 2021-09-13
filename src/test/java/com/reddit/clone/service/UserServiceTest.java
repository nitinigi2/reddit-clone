package com.reddit.clone.service;

import com.reddit.clone.model.User;
import com.reddit.clone.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void saveTest(){
        User user = new User();
        user.setUserId(1L);
        user.setEnabled(false);
        user.setUsername("abc");
        user.setCreated(Instant.now());
        user.setEmail("def@gmail.com");
        user.setPassword("password");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        assertEquals(userService.save(user), user);
    }
}