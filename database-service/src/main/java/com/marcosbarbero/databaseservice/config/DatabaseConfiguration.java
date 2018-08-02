package com.marcosbarbero.databaseservice.config;

import com.marcosbarbero.databaseservice.Avenger;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableJpaRepositories("com.marcosbarbero.databaseservice")
@EnableTransactionManagement(proxyTargetClass = true)
public class DatabaseConfiguration {

    @Bean
    public MultiTenantConnectionProvider multiTenantConnectionProvider(final RestTemplate restTemplate) {
        return new DynamicDataSourceConnectionProvider(restTemplate);
    }

    @Bean
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new DefaultCurrentTenantIdentifierResolver();
    }

    @Primary
    @Bean
    @PersistenceContext
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(MultiTenantConnectionProvider multiTenantConnectionProvider,
                                                                           CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

        Map<String, Object> hibernateProps = new LinkedHashMap<>();
        hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

        // No dataSource is set to resulting entityManagerFactoryBean
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan(Avenger.class.getPackage().getName());
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(hibernateProps);

        return em;
    }


    @Bean
    public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return entityManagerFactoryBean.getObject();
    }


    @Bean(name = "transactionManager")
    public PlatformTransactionManager txManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpa = new JpaTransactionManager();
        jpa.setEntityManagerFactory(entityManagerFactory);
        return jpa;
    }

}
