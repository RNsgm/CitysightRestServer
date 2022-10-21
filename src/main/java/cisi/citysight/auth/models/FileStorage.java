package cisi.citysight.auth.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "file_storage")
@Data
public class FileStorage extends BaseEntityTypeString {
    @Column(name = "id", length = 255)
    private String id;
    @Column(name = "name", length = 2048)
    private String name;
    @Column(name = "source", length = 2048)
    private String source;
    @ManyToOne
    @JoinColumn(
        name = "user_id"
    )
    private User user_id;
}
