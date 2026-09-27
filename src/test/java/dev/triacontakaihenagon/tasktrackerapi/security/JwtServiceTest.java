package dev.triacontakaihenagon.tasktrackerapi.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtService jwtService =
            new JwtService("test-secret-key-for-tests-only-1234567890", 3600000);

    @Test
    void generateToken_thenExtractUsername_roundTrips() {
        String token = jwtService.generateToken("alice");

        assertThat(jwtService.extractUsername(token)).isEqualTo("alice");
    }

    @Test
    void isTokenValid_trueForMatchingUsernameAndUnexpired() {
        String token = jwtService.generateToken("alice");

        assertThat(jwtService.isTokenValid(token, "alice")).isTrue();
    }

    @Test
    void isTokenValid_falseForDifferentUsername() {
        String token = jwtService.generateToken("alice");

        assertThat(jwtService.isTokenValid(token, "bob")).isFalse();
    }

    @Test
    void isTokenValid_falseWhenExpired() throws InterruptedException {
        JwtService shortLived = new JwtService("test-secret-key-for-tests-only-1234567890", 1);
        String token = shortLived.generateToken("alice");

        Thread.sleep(5);

        assertThat(shortLived.isTokenValid(token, "alice")).isFalse();
    }
}