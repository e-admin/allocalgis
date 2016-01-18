/**
 * RangoEscalaDialogo.java
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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.RangoEscala;
import com.vividsolutions.jump.I18N;

/**
 * Clase que representa el cuadro que crea o modifica los rangos de escala 
 * 
 * @author David Vicente
 *
 */
public class RangoEscalaDialogo extends JDialog{

	public static final int DIM_X = 480;
	public static final int DIM_Y = 200;

	/** Rango de escala a modificar */
	private RangoEscala rangoModificar = null;
	
	/** Rangos de escala que hay ya creados */
	private Vector <RangoEscala> rangosEscala = null;
	
	
	JTextField campoEscMinima = new JTextField();
	JTextField campoEscMaxima = new JTextField();
	


	public RangoEscalaDialogo ()
	{
		this ( new Vector <RangoEscala> () );
	}
	
	
	
	public RangoEscalaDialogo ( Vector <RangoEscala> rangosEscala )
	{
		this ( rangosEscala, null );		
	}
	
	
	
	public RangoEscalaDialogo ( Vector <RangoEscala> rangosEscala, RangoEscala rangoModificar)
	{
		this.rangosEscala = rangosEscala;
		this.rangoModificar = rangoModificar;
		
		this.inicializar();
	}

	
	
	/**
	 * Inicializamos el cuadro de dialogo
	 */
	private void inicializar()
	{
		this.setModal(true);  
		this.setTitle(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoEstilos.tituloDialogo"));
		this.setResizable(true);  
		this.setSize(DIM_X, DIM_Y);
		this.setContentPane(this.creaPanelModificaCreaRangosEscala());
		
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
	 * Crea el panel principal del dialogo 
	 * 
	 * @return
	 */
	private JPanel creaPanelModificaCreaRangosEscala()
	{
		JPanel panelPrincipal = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		// Crea y agrega el panel de los datos
		constraints.gridx = 0; // El área de texto empieza en la columna cero.
		constraints.gridy = 0; // El área de texto empieza en la fila cero
		constraints.gridwidth = 1; // El área de texto ocupa dos columnas.
		constraints.gridheight = 1; // El área de texto ocupa 1 fila.
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelPrincipal.add ( this.crePanelDatos(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		// Crea el panel con los botones de aceptar y cancelar
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 					
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelPrincipal.add ( this.creaPanelBotonesAceptarCancelar(), constraints );		
		
		
		
		return panelPrincipal;
	}
	
	
	
	
	/**
	 * Crea el panel con los datos
	 * 
	 * @return
	 */
	private JPanel crePanelDatos ()
	{
		JPanel panelDatos = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();
				
		JLabel etiquetaEscMinima = new JLabel ( I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoRangosEscala.escalaMinima") );
		JLabel etiquetaEscMaxima = new JLabel ( I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoRangosEscala.escalaMaxima") );
		
		
		if ( this.rangoModificar != null )
		{
			this.campoEscMinima = new JTextField ( this.rangoModificar.getMinEscala() );
			this.campoEscMaxima = new JTextField( this.rangoModificar.getMaxEscala() );
		}
		else
		{
			this.campoEscMinima = new JTextField();
			this.campoEscMaxima = new JTextField();
		}
							
		
		// Agrega la etiqueta de la escala minima
		constraints.gridx = 0; // El área de texto empieza en la columna cero.
		constraints.gridy = 0; // El área de texto empieza en la fila cero
		constraints.gridwidth = 1; // El área de texto ocupa dos columnas.
		constraints.gridheight = 1; // El área de texto ocupa 1 fila.
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelDatos.add ( etiquetaEscMinima, constraints );
		
		
		// agrega el campo de escala minima
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;	
		constraints.fill = GridBagConstraints.BOTH;
		
		panelDatos.add ( this.campoEscMinima, constraints );
		
		constraints.weightx = 0.0;		
		constraints.fill = GridBagConstraints.NONE;
		
		
		// Agrega la etiqueta de la escala maxima
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		
		panelDatos.add ( etiquetaEscMaxima, constraints );
		
		
		// agrega el campo de escala minima
		constraints.gridx = 1; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		panelDatos.add ( this.campoEscMaxima, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		return panelDatos;
	}
	
	
	
	
	/**
	 * Crea el panel con los botones de aceptar y cancelar
	 *   
	 * @return
	 */
	private JPanel creaPanelBotonesAceptarCancelar()
	{
		JPanel panel = new JPanel ( new GridLayout ( 1, 2, 30, 10 ) );
		
		JButton botonAceptar = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoRangosEscala.botonAceptar" ) );
		JButton botonCancelar = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoRangosEscala.botonCancelar" ) );
		
		
		botonAceptar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
            	try
            	{
            		RangoEscala.valida ( RangoEscalaDialogo.this.campoEscMinima.getText(), RangoEscalaDialogo.this.campoEscMaxima.getText() );
            		
            		if ( RangoEscalaDialogo.this.rangoModificar != null )
            		{
	            		RangoEscalaDialogo.this.rangoModificar.setMinEscala ( RangoEscalaDialogo.this.campoEscMinima.getText() );
	            		RangoEscalaDialogo.this.rangoModificar.setMaxEscala ( RangoEscalaDialogo.this.campoEscMaxima.getText() );
            		}
            		else
            		{
            			RangoEscala rangoNuevo = new RangoEscala ( RangoEscalaDialogo.this.campoEscMinima.getText(), RangoEscalaDialogo.this.campoEscMaxima.getText() );
            			RangoEscalaDialogo.this.rangosEscala.add ( rangoNuevo );
            		}
            		
            		RangoEscalaDialogo.this.dispose();
            	}
            	catch ( NumberFormatException e )            
            	{
            		JOptionPane.showMessageDialog ( RangoEscalaDialogo.this, I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoRangosEscala.mensajeDatosEscalaNoNumericos" ) , I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoRangosEscala.tituloMensajeError" ) , JOptionPane.WARNING_MESSAGE );
            	}                            	
            }
        });
		
		
		botonCancelar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                RangoEscalaDialogo.this.dispose();
            }
        });
		
		
		panel.add(botonAceptar);
		panel.add(botonCancelar);
		
		return panel;
	}
	
	
}
