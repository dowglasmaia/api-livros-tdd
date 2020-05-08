package org.maia.livro.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maia.livro.domain.Book;
import org.maia.livro.dtos.BookDTO;
import org.maia.livro.services.BookServicesImpl;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    static  String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookServicesImpl services;


    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void createBookTest()  throws  Exception{
        BookDTO dto = BookDTO.builder().author("Dowglas Maia").title("Game of Thrones Vol 05").isbn("Vol 5 03º Ed").build();

        Book saveBook = Book.builder().author("Dowglas Maia").title("Game of Thrones Vol 05").isbn("Vol 5 03º Ed").id(1L).build();

        BDDMockito.given(services.save(Mockito.any(Book.class))).willReturn(saveBook);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()));

    }

    @Test
    @DisplayName("Deve lançar error de validação quando não houver dados suficientes para criação do livro.")
    public void createInvalidBookTest()  throws  Exception {

        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(3))); // Matchers.hasSize(3) - numero de campos qual estarão invalidos

    }

}
