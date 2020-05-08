package org.maia.livro.exception;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class ApiErrors {

    private List<String> errors = new ArrayList<>();

    public ApiErrors(BindingResult bindingResult) {
        //Pega a lista de Error a joga para o atributo errors
        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public List<String> getErrors() {
        return errors;
    }
}
