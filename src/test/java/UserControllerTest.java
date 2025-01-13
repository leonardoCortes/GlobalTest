import com.global.evaluation.app.controller.UserController;
import com.global.evaluation.app.model.User;
import com.global.evaluation.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setName("Leonardo");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("Leo4nardo7");
    }

    @Test
    void testSignUpSuccess() {
        when(userService.signUp(any(User.class))).thenReturn(mockUser);

        ResponseEntity<?> response = userController.signUp(mockUser);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void testSignUpFailureInvalidEmail() {
        mockUser.setEmail("invalidemail");
        doThrow(new IllegalArgumentException("Invalid email format")).when(userService).signUp(any(User.class));

        ResponseEntity<?> response = userController.signUp(mockUser);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).get("error").toString().contains("Invalid email format"));
    }

    @Test
    void testLoginSuccess() {
        when(userService.login(anyString())).thenReturn(mockUser);

        ResponseEntity<?> response = userController.login("valid-token");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void testLoginFailureInvalidToken() {
        doThrow(new IllegalArgumentException("Invalid or expired token")).when(userService).login(anyString());

        ResponseEntity<?> response = userController.login("invalid-token");

        assertEquals(401, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).get("error").toString().contains("Invalid or expired token"));
    }
}