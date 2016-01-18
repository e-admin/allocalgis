
/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

package com.geopista.ui.dialogs;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.AppContext;
import com.geopista.ui.GeopistaTaskFrame;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.util.FileUtil;
import com.vividsolutions.jump.util.java2xml.Java2XML;
import com.vividsolutions.jump.util.java2xml.XML2Java;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class GeopistaGestionBookmarksPanel extends JPanel
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;
	private ApplicationContext aplicacion = AppContext.getApplicationContext();

	private JList lstBookmark = new JList();


	private JLabel lblNombreMarcador = new JLabel();
	private PlugInContext context;
	private HashMap hashTabla = new HashMap();
	private DefaultListModel modeloList = null;
	private DefaultListModel defaultListModel = null;
	private JTextField txtNombreMarcador = null;
	private JPanel buttonPanel = null;
	private JButton btnNuevo = null;
	private JButton btnEliminar = null;
	private JButton btnIrA = null;
	private JButton btnCerrar = null;
	private JScrollPane scpBookmark = null;
	private JPanel jPanelSelectBookmark = null;
	
	public GeopistaGestionBookmarksPanel ()
	{
		jbInit();
	}
	public GeopistaGestionBookmarksPanel (PlugInContext context)
	{
		this.context = context;

		jbInit();

	}

	private void jbInit() 
	{

		this.setLayout(new GridBagLayout());

		this.add(getJPanelSelectBookmark(), 
				new GridBagConstraints(0,0,1,1, 1.0, 1.0,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

		this.setVisible(true);	
	}

	private JPanel getJPanelSelectBookmark() {

		if (jPanelSelectBookmark  == null){			

			lstBookmark = new JList();
			lstBookmark.setModel(getDefaultListModel());		
			lstBookmark.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			
			lblNombreMarcador.setText(aplicacion.getI18nString("GeopistaGestionBookmarksPanel.NombreMarcador"));

			jPanelSelectBookmark = new JPanel(new GridBagLayout());
			
			jPanelSelectBookmark.setSize(new java.awt.Dimension(500,220));
			jPanelSelectBookmark.setPreferredSize(new Dimension(500,220));
			jPanelSelectBookmark.setMaximumSize(jPanelSelectBookmark.getPreferredSize());
			jPanelSelectBookmark.setMinimumSize(jPanelSelectBookmark.getPreferredSize());
				
			jPanelSelectBookmark.setBorder(BorderFactory.createTitledBorder
	                  (null,aplicacion.getI18nString("GeopistaGestionBookmarksPanel.SelectBookmark"), 
	                          TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 10),Color.BLUE));
	          
			jPanelSelectBookmark.add(lblNombreMarcador,
					new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(5,5,5,5),0,0));
			
			jPanelSelectBookmark.add(getTxtNombreMarcador(),
					new GridBagConstraints(1,0,2,1, 1.0, 1.0,GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));

			jPanelSelectBookmark.add(getScpBookmark(), 
					new GridBagConstraints(1,1,3,2, 1.0, 1.0,GridBagConstraints.NORTH,
							GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
			
			jPanelSelectBookmark.add(getButtonPanel(),
					new GridBagConstraints(4,0,1,3, 0.1, 0.1,GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(5,5,5,5),0,0));

			jPanelSelectBookmark.add(getBtnCerrar(),
					new GridBagConstraints(4,3,1,1, 1.0, 1.0,GridBagConstraints.SOUTH,
							GridBagConstraints.NONE, new Insets(5,5,5,5),0,0));

		}
		return jPanelSelectBookmark;
	}
	
	private void btnNuevo_actionPerformed(ActionEvent e)
	{
		/*Activamos el botón del Nombre del Marcador*/

		if (txtNombreMarcador.getText().trim().equals(""))
		{
			txtNombreMarcador.setText(null);
			JOptionPane.showMessageDialog(this,aplicacion.getI18nString("GeopistaGestionBookmarksPanel.DebeDarUnNombreAlMarcador"));
		}else{
			//Procedimiento para añadir un elemento más a la lista  
			// comprobamos que no exista el nombre ya en la lista
			if (hashTabla.containsKey(txtNombreMarcador.getText().trim())){
				JOptionPane.showMessageDialog(this,aplicacion.getI18nString("GeopistaGestionBookmarksPanel.NoSePuedenCrearMarcadoresConElMismoNombre"));            
			}else{
				modeloList.addElement(txtNombreMarcador.getText().trim());
				GeopistaBookmark c1 = new GeopistaBookmark();

				c1.xmin=this.context.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMinX();
				c1.ymin=this.context.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMinY();
				c1.xmax=this.context.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMaxX();
				c1.ymax =this.context.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates().getMaxY();
				c1.nombreMarcador=txtNombreMarcador.getText().trim();
				hashTabla.put(txtNombreMarcador.getText().trim(),c1);

				String bookmarks  = aplicacion.getPath("bookmarks");
				GeopistagrabarHashTabla(bookmarks);



				txtNombreMarcador.setText(null);
			}
		} 

	}

	public static HashMap getListaMarcadores(){
		HashMap lista = new HashMap();
		try {


			String bookmarks = AppContext.getApplicationContext().getPath("bookmarks");

			File file = new File(bookmarks);

			//creamos el directorio si no existe
			File directorioBase = new File(file.getParent());
			if(!directorioBase.exists())
				directorioBase.mkdirs();

			if (file.exists()){
				FileReader reader = null;
				GeopistaSaveBookmark sourceMap = null;
				try {

					reader = new FileReader(file);

					XML2Java converter = new XML2Java();
					sourceMap = (GeopistaSaveBookmark) converter.read(reader, GeopistaSaveBookmark.class);
					lista = sourceMap.getProperties();

				} finally {reader.close();}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return (lista);

	}

	private void btnEliminar_actionPerformed(ActionEvent e)
	{
		//Eliminamos el marcador de la tabla
		if((lstBookmark.getSelectedIndex()==-1) && (modeloList.elements().hasMoreElements()))
		{
			JOptionPane.showMessageDialog(this,aplicacion.getI18nString("GeopistaGestionBookmarksPanel.SeleccioneUnMarcadorABorrar"));
		}else{
			if (modeloList.elements().hasMoreElements()){
				hashTabla.remove(lstBookmark.getSelectedValue());
				modeloList.removeAllElements();
				//Asignamos de nuevo los datos al list
				Set e1 = hashTabla.keySet();
				for (Iterator eIter = e1.iterator(); eIter.hasNext();){
					modeloList.addElement(eIter.next());
				}

				String bookmarks = aplicacion.getPath("bookmarks");
				GeopistagrabarHashTabla(bookmarks);
			}else{
				JOptionPane.showMessageDialog(this,aplicacion.getI18nString("GeopistaGestionBookmarksPanel.NoExistenMarcadoresABorrar"));
			} //De no exisitir elementos
		} //Del Vacío
	} //De método eleminar

	/**
	 * Método que graba la tabla que esta en memoria con los bookmarks
	 * a un fichero XML que se pasa como parámetro
	 * @param String, fichero XML donde se grabarán los marcadores
	 */
	public void GeopistagrabarHashTabla(String ficheroXml){
		File saveSchemaLayer = new File(ficheroXml);
		StringWriter stringWriterXml = new StringWriter();
		try {
			Java2XML converter = new Java2XML(); 
			GeopistaSaveBookmark b = new GeopistaSaveBookmark();
			b.setProperties(hashTabla);
			converter.write(b,"NodoRaiz",stringWriterXml);
			FileUtil.setContents(saveSchemaLayer.getAbsolutePath(),stringWriterXml.toString());

		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
			stringWriterXml.flush();
		}
	}

	/**
	 * Método que llamara a la función del editor de irA 
	 * Se le pasa la clase GeopistaBookmark 
	 */
	public void irAMarcador(GeopistaBookmark elemento){
		//CuadroDialogo.showMessageDialog(this,"Nombre" + elemento.getNombreMarcador());
		Envelope envelope = new Envelope(); //context.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates();

		// CALCULAMOS EL ENVELOPE DE LA FEATURE      
		double minX = elemento.getXmin();
		double maxX = elemento.getXmax();
		double minY = elemento.getYmin();
		double maxY = elemento.getYmax();

		// INICIALIZAMOS EL ENVELOPE
		envelope.init(minX,maxX,minY,maxY);

		// ZOOM AL ENVELOPE DEL BOOKMARK
		try
		{
			if (context.getActiveInternalFrame() instanceof GeopistaTaskFrame)
			{
				((GeopistaTaskFrame)context.getActiveInternalFrame()).getLayerViewPanel().getViewport().zoom(envelope);    
			}


		}catch(NoninvertibleTransformException ne)
		{
			ne.printStackTrace();
		}


	}

	private void btnIrA_actionPerformed(ActionEvent e)
	{
		//Comprobar que existan marcadores
		if (lstBookmark.getSelectedIndex()==-1)
		{ //No existen Marcadores
			JOptionPane.showMessageDialog(this,aplicacion.getI18nString("GeopistaGestionBookmarksPanel.SeleccioneUnMarcador"));            
		}else{
			//LLamar a la función de GeopistaIrAMarcador();
			if (hashTabla.containsKey(lstBookmark.getSelectedValue()))
			{

				irAMarcador(getSelectedBookmark());


			}

		}

	}
	public GeopistaBookmark getSelectedBookmark()
	{
		return  (GeopistaBookmark) hashTabla.get(lstBookmark.getSelectedValue());
	}
	private void btnCerrar_actionPerformed(ActionEvent e)
	{
		Frame dialogoPadre  = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,this);
		dialogoPadre.setVisible(false);
	}



	/**
	 * @param listener
	 */
	public void addListSelectionListener(ListSelectionListener listener)
	{
		lstBookmark.addListSelectionListener(listener);
	}
	/**
	 * @param listener
	 */
	public void removeListSelectionListener(ListSelectionListener listener)
	{
		lstBookmark.removeListSelectionListener(listener);
	}
	/**
	 * @param context The context to set.
	 */
	public void setContext(PlugInContext context)
	{
		this.context = context;
	}
	/**
	 * This method initializes defaultListModel	
	 * 	
	 * @return javax.swing.DefaultListModel	
	 */    
	private DefaultListModel getDefaultListModel() {
		
		if (defaultListModel == null) {

			defaultListModel = new DefaultListModel();
			
			//Iteramos sobre el Hastable con los marcadores almacenados
			//Creamos un modeloList que es la forma de cargar la lista
			modeloList=defaultListModel;	   
			hashTabla = getListaMarcadores();
			modeloList = defaultListModel;
			Set e = hashTabla.keySet();
			for (Iterator eIter = e.iterator(); eIter.hasNext();){
				modeloList.addElement(eIter.next());
			}
		}
		return defaultListModel;
	}
	/**
	 * This method initializes txtNombreMarcador	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtNombreMarcador() {
		if (txtNombreMarcador == null) {
			txtNombreMarcador = new JTextField();
		}
		return txtNombreMarcador;
	}
	public void setCloseVisible(boolean visible)
	{
		getBtnCerrar().setVisible(visible);
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getButtonPanel() {
		
		if (buttonPanel == null) {
			
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());  // Generated
			
			buttonPanel.add(getBtnIrA(),
					new GridBagConstraints(0,0,1,1, 0.1, 0.1, GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
			
			buttonPanel.add(getBtnEliminar(),
					new GridBagConstraints(0,1,1,1, 0.1, 0.1, GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
			
			buttonPanel.add(getBtnNuevo(),
					new GridBagConstraints(0,2,1,1, 0.1, 0.1, GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
		}
		return buttonPanel;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnNuevo() {
		if (btnNuevo == null) {
			btnNuevo = new JButton();
			btnNuevo.setText(aplicacion.getI18nString("GeopistaGestionBookmarksPanel.Nuevo"));
			btnNuevo.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					btnNuevo_actionPerformed(e);
				}
			});
		}
		return btnNuevo;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnEliminar() {
		if (btnEliminar == null) {
			btnEliminar = new JButton();
			btnEliminar.setText(aplicacion.getI18nString("GeopistaGestionBookmarksPanel.Eliminar"));
			btnEliminar.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					btnEliminar_actionPerformed(e);
				}
			});
		}
		return btnEliminar;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnIrA() {
		if (btnIrA == null) {
			btnIrA = new JButton();
			btnIrA.setText(aplicacion.getI18nString("GeopistaGestionBookmarksPanel.IrA"));
			btnIrA.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					btnIrA_actionPerformed(e);
				}
			});

		}
		return btnIrA;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnCerrar() {
		
		if (btnCerrar == null) {
			
			btnCerrar = new JButton();
			btnCerrar.setText(aplicacion.getI18nString("GeopistaGestionBookmarksPanel.Cerrar"));
			btnCerrar.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					btnCerrar_actionPerformed(e);
				}
			});
		}
		return btnCerrar;
	}

	public void setEnabled (boolean isEnabled)
	{

		Color fg = null; 
		Color nofg = null;

		if (isEnabled)
		{
			fg = Color.BLACK;
			nofg = Color.GRAY;

		}
		else
		{
			fg = Color.GRAY;
			nofg = Color.BLACK;
		}


		lblNombreMarcador.setForeground(fg);
		getBtnEliminar().setEnabled(isEnabled);
		getBtnIrA().setEnabled(isEnabled);
		getBtnNuevo().setEnabled(isEnabled);
		getScpBookmark().setEnabled(isEnabled);
		getBtnCerrar().setEnabled(isEnabled);
		txtNombreMarcador.setEnabled(isEnabled);
		lstBookmark.setEnabled(isEnabled);

	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getScpBookmark() {
		
		if (scpBookmark == null) {
			
			scpBookmark = new JScrollPane();
			scpBookmark.setViewportView(lstBookmark);
		}
		return scpBookmark;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10" //De la clase 
