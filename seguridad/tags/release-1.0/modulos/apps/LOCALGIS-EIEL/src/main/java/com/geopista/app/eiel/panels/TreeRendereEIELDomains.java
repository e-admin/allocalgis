package com.geopista.app.eiel.panels;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.geopista.app.eiel.beans.filter.LCGNodoEIEL;
import com.geopista.app.eiel.images.IconLoader;
import com.geopista.app.eiel.utils.HideableNode;
import com.geopista.protocol.administrador.dominios.DomainNode;



public class TreeRendereEIELDomains extends DefaultTreeCellRenderer{
    
	
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

