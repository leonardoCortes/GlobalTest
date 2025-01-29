import com.global.evaluation.app.controller.UserController;
import com.global.evaluation.app.exception.GlobalException;
import com.global.evaluation.app.model.User;
import com.global.evaluation.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp_Success() throws GlobalException {
        User user = new User();
        user.setName("testUser");
        user.setPassword("testPassword");

        when(userService.signUp(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.signUp(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).signUp(any(User.class));
    }

    @Test
    void testSignUp_GlobalException() throws GlobalException {
        User user = new User();
        user.setName("testUser");
        user.setPassword("testPassword");

        GlobalException globalException = new GlobalException("User already exists", HttpStatus.CONFLICT);

        when(userService.signUp(any(User.class))).thenThrow(globalException);

        ResponseEntity<?> response = userController.signUp(user);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("User already exists", responseBody.get("detalle"));
        assertEquals(HttpStatus.CONFLICT.value(), responseBody.get("codigo"));
        verify(userService, times(1)).signUp(any(User.class));
    }

    @Test
    void testLogin_Success() throws GlobalException {
        String token = "validToken";
        User user = new User();
        user.setName("testUser");
        user.setPassword("testPassword");

        when(userService.login(any(String.class))).thenReturn(user);

        ResponseEntity<?> response = userController.login("Bearer " + token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).login(any(String.class));
    }

    @Test
    void testLogin_GlobalException() throws GlobalException {
        String token = "invalidToken";

        GlobalException globalException = new GlobalException("Invalid token", HttpStatus.UNAUTHORIZED);

        when(userService.login(any(String.class))).thenThrow(globalException);

        ResponseEntity<?> response = userController.login("Bearer " + token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Invalid token"+HttpStatus.UNAUTHORIZED, responseBody.get("detalle"));
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.get("codigo"));
        verify(userService, times(1)).login(any(String.class));
    }
}