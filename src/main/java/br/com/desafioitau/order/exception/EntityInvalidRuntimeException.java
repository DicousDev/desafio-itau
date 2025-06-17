package br.com.desafioitau.order.exception;

public class EntityInvalidRuntimeException extends RuntimeException {

    public EntityInvalidRuntimeException(String mensagem) {
        super(mensagem);
    }
}
