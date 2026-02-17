package escuelaing.ieti.bookingSystem.controller.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import escuelaing.ieti.bookingSystem.dto.AuthenticationRequest;
import escuelaing.ieti.bookingSystem.dto.AuthenticationResponse;
import escuelaing.ieti.bookingSystem.dto.RegisterRequest;
import escuelaing.ieti.bookingSystem.model.User;
import escuelaing.ieti.bookingSystem.security.JwtUtil;
import escuelaing.ieti.bookingSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthenticationController controller;

    private AuthenticationRequest authenticationRequest;

    @BeforeEach
    void setUp() {
        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("jane@example.com");
        authenticationRequest.setPassword("MySecret123!");
    }

    @Test
    void loginShouldReturnTokenWhenCredentialsAreValid() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("token123");
        when(jwtUtil.getExpirationMillis()).thenReturn(3600L);

        ResponseEntity<AuthenticationResponse> response = controller.login(authenticationRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isEqualTo("token123");
        assertThat(response.getBody().getExpiresIn()).isEqualTo(3600L);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(userDetails);
    }

    @Test
    void loginShouldThrowUnauthorizedWhenAuthenticationFails() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad"));

        assertThrows(ResponseStatusException.class, () -> controller.login(authenticationRequest));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoMoreInteractions(jwtUtil);
    }

    @Test
    void registerShouldCreateUserAndReturnCreatedStatus() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Jane");
        request.setEmail("jane@example.com");
        request.setPassword("pass");
        User stored = new User("1", "Jane", "jane@example.com", null);
        when(userService.create(any(User.class))).thenReturn(stored);

        ResponseEntity<User> response = controller.register(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(stored);
        verify(userService).create(any(User.class));
    }

    @Test
    void loginShouldUseProvidedCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenAnswer(invocation -> {
                    UsernamePasswordAuthenticationToken token = invocation.getArgument(0);
                    assertThat(token.getPrincipal()).isEqualTo("jane@example.com");
                    assertThat(token.getCredentials()).isEqualTo("MySecret123!");
                    return authentication;
                });
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("token123");
        when(jwtUtil.getExpirationMillis()).thenReturn(10L);

        controller.login(authenticationRequest);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void registerShouldSendRequestPayloadToService() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John");
        request.setEmail("john@example.com");
        request.setPassword("secret");
        when(userService.create(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        controller.register(request);

        verify(userService).create(userCaptor.capture());
        User captured = userCaptor.getValue();
        assertThat(captured.getName()).isEqualTo("John");
        assertThat(captured.getEmail()).isEqualTo("john@example.com");
        assertThat(captured.getPassword()).isEqualTo("secret");
    }
}
