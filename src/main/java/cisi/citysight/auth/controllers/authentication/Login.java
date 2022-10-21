package cisi.citysight.auth.controllers.authentication;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cisi.citysight.auth.models.AuthenticationRequestDto;
import cisi.citysight.auth.models.User;
import cisi.citysight.auth.response_models.AuthenticationToken;
import cisi.citysight.auth.response_models.ResponseError;
import cisi.citysight.auth.security.jwt.JwtTokenProvider;
import cisi.citysight.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/auth")
@Slf4j
public class Login {
    
    private AuthenticationManager authenticationManager;

    private JwtTokenProvider JwtTokenProvider;

    private UserService userService;

    @Autowired
    public Login(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        JwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto auth, HttpServletRequest request){

        try {
            String username = auth.getUsername();
            User user = userService.forAuthenticationUser(username);

            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), auth.getPassword()));
            } catch (AuthenticationException e) {}
                
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), auth.getPassword()));

            String token = JwtTokenProvider.createToken(username, user.getRoles());

            AuthenticationToken result = new AuthenticationToken(username, token);
                
            log.info("Username: {}, Token: {}", username, token);
            return ResponseEntity.ok(result);
        } catch (AuthenticationException e) {
            ResponseError error = new ResponseError("Uninvalid username or password");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }
}
