package org.maia.livro.services.interfaces;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmailServices {

    void sendMails(String messagem,List<String> emailList);
}
