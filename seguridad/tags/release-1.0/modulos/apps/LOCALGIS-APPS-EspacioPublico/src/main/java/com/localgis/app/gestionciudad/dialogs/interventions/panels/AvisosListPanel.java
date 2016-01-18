package com.localgis.app.gestionciudad.dialogs.interventions.panels;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicNetwork;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.localgis.app.gestionciudad.ConstantessLocalGISObraCivil;
import com.localgis.app.gestionciudad.images.IconLoader;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.dialogs.interventions.dialogs.AvisosEdicionDialog;
import com.localgis.app.gestionciudad.dialogs.interventions.tables.AvisosListTable;
import com.localgis.app.gestionciudad.dialogs.interventions.tables.models.AvisoSimpleTableModel;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.NotesInterventionsEditionTypes;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.dialogs.main.GeopistaEditorPanel;
import com.localgis.app.gestionciudad.utils.RouteNetworksUtils;
import com.localgis.app.gestionciudad.utils.TableSorted;
import com.localgis.app.gestionciudad.webservicesclient.wrapper.FromStubDataWrapperUtils;
import com.localgis.app.gestionciudad.webservicesclient.wrapper.WSInterventionsWrapper;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author javieraragon
 *
 */
public class AvisosListPanel extends JPanel{

	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 4358687154787788152L;

	private static int NUM_AVISOS_POR_TABLA = 20;
	private int pagina = 0;
	// Insets para cuadrar el boton de siguiente y anterior al tamño del icono de flecha
	private static Insets arrowButtonsInset = new Insets(2,2,2,2);

	private NotesInterventionsEditionTypes tipoEdicion = NotesInterventionsEditionTypes.EDIT;

	private JButton paginaSiguienteButton = null;
	private JButton paginaAnteriorButton = null;
	private JButton paginaSeleccionadaButton = null;
	private JComboBox seleccionarPaginaComboBox = null;

	private JButton eliminarAvisoButton = null;
	private JButton modificarAvisoButton = null;
	private JButton nuevoAvisoButton = null;

	private JPanel paginacionPanel = null;
	private JPanel edicionPanel = null;
	private JPanel botoneraPanel = null;
	private AvisosViewPanel datosAvisoPanel = null;

	private AvisosListTable avisosTable = null;
	private JScrollPane tableScrollPane = null;
	
	private JCheckBox filtrarListaPorGeometriaCheckBox = null;


	public AvisosListPanel(){
		super(new GridBagLayout());
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		this.initialize();
	}

	private void initialize(){
		this.setLayout(new GridBagLayout());

		this.setIgnoreRepaint(true);

		//		this.add(new ListOptionPanel(), 
		//				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
		//						GridBagConstraints.BOTH, 
		//						new Insets(0, 5, 0, 5), 0, 0));
		//		
		this.add(this.getDatosAvisoPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.BOTH, 
						new Insets(0, 5, 0, 5), 0, 0));

