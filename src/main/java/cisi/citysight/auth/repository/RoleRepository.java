package cisi.citysight.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cisi.citysight.auth.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    Role findByName(String name);
}
