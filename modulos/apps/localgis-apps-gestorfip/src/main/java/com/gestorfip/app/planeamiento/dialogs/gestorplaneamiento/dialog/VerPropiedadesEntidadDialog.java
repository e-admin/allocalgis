/**
 * VerPropiedadesEntidadDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.dialog;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean;
import com.gestorfip.app.planeamiento.utils.GestorFipUtils;
import com.vividsolutions.jump.I18N;

public class VerPropiedadesEntidadDialog extends JDialog{

	
	private static final long serialVersionUID = 1L;
	
	private AppContext appContext = (AppContext) AppContext.getApplicationContext();
	private ApplicationContext application = (AppContext)AppContext.getApplicationContext();

	public static final int DIM_X = 400;
	public static final int DIM_Y = 200;
	private JPanel visorPropEntidadesPanel = null;
	
	private JLabel labelNombre = null;
	private JLabel labelEtiqueta = null;
	private JLabel labelClave = null;
	private JLabel labelBase = null;
	 
	private JTextField textNombre = null;
	private JTextField textEtiqueta = null;
	private JTextField textClave = null;
	private JTextField textBase = null;
	 
	
	 public VerPropiedadesEntidadDialog( EntidadPanelBean epb, ApplicationContext application){
		 
		super(application.getMainFrame());

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		GestorFipUtils.menuBarSetEnabled(false,this.application.getMainFrame());
		
	    inicializaElementos();
	    cargarDatos(epb);
	    
	    int posx = (this.application.getMainFrame().size().width - this.getWidth())/2;
		int posy = (this.application.getMainFrame().size().height -this.getHeight())/2;
		this.setLocation(posx, posy);
		
	    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    
	 }
	
	
	 
	 /**
		* Inicializa los elementos del panel.
		*/
		private void inicializaElementos()
		{
			
			this.setModal(true);
			this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			this.setSize(DIM_X, DIM_Y);
			
			
			visorPropEntidadesPanel = new JPanel();		
			visorPropEntidadesPanel.setLayout(new GridBagLayout());
			
			labelNombre = new JLabel();
			labelNombre.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propentidad.nombre"));
			
			labelEtiqueta = new JLabel();
			labelEtiqueta.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propentidad.etiqueta"));
			
			labelClave = new JLabel();
			labelClave.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propentidad.clave"));
			
			labelBase = new JLabel();
			labelBase.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propentidad.base"));
			
	
			visorPropEntidadesPanel.add(labelNombre, 
					new GridBagConstraints(0,0,1,1, 0.01, 0.5,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
			visorPropEntidadesPanel.add(getTextNombre(), 
					new GridBagConstraints(1,0,1,1, 1, 0.5,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
			
			
			visorPropEntidadesPanel.add(labelEtiqueta, 
					new GridBagConstraints(0,1,1,1, 0.01, 0.5,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
			visorPropEntidadesPanel.add(getTextEtiqueta(), 
					new GridBagConstraints(1,1,1,1, 1, 0.5,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
			
			visorPropEntidadesPanel.add(labelClave, 
					new GridBagConstraints(0,2,1,1, 0.01, 0.5,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
			visorPropEntidadesPanel.add(getTextClave(), 
					new GridBagConstraints(1,2,1,1, 1, 0.5,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
			
			visorPropEntidadesPanel.add(labelBase, 
					new GridBagConstraints(0,3,1,1, 0.01, 0.5,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
			visorPropEntidadesPanel.add(getTextBase(), 
					new GridBagConstraints(1,3,1,1, 1, 0.5,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
			
			
			this.add(visorPropEntidadesPanel);
			this.setTitle(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propentidad.title"));
			
			this.addWindowListener(new java.awt.event.WindowAdapter()
	         {
			     public void windowClosing(java.awt.event.WindowEvent e)
			     {
			         dispose();
			     }
			 });
						
		}
		
		private void cargarDatos(EntidadPanelBean epb){
			
			getTextNombre().setText(epb.getNombre());
			getTextEtiqueta().setText(epb.getEtiqueta());
			getTextClave().setText(epb.getClave());
			if (epb.getBaseBean() != null && epb.getBaseBean().getEntidad() != null){
				getTextBase().setText(epb.getBaseBean().getEntidad().getNombre());
			}
			else{
				getTextBase().setText("");
			}
			
		}
	

		public JTextField getTextNombre() {
			if(textNombre == null){
				textNombre = new JTextField();
				
				textNombre.setEditable(false);
				textNombre.setEnabled(true);
			}
			return textNombre;
		}

		public void setTextNombre(JTextField textNombre) {
			this.textNombre = textNombre;
		}

		public JTextField getTextEtiqueta() {
			if(textEtiqueta == null){
				textEtiqueta = new JTextField();
				
				textEtiqueta.setEditable(false);
				textEtiqueta.setEnabled(true);
			}
			return textEtiqueta;
		}

		public void setTextEtiqueta(JTextField textEtiqueta) {
			this.textEtiqueta = textEtiqueta;
		}

		public JTextField getTextClave() {
			if(textClave == null){
				textClave = new JTextField();
				
				textClave.setEditable(false);
				textClave.setEnabled(true);
			}
			return textClave;
		}

		public void setTextClave(JTextField textClave) {
			this.textClave = textClave;
		}

		public JTextField getTextBase() {
			if(textBase == null){
				textBase = new JTextField();
				
				textBase.setEditable(false);
				textBase.setEnabled(true);
			}
			return textBase;
		}

		public void setTextBase(JTextField textBase) {
			this.textBase = textBase;
		}
	
}
