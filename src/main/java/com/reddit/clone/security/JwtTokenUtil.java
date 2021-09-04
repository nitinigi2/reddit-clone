package com.reddit.clone.security;

import com.reddit.clone.dto.JwtResponse;
import com.reddit.clone.exception.JwtTokenNotValidException;
import com.reddit.clone.exception.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@PropertySource("classpath:application.properties")
@Slf4j
public class JwtTokenUtil implements Serializable {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    //creates a spec-compliant secure-random key:
    private SecretKey securityKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); //or HS384 or HS512

    public JwtResponse generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String token = doGenerateToken(claims, userDetails.getUsername());

        return new JwtResponse(token);
    }

    /**
     * 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
     * 2. Sign the JWT using the HS512 algorithm and secret key.
     * 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(securityKey).compact();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    //validate token
    public Boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);
        return !isTokenExpired(token);
    }

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        Claims claims = null;
        try{
            claims = Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody();
        }catch (SignatureException e){
            throw new JwtTokenNotValidException(e);
        }catch (Exception e){
            log.debug(e.getMessage());
            throw new SpringRedditException("Error occurred while getting claims from token");
        }
        return claims;
    }
}
