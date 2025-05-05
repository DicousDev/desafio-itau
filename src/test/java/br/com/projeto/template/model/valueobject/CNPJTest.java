package br.com.projeto.template.model.valueobject;

import br.com.projeto.template.exception.EntidadeInvalidaRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class CNPJTest {

    @ParameterizedTest
    @MethodSource("examplesCnpjInvalid")
    public void assertExceptionErrors(String cnpj, String erro) {
        Assertions.assertThatThrownBy(() -> CNPJ.of(cnpj))
                .isInstanceOf(EntidadeInvalidaRuntimeException.class)
                .hasMessage(erro);
    }

    public static Stream<Arguments> examplesCnpjInvalid() {
        return Stream.of(
                Arguments.of(null, "CNPJ não pode ser nulo."),
                Arguments.of(" ", "CNPJ [ ] inválido."),
                Arguments.of("198252300 0118", "CNPJ [198252300 0118] inválido."),
                Arguments.of("198252@00a0118", "CNPJ [198252@00a0118] inválido.")
        );
    }
}
