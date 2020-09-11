package org.maia.livro.services.impl;

import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.maia.livro.dtos.LoanFilterDTO;
import org.maia.livro.exception.BusinessException;
import org.maia.livro.repository.LoanRepository;
import org.maia.livro.services.interfaces.LoanServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanServices {

    private LoanRepository repository;

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

    @Override
    public Optional<Loan> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return repository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO loan, Pageable page) {
        return repository.findByBookIsbnOrCustomer(loan.getIsbn(),loan.getCostumer(),page);
    }

    @Override
    public Page<Loan> getLoansByBook(Book book, Pageable pageable) {
        return repository.findByBook(book,pageable);
    }

    @Override
    public List<Loan> getAllLateLoans() {
        final Integer loanDays = 4;
        LocalDate tresDiasAntras = LocalDate.now().minusDays(loanDays);
        return repository.findByLoanComDiasDeAtrasoDeRetorno(tresDiasAntras);
    }
}
