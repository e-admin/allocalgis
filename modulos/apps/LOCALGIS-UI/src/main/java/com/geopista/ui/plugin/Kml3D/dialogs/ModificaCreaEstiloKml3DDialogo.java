/**
 * ModificaCreaEstiloKml3DDialogo.java
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
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.RangoEscala;
import com.vividsolutions.jump.I18N;


/**
 * Dialogo para modificar o crear un nuevo estilo de KML 3D 
 * 
 * @author David Vicente
 *
 */
public class ModificaCreaEstiloKml3DDialogo extends JDialog
{	  
	/** Enum con los tipos de reglas de pintado que hay */
	private enum TIPOS_REGLAS_PINTADO { Puntos, Lineas, Poligonos, Textos };
	
	
	/** Panel del dialogo que crea o modifica un estilo */
	private JPanel panelDialogoModicaCreaEstiloKml3D = null;
	private JPanel panelRangosEscala = null;
	private JPanel panelReglasPintado = null;
	private JButton botonBorrarRangosEscala = null;
	private JButton botonModificarRangosEscala = null;
	private JButton botonBorrarReglasPintado = null;
	private JButton botonModificarReglasPintado = null;
	private JList listaPanelRangosEscala = null;
	private Hashtable<TIPOS_REGLAS_PINTADO, JList> listaPanelesReglasPintado = null;
	
	
	public static final int DIM_X = 480;
	public static final int DIM_Y = 500;
	
	
	private String nombreCapa = "";
	private RangoEscala rangoEscalaSeleccionado = null;
	private String nombreReglaPintadoSeleccionada = "";
	private TIPOS_REGLAS_PINTADO tipoReglasPintadoSeleccionado = TIPOS_REGLAS_PINTADO.Puntos;	
	private Vector<RangoEscala> listaRangosEscala = null;
	private Hashtable<TIPOS_REGLAS_PINTADO, Vector<String>> listaReglasPintado = null;
	


	
	public ModificaCreaEstiloKml3DDialogo(String nombreCapa)
	{
		this.nombreCapa = nombreCapa;	
		this.inicializar();	
	}
	
	
	
