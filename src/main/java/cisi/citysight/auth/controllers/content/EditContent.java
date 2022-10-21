package cisi.citysight.auth.controllers.content;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cisi.citysight.auth.models.ContentDto;
import cisi.citysight.auth.models.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cisi.citysight.auth.enums.ContentType;
import cisi.citysight.auth.enums.Status;
import cisi.citysight.auth.models.Content;
import cisi.citysight.auth.models.Tag;
import cisi.citysight.auth.repository.ContentRepository;
import cisi.citysight.auth.repository.CriteriaRepository;
import cisi.citysight.auth.repository.TagRepository;
import cisi.citysight.auth.repository.UserRepository;
import cisi.citysight.auth.security.jwt.JwtUser;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/content")
@Slf4j
public class EditContent {

    @Autowired
    private ContentRepository repository;

    @Autowired
    private CriteriaRepository criteriaRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;
    
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/save")
    public ResponseEntity getSaveContent(
        @AuthenticationPrincipal JwtUser user,
        @RequestParam("id") long c_id,
        @RequestParam("type") ContentType type,
        @RequestParam("title") String title,
        @RequestParam("image") String image,
        @RequestParam("data") String data,
        @RequestParam("geojson") String geojson,
        @RequestParam("time_start") String timeStart,
        @RequestParam("time_end") String timeEnd,
        @RequestParam("criteria") int criteria,
        @RequestParam("tags") List<Long> tags
    ){
        Timestamp timestampStart = null;
        Timestamp timestampEnd = null;

        log.error("ID: "+c_id);
        log.error("Type: "+type.name());
        log.error("Title: "+title);
        log.error("Image: "+image);
        log.error("Data: "+data);
        log.error("Geojson: "+geojson);
        log.error("Crit: "+criteria);
        log.error("Start: "+timeStart);
        log.error("End: "+timeEnd);
        log.error("Tags: "+tags);

        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try{
            if(!timeStart.isEmpty()){
                Instant startTimeInstant = LocalDateTime.parse(timeStart, formatDateTime).atZone(ZoneId.of("UTC")).toInstant();
                timestampStart = Timestamp.from(startTimeInstant);
            }
            if(!timeEnd.isEmpty()){
                Instant endTimeInstant = LocalDateTime.parse(timeEnd, formatDateTime).atZone(ZoneId.of("UTC")).toInstant();
                timestampEnd = Timestamp.from(endTimeInstant);
            }
        }catch(IllegalArgumentException er){
            log.error(er.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
        Content saved = null;
        if(c_id != 0){
            Content content = repository.findById(c_id).orElseThrow();
    
            Set<Tag> tagList = new HashSet<Tag>();
            for(Long tag : tags){
                tagList.add(tagRepository.findById(tag).orElseThrow());
            }
            tagList.forEach(x -> log.error(x.getName_ru()));

            log.error(""+content.getId());
            content.setTitle(title);
            content.setMarkerImage(image+".s.jpg");
            content.setData(data);
            content.setGeodata(geojson);
            content.setCriterion(criteriaRepository.findById(criteria).orElseThrow());
            content.setTags(tagList);
            content.setUpdated(new Date(new java.util.Date().getTime()));

            content.setTimeStart(timestampStart);
            content.setTimeEnd(timestampEnd);

            saved = repository.save(content);
        }else{
            Content content = new Content();
            Set<Tag> tagList = new HashSet<Tag>();
            for(Long tag : tags){
                tagList.add(tagRepository.findById(tag).orElseThrow());
            }
            tagList.forEach(x -> log.error(x.getName_ru()));

            content.setType(type);
            content.setTitle(title);
            content.setAuthor(userRepository.findById(user.getId()).orElseThrow());
            content.setData(data);
            content.setMarkerImage(image+".s.jpg");
            content.setGeodata(geojson);
            content.setCriterion(criteriaRepository.findById(criteria).orElseThrow());
            content.setAllow(1);
            content.setModerator(null);
            content.setTags(tagList);
            content.setCreated(new Date(new java.util.Date().getTime()));
            content.setUpdated(new Date(new java.util.Date().getTime()));

            content.setTimeStart(timestampStart);
            content.setTimeEnd(timestampEnd);
            
            content.setStatus(Status.ACTIVE);

            saved = repository.save(content);
        }
        return ResponseEntity.ok(saved != null);
    }

    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/like/{id}")
    public ResponseEntity<IsLikedMeta> like(
        @AuthenticationPrincipal JwtUser jwtUser,
        @PathVariable("id") long c_id
    ){

        Content content = repository.findById(c_id).orElseThrow();
        if(jwtUser == null) return ResponseEntity.ok(new IsLikedMeta(false, content.getLikes().size()));

        User user = userRepository.findById(jwtUser.getId()).orElseThrow();

        if(content.getLikes().contains(user)){
            content.getLikes().remove(user);
        }else{
            content.getLikes().add(user);
        }

        Content saved = repository.save(content);
        return ResponseEntity.ok(new IsLikedMeta(saved.getLikes().contains(user), saved.getLikes().size()));
    }
    @Data
    class IsLikedMeta{
        boolean isLiked;
        int count;

        public IsLikedMeta(boolean isLiked, int count) {
            this.isLiked = isLiked;
            this.count = count;
        }
    }

}

