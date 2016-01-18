/**
 * ReglaTematicaDialogo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.ReglaTematicaPintado;
import com.geopista.ui.plugin.Kml3D.datos.ValorTematico;
import com.geopista.ui.plugin.Kml3D.dialogs.ReglaPintadoDialogo.TIPO_ALTURA;
import com.vividsolutions.jump.I18N;

/**
 * Clase de la que hereda cualquier dialogo de regla temática pintado,
 * para reutilizar cierta funcionalidad
 * 
 * @author David Vicente
 *
 */
public abstract class ReglaTematicaDialogo extends JDialog {

	protected JPanel panelValores; 
	protected JCheckBox checkBoxRangos;
	protected JComboBox desplegablePropiedades;
//	protected JTable tablaValores;
	protected JScrollPane scroll;
	protected JTextField campoAltura = null;
	
	protected JButton ultimoBotonUtilizado = null;	
	
	protected ReglaTematicaPintado reglaTematicaPintado = null;
	
	protected Hashtable<JButton, ValorTematico> botonesValores = new Hashtable<JButton, ValorTematico>();
	protected Hashtable<JButton, JTextField> botonesCampos = new Hashtable<JButton, JTextField>(); 
		
	
	
	
	
	public ReglaTematicaDialogo()
	{		
		this ( null );
	}
	
	
	
