ipackage com.srms.service;

import com.srms.exception.InvalidLoginException;
import com.srms.model.Student;
import com.srms.model.User;
import com.srms.util.DBConnection;
import com.srms.util.PasswordUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    public void testAuthenticateAdminSuccess() throws Exception {
        AuthService authService = new AuthService();
        User admin = authService.authenticate("admin", "admin123");
        assertNotNull(admin);
        assertEquals("ADMIN", admin.getRole());
    }

    @Test
    public void testAuthenticateInvalidPassword() {
        AuthService authService = new AuthService();
        assertThrows(InvalidLoginException.class, () -> {
            authService.authenticate("admin", "wrongpass");
        });
    }

    @Test
    public void testAuthenticateEmptyCredentials() {
        AuthService authService = new AuthService();
        assertThrows(InvalidLoginException.class, () -> {
            authService.authenticate("", "");
        });
    }
}
