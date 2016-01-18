/**
 * ElemTableRender.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.panel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.table.TableCellRenderer;

public class ElemTableRender extends javax.swing.JTable{
    	private int columnRevisionExpirada; 
    	
    	ElemTableRender(int columnRevisionExpirada){   		
    		this.columnRevisionExpirada = columnRevisionExpirada;
    	}
    	
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column){ 
        	Component returnComp = super.prepareRenderer(renderer, row,column); 
        	returnComp.setForeground(Color.BLUE); 
        	return returnComp; 
        } 
	}

