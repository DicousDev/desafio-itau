package br.com.projeto.template.model.valueobject;

import br.com.projeto.template.exception.EntidadeInvalidaRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class EmailTest {

    @ParameterizedTest
    @MethodSource("examplesEmailInvalid")
    public void assertExceptionErrors(String email, String erro) {
        Assertions.assertThatThrownBy(() -> Email.of(email))
                .isInstanceOf(EntidadeInvalidaRuntimeException.class)
                .hasMessage(erro);
    }

    public static Stream<Arguments> examplesEmailInvalid() {
        return Stream.of(
                Arguments.of(null, "Email não pode ser nulo."),
                Arguments.of(" ", "Email [ ] inválido."),
                Arguments.of("joao", "Email [joao] inválido.")
        );
    }
}
