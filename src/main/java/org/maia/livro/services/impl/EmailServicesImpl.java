package org.maia.livro.services.impl;

import lombok.RequiredArgsConstructor;
import org.maia.livro.services.interfaces.EmailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServicesImpl implements EmailServices {

    @Value("${application.mail.default-remetente}")
    private String remetent;

    @Autowired
    private final JavaMailSender javaMailSender;

    @Override
    public void sendMails(String messagem, List<String> emailList) {

        String [] mails = emailList.toArray( new String[ emailList.size() ] ); // convert a lista para um Array

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(remetent);                           //Remetente
        mailMessage.setSubject("Livro com empréstimo atrasado"); // assunto
        mailMessage.setText(messagem);                           // messagem do email
        mailMessage.setTo(mails);                                // Array com Lista de email  - Destinatarios
    }
}
