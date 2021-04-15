package it.unicam.c3.Exception;

public class EmailSyntaxException extends IllegalArgumentException{

    private static final long serialVersionUID = 1L;

    public EmailSyntaxException() {
        super("Sintassi email errata");
    }

}
