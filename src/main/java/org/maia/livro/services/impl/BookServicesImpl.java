package org.maia.livro.services.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.maia.livro.domain.Book;
import org.maia.livro.exception.BusinessException;
import org.maia.livro.repository.BookRepository;
import org.maia.livro.services.interfaces.BookServices;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j	
@Service
@RequiredArgsConstructor
public class BookServicesImpl implements BookServices {

    private BookRepository repository;

    public BookServicesImpl(
            BookRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public Book save(Book obj) {
        if (repository.existsByIsbn(obj.getIsbn())) {
            throw new BusinessException("Isbn já cadastrado!");
        }
        return repository.save(obj);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Book book) {
        if (book == null || book.getId() == null) {
            throw new IllegalArgumentException("Book id cant be bull");
        }
        repository.delete(book);
    }

    @Override
    public Book update(Book book) {
        if (book == null || book.getId() == null) {
            throw new IllegalArgumentException("Book id cant be bull");
        }
       return this.repository.save(book);
    }

    @Override
    public Page<Book> find(Book filter, Pageable pages) {
    	log.info("Listando Todos os Livros pelo Services!");
        Example<Book> example = Example.of(filter, ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher( ExampleMatcher.StringMatcher.CONTAINING )
        );
        return repository.findAll(example, pages);
    }

    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        return repository.findByIsbn(isbn);
    }

}
