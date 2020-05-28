package org.maia.livro.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maia.livro.domain.Book;
import org.maia.livro.services.impl.BookServicesImpl;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository repository;

    @Autowired
    private BookServicesImpl services;

    private Book createBook() {
        return Book.builder().title("Aventuras").author("Kayron").isbn("123").build();
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o ISBN informado.")
    public void returnTrueWhenIsbnExists() {
        //cenario
        String isbn = "123";
        Book book = createBook();
        entityManager.persist(book);

        //execução
        boolean exists = repository.existsByIsbn(isbn);

        //verificação
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando não existir um livro na base com o ISBN informado.")
    public void returnFalseWhenIsbnExists() {
        //cenario
        String isbn = "123";

        //execução
        boolean exists = repository.existsByIsbn(isbn);

        //verificação
        Assertions.assertThat(exists).isFalse();
    }


}
