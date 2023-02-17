package ro.mariuscirstea.template.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
public class FileUtils {

    private static final String GENERIC_ERROR = "<body><h2>Generic error</h2><p>An error occurred, check the X-CorrelationId header with the administrator</p></body>";

    public static String getResourceFileAsString(String fileName) {
        Resource resource = new ClassPathResource(fileName);
        if (resource.exists()) {
            try (InputStream is = resource.getInputStream()) {
                try (InputStreamReader isr = new InputStreamReader(is);
                     BufferedReader reader = new BufferedReader(isr)) {
                    return reader.lines().collect(Collectors.joining(System.lineSeparator()));
                }
            } catch (IOException e) {
                return GENERIC_ERROR;
            }
        } else {
            log.warn("Resource {} doesn't exist!", fileName);
            return GENERIC_ERROR;
        }
    }

}
