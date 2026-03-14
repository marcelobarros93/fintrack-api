package com.example.fintrack.api.test.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class BCryptPasswordEncoderTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void encode_ShouldReturnEncodedPassword_WhenRawPasswordIsProvided() {
        String rawPassword = "senha123";

        String encoded = passwordEncoder.encode(rawPassword);

        System.out.println("Raw password  : " + rawPassword);
        System.out.println("Encoded password: " + encoded);

        assertThat(encoded)
                .isNotNull()
                .isNotEqualTo(rawPassword)
                .startsWith("$2a$");
    }

    @Test
    void matches_ShouldReturnTrue_WhenRawPasswordMatchesEncoded() {
        String rawPassword = "minha_senha_123";
        String encoded = passwordEncoder.encode(rawPassword);

        boolean matches = passwordEncoder.matches(rawPassword, encoded);

        assertThat(matches).isTrue();
    }

    @Test
    void matches_ShouldReturnFalse_WhenRawPasswordDoesNotMatchEncoded() {
        String rawPassword = "minha_senha_123";
        String wrongPassword = "senha_errada";
        String encoded = passwordEncoder.encode(rawPassword);

        boolean matches = passwordEncoder.matches(wrongPassword, encoded);

        assertThat(matches).isFalse();
    }

    @Test
    void encode_ShouldGenerateDifferentHashes_WhenSamePasswordIsEncodedTwice() {
        String rawPassword = "minha_senha_123";

        String encoded1 = passwordEncoder.encode(rawPassword);
        String encoded2 = passwordEncoder.encode(rawPassword);

        System.out.println("Encoded 1: " + encoded1);
        System.out.println("Encoded 2: " + encoded2);

        assertThat(encoded1).isNotEqualTo(encoded2);
        assertThat(passwordEncoder.matches(rawPassword, encoded1)).isTrue();
        assertThat(passwordEncoder.matches(rawPassword, encoded2)).isTrue();
    }
}

