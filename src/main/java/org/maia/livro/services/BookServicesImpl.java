package org.maia.livro.services;

import org.maia.livro.domain.Book;
import org.maia.livro.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServicesImpl {

    @Autowired
    private BookRepository repository;

    public Book save(Book obj) {
        return repository.save(obj);
    }

}
