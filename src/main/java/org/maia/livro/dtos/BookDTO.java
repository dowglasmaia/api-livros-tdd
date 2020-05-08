package org.maia.livro.dtos;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;

    @NotEmpty(message = "Informe o Tituto do Livro")
    private String title;

    @NotEmpty(message = "Informe o Author do Livro")
    private String author;

    @NotEmpty(message = "Informe o ISBN do Livro")
    private String isbn;

}