	public ReglaTematicaDialogo( ReglaTematicaPintado reglaTematicaPintado )
	{
		this.reglaTematicaPintado = reglaTematicaPintado;
		
		if (this.reglaTematicaPintado == null )
		{
			this.reglaTematicaPintado = new ReglaTematicaPintado();
		}
		
		this.inicializarComponentes();		
	}
	

	
	
	
	private void inicializarComponentes()
	{
		// Inicializamos el desplegable de propiedades
		this.desplegablePropiedades = new JComboBox( this.dameListaPropiedades() );
		if ( ( this.reglaTematicaPintado != null )  &&  ( this.reglaTematicaPintado.getPropiedad() != null ) )
		{		
			this.desplegablePropiedades.setSelectedItem ( this.reglaTematicaPintado.getPropiedad() );
		}
		
		

		this.checkBoxRangos = new JCheckBox();
		if ( this.reglaTematicaPintado != null )  
		{
			this.checkBoxRangos.setSelected ( this.reglaTematicaPintado.isPorRangos() );
		}
				
	}
	
	
	
	
	protected void refrescaValores()
	{
		this.creaPanelValores();
		this.panelValores.updateUI();
	}
	
	
	
	
	/**
	 * Crea el panel para editar las propiedades de la regla tematica
	 * 
	 * @return
	 */
	protected JPanel creaPanelComun()
	{
		JPanel panelRegalTematica = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();

		
		

		// Agregamos la etiqueta "Propiedad"
		JLabel etiquetaPropiedad = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematica.etiquetaPropiedad" ) );
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.insets = new Insets(7, 7, 7, 7);
		
		panelRegalTematica.add ( etiquetaPropiedad, constraints );
		
						
		
		
		
		
		// Agregamos el desplegable propiedad
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;		
		constraints.fill = GridBagConstraints.BOTH;		
		
		panelRegalTematica.add ( this.desplegablePropiedades, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;		
					
		
		
		
		// Agregamos la etiqueta "Por rangos"
		JLabel etiquetaPorRangos = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematica.etiquetaPorRangos" ) );
		
		constraints.gridx = 2; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		
		panelRegalTematica.add ( etiquetaPorRangos, constraints );		
		
		
		
		
		// Agregamos el checkBox
		constraints.gridx = 3; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;		
		constraints.anchor = GridBagConstraints.WEST;
		
		panelRegalTematica.add ( this.checkBoxRangos, constraints );

		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
			
		
		
		
		
		

		// Agregamos el desplegable propiedad
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 4; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;		
		constraints.fill = GridBagConstraints.BOTH;		
		
		panelRegalTematica.add ( this.creaPanelValores(), constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;		
						
		
		
		
		return panelRegalTematica;
	}
	
	
	
	
	
	/** 
	 * Crea el panel de valores
	 * 
	 * @return
	 */
	private JPanel creaPanelValores()
	{
		if ( this.panelValores == null )
		{
			this.panelValores = new JPanel ( new GridBagLayout() );
		}
		else
		{
			this.panelValores.removeAll();
		}
		
		
		GridBagConstraints constraints = new GridBagConstraints();

		
		
		// Agregamos la tabla			
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		panelValores.add ( this.creaTablaValores(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		
		
		
		
		// Agregamos el panel de botones	
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 		
		
		panelValores.add ( this.creaPanelBotones(), constraints );		
		
		
		TitledBorder borde = new TitledBorder( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematica.bordeValores" ) );
		panelValores.setBorder ( borde );
		
		
		return panelValores;
	}
	
	
	
	
	private Component creaTablaValores()
	{
		if ( this.reglaTematicaPintado == null )
		{
			return new JPanel();
		}
		
		
		JPanel panelValores = new JPanel ( new GridBagLayout ( ) );
		GridBagConstraints constraints = new GridBagConstraints();
		Enumeration<JButton> botones = this.botonesValores.keys();		
		
		int fila = 0;
		
		LineBorder borde = new LineBorder ( Color.LIGHT_GRAY, 2 );
		
		while ( botones.hasMoreElements() )
		{
			JButton boton = (JButton) botones.nextElement();
			ValorTematico valorTematico = this.botonesValores.get ( boton );
			boton.setBorder ( borde );
			
			constraints.gridx = 0; 
			constraints.gridy = fila; 
			constraints.gridwidth = 1; 
			constraints.gridheight = 1; 
			constraints.weightx = 1.0;
			constraints.fill = GridBagConstraints.BOTH;			
			
			panelValores.add ( boton, constraints );
			
		
			
			
			JTextField campo = this.botonesCampos.get ( boton );
			if ( campo == null )
			{
				campo = new JTextField ( valorTematico.getValor() );
				campo.addFocusListener
				( 
					new FocusListener () 
					{						
						@Override
						public void focusLost(FocusEvent e) 
						{													
						}
						
						@Override
						public void focusGained(FocusEvent e) 
						{
							JTextField campo = (JTextField) e.getSource();
							
							Hashtable<JButton, JTextField> botonesCampos = ReglaTematicaDialogo.this.botonesCampos;
							Enumeration<JButton> botones = botonesCampos.keys();
							
							while ( botones.hasMoreElements() )
							{
								JButton botonActual = botones.nextElement();
								JTextField campoActual = botonesCampos.get ( botonActual );
								
								if ( campoActual.equals ( campo ) )
								{
									ReglaTematicaDialogo.this.ultimoBotonUtilizado = botonActual;
									break;
								}																
							}
						}
					}	
				);
				
				this.botonesCampos.put ( boton, campo );
			}
			
			
			campo.setBorder ( borde );
			
			constraints.gridx = 1; 
			constraints.gridy = fila; 
			constraints.gridwidth = 1; 
			constraints.gridheight = 1; 
			constraints.weightx = 1.0;
			constraints.fill = GridBagConstraints.BOTH;		
			
			panelValores.add ( campo, constraints );
			
			fila++;
		}
		
		JScrollPane scroll = new JScrollPane ( panelValores );
		
		
		
		return scroll;
	}
	
	
	
	
	/**
	 * Crea el panel con los botones para editar los valores
	 * 
	 * @return
	 */
	private JPanel creaPanelBotones()
	{
		JPanel panelBotones = new JPanel ( new GridLayout(4, 1, 7, 7) );
		
		JButton botonNuevo = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematica.botonNuevo" ) );
		JButton botonBorrar = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematica.botonBorrar" ) );
		JButton botonTodos = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematica.botonTodos" ) );
		JButton botonRampa = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematica.botonRampa" ) );
		
		
		botonNuevo.addActionListener( new ActionListener ()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{			
				int r = ( (int) ( Math.random() * 10000000 ) ) % 255;
				int g = ( (int) ( Math.random() * 10000000 ) ) % 255;
				int b = ( (int) ( Math.random() * 10000000 ) ) % 255;
				Color color = new Color ( r, g, b );
				JButton boton = new JButton();
				boton.setBackground ( color );
				boton.setContentAreaFilled(false);
				boton.setOpaque(true);
				
				boton.addActionListener(new ActionListener() 
				{				  
					JColorChooser selectorColor = new JColorChooser( Color.black );

					public void actionPerformed ( ActionEvent e )
					{
						JButton boton = ( JButton ) e.getSource();
				        JDialog dialog = JColorChooser.createDialog ( ReglaTematicaDialogo.this,
				        		"Color", true, selectorColor, new ActionListener() 
				        {
				        	public void actionPerformed(ActionEvent e) 		            
				        	{			        				        	
				        					        		
				            }
				        }, null);
				        
				        
				        dialog.setVisible(true);			        
				        boton.setBackground ( selectorColor.getColor() );			        
				        ValorTematico valorActual = ReglaTematicaDialogo.this.botonesValores.get( boton );
		        		valorActual.setColor ( selectorColor.getColor() );
		        		
		        		ReglaTematicaDialogo.this.ultimoBotonUtilizado = boton;
					}			
				});
				
				
				String valor = "";
				ValorTematico valorTematico = new ValorTematico ( color, valor );
				ReglaTematicaDialogo.this.reglaTematicaPintado.getValores().add(valorTematico);
				ReglaTematicaDialogo.this.botonesValores.put ( boton, valorTematico );
				ReglaTematicaDialogo.this.refrescaValores();
			}			
		}
		);
		
		
		botonBorrar.addActionListener( new ActionListener ()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{			
				JButton ultimoBotonUtilizado = ReglaTematicaDialogo.this.ultimoBotonUtilizado;
				
				if ( ultimoBotonUtilizado != null )
				{
					ReglaTematicaDialogo.this.botonesValores.remove ( ultimoBotonUtilizado );
					ReglaTematicaDialogo.this.botonesCampos.remove ( ultimoBotonUtilizado );
					
					ReglaTematicaDialogo.this.refrescaValores();
					
					ReglaTematicaDialogo.this.ultimoBotonUtilizado = null;
				}
			}			
		}
		);
		
		
		
		botonTodos.addActionListener( new ActionListener ()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{			
				//Preguntamos si quiere borrar todos los valores
				int respuesta = JOptionPane.showConfirmDialog(
				    ReglaTematicaDialogo.this,
				    I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematica.etiquetaPreguntaBorrarTodos"),
				    I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematica.tituloDialogoBorrarTodos"),
				    JOptionPane.YES_NO_OPTION);
				
				
				if ( respuesta == JOptionPane.YES_OPTION ) 	 //BORRAMOS TODOS LOS VALORES					
				{
					Hashtable<JButton, JTextField> botonesCampos = ReglaTematicaDialogo.this.botonesCampos;
					Enumeration<JButton> botones = botonesCampos.keys();
					
					while ( botones.hasMoreElements() )
					{
						JButton botonActual = botones.nextElement();					
						botonesCampos.remove ( botonActual );
						ReglaTematicaDialogo.this.botonesValores.remove ( botonActual );
					}
					
					ReglaTematicaDialogo.this.ultimoBotonUtilizado = null;				
					ReglaTematicaDialogo.this.refrescaValores();
				}
			}			
		}
		);
		
		
		
		botonRampa.addActionListener( new ActionListener ()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{			
				Hashtable<JButton, JTextField> botonesCampos = ReglaTematicaDialogo.this.botonesCampos;
				Enumeration<JButton> botones = botonesCampos.keys();
				
				Color colorInicial;
				if ( botones.hasMoreElements() )
				{
					JButton botonActual = botones.nextElement();
					ValorTematico valorTematico = ReglaTematicaDialogo.this.botonesValores.get ( botonActual );					
					colorInicial = valorTematico.getColor();
				}
				else 
				{
					return;
				}
				
				
				while ( botones.hasMoreElements() )
				{
					int r = colorInicial.getRed() + 20;
					int g = colorInicial.getGreen() + 20;
					int b = colorInicial.getBlue() + 20;
					
					if ( r > 255 )
					{
						r = 255;
					}
					
					if ( g > 255 )
					{
						g = 255;
					}
					
					if ( b > 255 )
					{
						b = 255;
					}
					
					colorInicial = new Color ( r, g, b );
					JButton botonActual = botones.nextElement();
					botonActual.setBackground ( colorInicial );										
				}
				
				ReglaTematicaDialogo.this.refrescaValores();
			}			
		}
		);
		
		
		
		
		panelBotones.add ( botonNuevo );
		panelBotones.add ( botonBorrar );
		panelBotones.add ( botonTodos );
		panelBotones.add ( botonRampa );
		
		
		return panelBotones;
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
		JLabel etiquetaModoAltura = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematica.modoAltura" ) );
				
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
		JLabel etiquetaAltura = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematica.etiquetaAltura" ) );
				
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
		TitledBorder bordeAltura = new TitledBorder( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoReglaTematica.bordeAltura" ) );
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
		
		
		JButton botonAceptar = new JButton ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematica.aceptar" ) );
		JButton botonCancelar = new JButton ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoReglaTematica.cancelar" ) );
		
		
		botonAceptar.addActionListener(new ActionListener()
        {
			public void actionPerformed(ActionEvent evt)
            {
                ReglaTematicaDialogo.this.dispose();
            }
        });
				
		
		botonCancelar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
            	ReglaTematicaDialogo.this.dispose();
            }
        });
		
		
		panel.add(botonAceptar);
		panel.add(botonCancelar);
		
		return panel;
	}
	
	
	
	
	
	
	
	/**
	 * Crea estáticamente la lista de propiedades
	 * 
	 * @return
	 */
	private String [] dameListaPropiedades()
	{
		String [] propiedades = { "", "GEOMETRY" };
		return propiedades;
	}
	
	
	
	
}
	





	
	