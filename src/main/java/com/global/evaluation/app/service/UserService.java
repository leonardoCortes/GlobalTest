package com.global.evaluation.app.service;

import com.global.evaluation.app.model.User;
import com.global.evaluation.app.repository.UserRepository;
import com.global.evaluation.app.util.GlobalException;
import com.global.evaluation.app.util.Jwt;
import com.global.evaluation.app.util.Password;
import com.global.evaluation.app.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Jwt jwtUtil = new Jwt();

    /**
     * Create User Service
     *
     * @param user
     * @return
     */
    public User signUp(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent() && !user.getEmail().isEmpty() && user.getEmail() != null) {
            throw new GlobalException("User already exists", HttpStatus.BAD_REQUEST);
        }

        if (!Validation.isValidEmail(user.getEmail())) {
            throw new GlobalException("Invalid email format: aaaaa@domain.com", HttpStatus.BAD_REQUEST);
        }

        if (!Validation.isValidPassword(user.getPassword())) {
            throw new GlobalException("Invalid password format: min 8 max 12-2 numbers no consecutive, Ex: a2asdf3jjaaM ", HttpStatus.BAD_GATEWAY);
        }

        user.setPassword(Password.encrypt(user.getPassword()));
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setToken(jwtUtil.generateToken(user.getEmail()));
        user.setActive(true);

        return userRepository.save(user);
    }

    /**
     * Get Login Service
     *
     * @param token
     * @return
     */
    public User login(String token) {
        String email = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new GlobalException("User not found", HttpStatus.NOT_FOUND));

        user.setLastLogin(LocalDateTime.now());
        user.setToken(jwtUtil.generateToken(user.getEmail()));

        return userRepository.save(user);
    }
}