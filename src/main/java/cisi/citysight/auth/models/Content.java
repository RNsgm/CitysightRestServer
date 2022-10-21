package cisi.citysight.auth.models;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import cisi.citysight.auth.enums.ContentType;
import cisi.citysight.auth.enums.Status;
import lombok.Data;

@Entity
@Table(name = "contents")
@Data
public class Content extends BaseEntity{
    
    @Column(name = "content_type", columnDefinition = "privatechar")
    @Enumerated(EnumType.STRING)
    private ContentType type;

    @Column(name = "content_title", columnDefinition = "privatechar")
    private String title;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "content_author")
    private User author;

    @Column(name = "content_data", columnDefinition = "text")
    private String data;

    @Column(name = "content_marker_image", columnDefinition = "privatechar")
    private String markerImage;

    @Column(name = "content_geodata", columnDefinition = "text")
    private String geodata;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "criterion_id")
    private Criteria criterion;

    @Column(name = "content_allow")
    private int allow;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "content_moderator")
    private User moderator;

    @Column(name = "time_start")
    private Timestamp timeStart;

    @Column(name = "time_end")
    private Timestamp timeEnd;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tags_to_content",
        joinColumns = {@JoinColumn(name = "content_id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<Tag> tags;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "content_likes",
        joinColumns = {@JoinColumn(name = "content_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> likes;
}
