package com.gestorfip.ws.xml.parser;

import java.util.Stack;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gestorfip.ws.utils.Constants;
import com.gestorfip.ws.utils.UtilsWS;
import com.gestorfip.ws.xml.beans.importacion.catalogosistematizado.CatalogoSistematizadoBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.AmbitoBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.CaracterDeterminacionBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.DiccionarioBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.GrupoDocumentoBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.InstrumentoBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.OperacionCaracterBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.RelacionBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.TipoAmbitoBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.TipoDocumentoBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.TipoEntidadBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.TipoOperacionDeterminacionBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.TipoOperacionEntidadBean;
import com.gestorfip.ws.xml.beans.importacion.diccionario.TipoTramiteBean;
import com.gestorfip.ws.xml.beans.importacion.fip.FIP1Bean;
import com.gestorfip.ws.xml.beans.importacion.planeamientoencargado.PlaneamientoEncargadoBean;
import com.gestorfip.ws.xml.beans.importacion.planeamientovigente.PlaneamientoVigenteBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.TramiteBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.adscripcion.AdscripcionBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.aplicacionambito.AplicacionAmbitoBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.condicionurbanistica.CasoBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.condicionurbanistica.Caso_DocumentoBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.condicionurbanistica.Caso_RegimenBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.condicionurbanistica.Caso_Regimen_RegimenEspecificoBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.condicionurbanistica.Caso_VinculoBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.condicionurbanistica.CondicionUrbanisticaBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.determinacion.DeterminacionBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.determinacion.DeterminacionReguladoraBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.determinacion.GrupoAplicacionBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.determinacion.RegulacionEspecificaBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.determinacion.ValorReferenciaBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.documento.DocumentoBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.documento.HojaBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.entidad.EntidadBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.operacion.OperacionDeterminacionBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.operacion.OperacionEntidadBean;
import com.gestorfip.ws.xml.beans.importacion.tramite.unidad.UnidadBean;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;


/**
 * This class implements the SAX handler whose objective is to parse the FIP1
 * file. The retrieved data is stored inside the FIP1Bean attributes which is
 * then used to populate the database. 
 * 
 * @author davidriou
 *
 */
public class FIP1Handler extends DefaultHandler {

	static Logger logger = Logger.getLogger(FIP1Handler.class);
	
	private String currentTag = ""; //$NON-NLS-1$
	
	// For geometry data process
	private StringBuffer geometryBuffer = new StringBuffer();
	private boolean isNewGeometry = true;
	private boolean isPolygon = false;
	private boolean isMultipolygon = false;
	private boolean isLineString = false;
	private boolean isMultiLineString = false;
	private boolean isPoint = false;
	private boolean isMultiPoint = false;
	
	// For text data process
	private StringBuffer textBuffer = new StringBuffer();
	private String currentDeterminacionCode = "";
	private int currentRegulacionEspecificaOrden = 0;
	private String currentRegulacionEspecificaNombre = "";
	private String currentRegulacionEspeceficaDeterminacionCode = "";
	
	private int currentRegimenEspecificoOrden = 0;
	private String currentRegimenEspecificoNombre = "";
	private String currentRegimenEspecificoCasoCode = "";
	
	private long currentOperacionDeterminacionOrden = 0;
	private String currentOperacionDeterminacionTipo = "";	

	private long currentOperacionEntidadOrden = 0;
	private String currentOperacionEntidadTipo = "";
	
	private String currentEntidadOrigen = "";
	private String currentEntidadDestino = "";
	
	private String currentTramiteCodigo = "";	

	private FIP1Bean fip1;
	private DiccionarioBean diccionario;
	private CatalogoSistematizadoBean catalogosistematizado;
	private TramiteBean currentTramite;
	private PlaneamientoVigenteBean planeamientovigente;
	private PlaneamientoEncargadoBean planeamientoencargado;

	public FIP1Handler() {
	}

	public FIP1Bean getFip1() {
		return fip1;
	}

	// Elementos del diccionario
	private TipoAmbitoBean currentTipoAmbito;
	private AmbitoBean currentAmbito;
	private RelacionBean currentRelacion;
	private CaracterDeterminacionBean currentCaracterDeterminacion;
	private TipoEntidadBean currentTipoEntidad;
	private TipoOperacionEntidadBean currentTipoOperacionEntidad;
	private TipoOperacionDeterminacionBean currentTipoOperacionDeterminacion;
	private OperacionCaracterBean currentOperacionCaracter;
	private TipoDocumentoBean currentTipoDocumento;
	private GrupoDocumentoBean currentGrupoDocumento;
	private TipoTramiteBean currentTipoTramite;
	private InstrumentoBean currentInstrumento;

	// Elementos del catalogo sistematizado
	private DocumentoBean currentDocumento;
	private HojaBean currentHoja;
	private EntidadBean currentEntidad;
	private com.gestorfip.ws.xml.beans.importacion.tramite.entidad.DocumentoBean currentDocumentoEntidad;
	private DeterminacionBean currentDeterminacion;
	private ValorReferenciaBean currentValorReferencia;
	private com.gestorfip.ws.xml.beans.importacion.tramite.determinacion.DocumentoBean currentDocumentoDeterminacion;
	private GrupoAplicacionBean currentGrupoAplicacion;
	private DeterminacionReguladoraBean currentDeterminacionReguladora;
	private RegulacionEspecificaBean currentRegulacionEspecifica;
	private CondicionUrbanisticaBean currentCondicionUrbanistica;
	private CasoBean currentCaso;
	private Caso_RegimenBean currentRegimen;
	private Caso_Regimen_RegimenEspecificoBean currentRegimenEspecifico;
	private Caso_VinculoBean currentVinculo;
	private Caso_DocumentoBean currentDocumentoCaso;
	private OperacionDeterminacionBean currentOperacionDeterminacion;
	private OperacionEntidadBean currentOperacionEntidad;
	private UnidadBean currentUnidad;
	private AplicacionAmbitoBean currentAplicacionAmbito;
	private AdscripcionBean currentAdscripcion;

	private Stack<EntidadBean> entidadesMadres = new Stack<EntidadBean>();
	private Stack<DeterminacionBean> determinacionesMadres = new Stack<DeterminacionBean>();
	private Stack<RegulacionEspecificaBean> regulacionesespecificasMadres = new Stack<RegulacionEspecificaBean>();
	private Stack<Caso_Regimen_RegimenEspecificoBean> regimenesEspecificosPadres = new Stack<Caso_Regimen_RegimenEspecificoBean>();

	private int hijasEntidadCounter = 0;
	private int hijasDeterminacionCounter = 0;
	private int hijasRegulacionesespecificasCounter = 0;
	private int hijosRegimenEspicificoCounter = 0;

