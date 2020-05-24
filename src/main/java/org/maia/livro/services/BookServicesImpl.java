package org.maia.livro.services;

import org.maia.livro.domain.Book;
import org.maia.livro.exception.BusinessException;
import org.maia.livro.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServicesImpl {

    @Autowired
    private BookRepository repository;

    public Book save(Book obj) {
        if(repository.existsByIsbn(obj.getIsbn()) ){
            throw new BusinessException("Isbn já cadastrado!");
        }
        return repository.save(obj);
    }

}
