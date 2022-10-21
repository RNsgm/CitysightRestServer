package cisi.citysight.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cisi.citysight.auth.models.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {
    @Query("SELECT content FROM Content content WHERE content.allow = :allowed")
    List<Content> findContentByAllowed(int allowed);
}
