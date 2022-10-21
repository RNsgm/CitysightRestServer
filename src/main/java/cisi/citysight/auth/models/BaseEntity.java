package cisi.citysight.auth.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import cisi.citysight.auth.enums.Status;
import lombok.Data;

@MappedSuperclass
@Data
public class BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @CreatedDate
    @Column(name = "created")
    protected Date created;

    @LastModifiedDate
    @Column(name = "updated")
    protected Date updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    protected Status status;
    
}
