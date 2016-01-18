/**
 * WFSGService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.dwr;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.localgis.web.beans.PlaceNameInfoService;
import com.localgis.web.config.LocalgisWebConfiguration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.model.GeopistaMunicipio;
import com.localgis.web.core.utils.ChangeCoordinateSystem;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.util.WFSGUtils;
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
 * Clase para acceder a los servicios DWR relacionados con el servicio WFS-G
 * @author albegarcia
 *
 */
public class WFSGService {

    private static Log logger = LogFactory.getLog(WFSGService.class);
    
    private static String SERVICE_WFS_MNE_STR = "WFS-MNE";
    private static String SERVICE_WFS_G_STR = "WFS-G";
    
    /**
     * Método para obtener la lista de servicios de busqueda de topónimos
     * @return Una lista de servicios como objetos PlaceNameInfoService
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
     * @param service
     * @param query
     * @param municipio
     * @param targetCRSCode
     * @return
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
    
    private List getEntityElements(LocalGIS_WFSG_Resultados results, String targetCRSCode) {
        Entity entity = results.getEntityReference();
        //String sourceCRSCode = "EPSG:"+results.getSRS();
        String sourceCRSCode = results.getSRS();
        List entityElements = entity.getElements();
        for (Iterator iterator = entityElements.iterator(); iterator.hasNext();) {
            ElementEntity item = (ElementEntity) iterator.next();
            //--
            //BUG. Si el elemento no tiene geometría pasamos de ella.
        	if ((item.getPosX()!=null) && (item.getPosY()!=null)){
            	double posX  = Double.parseDouble(item.getPosX());
            	double posY  = Double.parseDouble(item.getPosY());
            	
            	
            	double[] transformedCoordinates=ChangeCoordinateSystem.transformSpecial(sourceCRSCode,targetCRSCode,new double[] {posX,posY});
    			
            	
            	/*ChangecoodinateSystem.transformCoordinates(String sourceCRSCode,String targetCRSCode, double[] coordinates) {
            		
            	//Las coordenadas hay que enviarlas en orden inverso.
            	double[] transformedCoordinates = WFSGUtils.transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {posX,posY});

            	//No se porque es exactamente pero en un PC windows funciona distinto que en la maquna
            	//hay que cambiar el orden de las coordenadas.
            	if ((transformedCoordinates[0]<0) || (transformedCoordinates[1]<0))
	            	transformedCoordinates = WFSGUtils.transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {posY,posX});
            	//double[] transformedCoordinates = WFSGUtils.transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {posY,posX});
            	logger.info("SRID Origen-Destino:"+sourceCRSCode+" "+targetCRSCode+" Coordenadas de busqueda:("+posX+","+posY+") ("+transformedCoordinates[0]+" "+transformedCoordinates[1]+")");
            	//System.out.println("Coordenadas de busqueda:"+transformedCoordinates[0]+" "+transformedCoordinates[1]);

            	*/
    			
            		
            		
            	item.setPosX(String.valueOf(transformedCoordinates[0]));
            	item.setPosY(String.valueOf(transformedCoordinates[1]));      
            	//item.setName(item.getName()+" ("+municipio.getNombreoficial()+") ");
        	}
        }

        return entityElements;
    }
    
    /**
     * Método para realizar la búsqueda de calles
     * @param service
     * @param streetName
     * @param streetNumber
     * @param municipio
     * @param targetCRSCode
     * @return
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
	            	//BUG. Si la calle no tiene geometría pasamos de ella.
	            	if ((item.getPosX()!=null) && (item.getPosY()!=null)){
		            	double posX  = Double.parseDouble(item.getPosX());
		            	double posY  = Double.parseDouble(item.getPosY());
		            	
		            	
		            	//double[] transformedCoordinates=ChangeCoordinateSystem.transform(sourceCRSCode,targetCRSCode,new double[] {posX,posY});
		            	double[] transformedCoordinates=ChangeCoordinateSystem.transformSpecial(sourceCRSCode,targetCRSCode,new double[] {posX,posY});
		        				         
		            	/*
		            	//Las coordenadas hay que enviarlas en orden inverso.
		            	double[] transformedCoordinates = WFSGUtils.transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {posX,posY});

		            	//No se porque es exactamente pero en un PC windows funciona distinto que en la maquna
		            	//hay que cambiar el orden de las coordenadas.
		            	if ((transformedCoordinates[0]<0) || (transformedCoordinates[1]<0))
			            	transformedCoordinates = WFSGUtils.transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {posY,posX});
		            		
		            	//double[] transformedCoordinates = WFSGUtils.transformCoordinates(sourceCRSCode, targetCRSCode, new double[] {posY,posX});
		            	logger.info("SRID Origen-Destino:"+sourceCRSCode+" "+targetCRSCode+" Coordenadas de busqueda:("+posX+","+posY+") ("+transformedCoordinates[0]+" "+transformedCoordinates[1]+")");
		            	//System.out.println("Coordenadas de busqueda:"+transformedCoordinates[0]+" "+transformedCoordinates[1]);
		            	 
		            	 */
		            	item.setPosX(String.valueOf(transformedCoordinates[0]));
		            	item.setPosY(String.valueOf(transformedCoordinates[1]));      
		            	item.setName(item.getName()+" ("+municipio.getNombreoficial()+") ");
		            	listaElementos.add(item);
	            	}
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
		catch (Exception e) {
			 logger.error("Error al consultar por toponimos", e);
			throw new InternalException("Error");
		} 
    }

    public static void main(String[] args) {
        //Transformacion de las coordenadas de españa a 23029, 23030, 23031, 23032
        double minX = -19.46;
        double minY = 24.83;
        double maxX = 4.27;
        double maxY = 45.33;

        /*String[] targetsSRS = new String[] {"EPSG:23028", "EPSG:23029", "EPSG:23030", "EPSG:23031"};
        for (int i = 0; i < targetsSRS.length; i++) {
            double[] minTransformed = WFSGUtils.transformCoordinates("EPSG:4326", targetsSRS[i], new double[] {minX,minY});
            double[] maxTransformed = WFSGUtils.transformCoordinates("EPSG:4326", targetsSRS[i], new double[] {maxX,maxY});
            System.out.println(targetsSRS[i]+": min = "+minTransformed[0]+", "+minTransformed[1]);
            System.out.println(targetsSRS[i]+": max = "+maxTransformed[0]+", "+maxTransformed[1]);
        }*/
        
        
    }
}
