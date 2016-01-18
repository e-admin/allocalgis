/**
 * LocalgisManagerBuilderGeoWfst.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core;

import java.util.Collection;

import com.ibatis.dao.client.DaoManager;
import com.localgis.web.core.manager.LocalgisGeoFeatureManager;
import com.localgis.web.core.manager.LocalgisGeoFeatureManagerImpl;
import com.localgis.web.core.manager.LocalgisGwfstPublishManagerImpl;
import com.localgis.web.core.manager.LocalgisGwftsPublishManager;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManagerGeoWfst;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManagerGeoWfstImpl;
import com.localgis.web.core.wms.WMSConfigurator;

public class LocalgisManagerBuilderGeoWfst {
    
    /**
     * Locales con las que se desea trabajar
     */
    private String locale;

    /**
     * Locales disponibles para usar en las distintas operaciones
     */
    private Collection availableLocales;

    /**
     * Distintos managers de localgis
     */
    private LocalgisMapsConfigurationManagerGeoWfst localgisMapsConfigurationManagerGeoWfst;
    private LocalgisGwftsPublishManager localgisAutoPublishManager;
    private LocalgisGeoFeatureManager localgisGeoFeatureManager;
    
    /**
     * Constructor a partir de un DaoManager, un configurador wms, las locales a utilizar y las locales disponibles 
     * @param daoManager DaoManager a utilizar
     * @param locale Locales a utilizar
     */
    protected LocalgisManagerBuilderGeoWfst(DaoManager daoManager, WMSConfigurator wmsConfigurator, String locale, Collection availableLocales) {
        this.locale = locale;
        this.availableLocales = availableLocales;
        this.localgisMapsConfigurationManagerGeoWfst = new LocalgisMapsConfigurationManagerGeoWfstImpl(daoManager, wmsConfigurator, this);
        this.localgisAutoPublishManager = new LocalgisGwfstPublishManagerImpl(daoManager);
        this.localgisGeoFeatureManager = new LocalgisGeoFeatureManagerImpl(daoManager);
    }
    
    /**
     * Método que devuelve la locale que por defecto estamos usando
     * 
     * @return Locale usada por defecto
     */
    public String getDefaultLocale() {
        return getLocaleSelected(null);
    }

    /**
     * Método que devuelve la locale seleccionada a partir de una locale pasada
     * como parametro. Si la locale no es null y esta disponible se devuelve, en
     * otro caso se devuelve la configurada en el constructor
     * 
     * @param locale Locale que se desea utilizar
     * @return Locale seleccionada
     */
    public String getLocaleSelected(String locale) {
        if (locale != null && availableLocales.contains(locale)) {
            return locale;
        } else {
            return this.locale;
        }
    }
    
    /**
     * Devuelve el campo localgisMapsConfigurationManager
     * @return El campo localgisMapsConfigurationManager
     */
    public LocalgisMapsConfigurationManagerGeoWfst getLocalgisMapsConfigurationManagerGeoWfst() {
        return localgisMapsConfigurationManagerGeoWfst;
    }
    
    /**
     * Devuelve el campo localgisAutoPublishManager
     * @return El campo localgisAutoPublishManager
     */
    public LocalgisGwftsPublishManager getLocalgisAutoPublishManager(){
    	return localgisAutoPublishManager;
    }
    
    public LocalgisGeoFeatureManager getLocalgisGeoFeatureManager(){
    	return localgisGeoFeatureManager;
    }

}
