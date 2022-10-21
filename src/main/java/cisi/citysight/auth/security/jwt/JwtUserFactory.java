package cisi.citysight.auth.security.jwt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import cisi.citysight.auth.models.Role;
import cisi.citysight.auth.enums.Status;
import cisi.citysight.auth.models.User;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }
    
    public static JwtUser create(User user){
        return new JwtUser(
            user.getId(), 
            user.getUsername(), 
            user.getFirstname(), 
            user.getLastname(), 
            user.getPassword(), 
            user.getEmail(), 
            user.getLevel(), 
            user.getStatus().equals(Status.ACTIVE), 
            user.getUpdated(), 
            mapToGrantedAuthority(new ArrayList<>(user.getRoles()))
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthority(List<Role> userRoles){
        return userRoles.stream()
                .map(
                    role -> new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }
}
