package ro.mariuscirstea.template;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class PasswordEncoderTest extends BasicTest{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncodePassword() {
        String encoded = passwordEncoder.encode("test_password#");
        log.info("Encoded password: {}", encoded);
        Assert.notNull(encoded, "Password should be not null");
        boolean decodedPass = passwordEncoder.matches("test_password#", encoded);
        Assert.isTrue(decodedPass, "Password should match!");
    }

}
