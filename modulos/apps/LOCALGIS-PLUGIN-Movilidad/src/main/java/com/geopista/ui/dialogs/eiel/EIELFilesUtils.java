/**
 * EIELFilesUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.eiel;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
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
import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.beans.Depuradora1EIEL;
import com.geopista.app.eiel.beans.Depuradora2EIEL;
import com.geopista.app.eiel.beans.EdificiosSinUsoEIEL;
import com.geopista.app.eiel.beans.IncendiosProteccionEIEL;
import com.geopista.app.eiel.beans.InstalacionesDeportivasEIEL;
import com.geopista.app.eiel.beans.LonjasMercadosEIEL;
import com.geopista.app.eiel.beans.MataderosEIEL;
import com.geopista.app.eiel.beans.ParquesJardinesEIEL;
import com.geopista.app.eiel.beans.PuntosVertidoEIEL;
import com.geopista.app.eiel.beans.SaneamientoAutonomoEIEL;
import com.geopista.app.eiel.beans.ServiciosAbastecimientosEIEL;
import com.geopista.app.eiel.beans.ServiciosSaneamientoEIEL;
import com.geopista.app.eiel.beans.TanatoriosEIEL;
import com.geopista.app.eiel.beans.TramosCarreterasEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL;
import com.geopista.app.eiel.beans.VertederosEIEL;
import com.geopista.app.eiel.beans.filter.LCGCampoCapaEIEL;
import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.ui.dialogs.beans.eiel.EIELLayerBean;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.dialogs.global.FilesUtils;
import com.geopista.ui.dialogs.global.Utils;
import com.vividsolutions.jump.I18N;

/**
 * Clase de gestión de ficheros de eiel
 * @author irodriguez
 *
 */
public class EIELFilesUtils extends FilesUtils{

	private static Logger logger = Logger.getLogger(EIELFilesUtils.class);

	private final static String EIEL_SERVLET = "/EIELServlet";
	
	private LocalGISEIELClient eielClient;	
	private HashMap<String,String> reflectionMethods;
	private HashMap<String,EIELLayerBean> eielLayerBeans;
	
