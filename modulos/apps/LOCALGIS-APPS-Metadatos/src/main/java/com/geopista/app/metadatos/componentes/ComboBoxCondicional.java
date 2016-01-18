/**
 * ComboBoxCondicional.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.componentes;

import java.awt.Color;

import javax.swing.JComboBox;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 29-jul-2004
 * Time: 12:46:29
 */
public class ComboBoxCondicional extends JComboBox{

   public ComboBoxCondicional ()
   {
       super();
       this.setBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(2, 2, 2, 2), Color.YELLOW));
   }

}
