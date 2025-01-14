package com.global.evaluation.app.controller;

import com.global.evaluation.app.model.User;
import com.global.evaluation.app.service.UserService;
import com.global.evaluation.app.util.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * Create user POST request
     *
     * @param user
     * @return
     */
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody User user) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(user));
        } catch (GlobalException ex) {
            return ResponseEntity.status(ex.getStatus()).body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "codigo", ex.getStatus().value(),
                    "detalle", ex.getMessage()
            ));
        }
    }
    /**
     * Get Login User
     *
     * @param token
     * @return
     */
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.login(token.replace("Bearer", "")));
        } catch (GlobalException ex) {
            return ResponseEntity.status(ex.getStatus()).body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "codigo", ex.getStatus().value(),
                    "detalle", ex.getMessage()+ex.getStatus()
            ));
        }
    }
}
