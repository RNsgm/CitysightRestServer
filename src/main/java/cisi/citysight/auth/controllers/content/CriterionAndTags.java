package cisi.citysight.auth.controllers.content;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cisi.citysight.auth.enums.ContentType;
import cisi.citysight.auth.models.Criteria;
import cisi.citysight.auth.models.Tag;
import cisi.citysight.auth.models.TagDto;
import cisi.citysight.auth.repository.ContentRepository;
import cisi.citysight.auth.repository.CriteriaRepository;
import cisi.citysight.auth.repository.TagRepository;
import cisi.citysight.auth.security.jwt.JwtUser;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/values")
@Slf4j
public class CriterionAndTags {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Autowired
    EntityManager em;

    String critField[] = {
        "common",
        "route",
        "music",
        "rest",
        "meal",
        "art",
        "entertainments",
        "sport",
        "nature",
        "education",
        "interesting_place",
        "health",
        "shopping",
        "rendezvous",
        "walk",
        "tour",
        "city_trip",
        "sporting"
    };

    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/criterions")
    public ResponseEntity getCriterionByType(
        @RequestParam() ContentType type
    ){
        switch(type){
            case ARTICLE:
                return ResponseEntity.ok(criteriaRepository.findForArticle());
            case EVENT:
                return ResponseEntity.ok(criteriaRepository.findForEvent());
            case ROUTE:
                return ResponseEntity.ok(criteriaRepository.findForRoute());
            case PLACE:
            case ORGANIZATION:
                return ResponseEntity.ok(criteriaRepository.findForOrganization());
            default:
                return ResponseEntity.ok(null);
        }
    }

    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("/tag/{id}")
    public ResponseEntity getTags(
        @AuthenticationPrincipal JwtUser user,
        @PathVariable("id") int id
    ){
        if(id == 0) ResponseEntity.ok(new ArrayList());
        
        List<TagDto> tags = getTags(id).stream().map((x) -> TagDto.fromTag(x)).toList();
        log.info("Tags: "+tags.size());

        return ResponseEntity.ok(tags); 
    }

    public List<Tag> getTags(int tag) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);

        Root<Tag> root = cq.from(Tag.class);
        List<Predicate> predicates = new ArrayList<>();

        Predicate common = cb.equal(root.get("common"), true);
        Predicate second = cb.equal(root.get(critField[tag - 1]), true);
        predicates.add(cb.or(common, second));

        // Predicate route = cb.equal(root.get("route"), criteria[1]);
        // Predicate music = cb.equal(root.get("music"), criteria[2]);
        // Predicate rest = cb.equal(root.get("rest"), criteria[3]);
        // Predicate meal = cb.equal(root.get("meal"), criteria[4]);
        // Predicate art = cb.equal(root.get("art"), criteria[5]);
        // Predicate entertainments = cb.equal(root.get("entertainments"), criteria[6]);
        // Predicate sport = cb.equal(root.get("sport"), criteria[7]);
        // Predicate nature = cb.equal(root.get("nature"), criteria[8]);
        // Predicate education = cb.equal(root.get("education"), criteria[9]);
        // Predicate interesting_place = cb.equal(root.get("interesting_place"), criteria[10]);
        // Predicate health = cb.equal(root.get("health"), criteria[11]);
        // Predicate shopping = cb.equal(root.get("shopping"), criteria[12]);
        // Predicate rendezvous = cb.equal(root.get("rendezvous"), criteria[13]);
        // Predicate walk = cb.equal(root.get("walk"), criteria[14]);
        // Predicate tour = cb.equal(root.get("tour"), criteria[15]);
        // Predicate city_trip = cb.equal(root.get("city_trip"), criteria[16]);
        // Predicate sporting = cb.equal(root.get("sporting"), criteria[17]);

        // predicates.add(cb.or(
        //     common,
        //     route,
        //     music,
        //     rest,
        //     meal,
        //     art,
        //     entertainments,
        //     sport,
        //     nature,
        //     education,
        //     interesting_place,
        //     health,
        //     shopping,
        //     rendezvous,
        //     walk,
        //     tour,
        //     city_trip,
        //     sporting
        // ));

        cq.where(cb.or(predicates.toArray(new Predicate[0])));
        return em.createQuery(cq).getResultList();
    }
}
