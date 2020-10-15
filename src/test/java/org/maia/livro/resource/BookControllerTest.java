package org.maia.livro.resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maia.livro.domain.Book;
import org.maia.livro.dtos.BookDTO;
import org.maia.livro.exception.BusinessException;
import org.maia.livro.restcontroller.BookController;
import org.maia.livro.services.impl.BookServicesImpl;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookServicesImpl services;

    private BookDTO createNewBook() {
        return BookDTO.builder().author("Dowglas Maia").title("Game of Thrones Vol 05").isbn("Vol 5 03º Ed").build();
    }

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void createBookTest() throws Exception {
        BookDTO dto = createNewBook();
        Book saveBook = Book.builder().author("Dowglas Maia").title("Game of Thrones Vol 05").isbn("Vol 5 03º Ed").id(1L).build();
        BDDMockito.given(services.save(Mockito.any(Book.class))).willReturn(saveBook);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()));
    }


    @Test
    @DisplayName("Deve lançar error de validação quando não houver dados suficientes para criação do livro.")
    public void createInvalidBookTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(3))); // Matchers.hasSize(3) - numero de campos qual estarão invalidos

    }

    @Test
    @DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn ja cadastrado no sistema.")
    public void createBookWithDuplicatedIsbn() throws Exception {
        BookDTO dto = createNewBook();
        String json = new ObjectMapper().writeValueAsString(dto);
        String msgError = "Isbn já cadastrado!";

        BDDMockito.given(services.save(Mockito.any(Book.class)))
                .willThrow(new BusinessException(msgError));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(msgError));

    }


    @Test
    @DisplayName("Deve obter informações de um Livro por ID")
    public void getBookDetailTest() throws Exception {
        //cenario (given)
        Long id = 20l;
        Book book = Book.builder()
                .id(id)
                .title(createNewBook().getTitle())
                .author(createNewBook().getAuthor())
                .isbn(createNewBook().getIsbn())
                .build();
        BDDMockito.given(services.getById(id)).willReturn(Optional.of(book));

        //execução (when)
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(createNewBook().getTitle()))
                .andExpect(jsonPath("author").value(createNewBook().getAuthor()))
                .andExpect(jsonPath("isbn").value(createNewBook().getIsbn()));
    }

    @Test
    @DisplayName("Deve retornar resource not found quando o livro procurado não existir")
    public void bookNotFoundTest() throws Exception {
        //cenario - passabdo o ID vazio
        BDDMockito.given(services.getById(Mockito.anyLong())).willReturn(Optional.empty());

        //execução
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + 1l))
                .accept(MediaType.APPLICATION_JSON);

        //verificação - validação
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest() throws Exception {
        //cenario
        BDDMockito.given(services.getById(Mockito.anyLong())).willReturn(Optional.of(Book.builder().id(1l).build()));

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        //verificação
        mvc.perform(request).andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Deve reotnar resource not found quando não encontrar Book para Excluir")
    public void bookNotFoundDelete() throws Exception {
        //cenario
        BDDMockito.given(services.getById(Mockito.anyLong())).willReturn(Optional.empty());

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/"+1))
                .accept(MediaType.APPLICATION_JSON);

        //verificação
        mvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest() throws Exception {
        //cenario
        Long id =1l;
        String json = new ObjectMapper().writeValueAsString(createNewBook());
        Book bookUpdate = Book.builder().title("Deus é Fiel").author("Maia").isbn("D-87B").build();
        BDDMockito.given( services.getById(id) )
                .willReturn(Optional.of(bookUpdate) );
        BDDMockito.given( services.update(bookUpdate)).willReturn(Book.builder().id(1l).author("Dowglas Maia").title("Game of Thrones Vol 05").isbn("Vol 5 03º Ed").build());

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" +1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id) )
                .andExpect(jsonPath("title").value(createNewBook().getTitle()) )
                .andExpect(jsonPath("author").value(createNewBook().getAuthor()) )
                .andExpect(jsonPath("isbn").value(createNewBook().getIsbn()));
    }

    @Test
    @DisplayName("Deve retornar 404 quando tentar atualizar um livro inexistente")
    public void updateInexistenteBookTest() throws Exception {
      BDDMockito.given( services.getById(Mockito.anyLong()) )
                .willReturn(Optional.empty() );

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" +1))
                .accept(MediaType.APPLICATION_JSON);

        //verificação
        mvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve filtar livros , com retorno de paginação")
    public void findBooksTest() throws Exception {
        //cenario
        Long id = 1l;
        Book book = Book.builder().id(id).title(createNewBook().getTitle()).author(createNewBook().getAuthor()).isbn(createNewBook().getIsbn()).build();

        BDDMockito.given( services.find(Mockito.any(Book.class), Mockito.any(Pageable.class)) )
                .willReturn( new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0, 100), 1) ); // pagina ZERO com 100 NO MAXIMO REGISTRO e ESPERO 1 NO RESTORN.

        String queryString = String.format("?title=%s&author=%s&page=0&size=100", book.getTitle(), book.getAuthor() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk()) // epero resultados sem errors
                .andExpect(jsonPath( "content", Matchers.hasSize(1))) //espero que o conteudo possua apenas  01 elemento
                .andExpect(jsonPath("totalElements").value(1))// espero um total de elementos 01
                .andExpect(jsonPath("pageable.pageSize").value(100)) // espero o tamanho da pagina com no max 100 registros
                .andExpect(jsonPath("pageable.pageNumber").value(0)); // espero a pagina 0
    }

}
