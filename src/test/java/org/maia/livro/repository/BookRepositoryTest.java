package org.maia.livro.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maia.livro.domain.Book;
import org.maia.livro.services.impl.BookServicesImpl;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

/*
* Testes de Integração
* */

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository repository;

    public static Book createBook() {
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

    @Test
    @DisplayName("Deve obter um livro por id")
    public void findByIdTest(){
        //cenario
        Book book = createBook(); // cria um livro populado
        entityManager.persist(book); // simula um insert na banco

        //execução
        Optional<Book> foundBook = repository.findById(book.getId()); // retorna o livro persistido

        //verificação
        Assertions.assertThat(foundBook.isPresent()).isTrue();  //espera um livro na resposta
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        Book book = createBook();  // cria o livro populado

        Book savedBook = repository.save(book); // salva o livro

        Assertions.assertThat(savedBook.getId() ).isNotNull();  // verifica se o livro salvar estar com ID
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest(){
        Book book= createBook();
        entityManager.persist(book); // grava o livro na base de dados

        Book foundBook = entityManager.find(Book.class, book.getId() ); // busca o livro na base de ados
        Assertions.assertThat(foundBook).isNotNull(); //verifica que o retorno do livro não estar nulo

        repository.delete(foundBook); // deleta o livro da base dados

        Book deletedBook = entityManager.find(Book.class, book.getId()); // livro deletado - deve retorna nulo
        Assertions.assertThat(deletedBook).isNull(); // verifica que o retorna e nulo.
    }

}
