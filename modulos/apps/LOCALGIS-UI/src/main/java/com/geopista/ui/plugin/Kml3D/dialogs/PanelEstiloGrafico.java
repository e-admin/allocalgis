/**
 * PanelEstiloGrafico.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.dialogs;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.ReglaPintadoGrafico;
import com.vividsolutions.jump.I18N;

/**
 * Panel de estilo para definir las caracteristicas de un gráfico
 * 
 * @author David Vicente
 *
 */
public class PanelEstiloGrafico extends JPanel {

	
	private JTextField campoURL = null;
	private JTextField campoEscalado = null;
	private JTextField campoRadio = null;
	private JComboBox desplegableCampoTexto = null;
	private JComboBox campoCampo = null;
	private JComboBox desplegableRepresentacion = null;
	
		
	private JDialog dialogo;
	
	private ReglaPintadoGrafico reglaPintadoGrafico = null;
	
	
	
	
	
	public PanelEstiloGrafico ( JDialog dialogo, ReglaPintadoGrafico reglaPintadoGrafico )
	{
		super ( new GridBagLayout() );
		this.dialogo = dialogo;
		this.reglaPintadoGrafico = reglaPintadoGrafico;
		
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
		
		
		
		
		// Agregamos la etiqueta "Tipo"
		JLabel etiquetaTipo = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoGrafico.etiquetaTipo" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.EAST;
		
		this.add( etiquetaTipo, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		// Agregamos el desplegable de "Tipo"
		String [] valoresTipo = 
		{
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoGrafico.desplegableTipoValoresAbsolutos" ),
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoGrafico.desplegableTipoPorcentajes" )
		};		
		JComboBox desplegableTipo = new JComboBox ( valoresTipo );
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 3; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		this.add( desplegableTipo, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		

		// Agregamos la etiqueta "Representación"
		JLabel etiquetaRepresentacion = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoGrafico.etiquetaRepresentacion" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.EAST;
		
		this.add( etiquetaRepresentacion, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		// Agregamos el desplegable de "Tipo"
		String [] valoresRepresentacion = 
		{
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoGrafico.desplegableRepresentacionBarras" ),
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoGrafico.desplegableRepresentacionModelos" ),
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoGrafico.desplegableRepresentacionQuesitos" )
		};		
		this.desplegableRepresentacion = new JComboBox ( valoresRepresentacion );
		
		constraints.gridx = 1; 
		constraints.gridy = 1; 
		constraints.gridwidth = 3; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		this.add( this.desplegableRepresentacion, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		

		
		// Agregamos la etiqueta "URL"
		JLabel etiquetaURL = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoGrafico.etiquetaURL" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.EAST;
		
		this.add( etiquetaURL, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		// Agregamos el campo URL
		this.campoURL = new JTextField ( 10 );
		
		constraints.gridx = 1; 
		constraints.gridy = 2; 
		constraints.gridwidth = 3; 
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
				    int returnVal = fileChooser.showSaveDialog(PanelEstiloGrafico.this.dialogo);
		
				    if (returnVal == JFileChooser.APPROVE_OPTION) 
				    {
				    	File file = fileChooser.getSelectedFile();	
				    	PanelEstiloGrafico.this.campoURL.setText ( file.getAbsolutePath() );
				    } 
				    else 
				    {				    	
				    }
				}
			});
		
						
		constraints.gridx = 2; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		
		this.add( botonSelectorImagen, constraints );
		
		
		
		
		
		


		
		// Agregamos la etiqueta "Campo"
		JLabel etiquetaCampo = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoGrafico.etiquetaCampo" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 3; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.EAST;
		
		this.add( etiquetaCampo, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		// Agregamos el campo "Campo"
		String [] valoresCampo = 
		{
			"TIPO_CASA", "TIPO_UBICACIÓN", "NUMERO_PISOS"	
		};
		this.campoCampo = new JComboBox ( valoresCampo );
		
		constraints.gridx = 1; 
		constraints.gridy = 3; 
		constraints.gridwidth = 3; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		this.add( this.campoCampo, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		
		// Agregamos la etiqueta de "Color"
		JLabel etiquetaColor = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.etiquetaColor" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 4; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		constraints.anchor = GridBagConstraints.EAST;	
		
		this.add ( etiquetaColor, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		
		// Creamos el boton para elegir color
		JButton botonSelectorColor = new JButton ( "   " );
				
		botonSelectorColor.addActionListener ( new ActionListener()
		{
			JColorChooser selectorColor = new JColorChooser( Color.black );

			public void actionPerformed ( ActionEvent e )
			{
		        JDialog dialog = JColorChooser.createDialog ( PanelEstiloGrafico.this.dialogo,
		        		"Color", true, selectorColor, new ActionListener() 
		        {
		        	public void actionPerformed(ActionEvent e) 		            
		        	{
		        		PanelEstiloGrafico.this.reglaPintadoGrafico.setColor ( selectorColor.getColor() );
		            }
		        }, null);
		        
		        dialog.setVisible(true);
			}
		});
		        
		
		
		constraints.gridx = 1; 
		constraints.gridy = 4; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.WEST;
		
		this.add ( botonSelectorColor, constraints );
		
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		

		

		// Agregamos la etiqueta "Escalado"
		JLabel etiquetaEscalado = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoGrafico.etiquetaEscalado" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 5; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.EAST;
		
		this.add( etiquetaEscalado, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		// Agregamos el campo "Escalado"
		this.campoEscalado = new JTextField ( 10 );
		
		constraints.gridx = 1; 
		constraints.gridy = 5; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		this.add( this.campoEscalado, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		

		

		// Agregamos la etiqueta "Radio"
		JLabel etiquetaRadio = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoGrafico.etiquetaRadio" ) );
		
		constraints.gridx = 2; 
		constraints.gridy = 5; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.EAST;
		
		this.add( etiquetaRadio, constraints );
		
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		// Agregamos el campo "Radio"
		this.campoRadio = new JTextField ( 10 );
		
		constraints.gridx = 3; 
		constraints.gridy = 5; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		this.add( this.campoRadio, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		


		// Agregamos la etiqueta "Campo texto"
		JLabel etiquetaCampoTexto = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoGrafico.etiquetaCampoTexto" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 6; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.EAST;
		
		this.add( etiquetaCampoTexto, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		// Agregamos el campo "Campo texto"
		String [] valoresCampoTexto =
		{
			"TIPO_CASA", "TIPO_UBICACIÓN", "NUMERO_PISOS"	
		};
		this.desplegableCampoTexto = new JComboBox ( valoresCampoTexto );
		
		constraints.gridx = 1; 
		constraints.gridy = 6; 
		constraints.gridwidth = 3; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		this.add( this.desplegableCampoTexto, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
	}
	
	
	
	
	
	
	
	
}
