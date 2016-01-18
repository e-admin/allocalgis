package com.geopista.server.administradorCartografia;

public class CancelException extends Exception{
    public Throwable cause=null;
    
    public CancelException(){
    	
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public CancelException(String sMsg){
        super(sMsg);
    }

    public CancelException(Throwable cause){
        this.cause=cause;
    }
}
