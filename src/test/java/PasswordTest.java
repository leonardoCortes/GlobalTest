import com.global.evaluation.app.util.Password;
import org.junit.jupiter.api.Test;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordTest {

    @Test
    void testEncryptValidPassword() {
        String password = "securePassword123";
        String encrypted = Password.encrypt(password);

        assertNotNull(encrypted, "The encrypted password should not be null");
        assertEquals(Base64.getEncoder().encodeToString(password.getBytes()), encrypted, "The encryption should match the expected Base64 encoding");
    }

    @Test
    void testEncryptEmptyPassword() {
        String password = "";
        String encrypted = Password.encrypt(password);

        assertNotNull(encrypted, "The encrypted password should not be null even for an empty input");
        assertEquals(Base64.getEncoder().encodeToString(password.getBytes()), encrypted, "The encryption of an empty password should match the expected Base64 encoding");
    }

    @Test
    void testEncryptNullPassword() {
        assertThrows(NullPointerException.class, () -> Password.encrypt(null), "Encrypting a null password should throw NullPointerException");
    }
}