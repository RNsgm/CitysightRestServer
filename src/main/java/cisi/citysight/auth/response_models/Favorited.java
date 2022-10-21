package cisi.citysight.auth.response_models;

import cisi.citysight.auth.models.BaseEntity;
import lombok.Data;
import cisi.citysight.auth.models.Content;

@Data
public class Favorited {
    boolean isFavorited;
    int count;

    public Favorited(boolean isFavorited, int count){
        this.isFavorited = isFavorited;
        this.count = count;
    }

    public static Favorited fromContent(Long userId, Content content){
        return new Favorited(
                content.getLikes().stream().map(BaseEntity::getId).toList().contains(userId),
                content.getLikes().size()
        );
    }
}
