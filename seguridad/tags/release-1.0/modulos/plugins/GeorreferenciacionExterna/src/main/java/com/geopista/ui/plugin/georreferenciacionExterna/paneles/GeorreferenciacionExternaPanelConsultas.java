package com.geopista.ui.plugin.georreferenciacionExterna.paneles;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.geopista.ui.plugin.georreferenciacionExterna.consulta.JPanelTablaConsultas;
import com.geopista.ui.plugin.georreferenciacionExterna.dialog.GeorreferenciaExternaConsultaDialog;
import com.localgis.ws.servidor.SQLExceptionException0;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.ws.cliente.Test;

public class GeorreferenciacionExternaPanelConsultas extends JPanel{

	private boolean okPressed = false;
	private PlugInContext context = null;
	
	private JPanel jPanelBusquedaConsultas = null;
	private JPanelTablaConsultas jPanelTablaConsultas = null; 
	private String user = "";
	private GeorreferenciaExternaConsultaDialog geoDialog;
	
	public GeorreferenciacionExternaPanelConsultas(PlugInContext context, String user, GeorreferenciaExternaConsultaDialog geoDialog)
	  {
	
	  Object[] projections = CoordinateSystemRegistry
	  .instance(context.getWorkbenchContext()
	          .getBlackboard()).getCoordinateSystems().toArray();
		this.user = user;
		this.geoDialog = geoDialog;
	    this.context=context;
	    try
	    {
	      jbInit();
	    }
	    catch(Exception e)
	    {
	      e.printStackTrace();
	    }

	  }
	
	  private void jbInit() throws Exception
	  {
	    this.setLayout(new GridBagLayout());

	    this.add(getPanelBusquedaConsultas(), 
				new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
	    
	  }
	
	  private JPanel getPanelBusquedaConsultas() throws SQLExceptionException0{
	    	
	    	if (jPanelBusquedaConsultas == null){
				try {
						jPanelBusquedaConsultas = new JPanel();
						jPanelTablaConsultas = new JPanelTablaConsultas(this.context, this.geoDialog);
						jPanelBusquedaConsultas.add(jPanelTablaConsultas, 
								new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
										GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
						
						
						//consultamos los filtros de la bbdd

						ArrayList lstConsultas = Test.obtenerConsultas(this.user);
						jPanelTablaConsultas.pintarConsultas(lstConsultas);
						if(lstConsultas.isEmpty()){
							jPanelTablaConsultas.deshabilitarBotones();
						}
						else{
							jPanelTablaConsultas.habilitarBotones();
						}
						
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    	}
	    	
	    	return jPanelBusquedaConsultas;
	    }
	  

	private void btnCancelar_actionPerformed(ActionEvent e)
	  {
	    this.setVisible(false);
	    JDialog Dialog = (JDialog) SwingUtilities.getWindowAncestor(this );
	    Dialog.setVisible(false);
	  }
	  
	  public boolean wasOKPressed() {
	      return okPressed;
	  }

	  public void setOKPressed(boolean okPressed) {
	      this.okPressed = okPressed;
	  }
}
