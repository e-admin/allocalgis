package es.satec.localgismobile.server.projectsync.xml.eiel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.geopista.app.eiel.ConstantesLocalGISEIEL_LCGIII;
import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.app.eiel.beans.AbastecimientoAutonomoEIEL;
import com.geopista.app.eiel.beans.CabildoConsejoEIEL;
import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.CasasConsistorialesEIEL;
import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.CentrosAsistencialesEIEL;
import com.geopista.app.eiel.beans.CentrosCulturalesEIEL;
import com.geopista.app.eiel.beans.CentrosEnsenianzaEIEL;
import com.geopista.app.eiel.beans.CentrosSanitariosEIEL;
import com.geopista.app.eiel.beans.ColectorEIEL;
import com.geopista.app.eiel.beans.ComarcaEIEL;
import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.beans.Depuradora1EIEL;
import com.geopista.app.eiel.beans.Depuradora2EIEL;
import com.geopista.app.eiel.beans.DiseminadosEIEL;
import com.geopista.app.eiel.beans.EdificiosSinUsoEIEL;
import com.geopista.app.eiel.beans.ElementoPuntualAbastecimientoEIEL;
import com.geopista.app.eiel.beans.ElementoPuntualSaneamientoEIEL;
import com.geopista.app.eiel.beans.EmisariosEIEL;
import com.geopista.app.eiel.beans.Encuestados1EIEL;
import com.geopista.app.eiel.beans.Encuestados2EIEL;
import com.geopista.app.eiel.beans.EntidadEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.IncendiosProteccionEIEL;
import com.geopista.app.eiel.beans.InstalacionesDeportivasEIEL;
import com.geopista.app.eiel.beans.LimpiezaCallesEIEL;
import com.geopista.app.eiel.beans.LonjasMercadosEIEL;
import com.geopista.app.eiel.beans.MataderosEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.beans.NucleoEncuestado7EIEL;
import com.geopista.app.eiel.beans.NucleosAbandonadosEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.OtrosServMunicipalesEIEL;
import com.geopista.app.eiel.beans.PadronMunicipiosEIEL;
import com.geopista.app.eiel.beans.PadronNucleosEIEL;
import com.geopista.app.eiel.beans.ParquesJardinesEIEL;
import com.geopista.app.eiel.beans.PlaneamientoUrbanoEIEL;
import com.geopista.app.eiel.beans.PuntosVertidoEIEL;
import com.geopista.app.eiel.beans.RecogidaBasurasEIEL;
import com.geopista.app.eiel.beans.SaneamientoAutonomoEIEL;
import com.geopista.app.eiel.beans.ServiciosAbastecimientosEIEL;
import com.geopista.app.eiel.beans.ServiciosRecogidaBasuraEIEL;
import com.geopista.app.eiel.beans.ServiciosSaneamientoEIEL;
import com.geopista.app.eiel.beans.TanatoriosEIEL;
import com.geopista.app.eiel.beans.TramosCarreterasEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL;
import com.geopista.app.eiel.beans.VertederosEIEL;
import com.geopista.app.eiel.beans.WorkflowEIEL;
import com.geopista.ui.dialogs.eiel.EIELFilesUtils;
import com.geopista.ui.dialogs.global.Constants_LCGIII;
import com.geopista.ui.dialogs.global.Utils;

import es.satec.localgismobile.server.projectsync.MobilePermissionException;
import es.satec.localgismobile.server.projectsync.beans.ResultString;
import es.satec.localgismobile.server.projectsync.xml.ConstantsXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.XMLUpload;
import es.satec.localgismobile.server.projectsync.xml.beans.ItemXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.beans.MetadataXMLUpload;
import es.satec.localgismobile.server.projectsync.xml.beans.TabXMLUpload;


public class XMLUploadEIEL extends XMLUpload {

	private static Logger logger = Logger
	.getLogger(XMLUploadEIEL.class);
	
	private String urlEIEL;
	private LocalGISEIELClient eielClient;
	private com.geopista.security.SecurityManager sm;
	
	public XMLUploadEIEL(){		
	}
	
	public XMLUploadEIEL(String urlEIEL, LocalGISEIELClient eielClient, com.geopista.security.SecurityManager sm){		
		this.urlEIEL = urlEIEL;
		this.eielClient = eielClient;
		this.sm = sm;
	}
	
	public String getUrlEIEL() {
		return urlEIEL;
	}

	public void setUrlEIEL(String urlEIEL) {
		this.urlEIEL = urlEIEL;
	}

	public LocalGISEIELClient getEielClient() {
		return eielClient;
	}

	public void setEielClient(LocalGISEIELClient eielClient) {
		this.eielClient = eielClient;
	}
	
