package ro.mariuscirstea.template.payload.auth;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private String name;
    private String userId;
    private String refreshToken;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtResponse(String accessToken, String username, String email, String name,
                       Collection<? extends GrantedAuthority> authorities, String userId, String refreshToken) {
        this.token = accessToken;
        this.username = username;
        this.authorities = authorities;
        this.email = email;
        this.name = name;
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}


