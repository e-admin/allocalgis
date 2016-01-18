/**
 * ReglaTematicaPuntoDialogo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.ReglaPintadoGrafico;
import com.geopista.ui.plugin.Kml3D.datos.ReglaPintadoModelo3D;
import com.geopista.ui.plugin.Kml3D.datos.ReglaPintadoPunto;
import com.vividsolutions.jump.I18N;

/**
 * Dialogo para editar las reglas tematicas de un punto 
 * 
 * @author David Vicente
 *
 */
public class ReglaTematicaPuntoDialogo extends ReglaTematicaDialogo {

	
	public static final int DIM_X = 500;
	public static final int DIM_Y = 770;
	
	
	
	
	private JPanel panelEstilo;
	private JPanel panelEstiloPunto;
	private JPanel panelEstiloModelo3D;
	private JPanel panelEstiloGrafico;
	
	private JComboBox desplegableTiposPunto;
	
	
	private ReglaPintadoPunto reglaPintadoPunto = null;
	private ReglaPintadoModelo3D reglaPintadoModelo3D = null;
	private ReglaPintadoGrafico reglaPintadoGrafico = null;
	
	
	
	
	
	
	public ReglaTematicaPuntoDialogo()
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
		this.setTitle ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.tituloDialogo" ) );
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
	
	
	
