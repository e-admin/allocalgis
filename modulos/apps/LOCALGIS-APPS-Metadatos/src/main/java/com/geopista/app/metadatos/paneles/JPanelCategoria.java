/**
 * JPanelCategoria.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.paneles;


import java.util.ResourceBundle;

import com.geopista.app.metadatos.componentes.PanelCheckBoxEstructurasCondicional;
import com.geopista.app.metadatos.estructuras.Estructuras;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 30-jul-2004
 * Time: 12:10:37
 */
public class JPanelCategoria extends PanelCheckBoxEstructurasCondicional {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JPanelCategoria.class);
    public JPanelCategoria(ResourceBundle messages) {
        super(Estructuras.getListaTopicCategory(),10,2);
        logger.debug("ALTISIMO: "+this.getPreferredSize().getHeight());
        logger.debug("ANCHISIMO: "+this.getPreferredSize().getWidth());

    }
    public void changeScreenLang()
    {

    }

}
