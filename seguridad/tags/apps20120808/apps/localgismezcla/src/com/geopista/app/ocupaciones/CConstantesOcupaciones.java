package com.geopista.app.ocupaciones;

import com.geopista.security.GeopistaPrincipal;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;

import java.text.DateFormat;
import java.text.SimpleDateFormat;




public class CConstantesOcupaciones {

	public static final String idApp="Licencias";

	public static String selectedSubApp="0";
	public static final String LicenciasObraMayor="0";
	public static final String LicenciasObraMenor="1";
	public static final String LicenciasActividad="2";
	public static final String Ocupacion="4";
    public static String tipoOcupacion="2";


	public static int MaxLengthDNI = 10;
	public static int MaxLengthNombre = 32;
	public static int MaxLengthApellido = 32;
	public static int MaxLengthColegio = 32;
	public static int MaxLengthVisado = 32;
	public static int MaxLengthTitulacion = 128;

	public static int MaxLengthTelefono = 20;
	public static int MaxLengthEmail = 32;

	public static int MaxLengthUnidad = 32;
	public static int MaxLengthMotivo = 254;
	public static int MaxLengthAsunto = 254;
    public static int MaxLengthAsuntoExpediente = 128;
    public static int MaxLengthTasa = 20;
	public static int MaxLengthObservaciones = 254;

	public static int MaxLengthTipoVia = 20;
	public static int MaxLengthNombreVia = 128;
	public static int MaxLengthNumeroVia = 10;
	public static int MaxLengthPortal = 10;
	public static int MaxLengthPlanta = 10;
	public static int MaxLengthLetra = 10;
	public static int MaxLengthCPostal = 5;
	public static int MaxLengthMunicipio = 64;
	public static int MaxLengthProvincia = 32;

    public static int MaxLengthServicioEncargado= 128;
    public static int MaxLengthFormaInicio= 64;
    public static int MaxLengthNumFolios= 4;
    public static int MaxLengthResponsableExpediente= 128;
    public static int MaxLengthNumDias= 3;
    public static int MaxLengthHora= 5;


    /** Tipos de licencia */
	public static int ObraMayor = 0;
	public static int ObraMenor = 1;
	public static int Actividades = 2;

    /** Estado OK de la Solicitud */
    public static int StepNotOK= 0;
    public static int StepOK= 1;
    public static int EstadoNoOK= 9;

    /** Estados de expediente que requieren notificacion */
    public static int EsperaMejora= 1;

	public static int IdMunicipio = 34083;
	public static String Provincia = null;
	public static String Municipio = null;

	public static DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	//public static String country = "ES";
	//public static String language = "es";
    public static String Locale="es_ES";

	public static String searchValue="";
	public static String calendarValue="";


    /** Formatos de salida de los informes */
    //public static int formatoHTML= 0;
    public static int formatoPDF= 0;
    public static int formatoTEXTO= 1;
    public static int formatoPREVIEW= 2;
    public static int formatoPRINT= 3;

    /** Ultimos eventos */
    public static int N_UltimosEventos= 0;

    /** Opcion de búsqueda para las ComboBox */
    public static String opcionDefecto= "TODOS";

    /** Operaciones sobre los anexos */
    public static final int CMD_ANEXO_ADDED = 201;
    public static final int CMD_ANEXO_DELETED = 202;

    /** Estados de una notificacion que actualizan fecha en BD */
    public static final int ID_ESTADO_NOTIFICADA= 3;
    public static final int ID_ESTADO_PENDIENTE_REENVIO= 2;

    /** Usuario de login */
    public static GeopistaPrincipal principal= new GeopistaPrincipal();

    /** Operaciones sobre el historico */
    public static final int CMD_HISTORICO_ADDED = 201;
    public static final int CMD_HISTORICO_MODIFIED = 202;
    public static final int CMD_HISTORICO_DELETED = 203;
    public static int OPERACION_HISTORICO= -1;


    /** Base de Datos */
    public static String DriverClassName= "";
    public static String ConnectionInfo= "";
    public static String DBName= "";
    public static String DBUser= "";
    public static String DBPassword= "";

    /** Ayuda */
    public static String helpSetHomeID= "geopistaIntro";

    /** Opcion de busqueda TODOS en funcion del idioma */
    public static String LocalCastellano= "es_ES";
    public static String LocalCatalan= "ca_ES";
    public static String LocalEuskera= "eu_ES";
    public static String LocalValenciano= "va_ES";
    public static String LocalGallego= "ga_ES";

    public static String opcionDefectoCastellano= "TODOS";
    public static String opcionDefectoCatalan= "(c)TODOS";
    public static String opcionDefectoEuskera= "(e)TODOS";
    public static String opcionDefectoValenciano= "(v)TODOS";
    public static String opcionDefectoGallego= "(g)TODOS";

    /** Tamanno Total maximo permitido en los anexos */
    public static long totalMaxSizeFilesUploaded= 1024 * 1000; // 1M

    /** Estado de apertura de expediente */
    public static String patronAperturaExpediente= "1";

    /** Patron del tipo de notificacion por email */
    public static String patronNotificacionEmail= "1";

    /** Patron del tipo de Ocupacion (para las concesiones) */
    public static String patronTipoOcupacion;

    public static CPersonaJuridicoFisica persona;
    public static CPersonaJuridicoFisica representante;



}