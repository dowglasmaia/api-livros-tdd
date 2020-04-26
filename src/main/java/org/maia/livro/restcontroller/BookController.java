package org.maia.livro.restcontroller;

import org.maia.livro.domain.Book;
import org.maia.livro.dtos.BookDTO;
import org.maia.livro.services.BookServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookServices service;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody BookDTO dto) {
        Book entity = modelMapper.map(dto, Book.class);
        entity = service.save(entity);
        return  modelMapper.map(entity, BookDTO.class);

       // Book entity = Book.builder().author(dto.getAuthor()).title(dto.getTitle()).isbn(dto.getIsbn()).build();
        /*
        return BookDTO.builder()
                .id(entity.getId())
                .author(entity.getAuthor())
                .title(entity.getTitle())
                .isbn(entity.getIsbn()).build();
        */
    }


}
