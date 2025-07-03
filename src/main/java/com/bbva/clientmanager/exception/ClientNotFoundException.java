package com.bbva.clientmanager.exception;

public class ClientNotFoundException extends RuntimeException{

    public ClientNotFoundException(String message) {
        super(message);
    }
}
