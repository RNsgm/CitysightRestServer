package cisi.citysight.auth.models;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

import cisi.citysight.auth.enums.ContentType;
import cisi.citysight.auth.enums.Status;
import lombok.Data;

@Data
public class ContentDto extends BaseEntity{

    private Long id;

    private ContentType type;

    private String title;

    private long author;

    private String data;

    private String markerImage;

    private String geodata;

    private Criteria criterion;

    private int allow;

    private long moderator;

    private Timestamp timeStart;
    
    private Timestamp timeEnd;

    private Set<Long> tags;

    private int likes;

    private Date created;

    private Date updated;

    private Status status;

    public ContentDto(Long id, ContentType type, String title, long author, String data, String markerImage,
            String geodata, Criteria criterion, int allow, long moderator, Timestamp timeStart, Timestamp timeEnd,
            Set<Tag> tags, Set<User> likes, Date created, Date updated, Status status) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.author = author;
        this.data = data;
        this.markerImage = markerImage;
        this.geodata = geodata;
        this.criterion = criterion;
        this.allow = allow;
        this.moderator = moderator;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.tags = tags.stream().map(x -> x.getId()).collect(Collectors.toSet());;
        this.likes = likes.size();
        this.created = created;
        this.updated = updated;
        this.status = status;
    }



    public static ContentDto fromContent(Content content){

        User moderator = content.getModerator();
        return new ContentDto(
            content.getId(),
            content.getType(),
            content.getTitle(),
            content.getAuthor().getId(),
            content.getData(),
            content.getMarkerImage(),
            content.getGeodata(),
            content.getCriterion(),
            content.getAllow(),
            content.getModerator() != null ? content.getModerator().getId() : 0,
            content.getTimeStart(),
            content.getTimeEnd(),
            content.getTags(),
            content.getLikes(),
            content.getCreated(),
            content.getUpdated(),
            content.getStatus()
        );
    }
}
