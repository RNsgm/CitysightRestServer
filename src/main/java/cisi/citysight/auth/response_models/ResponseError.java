package cisi.citysight.auth.response_models;

import lombok.Data;

@Data
public class ResponseError {
    private String message;

    public ResponseError(String message) {
        this.message = message;
    }
    
}
