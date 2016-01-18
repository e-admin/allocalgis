/**
 * WFSGService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.dwr;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.model.GeopistaMunicipio;
import com.localgis.web.gwfst.beans.PlaceNameInfoService;
import com.localgis.web.gwfst.config.LocalgisWebConfiguration;
import com.localgis.web.core.wm.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.gwfst.util.WFSGUtils;
import com.localgis.web.wfsg.LocalGIS_WFSG;
import com.localgis.web.wfsg.LocalGIS_WFSG_Resultados;
import com.localgis.web.wfsg.domain.ElementEntity;
import com.localgis.web.wfsg.domain.Entity;
import com.localgis.web.wfsg.exceptions.ConnectionException;
import com.localgis.web.wfsg.exceptions.IncorrectArgumentException;
import com.localgis.web.wfsg.exceptions.InternalException;
import com.localgis.web.wfsg.exceptions.NoDataFoundException;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Servicio para la recuperación de información
 */
public class WFSGService {

	/**
	 * Logger
	 */
    private static Log logger = LogFactory.getLog(WFSGService.class);
    
    /**
     * Constantes
     */
    private static final String SERVICE_WFS_MNE_STR = "WFS-MNE";
    private static final String SERVICE_WFS_G_STR = "WFS-G";
    
    /**
     * Método para obtener la lista de servicios de busqueda de topónimos
     * @return List: Una lista de servicios como objetos PlaceNameInfoService
     * @throws LocalgisConfigurationException
     */
    public List getPlaceNameServices() throws LocalgisConfigurationException {
        try {
            String placeNameInfoServices = LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_PLACE_NAME_INFO_SERVICES);
            String[] services = placeNameInfoServices.split("#");
            List servicesList = new ArrayList();
            if (services != null) {
                for (int i = 0; i < services.length; i++) {
                    String serviceStr = services[i];
                    String[] serviceProperties = serviceStr.split(";");
                    if (serviceProperties.length == 4) {
                        PlaceNameInfoService service = new PlaceNameInfoService();
                        service.setName(serviceProperties[0]);
                        if (serviceProperties[1].equals(SERVICE_WFS_MNE_STR)) {
                            service.setType(PlaceNameInfoService.SERVICE_WFS_MNE);
                        } else if (serviceProperties[1].equals(SERVICE_WFS_G_STR)) {
                            service.setType(PlaceNameInfoService.SERVICE_WFS_G);
                        } else {
                            service.setType(PlaceNameInfoService.SERVICE_WFS_MNE);
                        }
                        service.setFeatureType(serviceProperties[2]);
                        service.setService(serviceProperties[3]);
                        servicesList.add(service);
                    }
                }
            }
            return servicesList;
        } catch (LocalgisConfigurationException e) {
            logger.error("Error al consultar los servicios de busqueda de toponimos", e);
            throw e;
        }
    }   
    
    /**
     * Metodo para realizar una búsqueda de un topónimo
     * @param service:
     * @param query:
     * @param municipio:
     * @param targetCRSCode:
     * @return List:
     * @throws IncorrectArgumentException
     * @throws NoDataFoundException
     * @throws ConnectionException
     * @throws InternalException
     */
    public List getEntitiesWFS(String service, int serviceType, String featureType, String query, Integer idEntidad, String targetCRSCode, Boolean withoutSpacialRestriction) throws IncorrectArgumentException, NoDataFoundException, ConnectionException, InternalException {
        if (serviceType == PlaceNameInfoService.SERVICE_WFS_MNE) {
            return getEntitiesWfsMne(service, query, idEntidad, targetCRSCode, withoutSpacialRestriction);
        } else {
            return getEntitiesWfsG(service, featureType, query, idEntidad,targetCRSCode);
        }        
    }
    
    /**
     * 
     * @param service:
     * @param query:
     * @param idEntidad:
     * @param targetCRSCode:
     * @param withoutSpacialRestriction:
     * @return List:
     * @throws IncorrectArgumentException
     * @throws NoDataFoundException
     * @throws ConnectionException
     * @throws InternalException
     */
    private List getEntitiesWfsMne(String service, String query, Integer idEntidad, String targetCRSCode, Boolean withoutSpacialRestriction) throws IncorrectArgumentException, NoDataFoundException, ConnectionException, InternalException {
        try {
            LocalGIS_WFSG_Resultados results;
            Hashtable hQuery=new Hashtable();
            hQuery.put("nombreEntidad/nombre",query);
            List entityElements = new ArrayList();
            if (!withoutSpacialRestriction.booleanValue()) {
                /*
                 * Si hay restriccion espacial hay que iterar sobre cada uno
                 * de los municipios que hay en la entidad y hacer una
                 * consulta al IDEE, al menos de momento
                 */
                try {
                    LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisEntidadSupramunicipalManager();
                    List municipios = localgisEntidadSupramunicipalManager.getMunicipiosByIdEntidad(idEntidad);
                    Iterator iterator = municipios.iterator();
                    while (iterator.hasNext()) {
                        GeopistaMunicipio municipio = (GeopistaMunicipio) iterator.next();
                        hQuery.put("entidadLocal/municipio", municipio.getNombreoficial());
                        results = LocalGIS_WFSG.getPlaceNameInformation(service, hQuery);
                        entityElements.addAll(getEntityElements(results, targetCRSCode));   
                    }
                } catch (LocalgisInitiationException e) {
                    logger.error("El cliente de LocalGIS Core esta mal inicializado. La lista de entidades se devuelve vacia.",e);
                } catch (LocalgisConfigurationException e) {
                    logger.error("El cliente de LocalGIS Core esta mal configurado. La lista de entidades se devuelve vacia.",e);
                } catch (LocalgisDBException e) {
                    logger.error("Error de base de datos al obtener los municipios de la entidad. La lista de entidades se devuelve vacia.",e);
                }
                
            } else {
                results = LocalGIS_WFSG.getPlaceNameInformation(service, hQuery);
                entityElements.addAll(getEntityElements(results, targetCRSCode));
            }
            return entityElements;
        } catch (IncorrectArgumentException e) {
            logger.error("Error al consultar por toponimos", e);
            throw e;
        } catch (NoDataFoundException e) {
            logger.error("Error al consultar por toponimos", e);
            throw e;
        } catch (ConnectionException e) {
            logger.error("Error al consultar por toponimos", e);
            throw e;
        } catch (InternalException e) {
            logger.error("Error al consultar por toponimos", e);
            throw e;
        }        

    }
    
    /**
     * 
     * @param service:
     * @param featureType:
     * @param query:
     * @param idEntidad:
     * @param targetCRSCode:
     * @return List:
     * @throws IncorrectArgumentException
     * @throws NoDataFoundException
     * @throws ConnectionException
     * @throws InternalException
     */
    private List getEntitiesWfsG(String service, String featureType, String query, Integer idEntidad,String targetCRSCode) throws IncorrectArgumentException, NoDataFoundException, ConnectionException, InternalException {
        try {
            LocalGIS_WFSG_Resultados results;
            Hashtable hQuery=new Hashtable();
            hQuery.put("app:geographicIdentifier",query.toLowerCase());
            hQuery.put("app:entidadIdentifier",idEntidad.intValue());
            results = LocalGIS_WFSG.getStandardWFSGInformation(service, hQuery, "app:"+featureType);
            List entityElements = getEntityElements(results, targetCRSCode);
            return entityElements;
        } catch (IncorrectArgumentException e) {
            logger.error("Error al consultar por toponimos", e);
            throw e;
        } catch (NoDataFoundException e) {
            logger.error("Error al consultar por toponimos", e);
            throw e;
        } catch (ConnectionException e) {
            logger.error("Error al consultar por toponimos", e);
            throw e;
        } catch (InternalException e) {
            logger.error("Error al consultar por toponimos", e);
            throw e;
        }        
    }
    
    /**
     * 
     * @param results: 
     * @param targetCRSCode: 
     * @return List:
     */
    private List getEntityElements(LocalGIS_WFSG_Resultados results, String targetCRSCode) {
        Entity entity = results.getEntityReference();
        //String sourceCRSCode = "EPSG:"+results.getSRS();
        String sourceCRSCode = results.getSRS();
        List entityElements = entity.getElements();
        for (Iterator iterator = entityElements.iterator(); iterator.hasNext();) {
            ElementEntity item = (ElementEntity) iterator.next();
            double posX  = Double.parseDouble(item.getPosX());
            double posY  = Double.parseDouble(item.getPosY());
            double[] transformedCoordinates = WFSGUtils.transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {posX,posY});
            item.setPosX(String.valueOf(transformedCoordinates[0]));
            item.setPosY(String.valueOf(transformedCoordinates[1]));
        }

        return entityElements;
    }
    
    /**
     * Método para realizar la búsqueda de calles
     * @param service: 
     * @param streetName: Nombre de la calle
     * @param streetNumber: Número de calle
     * @param idEntidad: Identificador de entidad (Municipio)
     * @param targetCRSCode: 
     * @return List: Lista con las calles coincidentes
     * @throws IncorrectArgumentException
     * @throws NoDataFoundException
     * @throws ConnectionException
     * @throws InternalException
     * @throws LocalgisDBException 
     * @throws LocalgisInitiationException 
     * @throws LocalgisConfigurationException 
     */
    public List getStreetInformation(String service, String streetName, Integer streetNumber, String idEntidad, String targetCRSCode) throws IncorrectArgumentException, NoDataFoundException, ConnectionException, InternalException, LocalgisDBException, LocalgisInitiationException, LocalgisConfigurationException {
        try {
            LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisEntidadSupramunicipalManager();
            List municipios = localgisEntidadSupramunicipalManager.getMunicipiosByIdEntidad(Integer.parseInt(idEntidad));
            Iterator iterator = municipios.iterator();
            ArrayList listaElementos=new ArrayList();
            while (iterator.hasNext()) {
        	
            	GeopistaMunicipio municipio = (GeopistaMunicipio) iterator.next();
            	
	            Hashtable hQuery=new Hashtable();
	            hQuery.put("nombreEntidad_nombre",streetName);
	            
	            // Para que busque las calles solo del municipio seleccionado:
	            hQuery.put("entidadLocal_municipio",municipio.getIdProvincia()+municipio.getIdIne());
            
            
	            LocalGIS_WFSG_Resultados results = LocalGIS_WFSG.getStreetInformation(service, hQuery, streetNumber.toString());
	            Entity entity = results.getEntityReference();
	            String sourceCRSCode = results.getSRS();
	            List entityElements = entity.getElements();
	            for (Iterator iterator2 = entityElements.iterator(); iterator2
                    .hasNext();) {
	            	ElementEntity item = (ElementEntity) iterator2.next();
	            	double posX  = Double.parseDouble(item.getPosX());
	            	double posY  = Double.parseDouble(item.getPosY());
	            	double[] transformedCoordinates = WFSGUtils.transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {posY,posX});
	            	item.setPosX(String.valueOf(transformedCoordinates[0]));
	            	item.setPosY(String.valueOf(transformedCoordinates[1]));      
	            	item.setName(item.getName()+" ("+municipio.getNombreoficial()+") ");
	            	listaElementos.add(item);
	            }
	           
            }
            return listaElementos;
        } catch (IncorrectArgumentException e) {
            logger.error("Error al consultar por toponimos", e);
            throw e;
        } catch (NoDataFoundException e) {
            logger.error("Error al consultar por toponimos", e);
            throw e;
        } catch (ConnectionException e) {
            logger.error("Error al consultar por toponimos", e);
            throw e;
        } catch (InternalException e) {
            logger.error("Error al consultar por toponimos", e);
            throw e;
        } catch (NumberFormatException e) {
        	 logger.error("Error al consultar por toponimos", e);
        	throw e;
		} catch (LocalgisDBException e) {
			 logger.error("Error al consultar por toponimos", e);
			throw e;
		} catch (LocalgisInitiationException e) {
			 logger.error("Error al consultar por toponimos", e);
				throw e;
		} catch (LocalgisConfigurationException e) {
			 logger.error("Error al consultar por toponimos", e);
				throw e;
		}        
    }

    /**
     * Método main
     * @param args: Argumentos del main
     */
    public static void main(String[] args) {
        //Transformacion de las coordenadas de españa a 23029, 23030, 23031, 23032
        double minX = -19.46;
        double minY = 24.83;
        double maxX = 4.27;
        double maxY = 45.33;

        String[] targetsSRS = new String[] {"EPSG:23028", "EPSG:23029", "EPSG:23030", "EPSG:23031"};
        for (int i = 0; i < targetsSRS.length; i++) {
            double[] minTransformed = WFSGUtils.transformCoordinates("EPSG:4326", targetsSRS[i], new double[] {minX,minY});
            double[] maxTransformed = WFSGUtils.transformCoordinates("EPSG:4326", targetsSRS[i], new double[] {maxX,maxY});
            System.out.println(targetsSRS[i]+": min = "+minTransformed[0]+", "+minTransformed[1]);
            System.out.println(targetsSRS[i]+": max = "+maxTransformed[0]+", "+maxTransformed[1]);
        }
    }
    
}
