package org.lapysh.service.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableCaching
@EnableRetry
@ComponentScan("org.lapysh")
@EntityScan("org.lapysh.service.account.data.entity")
@EnableJpaRepositories(basePackages = "org.lapysh.service.account.data.repository")
public class ServiceAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAccountApplication.class, args);
    }

}
