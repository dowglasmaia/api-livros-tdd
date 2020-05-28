package org.maia.livro.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maia.livro.domain.Book;
import org.maia.livro.exception.BusinessException;
import org.maia.livro.repository.BookRepository;
import org.maia.livro.services.impl.BookServicesImpl;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("tests")
public class BookServiceTest {


    BookServicesImpl service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServicesImpl(repository);
    }


    private Book createBook() {
        return Book.builder().isbn("123").author("kayron").title("T2").build();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        //cenario
        Book book = createBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book)).thenReturn(
                Book.builder().id(1l)
                        .isbn("123")
                        .title("t2")
                        .author("kayron").build());

        //execução
        Book savedBook = service.save(book);

        //verificação/
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getAuthor()).isEqualTo("kayron");
        assertThat(savedBook.getTitle()).isEqualTo("t2");
        assertThat(savedBook.getIsbn()).isEqualTo("123");
    }

    @Test
    @DisplayName("Deve lançar error de negócio ao tentar salvar um livro com isbn duplicado")
    public void shoulNotSaveABookWithDuplicatedISBN() {
        //cenario
        Book book = createBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //execução
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        //verificação
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado!");
        Mockito.verify(repository, Mockito.never()).save(book);

    }

    @Test
    @DisplayName("Deve obter um livro por ID")
    public void getByIdTest() {
        //CENARIO
        Long id = 1l;
        Book book = createBook();
        book.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book)); // simula uma pesquisa ao banco de Dados

        //EXECUÇÃO
        Optional<Book> foundBook = service.getById(id);

        //VERIFICAÇÃO
        assertThat(foundBook.isPresent()).isTrue(); // espera um livro na resposta da consulta
        assertThat(foundBook.get().getId()).isEqualTo(id); // o id do Livro deve ser igual ao id do livro recuperado na consulta
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn()); // o isbn do Livro deve ser igual ao isbn do livro recuperado na consulta
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor()); // o Author do Livro deve ser igual ao Author do livro recuperado na consulta
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle()); // o Title do Livro deve ser igual ao Title do livro recuperado na consulta
    }

    @Test
    @DisplayName("Deve retornar vazio quando o id do livro passado não existir na base de dados")
    public void bookNotFoundByIdTest() {
        //CENARIO
        Long id = 1l;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty()); // simula uma pesquisa ao banco de Dados e retorna vazio

        //EXECUÇÃO
        Optional<Book> book = service.getById(id);

        //VERIFICAÇÃO
        assertThat(book.isPresent()).isFalse(); // não espera um livro na resposta da consulta

    }
}
