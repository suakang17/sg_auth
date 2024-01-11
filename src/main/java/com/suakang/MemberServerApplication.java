package com.suakang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MemberServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberServerApplication.class, args);
    }

}
