package com.Shadows.SpringZ.api;

import com.Shadows.SpringZ.api.auth.AuthController;
import com.Shadows.SpringZ.api.auth.JwtResponse;
import com.Shadows.SpringZ.api.auth.LoginRequest;
import com.Shadows.SpringZ.api.auth.MessageResponse;
import com.Shadows.SpringZ.api.auth.SignupRequest;
import com.Shadows.SpringZ.model.User;
import com.Shadows.SpringZ.repository.UserRepository;
import com.Shadows.SpringZ.security.jwt.JwtUtils;
import com.Shadows.SpringZ.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @Test
    void signup_Success() {
        SignupRequest request = new SignupRequest();
        request.setName("Bob");
        request.setEmail("bob@example.com");
        request.setPassword("password");

        when(userRepository.existsByEmail("bob@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<?> response = authController.signup(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("User registered successfully", ((MessageResponse) response.getBody()).getMessage());
    }

    @Test
    void signup_EmailAlreadyExists_ReturnsBadRequest() {
        SignupRequest request = new SignupRequest();
        request.setEmail("existing@example.com");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        ResponseEntity<?> response = authController.signup(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Error: Email is already in use!", ((MessageResponse) response.getBody()).getMessage());
    }

    @Test
    void signin_Success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("password");

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "user@example.com", "encoded", null);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateTokenFromSubject("user@example.com")).thenReturn("mocked_jwt_token");

        ResponseEntity<JwtResponse> response = authController.signin(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mocked_jwt_token", response.getBody().getToken());
        assertEquals("user@example.com", response.getBody().getEmail());
    }
}
