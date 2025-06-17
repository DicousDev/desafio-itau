package br.com.desafioitau.order.validator;

import br.com.desafioitau.order.exception.EntityInvalidRuntimeException;

import java.util.Objects;
import java.util.function.BooleanSupplier;

public class GenericValidator {

    public static void validation(String erro, BooleanSupplier validations) {
        if(validations.getAsBoolean()) {
            throw new EntityInvalidRuntimeException(erro);
        }
    }

    public static void validateNotNull(Object target, String erro) {
        validation(erro, () -> Objects.isNull(target));
    }

    public static void validateMaxLength(String value, int maxSize, String erro) {
        validation(erro, () -> Objects.nonNull(value) && value.length() > maxSize);
    }
}
