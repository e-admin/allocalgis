package com.geopista.app.alptolocalgis.beans;

import java.text.DecimalFormat;
import java.util.Hashtable;
import com.geopista.app.alptolocalgis.AlpClient;
import com.geopista.security.GeopistaPrincipal;

/**
 * Clase utilizada por el cliente y el servidor que define las constantes de las acciones que el cliente desea hacer
 * en la parte servidora. En la parte servidora se comparara la accion obtenida y se realizara la accion deseada. El
 * resto de constantes son para permitir obtener los objetos de la hash params en el envio de la peticion al servidor
 * o utilzadas en el cliente y servidor.
 * */

public class ConstantesAlp_LCGIII
{
	public static GeopistaPrincipal principal;
    
    public static AlpClient clienteAlp = null;

}
