package cisi.citysight.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cisi.citysight.auth.models.Tag;

public interface TagRepository extends JpaRepository<Tag, Long>{
    
}
