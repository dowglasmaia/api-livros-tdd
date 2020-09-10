package org.maia.livro;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling // agendamento de tarefas
public class ApiLivrosApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	/* * Criando Cronograma para agendamentos de tarefas
	 * 	http://www.cronmaker.com/?1
	 * */
	@Scheduled(cron = "0 52 11 1/1 * ?") // segundo | minuto | hora | dia | mes | ano
	public void testAgedamentoTarefas(){
		System.out.println("Agendamento de tarefas com sucesso");
	}

    public static void main(String[] args) {
        SpringApplication.run(ApiLivrosApplication.class, args);
    }

}
