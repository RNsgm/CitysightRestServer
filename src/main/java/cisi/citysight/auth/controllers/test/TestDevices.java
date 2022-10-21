package cisi.citysight.auth.controllers.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/test")
@Slf4j
public class TestDevices {

    @Data
    class Test{
        private String message;

        public Test(String message) {
            this.message = message;
        }
        
    }
}
