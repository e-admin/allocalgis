/**
 * EstilosKml3DDialogo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.dialogs;

import java.awt.BorderLayout;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.vividsolutions.jump.I18N;

/**
 * Dialogo para comenzar la gestion de los estilos para el plugin de Kml3D
 * 
 * @author David Vicente
 *
 */
public class EstilosKml3DDialogo extends JDialog {
	
	/** Panel del cuadro de dialogo */
	private JPanel panelDialogoEstilosKml3D = null; 
	private JButton botonModificar = null;
	private JButton botonBorrar= null;
	
	public static final int DIM_X = 300;
	public static final int DIM_Y = 300;
	
	
	private Vector<String> listaEstilosCreados = null;
	private String nombreEstiloSeleccionado = null;
	
	private static boolean estiloSeleccionado = false;
	    	
	
	private String nombreCapa = "";
	
	
	public EstilosKml3DDialogo(String nombreCapa)
	{
		super(AppContext.getApplicationContext().getMainFrame());
		this.nombreCapa = nombreCapa;
		this.inicializar();
	}

	
	
	/**
	 * Inicializamos el dialogo
	 * 
	 * @return void
	 */
	private void inicializar() 
	{
		this.setModal(true);  
		this.setTitle(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoEstilos.tituloDialogo"));
		this.setResizable(true);  
		this.setSize(DIM_X, DIM_Y);
		this.setContentPane(this.creaPanelEstilosKml3DDialog());
		
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
	 * Devuelve el panel principal del dialogo. Si no esta creado lo crea
	 * 
	 * @return Panel principal del dialogo
	 */
	private JPanel creaPanelEstilosKml3DDialog()
	{
		if(this.panelDialogoEstilosKml3D != null)
		{
			return this.panelDialogoEstilosKml3D;
		}
			
		
		this.panelDialogoEstilosKml3D = new JPanel(new GridBagLayout());
		
		
		//Crea la etiqueta con el nombre de la capa seleccionada
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0; // El área de texto empieza en la columna cero.
		constraints.gridy = 0; // El área de texto empieza en la fila cero
		constraints.gridwidth = 2; // El área de texto ocupa dos columnas.
		constraints.gridheight = 1; // El área de texto ocupa 1 fila.
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(5, 5, 5, 5);
					
		this.panelDialogoEstilosKml3D.add(this.creaEtiquetaNombreCapa(), constraints);
		
		constraints.weightx = 0.0;		
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		//Creamos la lista con los estilos que hay ya creados		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		
		this.panelDialogoEstilosKml3D.add(this.creaListaEstilosCreados(), constraints);
		
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;	
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		//Añadimos el panel con los botones a la derecha
		constraints.gridx = 1; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;						
		constraints.anchor = GridBagConstraints.CENTER;
		
		this.panelDialogoEstilosKml3D.add(this.creaPanelBotones(), constraints);
		
	
				
		//Añadimos el panel con los botones de aceptar y cancelar
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 2; 
		constraints.gridheight = 1;		
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		this.panelDialogoEstilosKml3D.add(this.creaPanelAceptarCancelar(), constraints);
		
		constraints.weightx = 0.0;
		
		
		
		return this.panelDialogoEstilosKml3D;
	}
	
	
	
	

	/**
	 * Crea la etiqueta con el nombre de la capa
	 * 
	 * @return
	 */
	private JTextField creaEtiquetaNombreCapa()
	{
		//Creamos el campo con el nombre de la capa
		String textoCapa = I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoEstilos.etiquetaNombreCapa");
		JTextField campoTextoNombreCapa = new JTextField("     " + textoCapa + ": " + this.nombreCapa + " ");
		campoTextoNombreCapa.setEditable(false);
		campoTextoNombreCapa.setBorder(null);
		
		return campoTextoNombreCapa;
	}
	
	
	
	
	/**
	 * Crea el panel con la lista de estilos que existen para seleccionar uno
	 * 
	 * @return
	 */
	private JPanel creaListaEstilosCreados()
	{
		this.listaEstilosCreados = this.dameEstilosCreados();			
		JList listaPanel = new JList(listaEstilosCreados);				
		listaPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		listaPanel.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e) 
			{
		        ListSelectionModel lsm = (ListSelectionModel)e.getSource();

		        int elementoSeleccionado = lsm.getMinSelectionIndex();		   
		        EstilosKml3DDialogo.this.nombreEstiloSeleccionado = EstilosKml3DDialogo.this.listaEstilosCreados.get(elementoSeleccionado);
		        
		        EstilosKml3DDialogo.this.botonModificar.setEnabled(true);
		        EstilosKml3DDialogo.this.botonBorrar.setEnabled(true);
			}
		}
		);
		
