package cisi.citysight.auth.models;

import lombok.Data;

@Data
public class GeopointOnMap {
    private Long id;
    private int criteria;
    private String geodata;

    public GeopointOnMap(Long id, int criteria, String geodata) {
        this.id = id;
        this.criteria = criteria;
        this.geodata = geodata;
    }
}
