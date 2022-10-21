package cisi.citysight.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cisi.citysight.auth.models.Role;
import cisi.citysight.auth.enums.Status;
import cisi.citysight.auth.models.User;
import cisi.citysight.auth.repository.RoleRepository;
import cisi.citysight.auth.repository.UserRepository;
import cisi.citysight.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepo;
    private RoleRepository roleRepo;
    private final BCryptPasswordEncoder encoder;

    
    @Autowired
    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
    }

    @Override
    public User findById(Long id) {
        User user = userRepo.findById(id).orElse(null);

        if(user == null){
            log.warn("IN findById - no user found by id {}", id);
            return null;
        }

        log.debug("IN findById - user: {} found by id: {}", user, id);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepo.findByUsername(username);
        log.debug("IN findByUsername - user: {} found by username: {}", user, username);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepo.findAll();
        log.debug("IN getAll - {} users found", users.size());
        return users;
    }

    @Override
    public User register(User user) {
        Role roleUser = roleRepo.findByName(Role.ROLE_USER);
        List<Role> newRoles = new ArrayList<>();
        newRoles.add(roleUser);

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(newRoles);
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepo.save(user);

        log.debug("IN register - user: {} successfully registered", registeredUser);
        return registeredUser;
    }
    
    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);
        log.warn("IN delete - user with id {} successfully deleted", id);
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepo.findByEmail(email);
        log.debug("IN findByEmail - user: {} found by email: {}", user, email);
        return user;
    }

    @Override
    public User forAuthenticationUser(String param) throws AuthenticationException {
        User user = userRepo.findByUsername(param);

        return user != null ? user : userRepo.findByEmail(param);
    }
}
