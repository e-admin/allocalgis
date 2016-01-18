package com.geopista.ui.plugin.importer.beans;

import java.text.DecimalFormat;

import com.geopista.security.GeopistaPrincipal;
import com.geopista.ui.plugin.importer.ImporterClient;

/**
 * Clase utilizada por el cliente y el servidor que define las constantes de las acciones que el cliente desea hacer
 * en la parte servidora. En la parte servidora se comparara la accion obtenida y se realizara la accion deseada. El
 * resto de constantes son para permitir obtener los objetos de la hash params en el envio de la peticion al servidor
 * o utilzadas en el cliente y servidor.
 * */

public class ConstantesImporter
{
	public static GeopistaPrincipal principal;
	
    //Acciones sobre BD.
    public static final int ACTION_GET_DOMAIN_VALUES = 0;
    public static final int ACTION_OBTENER_OPERACIONES_FILTRO = 1;
    public static final int ACTION_GET_ID_USUARIO = 2;
    public static final int ACTION_ELIMINAR_OPERACIONES = 3;
    public static final int ACTION_OBTENER_IDMAPA = 4;
    
    public static final String idImporter= "Importer";
    public static ImporterClient clienteImporter = null;
    
    public static String Locale= "es_ES";
    public static String LocalCastellano= "es_ES";
    
    public static int IdMunicipio= 34083;
    
    public static final String ACTION_ADD_ASSOCIATION = "AA";
    public static final String ACTION_DEL_ASSOCIATION = "DA";
    
    public static final String ADD = "Alta";
    public static final String DEL = "Baja";

	public static final String IDDOMAIN = "ID_DOMAIN";
	public static final String IDLOCALE = "ID_LOCALE";
   
    public static DecimalFormat decimalFormatter = new DecimalFormat("00000.00");
    
    
    public static final String[] erroresFeatureArray = {
    	"Unrecognized symbol", 
    	"Unexpected \"<EOF>\" at column", 
    	"Invalid parameter type"};

    public static final String errorAtributoNoNulo = "NotNullableField";
    public static final String errorNoExpression ="No expression entered\n";
    
    public static final String IDVALIDATED="VALIDATE_MARKED";
    
    public static final int NUMTESTFEATURES = 10;
    public static final String DATEFORMAT = "dd-MM-yyyy"; 

}
