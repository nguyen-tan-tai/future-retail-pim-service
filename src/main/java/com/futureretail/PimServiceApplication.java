package com.futureretail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PimServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PimServiceApplication.class, args);
    }

}
