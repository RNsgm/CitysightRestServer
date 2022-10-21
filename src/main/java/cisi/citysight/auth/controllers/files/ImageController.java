package cisi.citysight.auth.controllers.files;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cisi.citysight.auth.enums.Status;
import cisi.citysight.auth.models.FileStorage;
import cisi.citysight.auth.models.User;
import cisi.citysight.auth.repository.FileRepository;
import cisi.citysight.auth.repository.UserRepository;
import cisi.citysight.auth.response_models.ImageName;
import cisi.citysight.auth.security.jwt.JwtUser;
import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/images")
@Slf4j
public class ImageController {

    @Value("${upload.path}")
    private String uploadPath;

    private final int WIDTH_SQUARE = 1080;
    private final int HEIGHT_SQUARE = 1080;
    
    private final int WIDTH_RECTANGLE = 1080;
    private final int HEIGHT_RECTANGLE = 1890;
    
    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserRepository userRepository;

    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("name")
    public ResponseEntity imageName(@RequestParam("id") String id){

        try{
            FileStorage file = fileRepository.findById(id).orElseThrow();
            return ResponseEntity.ok(new ImageName(file.getId(), file.getName()));
        }catch(NoSuchElementException e){
            log.error("FileNotFound");
            return ResponseEntity.ok(new ImageName("", ""));
        }
    }

    @CrossOrigin(origins = "https://citysight.ru")
    @PostMapping("upload")
    
    public ResponseEntity uploadImage(
        @AuthenticationPrincipal JwtUser user,
        @RequestParam(name = "image_s") MultipartFile fileS,
        @RequestParam(name = "image_r") MultipartFile fileR,
        @RequestParam(name = "image_name") String image_name,
        @RequestParam(name = "image_source") String image_source
    ) throws java.io.IOException{

        String type = "jpg";
        String uuidFilename = UUID.randomUUID().toString();
        
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()) uploadDir.mkdirs();

        if(!productionAndSaveImage(fileS, uuidFilename, type, ".s", WIDTH_SQUARE, HEIGHT_SQUARE)) return ResponseEntity.ok(new ImageName("", ""));
        if(!productionAndSaveImage(fileR, uuidFilename, type, ".r", WIDTH_RECTANGLE, HEIGHT_RECTANGLE)) return ResponseEntity.ok(new ImageName("", ""));

        FileStorage file = new FileStorage();
        file.setId(uuidFilename);
        file.setName(image_name);
        file.setSource(image_source);
        file.setUser_id(userRepository.findById(user.getId()).orElseThrow());
        file.setCreated(new Date(new java.util.Date().getTime()));
        file.setUpdated(new Date(new java.util.Date().getTime()));
        file.setStatus(Status.ACTIVE);
        FileStorage saved = fileRepository.save(file);

        return ResponseEntity.ok(new ImageName(saved.getId(), saved.getName()));
    }

    boolean productionAndSaveImage(MultipartFile file, String uuidFilename, String type, String prefix, int width, int height) throws java.io.IOException{
        BufferedImage image = ImageIO.read(file.getInputStream());
        BufferedImage editableImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            
        Graphics2D graphics2D = editableImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );
        graphics2D.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );
        graphics2D.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        graphics2D.drawImage(
            image,
            0,
            0,
            width,
            height,
            null
        );
        graphics2D.dispose();
        File uploadFile = new File(uploadPath + "/" + uuidFilename + prefix + "." + type);
        boolean saved = ImageIO.write(editableImage, type, uploadFile);
        return saved;
    }

}
