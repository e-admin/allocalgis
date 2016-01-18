/**
 * BeyondDialogo.java
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
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperadorEspacialBeyond;
import com.vividsolutions.jump.I18N;

/**
 * Dialogo que pide los datos del BEYOND
 * 
 * @author David Vicente
 *
 */
public class BeyondDialogo extends JDialog{

	
	private JComboBox desplegableSRS = null;
	private JTextField campoCoorX = null;
	private JTextField campoCoorY = null;
	private JTextField campoCoorZ = null;
	private JTextField campoDistancia = null;
	
	private String textoBeyond = "";
	
	private OperadorEspacialBeyond operadorEspacialBeyond = null;
	
	
	public static final int DIM_X = 370;
	public static final int DIM_Y = 370;
	
	
	public BeyondDialogo()
	{
		this.inicializar();
	}
	
	
	
	public BeyondDialogo ( OperadorEspacialBeyond operadorEspacialBeyond )
	{
		this.operadorEspacialBeyond = operadorEspacialBeyond;
		this.inicializar();
	}
	
	
	
	
	
	/**
	 * Inicializamos el cuadro de dialogo
	 */
	private void inicializar()
	{
		this.inicializarCamposDinamicos();
		this.setModal(true);  
		this.setTitle ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBEYOND.tituloDialogo" ) );
		this.setResizable(true);  
		this.setSize(DIM_X, DIM_Y);
		this.setContentPane ( this.creaPanelDwithin() );
		
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
		
		
		this.campoCoorX = new JTextField ( "0.0" );
		this.campoCoorY = new JTextField ( "0.0" );
		this.campoCoorZ = new JTextField ( "0.0" );
		this.campoDistancia = new JTextField ( "0.0" );
		
		
		if ( this.operadorEspacialBeyond != null )
		{
			this.desplegableSRS.setSelectedItem ( this.operadorEspacialBeyond.getSRS() );
			
			this.campoCoorX.setText ( this.operadorEspacialBeyond.getCoorX() );
			this.campoCoorY.setText ( this.operadorEspacialBeyond.getCoorY() );
			this.campoCoorZ.setText ( this.operadorEspacialBeyond.getCoorZ() );
			this.campoDistancia.setText ( this.operadorEspacialBeyond.getDistancia() );
		}
	}
	
	
	/**
	 * Crea el panel principal de la ventana
	 * 
	 * @return
	 */
	private JPanel creaPanelDwithin()
	{
		JPanel panelPrincipal = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		JPanel panelGmlGeometry = new JPanel ( new GridBagLayout() );
		
		
		
		// Crea el panel con los sistemas de referencia		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.insets = new Insets(7, 7, 7, 7);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelGmlGeometry.add ( this.creaPanelSistemaReferenciaEspacial(), constraints );		
		
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
		
		panelGmlGeometry.add ( this.creaPanelCoordenadasPuntos(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;		
		
		
		
		
		TitledBorder bordeGmlGeometry = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBEYOND.bordeGmlGeometry" ) );
		panelGmlGeometry.setBorder ( bordeGmlGeometry );
		
		
		
		
		// Agrega el panel GML Geometry				
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelPrincipal.add ( panelGmlGeometry, constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;	
		
		
		
		
		
		// Crea el panel con la distancia		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelPrincipal.add ( this.creaPanelDistancia(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		

		// Crea el panel con los botones aceptar y cancelar				
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;		
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelPrincipal.add ( this.creaPanelBotonesAceptarCancelar(), constraints );		
		
		constraints.weightx = 0.0;			
		
		
		
		
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
		JLabel etiquetaSRS = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBEYOND.etiquetaSRS" )  );
	
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.insets = new Insets(7, 7, 7, 7);
		
		panelSistemaReferencia.add (etiquetaSRS , constraints );		
		
		
		
		// Crea el campo para pedir el literal
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;				
		
		panelSistemaReferencia.add ( this.desplegableSRS , constraints );		
				
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;		
		
				
		TitledBorder bordeSistemaEspacial = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBEYOND.bordeSistemaReferencia" ) );
		panelSistemaReferencia.setBorder ( bordeSistemaEspacial );
		
		return panelSistemaReferencia;
	}
	
	
	
	
	
	
	/**
	 * Panel para coordenadas Punto
	 * 
	 * @return
	 */
	private JPanel creaPanelCoordenadasPuntos()
	{
		JPanel panelCoordenadas = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
	
		// Crea la etiqueta 
		JLabel etiquetaCoorX = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBEYOND.etiquetaCoorX" )  );
	
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		constraints.insets = new Insets(4, 4, 4, 4);
		
		panelCoordenadas.add ( etiquetaCoorX , constraints );		
		
		
		
		// Crea el campo
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelCoordenadas.add ( this.campoCoorX , constraints );	
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		
		
		// Crea la etiqueta 
		JLabel etiquetaCoorY = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBEYOND.etiquetaCoorY" )  );
	
		constraints.gridx = 2; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 						
		
		panelCoordenadas.add ( etiquetaCoorY , constraints );		
		
		
		
		// Crea el campo
		constraints.gridx = 3; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelCoordenadas.add ( this.campoCoorY , constraints );	
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		
		// Crea la etiqueta 
		JLabel etiquetaCoorZ = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBEYOND.etiquetaCoorZ" )  );
	
		constraints.gridx = 4; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 						
		
		panelCoordenadas.add ( etiquetaCoorZ , constraints );		
		
		
		
		// Crea el campo
		constraints.gridx = 5; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelCoordenadas.add ( this.campoCoorZ , constraints );	
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
				
		
		
		TitledBorder bordeSistemaEspacial = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBEYOND.bordeCoordenadasPunto" ) );
		panelCoordenadas.setBorder ( bordeSistemaEspacial );
		
		
		return panelCoordenadas;
	}
	
	
	
	
	
	

	
	/**
	 * Panel para la distancia
	 * 
	 * @return
	 */
	private JPanel creaPanelDistancia()
	{
		JPanel panelDistancia = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
	
		// Crea la etiqueta 
		JLabel etiquetaDistancia = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBEYOND.etiquetaDistancia" )  );
	
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		constraints.insets = new Insets(4, 4, 4, 4);
		
		panelDistancia.add ( etiquetaDistancia , constraints );		
		
		
		
		// Crea el campo
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelDistancia.add ( this.campoDistancia , constraints );	
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
				
		
		
		TitledBorder bordeDistancia = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoBEYOND.bordeDistancia" ) );
		panelDistancia.setBorder ( bordeDistancia );
		
		
		
		return panelDistancia;
	}
	
	
	
	
	
	/**
	 * Crea el panel con los botones de aceptar y cancelar
	 *   
	 * @return
	 */
	private JPanel creaPanelBotonesAceptarCancelar()
	{
		JPanel panel = new JPanel(new GridLayout(1, 2, 30, 10));
		
		JButton botonAceptar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoBEYOND.botonAceptar"));
		JButton botonCancelar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoBEYOND.botonCancelar"));
		
		
		botonAceptar.addActionListener(new ActionListener()
        {
			public void actionPerformed(ActionEvent evt)
            {
				BeyondDialogo.this.generaTexto();
				BeyondDialogo.this.dispose();
            }
        });
				
		
		botonCancelar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
            	BeyondDialogo.this.dispose();
            }
        });
		
		
		panel.add(botonAceptar);
		panel.add(botonCancelar);
		
		return panel;
	}



	
	/**
	 * Genera el objeto Beyond
	 * @return
	 */
	private void generaTexto() 
	{
		if ( this.operadorEspacialBeyond == null )
		{
			this.operadorEspacialBeyond = new OperadorEspacialBeyond ( OPERADOR_ESPACIAL.BEYOND );			
		}
		
		this.operadorEspacialBeyond.setSRS( this.desplegableSRS.getSelectedItem().toString() );
		this.operadorEspacialBeyond.setCoorX( this.campoCoorX.getText() );
		this.operadorEspacialBeyond.setCoorY( this.campoCoorY.getText() );
		this.operadorEspacialBeyond.setCoorZ( this.campoCoorZ.getText() );
		this.operadorEspacialBeyond.setDistancia ( this.campoDistancia.getText() );		
	}
	
	
	
	public OperadorEspacialBeyond getOperadorEspacialBeyond()
	{
		return this.operadorEspacialBeyond;
	}
	
	
	
	
	
	
}
