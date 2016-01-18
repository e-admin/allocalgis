/**
 * LocalgisManagerBuilderSingleton.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * LocalgisManagerBuilderSingleton.java
 *
 * Created on 8 de octubre de 2007, 10:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.localgis.web.wm.util;

import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.LocalgisManagerBuilderFactory;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;

/**
 *
 * @author arubio
 */
public class LocalgisManagerBuilderSingleton {
   
    private static LocalgisManagerBuilder localgisManagerBuilder;
   

    
    public static LocalgisManagerBuilder getInstance() throws LocalgisConfigurationException, LocalgisInitiationException {
          if (localgisManagerBuilder == null) {
              localgisManagerBuilder = LocalgisManagerBuilderFactory.createLocalgisManagerBuilder();          
          }
          return localgisManagerBuilder;
    }
    
}
