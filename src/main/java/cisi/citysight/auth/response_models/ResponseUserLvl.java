package cisi.citysight.auth.response_models;

import cisi.citysight.auth.models.AccessLevel;
import lombok.Data;

@Data
public class ResponseUserLvl {
    private AccessLevel level;

    public ResponseUserLvl(AccessLevel level) {
        this.level = level;
    }
    
}