	// Tags
	private final String FIP_TAG = Messages.getString("FIP11"); //$NON-NLS-1$
	private final String DICCIONARIO_TAG = Messages.getString("FIP12"); //$NON-NLS-1$
	private final String TIPOAMBITO_TAG = Messages.getString("FIP13"); //$NON-NLS-1$
	private final String AMBITO_TAG = Messages.getString("FIP14"); //$NON-NLS-1$
	private final String GEOMETRIA_TAG = Messages.getString("FIP15"); //$NON-NLS-1$
	private final String RELACION_TAG = Messages.getString("FIP16"); //$NON-NLS-1$
	private final String CARACTERDETERMINACION_TAG = Messages
			.getString("FIP17"); //$NON-NLS-1$
	private final String TIPOENTIDAD_TAG = Messages.getString("FIP18"); //$NON-NLS-1$
	private final String TIPOOPERACIONENTIDAD_TAG = Messages.getString("FIP19"); //$NON-NLS-1$
	private final String TIPOOPERACIONDETERMINACION_TAG = Messages
			.getString("FIP110"); //$NON-NLS-1$
	private final String OPERACIONCARACTER_TAG = Messages.getString("FIP111"); //$NON-NLS-1$
	private final String TIPODOCUMENTO_TAG = Messages.getString("FIP112"); //$NON-NLS-1$
	private final String GRUPODOCUMENTO_TAG = Messages.getString("FIP113"); //$NON-NLS-1$
	private final String TIPOTRAMITE_TAG = Messages.getString("FIP114"); //$NON-NLS-1$
	private final String INSTRUMENTO_TAG = Messages.getString("FIP115"); //$NON-NLS-1$
	private final String CATALOGOSISTEMATIZADO_TAG = Messages.getString("FIP1Handler.0"); //$NON-NLS-1$
	private final String PLANEAMIENTOVIGENTE_TAG = Messages.getString("FIP1Handler.1"); //$NON-NLS-1$
	private final String PLANEAMIENTOENCARGADO_TAG = Messages.getString("FIP1Handler.2"); //$NON-NLS-1$
	private final String TRAMITE_TAG = Messages.getString("FIP1Handler.3"); //$NON-NLS-1$
	private final String TEXTO_TAG = Messages.getString("FIP1Handler.4"); //$NON-NLS-1$
	private final String DOCUMENTO_TAG = Messages.getString("FIP1Handler.5"); //$NON-NLS-1$
	private final String DOCUMENTOREFUNDIDO_TAG = Messages.getString("FIP1Handler.61"); //$NON-NLS-1$
	private final String COMENTARIO_TAG = Messages.getString("FIP1Handler.6"); //$NON-NLS-1$
	private final String HOJA_TAG = Messages.getString("FIP1Handler.7"); //$NON-NLS-1$
	private final String ENTIDAD_TAG = Messages.getString("FIP1Handler.8"); //$NON-NLS-1$
	private final String HIJAS_TAG = Messages.getString("FIP1Handler.9"); //$NON-NLS-1$
	private final String BASE_TAG = Messages.getString("FIP1Handler.10"); //$NON-NLS-1$
	private final String DETERMINACION_TAG = Messages.getString("FIP1Handler.11"); //$NON-NLS-1$
	private final String VALORREFERENCIA_TAG = Messages.getString("FIP1Handler.12"); //$NON-NLS-1$
	private final String UNIDAD_TAG = Messages.getString("FIP1Handler.13"); //$NON-NLS-1$
	private final String DETERMINACIONREGULADORA_TAG = Messages.getString("FIP1Handler.14"); //$NON-NLS-1$
	private final String REGULACIONESPECIFICA_TAG = Messages.getString("FIP1Handler.15"); //$NON-NLS-1$
	private final String GRUPOAPLICACION_TAG = Messages.getString("FIP1Handler.16"); //$NON-NLS-1$
	private final String CONDICIONURBANISTICA_TAG = Messages.getString("FIP1Handler.17"); //$NON-NLS-1$
	private final String CASO_TAG = Messages.getString("FIP1Handler.18"); //$NON-NLS-1$
	private final String REGIMEN_TAG = Messages.getString("FIP1Handler.19"); //$NON-NLS-1$
	private final String REGIMENESPECIFICO_TAG = Messages.getString("FIP1Handler.20"); //$NON-NLS-1$
	private final String HIJOS_TAG = Messages.getString("FIP1Handler.21"); //$NON-NLS-1$
	private final String VALOR_TAG = Messages.getString("FIP1Handler.22"); //$NON-NLS-1$
	private final String DETERMINACIONREGIMEN_TAG = Messages.getString("FIP1Handler.23"); //$NON-NLS-1$
	private final String CASOAPLICACION_TAG = Messages.getString("FIP1Handler.24"); //$NON-NLS-1$
	private final String VINCULO_TAG = Messages.getString("FIP1Handler.25"); //$NON-NLS-1$
	private final String OPERACIONDETERMINACION_TAG = Messages.getString("FIP1Handler.26"); //$NON-NLS-1$
	private final String OPERADA_TAG = Messages.getString("FIP1Handler.27"); //$NON-NLS-1$
	private final String OPERADORA_TAG = Messages.getString("FIP1Handler.28"); //$NON-NLS-1$
	private final String OPERACIONENTIDAD_TAG = Messages.getString("FIP1Handler.29"); //$NON-NLS-1$
	private final String PROPIEDADESADSCRIPCION_TAG = Messages.getString("FIP1Handler.30"); //$NON-NLS-1$
	private final String TIPO_TAG = Messages.getString("FIP1Handler.31"); //$NON-NLS-1$
	private final String DEFINICION_TAG = Messages.getString("FIP1Handler.32"); //$NON-NLS-1$
	private final String APLICACIONAMBITO_TAG = Messages.getString("FIP1Handler.33"); //$NON-NLS-1$
	private final String ADSCRIPCION_TAG = Messages.getString("FIP1Handler.34"); //$NON-NLS-1$
	private final String PROPIEDADES_TAG = Messages.getString("FIP1Handler.35"); //$NON-NLS-1$
	private final String AMBITOAPLICACION_TAG = Messages.getString("FIP1Handler.36"); //$NON-NLS-1$
	
	// Atributos
	private final String PAIS_ATTRIBUTE = Messages.getString("FIP116"); //$NON-NLS-1$
	private final String FECHA_ATTRIBUTE = Messages.getString("FIP117"); //$NON-NLS-1$
	private final String VERSION_ATTRIBUTE = Messages.getString("FIP118"); //$NON-NLS-1$
	private final String SRS_ATTRIBUTE = Messages.getString("FIP119"); //$NON-NLS-1$
	private final String CODIGO_ATTRIBUTE = Messages.getString("FIP120"); //$NON-NLS-1$
	private final String NOMBRE_ATTRIBUTE = Messages.getString("FIP121"); //$NON-NLS-1$
	private final String INE_ATTRIBUTE = Messages.getString("FIP122"); //$NON-NLS-1$
	private final String TIPOAMBITO_ATTRIBUTE = Messages.getString("FIP123"); //$NON-NLS-1$
	private final String PADRE_ATTRIBUTE = Messages.getString("FIP124"); //$NON-NLS-1$
	private final String HIJO_ATTRIBUTE = Messages.getString("FIP125"); //$NON-NLS-1$
	private final String APLICACIONESMAX_ATTRIBUTE = Messages
			.getString("FIP126"); //$NON-NLS-1$
	private final String APLICACIONESMIN_ATTRIBUTE = Messages
			.getString("FIP127"); //$NON-NLS-1$
	private final String TIPOENTIDAD_ATTRIBUTE = Messages.getString("FIP128"); //$NON-NLS-1$
	private final String TIPOOPERACIONDETERMINACION_ATTRIBUTE = Messages
			.getString("FIP129"); //$NON-NLS-1$
	private final String CARACTEROPERADORA_ATTRIBUTE = Messages
			.getString("FIP130"); //$NON-NLS-1$
	private final String CARACTEROPERADA_ATTRIBUTE = Messages
			.getString("FIP131"); //$NON-NLS-1$
	private final String AMBITO_ATTRIBUTE = Messages.getString("FIP1Handler.37"); //$NON-NLS-1$
	private final String TIPOTRAMITE_ATTRIBUTE = Messages.getString("FIP1Handler.38"); //$NON-NLS-1$
	private final String ARCHIVO_ATTRIBUTE = Messages.getString("FIP1Handler.39"); //$NON-NLS-1$
	private final String TIPO_ATTRIBUTE = Messages.getString("FIP1Handler.40"); //$NON-NLS-1$
	private final String ESCALA_ATTRIBUTE = Messages.getString("FIP1Handler.41"); //$NON-NLS-1$
	private final String GRUPO_ATTRIBUTE = Messages.getString("FIP1Handler.42"); //$NON-NLS-1$
	private final String ETIQUETA_ATTRIBUTE = Messages.getString("FIP1Handler.43"); //$NON-NLS-1$
	private final String CLAVE_ATTRIBUTE = Messages.getString("FIP1Handler.44"); //$NON-NLS-1$
	private final String SUSPENDIDA_ATTRIBUTE = Messages.getString("FIP1Handler.45"); //$NON-NLS-1$
	private final String TRAMITE_ATTRIBUTE = Messages.getString("FIP1Handler.46"); //$NON-NLS-1$
	private final String ENTIDAD_ATTRIBUTE = Messages.getString("FIP1Handler.47"); //$NON-NLS-1$
	private final String CARACTER_ATTRIBUTE = Messages.getString("FIP1Handler.48"); //$NON-NLS-1$
	private final String APARTADO_ATTRIBUTE = Messages.getString("FIP1Handler.49"); //$NON-NLS-1$
	private final String DETERMINACION_ATTRIBUTE = Messages.getString("FIP1Handler.50"); //$NON-NLS-1$
	private final String ORDEN_ATTRIBUTE = Messages.getString("FIP1Handler.51"); //$NON-NLS-1$
	private final String SUPERPOSICION_ATTRIBUTE = Messages.getString("FIP1Handler.52"); //$NON-NLS-1$
	private final String CASO_ATTRIBUTE = Messages.getString("FIP1Handler.53"); //$NON-NLS-1$
	private final String CUANTIA_ATTRIBUTE = Messages.getString("FIP1Handler.54"); //$NON-NLS-1$
	private final String ABREVIATURA_ATTRIBUTE = Messages.getString("FIP1Handler.55"); //$NON-NLS-1$
	private final String ENTIDADORIGEN_ATTRIBUTE = Messages.getString("FIP1Handler.56"); //$NON-NLS-1$
	private final String ENTIDADDESTINO_ATTRIBUTE = Messages.getString("FIP1Handler.57"); //$NON-NLS-1$
	private final String INSTRUMENTO_ATTRIBUTE = Messages.getString("FIP1Handler.58"); //$NON-NLS-1$
	private final String ITERACION_ATTRIBUTE = Messages.getString("FIP1Handler.59"); //$NON-NLS-1$
	private final String CODIGOTRAMITE_ATTRIBUTE = Messages.getString("FIP1Handler.60"); //$NON-NLS-1$

