package com.marcosbarbero.databaseservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@SpringBootApplication
public class DatabaseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    @RequiredArgsConstructor
    class AvengersController {

        private final AvengerRepository avengerRepository;

        @GetMapping("/avengers")
        public ResponseEntity<Collection<Avenger>> get() {
            return ResponseEntity.ok(this.avengerRepository.findAll());
        }

    }
}
