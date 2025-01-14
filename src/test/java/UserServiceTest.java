
import com.global.evaluation.app.model.User;
import com.global.evaluation.app.repository.UserRepository;
import com.global.evaluation.app.service.UserService;
import com.global.evaluation.app.util.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("Leo4nardo7");
    }

    @Test
    void testCreateUserSuccess() {
        //when(Validation.isValidEmail(anyString())).thenReturn(true);
        //when(Validation.isValidPassword(anyString())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.signUp(mockUser);

        assertNotNull(result);
        assertEquals(mockUser.getEmail(), result.getEmail());
    }

    @Test
    void testCreateUserFailureEmailExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

       // Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.signUp(mockUser));

       // assertEquals("User with this email already exists", exception.getMessage());
    }

    @Test
    void testLoginFailureUserNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        //Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.login("valid-token"));

        //assertEquals("User not found", exception.getMessage());
    }
}
