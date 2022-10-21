package cisi.citysight.auth.models;

import cisi.citysight.auth.enums.ContentType;
import cisi.citysight.auth.enums.Status;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class LiteContentDto {

    private String title;

    private long author;

    private String data;

    private String markerImage;

    private String geodata;

    private Criteria criterion;

    private Timestamp timeStart;

    private Timestamp timeEnd;

    private Set<Long> tags;

    private int likes;

    public LiteContentDto(String title, long author, String data, String markerImage,
                      String geodata, Criteria criterion, Timestamp timeStart, Timestamp timeEnd,
                      Set<Tag> tags, Set<User> likes) {
        this.title = title;
        this.author = author;
        this.data = data;
        this.markerImage = markerImage;
        this.geodata = geodata;
        this.criterion = criterion;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.tags = tags.stream().map(x -> x.getId()).collect(Collectors.toSet());;
        this.likes = likes.size();
    }



    public static LiteContentDto fromContent(Content content){

        return new LiteContentDto(
                content.getTitle(),
                content.getAuthor().getId(),
                content.getData(),
                content.getMarkerImage(),
                content.getGeodata(),
                content.getCriterion(),
                content.getTimeStart(),
                content.getTimeEnd(),
                content.getTags(),
                content.getLikes()
        );
    }
}
