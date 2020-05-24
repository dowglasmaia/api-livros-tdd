package org.maia.livro.restcontroller;

import org.maia.livro.domain.Book;
import org.maia.livro.dtos.BookDTO;
import org.maia.livro.exception.ApiErrors;
import org.maia.livro.exception.BusinessException;
import org.maia.livro.services.impl.BookServicesImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookServicesImpl service;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        Book entity = modelMapper.map(dto, Book.class);
        entity = service.save(entity);
        return modelMapper.map(entity, BookDTO.class);
    }

    @GetMapping("/{id}")
    public BookDTO getById(@PathVariable Long id){
        return service.getById(id)
                .map( book -> modelMapper.map(book, BookDTO.class) )
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handlerValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        return new ApiErrors(result);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handlerBusinessException(BusinessException ex) {
        return new ApiErrors(ex);

    }

}
