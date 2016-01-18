package com.ermapper.ecw;

/**
 * Excepción que se lanza cuando se realiza una operación no permitida sobre un fichero que aún no ha sido abierto 
 */
public class JNCSFileNotOpenException extends JNCSException
{

    public JNCSFileNotOpenException()
    {
    }

    public JNCSFileNotOpenException(String s)
    {
        super(s);
    }
}
