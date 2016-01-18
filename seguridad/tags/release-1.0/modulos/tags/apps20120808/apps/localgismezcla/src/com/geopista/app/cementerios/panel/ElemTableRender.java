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