		JScrollPane scrollPane = new JScrollPane(listaPanel);
		JPanel panel = new JPanel(new BorderLayout(20, 20));
		panel.add(scrollPane);
		
		return panel;
	}
	
	
	
	/**
	 * Crea el panel con todos los botones de la derecha
	 * 
	 * @return
	 */
	private JPanel creaPanelBotones()
	{
		JPanel panel = new JPanel(new GridLayout(2, 1, 10, 1));
				
		panel.add(this.creaPanelBotonesImportarExportar());
		panel.add(this.creaPanelBotonesNuevoModificarBorrar());
		
		return panel;
	}
	
	
	
	
	/**
	 * Crea el panel con los botones de importar y exportar
	 * 
	 * @return
	 */
	private JPanel creaPanelBotonesImportarExportar()
	{
		JPanel panel = new JPanel(new GridLayout(3, 1, 8, 8));
		
		JButton botonImportar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoEstilos.botonImportar"));
		JButton botonExportar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoEstilos.botonExportar"));
		
		
		botonImportar.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{					
					JDialog dialogo = new ImportarDialogo();
					dialogo.setVisible ( true );
				}
			}
		);
		
		
		botonExportar.addActionListener
		(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{					
						JDialog dialogo = new ExportarDialogo();
						dialogo.setVisible ( true );
					}
				}
		);
		
		
		panel.add(botonImportar);
		panel.add(botonExportar);
		panel.add(new JPanel());
		
		return panel;
	}
	
	
	
	
	/**
	 * Crea el panel con los botones de nuevo, modificar y borrar
	 * 
	 * @return
	 */
	private JPanel creaPanelBotonesNuevoModificarBorrar()
	{
		JPanel panel = new JPanel(new GridLayout(3, 1, 8, 8));
		
		JButton botonNuevo = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoEstilos.botonNuevo"));
		this.botonModificar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoEstilos.botonModificar"));
		this.botonBorrar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoEstilos.botonBorrar"));
		
		this.botonModificar.setEnabled(false);
		this.botonBorrar.setEnabled(false);
		
		panel.add(botonNuevo);
		panel.add(this.botonModificar);
		panel.add(this.botonBorrar);
		
		botonNuevo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                JDialog dialogoNuevoEstilo = new ModificaCreaEstiloKml3DDialogo();
                dialogoNuevoEstilo.setVisible(true);
            }
        });
		
		
		this.botonModificar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                JDialog dialogoModificaEstilo = new ModificaCreaEstiloKml3DDialogo(EstilosKml3DDialogo.this.nombreEstiloSeleccionado);
                dialogoModificaEstilo.setVisible(true);               
            }
        });
		
		
		return panel;
	}
	
	
	
	
	
	/**
	 * Crea el panel con los botones de aceptar y cancelar
	 * 
	 * @return
	 */
	private JPanel creaPanelAceptarCancelar()
	{
		JPanel panel = new JPanel(new GridLayout(1, 2, 30, 10));
		
		JButton botonAceptar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoEstilos.botonAceptar"));
		JButton botonCancelar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoEstilos.botonCancelar"));
		
		
		botonAceptar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                EstilosKml3DDialogo.this.dispose();
            }
        });
		
		
		botonCancelar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                EstilosKml3DDialogo.this.dispose();
            }
        });
		
		
		panel.add(botonAceptar);
		panel.add(botonCancelar);
		
		return panel;
	}
	
	
	
	
	/**
	 * Devuelve la lista de estilos que ya estan creados
	 * 
	 * @return
	 */
	private Vector<String> dameEstilosCreados()
	{
		Vector<String> listaEstilos = new Vector<String>();
		
		listaEstilos.add("Estilo 1");
		listaEstilos.add("Estilo 2");
		
		return listaEstilos;
	}
	
	
}
