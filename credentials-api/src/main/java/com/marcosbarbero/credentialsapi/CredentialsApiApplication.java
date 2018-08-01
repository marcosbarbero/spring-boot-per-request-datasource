package com.marcosbarbero.credentialsapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class CredentialsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CredentialsApiApplication.class, args);
	}

	@Getter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
	class Credentials {
		private String hostname;
		private int port;
		private String username;
		private String password;
		private String schema;
	}

	@RestController
	class CredentialsController {

	    @GetMapping("/credentials")
        public ResponseEntity<Credentials> get() {
	        return ResponseEntity.ok(new Credentials("localhost", 3306, "root", null, "sample"));
        }
    }
}
