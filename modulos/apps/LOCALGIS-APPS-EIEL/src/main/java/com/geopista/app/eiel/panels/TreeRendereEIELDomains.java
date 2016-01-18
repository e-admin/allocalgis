/**
 * TreeRendereEIELDomains.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.panels;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.geopista.app.eiel.images.IconLoader;
import com.geopista.protocol.administrador.dominios.DomainNode;



public class TreeRendereEIELDomains extends DefaultTreeCellRenderer{
    
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 8709499234972947823L;
	public static final int TIPO_NODO =1;
    public static final int TIPO_UNKNOW = 2;
        
    public static final String ICONO_NODO = "nodo.gif";
    public static final String ICONO_ROOT = "root.gif";
    public static final String ICONO_NOESPECIFICADO = "nodo.gif";
    public static final String ICONO_LOADED = "gif_ok.gif";
    
    String locale = null;
    
    private HashMap patrones=new HashMap();
    
    
    public TreeRendereEIELDomains(String locale) {
    	this.locale=locale;
    }
    
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {
        
    	//setSize(20,20);
    	//setAutoscrolls(true);
        super.getTreeCellRendererComponent(
                tree, value, sel,
                expanded, leaf, row,
                hasFocus);
        
        if (row>=0)
        {
            StringBuffer sTitle = new StringBuffer();
            Object obj = ((DefaultMutableTreeNode)value).getUserObject();      
            
            String patronSeleccionado=null;
            if (obj!=null){
	            DomainNode domain=(DomainNode)((DefaultMutableTreeNode)value).getUserObject();
	            patronSeleccionado=domain.getPatron();
            }
	            
            switch (getTypeNode(value))
            {            
                
            case TIPO_NODO:
            	if (row==0){
            		setIcon (IconLoader.icon(ICONO_ROOT));
            	}
            	else{
            		
            		if ((patronSeleccionado!=null) && (patrones.containsKey(patronSeleccionado))){
            			Boolean activo=(Boolean)patrones.get(patronSeleccionado);
            			if (activo)
            				setIcon (IconLoader.icon(ICONO_LOADED));
            			else
            				setIcon (IconLoader.icon(ICONO_NOESPECIFICADO));
            		}
            		else
            			setIcon (IconLoader.icon(ICONO_NOESPECIFICADO));
            	}
            	String title = ((DomainNode)obj).getTerm(locale);
            	
                if (title==null) 
                	title = ((DomainNode)obj).getFirstTerm();
                
                if (((DomainNode)obj).getParametros()!=null){
                	title+=((DomainNode)obj).getParametros();
                	 sTitle.append(title);
                	 tree.setLargeModel(true);            	 
                }         
                else{
                	sTitle.append(title);
                }
               
            	break;
            	
            case TIPO_UNKNOW:
            	setIcon(IconLoader.icon(ICONO_NOESPECIFICADO));
            	sTitle.append("Información de la EIEL");
            	break;
                        
            default:                   
                setIcon(IconLoader.icon(ICONO_NOESPECIFICADO));
            }            
            
            if (sTitle!=null)
            {
                this.setText(sTitle.toString().replaceAll("null", " "));                
            }
                        
            
        } else {
            setIcon(IconLoader.icon(ICONO_NOESPECIFICADO));
        }
        
        return this;
    }
    
    protected int getTypeNode(Object value) {

    	DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;        

    	if (node.getUserObject() instanceof DomainNode){
    		
    		return TIPO_NODO;
    	}

    	return TIPO_UNKNOW;
    }

	public void setIconoLoaded(String patron,boolean activo) {
		// TODO Auto-generated method stub
		patrones.put(patron, activo);
	}
    

}

