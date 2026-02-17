package escuelaing.ieti.bookingSystem.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtUtilTest {

    private static final String SECRET = "bookingSystemSuperSecretKeyChangeMe123456789";

    @Test
    void generateTokenShouldEmbedUsername() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 3_600_000);
        UserDetails userDetails = buildUser("jane@example.com");

        String token = jwtUtil.generateToken(userDetails);

        assertThat(jwtUtil.extractUsername(token)).isEqualTo("jane@example.com");
    }

    @Test
    void validateTokenShouldReturnTrueForValidUser() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 3_600_000);
        UserDetails userDetails = buildUser("jane@example.com");

        String token = jwtUtil.generateToken(userDetails);

        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void validateTokenShouldReturnFalseForDifferentUser() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 3_600_000);
        UserDetails userDetails = buildUser("jane@example.com");
        UserDetails otherUser = buildUser("john@example.com");
        String token = jwtUtil.generateToken(userDetails);

        assertFalse(jwtUtil.validateToken(token, otherUser));
    }

    @Test
    void validateTokenShouldThrowExceptionWhenExpired() throws InterruptedException {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 5);
        UserDetails userDetails = buildUser("jane@example.com");
        String token = jwtUtil.generateToken(userDetails);
        Thread.sleep(10);

        assertThrows(ExpiredJwtException.class, () -> jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void getExpirationMillisShouldExposeConfiguredValue() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 42_000);

        assertThat(jwtUtil.getExpirationMillis()).isEqualTo(42_000);
    }

    private UserDetails buildUser(String email) {
        return User.withUsername(email).password("password").roles("USER").build();
    }
}
