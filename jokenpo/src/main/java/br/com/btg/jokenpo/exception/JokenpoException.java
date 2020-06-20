package br.com.btg.jokenpo.exception;

import br.com.btg.jokenpo.enums.EnumException;

public class JokenpoException extends Exception {

	private static final long serialVersionUID = 7903429826465890495L;

	public JokenpoException(EnumException enumException){
        super(enumException.getCode() + " - " + enumException.getMessage());
    }

    public JokenpoException(String errorMessage){
        super(errorMessage);
    }
}
