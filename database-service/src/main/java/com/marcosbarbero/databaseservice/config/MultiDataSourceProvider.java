package com.marcosbarbero.databaseservice.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

import static java.lang.String.format;

@Slf4j
public class MultiDataSourceProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private final RestTemplate restTemplate;

    public MultiDataSourceProvider(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return datasource();
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return datasource();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Credentials {
        private String hostname;
        private int port;
        private String username;
        private String password;
        private String schema;

        @Override
        public String toString() {
            return "Credentials{" +
                    "hostname='" + hostname + '\'' +
                    ", port=" + port +
                    ", username='" + username + '\'' +
                    ", schema='" + schema + '\'' +
                    '}';
        }
    }

    private Credentials getCredentials() {
        ResponseEntity<Credentials> response = restTemplate.getForEntity("http://localhost:8080/credentials", Credentials.class);
        return response.getBody();
    }

    private String getUrl(Credentials credentials) {
        String connection = "jdbc:mysql://%s:%s/%s";
        return format(connection, credentials.getHostname(), credentials.getPort(), credentials.getSchema());
    }

    private DataSource datasource() {
        Credentials credentials = getCredentials();

        log.info("New credentials: {}", credentials);

        return DataSourceBuilder.create()
                .username(credentials.getUsername())
                .password(credentials.getPassword())
                .url(getUrl(credentials))
                .driverClassName("com.mysql.jdbc.Driver")
                .build();
    }
}
