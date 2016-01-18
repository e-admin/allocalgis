/**
 * LCGIII_DocumentManagerPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.edit;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.geopista.ui.images.IconLoader;

public interface LCGIII_DocumentManagerPlugin {

	public static final ImageIcon ICON = IconLoader.icon("documento.gif");
	public static final Icon iconWord = IconLoader.icon("iconWord.jpg");
	public static final Icon iconPDF = IconLoader.icon("iconPdf.jpg");
	public static final Icon iconTxt = IconLoader.icon("iconTxt.jpg");
	public static final Icon iconHTML = IconLoader.icon("iconHtml.jpg");
	public static final Icon iconPPT = IconLoader.icon("iconPpt.jpg");
	public static final Icon iconXML = IconLoader.icon("iconXml.jpg");
	public static final Icon iconDefault = IconLoader.icon("Sheet.gif");

}
