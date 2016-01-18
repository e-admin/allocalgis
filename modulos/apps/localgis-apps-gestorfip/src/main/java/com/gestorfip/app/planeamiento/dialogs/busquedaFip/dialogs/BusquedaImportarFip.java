/**
 * BusquedaImportarFip.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.busquedaFip.dialogs;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.panels.tramite.FipPanelBean;
import com.gestorfip.app.planeamiento.dialogs.busquedaFip.panels.TablaFicherosFip;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.gestorfip.app.planeamiento.utils.GestorFipUtils;
import com.vividsolutions.jump.I18N;

public class BusquedaImportarFip extends JInternalFrame{

	 private JFrame desktop;
	 private ApplicationContext application;
	 private ArrayList lstFip;
	 private ArrayList lstMunicipios;
	 private JButton aceptarJButton;
	 private JButton cancelarJButton;
	 private JPanel buscarPanel;
	 private JPanel botoneraPanel;
	 private JPanel tablaBusquedaPanel;
	
	private TablaFicherosFip tablaFicherosFip;


	/**
	     * Constructor de la clase. Inicializa todos los paneles de la pantalla y asocia los eventos para ser tratados.
	     * Se le pasa por parametro un JFrame y la lista de expedientes a mostrar.
	     *
	     * @param desktop  JFrame
	     * @param exp    lista de expedientes
	     */
	 public BusquedaImportarFip(final JFrame desktop, ArrayList lstFip,
			 ApplicationContext application){
		 
		 this.desktop= desktop;
		 this.application = application;
		 this.lstFip = lstFip;	
		
		 this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		 GestorFipUtils.menuBarSetEnabled(false, this.desktop);

		 inicializaElementos();

	     addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
                cierraInternalFrame();
                
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });
        this.setTitle(I18N.get("LocalGISGestorFip","gestorFip.abrirfip.busquedaFicheroFip1.title"));
        setClosable(true);

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	 }
	    
	

   /**
    * Inicializa los elementos del panel.
    */
    private void inicializaElementos()
    {
    	buscarPanel= new JPanel();
        buscarPanel.setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints1 = new java.awt.GridBagConstraints();
		GridBagConstraints gridBagConstraints2 = new java.awt.GridBagConstraints();
		GridBagConstraints gridBagConstraints4 = new java.awt.GridBagConstraints();
		
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 0.1;
        gridBagConstraints1.insets = new java.awt.Insets(5, 5, 0, 5);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1; 
        gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.weighty = 0.7;
        gridBagConstraints2.insets = new java.awt.Insets(5, 5, 0, 5);
        
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 3;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints4.weightx = 1.0;
        gridBagConstraints4.weighty = 0.1;
        gridBagConstraints4.insets = new java.awt.Insets(5, 0, 0, 5);

        buscarPanel.add(getTablaBusquedaPanel(), gridBagConstraints2);
        buscarPanel.add(getBotoneraPanel(), gridBagConstraints4);
    
        getContentPane().add(buscarPanel);

    }
    
    
    public JFrame getDesktop() {
    	return desktop;
    }



	public JButton getCancelarJButton() {
		
		if(cancelarJButton ==null){
			
			cancelarJButton = new JButton(I18N.get("LocalGISGestorFip",
		 				"gestorFip.cancelarJButton"));
			
		 	cancelarJButton.addActionListener(new ActionListener()
			{
		 		public void actionPerformed(java.awt.event.ActionEvent evt)
		 		{
		 			cierraInternalFrame();
		 		}
			});
		}
		return cancelarJButton;
	}
	
	
	
	public JPanel getBuscarPanel() {
		return buscarPanel;
	}

    
    /**
     * Cierra la ventana y habilita el menu de la aplicacion.
     */
    public void cierraInternalFrame()
    {
        try
        {
        	this.setClosed(true);
        }
        catch(Exception e){e.printStackTrace();}
        GestorFipUtils.menuBarSetEnabled(true, this.desktop);
//        if(application.getBlackboard().get(ConstantesGestorFIP.FIP_TRABAJO) == null){
//        	GestorFipUtils.activarDesactivarMenuPlaneamiento(false, this.desktop);
//        }
//        else{
//        		
//        }
        
    }    
    
    private ArrayList filtrarDatos(String municipio, String fechaConsolidacion){
		ArrayList lstFipFiltro = new ArrayList();
    	if(municipio.equals("") && fechaConsolidacion.equals("")){
    		lstFipFiltro = this.lstFip;	
    	}
    	else{
    		if(!municipio.equals("") && fechaConsolidacion.equals("")){
    			for(int i=0; i<this.lstFip.size(); i++){
        			if(((FipPanelBean)this.lstFip.get(i)).getMunicipio().contains(municipio)){
        				lstFipFiltro.add(this.lstFip.get(i));	
        			}  		
        		}
    		}
    		else if(!fechaConsolidacion.equals("") && municipio.equals(""))
    		{
    			for(int i=0; i<this.lstFip.size(); i++){
        			if(((FipPanelBean)this.lstFip.get(i)).getFechaConsolidacion().contains(fechaConsolidacion)){
        				lstFipFiltro.add(this.lstFip.get(i));	
        			}  		
        		}
    		}
    		else{
    			for(int i=0; i<this.lstFip.size(); i++){
        			if(((FipPanelBean)this.lstFip.get(i)).getMunicipio().contains(municipio) &&
        					((FipPanelBean)this.lstFip.get(i)).getFechaConsolidacion().contains(fechaConsolidacion)){
        				lstFipFiltro.add(this.lstFip.get(i));	
        			}  		
        		}
    		}
    		
    	}

    	return lstFipFiltro;
    }
    

	public JButton getAceptarJButton() {
		if(aceptarJButton == null){
			
			aceptarJButton = new JButton(I18N.get("LocalGISGestorFip",
								"gestorFip.aceptarJButton"));
			
		}
		return aceptarJButton;
	}
	
	public JPanel getBotoneraPanel() {
		if(botoneraPanel == null){
			
			botoneraPanel = new JPanel();
			botoneraPanel.setLayout(new GridBagLayout());
			
			botoneraPanel.add(getAceptarJButton(), 
					new GridBagConstraints(0,0,1,1, 1, 0.8,GridBagConstraints.EAST,
							GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
			
			botoneraPanel.add(getCancelarJButton(), 
					new GridBagConstraints(1,0,1,1, 1, 0.8,GridBagConstraints.EAST,
							GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));

		}
		return botoneraPanel;
	}
	
	
	public JPanel getTablaBusquedaPanel() {
		if(tablaBusquedaPanel == null){
			
			tablaBusquedaPanel = new JPanel();
			tablaBusquedaPanel.setLayout(new GridBagLayout());
			tablaBusquedaPanel.setBorder(new TitledBorder(I18N.get("LocalGISGestorFip","gestorFip.abrirfip.tabla.etiqueta")));

	        inicializaTabla();
	        
	        
		}
		return tablaBusquedaPanel;
	}
	
	   /**
	    * Inicializa la tabla donde se muestran los expedientes.
	    */
	    private void inicializaTabla()
	    {

	        tablaFicherosFip= new TablaFicherosFip("gestorFip.abrirfip.tabla.etiqueta", 
	        						lstFip);
	        
	        tablaBusquedaPanel.add(tablaFicherosFip.getPanel(), 
					new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));

	    }
    	
		public TablaFicherosFip getTablaBusquedaFicherosFip() {
			return tablaFicherosFip;
		}



		public void setTablaBusquedaFicherosFip(
				TablaFicherosFip tablaBusquedaFicherosFip) {
			this.tablaFicherosFip = tablaBusquedaFicherosFip;
		}
		
}

