/**
 * GetFeatureReport.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.reports;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.geopista.app.eiel.reports.UtilsReport;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.model.GeopistaMunicipio;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;


public class GetFeatureReport {

	private static final Logger logger=Logger.getLogger(GetFeatureReport.class);

	
	public static void mostrarInformes(Boolean publicMap, StringBuffer cadena,
			String layername, String idFeature, String x, String y,String srid,String idEntidad,String locale) {
		
		
		
			try {
				if (!publicMap){
					String geometrySelect= "POINT("+x+" "+y+")";    	
					String idMunicipio=getIdMunicipio(srid,geometrySelect);
	       
	       
	       			Collection listPlantillas=getPlantillasInformes(layername,idEntidad,idMunicipio);
	       			Iterator it=listPlantillas.iterator();
	       			
	       			String nombresInformes = "[";
					while (it.hasNext()){
						Object[] obj=(Object[])it.next();
						nombresInformes+="'"+obj[0]+"',";
						//System.out.println("Plantilla:"+obj[0]+"->"+obj[1]);
					}
					if (listPlantillas.size()>0)
						nombresInformes=nombresInformes.substring(0,nombresInformes.length()-1)+"]";
					else
						nombresInformes=nombresInformes+"]";
	
					//String nombresInformes = "['Bob','Tom','Harry']";
					if (listPlantillas.size()>0){
						String cadenaReport="<td><img border=0 witdh=\"20px\" height=\"20px\" src=\"img/report.jpg\" onclick=\"generateReport('"+layername+"','"+idFeature+"','"+idEntidad+"','"+idMunicipio+"','"+locale+"',"+nombresInformes+")\";></td>";
						cadena.append(cadenaReport);
					}
				}
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	private static Collection getPlantillasInformes(String layername, String idEntidad, String idMunicipio){
	
		ArrayList plantillas= new ArrayList();
		try {
			//En el caso de que la capa tenga la cadena "TC" la eliminamos para buscar la categoria
			//asignada. Por ejemplo CC_TC se quedaria como CC y su categoria asignada es la EQ.
			String layerNameShorted=null;
			if (layername.contains("TC")){
				layerNameShorted=layername.substring(0,layername.indexOf("_TC"));
			}
			
			String categoria=null;
			if (layerNameShorted!=null){
				String layerBusqueda=layerNameShorted;
				if (layerNameShorted.equals("TU"))
					layerNameShorted="IV";
				else if (layerNameShorted.equals("TEM"))
					layerNameShorted="EM";
				else if (layerNameShorted.equals("TCL"))
					layerNameShorted="CL";
				else if (layerNameShorted.equals("TCN"))
					layerNameShorted="CN";
				else if (layerNameShorted.equals("carreteras"))
					layerNameShorted="TC";				
				else if (layername.equals("PL"))
					layerNameShorted="ALUM";
				else if (layerNameShorted.equals("TN"))
					layerNameShorted="TA";
				else if (layerNameShorted.equals("PL"))
					layerNameShorted="ALUM";
				categoria=getCategoryByLayerName(layerNameShorted);
			}
			else{
				String layerBusqueda=layername;
				if (layername.equals("TU"))
					layername="IV";
				else if (layername.equals("TEM"))
					layername="EM";
				else if (layername.equals("TCL"))
					layername="CL";
				else if (layername.equals("TCN"))
					layername="CN";
				else if (layername.equals("carreteras"))
					layername="TC";
				else if (layername.equals("TN"))
					layername="TA";
				else if (layername.equals("PL"))
					layername="ALUM";

				categoria=getCategoryByLayerName(layername);
			}
			String directorioPlantillasGenericas=Configuration.getPropertyString("reports.plantillas.genericas");
			String directorioPlantillasEntidad=Configuration.getPropertyString("reports.plantillas.entidad")+File.separator+idEntidad;
			   
			
			UtilsReport.getNombresPlantillas(directorioPlantillasGenericas,layerNameShorted,null,null,plantillas);
			UtilsReport.getNombresPlantillas(directorioPlantillasGenericas,layername,categoria,null,plantillas);
			
			UtilsReport.getNombresPlantillas(directorioPlantillasEntidad,layerNameShorted,null,null,plantillas,idEntidad);
			UtilsReport.getNombresPlantillas(directorioPlantillasEntidad,layername,categoria,null,plantillas,idEntidad);
		
			//System.out.println("Plantillas:");
			Iterator it=plantillas.iterator();
			while (it.hasNext()){
				Object[] obj=(Object[])it.next();
				//System.out.println("Plantilla:"+obj[0]+"->"+obj[1]);
			}
		
		
			
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return plantillas;
	}
	

 /**
     * Obtiene el identificador de municipio
     * @param srid
     * @param geometrySelect
     * @return
     */
    private static String getIdMunicipio(String srid,String geometrySelect){
    	String idMunicipio=null;
		try {
			LocalgisMapManager localgisMapManager=LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapManager();

			GeopistaMunicipio municipio=localgisMapManager.selectMunicipioByGeometry(srid,geometrySelect);
			
			
			//DaoManager daoManager=LocalgisManagerBuilderFactory.getDaoManager();
			//LocalgisMunicipioDAO localgisMunicipioDAO=(LocalgisMunicipioDAO) daoManager.getDao(LocalgisMunicipioDAO.class);
			//Recuperar idMunicpio
			//GeopistaMunicipio municipio = (GeopistaMunicipio) localgisMunicipioDAO.selectMunicipioByGeometry(srid, geometrySelect);
			if (municipio!=null) {
				idMunicipio=String.valueOf(municipio.getId());
			}else{
				logger.error("No se ha podido obtener el codigo de municipio:"+idMunicipio);
			}
		} catch (LocalgisConfigurationException e) {
		} catch (LocalgisInitiationException e) {
		}
		catch (Exception e){	
			e.printStackTrace();
		}
		
        return idMunicipio;
    }
    
     private static String getCategoryByLayerName(String layerName){
    	String category=null;
		try {
			LocalgisMapsConfigurationManager localgisMapsConfigurationManager=LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
			category=localgisMapsConfigurationManager.getCategoryByLayerName(layerName);
						
			//DaoManager daoManager=LocalgisManagerBuilderFactory.getDaoManager();
			//GeopistaLayerDAO geopistaLayerDAO=(GeopistaLayerDAO) daoManager.getDao(GeopistaLayerDAO.class);
			//Recuperar idMunicpio
			//category = geopistaLayerDAO.getCategoryByLayerName(layerName);
		} catch (LocalgisConfigurationException e) {
		} catch (LocalgisInitiationException e) {
		}
		catch (Exception e){	
			e.printStackTrace();
		}
		
        return category;
    }
     
     public static void main(String args[]){
    		
 		String directorio="c:\\Satec\\Proyectos\\LOCALGIS\\LOCALGIS.MODELO.EIEL\\3Codigo\\eielmodelo\\classes\\plantillas\\";
 		ArrayList plantillas= new ArrayList();
 		//String plantillaBusqueda="EIEL_equipamientos_EQ.jrxml";
 		String plantillaBusqueda=null;
 		String filtro="municipios";
 		String categoria="EQ";
 		categoria=null;
 		UtilsReport.getNombresPlantillas(directorio,filtro,categoria,plantillaBusqueda,plantillas);
 		
 		directorio="c:\\Program Files\\LocalGIS\\plantillas.entidad\\";
 		UtilsReport.getNombresPlantillas(directorio,filtro,categoria,plantillaBusqueda,plantillas);
 		
 		System.out.println("Plantillas:");
 		Iterator it=plantillas.iterator();
 		while (it.hasNext()){
 			Object[] obj=(Object[])it.next();
 			System.out.println("Plantilla:"+obj[0]+"->"+obj[1]);
 		}

 	}
}
