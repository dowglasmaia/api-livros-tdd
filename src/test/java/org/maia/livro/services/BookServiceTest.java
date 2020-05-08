package org.maia.livro.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maia.livro.domain.Book;
import org.maia.livro.repository.BookRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("tests")
public class BookServiceTest {

   BookServicesImpl  service;

   @MockBean
   BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServicesImpl();
    }


    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        //cenario
        Book book = Book.builder().isbn("123").author("kayron").title("T2").build();
        Mockito.when(repository.save(book) ).thenReturn(
                Book.builder().id(1l)
                        .isbn("123")
                        .title("t2")
                        .author("kayron").build() );

        //execução
        Book savedBook = repository.save(book);

        //verificação/
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getAuthor()).isEqualTo("kayron");
        assertThat(savedBook.getTitle()).isEqualTo("t2");
        assertThat(savedBook.getIsbn()).isEqualTo("123");

    }

}
