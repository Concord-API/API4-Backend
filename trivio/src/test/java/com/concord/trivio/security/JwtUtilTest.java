package com.concord.trivio.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JwtUtil.class})
@TestPropertySource(properties = {
    "app.security.jwt.secret=dGVzdC1zZWNyZXQta2V5LWZvci11bml0LXRlc3Rpbmctb25seS10aGlzLWlzLWxvbmctZW5vdWdo",
    "app.security.jwt.expiration-seconds=3600"
})
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    private UserDetails manager;
    private UserDetails technician;

    @BeforeEach
    void setUp() {
        manager = User.withUsername("manager@trivio.com")
            .password("hash")
            .authorities("ROLE_MANAGER")
            .build();

        technician = User.withUsername("tech@trivio.com")
            .password("hash")
            .authorities("ROLE_TECHNICIAN")
            .build();
    }

    @Test
    void generateToken_returnsNonBlankString() {
        assertThat(jwtUtil.generateToken(manager)).isNotBlank();
    }

    @Test
    void extractUsername_returnsSubject() {
        String token = jwtUtil.generateToken(manager);
        assertThat(jwtUtil.extractUsername(token)).isEqualTo("manager@trivio.com");
    }

    @Test
    void extractRole_returns_MANAGER_for_admin() {
        String token = jwtUtil.generateToken(manager);
        assertThat(jwtUtil.extractRole(token)).isEqualTo("MANAGER");
    }

    @Test
    void extractRole_returns_TECHNICIAN_for_non_admin() {
        String token = jwtUtil.generateToken(technician);
        assertThat(jwtUtil.extractRole(token)).isEqualTo("TECHNICIAN");
    }

    @Test
    void isTokenValid_trueForMatchingUser() {
        String token = jwtUtil.generateToken(manager);
        assertThat(jwtUtil.isTokenValid(token, manager)).isTrue();
    }

    @Test
    void isTokenValid_falseForDifferentUser() {
        String token = jwtUtil.generateToken(manager);
        assertThat(jwtUtil.isTokenValid(token, technician)).isFalse();
    }
}