	private boolean isAmbitoTag = false;
	private boolean isCatalogosistematizadoTag = false;
	private boolean isPlaneamientovigenteTag = false;
	private boolean isHojaTag = false;
	private boolean isEntidadTag = false;
	private boolean isDocumentoTag = false;
	private boolean isHijaEntidadTag = false;
	private boolean isDeterminacionTag = false;
	private boolean isHijaDeterminacionTag = false;
	private boolean isRegulacionEspecificaTag = false;
	private boolean isHijaRegulacionEspecificaTag = false;
	private boolean isCondicionUrbanisticaTag = false;
	private boolean isRegimenTag = false;
	private boolean isRegimenEspecificoTag = false;
	private boolean isHijoRegimenEspecificoTag = false;
	private boolean isCasoTag = false;
	private boolean isOperacionDeterminacionTag = false;
	private boolean isOperacionEntidadTag = false;
	private boolean isPropiedadesAdscripcionTag = false;
	private boolean isPropiedadesTag = false;

	public void startElement(String nameSpace, String localName, String qName,
			Attributes attr) throws SAXException {

		currentTag = localName;

		if (localName.equalsIgnoreCase(FIP_TAG)) {
			fip1 = new FIP1Bean();
			if(attr.getValue(FECHA_ATTRIBUTE) != null){
				String fech = attr.getValue(FECHA_ATTRIBUTE);
				if(attr.getValue(FECHA_ATTRIBUTE).length()>10){
					fech = attr.getValue(FECHA_ATTRIBUTE).substring(0, 10);
				}
				
				if(UtilsWS.validaFechaMascara(fech, "yyyy-MM-dd")){
					fip1.setFecha(fech);
				}
				else{
					throw new SAXException("La fecha de consolidación encontrada el el FIP no es correcta, no cumple el patrón yyyy-MM-dd");
				}
				
			}
			if(attr.getValue(PAIS_ATTRIBUTE) != null) fip1.setPais(attr.getValue(PAIS_ATTRIBUTE));
			if(attr.getValue(SRS_ATTRIBUTE) != null) fip1.setFip_srs(attr.getValue(SRS_ATTRIBUTE));
			if(attr.getValue(VERSION_ATTRIBUTE) != null) fip1.setVersion(attr.getValue(VERSION_ATTRIBUTE));
		}
		// Inicio Elementos del Diccionario
		else if (localName.equalsIgnoreCase(DICCIONARIO_TAG)) {
			diccionario = new DiccionarioBean();
		}
		else if (localName.equalsIgnoreCase(TIPOAMBITO_TAG)) {
			currentTipoAmbito = new TipoAmbitoBean();
			currentTipoAmbito.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
			currentTipoAmbito.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(AMBITO_TAG)) {
			isAmbitoTag = true;
			currentAmbito = new AmbitoBean();
			currentAmbito.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
			currentAmbito.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
			if(attr.getValue(INE_ATTRIBUTE) != null) currentAmbito.setIne(attr.getValue(INE_ATTRIBUTE));
			currentAmbito.setTipoambito(attr.getValue(TIPOAMBITO_ATTRIBUTE));
		}
		

		//--- FALTA ORGANIGRAMA-AMBITOS
		
		
		else if (localName.equalsIgnoreCase(CARACTERDETERMINACION_TAG)) {
			currentCaracterDeterminacion = new CaracterDeterminacionBean();
			currentCaracterDeterminacion.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
			currentCaracterDeterminacion.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));

