package org.maia.livro.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.maia.livro.exception.BusinessException;
import org.maia.livro.repository.LoanRepository;
import org.maia.livro.services.impl.BookServicesImpl;
import org.maia.livro.services.impl.LoanServiceImpl;
import org.maia.livro.services.interfaces.LoanServices;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    LoanServices service;

    @MockBean
    LoanRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new LoanServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um imprestimo")
    public void saveLoanTest() {
        //cenario
        Book book = Book.builder().id(1l).build();
        String costumer = "Kayron Maia";
        /* Criando um Emprestimo para ser salvo - que seria passado na requisição*/
        Loan savinLoan = Loan.builder()
                .book(book)
                .costumer(costumer)
                .loanDate(LocalDate.now())
                .build();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(false);
        /* emprestimo salvo - que sera retornado apos a chamada do metodo save */
        Loan loanSaved = Loan.builder().id(1l)
                .book(book)
                .costumer(costumer)
                .loanDate(LocalDate.now()).build();

        /* Simulando o salvar e retornando um imprestimo salvo */
        when(repository.save(savinLoan)).thenReturn(loanSaved);

        Loan loan = service.save(savinLoan);

        assertThat(loan.getId()).isEqualTo(loanSaved.getId()); // verifica se o ID do emprestimo salvo e retornado  é o esperado
        assertThat(loan.getBook()).isEqualTo(loanSaved.getBook());
        assertThat(loan.getCostumer()).isEqualTo(loanSaved.getCostumer());
        assertThat(loan.getLoanDate()).isEqualTo(loanSaved.getLoanDate());
    }


    @Test
    @DisplayName("Deve lançar error de negocio ao tentar salvar um imprestimo com livro indisponível. ")
    public void loandBookSaveTest() {
        //cenario
        Book book = Book.builder().id(1l).build();
        String costumer = "Kayron Maia";

        /* Criando um Emprestimo para ser salvo - que seria passado na requisição*/
        Loan savinLoan = Loan.builder()
                .book(book)
                .costumer(costumer)
                .loanDate(LocalDate.now())
                .build();

        // setando o valor verdadeiro para o empretimo do livro.
        when(repository.existsByBookAndNotReturned(book)).thenReturn(true);

        /* lança exception*/
        Throwable exception = catchThrowable(() -> service.save(savinLoan));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Book currently unavailable");

        verify(repository, never()).save(savinLoan);  // verifica que o metodo save do repository não foi chamado

    }
}
