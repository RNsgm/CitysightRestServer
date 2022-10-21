package cisi.citysight.auth.response_models;

import lombok.Data;

@Data
public class AuthenticationToken {

    private String username;
    private String token;
    public AuthenticationToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    
}