		this.add(this.getJScrollPaneListaAvisos(),
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 300));
		
		

		this.add(this.getBotoneraPanel(), 
				new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.BOTH, 
						new Insets(0, 5, 0, 5), 0, 0));
	}

	private JPanel getBotoneraPanel(){
		if (botoneraPanel == null){
			botoneraPanel = new JPanel(new GridBagLayout());

			botoneraPanel.add(this.getFiltrarListaPorGeometriaCheckBox(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			botoneraPanel.add(this.getPaginacionPanel(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			botoneraPanel.add(this.getEdicionPanel(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.BOTH, 
							new Insets(0, 5, 0, 5), 0, 0));

		}
		return botoneraPanel;
	}

	private JPanel getEdicionPanel(){
		if (edicionPanel == null){
			edicionPanel = new JPanel(new GridBagLayout());

			edicionPanel.add(this.getNuevoAvisoButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			edicionPanel.add(this.getModificarAvisoButton(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			edicionPanel.add(this.getEliminarAvisoButton(), 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

		}
		return edicionPanel;
	}

	private JPanel getPaginacionPanel(){
		if (paginacionPanel == null){
			paginacionPanel = new JPanel(new GridBagLayout());

			paginacionPanel.add(this.getPaginaAnteriorButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

			paginacionPanel.add(this.getSeleccionarPaginaComboBox(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 20, 0));

			paginacionPanel.add(this.getPaginaSeleccionadaButton(), 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));			
			
			paginacionPanel.add(this.getPaginaSiguienteButton(), 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
		}
		return paginacionPanel;
	}

	private JButton getPaginaSiguienteButton(){
		if (paginaSiguienteButton == null){
			paginaSiguienteButton = new JButton(IconLoader.icon(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.siguienteicon")));
			
			paginaSiguienteButton.setMargin(arrowButtonsInset);
			paginaSiguienteButton.setToolTipText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.siguientelabel"));
			
			paginaSiguienteButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onPaginaSiguienteButtonDo();
				}
			});
		}
		return paginaSiguienteButton;
	}

	private void onPaginaSiguienteButtonDo() {
		String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
		int idMunicipio = AppContext.getIdMunicipio();
		if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
			int numInterventions=0;
			try {
				numInterventions = WSInterventionsWrapper.getNumInterventionsByConditions(usuario, idMunicipio,getFiltrarFeaturesSelected());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (numInterventions > 0){
				int numPaginas = numInterventions / NUM_AVISOS_POR_TABLA;
				if (numPaginas > 0){
					if((this.pagina+1>0) && (this.pagina+1 <= numPaginas)){
						this.pagina = pagina+1;

						this.AnniadirSeleccionarPaginaComboBox(pagina+1);
						reloadInterventionList();						
					}
				}
			}
		}

	}


	private void AnniadirSeleccionarPaginaComboBox(int pagina) {
		if (this.getSeleccionarPaginaComboBox() != null && pagina>0){
			if (this.getSeleccionarPaginaComboBox().getItemCount() < pagina){
				this.getSeleccionarPaginaComboBox().addItem(pagina);
			}
			this.getSeleccionarPaginaComboBox().setSelectedItem(pagina);
		}


	}

	private JButton getPaginaAnteriorButton(){
		if (paginaAnteriorButton == null){
			paginaAnteriorButton = new JButton(
					IconLoader.icon(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.anteiroricon")));
			
			paginaAnteriorButton.setMargin(arrowButtonsInset);
			paginaAnteriorButton.setToolTipText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.anteirorlabel"));
			
			paginaAnteriorButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onPaginaAnteriorButtonDo();
				}
			});
		}
		return paginaAnteriorButton;
	}


	private void onPaginaAnteriorButtonDo() {
		String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
		int idMunicipio = AppContext.getIdMunicipio();
		if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
			if (pagina - 1 >= 0){
				this.pagina = pagina - 1;
				reloadInterventionList();
			}
		}
	}

	private JComboBox getSeleccionarPaginaComboBox(){
		if (seleccionarPaginaComboBox == null){
			seleccionarPaginaComboBox = new JComboBox();

			String userName = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
			int idMunicipio = AppContext.getIdMunicipio();
			if( (userName!=null && !userName.equals("")) && (idMunicipio >0) ){
				int numInterventions = 0;
				try {
					numInterventions = WSInterventionsWrapper.getNumInterventions(userName, idMunicipio);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (numInterventions >= 0){
					int numPaginas = numInterventions / (NUM_AVISOS_POR_TABLA + 1);
					if (numPaginas >= 0){
						for (int i=0; i <= numPaginas; i++){
							seleccionarPaginaComboBox.addItem(i + 1);
						}
					}
				}
			}



		}
		return seleccionarPaginaComboBox;
	}

	private JButton getPaginaSeleccionadaButton(){
		if (paginaSeleccionadaButton == null){
			paginaSeleccionadaButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.irlabel"));
			paginaSeleccionadaButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onPaginaSeleccionadaButtonDo();
				}
			});
		}
		return paginaSeleccionadaButton;
	}

	private void onPaginaSeleccionadaButtonDo() {
		if (this.getSeleccionarPaginaComboBox() != null){
			Object selectedItem = this.getSeleccionarPaginaComboBox().getSelectedItem();
			if (selectedItem != null && selectedItem instanceof Integer){
				String userName = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
				int idMunicipio = AppContext.getIdMunicipio();
				if ((userName!=null && !userName.equals("")) && idMunicipio>0 ){
					int paginaSeleccionada = (Integer) selectedItem - 1;
					int numPaginas = 0;
					try {
						numPaginas = WSInterventionsWrapper.getNumInterventionsByConditions(userName, idMunicipio, getFiltrarFeaturesSelected())/NUM_AVISOS_POR_TABLA;
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (paginaSeleccionada>=0 && paginaSeleccionada<=numPaginas ){
						this.pagina = paginaSeleccionada;
						reloadInterventionList();
					}
				}
			}
		}

	}

	private LayerFeatureBean[] getFiltrarFeaturesSelected() {
		ArrayList<LayerFeatureBean> layerFeaturesSelectedList = new ArrayList<LayerFeatureBean>();
		if (getFiltrarListaPorGeometriaCheckBox().isSelected()){
			GeopistaEditor geopistaEditor =  ((GeopistaEditorPanel) AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor")).getEditor();
			if (geopistaEditor != null){
				Iterator fearuesIt = geopistaEditor.getSelectionManager().getFeatureSelection().getFeaturesWithSelectedItems().iterator();
				while(fearuesIt.hasNext()){
					GeopistaFeature feature = (GeopistaFeature) fearuesIt.next();
					if (!feature.getLayer().isLocal()){
						try{
						LayerFeatureBean layerFeature = new LayerFeatureBean(
								feature.getLayer().getId_LayerDataBase(),
								Integer.parseInt(feature.getSystemId()) 
								);
						
						layerFeaturesSelectedList.add(layerFeature);
						}catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return layerFeaturesSelectedList.toArray(new LayerFeatureBean[layerFeaturesSelectedList.size()]);
	}

	private JButton getEliminarAvisoButton(){
		if (eliminarAvisoButton == null){
			eliminarAvisoButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listbuttons.delete"));
			eliminarAvisoButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onEliminarAvisoButtonDo();
				}
			});
			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				eliminarAvisoButton.setEnabled(false);
			}
		}
		return eliminarAvisoButton;
	}

	private void onEliminarAvisoButtonDo() {
		int row = this.getJTableListaAvisos().getSelectedRow();
		if (row >= 0){
			Object selectedItem = ((AvisoSimpleTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISIntervention){
				if (confirmarEliminar((LocalGISIntervention) selectedItem)){
					String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
					int idMunicipio = AppContext.getIdMunicipio();
					if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
						try {
							WSInterventionsWrapper.deleteIntervention((LocalGISIntervention)selectedItem, usuario, idMunicipio);
						} catch (Exception e) {
							e.printStackTrace();
						}

						reloadInterventionList();
						getJTableListaAvisos().updateUI();
					}
				}
			}
		}
	}

	private boolean confirmarEliminar(LocalGISIntervention aviso) {
		if (aviso != null){

			String mensaje = 
				I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.deletemessage")
				+ " (" + I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.idAction") + ": " + aviso.getId() + ")";

			int seleccion = JOptionPane.showOptionDialog(
					this,
					mensaje, 
					"",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,    // null para icono por defecto.
					new Object[]{
							I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.deleteconfirmdialog.accept"), 
							I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.deleteconfirmdialog.cancel")
					},   
					// null para YES, NO y CANCEL
					I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.deleteconfirmdialog.cancel"));

			if (seleccion == 0)
				return true;
		}

		return false;
	}

	private JButton getModificarAvisoButton(){
		if (modificarAvisoButton == null){
			modificarAvisoButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listbuttons.modify"));
			modificarAvisoButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onModificarAvisoButtonDo();
				}
			});
			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				modificarAvisoButton.setEnabled(false);
			}
		}
		return modificarAvisoButton;
	}

	private void onModificarAvisoButtonDo() {
		int row = this.getJTableListaAvisos().getSelectedRow();
		if (row >= 0){
			Object selectedItem = ((AvisoSimpleTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISIntervention){
				AvisosEdicionDialog dialog = AvisosEdicionDialog.createAvisosEdicionDialog(this,NotesInterventionsEditionTypes.EDIT,(LocalGISIntervention) selectedItem);
				if (dialog.wasOKPressed()){

					String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
					int idMunicipio = AppContext.getIdMunicipio();
					if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
						try {
							WSInterventionsWrapper.modifyIntervention(dialog.getAviso(),usuario,idMunicipio);
						} catch (Exception e) {
							e.printStackTrace();
						}
						reloadInterventionList();
						getJTableListaAvisos().updateUI();
						
						if (dialog.getIsIncidentToRoutesModified()){
							reloadNetwork();
						}
					}
				}
			}
		}
	}


	private void reloadNetwork() {
		if (confirmReloadNetwork()){
			final GeopistaEditor geopistaEditor =  ((GeopistaEditorPanel) AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor")).getEditor();
			final JFrame desktop= geopistaEditor.getMainFrame();
			final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
			progressDialog.setTitle("TaskMonitorDialog.Wait");
			progressDialog.addComponentListener(new ComponentAdapter()
			{

				public void componentShown(ComponentEvent e)
				{
					new Thread(new Runnable()
					{
						public void run()
						{
							try {
								
								progressDialog.report("Recargando red '" +  ConstantessLocalGISObraCivil.NOMBRE_RED + "'.");
								PlugInContext context = geopistaEditor.getContext().createPlugInContext();
								NetworkManager networkMgr = FuncionesAuxiliares.getNetworkManager(context);
								if (networkMgr.getNetwork(ConstantessLocalGISObraCivil.NOMBRE_RED) != null)
									networkMgr.eraseNetwork(ConstantessLocalGISObraCivil.NOMBRE_RED);

								RouteNetworksUtils.leerGrafodeBase(ConstantessLocalGISObraCivil.NOMBRE_RED, progressDialog, context);
								
								
							}catch (Exception e) {
								ErrorDialog.show(desktop, "Error al carger red Callejero", "", "");
							}finally{
								progressDialog.setVisible(false);
							}

						}
					}).start();
				}
			});
			
			GUIUtil.centreOnWindow(progressDialog);
			progressDialog.setVisible(true);
		}
	}

	private boolean confirmReloadNetwork() {

		String mensaje ="<html>¿Desea recargar la red para el Cálculo de Rutas?</html>";

		int seleccion = JOptionPane.showOptionDialog(
				this,
				mensaje, 
				"",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,    // null para icono por defecto.
				new Object[]{
						I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.deleteconfirmdialog.accept"), 
						I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.deleteconfirmdialog.cancel")
				},   
				// null para YES, NO y CANCEL
				I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.deleteconfirmdialog.cancel"));

		if (seleccion == 0)
			return true;

		return false;
	}

	private JButton getNuevoAvisoButton(){
		if (nuevoAvisoButton == null){
			nuevoAvisoButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listbuttons.new"));
			nuevoAvisoButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onNuevoAvisoButtonDo();
				}
			});

			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				nuevoAvisoButton.setEnabled(false);
			}
		}
		return nuevoAvisoButton;
	}

	private void onNuevoAvisoButtonDo() {
		GeopistaEditor editorGeopista = ((GeopistaEditorPanel) AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor")).getEditor();
		if (editorGeopista!=null && !editorGeopista.getSelectionManager().getSelectedItems().isEmpty()){
			AvisosEdicionDialog dialog = AvisosEdicionDialog.createAvisosEdicionDialog(this,NotesInterventionsEditionTypes.NEW,new LocalGISIntervention());
			if (dialog.wasOKPressed()){
				ArrayList<LayerFeatureBean> layersAndFeatures = new ArrayList<LayerFeatureBean>();
				Iterator it = editorGeopista.getSelectionManager().getFeatureSelection().getFeaturesWithSelectedItems().iterator();

				while (it.hasNext()){
					GeopistaFeature feature = (GeopistaFeature) it.next();
					layersAndFeatures.add(new LayerFeatureBean(feature.getLayer().getId_LayerDataBase(),UtilidadesAvisosPanels.getFeatureSystemId(feature)));
				}


				LocalGISIntervention nuevoAviso = dialog.getAviso();
				nuevoAviso.setFeatureRelation(layersAndFeatures.toArray(new LayerFeatureBean[layersAndFeatures.size()]));
				//				((AvisoSimpleTableModel)((TableSorted)(getJTableListaAvisos()).getModel()).getTableModel()).anniadirAviso(nuevoAviso);

				try {
					WSInterventionsWrapper.addIntervention(nuevoAviso, 
							AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true), 
							AppContext.getIdMunicipio());
				} catch (Exception e) {
					e.printStackTrace();
				}

				this.reloadInterventionList();
				getJTableListaAvisos().updateUI();
				
				if (dialog.getIsIncidentToRoutesModified()){
					reloadNetwork();
				}

			}
		}else{
			JOptionPane.showMessageDialog(this, "Error! Seleccione alguna feature del Editor.");
		}
	}
	
	public void reloadInterventionList() {
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
		//progressDialog.setTitle(I18N.get("Expedientes","fxcc.panel.CargandoFicheroDXF"));
		progressDialog.setTitle("TaskMonitorDialog.Wait");
		progressDialog.report(I18N.get("avisospanels","localgisgestionciudad.interfaces.notes.panel.reloadinginterventions"));
		progressDialog.addComponentListener(new ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{   
				new Thread(new Runnable()
				{
					public void run()
					{
						try {   
							LocalGISIntervention selectedIntervention = null;
							int row = getJTableListaAvisos().getSelectedRow();
							if (row >= 0){
								Object selectedItem =null;
								try{
									selectedItem = ((AvisoSimpleTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(row);
								}catch (Exception e) {
								}
								if (selectedItem!=null){
									try{
										selectedIntervention = (LocalGISIntervention) selectedItem;
									}catch (ClassCastException e) {
										selectedIntervention = null;
									}
								}
							}
							String userName = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
							int idMunicipio = AppContext.getIdMunicipio();
							if ((userName != null && !userName.equals("")) && idMunicipio > 0 ){
								
								avisosTable = null;
								
								
								// Obtenemos las features seleccionadas por si queremos filtrar por feature
								
								
								((AvisoSimpleTableModel)((TableSorted)(getJTableListaAvisos()).getModel()).getTableModel()).
								setData(
//										FromStubDataWrapperUtils.stubInterventionListToLocalGisInterventionArray(WSInterventionsWrapper.getRangeInterventions(
//												userName, 
//												AppContext.getIdMunicipio(),
//												pagina,
//												NUM_AVISOS_POR_TABLA)
										WSInterventionsWrapper.getRangeOrderInterventions(userName, 
												AppContext.getIdMunicipio(),
												pagina,
												NUM_AVISOS_POR_TABLA,
												getFiltrarFeaturesSelected()
										)
								);
								
								tableScrollPane.setViewportView(getJTableListaAvisos());
								
								try{
									if (selectedIntervention != null){
										Iterator<LocalGISIntervention> it = ((AvisoSimpleTableModel)((TableSorted)(getJTableListaAvisos()).getModel()).getTableModel()).getData().iterator();
										int i = 0;
										while(it.hasNext()){
											LocalGISIntervention intervention = it.next();
											if (intervention.getId() == selectedIntervention.getId()){
												break;
											}
											i ++;
										}
										getJTableListaAvisos().getSelectionModel().setSelectionInterval(i, i);
									}
								}catch (Exception e) {
									e.printStackTrace();
								}
								
								int numInterventions = WSInterventionsWrapper.getNumInterventionsByConditions(userName, 
										idMunicipio,
										getFiltrarFeaturesSelected());
								if (numInterventions/NUM_AVISOS_POR_TABLA > 0){
									int numPaginas = numInterventions/NUM_AVISOS_POR_TABLA;
									if (getSeleccionarPaginaComboBox().getItemCount()-1 < numPaginas){
										for (int i = getSeleccionarPaginaComboBox().getItemCount()-1; i < numPaginas; i ++){
											getSeleccionarPaginaComboBox().addItem(i+1+1);
										}
									}
								}	
							}
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						} 
						finally
						{
							progressDialog.setVisible(false);
						}
					}
				}).start();
			}
		});
		GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);
		
		getJScrollPaneListaAvisos().updateUI();
		getJTableListaAvisos().updateUI();
		
	}

	public JScrollPane getJScrollPaneListaAvisos(){
		if (tableScrollPane == null){
			tableScrollPane = new JScrollPane();
			tableScrollPane.setViewportView(getJTableListaAvisos());
			tableScrollPane.setSize(10,10);
		}
		return tableScrollPane;
	}

	public JTable getJTableListaAvisos()
	{
		if (avisosTable  == null)
		{		
			try {
				ArrayList<LocalGISIntervention> lista = 
						WSInterventionsWrapper.getRangeOrderInterventions(
								AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true), 
								AppContext.getIdMunicipio(),
								0,
								NUM_AVISOS_POR_TABLA,
								null );
				
				avisosTable = new AvisosListTable(lista);
			} catch (Exception e1) {
				e1.printStackTrace();
				avisosTable = new AvisosListTable(new ArrayList<LocalGISIntervention>());
			}
			avisosTable.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent evt) {
				}
				public void mouseClicked(MouseEvent e){
					if (e.getClickCount() == 2){
						onTableDoubleClickDo();
					}
					if (!getFiltrarListaPorGeometriaCheckBox().isSelected()){
						loadSelectedItem();
					}
				}
			});
			avisosTable.addKeyListener(new KeyListener(){
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER){
						onTableEnterKeyPressedDo();
					}
				}
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN ||
							e.getKeyCode() == KeyEvent.VK_ENTER){
						loadSelectedItem();
					}
				}
				@Override
				public void keyTyped(KeyEvent e) {
				}
			});
		}
		return avisosTable;
	}

	private void onTableEnterKeyPressedDo(){
		int row = this.getJTableListaAvisos().getSelectedRow();
		if (row >= 0){
			Object selectedItem = ((AvisoSimpleTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISIntervention){
				AvisosEdicionDialog dialog = AvisosEdicionDialog.createAvisosEdicionDialog(this, this.tipoEdicion, (LocalGISIntervention) selectedItem);
				
				if (dialog.wasOKPressed()){
					
					String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
					int idMunicipio = AppContext.getIdMunicipio();
					if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
						try {
							WSInterventionsWrapper.modifyIntervention(dialog.getAviso(),usuario,idMunicipio);
						} catch (Exception e) {
							e.printStackTrace();
						}
						reloadInterventionList();
						getJTableListaAvisos().updateUI();

						if (dialog.getIsIncidentToRoutesModified()){
							reloadNetwork();
						}
					}
				}
				
				dialog.dispose();
			}
			try{
				Object newItem = ((AvisoSimpleTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(row +1);

				if (newItem != null && newItem instanceof LocalGISIntervention){
					this.getDatosAvisoPanel().loadAviso((LocalGISIntervention)newItem);
				}    	
			}catch (IndexOutOfBoundsException e) {
				this.getJTableListaAvisos().getSelectionModel().setSelectionInterval(getJTableListaAvisos().getModel().getRowCount()-2, getJTableListaAvisos().getModel().getRowCount()-2);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	private void onTableDoubleClickDo() {
		int row = this.getJTableListaAvisos().getSelectedRow();
		if (row >= 0){
			Object selectedItem = ((AvisoSimpleTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISIntervention){
				AvisosEdicionDialog dialog = AvisosEdicionDialog.createAvisosEdicionDialog(this,NotesInterventionsEditionTypes.VIEW,(LocalGISIntervention) selectedItem);
				dialog.dispose();
			}
		}
	}

	private void loadSelectedItem(){

		int selectedRow = getJTableListaAvisos().getSelectedRow();
		Object selectedItem = null;
		try{
			selectedItem = ((AvisoSimpleTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(selectedRow);
		} catch (ArrayIndexOutOfBoundsException exception){
		}

		if (selectedItem != null && selectedItem instanceof LocalGISIntervention){
			this.getDatosAvisoPanel().loadAviso((LocalGISIntervention)selectedItem);
			if (getJTableListaAvisos().getSelectedRows().length > 0){
				ArrayList<LayerFeatureBean> layersAndFeatures = new ArrayList<LayerFeatureBean>();
				if (((LocalGISIntervention)selectedItem).getFeatureRelation() != null && 
						((LocalGISIntervention)selectedItem).getFeatureRelation().length > 0 ){
					for(int i = 0; i < ((LocalGISIntervention)selectedItem).getFeatureRelation().length; i++){
						layersAndFeatures.add(((LocalGISIntervention)selectedItem).getFeatureRelation()[i]);
					}
				}
				selectAndZoomToFeatureSelection(layersAndFeatures);
			}
		}    	
	}

	private AvisosViewPanel getDatosAvisoPanel() {
		if (datosAvisoPanel == null){
			datosAvisoPanel = new AvisosViewPanel(null);
		}
		return this.datosAvisoPanel;
	}

	public void selectAndZoomToFeatureSelection(ArrayList<LayerFeatureBean> LayersFeaturesIds) {
		try {
			GeopistaEditor geopistaEditor =  ((GeopistaEditorPanel) AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor")).getEditor();
			if (geopistaEditor != null){
				this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				geopistaEditor .getSelectionManager().clear();
				Iterator<GeopistaLayer> geopistaLayers = geopistaEditor.getLayerManager().getLayers().iterator();
				while(geopistaLayers.hasNext()){
					GeopistaLayer geopistaLayer = geopistaLayers.next();
					if (!(geopistaLayer instanceof DynamicLayer)){
						int idLayer = geopistaLayer.getId_LayerDataBase();
						Iterator<Feature> allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
						while (allFeatures.hasNext()) {
							Feature feature= allFeatures.next();
							for (int i=0; i<LayersFeaturesIds.size(); i++){
								if (idLayer ==LayersFeaturesIds.get(i).getIdLayer()){
									if (UtilidadesAvisosPanels.getFeatureSystemId((GeopistaFeature)feature) == LayersFeaturesIds.get(i).getIdFeature()){
										geopistaEditor.select(geopistaLayer, feature);
									}
								}
							}
			            }
					}else{
		            	selectDynamicFeatures(geopistaLayer, LayersFeaturesIds);
		            }
				}
				geopistaEditor.zoomToSelected();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	
	public JCheckBox getFiltrarListaPorGeometriaCheckBox(){
		if (filtrarListaPorGeometriaCheckBox == null){
			filtrarListaPorGeometriaCheckBox = new JCheckBox(
					I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.list.filtrar.checkbox"));
			
//			filtrarListaPorGeometriaCheckBox.addItemListener(new ItemListener() {
//				@Override public void itemStateChanged(ItemEvent e) {
//					if (e.getItem().equals(getFiltrarListaPorGeometriaCheckBox())){
//						pagina = 0;
//						reloadInterventionList();
//					}
//				}
//			});
			
			filtrarListaPorGeometriaCheckBox.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource().equals(getFiltrarListaPorGeometriaCheckBox())){
						pagina = 0;
						reloadInterventionList();
					}
				}
			});
		}
		return filtrarListaPorGeometriaCheckBox;
	}

	public void onEditorAcctionPerformedDo(){
		if ( getFiltrarListaPorGeometriaCheckBox()!=null &&
				getFiltrarListaPorGeometriaCheckBox().isSelected() ){
			pagina = 0;
			reloadInterventionList();
		}
	}
    /**
     * Método que selecciona features en capas dinámicas
     */
    private void selectDynamicFeatures(GeopistaLayer geopistaLayer, java.util.List<LayerFeatureBean> LayersFeaturesIds){
		int n = LayersFeaturesIds.size();
		List featuresIds = new ArrayList();
		for (int i=0; i<n; i++){
			if (LayersFeaturesIds.get(i).getIdLayer() == geopistaLayer.getId_LayerDataBase()){
				featuresIds.add(((Integer)LayersFeaturesIds.get(i).getIdFeature()).toString());
			}
		}
		if (featuresIds.size()>0 ){
			GeopistaEditor geopistaEditor =  ((GeopistaEditorPanel) AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor")).getEditor();
	        GeopistaConnection geopistaConnection = (GeopistaConnection) geopistaLayer.getDataSourceQuery().getDataSource().getConnection();
	    	CoordinateSystem inCoord = geopistaLayer.getLayerManager().getCoordinateSystem();
	        DriverProperties driverProperties = geopistaConnection.getDriverProperties();
	    	driverProperties.put("srid_destino",inCoord.getEPSGCode());
	    	java.util.List listaFeatures = geopistaConnection.loadFeatures(geopistaLayer,featuresIds.toArray());
	    	Iterator itFeatures = listaFeatures.iterator();
	    	while (itFeatures.hasNext()){
	    		GeopistaFeature geopistaFeature = (GeopistaFeature)itFeatures.next();
	    		if (geopistaFeature != null)
	    			geopistaEditor.select(geopistaLayer, (GeopistaFeature)itFeatures.next());
	    	}
		}
    }

}
