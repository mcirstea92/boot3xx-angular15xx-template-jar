package ro.mariuscirstea.template.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ro.mariuscirstea.template.filters.MDCFilter;

@Configuration
@Slf4j
public class SpringConfig {

    @Bean
    public MDCFilter mdcFilter() {
        return new MDCFilter();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapperWithNonNulls = new ObjectMapper();
        mapperWithNonNulls.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapperWithNonNulls;
    }

}

