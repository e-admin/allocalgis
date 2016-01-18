package com.ermapper.ecw;

/**
 * Excepción genérica al utilizar la librería de decompresión de imágenes ECW.
 */
public class JNCSException extends Exception {

    public JNCSException()
    {
    }

    public JNCSException(String s)
    {
        super(s);
    }
}
