import com.global.evaluation.app.util.GlobalException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionTest {

    @Test
    public void testGlobalExceptionWithMessageAndStatus() {
        String message = "Invalid request";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        GlobalException exception = new GlobalException(message, status);

        assertEquals(message, exception.getMessage());
        assertEquals(status, exception.getStatus());
    }

    @Test
    public void testGlobalExceptionWithMessageStatusAndCause() {
        String message = "Invalid request";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Throwable cause = new RuntimeException("Underlying cause");

        GlobalException exception = new GlobalException(message, status, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(status, exception.getStatus());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testGlobalExceptionDefaults() {
        GlobalException exception = new GlobalException("Default test", HttpStatus.INTERNAL_SERVER_ERROR);

        assertEquals("Default test", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }
}
