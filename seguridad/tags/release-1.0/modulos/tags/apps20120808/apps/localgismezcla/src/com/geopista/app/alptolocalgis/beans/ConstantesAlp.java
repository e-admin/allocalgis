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

public class ConstantesAlp
{
	public static GeopistaPrincipal principal;
	public static Hashtable permisos= new Hashtable();
	
    //Acciones sobre BD.
    public static final int ACTION_OBTENER_OPERACIONES = 0;
    public static final int ACTION_OBTENER_OPERACIONES_FILTRO = 1;
    public static final int ACTION_GET_ID_USUARIO = 2;
    public static final int ACTION_ELIMINAR_OPERACIONES = 3;
    public static final int ACTION_OBTENER_IDMAPA = 4;
    
    public static AlpClient clienteAlp = null;
    
    public static final String idApp= "Alp";
    
    public static String Locale= "es_ES";
    public static String LocalCastellano= "es_ES";
    
    public static String helpSetHomeID= "AlpIntro";    
    
    public static int IdMunicipio= 34083;
    public static int IdEntidad= -1;
    public static String Provincia = null;
	public static String Municipio = null;
	public static String Entidad = null;
    
    public static final String ACTION_ADD_VIA = "AV";
    public static final String ACTION_ADD_NUMERO_POLICIA = "AN";
    public static final String ACTION_MOD_VIA = "MV";
    public static final String ACTION_MOD_NUMERO_POLICIA = "MN";
    public static final String ACTION_DEL_VIA = "BV";
    public static final String ACTION_DEL_NUMERO_POLICIA = "BN";
    
    public static final String ADD = "Alta";
    public static final String DEL = "Baja";
    public static final String MOD = "Modificación";
    
    public static final String NUMBER = "Portal";
    public static final String VIA = "Via";
    
    public static final String FILTRO = "Filtro";
    public static final String IDOPERACION = "IdOperacion";
    public static final String NOMBREMAPA = "NombreMapa";
  
    public static DecimalFormat decimalFormatter = new DecimalFormat("00000.00");




}
