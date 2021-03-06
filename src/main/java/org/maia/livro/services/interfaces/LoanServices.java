package org.maia.livro.services.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.maia.livro.dtos.LoanFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public interface LoanServices extends Serializable {

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
