package cisi.citysight.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cisi.citysight.auth.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
