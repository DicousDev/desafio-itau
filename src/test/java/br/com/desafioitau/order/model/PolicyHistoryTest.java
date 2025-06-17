package br.com.desafioitau.order.model;

import br.com.desafioitau.order.exception.EntityInvalidRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class PolicyHistoryTest {

    @ParameterizedTest
    @MethodSource(value = "shouldFailBuildSource")
    public void shouldFailBuild(PolicyHistory.PolicyHistoryBuilder policyHistory, String error) {
        Assertions.assertThatThrownBy(() -> policyHistory.build())
                .hasMessage(error)
                .isInstanceOf(EntityInvalidRuntimeException.class);
    }

    public static Stream<Arguments> shouldFailBuildSource() {
        return Stream.of(
                Arguments.of(PolicyHistory.builder().status(null).createdAt(LocalDateTime.now()), "Status cannot be null."),
                Arguments.of(PolicyHistory.builder().status(PolicySolicitation.PENDING).createdAt(null), "Created at cannot be null.")
        );
    }
}
