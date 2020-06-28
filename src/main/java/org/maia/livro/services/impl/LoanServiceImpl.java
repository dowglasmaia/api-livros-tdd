package org.maia.livro.services.impl;

import org.maia.livro.domain.Loan;
import org.maia.livro.repository.LoanRepository;
import org.maia.livro.services.interfaces.LoanServices;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanServices {

    private LoanRepository repository;

    public LoanServiceImpl (){   }

    public LoanServiceImpl (LoanRepository repository){
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        return repository.save(loan);
    }
}
