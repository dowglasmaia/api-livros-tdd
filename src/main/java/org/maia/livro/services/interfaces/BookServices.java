package org.maia.livro.services.interfaces;

import org.maia.livro.domain.Book;
import org.maia.livro.exception.BusinessException;
import org.maia.livro.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface BookServices {

    Book save(Book book);

    Optional<Book>getById(Long id);

    void delete(Book book);

    Book update(Book book);
}
