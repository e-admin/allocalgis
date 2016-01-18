/**
 * BBoxDialogo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.dialogs.filtro;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperadorEspacial.OPERADOR_ESPACIAL;
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperadorEspacialBbox;
import com.vividsolutions.jump.I18N;

/**
 * Dialogo que pide los datos del BBOX
 * 
 * @author David Vicente
 *
 */
public class BBoxDialogo extends JDialog{

	
	private JComboBox desplegableSRS = null;
	private JTextField campoMinX = null;
	private JTextField campoMaxX = null;
	private JTextField campoMinY = null;
	private JTextField campoMaxY = null;
	
	
	private OperadorEspacialBbox operadorEspacialBbox = null;
	
	public static final int DIM_X = 370;
	public static final int DIM_Y = 230;
	
	
	public BBoxDialogo()
	{
		this.inicializar();
	}
	
	
	
	public BBoxDialogo ( OperadorEspacialBbox operadorEspacialBbox )
	{
		this.operadorEspacialBbox = operadorEspacialBbox;
		this.inicializar();
	}
	
	
	
	/**
	 * Inicializamos el cuadro de dialogo
	 */
	private void inicializar()
	{
		this.inicializarCamposDinamicos();
		this.setModal(true);  
		this.setTitle ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBBOX.tituloDialogo" ) );
		this.setResizable(true);  
		this.setSize(DIM_X, DIM_Y);
		this.setContentPane ( this.creaPanelBBox() );
		
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
	
	
	
	private void inicializarCamposDinamicos()
	{
		String [] valoresSRS = { "EPSG:4326", "EPSG:23028", "EPSG:23029", "EPSG:23030" };
		this.desplegableSRS = new JComboBox ( valoresSRS );
				
		this.campoMinX = new JTextField ( "0.0" );
		this.campoMaxX = new JTextField ( "0.0" );
		this.campoMinY = new JTextField ( "0.0" );
		this.campoMaxY = new JTextField ( "0.0" );
		
		if ( this.operadorEspacialBbox != null )
		{
			this.campoMinX.setText ( this.operadorEspacialBbox.getMinX() );
			this.campoMinY.setText ( this.operadorEspacialBbox.getMinY() );
			this.campoMaxX.setText ( this.operadorEspacialBbox.getMaxX() );
			this.campoMaxY.setText ( this.operadorEspacialBbox.getMaxY() );	
			
			this.desplegableSRS.setSelectedItem ( this.operadorEspacialBbox.getSRS() );
		}
	}
	
	
	
	/**
	 * Crea el panel principal de la ventana
	 * 
	 * @return
	 */
	private JPanel creaPanelBBox()
	{
		JPanel panelPrincipal = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		// Crea el panel con los sistemas de referencia		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelPrincipal.add ( this.creaPanelSistemaReferenciaEspacial(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		// 	Crea el panel con las coordenadas GMLBox
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelPrincipal.add ( this.creaPanelCoordenadasGMLBox(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;		
		
		
		
		
		

		// Crea el panel con los botones aceptar y cancelar		
		
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelPrincipal.add ( this.creaPanelBotonesAceptarCancelar(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;		
		
		
		TitledBorder bordeGmlBox = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBBOX.bordeGmlBox" ) );
		panelPrincipal.setBorder ( bordeGmlBox );
		
		
		
		
		return panelPrincipal;
	}
	
	
	
	
	/**
	 * Panel para el sistema de referencia espacial
	 * 
	 * @return
	 */
	private JPanel creaPanelSistemaReferenciaEspacial()
	{
		JPanel panelSistemaReferencia = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
	
		// Crea la etiqueta "SRS"
		JLabel etiquetaSRS = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBBOX.etiquetaSRS" )  );
	
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 						
		
		panelSistemaReferencia.add (etiquetaSRS , constraints );		
		
		
		
		// Crea el campo para pedir el literal		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		constraints.insets = new Insets(7, 7, 7, 7);
		
		panelSistemaReferencia.add ( this.desplegableSRS , constraints );		
				
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;		
		
				
		TitledBorder bordeSistemaEspacial = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBBOX.bordeSistemaLiteral" ) );
		panelSistemaReferencia.setBorder ( bordeSistemaEspacial );
		
		return panelSistemaReferencia;
	}
	
	
	
	
	
	
	/**
	 * Panel para el sistema de referencia espacial
	 * 
	 * @return
	 */
	private JPanel creaPanelCoordenadasGMLBox()
	{
		JPanel panelCoordenadas = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
	
		// Crea la etiqueta 
		JLabel etiquetaMinX = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBBOX.etiquetaMinX" )  );
	
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		constraints.insets = new Insets(4, 4, 4, 4);
		
		panelCoordenadas.add ( etiquetaMinX , constraints );		
		
		
		
		// Crea el campo		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelCoordenadas.add ( this.campoMinX , constraints );	
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		
		
		// Crea la etiqueta 
		JLabel etiquetaMinY = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBBOX.etiquetaMinY" )  );
	
		constraints.gridx = 2; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 						
		
		panelCoordenadas.add ( etiquetaMinY , constraints );		
		
		
		
		// Crea el campo
		constraints.gridx = 3; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelCoordenadas.add ( this.campoMinY , constraints );	
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		
		// Crea la etiqueta 
		JLabel etiquetaMaxX = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBBOX.etiquetaMaxX" )  );
	
		constraints.gridx = 4; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 						
		
		panelCoordenadas.add ( etiquetaMaxX , constraints );		
		
		
		
		// Crea el campo	
		constraints.gridx = 5; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelCoordenadas.add ( this.campoMaxX , constraints );	
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		
		
		// Crea la etiqueta 
		JLabel etiquetaMaxY = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBBOX.etiquetaMaxY" )  );
	
		constraints.gridx = 6; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 						
		
		panelCoordenadas.add ( etiquetaMaxY , constraints );		
		
		
		
		// Crea el campo		
		constraints.gridx = 7; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelCoordenadas.add ( this.campoMaxY , constraints );	
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
				
		
		
		TitledBorder bordeSistemaEspacial = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBBOX.bordeCoordenadas" ) );
		panelCoordenadas.setBorder ( bordeSistemaEspacial );
		
		
		return panelCoordenadas;
	}
	
	
	
	
	
	
	
	/**
	 * Crea el panel con los botones de aceptar y cancelar
	 *   
	 * @return
	 */
	private JPanel creaPanelBotonesAceptarCancelar()
	{
		JPanel panel = new JPanel(new GridLayout(1, 2, 30, 10));
		
		JButton botonAceptar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoBBOX.botonAceptar"));
		JButton botonCancelar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoBBOX.botonCancelar"));
		
		
		botonAceptar.addActionListener(new ActionListener()
        {
			public void actionPerformed(ActionEvent evt)
            {
				BBoxDialogo.this.generaTexto();
				BBoxDialogo.this.dispose();
            }
        });
				
		
		botonCancelar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
            	BBoxDialogo.this.dispose();
            }
        });
		
		
		panel.add(botonAceptar);
		panel.add(botonCancelar);
		
		return panel;
	}



	public OperadorEspacialBbox dameOperadorEspacialBbox()
	{
		return this.operadorEspacialBbox;
	}
	
	
	
	/**
	 * Genera y devuelve el texto del BBOX
	 * @return
	 */
	private void generaTexto() 
	{		
		if ( this.operadorEspacialBbox == null )
		{
			this.operadorEspacialBbox = new OperadorEspacialBbox ( OPERADOR_ESPACIAL.BBOX );
		}
		
		double minX = Double.parseDouble ( this.campoMinX.getText() );
		double maxX = Double.parseDouble ( this.campoMaxX.getText() );
		double minY = Double.parseDouble ( this.campoMinY.getText() );
		double maxY = Double.parseDouble ( this.campoMaxY.getText() );
		
		if ( minX > maxX )
		{
			double aux = minX;
			minX = maxX;
			maxX = aux;
		}
		
		if ( minY > maxY )
		{
			double aux = minY;
			minY = maxY;
			maxY = aux;
		}
		
		this.operadorEspacialBbox.setSRS ( this.desplegableSRS.getSelectedItem().toString() ); 
		this.operadorEspacialBbox.setMinX(String.valueOf(minX));
		this.operadorEspacialBbox.setMaxX(String.valueOf(maxX));
		this.operadorEspacialBbox.setMinY(String.valueOf(minY));
		this.operadorEspacialBbox.setMaxY(String.valueOf(maxY));	
	}
	
	
	
	public OperadorEspacialBbox getOperadorEspacialBbox()
	{
		return this.operadorEspacialBbox;
	}
	
	
	
	
	
	
}
