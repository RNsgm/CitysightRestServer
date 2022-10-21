package cisi.citysight.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cisi.citysight.auth.models.Criteria;

public interface CriteriaRepository extends JpaRepository<Criteria, Integer>{
    
    @Query("SELECT criteria FROM Criteria criteria WHERE criteria.type_article = true AND criteria.id <> 1")
    List<Criteria> findForArticle();
    
    @Query("SELECT criteria FROM Criteria criteria WHERE criteria.type_route = true AND criteria.id <> 1")
    List<Criteria> findForRoute();

    @Query("SELECT criteria FROM Criteria criteria WHERE criteria.type_event = true AND criteria.id <> 1")
    List<Criteria> findForEvent();

    @Query("SELECT criteria FROM Criteria criteria WHERE criteria.type_organization = true AND criteria.id <> 1")
    List<Criteria> findForOrganization();
}
