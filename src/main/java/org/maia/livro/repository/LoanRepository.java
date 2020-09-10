package org.maia.livro.repository;

import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    /* fazendo uma contagem na tabela Loan pelo livro informado, caso o retorno seja maior que zero, retorna TRUE se não FALSE */
    @Query(value = "SELECT CASE WHEN ( COUNT(l.id ) > 0 ) THEN TRUE ELSE FALSE END " +
                    " FROM Loan l WHERE l.book = :book AND (l.returned IS NULL OR l.returned IS FALSE ) ")
    boolean existsByBookAndNotReturned(@Param("book") Book book);

    @Query(value = "SELECT l FROM Loan AS l JOIN l.book AS b WHERE b.isbn = :isbn OR l.costumer = :costumer")
    Page<Loan> findByBookIsbnOrCustomer(
            @Param ("isbn")String isbn,
            @Param ("costumer") String customer,
            Pageable pageable);

    Page<Loan> findByBook(Book book,Pageable pageable);
}
