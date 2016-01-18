/**
 * PanelEstiloModelo3D.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.ReglaPintadoModelo3D;
import com.vividsolutions.jump.I18N;

/**
 * Panel de estilo para definir las caracteristicas de un modelo 3D
 * 
 * @author David Vicente
 *
 */
public class PanelEstiloModelo3D extends JPanel {

	
	private JTextField campoURL = null;
	private JTextField campoEscalado = null;
	
	
	private JDialog dialogo;
	
	
	private ReglaPintadoModelo3D reglaPintadoModelo3D = null;
	
	
	
	
	public PanelEstiloModelo3D ( JDialog dialogo, ReglaPintadoModelo3D reglaPintadoModelo3D )
	{
		super ( new GridBagLayout() );
		this.dialogo = dialogo;
		this.reglaPintadoModelo3D = reglaPintadoModelo3D;
		
		this.creaPanelEstilo();
	}
	
	
	

	
	

	
	
	/**
	 * Crea el panel con las caracteristicas de estilo
	 * 
	 * @return
	 */
	private void creaPanelEstilo()
	{		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		
		
		// Agregamos la etiqueta "URL"
		JLabel etiquetaURL = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloModelo3D.etiquetaURL" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.insets = new Insets(8, 8, 8, 8);		
		constraints.anchor = GridBagConstraints.EAST;
		
		this.add( etiquetaURL, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		// Agregamos el campo URL
		this.campoURL = new JTextField ( 10 );
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		this.add( this.campoURL, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		
		// Agregamos el botón para seleccionar imagen
		JButton botonSelectorImagen = new JButton ( "..." );		
		
		botonSelectorImagen.addActionListener ( 
			new ActionListener() 
			{			
				public void actionPerformed(ActionEvent e) 
				{
					JFileChooser fileChooser = new JFileChooser ();
				    int returnVal = fileChooser.showSaveDialog ( PanelEstiloModelo3D.this.dialogo );
		
				    if (returnVal == JFileChooser.APPROVE_OPTION) 
				    {
				    	File file = fileChooser.getSelectedFile();	
				    	PanelEstiloModelo3D.this.campoURL.setText ( file.getAbsolutePath() );
				    } 
				    else 
				    {				    	
				    }
				}
			});
		
						
		constraints.gridx = 2; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		
		this.add( botonSelectorImagen, constraints );
		
		
		
		
		
		
		
		// Agregamos la etiqueta "Escalado"
		JLabel etiquetaEscalado = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloModelo3D.etiquetaEscalado" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.anchor = GridBagConstraints.EAST;		
		
		this.add( etiquetaEscalado, constraints );
				
		constraints.anchor = GridBagConstraints.CENTER;
		


		

		

		// Agregamos el campo Escalado
		this.campoEscalado = new JTextField ( 7 );
		
		constraints.gridx = 1; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		constraints.anchor = GridBagConstraints.WEST;
		
		this.add( this.campoEscalado, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
	}
	
	
	
	
	
	
}
