package br.com.desafioitau.order.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import support.PolicyProvider;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class CustomerNoInformationTest {

    private final CustomerNoInformation customerNoInformation = new CustomerNoInformation();

    @ParameterizedTest
    @MethodSource("shouldReturnPendingSource")
    public void shouldReturnPending(Policy policy) {
        Assertions.assertThat(customerNoInformation.analyse(policy))
                .isEqualTo(PolicySolicitation.PENDING);
    }

    public static Stream<Arguments> shouldReturnPendingSource() {
        return Stream.of(
                Arguments.of(PolicyProvider.create().category(InsuranceCategory.LIFE).insuredAmount(BigDecimal.valueOf(20000)).build()),
                Arguments.of(PolicyProvider.create().category(InsuranceCategory.RESIDENTIAL).insuredAmount(BigDecimal.valueOf(20000)).build()),
                Arguments.of(PolicyProvider.create().category(InsuranceCategory.AUTO).insuredAmount(BigDecimal.valueOf(75000)).build()),
                Arguments.of(PolicyProvider.create().category(InsuranceCategory.BUSINESS).insuredAmount(BigDecimal.valueOf(55000)).build())
        );
    }

    @ParameterizedTest
    @MethodSource("shouldReturnRejectedSource")
    public void shouldReturnRejected(Policy policy) {
        Assertions.assertThat(customerNoInformation.analyse(policy))
                .isEqualTo(PolicySolicitation.REJECTED);
    }

    public static Stream<Arguments> shouldReturnRejectedSource() {
        return Stream.of(
                Arguments.of(PolicyProvider.create().category(InsuranceCategory.LIFE).insuredAmount(BigDecimal.valueOf(20001)).build()),
                Arguments.of(PolicyProvider.create().category(InsuranceCategory.RESIDENTIAL).insuredAmount(BigDecimal.valueOf(20001)).build()),
                Arguments.of(PolicyProvider.create().category(InsuranceCategory.AUTO).insuredAmount(BigDecimal.valueOf(75001)).build()),
                Arguments.of(PolicyProvider.create().category(InsuranceCategory.BUSINESS).insuredAmount(BigDecimal.valueOf(55001)).build())
        );
    }
}
