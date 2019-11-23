package com.github.daggerok.springbootautoconfigurations.demoapplication;

import com.github.daggerok.springbootautoconfigurations.demolibrary.HelloLibraryAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(HelloLibraryAutoConfiguration.class)
public class HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }
}
