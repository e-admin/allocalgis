package com.gestorfip.ws.xml.beans.importacion.fip;  
 
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger; 

import com.geopista.instalador.util.Ed50toetrs89Utils;
import com.gestorfip.ws.servidor.GestionLayersBBDD;
import com.gestorfip.ws.servidor.OperacionesBBDD;
import com.gestorfip.ws.servidor.UtilsConversor;
import com.gestorfip.ws.utils.ConnectionUtilities;
import com.gestorfip.ws.utils.Constants;
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
import com.gestorfip.ws.xml.beans.importacion.layers.DeterminacionStyleBean;
import com.gestorfip.ws.xml.beans.importacion.layers.EdificabilidadBean;
import com.gestorfip.ws.xml.beans.importacion.layers.EdificabilidadConfigBean;
import com.gestorfip.ws.xml.beans.importacion.layers.EdificabilidadStyleBean;
import com.gestorfip.ws.xml.beans.importacion.layers.LayerConfigBean;
import com.gestorfip.ws.xml.beans.importacion.layers.LayerStylesBean;
import com.gestorfip.ws.xml.beans.importacion.layers.RegulaConfBean;
import com.gestorfip.ws.xml.beans.importacion.layers.RegulacionesConfigBean;
import com.gestorfip.ws.xml.beans.importacion.layers.UsosConfigBean;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfLayerBean;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfLayerDeterminacionAplicadaBean;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfLayerRegimenEspecificoBean;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfLayerUsosRegulacionesBean;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfLayerValorBean;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfLayerValorReferenciaBean;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfRegulacionesBean;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.DeterminacionLayerBean;
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

/**
 * The purpose of this class is to offer support to parse and load the FIP1 file
 * data into the database exploited by the GestorFip.
 * This class represents the content of the FIP1 file. When parsing the FIP1
 * file, the SAX parser populates the different attributes of the FIP1Bean with
 * the XML data.
 * The FIP1Bean possess various methods to load its data into the database.
 * 
 * @author davidriou
 *
 */
public class FIP1Bean implements Serializable {

	private static final long serialVersionUID = -2685400793754882320L;
	
	static Logger logger = Logger.getLogger(FIP1Bean.class);
	
	private String pais;
	private String fecha;
	private String version;
	private String fip_srs; //contiene el el dato del atributo SRS que viene en el tag FIP. ej <FIP FECHA="2011-10-27" PAIS="es" SRS="EPSG:25830" VERSION="2.0">
	private DiccionarioBean diccionario;
	private CatalogoSistematizadoBean catalogosistematizado;
	private PlaneamientoVigenteBean planeamientovigente;
	private PlaneamientoEncargadoBean planeamientoencargado;

	private Connection conn = null; // Connection JDBC
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	//private int ccaa_iden = 0; // Identificador ccaa
	private int fip_id = 0; // Identificador del FIP en BD

	public FIP1Bean() {
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFip_srs() {
		return fip_srs;
	}

	public void setFip_srs(String fipSrs) {
		fip_srs = fipSrs;
	}

	public DiccionarioBean getDiccionario() {
		return diccionario;
	}

	public void setDiccionario(DiccionarioBean diccionario) {
		this.diccionario = diccionario;
	}

	public CatalogoSistematizadoBean getCatalogosistematizado() {
		return catalogosistematizado;
	}

	public void setCatalogosistematizado(
			CatalogoSistematizadoBean catalogosistematizado) {
		this.catalogosistematizado = catalogosistematizado;
	}

	public PlaneamientoVigenteBean getPlaneamientovigente() {
		return planeamientovigente;
	}

	public void setPlaneamientovigente(
			PlaneamientoVigenteBean planeamientovigente) {
		this.planeamientovigente = planeamientovigente;
	}

	public PlaneamientoEncargadoBean getPlaneamientoencargado() {
		return planeamientoencargado;
	}

	public void setPlaneamientoencargado(
			PlaneamientoEncargadoBean planeamientoencargado) {
		this.planeamientoencargado = planeamientoencargado;
	}
	
	
	 /**
	 * Inserta la informacion del FIP en la BD
	 * 
	 * @param conn
	 *            Conexion a la DB
	 * @param ccaa
	 *            Numero de la comunidad autonoma
	 * @return String
	 * 			  El resultado de la insercion del fichero en BD
	 */
	public String loadFip1IntoDB(Connection conn, int idAmbito,int idMunicipioLG , ConfLayerBean[] lstConfLayerBean) {
		
		this.conn = conn;
		try {
			this.conn.setAutoCommit(false);
		} catch (SQLException e) {
			logger.error("Error al establecer la conexion a BBDD");
			logger.error(e.getMessage());
			return Constants.OTHER_ERROR_CODE + e.getMessage();
		}
		
		Date dateFechConsolidacion = null;
		try {

			dateFechConsolidacion = dateFechConsolidacion.valueOf(this.fecha);
		} catch (Exception e) {
			logger.error("Error Al obtener la fecha de consolidacion.");
			logger.error(e.getMessage());
			return Constants.OTHER_ERROR_CODE + e.getMessage();
		}

		try{
			pst = this.conn
					.prepareStatement("INSERT INTO gestorfip.fip (id, pais, fecha, version, srs, fechaconsolidacion, idambito) " +
							"VALUES (nextval('gestorfip.seq_fip'),?,?,?,?,?,?)");
			pst.setString(1, this.getPais());
			pst.setDate(2, java.sql.Date.valueOf(this.getFecha())); // DATE
			pst.setString(3, this.getVersion());
			pst.setString(4, this.getFip_srs());			
			pst.setDate(5, dateFechConsolidacion);
			pst.setInt(6, idAmbito);

			pst.executeUpdate();

		} catch (Exception e) {
			logger.error("Error al insertar en la tabla fip.");
			logger.error(e.getMessage());
			return Constants.OTHER_ERROR_CODE + e.getMessage();
		}
		
		try{

			// Recuperacion del id del FIP
			rs = null;
			pst = this.conn
					.prepareStatement("SELECT currval('gestorfip.seq_fip')");
			rs = pst.executeQuery();
			while (rs.next())
				this.fip_id = rs.getInt(1);
			
		} catch (Exception e) {
			logger.error("Error al obtener el id de fip.");
			logger.error(e.getMessage());
			return Constants.OTHER_ERROR_CODE + e.getMessage();
		}
		
		long before;
		try{
			// Loading the diccionary
			if (!existDiccionarioIntoDB(conn) && this.diccionario != null) {
				logger.info("Loading Diccionario...");
				before = System.currentTimeMillis();
				this.loadDiccionarioIntoDB();
				logger.info("Diccionario loaded [" + printTime(before).get(Calendar.MINUTE) + "m " + printTime(before).get(Calendar.SECOND) +
									"s " + printTime(before).get(Calendar.MILLISECOND) + "ms]");
			}

			// Loading the catalogo sistematizado
			if (this.catalogosistematizado != null) {
				logger.info("Loading Catalogo Sistematizado...");
				before = System.currentTimeMillis();
				this.loadTramiteIntoDB(catalogosistematizado.getTramite());
				this.loadCatalogoSistematizadoIntoDB();
				logger.info("Catalogo Sistematizado loaded [" + printTime(before).get(Calendar.MINUTE) + "m " + printTime(before).get(Calendar.SECOND) +
									"s " + printTime(before).get(Calendar.MILLISECOND) + "ms]");
			}

			// Loading the planeamiento vigente
			if (this.planeamientovigente != null) {
				logger.info("Loading Pleaneamiento Vigente...");
				before = System.currentTimeMillis();
				this.loadTramiteIntoDB(planeamientovigente.getTramite());
				this.loadPlaneamientoVigenteIntoDB();
				logger.info("Planeamiento Vigente loaded [" + printTime(before).get(Calendar.MINUTE) + "m " + printTime(before).get(Calendar.SECOND) +
									"s " + printTime(before).get(Calendar.MILLISECOND) + "ms]");
			}
			before = System.currentTimeMillis();
			
			//insercion de las geometrias de las entidades en las tablas de planeamiento
			this.loadGeometriesEntidades(idMunicipioLG, lstConfLayerBean);
			logger.info("Geometriesloaded [" + printTime(before).get(Calendar.MINUTE) + "m " + printTime(before).get(Calendar.SECOND) +
									"s " + printTime(before).get(Calendar.MILLISECOND) + "ms]");
		
			this.conn.commit();
			this.conn.setAutoCommit(true);
			this.pst.close();
			this.conn.close();
			
		} catch(SQLException ex) {
			logger.error("Error when loading FIP data into the DB.");
			logger.error(ex.getMessage());
			if (this.conn != null) {
				try {
					this.conn.rollback();
					logger.info("Transaction has been rolled back.");
				} catch(SQLException excep) {
					return Constants.OTHER_ERROR_CODE + excep.getMessage();
				}
			}
			return Constants.DB_INSERTION_ERROR_CODE + ex.getMessage();
		
		} catch (Exception e) {
			logger.error("Error when loading FIP data into the DB.");
			logger.error(e.getMessage());
			return Constants.OTHER_ERROR_CODE + e.getMessage();
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,null);
		}
		
		return Constants.SUCCESS_CODE;
	}
	
	public com.gestorfip.ws.beans.tramite.ui.DeterminacionBean[] obtenerDeterminaciones_XML_CS(){
		int index = 0;
		
		com.gestorfip.ws.beans.tramite.ui.DeterminacionBean[] lstDet = new com.gestorfip.ws.beans.tramite.ui.DeterminacionBean[catalogosistematizado.getTramite().getDeterminaciones().size()];
		for (Iterator iterator = catalogosistematizado.getTramite().getDeterminaciones().iterator(); iterator.hasNext();) {
			DeterminacionBean det = (DeterminacionBean) iterator.next();
			 com.gestorfip.ws.beans.tramite.ui.DeterminacionBean detfin = new com.gestorfip.ws.beans.tramite.ui.DeterminacionBean();
			 detfin.setCodigo(det.getCodigo());
			 detfin.setNombre(det.getNombre());
			lstDet[index++] = detfin;
			
		}
		return lstDet;	
	}
	
