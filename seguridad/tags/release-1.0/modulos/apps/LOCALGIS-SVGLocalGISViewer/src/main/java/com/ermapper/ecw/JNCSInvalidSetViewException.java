package com.ermapper.ecw;

/**
 * Excepción que se lanza cuando se intenta establecer una vista inválida para la imagen
 */
public class JNCSInvalidSetViewException extends JNCSException
{

    public JNCSInvalidSetViewException()
    {
    }

    public JNCSInvalidSetViewException(String s)
    {
        super(s);
    }
}
