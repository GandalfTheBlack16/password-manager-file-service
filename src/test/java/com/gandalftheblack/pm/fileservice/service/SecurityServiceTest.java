package com.gandalftheblack.pm.fileservice.service;

import com.gandalftheblack.pm.fileservice.exception.InvalidTokenException;
import com.gandalftheblack.pm.fileservice.exception.UnauthenticatedUserException;
import com.gandalftheblack.pm.fileservice.model.entity.UserEntity;
import com.gandalftheblack.pm.fileservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private SecurityService securityService;

    private static final String SECRET = "caa288691c0e008d66eb44e6c3dc6d5280660ea8cb6d249c449d3750cb54d260";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(securityService, "jwtSecret", SECRET);
    }

    @Test
    void shouldReturnUserIdAfterVerifyingTokenAndFindUserInDatabase() {
        String expectedEmail = "admin@example.com";
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImU1ZTI4ZjBlLTE2ZjItNGIxMy04ZTMzLWVlOD" +
                "BmZjc5MGQ5MyIsImVtYWlsIjoiYWRtaW5AZXhhbXBsZS5jb20iLCJ1c2VybmFtZSI6ImFkbWluIiwiaWF0IjoxNzMzMDA2MDg5LCJle" +
                "HAiOjQ4ODg3NjYwODl9.-CxTdu5bAxFdBn68xmLEPXZip5w8KHyUyWv9_7rp_Bg";
        when(userRepository.findUserByEmail(expectedEmail)).thenReturn(Optional.of(UserEntity.builder().id("1").build()));
        assertDoesNotThrow(() -> {
            String userId = securityService.getUserIdFromToken(authHeader);
            assertEquals("1", userId);
            verify(userRepository, times(1)).findUserByEmail(expectedEmail);
        });
    }

    @Test
    void shouldThrowExceptionIfAuthHeaderIsInvalid() {
        String authHeader = "FakeToken eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        assertThrows(InvalidTokenException.class, () -> securityService.getUserIdFromToken(authHeader));
    }

    @Test
    void shouldThrowExceptionIfJwtIsInvalid() {
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9InvalidToken";
        assertThrows(InvalidTokenException.class, () -> securityService.getUserIdFromToken(authHeader));
    }

    @Test
    void shouldThrowExceptionIfUserDoesNotExists() {
        String expectedEmail = "admin@example.com";
        String authHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImU1ZTI4ZjBlLTE2ZjItNGIxMy04ZTMzLWVlOD" +
                "BmZjc5MGQ5MyIsImVtYWlsIjoiYWRtaW5AZXhhbXBsZS5jb20iLCJ1c2VybmFtZSI6ImFkbWluIiwiaWF0IjoxNzMzMDA2MDg5LCJle" +
                "HAiOjQ4ODg3NjYwODl9.-CxTdu5bAxFdBn68xmLEPXZip5w8KHyUyWv9_7rp_Bg";
        when(userRepository.findUserByEmail(expectedEmail)).thenReturn(Optional.empty());
        assertThrows(UnauthenticatedUserException.class, () -> securityService.getUserIdFromToken(authHeader));
    }
}