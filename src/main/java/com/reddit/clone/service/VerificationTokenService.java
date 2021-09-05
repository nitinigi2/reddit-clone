package com.reddit.clone.service;

import com.reddit.clone.exception.SpringRedditException;
import com.reddit.clone.model.User;
import com.reddit.clone.model.VerificationToken;
import com.reddit.clone.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserService userService;

    public void save(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    public void findByToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationToken);
    }

    @Transactional
    private void fetchUserAndEnable(Optional<VerificationToken> verificationToken) {
        String username = verificationToken.get().getUser().getUsername();
        User user = userService.findByUserName(username);
        user.setEnabled(true);
        userService.save(user);
    }
}
