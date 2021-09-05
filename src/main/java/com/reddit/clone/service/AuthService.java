package com.reddit.clone.service;

import com.reddit.clone.dto.JwtResponse;
import com.reddit.clone.dto.LoginRequest;
import com.reddit.clone.dto.RegisterRequest;
import com.reddit.clone.model.NotificationEmail;
import com.reddit.clone.model.User;
import com.reddit.clone.model.VerificationToken;
import com.reddit.clone.security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final MailService mailService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    private static final String EMAIL_BODY = "Thank you for signing up to Spring Reddit, " +
            "please click on the below url to activate your account : " +
            "http://localhost:8080/api/auth/accountVerification/";
    private final Instant VERIFICATION_TOKEN_EXPIRATION_TIME = Instant.now().plus(3, ChronoUnit.DAYS); // 3 days from current instant

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(encodePassword(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .created(Instant.now()) // current time
                .enabled(false) // user is disabled initially
                .build();
        userService.save(user);

        String token = generateVerificationToken(user);
        mailService.sendEmail(NotificationEmail
                .builder()
                .subject("Please activate your account")
                .recipient(registerRequest.getEmail())
                .body(EMAIL_BODY + token)
                .build());

    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(VERIFICATION_TOKEN_EXPIRATION_TIME);

        verificationTokenService.save(verificationToken);
        return token;
    }

    public ResponseEntity<String> verifyAccount(String token) {
        verificationTokenService.findByToken(token);
        return new ResponseEntity<>("Account activated successfully. ", HttpStatus.OK);
    }

    public JwtResponse login(LoginRequest loginRequest) throws Exception {
        System.out.println("Inside login method in authService class");
        Authentication authentication = authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginRequest.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenUtil.generateToken(userDetails);
    }

    private Authentication authenticate(String username, String password) throws Exception {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        return authentication;
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userService.findByUserName(principal.getUsername());
    }
}
