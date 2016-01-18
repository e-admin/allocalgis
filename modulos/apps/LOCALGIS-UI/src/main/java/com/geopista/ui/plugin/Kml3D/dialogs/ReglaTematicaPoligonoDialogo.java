/**
 * ReglaTematicaPoligonoDialogo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.ReglaPintadoPoligono;
import com.vividsolutions.jump.I18N;

/**
 * Dialogo para editar las reglas tematicas de un polígono 
 * 
 * @author David Vicente
 *
 */
public class ReglaTematicaPoligonoDialogo extends ReglaTematicaDialogo {

	
	
	
	private ReglaPintadoPoligono reglaPintadoPoligono = null;
	
	
	
	public static final int DIM_X = 500;
	public static final int DIM_Y = 760;
	
	
	
	public ReglaTematicaPoligonoDialogo()
	{
		this.inicializar();
	}
	
	
	

	/**
	 * Inicializa los elementos
	 */
	private void inicializar()
	{
		this.inicializarComponentes();
		this.setModal ( true );  
		this.setTitle ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.tituloDialogo" ) );
		this.setResizable ( true );  
		this.setSize ( DIM_X, DIM_Y );
		this.setContentPane ( this.creaPanelReglaTematicaPunto() );
		
		//Se localiza el dialogo en la pantalla
		// Se obtienen las dimensiones en pixels de la pantalla.
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Se obtienen las dimensiones en pixels de la ventana.
        Dimension ventana = this.getSize();
        
        // Una cuenta para situar la ventana en el centro de la pantalla.
        this.setLocation(
            (pantalla.width - ventana.width) / 2,
            (pantalla.height - ventana.height) / 2);
	}
	
	
	
