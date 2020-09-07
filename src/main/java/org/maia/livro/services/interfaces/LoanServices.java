package org.maia.livro.services.interfaces;

import org.maia.livro.domain.Loan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public interface LoanServices {

    @Transactional
    Loan save(Loan loan);

   Optional<Loan> getById(Long id);

    @Transactional
    Loan update(Loan loan);
}
