package org.maia.livro.services.interfaces;

import org.maia.livro.domain.Loan;
import org.springframework.transaction.annotation.Transactional;

public interface LoanServices {

    @Transactional
    Loan save(Loan loan);
}
