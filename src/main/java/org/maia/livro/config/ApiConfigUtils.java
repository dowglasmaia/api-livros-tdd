package org.maia.livro.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class ApiConfigUtils {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public CommandLineRunner commandLineRunner(){

        return  null;
    }


    /* * Criando Cronograma para agendamentos de tarefas
     * 	http://www.cronmaker.com/?1
     **/
    @Scheduled(cron = "0 52 11 1/1 * ?") // segundo | minuto | hora | dia | mes | ano
    public void testAgedamentoTarefas(){
        System.out.println("Agendamento de tarefas com sucesso");
    }


}
