package br.com.desafioitau.order.model;

import br.com.desafioitau.order.exception.EntityInvalidRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssistanceTest {

    @Test
    public void shouldCreateAssistance() {
        var assistance = Assistance.builder().assistance("T".repeat(255)).build();
        Assertions.assertThat(assistance.getAssistance()).isNotNull();
    }

    @Test
    public void shouldFailDueToFieldExceedingMaximumCharacterLimit() {
        Assertions.assertThatThrownBy(() -> Assistance.builder().assistance("T".repeat(256)).build())
                .isInstanceOf(EntityInvalidRuntimeException.class)
                .hasMessage("Assistance exceeded character limit");
    }
}
