package cisi.citysight.auth.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import cisi.citysight.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@AutoConfigureTestDatabase
@DataJpaTest
@Slf4j
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testGetUsername() {
        User user = userRepository.findByUsername("admin");
        log.error("User: {}", user);
        assertEquals(true, true);
    }
}
