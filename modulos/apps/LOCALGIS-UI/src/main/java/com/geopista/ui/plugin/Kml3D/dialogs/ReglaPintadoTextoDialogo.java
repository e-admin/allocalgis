/**
 * ReglaPintadoTextoDialogo.java
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
 * Crea un dialogo para la creacion y modificacion de las reglas 
 * de dibujado para el texto
 * 
 * @author David Vicente
 *
 */
public class ReglaPintadoTextoDialogo extends ReglaPintadoDialogo {

	
	public static final int DIM_X = 440;
	public static final int DIM_Y = 550;
	
	
	private JPanel panelPrincipalReglaPintado = null;
	
	
	private ReglaPintadoTexto reglaPintadoTexto = null;
	
	
	
	public ReglaPintadoTextoDialogo ()
	{		
		this ( new ReglaPintadoTexto() );
	}
	
	
	
	public ReglaPintadoTextoDialogo ( ReglaPintadoTexto reglaPintadoTexto )
	{
		super ( reglaPintadoTexto );
		this.reglaPintadoTexto = reglaPintadoTexto;
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
		this.setTitle ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.tituloDialogo" ) );
		this.setResizable ( true );  
		this.setSize ( DIM_X, DIM_Y );
		this.setContentPane ( this.creaPanelReglaPintadoTexto() );
		
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
	private JPanel creaPanelReglaPintadoTexto()
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
		
		
		
		// Agregamos la etiqueta "Etiqueta"
		JLabel etiquetaEtiqueta = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.etiquetaEtiqueta" ) );
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 				
		constraints.insets = new Insets(6, 6, 6, 6);
		
		panelEstilo.add( etiquetaEtiqueta, constraints );
		
		
		
		
		
		// Agregamos el campo para seleccionar la etiqueta	
		String [] valoresCampoEtiqueta = {				
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaGeometria" ), 
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaIdParcela" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaReferenciaCatastral" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaIdMunicipio" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaIdDistrito" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaCodigoParcela" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaCodigoPoligono" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaIdVia" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaPrimerNumero" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaPrimeraLetra" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaSegundoNumero" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaSegundaLetra" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaKilometro" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaBloque" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaDireccionNoEstructurada" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaCodigoPostal" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaCodigoDelegacionMEH" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaLongitud" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaArea" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaFechaAlta" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaFechaBaja" ),
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaModificado" ), 
				I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintadoTexto.etiquetaRevisionActual" )
		};
		
		JComboBox desplegableCampoEtiqueta = new JComboBox ( valoresCampoEtiqueta );
		desplegableCampoEtiqueta.setSelectedIndex ( 0 );
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		
		panelEstilo.add( desplegableCampoEtiqueta, constraints );
		
		
		
		
		
		
		
		// Agregamos la etiqueta de "Color"
		JLabel etiquetaColor = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.etiquetaColor" ) );
		
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
		        JDialog dialog = JColorChooser.createDialog ( ReglaPintadoTextoDialogo.this,
		        		"Color", true, selectorColor, new ActionListener() 
		        {
		        	public void actionPerformed(ActionEvent e) 		            
		        	{
		        		ReglaPintadoTextoDialogo.this.reglaPintadoTexto.setColor ( selectorColor.getColor() );
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
		TitledBorder bordeEstilo = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.bordeEstilo" ) );
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
		JLabel etiquetaFuente = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.etiquetaFuente" ) );
		
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
		JLabel etiquetaEstilo = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.etiquetaEstilo" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		
		panelFuente.add ( etiquetaEstilo, constraints );
		
		
		
		
		
		// Agregamos el desplegable para estilo normal o italic
		String [] valoresEstilo = 
		{ 	
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.desplegableEstiloNormal" ),
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.desplegableEstiloItalic" )
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
		JLabel etiquetaGrosor = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.etiquetaGrosor" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		
		panelFuente.add ( etiquetaGrosor, constraints );
		
		
		
		
		
		// Agregamos el desplegable para grosor normal o bold
		String [] valoresGrosor = 
		{ 	
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.desplegableGrosorNormal" ),
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.desplegableGrosorBold" )
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
		JLabel etiquetaTamanno = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.etiquetaTamanno" ) );
		
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
		TitledBorder bordeFuente = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoTexto.bordeFuente" ) );
		panelFuente.setBorder ( bordeFuente );
		
		
		
		
		return panelFuente;
	}
	
	
	
	
	
	
}
