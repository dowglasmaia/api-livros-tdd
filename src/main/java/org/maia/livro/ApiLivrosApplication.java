package org.maia.livro;

import org.maia.livro.services.interfaces.EmailServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ApiLivrosApplication {


    public static void main(String[] args) {
        SpringApplication.run(ApiLivrosApplication.class, args);
    }

}
