/**
 * ImportarDialogo.java
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
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.vividsolutions.jump.I18N;

/**
 * Dialogo para importar estilos
 * 
 * @author David Vicente
 *
 */
public class ImportarDialogo extends JDialog{

	
	private JTextField campoFichero;
	private JButton botonExaminar;
	
	
	public static final int DIM_X = 400;
	public static final int DIM_Y = 200;
	
	
	
	public ImportarDialogo ()
	{
		this.inicializar();
	}
	
	
	
	public void inicializar()
	{
		this.inicializaComponentes();
		this.setModal ( true );  
		this.setTitle ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoImportar.tituloDialogo" ) );
		this.setResizable ( true );  
		this.setSize ( DIM_X, DIM_Y );
		this.setContentPane ( this.creaPanelImportarEstilo() );
		
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
	 * Inicializa los componentes de la ventana
	 */
	public void inicializaComponentes()
	{
		this.campoFichero = new JTextField ();
		
		this.botonExaminar = new JButton ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoImportar.botonExaminar" ) );
		this.botonExaminar.addActionListener ( 
				new ActionListener() 
				{			
					public void actionPerformed(ActionEvent e) 
					{
						JFileChooser fileChooser = new JFileChooser ();
					    int returnVal = fileChooser.showSaveDialog(ImportarDialogo.this);
			
					    if (returnVal == JFileChooser.APPROVE_OPTION) 
					    {
					    	File file = fileChooser.getSelectedFile();	
					    	ImportarDialogo.this.campoFichero.setText ( file.getAbsolutePath() );
					    } 
					    else 
					    {				    	
					    }
					}
				});
	}
	
	
	
	
	
	/**
	 * Crea el panel principal de la ventana
	 * 
	 * @return
	 */
	private JPanel creaPanelImportarEstilo()
	{
		JPanel panelPrincipal = new JPanel ( new GridBagLayout() );
		
		GridBagConstraints constraints = new GridBagConstraints();


		
		// Añadimos la etiqueta de información		
		JLabel etiquetaInformacion = new JLabel ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoImportar.etiquetaInforamcion" ) );
		
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(7, 7, 7, 7);
				
		panelPrincipal.add ( etiquetaInformacion, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		// Añadimos el campo del fichero
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		constraints.insets = new Insets(7, 7, 7, 7);
				
		panelPrincipal.add ( this.campoFichero, constraints );
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		// Añadimos el boton para cargar el fichero		
		constraints.gridx = 1; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
				
		panelPrincipal.add ( this.botonExaminar, constraints );
		
		
		
		
		
		// Añadimos el panel con los botones aceptar y cancelar		
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
				
		panelPrincipal.add ( this.creaPanelBotonesAceptarCancelar(), constraints );
				
		constraints.weightx = 0.0;
		
		
		
		
		return panelPrincipal;
	}
	
	
	
	
	
	
	

	
	
	/**
	 * Crea el panel con los botones de aceptar y cancelar
	 *   
	 * @return
	 */
	protected JPanel creaPanelBotonesAceptarCancelar()
	{
		JPanel panel = new JPanel(new GridLayout(1, 2, 30, 10));
		
		
		JButton botonAceptar = new JButton ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoImportar.aceptar" ) );
		JButton botonCancelar = new JButton ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoImportar.cancelar" ) );
		
		
		botonAceptar.addActionListener(new ActionListener()
        {
			public void actionPerformed(ActionEvent evt)
            {
				ImportarDialogo.this.dispose();
            }
        });
				
		
		botonCancelar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
            	ImportarDialogo.this.dispose();
            }
        });
		
		
		panel.add(botonAceptar);
		panel.add(botonCancelar);
		
		return panel;
	}
	
	
	
	
	
	
}
