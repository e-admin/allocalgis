/**
 * FieldPanelAccessor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 10-may-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.geopista.ui.autoforms;
import javax.swing.JComponent;
/**
 * @author juacas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface FieldPanelAccessor {
	/**
	 * @param attName
	 * @return
	 */
	public abstract JComponent getComponentByFieldName(String attName);
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.MultiInputDialog#getLabel(java.lang.String)
	 */public abstract JComponent getLabel(String fieldName);
} 