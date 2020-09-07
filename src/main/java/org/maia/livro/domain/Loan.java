package org.maia.livro.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Loan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String costumer;

    @JoinColumn(name = "id_book")
    @ManyToOne
    private Book book;

    @Column
    @JsonFormat( pattern = "dd/MM/yyyy")
    private LocalDate loanDate;

    @Column
    private Boolean returned;
}
