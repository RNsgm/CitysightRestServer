package cisi.citysight.auth.controllers.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cisi.citysight.auth.models.AccessLevel;
import cisi.citysight.auth.models.Role;
import cisi.citysight.auth.models.User;
import cisi.citysight.auth.response_models.ResponseDefaultBooleanValue;
import cisi.citysight.auth.response_models.ResponseUserLvl;
import cisi.citysight.auth.security.jwt.JwtUser;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/user")
@Slf4j
public class UserController {
    
    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("access_lvl")
    public ResponseEntity access_lvl(@AuthenticationPrincipal JwtUser user){
        if(user == null) return ResponseEntity.ok(new ResponseUserLvl(AccessLevel.LEVEL_1));
        return ResponseEntity.ok(new ResponseUserLvl(user.getLevel()));
    }

    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("is_admin")
    public ResponseEntity isAdmin(@AuthenticationPrincipal JwtUser user){
        if(user == null) return ResponseEntity.ok(new ResponseDefaultBooleanValue(false));
        boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_ADMIN));
        return ResponseEntity.ok(new ResponseDefaultBooleanValue(isAdmin));
    }
    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("is_moderator")
    public ResponseEntity isModerator(@AuthenticationPrincipal JwtUser user){
        if(user == null) return ResponseEntity.ok(new ResponseDefaultBooleanValue(false));
        boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_MODERATOR));
        return ResponseEntity.ok(new ResponseDefaultBooleanValue(isAdmin));
    }
    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("is_editor")
    public ResponseEntity isEditor(@AuthenticationPrincipal JwtUser user){
        if(user == null) return ResponseEntity.ok(new ResponseDefaultBooleanValue(false));
        boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_EDITOR));
        return ResponseEntity.ok(new ResponseDefaultBooleanValue(isAdmin));
    }










    // @PostMapping("is_")
    // public ResponseEntity is(@AuthenticationPrincipal JwtUser user){
    //     boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_));
    //     return ResponseEntity.ok(new ResponseDefaultBooleanValue(isAdmin));
    // }
    // @PostMapping("is_")
    // public ResponseEntity is(@AuthenticationPrincipal JwtUser user){
    //     boolean isAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_));
    //     return ResponseEntity.ok(new ResponseDefaultBooleanValue(isAdmin));
    // }
}
