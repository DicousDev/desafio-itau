package br.com.desafioitau.order.exception;

public class EntityNotFoundRuntimeException extends RuntimeException {

    public EntityNotFoundRuntimeException(String mensagem) {
        super(mensagem);
    }
}
