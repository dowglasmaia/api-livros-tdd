package org.maia.livro;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiLivrosApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

    public static void main(String[] args) {
        SpringApplication.run(ApiLivrosApplication.class, args);
    }

}
