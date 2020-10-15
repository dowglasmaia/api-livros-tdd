package org.maia.livro.resource;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.maia.livro.dtos.LoanDTO;
import org.maia.livro.dtos.LoanFilterDTO;
import org.maia.livro.dtos.ReturnedLoadDTO;
import org.maia.livro.exception.BusinessException;
import org.maia.livro.restcontroller.LoanController;
import org.maia.livro.services.LoanServiceTest;
import org.maia.livro.services.interfaces.LoanServices;
import org.maia.livro.services.interfaces.BookServices;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
public class LoanControllerTest {

    static final String LOAN_API = "/api/loans";

    @Autowired
    MockMvc mvc;

    @MockBean
    private BookServices bookService;

    @MockBean
    private LoanServices loanService;

    @Test
    @DisplayName("Deve realizar um emprestimo de livro")
    public void createLoanTest() throws Exception {
        /* ============ CENÁRIO ============= */
        LoanDTO dto = LoanDTO.builder().isbn("123").costumerEmail("dowglasmaia@gmail.com").costumer("Kayron").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Book book = Book.builder().id(1L).isbn("123").build();

        /* Simulando um retorno que estaria na Base de Dados*/
        BDDMockito.given(bookService.getBookByIsbn("123")).willReturn(Optional.of(book));

        /* Simulando um insert na Base de Dados*/
        Loan loan = Loan.builder().id(1L).costumer("Kayron").book(book).loanDate(LocalDate.now()).returned(false).build();
        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);

        /* Fazendo a requisição */
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        /*  Verificação - dos Dados Esperados - no Response da Requisição */
        mvc.perform(request)
                .andExpect(status().isCreated())
                // .andExpect( jsonPath("id").value(1l) );
                .andExpect(content().string("1")); /* quando quero validar o retorno do id , usar o content */
    }

    @Test
    @DisplayName("Deve retornar error ao tentar fazer emprestimo de um livro inexistente")
    public void invalidIsbnCreateLoanTest() throws Exception {
        /* ============ CENÁRIO ============= */
        LoanDTO dto = LoanDTO.builder().isbn("123").costumer("Kayron").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        /* Simulando um retorno que estaria na Base de Dados - neste caso vai retorna vazio*/
        BDDMockito.given(bookService.getBookByIsbn("123")).willReturn(Optional.empty());

        /* Fazendo a requisição */
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        /*  Verificação - dos Dados Esperados - no Response da Requisição */
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1))) // espera ( 01 )um error
                .andExpect(jsonPath("errors[0]").value("Book not found for passed isbn")); // msg de error esperada.
    }

    @Test
    @DisplayName("Deve retornar error ao tentar fazer emprestimo de um livro indisponível")
    public void loanedBookErrorOnCreateLoanTest() throws Exception {
        /* ============ CENÁRIO ============= */
        LoanDTO dto = LoanDTO.builder().isbn("123").costumer("Kayron").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        /* Criando um Livro*/
        Book book = Book.builder().id(1L).isbn("123").build();

        /* Simulando um retorno que estaria na Base de Dados */
        BDDMockito.given(bookService.getBookByIsbn("123")).willReturn(Optional.of(book));
        BDDMockito.given(loanService.save(Mockito.any(Loan.class)) ).willThrow(
                new BusinessException("Book currently unavailable")
        );

        /* Fazendo a requisição */
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        /*  Verificação - dos Dados Esperados - no Response da Requisição */
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1))) // espera ( 01 )um error
                .andExpect(jsonPath("errors[0]").value("Book currently unavailable")); // msg de error esperada.
    }

    @Test
    @DisplayName("Deve retornar um livro")
    public void returnBookTest() throws Exception {

        //CENARIO
        ReturnedLoadDTO dto = ReturnedLoadDTO.builder().returned(true).build(); // criando obj
        String json  = new ObjectMapper().writeValueAsString(dto);

        Loan loan = Loan.builder().id(1L).build();  // criando obj
        BDDMockito.given(loanService.getById(Mockito.anyLong())).willReturn( Optional.of(loan) ); // manipulando retorno

       mvc.perform(
                patch(LOAN_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect( status().isOk() );

        //VEIFICANDO SE PASSOU PELO UPDATE PELO MENOS 1 VEZ
        Mockito.verify(loanService, Mockito.times(1)).update(loan);
    }

    @Test
    @DisplayName("Deve retornar um 404 quando tentar devolver um livro inexistente")
    public void returnInexistentBookTest() throws Exception {

        //CENARIO
        ReturnedLoadDTO dto = ReturnedLoadDTO.builder().returned(true).build(); //criando obj
        String json  = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(loanService.getById(Mockito.anyLong())).willReturn( Optional.empty() ); // manipulando retorno

        mvc.perform(
                patch(LOAN_API.concat("/1"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Deve filtar Emprestimos , com retorno de paginação")
    public void findLoanTest() throws Exception {
        //cenario
        Long id = 1l;
        Loan loan = LoanServiceTest.createLoan();
        loan.setId(id);

        BDDMockito.given( loanService.find(Mockito.any(LoanFilterDTO.class), Mockito.any(Pageable.class)) )
                .willReturn( new PageImpl<Loan>(Arrays.asList(loan),
                        PageRequest.of(0, 10), 1) ); // pagina ZERO com 10 NO MAXIMO REGISTRO e ESPERO 1 NO RESTORN.

        String queryString = String.format("?isbn=%s&costumer=%s&page=0&size=10",
                loan.getBook().getIsbn(),
                loan.getCostumer() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LOAN_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())                                                     // epero resultados sem errors
                .andExpect(jsonPath( "content", Matchers.hasSize(1)))                //espero que o conteudo possua apenas  01 elemento
                .andExpect(jsonPath("totalElements").value(1))          // espero um total de elementos 01
                .andExpect(jsonPath("pageable.pageSize").value(10))    // espero o tamanho da pagina com no max 100 registros
                .andExpect(jsonPath("pageable.pageNumber").value(0));   // espero a pagina 0
    }

}
