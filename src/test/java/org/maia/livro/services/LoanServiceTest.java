package org.maia.livro.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.maia.livro.dtos.LoanDTO;
import org.maia.livro.dtos.LoanFilterDTO;
import org.maia.livro.exception.BusinessException;
import org.maia.livro.repository.LoanRepository;
import org.maia.livro.services.impl.BookServicesImpl;
import org.maia.livro.services.impl.LoanServiceImpl;
import org.maia.livro.services.interfaces.LoanServices;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        Loan savinLoan = this.createLoan();

        when(repository.existsByBookAndNotReturned( this.createLoan().getBook() )).thenReturn(false);
        /* emprestimo salvo - que sera retornado apos a chamada do metodo save */
        Loan loanSaved = this.createLoan();
        loanSaved.setId(1l);

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
        Loan savinLoan = this.createLoan();

        // setando o valor verdadeiro para o empretimo do livro.
        when(repository.existsByBookAndNotReturned( this.createLoan().getBook() )).thenReturn(true);

        /* lança exception*/
        Throwable exception = catchThrowable(() -> service.save(savinLoan));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Book currently unavailable");

        verify(repository, never()).save(savinLoan);  // verifica que o metodo save do repository não foi chamado
    }

    @Test
    @DisplayName("Deve obter as informações de um emprestimo pelo ID")
    public void getLoanDetailsTest(){
        // CENARIO
        Long id = 1l;
        Loan loan = createLoan();
        loan.setId(id);

        Mockito.when( repository.findById(id) ).thenReturn(Optional.of( loan )); //simula o retorn de Loan

        //EXECUÇÃO
        Optional<Loan> result = service.getById(id);

        //VERIFICAÇÃO
        assertThat( result.isPresent() ).isTrue();
        assertThat( result.get().getId() ).isEqualTo( id );
        assertThat( result.get().getCostumer() ).isEqualTo( loan.getCostumer() );
        assertThat( result.get().getBook() ).isEqualTo( loan.getBook());
        assertThat( result.get().getReturned() ).isEqualTo( loan.getReturned() );
        assertThat( result.get().getLoanDate() ).isEqualTo( loan.getLoanDate() );

        verify(repository, times(1) ).findById(id); // verifica se o metodo findById foi chamado ao menos UMA vez.

    }

    @Test
    @DisplayName("Deve atualizar um emprestimo")
    public void updateLoanTest() {
        Loan loan = createLoan();
        loan.setId(1l);
        loan.setReturned(true);

        when( repository.save(loan) ).thenReturn(loan); // mokando o emprestimo para verificação

        Loan updatedLoan = service.update(loan);

        assertThat(updatedLoan.getReturned()).isTrue();
        verify(repository).save(loan);
    }

    @Test
    @DisplayName("Deve filtrar emprestimo pelas propiedades")
    public void filterLoanTest() {
        //cenario
        LoanFilterDTO loanDTO = LoanFilterDTO.builder().costumer("Kayron").isbn("abc").build();
        Loan loan = createLoan();
        loan.setId(1l);
        List<Loan>  lista=  Arrays.asList(loan);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Loan> page = new PageImpl<Loan>(lista, pageRequest , lista.size());

        when( repository.findByBookIsbnOrCustomer(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(PageRequest.class)))
                .thenReturn(page);

        //execução
        Page<Loan> pageResult = service.find(loanDTO, pageRequest);

        //verificação da resposta esperada.
        assertThat(pageResult.getTotalElements()).isEqualTo(1);
        assertThat(pageResult.getContent()).isEqualTo(lista);
        assertThat(pageResult.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(pageResult.getPageable().getPageSize()).isEqualTo(10);
    }


    public static Loan createLoan(){
        Book book = Book.builder().id(1l).isbn("123").build();
        String costumer = "Kayron Maia";
        return  Loan.builder()
                .book(book)
                .costumer(costumer)
                .loanDate(LocalDate.now())
                .build();
    }

}
