package com.ermapper.ecw;

/**
 * Listener para la carga progresiva de imágenes
 */
public interface JNCSProgressiveUpdate
{

    public abstract void refreshUpdate(int i, int j, double d, double d1, double d2, double d3);

    public abstract void refreshUpdate(int i, int j, int k, int l, int i1, int j1);
}
