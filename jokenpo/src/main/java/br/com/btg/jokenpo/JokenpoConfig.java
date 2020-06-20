package br.com.btg.jokenpo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class JokenpoConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
