package org.maia.livro.services.impl;

import org.maia.livro.domain.Book;
import org.maia.livro.exception.BusinessException;
import org.maia.livro.repository.BookRepository;
import org.maia.livro.services.interfaces.BookServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServicesImpl implements BookServices {

    private BookRepository repository;


    public BookServicesImpl(BookRepository repository) {
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
        repository.delete(book);
    }

    @Override
    public Book update(Book book) {
        return null;
    }

}