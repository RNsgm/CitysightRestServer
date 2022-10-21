package cisi.citysight.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import cisi.citysight.auth.security.jwt.JwtConfigurer;
import cisi.citysight.auth.security.jwt.JwtTokenProvider;

@EnableWebSecurity
@Configuration
public class SecurityConfig{

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    } 

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/img/**").permitAll()
            .antMatchers("/api/v1/user/**").permitAll()
            .antMatchers("/api/v1/auth/**").permitAll()
            .antMatchers("/api/v1/content/**").permitAll()
            .antMatchers("/api/v1/values/**").permitAll()
            .antMatchers("/api/v1/images/**").permitAll()
            .antMatchers("/api/v1/test/**").permitAll()
            .antMatchers("/api/v1/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .apply(new JwtConfigurer(tokenProvider));

        return http.build();
    }
    
}
