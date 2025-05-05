package br.com.projeto.template.model.valueobject;

import br.com.projeto.template.exception.EntidadeInvalidaRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class CPFTest {

    @ParameterizedTest
    @MethodSource("examplesCpfInvalid")
    public void assertExceptionErrors(String cpf, String erro) {
        Assertions.assertThatThrownBy(() -> CPF.of(cpf))
                .isInstanceOf(EntidadeInvalidaRuntimeException.class)
                .hasMessage(erro);
    }

    public static Stream<Arguments> examplesCpfInvalid() {
        return Stream.of(
                Arguments.of(null, "CPF não pode ser nulo."),
                Arguments.of(" ", "CPF [ ] inválido."),
                Arguments.of("19 825 2300", "CPF [19 825 2300] inválido."),
                Arguments.of("19@825a2300", "CPF [19@825a2300] inválido.")
        );
    }
}
