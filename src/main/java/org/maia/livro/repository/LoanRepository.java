package org.maia.livro.repository;

import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {


}