	public EIELFilesUtils getEIELFilesUtils(String layerId){
		ArrayList<String> extractLayersNames = new ArrayList<String>();
		extractLayersNames.add(layerId);
		return new EIELFilesUtils(extractLayersNames, ConstantesLocalGISEIEL_LCGIII.Locale, eielClient, sm);	
	}
	
	public WorkflowEIEL getEIELBean(String classId){
		if(classId.equals(Constants_LCGIII.CLASSID_ABASTECIMIENTO_AUTONOMO))
			return new AbastecimientoAutonomoEIEL();		
		else if(classId.equals(Constants_LCGIII.CLASSID_CABILDOCONSEJO))
			return new CabildoConsejoEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_CAPTACIONES))
			return new CaptacionesEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_CASASCONSISTORIALES))
			return new CasasConsistorialesEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_CEMENTERIOS))
			return new CementeriosEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_CENTROSASISTENCIALES))
			return new CentrosAsistencialesEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_CENTROSCULTURALES))
			return new CentrosCulturalesEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_CENTROSENSENIANZA))
			return new CentrosEnsenianzaEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_CENTROSSANITARIOS))
			return new CentrosSanitariosEIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_COLECTOR))
//			return new ColectorEIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_COMARCA))
//			return new ComarcaEIEL();		
		else if(classId.equals(Constants_LCGIII.CLASSID_DEPOSITOS))
			return new DepositosEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_DEPURADORAS1))
			return new Depuradora1EIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_DEPURADORAS2))
			return new Depuradora2EIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_DISEMINADOS))
//			return new DiseminadosEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_EDIFICIOSSINUSO))
			return new EdificiosSinUsoEIEL();
		//REVISAR
//		else if(classId.equals(Constants_LCGIII.CLASSID_ELEMEN_PUNTUALES_ABASTECIMIENTO))
//			return new ElementoPuntualAbastecimientoEIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_ELEMEN_PUNTUALES_SANEAMIENTO))
//			return new ElementoPuntualSaneamientoEIEL();
		//FIN REVISAR
//		else if(classId.equals(Constants_LCGIII.CLASSID_EMISARIOS))
//			return new EmisariosEIEL();			
//		else if(classId.equals(Constants_LCGIII.CLASSID_ENCUESTADOS1))
//			return new Encuestados1EIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_ENCUESTADOS2))
//			return new Encuestados2EIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_ENTIDADES))
//			return new EntidadEIEL();	
//		else if(classId.equals(Constants_LCGIII.CLASSID_ENTIDADES_SINGULARES))
//			return new EntidadesSingularesEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_INCENDIOS_PROTECCION))
			return new IncendiosProteccionEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_INSTALACIONES_DEPORTIVAS))
			return new InstalacionesDeportivasEIEL();
//INVENTARIO		
//		else if(classId.equals(Constants_LCGIII))
//			eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL_LCGIII.INSTALACIONES_DEPORTIVAS_CLAVE, Constants_LCGIII.CLASSID_INSTALACIONES_DEPORTIVAS, getCamposCapaEIEL(layerName, locale, Constants_LCGIII.CLASSID_INSTALACIONES_DEPORTIVAS,(new InstalacionesDeportivasEIEL()).getRelacionFields()), (new InstalacionesDeportivasEIEL()).getRelacionFields()));
//		else if(classId.equals(Constants_LCGIII.CLASSID_LIMPIEZA_CALLES))
//			return new LimpiezaCallesEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_LONJAS_MERCADOS))
			return new LonjasMercadosEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_MATADEROS))
			return new MataderosEIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_MUNICIPIO))
//			return new MunicipioEIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_NUCLEOENCUESTADO7))
//			new NucleoEncuestado7EIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_NUCLEOS_ABANDONADOS))
//			return new NucleosAbandonadosEIEL();		
//		else if(classId.equals(Constants_LCGIII.CLASSID_NUCLEOS_POBLACION))
//			return new NucleosPoblacionEIEL();	
//		else if(classId.equals(Constants_LCGIII.CLASSID_OTROS_SERVICIOS_MUNICIPALES))
//			return new OtrosServMunicipalesEIEL();	
//		else if(classId.equals(Constants_LCGIII.CLASSID_PADRON_MUNICIPIOS))
//			return new PadronMunicipiosEIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_PADRON_NUCLEOS))
//			return new PadronNucleosEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_PARQUESJARDINES))
			return new ParquesJardinesEIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_PLANEAMIENTO_URBANO))
//			return new PlaneamientoUrbanoEIEL();		
		else if(classId.equals(Constants_LCGIII.CLASSID_PUNTOSDEVERTIDO))
			return new PuntosVertidoEIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_RECOGIDABASURA))
