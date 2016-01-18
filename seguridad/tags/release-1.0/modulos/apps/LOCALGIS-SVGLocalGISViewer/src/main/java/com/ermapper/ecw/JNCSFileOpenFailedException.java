package com.ermapper.ecw;

/**
 * Excepción que se lanza si se produce un error al abrir el fichero
 */
public class JNCSFileOpenFailedException extends JNCSException
{

    public JNCSFileOpenFailedException()
    {
    }

    public JNCSFileOpenFailedException(String s)
    {
        super(s);
    }
}
