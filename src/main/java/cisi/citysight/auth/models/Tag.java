package cisi.citysight.auth.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "tags")
@Data
public class Tag{
    @Id
    @GeneratedValue
    private long id;
    private String name_ru;
    private String name_en;
    private boolean type_article;
    private boolean type_event;
    private boolean type_organization;
    private boolean type_route;

    private boolean common;
    private boolean route;
    private boolean music;
    private boolean rest;
    private boolean meal;
    private boolean art;
    private boolean entertainments;
    private boolean sport;
    private boolean nature;
    private boolean education;
    private boolean interesting_place;
    private boolean health;
    private boolean shopping;
    private boolean rendezvous;
    private boolean walk;
    private boolean tour;
    private boolean city_trip;
    private boolean sporting;

}
