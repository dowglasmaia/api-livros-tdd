package org.maia.livro.services.impl;

import org.maia.livro.domain.Loan;
import org.maia.livro.exception.BusinessException;
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
        if(repository.existsByBookAndNotReturned(loan.getBook()) ){
            throw new BusinessException("Book currently unavailable");
        }

        return repository.save(loan);
    }
}
