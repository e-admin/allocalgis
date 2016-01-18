/**
 * ReglaPintadoPuntoDialogo.java
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
 * Crea un dialogo para la creacion y modificacion de las reglas 
 * de dibujado para un punto
 * 
 * @author David Vicente
 *
 */
public class ReglaPintadoPuntoDialogo extends ReglaPintadoDialogo {

	
	public static final int DIM_X = 480;
	public static final int DIM_Y = 690;
	
	
	private JPanel panelPrincipalReglaPintado;
	
	private JPanel panelEstilo;
	private JPanel panelEstiloPunto;
	private JPanel panelEstiloModelo3D;
	private JPanel panelEstiloGrafico;
	
	private JComboBox desplegableTiposPunto;

	
	
	private ReglaPintadoPunto reglaPintadoPunto = null;
	private ReglaPintadoModelo3D reglaPintadoModelo3D = null;
	private ReglaPintadoGrafico reglaPintadoGrafico = null;
	
	
	
	
	
	public ReglaPintadoPuntoDialogo ()
	{
		this ( null, null, null );
	}
	
	
	
	public ReglaPintadoPuntoDialogo ( ReglaPintadoPunto reglaPintadoPunto, ReglaPintadoModelo3D reglaPintadoModelo3D, ReglaPintadoGrafico reglaPintadoGrafico )
	{
		super ( reglaPintadoPunto );
		this.reglaPintadoPunto = reglaPintadoPunto;
		this.reglaPintadoModelo3D = reglaPintadoModelo3D;
		this.reglaPintadoGrafico = reglaPintadoGrafico;
		this.inicializar();
	}
	
	

	
	/**
	 * Inicializamos el dialogo
	 * 
	 * @return void
	 */
	private void inicializar() 
	{
		this.inicializarComponentes();
		this.setModal ( true );  
		this.setTitle ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoPunto.tituloDialogo" ) );
		this.setResizable ( true );  
		this.setSize ( DIM_X, DIM_Y );
		this.setContentPane ( this.creaPanelReglaPintadoPunto() );
		
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
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoPunto.desplegableTipoPunto" ),
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoPunto.desplegableTipoModelo3D" ),
			I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoPunto.desplegableTipoGrafico" )			
		};		
		this.desplegableTiposPunto = new JComboBox ( valoresTiposPunto );
		
		this.desplegableTiposPunto.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				ReglaPintadoPuntoDialogo.this.refrescarPanelEstilo();
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
	 * Crea el panel principal de la regla de pintado de puntos
	 * 
	 * @return
	 */
	private JPanel creaPanelReglaPintadoPunto()
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
		
		this.panelPrincipalReglaPintado.add ( this.creaPanelFiltro(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		

		// Agregamos el campo para editar los estilos
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		this.panelPrincipalReglaPintado.add ( this.creaPanelEstilo(), constraints );
		
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
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
		
		this.panelPrincipalReglaPintado.add ( this.creaPanelBotonesAceptarCancelar(), constraints );
				
		constraints.fill = GridBagConstraints.NONE;

		
		
		
		return this.panelPrincipalReglaPintado;
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
		constraints.insets = new Insets(8, 8, 8, 8);
		constraints.anchor = GridBagConstraints.CENTER;		
		
		panelEstilo.add( this.desplegableTiposPunto, constraints );
		
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;		
		
		
		
		// Cargamos el panel dependiendo del tipo de punto que se ha seleccionado
		JPanel panelEstiloConcreto;
		String tipoSeleccionado = this.desplegableTiposPunto.getSelectedItem().toString();
		
		if ( tipoSeleccionado.equals ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoPunto.desplegableTipoPunto" ) ) )
		{
			panelEstiloConcreto = this.panelEstiloPunto;
		}
		else if ( tipoSeleccionado.equals ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoPunto.desplegableTipoModelo3D" ) ) )
		{
			panelEstiloConcreto = this.panelEstiloModelo3D;
		}
		else if ( tipoSeleccionado.equals ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoPunto.desplegableTipoGrafico" ) ) )
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
		TitledBorder bordeEstilo = new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintadoPunto.bordeEstilo" ) );
		panelEstilo.setBorder ( bordeEstilo );
				
		
		return panelEstilo;
	}
	
	
}