			if (attr.getValue(APLICACIONESMAX_ATTRIBUTE).equalsIgnoreCase(Constants.APLICACIONES_MAX_VALUE_FIP)) {
				currentCaracterDeterminacion.setAplicaciones_max(Constants.APLICACIONES_MAX_VALUE);
			} else {
				if(!attr.getValue(APLICACIONESMAX_ATTRIBUTE).equals("null")){
					currentCaracterDeterminacion.setAplicaciones_max(Integer.parseInt(attr.getValue(APLICACIONESMAX_ATTRIBUTE)));
				}
				else{
					currentCaracterDeterminacion.setAplicaciones_max(Constants.APLICACIONES_MAX_VALUE);
				}
			}
			if (attr.getValue(APLICACIONESMIN_ATTRIBUTE).equalsIgnoreCase(Constants.APLICACIONES_MAX_VALUE_FIP)) {
				currentCaracterDeterminacion.setAplicaciones_min(Constants.APLICACIONES_MAX_VALUE);
			} else {
				if(!attr.getValue(APLICACIONESMIN_ATTRIBUTE).equals("null")){
					currentCaracterDeterminacion.setAplicaciones_min(Integer.parseInt(attr.getValue(APLICACIONESMIN_ATTRIBUTE)));
				}
				else{
					currentCaracterDeterminacion.setAplicaciones_min(Constants.APLICACIONES_MAX_VALUE);
				}
			}
		}
		else if (localName.equalsIgnoreCase(TIPOENTIDAD_TAG)) {
			currentTipoEntidad = new TipoEntidadBean();
			currentTipoEntidad.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
			currentTipoEntidad.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(TIPOOPERACIONENTIDAD_TAG)) {
			currentTipoOperacionEntidad = new TipoOperacionEntidadBean();
			currentTipoOperacionEntidad.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
			currentTipoOperacionEntidad.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
			if(attr.getValue(TIPOENTIDAD_ATTRIBUTE) != null) currentTipoOperacionEntidad.setTipoEntidad(attr.getValue(TIPOENTIDAD_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(TIPOOPERACIONDETERMINACION_TAG)) {
			currentTipoOperacionDeterminacion = new TipoOperacionDeterminacionBean();
			currentTipoOperacionDeterminacion.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
			currentTipoOperacionDeterminacion.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(OPERACIONCARACTER_TAG)) {
			currentOperacionCaracter = new OperacionCaracterBean();
			currentOperacionCaracter.setTipoOperacionDeterminacion(attr.getValue(TIPOOPERACIONDETERMINACION_ATTRIBUTE));
			currentOperacionCaracter.setCaracterOperadora(attr.getValue(CARACTEROPERADORA_ATTRIBUTE));
			currentOperacionCaracter.setCaracterOperada(attr.getValue(CARACTEROPERADA_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(TIPODOCUMENTO_TAG)) {
			currentTipoDocumento = new TipoDocumentoBean();
			currentTipoDocumento.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
			currentTipoDocumento.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(GRUPODOCUMENTO_TAG)) {
			currentGrupoDocumento = new GrupoDocumentoBean();
			currentGrupoDocumento.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
			currentGrupoDocumento.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(TIPOTRAMITE_TAG)) {
			currentTipoTramite = new TipoTramiteBean();
			if(attr.getValue(CODIGO_ATTRIBUTE) != null) currentTipoTramite.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
			if(attr.getValue(NOMBRE_ATTRIBUTE) != null) currentTipoTramite.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(INSTRUMENTO_TAG)) {
			currentInstrumento = new InstrumentoBean();
			if(attr.getValue(CODIGO_ATTRIBUTE) != null) currentInstrumento.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
			if(attr.getValue(NOMBRE_ATTRIBUTE) != null) currentInstrumento.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
		}
		/// Fin elementos del diccionario
		
		
		else if (localName.equalsIgnoreCase(CATALOGOSISTEMATIZADO_TAG)) {
			isCatalogosistematizadoTag = true;
			catalogosistematizado = new CatalogoSistematizadoBean();
			catalogosistematizado.setAmbito(attr.getValue(AMBITO_ATTRIBUTE));
			catalogosistematizado.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(PLANEAMIENTOVIGENTE_TAG)) {
			isPlaneamientovigenteTag = true;
			planeamientovigente = new PlaneamientoVigenteBean();
			planeamientovigente.setAmbito(attr.getValue(AMBITO_ATTRIBUTE));
			planeamientovigente.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(PLANEAMIENTOENCARGADO_TAG)) {
			planeamientoencargado = new PlaneamientoEncargadoBean();
			planeamientoencargado.setAmbito(attr.getValue(AMBITO_ATTRIBUTE));
			planeamientoencargado.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
			planeamientoencargado.setInstrumento(attr.getValue(INSTRUMENTO_ATTRIBUTE));
			planeamientoencargado.setIteracion(Integer.parseInt(attr.getValue(ITERACION_ATTRIBUTE)));
			planeamientoencargado.setTipotramite(attr.getValue(TIPOTRAMITE_ATTRIBUTE));
			planeamientoencargado.setCodigotramite(attr.getValue(CODIGOTRAMITE_ATTRIBUTE));
		}
		
		
		/// Inicio Elementos del Tramite
		
		else if (localName.equalsIgnoreCase(TRAMITE_TAG)) {
			currentTramite = new TramiteBean();
			currentTramite.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
			currentTramite.setTipotramite(attr.getValue(TIPOTRAMITE_ATTRIBUTE));
		}
		
		//Inicio Elementos Documentos
		else if (localName.equalsIgnoreCase(DOCUMENTO_TAG)) {
			isDocumentoTag = true;
			if (isEntidadTag) {
				currentDocumentoEntidad = new com.gestorfip.ws.xml.beans.importacion.tramite.entidad.DocumentoBean();
				currentDocumentoEntidad.setDocumento(attr.getValue(CODIGO_ATTRIBUTE));
			} else if (isDeterminacionTag) {
				currentDocumentoDeterminacion = new com.gestorfip.ws.xml.beans.importacion.tramite.determinacion.DocumentoBean();
				currentDocumentoDeterminacion.setDocumento(attr.getValue(CODIGO_ATTRIBUTE));
			} else if (isCasoTag) {
				currentDocumentoCaso = new Caso_DocumentoBean();
				currentDocumentoCaso.setDocumento(attr.getValue(CODIGO_ATTRIBUTE));
			} else {
				currentDocumento = new DocumentoBean();
				currentDocumento.setArchivo(attr.getValue(ARCHIVO_ATTRIBUTE));
				currentDocumento.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
				if(attr.getValue(ESCALA_ATTRIBUTE)!=null)
				currentDocumento.setEscala(attr
						.getValue(ESCALA_ATTRIBUTE));
				if(attr.getValue(GRUPO_ATTRIBUTE)!=null)
				currentDocumento.setGrupo(attr.getValue(GRUPO_ATTRIBUTE));
				currentDocumento.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
				currentDocumento.setTipo(attr.getValue(TIPO_ATTRIBUTE));
			}
		}
		else if(localName.equalsIgnoreCase(DOCUMENTOREFUNDIDO_TAG)) {
			currentDocumento = new DocumentoBean();
			currentDocumento.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(HOJA_TAG)) {
			isHojaTag = true;
			currentHoja = new HojaBean();
			if(attr.getValue(NOMBRE_ATTRIBUTE)!=null) currentHoja.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
		}
		
		// Fin Elementos Documentos
		
		
		// Inicio Elementos Entidades
		else if (localName.equalsIgnoreCase(ENTIDAD_TAG)) {
			
			if (isCondicionUrbanisticaTag) {
				currentCondicionUrbanistica.setCodigoentidad_entidad(attr.getValue(ENTIDAD_ATTRIBUTE));
				currentCondicionUrbanistica.setCodigoTramiteentidad_entidad(attr.getValue(TRAMITE_ATTRIBUTE));
			} else {
				isEntidadTag = true;
				currentEntidad = new EntidadBean();
				currentEntidad.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
				if(attr.getValue(ETIQUETA_ATTRIBUTE)!=null) currentEntidad.setEtiqueta(attr.getValue(ETIQUETA_ATTRIBUTE));
				currentEntidad.setClave(attr.getValue(CLAVE_ATTRIBUTE));
				currentEntidad.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
				currentEntidad.setSuspendida(Boolean.parseBoolean(attr.getValue(SUSPENDIDA_ATTRIBUTE)));
				if (isHijaEntidadTag)
					currentEntidad.setMadre(entidadesMadres.peek().getCodigo());
			}
		}
		
		else if (localName.equalsIgnoreCase(HIJAS_TAG)) {
			if (isEntidadTag) {
				hijasEntidadCounter++;
				isHijaEntidadTag = true;
				entidadesMadres.push(currentEntidad);
			} else if (isDeterminacionTag) {
				hijasDeterminacionCounter++;
				isHijaDeterminacionTag = true;
				determinacionesMadres.push(currentDeterminacion);
			} else if (isRegulacionEspecificaTag) {
				hijasRegulacionesespecificasCounter++;
				isHijaRegulacionEspecificaTag = true;
				regulacionesespecificasMadres.push(currentRegulacionEspecifica);
			}
		}
		
		else if (localName.equalsIgnoreCase(BASE_TAG)) {
			if (isEntidadTag) {
				currentEntidad.setBase_entidad(attr.getValue(ENTIDAD_ATTRIBUTE));
				currentEntidad.setBase_codigoTramite(attr.getValue(TRAMITE_ATTRIBUTE));
			} 
			else if (isDeterminacionTag) {
				currentDeterminacion.setBase_determinacion(attr.getValue(DETERMINACION_ATTRIBUTE));
				currentDeterminacion.setBase_codigoTramite(attr.getValue(TRAMITE_ATTRIBUTE));
			}
		}

		
		// Fin Elementos Entidades
		
		// Inicio Elementos Determinaciones
		
		else if (localName.equalsIgnoreCase(DETERMINACION_TAG)) {
			
			if (isCondicionUrbanisticaTag) {
				currentCondicionUrbanistica.setCodigodeterminacion_determinacion(attr.getValue(DETERMINACION_ATTRIBUTE));
				currentCondicionUrbanistica.setCodigoTramitedeterminacion_determinacion(attr.getValue(TRAMITE_ATTRIBUTE));
			} else {
				isDeterminacionTag = true;
				currentDeterminacion = new DeterminacionBean();
				currentDeterminacion.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
				currentDeterminacion.setCaracter(attr.getValue(CARACTER_ATTRIBUTE));
				currentDeterminacion.setApartado(attr.getValue(APARTADO_ATTRIBUTE));
				currentDeterminacion.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
				if(attr.getValue(ETIQUETA_ATTRIBUTE)!=null) 
					currentDeterminacion.setEtiqueta(attr.getValue(ETIQUETA_ATTRIBUTE));
				currentDeterminacion.setSuspendida(Boolean.parseBoolean(attr.getValue(SUSPENDIDA_ATTRIBUTE)));
				if (isHijaDeterminacionTag) {
					currentDeterminacion.setMadre(determinacionesMadres.peek().getCodigo());
				}
			}
		}

		else if (localName.equalsIgnoreCase(VALORREFERENCIA_TAG)) {
			if (isDeterminacionTag) {
				currentValorReferencia = new ValorReferenciaBean();
				currentValorReferencia.setDeterminacion(attr.getValue(DETERMINACION_ATTRIBUTE));
			} else if (isRegimenTag) {
				currentRegimen.setValorreferencia_determinacion(attr.getValue(DETERMINACION_ATTRIBUTE));
				currentRegimen.setValorreferencia_codigoTramite(attr.getValue(TRAMITE_ATTRIBUTE));
			}
		}
		else if (localName.equalsIgnoreCase(UNIDAD_TAG)) {
			if (isDeterminacionTag) {
				currentDeterminacion.setUnidad_determinacion(attr.getValue(DETERMINACION_ATTRIBUTE));
				currentDeterminacion.setUnidad_codigoTramite(attr.getValue(TRAMITE_ATTRIBUTE));
			} else if (isPropiedadesAdscripcionTag) {
				currentOperacionEntidad.setPropiedadesadscripcion_unidad_determinacion(attr.getValue(DETERMINACION_ATTRIBUTE));
			} else if (isPropiedadesTag) {
				currentAdscripcion.setPropiedades_unidad_determinacion(attr.getValue(DETERMINACION_ATTRIBUTE));
				currentAdscripcion.setPropiedades_tramite_unidad_determinacion(attr.getValue(TRAMITE_ATTRIBUTE));
			} else {
				currentUnidad = new UnidadBean();
				currentUnidad.setDeterminacion(attr.getValue(DETERMINACION_ATTRIBUTE));
				currentUnidad.setAbreviatura(attr.getValue(ABREVIATURA_ATTRIBUTE));
				currentUnidad.setCodigoTramite(attr.getValue(TRAMITE_ATTRIBUTE));
			}
		}
		
		else if (localName.equalsIgnoreCase(DETERMINACIONREGULADORA_TAG)) {
			currentDeterminacionReguladora = new DeterminacionReguladoraBean();
			currentDeterminacionReguladora.setDeterminacion(attr.getValue(DETERMINACION_ATTRIBUTE));
			currentDeterminacionReguladora.setCodigoTramite(attr.getValue(TRAMITE_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(REGULACIONESPECIFICA_TAG)) {
			isRegulacionEspecificaTag = true;
			currentRegulacionEspecifica = new RegulacionEspecificaBean();
			currentRegulacionEspecifica.setOrden(Integer.parseInt(attr.getValue(ORDEN_ATTRIBUTE)));
			currentRegulacionEspecifica.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
			if (isHijaRegulacionEspecificaTag)
				currentRegulacionEspecifica.setMadre(regulacionesespecificasMadres.peek().getNombre());
		}
		else if (localName.equalsIgnoreCase(GRUPOAPLICACION_TAG)) {
			currentGrupoAplicacion = new GrupoAplicacionBean();
			currentGrupoAplicacion.setDeterminacion(attr.getValue(DETERMINACION_ATTRIBUTE));
			currentGrupoAplicacion.setCodigoTramite(attr.getValue(TRAMITE_ATTRIBUTE));
		}
		
		// Fin Elementos Determinaciones
		
		// Inicio Elementos Condiciones Urbanisticas
		
		if (localName.equalsIgnoreCase(CONDICIONURBANISTICA_TAG)) {
			isCondicionUrbanisticaTag = true;
			currentCondicionUrbanistica = new CondicionUrbanisticaBean();
		}
		else if (localName.equalsIgnoreCase(CASO_TAG)) {
			isCasoTag = true;
			currentCaso = new CasoBean();
			currentCaso.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
			currentCaso.setCodigo(attr.getValue(CODIGO_ATTRIBUTE));
		}
		
		else if (localName.equalsIgnoreCase(REGIMEN_TAG)) {
			isRegimenTag = true;
			currentRegimen = new Caso_RegimenBean();
			if(attr.getValue(SUPERPOSICION_ATTRIBUTE)!=null) 
				currentRegimen.setSuperposicion(attr.getValue(SUPERPOSICION_ATTRIBUTE));
		}
		
		// Fin Elementos Condiciones Urbanisticas
		
		
		// Fin Elementos del Tramite
		
		else if (localName.equalsIgnoreCase(RELACION_TAG)) {
			currentRelacion = new RelacionBean();
			currentRelacion.setPadre(attr.getValue(PADRE_ATTRIBUTE));
			currentRelacion.setHijo(attr.getValue(HIJO_ATTRIBUTE));
		}
	
		else if (localName.equalsIgnoreCase(REGIMENESPECIFICO_TAG)) {
			isRegimenEspecificoTag = true;
			currentRegimenEspecifico = new Caso_Regimen_RegimenEspecificoBean();
			currentRegimenEspecifico.setOrden(Integer.parseInt(attr.getValue(ORDEN_ATTRIBUTE)));
			currentRegimenEspecifico.setNombre(attr.getValue(NOMBRE_ATTRIBUTE));
			if (isHijoRegimenEspecificoTag)
				currentRegimenEspecifico.setPadre(regimenesEspecificosPadres.peek().getPadre());
		}
		else if (localName.equalsIgnoreCase(HIJOS_TAG)) {
			hijosRegimenEspicificoCounter++;
			isHijoRegimenEspecificoTag = true;
			regimenesEspecificosPadres.push(currentRegimenEspecifico);
		}
		else if (localName.equalsIgnoreCase(DETERMINACIONREGIMEN_TAG)) {
			currentRegimen.setDeterminacionregimen_determinacion(attr.getValue(DETERMINACION_ATTRIBUTE));
			currentRegimen.setDeterminacionregimen_codigoTramite(attr.getValue(TRAMITE_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(CASOAPLICACION_TAG)) {
			currentRegimen.setCasoaplicacion_caso(attr.getValue(CASO_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(VINCULO_TAG)) {
			currentVinculo = new Caso_VinculoBean();
			currentVinculo.setCaso(attr.getValue(CASO_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(OPERACIONDETERMINACION_TAG)) {
			isOperacionDeterminacionTag = true;
			currentOperacionDeterminacion = new OperacionDeterminacionBean();
			currentOperacionDeterminacion.setTipo(attr.getValue(TIPO_ATTRIBUTE));
			currentOperacionDeterminacion.setOrden(Long.parseLong(attr.getValue(ORDEN_ATTRIBUTE)));
		}
		else if (localName.equalsIgnoreCase(OPERADA_TAG)) {
			if (isOperacionDeterminacionTag) {
				currentOperacionDeterminacion.setOperada_determinacion(attr.getValue(DETERMINACION_ATTRIBUTE));
			} else if (isOperacionEntidadTag) {
				currentOperacionEntidad.setOperada_entidad(attr.getValue(ENTIDAD_ATTRIBUTE));
			}
		}
		else if (localName.equalsIgnoreCase(OPERADORA_TAG)) {
			if (isOperacionDeterminacionTag) {
				currentOperacionDeterminacion.setOperadora_determinacion(attr.getValue(DETERMINACION_ATTRIBUTE));
			} else if (isOperacionEntidadTag) {
				currentOperacionEntidad.setOperadora_entidad(attr.getValue(ENTIDAD_ATTRIBUTE));
			}
		}
		else if (localName.equalsIgnoreCase(OPERACIONENTIDAD_TAG)) {
			isOperacionEntidadTag = true;
			currentOperacionEntidad = new OperacionEntidadBean();
			currentOperacionEntidad.setTipo(attr.getValue(TIPO_ATTRIBUTE));
			currentOperacionEntidad.setOrden(Long.parseLong(attr.getValue(ORDEN_ATTRIBUTE)));
		}
		else if (localName.equalsIgnoreCase(PROPIEDADESADSCRIPCION_TAG)) {
			isPropiedadesAdscripcionTag = true;
			currentOperacionEntidad.setPropiedadesadscripcion_cuantia(Double.parseDouble(attr.getValue(CUANTIA_ATTRIBUTE)));
		}
		else if (localName.equalsIgnoreCase(TIPO_TAG)) {
			if (isOperacionEntidadTag) {
				currentOperacionEntidad.setPropiedadesadscripcion_tipo_determinacion(attr.getValue(DETERMINACION_ATTRIBUTE));
			} else if (isPropiedadesTag) {
				currentAdscripcion.setPropiedades_tipo_determinacion(attr.getValue(DETERMINACION_ATTRIBUTE));
				currentAdscripcion.setPropiedades_tramite_tipo_determinacion(attr.getValue(TRAMITE_ATTRIBUTE));
			}
		}
		else if (localName.equalsIgnoreCase(APLICACIONAMBITO_TAG)) {
			currentAplicacionAmbito = new AplicacionAmbitoBean();
			currentAplicacionAmbito.setAmbito(attr.getValue(AMBITO_ATTRIBUTE));
			currentAplicacionAmbito.setEntidad(attr.getValue(ENTIDAD_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(ADSCRIPCION_TAG)) {
			currentAdscripcion = new AdscripcionBean();
			currentAdscripcion.setEntidadorigen(attr.getValue(ENTIDADORIGEN_ATTRIBUTE));
			currentAdscripcion.setEntidaddestino(attr.getValue(ENTIDADDESTINO_ATTRIBUTE));
		}
		else if (localName.equalsIgnoreCase(PROPIEDADES_TAG)) {
			isPropiedadesTag = true;
			currentAdscripcion.setPropiedades_cuantia(Double.parseDouble(attr
					.getValue(CUANTIA_ATTRIBUTE)));
		}

	}

	public void endElement(String nameSpace, String localName, String qName)
			throws SAXException {
		currentTag = ""; //$NON-NLS-1$

		if (localName.equalsIgnoreCase(DICCIONARIO_TAG)) {
			fip1.setDiccionario(diccionario);
		}
		else if (localName.equalsIgnoreCase(TIPOAMBITO_TAG)) {
			diccionario.getTiposAmbito().add(currentTipoAmbito);
		}
		else if (localName.equalsIgnoreCase(AMBITO_TAG)) {
			diccionario.getAmbitos().add(currentAmbito);
			isAmbitoTag = false;
		}
		else if (localName.equalsIgnoreCase(RELACION_TAG)) {
			diccionario.getOrganigramaAmbitos().add(currentRelacion);
		}
		else if (localName.equalsIgnoreCase(CARACTERDETERMINACION_TAG)) {
			diccionario.getCaracteresDeterminacion().add(currentCaracterDeterminacion);
		}
		else if (localName.equalsIgnoreCase(TIPOENTIDAD_TAG)) {
			diccionario.getTiposEntidad().add(currentTipoEntidad);
		}
		else if (localName.equalsIgnoreCase(TIPOOPERACIONENTIDAD_TAG)) {
			diccionario.getTiposOperacionEntidad().add(currentTipoOperacionEntidad);
		}
		else if (localName.equalsIgnoreCase(TIPOOPERACIONDETERMINACION_TAG)) {
			diccionario.getTiposOperacionDeterminacion().add(currentTipoOperacionDeterminacion);
		}
		else if (localName.equalsIgnoreCase(OPERACIONCARACTER_TAG)) {
			diccionario.getOperacionesCaracteres()
					.add(currentOperacionCaracter);
		}
		else if (localName.equalsIgnoreCase(TIPODOCUMENTO_TAG)) {
			diccionario.getTiposDocumento().add(currentTipoDocumento);
		}
		else if (localName.equalsIgnoreCase(GRUPODOCUMENTO_TAG)) {
			diccionario.getGruposDocumento().add(currentGrupoDocumento);
		}
		else if (localName.equalsIgnoreCase(TIPOTRAMITE_TAG)) {
			diccionario.getTiposTramites().add(currentTipoTramite);
		}
		else if (localName.equalsIgnoreCase(INSTRUMENTO_TAG)) {
			diccionario.getInstrumentos().add(currentInstrumento);
		}
		else if (localName.equalsIgnoreCase(CATALOGOSISTEMATIZADO_TAG)) {
			isCatalogosistematizadoTag = false;
			fip1.setCatalogosistematizado(catalogosistematizado);
		}
		else if (localName.equalsIgnoreCase(PLANEAMIENTOVIGENTE_TAG)) {
			isPlaneamientovigenteTag = false;
			fip1.setPlaneamientovigente(planeamientovigente);
		}
		else if (localName.equalsIgnoreCase(PLANEAMIENTOENCARGADO_TAG)) {
			fip1.setPlaneamientoencargado(planeamientoencargado);
		}
		else if (localName.equalsIgnoreCase(TRAMITE_TAG)) {
			if (isCatalogosistematizadoTag)
				catalogosistematizado.setTramite(currentTramite);
			else if(isPlaneamientovigenteTag)
				planeamientovigente.setTramite(currentTramite);
		}
		else if (localName.equalsIgnoreCase(DOCUMENTO_TAG)) {
			isDocumentoTag = false;
			if (isEntidadTag) {
				currentEntidad.getDocumentos().add(currentDocumentoEntidad);
			} else if (isDeterminacionTag) {
				currentDeterminacion.getDocumentos().add(currentDocumentoDeterminacion);
			} else if (isCasoTag) {
				currentCaso.getDocumentos().add(currentDocumentoCaso);
			} else {
				currentTramite.getDocumentos().add(currentDocumento);
			}
		}
		else if (localName.equalsIgnoreCase(DOCUMENTOREFUNDIDO_TAG)) {
			currentTramite.getDocumentos().add(currentDocumento);
		}
		else if (localName.equalsIgnoreCase(HOJA_TAG)) {
			currentDocumento.getHojas().add(currentHoja);
			isHojaTag = false;
		}
		else if (localName.equalsIgnoreCase(ENTIDAD_TAG)) {
			if(!isCondicionUrbanisticaTag) {
				if(isEntidadTag) {
					currentTramite.getEntidades().add(currentEntidad);
					if (!isHijaEntidadTag) {
						isEntidadTag = false;
					}
				}				
			}
		}
		else if (localName.equalsIgnoreCase(HIJAS_TAG)) {
			if (isEntidadTag) {
				hijasEntidadCounter--;
				if (hijasEntidadCounter == 0) {
					isHijaEntidadTag = false;
				}
				currentEntidad = entidadesMadres.pop();
			} else if (isDeterminacionTag) {
				hijasDeterminacionCounter--;
				if (hijasDeterminacionCounter == 0)
					isHijaDeterminacionTag = false;
				currentDeterminacion = determinacionesMadres.pop();
			} else if (isRegulacionEspecificaTag) {
				hijasRegulacionesespecificasCounter--;
				if (hijasRegulacionesespecificasCounter == 0)
					isHijaRegulacionEspecificaTag = false;
				currentRegulacionEspecifica = regulacionesespecificasMadres
						.pop();
			}
		}
		else if (localName.equalsIgnoreCase(DETERMINACION_TAG)) {
			if(!isCondicionUrbanisticaTag) {
				if(isDeterminacionTag) {
					currentTramite.getDeterminaciones().add(currentDeterminacion);
					if (!isHijaDeterminacionTag)
						isDeterminacionTag = false;
				}				
			}
		}
		else if (localName.equalsIgnoreCase(VALORREFERENCIA_TAG)) {
			if(isDeterminacionTag) {
				currentDeterminacion.getValoresreferencia().add(currentValorReferencia);
			}
		}
		else if (localName.equalsIgnoreCase(UNIDAD_TAG)) {
			if(!isDeterminacionTag && !isPropiedadesAdscripcionTag && !isPropiedadesTag)
			currentTramite.getUnidades().add(currentUnidad);
		}
		else if (localName.equalsIgnoreCase(DETERMINACIONREGULADORA_TAG)) {
			currentDeterminacion.getDeterminacionesreguladoras().add(currentDeterminacionReguladora);
		}
		else if (localName.equalsIgnoreCase(REGULACIONESPECIFICA_TAG)) {
			currentDeterminacion.getRegulacionesespecificas().add(currentRegulacionEspecifica);
			if (!isHijaRegulacionEspecificaTag)
				isRegulacionEspecificaTag = false;
		}
		else if (localName.equalsIgnoreCase(GRUPOAPLICACION_TAG)) {
			currentDeterminacion.getGruposaplicaciones().add(currentGrupoAplicacion);
		}
		else if (localName.equalsIgnoreCase(CONDICIONURBANISTICA_TAG)) {
			isCondicionUrbanisticaTag = false;
			currentTramite.getCondicionesurbanisticas().add(currentCondicionUrbanistica);
		}
		else if (localName.equalsIgnoreCase(CASO_TAG)) {
			currentCondicionUrbanistica.getCasos().add(currentCaso);
			isCasoTag = false;
		}
		else if (localName.equalsIgnoreCase(REGIMEN_TAG)) {
			currentCaso.getRegimenes().add(currentRegimen);
			isRegimenTag = false;
		}
		else if (localName.equalsIgnoreCase(REGIMENESPECIFICO_TAG)) {
			currentRegimen.getRegimenesespecificos().add(currentRegimenEspecifico);
			if (!isHijoRegimenEspecificoTag)
				isRegimenEspecificoTag = false;
		}
		else if (localName.equalsIgnoreCase(HIJOS_TAG)) {
			hijosRegimenEspicificoCounter--;
			if (hijosRegimenEspicificoCounter == 0)
				isHijoRegimenEspecificoTag = false;
			currentRegimenEspecifico = regimenesEspecificosPadres.pop();
		}
		else if (localName.equalsIgnoreCase(VINCULO_TAG)) {
			currentCaso.getVinculos().add(currentVinculo);
		}
		else if (localName.equalsIgnoreCase(OPERACIONDETERMINACION_TAG)) {
			isOperacionDeterminacionTag = false;
			currentTramite.getOperacionesdeterminaciones().add(	currentOperacionDeterminacion);
		}
		else if (localName.equalsIgnoreCase(OPERACIONENTIDAD_TAG)) {
			isOperacionEntidadTag = false;
			currentTramite.getOperacionesentidades().add(currentOperacionEntidad);
		}
		else if (localName.equalsIgnoreCase(PROPIEDADESADSCRIPCION_TAG)) {
			isPropiedadesAdscripcionTag = false;
		}
		else if (localName.equalsIgnoreCase(APLICACIONAMBITO_TAG)) {
			currentTramite.getAplicacionambitos().add(currentAplicacionAmbito);
		}
		else if (localName.equalsIgnoreCase(ADSCRIPCION_TAG)) {
			currentTramite.getAdscripciones().add(currentAdscripcion);
		}
		else if (localName.equalsIgnoreCase(PROPIEDADES_TAG)) {
			isPropiedadesTag = false;
		}
	}

	public void startDocument() {

	}

	public void endDocument() {

	}

	public void characters(char[] characters, int start, int length)
			throws SAXException {
		String data = new String(characters, start, length);

		if (!currentTag.equals("")) { //$NON-NLS-1$
			//if (!Character.isISOControl(characters[start])) {
				if (isAmbitoTag && currentTag.equalsIgnoreCase(GEOMETRIA_TAG)) {
					geometryBuffer.append(data);
//					if(isNewGeometry) {
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.62"))) isPolygon = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.63"))) isMultipolygon = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.64"))) isLineString = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.65"))) isMultiLineString = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.66"))) isPoint = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.67"))) isMultiPoint = true; //$NON-NLS-1$
//					}
					
					if(geometryBuffer.toString().endsWith("))") && isPolygon || geometryBuffer.toString().endsWith(")))") && isMultipolygon ||
							geometryBuffer.toString().endsWith(")") && isLineString || geometryBuffer.toString().endsWith("))") && isMultiLineString ||
							geometryBuffer.toString().endsWith(")") && isPoint || geometryBuffer.toString().endsWith(")") && isMultiPoint) // Geometry is complete  //$NON-NLS-1$ //$NON-NLS-2$
					{
						WKTReader wkt = new WKTReader();
						Geometry geom = null;
						try {
							geom = wkt.read(geometryBuffer.toString());
							String valueSrid = fip1.getFip_srs().substring(fip1.getFip_srs().indexOf(":")+1);
							geom.setSRID(Integer.valueOf(valueSrid).intValue());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new SAXException(e);
						}
						currentAmbito.setGeometria(geom);
						geometryBuffer = new StringBuffer();
						isNewGeometry = true;
						isPolygon = false;
						isMultipolygon = false;
						isLineString = false;
						isMultiLineString = false;
						isPoint = false;
						isMultiPoint = false;
					}
					else // We are still expecting the rest of the geometry
					{
						isNewGeometry = false;
					}

					
				} else if (isDocumentoTag &&
							currentTag.equalsIgnoreCase(COMENTARIO_TAG)) {
					currentDocumento.setComentario(data);
					
				} else if (isHojaTag
						&& currentTag.equalsIgnoreCase(GEOMETRIA_TAG)) {
					geometryBuffer.append(data);
//					if(isNewGeometry) {
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.62"))) isPolygon = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.63"))) isMultipolygon = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.64"))) isLineString = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.65"))) isMultiLineString = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.66"))) isPoint = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.67"))) isMultiPoint = true; //$NON-NLS-1$
//					}
					
					if(geometryBuffer.toString().endsWith("))") && isPolygon || geometryBuffer.toString().endsWith(")))") && isMultipolygon ||
							geometryBuffer.toString().endsWith(")") && isLineString || geometryBuffer.toString().endsWith("))") && isMultiLineString ||
							geometryBuffer.toString().endsWith(")") && isPoint || geometryBuffer.toString().endsWith(")") && isMultiPoint) // Geometry is complete  //$NON-NLS-1$ //$NON-NLS-2$
					{
						WKTReader wkt = new WKTReader();
						Geometry geom = null;
						try {
							geom = wkt.read(geometryBuffer.toString());
							String valueSrid = fip1.getFip_srs().substring(fip1.getFip_srs().indexOf(":")+1);
							geom.setSRID(Integer.valueOf(valueSrid).intValue());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new SAXException(e);
						}			
						currentHoja.setGeometria(geom);
						geometryBuffer = new StringBuffer();
						isNewGeometry = true;
						isPolygon = false;
						isMultipolygon = false;
						isLineString = false;
						isMultiLineString = false;
						isPoint = false;
						isMultiPoint = false;
					}
					else // We are still expecting the rest of the geometry
					{
						isNewGeometry = false;
					}

					
				} else if (isEntidadTag
						&& currentTag.equalsIgnoreCase(GEOMETRIA_TAG)) {
					
					geometryBuffer.append(data);
//					if(isNewGeometry) {
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.62"))) isPolygon = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.63"))) isMultipolygon = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.64"))) isLineString = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.65"))) isMultiLineString = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.66"))) isPoint = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.67"))) isMultiPoint = true; //$NON-NLS-1$
//					}
					
					if(geometryBuffer.toString().endsWith("))") && isPolygon || geometryBuffer.toString().endsWith(")))") && isMultipolygon ||
							geometryBuffer.toString().endsWith(")") && isLineString || geometryBuffer.toString().endsWith("))") && isMultiLineString ||
							geometryBuffer.toString().endsWith(")") && isPoint || geometryBuffer.toString().endsWith(")") && isMultiPoint) // Geometry is complete  //$NON-NLS-1$ //$NON-NLS-2$
					{
						WKTReader wkt = new WKTReader();
						Geometry geom = null;
						try {
							geom = wkt.read(geometryBuffer.toString());
							String valueSrid = fip1.getFip_srs().substring(fip1.getFip_srs().indexOf(":")+1);
							geom.setSRID(Integer.valueOf(valueSrid).intValue());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new SAXException(e);
						}
						currentEntidad.setGeometria(geom);
						geometryBuffer = new StringBuffer();
						isNewGeometry = true;
						isPolygon = false;
						isMultipolygon = false;
						isLineString = false;
						isMultiLineString = false;
						isPoint = false;
						isMultiPoint = false;
					}
					else // We are still expecting the rest of the geometry
					{
						isNewGeometry = false;
					}
					
				} else if (isDeterminacionTag && !isRegulacionEspecificaTag
						&& currentTag.equalsIgnoreCase(TEXTO_TAG)) {

					if(!currentDeterminacionCode.equalsIgnoreCase(currentDeterminacion.getCodigo())) {
						// We have a new determination text, resetting the buffer
						textBuffer = new StringBuffer();
					}
					textBuffer.append(data);
					currentDeterminacion.setTexto(textBuffer.toString()); // Overwriting the current text
					currentDeterminacionCode = currentDeterminacion.getCodigo(); // Saving current determination code
					
				} else if (isRegulacionEspecificaTag
						&& currentTag.equalsIgnoreCase(TEXTO_TAG)) {		

					if(currentRegulacionEspeceficaDeterminacionCode!=currentDeterminacion.getCodigo() 
							|| currentRegulacionEspecificaOrden != currentRegulacionEspecifica.getOrden()
							|| !currentRegulacionEspecificaNombre.equalsIgnoreCase(currentRegulacionEspecifica.getNombre())) {
						// We have a new regulacion especifica text, resetting the buffer
						textBuffer = new StringBuffer();
					}
					textBuffer.append(data);
					currentRegulacionEspecifica.setTexto(textBuffer.toString()); // Overwriting the current text

					currentRegulacionEspecificaOrden = currentRegulacionEspecifica.getOrden(); // Saving current regulacion especifica orden
					currentRegulacionEspecificaNombre = currentRegulacionEspecifica.getNombre(); // Saving current regulacion especifica name
					currentRegulacionEspeceficaDeterminacionCode = currentDeterminacion.getCodigo();
					
				} else if (isRegimenTag
						&& currentTag.equalsIgnoreCase(COMENTARIO_TAG)) {
					currentRegimen.setComentario(data);
					
				} else if (currentTag.equalsIgnoreCase(VALOR_TAG)) {
					currentRegimen.setValor(data);
					
				} else if (isRegimenEspecificoTag
						&& currentTag.equalsIgnoreCase(TEXTO_TAG)) {
					
					if(currentRegimenEspecificoCasoCode!=currentCaso.getCodigo() 
							|| currentRegimenEspecificoOrden != currentRegimenEspecifico.getOrden()
							|| !currentRegimenEspecificoNombre.equalsIgnoreCase(currentRegimenEspecifico.getNombre())) {
						// We have a new regimen especifico text, resetting the buffer
						textBuffer = new StringBuffer();
					}
					textBuffer.append(data);
					currentRegimenEspecifico.setTexto(textBuffer.toString()); // Overwriting the current text

					currentRegimenEspecificoOrden = currentRegimenEspecifico.getOrden(); // Saving current regimen especifico orden
					currentRegimenEspecificoNombre = currentRegimenEspecifico.getNombre(); // Saving current regimen especifico name
					currentRegimenEspecificoCasoCode = currentCaso.getCodigo();

				} else if (isOperacionDeterminacionTag
						&& currentTag.equalsIgnoreCase(TEXTO_TAG)) {
					
					if(currentOperacionDeterminacionOrden != currentOperacionDeterminacion.getOrden()
							|| !currentOperacionDeterminacionTipo.equalsIgnoreCase(currentOperacionDeterminacion.getTipo())) {
						// We have a new operacion determinacion text, resetting the buffer
						textBuffer = new StringBuffer();
					}
					textBuffer.append(data);
					currentOperacionDeterminacion.setTexto(textBuffer.toString()); // Overwriting the current text

					currentOperacionDeterminacionOrden = currentOperacionDeterminacion.getOrden(); // Saving current operacion determinacion orden
					currentOperacionDeterminacionTipo = currentOperacionDeterminacion.getTipo(); // Saving current operacion determinacion name

				} else if (isOperacionEntidadTag
						&& currentTag.equalsIgnoreCase(TEXTO_TAG)) {
					
					// When isOperacionEntidadTag we can have 2 TEXT inside
					if(isPropiedadesAdscripcionTag) {
						currentOperacionEntidad.setPropiedadesadscripcion_texto(data);
					}
					else {	
						if(currentOperacionEntidadOrden != currentOperacionEntidad.getOrden()
								|| !currentOperacionEntidadTipo.equalsIgnoreCase(currentOperacionEntidad.getTipo())) {
							// We have a new operacion entidad text, resetting the buffer
							textBuffer = new StringBuffer();
						}
						textBuffer.append(data);
						currentOperacionEntidad.setTexto(textBuffer.toString()); // Overwriting the current text
						
						currentOperacionEntidadOrden = currentOperacionEntidad.getOrden(); // Saving current operacion entidad orden
						currentOperacionEntidadTipo = currentOperacionEntidad.getTipo(); // Saving current operacion entidad name
					}

				} else if (currentTag.equalsIgnoreCase(DEFINICION_TAG)) {
					currentUnidad.setDefinicion(data);
					
				} else if (isPropiedadesTag
						&& currentTag.equalsIgnoreCase(TEXTO_TAG)) {
					
					if(!currentEntidadOrigen.equalsIgnoreCase(currentAdscripcion.getEntidadorigen())
							|| !currentEntidadDestino.equalsIgnoreCase(currentAdscripcion.getEntidaddestino())) {
						// We have a new operacion entidad text, resetting the buffer
						textBuffer = new StringBuffer();
					}
					textBuffer.append(data);
					currentAdscripcion.setPropiedades_texto(textBuffer.toString()); // Overwriting the current text

					currentEntidadOrigen = currentAdscripcion.getEntidadorigen(); // Saving current operacion entidad orden
					currentEntidadDestino = currentAdscripcion.getEntidaddestino(); // Saving current operacion entidad name

				} else if (currentTag.equalsIgnoreCase(TEXTO_TAG)) {
					if(!currentTramiteCodigo.equalsIgnoreCase(currentTramite.getCodigo())) {
						// We have a new tramite text, resetting the buffer
						textBuffer = new StringBuffer();
					}
					textBuffer.append(data);
					currentTramite.setTexto(textBuffer.toString()); // Overwriting the current text
					currentTramiteCodigo = currentTramite.getCodigo(); // Saving current tramite code

					
				} else if (currentTag.equalsIgnoreCase(AMBITOAPLICACION_TAG)) {
					geometryBuffer.append(data);
//					if(isNewGeometry) {
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.62"))) isPolygon = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.63"))) isMultipolygon = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.64"))) isLineString = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.65"))) isMultiLineString = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.66"))) isPoint = true; //$NON-NLS-1$
						if(geometryBuffer.toString().startsWith(Messages.getString("FIP1Handler.67"))) isMultiPoint = true; //$NON-NLS-1$
