package org.maia.livro.config;

import org.maia.livro.services.interfaces.EmailServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableScheduling // agendamento de tarefas
public class ApiConfigUtils {


    @Autowired
    private EmailServices emailServices;

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public CommandLineRunner commandLineRunner(){
        return args -> {
            List<String> emails = Arrays.asList("maia-api-3cfead@inbox.mailtrap.io");
            emailServices.sendMails("Testando  Servços de emails." , emails);
            System.out.println("Email Enviados...");
        };
    };






    /* * Criando Cronograma para agendamentos de tarefas
     * 	http://www.cronmaker.com/?1
     **/
    @Scheduled(cron = "0 52 11 1/1 * ?") // segundo | minuto | hora | dia | mes | ano
    public void testAgedamentoTarefas(){
        System.out.println("Agendamento de tarefas com sucesso");
    }






}
