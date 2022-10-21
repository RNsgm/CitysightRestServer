package cisi.citysight.auth.request_models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cisi.citysight.auth.enums.ContentType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentSave {
    // long id;
    ContentType type;
    String title;
    String image;
    String data;
    String geojson;
    Long criteria;
    List<Long> tags;
}
