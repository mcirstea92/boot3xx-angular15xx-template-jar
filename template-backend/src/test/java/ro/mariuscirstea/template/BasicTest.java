package ro.mariuscirstea.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.mariuscirstea.template.config.SpringConfig;
import ro.mariuscirstea.template.security.jwt.JwtProvider;
import ro.mariuscirstea.template.service.impl.UserServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TemplateApplication.class)
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@Slf4j
@ActiveProfiles("test")
public class BasicTest {

    @MockBean
    JwtProvider jwtProvider;

    @MockBean
    UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSuccessfullyAutowire() {
        Assert.assertNotNull("Bean should be auto-wired successfully!", passwordEncoder);
    }

}
