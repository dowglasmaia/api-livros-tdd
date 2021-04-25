package org.maia.livro.restcontroller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.maia.livro.domain.Book;
import org.maia.livro.domain.Loan;
import org.maia.livro.dtos.BookDTO;
import org.maia.livro.dtos.LoanDTO;
import org.maia.livro.dtos.LoanFilterDTO;
import org.maia.livro.dtos.ReturnedLoadDTO;
import org.maia.livro.services.interfaces.BookServices;
import org.maia.livro.services.interfaces.LoanServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor  //para o spring fazer a injeção dos servicos -  pelo lombok
public class LoanController {

    private final LoanServices loanServices;
    private final BookServices bookServices;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create (@RequestBody LoanDTO dto){

        Book book = bookServices.getBookByIsbn(dto.getIsbn()).orElseThrow(
                () -> new ResponseStatusException( HttpStatus.BAD_REQUEST, "Book not found for passed isbn") ) ;//new ObjectNotFoundException("Book not found for passed isbn"

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
            @RequestBody ReturnedLoadDTO dto ){

        Loan loan =  loanServices.getById(id).orElseThrow(
                () -> new ResponseStatusException( HttpStatus.NOT_FOUND,"Book not found for passed id: "+id )  );
        loan.setReturned(dto.getReturned() );

        loanServices.update(loan);
    }


    @GetMapping
    public Page<LoanDTO> findFilter(LoanFilterDTO filterDTO, Pageable pageRequest) {
        Page<Loan> result = loanServices.find(filterDTO, pageRequest);

       List<LoanDTO> loans =  result
                .getContent()
                .stream()
                .map( entity -> {
                  Book book = entity.getBook();
                  BookDTO bookDTO = modelMapper.map( book, BookDTO.class );
                  LoanDTO loanDTO = modelMapper.map( entity, LoanDTO.class );
                  loanDTO.setBook(bookDTO);
                  return loanDTO;
                }).collect(Collectors.toList());

        return new PageImpl<LoanDTO>(loans, pageRequest, result.getTotalElements());

    }

}