//			return new RecogidaBasurasEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_SANEAMIENTOAUTONOMO))
			return new SaneamientoAutonomoEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_SERVICIOS_ABASTECIMIENTOS))
			return new ServiciosAbastecimientosEIEL();
//		else if(classId.equals(Constants_LCGIII.CLASSID_SERVICIOS_RECOGIDA_BASURA))
//			return new ServiciosRecogidaBasuraEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_SERVICIOS_SANEAMIENTO))
			return new ServiciosSaneamientoEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_TANATORIOS))
			return new TanatoriosEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_TRAMOS_CARRETERAS))
			return new TramosCarreterasEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_TRAMOS_CONDUCCION))
			return new TramosConduccionEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_TRATAMIENTOSPOTABILIZACION))
			return new TratamientosPotabilizacionEIEL();
		else if(classId.equals(Constants_LCGIII.CLASSID_VERTEDEROS))
			return new VertederosEIEL();		
		return null;
	}
	
	/**
	 * Inserta una feature de tipo EIEL
	 * @param metadata
	 * @param results
	 * @throws Exception
	 *
	 */
    public void insertarModificarEIEL(MetadataXMLUpload metadata, ResultString results, String layerId) throws Exception{
    	logger.info("Insertando/Modificando Elemento EIEL\n");	
    	HashMap<String, String> idFeat = metadata.getIdFeatures();		
		EIELFilesUtils eielFilesUtils = getEIELFilesUtils(layerId);
		if(eielFilesUtils.getReflectionMethods().size()>0){
			logger.info("Rellenando Id features: "+ idFeat);	
			
			logger.info("Realizando Reflexión");
			WorkflowEIEL eielElement = getEIELBean(eielFilesUtils.getEIELLayerBean(layerId).getClassId());	
			HashMap changedAttributes = reflectSetMethod(eielElement, metadata, eielFilesUtils.getReflectionMethods());
			
			logger.info("Guardando en Base de datos");	
		    eielClient.insertarElemento(eielElement, layerId, eielFilesUtils.getEIELLayerBean(layerId).getCodEIEL());
			
		    logger.info("Resultado Correcto");
		}
		else throw new MobilePermissionException("\n");
    }    	
    
    public void borrarEIEL(MetadataXMLUpload metadata, ResultString results, String layerId) throws Exception{
    	HashMap<String, String> idFeat = metadata.getIdFeatures();		
		EIELFilesUtils eielFilesUtils = getEIELFilesUtils(layerId);
		if(eielFilesUtils.getReflectionMethods().size()>0){
			logger.info("Rellenando Id features: "+ idFeat);	
			
			logger.info("Realizando Reflexión");
			WorkflowEIEL eielElement = getEIELBean(eielFilesUtils.getEIELLayerBean(layerId).getClassId());	
			HashMap changedAttributes = reflectSetMethod(eielElement, metadata, eielFilesUtils.getReflectionMethods());	
			
		    logger.info("Actualizando en Base de datos");		    
			eielClient.eliminarElemento(eielElement, new ArrayList<Integer>(), layerId, eielFilesUtils.getEIELLayerBean(layerId).getCodEIEL());
			
			logger.info("Elemento EIEL actualizado");			
		}
		else throw new MobilePermissionException("\n");    	
    }    
    
    /**
	 * Busca el atributo del inventario de patrimonio en el xml metadata
	 * 
	 * @param tabList
	 * @return
	 * @throws Exception
	 */
	public String getAttEIEL(List<TabXMLUpload> tabList,
			String attName) throws Exception {
		TabXMLUpload tabXMLUpload = null;
		List<ItemXMLUpload> itemList = null;
		ItemXMLUpload itemXMLUpload = null;
		String res = "";
		for (Iterator iterator = tabList.iterator(); iterator.hasNext();) {
			tabXMLUpload = (TabXMLUpload) iterator.next();
			if (Utils.isInArray(Constants_LCGIII.TIPOS_CLASSID_EIEL,tabXMLUpload.getClassId())) {
				itemList = tabXMLUpload.getItemList();
				for (Iterator iterator2 = itemList.iterator(); iterator2
						.hasNext();) {
					itemXMLUpload = (ItemXMLUpload) iterator2.next();
					if (itemXMLUpload.getReflectMethod().equals(attName)) {
						res = itemXMLUpload.getValue();
						return res;
					}
				}
			}
		}

		throw new Exception("No se ha encontrado el atributo " + attName);
	}
	
	/**
	 * Ejecutar el set correspondiente al bean añadiendo el valor del nodo XML
	 * 
	 * @param itemValue
	 * @param elem
	 * @param strMethod
	 * @param objectInvoke
	 * @throws Exception
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public HashMap reflectSetMethod(WorkflowEIEL eielElement,
			MetadataXMLUpload metadata, HashMap<String,String> reflectMethods) throws Exception {
		List<TabXMLUpload> tabList = metadata.getTabList();
		TabXMLUpload tabXMLUpload = null;
		List<ItemXMLUpload> itemList = null;
		HashMap changedAttributes = new HashMap();

		String classId = null;	
		Iterator it = tabList.iterator();
		while(it.hasNext()){
			tabXMLUpload = (TabXMLUpload) it.next();
			classId = tabXMLUpload.getClassId();
			itemList = tabXMLUpload.getItemList();
			changedAttributes.putAll(reflectItems(eielElement, itemList, classId, reflectMethods));
		}

		try{
			//Rellena el campo IdMunicipio necesario para InventarioEIEL
			eielElement.setIdMunicipio(Integer.parseInt(eielElement.getCodINEProvincia()+eielElement.getCodINEMunicipio()));
		}
		catch(Exception e){				
		}
		
		return changedAttributes;
	}
	
	protected HashMap reflectItems(Object objectInvoke,
			List<ItemXMLUpload> itemList, String classId, HashMap<String,String> reflectMethods) throws Exception {
		if (objectInvoke == null) {
			return null;
		}
		//NUEVO
		setValidationState(objectInvoke);
//		setIdMunicipality(objectInvoke);
		//FIN NUEVO
		ItemXMLUpload itemXMLUpload = null;
		String strMethod = null;
		Method method = null;
		HashMap changedAtributes = new HashMap();
		try {
			Iterator it = itemList.iterator();
			while(it.hasNext()){
				itemXMLUpload = (ItemXMLUpload) it.next();
				strMethod = "set" + reflectMethods.get(classId + "." + itemXMLUpload.getReflectMethod());
				if (itemXMLUpload.getType().toLowerCase().equals(
						ConstantsXMLUpload.TAG_ITEM) || itemXMLUpload.getType().toLowerCase().equals(
								ConstantsXMLUpload.TAG_ITEMLIST)) {	
					Class<?>[] reflectionMethodType = getReflectionMethodType(objectInvoke.getClass().getMethods(), strMethod);
					method = objectInvoke.getClass().getMethod(strMethod, reflectionMethodType);
					if (method != null) {
						try {						
							method.invoke(objectInvoke, XMLUpload.getRelfectionValue(itemXMLUpload.getValue(), reflectionMethodType[0].getName()));
						} catch (Exception e) {
							logger.error(
									"Error al acceder al metodo "
											+ objectInvoke.getClass() + "."
											+ strMethod, e);
						}
					}
				} else if (itemXMLUpload.getType().toLowerCase().equals(
						ConstantsXMLUpload.TAG_ITEMLIST)) {
					method = objectInvoke.getClass().getMethod(strMethod,
							new Class[] { List.class });
					if (method != null) {
						try {
							method.invoke(objectInvoke, itemXMLUpload
									.getSubItems());
						} catch (Exception e) {
							logger.error(
									"Error al acceder al metodo "
											+ objectInvoke.getClass() + "."
											+ strMethod, e);
						}
					}
				}
				if (itemXMLUpload.getUpdatable().toLowerCase().equals("true")) {
					changedAtributes
							.put(itemXMLUpload.getReflectMethod(), "");
				}
			}

		} catch (Exception e) {
			logger.error("Error al acceder al metodo "
					+ objectInvoke.getClass() + "." + strMethod, e);
			throw new Exception("Error al acceder al metodo "
					+ objectInvoke.getClass() + "." + strMethod + ": "
					+ e.getMessage());
		}
		return changedAtributes;

	}
	
	private void setValidationState(Object objectInvoke) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, IllegalAccessException {
		Method method = objectInvoke.getClass().getMethod("setEstadoValidacion", new Class[] {int.class});
		method.invoke(objectInvoke, ConstantesLocalGISEIEL_LCGIII.ESTADO_PUBLICABLE_MOVILIDAD);
	}
	
//	private void setIdMunicipality(Object objectInvoke) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, IllegalAccessException {
//		Method method = objectInvoke.getClass().getMethod("setIdMunicipio", new Class[] {int.class});
//	}	
	
	public Class<?> [] getReflectionMethodType(Method [] methods, String strMethod) throws SecurityException, NoSuchMethodException{		
		for(int i=0;i<methods.length;i++){
			if(strMethod.equals(methods[i].getName()))
				return methods[i].getParameterTypes();	
		}
		return null;
	}
	
}
