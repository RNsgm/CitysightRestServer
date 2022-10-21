package cisi.citysight.auth.models;

import lombok.Data;

@Data
public class TagDto{
    private long id;
    private String name_ru;
    private String name_en;
    
        
    public TagDto(long id, String name_ru, String name_en) {
        this.id = id;
        this.name_ru = name_ru;
        this.name_en = name_en;
    }

    public static TagDto fromTag(Tag tag){
        return new TagDto(
            tag.getId(),
            tag.getName_ru(),
            tag.getName_en()
        );
    }
}
