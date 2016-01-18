package com.geopista.app.utilidades;


public class CUtilidades
{

    public CUtilidades()
    {
    }

    public static boolean isWindows()
    {
        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase();
        return osName.indexOf("windows") != -1;
    }
}
