package cisi.citysight.auth.controllers.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cisi.citysight.auth.models.AuthenticationRequestDto;
import cisi.citysight.auth.models.User;
import cisi.citysight.auth.security.jwt.JwtTokenProvider;
import cisi.citysight.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/admin")
@Slf4j
public class Admin {
    
    private UserService userService;

    @Autowired
    public Admin() {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity login(){

        Map<Object, Object> response = new HashMap<>();
        response.put("Status", "OK");

        return ResponseEntity.ok(response);
    }

    
}

