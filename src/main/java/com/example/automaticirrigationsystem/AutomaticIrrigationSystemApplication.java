package com.example.automaticirrigationsystem;

import com.example.automaticirrigationsystem.repository.CustomRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
public class AutomaticIrrigationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutomaticIrrigationSystemApplication.class, args);
    }

}
