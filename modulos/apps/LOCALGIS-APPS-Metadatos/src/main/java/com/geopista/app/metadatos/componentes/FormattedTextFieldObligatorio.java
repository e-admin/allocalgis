/**
 * FormattedTextFieldObligatorio.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.componentes;

import java.awt.Color;
import java.text.SimpleDateFormat;

import javax.swing.JFormattedTextField;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 29-jul-2004
 * Time: 13:00:36
 */
public class FormattedTextFieldObligatorio extends JFormattedTextField {
    public FormattedTextFieldObligatorio(){
      super(new SimpleDateFormat("dd-MM-yyyy"));
         this.setBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(2, 2, 2, 2), Color.red));
    }

}
