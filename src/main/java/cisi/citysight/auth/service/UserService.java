package cisi.citysight.auth.service;

import java.util.List;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cisi.citysight.auth.models.User;

public interface UserService {
    
    User register(User user);

    User forAuthenticationUser(String param) throws AuthenticationException;

    List<User> getAll();

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id);

    void delete(Long id);
}
