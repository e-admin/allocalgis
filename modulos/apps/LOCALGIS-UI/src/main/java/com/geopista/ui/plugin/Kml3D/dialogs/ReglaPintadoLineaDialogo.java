/**
 * ReglaPintadoLineaDialogo.java
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
import com.geopista.ui.plugin.Kml3D.datos.ReglaPintadoLinea;
import com.vividsolutions.jump.I18N;

/**
 * Crea un dialogo para la creacion y modificacion de las reglas 
 * de dibujado para una linea
 * 
 * @author David Vicente
 *
 */
public class ReglaPintadoLineaDialogo extends ReglaPintadoDialogo {

	
	public static final int DIM_X = 470;
	public static final int DIM_Y = 500;
	
	
	private JPanel panelPrincipalReglaPintado = null;
	
	
	private ReglaPintadoLinea reglaPintadoLinea = null;
	
	
	
	public ReglaPintadoLineaDialogo ()
	{		
		this ( new ReglaPintadoLinea() );
	}
	
	
	
	public ReglaPintadoLineaDialogo ( ReglaPintadoLinea reglaPintadoLinea )
	{
		super ( reglaPintadoLinea );
		this.reglaPintadoLinea = reglaPintadoLinea;
		this.inicializar();
	}
	
	

	
	/**
	 * Inicializamos el dialogo
	 * 
	 * @return void
	 */
	private void inicializar() 
	{
		this.setModal ( true );  
		this.setTitle ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.tituloDialogo" ) );
		this.setResizable ( true );  
		this.setSize ( DIM_X, DIM_Y );
		this.setContentPane ( this.creaPanelReglaPintadoLinea() );
		
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
	
	
	
	
	
	
	/**
	 * Crea el panel principal de la regla de pintado de lineas
	 * 
	 * @return
	 */
	private JPanel creaPanelReglaPintadoLinea()
	{
		if ( this.panelPrincipalReglaPintado == null )
		{
			this.panelPrincipalReglaPintado = new JPanel ( new GridBagLayout() );
		}
		else
		{
			this.panelPrincipalReglaPintado.removeAll();
		}
		
		
		GridBagConstraints constraints = new GridBagConstraints();

		
		
		
		// Agregamos el campo para editar el nombre
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		this.panelPrincipalReglaPintado.add ( this.creaPanelNombreRegla(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		// Agregamos el campo para editar los filtros
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		this.panelPrincipalReglaPintado.add ( this.creaPanelFiltro(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		

		// Agregamos el campo para editar los estilos
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		this.panelPrincipalReglaPintado.add ( this.creaPanelEstilo(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		

		
		

		// Agregamos el campo para establecer las alturas
		constraints.gridx = 0; 
		constraints.gridy = 3; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		
		this.panelPrincipalReglaPintado.add( this.creaPanelAltura(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		

		// Agregamos el campo con los botones de aceptar y cancelar
		constraints.gridx = 0; 
		constraints.gridy = 4; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 				
		constraints.anchor = GridBagConstraints.CENTER;		
		
		this.panelPrincipalReglaPintado.add ( this.creaPanelBotonesAceptarCancelar(), constraints );
				
		constraints.fill = GridBagConstraints.NONE;

		
		
		
		return this.panelPrincipalReglaPintado;
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
		
		panelEstilo.add( this.creaPanelColorAncho(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		// Agregamos el campo para editar la opacidad
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelEstilo.add( this.creaPanelOpacidad(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		// Agregamos el campo para indicar si requiere extrusión
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.WEST;
		
		panelEstilo.add( this.creaPanelExtrusion(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
				
		
		
		
		//Ponemos el borde con la palabra "Estilo" al panel
		
		TitledBorder bordeEstilo = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.bordeEstilo" ) );
		panelEstilo.setBorder ( bordeEstilo );
		
		
		
		return panelEstilo;
	}
	
	
	
	
	
	
	/** 
	 * Crea el panel para seleccionar el color a aplicar a la linea y el ancho
	 * 
	 * @return
	 */
	private JPanel creaPanelColorAncho()
	{
		JPanel panelcolorAncho = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		
		
		// Agregamos la etiqueta de "Color"
		JLabel etiquetaColor = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.etiquetaColor" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(3, 3, 3, 3);		
		
		panelcolorAncho.add ( etiquetaColor, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		
		// Creamos el boton para elegir color
		JButton botonSelectorColor = new JButton ( "   " );
				
		botonSelectorColor.addActionListener ( new ActionListener()
		{
			JColorChooser selectorColor = new JColorChooser( Color.black );

			public void actionPerformed ( ActionEvent e )
			{
		        JDialog dialog = JColorChooser.createDialog ( ReglaPintadoLineaDialogo.this,
		        		"Color", true, selectorColor, new ActionListener() 
		        {
		        	public void actionPerformed(ActionEvent e) 		            
		        	{
		        		ReglaPintadoLineaDialogo.this.reglaPintadoLinea.setColor ( selectorColor.getColor() );
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
		
		panelcolorAncho.add ( botonSelectorColor, constraints );
		
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		
		// Creamos la etiqueta del Ancho
		JLabel etiquetaAncho = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.etiquetaAncho" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 							
		
		panelcolorAncho.add ( etiquetaAncho, constraints );
		




		
		
		// Creamos el desplegable del tamaño para elegir operador
		String [] valoresAncho = { "0.1", "0.2", "0.5", "1.0", "2.0", "3.0", "4.0", "5.0" };
		
		JComboBox desplegableAncho = new JComboBox ( valoresAncho );
		desplegableAncho.setSelectedIndex ( 3 );
		
		constraints.gridx = 1; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		
		panelcolorAncho.add ( desplegableAncho, constraints );
				
		
		
		
		
		
		
		// Creamos el desplegable del tamaño para elegir operador
		String [] valoresOperadorTamanno = {
				"",
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.suma" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.resta" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.multiplicacion" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.division" ),				
		};
		
		JComboBox desplegableOperador = new JComboBox ( valoresOperadorTamanno );
		
		
		constraints.gridx = 2; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		
		panelcolorAncho.add ( desplegableOperador, constraints );
	
		
		
		
		
		
		
		// Creamos el primer desplegable del tamaño, para elegir el campo
		String [] valoresCampoAncho = {
				"",
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.idAmbitoPlan" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.idMunicipio" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.idPlan" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.idClasificacion" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.indiceEdificabilidad" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.edificabilidadM2" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.area" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.perimetro" )
		};
		
		JComboBox desplegableCampo = new JComboBox ( valoresCampoAncho );				
		
		constraints.gridx = 3; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 								
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelcolorAncho.add ( desplegableCampo, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
				
		return panelcolorAncho;
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
		JLabel etiquetaOpacidad = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.etiquetaOpacidad" ) );
		
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
	 * Crea el panel para indicar si requiere extrusión o no
	 * 
	 * @return
	 */
	private JPanel creaPanelExtrusion()
	{
		JPanel panelExtrusion = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
				
		
		
		// Creamos la etiqueta con "Extrusion"
		JLabel etiquetaExtrusion = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.etiquetaExtrusion" ) );
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelExtrusion.add ( etiquetaExtrusion, constraints );
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		// Creamos el desplegable de la extrusión
		String [] eleccion = new String[] { 
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.si" ),
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoLinea.no" )
		};
		JComboBox desplegableExtrusion = new JComboBox ( eleccion );
		desplegableExtrusion.setSelectedIndex ( 1 );	
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelExtrusion.add ( desplegableExtrusion, constraints );
		
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		return panelExtrusion;
	}
	
	
	
	
}
