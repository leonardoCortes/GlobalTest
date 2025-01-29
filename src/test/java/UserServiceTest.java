import com.global.evaluation.app.exception.GlobalException;
import com.global.evaluation.app.model.User;
import com.global.evaluation.app.repository.UserRepository;
import com.global.evaluation.app.service.UserService;
import com.global.evaluation.app.util.Jwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Jwt jwtUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("ValidPass12");

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.signUp(user);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignUp_UserAlreadyExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("ValidPass12");

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        GlobalException exception = assertThrows(GlobalException.class, () -> userService.signUp(user));

        assertEquals("User already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testSignUp_InvalidEmailFormat() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setPassword("ValidPass12");

        GlobalException exception = assertThrows(GlobalException.class, () -> userService.signUp(user));

        assertEquals("Invalid email format: aaaaa@domain.com", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testSignUp_InvalidPasswordFormat() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("invalid");

        GlobalException exception = assertThrows(GlobalException.class, () -> userService.signUp(user));

        assertEquals("Invalid password format: min 8 max 12 Characters, 2 numbers no consecutive, Ex: a2asdf3jjaaM ", exception.getMessage());
        assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatus());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        String token = "validToken";
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("ValidPass12");

        when(jwtUtil.validateToken(any(String.class))).thenReturn(HttpStatus.OK);
        when(jwtUtil.extractUsername(any(String.class))).thenReturn("test@example.com");
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.login(token);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(jwtUtil, times(1)).validateToken(any(String.class));
        verify(jwtUtil, times(1)).extractUsername(any(String.class));
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testLogin_TokenValidationFailed() {
        String token = "invalidToken";

        when(jwtUtil.validateToken(any(String.class))).thenReturn(HttpStatus.UNAUTHORIZED);

        GlobalException exception = assertThrows(GlobalException.class, () -> userService.login(token));

        assertEquals("Token validation failed with status: ", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        verify(jwtUtil, times(1)).validateToken(any(String.class));
        verify(jwtUtil, never()).extractUsername(any(String.class));
        verify(userRepository, never()).findByEmail(any(String.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_UserNotFound() {
        String token = "validToken";

        when(jwtUtil.validateToken(any(String.class))).thenReturn(HttpStatus.OK);
        when(jwtUtil.extractUsername(any(String.class))).thenReturn("test@example.com");
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        GlobalException exception = assertThrows(GlobalException.class, () -> userService.login(token));

        assertEquals("User not found  ", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(jwtUtil, times(1)).validateToken(any(String.class));
        verify(jwtUtil, times(1)).extractUsername(any(String.class));
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(userRepository, never()).save(any(User.class));
    }
}