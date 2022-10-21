package cisi.citysight.auth.controllers.content;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import cisi.citysight.auth.models.*;
import cisi.citysight.auth.repository.TagRepository;
import cisi.citysight.auth.response_models.Favorited;
import cisi.citysight.auth.response_models.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cisi.citysight.auth.enums.ContentAllowed;
import cisi.citysight.auth.enums.ContentType;
import cisi.citysight.auth.repository.ContentRepository;
import cisi.citysight.auth.security.jwt.JwtUser;
import liquibase.pro.packaged.in;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/content")
@Slf4j
public class GetContent {

    int MAIN_SIZE = 5;
    int NEWS_SIZE = 9;
    int EVENTS_SIZE = 5;
    int PLACES_SIZE = 5;
    int PROMO_SIZE = 9;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private TagRepository tagRepository;

    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/all/geopoint")
    public ResponseEntity getAllGeopoint(){
        List<GeopointOnMap> markers = contentRepository.findAll().stream()
                                                        .filter(x -> x.getType() != ContentType.ROUTE && x.getAllow() == ContentAllowed.ALLOW.value())
                                                        .map(x -> new GeopointOnMap(x.getId(), x.getCriterion().getId(), x.getGeodata()))
                                                        .collect(Collectors.toList());
        return ResponseEntity.ok(markers);
    }

    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/get/{id}")
    public ResponseEntity getContentById(
        @AuthenticationPrincipal JwtUser user,
        @PathVariable("id") long id
    ){
        Content content = contentRepository.findById(id).orElseThrow();

        if(user != null){
            if(user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_EDITOR))){
                return ResponseEntity.ok(ContentDto.fromContent(content));
            }
            if(!user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_EDITOR)) && content.getAllow() != ContentAllowed.ALLOW.value()){
                return ResponseEntity.badRequest().body(new ResponseError("Access Denied"));
            }
        }
        return ResponseEntity.ok(LiteContentDto.fromContent(content));
    }
    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/is_liked/{id}")
    public ResponseEntity getIsLiked(
            @AuthenticationPrincipal JwtUser user,
            @PathVariable("id") long id
    ){
        Content content = contentRepository.findById(id).orElseThrow();

        if(user != null){
            return ResponseEntity.ok(Favorited.fromContent(user.getId(), content));
        }
        return ResponseEntity.ok(new Favorited(false, 0));
    }


