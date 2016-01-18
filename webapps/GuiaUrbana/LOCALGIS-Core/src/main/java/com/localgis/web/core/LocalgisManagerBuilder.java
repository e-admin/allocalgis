/**
 * LocalgisManagerBuilder.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core;

import java.util.Collection;

import com.ibatis.dao.client.DaoManager;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.manager.LocalgisLayerManagerImpl;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.manager.LocalgisMapManagerImpl;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManagerImpl;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManagerImpl;
import com.localgis.web.core.manager.LocalgisURLsManager;
import com.localgis.web.core.manager.LocalgisURLsManagerImpl;
import com.localgis.web.core.manager.LocalgisUtilsManager;
import com.localgis.web.core.manager.LocalgisUtilsManagerImpl;
import com.localgis.web.core.wms.WMSConfigurator;

public class LocalgisManagerBuilder {
    
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
    private LocalgisLayerManager localgisLayerManager;
    private LocalgisMapManager localgisMapManager;
    private LocalgisMapsConfigurationManager localgisMapsConfigurationManager;
    private LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager;
    private LocalgisURLsManager localgisURLsManager;
    private LocalgisUtilsManager localgisUtilsManager;
    
    /**
     * Constructor a partir de un DaoManager, un configurador wms, las locales a utilizar y las locales disponibles 
     * @param daoManager DaoManager a utilizar
     * @param locale Locales a utilizar
     */
    protected LocalgisManagerBuilder(DaoManager daoManager, WMSConfigurator wmsConfigurator, String locale, Collection availableLocales) {
        this.locale = locale;
        this.availableLocales = availableLocales;
        this.localgisLayerManager = new LocalgisLayerManagerImpl(daoManager, wmsConfigurator, this);
        this.localgisMapManager = new LocalgisMapManagerImpl(daoManager, wmsConfigurator, this);
        this.localgisMapsConfigurationManager = new LocalgisMapsConfigurationManagerImpl(daoManager, wmsConfigurator, this);
        this.localgisEntidadSupramunicipalManager = new LocalgisEntidadSupramunicipalManagerImpl(daoManager);
        this.localgisURLsManager = new LocalgisURLsManagerImpl(daoManager, wmsConfigurator, this);
        this.localgisUtilsManager = new LocalgisUtilsManagerImpl(daoManager, wmsConfigurator);
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
     * Devuelve el campo localgisLayerManager
     * @return El campo localgisLayerManager
     */
    public LocalgisLayerManager getLocalgisLayerManager() {
        return localgisLayerManager;
    }

    /**
     * Devuelve el campo localgisMapManager
     * @return El campo localgisMapManager
     */
    public LocalgisMapManager getLocalgisMapManager() {
        return localgisMapManager;
    }

    /**
     * Devuelve el campo localgisMapsConfigurationManager
     * @return El campo localgisMapsConfigurationManager
     */
    public LocalgisMapsConfigurationManager getLocalgisMapsConfigurationManager() {
        return localgisMapsConfigurationManager;
    }

    /**
     * Devuelve el campo localgisMunicipioManager
     * @return El campo localgisMunicipioManager
     */
    public LocalgisEntidadSupramunicipalManager getLocalgisEntidadSupramunicipalManager() {
        return localgisEntidadSupramunicipalManager;
    }

    /**
     * Devuelve el campo localgisURLsManager
     * @return El campo localgisURLsManager
     */
    public LocalgisURLsManager getLocalgisURLsManager() {
        return localgisURLsManager;
    }
    
    /**
     * Devuelve el campo localgisUtilsManager
     * @return El campo localgisUtilsManager
     */
    public LocalgisUtilsManager getLocalgisUtilsManager(){
    	return localgisUtilsManager;
    }
    
}
