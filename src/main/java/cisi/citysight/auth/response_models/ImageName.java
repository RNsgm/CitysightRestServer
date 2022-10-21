package cisi.citysight.auth.response_models;

import lombok.Data;

@Data
public class ImageName {
    private String id;
    private String name;

    public ImageName(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
