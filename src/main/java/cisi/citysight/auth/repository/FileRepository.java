package cisi.citysight.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cisi.citysight.auth.models.FileStorage;

public interface FileRepository extends JpaRepository<FileStorage, String> {
    
}
