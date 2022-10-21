package cisi.citysight.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cisi.citysight.auth.models.User;
import cisi.citysight.auth.security.jwt.JwtUser;
import cisi.citysight.auth.security.jwt.JwtUserFactory;
import cisi.citysight.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService{

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.forAuthenticationUser(username);

        if(user == null){
            return null;
//            throw new UsernameNotFoundException("Username with username: " + username + " not found");
        }

        JwtUser jwtUser = JwtUserFactory.create(user);
        log.debug("IN loadUserByUsername - user with username: {} successfully loaded", username);
        
        return jwtUser;
    }

    
}