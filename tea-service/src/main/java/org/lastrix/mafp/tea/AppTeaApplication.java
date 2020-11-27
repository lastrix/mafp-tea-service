package org.lastrix.mafp.tea;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@EntityScan
@EnableAspectJAutoProxy
public class AppTeaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AppTeaApplication.class, args);
    }

    @Override
    public void run(String... args) {

    }

}