	/** funcion para obtener la diferencia de tiempo.
	 * @param before tiempo de inicio
	 * @return
	 */
	private Calendar printTime(long before){
		long diff = System.currentTimeMillis() - before;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(diff);
		return calendar;
	}
	
	
	/** Inicialización de las capas de la migración asistida
	 * @return
	 */
	public ConfLayerBean[] initDataConfLayers(){
		
		ConfLayerBean[] lstConfLayerBean =  new ConfLayerBean[12];
		
		lstConfLayerBean[0] = new ConfLayerBean();
		lstConfLayerBean[0].setNameLayer(Constants.LAYER_TITLE_PLANEAMIENTO_AMBITO);
		
		lstConfLayerBean[1] = new ConfLayerBean();
		lstConfLayerBean[1].setNameLayer(Constants.LAYER_TITLE_PLANEAMIENTO_CLASIFICACION);
		
		lstConfLayerBean[2] = new ConfLayerBean();
		lstConfLayerBean[2].setNameLayer(Constants.LAYER_TITLE_PLANEAMIENTO_CATEGORIA);
		
		lstConfLayerBean[3] = new ConfLayerBean();
		lstConfLayerBean[3].setNameLayer(Constants.LAYER_TITLE_PLANEAMIENTO_ZONA);
		
		lstConfLayerBean[4] = new ConfLayerBean();
		lstConfLayerBean[4].setNameLayer(Constants.LAYER_TITLE_PLANEAMIENTO_GESTION);
				
		lstConfLayerBean[5] = new ConfLayerBean();
		lstConfLayerBean[5].setNameLayer(Constants.LAYER_TITLE_PLANEAMIENTO_DESARROLLO);
		
		lstConfLayerBean[6] = new ConfLayerBean();
		lstConfLayerBean[6].setNameLayer(Constants.LAYER_TITLE_PLANEAMIENTO_EQUIDISTRIBUCION);
		
		lstConfLayerBean[7] = new ConfLayerBean();
		lstConfLayerBean[7].setNameLayer(Constants.LAYER_TITLE_PLANEAMIENTO_SISTEMAS);
		
		lstConfLayerBean[8] = new ConfLayerBean();
		lstConfLayerBean[8].setNameLayer(Constants.LAYER_TITLE_PLANEAMIENTO_PROTECCION);
		
		lstConfLayerBean[9] = new ConfLayerBean();
		lstConfLayerBean[9].setNameLayer(Constants.LAYER_TITLE_PLANEAMIENTO_AFECCIONES);
		
		lstConfLayerBean[10] = new ConfLayerBean();
		lstConfLayerBean[10].setNameLayer(Constants.LAYER_TITLE_PLANEAMIENTO_ALINEACION);
	
		lstConfLayerBean[11] = new ConfLayerBean();
		lstConfLayerBean[11].setNameLayer(Constants.LAYER_TITLE_PLANEAMIENTO_ACCIONES);
	
		return lstConfLayerBean;
	}
	
	
	/** Añade la Determiación aplicada si esta no esta incluida previamente en la migracion asistida
	 * @param nameLayer
	 * @param lstConfLayerBean
	 * @param confLayerDeterminacionAplicadaBean
	 * @param determinacion
	 */
	private void  addDeterminacionAplicada(String nameLayer, ConfLayerBean[] lstConfLayerBean, 
				ConfLayerDeterminacionAplicadaBean confLayerDeterminacionAplicadaBean){
		
		int index = 0;
		for (index = 0; index < lstConfLayerBean.length; index++) {
			if(lstConfLayerBean[index].getNameLayer().equals(nameLayer)){
					// se obtienen los datos del grupo de aplicacion con el que se esta trabajando
				if(lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada() == null){
					// la lista esta vacia
					lstConfLayerBean[index].setLstConfLayerDeterminacionAplicada(new ConfLayerDeterminacionAplicadaBean[1]);
					lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[0] = confLayerDeterminacionAplicadaBean;
				}
				else{
					//existen previamente valores en la lista
					// se comprueba si la determinacion aplicada ya existia para añadirla los nuevos valores
					boolean existe = false;
					//comprobamos si la determinacion ya existe
					int indexEncontrada = -1;
					for(int r=0 ; r<lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada().length; r++){
						if(confLayerDeterminacionAplicadaBean.getDeterminacionLayer().getCodigo()
									.equals(lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[r].getDeterminacionLayer().getCodigo())){
							existe = true;
							indexEncontrada = r;
						}
					}
					
					if(!existe){
						lstConfLayerBean[index].setLstConfLayerDeterminacionAplicada((ConfLayerDeterminacionAplicadaBean[]) Arrays.copyOf(lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada(), 
								lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada().length+1));
						lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada().length-1] = confLayerDeterminacionAplicadaBean;
						
					}
					else{
						//Si la determinacion ya existe en la lista, añadimos los nuevos valores 
						if(confLayerDeterminacionAplicadaBean.getLstValores() != null){
							for(int t=0; t<confLayerDeterminacionAplicadaBean.getLstValores().length; t++){
								
								if(lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstValores() == null){
									lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].setLstValores(new ConfLayerValorBean[1]);
									lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstValores()[0] = 
												confLayerDeterminacionAplicadaBean.getLstValores()[t];
								}
								else{
									lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].setLstValores(
											(ConfLayerValorBean[]) Arrays.copyOf(lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstValores(), 
													lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstValores().length+1));
									
									lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].
										getLstValores()[lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstValores().length-1] = confLayerDeterminacionAplicadaBean.getLstValores()[t];
								}
							}
						}
						//Si la determinacion ya existe en la lista, añadimos los nuevos valores de referencia
						if(confLayerDeterminacionAplicadaBean.getLstValoresReferencia()!= null){
							for(int t=0; t<confLayerDeterminacionAplicadaBean.getLstValoresReferencia().length; t++){

								if(lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstValoresReferencia() == null){
									lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].setLstValoresReferencia(new ConfLayerValorReferenciaBean[1]);
									lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstValoresReferencia()[0] = 
											confLayerDeterminacionAplicadaBean.getLstValoresReferencia()[t];
								}
								else{
									lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].setLstValoresReferencia(
											(ConfLayerValorReferenciaBean[]) Arrays.copyOf(lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstValoresReferencia(), 
													lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstValoresReferencia().length+1));
									
									lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].
										getLstValoresReferencia()[lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstValoresReferencia().length-1] = confLayerDeterminacionAplicadaBean.getLstValoresReferencia()[t];
								}
							}
						}
						
						
						//Si la determinacion ya existe en la lista, añadimos los nuevos regimenes especificos 
						if(confLayerDeterminacionAplicadaBean.getLstRegimenesEspecificos() != null){
							for(int t=0; t<confLayerDeterminacionAplicadaBean.getLstRegimenesEspecificos().length; t++){
								if(lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstRegimenesEspecificos() == null){
									lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].setLstRegimenesEspecificos(new ConfLayerRegimenEspecificoBean[1]);
									lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstRegimenesEspecificos()[0] = 
											confLayerDeterminacionAplicadaBean.getLstRegimenesEspecificos()[t];

								}	
								else{
									lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].setLstRegimenesEspecificos(
											(ConfLayerRegimenEspecificoBean[]) Arrays.copyOf(lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstRegimenesEspecificos(), 
													lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstRegimenesEspecificos().length+1));
									
									lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].
										getLstRegimenesEspecificos()[lstConfLayerBean[index].getLstConfLayerDeterminacionAplicada()[indexEncontrada].getLstRegimenesEspecificos().length-1] = confLayerDeterminacionAplicadaBean.getLstRegimenesEspecificos()[t];
								}
							}
						}
					}
				}
			}
		}	

	}
	
	
	
	/** Obtiene los datos de la configuración de determinaciones en la 
	 * @param lstConfLayerBean
	 * @throws Exception 
	 */
	public void obtenerDatosDeterminacinoesMigracionAsistida(ConfLayerBean[] lstConfLayerBean) throws Exception{
		int idValor = 0;
		int idValorReg = 0;
		for (Iterator<EntidadBean> itEntPV = planeamientovigente.getTramite().getEntidades().iterator(); itEntPV.hasNext();) {
			// recorremos las condiciones urbanisticas del planeamiento vigente
			EntidadBean entidad = itEntPV.next();

			String grupoAplicacion = searchCodigoGrupoApliacionEntidad(entidad.getCodigo(), planeamientovigente.getTramite().getCondicionesurbanisticas());
			if(grupoAplicacion != null){

				ConfLayerValorBean[] lstConfLayersValores = null;
				ConfLayerValorReferenciaBean[] lstConfLayerValorReferencia = null;
				ConfLayerRegimenEspecificoBean[] lstConfLayerRegimenEspecifico = null;
				
				for (Iterator<CondicionUrbanisticaBean> itCuPV = planeamientovigente.getTramite().getCondicionesurbanisticas().iterator(); itCuPV.hasNext();) {
					
					CondicionUrbanisticaBean cu = itCuPV.next();
					
					if(cu.getCodigoentidad_entidad().equals(entidad.getCodigo()) && 
								!cu.getCodigodeterminacion_determinacion().equals(Constants.GRUPO_ENTIDADES)){
						
						if(!isDeterminacinoUso( cu.getCodigodeterminacion_determinacion())){
							// se busca en las condiciones urbanisticas todas aquellas en las que interviene la entidad y se 
							// descarta la condicion urbanistica que tiene asociada la determinacion con codigo 7777777777 (GRUPO_ENTIDADES)
							DeterminacionBean determinacion= null;
							
							HashMap<String, Object> hashValores =  obtenerValores(cu, idValor, idValorReg );
							lstConfLayersValores = (ConfLayerValorBean[])hashValores.get(Constants.ID_VALOR);
							lstConfLayerValorReferencia = (ConfLayerValorReferenciaBean[])hashValores.get(Constants.ID_VALOR_REFERENCIA);
							lstConfLayerRegimenEspecifico = (ConfLayerRegimenEspecificoBean[])hashValores.get(Constants.ID_REGIMEN_ESPECIFICO);
							idValor = (Integer)hashValores.get("idvalor");
							idValorReg = (Integer)hashValores.get("idvalorreg");
							
							if(lstConfLayersValores != null || lstConfLayerValorReferencia != null | lstConfLayerRegimenEspecifico != null){
								// si la condicion urbanistica tiene algun valor o algun valor de referencia o algun regimen especifico
								boolean encontrada = false;
								Iterator<DeterminacionBean> itDetPV = planeamientovigente.getTramite().getDeterminaciones().iterator();
								while (!encontrada){
									DeterminacionBean determinacionAux = itDetPV.next();
									if( determinacionAux.getCodigo().equals(cu.getCodigodeterminacion_determinacion())){
										determinacion = determinacionAux;
										encontrada = true;
									}
								}
	
								if(determinacion != null){
									// la determinacion de la condicion urbanistica ha sido encontrada en la lista de determinaciones
									ConfLayerDeterminacionAplicadaBean confLayerDeterminacionAplicadaBean = new ConfLayerDeterminacionAplicadaBean();
									confLayerDeterminacionAplicadaBean.setSelected(false);
									DeterminacionLayerBean dl = new DeterminacionLayerBean();
									dl.setCodigo(determinacion.getCodigo());
									dl.setNombre(determinacion.getNombre().trim());
									confLayerDeterminacionAplicadaBean.setDeterminacionLayer(dl);
									String aliasAux = remplazaCaracteres(determinacion.getNombre().trim().toLowerCase().replace(" ","_"));
									String alias = "";
									if(aliasAux.length() > Constants.TAMANIO_COLUMNA){
										alias = aliasAux.substring(0,Constants.TAMANIO_COLUMNA-1);
									}
									else{
										alias = aliasAux;
									}
									confLayerDeterminacionAplicadaBean.setAliasDeterminacion(alias);
				
									// se añaden las listas de valores encontrados.
									confLayerDeterminacionAplicadaBean.setLstValores(lstConfLayersValores);
									confLayerDeterminacionAplicadaBean.setLstValoresReferencia(lstConfLayerValorReferencia);
									confLayerDeterminacionAplicadaBean.setLstRegimenesEspecificos(lstConfLayerRegimenEspecifico);
								
									if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACCION) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACCIONES)){
										
										//Acciones	
										addDeterminacionAplicada(Constants.LAYER_TITLE_PLANEAMIENTO_ACCIONES, lstConfLayerBean,
													confLayerDeterminacionAplicadaBean);					
									}	
									else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCION_POLIGONAL) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCION_LINEAL)||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCION_PUNTUAL)||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCIONES_ESTUDIO_DETALLE)){
										
										//Afecciones	
										addDeterminacionAplicada(Constants.LAYER_TITLE_PLANEAMIENTO_AFECCIONES, lstConfLayerBean,
												confLayerDeterminacionAplicadaBean);
									}
									else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_ALINEACION) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_RASANTE) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_COTA) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_PAUTA_ORDENACION) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_FONDOS_LINEAL)){
										
										//Alineaciones
										addDeterminacionAplicada(Constants.LAYER_TITLE_PLANEAMIENTO_ALINEACION, lstConfLayerBean,
												confLayerDeterminacionAplicadaBean);
									}
									else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO)){
										
										//Ambito	
										addDeterminacionAplicada(Constants.LAYER_TITLE_PLANEAMIENTO_AMBITO, lstConfLayerBean,
												confLayerDeterminacionAplicadaBean);
									}
									else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_URBANO) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_URBANIZABLE) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_NO_URBANIZABLE) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_URBANO) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_URBANIZABLE) ||					
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_RUSTICO) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_PUNTUAL) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_LINEAL) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_PROTEGIDO) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_NO_PROTEGIDO) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_RUSTICO_LINEAL) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_RUSTICO_PUNTUAL)){
										
										//Categoria
										addDeterminacionAplicada(Constants.LAYER_TITLE_PLANEAMIENTO_CATEGORIA, lstConfLayerBean,
												confLayerDeterminacionAplicadaBean);
									}
									else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_CLASE_SUELO)){
										
										//Clasificacion	
										addDeterminacionAplicada(Constants.LAYER_TITLE_PLANEAMIENTO_CLASIFICACION, lstConfLayerBean,
												confLayerDeterminacionAplicadaBean);
									}
									else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_SECTOR) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_SECTORIZACION)){
										
										//Desarrollo	
										addDeterminacionAplicada(Constants.LAYER_TITLE_PLANEAMIENTO_DESARROLLO, lstConfLayerBean,
												confLayerDeterminacionAplicadaBean);
									}
									else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_URBANA) || 
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_REPARTO)){
										
										//Equidistribucion	
										addDeterminacionAplicada(Constants.LAYER_TITLE_PLANEAMIENTO_EQUIDISTRIBUCION, lstConfLayerBean,
												confLayerDeterminacionAplicadaBean);
									}
									else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_HOMOGENEA) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_ACTUACION) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_EJECUCION) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_RESERVA) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ESPACIOS_GESTION_INTEGRADA) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO_ACTUACION)||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO_DESARROLLO) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_PLANEAMIENTO_ASUMIDO) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO_PLANEAMIENTO_DESARROLLO) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_NORMALIZACION) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_OTROS_AMBITOS) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACTUACION_AISLADA) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_PATRIMONIO_MUNICIPAL_SUELO) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDADES_NORMALIZACION_FINCAS)  ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACTUACIONES_URBANAS_CONCERTADAS) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROYECTO_ACTUACION_PRIORITARIA) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_INSTRUMENTO_GESTION) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_USO) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_INTERVENCION)){
										
										//Gestion	
										addDeterminacionAplicada(Constants.LAYER_TITLE_PLANEAMIENTO_GESTION, lstConfLayerBean,
												confLayerDeterminacionAplicadaBean);
		
									}							
									else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROTECCION_POLIGONAL) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROTECCION_LINEAL) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROTECCION_PUNTUAL)){
										
										//Proteccion	
										addDeterminacionAplicada(Constants.LAYER_TITLE_PLANEAMIENTO_PROTECCION, lstConfLayerBean,
												confLayerDeterminacionAplicadaBean);
									}
									else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_SISTEMA_POLIGONAL) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_SISTEMA_LINEAL) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_SISTEMAS_ESTUDIO_DETALLE)){
										
										//Sistemas	
										addDeterminacionAplicada(Constants.LAYER_TITLE_PLANEAMIENTO_SISTEMAS, lstConfLayerBean,
												confLayerDeterminacionAplicadaBean);
		//								
									}
									else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_USO_GLOBAL) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_ORDENACION_URBANISTICA) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_SUELO_NO_URBANIZABLE) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_SUELO_URBANIZABLE) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_SUELO_URBANO)||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_ALTURAS) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONAS_PLTO_INCORPORADO) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONAS_PLAN_PARCIAL) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONAS_ESTUDIO_DETALLE) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_PARCELA) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ALTURAS) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_FONFO) || 
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_EDIFICABILIDADES) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_INTERVENCION_PUNTUAL) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_ALTURA) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_EDIFICABILIDAD) ||
											grupoAplicacion.equals(Constants.GRUPO_APLICACION_PARCELA_SUSPENDIDA)){
										
										//Zona	
										addDeterminacionAplicada(Constants.LAYER_TITLE_PLANEAMIENTO_ZONA, lstConfLayerBean,
												confLayerDeterminacionAplicadaBean);
									}
								}
							}	
						}	
					}
				}
			}
		}
	}
	
	
	
	/** Remplaza los caracteres especiales.
	 * @param input
	 * @return
	 */
	public static String remplazaCaracteres(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ,;.:-*%ºª0123456789!@·#$~&/()=?¿^[]`+´¨{}";
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC_________________________________________";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }//for i
	    return output;
	}
	
	private void addDeterminacionUso(String nameLayer, ConfLayerBean[] lstConfLayerBean, DeterminacionLayerBean detUso){
		
		ConfLayerUsosRegulacionesBean[] lstConfLayerUsos = null;
		int index = 0;
		for (index = 0; index < lstConfLayerBean.length; index++) {
			if(lstConfLayerBean[index].getNameLayer().equals(nameLayer)){
				lstConfLayerUsos = lstConfLayerBean[index].getLstConfUsosRegulaciones();
				break;
			}
		}	
		ConfLayerUsosRegulacionesBean confLayerUso = new ConfLayerUsosRegulacionesBean();
		confLayerUso.setAlias(detUso.getNombre());
		confLayerUso.setDeterminacionUso(detUso);
		confLayerUso.setSelected(true);
		
		if(lstConfLayerUsos == null){
		
			lstConfLayerUsos = new ConfLayerUsosRegulacionesBean[1];
			lstConfLayerUsos[0] = confLayerUso;
			lstConfLayerBean[index].setLstConfUsosRegulaciones(lstConfLayerUsos);
		}
		else{
			boolean isExist = false;
			for (int j = 0; j < lstConfLayerUsos.length; j++) {
				if(((ConfLayerUsosRegulacionesBean)lstConfLayerUsos[j]).getDeterminacionUso().getCodigo().equals(detUso.getCodigo())){
					isExist = true;
				}
			}
			
			if(!isExist){
				
				lstConfLayerUsos = (ConfLayerUsosRegulacionesBean[]) Arrays.copyOf(lstConfLayerUsos, 
						lstConfLayerUsos.length+1);
			
				lstConfLayerUsos[lstConfLayerUsos.length-1] = confLayerUso;
				lstConfLayerBean[index].setLstConfUsosRegulaciones(lstConfLayerUsos);
			}
		}
	}

	
	private void gestionBusquedaRegulaciones(CondicionUrbanisticaBean cu, HashMap<String, RegulaConfBean> hashDetRegula){
		
		for (Iterator<CasoBean> itCasos = cu.getCasos().iterator(); itCasos.hasNext();) {
			CasoBean caso = itCasos.next();
			if(!caso.getRegimenes().isEmpty()){
				
				for (Iterator<Caso_RegimenBean> itRegimenes = caso.getRegimenes().iterator(); itRegimenes.hasNext();) {
			
					Caso_RegimenBean casoRegimen = itRegimenes.next();
					if(casoRegimen.getDeterminacionregimen_determinacion() != null &&
							!casoRegimen.getDeterminacionregimen_determinacion().equals("")){
						
						if(!hashDetRegula.containsKey(casoRegimen.getDeterminacionregimen_determinacion())){
							RegulaConfBean rcb = new RegulaConfBean();
							rcb.setCu(cu);
							rcb.setValorReferenciaDeterminacion(casoRegimen.getValorreferencia_determinacion());
							rcb.setValorReferenciaTramite(casoRegimen.getValorreferencia_codigoTramite());
							
							hashDetRegula.put(casoRegimen.getValorreferencia_determinacion(), rcb);
						}
					}
				}
			}
		}
		
	}
	
	/** Realiza la búsqueda de regulaciones aplicables para la configuración de las layers
	 * @param usoAplicableList
	 * @return
	 * @throws Exception
	 */
	public void obtenerDatosRegulacionesMigracionAsistida(ConfLayerBean[] lstConfLayerBean) throws Exception{

		HashMap<String, HashMap> lstHashRegLayers = new HashMap<String, HashMap>();
		for(int i=0; i<lstConfLayerBean.length; i++){
			HashMap<String, RegulaConfBean> hashDetReg = new HashMap<String, RegulaConfBean> ();
			lstHashRegLayers.put(lstConfLayerBean[i].getNameLayer(),hashDetReg);
		}

		for (Iterator itCuPV = planeamientovigente.getTramite().getCondicionesurbanisticas().iterator(); itCuPV.hasNext();) {
			// recorremos las condiciones urbanisticas del planeamiento vigente
			CondicionUrbanisticaBean cu = (CondicionUrbanisticaBean) itCuPV.next();
			ConfLayerBean confLayer = null;
			String grupoAplicacion = searchCodigoGrupoApliacionEntidad(cu.getCodigoentidad_entidad(), planeamientovigente.getTramite().getCondicionesurbanisticas());

			if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACCION) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACCIONES)){
				
				//Acciones	
				gestionBusquedaRegulaciones(cu, lstHashRegLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_ACCIONES));					
			}	
			else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCION_POLIGONAL) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCION_LINEAL)||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCION_PUNTUAL)||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCIONES_ESTUDIO_DETALLE)){
				
				//Afecciones	
				gestionBusquedaRegulaciones(cu, lstHashRegLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_AFECCIONES));	
			}
			else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_ALINEACION) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_RASANTE) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_COTA) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_PAUTA_ORDENACION) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_FONDOS_LINEAL)){
				
				//Alineaciones
				gestionBusquedaRegulaciones(cu, lstHashRegLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_ALINEACION));	
			}
			else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO)){
				
				//Ambito	
				gestionBusquedaRegulaciones(cu, lstHashRegLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_AMBITO));	
			}
			else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_URBANO) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_URBANIZABLE) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_NO_URBANIZABLE) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_URBANO) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_URBANIZABLE) ||					
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_RUSTICO) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_PUNTUAL) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_LINEAL) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_PROTEGIDO) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_NO_PROTEGIDO) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_RUSTICO_LINEAL) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_RUSTICO_PUNTUAL)){
				
				//Categoria
				gestionBusquedaRegulaciones(cu, lstHashRegLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_CATEGORIA));	

			}
			else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_CLASE_SUELO)){
				
				//Clasificacion	
				gestionBusquedaRegulaciones(cu, lstHashRegLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_CLASIFICACION));	
			}
			else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_SECTOR) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_SECTORIZACION)){
				
				//Desarrollo	
				gestionBusquedaRegulaciones(cu, lstHashRegLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_DESARROLLO));	
			}
			else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_URBANA) || 
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_REPARTO)){
				
				//Equidistribucion	
				gestionBusquedaRegulaciones(cu, lstHashRegLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_EQUIDISTRIBUCION));	
			}			
			else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_HOMOGENEA) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_ACTUACION) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_EJECUCION) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_RESERVA) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ESPACIOS_GESTION_INTEGRADA) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO_ACTUACION)||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO_DESARROLLO) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_PLANEAMIENTO_ASUMIDO) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO_PLANEAMIENTO_DESARROLLO) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_NORMALIZACION) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_OTROS_AMBITOS) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACTUACION_AISLADA) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_PATRIMONIO_MUNICIPAL_SUELO) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDADES_NORMALIZACION_FINCAS)  ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACTUACIONES_URBANAS_CONCERTADAS) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROYECTO_ACTUACION_PRIORITARIA) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_INSTRUMENTO_GESTION) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_USO) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_INTERVENCION)){
				
				//Gestion	
				gestionBusquedaRegulaciones(cu, lstHashRegLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_GESTION));	

			}
			else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROTECCION_POLIGONAL) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROTECCION_LINEAL) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROTECCION_PUNTUAL)){
				
				//Proteccion	
				gestionBusquedaRegulaciones(cu, lstHashRegLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_PROTECCION));
			}
			else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_SISTEMA_POLIGONAL) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_SISTEMA_LINEAL) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_SISTEMAS_ESTUDIO_DETALLE)){
				
				//Sistemas	
				gestionBusquedaRegulaciones(cu, lstHashRegLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_SISTEMAS));	
			}

			else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_USO_GLOBAL) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_ORDENACION_URBANISTICA) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_SUELO_NO_URBANIZABLE) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_SUELO_URBANIZABLE) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_SUELO_URBANO)||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_ALTURAS) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONAS_PLTO_INCORPORADO) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONAS_PLAN_PARCIAL) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONAS_ESTUDIO_DETALLE) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_PARCELA) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ALTURAS) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_FONFO) || 
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_EDIFICABILIDADES) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_INTERVENCION_PUNTUAL) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_ALTURA) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_EDIFICABILIDAD) ||
					grupoAplicacion.equals(Constants.GRUPO_APLICACION_PARCELA_SUSPENDIDA)){

				// Zona
				gestionBusquedaRegulaciones(cu, lstHashRegLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_ZONA));
			}
		}
		
		
		for (Iterator<String> itLstRegulaciones = lstHashRegLayers.keySet().iterator(); itLstRegulaciones.hasNext();) {
			
			String nameLayer = itLstRegulaciones.next();
			HashMap<String, RegulaConfBean> hashDetReg = lstHashRegLayers.get(nameLayer);
			
			boolean encontrado = false;
			int index=0;
			ConfLayerBean confLayer = null;
			while(!encontrado){
				if(lstConfLayerBean[index].getNameLayer().equals(nameLayer)){
					confLayer = lstConfLayerBean[index];
					encontrado = true;
				}
				index++;
			}
			
			
			for (Iterator<String> itZona = hashDetReg.keySet().iterator(); itZona.hasNext();) {
				String keyCodDetVR = itZona.next();
				RegulaConfBean rcb = hashDetReg.get(keyCodDetVR);
				
				if(catalogosistematizado.getTramite().getCodigo().equals(rcb.getValorReferenciaTramite())){
					guardarRegulacion(catalogosistematizado.getTramite(), rcb, keyCodDetVR, confLayer);
				}
				else if (planeamientovigente.getTramite().getCodigo().equals(rcb.getValorReferenciaTramite())){
					guardarRegulacion(planeamientovigente.getTramite(), rcb, keyCodDetVR, confLayer);
				}
			}
		}
	}
	
	
	private void guardarRegulacion(TramiteBean tramite, RegulaConfBean rcb, String keyCodDetVR, ConfLayerBean lstConfLayerBean){
		
		//if(tramite.getCodigo().equals(rcb.getValorReferenciaTramite())){
			for (Iterator<DeterminacionBean> itDetCS = tramite.getDeterminaciones().iterator(); itDetCS.hasNext();) {
	
				DeterminacionBean detCS = (DeterminacionBean) itDetCS.next();
				if(detCS.getCodigo().equals(keyCodDetVR)){
					if(lstConfLayerBean.getLstConfUsosRegulaciones() != null) {
						for(int i=0; i<lstConfLayerBean.getLstConfUsosRegulaciones().length; i++){
							
							if(lstConfLayerBean.getLstConfUsosRegulaciones()[i].getDeterminacionUso().getCodigo()
									.equals(rcb.getCu().getCodigodeterminacion_determinacion())){
								
								DeterminacionLayerBean detCopyRegu = new DeterminacionLayerBean();
								detCopyRegu.setCodigo(detCS.getCodigo());
								detCopyRegu.setNombre(detCS.getNombre());
								
								ConfRegulacionesBean[] lstConfRegula = lstConfLayerBean.getLstConfUsosRegulaciones()[i].getLstRegulaciones();
								ConfRegulacionesBean confRegula = new ConfRegulacionesBean();
								confRegula.setAlias(detCS.getNombre());
								confRegula.setSelected(false);
								confRegula.setRegulacionValor(detCopyRegu);
								
								if(lstConfRegula == null){
									lstConfRegula = new ConfRegulacionesBean[1];
									lstConfRegula[0] = confRegula;
									lstConfLayerBean.getLstConfUsosRegulaciones()[i].setLstRegulaciones(lstConfRegula);
								
								}
								else{
									lstConfRegula = (ConfRegulacionesBean[]) Arrays.copyOf(lstConfRegula, 
											lstConfRegula.length+1);
								
									lstConfRegula[lstConfRegula.length-1] = confRegula;
									lstConfLayerBean.getLstConfUsosRegulaciones()[i].setLstRegulaciones(lstConfRegula);
								}
							}
						}
					}
				}
			}
		//}	
	}
	
	
	private boolean isDeterminacinoUso(String codigoDet) throws Exception{
		
		boolean isCaracterUso = false;
		String codCarDetUso = OperacionesBBDD.obtenerPropiedadesConfiguracion(Constants.CARACTERDETERMINACION_USO);
		Iterator<DeterminacionBean> itDet =  planeamientovigente.getTramite().getDeterminaciones().iterator();
		while (!isCaracterUso && itDet.hasNext()) {
			DeterminacionBean detPV =  itDet.next();
			if(codigoDet.equals(detPV.getCodigo())){
				
				if(detPV.getCaracter().equals(codCarDetUso)){
					isCaracterUso = true;
				}
				
			}
			
		}
		
		itDet =  planeamientovigente.getTramite().getDeterminaciones().iterator();
		while (!isCaracterUso && itDet.hasNext()) {
			DeterminacionBean detPV =  itDet.next();
			if(codigoDet.equals(detPV.getCodigo())){
				
				if(detPV.getCaracter().equals(codCarDetUso)){
					isCaracterUso = true;
				}
				
			}
			
		}
		
		return isCaracterUso;
	}
	
	
	
	
	
	
	/** Realiza la búsqueda de usos aplicables para la configuración de las layers
	 * @param usoAplicableList
	 * @return
	 * @throws Exception
	 */
	public void obtenerDatosUsosRegulacionesMigracionsAsistida(ConfLayerBean[] lstConfLayerBean) throws Exception{

		String codCarDetUso = OperacionesBBDD.obtenerPropiedadesConfiguracion(Constants.CARACTERDETERMINACION_USO);
	
		for (Iterator itDetPV =  planeamientovigente.getTramite().getDeterminaciones().iterator(); itDetPV.hasNext();) {
			// se recorren todas las determinaciones que existen en el Plan Vigente 
			DeterminacionBean detPV = (DeterminacionBean) itDetPV.next();
			
			if(detPV.getCaracter().equals(codCarDetUso)){
			
			
			
//			if(detPV.getBase_determinacion() != null && !detPV.getBase_determinacion().equals("")){
//				//se obtienen todas aquellas determinaciones del Plan Vigente que tienen asociada una Base
//				if (detPV.getBase_codigoTramite().equals(catalogosistematizado.getTramite().getCodigo())){
//					// se obtienen todas aquelas determinaciones que la base es del Catalogo Sistematizado
//					for (Iterator itDetCS = catalogosistematizado.getTramite().getDeterminaciones().iterator(); itDetCS.hasNext();) {
//						//se recorren todas las determinaciones del Catalogo Sistematizado
//						DeterminacionBean detCS = (DeterminacionBean) itDetCS.next();
//						if(detCS.getCodigo().equals(detPV.getBase_determinacion())){
//							// se obtiene la determinacio del Catalogo Sistematizado que es base en el Plan Vigente
//							if(detCS.getCaracter().equals(codCarDetUso)){
								// Obtenemos solo aquellas determinaciones que tienen caracter de USO -- 000009

								DeterminacionLayerBean detUso = new DeterminacionLayerBean();
								detUso.setCodigo(detPV.getCodigo());
								detUso.setNombre(detPV.getNombre().trim()); 
//								detUso.setNombre(detCS.getNombre().trim().toLowerCase()); 
								
								//Se busca a que grupo es aplicable
								for (Iterator itCUPV =  planeamientovigente.getTramite().getCondicionesurbanisticas().iterator(); itCUPV.hasNext();) {
									CondicionUrbanisticaBean cuPV = (CondicionUrbanisticaBean) itCUPV.next();
									
									if(cuPV.getCodigodeterminacion_determinacion().equals(detPV.getCodigo())){
										String codigoEntidadCU = cuPV.getCodigoentidad_entidad();
										 // se busca el grupo de aplicacion de la entidadad para determinar la capa
										String grupoAplicacion = searchCodigoGrupoApliacionEntidad(codigoEntidadCU, planeamientovigente.getTramite().getCondicionesurbanisticas() );
		
										if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACCION) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACCIONES)){
											
											//Acciones	
											addDeterminacionUso(Constants.LAYER_TITLE_PLANEAMIENTO_ACCIONES, lstConfLayerBean, detUso);	
											addDeterminacionRegulacion(cuPV, lstConfLayerBean, Constants.LAYER_TITLE_PLANEAMIENTO_ACCIONES, detUso);
										}	
										else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCION_POLIGONAL) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCION_LINEAL)||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCION_PUNTUAL)||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCIONES_ESTUDIO_DETALLE)){
											
											//Afecciones	
											addDeterminacionUso(Constants.LAYER_TITLE_PLANEAMIENTO_AFECCIONES, lstConfLayerBean, detUso);	
											addDeterminacionRegulacion(cuPV, lstConfLayerBean, Constants.LAYER_TITLE_PLANEAMIENTO_AFECCIONES, detUso);
										}
										else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_ALINEACION) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_RASANTE) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_COTA) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_PAUTA_ORDENACION) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_FONDOS_LINEAL)){
											
											//Alineaciones
											addDeterminacionUso(Constants.LAYER_TITLE_PLANEAMIENTO_ALINEACION, lstConfLayerBean, detUso);	
											addDeterminacionRegulacion(cuPV, lstConfLayerBean, Constants.LAYER_TITLE_PLANEAMIENTO_ALINEACION, detUso);
										}
										else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO)){
											
											//Ambito	
											addDeterminacionUso(Constants.LAYER_TITLE_PLANEAMIENTO_AMBITO, lstConfLayerBean, detUso);
											addDeterminacionRegulacion(cuPV, lstConfLayerBean, Constants.LAYER_TITLE_PLANEAMIENTO_AMBITO, detUso);
										}
										else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_URBANO) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_URBANIZABLE) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_NO_URBANIZABLE) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_URBANO) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_URBANIZABLE) ||					
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_RUSTICO) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_PUNTUAL) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_LINEAL) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_PROTEGIDO) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_NO_PROTEGIDO) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_RUSTICO_LINEAL) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_RUSTICO_PUNTUAL)){
											
											//Categoria
											addDeterminacionUso(Constants.LAYER_TITLE_PLANEAMIENTO_CATEGORIA, lstConfLayerBean, detUso);	
											addDeterminacionRegulacion(cuPV, lstConfLayerBean, Constants.LAYER_TITLE_PLANEAMIENTO_CATEGORIA, detUso);

										}
										else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_CLASE_SUELO)){
											
											//Clasificacion	
											addDeterminacionUso(Constants.LAYER_TITLE_PLANEAMIENTO_CLASIFICACION, lstConfLayerBean,	detUso);	
											addDeterminacionRegulacion(cuPV, lstConfLayerBean, Constants.LAYER_TITLE_PLANEAMIENTO_CLASIFICACION, detUso);
										}
										else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_SECTOR) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_SECTORIZACION)){
											
											//Desarrollo	
											addDeterminacionUso(Constants.LAYER_TITLE_PLANEAMIENTO_DESARROLLO, lstConfLayerBean, detUso);		
											addDeterminacionRegulacion(cuPV, lstConfLayerBean, Constants.LAYER_TITLE_PLANEAMIENTO_DESARROLLO, detUso);
										}	
										else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_URBANA) || 
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_REPARTO)){
											
											//Equidistribucion	
											addDeterminacionUso(Constants.LAYER_TITLE_PLANEAMIENTO_EQUIDISTRIBUCION, lstConfLayerBean, detUso);
											addDeterminacionRegulacion(cuPV, lstConfLayerBean, Constants.LAYER_TITLE_PLANEAMIENTO_EQUIDISTRIBUCION, detUso);
										}		
										else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_HOMOGENEA) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_ACTUACION) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_EJECUCION) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_RESERVA) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ESPACIOS_GESTION_INTEGRADA) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO_ACTUACION)||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO_DESARROLLO) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_PLANEAMIENTO_ASUMIDO) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO_PLANEAMIENTO_DESARROLLO) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_NORMALIZACION) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_OTROS_AMBITOS) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACTUACION_AISLADA) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_PATRIMONIO_MUNICIPAL_SUELO) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDADES_NORMALIZACION_FINCAS)  ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACTUACIONES_URBANAS_CONCERTADAS) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROYECTO_ACTUACION_PRIORITARIA) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_INSTRUMENTO_GESTION) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_USO) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_INTERVENCION)){
											
											//Gestion
											addDeterminacionUso(Constants.LAYER_TITLE_PLANEAMIENTO_GESTION, lstConfLayerBean, detUso);	
											addDeterminacionRegulacion(cuPV, lstConfLayerBean, Constants.LAYER_TITLE_PLANEAMIENTO_GESTION, detUso);
										}
										else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROTECCION_POLIGONAL) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROTECCION_LINEAL) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROTECCION_PUNTUAL)){
											
											//Proteccion	
											addDeterminacionUso(Constants.LAYER_TITLE_PLANEAMIENTO_PROTECCION, lstConfLayerBean, detUso);	
											addDeterminacionRegulacion(cuPV, lstConfLayerBean, Constants.LAYER_TITLE_PLANEAMIENTO_PROTECCION, detUso);
										}
										else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_SISTEMA_POLIGONAL) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_SISTEMA_LINEAL) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_SISTEMAS_ESTUDIO_DETALLE)){
											
											//Sistemas	
											addDeterminacionUso(Constants.LAYER_TITLE_PLANEAMIENTO_SISTEMAS, lstConfLayerBean, detUso);	
											addDeterminacionRegulacion(cuPV, lstConfLayerBean, Constants.LAYER_TITLE_PLANEAMIENTO_SISTEMAS, detUso);
										}
										else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_USO_GLOBAL) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_ORDENACION_URBANISTICA) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_SUELO_NO_URBANIZABLE) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_SUELO_URBANIZABLE) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_SUELO_URBANO)||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_ALTURAS) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONAS_PLTO_INCORPORADO) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONAS_PLAN_PARCIAL) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONAS_ESTUDIO_DETALLE) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_PARCELA) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ALTURAS) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_FONFO) || 
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_EDIFICABILIDADES) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_INTERVENCION_PUNTUAL) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_ALTURA) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_EDIFICABILIDAD) ||
												grupoAplicacion.equals(Constants.GRUPO_APLICACION_PARCELA_SUSPENDIDA)){

											// Zona
											addDeterminacionUso(Constants.LAYER_TITLE_PLANEAMIENTO_ZONA, lstConfLayerBean, detUso);
											addDeterminacionRegulacion(cuPV, lstConfLayerBean, Constants.LAYER_TITLE_PLANEAMIENTO_ZONA, detUso);
										}
									}
								}
