package org.maia.livro.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.maia.livro.dtos.BookDTO;
import org.maia.livro.dtos.LoanDTO;
import org.maia.livro.services.interfaces.BookServices;
import org.maia.livro.services.interfaces.LoanServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor  //para o spring fazer a injeção dos servicos -  pelo lombok
public class BookController {



    private final LoanServices loanServices;
    private final BookServices service;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
    	log.info("Iniciando Criação de um novo Livro!");
        Book entity = modelMapper.map(dto, Book.class);
        entity = service.save(entity);
        
        BookDTO obj = modelMapper.map(entity, BookDTO.class);
        log.info("Livro criado com sucesso" );
        return obj;
    }

    @GetMapping("/{id}")
    public BookDTO getById(@PathVariable Long id) {
        return service.getById(id)
                .map(book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ;
        service.delete(book);

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO update(@PathVariable Long id, @RequestBody BookDTO dto) {
        return service.getById(id).map(book -> {
            book.setAuthor(dto.getAuthor());
            book.setTitle(dto.getTitle());
            book = service.update(book);
            return modelMapper.map(book, BookDTO.class);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping
    public Page<BookDTO> find(BookDTO dto, Pageable pagRequest) { 	
    	
        Book filter = modelMapper.map(dto, Book.class); //coverte o BookDTO para um Book
        Page<Book> result = service.find(filter, pagRequest);

        List<BookDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, BookDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<BookDTO>(list, pagRequest, result.getTotalElements() );

    }


   /* @GetMapping
    public Page<BookDTO>(BookDTO dto, Pageable pagRequest	) {
    	
    	
        Book filter = modelMapper.map(dto, Book.class); //coverte o BookDTO para um Book
        Page<Book> result = service.find(filter, pagRequest);

        List<BookDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, BookDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<BookDTO>(list, pagRequest, result.getTotalElements() );


   
   




    @GetMapping("/{id}/loans")
    public Page<LoanDTO>loansByBook(@PathVariable Long id, Pageable pageable){
        Book book = service.getById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book não encontrado para o id: "+id) );
        Page<Loan> result = loanServices.getLoansByBook(book,pageable);

        List<LoanDTO> list = result.getContent()
                .stream()
                .map(loan -> {
                    Book loanBook = loan.getBook();
                    BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class); // convert um book para bookDTO
                    LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class); // convert um loan em loanDTO
                    loanDTO.setBook(bookDTO);
                    return loanDTO;
                }).collect(Collectors.toList());

        return new PageImpl<LoanDTO>(list, pageable, result.getTotalElements());
    }


}
