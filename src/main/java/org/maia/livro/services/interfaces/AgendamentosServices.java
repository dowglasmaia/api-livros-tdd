package org.maia.livro.services.interfaces;

import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public interface AgendamentosServices extends Serializable {

    public void sendMailToLateLoans();
}
