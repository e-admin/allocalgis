package com.geopista.server.catastro.intercambio.importacion.utils;

public class EdicionUtilsCatastroServer {

    /**
     ** Rellena una cadena de caracteres S hasta un número N de caracteres con caracteres C
     ** a la izquierda (true) o la derecha (false)
     **/
    public static String paddingString ( String s, int n, char c , boolean paddingLeft  ) {
        if (s==null)
            return null;
        
        StringBuffer str = new StringBuffer(s);
        int strLength  = str.length();
        if ( n > 0 && n > strLength ) {
            for ( int i = 0; i <= n ; i ++ ) {
                if ( paddingLeft ) {
                    if ( i < n - strLength ) str.insert( 0, c );
                }
                else {
                    if ( i > strLength ) str.append( c );
                }
            }
        }
        return str.toString();
    }
	
}