	private void inicializarComponentes()
	{
		
	}
	
	
	
	
	/**
	 * Crea el panel principal de la regla tematica
	 * 
	 * @return
	 */
	private JPanel creaPanelReglaTematicaPunto()
	{
		JPanel panelReglaTematica = new JPanel ( new GridBagLayout() );
		

		GridBagConstraints constraints = new GridBagConstraints();

		
		
		// Agregamos el panel comun para reglas tematicas
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelReglaTematica.add ( this.creaPanelComun(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
				
		
		
		
		// Agregamos el campo de estilo
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelReglaTematica.add ( this.creaPanelEstilo(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;


		
		
		
		// Agregamos el panel con los botones aceptar y cancelar
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelReglaTematica.add ( this.creaPanelAltura(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		
		// Agregamos el panel con los botones aceptar y cancelar
		constraints.gridx = 0; 
		constraints.gridy = 3; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		
		panelReglaTematica.add ( this.creaPanelBotonesAceptarCancelar(), constraints );		
		
		constraints.weightx = 0.0;
		
		
		
				
		return panelReglaTematica;
	}
	
	
	
	
	
	
	
	/**
	 * Crea el panel con las caracteristicas de estilo
	 * 
	 * @return
	 */
	private JPanel creaPanelEstilo()
	{
		JPanel panelEstilo = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		
		// Agregamos el campo para seleccionar el color de la linea
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelEstilo.add( this.creaPanelEstiloLineaPoligono(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		// Agregamos el campo para editar la opacidad
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelEstilo.add( this.creaPanelEstiloRellenoPoligono(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		// Agregamos el campo para indicar si requiere extrusión
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1; 		
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.WEST;
		
		panelEstilo.add( this.creaPanelExtrusion(), constraints );
		
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
				
		
		
		
		//Ponemos el borde con la palabra "Estilo" al panel
		
		TitledBorder bordeEstilo = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.bordeEstilo" ) );
		panelEstilo.setBorder ( bordeEstilo );
		
		
		
		return panelEstilo;
	}
	
	
	
	
	
	
	/** 
	 * Crea el panel para editar el estilo de la línea del polígono
	 * 
	 * @return
	 */
	private JPanel creaPanelEstiloLineaPoligono()
	{
		JPanel panelEstiloLineaPoligono = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		
		
		// Agregamos la etiqueta de "Color"
		JLabel etiquetaColor = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.etiquetaColorLinea" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(3, 3, 3, 3);		
		
		panelEstiloLineaPoligono.add ( etiquetaColor, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		
		// Creamos el boton para elegir color
		JButton botonSelectorColor = new JButton ( "   " );
				
		botonSelectorColor.addActionListener ( new ActionListener()
		{
			JColorChooser selectorColor = new JColorChooser( Color.black );

			public void actionPerformed ( ActionEvent e )
			{
		        JDialog dialog = JColorChooser.createDialog ( ReglaTematicaPoligonoDialogo.this,
		        		"Color", true, selectorColor, new ActionListener() 
		        {
		        	public void actionPerformed(ActionEvent e) 		            
		        	{
		        		ReglaTematicaPoligonoDialogo.this.reglaPintadoPoligono.setColor ( selectorColor.getColor() );
		            }
		        }, null);
		        
		        dialog.setVisible(true);
			}
		});
		        
		
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.WEST;
		
		panelEstiloLineaPoligono.add ( botonSelectorColor, constraints );
		
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		
		// Creamos la etiqueta del ancho
		JLabel etiquetaAncho = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.etiquetaAncho" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 							
		
		panelEstiloLineaPoligono.add ( etiquetaAncho, constraints );
		




		
		
		// Creamos el desplegable del tamaño para elegir operador
		String [] valoresAncho = { "1.0", "2.0", "3.0", "4.0", "5.0" };
		
		JComboBox desplegableAncho = new JComboBox ( valoresAncho );
		desplegableAncho.setSelectedIndex ( 0 );
		
		constraints.gridx = 1; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		
		panelEstiloLineaPoligono.add ( desplegableAncho, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		
		// Creamos el desplegable del tamaño para elegir operador
		String [] valoresOperadorTamanno = {
				"",
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.suma" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.resta" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.multiplicacion" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.division" ),				
		};
		
		JComboBox desplegableOperador = new JComboBox ( valoresOperadorTamanno );
		
		
		constraints.gridx = 2; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		
		panelEstiloLineaPoligono.add ( desplegableOperador, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		
		// Creamos el desplegable del polígono, para elegir el campo
		String [] valoresCampoAncho = {
				"",
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.idAmbitoPlan" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.idMunicipio" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.idPlan" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.idClasificacion" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.indiceEdificabilidad" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.edificabilidadM2" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.area" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.perimetro" )
		};
		
		JComboBox desplegableCampo = new JComboBox ( valoresCampoAncho );
		
		
		constraints.gridx = 3; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		
		panelEstiloLineaPoligono.add ( desplegableCampo, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;		
		
		
		
		
		
		

		// Creamos la etiqueta de opacidad
		JLabel etiquetaOpacidad = new JLabel ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaPoligono.etiquetaOpacidadLinea" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 				
		
		panelEstiloLineaPoligono.add ( etiquetaOpacidad, constraints );
		
		
		
		
		
		
		

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
		constraints.gridy = 2; 
		constraints.gridwidth = 3; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelEstiloLineaPoligono.add ( slideOpacidad, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;	
		
		
		
		TitledBorder borde = new TitledBorder ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaPoligono.bordeLinea" ) );
		panelEstiloLineaPoligono.setBorder ( borde );
		
		
				
		return panelEstiloLineaPoligono;
	}
	
	
	
	
	

	
	

	/** 
	 * Crea el panel para editar el estilo del relleno del polígono
	 * 
	 * @return
	 */
	private JPanel creaPanelEstiloRellenoPoligono()
	{
		JPanel panelEstiloRellenoPoligono = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		
		
		// Agregamos la etiqueta de "Color"
		JLabel etiquetaColor = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPoligono.etiquetaColorRelleno" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(3, 3, 3, 3);		
		
		panelEstiloRellenoPoligono.add ( etiquetaColor, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		
		// Creamos el boton para elegir color
		JButton botonSelectorColor = new JButton ( "   " );
				
		botonSelectorColor.addActionListener ( new ActionListener()
		{
			JColorChooser selectorColor = new JColorChooser( Color.black );

			public void actionPerformed ( ActionEvent e )
			{
		        JDialog dialog = JColorChooser.createDialog ( ReglaTematicaPoligonoDialogo.this,
		        		"Color", true, selectorColor, new ActionListener() 
		        {
		        	public void actionPerformed(ActionEvent e) 		            
		        	{
		        		ReglaTematicaPoligonoDialogo.this.reglaPintadoPoligono.setColor ( selectorColor.getColor() );
		            }
		        }, null);
		        
		        dialog.setVisible(true);
			}
		});
		        
		
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.WEST;
		
		panelEstiloRellenoPoligono.add ( botonSelectorColor, constraints );
		
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		
		// Creamos la etiqueta de opacidad
		JLabel etiquetaOpacidad = new JLabel ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaPoligono.etiquetaOpacidadRelleno" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 				
		
		panelEstiloRellenoPoligono.add ( etiquetaOpacidad, constraints );
		
		
		
		

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
		constraints.gridy = 1; 
		constraints.gridwidth = 3; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelEstiloRellenoPoligono.add ( slideOpacidad, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;	
		
		
		
		
		TitledBorder borde = new TitledBorder ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaPoligono.bordeRelleno" ) );
		panelEstiloRellenoPoligono.setBorder ( borde );		
		
		
		
				
		return panelEstiloRellenoPoligono;
	}
	
	
	
	
	
	
	
	/** 
	 * Crea el panel para indicar si requiere extrusión o no
	 * 
	 * @return
	 */
	private JPanel creaPanelExtrusion()
	{
		JPanel panelExtrusion = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
				
		
		
		// Creamos la etiqueta con "Extrusion"
		JLabel etiquetaExtrusion = new JLabel ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaPoligono.etiquetaExtrusion" ) );
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelExtrusion.add ( etiquetaExtrusion, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		// Creamos el desplegable de la extrusión
		String [] eleccion = new String[] 
		{ 
			I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaPoligono.si" ),
			I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaPoligono.no" )
		};
		JComboBox desplegableExtrusion = new JComboBox ( eleccion );
		desplegableExtrusion.setSelectedIndex ( 1 );	
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.WEST;
		
		panelExtrusion.add ( desplegableExtrusion, constraints );
		
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		return panelExtrusion;
	}
	
	
	
	
	
	
}
