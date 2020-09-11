package org.maia.livro.services.interfaces;

import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.maia.livro.dtos.LoanFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface LoanServices {

    @Transactional(rollbackFor = Exception.class)
    Loan save(Loan loan);

    @Transactional(readOnly = true)
    Optional<Loan> getById(Long id);

    @Transactional(rollbackFor = Exception.class)
    Loan update(Loan loan);

    @Transactional(readOnly = true)
    Page<Loan> find(LoanFilterDTO filterDTO, Pageable page);

    @Transactional(readOnly = true)
    Page<Loan> getLoansByBook(Book book, Pageable pageable);

    @Transactional(readOnly = true)
    List<Loan> getAllLateLoans();
}
