import com.global.evaluation.app.util.Validation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {

    @Test
    public void testValidEmail() {
        // Valid email cases
        assertTrue(Validation.isValidEmail("test@example.com"));
        assertTrue(Validation.isValidEmail("user.name+tag+sorting@example.com"));
        // Invalid email cases
        assertFalse(Validation.isValidEmail("plainaddress"));
        assertFalse(Validation.isValidEmail("@missingusername.com"));
        assertFalse(Validation.isValidEmail("username@.com"));
        assertFalse(Validation.isValidEmail("username@com"));
    }

    @Test
    public void testValidPassword() {
        // Valid password cases
        assertTrue(Validation.isValidPassword("1Password2"));
        // Invalid password cases
        assertFalse(Validation.isValidPassword("short")); // Too short
        assertFalse(Validation.isValidPassword("toolongpassword123")); // Too long
        assertFalse(Validation.isValidPassword("NoNumbers")); // Missing numbers
        assertFalse(Validation.isValidPassword("nonumberscapital")); // Missing numbers and capital letter
        assertFalse(Validation.isValidPassword("CAPSANDNUM2")); // No lowercase letters
        assertFalse(Validation.isValidPassword("12345678")); // Only numbers
    }
}
