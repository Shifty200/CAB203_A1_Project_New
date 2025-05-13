import com.example.quizapp.model.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class DatabaseTest {
    @Test
    public void testConnection() {
        Connection conn = SQLiteUserConnectionLive.getInstance();
        assertEquals(true, conn != null);
    }
}