package br.com.projeto.template.model.valueobject;

import br.com.projeto.template.exception.EntidadeInvalidaRuntimeException;
import br.com.projeto.template.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CPF {

    @Column(name = "cpf")
    private String valor;
    private static int CPF_TAMANHO_VALIDO = 11;


    public static CPF of(String cpf) {
        return new CPF(cpf);
    }

    private CPF() {}

    private CPF(String valor) {
        validaCPF(valor);
        this.valor = valor;
    }

    private void validaCPF(String cpf) {
        if(Objects.isNull(cpf)) {
            throw new EntidadeInvalidaRuntimeException("CPF não pode ser nulo.");
        }

        String cpfSemEspacos = StringUtils.retirarTodosEspacosEmBranco(cpf);
        if(cpfSemEspacos.length() != CPF_TAMANHO_VALIDO || Boolean.FALSE.equals(StringUtils.isNumericOnly(cpf))) {
            throw new EntidadeInvalidaRuntimeException("CPF [%s] inválido.".formatted(cpf));
        }
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "CPF{" +
                "valor='" + valor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CPF cpf = (CPF) o;
        return Objects.equals(valor, cpf.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(valor);
    }
}
