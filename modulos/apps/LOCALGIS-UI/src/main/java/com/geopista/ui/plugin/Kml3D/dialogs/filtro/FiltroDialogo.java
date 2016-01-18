/**
 * FiltroDialogo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.dialogs.filtro;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.geopista.ui.plugin.Kml3D.datos.filtros.Elemento;
import com.geopista.ui.plugin.Kml3D.datos.filtros.Operador;
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperadorComparacion;
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperadorEspacial;
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperadorEspacialBbox;
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperadorEspacialBeyond;
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperadorEspacialDwithin;
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperadorLogico;
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperandoLiteral;
import com.geopista.ui.plugin.Kml3D.datos.filtros.OperandoPropiedad;
import com.vividsolutions.jump.I18N;

/**
 * Dialogo para generar los filtros
 * 
 * @author David Vicente
 *
 */
public class FiltroDialogo extends JDialog implements ActionListener{

	
	public static final int DIM_X = 400;
	public static final int DIM_Y = 300;

		
	private JPanel panelPrincipal = null;
	private JTextField campoTexto = null;
	
	private String textoFiltro = "";
	private String textoArbolVacio = I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoFiltro.textoAreaFiltro" ) ;
	private boolean arbolVacio = true;
	
	private JTree arbol = null;
	
	
	
	
	public FiltroDialogo()
	{
		super();
		this.inicializar();
	}
	
	
	
	
	/**
	 * Inicializamos el cuadro de dialogo
	 */
	private void inicializar()
	{
		this.setModal(true);  
		this.setTitle(I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoFiltro.tituloDialogo"));
		this.setResizable(true);  
		this.setSize(DIM_X, DIM_Y);
		this.setContentPane ( this.creaPanelModificaCreaFiltro() );
		
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
	 * Refresca el panel principal
	 */
	public void refrescarPanelPrincipal()
	{
		this.creaPanelModificaCreaFiltro();
		this.arbol.updateUI();
		this.panelPrincipal.updateUI();		
	}
	
	
	
	
	/**
	 * Crea el panel principal de la ventana
	 * 
	 * @return
	 */
	public JPanel creaPanelModificaCreaFiltro()
	{
		if ( this.panelPrincipal == null )
		{
			this.panelPrincipal = new JPanel ( new GridBagLayout() );
		}
		else
		{
			this.panelPrincipal.removeAll();
		}
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		
		
		// Agrega la lista de reglas
		if ( this.arbol == null )
		{
			DefaultMutableTreeNode arbolVacio = new DefaultMutableTreeNode( this.textoArbolVacio );
			this.arbol = new JTree ( arbolVacio );
		}
		
		this.arbol.addMouseListener
		(
			new MouseAdapter()
			{
			   public void mousePressed(MouseEvent ev)
			   {			
				   // Si no hay ningun elemento seleccionado no mostramos nada
				   if ( FiltroDialogo.this.arbol.getSelectionPath() == null )
				   {
					   return;
				   }
				   
				   if(ev.getButton()==MouseEvent.BUTTON3)
				   {
					   	JPopupMenu pop = FiltroDialogo.this.creaPopup();
					   	pop.show ( (Component) ev.getSource(), ev.getX(), ev.getY() );
				   		pop.setInvoker( FiltroDialogo.this.arbol );
				   }
			   }
			}				
		);
		
		constraints.gridx = 0; // El área de texto empieza en la columna cero.
		constraints.gridy = 0; // El área de texto empieza en la fila cero
		constraints.gridwidth = 1; // El área de texto ocupa dos columnas.
		constraints.gridheight = 1; // El área de texto ocupa 1 fila.
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(8, 8, 8, 8);
		
		JScrollPane scroll = new JScrollPane ( this.arbol );
		this.panelPrincipal.add ( scroll, constraints );
		
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		
		// Agrega el campoTexto
		this.campoTexto = new JTextField ( 10 );
		this.campoTexto.setFont( new Font ( "Arial", Font.PLAIN, 12 ) );
		this.campoTexto.setText( I18N.get ( Kml3DPlugIn.name,"Kml3D.dialogoFiltro.textoCampo" ) );
		this.campoTexto.setBackground ( Color.LIGHT_GRAY );
		this.campoTexto.setEditable ( false );
		
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;		
		
		this.panelPrincipal.add (this.campoTexto, constraints );
		
		constraints.weightx = 0.0;		
		constraints.fill = GridBagConstraints.NONE;
		
		
		
		
		// Agregamos el panel de botones aceptar y cancelar
		constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;		
		
		this.panelPrincipal.add (this.creaPanelBotonesAceptarCancelar(), constraints );
		
		constraints.weightx = 0.0;		
		
		
		
		
		
		return this.panelPrincipal;
	}
	
	
	
	
	

	
	/**
	 * Crea el panel con los botones de aceptar y cancelar
	 *   
	 * @return
	 */
	protected JPanel creaPanelBotonesAceptarCancelar()
	{
		JPanel panel = new JPanel(new GridLayout(1, 2, 30, 10));
		
		
		JButton botonAceptar = new JButton ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.botonAceptar" ) );
		JButton botonCancelar = new JButton ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.botonCancelar" ) );
		
		
		botonAceptar.addActionListener(new ActionListener()
        {
			public void actionPerformed(ActionEvent evt)
            {
                FiltroDialogo.this.dispose();
            }
        });
				
		
		botonCancelar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
            	FiltroDialogo.this.dispose();
            }
        });
		
		
		panel.add(botonAceptar);
		panel.add(botonCancelar);
		
