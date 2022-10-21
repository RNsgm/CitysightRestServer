package cisi.citysight.auth.models;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String username;
    private String password;
}
