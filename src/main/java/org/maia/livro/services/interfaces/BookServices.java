package org.maia.livro.services.interfaces;

import org.maia.livro.domain.Book;
import org.maia.livro.exception.BusinessException;
import org.maia.livro.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public interface BookServices {

    @Transactional
    Book save(Book book);


    Optional<Book>getById(Long id);

    @Transactional
    void delete(Book book);

    @Transactional
    Book update(Book book);

    Page<Book> find(Book filter, Pageable pages);

    Optional< Book > getBookByIsbn(String isbn);
}
