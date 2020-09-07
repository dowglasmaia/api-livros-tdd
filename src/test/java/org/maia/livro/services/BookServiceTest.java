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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        when(repository.save(book)).thenReturn(
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
        when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

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
        when(repository.findById(id)).thenReturn(Optional.of(book)); // simula uma pesquisa ao banco de Dados

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
        when(repository.findById(id)).thenReturn(Optional.empty()); // simula uma pesquisa ao banco de Dados e retorna vazio

        //EXECUÇÃO
        Optional<Book> book = service.getById(id);

        //VERIFICAÇÃO
        assertThat(book.isPresent()).isFalse(); // não espera um livro na resposta da consulta

    }

    @Test
    @DisplayName("Deve detelar um Livro")
    public void deleteBookTest() {
        //cenario
        Long id = 1l;
        Book book = createBook();
        book.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(book)); // retonar um Livro criado

        //execução
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(book)); // verifica que não lançou nenhuma Exceção ao deletar o livro

        //verificação
        Mockito.verify(repository, Mockito.times(1)).delete(book); // verifica que o metodo delete foi chamado 1x
    }


    @Test
    @DisplayName("Deve lançar error ao tentar deletar um livro inexistente")
    public void deleteInvalidBookTest() {
        //cenario
        Book book = new Book(); // cria um libro vazio

        //execução
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book)); // deve lançar esta exception ao chamar o delete()

        //verificação
        Mockito.verify(repository, Mockito.never()).delete(book); // verifica que o metodo delete(), nunca foi chamado
    }

    @Test
    @DisplayName("Deve lançar error ao tentar atualizar um livro inexistente")
    public void updateInvalidBookTest() {
        //cenario
        Book book = new Book(); // cria um libro vazio

        //execução
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book)); // deve lançar esta exception ao chamar o update()

        //verificação
        Mockito.verify(repository, Mockito.never()).save(book); // verifica que o metodo save(), nunca foi chamado
    }

    @Test
    @DisplayName("Deve atualizar um livro ")
    public void updateBookTest() {
        //cenario
        //livro a ser atualizado
        Book updatingBook = Book.builder().id(1l).build();

        //simulação update
        Book updatedBook = createBook();
        updatedBook.setId(1l);
        when( repository.save(updatingBook)).thenReturn(updatedBook); // retorna o livro atualizado

        //execução
        Book book = service.update(updatingBook);

        //verificação  - compara o retorno com o livro atualizado.
        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
    }

    @Test
    @DisplayName("Deve filtrar livros pelas propiedades")
    public void finsBookTest(){
        //cenario
        Book book = createBook();
        List<Book>  lista=  Arrays.asList(book); // cria uma lista de book
        PageRequest pageRequest = PageRequest.of(0,10); // pagina 'ZERO' com no maximo 'DEZ' elementos
        Page<Book> page = new PageImpl<Book>(lista, pageRequest , 1); // populando a pagina com 01(UM) Elemento
        when( repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        //execução
        Page<Book> pageResult = service.find(book, pageRequest);

        //verificação da resposta esperada.
        assertThat(pageResult.getTotalElements()).isEqualTo(1);
        assertThat(pageResult.getContent()).isEqualTo(lista);
        assertThat(pageResult.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(pageResult.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve obter um livro pelo isbn")
    public void getBookByIsbnTest(){
        //cenario
        String isbn = "1230";
        when(repository.findByIsbn(isbn)).thenReturn(Optional.of(Book.builder().id(1l).isbn(isbn).build() ));

        Optional<Book> book = service.getBookByIsbn(isbn);

        assertThat(book.isPresent()).isTrue(); //verifica que o livro estar presente
        assertThat(book.get().getId()).isEqualTo(1l);
        assertThat(book.get().getIsbn()).isEqualTo(isbn); //verifica que o isbn retornado é o mesmo que foi passado como parametro.

        verify(repository,times(1)).findByIsbn(isbn); //verifica que o metodo foi chamado uma vez

    }



}