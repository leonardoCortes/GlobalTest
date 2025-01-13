

import com.global.evaluation.app.UsersApplication;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class UsersApplicationTest {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> UsersApplication.main(new String[]{}));
    }
}
