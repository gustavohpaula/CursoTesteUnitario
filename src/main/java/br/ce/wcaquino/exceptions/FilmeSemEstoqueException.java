package br.ce.wcaquino.exceptions;

public class FilmeSemEstoqueException extends Exception {
    private static final long serialVersionUID = 1;

    public FilmeSemEstoqueException(String message) {
        super(message);
    }
}
