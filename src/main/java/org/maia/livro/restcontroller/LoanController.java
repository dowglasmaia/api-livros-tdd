package org.maia.livro.restcontroller;

import lombok.RequiredArgsConstructor;
import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.maia.livro.dtos.LoanDTO;
import org.maia.livro.services.interfaces.BookServices;
import org.maia.livro.services.interfaces.LoanServices;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor  //para o spring fazer a injeção dos servicos -  pelo lombok
public class LoanController {

    private final LoanServices loanServices;
    private final BookServices bookServices;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create (@RequestBody LoanDTO dto){
        Book book = bookServices.getBookByIsbn(dto.getIsbn()).get();
        Loan entity = Loan.builder()
                .book(book)
                .costumer(dto.getCostumer())
                .loanDate(LocalDate.now())
                .build();

        Loan newEntity = loanServices.save(entity);
        return newEntity.getId();
    }


}
