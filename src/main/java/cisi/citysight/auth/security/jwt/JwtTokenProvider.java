package cisi.citysight.auth.security.jwt;

import java.security.Key;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import cisi.citysight.auth.models.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private long expiredInMilliseconds;

    @Autowired
    private UserDetailsService userDetailsService;

    private SecretKey secretKey;

    private final String DEVICE_ID = "";

    @PostConstruct
    protected void init() { 
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); 
    }
    
    public String createToken(String username, List<Role> roles){

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleNames(roles));

        Date now = new Date();
        Date validity = new Date(now.getTime() + expiredInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token){
        String username = Jwts.parserBuilder()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(token)
                            .getBody()
                            .getSubject();

        log.info("JTP getUsername username: {}", username);
        return username;
    }

    public String resolveToken(HttpServletRequest request){
        String cisiToken = request.getHeader("Authorization");
        if (cisiToken != null && cisiToken.startsWith("cisi_")){
            return cisiToken.substring(5, cisiToken.length());
        }
        return null;
    }

    public boolean validateToken(String token){
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                                    .setSigningKey(secretKey)
                                    .build()
                                    .parseClaimsJws(token);

            if(claims.getBody().getExpiration().before(new Date())){
                return false;
            }
            // if(claims.getBody().containsKey(DEVICE_ID)){

            // }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT token is expired or invalid");
            return false;
        }
    }

    private List<String> getRoleNames(List<Role> userRoles){
        List<String> result = new ArrayList<>();

        userRoles.forEach(role -> {
            result.add(role.getName());
        });

        return result;
    }
}
