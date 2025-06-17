package br.com.desafioitau.order.validator;

import br.com.desafioitau.order.exception.EntityInvalidRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class GenericValidatorTest {

    @Test
    public void shouldFailValidation() {
        Assertions.assertThatThrownBy(() -> GenericValidator.validation("fail error", () -> true))
                .hasMessage("fail error")
                .isInstanceOf(EntityInvalidRuntimeException.class);
    }

    @Test
    public void shouldValidateNotNull() {
        Assertions.assertThatThrownBy(() -> GenericValidator.validateNotNull(null, "fail error"))
                .hasMessage("fail error")
                .isInstanceOf(EntityInvalidRuntimeException.class);
    }

    @Test
    public void shouldValidateMaxLength() {
        Assertions.assertThatThrownBy(() -> GenericValidator.validateMaxLength("abc", 1, "exceeded character limit"))
                .hasMessage("exceeded character limit")
                .isInstanceOf(EntityInvalidRuntimeException.class);
    }

}