		return panel;
	}
	
	
	
	
	/**
	 * Crea el Popup del menu 
	 * 
	 * @return
	 */
	private JPopupMenu creaPopup()
	{
		if ( this.arbolVacio )
		{
			return this.creaPopupArbolVacio();
		}			
		else
		{
			return this.creaPopupArbolNoVacio();
		}
	}
	
	
	
	
	/**
	 * Crea el Popup del menu cuando el arbol esta vacio
	 * 
	 * @return
	 */
	private JPopupMenu creaPopupArbolVacio()
	{
		JPopupMenu popMenu = new JPopupMenu();		
		
		this.creaMenuCambiarA(popMenu, null);
			 	
		return popMenu;
	}
	
	
	
	/**
	 * Crea el Popup cuando el arbol no esta vacio
	 * @return
	 */
	private JPopupMenu creaPopupArbolNoVacio()
	{
		JPopupMenu popMenu = new JPopupMenu();					
		
		TreePath path = this.arbol.getSelectionPath();
		DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode) path.getLastPathComponent();
		Elemento elemento = Elemento.dameElemento(nodoSeleccionado.toString());
		
		
		if ( !this.arbol.getModel().getRoot().equals( nodoSeleccionado ) ) 
		{
			this.creaMenuBorrar ( popMenu );
		}
			
		
		 
		if ( elemento instanceof Operador )
		{
			( ( Operador ) elemento ).setNumHijos ( nodoSeleccionado.getChildCount() );
			if ( elemento instanceof OperadorEspacial )
			{
				this.creaMenuModificar ( popMenu );
			}
			
			this.creaMenuCambiarA ( popMenu, elemento );
			
			if ( ((Operador) elemento).masHijosPermitidos() )
			{
				this.creaMenuAnnadirHijo ( popMenu, elemento );
			}
		}
		else   // Operando
		{
			this.creaMenuModificar ( popMenu );
		}
		
		
		return popMenu;
	}
	

	
	
	private void creaMenuCambiarA ( JPopupMenu popMenu, Elemento elemento )
	{
		String actionCommand = I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupCambiarA" );
			
		//Item "Cambiar a"
		JMenu menuCambiarA = new JMenu ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupCambiarA" ) );		
		popMenu.add(menuCambiarA);         
		
		
		JMenu menuLogico = new JMenu ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupCambiarALogico" ) );
		this.creaSubmenuLogico ( menuLogico, actionCommand );
		menuCambiarA.add ( menuLogico );
		
		
		if ( ( elemento == null )  ||  ( ! ( elemento instanceof OperadorLogico ) ) )
		{
			JMenu menuComparacion = new JMenu ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupCambiarAComparacion" ) );
			this.creaSubmenuComparacion ( menuComparacion, actionCommand );
			menuCambiarA.add ( menuComparacion );
			
			JMenu menuEspacial = new JMenu ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupCambiarAEspacial" ) );
			this.creaSubmenuEspacial ( menuEspacial, actionCommand );
			menuCambiarA.add ( menuEspacial );
		}
	}

	
	
	private void creaMenuAnnadirHijo ( JPopupMenu popMenu, Elemento elemento )
	{
		String actionCommand = I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupAnnadirHijo" );
			
		// Item Añadir hijo
		JMenu menuAnnadirHijo = new JMenu ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupAnnadirHijo" ) );
		popMenu.add ( menuAnnadirHijo );
		
		
		if ( ( elemento == null )  ||  ( elemento instanceof OperadorLogico ) )
		{
			JMenu menuOperador = new JMenu ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupOperador" ) );
			menuAnnadirHijo.add ( menuOperador );
			
			
			JMenu menuLogico = new JMenu ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupCambiarALogico" ) );
			this.creaSubmenuLogico ( menuLogico, actionCommand );
			menuOperador.add ( menuLogico );
		
			JMenu menuComparacion = new JMenu ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupCambiarAComparacion" ) );
			this.creaSubmenuComparacion ( menuComparacion, actionCommand );
			menuOperador.add ( menuComparacion );
			
			JMenu menuEspacial = new JMenu ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupCambiarAEspacial" ) );
			this.creaSubmenuEspacial ( menuEspacial, actionCommand );
			menuOperador.add ( menuEspacial );
		}
		else
		{
			JMenu menuOperador = new JMenu ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupExpresion" ) );
			menuAnnadirHijo.add ( menuOperador );
			
			
			JMenuItem menuPropertyName = new JMenuItem ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupPropertyName" ) );
			menuPropertyName.addActionListener ( this );
			menuOperador.add ( menuPropertyName );
			
			JMenuItem menuLiteral = new JMenuItem ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupLiteral" ) );
			menuLiteral.addActionListener ( this );
			menuOperador.add ( menuLiteral );
		}
	}
	
	
	

	private void creaMenuBorrar ( JPopupMenu popMenu )
	{
		JMenuItem itemBorrar = new JMenuItem ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupBorrar" ) );
		itemBorrar.setActionCommand( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupBorrar" ) );
		itemBorrar.addActionListener( this );
		popMenu.add ( itemBorrar );		
	}
	
	
	
	
	private void creaMenuModificar ( JPopupMenu popMenu )
	{
		JMenuItem itemModificar = new JMenuItem ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupModificar" ) );
		itemModificar.setActionCommand( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupModificar" ) );
		itemModificar.addActionListener ( this );
		popMenu.add ( itemModificar );		
	}
	
	
	
	
	
	/**
	 * Crea el submenu "lógico"
	 * 
	 * @param menuLogico
	 */
	private void creaSubmenuLogico ( JMenu menuLogico, String actionCommand  )
	{
		for ( JMenuItem menuItem : OperadorLogico.dameItemsMenuLogico ( this ) )
		{			
			menuItem.setActionCommand ( actionCommand );
			menuLogico.add ( menuItem );
		}
	}
	
	
	
	
	/**
	 * Crea el submenu "comparación"
	 * 
	 * @param menuComparacion
	 */
	private void creaSubmenuComparacion ( JMenu menuComparacion, String actionCommand )
	{				
		for ( JMenuItem menuItem : OperadorComparacion.dameItemsMenuComparacion ( this ) )
		{			
			menuItem.setActionCommand ( actionCommand );
			menuComparacion.add ( menuItem );
		}
	}
	
	
	
	
	
	
	/**
	 * Crea el submenu "espacial"
	 * 
	 * @param menuEspacial
	 */
	private void creaSubmenuEspacial ( JMenu menuEspacial, String actionCommand  )
	{
		for ( JMenuItem menuItem : OperadorEspacial.dameItemsMenuEspacial ( this ) )
		{			
			menuItem.setActionCommand ( actionCommand );
			menuEspacial.add ( menuItem );
		}
	}
	
	
	
	
	
	

	@Override
	public void actionPerformed(ActionEvent e) 
	{		
		JMenuItem menuItem = (JMenuItem) e.getSource();		
		
		String textoItem = menuItem.getText();
		if ( textoItem.equals ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupModificar" ) ) )
		{
			this.modificaNodo ();
		}
		else if ( textoItem.equals ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupBorrar" ) ) )
		{
			TreePath path = this.arbol.getSelectionPath();
			DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode) path.getLastPathComponent();						
			
			( (DefaultTreeModel) this.arbol.getModel()).removeNodeFromParent ( nodoSeleccionado );			
			this.arbol.expandPath(path);
		}
		else if ( textoItem.equals( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupLiteral" ) ) )
		{
			LiteralParametersDialogo dialogo = new LiteralParametersDialogo();
			dialogo.setVisible ( true );
			String textoLiteral = dialogo.getTextoLiteral();
			
			if ( ( textoLiteral != null )  &&  ( !textoLiteral.equals( "" ) ) )
			{
				this.agregaNodo( new OperandoLiteral ( textoLiteral ) );
			}
		}
		else if ( textoItem.equals( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupPropertyName" ) ) )
		{
			PropertyNameDialogo dialogo = new PropertyNameDialogo();
			dialogo.setVisible ( true );
			String textoPropertyName = dialogo.getTexto();
			
			if ( ( textoPropertyName != null )  &&  ( !textoPropertyName.equals( "" ) ) )
			{
				this.agregaNodo( new OperandoPropiedad( textoPropertyName ) );
			}
		}		
		else 
		{
			String comando = menuItem.getActionCommand();
			
			if ( ( this.arbolVacio )  ||  ( comando.equals ( I18N.get ( Kml3DPlugIn.name, "Kml3D.dialogoFiltro.popupAnnadirHijo" ) ) ) )
			{
				Elemento elemento = Elemento.dameElemento( textoItem );
				this.agregaNodo ( elemento );
			}
			else
			{
				this.cambiaItem ( menuItem );
			}
		}	
		
		
		this.actualizarFiltro();
	}
	


	
	/**
	 * Agrega un elemento al arbol
	 * 
	 * @param elemento
	 */
	private void agregaNodo ( Elemento elemento )
	{	
		if ( elemento instanceof OperadorEspacial )
		{
			OperadorEspacial operadorEspacial = ( OperadorEspacial ) elemento;
			if ( operadorEspacial.getOperadorEspacial().equals ( OperadorEspacial.OPERADOR_ESPACIAL.BBOX ) )
			{
				BBoxDialogo dialogo = new BBoxDialogo();
				dialogo.setVisible ( true );
				OperadorEspacialBbox operadorEspacialBbox = dialogo.getOperadorEspacialBbox();
				String texto = operadorEspacialBbox.getTexto();
				
				if ( ( texto != null )  &&  ( !texto.equals("") ) )
				{
					this.agregaNodo ( elemento, texto );
					return;
				}
			}
			else if ( operadorEspacial.getOperadorEspacial().equals ( OperadorEspacial.OPERADOR_ESPACIAL.BEYOND ) )
			{
				BeyondDialogo dialogo = new BeyondDialogo();
				dialogo.setVisible ( true );
				OperadorEspacialBeyond operadorEspacialBeyond = dialogo.getOperadorEspacialBeyond();
				String texto = operadorEspacialBeyond.getTexto();
				
				if ( ( texto != null )  &&  ( !texto.equals("") ) )
				{
					this.agregaNodo ( elemento, texto );
					return;
				}
			}
			else if ( operadorEspacial.getOperadorEspacial().equals ( OperadorEspacial.OPERADOR_ESPACIAL.DWITHIN ) )
			{
				DwithinDialogo dialogo = new DwithinDialogo();
				dialogo.setVisible ( true );
				OperadorEspacialDwithin operadorEspacialDwithin = dialogo.getOperadorEspacialDwithin();
				String texto = operadorEspacialDwithin.getTexto();
				
				if ( ( texto != null )  &&  ( !texto.equals("") ) )
				{
					this.agregaNodo ( elemento, texto );
					return;
				}
			}
		}
		
		
		this.agregaNodo ( elemento, null );
	}
	
	
	
	/**
	 * Agrega un elemento al arbol
	 * 
	 * @param elemento
	 */
	private void agregaNodo ( Elemento elemento, String info )
	{				
		DefaultMutableTreeNode nodoArbol = new DefaultMutableTreeNode( elemento.getTexto() );
		
		if ( info != null )
		{
			nodoArbol.setUserObject ( info );
		}
		
		
		if ( this.arbolVacio )
		{			
			this.arbol = new JTree ( nodoArbol );			
			this.refrescarPanelPrincipal();
			this.arbol.setSelectionRow ( -1 );
			this.arbolVacio = false;
		}
		else
		{								
			TreePath path = this.arbol.getSelectionPath();
			DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode) path.getLastPathComponent();			
			int numeroHijos = nodoSeleccionado.getChildCount();
			
			( (DefaultTreeModel) this.arbol.getModel()).insertNodeInto ( nodoArbol, nodoSeleccionado, numeroHijos);			
			this.arbol.expandPath(path);
		}
	}
	
	
	
	
	
	private void modificaNodo()
	{
		TreePath path = this.arbol.getSelectionPath();
		DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode) path.getLastPathComponent();
		
		Elemento elemento = Elemento.dameElemento(nodoSeleccionado.toString());
		
		if ( elemento instanceof OperandoLiteral )
		{
			OperandoLiteral operandoLiteral = (OperandoLiteral) elemento;
			LiteralParametersDialogo dialogo = new LiteralParametersDialogo ( operandoLiteral );
			dialogo.setVisible(true);
			
			nodoSeleccionado.setUserObject ( operandoLiteral.getTexto() );
			this.refrescarPanelPrincipal();
		}
		else if ( elemento instanceof OperandoPropiedad )
		{
			OperandoPropiedad operandoPropiedad = (OperandoPropiedad) elemento;
			PropertyNameDialogo dialogo = new PropertyNameDialogo ( operandoPropiedad );
			dialogo.setVisible(true);
			
			nodoSeleccionado.setUserObject ( operandoPropiedad.getTexto() );
			this.refrescarPanelPrincipal();
		}
		else if ( elemento instanceof OperadorEspacialBbox )
		{
			OperadorEspacialBbox operandoEspacial = (OperadorEspacialBbox) elemento;
			BBoxDialogo dialogo = new BBoxDialogo ( operandoEspacial );
			dialogo.setVisible(true);
			
			nodoSeleccionado.setUserObject ( operandoEspacial.getTexto() );
			this.refrescarPanelPrincipal();
		}
		else if ( elemento instanceof OperadorEspacialBeyond )
		{
			OperadorEspacialBeyond operandoEspacial = (OperadorEspacialBeyond) elemento;
			BeyondDialogo dialogo = new BeyondDialogo ( operandoEspacial );
			dialogo.setVisible(true);
			
			nodoSeleccionado.setUserObject ( operandoEspacial.getTexto() );
			this.refrescarPanelPrincipal();
		}
		else if ( elemento instanceof OperadorEspacialDwithin )
		{
			OperadorEspacialDwithin operandoEspacial = (OperadorEspacialDwithin) elemento;
			DwithinDialogo dialogo = new DwithinDialogo ( operandoEspacial );
			dialogo.setVisible(true);
			
			nodoSeleccionado.setUserObject ( operandoEspacial.getTexto() );
			this.refrescarPanelPrincipal();
		}
	}
	
	
	
	
	/**
	 * Cambia un elemento al arbol
	 * 
	 * @param menuItem
	 */
	private void cambiaItem ( JMenuItem menuItem )
	{		
		TreePath path = this.arbol.getSelectionPath();
		DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode) path.getLastPathComponent();
		
		String texto = menuItem.getText();
		Elemento elemento = Elemento.dameElemento ( texto );		
			
		if ( elemento instanceof OperadorEspacial )
		{
			OperadorEspacial operadorEspacial = ( OperadorEspacial ) elemento;
			if ( operadorEspacial.getOperadorEspacial().equals ( OperadorEspacial.OPERADOR_ESPACIAL.BBOX ) )
			{
				BBoxDialogo dialogo = new BBoxDialogo();
				dialogo.setVisible ( true );
				OperadorEspacialBbox operadorEspacialBbox = dialogo.getOperadorEspacialBbox();
				texto = operadorEspacialBbox.getTexto();
			}
			else if ( operadorEspacial.getOperadorEspacial().equals ( OperadorEspacial.OPERADOR_ESPACIAL.BEYOND ) )
			{
				BeyondDialogo dialogo = new BeyondDialogo();
				dialogo.setVisible ( true );
				OperadorEspacialBeyond operadorEspacialBeyond = dialogo.getOperadorEspacialBeyond();
				texto = operadorEspacialBeyond.getTexto();				
			}
			else if ( operadorEspacial.getOperadorEspacial().equals ( OperadorEspacial.OPERADOR_ESPACIAL.DWITHIN ) )
			{
				DwithinDialogo dialogo = new DwithinDialogo();
				dialogo.setVisible ( true );
				OperadorEspacialDwithin operadorEspacialDwithin = dialogo.getOperadorEspacialDwithin();
				texto = operadorEspacialDwithin.getTexto();
			}
		}
		
		nodoSeleccionado.setUserObject( texto );
		this.arbol.updateUI();
	}
	
	
	
	
	
	public String getTextoFiltro() 
	{
		return textoFiltro;
	}


	
	/**
	 * Actualiza el texto completo del filtro
	 */
	private void actualizarFiltro()
	{
		TreeNode rootNode = ( TreeNode ) this.arbol.getModel().getRoot();
		 
		Operador operadorRaiz = ( Operador ) Elemento.dameElemento ( rootNode );
		this.textoFiltro = operadorRaiz.creaFiltro();
		this.campoTexto.setText ( this.textoFiltro );
	}
	

	

	/**
	 * Metodo que crea un filtro y lo retorna 
	 * 
	 * @return
	 */
	public static String creaFiltro()
	{					
		FiltroDialogo filtroDialogo = new FiltroDialogo();		
		filtroDialogo.setVisible ( true );		
		
		return filtroDialogo.getTextoFiltro();
	}




	
	
	
	
}
