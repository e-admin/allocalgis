package com.ermapper.util;

/**
 * Punto en coordenadas de pantalla
 */
public class JNCSScreenPoint
{

    public JNCSScreenPoint()
    {
        x = 0;
        y = 0;
    }

    public JNCSScreenPoint(int i, int j)
    {
        x = i;
        y = j;
    }

    public int x;
    public int y;
}
