package org.maia.livro.services.interfaces;

import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.maia.livro.dtos.LoanFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Loan> find(LoanFilterDTO filterDTO, Pageable page);

    Page<Loan> getLoansByBook(Book book, Pageable pageable);
}
