import com.example.quizapp.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static final String USERNAME = "test-username";
    private static final String PASSWORD = "test-password";
    private static final String EMAIL = "test@test.com";
    private static final String USERNAME2 = "test-username-2";
    private static final String PASSWORD2 = "test-password-2";
    private static final String EMAIL2 = "test2@test.com";

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(USERNAME, PASSWORD, EMAIL);
    }

    @Test
    public void testGetUserName() {
        assertEquals(USERNAME, user.getUserName());
    }
    @Test
    public void testSetUserName() {
        user.setUserName(USERNAME2);
        assertEquals(USERNAME2, user.getUserName());
    }
    @Test
    public void testGetPassword() {
        assertEquals(PASSWORD, user.getPassword());
    }
    @Test
    public void testSetPassword() {
        user.setPassword(PASSWORD2);
        assertEquals(PASSWORD2, user.getPassword());
    }
    @Test
    public void testGetEmail() {
        assertEquals(EMAIL, user.getEmail());
    }
    @Test
    public void testSetEmail() {
        user.setEmail(EMAIL2);
        assertEquals(EMAIL2, user.getEmail());
    }

    // failing tests -- functionality not yet implemented in User class
//    @Test
//    public void testSetUserNameEmpty() {
//        assertThrows(IllegalArgumentException.class, () -> user.setUserName(""));
//    }
//    @Test
//    public void testSetPasswordEmpty() {
//        assertThrows(IllegalArgumentException.class, () -> user.setPassword(""));
//    }
//    @Test
//    public void testSetPasswordShort() { // throw exception if password shorter than some min. length
//        assertThrows(IllegalArgumentException.class, () -> user.setPassword("123"));
//    }
//    @Test
//    public void testSetEmailEmpty() {
//        assertThrows(IllegalArgumentException.class, () -> user.setEmail(""));
//    }
//    @Test
//    public void testSetEmailInvalid() { // throw exception if input is not a valid email address
//        assertThrows(IllegalArgumentException.class, () -> user.setEmail("invalid email"));
//    }
}
