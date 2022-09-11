package org.crok4it.em.e2e;

import io.cucumber.spring.CucumberContextConfiguration;
import org.crok4it.em.EventManagementApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@AutoConfigureMockMvc
@ActiveProfiles({"e2e"})
@CucumberContextConfiguration

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {EventManagementApplication.class}
)
public class CucumberSpringConfig {
}
