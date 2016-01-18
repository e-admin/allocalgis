/**
 * PanelEstiloPunto.java
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
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.ReglaPintadoPunto;
import com.vividsolutions.jump.I18N;

/**
 * Panel de estilo para definir las caracteristicas de un punto
 * 
 * @author David Vicente
 *
 */
public class PanelEstiloPunto extends JPanel {

	
	private JDialog dialogo;
	
	private JTextField campoTamanno = null;
	private JTextField campoURL = null;
	private JTextField campoFormato = null;
	private JList iconsList = null;
	private JScrollPane scroll = null;
	
	
	private ReglaPintadoPunto reglaPintadoPunto = null;
	
	
	
	
	public PanelEstiloPunto ( JDialog dialogo, ReglaPintadoPunto reglaPintadoPunto )
	{
		super ( new GridBagLayout() );
		this.dialogo = dialogo;
		this.reglaPintadoPunto = reglaPintadoPunto;
		
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
		
		
		
		// Agregamos el campo para editar la rotacion y el tamaño del punto
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		this.add( this.creaPanelRotacionTamanno(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		// Agregamos el campo para editar la opacidad
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		this.add( this.creaPanelOpacidad(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		// Agregamos el campo para editar la imagen que representa los puntos
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		
		this.add( this.creaPanelIcono(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
	}
	
	
	
	
	
	
	/** 
	 * Crea el panel para configurar la rotacion y el tamaño del punto 
	 * 
	 * @return
	 */
	private JPanel creaPanelRotacionTamanno()
	{
		JPanel panelRotacionTamanno = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		// Agregamos la etiqueta de "Rotacion"
		JLabel etiquetaRotacion = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.etiquetaRotacion" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(3, 3, 3, 3);		
		
		panelRotacionTamanno.add ( etiquetaRotacion, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		//Creamos el desplegable de rotacion
		String [] valoresRotacion = { "0.0", "45.0", "90.0", "135.0", "180.0", "225.0", "270.0", "315.0", "360.0" };
		JComboBox desplegableRotacion = new JComboBox(valoresRotacion);
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.anchor = GridBagConstraints.WEST;
		
		panelRotacionTamanno.add ( desplegableRotacion, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		// Creamos la etiqueta del tamaño
		JLabel etiquetaTamanno = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.etiquetaTamanno" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 							
		
		panelRotacionTamanno.add ( etiquetaTamanno, constraints );
		


		
		// Creamos el primer desplegable del tamaño, para elegir el campo
		String [] valoresCampoTamanno = {
				"",
				I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.idAfeccionSectorial" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.idMunicipio" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.idPlan" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.area" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.perimetro" )
		};
		
		JComboBox desplegableCampo = new JComboBox ( valoresCampoTamanno );
		
		
		constraints.gridx = 1; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		
		panelRotacionTamanno.add ( desplegableCampo, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;		
		
		
		
		
		
		// Creamos el desplegable del tamaño para elegir operador
		String [] valoresOperadorTamanno = {
				"",
				I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.suma" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.resta" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.multiplicacion" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.division" ),				
		};
		
		JComboBox desplegableOperador = new JComboBox ( valoresOperadorTamanno );
		
		
		constraints.gridx = 2; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		
		panelRotacionTamanno.add ( desplegableOperador, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		// Creamos el campo para introducir el tamaño
		this.campoTamanno = new JTextField ( 4 );
		this.campoTamanno.setText ( "5.0" );		
		
		constraints.gridx = 3; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelRotacionTamanno.add ( this.campoTamanno, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
				
		return panelRotacionTamanno;
	}
	
	
	
	
	
	
	
	/** 
	 * Crea el panel para configurar la opacidad 
	 * 
	 * @return
	 */
	private JPanel creaPanelOpacidad()
	{
		JPanel panelOpacidad = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		
		// Creamos la etiqueta de opacidad
		JLabel etiquetaOpacidad = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.etiquetaOpacidad" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 				
		
		panelOpacidad.add ( etiquetaOpacidad, constraints );
		

		
		
		
		// Creamos el slide de opacidad
		Dictionary<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(0), new JLabel("0.0"));
		labelTable.put(new Integer(10), new JLabel("0.1"));
		labelTable.put(new Integer(20), new JLabel("0.2"));
		labelTable.put(new Integer(30), new JLabel("0.3"));
		labelTable.put(new Integer(40), new JLabel("0.4"));
		labelTable.put(new Integer(50), new JLabel("0.5"));
		labelTable.put(new Integer(60), new JLabel("0.6"));
		labelTable.put(new Integer(70), new JLabel("0.7"));
		labelTable.put(new Integer(80), new JLabel("0.8"));
		labelTable.put(new Integer(90), new JLabel("0.9"));
		labelTable.put(new Integer(100), new JLabel("1.0"));		
		
		
		JSlider slideOpacidad = new JSlider ( JSlider.HORIZONTAL );
		slideOpacidad.setMajorTickSpacing(10);
		slideOpacidad.setMinorTickSpacing(5);
		slideOpacidad.setPaintLabels(true);
		slideOpacidad.setPaintTicks(true);
		slideOpacidad.setValue(100);
		
		slideOpacidad.setLabelTable(labelTable);
		
		
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelOpacidad.add ( slideOpacidad, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;	
		
		
		
		
		return panelOpacidad;
	}
	
	
	
	
	
	
	/** 
	 * Crea el panel para cargar y configurar el icono
	 * 
	 * @return
	 */
	private JPanel creaPanelIcono()
	{
		JPanel panelIcono = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		// Creamos el panel que muestra la imagen
		JPanel panelImagen = new JPanel ( new GridBagLayout() );
		
		
		// Creamos la etiqueta con "Icono predefinido"
		JLabel etiquetaIconoPredefinido = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.iconoPredefinido" ) );
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelImagen.add ( etiquetaIconoPredefinido, constraints );
					
		
		
		
		// Creamos la lista de iconos
		String [] mesg = new String[] { "Instale librería de símbolos." };
		this.iconsList = new JList ( mesg );
		this.scroll = new JScrollPane();
		this.scroll.setSize ( 60,100 );
		this.scroll.getViewport().add ( this.iconsList );
		this.iconsList.setEnabled(false);
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		
		panelImagen.add ( this.scroll, constraints );
		
		
		
		
		// Agregamos el panel de la imagen al panel global del icono
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		
		panelIcono.add( panelImagen, constraints );
		
		
		
		
		
		// Creamos el campo para cargar la URL
		JPanel panelURL = new JPanel ( new GridBagLayout() );
		
		
		
		// Agregamos la etiqueta "URL"
		JLabel etiquetaURL = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.etiquetaURL" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelURL.add( etiquetaURL, constraints );
		
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		// Agregamos el campo URL
		this.campoURL = new JTextField ( 10 );
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelURL.add( this.campoURL, constraints );
		
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
				    int returnVal = fileChooser.showSaveDialog ( PanelEstiloPunto.this.dialogo );
		
				    if (returnVal == JFileChooser.APPROVE_OPTION) 
				    {
				    	File file = fileChooser.getSelectedFile();	
				    	PanelEstiloPunto.this.campoURL.setText ( file.getAbsolutePath() );
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
		
		panelURL.add( botonSelectorImagen, constraints );
		
		
		
		
		
		
		// Agregamos la etiqueta "Formato"
		JLabel etiquetaFormato = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.panelEstiloPunto.etiquetaFormato" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelURL.add( etiquetaFormato, constraints );
		
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		// Agregamos el campo formato
		this.campoFormato = new JTextField ( 10 );
		
		constraints.gridx = 1; 
		constraints.gridy = 1; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;		
		constraints.fill = GridBagConstraints.BOTH;
		
		panelURL.add( this.campoFormato, constraints );
		
		constraints.weightx = 0.0;		
		constraints.fill = GridBagConstraints.NONE;
		
		


		// Agregamos el panel de la URL al panel del icono
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;	
		
		panelIcono.add ( panelURL, constraints );
		
		constraints.weightx = 0.0;	
		
		
		return panelIcono;
	}
	
	
	
	
	
}
