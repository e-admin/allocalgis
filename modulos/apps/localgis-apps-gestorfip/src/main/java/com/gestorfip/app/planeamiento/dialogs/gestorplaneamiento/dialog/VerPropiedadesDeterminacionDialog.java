/**
 * VerPropiedadesDeterminacionDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.dialog;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableTreeModel;
import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.diccionario.CaracteresDeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.RegulacionesEspecificasPanelBean;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.RegulacionesEspecificasPanelTree;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.TablaDetEnt;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.gestorfip.app.planeamiento.utils.GestorFipUtils;
import com.vividsolutions.jump.I18N;

public class VerPropiedadesDeterminacionDialog extends JDialog implements TreeSelectionListener{

	
	private static final long serialVersionUID = 1L;
	
	private AppContext appContext = (AppContext) AppContext.getApplicationContext();
	private ApplicationContext application = (AppContext)AppContext.getApplicationContext();

	public static final int DIM_X = 900;
	public static final int DIM_Y = 600;
	

	private JPanel visorPropDeterminacionPanel = null;
	private JPanel zonaDatPanel = null;
	private JPanel zonaDatGenPanel = null;
	private JPanel zonaValRGAPanel = null;
	private JPanel zonaValoresRefPanel = null;
	private JPanel zonaGrupAplicPanel = null;
	private JPanel zonaRegExpPanel = null;
	
	private JLabel labelNombre = null;
	private JLabel labelCaracter = null;
	private JLabel labelApartado = null;
	private JLabel labelEtiqueta = null;
	private JLabel labelUnidad = null;
	private JLabel labelBase = null;
	private JLabel labelNombreRegExp = null;
	
	 
	private JTextField textNombre = null;
	private JTextField textCaracter = null;
	private JTextField textApartado = null;
	private JTextField textEtiqueta = null;
	private JTextField textUnidad = null;
	private JTextField textBase = null;
	private JTextField textNombreRegExp  = null;
	
	private JTextArea regulacionTextoJTextArea;
	private JScrollPane pScroll;

	private TablaDetEnt tablaValoresRef;
	private TablaDetEnt tablaGrupoAplic;
	
	
	private JScrollPane treeJScrollPane = null;
	private RegulacionesEspecificasPanelTree regEspPanelTree;
	private JTree tree = null;
    private HideableTreeModel model = null;

	private DeterminacionPanelBean dpb;
	public VerPropiedadesDeterminacionDialog( DeterminacionPanelBean dpb, ApplicationContext application){
		
		super(application.getMainFrame());
		this.dpb = dpb;
		
	
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		GestorFipUtils.menuBarSetEnabled(false,this.application.getMainFrame());
		
	    inicializaElementos();
	    cargarDatos();
	    
	    int posx = (this.application.getMainFrame().size().width - this.getWidth())/2;
		int posy = (this.application.getMainFrame().size().height -this.getHeight())/2;
		this.setLocation(posx, posy);
		
	    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
	 }
	
	
	 
	 /**
		* Inicializa los elementos del panel.
		*/
		private void inicializaElementos()
		{
			
			this.setModal(true);
			this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			this.setSize(DIM_X, DIM_Y);
			this.setMinimumSize(new Dimension(DIM_X, DIM_Y));
			
			
			visorPropDeterminacionPanel = new JPanel();		
			visorPropDeterminacionPanel.setLayout(new GridBagLayout());
			
			visorPropDeterminacionPanel.add(getZonaDatPanel(), 
					new GridBagConstraints(0,0,1,1, 0.01, 1,GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(10,5,0,5),0,0));
			
			visorPropDeterminacionPanel.add(getZonaRegExpPanel(), 
					new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(10,5,0,5),0,0));

			
			this.add(visorPropDeterminacionPanel);
			this.setTitle(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propdeterminacion.title"));
			
			this.addWindowListener(new java.awt.event.WindowAdapter()
	         {
			     public void windowClosing(java.awt.event.WindowEvent e)
			     {
			         dispose();
			     }
			 });
						
		}
		
		public JPanel getZonaDatPanel() {
			if(zonaDatPanel == null){
				zonaDatPanel = new JPanel();
				zonaDatPanel.setLayout(new GridBagLayout());
			

				zonaDatPanel.add(getZonaDatGenPanel(), 
						new GridBagConstraints(0,0,1,1, 1, 0.01,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				
				zonaDatPanel.add(getZonaValRGAPanel(), 
						new GridBagConstraints(0,1,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.BOTH, new Insets(10,5,0,5),0,0));
				
				
			}
			return zonaDatPanel;
		}



		public void setZonaDatPanel(JPanel zonaDatPanel) {
			this.zonaDatPanel = zonaDatPanel;
		}

		
		public JPanel getZonaValRGAPanel() {
			if(zonaValRGAPanel == null){
				zonaValRGAPanel = new JPanel();
				zonaValRGAPanel.setLayout(new GridBagLayout());
				
				zonaValRGAPanel.add(getZonaValoresRefPanel(), 
				new GridBagConstraints(0,0,1,1, 0.01, 1,GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(10,5,0,5),0,0));
		
				zonaValRGAPanel.add(getTablaGrupoAplic(), 
				new GridBagConstraints(1,0,1,1, 0.01, 1,GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(10,5,0,5),0,0));
				
			}
			return zonaValRGAPanel;
		}



		public void setZonaValRGAPanel(JPanel zonaValRGAPanel) {
			this.zonaValRGAPanel = zonaValRGAPanel;
		}

		
		public JPanel getZonaDatGenPanel() {
			if(zonaDatGenPanel == null){
				zonaDatGenPanel = new JPanel();
				zonaDatGenPanel.setLayout(new GridBagLayout());
				
				labelNombre = new JLabel();
				labelNombre.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propdeterminacion.nombre"));
				
				labelCaracter = new JLabel();
				labelCaracter.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propdeterminacion.caracter"));
				
				labelApartado = new JLabel();
				labelApartado.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propdeterminacion.apartado"));
				
				labelEtiqueta = new JLabel();
				labelEtiqueta.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propdeterminacion.etiqueta"));
				
				labelUnidad = new JLabel();
				labelUnidad.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propdeterminacion.unidad"));
				
				labelBase = new JLabel();
				labelBase.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propdeterminacion.base"));
				
		
				zonaDatGenPanel.add(labelNombre, 
						new GridBagConstraints(0,0,1,1, 0.01, 0.5,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				zonaDatGenPanel.add(getTextNombre(), 
						new GridBagConstraints(1,0,1,1, 1, 0.5,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				
				zonaDatGenPanel.add(labelCaracter, 
						new GridBagConstraints(0,1,1,1, 0.01, 0.5,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				zonaDatGenPanel.add(getTextCaracter(), 
						new GridBagConstraints(1,1,1,1, 1, 0.5,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				
				zonaDatGenPanel.add(labelApartado, 
						new GridBagConstraints(0,2,1,1, 0.01, 0.5,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				zonaDatGenPanel.add(getTextApartado(), 
						new GridBagConstraints(1,2,1,1, 1, 0.5,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
					
				zonaDatGenPanel.add(labelEtiqueta, 
						new GridBagConstraints(0,3,1,1, 0.01, 0.5,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				zonaDatGenPanel.add(getTextEtiqueta(), 
						new GridBagConstraints(1,3,1,1, 1, 0.5,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				
				zonaDatGenPanel.add(labelUnidad, 
						new GridBagConstraints(0,4,1,1, 0.01, 0.5,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				zonaDatGenPanel.add(getTextUnidad(), 
						new GridBagConstraints(1,4,1,1, 1, 0.5,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				
				zonaDatGenPanel.add(labelBase, 
						new GridBagConstraints(0,5,1,1, 0.01, 0.5,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				zonaDatGenPanel.add(getTextBase(), 
						new GridBagConstraints(1,5,1,1, 1, 0.5,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));	
				
			}
			return zonaDatGenPanel;
		}

		public JPanel getZonaValoresRefPanel() {
			if(zonaValoresRefPanel == null){
				zonaValoresRefPanel = new JPanel();
				zonaValoresRefPanel.setLayout(new GridBagLayout());
				
				zonaValoresRefPanel.add(getTablaValoresRef(), 
						new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
				
				
			}
			return zonaValoresRefPanel;
		}

		public JPanel getZonaGrupAplicPanel() {
			if(zonaGrupAplicPanel == null){
				zonaGrupAplicPanel = new JPanel();
				zonaGrupAplicPanel.setLayout(new GridBagLayout());
			
				zonaGrupAplicPanel.add(getTablaGrupoAplic(), 
						new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
			}
			return zonaGrupAplicPanel;
		}



		public JPanel getZonaRegExpPanel() {
			if(zonaRegExpPanel == null){
				zonaRegExpPanel = new JPanel();
				zonaRegExpPanel.setLayout(new GridBagLayout());
				zonaRegExpPanel.setBorder(new TitledBorder(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propdeterminacion.regesp")));
				
				labelNombreRegExp = new JLabel();
				labelNombreRegExp.setText(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades","gestorFip.planeamiento.propdeterminacion.regespnombre"));
				
				zonaRegExpPanel.add(getTreeJScrollPane(), 
						new GridBagConstraints(0,0,2,1, 1, 0.01,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				
				zonaRegExpPanel.add(labelNombreRegExp, 
						new GridBagConstraints(0,1,1,1, 0.01, 0.01,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
				
				zonaRegExpPanel.add(getTextNombreRegExp(), 
						new GridBagConstraints(1,1,1,1, 1, 0.01,GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));	
				
				zonaRegExpPanel.add(getpScroll(), 
						new GridBagConstraints(0,2,2,1, 1, 1,GridBagConstraints.NORTH,
							GridBagConstraints.BOTH, new Insets(10,5,0,5),0,0));
	
			}
			return zonaRegExpPanel;
		}
		
		
		public JScrollPane getpScroll() {
			if(pScroll == null){
				pScroll =new JScrollPane(getRegulacionTextoJTextArea(), 
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				
			     
			}
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        	   public void run() { 
	        		   JScrollBar scrollbar = pScroll.getVerticalScrollBar();
	        		   int min = scrollbar.getMinimum();
	        		   scrollbar.setValue(min);
	        		   //pScroll.getVerticalScrollBar().setValue(0);
	        	   }
	        	});
				
			pScroll.getViewport().setViewPosition(new java.awt.Point(0, 0));
			return pScroll;
		}
		

		
		private void cargarDatos(){
			
			getTextNombre().setText(dpb.getNombre());
			
			CaracteresDeterminacionPanelBean[] lstCaracteresDeterminacionPanelBean = 
					(CaracteresDeterminacionPanelBean[]) application.getBlackboard().get(ConstantesGestorFIP.LISTA_TIPOS_CARACTER_DETERMINACION);
			String nombreCaracter = "";
			for (int i = 0; i < lstCaracteresDeterminacionPanelBean.length; i++) {
				if(lstCaracteresDeterminacionPanelBean[i].getId() == dpb.getCaracter()){
					nombreCaracter = lstCaracteresDeterminacionPanelBean[i].getNombre();
				
				}
			}
			getTextCaracter().setText(nombreCaracter);
			
			getTextApartado().setText(dpb.getApartado());
			getTextEtiqueta().setText(dpb.getEtiqueta());
			
			if (dpb.getBaseBean() != null && dpb.getBaseBean().getDeterminacion() != null){
				getTextBase().setText(dpb.getBaseBean().getDeterminacion().getNombre());
			}
			else{
				getTextBase().setText("");
			}
			
			if (dpb.getUnidadBean() != null && dpb.getUnidadBean().getDeterminacion() != null){
				getTextUnidad().setText(dpb.getUnidadBean().getDeterminacion().getNombre());
			}
			else{
				getTextUnidad().setText("");
			}
			
			//Valores de referencia
			if(dpb.getValoresReferenciaPanelBean() != null && dpb.getValoresReferenciaPanelBean().getLstDeterminacionesAsoc() != null &&
					dpb.getValoresReferenciaPanelBean().getLstDeterminacionesAsoc().length != 0){
				DeterminacionPanelBean[] lstDet = dpb.getValoresReferenciaPanelBean().getLstDeterminacionesAsoc();
			
				getTablaValoresRef().cargaDatosTablaDet(lstDet);
			
			}
			else{
				getTablaValoresRef().vaciarTabla();
			}
			
			//Grupos de aplicacion
			if(dpb.getGrupoAplicacionPanelBean() != null && dpb.getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc() != null &&
					dpb.getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc().length != 0){
				DeterminacionPanelBean[] lstDet = dpb.getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc();
			
				getTablaGrupoAplic().cargaDatosTablaDet(lstDet);
			
			}
			else{
				getTablaGrupoAplic().vaciarTabla();
			}
			
			//Regulaciones especificas
			if(dpb.getRegulacionEspecificaPanelBean() != null && dpb.getRegulacionEspecificaPanelBean().length != 0){
				
				cargarArbolNodoSeleccionado(dpb.getRegulacionEspecificaPanelBean());
			}
			
			
			SwingUtilities.invokeLater(new Runnable() {
				   public void run() {
				         Point p = new Point(
				            0,
				            0
				         );
				         pScroll.getViewport().setViewPosition(p);
				      
				   }
				});

		}
	
		public JTextField getTextNombre() {
			if(textNombre == null){
				textNombre = new JTextField();
				
				textNombre.setEditable(false);
				textNombre.setEnabled(true);
			}
			return textNombre;
		}

		public void setTextNombre(JTextField textNombre) {
			this.textNombre = textNombre;
		}

		public JTextField getTextEtiqueta() {
			if(textEtiqueta == null){
				textEtiqueta = new JTextField();
				
				textEtiqueta.setEditable(false);
				textEtiqueta.setEnabled(true);
			}
			return textEtiqueta;
		}

		public void setTextEtiqueta(JTextField textEtiqueta) {
			this.textEtiqueta = textEtiqueta;
		}

		public JTextField getTextBase() {
			if(textBase == null){
				textBase = new JTextField();
				
				textBase.setEditable(false);
				textBase.setEnabled(true);
			}
			return textBase;
		}

		public void setTextBase(JTextField textBase) {
			this.textBase = textBase;
		}
		
		public JTextField getTextCaracter() {
			if(textCaracter == null){
				textCaracter = new JTextField();
				
				textCaracter.setEditable(false);
				textCaracter.setEnabled(true);
			}
			return textCaracter;
		}



		public void setTextCaracter(JTextField textCaracter) {
			this.textCaracter = textCaracter;
		}

		public JTextField getTextApartado() {
			if(textApartado == null){
				textApartado = new JTextField();
				
				textApartado.setEditable(false);
				textApartado.setEnabled(true);
			}
			return textApartado;
		}

		public void setTextApartado(JTextField textApartado) {
			this.textApartado = textApartado;
		}

		public JTextField getTextUnidad() {
			if(textUnidad == null){
				textUnidad = new JTextField();
				
				textUnidad.setEditable(false);
				textUnidad.setEnabled(true);
			}
			return textUnidad;
		}

		public void setTextUnidad(JTextField textUnidad) {
			this.textUnidad = textUnidad;
		}	
	
		public JTextField getTextNombreRegExp() {
			if(textNombreRegExp == null){
				textNombreRegExp = new JTextField();
				
				textNombreRegExp.setEditable(false);
				textNombreRegExp.setEnabled(true);
			}
			return textNombreRegExp;
		}



		public void setTextNombreRegExp(JTextField textNombreRegExp) {
			this.textNombreRegExp = textNombreRegExp;
		}

		
		public TablaDetEnt getTablaValoresRef() {
			if(tablaValoresRef == null){
				tablaValoresRef = new TablaDetEnt(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
													"gestorFip.planeamiento.propdeterminacion.valref"), 
												I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
													"gestorFip.planeamiento.propdeterminacion.determinacion.nombre"),
													getLstValoresRef(), 180, 300);
				
				
			}
			return tablaValoresRef;
		}
		
		public TablaDetEnt getTablaGrupoAplic() {
			if(tablaGrupoAplic == null){
				
				tablaGrupoAplic = new TablaDetEnt(I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
													"gestorFip.planeamiento.propdeterminacion.grupapli"), 
												I18N.get("LocalGISGestorFipAsociacionDeterminacionesEntidades",
													"gestorFip.planeamiento.propdeterminacion.determinacion.nombre"),
													getLstGrupApi(), 180, 300);
				
				
			}
			return tablaGrupoAplic;
		}

		
		
		private DeterminacionPanelBean[] getLstValoresRef(){
			DeterminacionPanelBean[] lstDet = null;
			if(dpb.getGrupoAplicacionPanelBean() != null){
				 lstDet = dpb.getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc();
			}
			return lstDet;
		}
		

		private DeterminacionPanelBean[] getLstGrupApi(){
			DeterminacionPanelBean[] lstDet = null;
			if(dpb.getGrupoAplicacionPanelBean() != null){
				 lstDet = dpb.getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc();
			}
			return lstDet;
		}
		
		public JTextArea getRegulacionTextoJTextArea() {
			if(regulacionTextoJTextArea == null){
				regulacionTextoJTextArea = new JTextArea(4, 20);
				
				regulacionTextoJTextArea.setLineWrap(true);
				regulacionTextoJTextArea.setEditable(false);
				regulacionTextoJTextArea.setSelectionStart(0);
				regulacionTextoJTextArea.setSelectionEnd(0);
				regulacionTextoJTextArea.setCaretPosition(0);
			}
			return regulacionTextoJTextArea;
		}

		public void setRegulacionTextoJTextArea(JTextArea regulacionTextoJTextArea) {
			this.regulacionTextoJTextArea = regulacionTextoJTextArea;
		}
		
		
		public JScrollPane getTreeJScrollPane() {
			if (treeJScrollPane == null) {
				treeJScrollPane = new JScrollPane();
				treeJScrollPane.setViewportView(getRegEspPanelTree());
				
			}
			return treeJScrollPane;
		}
		
		public void cargarArbolNodoSeleccionado(RegulacionesEspecificasPanelBean[] lstRegEspecifPanelBean){
			
			getTreeJScrollPane().getViewport().removeAll();
		
			regEspPanelTree = new RegulacionesEspecificasPanelTree(
					TreeSelectionModel.SINGLE_TREE_SELECTION,  lstRegEspecifPanelBean);
			regEspPanelTree.setPreferredSize(new Dimension(200,200));

			tree = regEspPanelTree.getTree();
			model = (HideableTreeModel) tree.getModel();
			
			treeJScrollPane.setViewportView(regEspPanelTree);
			// Listener de los cambios en el arbol
			regEspPanelTree.getTree().addTreeSelectionListener(this);
			
			tree.setSelectionRow(1);
			
		}

		public void setTreeJScrollPane(JScrollPane jScrollPane) {
			this.treeJScrollPane = jScrollPane;
		}
		
		
		public RegulacionesEspecificasPanelTree getRegEspPanelTree() {
			if(regEspPanelTree == null){
				regEspPanelTree = new RegulacionesEspecificasPanelTree(
						TreeSelectionModel.SINGLE_TREE_SELECTION,  new RegulacionesEspecificasPanelBean[1]);
				
				regEspPanelTree.setPreferredSize(new Dimension(200,200));

			}
			return regEspPanelTree;
		}
		
		public void setRegEspPanelTree(
					RegulacionesEspecificasPanelTree regEspPanelTree) {
			 this.regEspPanelTree = regEspPanelTree;
		}



	
		public void valueChanged(TreeSelectionEvent e) {
			// TODO Auto-generated method stub
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

			if (node != null) {
				
				Object nodeInfo = node.getUserObject();	
				if(nodeInfo instanceof RegulacionesEspecificasPanelBean){
					cargarDatosInformacionRegulacionEspecifica((RegulacionesEspecificasPanelBean)nodeInfo);
				}
			}
		}
		
		private void cargarDatosInformacionRegulacionEspecifica(RegulacionesEspecificasPanelBean regEspecifPanelBean){
			
			getTextNombreRegExp().setText(regEspecifPanelBean.getNombre());
			getRegulacionTextoJTextArea().setText(regEspecifPanelBean.getTexto());

		}

}
