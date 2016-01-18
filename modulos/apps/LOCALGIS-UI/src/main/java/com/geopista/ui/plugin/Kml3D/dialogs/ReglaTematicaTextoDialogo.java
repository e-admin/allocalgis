/**
 * ReglaTematicaTextoDialogo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.ReglaPintadoTexto;
import com.vividsolutions.jump.I18N;

/**
 * Dialogo para editar las reglas tematicas de texto 
 * 
 * @author David Vicente
 *
 */
public class ReglaTematicaTextoDialogo extends ReglaTematicaDialogo {

	
	
	
	private ReglaPintadoTexto reglaPintadoTexto = null;
	
	
	
	public static final int DIM_X = 500;
	public static final int DIM_Y = 680;
	
	
	
	public ReglaTematicaTextoDialogo()
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
		this.setTitle ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.tituloDialogo" ) );
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
		constraints.insets = new Insets(7, 7, 7, 7);
		
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
		
		
		
		// Agregamos la etiqueta "Etiqueta"
		JLabel etiquetaEtiqueta = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.etiquetaEtiqueta" ) );
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 				
		constraints.insets = new Insets(6, 6, 6, 6);
		
		panelEstilo.add( etiquetaEtiqueta, constraints );
		
		
		
		
		
		// Agregamos el campo para seleccionar la etiqueta	
		String [] valoresCampoEtiqueta = {				
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaGeometria" ), 
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaIdParcela" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaReferenciaCatastral" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaIdMunicipio" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaIdDistrito" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaCodigoParcela" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaCodigoPoligono" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaIdVia" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaPrimerNumero" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaPrimeraLetra" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaSegundoNumero" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaSegundaLetra" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaKilometro" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaBloque" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaDireccionNoEstructurada" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaCodigoPostal" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaCodigoDelegacionMEH" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaLongitud" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaArea" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaFechaAlta" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaFechaBaja" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaModificado" ), 
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematicaTexto.etiquetaRevisionActual" )
		};
		
		JComboBox desplegableCampoEtiqueta = new JComboBox ( valoresCampoEtiqueta );
		desplegableCampoEtiqueta.setSelectedIndex ( 0 );
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		
		panelEstilo.add( desplegableCampoEtiqueta, constraints );
		
		
		
		
		
		
		
		// Agregamos la etiqueta de "Color"
		JLabel etiquetaColor = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.etiquetaColor" ) );
		
		constraints.gridx = 2; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.EAST;
		
		panelEstilo.add ( etiquetaColor, constraints );
		
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
		
		// Creamos el boton para elegir color
		JButton botonSelectorColor = new JButton ( "   " );
				
		botonSelectorColor.addActionListener ( new ActionListener()
		{
			JColorChooser selectorColor = new JColorChooser( Color.black );

			public void actionPerformed ( ActionEvent e )
			{
		        JDialog dialog = JColorChooser.createDialog ( ReglaTematicaTextoDialogo.this,
		        		"Color", true, selectorColor, new ActionListener() 
		        {
		        	public void actionPerformed(ActionEvent e) 		            
		        	{
		        		ReglaTematicaTextoDialogo.this.reglaPintadoTexto.setColor ( selectorColor.getColor() );
		            }
		        }, null);
		        
		        dialog.setVisible(true);
			}
		});
		        
		
		
		constraints.gridx = 3; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		
		panelEstilo.add ( botonSelectorColor, constraints );
		
		
		
		
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 4; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelEstilo.add ( this.creaPanelFuente(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
	
		
	
		
		//Ponemos el borde con la palabra "Estilo" al panel		
		TitledBorder bordeEstilo = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.bordeEstilo" ) );
		panelEstilo.setBorder ( bordeEstilo );
		
		
		
		return panelEstilo;
	}
	
	
	
	
	
	
	
	/**
	 * Crea el panel para configurar la fuente
	 * 
	 * @return
	 */
	public JPanel creaPanelFuente()
	{
		JPanel panelFuente = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		
		
		
		// Añade la etiqueta fuente
		JLabel etiquetaFuente = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.etiquetaFuente" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 				
		constraints.insets = new Insets(6, 6, 6, 6);
		
		panelFuente.add ( etiquetaFuente, constraints );


		
		
		
		// Agregamos el desplegable con las fuentes
		DefaultComboBoxModel fontFamilyModel = new DefaultComboBoxModel();
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment ();
		String[] fontFamilyNames = env.getAvailableFontFamilyNames();
		for (int i=0; i< fontFamilyNames.length; i++) 
		{
			fontFamilyModel.addElement(fontFamilyNames[i]);
		}
		
		JComboBox desplegableFuentes = new JComboBox();
		desplegableFuentes.setModel(fontFamilyModel);	
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelFuente.add ( desplegableFuentes, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		

		// Añade la etiqueta estilo
		JLabel etiquetaEstilo = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.etiquetaEstilo" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		
		panelFuente.add ( etiquetaEstilo, constraints );
		
		
		
		
		
		// Agregamos el desplegable para estilo normal o italic
		String [] valoresEstilo = 
		{ 	
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.desplegableEstiloNormal" ),
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.desplegableEstiloItalic" )
		};
		JComboBox desplegableEstilo = new JComboBox ( valoresEstilo );
		
		constraints.gridx = 1; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelFuente.add ( desplegableEstilo, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		


		// Añade la etiqueta grosor
		JLabel etiquetaGrosor = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.etiquetaGrosor" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		
		panelFuente.add ( etiquetaGrosor, constraints );
		
		
		
		
		
		// Agregamos el desplegable para grosor normal o bold
		String [] valoresGrosor = 
		{ 	
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.desplegableGrosorNormal" ),
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.desplegableGrosorBold" )
		};
		JComboBox desplegableGrosor = new JComboBox ( valoresGrosor );
		
		constraints.gridx = 1; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelFuente.add ( desplegableGrosor, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		
		


		// Añade la etiqueta tamaño
		JLabel etiquetaTamanno = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.etiquetaTamanno" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 3; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		
		panelFuente.add ( etiquetaTamanno, constraints );
		
		
		
		
		
		
		// Agregamos el desplegable para el tamaño
		String [] valoresTamanno = 
		{ 
				"4", "6", "8", "9", "10", "11", "12", "14", "16", 
				"18", "20", "22", "24", "26", "28", "36", "48", "72" 
		};
		JComboBox desplegableTamanno = new JComboBox ( valoresTamanno );
		
		constraints.gridx = 1; 
		constraints.gridy = 3; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelFuente.add ( desplegableTamanno, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		//Ponemos el borde "Fuente" al panel		
		TitledBorder bordeFuente = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaTexto.bordeFuente" ) );
		panelFuente.setBorder ( bordeFuente );
		
		
		
		
		return panelFuente;
	}
	
	
	
	
	
	
	
}
