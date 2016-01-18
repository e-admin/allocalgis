/**
 * Attributes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.dwr;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContextFactory;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisWMSException;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.model.GeopistaColumn;
import com.localgis.web.core.model.LocalgisRestrictedAttribute;
import com.localgis.web.wm.util.LocalgisManagerBuilderSingleton;

public class Attributes {
    
    private Logger logger = Logger.getLogger(Attributes.class);

	public void check() {
		
	}
	
	public List getAttributesGeopista(int idLayer) throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisDBException {
		try {
		HttpSession httpSession = WebContextFactory.get().getSession();
		Locale locale = (Locale) httpSession.getAttribute("org.apache.struts.action.LOCALE");
		LocalgisLayerManager localgisLayerManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisLayerManager();
		return localgisLayerManager.getColumnsLayer(new Integer(idLayer),locale.toString());
		} catch (LocalgisConfigurationException e) {
			logger.error("LocalgisConfigurationException al obtener los atributos de la capa de LocalGIS ["+idLayer+"]", e);
			throw e;
		} catch (LocalgisInitiationException e) {
			logger.error("LocalgisInitiationException al obtener los atributos de la capa de LocalGIS ["+idLayer+"]", e);
			throw e;
		} catch (LocalgisDBException e) {
			logger.error("LocalgisDBException al obtener los atributos de la capa de LocalGIS ["+idLayer+"]", e);
			throw e;
		}
	}
	
	
	
	public Hashtable getAttributesByLayer(int idLayer,int idEntidad,boolean mapPublic) throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisDBException {
		try {
		LocalgisLayerManager localgisLayerManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisLayerManager();
		List layerAttributes = null;
		List restrictedAttributes = null;
		HttpSession httpSession = WebContextFactory.get().getSession();
		Locale locale = (Locale) httpSession.getAttribute("org.apache.struts.action.LOCALE");
		layerAttributes = localgisLayerManager.getColumnsLayer(new Integer(idLayer),locale.toString());
		restrictedAttributes = localgisLayerManager.getRestrictedAttributesLayer(new Integer(idLayer),new Integer(idEntidad) ,locale.toString(),new Boolean(mapPublic));
		for (Iterator iteratorLayerAttributes = layerAttributes.iterator(); iteratorLayerAttributes.hasNext();) {
			GeopistaColumn layerAttribute = (GeopistaColumn) iteratorLayerAttributes.next();
			layerAttribute.getIdAttributeGeopista();
			if (layerAttribute.getAlias() == null) {
				layerAttribute.setAlias(layerAttribute.getName());
			}
			for (Iterator iteratorRestrictedAttributes = restrictedAttributes.iterator(); iteratorRestrictedAttributes.hasNext();) {
				LocalgisRestrictedAttribute restrictedAttribute = (LocalgisRestrictedAttribute) iteratorRestrictedAttributes.next();
				if (restrictedAttribute.getAlias() == null) {
					restrictedAttribute.setAlias(layerAttribute.getName());
				}
				if (restrictedAttribute.getAttributeidgeopista().equals(layerAttribute.getIdAttributeGeopista())) {
					iteratorLayerAttributes.remove();
					break;
				}
			}			
		}
		Hashtable hashtable = new Hashtable();
		hashtable.put("layerAttributes", layerAttributes);
		hashtable.put("restrictedAttributes", restrictedAttributes);
		return hashtable;
	} catch (LocalgisConfigurationException e) {
		logger.error("LocalgisConfigurationException al obtener los atributos de la capa de LocalGIS", e);
		throw e;
	} catch (LocalgisInitiationException e) {
		logger.error("LocalgisInitiationException al obtener los atributos de la capa de LocalGIS", e);
		throw e;
	} catch (LocalgisDBException e) {
		logger.error("LocalgisDBException al obtener los atributos de la capa de LocalGIS", e);
		throw e;
	}
	}
	
	
	
	public void saveRestrictedAttributes(int idLayer,int idEntidad,List restrictedAttributes,boolean mapPublic) throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisDBException, LocalgisWMSException, LocalgisInvalidParameterException {
		
        LocalgisLayerManager localgisLayerManager;
        LocalgisMapsConfigurationManager localgisMapsConfigurationManager;
		HttpSession httpSession = WebContextFactory.get().getSession();
		Locale locale = (Locale) httpSession.getAttribute("org.apache.struts.action.LOCALE");
		try {
			Vector restrictedAttributesVector = new Vector();
			localgisLayerManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisLayerManager();
			localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
			for (Iterator iterator = restrictedAttributes.iterator(); iterator
					.hasNext();) {
				String restrictedAttributesString = (String) iterator.next();	
				StringTokenizer stringTokenizer = new StringTokenizer(restrictedAttributesString,";");
				String attributeIdString = stringTokenizer.nextToken();
				String idalias = stringTokenizer.nextToken();
				LocalgisRestrictedAttribute localgisRestrictedAttribute = new LocalgisRestrictedAttribute();
				localgisRestrictedAttribute.setAttributeidgeopista(new Integer(attributeIdString));
				localgisRestrictedAttribute.setIdalias(new Integer(idalias));
				localgisRestrictedAttribute.setIdentidad(new Integer(idEntidad));
				localgisRestrictedAttribute.setMappublicBoolean(new Boolean(mapPublic));
				restrictedAttributesVector.add(localgisRestrictedAttribute);
			}
			List actualRestrictedAttributes =  localgisLayerManager.getRestrictedAttributesLayer(new Integer(idLayer),new Integer(idEntidad) ,locale.toString(),
					new Boolean(mapPublic));
			if (restrictedAttributesVector.size() >0) {
				for (Iterator iteratorRestrictedAttributesVector = restrictedAttributesVector.iterator(); iteratorRestrictedAttributesVector.hasNext();) {
					LocalgisRestrictedAttribute restrictedAttribute = (LocalgisRestrictedAttribute) iteratorRestrictedAttributesVector.next();
					boolean found = false;
					for (Iterator iteratorActualRestrictedAttributes = actualRestrictedAttributes.iterator(); iteratorActualRestrictedAttributes.hasNext();) {
						LocalgisRestrictedAttribute actualRestrictedAttribute = (LocalgisRestrictedAttribute) iteratorActualRestrictedAttributes.next();
						if (restrictedAttribute.getAttributeidgeopista().equals(actualRestrictedAttribute.getAttributeidgeopista())) {
							found = true;
							iteratorActualRestrictedAttributes.remove();
							break;
						}
					}
					if (!found) {
					    localgisLayerManager.addRestrictedAttributesLayer(new Integer(idLayer), restrictedAttribute.getAttributeidgeopista(), new Integer(idEntidad),restrictedAttribute.getIdalias(), new Boolean(mapPublic));
					}
				}			
			}
			for (Iterator iterator = actualRestrictedAttributes.iterator(); iterator.hasNext();) {					
				LocalgisRestrictedAttribute item = (LocalgisRestrictedAttribute) iterator.next();
				localgisLayerManager.removeRestrictedAttributesLayer(item.getLayeridgeopista(), item.getAttributeidgeopista(),new Integer(idEntidad) ,new Boolean(mapPublic));
			}
			localgisMapsConfigurationManager.reconfigureWMSServer(new Integer(idEntidad) ,new Boolean(mapPublic));
		} catch (LocalgisConfigurationException e) {
            logger.error("Error al salvar los atributos de la capa ["+idLayer+"] de la entidad ["+idEntidad+"] para configuracion public ["+mapPublic+"]", e);
            throw e;
		} catch (LocalgisInitiationException e) {
            logger.error("Error al salvar los atributos de la capa ["+idLayer+"] de la entidad ["+idEntidad+"] para configuracion public ["+mapPublic+"]", e);
            throw e;
		} catch (LocalgisDBException e) {
            logger.error("Error al salvar los atributos de la capa ["+idLayer+"] de la entidad ["+idEntidad+"] para configuracion public ["+mapPublic+"]", e);
            throw e;
		} catch (LocalgisWMSException e) {
            logger.error("Error al salvar los atributos de la capa ["+idLayer+"] de la entidad ["+idEntidad+"] para configuracion public ["+mapPublic+"]", e);
            throw e;
        } catch (LocalgisInvalidParameterException e) {
            logger.error("Error al salvar los atributos de la capa ["+idLayer+"] de la entidad ["+idEntidad+"] para configuracion public ["+mapPublic+"]", e);
            throw e;
        }		
	}
	
	
	public List getRestrictedAttributes(int idLayer,int idEntidad ,boolean mapPublic ) throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisDBException{
		LocalgisLayerManager localgisLayerManager;
		try {
		    localgisLayerManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisLayerManager();
			return localgisLayerManager.getRestrictedAttributesLayer(new Integer(idLayer),new Integer(idEntidad),"es_ES",new Boolean(mapPublic));
		} catch (LocalgisConfigurationException e) {
            logger.error("Error al obtener los atributos restringidos de la capa ["+idLayer+"] de la entidad ["+idEntidad+"] para configuracion public ["+mapPublic+"]", e);
            throw e;
		} catch (LocalgisInitiationException e) {
            logger.error("Error al obtener los atributos restringidos de la capa ["+idLayer+"] de la entidad ["+idEntidad+"] para configuracion public ["+mapPublic+"]", e);
            throw e;
		} catch (LocalgisDBException e) {
            logger.error("Error al obtener los atributos restringidos de la capa ["+idLayer+"] de la entidad ["+idEntidad+"] para configuracion public ["+mapPublic+"]", e);
            throw e;
		}	
	}
}
