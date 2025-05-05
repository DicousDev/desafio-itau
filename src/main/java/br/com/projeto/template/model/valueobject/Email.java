package br.com.projeto.template.model.valueobject;

import br.com.projeto.template.exception.EntidadeInvalidaRuntimeException;
import br.com.projeto.template.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Email {

    @Column(name = "email")
    private String valor;

    public static Email of(String email) {
        return new Email(email);
    }

    private Email() {}

    public Email(String valor) {
        validaEmail(valor);
        this.valor = valor;
    }

    private void validaEmail(String email) {
        if(Objects.isNull(email)) {
            throw new EntidadeInvalidaRuntimeException("Email não pode ser nulo.");
        }

        if(StringUtils.isBlank(email) || !email.contains("@")) {
            throw new EntidadeInvalidaRuntimeException("Email [%s] inválido.".formatted(email));
        }
    }


    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Email{" +
                "valor='" + valor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(valor, email.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(valor);
    }
}