//							}
//						}
//					}
//				}
			}	
		}
	}
	
	
	/** Busqueda de las regulaciones aplicadas y se almacenan con el uso correspondiente
	 * @param cuPV
	 * @param lstConfLayerBean
	 * @param nameLayer
	 */
	private void addDeterminacionRegulacion(CondicionUrbanisticaBean cuPV, ConfLayerBean[] lstConfLayerBean, String nameLayer, DeterminacionLayerBean detUso){
		
		for (Iterator<CasoBean> itCasos = cuPV.getCasos().iterator(); itCasos.hasNext();) {
			CasoBean caso = itCasos.next();
			for (Iterator<Caso_RegimenBean> itRegimenes = caso.getRegimenes().iterator(); itRegimenes.hasNext();) {
				Caso_RegimenBean regimen = itRegimenes.next();
				
				DeterminacionBean detRegVr = null;
				DeterminacionBean detReg = null;
			
				if(this.catalogosistematizado != null && regimen != null && regimen.getValorreferencia_codigoTramite() != null &&
						regimen.getValorreferencia_codigoTramite().equals(this.catalogosistematizado.getTramite().getCodigo())){
					detRegVr = obtenerDeterminacionTramite(this.catalogosistematizado.getTramite().getDeterminaciones(), regimen.getValorreferencia_determinacion());
				}
				else if(this.planeamientovigente != null &&  regimen != null && regimen.getValorreferencia_codigoTramite() != null &&
						regimen.getValorreferencia_codigoTramite().equals(this.planeamientovigente.getTramite().getCodigo())){
					detRegVr = obtenerDeterminacionTramite(this.planeamientovigente.getTramite().getDeterminaciones(), regimen.getValorreferencia_determinacion());
				}
				
				if(this.catalogosistematizado != null && regimen != null && regimen.getDeterminacionregimen_codigoTramite() != null &&
						regimen.getDeterminacionregimen_codigoTramite().equals(this.catalogosistematizado.getTramite().getCodigo())){
					detReg = obtenerDeterminacionTramite(this.catalogosistematizado.getTramite().getDeterminaciones(), regimen.getDeterminacionregimen_determinacion());
				}
				else if(this.planeamientovigente != null && regimen != null && regimen.getDeterminacionregimen_codigoTramite() != null &&
						regimen.getDeterminacionregimen_codigoTramite().equals(this.planeamientovigente.getTramite().getCodigo())){
					detReg = obtenerDeterminacionTramite(this.planeamientovigente.getTramite().getDeterminaciones(), regimen.getDeterminacionregimen_determinacion()); 
				}
				
				if(detRegVr != null && detReg != null){
					int index = 0;
					ConfLayerUsosRegulacionesBean[] lstConfLayerUsos = null;
					for (index = 0; index < lstConfLayerBean.length; index++) {
						if(lstConfLayerBean[index].getNameLayer().equals(nameLayer)){
							lstConfLayerUsos = lstConfLayerBean[index].getLstConfUsosRegulaciones();
							break;
						}
					}	
					if(lstConfLayerUsos != null){
						for (int b=0; b< lstConfLayerUsos.length; b++){
							ConfLayerUsosRegulacionesBean usoRegula = lstConfLayerUsos[b];
							if(usoRegula.getDeterminacionUso().getCodigo().equals(detUso.getCodigo())){
								
								ConfRegulacionesBean regula = new ConfRegulacionesBean();
								regula.setSelected(false);
								String aliasAux = remplazaCaracteres(detRegVr.getNombre().trim().replace(" ", "_").toLowerCase());
								String alias = "";
								if(aliasAux.length() > Constants.TAMANIO_COLUMNA){
									alias = aliasAux.substring(0,Constants.TAMANIO_COLUMNA-1);
								}
								else{
									alias = aliasAux;
								}
								regula.setAlias(alias);
								
								DeterminacionLayerBean detlayerRegVr = new DeterminacionLayerBean();
								detlayerRegVr.setCodigo(detRegVr.getCodigo());
								detlayerRegVr.setNombre(detRegVr.getNombre().trim());
								
								DeterminacionLayerBean detlayerReg = new DeterminacionLayerBean();
								if(detReg != null){
									detlayerReg.setCodigo(detReg.getCodigo());
									detlayerReg.setNombre(detReg.getNombre().trim());
								}
								
								regula.setRegulacionValor(detlayerRegVr);
								regula.setRegulacion(detlayerReg);
								
								if(usoRegula.getLstRegulaciones() == null){
									
									usoRegula.setLstRegulaciones(new ConfRegulacionesBean[1]);
									usoRegula.getLstRegulaciones()[0] = regula;				
								}
								else{
									
									boolean isExist = false;
									for (int j = 0; j < usoRegula.getLstRegulaciones().length; j++) {
										if(((ConfRegulacionesBean)usoRegula.getLstRegulaciones()[j]).getRegulacionValor().getCodigo().equals(detlayerRegVr.getCodigo())){
											isExist = true;
										}
									}
									if(!isExist){
										
										usoRegula.setLstRegulaciones(  (ConfRegulacionesBean[]) Arrays.copyOf(usoRegula.getLstRegulaciones()
															,usoRegula.getLstRegulaciones().length+1)  );
										usoRegula.getLstRegulaciones()[usoRegula.getLstRegulaciones().length-1] = regula;
										
									}
								}
							}
						}
					}
				}
			}
		}

	}

	
	/**
	 * Comprueba si ya existe diccionario cargado en la BBDD
	 */
	public boolean existDiccionarioIntoDB(Connection conn) throws SQLException {
		
		boolean existeDiccionario = false;
		pst = this.conn
				.prepareStatement("SELECT COUNT(*) AS total FROM gestorfip.diccionario_ambitos");
		rs = pst.executeQuery();
		while (rs.next()){
			if(rs.getInt("total") != 0){
				existeDiccionario = true;
			}
		}
			
		
		return existeDiccionario;
	}
	/**
	 * Inserta el diccionario contenido en el FIP en la BD
	 */
	public void loadDiccionarioIntoDB() throws SQLException {
		//String loadDictionary = "";
		TipoAmbitoBean tab = null;
		AmbitoBean ab = null;
		RelacionBean rb = null;
		CaracterDeterminacionBean cdb = null;
		TipoEntidadBean teb = null;
		TipoOperacionEntidadBean toeb = null;
		TipoOperacionDeterminacionBean todb = null;
		OperacionCaracterBean ocb = null;
		TipoDocumentoBean tdb = null;
		GrupoDocumentoBean gdb  = null;
		TipoTramiteBean ttb = null;
		InstrumentoBean ib  = null;
		try { 
			// Insercion de los tipos de ambitos

			Iterator<TipoAmbitoBean> i_tab = diccionario.getTiposAmbito()
					.iterator();
			while (i_tab.hasNext()) {

				tab = (TipoAmbitoBean) i_tab.next();

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.diccionario_tiposambito (id, codigo, nombre) "
								+ "VALUES (nextval('gestorfip.seq_diccionario_tiposambito'),?,?)");
				pst.setString(1, tab.getCodigo());
				pst.setString(2, tab.getNombre());
				pst.executeUpdate();

			}
			//loadDictionary = "Cargado diccionario_tiposambito";
		} catch (SQLException e) {
			logger.error("Se ha producido un error en la carga del diccionario: tiposAmbito: codigo: " +tab.getCodigo() + " *** nombre: "+tab.getNombre());
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		try {
			// Insercion de los ambitos
			Iterator<AmbitoBean> i_ab = diccionario.getAmbitos().iterator();
			while (i_ab.hasNext()) {

				 ab = (AmbitoBean) i_ab.next();

				// Recuperacion del id del tipoambito
				int tipoambito_id = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.diccionario_tiposambito WHERE codigo = ?");
				pst.setString(1, ab.getTipoambito());
				rs = pst.executeQuery();
				while (rs.next())
					tipoambito_id = rs.getInt(1);

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.diccionario_ambitos (id, nombre, codigo, ine, tipoambito) "
								+ "VALUES (nextval('gestorfip.seq_diccionario_ambitos'),?,?,?,?)");
				pst.setString(1, ab.getNombre());
				pst.setString(2, ab.getCodigo());
				pst.setString(3, ab.getIne());
				pst.setInt(4, tipoambito_id);
				pst.executeUpdate();
				
			}
			//loadDictionary = "Cargado diccionario_ambitos";
		} catch (SQLException e) {
			logger.error("Se ha producido un error en la carga del diccionario: Ambito: codigo: " +ab.getCodigo()+ " *** nombre: "+ab.getNombre());
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		try{
			// Insercion de las relaciones
			Iterator<RelacionBean> i_rb = diccionario.getOrganigramaAmbitos()
					.iterator();
			while (i_rb.hasNext()) {

				 rb = (RelacionBean) i_rb.next();

				// Recuperacion de los ids de los padres/hijos
				int padre_id = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.diccionario_ambitos WHERE codigo = ?");
				pst.setString(1, rb.getPadre());
				rs = pst.executeQuery();
				while (rs.next())
					padre_id = rs.getInt(1);

				int hijo_id = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.diccionario_ambitos WHERE codigo = ?");
				pst.setString(1, rb.getHijo());
				rs = pst.executeQuery();
				while (rs.next())
					hijo_id = rs.getInt(1);

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.diccionario_organigramaambitos (id, padre, hijo) "
								+ "VALUES (nextval('gestorfip.seq_diccionario_organigramaambitos'),?,?)");
				pst.setInt(1, padre_id);
				pst.setInt(2, hijo_id);
				pst.executeUpdate();
			}
			//loadDictionary = "Cargado diccionario_organigramaambitos";
		} catch (SQLException e) {
			logger.error("Se ha producido un error en la carga del diccionario: RelacionAmbitos: padre: " +rb.getPadre()+ " *** hijo: "+rb.getHijo());
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		try{
			// Insercion de los caracteres determinacion
			Iterator<CaracterDeterminacionBean> i_cdb = diccionario
					.getCaracteresDeterminacion().iterator();
			while (i_cdb.hasNext()) {

				 cdb = (CaracterDeterminacionBean) i_cdb
						.next();

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.diccionario_caracteresdeterminacion (id, codigo, nombre, aplicaciones_max, aplicaciones_min) "
								+ "VALUES (nextval('gestorfip.seq_diccionario_caracteresdeterminacion'),?,?,?,?)");
				pst.setString(1, cdb.getCodigo());
				pst.setString(2, cdb.getNombre());
				pst.setInt(3, cdb.getAplicaciones_max());
				pst.setInt(4, cdb.getAplicaciones_min());
				pst.executeUpdate();
			}
			//loadDictionary = "Cargado diccionario_caracteresdeterminacion";
		} catch (SQLException e) {
			logger.error("Se ha producido un error en la carga del diccionario: CaracteresDeterminacion: codigo: " +cdb.getCodigo()+ " *** nombre: "+cdb.getNombre());
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		try{
			// Insercion de los tipos entitad
			Iterator<TipoEntidadBean> i_teb = diccionario.getTiposEntidad()
					.iterator();
			while (i_teb.hasNext()) {

				 teb = (TipoEntidadBean) i_teb.next();

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.diccionario_tiposentidad (id, codigo, nombre) "
								+ "VALUES (nextval('gestorfip.seq_diccionario_tiposentidad'),?,?)");
				pst.setString(1, teb.getCodigo());
				pst.setString(2, teb.getNombre());
				pst.executeUpdate();
			}
		//	loadDictionary = "tipos diccionario_tiposentidad";
			
		} catch (SQLException e) {
			logger.error("Se ha producido un error en la carga del diccionario: TiposEntidad: codigo: " +teb.getCodigo()+ " *** nombre: "+teb.getNombre());
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		try{
			// Insercion de los tipos operacion entitad
			Iterator<TipoOperacionEntidadBean> i_toeb = diccionario
					.getTiposOperacionEntidad().iterator();
			while (i_toeb.hasNext()) {

				 toeb = (TipoOperacionEntidadBean) i_toeb
						.next();

				// Recuperacion del id del tipoentidad
				int tipoentidad_id = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.diccionario_tiposentidad WHERE codigo = ?");
				pst.setString(1, toeb.getTipoEntidad());
				rs = pst.executeQuery();
				while (rs.next())
					tipoentidad_id = rs.getInt(1);

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.diccionario_tiposoperacionentidad (id, codigo, nombre, tipoentidad) "
								+ "VALUES (nextval('gestorfip.seq_diccionario_tiposoperacionentidad'),?,?,?)");
				pst.setString(1, toeb.getCodigo());
				pst.setString(2, toeb.getNombre());
				pst.setInt(3, tipoentidad_id);
				pst.executeUpdate();
			}
		//	loadDictionary = "Cargado diccionario_tiposoperacionentidad";
			
		} catch (SQLException e) {
			logger.error("Se ha producido un error en la carga del diccionario: TiposOperacionEntidad: codigo: " +toeb.getCodigo()+ " *** nombre: "+toeb.getNombre());
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		try{
			// Insercion de los tipos operacion determinacion
			Iterator<TipoOperacionDeterminacionBean> i_todb = diccionario
					.getTiposOperacionDeterminacion().iterator();
			while (i_todb.hasNext()) {

				 todb = (TipoOperacionDeterminacionBean) i_todb
						.next();

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.diccionario_tiposoperaciondeterminacion (id, codigo, nombre) "
								+ "VALUES (nextval('gestorfip.seq_diccionario_tiposoperaciondeterminacion'),?,?)");
				pst.setString(1, todb.getCodigo());
				pst.setString(2, todb.getNombre());
				pst.executeUpdate();
			}
		//	loadDictionary = "Cargado diccionario_tiposoperaciondeterminacion";
		} catch (SQLException e) {
			logger.error("Se ha producido un error en la carga del diccionario: TiposOperacionDeterminacion: codigo: " +todb.getCodigo()+ " *** nombre: "+todb.getNombre());
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
			
		try{
			// Insercion de las operaciones caracteres
			Iterator<OperacionCaracterBean> i_ocb = diccionario
					.getOperacionesCaracteres().iterator();
			while (i_ocb.hasNext()) {

				 ocb = (OperacionCaracterBean) i_ocb
						.next();

				// Recuperacion del id del tipooperaciondeterminacion
				int tipooperaciondeterminacion_id = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.diccionario_tiposoperaciondeterminacion WHERE codigo = ?");
				pst.setString(1, ocb.getTipoOperacionDeterminacion());
				rs = pst.executeQuery();
				while (rs.next())
					tipooperaciondeterminacion_id = rs.getInt(1);

				// Recuperacion de los ids del caracteroperadora y del
				// caracteroperada
				int caracteroperadora_id = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.diccionario_caracteresdeterminacion WHERE codigo = ?");
				pst.setString(1, ocb.getCaracterOperadora());
				rs = pst.executeQuery();
				while (rs.next())
					caracteroperadora_id = rs.getInt(1);

				int caracteroperada_id = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.diccionario_caracteresdeterminacion WHERE codigo = ?");
				pst.setString(1, ocb.getCaracterOperada());
				rs = pst.executeQuery();
				while (rs.next())
					caracteroperada_id = rs.getInt(1);

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.diccionario_operacionescaracteres (id, tipooperaciondeterminacion, caracteroperadora, caracteroperada) "
								+ "VALUES (nextval('gestorfip.seq_diccionario_operacionescaracteres'),?,?,?)");
				pst.setInt(1, tipooperaciondeterminacion_id);
				pst.setInt(2, caracteroperadora_id);
				pst.setInt(3, caracteroperada_id);
				pst.executeUpdate();
			}
			//loadDictionary = "Cargado diccionario_operacionescaracteres";
		} catch (SQLException e) {
			logger.error("Se ha producido un error en la carga del diccionario: TiposOperacionCaracteres: operadora: " +ocb.getCaracterOperadora()+ " *** operada: "+ocb.getCaracterOperada());
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		
		try{
			// Insercion de los tipos documento
			Iterator<TipoDocumentoBean> i_tdb = diccionario.getTiposDocumento()
					.iterator();
			while (i_tdb.hasNext()) {

				 tdb = (TipoDocumentoBean) i_tdb.next();

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.diccionario_tiposdocumento (id, codigo, nombre) "
								+ "VALUES (nextval('gestorfip.seq_diccionario_tiposdocumento'),?,?)");
				pst.setString(1, tdb.getCodigo());
				pst.setString(2, tdb.getNombre());
				pst.executeUpdate();
			}
		//	loadDictionary = "Cargado diccionario_tiposdocumento";
			
		} catch (SQLException e) {
			logger.error("Se ha producido un error en la carga del diccionario: TiposDocumento: codigo: " +tdb.getCodigo()+ " *** nombre: "+tdb.getNombre());
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		
		try{
			// Insercion de los grupos documento
			Iterator<GrupoDocumentoBean> i_gdb = diccionario
					.getGruposDocumento().iterator();
			while (i_gdb.hasNext()) {

				gdb = (GrupoDocumentoBean) i_gdb.next();

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.diccionario_gruposdocumento (id, codigo, nombre) "
								+ "VALUES (nextval('gestorfip.seq_diccionario_gruposdocumento'),?,?)");
				pst.setString(1, gdb.getCodigo());
				pst.setString(2, gdb.getNombre());
				pst.executeUpdate();
			}
			//loadDictionary = "Cargado diccionario_gruposdocumento";
			
		} catch (SQLException e) {
			logger.error("Se ha producido un error en la carga del diccionario: GruposDocumento: codigo: " +gdb.getCodigo()+ " *** nombre: "+gdb.getNombre());
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		try{
			// Insercion de los tipos tramite
			Iterator<TipoTramiteBean> i_ttb = diccionario.getTiposTramites()
					.iterator();
			while (i_ttb.hasNext()) {

				ttb = (TipoTramiteBean) i_ttb.next();

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.diccionario_tipostramite (id, codigo, nombre) "
								+ "VALUES (nextval('gestorfip.seq_diccionario_tipostramite'),?,?)");
				pst.setString(1, ttb.getCodigo());
				pst.setString(2, ttb.getNombre());
				pst.executeUpdate();
			}
		//	loadDictionary = "Cargado diccionario_tipostramite";
				
		} catch (SQLException e) {
			logger.error("Se ha producido un error en la carga del diccionario: TiposTramite: codigo: " +ttb.getCodigo()+ " *** nombre: "+ttb.getNombre());
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		try{
			// Insercion de los instrumentos
			Iterator<InstrumentoBean> i_ib = diccionario.getInstrumentos()
					.iterator();
			while (i_ib.hasNext()) {

				ib = (InstrumentoBean) i_ib.next();

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.diccionario_instrumentos (id, codigo, nombre) "
								+ "VALUES (nextval('gestorfip.seq_diccionario_instrumentos'),?,?)");
				pst.setString(1, ib.getCodigo());
				pst.setString(2, ib.getNombre());
				pst.executeUpdate();
			}
			//loadDictionary = "Cargado diccionario_instrumentos";

		} catch (SQLException e) {
			logger.error("Se ha producido un error en la carga del diccionario: Instrumentos: codigo: " +ib.getCodigo()+ " *** nombre: "+ib.getNombre());
			logger.error(e.getMessage());
		///	System.out.println("Se ha producido un error en la carga del diccionario: " +loadDictionary);
			e.printStackTrace();
			throw e;
		}

	}

	private Hashtable<String, Integer> obtenerIDLayers(ArrayList<String> lstLayersPlaneamiento) throws SQLException{
		
		Hashtable<String, Integer> hashIdLayers = new Hashtable<String, Integer>();
		String nameLayer = null;
		try{
			for (int i = 0; i < lstLayersPlaneamiento.size(); i++) {
				nameLayer = lstLayersPlaneamiento.get(i);
				pst = this.conn
						.prepareStatement("SELECT id_layer FROM layers WHERE name = ?");
				pst.setString(1, nameLayer);
				rs = pst.executeQuery();
				
				while (rs.next()){
					hashIdLayers.put(lstLayersPlaneamiento.get(i), rs.getInt(1));
				}

			}
			return hashIdLayers;

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " - obtenerIDLayers -- name: "+nameLayer );
			throw e;
		}

	}
	
	
	
	private void searchUsoRegulacionMigracion(EntidadBean entidad, CondicionUrbanisticaBean cu, 
			ConfLayerBean confLayerBean, HashMap<String, String> hahsUsosRegula) throws Exception{
			
		try{
			if(confLayerBean.getLstConfUsosRegulaciones() != null){
			
				for(int i=0; i<confLayerBean.getLstConfUsosRegulaciones().length;i++){
			
					ConfLayerUsosRegulacionesBean confLayerUsosRegu = confLayerBean.getLstConfUsosRegulaciones()[i];
					DeterminacionLayerBean detUso = confLayerUsosRegu.getDeterminacionUso();
					if(confLayerUsosRegu.isSelected() && detUso.getCodigo().equals(cu.getCodigodeterminacion_determinacion())){
						
						for(int j=0; j<cu.getCasos().size(); j++){
							CasoBean caso = cu.getCasos().get(j);
							
							for(int h=0; h<caso.getRegimenes().size(); h++){
								Caso_RegimenBean casoRegimen = caso.getRegimenes().get(h);
								
								if(casoRegimen.getValorreferencia_codigoTramite() != null &&  !casoRegimen.getValorreferencia_codigoTramite().equals("") &&
										 casoRegimen.getValorreferencia_determinacion() != null &&  !casoRegimen.getValorreferencia_determinacion().equals("") ){
							
									for(int q=0; q<confLayerUsosRegu.getLstRegulaciones().length; q++){

										ConfRegulacionesBean confRegu = confLayerUsosRegu.getLstRegulaciones()[q];
										if(confRegu.isSelected() && 
												confRegu.getRegulacionValor().getCodigo().equals(casoRegimen.getValorreferencia_determinacion())){
											
											String nombreRegulacion = confRegu.getAlias();
											
											if(hahsUsosRegula.containsKey(nombreRegulacion)){
												StringBuffer value = new StringBuffer();

												String[] valueSplit = hahsUsosRegula.get(nombreRegulacion).split(";");
												ArrayList<String> lstNames = new ArrayList<String>();
												for (int t=0; t<valueSplit.length;t++){
													lstNames.add(valueSplit[t]);
												}
												
												if(!lstNames.contains(confLayerUsosRegu.getAlias())){
													value.append(hahsUsosRegula.get(nombreRegulacion)).append(";").append(confLayerUsosRegu.getAlias());
													hahsUsosRegula.put(nombreRegulacion, value.toString());
												}
												
											}
											else{
												hahsUsosRegula.put(nombreRegulacion, confLayerUsosRegu.getAlias());
											}
											
										}	
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage() + "searchUsoRegulacionStyles" );
			throw e;
		}
	
	}
	
	
	
	/**Inserta las geometrias en la tabla correspondiente en funcion del grupo de aplicacion de la entidad.
	 * 
	 * @param tramiteCS
	 * @param tramitePV
	 * @param idMunicipioLG
	 * @param lstLayersStyles
	 * @throws Exception
	 */
	private void loadGeometriesEntidades(int idMunicipioLG, ConfLayerBean[] lstConfLayerBean) throws Exception{
		EntidadBean entidad = null;
		
		LayerStylesBean[] lstLayersStyles = null;

		try{
			ArrayList<String> lstLayersPlaneamiento = new ArrayList<String>();
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_ACCIONES + "_" + idMunicipioLG);
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_AFECCIONES + "_" + idMunicipioLG);
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_ALINEACION + "_" + idMunicipioLG);
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_AMBITO + "_" + idMunicipioLG);
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_CATEGORIA + "_" + idMunicipioLG);
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_CLASIFICACION + "_" + idMunicipioLG);
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_DESARROLLO + "_" + idMunicipioLG);
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_EQUIDISTRIBUCION + "_" + idMunicipioLG);
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_GESTION + "_" + idMunicipioLG);
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_PROTECCION + "_" + idMunicipioLG);
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_SISTEMAS + "_" + idMunicipioLG);
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_ZONA + "_" + idMunicipioLG);
			lstLayersPlaneamiento.add(Constants.LAYER_TITLE_PLANEAMIENTO_OTRAS_INDICACIONES + "_" + idMunicipioLG);
			
			HashMap<String, ConfLayerBean> hashLayers = new HashMap<String, ConfLayerBean>();
			for(int i=0; i<lstConfLayerBean.length; i++){
				hashLayers.put(lstConfLayerBean[i].getNameLayer(), lstConfLayerBean[i]);
			}
			
			Hashtable<String, Integer> hashIdLayers = obtenerIDLayers(lstLayersPlaneamiento);
			
			for (Iterator iterator = this.planeamientovigente.getTramite().getEntidades().iterator(); iterator.hasNext();) {
				entidad = (EntidadBean) iterator.next();
			
				if(entidad.getGeometria() != null){
				
					// entidades con geometria
					// se busca el grupo de aplicacion al que pertenece para hacer su insercion en la tabla correspondiente.
					String grupoAplicacion = searchCodigoGrupoApliacionEntidad(entidad.getCodigo(), this.planeamientovigente.getTramite().getCondicionesurbanisticas() );
					String txtGrupoAplicacion = searchNombreGrupoApliacionEntidad(grupoAplicacion);
					
					if(grupoAplicacion != null){
						
						if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACCION) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACCIONES)){
							//Acciones
							ConfLayerBean confLayerBean = hashLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_ACCIONES);
							HashMap<String, HashMap> hashValores =  busquedaDeterminacionesCUAsociadasEntidad(entidad, confLayerBean);
							insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG, confLayerBean,
									Constants.TABLE_NAME_ACCIONES, Constants.SEQ_ACCIONES ,Constants.LAYER_TITLE_PLANEAMIENTO_ACCIONES, 
									txtGrupoAplicacion, hashIdLayers, hashValores);

						}
						else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCION_POLIGONAL) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCION_LINEAL)||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCION_PUNTUAL)||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_AFECCIONES_ESTUDIO_DETALLE)){
							//Afecciones
							ConfLayerBean confLayerBean = hashLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_AFECCIONES);
							HashMap<String, HashMap> hashValores =  busquedaDeterminacionesCUAsociadasEntidad(entidad, confLayerBean);
							insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG, confLayerBean,
									Constants.TABLE_NAME_AFECCIONES, Constants.SEQ_AFECCIONES ,Constants.LAYER_TITLE_PLANEAMIENTO_AFECCIONES, 
									txtGrupoAplicacion, hashIdLayers, hashValores);
						}		
						
						else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_ALINEACION) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_RASANTE) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_COTA) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_PAUTA_ORDENACION) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_FONDOS_LINEAL)){
							//Alineaciones
							ConfLayerBean confLayerBean = hashLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_ALINEACION);
							HashMap<String, HashMap> hashValores =  busquedaDeterminacionesCUAsociadasEntidad(entidad, confLayerBean);
							insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG, confLayerBean, 
									Constants.TABLE_NAME_ALINEACION, Constants.SEQ_ALINEACION ,Constants.LAYER_TITLE_PLANEAMIENTO_ALINEACION, 
									txtGrupoAplicacion, hashIdLayers, hashValores);
				
						}
						else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO)){
							//Ambito	
							ConfLayerBean confLayerBean = hashLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_AMBITO);
							HashMap<String, HashMap> hashValores =  busquedaDeterminacionesCUAsociadasEntidad(entidad, confLayerBean);
							insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG, confLayerBean, 
									Constants.TABLE_NAME_AMBITO, Constants.SEQ_AMBITO ,Constants.LAYER_TITLE_PLANEAMIENTO_AMBITO, 
									txtGrupoAplicacion, hashIdLayers, hashValores);
						}
						else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_URBANO) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_URBANIZABLE) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_NO_URBANIZABLE) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_URBANO) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_URBANIZABLE) ||					
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_RUSTICO) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_PUNTUAL) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_LINEAL) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_PROTEGIDO) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_SUBCAT_SUELO_NO_URBANIZABLE_NO_PROTEGIDO) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_RUSTICO_LINEAL) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_CAT_SUELO_RUSTICO_PUNTUAL)){
							//Categoria
							 ConfLayerBean confLayerBean = hashLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_CATEGORIA);
							 HashMap<String, HashMap> hashValores =  busquedaDeterminacionesCUAsociadasEntidad(entidad, confLayerBean);
							 insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG, confLayerBean, 
									Constants.TABLE_NAME_CATEGORIA, Constants.SEQ_CATEGORIA ,Constants.LAYER_TITLE_PLANEAMIENTO_CATEGORIA, 
									txtGrupoAplicacion, hashIdLayers, hashValores);
						}
						else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_CLASE_SUELO)){
							//Clasificacion	
							ConfLayerBean confLayerBean = hashLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_CLASIFICACION);
							HashMap<String, HashMap> hashValores =  busquedaDeterminacionesCUAsociadasEntidad(entidad, confLayerBean);
							insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG, confLayerBean, 
									Constants.TABLE_NAME_CLASIFICACION, Constants.SEQ_CLASIFICACION ,Constants.LAYER_TITLE_PLANEAMIENTO_CLASIFICACION, 
									txtGrupoAplicacion, hashIdLayers, hashValores);
						}
						else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_SECTOR) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_SECTORIZACION)){
							//Desarrollo	
							ConfLayerBean confLayerBean = hashLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_DESARROLLO);
							HashMap<String, HashMap> hashValores =  busquedaDeterminacionesCUAsociadasEntidad(entidad, confLayerBean);
							insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG, confLayerBean, 
									Constants.TABLE_NAME_DESARROLLO, Constants.SEQ_DESARROLLO ,Constants.LAYER_TITLE_PLANEAMIENTO_DESARROLLO, 
									txtGrupoAplicacion, hashIdLayers, hashValores);
						}
						else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_URBANA) || 
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_REPARTO)){
							//Equidistribucion	
							ConfLayerBean confLayerBean = hashLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_EQUIDISTRIBUCION);
							HashMap<String, HashMap> hashValores =  busquedaDeterminacionesCUAsociadasEntidad(entidad, confLayerBean);
							insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG, confLayerBean, 
									Constants.TABLE_NAME_EQUIDISTRIBUCION, Constants.SEQ_EQUIDISTRIBUCION ,Constants.LAYER_TITLE_PLANEAMIENTO_EQUIDISTRIBUCION, 
									txtGrupoAplicacion, hashIdLayers, hashValores);
						}
						else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_HOMOGENEA) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_ACTUACION) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_EJECUCION) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_RESERVA) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ESPACIOS_GESTION_INTEGRADA) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO_ACTUACION)||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO_DESARROLLO) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_PLANEAMIENTO_ASUMIDO) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_AMBITO_PLANEAMIENTO_DESARROLLO) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDAD_NORMALIZACION) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_OTROS_AMBITOS) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACTUACION_AISLADA) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_PATRIMONIO_MUNICIPAL_SUELO) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_UNIDADES_NORMALIZACION_FINCAS)  ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ACTUACIONES_URBANAS_CONCERTADAS) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROYECTO_ACTUACION_PRIORITARIA) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_INSTRUMENTO_GESTION) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_USO) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_INTERVENCION)){
							//Gestion	
							ConfLayerBean confLayerBean = hashLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_GESTION);
							HashMap<String, HashMap> hashValores =  busquedaDeterminacionesCUAsociadasEntidad(entidad, confLayerBean);
							insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG, confLayerBean, 
									Constants.TABLE_NAME_GESTION, Constants.SEQ_GESTION ,Constants.LAYER_TITLE_PLANEAMIENTO_GESTION, 
									txtGrupoAplicacion, hashIdLayers, hashValores);
						}
						else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_SISTEMA_POLIGONAL) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_SISTEMA_LINEAL) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_SISTEMAS_ESTUDIO_DETALLE)){
							//Sistemas	
							ConfLayerBean confLayerBean = hashLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_SISTEMAS);
							HashMap<String, HashMap> hashValores =  busquedaDeterminacionesCUAsociadasEntidad(entidad, confLayerBean);
							insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG, confLayerBean, 
									Constants.TABLE_NAME_SISTEMAS, Constants.SEQ_SISTEMAS ,Constants.LAYER_TITLE_PLANEAMIENTO_SISTEMAS, 
									txtGrupoAplicacion, hashIdLayers, hashValores );
						}
						else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROTECCION_POLIGONAL) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROTECCION_LINEAL) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_PROTECCION_PUNTUAL)){
							//Proteccion	
							ConfLayerBean confLayerBean = hashLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_PROTECCION);
							HashMap<String, HashMap> hashValores =  busquedaDeterminacionesCUAsociadasEntidad(entidad, confLayerBean);
							insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG, confLayerBean, 
									Constants.TABLE_NAME_PROTECCION, Constants.SEQ_PROTECCION ,Constants.LAYER_TITLE_PLANEAMIENTO_PROTECCION, 
									txtGrupoAplicacion, hashIdLayers, hashValores);
						}
						else if(grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_AREA_USO_GLOBAL) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_ORDENACION_URBANISTICA) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_SUELO_NO_URBANIZABLE) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_SUELO_URBANIZABLE) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_SUELO_URBANO)||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_ALTURAS) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONAS_PLTO_INCORPORADO) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONAS_PLAN_PARCIAL) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONAS_ESTUDIO_DETALLE) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_PARCELA) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ALTURAS) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_FONFO) || 
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_EDIFICABILIDADES) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_INTERVENCION_PUNTUAL) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_ALTURA) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_ZONA_EDIFICABILIDAD) ||
								grupoAplicacion.equals(Constants.GRUPO_APLICACION_PARCELA_SUSPENDIDA)){
							//Zona	
							ConfLayerBean confLayerBean = hashLayers.get(Constants.LAYER_TITLE_PLANEAMIENTO_ZONA);
							HashMap<String, HashMap> hashValores =  busquedaDeterminacionesCUAsociadasEntidad(entidad, confLayerBean);
							insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG, confLayerBean, 
									Constants.TABLE_NAME_ZONA, Constants.SEQ_ZONA ,Constants.LAYER_TITLE_PLANEAMIENTO_ZONA, 
									txtGrupoAplicacion, hashIdLayers, hashValores);
						}
						else{
							HashMap<String, HashMap> hashValores= new  HashMap<String, HashMap>();
							insertarGeometriaTablePlaneamientoConfig(entidad, idMunicipioLG,  null, 
									Constants.TABLE_NAME_OTRAS_INDICACIONES, Constants.SEQ_OTRAS_INDICACIONES ,Constants.LAYER_TITLE_PLANEAMIENTO_OTRAS_INDICACIONES, 
									txtGrupoAplicacion, hashIdLayers, hashValores);
						}
					}
				}
			}	

		}
		catch(NullPointerException e){
			if (entidad!= null){
				logger.error("Error insercion geometrias: entidad [codigo="+ entidad.getCodigo()+ "] [nombre="+entidad.getNombre()+"]");
			}
			else{
				logger.error("Error No se ha podido insertar la geometria");
			}
			logger.error(e);
		}
		catch(Exception e){
			if (entidad!= null){
				logger.error("Error insercion geometrias: entidad [codigo="+ entidad.getCodigo()+ "] [nombre="+entidad.getNombre()+"]");
			}
			else{
				logger.error("Error loadGeometriesEntidades");
			}
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e);
			throw e;
		}
		
		try{
			

			if(lstConfLayerBean != null && lstConfLayerBean[0] != null){
				for(int i=0; i<lstConfLayerBean.length; i++){
					// Se configuran los estilos para cada layer creada
					ConfLayerBean confLayer = lstConfLayerBean[i];
					GestionLayersBBDD.generateStylesConfig(confLayer, idMunicipioLG, this.conn);
				}
			}
		}
		catch(Exception e){
			if (entidad!= null){
				logger.error("Error insercion de los estilos SLD");
			}
			else{
				logger.error("Error loadGeometriesEntidades");
			}
			e.printStackTrace();
			logger.error(e.getMessage());
			throw e;
		}
	}
	

	private void obtenerValoresDeterminacionAplicadaMigracion(TramiteBean tramite, CondicionUrbanisticaBean cu, ConfLayerBean confLayerBean, 
			HashMap<String, ArrayList<String>> hahsDetAplic){
		
		for (Iterator<DeterminacionBean> itDet = tramite.getDeterminaciones().iterator(); itDet
				.hasNext();) {
			DeterminacionBean detPV = itDet.next();
			if(cu.getCodigodeterminacion_determinacion().equals(detPV.getCodigo())){
				// se ha encontrado la determinacion de la condicion urbanistica en el planeamiento vigente
				for(int i=0; i<confLayerBean.getLstConfLayerDeterminacionAplicada().length; i++){
					
					ConfLayerDeterminacionAplicadaBean confDetApli = confLayerBean.getLstConfLayerDeterminacionAplicada()[i];
					if(confDetApli.isSelected()){
						if(confDetApli.getDeterminacionLayer().getCodigo().equals(detPV.getCodigo())){
							// la determinacion de la condicion urbanistica se ha encontrado entre las aplicadas en la configuracion
							// de las capas

							if(hahsDetAplic.get(confDetApli.getAliasDeterminacion()) == null){
								hahsDetAplic.put(confDetApli.getAliasDeterminacion(),new ArrayList<String>());
							}
							
							for (Iterator<CasoBean> itCasos = cu.getCasos().iterator(); itCasos.hasNext();) {
								CasoBean caso =  itCasos.next();
								// iteracion de los casos
								for (Iterator<Caso_RegimenBean> itRegimenes= caso.getRegimenes().iterator(); itRegimenes.hasNext();){
									Caso_RegimenBean casoReg = itRegimenes.next();
									//iteracion de los regimenes 
									
									if(confDetApli.getLstValores() != null){
										for(int j=0; j<confDetApli.getLstValores().length; j++){
											ConfLayerValorBean confLayerValor = confDetApli.getLstValores()[j];
											if(confLayerValor.isSelected()){
												// el valor ha sido seleccionado en la configuración de la migracion en la pantalla 2
												if (casoReg != null && casoReg.getValor() != null){
													if(confLayerValor.getValor().equals(casoReg.getValor().trim())){
														if(!hahsDetAplic.get(confDetApli.getAliasDeterminacion()).contains(confLayerValor.getAlias())){
															hahsDetAplic.get(confDetApli.getAliasDeterminacion()).add(confLayerValor.getAlias());
														}
													}
												}
											}
										}
									}
									
									if(confDetApli.getLstValoresReferencia() != null){
										for(int j=0; j<confDetApli.getLstValoresReferencia().length; j++){
											ConfLayerValorReferenciaBean confValorRef = confDetApli.getLstValoresReferencia()[j];
											if(casoReg != null && confValorRef != null && confValorRef.getDeterminacionLayer().getCodigo() != null){
												if(confValorRef.isSelected()){
													if(confValorRef.getDeterminacionLayer().getCodigo().equals(casoReg.getValorreferencia_determinacion())){
														if(!hahsDetAplic.get(confDetApli.getAliasDeterminacion()).contains(confValorRef.getAlias())){
															hahsDetAplic.get(confDetApli.getAliasDeterminacion()).add(confValorRef.getAlias());
														}
													}
												}
											}
										}
									}
									
									if(confDetApli.getLstRegimenesEspecificos() != null){
										for(int j=0; j<confDetApli.getLstRegimenesEspecificos().length; j++){
											ConfLayerRegimenEspecificoBean confRegimen = confDetApli.getLstRegimenesEspecificos()[j];
											if(confRegimen.isSelected()){
												
												for (Iterator<Caso_Regimen_RegimenEspecificoBean> itRegEspe = casoReg.getRegimenesespecificos().iterator(); itRegEspe.hasNext();) {
													Caso_Regimen_RegimenEspecificoBean regEspecifico = itRegEspe.next();
													if(regEspecifico != null && confRegimen.getOrden() != null){
														if(confRegimen.getOrden().equals(String.valueOf(regEspecifico.getOrden())) && 
																confRegimen.getNombre().trim().equals(regEspecifico.getNombre().trim()) && 
																confRegimen.getTexto().trim().equals(regEspecifico.getTexto().trim())){
															if(!hahsDetAplic.get(confDetApli.getAliasDeterminacion()).contains(confRegimen.getAlias())){
																hahsDetAplic.get(confDetApli.getAliasDeterminacion()).add(confRegimen.getAlias());
															}
														}
													}
												}
											}
										}			
									}
								}
							}
						}
					}
				}
			}
		}
	}


	/**
	 * @param entidad
	 * @param confLayerBean
	 * @return
	 * @throws Exception 
	 */
	private HashMap<String, HashMap> busquedaDeterminacionesCUAsociadasEntidad(EntidadBean entidad, ConfLayerBean confLayerBean) throws Exception{
			
		// se guardan los datos en un hash, la clave es el alias de la determinacion aplicada para saber el nombre de la 
		// columna donde se insertarán los datos.
		// el value es un arraylist con los valores a introducir
		HashMap<String, ArrayList<String>> hahsDetAplic = new HashMap<String, ArrayList<String>>();
		
		
		HashMap<String, HashMap> hashValores = new HashMap <String, HashMap>();
		if(confLayerBean.isAplicada()){
			//Se buscan los valores si la layer esta aplicada, es decir si en la configuracion se ha seleccionado una determinacion aplicada y un valor
			// o se ha seleccionado un uso y una regulacion para la layer
			HashMap<String, String>  hahsUsosRegula = new HashMap<String, String>();
	
	
			for (Iterator<CondicionUrbanisticaBean> itCu = this.planeamientovigente.getTramite().getCondicionesurbanisticas().iterator(); itCu.hasNext();) {
				CondicionUrbanisticaBean cu = itCu.next();
				
				if(entidad.getCodigo().equals(cu.getCodigoentidad_entidad()) && !cu.getCodigodeterminacion_determinacion().equals(Constants.GRUPO_ENTIDADES)){
					// se ha encontrado la entidad de trabajo en una condicion urbanistica

					obtenerValoresDeterminacionAplicadaMigracion(this.planeamientovigente.getTramite(), cu, confLayerBean, hahsDetAplic);
					obtenerValoresDeterminacionAplicadaMigracion(this.catalogosistematizado.getTramite(), cu, confLayerBean, hahsDetAplic);
					
					searchUsoRegulacionMigracion(entidad, cu ,confLayerBean, hahsUsosRegula);
					
				}
			}
			
			hashValores.put(Constants.KEY_LST_VALORES_DETERMINACIONES_APLICADA, hahsDetAplic);
			hashValores.put(Constants.KEY_LST_VALORES_USOS_REGULACION, hahsUsosRegula);
		}
		return hashValores;
	}
		
	/** Inserta la geometria en la tabla de planeamiento y añade los posibles valores en funcion de la configuracion
	 * en la migracion
	 * @param entidad
	 * @param idMunicipioLG
	 * @param confLayerBean
	 * @param nameTable
	 * @param nameSeq
	 * @param layerTitle
	 * @param txtGrupoAplicacion
	 * @param hashIdLayers
	 * @param hashValores
	 * @throws Exception
	 */
	private void insertarGeometriaTablePlaneamientoConfig(EntidadBean entidad, int idMunicipioLG, ConfLayerBean confLayerBean ,
			String nameTable, String nameSeq, String layerTitle, String txtGrupoAplicacion, 
			Hashtable<String, Integer> hashIdLayers, HashMap<String, HashMap> hashValores) throws Exception{
		logger.info(nameTable);
		try{
			HashMap<String, ArrayList<String>> hashValDet = hashValores.get(Constants.KEY_LST_VALORES_DETERMINACIONES_APLICADA);
			HashMap<String, String>  hashValUsosRegu = hashValores.get(Constants.KEY_LST_VALORES_USOS_REGULACION);
			
			//convertimos la geometria al sistema de la BBDD para su insercion.
			Geometry geom = null;
			if(entidad.getGeometria().getSRID() != 25830){
				geom = Ed50toetrs89Utils.convertGeometryED50ToETRS89(entidad.getGeometria());
			}
			else{
				geom = entidad.getGeometria();
			}
		
			try{
		
				String nameTableGen = nameTable + Constants.IDENTIFICADOR_TABLE_GENERICO;
				String nameTableMun = nameTable + "_" + idMunicipioLG;;
				
				StringBuffer queryInsertPlaneamientoGenerico = new StringBuffer();
				queryInsertPlaneamientoGenerico.append("INSERT INTO public."+nameTableGen)
					.append(" (").append(Constants.ATT_ID.toLowerCase())
					.append(",").append("\"GEOMETRY\"")
					.append(",").append(Constants.ATT_ID_MUNICIPIO.toLowerCase())
					.append(",").append(Constants.ATT_CLAVE.toLowerCase())
					.append(",").append(Constants.ATT_CODIGO.toLowerCase())
					.append(",").append(Constants.ATT_ETIQUETA.toLowerCase())
					.append(",").append(Constants.ATT_NOMBRE.toLowerCase())
					.append(",").append(Constants.ATT_GRUPOAPLICACION.toLowerCase()).append(") ")
					.append("VALUES (nextval('public."+nameSeq+"'),ST_GeomFromText(?,?),?,?,?,?,?,?)");
					
		
				int indexInsert = 1;
				pst = this.conn.prepareStatement(queryInsertPlaneamientoGenerico.toString());
				pst.setString(indexInsert++, geom.toText());
				pst.setInt(indexInsert++, geom.getSRID());
				pst.setInt(indexInsert++, new Integer(idMunicipioLG));
				pst.setString(indexInsert++, entidad.getClave());
				pst.setString(indexInsert++, entidad.getCodigo());
				pst.setString(indexInsert++, entidad.getEtiqueta());
				pst.setString(indexInsert++, entidad.getNombre());
				pst.setString(indexInsert++, txtGrupoAplicacion);
				pst.executeUpdate();		
	
				if(confLayerBean != null && confLayerBean.isAplicada()){
					//if(hashValores != null && !hashValores.isEmpty()){
						
						//tabla auxiliar con los datos configurados en la migracion
						StringBuffer queryInsertPlaneamientoEspecifico = new StringBuffer();
						queryInsertPlaneamientoEspecifico.append("INSERT INTO public."+nameTableMun)
							.append(" (").append(Constants.ATT_ID.toLowerCase());
						
						ArrayList<String> lstAliasDetApli = new ArrayList<String>();
						ArrayList<String> lstAliasUsosApli = new ArrayList<String>();
						for (Iterator<String> itHash = hashValDet.keySet().iterator(); itHash.hasNext();) {
							String aliasDetApli =  itHash.next();
							queryInsertPlaneamientoEspecifico.append(",").append(aliasDetApli.toLowerCase());
							lstAliasDetApli.add(aliasDetApli);
						}
	
						for (Iterator<String> itHash = hashValUsosRegu.keySet().iterator(); itHash.hasNext();) {
							String aliasUso =  itHash.next();
							queryInsertPlaneamientoEspecifico.append(",").append(aliasUso.toLowerCase());
							lstAliasUsosApli.add(aliasUso);
						}
	
						queryInsertPlaneamientoEspecifico.append(") VALUES (currval('public."+nameSeq+"')");
						for(int i=0; i<lstAliasDetApli.size(); i++){
							queryInsertPlaneamientoEspecifico.append(",?");
						}
						for(int i=0; i<lstAliasUsosApli.size(); i++){
							queryInsertPlaneamientoEspecifico.append(",?");
						}
						queryInsertPlaneamientoEspecifico.append(")");
							
						
						indexInsert = 1;
						pst = this.conn.prepareStatement(queryInsertPlaneamientoEspecifico.toString());
			
						for (Iterator<String> itAliasDetApli= lstAliasDetApli.iterator(); itAliasDetApli.hasNext();) {
							ArrayList<String> lstValores = hashValDet.get(itAliasDetApli.next());
							StringBuffer sbVal = new StringBuffer();
							for (Iterator<String> itVal = lstValores.iterator(); itVal.hasNext();) {					
								sbVal.append(itVal.next());
								if(itVal.hasNext()){
									sbVal.append(";");
								}
							}
							pst.setString(indexInsert++, sbVal.toString().trim());
							
						}
						
						
						for (Iterator<String> itAliasDetApli= lstAliasUsosApli.iterator(); itAliasDetApli.hasNext();) {
							String valores = hashValUsosRegu.get(itAliasDetApli.next());
							StringBuffer sbVal = new StringBuffer(valores);
							pst.setString(indexInsert++, sbVal.toString().trim() );
							
						}
						pst.executeUpdate();
					//}
				}
				
				// se actualiza la entidad con los datos de la feature y layer, para asociar la entidad a la geometria
				Integer idLayer = hashIdLayers.get(layerTitle + "_" + idMunicipioLG);
		
				pst = this.conn
						.prepareStatement("UPDATE gestorfip.tramite_entidades SET idlayer= ? , idfeature = currval('public."+nameSeq+"')"+
										" WHERE id = ?");
				pst.setInt(1, idLayer);
				pst.setInt(2, entidad.getSeq_id());
				pst.executeUpdate();
									
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(e);
				logger.error(e.getMessage() + " " +nameTable );
				throw e;
			}
		}catch (NullPointerException e) {
			e.printStackTrace();
			logger.error(e);
			logger.error(e.getMessage() + " " +nameTable );
			//throw e;
		}
	}
	
	
	/** Devuelve el nombre de la determinacion a partir del codigo de la misma
	 * @param codigo
	 * @return
	 */
	private String searchNombreGrupoApliacionEntidad(String codigo ){

		if(codigo != null){
			for (Iterator<DeterminacionBean> itDetPV = planeamientovigente.getTramite().getDeterminaciones().iterator(); itDetPV.hasNext();) {
				DeterminacionBean det = itDetPV.next();
				if(codigo.equals(det.getCodigo())){
					return det.getNombre();
				}
				
			}
			for (Iterator<DeterminacionBean> itDetPV = catalogosistematizado.getTramite().getDeterminaciones().iterator(); itDetPV.hasNext();) {
				DeterminacionBean det = itDetPV.next();
				if(codigo.equals(det.getCodigo())){
					return det.getNombre();
				}
				
			}
		}
		else{
			return "";
		}
		
		return null;
	}
	
	
	/** Devuelve el grupo de aplicacion apalicado a una entidad
	 * @param codigoEntidad
	 * @param condicionesurbanisticas
	 * @return
	 */
	private String searchCodigoGrupoApliacionEntidad(String codigoEntidad, List<CondicionUrbanisticaBean> condicionesurbanisticas ){
		
		String grupoAplicacion = null;
		for(int i=0; i<condicionesurbanisticas.size(); i++){
			
			CondicionUrbanisticaBean condicionU =	(CondicionUrbanisticaBean)condicionesurbanisticas.get(i);
			
			if(condicionU.getCodigoentidad_entidad().equals(codigoEntidad) &&  
					condicionU.getCodigodeterminacion_determinacion().equals(Constants.GRUPO_ENTIDADES)){
				// buscamos en todas las condiciones urbanisticas en las que interviene la entidad, 
				//y seleccionamos la condicion urbanistica que tiene asociada una determinacion con 
				//codigo = Constants.GRUPO_ENTIDADES = 7777777777
				CasoBean caso = (CasoBean)condicionU.getCasos().get(0);
				
				for(int j=0; j<caso.getRegimenes().size(); j++){
					//seleccionamos del caso de la condicion urbanistica el valor de referencia aplicado
					Caso_RegimenBean casoRegimen = (Caso_RegimenBean)caso.getRegimenes().get(j);
					grupoAplicacion = casoRegimen.getValorreferencia_determinacion();
					break;
				}
			}
		}
		
		return grupoAplicacion;
	}


	/** Obtiene los valores de la condición urbanistica, ya sean valores, valores de referencia o regimenes especificos
	 * @param cu
	 * @return
	 */
	private HashMap<String, Object> obtenerValores(CondicionUrbanisticaBean cu, int idValor, int idValorReg){
		HashMap<String, Object> hashDatos = new HashMap<String, Object>();

		ConfLayerValorBean[] lstConfLayerValores = null;
		ConfLayerValorReferenciaBean[] lstConfLayerValorReferencia = null;
		ConfLayerRegimenEspecificoBean[] lstConfLayerRegimenEspecifico = null;
		
		for (Iterator<CasoBean> itCasosPV = cu.getCasos().iterator(); itCasosPV.hasNext();) {
			
			CasoBean caso = itCasosPV.next();
			if(caso != null){
				for (Iterator<Caso_RegimenBean> itRegimenesPV = caso.getRegimenes().iterator(); itRegimenesPV.hasNext();) {
					Caso_RegimenBean casoRegimen = itRegimenesPV.next();
					if(casoRegimen != null){
					// Valores
						if(casoRegimen.getValor() != null){
							ConfLayerValorBean confLayerValor = new ConfLayerValorBean();
							confLayerValor.setId(idValor++);
							confLayerValor.setAlias(casoRegimen.getValor().trim());
							confLayerValor.setValor(casoRegimen.getValor().trim());
							confLayerValor.setSelected(false);
						
							if(lstConfLayerValores == null){				
								lstConfLayerValores = new ConfLayerValorBean[1];
								lstConfLayerValores[0] = confLayerValor;
							}
							else{
								lstConfLayerValores = (ConfLayerValorBean[]) Arrays.copyOf(lstConfLayerValores, 
										lstConfLayerValores.length+1);
								lstConfLayerValores[lstConfLayerValores.length-1] = confLayerValor;
							}
						}
					
						// Valores de referencia
						if(casoRegimen.getValorreferencia_codigoTramite() != null && !casoRegimen.getValorreferencia_codigoTramite().equals("") &&
								casoRegimen.getValorreferencia_determinacion() != null && !casoRegimen.getValorreferencia_determinacion().equals("")){
							// se busca la determinacion en el trámite
							DeterminacionBean det = null;
							String codTramite = null;
		
							if(catalogosistematizado != null && 
									catalogosistematizado.getTramite().getCodigo().equals(casoRegimen.getValorreferencia_codigoTramite())){
								det = obtenerDeterminacionTramite(catalogosistematizado.getTramite().getDeterminaciones(),
																casoRegimen.getValorreferencia_determinacion());
								codTramite = catalogosistematizado.getTramite().getCodigo();
							}
							else if(planeamientovigente != null && 
									planeamientovigente.getTramite().getCodigo().equals(casoRegimen.getValorreferencia_codigoTramite())){
								det = obtenerDeterminacionTramite(planeamientovigente.getTramite().getDeterminaciones(),
																casoRegimen.getValorreferencia_determinacion());
								codTramite = planeamientovigente.getTramite().getCodigo();
							}
							if(det != null){
								ConfLayerValorReferenciaBean confLayerValorRef = new ConfLayerValorReferenciaBean();
								confLayerValorRef.setSelected(false);
								DeterminacionLayerBean detLayer = new DeterminacionLayerBean();
								detLayer.setCodigo(det.getCodigo());
								detLayer.setNombre(det.getNombre().trim());
								detLayer.setCodTramite(codTramite);
								confLayerValorRef.setDeterminacionLayer(detLayer);
								confLayerValorRef.setAlias(det.getNombre());
								
								if(lstConfLayerValorReferencia == null){				
									lstConfLayerValorReferencia = new ConfLayerValorReferenciaBean[1];
									lstConfLayerValorReferencia[0] = confLayerValorRef;
								}
								else{
									lstConfLayerValorReferencia = (ConfLayerValorReferenciaBean[]) Arrays.copyOf(lstConfLayerValorReferencia, 
											lstConfLayerValorReferencia.length+1);
									lstConfLayerValorReferencia[lstConfLayerValorReferencia.length-1] = confLayerValorRef;
								}
							}
						}	
					
						//Regimenes especificos
						for (Iterator<Caso_Regimen_RegimenEspecificoBean> itRegEsp = casoRegimen.getRegimenesespecificos().iterator(); itRegEsp
								.hasNext();) {
							Caso_Regimen_RegimenEspecificoBean casoRegEspe = (Caso_Regimen_RegimenEspecificoBean) itRegEsp.next();
							if(casoRegEspe!= null){
								ConfLayerRegimenEspecificoBean regimenEspeficico = new ConfLayerRegimenEspecificoBean();
								regimenEspeficico.setNombre(casoRegEspe.getNombre());
								regimenEspeficico.setOrden(String.valueOf(casoRegEspe.getOrden()));

								StringBuffer alias = new StringBuffer();
								if(casoRegEspe.getTexto() != null){
									regimenEspeficico.setTexto(casoRegEspe.getTexto().trim());
									if(casoRegEspe.getNombre() != null){
										alias.append(casoRegEspe.getNombre());
									}
									alias.append(" - ").append(casoRegEspe.getTexto().trim());
								}
								else{
									if(casoRegEspe.getNombre() != null){
										regimenEspeficico.setTexto(casoRegEspe.getNombre());
										alias.append(casoRegEspe.getNombre());
									}
									else{
										regimenEspeficico.setTexto("");
									}
								}
								regimenEspeficico.setAlias(alias.toString());
								regimenEspeficico.setId(idValorReg++);
								regimenEspeficico.setSelected(false);
			
								if(lstConfLayerRegimenEspecifico == null){				
									lstConfLayerRegimenEspecifico = new ConfLayerRegimenEspecificoBean[1];
									lstConfLayerRegimenEspecifico[0] = regimenEspeficico;
								}
								else{
									lstConfLayerRegimenEspecifico = (ConfLayerRegimenEspecificoBean[]) Arrays.copyOf(lstConfLayerRegimenEspecifico, 
											lstConfLayerRegimenEspecifico.length+1);
									lstConfLayerRegimenEspecifico[lstConfLayerRegimenEspecifico.length-1] = regimenEspeficico;
								}
							}
						}
					}
				}
			}
		}
		hashDatos.put(Constants.ID_VALOR, lstConfLayerValores);
		hashDatos.put(Constants.ID_VALOR_REFERENCIA, lstConfLayerValorReferencia);
		hashDatos.put(Constants.ID_REGIMEN_ESPECIFICO, lstConfLayerRegimenEspecifico);
		hashDatos.put("idvalor", idValor);
		hashDatos.put("idvalorreg", idValorReg);
		return hashDatos;
	}
	
	private DeterminacionBean obtenerDeterminacionTramite(List<DeterminacionBean> lstDeterminaciones, String codDet){
		
		DeterminacionBean det = null;
		Iterator<DeterminacionBean> itDet = lstDeterminaciones.iterator();
		while (itDet.hasNext()) {
			DeterminacionBean determinacionBean = (DeterminacionBean) itDet.next();
			if(determinacionBean.getCodigo().equals(codDet)){
				det = determinacionBean;
			}
		}

		return det;
	}
	
	
	
	/**
	 * Inserta el catalogo sistematizado contenido en el FIP en la BD
	 * 
	 * @param tb: Bean representing the tramite to be stored
	 */
	public void loadTramiteIntoDB(TramiteBean tb) throws Exception {
		DocumentoBean docb = null;
		EntidadBean eb = null;
		DeterminacionBean db = null;
		CondicionUrbanisticaBean cub = null;
		OperacionEntidadBean oeb = null;
		OperacionDeterminacionBean odb = null;
		UnidadBean ub = null;
		AplicacionAmbitoBean aab = null;
		AdscripcionBean ab = null;
		try {
			// /////////////////////////////////////////////////////////////////////////////////////////////
			// TRAMITE
			// /////////////////////////////////////////////////////////////////////////////////////////////

			// Recuperacion del id del tipotramite
			int tipotramite_id = 0;
			pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.diccionario_tipostramite WHERE codigo = ?");
			pst.setString(1, tb.getTipotramite());
			rs = pst.executeQuery();
			while (rs.next())
				tipotramite_id = rs.getInt(1);

			pst = this.conn
					.prepareStatement("INSERT INTO gestorfip.tramite (id, tipotramite, codigo, texto, fip) "
							+ "VALUES (nextval('gestorfip.seq_tramite'),?,?,?,?)");
			pst.setInt(1, tipotramite_id);
			pst.setString(2, tb.getCodigo());
			pst.setString(3, tb.getTexto());
			pst.setInt(4, this.fip_id);
			pst.executeUpdate();

			//logger.info("Load Tramite");
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar el Tramite: codigo: "+tb.getCodigo());
			throw e;
		}
		
		try{
			// /////////////////////////////////////////////////////////////////////////////////////////////
			// DOCUMENTOS
			// /////////////////////////////////////////////////////////////////////////////////////////////

			// Recuperacion del idlayer
			int layer_id = 0;
			pst = this.conn
					.prepareStatement("SELECT id_layer FROM public.layers WHERE name = 'planeamiento_vigente'");
			rs = pst.executeQuery();
			while (rs.next())
				layer_id = rs.getInt(1);
			
			Iterator<DocumentoBean> i_db = tb.getDocumentos().iterator();
			while (i_db.hasNext()) {
				 docb = (DocumentoBean) i_db.next();

				// Recuperacion del id del tipo de documento
				if( docb.getTipo()!=null) {
					int tipodocumento_id = 0;
					pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.diccionario_tiposdocumento WHERE codigo = ?");
					pst.setString(1, docb.getTipo());
					rs = pst.executeQuery();
					while (rs.next())
						tipodocumento_id = rs.getInt(1);
						
					pst = this.conn
							.prepareStatement("INSERT INTO gestorfip.tramite_documentos (id, tipo, archivo, nombre, codigo, comentario, tramite) "
									+ "VALUES (nextval('gestorfip.seq_tramite_documentos'),?,?,?,?,?,currval('gestorfip.seq_tramite'))");
					pst.setInt(1, tipodocumento_id);
					pst.setString(2,  docb.getArchivo());
					pst.setString(3,  docb.getNombre());
					pst.setString(4,  docb.getCodigo());
					pst.setString(5,  docb.getComentario());
					pst.executeUpdate();
				}
				else { // DOCUMENTOS REFUNDIDOS
					pst = this.conn
					.prepareStatement("INSERT INTO gestorfip.tramite_documentos (id, archivo, nombre, codigo, comentario, tramite) "
							+ "VALUES (nextval('gestorfip.seq_tramite_documentos'),?,?,?,?,currval('gestorfip.seq_tramite'))");
					pst.setString(1,  docb.getArchivo());
					pst.setString(2,  docb.getNombre());
					pst.setString(3,  docb.getCodigo());
					pst.setString(4,  docb.getComentario());
					pst.executeUpdate();					
				}
				// Recuperacion del id del grupo de documento
				if( docb.getGrupo() != null) {
					int grupodocumento_id = 0;
					pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.diccionario_gruposdocumento WHERE codigo = ?");
					pst.setString(1,  docb.getGrupo());
					rs = pst.executeQuery();
					while (rs.next())
						grupodocumento_id = rs.getInt(1);

					pst = this.conn
							.prepareStatement("UPDATE gestorfip.tramite_documentos SET grupo = ? WHERE id = currval('gestorfip.seq_tramite_documentos')");
					pst.setInt(1, grupodocumento_id);
					pst.executeUpdate();
				}
				
				// Actualizacion de la escala
				if( docb.getEscala() != null) {
					pst = this.conn
					.prepareStatement("UPDATE gestorfip.tramite_documentos SET escala = ? WHERE id = currval('gestorfip.seq_tramite_documentos')");
					pst.setLong(1, Long.parseLong( docb.getEscala()));
					pst.executeUpdate();					
				}

				// Insercion de las hojas de los documentos
				Iterator<HojaBean> i_hb =  docb.getHojas().iterator();
				while (i_hb.hasNext()) {
					HojaBean hb = (HojaBean) i_hb.next();
					pst = this.conn
							.prepareStatement("INSERT INTO gestorfip.tramite_documento_hojas (id, nombre) "
									+ "VALUES (nextval('gestorfip.seq_tramite_documento_hojas'),?)");
					pst.setString(1, hb.getNombre());
					pst.executeUpdate();
				}
			}
			//logger.info("Load Documentos");
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar el Documentos: codigo: "+ docb.getCodigo() + " *** nombre: "+ docb.getNombre());
			throw e;
		}
		
		Iterator<EntidadBean> i_eb = tb.getEntidades().iterator();
		try{
			// /////////////////////////////////////////////////////////////////////////////////////////////
			// ENTIDADES
			// /////////////////////////////////////////////////////////////////////////////////////////////			
			
			//Iterator<EntidadBean> i_eb = tb.getEntidades().iterator();

			while (i_eb.hasNext()) {
				eb = (EntidadBean) i_eb.next();
				
				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.tramite_entidades (id, codigo, etiqueta, clave, nombre, suspendida, tramite) "
								+ "VALUES (nextval('gestorfip.seq_tramite_entidades'),?,?,?,?,?,currval('gestorfip.seq_tramite'))");
			
				pst.setString(1, eb.getCodigo());
				pst.setString(2, eb.getEtiqueta());
				pst.setString(3, eb.getClave());
				pst.setString(4, eb.getNombre());
				pst.setBoolean(5, eb.isSuspendida());

				pst.executeUpdate();

				// Registering the id of the current entidad
				pst = this.conn
						.prepareStatement("SELECT currval('gestorfip.seq_tramite_entidades')");
				rs = pst.executeQuery();
				while (rs.next())
					eb.setSeq_id(rs.getInt(1));

				// Insercion de los documentos de las entidades
				Iterator<com.gestorfip.ws.xml.beans.importacion.tramite.entidad.DocumentoBean> i_edb = eb
						.getDocumentos().iterator();
				while (i_edb.hasNext()) {
					com.gestorfip.ws.xml.beans.importacion.tramite.entidad.DocumentoBean edb = (com.gestorfip.ws.xml.beans.importacion.tramite.entidad.DocumentoBean) i_edb
							.next();

					// Recuperacion del id del documento
					int documentoentidad_id = 0;
					pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.tramite_documentos WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
					pst.setString(1, edb.getDocumento());
					rs = pst.executeQuery();
					while (rs.next())
						documentoentidad_id = rs.getInt(1);

					pst = this.conn
							.prepareStatement("INSERT INTO gestorfip.tramite_entidad_documentos (id, documentoid, entidad) "
									+ "VALUES (nextval('gestorfip.seq_tramite_entidad_documentos'),?,currval('gestorfip.seq_tramite_entidades'))");
					pst.setInt(1, documentoentidad_id);
					pst.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar Entidades: codigo: "+eb.getCodigo() + " *** nombre: "+eb.getNombre());
			throw e;
		}

		try{
			i_eb = tb.getEntidades().iterator();
			// Actualizacion de las llaves foraneas (base_entidadid y madre)
			while (i_eb.hasNext()) {
				eb = (EntidadBean) i_eb.next();

				// base_entidadid
				if (eb.getBase_entidad() != null) {
					int baseentidad_idTramite = 0;
					pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.tramite WHERE codigo = ? AND fip = currval('gestorfip.seq_fip')");
					pst.setString(1, eb.getBase_codigoTramite());
					rs = pst.executeQuery();
					while (rs.next())
						baseentidad_idTramite = rs.getInt(1);

					
					int baseentidad_id = 0;
					pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.tramite_entidades WHERE codigo = ? AND tramite = ?");
					pst.setString(1, eb.getBase_entidad());
					pst.setInt(2, baseentidad_idTramite);
					rs = pst.executeQuery();
					while (rs.next())
						baseentidad_id = rs.getInt(1);
					
					pst = this.conn
							.prepareStatement("UPDATE gestorfip.tramite_entidades SET base_entidadid = ? WHERE id = "
									+ eb.getSeq_id());
					pst.setInt(1, baseentidad_id);
					pst.executeUpdate();
				}

				// madre
				if (eb.getMadre() != null) {
					int entidadmadre_id = 0;
					pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.tramite_entidades WHERE codigo = ? and tramite = currval('gestorfip.seq_tramite')");
					pst.setString(1, eb.getMadre());
					rs = pst.executeQuery();
					while (rs.next())
						entidadmadre_id = rs.getInt(1);

					pst = this.conn
							.prepareStatement("UPDATE gestorfip.tramite_entidades SET madre = ? WHERE id = "
									+ eb.getSeq_id());
					pst.setInt(1, entidadmadre_id);
					pst.executeUpdate();
				}
			}
			
			//logger.info("Load Entidades");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar Entidades: codigo: "+eb.getCodigo() + " *** nombre: "+eb.getNombre());
			throw e;
		}


		try{
			// /////////////////////////////////////////////////////////////////////////////////////////////
			// DETERMINACIONES
			// /////////////////////////////////////////////////////////////////////////////////////////////

			Iterator<DeterminacionBean> i_deb = tb.getDeterminaciones().iterator();
			while (i_deb.hasNext()) {
				 db = (DeterminacionBean) i_deb.next();

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.tramite_determinaciones (id, codigo, apartado, nombre, etiqueta, suspendida, texto, tramite) "
								+ "VALUES (nextval('gestorfip.seq_tramite_determinaciones'),?,?,?,?,?,?,currval('gestorfip.seq_tramite'))");
				
				pst.setString(1, db.getCodigo());
				pst.setString(2, db.getApartado());
				pst.setString(3, db.getNombre());
				pst.setString(4, db.getEtiqueta());
				pst.setBoolean(5, db.isSuspendida());
				pst.setString(6, db.getTexto());
				pst.executeUpdate();

				// Registering the id of the current determinacion
				pst = this.conn
						.prepareStatement("SELECT currval('gestorfip.seq_tramite_determinaciones')");
				rs = pst.executeQuery();
				while (rs.next())
					db.setSeq_id(rs.getInt(1));

				// Insercion de los documentos de las determinaciones
				Iterator<com.gestorfip.ws.xml.beans.importacion.tramite.determinacion.DocumentoBean> i_ddb = db
						.getDocumentos().iterator();
				while (i_ddb.hasNext()) {
					com.gestorfip.ws.xml.beans.importacion.tramite.determinacion.DocumentoBean ddb = (com.gestorfip.ws.xml.beans.importacion.tramite.determinacion.DocumentoBean) i_ddb
							.next();

					// Recuperacion del id del documento
					int documentodeterminacion_id = 0;
					pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.tramite_documentos WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
					pst.setString(1, ddb.getDocumento());
					rs = pst.executeQuery();
					while (rs.next())
						documentodeterminacion_id = rs.getInt(1);

					pst = this.conn
							.prepareStatement("INSERT INTO gestorfip.tramite_determinacion_documentos (id, documentoid, determinacion) "
									+ "VALUES (nextval('gestorfip.seq_tramite_determinacion_documentos'),?,currval('gestorfip.seq_tramite_determinaciones'))");
					pst.setInt(1, documentodeterminacion_id);
					pst.executeUpdate();
				}
				
				// Insercion de las regulaciones especificas
				Iterator<RegulacionEspecificaBean> i_reb = db
						.getRegulacionesespecificas().iterator();
				while (i_reb.hasNext()) {
					RegulacionEspecificaBean reb = (RegulacionEspecificaBean) i_reb
							.next();

					pst = this.conn
							.prepareStatement("INSERT INTO gestorfip.tramite_determinacion_regulacionesespecificas (id, orden, nombre, texto, determinacion) "
									+ "VALUES (nextval('gestorfip.seq_tramite_determinacion_regulacionesespecificas'),?,?,?,currval('gestorfip.seq_tramite_determinaciones'))");
					pst.setInt(1, reb.getOrden());
					pst.setString(2, reb.getNombre());
					pst.setString(3, reb.getTexto());
					pst.executeUpdate();

					// Registering the id of the current regulacionespecifica
					pst = this.conn
							.prepareStatement("SELECT currval('gestorfip.seq_tramite_determinacion_regulacionesespecificas')");
					rs = pst.executeQuery();
					while (rs.next())
						reb.setSeq_id(rs.getInt(1));
				}

			}
			
			// Actualizacion de las tablas con FK y insercion de la informacion
			// que queda
			i_deb = tb.getDeterminaciones().iterator();
			while (i_deb.hasNext()) {
				db = (DeterminacionBean) i_deb.next();

				// Tabla tramite_determinaciones
				// caracterid
				if (db.getCaracter() != null) {
					int caracter_id = 0;
					pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.diccionario_caracteresdeterminacion WHERE codigo = ? ");
					pst.setString(1, db.getCaracter());
					rs = pst.executeQuery();
					while (rs.next())
						caracter_id = rs.getInt(1);

					pst = this.conn
							.prepareStatement("UPDATE gestorfip.tramite_determinaciones SET caracterid = ? WHERE id = "
									+ db.getSeq_id());
					pst.setInt(1, caracter_id);
					pst.executeUpdate();
				}
				
				// unidad_determinacionid
				if (db.getUnidad_determinacion() != null) {
					
					
					int unidaddeterminacion_idTramite = 0;
					pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.tramite WHERE codigo = ? AND fip = currval('gestorfip.seq_fip')");
					pst.setString(1, db.getUnidad_codigoTramite());
					rs = pst.executeQuery();
					while (rs.next())
						unidaddeterminacion_idTramite = rs.getInt(1);

					
					int unidaddeterminacion_id = 0;
					pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = ?");
					pst.setString(1, db.getUnidad_determinacion());
					pst.setInt(2, unidaddeterminacion_idTramite);
					rs = pst.executeQuery();
					while (rs.next())
						unidaddeterminacion_id = rs.getInt(1);
					
					pst = this.conn
							.prepareStatement("UPDATE gestorfip.tramite_determinaciones SET unidad_determinacionid = ? WHERE id = "
									+ db.getSeq_id());
					pst.setInt(1, unidaddeterminacion_id);
					pst.executeUpdate();
				}

				// base_determinacionid
				if (db.getBase_determinacion() != null) {
					
					
					int basedeterminacion_idTramite = 0;
					pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.tramite WHERE codigo = ? AND fip = currval('gestorfip.seq_fip')");
					pst.setString(1, db.getBase_codigoTramite());
					rs = pst.executeQuery();
					while (rs.next())
						basedeterminacion_idTramite = rs.getInt(1);

					
					int basedeterminacion_id = 0;
					pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = ?");
					pst.setString(1, db.getBase_determinacion());
					pst.setInt(2, basedeterminacion_idTramite);
					rs = pst.executeQuery();
					while (rs.next())
						basedeterminacion_id = rs.getInt(1);

					pst = this.conn
							.prepareStatement("UPDATE gestorfip.tramite_determinaciones SET base_determinacionid = ? WHERE id = "
									+ db.getSeq_id());
					pst.setInt(1, basedeterminacion_id);
					pst.executeUpdate();
				}

				// madre
				if (db.getMadre() != null) {
					int determinacionmadre_id = 0;
					pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
					pst.setString(1, db.getMadre());
					rs = pst.executeQuery();
					while (rs.next())
						determinacionmadre_id = rs.getInt(1);

					pst = this.conn
							.prepareStatement("UPDATE gestorfip.tramite_determinaciones SET madre = ? WHERE id = "
									+ db.getSeq_id());
					pst.setInt(1, determinacionmadre_id);
					pst.executeUpdate();
				}

				// Tabla tramite_determinacion_valoresreferencia

				Iterator<ValorReferenciaBean> i_vrb = db.getValoresreferencia()
						.iterator();
				while (i_vrb.hasNext()) {
					ValorReferenciaBean vrb = (ValorReferenciaBean) i_vrb.next();

					// Recuperacion del id de la determinacion
					int determinaciondeterminacion_id = 0;
					pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
					pst.setString(1, vrb.getDeterminacion());
					rs = pst.executeQuery();
					while (rs.next())
						determinaciondeterminacion_id = rs.getInt(1);

					pst = this.conn
							.prepareStatement("INSERT INTO gestorfip.tramite_determinacion_valoresreferencia (id, determinacionid, determinacion) "
									+ "VALUES (nextval('gestorfip.seq_tramite_determinacion_valoresreferencia'),?,"
									+ db.getSeq_id() + " )");
					pst.setInt(1, determinaciondeterminacion_id);
					pst.executeUpdate();

				}

				// Tabla tramite_determinacion_determinacionesreguladoras

				Iterator<DeterminacionReguladoraBean> i_drb = db
						.getDeterminacionesreguladoras().iterator();
				while (i_drb.hasNext()) {
					DeterminacionReguladoraBean drb = (DeterminacionReguladoraBean) i_drb
							.next();

					// Recuperacion del id de la determinacion
					int determinaciondeterminacionreguladora_id = 0;
					pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
					pst.setString(1, drb.getDeterminacion());
					rs = pst.executeQuery();
					while (rs.next())
						determinaciondeterminacionreguladora_id = rs.getInt(1);

					pst = this.conn
							.prepareStatement("INSERT INTO gestorfip.tramite_determinacion_determinacionesreguladoras (id, determinacionid, determinacion) "
									+ "VALUES (nextval('gestorfip.seq_tramite_determinacion_determinacionesreguladoras'),?,"
									+ db.getSeq_id() + " )");
					pst.setInt(1, determinaciondeterminacionreguladora_id);
					pst.executeUpdate();

				}

				// Tabla tramite_determinacion_regulacionesespecificas

				// madre

				Iterator<RegulacionEspecificaBean> i_reb = db
						.getRegulacionesespecificas().iterator();
				while (i_reb.hasNext()) {
					RegulacionEspecificaBean reb = (RegulacionEspecificaBean) i_reb
							.next();

					if (reb.getMadre() != null) {
						// Recuperacion del id de la regulacion especifica madre
						int regulacionespecificamadre_id = 0;
						pst = this.conn
								.prepareStatement("SELECT id FROM gestorfip.tramite_determinacion_regulacionesespecificas WHERE nombre = ?");
						pst.setString(1, reb.getMadre());
						rs = pst.executeQuery();
						while (rs.next())
							regulacionespecificamadre_id = rs.getInt(1);

						pst = this.conn
								.prepareStatement("UPDATE gestorfip.tramite_determinacion_regulacionesespecificas SET madre = ? WHERE id = "
										+ reb.getSeq_id());
						pst.setInt(1, regulacionespecificamadre_id);
						pst.executeUpdate();
					}
				}

				// Tabla tramite_determinacion_gruposaplicacion

				Iterator<GrupoAplicacionBean> i_gab = db
						.getGruposaplicaciones().iterator();
				while (i_gab.hasNext()) {
					GrupoAplicacionBean gab = (GrupoAplicacionBean) i_gab
							.next();

					
					int grupAplicdeterminacion_idTramite = 0;
					pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.tramite WHERE codigo = ? AND fip = currval('gestorfip.seq_fip')");
					pst.setString(1, gab.getCodigoTramite());
					rs = pst.executeQuery();
					while (rs.next())
						grupAplicdeterminacion_idTramite = rs.getInt(1);

					
					int determinaciongrupoaplicacion_id = 0;
					pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = ?");
					pst.setString(1, gab.getDeterminacion());
					pst.setInt(2, grupAplicdeterminacion_idTramite);
					rs = pst.executeQuery();
					while (rs.next())
						determinaciongrupoaplicacion_id = rs.getInt(1);
					
					pst = this.conn
							.prepareStatement("INSERT INTO gestorfip.tramite_determinacion_gruposaplicacion (id, determinacionid, determinacion) "
									+ "VALUES (nextval('gestorfip.seq_tramite_determinacion_gruposaplicacion'),?,"
									+ db.getSeq_id() + " )");
					pst.setInt(1, determinaciongrupoaplicacion_id);
					pst.executeUpdate();

				}
			}

			System.gc();
			//logger.info("Load Determinaciones");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar Determinaciones: codigo: "+db.getCodigo() + " *** nombre: "+db.getNombre());
			throw e;
		}
		
	
		try{
			// /////////////////////////////////////////////////////////////////////////////////////////////
			// CONDICIONES URBANISTICAS
			// /////////////////////////////////////////////////////////////////////////////////////////////

			Iterator<CondicionUrbanisticaBean> i_cub = tb
					.getCondicionesurbanisticas().iterator();
			while (i_cub.hasNext()) {
				 cub = (CondicionUrbanisticaBean) i_cub
						.next();

				int entidad_idTramite = 0;
				pst = this.conn
				.prepareStatement("SELECT id FROM gestorfip.tramite WHERE codigo = ? AND fip = currval('gestorfip.seq_fip')");
				pst.setString(1, cub.getCodigoTramiteentidad_entidad());
				rs = pst.executeQuery();
				while (rs.next())
					entidad_idTramite = rs.getInt(1);

				
				int codigoentidad_entidadid = 0;
				pst = this.conn
				.prepareStatement("SELECT id FROM gestorfip.tramite_entidades WHERE codigo = ? AND tramite = ?");
				pst.setString(1, cub.getCodigoentidad_entidad());
				pst.setInt(2, entidad_idTramite);
				rs = pst.executeQuery();
				while (rs.next())
					codigoentidad_entidadid = rs.getInt(1);
				
				
				int determinacion_idTramite = 0;
				pst = this.conn
				.prepareStatement("SELECT id FROM gestorfip.tramite WHERE codigo = ? AND fip = currval('gestorfip.seq_fip')");
				pst.setString(1, cub.getCodigoTramitedeterminacion_determinacion());
				rs = pst.executeQuery();
				while (rs.next())
					determinacion_idTramite = rs.getInt(1);

				
				int codigodeterminacion_determinacionid = 0;
				pst = this.conn
				.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = ?");
				pst.setString(1, cub.getCodigodeterminacion_determinacion());
				pst.setInt(2, determinacion_idTramite);
				rs = pst.executeQuery();
				while (rs.next())
					codigodeterminacion_determinacionid = rs.getInt(1);
		
			
				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.tramite_condicionesurbanisticas (id, codigoentidad_entidadid, codigodeterminacion_determinacionid, tramite) "
								+ "VALUES (nextval('gestorfip.seq_tramite_condicionesurbanisticas'),?,?,currval('gestorfip.seq_tramite'))");
				pst.setInt(1, codigoentidad_entidadid);
				pst.setInt(2, codigodeterminacion_determinacionid);
				pst.executeUpdate();

				// Insercion de los casos de las condiciones urbanisticas
				Iterator<CasoBean> i_cb = cub.getCasos().iterator();
				while (i_cb.hasNext()) {
					CasoBean cb = (CasoBean) i_cb.next();

					pst = this.conn
							.prepareStatement("INSERT INTO gestorfip.tramite_condicionurbanistica_casos (id, nombre, codigo, condicionurbanistica) "
									+ "VALUES (nextval('gestorfip.seq_tramite_condicionurbanistica_casos'),?,?,currval('gestorfip.seq_tramite_condicionesurbanisticas'))");
					pst.setString(1, cb.getNombre());
					pst.setString(2, cb.getCodigo());
					pst.executeUpdate();

					// Registering the id of the current caso
					pst = this.conn
							.prepareStatement("SELECT currval('gestorfip.seq_tramite_condicionurbanistica_casos')");
					rs = pst.executeQuery();
					while (rs.next())
						cb.setSeq_id(rs.getInt(1));

					// Insercion de los documentos de los casos de las
					// condiciones urbanisticas
					Iterator<Caso_DocumentoBean> i_cdb = cb.getDocumentos()
							.iterator();
					while (i_cdb.hasNext()) {
						Caso_DocumentoBean cdb = (Caso_DocumentoBean) i_cdb
								.next();

						// Recuperacion del id del documento
						int casodocumento_documentoid = 0;
						pst = this.conn
								.prepareStatement("SELECT id FROM gestorfip.tramite_documentos WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
						pst.setString(1, cdb.getDocumento());
						rs = pst.executeQuery();
						while (rs.next())
							casodocumento_documentoid = rs.getInt(1);

						pst = this.conn
								.prepareStatement("INSERT INTO gestorfip.tramite_condicionurbanistica_caso_documentos (id, documentoid, caso) "
										+ "VALUES (nextval('gestorfip.seq_tramite_condicionurbanistica_caso_documentos'),?,currval('gestorfip.seq_tramite_condicionurbanistica_casos'))");
						pst.setInt(1, casodocumento_documentoid);
						pst.executeUpdate();
					}

					// Insercion de los regimenes de los casos de las
					// condiciones urbanisticas
					Iterator<Caso_RegimenBean> i_crb = cb.getRegimenes()
							.iterator();
					while (i_crb.hasNext()) {
						Caso_RegimenBean crb = (Caso_RegimenBean) i_crb.next();
						
						pst = this.conn
								.prepareStatement("INSERT INTO gestorfip.tramite_condicionurbanistica_caso_regimenes (id, comentario, valor, caso) "
										+ "VALUES (nextval('gestorfip.seq_tramite_condicionurbanistica_caso_regimenes'),?,?,currval('gestorfip.seq_tramite_condicionurbanistica_casos'))");
						pst.setString(1, crb.getComentario());
						pst.setString(2, crb.getValor());
						pst.executeUpdate();
						
						// Registering the id of the current regimen
						pst = this.conn
								.prepareStatement("SELECT currval('gestorfip.seq_tramite_condicionurbanistica_caso_regimenes')");
						rs = pst.executeQuery();
						while (rs.next())
							crb.setSeq_id(rs.getInt(1));

						// Insercion de la superposicion si existe
						if (crb.getSuperposicion() != null) {
							pst = this.conn
									.prepareStatement("UPDATE gestorfip.tramite_condicionurbanistica_caso_regimenes SET superposicion = ? WHERE id = currval('gestorfip.seq_tramite_condicionurbanistica_caso_regimenes')");
							pst.setInt(1, Integer.parseInt(crb
									.getSuperposicion()));
							pst.executeUpdate();
						}

						// Actualizamos el id de la determinacion del valor de
						// referencia si existe
						// Recuperacion del id de la determinacion del valor de
						// referencia
						if (crb.getValorreferencia_determinacion() != null) {
							
							
							int valrefdeterminacion_idTramite = 0;
							pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.tramite WHERE codigo = ? AND fip = currval('gestorfip.seq_fip')");
							pst.setString(1, crb.getValorreferencia_codigoTramite());
							rs = pst.executeQuery();
							while (rs.next())
								valrefdeterminacion_idTramite = rs.getInt(1);

							
							int valorreferencia_determinacionid = 0;
							pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = ?");
							pst.setString(1, crb.getValorreferencia_determinacion());
							pst.setInt(2, valrefdeterminacion_idTramite);
							rs = pst.executeQuery();
							while (rs.next())
								valorreferencia_determinacionid = rs.getInt(1);

							if (valorreferencia_determinacionid != 0) {
								pst = this.conn
										.prepareStatement("UPDATE gestorfip.tramite_condicionurbanistica_caso_regimenes SET valorreferencia_determinacionid = ? WHERE id = currval('gestorfip.seq_tramite_condicionurbanistica_caso_regimenes')");
								pst.setInt(1, valorreferencia_determinacionid);
								pst.executeUpdate();
							}
						}

						// Actualizamos el id de la determinacion del regimen si
						// existe
						// Recuperacion del id de la determinacion del regimen

						if (crb.getDeterminacionregimen_determinacion() != null) {
							
							
							int detregdeterminacion_idTramite = 0;
							pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.tramite WHERE codigo = ? AND fip = currval('gestorfip.seq_fip')");
							pst.setString(1, crb.getDeterminacionregimen_codigoTramite());
							rs = pst.executeQuery();
							while (rs.next())
								detregdeterminacion_idTramite = rs.getInt(1);

							
							int determinacionregimen_determinacionid = 0;
							pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = ?");
							pst.setString(1, crb.getDeterminacionregimen_determinacion());
							pst.setInt(2, detregdeterminacion_idTramite);
							rs = pst.executeQuery();
							while (rs.next())
								determinacionregimen_determinacionid = rs.getInt(1);
							
							if (determinacionregimen_determinacionid != 0) {
								pst = this.conn
										.prepareStatement("UPDATE gestorfip.tramite_condicionurbanistica_caso_regimenes SET determinacionregimen_determinacionid = ? WHERE id = currval('gestorfip.seq_tramite_condicionurbanistica_caso_regimenes')");
								pst.setInt(1,
										determinacionregimen_determinacionid);
								pst.executeUpdate();
							}

						}

						// Insercion de los regimenes especificos de los
						// regimenes de los casos de las condiciones
						// urbanisticas
						Iterator<Caso_Regimen_RegimenEspecificoBean> i_reb = crb
								.getRegimenesespecificos().iterator();
						while (i_reb.hasNext()) {
							Caso_Regimen_RegimenEspecificoBean reb = (Caso_Regimen_RegimenEspecificoBean) i_reb
									.next();

							pst = this.conn
									.prepareStatement("INSERT INTO gestorfip.tramite_condicionurbanistica_caso_regimen_regimenesespecificos (id, orden, nombre, texto, regimen) "
											+ "VALUES (nextval('gestorfip.seq_tramite_condicionurbanistica_caso_regimen_regimenesespecificos'),?,?,?,currval('gestorfip.seq_tramite_condicionurbanistica_caso_regimenes'))");
							pst.setInt(1, reb.getOrden());
							pst.setString(2, reb.getNombre());
							pst.setString(3, reb.getTexto());
							pst.executeUpdate();

							// Registering the id of the current regimen
							// especifico
							pst = this.conn
									.prepareStatement("SELECT currval('gestorfip.seq_tramite_condicionurbanistica_caso_regimen_regimenesespecificos')");
							rs = pst.executeQuery();
							while (rs.next())
								reb.setSeq_id(rs.getInt(1));
						}
					}

				}

			}
			
			//logger.info("Load Condiciones Urbanisticas");

			// Actualizacion de las tablas con FK y insercion de la informacion
			// que queda
			// Tabla tramite_condicionurbanistica_caso_regimenes
			// casoaplicacion_casoid
			i_cub = tb.getCondicionesurbanisticas().iterator();
			while (i_cub.hasNext()) {
				cub = (CondicionUrbanisticaBean) i_cub
						.next();

				Iterator<CasoBean> i_cb = cub.getCasos().iterator();
				while (i_cb.hasNext()) {
					CasoBean cb = (CasoBean) i_cb.next();

					Iterator<Caso_RegimenBean> i_crb = cb.getRegimenes()
							.iterator();
					while (i_crb.hasNext()) {
						Caso_RegimenBean crb = (Caso_RegimenBean) i_crb.next();

						if (crb.getCasoaplicacion_caso() != null) {
							int regimen_casoid = 0;
							pst = this.conn
									.prepareStatement("SELECT id FROM gestorfip.tramite_condicionurbanistica_casos WHERE codigo = ?");
							pst.setString(1, crb.getCasoaplicacion_caso());
							rs = pst.executeQuery();
							while (rs.next())
								regimen_casoid = rs.getInt(1);

							pst = this.conn
									.prepareStatement("UPDATE gestorfip.tramite_condicionurbanistica_caso_regimenes SET casoaplicacion_casoid = ? WHERE id = "
											+ crb.getSeq_id());
							pst.setInt(1, regimen_casoid);
							pst.executeUpdate();
						}

						// Tabla
						// tramite_condicionurbanistica_caso_regimen_regimenesespecificos
						// padre
						Iterator<Caso_Regimen_RegimenEspecificoBean> i_crreb = crb
								.getRegimenesespecificos().iterator();
						while (i_crreb.hasNext()) {
							Caso_Regimen_RegimenEspecificoBean crreb = (Caso_Regimen_RegimenEspecificoBean) i_crreb
									.next();

							if (crreb.getPadre() != null) {
								int regimenespecifico_padreid = 0;
								pst = this.conn
										.prepareStatement("SELECT id FROM gestorfip.tramite_condicionurbanistica_caso_regimen_regimenesespecificos WHERE nombre = ?");
								pst.setString(1, crreb.getPadre());
								rs = pst.executeQuery();
								while (rs.next())
									regimenespecifico_padreid = rs.getInt(1);

								pst = this.conn
										.prepareStatement("UPDATE gestorfip.tramite_condicionurbanistica_caso_regimen_regimenesespecificos SET padre = ? WHERE id = "
												+ crb.getSeq_id());
								pst.setInt(1, regimenespecifico_padreid);
								pst.executeUpdate();
							}
						}

					}

					// Insercion de los vinculos de los casos de las condiciones
					// urbanisticas
					Iterator<Caso_VinculoBean> i_cvb = cb.getVinculos()
							.iterator();
					while (i_cvb.hasNext()) {
						Caso_VinculoBean cvb = (Caso_VinculoBean) i_cvb.next();

						// Recuperacion del id del caso
						int vinculo_casoid = 0;
						pst = this.conn
								.prepareStatement("SELECT id FROM gestorfip.tramite_condicionurbanistica_casos WHERE codigo = ?");
						pst.setString(1, cvb.getCaso());
						rs = pst.executeQuery();
						while (rs.next())
							vinculo_casoid = rs.getInt(1);

						pst = this.conn
								.prepareStatement("INSERT INTO gestorfip.tramite_condicionurbanistica_caso_vinculos (id, casoid, caso) "
										+ "VALUES (nextval('gestorfip.seq_tramite_condicionurbanistica_caso_vinculos'),?,"
										+ cb.getSeq_id() + " )");
						pst.setInt(1, vinculo_casoid);
						pst.executeUpdate();

					}
				}

			}
			//logger.info("Load Condiciones Urbanisticas");
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar CondicionesUrbanisticas: determinacion: "+cub.getCodigodeterminacion_determinacion() + " *** entidad: "+cub.getCodigoentidad_entidad());
			throw e;
		}
			// /////////////////////////////////////////////////////////////////////////////////////////////
			// OPERACIONES
			// /////////////////////////////////////////////////////////////////////////////////////////////

		try{
			// Operaciones determinaciones
			Iterator<OperacionDeterminacionBean> i_odb = tb
					.getOperacionesdeterminaciones().iterator();
			while (i_odb.hasNext()) {
				odb = (OperacionDeterminacionBean) i_odb
						.next();

				// Recuperacion del id del tipo operacion determinacion
				int tipo_operacionentidadid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.diccionario_tiposoperaciondeterminacion WHERE codigo = ?");
				pst.setString(1, odb.getTipo());
				pst.setInt(2, this.fip_id);
				rs = pst.executeQuery();
				while (rs.next())
					tipo_operacionentidadid = rs.getInt(1);
				
				// Recuperacion del id de la determinacion operada
				int operada_determinacionid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
				pst.setString(1, odb.getOperada_determinacion());
				rs = pst.executeQuery();
				while (rs.next())
					operada_determinacionid = rs.getInt(1);

				// Recuperacion del id de la determinacion operadora
				int operadora_determinacionid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
				pst.setString(1, odb.getOperadora_determinacion());
				rs = pst.executeQuery();
				while (rs.next())
					operadora_determinacionid = rs.getInt(1);

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.tramite_operacionesdeterminaciones (id, tipo, orden, texto, operada_determinacionid, operadora_determinacionid, tramite) "
								+ "VALUES (nextval('gestorfip.seq_tramite_operacionesdeterminaciones'),?,?,?,?,?,currval('gestorfip.seq_tramite'))");
				pst.setInt(1, tipo_operacionentidadid);
				pst.setLong(2, odb.getOrden());
				pst.setString(3, odb.getTexto());
				pst.setInt(4, operada_determinacionid);
				pst.setInt(5, operadora_determinacionid);
				pst.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar OperacionesDeterminaciones: operadora: "+odb.getOperadora_determinacion() + " *** operada: "+odb.getOperada_determinacion());
			throw e;
		}

		try{
			// Operacion entidades
			Iterator<OperacionEntidadBean> i_oeb = tb.getOperacionesentidades()
					.iterator();
			while (i_oeb.hasNext()) {
				 oeb = (OperacionEntidadBean) i_oeb.next();

				// Recuperacion del id del tipo operacion entidad
				int tipo_operaciondeterminacionid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.diccionario_tiposoperacionentidad WHERE codigo = ?");
				pst.setString(1, oeb.getTipo());
				pst.setInt(2, this.fip_id);
				rs = pst.executeQuery();
				while (rs.next())
					tipo_operaciondeterminacionid = rs.getInt(1);
				
				// Recuperacion del id de la entidad operada
				int operada_entidadid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.tramite_entidades WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
				pst.setString(1, oeb.getOperada_entidad());
				rs = pst.executeQuery();
				while (rs.next())
					operada_entidadid = rs.getInt(1);

				// Recuperacion del id de la entidad operadora
				int operadora_entidadid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.tramite_entidades WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
				pst.setString(1, oeb.getOperadora_entidad());
				rs = pst.executeQuery();
				while (rs.next())
					operadora_entidadid = rs.getInt(1);

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.tramite_operacionesentidades (id, tipo, orden, texto, operada_entidadid, operadora_entidadid, propiedadesadscripcion_cuantia, propiedadesadscripcion_texto, tramite) "
								+ "VALUES (nextval('gestorfip.seq_tramite_operacionesentidades'),?,?,?,?,?,?,?,currval('gestorfip.seq_tramite'))");
				pst.setInt(1, tipo_operaciondeterminacionid);
				pst.setLong(2, oeb.getOrden());
				pst.setString(3, oeb.getTexto());
				pst.setInt(4, operada_entidadid);
				pst.setInt(5, operadora_entidadid);
				pst.setDouble(6, oeb.getPropiedadesadscripcion_cuantia());
				pst.setString(7, oeb.getPropiedadesadscripcion_texto());
				pst.executeUpdate();

				// Recuperacion del id de la determinacion de la unidad de la
				// propiedad adscripcion
				if (oeb.getPropiedadesadscripcion_tipo_determinacion() != null
						&& oeb.getPropiedadesadscripcion_unidad_determinacion() != null) {
					int unidadpropiedadadscripcion_determinacionid = 0;
					pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
					pst.setString(1, oeb
							.getPropiedadesadscripcion_unidad_determinacion());
					rs = pst.executeQuery();
					while (rs.next())
						unidadpropiedadadscripcion_determinacionid = rs
								.getInt(1);

					// Recuperacion del id de la determinacion del tipo de la
					// propiedad adscripcion
					int tipopropiedadadscripcion_determinacionid = 0;
					pst = this.conn
							.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
					pst.setString(1, oeb
							.getPropiedadesadscripcion_tipo_determinacion());
					rs = pst.executeQuery();
					while (rs.next())
						tipopropiedadadscripcion_determinacionid = rs.getInt(1);

					pst = this.conn
							.prepareStatement("UPDATE gestorfip.tramite_operacionesentidades SET propiedadesadscripcion_unidad_determinacionid = ?, propiedadesadscripcion_tipo_determinacionid = ? WHERE id = currval('gestorfip.seq_tramite_operacionesentidades')");
					pst.setInt(1, unidadpropiedadadscripcion_determinacionid);
					pst.setInt(2, tipopropiedadadscripcion_determinacionid);
					pst.executeUpdate();
				}
			}
		//	logger.info("Load Operaciones");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar OperacionesEntidades: operadora: "+oeb.getOperadora_entidad() + " *** operada: "+oeb.getOperada_entidad());
			throw e;
		}
		
		try{
			// /////////////////////////////////////////////////////////////////////////////////////////////
			// UNIDADES
			// /////////////////////////////////////////////////////////////////////////////////////////////

			Iterator<UnidadBean> i_ub = tb.getUnidades().iterator();
			while (i_ub.hasNext()) {
				ub = (UnidadBean) i_ub.next();

				// Recuperacion del id de la determinacion
				int unidad_determinacionid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
				pst.setString(1, ub.getDeterminacion());
				rs = pst.executeQuery();
				while (rs.next())
					unidad_determinacionid = rs.getInt(1);

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.tramite_unidades (id, abreviatura, definicion, determinacionid, tramite) "
								+ "VALUES (nextval('gestorfip.seq_tramite_unidades'),?,?,?,currval('gestorfip.seq_tramite'))");
				pst.setString(1, ub.getAbreviatura());
				pst.setString(2, ub.getDefinicion());
				pst.setInt(3, unidad_determinacionid);
				pst.executeUpdate();
			}
			//logger.info("Load Unidades");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar Unidades: determinacion: "+ub.getDeterminacion() );
			throw e;
		}
		
		try{
			// /////////////////////////////////////////////////////////////////////////////////////////////
			// APLICACION-AMBITOS
			// /////////////////////////////////////////////////////////////////////////////////////////////

			Iterator<AplicacionAmbitoBean> i_aab = tb.getAplicacionambitos()
					.iterator();
			while (i_aab.hasNext()) {
				aab = (AplicacionAmbitoBean) i_aab.next();

				// Recuperacion del id del ambito
				int aplicacionambito_ambitoid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.diccionario_ambitos WHERE codigo = ?");
				pst.setString(1, aab.getAmbito());
				rs = pst.executeQuery();
				while (rs.next())
					aplicacionambito_ambitoid = rs.getInt(1);

				// Recuperacion del id de la entidad
				int aplicacionambito_entidadid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.tramite_entidades WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
				pst.setString(1, aab.getEntidad());
				rs = pst.executeQuery();
				while (rs.next())
					aplicacionambito_entidadid = rs.getInt(1);

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.tramite_aplicacionambitos (id, ambitoid, entidadid, tramite) "
								+ "VALUES (nextval('gestorfip.seq_tramite_aplicacionambitos'),?,?,currval('gestorfip.seq_tramite'))");
				pst.setInt(1, aplicacionambito_ambitoid);
				pst.setInt(2, aplicacionambito_entidadid);
				pst.executeUpdate();

			}
			//logger.info("Load Aplicacion Ambitos");
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar AplicacionAmbitos: ambito: "+aab.getAmbito() );
			throw e;
		}
		
		try{
			// /////////////////////////////////////////////////////////////////////////////////////////////
			// ADSCRIPCIONES
			// /////////////////////////////////////////////////////////////////////////////////////////////
			//logger.info("Load Adscripciones");
			Iterator<AdscripcionBean> i_ab = tb.getAdscripciones().iterator();
			while (i_ab.hasNext()) {
				ab = (AdscripcionBean) i_ab.next();
				logger.info("entOrigen=="+ab.getEntidadorigen()+ " entDestino=="+ab.getEntidaddestino());
				// Recuperacion del id de la entidad origen
				int adscripcionorigen_entidadid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.tramite_entidades WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
				pst.setString(1, ab.getEntidadorigen());
				rs = pst.executeQuery();
				while (rs.next())
					adscripcionorigen_entidadid = rs.getInt(1);

				// Recuperacion del id de la entidad destino
				int adscripciondestino_entidadid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.tramite_entidades WHERE codigo = ? AND tramite = currval('gestorfip.seq_tramite')");
				pst.setString(1, ab.getEntidaddestino());
				rs = pst.executeQuery();
				while (rs.next())
					adscripciondestino_entidadid = rs.getInt(1);
				
				int detUni_idTramite = 0;
				pst = this.conn
				.prepareStatement("SELECT id FROM gestorfip.tramite WHERE codigo = ? AND fip = currval('gestorfip.seq_fip')");
				pst.setString(1, ab.getPropiedades_tramite_unidad_determinacion());
				rs = pst.executeQuery();
				while (rs.next())
					detUni_idTramite = rs.getInt(1);
				
				// Recuperacion del id de la determinacion de la unidad de la
				// propiedad
				int adscripcionunidadpropiedad_determinacionid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = ?");
				pst.setString(1, ab.getPropiedades_unidad_determinacion());
				pst.setInt(2, detUni_idTramite);
				rs = pst.executeQuery();
				while (rs.next())
					adscripcionunidadpropiedad_determinacionid = rs.getInt(1);

				
				int detTipo_idTramite = 0;
				pst = this.conn
				.prepareStatement("SELECT id FROM gestorfip.tramite WHERE codigo = ? AND fip = currval('gestorfip.seq_fip')");
				pst.setString(1, ab.getPropiedades_tramite_tipo_determinacion());
				rs = pst.executeQuery();
				while (rs.next())
					detTipo_idTramite = rs.getInt(1);
				
				// Recuperacion del id de la determinacion del tipo de la
				// propiedad
				int adscripciontipopropiedad_determinacionid = 0;
				pst = this.conn
						.prepareStatement("SELECT id FROM gestorfip.tramite_determinaciones WHERE codigo = ? AND tramite = ?");
				pst.setString(1, ab.getPropiedades_tipo_determinacion());
				pst.setInt(2, detTipo_idTramite);
				rs = pst.executeQuery();
				while (rs.next())
					adscripciontipopropiedad_determinacionid = rs.getInt(1);

				pst = this.conn
						.prepareStatement("INSERT INTO gestorfip.tramite_adscripciones (id, entidadorigenid, entidaddestinoid, propiedades_cuantia, propiedades_texto, propiedades_unidad_determinacionid, propiedades_tipo_determinacionid, tramite) "
								+ "VALUES (nextval('gestorfip.seq_tramite_adscripciones'),?,?,?,?,?,?,currval('gestorfip.seq_tramite'))");
				pst.setInt(1, adscripcionorigen_entidadid);
				pst.setInt(2, adscripciondestino_entidadid);
				pst.setDouble(3, ab.getPropiedades_cuantia());
				pst.setString(4, ab.getPropiedades_texto());
				pst.setInt(5, adscripcionunidadpropiedad_determinacionid);
				pst.setInt(6, adscripciontipopropiedad_determinacionid);
				pst.executeUpdate();
			}
			//logger.info("Load Adscripciones");

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar Adscripciones: destino: "+ab.getEntidaddestino() + " *** origen: " +ab.getEntidadorigen());
			throw e;
		}
		

	}

	/**
	 * Inserta el catalogo sistematizado contenido en el FIP en la BD
	 */
	public void loadCatalogoSistematizadoIntoDB() throws SQLException {

		try {
			// Recuperacion del id del ambito
			int ambito_id = 0;
			pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.diccionario_ambitos WHERE codigo = ? ");
			pst.setString(1, catalogosistematizado.getAmbito());
			rs = pst.executeQuery();
			while (rs.next())
				ambito_id = rs.getInt(1);

			pst = this.conn
					.prepareStatement("INSERT INTO gestorfip.catalogosistematizado (id, ambito, nombre, tramite) "
							+ "VALUES (nextval('gestorfip.seq_catalogosistematizado'),?,?,currval('gestorfip.seq_tramite'))");
			pst.setInt(1, ambito_id);
			pst.setString(2, catalogosistematizado.getNombre());
			pst.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar CatalogoSistematizado: nombre: "+catalogosistematizado.getNombre() +
					" *** codigo: "+catalogosistematizado.getTramite());
			throw e;
		}
	}

	/**
	 * Inserta el planeamiento vigente contenido en el FIP en la BD
	 */
	public void loadPlaneamientoVigenteIntoDB() throws SQLException {

		try {
			// Recuperacion del id del ambito
			int ambito_id = 0;
			pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.diccionario_ambitos WHERE codigo = ?");
			pst.setString(1, planeamientovigente.getAmbito());
			rs = pst.executeQuery();
			while (rs.next())
				ambito_id = rs.getInt(1);

			pst = this.conn
					.prepareStatement("INSERT INTO gestorfip.planeamientovigente (id, ambito, nombre, tramite) "
							+ "VALUES (nextval('gestorfip.seq_planeamientovigente'),?,?,currval('gestorfip.seq_tramite'))");
			pst.setInt(1, ambito_id);
			pst.setString(2, planeamientovigente.getNombre());
			pst.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar PlaneamientoVigente: nombre: "+planeamientovigente.getNombre() +
					" *** codigo: "+planeamientovigente.getTramite());
			throw e;
		}
	}

	/**
	 * Inserta el planeamiento encargado contenido en el FIP en la BD
	 */
	public void loadPlaneamientoEncargadoIntoDB() throws SQLException {

		try {
			// Recuperacion del id del ambito
			int ambito_id = 0;
			pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.diccionario_ambitos WHERE codigo = ?");
			pst.setString(1, planeamientoencargado.getAmbito());
			rs = pst.executeQuery();
			while (rs.next())
				ambito_id = rs.getInt(1);

			// Recuperacion del id del instrumento
			int instrumento_id = 0;
			pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.diccionario_instrumentos WHERE codigo = ? AND fip = ?");
			pst.setString(1, planeamientoencargado.getInstrumento());
			pst.setInt(2, this.fip_id);
			rs = pst.executeQuery();
			while (rs.next())
				instrumento_id = rs.getInt(1);

			// Recuperacion del id del tipo tramite
			int tipotramite_id = 0;
			pst = this.conn
					.prepareStatement("SELECT id FROM gestorfip.diccionario_tipostramite WHERE codigo = ? AND fip = ?");
			pst.setString(1, planeamientoencargado.getTipotramite());
			pst.setInt(2, this.fip_id);
			rs = pst.executeQuery();
			while (rs.next())
				tipotramite_id = rs.getInt(1);

			pst = this.conn
					.prepareStatement("INSERT INTO gestorfip.planeamientoencargado (id, ambito, nombre, instrumento, iteracion, tipotramite, codigotramite, ambitoaplicacion, fip) "
							+ "VALUES (nextval('gestorfip.seq_planeamientoencargado'),?,?,?,?,?,?,currval('gestorfip.seq_fip'))");
			pst.setInt(1, ambito_id);
			pst.setString(2, planeamientoencargado.getNombre());
			pst.setInt(3, instrumento_id);
			pst.setInt(4, planeamientoencargado.getIteracion());
			pst.setInt(5, tipotramite_id);
			pst.setString(6, planeamientoencargado.getCodigotramite());		
			pst.executeUpdate();
			
			// Insercion del tramite asociado
			pst = this.conn
			.prepareStatement("INSERT INTO gestorfip.tramite (id, tipotramite, codigo, fip) "
					+ "VALUES (nextval('gestorfip.seq_tramite'),?,?,?)");
			pst.setInt(1, tipotramite_id);
			pst.setString(2, planeamientoencargado.getCodigotramite());
			pst.setInt(3, this.fip_id);
			pst.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage() + " Error al cargar PlaneamientoVigente: nombre: "+planeamientoencargado.getNombre());
			throw e;
		}
	}
}
