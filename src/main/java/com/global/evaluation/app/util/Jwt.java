package com.global.evaluation.app.util;

import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Jwt {

    public static final String SECRET_KEY = "mySecretKey";

    /**
     * Generate Token Method
     *
     * @param subject
     * @return
     */
    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Method to extract username from Token
     *
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    /**
     * Method to validate TOKEN
     * @param token
     * @return
     */
    public HttpStatus validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            // Token is valid
            return HttpStatus.OK;
        } catch (MalformedJwtException e) {
            // Token structure is invalid
            return HttpStatus.BAD_REQUEST;
        } catch (ExpiredJwtException e) {
            // Token has expired
            return HttpStatus.UNAUTHORIZED;
        } catch (UnsupportedJwtException e) {
            // Token is unsupported
            return HttpStatus.NOT_ACCEPTABLE;
        } catch (SignatureException | SecurityException e) {
            // Token signature is invalid
            return HttpStatus.FORBIDDEN;
        } catch (IllegalArgumentException e) {
            // Token is empty or null
            return HttpStatus.BAD_REQUEST;
        }
    }
}