	public EIELFilesUtils(String extractLayerName){
		ArrayList<String> extractLayersNames = new ArrayList<String>();
		extractLayersNames.add(extractLayerName);
		initialize(extractLayersNames, ConstantesLocalGISEIEL.Locale,new LocalGISEIELClient(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL)+EIEL_SERVLET));		
	}
	
	public EIELFilesUtils(ArrayList<String> extractLayersNames){
		initialize(extractLayersNames, ConstantesLocalGISEIEL.Locale,new LocalGISEIELClient(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL)+EIEL_SERVLET));		
	}
	
	public EIELFilesUtils(ArrayList<String> extractLayersNames, String locale){	
		initialize(extractLayersNames, locale, new LocalGISEIELClient(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL)+EIEL_SERVLET));		
	}
	
	public EIELFilesUtils(ArrayList<String> extractLayersNames, String locale, LocalGISEIELClient eielClient, com.geopista.security.SecurityManager sm){

		com.geopista.security.SecurityManager.setSm(sm);
		initialize(extractLayersNames, locale, eielClient);		
	}
	
	public void initialize(ArrayList<String> extractLayersNames, String locale, LocalGISEIELClient eielClient){
		initResourceBundle(locale);		
		this.eielClient = eielClient;
		reflectionMethods = new HashMap<String,String>();
		eielLayerBeans = new HashMap<String,EIELLayerBean>();
		
		Iterator it = extractLayersNames.iterator();	
		while(it.hasNext()){
			String layerName = (String) it.next();		
			if(layerName.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO_CLAVE, ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO, Constants.CLASSID_ABASTECIMIENTO_AUTONOMO, getCamposCapaEIEL(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO, locale, Constants.CLASSID_ABASTECIMIENTO_AUTONOMO,(new AbastecimientoAutonomoEIEL()).getRelacionFields()), (new AbastecimientoAutonomoEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.CABILDO_CONSEJO_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.CABILDOCONSEJO_CLAVE, ConstantesLocalGISEIEL.CABILDO, Constants.CLASSID_CABILDOCONSEJO, getCamposCapaEIEL(ConstantesLocalGISEIEL.CABILDO, locale, Constants.CLASSID_CABILDOCONSEJO,(new CabildoConsejoEIEL()).getRelacionFields()), (new CabildoConsejoEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.CAPTACIONES_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.CAPTACIONES_ClAVE, ConstantesLocalGISEIEL.CAPTACIONES, Constants.CLASSID_CAPTACIONES, getCamposCapaEIEL(ConstantesLocalGISEIEL.CAPTACIONES, locale, Constants.CLASSID_CAPTACIONES,(new CaptacionesEIEL()).getRelacionFields()), (new CaptacionesEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.CASASCONSISTORIALES_CLAVE, ConstantesLocalGISEIEL.CASAS_CONSISTORIALES, Constants.CLASSID_CASASCONSISTORIALES, getCamposCapaEIEL(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES, locale, Constants.CLASSID_CASASCONSISTORIALES,(new CasasConsistorialesEIEL()).getRelacionFields()), (new CasasConsistorialesEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.CEMENTERIOS_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.CEMENTERIOS_CLAVE, ConstantesLocalGISEIEL.CEMENTERIOS, Constants.CLASSID_CEMENTERIOS, getCamposCapaEIEL(ConstantesLocalGISEIEL.CEMENTERIOS, locale, Constants.CLASSID_CEMENTERIOS,(new CementeriosEIEL()).getRelacionFields()), (new CementeriosEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.CENTROSASISTENCIALES_CLAVE, ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES, Constants.CLASSID_CENTROSASISTENCIALES, getCamposCapaEIEL(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES, locale, Constants.CLASSID_CENTROSASISTENCIALES,(new CentrosAsistencialesEIEL()).getRelacionFields()), (new CentrosAsistencialesEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.CENTROS_CULTURALES_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.CENTROCULTURAL_CLAVE, ConstantesLocalGISEIEL.CENTROS_CULTURALES, Constants.CLASSID_CENTROSCULTURALES, getCamposCapaEIEL(ConstantesLocalGISEIEL.CENTROS_CULTURALES, locale, Constants.CLASSID_CENTROSCULTURALES,(new CentrosCulturalesEIEL()).getRelacionFields()), (new CentrosCulturalesEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.CENTROENSENIANZA_CLAVE, ConstantesLocalGISEIEL.CENTROS_ENSENIANZA, Constants.CLASSID_CENTROSENSENIANZA, getCamposCapaEIEL(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA, locale, Constants.CLASSID_CENTROSENSENIANZA,(new CentrosEnsenianzaEIEL()).getRelacionFields()), (new CentrosEnsenianzaEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.CENTROS_SANITARIOS_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.CENTROSSANITARIOS_CLAVE, ConstantesLocalGISEIEL.CENTROS_SANITARIOS, Constants.CLASSID_CENTROSSANITARIOS, getCamposCapaEIEL(ConstantesLocalGISEIEL.CENTROS_SANITARIOS, locale, Constants.CLASSID_CENTROSSANITARIOS,(new CentrosSanitariosEIEL()).getRelacionFields()), (new CentrosSanitariosEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.COLECTOR_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.COLECTOR_CLAVE, ConstantesLocalGISEIEL.TCOLECTOR, Constants.CLASSID_COLECTOR, getCamposCapaEIEL(ConstantesLocalGISEIEL.TCOLECTOR, locale, Constants.CLASSID_COLECTOR,(new ColectorEIEL()).getRelacionFields()), (new ColectorEIEL()).getRelacionFields()));
//COMARCA
//			else if(layerName.equals(ConstantesLocalGISEIEL.COMARCA_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.COMARCA_CLAVE, Constants.CLASSID_COMARCA, getCamposCapaEIEL(layerName, locale, Constants.CLASSID_COMARCA,(new ComarcaEIEL()).getRelacionFields()), (new ComarcaEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.DEPOSITOS_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.DEPOSITOS_CLAVE, ConstantesLocalGISEIEL.DEPOSITOS, Constants.CLASSID_DEPOSITOS, getCamposCapaEIEL(ConstantesLocalGISEIEL.DEPOSITOS, locale, Constants.CLASSID_DEPOSITOS,(new DepositosEIEL()).getRelacionFields()), (new DepositosEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.DEPURADORAS1_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.DEPURADORAS1_CLAVE, ConstantesLocalGISEIEL.DEPURADORAS1, Constants.CLASSID_DEPURADORAS2, getCamposCapaEIEL(ConstantesLocalGISEIEL.DEPURADORAS1, locale, Constants.CLASSID_DEPURADORAS1,(new Depuradora1EIEL()).getRelacionFields()), (new Depuradora1EIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.DEPURADORAS2_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.DEPURADORAS2_CLAVE, ConstantesLocalGISEIEL.DEPURADORAS2, Constants.CLASSID_DEPURADORAS2, getCamposCapaEIEL(ConstantesLocalGISEIEL.DEPURADORAS2, locale, Constants.CLASSID_DEPURADORAS2,(new Depuradora2EIEL()).getRelacionFields()), (new Depuradora2EIEL()).getRelacionFields()));
//DISEMINADOS
//			else if(layerName.equals(ConstantesLocalGISEIEL.DISEMINADOS_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.DISEMINADOS_CLAVE, ConstantesLocalGISEIEL.DISEMINADOS, Constants.CLASSID_DISEMINADOS, getCamposCapaEIEL(ConstantesLocalGISEIEL.DISEMINADOS, locale, Constants.CLASSID_DISEMINADOS,(new DiseminadosEIEL()).getRelacionFields()), (new DiseminadosEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.EDIFICIOSSINUSO_CLAVE, ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO, Constants.CLASSID_EDIFICIOSSINUSO, getCamposCapaEIEL(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO, locale, Constants.CLASSID_EDIFICIOSSINUSO,(new EdificiosSinUsoEIEL()).getRelacionFields()), (new EdificiosSinUsoEIEL()).getRelacionFields()));
			//REVISAR
//ELEMENTO PUNTUAL ABASTECIMIENTO
//			else if(layerName.equals(ConstantesLocalGISEIEL.ELEMENTO_PUNTUAL_ABASTECIMIENTO_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.ELEMENTO_PUNTUAL_ABASTECIMIENTO_LAYER, ConstantesLocalGISEIEL.ELEMENTO_PUNTUAL_ABASTECIMIENTO_LAYER, Constants.CLASSID_EDIFICIOSSINUSO, getCamposCapaEIEL(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO, locale, Constants.CLASSID_EDIFICIOSSINUSO,(new EdificiosSinUsoEIEL()).getRelacionFields()), (new EdificiosSinUsoEIEL()).getRelacionFields()));
//ELEMENTO PUNTUAL SANEMAMIENTO
//			else if(layerName.equals(ConstantesLocalGISEIEL.ELEM_PUNTUAL_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.ELEM_PUNTUAL_LAYER, ConstantesLocalGISEIEL.ELEM_PUNTUAL_LAYER, Constants.CLASSID_EDIFICIOSSINUSO, getCamposCapaEIEL(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO, locale, Constants.CLASSID_EDIFICIOSSINUSO,(new EdificiosSinUsoEIEL()).getRelacionFields()), (new EdificiosSinUsoEIEL()).getRelacionFields()));
			//FIN REVISAR					
//EMISARIOS
//			else if(layerName.equals(ConstantesLocalGISEIEL.EMISARIOS_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.EMISARIOS_CLAVE, ConstantesLocalGISEIEL.EMISARIOS, Constants.CLASSID_EMISARIOS, getCamposCapaEIEL(ConstantesLocalGISEIEL.EMISARIOS, locale, Constants.CLASSID_EMISARIOS,(new EmisariosEIEL()).getRelacionFields()), (new EmisariosEIEL()).getRelacionFields()));
//NUCLEOS ENCUESTADOS 1(NP)	
//			else if(layerName.equals(ConstantesLocalGISEIEL.ENCUESTADOS1_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.ENCUESTADOS1_CLAVE, ConstantesLocalGISEIEL.ENCUESTADOS1, Constants.CLASSID_ENCUESTADOS1, getCamposCapaEIEL(ConstantesLocalGISEIEL.ENCUESTADOS1, locale, Constants.CLASSID_ENCUESTADOS1,(new Encuestados1EIEL()).getRelacionFields()), (new Encuestados1EIEL()).getRelacionFields()));
//NUCLEOS ENCUESTADOS 2(NP)	
//			else if(layerName.equals(ConstantesLocalGISEIEL.ENCUESTADOS2_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.ENCUESTADOS2_CLAVE, ConstantesLocalGISEIEL.ENCUESTADOS2, Constants.CLASSID_ENCUESTADOS2, getCamposCapaEIEL(ConstantesLocalGISEIEL.ENCUESTADOS2, locale, Constants.CLASSID_ENCUESTADOS2,(new Encuestados2EIEL()).getRelacionFields()), (new Encuestados2EIEL()).getRelacionFields()));

			//ENTIDAD
//			else if(layerName.equals(ConstantesLocalGISEIEL))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.EMISARIOS_CLAVE, Constants.CLASSID_EMISARIOS, getCamposCapaEIEL(layerName, locale, Constants.CLASSID_EMISARIOS,(new EntidadEIEL()).getRelacionFields()), (new EntidadEIEL()).getRelacionFields()));
//ENTIDADES SINGULARES
//			else if(layerName.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES_CLAVE, ConstantesLocalGISEIEL.ENTIDADES_SINGULARES, Constants.CLASSID_ENTIDADES_SINGULARES, getCamposCapaEIEL(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES, locale, Constants.CLASSID_ENTIDADES_SINGULARES,(new EntidadesSingularesEIEL()).getRelacionFields()), (new EntidadesSingularesEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_CLAVE, ConstantesLocalGISEIEL.INCENDIOS_PROTECCION, Constants.CLASSID_INCENDIOS_PROTECCION, getCamposCapaEIEL(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION, locale, Constants.CLASSID_INCENDIOS_PROTECCION,(new IncendiosProteccionEIEL()).getRelacionFields()), (new IncendiosProteccionEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_CLAVE, ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS, Constants.CLASSID_INSTALACIONES_DEPORTIVAS, getCamposCapaEIEL(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS, locale, Constants.CLASSID_INSTALACIONES_DEPORTIVAS,(new InstalacionesDeportivasEIEL()).getRelacionFields()), (new InstalacionesDeportivasEIEL()).getRelacionFields()));
//INVENTARIO		
//			else if(layerName.equals(ConstantesLocalGISEIEL.inv))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_CLAVE, Constants.CLASSID_INSTALACIONES_DEPORTIVAS, getCamposCapaEIEL(layerName, locale, Constants.CLASSID_INSTALACIONES_DEPORTIVAS,(new InstalacionesDeportivasEIEL()).getRelacionFields()), (new InstalacionesDeportivasEIEL()).getRelacionFields()));
//LIMPIEZA CALLES	
//			else if(layerName.equals(ConstantesLocalGISEIEL.li))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_CLAVE, Constants.CLASSID_INSTALACIONES_DEPORTIVAS, getCamposCapaEIEL(layerName, locale, Constants.CLASSID_INSTALACIONES_DEPORTIVAS,(new InstalacionesDeportivasEIEL()).getRelacionFields()), (new InstalacionesDeportivasEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.LONJAS_MERCADOS_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.LONJAS_MERCADOS_CLAVE, ConstantesLocalGISEIEL.LONJAS_MERCADOS, Constants.CLASSID_LONJAS_MERCADOS, getCamposCapaEIEL(ConstantesLocalGISEIEL.LONJAS_MERCADOS, locale, Constants.CLASSID_LONJAS_MERCADOS,(new LonjasMercadosEIEL()).getRelacionFields()), (new LonjasMercadosEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.MATADEROS_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.MATADEROS_CLAVE, ConstantesLocalGISEIEL.MATADEROS, Constants.CLASSID_MATADEROS, getCamposCapaEIEL(ConstantesLocalGISEIEL.MATADEROS, locale, Constants.CLASSID_MATADEROS,(new MataderosEIEL()).getRelacionFields()), (new MataderosEIEL()).getRelacionFields()));
//MUNICIPIO	
//			else if(layerName.equals(ConstantesLocalGISEIEL.MUNICIPIO_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.MATADEROS_CLAVE, Constants.CLASSID_MUNICIPIO, getCamposCapaEIEL(layerName, locale, Constants.CLASSID_MUNICIPIO,(new MunicipioEIEL()).getRelacionFields()), (new MunicipioEIEL()).getRelacionFields()));
//NUCLEO ENCUESTADO7
//			else if(layerName.equals(ConstantesLocalGISEIEL.NUCLEO_ENCT_7_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.NUCLEO_ENCT_7_CLAVE, ConstantesLocalGISEIEL.NUCLEO_ENCT_7, Constants.CLASSID_NUCLEOENCUESTADO7, getCamposCapaEIEL(ConstantesLocalGISEIEL.NUCLEO_ENCT_7, locale, Constants.CLASSID_MUNICIPIO,(new NucleoEncuestado7EIEL()).getRelacionFields()), (new NucleoEncuestado7EIEL()).getRelacionFields()));
//NUCLEOS ABANDONADOS
//			else if(layerName.equals(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS_CLAVE, ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS, Constants.CLASSID_NUCLEOS_ABANDONADOS, getCamposCapaEIEL(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS, locale, Constants.CLASSID_NUCLEOS_ABANDONADOS,(new NucleosAbandonadosEIEL()).getRelacionFields()), (new NucleosAbandonadosEIEL()).getRelacionFields()));
//NUCLEO POBLACION			
			//else if(layerName.equals(ConstantesLocalGISEIEL.NUCLEOS_POBLACION_LAYER))
			//	eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.NUCLEOS_POBLACION_CLAVE, ConstantesLocalGISEIEL.NUCLEOS_POBLACION, Constants.CLASSID_NUCLEOS_POBLACION, getCamposCapaEIEL(ConstantesLocalGISEIEL.NUCLEOS_POBLACION, locale, Constants.CLASSID_NUCLEOS_POBLACION,(new NucleosPoblacionEIEL()).getRelacionFields()), (new NucleosPoblacionEIEL()).getRelacionFields()));
//OTROS SERVICIOS MUNICIPALES
//			else if(layerName.equals(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES_CLAVE, ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES, Constants.CLASSID_OTROS_SERVICIOS_MUNICIPALES, getCamposCapaEIEL(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES, locale, Constants.CLASSID_OTROS_SERVICIOS_MUNICIPALES,(new OtrosServMunicipalesEIEL()).getRelacionFields()), (new OtrosServMunicipalesEIEL()).getRelacionFields()));
//PADRON MUNICIPIOS		
//			else if(layerName.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS_CLAVE, ConstantesLocalGISEIEL.PADRON_MUNICIPIOS, Constants.CLASSID_PADRON_MUNICIPIOS, getCamposCapaEIEL(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS, locale, Constants.CLASSID_PADRON_MUNICIPIOS,(new PadronMunicipiosEIEL()).getRelacionFields()), (new PadronMunicipiosEIEL()).getRelacionFields()));	
//PADRON NUECLEOS
//			else if(layerName.equals(ConstantesLocalGISEIEL.PADRON_NUCLEOS_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.PADRON_NUCLEOS_CLAVE, ConstantesLocalGISEIEL.PADRON_NUCLEOS, Constants.CLASSID_PADRON_NUCLEOS, getCamposCapaEIEL(ConstantesLocalGISEIEL.PADRON_NUCLEOS, locale, Constants.CLASSID_PADRON_NUCLEOS,(new PadronNucleosEIEL()).getRelacionFields()), (new PadronNucleosEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.PARQUESJARDINES_CLAVE, ConstantesLocalGISEIEL.PARQUES_JARDINES, Constants.CLASSID_PARQUESJARDINES, getCamposCapaEIEL(ConstantesLocalGISEIEL.PARQUES_JARDINES, locale, Constants.CLASSID_PARQUESJARDINES,(new ParquesJardinesEIEL()).getRelacionFields()), (new ParquesJardinesEIEL()).getRelacionFields()));
//PLANEAMIENTO URBANO
//			else if(layerName.equals(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO_CLAVE, ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO, Constants.CLASSID_PLANEAMIENTO_URBANO, getCamposCapaEIEL(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO, locale, Constants.CLASSID_PLANEAMIENTO_URBANO,(new PlaneamientoUrbanoEIEL()).getRelacionFields()), (new PlaneamientoUrbanoEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.PUNTOSDEVERTIDO_CLAVE, ConstantesLocalGISEIEL.PUNTOS_VERTIDO, Constants.CLASSID_PUNTOSDEVERTIDO, getCamposCapaEIEL(ConstantesLocalGISEIEL.PUNTOS_VERTIDO, locale, Constants.CLASSID_PUNTOSDEVERTIDO,(new PuntosVertidoEIEL()).getRelacionFields()), (new PuntosVertidoEIEL()).getRelacionFields()));
//RECOGIDA BASURAS (NP)
//			else if(layerName.equals(ConstantesLocalGISEIEL.RECOGIDA_BASURAS_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.RECOGIDABASURA_CLAVE, ConstantesLocalGISEIEL.RECOGIDA_BASURAS, Constants.CLASSID_RECOGIDABASURA, getCamposCapaEIEL(ConstantesLocalGISEIEL.RECOGIDA_BASURAS, locale, Constants.CLASSID_RECOGIDABASURA,(new RecogidaBasurasEIEL()).getRelacionFields()), (new RecogidaBasurasEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.SANEAMIENTOAUTONOMO_CLAVE, ConstantesLocalGISEIEL.SANEAMIENTO, Constants.CLASSID_SANEAMIENTOAUTONOMO, getCamposCapaEIEL(ConstantesLocalGISEIEL.SANEAMIENTO, locale, Constants.CLASSID_SANEAMIENTOAUTONOMO,(new SaneamientoAutonomoEIEL()).getRelacionFields()), (new SaneamientoAutonomoEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.SERVICIOS_ABASTECIMIENTOS_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.SERVICIOS_ABASTECIMIENTOS_CLAVE, ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS, Constants.CLASSID_SERVICIOS_ABASTECIMIENTOS, getCamposCapaEIEL(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS, locale, Constants.CLASSID_SERVICIOS_ABASTECIMIENTOS,(new ServiciosAbastecimientosEIEL()).getRelacionFields()), (new ServiciosAbastecimientosEIEL()).getRelacionFields()));
//			else if(layerName.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA_LAYER))
//				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA_CLAVE, ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA, Constants.CLASSID_SERVICIOS_RECOGIDA_BASURA, getCamposCapaEIEL(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA, locale, Constants.CLASSID_SERVICIOS_RECOGIDA_BASURA,(new ServiciosRecogidaBasuraEIEL()).getRelacionFields()), (new ServiciosRecogidaBasuraEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.SERVICIOS_SANEAMIENTO_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.SERVICIOS_SANEAMIENTO_CLAVE, ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO, Constants.CLASSID_SERVICIOS_SANEAMIENTO, getCamposCapaEIEL(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO, locale, Constants.CLASSID_SERVICIOS_SANEAMIENTO,(new ServiciosSaneamientoEIEL()).getRelacionFields()), (new ServiciosSaneamientoEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.TANATORIO_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.TANATORIOS_CLAVE, ConstantesLocalGISEIEL.TANATORIOS, Constants.CLASSID_TANATORIOS, getCamposCapaEIEL(ConstantesLocalGISEIEL.TANATORIOS, locale, Constants.CLASSID_TANATORIOS,(new TanatoriosEIEL()).getRelacionFields()), (new TanatoriosEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.CARRETERA_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.TRAMOSCARRETERAS_CLAVE, ConstantesLocalGISEIEL.TRAMOS_CARRETERAS, Constants.CLASSID_TRAMOS_CARRETERAS, getCamposCapaEIEL(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS, locale, Constants.CLASSID_TRAMOS_CARRETERAS,(new TramosCarreterasEIEL()).getRelacionFields()), (new TramosCarreterasEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.CONDUCCION_CLAVE, ConstantesLocalGISEIEL.TCONDUCCION, Constants.CLASSID_TRAMOS_CONDUCCION, getCamposCapaEIEL(ConstantesLocalGISEIEL.TCONDUCCION, locale, Constants.CLASSID_TRAMOS_CONDUCCION,(new TramosConduccionEIEL()).getRelacionFields()), (new TramosConduccionEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.TRATAMIENTOSPOTABILIZACION_CLAVE, ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION, Constants.CLASSID_TRATAMIENTOSPOTABILIZACION, getCamposCapaEIEL(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION, locale, Constants.CLASSID_TRATAMIENTOSPOTABILIZACION,(new TratamientosPotabilizacionEIEL()).getRelacionFields()), (new TratamientosPotabilizacionEIEL()).getRelacionFields()));
			else if(layerName.equals(ConstantesLocalGISEIEL.VERTEDERO_LAYER))
				eielLayerBeans.put(layerName, new EIELLayerBean(ConstantesLocalGISEIEL.VERTEDEROS_CLAVE, ConstantesLocalGISEIEL.DATOS_VERTEDEROS , Constants.CLASSID_VERTEDEROS, getCamposCapaEIEL(ConstantesLocalGISEIEL.DATOS_VERTEDEROS, locale, Constants.CLASSID_VERTEDEROS,(new VertederosEIEL()).getRelacionFields()), (new VertederosEIEL()).getRelacionFields()));
		}		
	}	
	
	public EIELLayerBean getEIELLayerBean(String layerName){
		return eielLayerBeans.get(layerName);
	}
	
	public String getCamposCapaEIEL(String nodo, String locale, String eielBeanType, ArrayList<LCGCampoCapaTablaEIEL> relacionFields){
		String skeleton ="";
		Collection<Object> c=null;
        try {
			c = eielClient.getCamposCapaEIEL(nodo, locale);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        if(c!=null && c.size()>0){
        	String groupLabel = "";
        	String tabContent = "";
			skeleton += "<skeleton>\n";
			Iterator it = c.iterator();
			while(it.hasNext()){
				LCGCampoCapaEIEL lcgCampoCapa = (LCGCampoCapaEIEL) it.next();
				if(lcgCampoCapa.isAplicaMovilidad()){
					if(!groupLabel.equals(lcgCampoCapa.getTraduccionGrupo())){
						if(!groupLabel.equals("") && !tabContent.equals("")){
							skeleton=skeleton.replace("#TAB_CONTENT#", tabContent);
							tabContent="";
						}
						skeleton += "<tab label=\"" + lcgCampoCapa.getTraduccionGrupo() + "\" classId=\"" + eielBeanType + "\">\n#TAB_CONTENT#</tab>\n";
						groupLabel = lcgCampoCapa.getTraduccionGrupo();
					}
		        	String domain = "";
					if(lcgCampoCapa.getDominio()!=null)
						domain = lcgCampoCapa.getDominio();
					tabContent += "\u0009\u0009<item rm=\"" + lcgCampoCapa.getCampoBD() + "\" label=\"" + lcgCampoCapa.getTraduccion() + "\" name=\"" + domain + "\"";
					if(!Utils.isInArray(getRelationFieldsCampoBD(relacionFields), lcgCampoCapa.getCampoBD()))
						tabContent +=" edit=\"true\"";
					else
						tabContent +=" edit=\"false\"";						
					if(!domain.equals(""))
						tabContent += " modif=\"true\" ";
					tabContent += "/>\n";
					if(lcgCampoCapa.getMetodo()!=null) 
						reflectionMethods.put(eielBeanType + "." + lcgCampoCapa.getCampoBD(), lcgCampoCapa.getMetodo());
				}
        	}
			if(!groupLabel.equals("") && !tabContent.equals("")){
				skeleton=skeleton.replace("#TAB_CONTENT#", tabContent);
				tabContent="";
			}
			skeleton += "</skeleton>\n";	
	    }
	    System.out.println(skeleton);    
        return skeleton;       
    }
	
	public Object[] getRelationFieldsCampoBD(ArrayList<LCGCampoCapaTablaEIEL> relacionFields){
		ArrayList<String> relacionFieldsCampoBD = new ArrayList<String>();
		Iterator it = relacionFields.iterator();	
		while(it.hasNext()){
			LCGCampoCapaTablaEIEL lcgCampoCapa = (LCGCampoCapaTablaEIEL) it.next();
			relacionFieldsCampoBD.add(lcgCampoCapa.getCampoBD());
		}
		return relacionFieldsCampoBD.toArray();
	}
	
	/**
	 * Crea los ficheros de eiel necesarios para la pda
	 * @param dirBaseMake 
	 * @param capasMetadata 
	 * @param hashSvgCellsHeader 
	 */
	public List<File> createEIELFilesSkeleton(List<File> ficherosSVG, List<String> capasMetadata, 
			HashMap<File, String> hashSvgCellsHeader, Map<String, List<EIELMetadataSVG>> parseEIEL){
		Iterator<File> fileIt = ficherosSVG.iterator();
		File eielFile = null;
		File svgCellFile = null;
		List<File> listaFicherosEIELCreados = new ArrayList<File>();
		String fileContent = null;
		FileOutputStream fOut = null;
		String metadataLayerName = null;
		String eielFileName = null;
		String eielSkeletonFileName = null;
		EIELMetadataSVG eielSVG = null;
		while (fileIt.hasNext()) {
			svgCellFile = (File) fileIt.next();
			eielFileName = svgCellFile.getAbsolutePath();			
			for (int j = 0; j < capasMetadata.size(); j++) {
				metadataLayerName = capasMetadata.get(j);
				eielSkeletonFileName = eielFileName.substring(0, eielFileName.length()-4) + metadataLayerName 
							+ "_meta0.svg"; //creamos los nuevos
				eielFile = new File(eielSkeletonFileName);
				if(!eielFile.exists()){
					try {
						//creación del skeleton
						eielSVG = searchElement(parseEIEL, metadataLayerName);							
						if (eielSVG!=null){
							eielFile.createNewFile();
							fileContent = createSkeleton(hashSvgCellsHeader.get(svgCellFile), eielSVG.getGrupo(), eielSVG.getPath(), getSkeleton(eielSVG.getNombreMetadato()));
							fOut = new FileOutputStream(eielFile);
							fOut.write(fileContent.getBytes("UTF-8"));
							fOut.close();
						}
						else{
							eielFile.createNewFile();
							fOut = new FileOutputStream(eielFile);
							fOut.close();
						}
						//añadimos el fichero a la lista de creados
						listaFicherosEIELCreados.add(eielFile);							
					} catch (IOException e) {
						logger.error("No se ha podido crear el fichero de eiel: " + eielFile);
					}
				}
			}
		}
			
		return listaFicherosEIELCreados;
	}

	private EIELMetadataSVG searchElement(Map<String, List<EIELMetadataSVG>> parseElement, String metadataLayerName) {
		for (Iterator iterator = parseElement.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			if(key.contains(metadataLayerName)){
				return parseElement.get(key).get(0);
			}			
		}
		return null;
	}

	public void fillEIELFiles(Map<String, List<EIELMetadataSVG>> parseEIEL, File dirBaseMake) {
		try {			
			Iterator<String> keyIt = parseEIEL.keySet().iterator();
			String ficheroEIEL = null;
			File f = null;
			List<EIELMetadataSVG> listEIEL = null;
			while (keyIt.hasNext()) {
				ficheroEIEL = (String) keyIt.next();
				//si esta vacío metemos el eskeleton
				f = new File(dirBaseMake, ficheroEIEL);
				listEIEL = parseEIEL.get(ficheroEIEL);
				fillXmlFile(f, listEIEL);
			}			
			
		}catch (Exception e) {
			logger.error(e,e);
		}
	}

	private void fillXmlFile(File f, List<EIELMetadataSVG> listEIEL) throws JDOMException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String fileContent = "";
		
		if(listEIEL==null || listEIEL.size()==0){return;}
				
		EIELMetadataSVG eielSVG = null;
		InputStream skeletonInput = null;
		int i=0;
		String path = "";
		List eielList = null;
		Object eielElement = null;
		for (Iterator iterator = listEIEL.iterator(); iterator.hasNext(); i++) {
			skeletonInput = getSkeletonInput(listEIEL.get(i).getNombreMetadato());
			eielSVG = (EIELMetadataSVG) iterator.next();
			path = eielSVG.getPath();
			
			//cada uno de los elementos eiel
			eielList = eielSVG.getEIEL();
			for (Iterator iterator2 = eielList.iterator(); iterator2.hasNext();) {
				eielElement = iterator2.next();
				fileContent += path.replace("/>", ">") + "\n";
				fileContent += fillFeatureSkeleton(eielElement, skeletonInput);
				if(path.toLowerCase().startsWith("<path")){
					fileContent += "</path>\n";
				}else if(path.toLowerCase().startsWith("<polyline")){
					fileContent += "</polyline>\n";
				}else if(path.toLowerCase().startsWith("<line")){
					fileContent += "</line>\n";
				}else if(path.toLowerCase().startsWith("<point")){
					fileContent += "</point>\n";
				}else if(path.toLowerCase().startsWith("<circle")){
					fileContent += "</circle>\n";
				}
				skeletonInput.reset();
			}			
		}
		fileContent += "</g></svg>";
		
		InputStream fin = null;
		InputStreamReader inread = null;
		BufferedReader buffread = null;
		OutputStream fout = null;
		try {
			fin = new FileInputStream(f);
			inread = new InputStreamReader(fin, "UTF-8");
			buffread = new BufferedReader(inread);
			String strLine = null;
			String finalStr = "";
			while((strLine = buffread.readLine()) != null){
				if(strLine.equals("</g></svg>")){
					break;
				}
				finalStr += strLine + "\n";
			}
			finalStr += fileContent;
			
			fout = new FileOutputStream(f);
			fout.write(finalStr.getBytes("UTF-8"));
			
		}catch (Exception e) {
			logger.error(e,e);
		}finally {
			try{
				buffread.close();
				inread.close();
				fin.close();
				fout.close();
			}catch (Exception e) {}
		}
	}

	private String fillFeatureSkeleton(Object eielElement, InputStream skeletonInput) throws JDOMException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(skeletonInput);
		Element rootElement = doc.getRootElement();	
		List children = rootElement.getChildren();
		Element elem = null;
		List items = null;
		List itemLists = null;
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			elem = (Element) iterator.next();
			items = elem.getChildren(Constants.TAG_ITEM);
			addDBValues(eielElement, items);
			itemLists = elem.getChildren(Constants.TAG_ITEMLIST);
			addDBValues(eielElement, itemLists);
		}		
		
		String str = "<metadata>\n<![CDATA[\n";
		str+=printDoc(doc);
		str+="]]>\n</metadata>\n";
		return str;
	}

	private void addDBValues(Object eielElement, List items) {
		Element elem = null;
		String strMethod = null;
		String parentClassID = null;
		boolean isEIELBean = false;
		Iterator iterator = items.iterator(); 
		while(iterator.hasNext()) {
			elem = (Element) iterator.next();
			parentClassID = elem.getParentElement().getAttribute(Constants.ATT_CLASSID).getValue();
			if(Utils.isInArray(Constants.TIPOS_CLASSID_EIEL, parentClassID)){
				strMethod = reflectionMethods.get(parentClassID + "." + elem.getAttribute(Constants.ATT_REFLECTMETHOD).getValue());
				if(strMethod!=null && strMethod.length()>0){
					strMethod = "get" + strMethod;
					isEIELBean=true;					
				}
			}	
				
			if(isEIELBean) reflectXmlMethod(elem, strMethod, eielElement);	
				
		}
	}

	private void reflectXmlMethod(Element elem, String strMethod, Object objectInvoke)  {
		try {
			if(objectInvoke==null){return;}
			if(strMethod!=null){
				Method method = objectInvoke.getClass().getMethod(strMethod, new Class[0]);
				String itemValue = null; 
				Object tempValue = null;
				List<String> itemListValues = null;
				String strValue = null;
				if(method!=null){
					if(elem.getName().toLowerCase().equals(Constants.TAG_ITEM)){
						tempValue = method.invoke(objectInvoke);
						if(tempValue==null)		
							tempValue = "";
						elem.setText(tempValue.toString());
					}
					else if(elem.getName().toLowerCase().equals(Constants.TAG_ITEMLIST)){
						itemListValues = (List<String>) method.invoke(objectInvoke);
						for (Iterator iterator = itemListValues.iterator(); iterator.hasNext();) {
							strValue = (String) iterator.next();
							elem.addContent(new Element(Constants.TAG_SUBITEM).setText(strValue));
						}
					}
				}
			}			
		}catch (Exception e) {
			logger.error("No se ha podido acceder al método "+objectInvoke.getClass()+"." + strMethod, e);
		}
	}


	
	private String getSkeleton(String layerName){
			return eielLayerBeans.get(layerName).getSkeleton();
	}

	public InputStream getSkeletonInput(String layerName) throws UnsupportedEncodingException{
		return new ByteArrayInputStream(getSkeleton(layerName).getBytes("UTF-8"));
	}

	private void initResourceBundle(String locale) {
        try
        {
			   try {
				   ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",new Locale(locale),this.getClass().getClassLoader());
				   I18N.plugInsResourceBundle.put("LocalGISEIEL", bundle);
               }catch (Exception e)
               {
                   locale=ConstantesLocalGISEIEL.LocalCastellano;
                   ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",new Locale(locale),this.getClass().getClassLoader());
				   I18N.plugInsResourceBundle.put("LocalGISEIEL", bundle);
               }
           
        } catch (Exception e) {
            logger.error("No se encuentra un resource para el locale: " + locale);
        } 
	}

	public HashMap<String, String> getReflectionMethods() {
		return reflectionMethods;
	}
		
}
