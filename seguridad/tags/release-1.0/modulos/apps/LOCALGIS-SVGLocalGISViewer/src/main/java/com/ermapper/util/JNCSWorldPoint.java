package com.ermapper.util;

/**
 * Punto en coordenadas del mundo real (geográficas, UTM, etc)
 */

public class JNCSWorldPoint
{

    public JNCSWorldPoint()
    {
        x = 0.0D;
        y = 0.0D;
        z = 0.0D;
    }

    public JNCSWorldPoint(double d, double d1)
    {
        x = d;
        y = d1;
    }

    public JNCSWorldPoint(double d, double d1, double d2)
    {
        x = d;
        y = d1;
        z = d2;
    }

    public double x;
    public double y;
    public double z;
}
