package org.maia.livro;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiLivrosApplication {


    public static void main(String[] args) {
        SpringApplication.run(ApiLivrosApplication.class, args);
    }

}