//					}
					
					if(geometryBuffer.toString().endsWith("))") && isPolygon || geometryBuffer.toString().endsWith(")))") && isMultipolygon ||
							geometryBuffer.toString().endsWith(")") && isLineString || geometryBuffer.toString().endsWith("))") && isMultiLineString ||
							geometryBuffer.toString().endsWith(")") && isPoint || geometryBuffer.toString().endsWith(")") && isMultiPoint) // Geometry is complete  //$NON-NLS-1$ //$NON-NLS-2$
					{
						WKTReader wkt = new WKTReader();
						Geometry geom = null;
						try {
							geom = wkt.read(geometryBuffer.toString());
							String valueSrid = fip1.getFip_srs().substring(fip1.getFip_srs().indexOf(":")+1);
							geom.setSRID(Integer.valueOf(valueSrid).intValue());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new SAXException(e);
						}
						planeamientoencargado.setGeometria(geom);
						geometryBuffer = new StringBuffer();
						isNewGeometry = true;
						isPolygon = false;
						isMultipolygon = false;
						isLineString = false;
						isMultiLineString = false;
						isPoint = false;
						isMultiPoint = false;
					}
					else // We are still expecting the rest of the geometry
					{
						isNewGeometry = false;
					}
				}
			//}
		}
	}

	/**
	 * Tells if the parsed file has the FIP1 format
	 * @return 
	 * 			true if the file respects the FIP1 format
	 * 			false elsewhere
	 */
	public boolean isFIP1() {
		
		if(diccionario == null) return false;
		else return true;
	}
}
