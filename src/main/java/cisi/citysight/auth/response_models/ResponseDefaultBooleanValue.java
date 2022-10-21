package cisi.citysight.auth.response_models;

import lombok.Data;

@Data
public class ResponseDefaultBooleanValue {
    private boolean value;

    public ResponseDefaultBooleanValue(boolean value) {
        this.value = value;
    }
    
}
