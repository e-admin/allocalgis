/**
 * PropertyNameDialogo.java
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

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperandoPropiedad;
import com.vividsolutions.jump.I18N;

/**
 * Dialogo para pedir los property name
 * 
 * @author David Vicente
 *
 */
public class PropertyNameDialogo extends JDialog {
	

	private JPanel panelPrincipal = null; 
	private JComboBox desplegableFeatures = null;
	private JComboBox desplegableAttributes = null;
	
	private OperandoPropiedad operandoPropiedad = null;
	
	private String textoPropertyName = "";

	public static final int DIM_X = 370;
	public static final int DIM_Y = 230;
	
	
	
	
	public PropertyNameDialogo()
	{
		this.inicializar();
	}
	
	
	
	
	public PropertyNameDialogo ( OperandoPropiedad operandoPropiedad )
	{
		this.operandoPropiedad = operandoPropiedad;
		this.inicializar();
	}
	
	
	
	
	/**
	 * Inicializamos el cuadro de dialogo
	 */
	private void inicializar()
	{
		this.inicializarCamposDinamicos();
		this.setModal(true);  
		this.setTitle ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoPropertyName.tituloDialogo" ) );
		this.setResizable(true);  
		this.setSize(DIM_X, DIM_Y);
		this.setContentPane ( this.creaPanelPropertyName() );
		
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
	
	
	
	public void inicializarCamposDinamicos ()
	{
		
	}
	
	
	
	private void refrescaPanel()
	{
		this.panelPrincipal.removeAll();
		this.creaPanelPropertyName();
		this.panelPrincipal.updateUI();
	}
	
	
	
	
	private JPanel creaPanelPropertyName()
	{		
		if ( this.panelPrincipal == null )
		{
			this.panelPrincipal = new JPanel ( new GridBagLayout() );
		}
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		// Crea la etiqueta "Features"
		JLabel etiquetaFeatures = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoPropertyName.etiquetaFeatures" ) );
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.insets = new Insets(7, 7, 7, 7);
		constraints.anchor = GridBagConstraints.CENTER;
		
		this.panelPrincipal.add ( etiquetaFeatures, constraints );		
		
		constraints.weightx = 0.0;		
		
		
		
		
		// Crea el desplegable para features		
		if ( this.desplegableFeatures == null )
		{
			String [] valoresFeatures = this.dameValoresFeatures();
			this.desplegableFeatures = new JComboBox ( valoresFeatures );
			

			this.desplegableFeatures.addActionListener 
			( 
				new ActionListener() 
				{					
					@Override
					public void actionPerformed(ActionEvent e) 
					{					
						if ( PropertyNameDialogo.this.desplegableFeatures.getSelectedItem().toString().equals ( "Nueva Capa" ) )
						{
							String [] valoresAttributes = PropertyNameDialogo.this.dameValoresAttributes();
							PropertyNameDialogo.this.desplegableAttributes = new JComboBox ( valoresAttributes );
							PropertyNameDialogo.this.refrescaPanel();
						}
						else 
						{
							String [] valoresAttributes = { "" };
							PropertyNameDialogo.this.desplegableAttributes = new JComboBox ( valoresAttributes );
							PropertyNameDialogo.this.refrescaPanel();
						}
					}
				}
			);
		}
		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		this.panelPrincipal.add ( this.desplegableFeatures, constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
	
		
		
		
		
		// Crea la etiqueta Attributes
		JLabel etiquetaAttributes = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoPropertyName.etiquetaAttributes" ) );
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		this.panelPrincipal.add ( etiquetaAttributes, constraints );		
		
		constraints.weightx = 0.0;		
		
		
		
		
		// Crea el desplegable para los Attributes
		if ( this.desplegableAttributes == null )
		{
			this.desplegableAttributes = new JComboBox ();
		}
		
		constraints.gridx = 1; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		this.panelPrincipal.add ( this.desplegableAttributes, constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		// Agrega los botones Aceptar y Cancelar
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		this.panelPrincipal.add ( this.creaPanelBotonesAceptarCancelar(), constraints );		
		
		constraints.weightx = 0.0;		
	
		
		
		
		return this.panelPrincipal;
	}
	
	
	
	
	/**
	 * Crea el panel con los botones de aceptar y cancelar
	 *   
	 * @return
	 */
	private JPanel creaPanelBotonesAceptarCancelar()
	{
		JPanel panel = new JPanel(new GridLayout(1, 2, 30, 10));
		
		JButton botonAceptar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoPropertyName.botonAceptar"));
		JButton botonCancelar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoPropertyName.botonCancelar"));
		
		
		botonAceptar.addActionListener(new ActionListener()
        {
			public void actionPerformed(ActionEvent evt)
            {
				PropertyNameDialogo.this.textoPropertyName = PropertyNameDialogo.this.generaTexto();
				PropertyNameDialogo.this.dispose();
            }
        });
				
		
		botonCancelar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
            	PropertyNameDialogo.this.dispose();
            }
        });
		
		
		panel.add(botonAceptar);
		panel.add(botonCancelar);
		
		return panel;
	}
	
	
	
	private String generaTexto()
	{
		String texto = "";	
		if ( PropertyNameDialogo.this.desplegableAttributes.getSelectedItem() != null )
		{
			texto = PropertyNameDialogo.this.desplegableAttributes.getSelectedItem().toString();
		}
		return texto;
	}
	
	
	
	public String getTexto()
	{
		return this.textoPropertyName;
	}
	
	
	
	public String [] dameValoresFeatures()
	{
		String [] valoresFeatures = { "", "Nueva Capa" };
		return valoresFeatures;
	}
	
	
	
	public String [] dameValoresAttributes()
	{
		String [] valoresAttributes = { "", "GEOMETRY" };
		return valoresAttributes;
	}
	

}
