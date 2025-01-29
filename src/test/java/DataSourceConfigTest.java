import com.global.evaluation.app.config.DataSourceConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataSourceConfigTest {

    private DataSourceConfig dataSourceConfig;

    @BeforeEach
    void setUp() throws Exception {
        dataSourceConfig = new DataSourceConfig();
        setPrivateField(dataSourceConfig, "dbURL", "jdbc:h2:mem:testdb");
        setPrivateField(dataSourceConfig, "dbDriver", "org.h2.Driver");
        setPrivateField(dataSourceConfig, "username", "sa");
        setPrivateField(dataSourceConfig, "password", "password");
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testDataSourceConfiguration() throws Exception {
        DataSource dataSource = dataSourceConfig.getDataSource();
        assertNotNull(dataSource);
        String dbURL = (String) getPrivateField(dataSourceConfig, "dbURL");
        String dbDriver = (String) getPrivateField(dataSourceConfig, "dbDriver");
        String username = (String) getPrivateField(dataSourceConfig, "username");
        String password = (String) getPrivateField(dataSourceConfig, "password");

        assertEquals("jdbc:h2:mem:testdb", dbURL);
        assertEquals("org.h2.Driver", dbDriver);
        assertEquals("sa", username);
        assertEquals("password", password);
    }

    private Object getPrivateField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}