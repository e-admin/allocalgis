/**
 * ConfiguracionGestorDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.configuracion.dialogs;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.gestorfip.app.planeamiento.utils.GestorFipUtils;
import com.vividsolutions.jump.I18N;

import es.gestorfip.serviciosweb.ServicesStub.CRSGestor;
import es.gestorfip.serviciosweb.ServicesStub.ConfiguracionGestor;
import es.gestorfip.serviciosweb.ServicesStub.VersionesUER;

public class ConfiguracionGestorDialog extends JInternalFrame{

	 private JFrame desktop;
	 private ApplicationContext application;
	 private JButton aceptarJButton;
	 private JButton cancelarJButton;
	 private JPanel botoneraPanel;
	 private JPanel configuracionPanel;
	 private JLabel labelWSConsole;
	 private JComboBox comboWSConsole;	
	 private JLabel labelCRS;
	 private JComboBox comboCRS;	
	
	 private ConfiguracionGestor config;
	 private VersionUER[] lstVersion;
	 private CRS[] lstCRS;
	

	/**
	     * Constructor de la clase. Inicializa todos los paneles de la pantalla y asocia los eventos para ser tratados.
	     * Se le pasa por parametro un JFrame y la lista de expedientes a mostrar.
	     *
	     * @param desktop  JFrame
	     * @param exp    lista de expedientes
	     */
	 public ConfiguracionGestorDialog(final JFrame desktop, ApplicationContext application){
		 
		 this.desktop= desktop;
		 this.application = application;
		 this.config = config;
		
		 this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		 GestorFipUtils.menuBarSetEnabled(false, this.desktop);
		
		 config();
		 inicializaElementos();
		 seleccionElementCombo();
		 seleccionElementComboCrs();

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
        this.setTitle(I18N.get("LocalGISGestorFip","gestorFip.configuracion.gestor.title"));
        setClosable(true);

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	 }
	    
	
	 private void config(){
		 
		VersionesUER[] versiones = (VersionesUER[]) application.getBlackboard().get(ConstantesGestorFIP.VERSIONES_CONSOLE_UER);
		CRSGestor[] crslst = (CRSGestor[]) application.getBlackboard().get(ConstantesGestorFIP.LST_CRS_GESTOR);
	 	this.config =  (ConfiguracionGestor) application.getBlackboard().get(ConstantesGestorFIP.CONFIG_VERSION_CONSOLE_UER);

		 lstVersion = new VersionUER[versiones.length];
		 for (int i=0; i< versiones.length; i++){
			 VersionUER ver = new VersionUER(versiones[i].getId(), versiones[i].getVersion());
			 lstVersion[i] = ver;
		 }
		 
		 lstCRS = new CRS[crslst.length];
		 for (int i=0; i< crslst.length; i++){
			 CRS crs = new CRS(crslst[i].getId(), crslst[i].getCrs());
			 lstCRS[i] = crs;
		 }
			 
	 }
	 
	 private void seleccionElementCombo(){

		for(int i=0; i<lstVersion.length; i++){
			if(lstVersion[i].getId() == this.config.getIdVersion()){
				getComboWSConsole().setSelectedItem(lstVersion[i]);
			}
		}

	 }
	 
	 
	 private void seleccionElementComboCrs(){

		for(int i=0; i<lstCRS.length; i++){
			if(lstCRS[i].getId() == this.config.getIdCrs()){
				getComboCRS().setSelectedItem(lstCRS[i]);
			}
		}

	 }

   /**
    * Inicializa los elementos del panel.
    */
    private void inicializaElementos()
    {
        getContentPane().add(getConfiguracionPanel());
    }
    
    
    private JFrame getDesktop() {
    	return desktop;
    }



    private JButton getCancelarJButton() {
		
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
	
	

    /**
     * Cierra la ventana y habilita el menu de la aplicacion.
     */
	private void cierraInternalFrame()
    {
        try
        {
        	this.setClosed(true);
        }
        catch(Exception e){e.printStackTrace();}
        GestorFipUtils.menuBarSetEnabled(true, this.desktop);      
    }    
    
   
    public JButton getAceptarJButton() {
		if(aceptarJButton == null){
			
			aceptarJButton = new JButton(I18N.get("LocalGISGestorFip",
								"gestorFip.aceptarJButton"));
			
		}
		return aceptarJButton;
	}
	

	private JPanel getConfiguracionPanel() {
		if(configuracionPanel == null){
			configuracionPanel = new JPanel();
			configuracionPanel.setLayout(new GridBagLayout());
	
			labelWSConsole = new JLabel(I18N.get("LocalGISGestorFip","gestorFip.configuracion.version.console"));
			labelCRS = new JLabel(I18N.get("LocalGISGestorFip","gestorFip.configuracion.crs"));
			
			configuracionPanel.add(labelWSConsole, 
					new GridBagConstraints(0,0,1,1, 0.001, 1,GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
			
			configuracionPanel.add(getComboWSConsole(), 
					new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));	
			
			configuracionPanel.add(labelCRS, 
					new GridBagConstraints(0,1,1,1, 0.001, 1,GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));

			configuracionPanel.add(getComboCRS(), 
					new GridBagConstraints(1,1,1,1, 1, 1,GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));	
			
			configuracionPanel.add(getBotoneraPanel(), 
					new GridBagConstraints(1,2,2,1, 1, 1,GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));	
		}
		return configuracionPanel;
	}



	private void setConfiguracionPanel(JPanel configuracionPanel) {
		this.configuracionPanel = configuracionPanel;
	}
		
 
	 public JComboBox getComboWSConsole() {
		if(comboWSConsole == null){
			comboWSConsole = new JComboBox(lstVersion);	
		}
		return comboWSConsole;
	}

	 private void setComboWSConsole(JComboBox textWSConsole) {
		this.comboWSConsole = textWSConsole;
	}
	 
	 public JComboBox getComboCRS() {
		if(comboCRS == null){
			comboCRS = new JComboBox(lstCRS);	
		}
		return comboCRS;
	}


	public void setComboCRS(JComboBox comboCRS) {
		this.comboCRS = comboCRS;
	}

	private JPanel getBotoneraPanel() {
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
		
	public ConfiguracionGestor getConfigSelected(){
		
		VersionUER ver = (VersionUER) this.comboWSConsole.getSelectedItem();
		CRS crs = (CRS) this.comboCRS.getSelectedItem();
		if(this.config != null){
			
			this.config.setIdVersion(ver.getId());
			this.config.setIdCrs(crs.getId());
		}
		else{
			this.config = new ConfiguracionGestor();
			config.setId(-1);
			config.setIdVersion(ver.getId());
			config.setIdCrs(crs.getId());
		}
	
		return this.config;
	}
	
	
		
	private class VersionUER {

		int id;
		double version;
		
		public VersionUER(int id, double version){
			this.id = id;
			this.version = version;
		}
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public double getVersion() {
			return version;
		}
		
		public void setVersion(double version) {
			this.version = version;
		}
		
		public String toString(){
			return String.valueOf(version);
		}
		
	}
	
	private class CRS {

		int id;
		int crs;
		
		public CRS(int id, int crs){
			this.id = id;
			this.crs = crs;
		}
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public int getCrs() {
			return crs;
		}
		
		public void setCrs(int crs) {
			this.crs = crs;
		}
		
		public String toString(){
			return String.valueOf(crs);
		}
		
	}

}

