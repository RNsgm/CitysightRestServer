package cisi.citysight.auth.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "criterions")
@Data
public class Criteria{
        @Id
        @GeneratedValue
        private int id;
        private String name_ru;
        private String name_en;
        private String service_name;
        private boolean type_article;
        private boolean type_event;
        private boolean type_organization;
        private boolean type_route;
        private boolean active;
}
