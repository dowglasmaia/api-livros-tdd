package org.maia.livro.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.maia.livro.repository.BookRepositoryTest.createBook;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LoanRepository repository;


    @Test
    @DisplayName("Deve verificar se existe emprestimo não devolvido para o Livro")
    public void existsByBookAndNotReturned(){
        //cenario
        Book book = createBook();
        entityManager.persist(book);

        Loan loan = Loan.builder().loanDate(LocalDate.now()).costumer("Kamilly Maia").book(book).build();
        entityManager.persist(loan);

        //execeção
        boolean exists = repository.existsByBookAndNotReturned(book);

        //verificação
        assertThat(exists).isTrue(); //deve retorna verdadeiro


    }

}
