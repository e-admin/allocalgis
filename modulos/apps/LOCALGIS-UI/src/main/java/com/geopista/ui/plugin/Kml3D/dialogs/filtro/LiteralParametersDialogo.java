/**
 * LiteralParametersDialogo.java
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperandoLiteral;
import com.vividsolutions.jump.I18N;

/**
 * Clase que recoge un campo de texto que sera un parametro literal
 * @author David Vicente
 *
 */
public class LiteralParametersDialogo extends JDialog{

	
	private JTextField campoLiteral = null;
	private String textoLiteral = "";
	
	private OperandoLiteral operandoLiteral = null;
	
	
	public static final int DIM_X = 300;
	public static final int DIM_Y = 200;
	
	
	public LiteralParametersDialogo()
	{		
		this.inicializar();
	}
	
	
	public LiteralParametersDialogo( OperandoLiteral operandoLiteral )
	{
		this.operandoLiteral = operandoLiteral;
		this.inicializar();
	}
	
	
	
	/**
	 * Inicializamos el cuadro de dialogo
	 */
	private void inicializar()
	{
		this.inicializarCamposDinamicos();
		this.setModal(true);  
		this.setTitle ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoLiteralParameters.tituloDialogo" ) );
		this.setResizable(true);  
		this.setSize(DIM_X, DIM_Y);
		this.setContentPane ( this.creaPanelAgregaParametroLiteral() );
		
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
		this.campoLiteral = new JTextField ( 15 );
		
		if ( this.operandoLiteral != null )
		{
			this.campoLiteral.setText ( this.operandoLiteral.getValor() );
		}
	}
	
	
	
	/**
	 * Crea el panel principal de la ventana
	 * 
	 * @return
	 */
	private JPanel creaPanelAgregaParametroLiteral()
	{
		JPanel panelPrincipal = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		// Crea el panel con los rangos de escala
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelPrincipal.add ( this.creaPanelPideLiteral(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		

		// Crea el panel con los botones aceptar y cancelar		
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelPrincipal.add ( this.creaPanelBotonesAceptarCancelar(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;		
		
		
		return panelPrincipal;
	}
	
	
	
	
	/**
	 * Panel que pide el campo literal 
	 * 
	 * @return
	 */
	private JPanel creaPanelPideLiteral()
	{
		JPanel panelPideLiteral = new JPanel ( new GridBagLayout() );
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
	
		// Crea la etiqueta "Literal"
		JLabel etiquetaLiteral = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoLiteralParameters.etiquetaLiteral" )  );
	
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 						
		
		panelPideLiteral.add (etiquetaLiteral , constraints );		
		
		
		
		// Crea el campo para pedir el literal
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 	
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		constraints.insets = new Insets(7, 7, 7, 7);
		
		panelPideLiteral.add ( this.campoLiteral , constraints );		
				
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;		
		
				
		
		return panelPideLiteral;
	}
	
	
	
	
	/**
	 * Crea el panel con los botones de aceptar y cancelar
	 *   
	 * @return
	 */
	private JPanel creaPanelBotonesAceptarCancelar()
	{
		JPanel panel = new JPanel(new GridLayout(1, 2, 30, 10));
		
		JButton botonAceptar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoLiteralParameters.botonAceptar"));
		JButton botonCancelar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoLiteralParameters.botonCancelar"));
		
		
		botonAceptar.addActionListener(new ActionListener()
        {
			public void actionPerformed(ActionEvent evt)
            {
				LiteralParametersDialogo.this.generaTexto();
                LiteralParametersDialogo.this.dispose();
            }
        });
				
		
		botonCancelar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
            	LiteralParametersDialogo.this.dispose();
            }
        });
		
		
		panel.add(botonAceptar);
		panel.add(botonCancelar);
		
		return panel;
	}


	
	
	private void generaTexto()
	{
		this.textoLiteral = this.campoLiteral.getText();
		
		if ( this.operandoLiteral != null )
		{
			this.operandoLiteral.setValor ( this.campoLiteral.getText() );
		}
	}

	

	public String getTextoLiteral() {
		return textoLiteral;
	}
	
	
	
	
	
	
}
