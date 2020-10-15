package org.maia.livro.repository;

import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

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
        Loan loan = this.createAndPersistBookAndLoan(LocalDate.now());
        Book book = this.createAndPersistBookAndLoan(LocalDate.now()).getBook();

        //execeção
        boolean exists = repository.existsByBookAndNotReturned(book);

        //verificação
        assertThat(exists).isTrue(); //deve retorna verdadeiro
    }

    @Test
    @DisplayName("Deve Buscar Emprestimo por isbn do Livro ou customer")
    public void findByLoanForBookIsbnOrCustomerTeste(){
        //cenario
        Loan loan = this.createAndPersistBookAndLoan(LocalDate.now());

      Page<Loan> result = repository.findByBookIsbnOrCustomer(
              "123","Kayron", PageRequest.of(0,10));

      assertThat(result.getContent()).hasSize(1);
      assertThat(result.getContent().contains(loan));
      assertThat(result.getPageable().getPageSize()).isEqualTo(10);
      assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
      assertThat(result.getTotalElements()).isEqualTo(1);

    }
    @Test
    @DisplayName("Deve obter emprestimos cuja data de emprestimo for menor ou igual a tres dias atrás e não retornados")
    public void findByLoanNotRetornedTeste(){
       Loan loan = createAndPersistBookAndLoan(LocalDate.now().minusDays(5));

        List<Loan>result = repository.findByLoanComDiasDeAtrasoDeRetorno(LocalDate.now().minusDays(4));

        assertThat(result).hasSize(1);
        assertThat(result).contains(loan);


    }

    @Test
    @DisplayName("Não Deve obter emprestimos cuja data de emprestimo for menor ou igual a tres dias atrás e não retornados")
    public void notFindByLoanNotRetornedTeste(){
        Loan loan = createAndPersistBookAndLoan(LocalDate.now());

        List<Loan>result = repository.findByLoanComDiasDeAtrasoDeRetorno(LocalDate.now().minusDays(4));

        assertThat(result).isEmpty();


    }



    private Loan createAndPersistBookAndLoan( LocalDate dataAtual) {
        Book book = createBook();
        entityManager.persist(book);

        Loan loan = Loan.builder()
                .loanDate(dataAtual)
                .costumer("Kayron")
                .book(book)
                .costumerEmail("dowglas@email.com")
                .build();
        entityManager.persist(loan);
       return loan;
    }


}
