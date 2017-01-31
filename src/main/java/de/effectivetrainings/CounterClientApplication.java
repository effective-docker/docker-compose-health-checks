package de.effectivetrainings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RestController
@Import(CounterClientApplication.RestConfiguration.class)
public class CounterClientApplication {

    @Autowired
    private RestTemplate restTemplateBuilder;
    @Value("${server.url:none}")
    private String url;
    @Value("${application.server}")
    private boolean serverMode;

    private int cnt = 0;

    public static void main(String[] args) {
        SpringApplication.run(CounterClientApplication.class, args);
    }

    @RequestMapping(value = "/counter", produces = "application/json")
    public Integer counter() {
        if (serverMode) {
            cnt++;
            return cnt;
        } else {
            return restTemplateBuilder
                    .exchange(url, HttpMethod.GET, new HttpEntity<Object>(new HttpHeaders()), Integer.class)
                    .getBody();
        }
    }

    @Configuration
    public static class RestConfiguration {

        @Autowired
        private RestTemplateBuilder restTemplateBuilder;
        @Value("${application.server}")
        private boolean serverMode;

        @Bean
        public RestTemplate restTemplate() {
            return restTemplateBuilder.build();
        }

        @PostConstruct
        public void waitIfServer() {
            if (serverMode) {
                try {
                    Thread.sleep(1000*60);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
