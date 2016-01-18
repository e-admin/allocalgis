package com.ermapper.util;

/**
 * Punto en coordenadas de la vista de la imagen ECW
 */
public class JNCSDatasetPoint
{

    public JNCSDatasetPoint()
    {
        x = 0;
        y = 0;
    }

    public JNCSDatasetPoint(int i, int j)
    {
        x = i;
        y = j;
    }

    public int x;
    public int y;
}
