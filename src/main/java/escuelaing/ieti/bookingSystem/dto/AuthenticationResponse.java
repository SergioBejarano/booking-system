package escuelaing.ieti.bookingSystem.dto;

public class AuthenticationResponse {

    private final String token;
    private final long expiresIn;

    public AuthenticationResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
