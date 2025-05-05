package br.com.projeto.template.model.valueobject;

import br.com.projeto.template.exception.EntidadeInvalidaRuntimeException;
import br.com.projeto.template.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;


@Embeddable
public class CNPJ {

    @Column(name = "cnpj")
    private String valor;
    private static int CNPJ_TAMANHO_VALIDO = 14;

    public static CNPJ of(String cnpj) {
        return new CNPJ(cnpj);
    }

    private CNPJ() {}

    private CNPJ(String cnpj) {
        validaCNPJ(cnpj);
        this.valor = cnpj;
    }

    private void validaCNPJ(String cnpj) {
        if(Objects.isNull(cnpj)) {
            throw new EntidadeInvalidaRuntimeException("CNPJ não pode ser nulo.");
        }

        String cnpjSemEspacos = StringUtils.retirarTodosEspacosEmBranco(cnpj);
        if(cnpjSemEspacos.length() != CNPJ_TAMANHO_VALIDO || Boolean.FALSE.equals(StringUtils.isNumericOnly(cnpj))) {
            throw new EntidadeInvalidaRuntimeException("CNPJ [%s] inválido.".formatted(cnpj));
        }
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "CNPJ{" +
                "valor='" + valor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        CNPJ cnpj = (CNPJ) o;
        return Objects.equals(valor, cnpj.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(valor);
    }
}
