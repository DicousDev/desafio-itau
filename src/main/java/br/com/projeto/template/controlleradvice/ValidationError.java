package br.com.projeto.template.controlleradvice;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationError {

    private String field;
    private String value;
}
