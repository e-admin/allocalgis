/**
 * ReglaPintadoDialogo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.ReglaPintado;
import com.geopista.ui.plugin.Kml3D.dialogs.filtro.FiltroDialogo;
import com.vividsolutions.jump.I18N;

/**
 * Clase de la que hereda cualquier dialogo de regla de pintado,
 * para reutilizar cierta funcionalidad
 * 
 * @author David Vicente
 *
 */
public abstract class ReglaPintadoDialogo extends JDialog {

	public static enum TIPO_ALTURA { ABSOLUTE, CLAMP_TO_GROUND, RELATIVE_TO_GROUND }; 
	
	
	protected JTextField campoNombre = null;
	protected JTextField campoFiltro = null;
	protected JTextField campoAltura = null;
	
	
	protected ReglaPintado reglaPintado = null;
	
	protected boolean filtroCreandose = false;
	protected Object objetoSincronizacion = new Object();
	
	
	
	public ReglaPintadoDialogo()
	{		
		this ( null );
	}
	
	
	
	public ReglaPintadoDialogo( ReglaPintado reglaPintado )
	{
		this.reglaPintado = reglaPintado;
		this.inicializar();		
	}
	
	
	
	/**
	 * Inicializa los elementos
	 */
	private void inicializar()
	{
		if ( ( this.reglaPintado != null )  &&  ( this.reglaPintado.getNombre() != null ) )
		{
			this.campoNombre = new JTextField ( this.reglaPintado.getNombre() );
		}
		else
		{
			this.campoNombre = new JTextField();
		}
		
		
		if ( ( this.reglaPintado != null )  &&  ( this.reglaPintado.getNombre() != null ) )
		{
			this.campoFiltro = new JTextField ( this.reglaPintado.getFiltro() );
		}
		else
		{
			this.campoFiltro = new JTextField();
		}
	}
	
	
	
	
	
	
	/**
	 * Crea el panel para modificar el nombre
	 * 
	 * @return
	 */
	protected JPanel creaPanelNombreRegla()
	{
		JPanel panelNombreRegla = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();

		
		
		// Agregamos la etiqueta "Nombre"
		JLabel etiquetaNombre = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintado.etiquetaNombre" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelNombreRegla.add(etiquetaNombre, constraints);		
		
		
		
		
		// Agregamos el campo para editar el texto del filtro
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelNombreRegla.add(this.campoNombre, constraints);

		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
						
		
		
		
		return panelNombreRegla;
	}
	
	
	
	
	
	/**
	 * Crea el panel para crear o modificar el filtro
	 * 
	 * @return
	 */
	protected JPanel creaPanelFiltro()
	{
		JPanel panelFiltro = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();

		
		// Agregamos el CheckBox
		JCheckBox checkBoxFiltro = new JCheckBox();	
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelFiltro.add(checkBoxFiltro, constraints);		
		
		
		// Agregamos el boton "Filtro"
		JButton botonFiltro = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintado.etiquetaFiltro" ) );
		

		botonFiltro.addActionListener
		(
			new ActionListener()
			{												
				public void actionPerformed(ActionEvent e) 
				{
					String filtro = FiltroDialogo.creaFiltro();
					ReglaPintadoDialogo.this.campoFiltro.setText ( filtro );
				}
			}
		);	
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelFiltro.add(botonFiltro, constraints);		
		
		
		// Agregamos el campo para editar el texto del filtro
		constraints.gridx = 2; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelFiltro.add(this.campoFiltro, constraints);

		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
						
		
		// Le ponemos borde al panel
		panelFiltro.setBorder ( new TitledBorder ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintado.bordeFiltro" ) ) );
		
		return panelFiltro;
	}
	
	
	
	
	
	/**
	 * Crea el panel para establecer la altura
	 * 
	 * @return
	 */
	protected JPanel creaPanelAltura()
	{
		JPanel panelAlturas = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		
		// Creamos la etiqueta del modo de altura
		JLabel etiquetaModoAltura = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintado.modoAltura" ) );
				
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(7, 7, 7, 7);
		
		panelAlturas.add(etiquetaModoAltura, constraints);	
		
		
		
		
		// Creamos el desplegable de modos de altura
		TIPO_ALTURA [] valoresModoAltura = TIPO_ALTURA.values();		
		JComboBox desplegableModoAltura = new JComboBox ( valoresModoAltura );
				
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;			
		constraints.anchor = GridBagConstraints.WEST;
		
		panelAlturas.add(desplegableModoAltura, constraints);	
		
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		// Creamos la etiqueta de "Altura"
		JLabel etiquetaAltura = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintado.etiquetaAltura" ) );
				
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.anchor = GridBagConstraints.EAST;		
		
		panelAlturas.add(etiquetaAltura, constraints);	
		
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		// Creamos el campo altura
		this.campoAltura = new JTextField();
				
		constraints.gridx = 1; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelAlturas.add ( this.campoAltura, constraints );					
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		

		// Creamos la etiqueta de "Altura"
		JLabel etiquetaMultiplicar = new JLabel ( "  *  " );
				
		constraints.gridx = 2; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		
		panelAlturas.add(etiquetaMultiplicar, constraints);	
		
		
		
		
		

		// Agregamos el desplegable que mostrara el campo del que se cogerán las alturas
		String [] camposAltura = { "Altura total",  "Número alturas", "Número pisos" };
		JComboBox desplegableCamposAltura = new JComboBox ( camposAltura );
				
		constraints.gridx = 3; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 			
		
		panelAlturas.add ( desplegableCamposAltura, constraints );					
		
		
		
		
		
		// Establecemos el borde del panel
		TitledBorder bordeAltura = new TitledBorder( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaPintado.bordeAltura" ) );
		panelAlturas.setBorder ( bordeAltura );
		
		
		
		
		return panelAlturas;
	}
	
	
	
	
	
	
	/**
	 * Crea el panel con los botones de aceptar y cancelar
	 *   
	 * @return
	 */
	protected JPanel creaPanelBotonesAceptarCancelar()
	{
		JPanel panel = new JPanel(new GridLayout(1, 2, 30, 10));
		
		
		JButton botonAceptar = new JButton ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintado.aceptar" ) );
		JButton botonCancelar = new JButton ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaPintado.cancelar" ) );
		
		
		botonAceptar.addActionListener(new ActionListener()
        {
			public void actionPerformed(ActionEvent evt)
            {
                ReglaPintadoDialogo.this.dispose();
            }
        });
				
		
		botonCancelar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
            	ReglaPintadoDialogo.this.dispose();
            }
        });
		
		
		panel.add(botonAceptar);
		panel.add(botonCancelar);
		
		return panel;
	}
	
	
	
	
}