//    @CrossOrigin(origins = "https://citysight.ru")
//    @PostMapping("/get/{id}")
//    public ResponseEntity getContentById(
//            @PathVariable("id") long id
//    ){
//        ContentDto content = ContentDto.fromContent(contentRepository.findById(id).orElseThrow());
//        if(content.getAllow() != ContentAllowed.ALLOW.value()){
//            return ResponseEntity.badRequest().body(new ResponseError("Access Denied"));
//        }
//        return ResponseEntity.ok(content);
//    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/show/full/page/{num_page}")
    public ResponseEntity getContentOnPage(
        @AuthenticationPrincipal JwtUser user,
        @PathVariable("num_page") int page
    ){
        int startElement = page * 10;
        int endElement = startElement + 10;
        List<ContentDto> contentList = contentRepository.findAll().stream().map( x -> ContentDto.fromContent(x)).toList();

        if(contentList.size() > 1){
            List<ContentDto> reverse = new ArrayList<>(contentList);
            Collections.reverse(reverse);
            contentList = reverse;
        }

        return ResponseEntity.ok(contentList.subList(startElement, endElement));
    }
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/search/all/{query}/{num_page}")
    public ResponseEntity getContentOnSearch(
        @AuthenticationPrincipal JwtUser user,
        @PathVariable("query") String query,
        @PathVariable("num_page") int page
    ){
        List<ContentDto> contentList = contentRepository.findAll().stream().map( x -> ContentDto.fromContent(x)).toList();
        
        if(contentList.size() > 2){
            contentList = reversedView(contentList);
        }
        
        if(query != ""){
            contentList = contentList.stream().filter(x -> x.getTitle().toLowerCase().contains(query.toLowerCase())).toList();
        }
        int startElement = page * 10;
        int endElement = (startElement + 10) >= contentList.size() ? contentList.size() : startElement + 10;
        
        return ResponseEntity.ok(contentList.subList(startElement, endElement));
    }
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/search/all/{type}/{query}/{num_page}")
    public ResponseEntity getContentOnSearchWithType(
        @AuthenticationPrincipal JwtUser user,
        @PathVariable("query") String query,
        @PathVariable("type") ContentType type,
        @PathVariable("num_page") int page
    ){
        List<ContentDto> contentList = contentRepository.findAll().stream().filter(x -> x.getType() == type).map( x -> ContentDto.fromContent(x)).toList();
        
        if(contentList.size() > 2){
            contentList = reversedView(contentList);
        }
        
        if(query != ""){
            contentList = contentList.stream().filter(x -> x.getTitle().toLowerCase().contains(query.toLowerCase())).toList();
        }
        int startElement = page * 10;
        int endElement = (startElement + 10) >= contentList.size() ? contentList.size() : startElement + 10;
        
        return ResponseEntity.ok(contentList.subList(startElement, endElement));
    }



    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/show/page/{num_page}")
    public ResponseEntity getAllowedContentOnPage(
        @AuthenticationPrincipal JwtUser user,
        @PathVariable("num_page") int page
    ){
        int startElement = page * 10;
        int endElement = startElement + 10;
        List<ContentDto> contentList = contentRepository.findContentByAllowed(ContentAllowed.ALLOW.value()).stream().map( x -> ContentDto.fromContent(x)).toList();

        if(contentList.size() > 1){
            List<ContentDto> reverse = new ArrayList<>(contentList);
            Collections.reverse(reverse);
            contentList = reverse;
        }

        return ResponseEntity.ok(contentList.subList(startElement, endElement));
    }
    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/search/{query}/{num_page}")
    public ResponseEntity getAllowedContentOnSearch(
        @AuthenticationPrincipal JwtUser user,
        @PathVariable("query") String query,
        @PathVariable("num_page") int page
    ){
        List<ContentDto> contentList = contentRepository.findContentByAllowed(ContentAllowed.ALLOW.value()).stream().map( x -> ContentDto.fromContent(x)).toList();
        
        if(contentList.size() > 2){
            contentList = reversedView(contentList);
        }
        
        if(query != ""){
            contentList = contentList.stream().filter(x -> x.getTitle().toLowerCase().contains(query.toLowerCase())).toList();
        }
        int startElement = page * 10;
        int endElement = (startElement + 10) >= contentList.size() ? contentList.size() : startElement + 10;
        
        return ResponseEntity.ok(contentList.subList(startElement, endElement));
    }
    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/search/{type}/{query}/{num_page}")
    public ResponseEntity getAllowedContentOnSearchWithType(
        @AuthenticationPrincipal JwtUser user,
        @PathVariable("query") String query,
        @PathVariable("type") ContentType type,
        @PathVariable("num_page") int page
    ){
        List<ContentDto> contentList = contentRepository.findContentByAllowed(ContentAllowed.ALLOW.value()).stream().filter(x -> x.getType() == type).map( x -> ContentDto.fromContent(x)).toList();
        
        if(contentList.size() > 2){
            contentList = reversedView(contentList);
        }
        
        if(query != ""){
            contentList = contentList.stream().filter(x -> x.getTitle().toLowerCase().contains(query.toLowerCase())).toList();
        }
        int startElement = page * 10;
        int endElement = (startElement + 10) >= contentList.size() ? contentList.size() : startElement + 10;
        
        return ResponseEntity.ok(contentList.subList(startElement, endElement));
    }

    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/feeds")
    public ResponseEntity getFeedContents(){
        Map<String, List<Long>> feeds = new HashMap<>();
        List<Content> contents = contentRepository.findContentByAllowed(ContentAllowed.ALLOW.value());
        Collections.reverse(contents);
        Tag tagMainFeed = tagRepository.findById(104L).orElseThrow();

        List<Long> ids = contents.stream().filter(content -> content.getTags().contains(tagMainFeed)).map(BaseEntity::getId).toList();

        feeds.put("main", contents.stream().filter(content -> content.getTags().contains(tagMainFeed)).map(BaseEntity::getId).limit(MAIN_SIZE).toList());
        feeds.put("news", contents.stream().filter(content -> content.getType() == ContentType.ARTICLE).map(BaseEntity::getId).limit(NEWS_SIZE).toList());
        feeds.put("events", contents.stream().filter(content -> content.getType() == ContentType.EVENT).map(BaseEntity::getId).limit(EVENTS_SIZE).toList());
        feeds.put("places", contents.stream().filter(content -> content.getType() == ContentType.PLACE || content.getType() == ContentType.ORGANIZATION || content.getType() == ContentType.ROUTE).map(BaseEntity::getId).limit(PLACES_SIZE).toList());
//        feeds.put("promo", contents.stream().filter(content -> content.getTags().contains(tagMainFeed)).map(BaseEntity::getId).toList());

        return ResponseEntity.ok(feeds);
    }

    private static <T> List<T> reversedView(final List<T> list)
    {
        return new AbstractList<T>()
        {
            @Override
            public T get(int index)
            {
                return list.get(list.size()-1-index);
            }

            @Override
            public int size()
            {
                return list.size();
            }
        };
    }
}
