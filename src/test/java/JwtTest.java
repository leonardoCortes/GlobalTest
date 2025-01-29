import com.global.evaluation.app.util.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class JwtTest {

    private Jwt jwt;

    @BeforeEach
    void setUp() {
        jwt = new Jwt();
    }

    @Test
    void testGenerateToken() {
        String subject = "testUser";
        String token = jwt.generateToken(subject);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testExtractUsername() {
        String subject = "testUser";
        String token = jwt.generateToken(subject);
        String extractedUsername = jwt.extractUsername(token);

        assertEquals(subject, extractedUsername);
    }

    @Test
    void testValidateToken_ValidToken() {
        String subject = "testUser";
        String token = jwt.generateToken(subject);
        HttpStatus status = jwt.validateToken(token);

        assertEquals(HttpStatus.OK, status);
    }

    @Test
    void testValidateToken_MalformedJwtException() {
        String invalidToken = "invalidToken";
        HttpStatus status = jwt.validateToken(invalidToken);

        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    @Test
    void testValidateToken_ExpiredJwtException() {
        String expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)) // Fecha de emisión en el pasado
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // Fecha de expiración en el pasado
                .signWith(SignatureAlgorithm.HS256, jwt.SECRET_KEY)
                .compact();

        HttpStatus status = jwt.validateToken(expiredToken);

        assertEquals(HttpStatus.UNAUTHORIZED, status);
    }

    @Test
    void testValidateToken_UnsupportedJwtException() {
        String unsupportedToken = "unsupportedToken";
        HttpStatus status = jwt.validateToken(unsupportedToken);

        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    @Test
    void testValidateToken_SignatureException() {
        String invalidSignatureToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, "invalidSecretKey") // Usar una clave secreta diferente
                .compact();

        HttpStatus status = jwt.validateToken(invalidSignatureToken);
        assertEquals(HttpStatus.FORBIDDEN, status);
    }

    @Test
    void testValidateToken_IllegalArgumentException() {
        HttpStatus status = jwt.validateToken(null);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }
}