	public ModificaCreaEstiloKml3DDialogo()
	{		
		this("");
	}
	
	
	
	
	/**
	 * Inicializamos el dialogo
	 * 
	 * @return void
	 */
	private void inicializar() 
	{
		this.setModal ( true );  
		this.setTitle ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoEstilos.tituloDialogo" ) );
		this.setResizable ( true );  
		this.setSize ( DIM_X, DIM_Y );
		this.setContentPane ( this.creaPanelModificaCreaEstiloKml3DDialog() );
		
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
	 * Se refresca el panel para que actualice los datos
	 */
	private void refrescaPanelRangosEscala() 
	{		
		this.creaPanelRangosEscala();
		this.panelRangosEscala.updateUI();
	}
	
	
	
	
	/**
	 * Se refresca el panel de reglas de pintado para que cargue y actualice los datos
	 */
	private void refrescaPanelReglasPintado()
	{
		this.creaPanelReglasPintado();
		this.panelReglasPintado.updateUI();
	}
	
	
	
	
	/**
	 * Crea el panel principal del dialogo
	 * 
	 * @return
	 */
	private JPanel creaPanelModificaCreaEstiloKml3DDialog()
	{
		if(this.panelDialogoModicaCreaEstiloKml3D != null)
		{
			return this.panelDialogoModicaCreaEstiloKml3D;
		}
					
		this.panelDialogoModicaCreaEstiloKml3D = new JPanel(new GridBagLayout());
		
				
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		//Crea el panel con el nombre del estilo y permite modificarlo
		constraints.gridx = 0; // El área de texto empieza en la columna cero.
		constraints.gridy = 0; // El área de texto empieza en la fila cero
		constraints.gridwidth = 1; // El área de texto ocupa dos columnas.
		constraints.gridheight = 1; // El área de texto ocupa 1 fila.
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(3, 3, 3, 3);
		
		this.panelDialogoModicaCreaEstiloKml3D.add(this.creaPanelNombreEstilo(), constraints);
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		JPanel panelRangosEscala = new JPanel(new GridBagLayout());
		
		// Crea el panel con los rangos de escala
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelRangosEscala.add ( this.creaPanelRangosEscala(), constraints );		
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		// Crea el panel con los botones que gestionan los rangos de escala
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.anchor = GridBagConstraints.CENTER;		
		
		panelRangosEscala.add(this.creaPanelBotonesRangosEscala(), constraints);
		
		
		TitledBorder borde = BorderFactory.createTitledBorder(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoNuevoEstilo.bordeRangosEscala"));
		borde.setTitleJustification(TitledBorder.LEFT);
		panelRangosEscala.setBorder(borde);
		
		
		
		
		// Añade el panel con los rangos de escala
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;		
		
		this.panelDialogoModicaCreaEstiloKml3D.add(panelRangosEscala, constraints);
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		JPanel panelReglasPintado = new JPanel(new GridBagLayout());
		
		
		// Crea el panel con las reglas de pintado
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelReglasPintado.add(this.creaPanelReglasPintado(), constraints);
		
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		// Crea el panel con los botones que gestionan las reglas de pintado
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.anchor = GridBagConstraints.CENTER;
		
		panelReglasPintado.add ( this.creaPanelBotonesReglasPintado(), constraints );
		
		
				

		TitledBorder bordeReglasPintado = BorderFactory.createTitledBorder(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoNuevoEstilo.bordeReglasPintado"));
		bordeReglasPintado.setTitleJustification(TitledBorder.LEFT);
		panelReglasPintado.setBorder(bordeReglasPintado);
		
		
		
		
		// Añade el panel con las reglas de pintado
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;		
		
		this.panelDialogoModicaCreaEstiloKml3D.add(panelReglasPintado, constraints);
		
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		
		// Crea el panel con los botones de aceptar y cancelar
		constraints.gridx = 0; 
		constraints.gridy = 3; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		
		this.panelDialogoModicaCreaEstiloKml3D.add(this.creaPanelBotonesAceptarCancelar(), constraints);
		
		constraints.weightx = 0.0;
		
		
		
		return this.panelDialogoModicaCreaEstiloKml3D;
	}
	
	
	
	
	/**
	 * Crea el panel con el nombre del estilo editable
	 *   
	 * @return
	 */
	private JPanel creaPanelNombreEstilo()
	{
		JPanel panel = new JPanel(new GridBagLayout());
		
		JLabel etiquetaNombre = new JLabel ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoNuevoEstilo.etiquetaNombre" ) );
		JTextField campoNombre = new JTextField(this.nombreCapa);
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		//Crea el panel con el nombre del estilo y permite modificarlo
		constraints.gridx = 0; // El área de texto empieza en la columna cero.
		constraints.gridy = 0; // El área de texto empieza en la fila cero
		constraints.gridwidth = 1; // El área de texto ocupa 1 columna.
		constraints.gridheight = 1; // El área de texto ocupa 1 fila.		
		constraints.insets = new Insets(5, 5, 5, 5);
		
		panel.add(etiquetaNombre, constraints);		
		
		
		
		//Crea el panel con el nombre del estilo y permite modificarlo
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		constraints.insets = new Insets(5, 5, 5, 5);
		
		panel.add(campoNombre, constraints);
		
		constraints.weightx = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		return panel;
	}
	
	
	
	
	/**
	 * Crea el panel con los rangos de escala
	 *   
	 * @return
	 */
	private JPanel creaPanelRangosEscala()
	{	
		// Si no esta creada la lista creamos una
		if ( this.listaRangosEscala == null )
		{
			//Este contenido se crea estaticamente a modo ejemplo
			if((this.nombreCapa != null)  &&  (!this.nombreCapa.equals(""))  &&  (this.listaRangosEscala == null))
			{
				this.listaRangosEscala = this.dameRangosEscala();
				this.listaPanelRangosEscala = new JList ( this.listaRangosEscala );			
			}
			else 
			{
				this.listaPanelRangosEscala = new JList();
				this.listaRangosEscala = new Vector<RangoEscala>();
			}
		}
		else
		{
			this.listaPanelRangosEscala = new JList ( this.listaRangosEscala );
		}
								
		listaPanelRangosEscala.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		listaPanelRangosEscala.getSelectionModel().addListSelectionListener ( new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e) 
			{
		        ListSelectionModel lsm = (ListSelectionModel)e.getSource();

		        int elementoSeleccionado = lsm.getMinSelectionIndex();		   
		        if ( ModificaCreaEstiloKml3DDialogo.this.listaRangosEscala != null )
		        {
		        	ModificaCreaEstiloKml3DDialogo.this.rangoEscalaSeleccionado = ModificaCreaEstiloKml3DDialogo.this.listaRangosEscala.get(elementoSeleccionado);
		        }
		        
		        ModificaCreaEstiloKml3DDialogo.this.botonModificarRangosEscala.setEnabled(true);
		        ModificaCreaEstiloKml3DDialogo.this.botonBorrarRangosEscala.setEnabled(true);
		        
		        ModificaCreaEstiloKml3DDialogo.this.refrescaPanelReglasPintado();
			}
		}
		);
		
		
		JScrollPane scrollPane = new JScrollPane(listaPanelRangosEscala);
		
		if ( this.panelRangosEscala == null )
		{
			this.panelRangosEscala = new JPanel(new GridBagLayout());
		}
		else
		{
			this.panelRangosEscala.removeAll();
		}
		
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		this.panelRangosEscala.add(scrollPane, constraints);
		
		return this.panelRangosEscala;
	}
	
	
	
	/**
	 * Crea el panel con los botones para gestionar los rangos de escala
	 *   
	 * @return
	 */
	private JPanel creaPanelBotonesRangosEscala()
	{
		JPanel panel = new JPanel(new GridLayout(3, 1, 8, 8));
		
		JButton botonNuevo = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoNuevoEstilo.botonNuevoRangoEscala"));
		this.botonBorrarRangosEscala = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoNuevoEstilo.botonBorrarRangoEscala"));
		this.botonModificarRangosEscala = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoNuevoEstilo.botonModificarRangoEscala"));		
		
		this.botonBorrarRangosEscala.setEnabled(false);
		this.botonModificarRangosEscala.setEnabled(false);
		
		panel.add(botonNuevo);		
		panel.add(this.botonBorrarRangosEscala);
		panel.add(this.botonModificarRangosEscala);
		
		
		
		botonNuevo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                JDialog dialogoNuevoRangoEscala = new RangoEscalaDialogo(ModificaCreaEstiloKml3DDialogo.this.listaRangosEscala);
                dialogoNuevoRangoEscala.setVisible(true);
                                               
                ModificaCreaEstiloKml3DDialogo.this.botonBorrarRangosEscala.setEnabled(false);
                ModificaCreaEstiloKml3DDialogo.this.botonModificarRangosEscala.setEnabled(false);
                
                ModificaCreaEstiloKml3DDialogo.this.refrescaPanelRangosEscala();
            }
        });
		
		
		
		this.botonModificarRangosEscala.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                JDialog dialogoModificaRangoEscala = new RangoEscalaDialogo(ModificaCreaEstiloKml3DDialogo.this.listaRangosEscala, ModificaCreaEstiloKml3DDialogo.this.rangoEscalaSeleccionado);
                dialogoModificaRangoEscala.setVisible(true);
                
                ModificaCreaEstiloKml3DDialogo.this.refrescaPanelRangosEscala();
            }
        });
		
		
		
		this.botonBorrarRangosEscala.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                ModificaCreaEstiloKml3DDialogo.this.listaRangosEscala.remove ( 
                		ModificaCreaEstiloKml3DDialogo.this.rangoEscalaSeleccionado );
                                               
                ModificaCreaEstiloKml3DDialogo.this.botonBorrarRangosEscala.setEnabled(false);
                ModificaCreaEstiloKml3DDialogo.this.botonModificarRangosEscala.setEnabled(false);
                
                ModificaCreaEstiloKml3DDialogo.this.refrescaPanelRangosEscala();
            }
        });
		
		
		
		return panel;
	}
	
	
	
	
	
	/**
	 * Crea el panel con las reglas de pintado
	 *   
	 * @return
	 */
	private JPanel creaPanelReglasPintado()
	{		
		this.listaPanelesReglasPintado = new Hashtable<TIPOS_REGLAS_PINTADO, JList>();
		
		// Inicializamos las reglas de pintado si no estan de modo estatico para pruebas
		if ( this.listaReglasPintado == null )
		{
			this.listaReglasPintado = this.dameReglasPintado();
		}
		
		JTabbedPane pestanasReglasPintado = new JTabbedPane ();
		
		for ( TIPOS_REGLAS_PINTADO tipo : TIPOS_REGLAS_PINTADO.values() )
		{
			JList listaActual = null;
			
			if ( ( this.nombreCapa != null )  &&  ( !this.nombreCapa.equals("") ) )
			{												
				listaActual = new JList ( this.listaReglasPintado.get ( tipo ) );
				
				// si hay una regla seleccionada la buscamos
				if ( ( this.nombreReglaPintadoSeleccionada != null )  &&  ( !this.nombreReglaPintadoSeleccionada.equals ( "" ) ) )
				{
					for ( int i = 0; i < listaActual.getModel().getSize(); i++ )
					{
						String reglaActual = (String) listaActual.getModel().getElementAt ( i );
						if ( reglaActual.equals ( this.nombreReglaPintadoSeleccionada ) )
						{
							listaActual.setSelectedIndex ( i );
							break;
						}
					}
				}
			}
			else
			{
				listaActual = new JList();
			}
		
			this.listaPanelesReglasPintado.put ( tipo, listaActual );								
			listaActual.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION );		
		
			listaActual.getSelectionModel().addListSelectionListener ( new ListSelectionListener()
			{
				public void valueChanged ( ListSelectionEvent e ) 
				{
			        ListSelectionModel lsm = (ListSelectionModel)e.getSource();	
			        int elementoSeleccionado = lsm.getMinSelectionIndex();
			        				        
			        if ( ( ModificaCreaEstiloKml3DDialogo.this.listaReglasPintado != null )
			        		&&  ( elementoSeleccionado >= 0 ) )
			        {
			        	Vector<String> reglasActual = ModificaCreaEstiloKml3DDialogo.this.listaReglasPintado
		        		.get ( ModificaCreaEstiloKml3DDialogo.this.tipoReglasPintadoSeleccionado );
			        	
			        	ModificaCreaEstiloKml3DDialogo.this.nombreReglaPintadoSeleccionada = 			        		 
			        		reglasActual.get ( elementoSeleccionado );
			        }
			        
			        ModificaCreaEstiloKml3DDialogo.this.botonModificarReglasPintado.setEnabled(true);
			        ModificaCreaEstiloKml3DDialogo.this.botonBorrarReglasPintado.setEnabled(true);
				}
			}
			);
		
			JScrollPane scrollPane = new JScrollPane ( listaActual );
			pestanasReglasPintado.addTab ( tipo.toString(), scrollPane );	
			
			
			// Si habia algun panel seleccionado y coincide con este lo dejamos seleccionado
			if( tipo.equals ( this.tipoReglasPintadoSeleccionado ) )
			{
				pestanasReglasPintado.setSelectedComponent ( scrollPane );
			}		
		}
		
		
		pestanasReglasPintado.addChangeListener(
			new ChangeListener()
			{
				public void stateChanged(ChangeEvent changeEvent)
				{					
					JList listaActiva = ModificaCreaEstiloKml3DDialogo.this.listaPanelesReglasPintado
						.get ( ModificaCreaEstiloKml3DDialogo.this.tipoReglasPintadoSeleccionado );					
					listaActiva.clearSelection();
					
			        JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
			        int index = sourceTabbedPane.getSelectedIndex();
			        String tipoSeleccionado = sourceTabbedPane.getTitleAt ( index );
			        ModificaCreaEstiloKml3DDialogo.this.tipoReglasPintadoSeleccionado = 
			        	TIPOS_REGLAS_PINTADO.valueOf ( tipoSeleccionado );
			        
			        ModificaCreaEstiloKml3DDialogo.this.nombreReglaPintadoSeleccionada = "";			        
			        ModificaCreaEstiloKml3DDialogo.this.botonModificarReglasPintado.setEnabled(false);
			        ModificaCreaEstiloKml3DDialogo.this.botonBorrarReglasPintado.setEnabled(false);
				}
			}
		);
		
				
		if ( this.panelReglasPintado == null )
		{
			this.panelReglasPintado = new JPanel ( new GridBagLayout() );
		}
		else
		{
			this.panelReglasPintado.removeAll();
			
			this.botonModificarReglasPintado.setEnabled(false);
	        this.botonBorrarReglasPintado.setEnabled(false);
		}
		
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		this.panelReglasPintado.add(pestanasReglasPintado, constraints);
		
		
		return this.panelReglasPintado;
	}
	
	
	
	/**
	 * Crea el panel con los botones para gestionar las reglas de pintado
	 *   
	 * @return
	 */
	private JPanel creaPanelBotonesReglasPintado()
	{
		JPanel panel = new JPanel(new GridLayout(6, 1, 8, 8));
		
		JButton botonNuevo = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoNuevoEstilo.botonNuevoReglaPintado" ) );
		JButton botonTematico = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoNuevoEstilo.botonTematicoReglaPintado" ) );
		this.botonModificarReglasPintado = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoNuevoEstilo.botonModificarReglaPintado" ) );
		this.botonBorrarReglasPintado = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoNuevoEstilo.botonBorrarReglaPintado" ) );
		JButton botonArriba = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoNuevoEstilo.botonArribaReglaPintado" ) );
		JButton botonAbajo = new JButton ( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoNuevoEstilo.botonAbajoReglaPintado" ) );			
		
		
		this.botonModificarReglasPintado.setEnabled(false);
		this.botonBorrarReglasPintado.setEnabled(false);
				
		
		
		
		botonNuevo.addActionListener ( new ActionListener()
        {
			public void actionPerformed(ActionEvent evt)
            {
				JDialog dialogo = null;
				switch ( ModificaCreaEstiloKml3DDialogo.this.tipoReglasPintadoSeleccionado )
				{
					case Puntos:
						dialogo = new ReglaPintadoPuntoDialogo();
						break;
					
					case Lineas:
						dialogo = new ReglaPintadoLineaDialogo();
						break;
						
					case Poligonos:
						dialogo = new ReglaPintadoPoligonoDialogo();
						break;
												
					case Textos:
						dialogo = new ReglaPintadoTextoDialogo();
						break;
				}
								
				dialogo.setVisible ( true );
                                               
                ModificaCreaEstiloKml3DDialogo.this.botonBorrarReglasPintado.setEnabled(false);
                ModificaCreaEstiloKml3DDialogo.this.botonModificarReglasPintado.setEnabled(false);                
                
                ModificaCreaEstiloKml3DDialogo.this.refrescaPanelReglasPintado();
            }
        });
		
		
		
		
		ModificaCreaEstiloKml3DDialogo.this.botonModificarReglasPintado.addActionListener ( new ActionListener()
        {
			public void actionPerformed(ActionEvent evt)
            {
				JDialog dialogo = null;
				switch ( ModificaCreaEstiloKml3DDialogo.this.tipoReglasPintadoSeleccionado )
				{
					case Puntos:
						dialogo = new ReglaPintadoPuntoDialogo();
						break;
					
					case Lineas:
						dialogo = new ReglaPintadoLineaDialogo();
						break;
						
					case Poligonos:
						dialogo = new ReglaPintadoPoligonoDialogo();
						break;
												
					case Textos:
						dialogo = new ReglaPintadoTextoDialogo();
						break;
				}
								
				dialogo.setVisible ( true );
                                               
                ModificaCreaEstiloKml3DDialogo.this.botonBorrarReglasPintado.setEnabled(false);
                ModificaCreaEstiloKml3DDialogo.this.botonModificarReglasPintado.setEnabled(false);
                
                ModificaCreaEstiloKml3DDialogo.this.refrescaPanelReglasPintado();
            }
        });
		
		
		
		
		botonTematico.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JDialog dialogo = null;
				switch ( ModificaCreaEstiloKml3DDialogo.this.tipoReglasPintadoSeleccionado )
				{
					case Puntos:
						dialogo = new ReglaTematicaPuntoDialogo();
						break;
					
					case Lineas:
						dialogo = new ReglaTematicaLineaDialogo();
						break;
						
					case Poligonos:
						dialogo = new ReglaTematicaPoligonoDialogo();
						break;
												
					case Textos:
						dialogo = new ReglaTematicaTextoDialogo();
						break;
				}
								
				dialogo.setVisible ( true );
                                               
                ModificaCreaEstiloKml3DDialogo.this.botonBorrarReglasPintado.setEnabled(false);
                ModificaCreaEstiloKml3DDialogo.this.botonModificarReglasPintado.setEnabled(false);
                
                ModificaCreaEstiloKml3DDialogo.this.refrescaPanelReglasPintado();
			}
		}
		);
		
		
		
		// Sube una posicion la regla seleccionado
		botonArriba.addActionListener( new ActionListener() 
		{			
			@Override
			public void actionPerformed(ActionEvent e) 
			{			
				TIPOS_REGLAS_PINTADO tipoSeleccionado = ModificaCreaEstiloKml3DDialogo.this.tipoReglasPintadoSeleccionado;
				String reglaSeleccionada = ModificaCreaEstiloKml3DDialogo.this.nombreReglaPintadoSeleccionada;
				Vector<String> listaReglas = ModificaCreaEstiloKml3DDialogo.this.listaReglasPintado.get ( tipoSeleccionado );
				
				for ( int i = 0; i < listaReglas.size(); i++ )
				{
					String reglaActual = listaReglas.get ( i );
					if ( reglaActual.equals ( reglaSeleccionada ) )
					{
						if ( i > 0 )
						{
							String reglaCambiar = listaReglas.get ( i-1 );
							listaReglas.set ( i-1, reglaActual );
							listaReglas.set ( i, reglaCambiar );
							ModificaCreaEstiloKml3DDialogo.this.refrescaPanelReglasPintado();
							return;
						}
					}
				}
			}
		});
		
		
		
		
		
		
		// Baja una posicion la regla seleccionado
		botonAbajo.addActionListener( new ActionListener() 
		{			
			@Override
			public void actionPerformed(ActionEvent e) 
			{			
				TIPOS_REGLAS_PINTADO tipoSeleccionado = ModificaCreaEstiloKml3DDialogo.this.tipoReglasPintadoSeleccionado;
				String reglaSeleccionada = ModificaCreaEstiloKml3DDialogo.this.nombreReglaPintadoSeleccionada;
				Vector<String> listaReglas = ModificaCreaEstiloKml3DDialogo.this.listaReglasPintado.get ( tipoSeleccionado );
				
				for ( int i = 0; i < listaReglas.size(); i++ )
				{
					String reglaActual = listaReglas.get ( i );
					if ( reglaActual.equals ( reglaSeleccionada ) )
					{
						if ( i < listaReglas.size()-1 )
						{
							String reglaCambiar = listaReglas.get ( i+1 );
							listaReglas.set ( i+1, reglaActual );
							listaReglas.set ( i, reglaCambiar );
							ModificaCreaEstiloKml3DDialogo.this.refrescaPanelReglasPintado();
							return;
						}
					}
				}
			}
		});
		
		
		
		
		
		panel.add ( botonNuevo );
		panel.add ( botonTematico );
		panel.add ( this.botonModificarReglasPintado );
		panel.add ( this.botonBorrarReglasPintado );
		panel.add ( botonArriba );
		panel.add ( botonAbajo );		
		
		return panel;
	}
			
	
	
	/**
	 * Crea el panel con los botones de aceptar y cancelar
	 *   
	 * @return
	 */
	private JPanel creaPanelBotonesAceptarCancelar()
	{
		JPanel panel = new JPanel(new GridLayout(1, 2, 30, 10));
		
		JButton botonAceptar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoEstilos.botonAceptar"));
		JButton botonCancelar = new JButton(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoEstilos.botonCancelar"));
		
		
		botonAceptar.addActionListener(new ActionListener()
        {
			public void actionPerformed(ActionEvent evt)
            {
                ModificaCreaEstiloKml3DDialogo.this.dispose();
            }
        });
				
		
		botonCancelar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                ModificaCreaEstiloKml3DDialogo.this.dispose();
            }
        });
		
		
		panel.add(botonAceptar);
		panel.add(botonCancelar);
		
		return panel;
	}
	
	
	
	

	
	
	/**
	 * Devuelve la lista de rangos de escala
	 * 
	 * @return
	 */
	private Vector<RangoEscala> dameRangosEscala()
	{
		Vector<RangoEscala> listaRangos = new Vector<RangoEscala>();
				
		listaRangos.add( new RangoEscala ( "25", "55" ) );
		listaRangos.add( new RangoEscala ( "89", "5E99" ) );
		
		return listaRangos;
	}
	
	
	
	
	/**
	 * Devuelve la lista de reglas de pintado
	 * 
	 * @return
	 */
	private Hashtable<TIPOS_REGLAS_PINTADO, Vector<String>> dameReglasPintado()
	{
		Hashtable<TIPOS_REGLAS_PINTADO, Vector<String>> listaReglas = new Hashtable<TIPOS_REGLAS_PINTADO, Vector<String>>();
		
		TIPOS_REGLAS_PINTADO [] tiposReglasPintado = TIPOS_REGLAS_PINTADO.values();
				
		int numRegla = 1;
		int totalReglas = 1;
		
		for ( TIPOS_REGLAS_PINTADO tipo : tiposReglasPintado )
		{
			Vector<String> listaRegla = new Vector<String>();
			
			for ( int i=0; i<totalReglas; i++)
			{
				listaRegla.add ( "Regla " + numRegla );
				numRegla ++;
			}
			
			listaReglas.put ( tipo, listaRegla );
			
			totalReglas ++;
		}		
		
		return listaReglas;
	}
	
	
}

