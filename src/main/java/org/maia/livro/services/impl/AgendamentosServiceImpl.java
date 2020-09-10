package org.maia.livro.services.impl;

import lombok.RequiredArgsConstructor;
import org.maia.livro.domain.Loan;
import org.maia.livro.services.interfaces.EmailServices;
import org.maia.livro.services.interfaces.LoanServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendamentosServiceImpl {

    /** SEGUNDO | MINUTO | HORA | DIA | MES | ANO **/
    private static final  String CRON_LATE_LOANS = "0 0 0 1/1 * ?"; // 0-Segundo | 0-Minunto | 0-Hora | 1/1(Todos os Dias) | *(Qual Mês) | ?(Qual Ano).

    @Value("${application.mail.lateloans.message}")
    private String message;

    private final LoanServices loanServices;
    private final EmailServices emailServices;

    @Scheduled(cron = "CRON_LATE_LOANS")
    public void sendMailToLateLoans(){
        List<Loan>allLateLoans = loanServices.getAllLateLoans();

        List<String> emailList = allLateLoans.stream()
                .map(loan -> loan.getCostumerEmail() )
                .collect(Collectors.toList());
        //String messagem = "Atenção! Você tem um emprestimo atraso. Favor devolver o livro o mais rápido possivel.";

        emailServices.sendMails(message, emailList);
    }
}
