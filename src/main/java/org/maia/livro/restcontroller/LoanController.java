package org.maia.livro.restcontroller;

import lombok.RequiredArgsConstructor;
import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.maia.livro.dtos.LoanDTO;
import org.maia.livro.dtos.ReturnedLoadDTO;
import org.maia.livro.exception.ObjectNotFoundException;
import org.maia.livro.services.interfaces.BookServices;
import org.maia.livro.services.interfaces.LoanServices;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor  //para o spring fazer a injeção dos servicos -  pelo lombok
public class LoanController {

    private final LoanServices loanServices;
    private final BookServices bookServices;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create (@RequestBody LoanDTO dto){

        Book book = bookServices.getBookByIsbn(dto.getIsbn()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn") ) ;//new ObjectNotFoundException("Book not found for passed isbn"

        Loan entity = Loan.builder()
                .book(book)
                .costumer(dto.getCostumer())
                .loanDate(LocalDate.now())
                .build();

        Loan newEntity = loanServices.save(entity);
        return newEntity.getId();
    }

    //@GetMapping("/{id}")
    @PatchMapping("/{id}")
    public void returnedBook(
            @PathVariable Long id,
            @RequestBody ReturnedLoadDTO dto){

        Loan loan =  loanServices.getById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Book not found for passed id: "+id )
        );
        loan.setReturned(dto.getReturned());

        loanServices.update(loan);
    }

}
