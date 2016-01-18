package com.geopista.server.administradorCartografia;

public class ACException extends Exception{
    public Throwable cause=null;

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public ACException(String sMsg){
        super(sMsg);
    }

    public ACException(Throwable cause){
        this.cause=cause;
    }
}
