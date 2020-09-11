package org.maia.livro.services.impl;

import lombok.RequiredArgsConstructor;
import org.maia.livro.services.interfaces.EmailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServicesImpl implements EmailServices {

    @Autowired
    private final JavaMailSender javaMailSender;

    @Override
    public void sendMails(String messagem, List<String> emailList) {

    }
}