	/**
	 * Inicializa los componentes
	 */
	private void inicializarComponentes()
	{
		this.panelEstiloPunto = new PanelEstiloPunto ( this, this.reglaPintadoPunto );
		this.panelEstiloModelo3D = new PanelEstiloModelo3D ( this, this.reglaPintadoModelo3D );
		this.panelEstiloGrafico = new PanelEstiloGrafico ( this, this.reglaPintadoGrafico );
		
		String [] valoresTiposPunto = 
		{
			"",
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.desplegableTipoPunto" ),
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.desplegableTipoModelo3D" ),
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.desplegableTipoGrafico" )			
		};		
		this.desplegableTiposPunto = new JComboBox ( valoresTiposPunto );
		
		this.desplegableTiposPunto.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				ReglaTematicaPuntoDialogo.this.refrescarPanelEstilo();
			}
			
		}
		);
	}
	
	
	
	
	/**
	 * Refresca el panel de la ventana
	 */
	private void refrescarPanelEstilo()
	{
		this.creaPanelEstilo();
		this.panelEstilo.updateUI();
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
		constraints.insets = new Insets(1, 1, 1, 1);
		
		panelReglaTematica.add ( this.creaPanelComun(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
				
		
		
		
		// Agregamos el campo de estilo
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelReglaTematica.add ( this.creaPanelEstilo(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
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
	 * Crea el panel de estilo
	 * 
	 * @return
	 */
	private JPanel creaPanelEstilo()
	{
		if ( this.panelEstilo == null )
		{
			this.panelEstilo = new JPanel ( new GridBagLayout () );
		}
		else
		{
			this.panelEstilo.removeAll();
		}
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		// Agregamos el desplegable con el tipo de Punto
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.anchor = GridBagConstraints.CENTER;		
		
		panelEstilo.add( this.desplegableTiposPunto, constraints );
		
		constraints.weightx = 0.0;		
		constraints.anchor = GridBagConstraints.CENTER;		
		
		
		
		// Cargamos el panel dependiendo del tipo de punto que se ha seleccionado
		JPanel panelEstiloConcreto;
		String tipoSeleccionado = this.desplegableTiposPunto.getSelectedItem().toString();
		
		if ( tipoSeleccionado.equals ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.desplegableTipoPunto" ) ) )
		{
			panelEstiloConcreto = this.panelEstiloPunto;
		}
		else if ( tipoSeleccionado.equals ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.desplegableTipoModelo3D" ) ) )
		{
			panelEstiloConcreto = this.panelEstiloModelo3D;
		}
		else if ( tipoSeleccionado.equals ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.desplegableTipoGrafico" ) ) )
		{
			panelEstiloConcreto = this.panelEstiloGrafico;
		}
		else
		{
			panelEstiloConcreto = new JPanel();
		}
		
		
		
		// Agregamos el panel seleccionado
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		
		panelEstilo.add( panelEstiloConcreto, constraints );
		
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.fill = GridBagConstraints.NONE;		

		
		
		
		//Ponemos el borde con la palabra "Estilo" al panel		
		TitledBorder bordeEstilo = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.bordeEstilo" ) );
		panelEstilo.setBorder ( bordeEstilo );
				
		
		return panelEstilo;
	}
	
	
	
	

	
	
	
	
/*	
	private JPanel creaPanelEstilo()
	{
		JPanel panelEstilo = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		
		// Agregamos el campo para editar la rotacion y el tamaño del punto
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelEstilo.add( this.creaPanelRotacionTamanno(), constraints );
		
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
		
		
		
		
		
		// Agregamos el campo para editar la imagen que representa los puntos
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		
		panelEstilo.add( this.creaPanelIcono(), constraints );
		
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.fill = GridBagConstraints.NONE;
				
		
		
		
		//Ponemos el borde con la palabra "Estilo" al panel
		
		TitledBorder bordeEstilo = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.bordeEstilo" ) );
		panelEstilo.setBorder ( bordeEstilo );
		
		
		
		return panelEstilo;
	}
	
	
	
	
	
	

	

	private JPanel creaPanelRotacionTamanno()
	{
		JPanel panelRotacionTamanno = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		// Agregamos la etiqueta de "Rotacion"
		JLabel etiquetaRotacion = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.etiquetaRotacion" ) );
		
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
		JLabel etiquetaTamanno = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.etiquetaTamanno" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 							
		
		panelRotacionTamanno.add ( etiquetaTamanno, constraints );
		


		
		// Creamos el primer desplegable del tamaño, para elegir el campo
		String [] valoresCampoTamanno = {
				"",
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.idAfeccionSectorial" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.idMunicipio" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.idPlan" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.area" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.perimetro" )
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
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.suma" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.resta" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.multiplicacion" ),
				I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.division" ),				
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
	
	
	
	
	
	
	

	private JPanel creaPanelOpacidad()
	{
		JPanel panelOpacidad = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		
		// Creamos la etiqueta de opacidad
		JLabel etiquetaOpacidad = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.etiquetaOpacidad" ) );
		
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
	
	
	
	
	
	

	private JPanel creaPanelIcono()
	{
		JPanel panelIcono = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		// Creamos el panel que muestra la imagen
		JPanel panelImagen = new JPanel ( new GridBagLayout() );
		
		
		// Creamos la etiqueta con "Icono predefinido"
		JLabel etiquetaIconoPredefinido = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.etiquetaOpacidad" ) );
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
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
				
		panelImagen.add ( this.scroll, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		// Agregamos el panel de la imagen al panel global del icono
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelIcono.add( panelImagen, constraints );
				
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		// Creamos el campo para cargar la URL
		JPanel panelURL = new JPanel ( new GridBagLayout() );
		
		
		
		// Agregamos la etiqueta "URL"
		JLabel etiquetaURL = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.etiquetaURL" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.EAST;
		
		panelURL.add( etiquetaURL, constraints );
		
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
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
				    int returnVal = fileChooser.showSaveDialog(ReglaTematicaPuntoDialogo.this);
		
				    if (returnVal == JFileChooser.APPROVE_OPTION) 
				    {
				    	File file = fileChooser.getSelectedFile();	
				    	ReglaTematicaPuntoDialogo.this.campoURL.setText ( file.getAbsolutePath() );
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
		JLabel etiquetaFormato = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematicaPunto.etiquetaFormato" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.EAST;
		
		panelURL.add( etiquetaFormato, constraints );
		
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		
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
		constraints.fill = GridBagConstraints.BOTH;
		
		panelIcono.add ( panelURL, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		return panelIcono;
	}
*/	
	
	
	
}
