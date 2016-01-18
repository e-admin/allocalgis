package com.localgis.app.gestionciudad.dialogs.printsearch.panels;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.reports.GenerarInformeExterno;
import com.geopista.app.utilidades.CUtilidadesComponentes;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.GeopistaPrintPlugIn;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.dialogs.interventions.dialogs.AvisosEdicionDialog;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.NotesInterventionsEditionTypes;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.dialogs.main.PrintEditorPanel;
import com.localgis.app.gestionciudad.dialogs.notes.dialogs.NotesEdicionDialog;
import com.localgis.app.gestionciudad.dialogs.notes.tables.NotesListTable;
import com.localgis.app.gestionciudad.dialogs.notes.tables.models.NoteSimpleTableModel;
import com.localgis.app.gestionciudad.dialogs.printsearch.dialogs.SearchPrintDialog;
import com.localgis.app.gestionciudad.images.IconLoader;
import com.localgis.app.gestionciudad.utils.GeopistaUtilLicenciaActividad;
import com.localgis.app.gestionciudad.utils.TableSorted;
import com.localgis.app.gestionciudad.webservicesclient.wrapper.WSInterventionsWrapper;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.FinderInterventionConditions;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.FinderNoteConditions;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;

/**
 * @author javieraragon
 *
 */
public class SearchListPanel extends JPanel{

	/**
	 */
	private static final long serialVersionUID = 4358687154787788152L;

	private static int NUM_NOTAS_POR_TABLA = 20;
	private int pagina = 0;
	// Insets para cuadrar el boton de siguiente y anterior al tamño del icono de flecha
	private static Insets arrowButtonsInset = new Insets(2,2,2,2);

	private NotesInterventionsEditionTypes tipoEdicion = NotesInterventionsEditionTypes.EDIT;

	private JButton paginaSiguienteButton = null;
	private JButton paginaAnteriorButton = null;
	private JButton paginaSeleccionadaButton = null;
	private JComboBox seleccionarPaginaComboBox = null;
	
	private JButton buscarButton = null;
	private SearchPrintDialog searchOptionsDialog = null;
	
	private JButton imprimirMapaButton = null;
	private JButton imprimirInformesActuaciones = null;

	private JButton eliminarNotaButton = null;
//	private JButton modificarAvisoButton = null;
//	private JButton nuevaNotaButton = null;

//	private JPanel paginacionPanel = null;
	private JPanel edicionPanel = null;
	private JPanel botoneraPanel = null;
	private NotesInterventionsSearchViewPanel datosNotasPanel = null;

	private NotesListTable notasTable = null;
	private JScrollPane tableScrollPane = null;
	
	 private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();


	public SearchListPanel(){
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
		this.add(this.getDatosNotesPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		this.add(this.getJScrollPaneListaNotas(),
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.BOTH, 
						new Insets(0, 5, 0, 5), 0, 100));

		this.add(this.getBotoneraPanel(), 
				new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
	}

	private JPanel getBotoneraPanel(){
		if (botoneraPanel == null){
			botoneraPanel = new JPanel(new GridBagLayout());

//			botoneraPanel.add(this.getPaginacionPanel(), 
//					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
//							GridBagConstraints.HORIZONTAL, 
//							new Insets(0, 5, 0, 5), 0, 0));

			botoneraPanel.add(this.getEdicionPanel(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

		}
		return botoneraPanel;
	}

	private JPanel getEdicionPanel(){
		if (edicionPanel == null){
			edicionPanel = new JPanel(new GridBagLayout());

			edicionPanel.add(this.getBuscarJButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(10, 5, 0, 5), 0, 0));

			edicionPanel.add(this.getEliminarNotaButton(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(10, 5, 0, 5), 0, 0));
			
			edicionPanel.add(this.getImprimirButton(), 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(10, 5, 0, 5), 0, 0));
			
			edicionPanel.add(this.getImprimirInformesActuaciones(), 
					new GridBagConstraints(0, 1, 3, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE , 
							new Insets(10, 5, 0, 5), 0, 0));

		}
		return edicionPanel;
	}

//	private JPanel getPaginacionPanel(){
//		if (paginacionPanel == null){
//			paginacionPanel = new JPanel(new GridBagLayout());
//
//			paginacionPanel.add(this.getPaginaAnteriorButton(), 
//					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
//							GridBagConstraints.NONE, 
//							new Insets(0, 5, 0, 5), 0, 0));
//
//			paginacionPanel.add(this.getSeleccionarPaginaComboBox(), 
//					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
//							GridBagConstraints.HORIZONTAL, 
//							new Insets(0, 5, 0, 5), 0, 0));
//
//			paginacionPanel.add(this.getPaginaSeleccionadaButton(), 
//					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
//							GridBagConstraints.HORIZONTAL, 
//							new Insets(0, 5, 0, 5), 0, 0));	
//
//			paginacionPanel.add(this.getPaginaSiguienteButton(), 
//					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
//							GridBagConstraints.NONE, 
//							new Insets(0, 5, 0, 5), 0, 0));
//		}
//		return paginacionPanel;
//	}

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
			int numNotes = 0;
			try {
				numNotes = WSInterventionsWrapper.getNumNotes(usuario, idMunicipio);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (numNotes > 0){
				int numPaginas = numNotes / NUM_NOTAS_POR_TABLA;
				if (numPaginas > 0){
					if((this.pagina+1>0) && (this.pagina+1 <= numPaginas)){
						this.pagina = pagina+1;

						this.AnniadirSeleccionarPaginaComboBox(pagina);
//						reloadNoteList();						
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
//				reloadNoteList();
			}
		}
	}

	private JComboBox getSeleccionarPaginaComboBox(){
		if (seleccionarPaginaComboBox == null){
			seleccionarPaginaComboBox = new JComboBox();

			String userName = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
			int idMunicipio = AppContext.getIdMunicipio();
			if( (userName!=null && !userName.equals("")) && (idMunicipio >0) ){
				int numNotes = 0;
				try {
					numNotes = WSInterventionsWrapper.getNumNotes(userName, idMunicipio);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (numNotes >= 0){
					int numPaginas = numNotes / NUM_NOTAS_POR_TABLA;
					if (numPaginas >= 0){
						for (int i=0; i <= numPaginas; i++){
							seleccionarPaginaComboBox.addItem(i);
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
					int paginaSeleccionada = (Integer) selectedItem;
					int numPaginas = 0;
					try {
						numPaginas = WSInterventionsWrapper.getNumNotes(userName, idMunicipio)/NUM_NOTAS_POR_TABLA;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (paginaSeleccionada>=0 && paginaSeleccionada<=numPaginas ){
						this.pagina = paginaSeleccionada;
//						reloadNoteList();
					}
				}
			}
		}
	}

	private JButton getEliminarNotaButton(){
		if (eliminarNotaButton == null){
			eliminarNotaButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listbuttons.delete"));
			eliminarNotaButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onEliminarNotaButtonDo();
				}
			});
			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				eliminarNotaButton.setEnabled(false);
			}
		}
		return eliminarNotaButton;
	}

	private void onEliminarNotaButtonDo() {
		int row = this.getJTableListaNotas().getSelectedRow();
		if (row >= 0){
			Object selectedItem = ((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISNote){
				if (confirmarEliminar((LocalGISNote) selectedItem)){
//					String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
//					int idMunicipio = AppContext.getIdMunicipio();
//					if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
//						try {
//							WSInterventionsWrapper.deleteNote((LocalGISNote)selectedItem, usuario, idMunicipio);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//						reloadNoteList();
//						getJTableListaNotas().updateUI();
//					}
					((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).getData().remove(selectedItem);
					getJTableListaNotas().updateUI();
					getJScrollPaneListaNotas().updateUI();
					refreshFeatureSelection();
				}
			}
		}
	}

	private boolean confirmarEliminar(LocalGISNote note) {
		if (note != null){

			String mensaje = 
				I18N.get("avisospanels","localgisgestionciudad.interfaces.notes.list.deletemessage")
				+ " (" + I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.idNote") + ": " + note.getId() + ")";

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

//	private JButton getModificarAvisoButton(){
//		if (modificarAvisoButton == null){
//			modificarAvisoButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listbuttons.modify"));
//			modificarAvisoButton.addActionListener(new ActionListener(){
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					onModificarNotaButtonDo();
//				}
//			});
//			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
//				modificarAvisoButton.setEnabled(false);
//			}
//		}
//		return modificarAvisoButton;
//	}

//	private void onModificarNotaButtonDo() {
//		int row = this.getJTableListaNotas().getSelectedRow();
//		if (row >= 0){
//			Object selectedItem = ((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).getValueAt(row);
//			if (selectedItem!=null && selectedItem instanceof LocalGISNote){
//				NotesEdicionDialog dialog = NotesEdicionDialog.createNotesEdicionDialog(this,NotesInterventionsEditionTypes.EDIT,(LocalGISNote) selectedItem);
//				if (dialog.wasOKPressed()){
//
//					String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
//					int idMunicipio = AppContext.getIdMunicipio();
//					if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
//						try {
//							WSInterventionsWrapper.modifyNote(dialog.getNote(),usuario,idMunicipio);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						reloadNoteList();
//						getJTableListaNotas().updateUI();
//					}
//
//				}
//			}
//		}
//	}

//	private JButton getNuevNotaButton(){
//		if (nuevaNotaButton == null){
//			nuevaNotaButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listbuttons.new"));
//			nuevaNotaButton.addActionListener(new ActionListener(){
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					onNuevaNotaButtonDo();
//				}
//			});
//
//			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
//				nuevaNotaButton.setEnabled(false);
//			}
//		}
//		return nuevaNotaButton;
//	}
//
//	private void onNuevaNotaButtonDo() {
//		GeopistaEditor editorGeopista = ((GeopistaEditorPanel) AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor")).getEditor();
//		if (editorGeopista!=null && !editorGeopista.getSelectionManager().getSelectedItems().isEmpty()){
//			NotesEdicionDialog dialog = NotesEdicionDialog.createNotesEdicionDialog(this,NotesInterventionsEditionTypes.EDIT,new LocalGISNote());
//			if (dialog.wasOKPressed()){
//				ArrayList<LayerFeatureBean> layersAndFeatures = new ArrayList<LayerFeatureBean>();
//				Iterator it = editorGeopista.getSelectionManager().getFeatureSelection().getFeaturesWithSelectedItems().iterator();
//
//				while (it.hasNext()){
//					GeopistaFeature feature = (GeopistaFeature) it.next();
//					layersAndFeatures.add(new LayerFeatureBean(feature.getLayer().getId_LayerDataBase(),feature.getID()));
//				}
//
//
//				LocalGISNote nuevaNota = dialog.getNote();
//				nuevaNota.setFeatureRelation(layersAndFeatures.toArray(new LayerFeatureBean[layersAndFeatures.size()]));
//
//				try {
//					WSInterventionsWrapper.addNote(nuevaNota, 
//							AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true), 
//							AppContext.getIdMunicipio());
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//				this.reloadNoteList();
//				this.updateUI();
//
//			}
//		}else{
//			JOptionPane.showMessageDialog(this, "Error! Seleccione alguna feature del Editor.");
//		}
//	}

//	private void reloadNoteList() {
//		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
//		//progressDialog.setTitle(I18N.get("Expedientes","fxcc.panel.CargandoFicheroDXF"));
//		progressDialog.setTitle("TaskMonitorDialog.Wait");
//		progressDialog.report(I18N.get("avisospanels","localgisgestionciudad.interfaces.notes.panel.reloadingnotes"));
//		progressDialog.addComponentListener(new ComponentAdapter()
//		{
//			public void componentShown(ComponentEvent e)
//			{   
//				new Thread(new Runnable()
//				{
//					public void run()
//					{
//						try {   
//							GUIUtil.centreOnWindow(progressDialog);
//							progressDialog.setVisible(true);
//							String userName = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
//							int idMunicipio = AppContext.getIdMunicipio();
//							if ((userName != null && !userName.equals("")) && idMunicipio > 0 ){
//								((NoteSimpleTableModel)((TableSorted)(getJTableListaNotas()).getModel()).getTableModel()).
//								setData(
//										WSInterventionsWrapper.getRangeNoteList(
//												userName, 
//												AppContext.getIdMunicipio(),
//												pagina,
//												NUM_NOTAS_POR_TABLA)
//								);
//
//								int numNotes = WSInterventionsWrapper.getNumNotes(userName, idMunicipio);
//								if (numNotes/NUM_NOTAS_POR_TABLA > 0){
//									int numPaginas = numNotes/NUM_NOTAS_POR_TABLA;
//									if (getSeleccionarPaginaComboBox().getItemCount()-1 < numPaginas){
//										for (int i = getSeleccionarPaginaComboBox().getItemCount()-1; i < numPaginas; i ++){
//											getSeleccionarPaginaComboBox().addItem(i+1);
//										}
//									}
//								}	
//							}
//						} 
//						catch (Exception e)
//						{
//							e.printStackTrace();
//						} 
//						finally
//						{
//							progressDialog.setVisible(false);
//						}
//					}
//				}).start();
//			}
//		});
//		GUIUtil.centreOnWindow(progressDialog);
//		progressDialog.setVisible(true);
//
//	}

	public JScrollPane getJScrollPaneListaNotas(){
		if (tableScrollPane == null){
			tableScrollPane = new JScrollPane();
			tableScrollPane.setViewportView(getJTableListaNotas());
			tableScrollPane.setSize(10,10);
		}
		return tableScrollPane;
	}

	public NotesListTable getJTableListaNotas()
	{
		if (notasTable  == null)
		{		
			notasTable = new NotesListTable();
			notasTable.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent evt) {

				}
				public void mouseClicked(MouseEvent e){
					if (e.getClickCount() == 2){
						onTableDoubleClickDo();
					}
					loadSelectedItem();
				}
			});
			notasTable.addKeyListener(new KeyListener(){
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
		return notasTable;
	}

	private void onTableEnterKeyPressedDo(){
		int row = this.getJTableListaNotas().getSelectedRow();
		if (row >= 0){
			Object selectedItem = ((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISIntervention){
				AvisosEdicionDialog dialog = AvisosEdicionDialog.createAvisosEdicionDialog(this, NotesInterventionsEditionTypes.VIEW, (LocalGISIntervention) selectedItem);
				dialog.dispose();
			} else if (selectedItem!=null && selectedItem instanceof LocalGISNote){
				NotesEdicionDialog dialog = NotesEdicionDialog.createNotesEdicionDialog(this, NotesInterventionsEditionTypes.VIEW, (LocalGISNote) selectedItem);		        
				dialog.dispose();
			}
			try{
				Object newItem = ((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).getValueAt(row +1);

				if (newItem != null && newItem instanceof LocalGISNote){
					this.getDatosNotesPanel().loadNote((LocalGISNote)newItem);
				}    	
			}catch (IndexOutOfBoundsException e) {
				this.getJTableListaNotas().getSelectionModel().setSelectionInterval(getJTableListaNotas().getModel().getRowCount()-2, getJTableListaNotas().getModel().getRowCount()-2);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	private void onTableDoubleClickDo() {
		int row = this.getJTableListaNotas().getSelectedRow();
		if (row >= 0){
			Object selectedItem = ((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISIntervention){
				AvisosEdicionDialog dialog = AvisosEdicionDialog.createAvisosEdicionDialog(this, NotesInterventionsEditionTypes.VIEW, (LocalGISIntervention) selectedItem);
				dialog.dispose();
			} else if (selectedItem!=null && selectedItem instanceof LocalGISNote){
				NotesEdicionDialog dialog = NotesEdicionDialog.createNotesEdicionDialog(this,NotesInterventionsEditionTypes.VIEW,(LocalGISNote) selectedItem);
				dialog.dispose();
			}
		}
	}

	private void loadSelectedItem(){

		int selectedRow = getJTableListaNotas().getSelectedRow();
		Object selectedItem = null;
		try{
			selectedItem = ((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).getValueAt(selectedRow);
		} catch (ArrayIndexOutOfBoundsException exception){
		}

		if (selectedItem != null && selectedItem instanceof LocalGISNote){
			this.getDatosNotesPanel().loadNote((LocalGISNote)selectedItem);
						if (getJTableListaNotas().getSelectedRows().length > 0){
				ArrayList<LayerFeatureBean> layersAndFeatures = new ArrayList<LayerFeatureBean>();
				if (((LocalGISNote)selectedItem).getFeatureRelation() != null && 
						((LocalGISNote)selectedItem).getFeatureRelation().length > 0 ){
					for(int i = 0; i < ((LocalGISNote)selectedItem).getFeatureRelation().length; i++){
						layersAndFeatures.add(((LocalGISNote)selectedItem).getFeatureRelation()[i]);
					}
				}
			}
		}    	
		
		if (selectedItem!= null && selectedItem instanceof LocalGISIntervention){
			this.getImprimirInformesActuaciones().setEnabled(true);
		} else {
			this.getImprimirInformesActuaciones().setEnabled(false);
		}
	}

	private NotesInterventionsSearchViewPanel getDatosNotesPanel() {
		if (datosNotasPanel == null){
			datosNotasPanel = new NotesInterventionsSearchViewPanel(null);
		}
		return this.datosNotasPanel;
	}
	
	
	
	

	
	@SuppressWarnings({ "static-access", "unchecked" })
	public Collection<Feature> selectedNoteLayerFeatures(ArrayList<LayerFeatureBean> LayersFeaturesIds){
		ArrayList<Feature> featureCollection = new ArrayList<Feature>();
		try {
			GeopistaEditor geopistaEditor =  ((PrintEditorPanel) AppContext.getApplicationContext().getBlackboard().get("GeopistaEditorPrint")).getEditor();
			if (geopistaEditor != null){
				this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				geopistaEditor .getSelectionManager().clear();
				Iterator<GeopistaLayer> geopistaLayers = geopistaEditor.getLayerManager().getLayers().iterator();
				while(geopistaLayers.hasNext()){
					GeopistaLayer geopistaLayer = geopistaLayers.next();
					int idLayer = geopistaLayer.getId_LayerDataBase();
					Iterator<Feature> allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
					while (allFeatures.hasNext()) {
						Feature feature= allFeatures.next();
						for (int i=0; i<LayersFeaturesIds.size(); i++){
							if (idLayer ==LayersFeaturesIds.get(i).getIdLayer()){
								if (((GeopistaFeature)feature).getID() == LayersFeaturesIds.get(i).getIdFeature()){
									featureCollection.add(feature);
								}
							}
						}
					}
				}
			}
			return featureCollection;
		} catch (Exception ex) {
			ex.printStackTrace();
			return featureCollection;
		}finally{
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	@SuppressWarnings("unchecked")
	private void refreshFeatureSelection() {
		
		ArrayList<LayerFeatureBean> layersFeaturesIds = new ArrayList<LayerFeatureBean>();
		
		if (getJTableListaNotas()!=null ){
			ArrayList<LocalGISNote> notes = ((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).getData();
			if(notes!=null && !notes.isEmpty()){
				Iterator<LocalGISNote> it = notes.iterator();
				while(it.hasNext()){
					LocalGISNote note = it.next();
					for(int i = 0; i < note.getFeatureRelation().length; i++){
						layersFeaturesIds.add(note.getFeatureRelation()[i]);
					}
				}
			}
		}

		selectAndZoomToFeatureSelection(layersFeaturesIds);


	}

	public void selectAndZoomToFeatureSelection(ArrayList<LayerFeatureBean> LayersFeaturesIds) {
		try {
			GeopistaEditor geopistaEditor =  ((PrintEditorPanel) AppContext.getApplicationContext().getBlackboard().get("GeopistaEditorPrint")).getEditor();
			if (geopistaEditor != null){
				if (LayersFeaturesIds!=null && !LayersFeaturesIds.isEmpty()){
					this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					geopistaEditor .getSelectionManager().clear();
					Iterator<GeopistaLayer> geopistaLayers = geopistaEditor.getLayerManager().getLayers().iterator();
					while(geopistaLayers.hasNext()){
						GeopistaLayer geopistaLayer = geopistaLayers.next();
						int idLayer = geopistaLayer.getId_LayerDataBase();
						Iterator<Feature> allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
						while (allFeatures.hasNext()) {
							Feature feature= allFeatures.next();
							if (feature!=null){
								for (int i=0; i<LayersFeaturesIds.size(); i++){
									if (LayersFeaturesIds.get(i)!=null && LayersFeaturesIds.get(i).getIdLayer()!=null && LayersFeaturesIds.get(i).getIdLayer()!=null){
										if (idLayer ==LayersFeaturesIds.get(i).getIdLayer()){
											if (UtilidadesAvisosPanels.getFeatureSystemId((GeopistaFeature)feature) == LayersFeaturesIds.get(i).getIdFeature()){
												geopistaEditor.select(geopistaLayer, feature);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			geopistaEditor.zoomToSelected();
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	private JButton getBuscarJButton(){
		if (buscarButton == null){
			buscarButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.print.panel.listbuttons.buscar"));
			
			buscarButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onBuscarButtonDo();
				}
			});
		}
		return buscarButton;
	}
	
	private void onBuscarButtonDo() {
		if (this.searchOptionsDialog == null){
			searchOptionsDialog = new SearchPrintDialog(AppContext.getApplicationContext().getMainFrame());
		} else{
			if(confirmarNuevaBusqueda()){
				this.searchOptionsDialog.setVisible(true);
			} else{
				this.searchOptionsDialog = new SearchPrintDialog(AppContext.getApplicationContext().getMainFrame());
			}
		}
		
		if (searchOptionsDialog.wasOKPressed()){
			ArrayList<LocalGISNote> listaNotas = new ArrayList<LocalGISNote>();
				
			String userName = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
			int idMunicipio = AppContext.getIdMunicipio();
			if ((userName != null && !userName.equals("")) && idMunicipio > 0 ){
				FinderNoteConditions noteConditions = searchOptionsDialog.getFinderNoteConditions();
				if (noteConditions != null){
					try {
						listaNotas.addAll(WSInterventionsWrapper.getNoteListByContidions(userName, idMunicipio, noteConditions));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				FinderInterventionConditions interventionConditions = searchOptionsDialog.getFrinderInterventionConditions();
				if (interventionConditions!=null){
					try {
						listaNotas.addAll(WSInterventionsWrapper.getInterventionListByConditions(userName, idMunicipio, interventionConditions));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).setData(listaNotas);
//			UtilidadesAvisosPanels.packColumns(getJTableListaNotas(), 2);
			getJTableListaNotas().pakcsNotesListColumns();
			getJTableListaNotas().updateUI();
			getJScrollPaneListaNotas().updateUI();
			this.updateUI();
			refreshFeatureSelection();
			
		}
		
		
	}
	
	private boolean confirmarNuevaBusqueda() {

		String mensaje = 
			I18N.get("avisospanels","localgisgestionciudad.interfaces.print.panel.newsearch");

		int seleccion = JOptionPane.showOptionDialog(
				AppContext.getApplicationContext().getMainFrame(),
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
	
	private JButton getImprimirButton(){
		if (imprimirMapaButton == null){
			imprimirMapaButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.printsearch.button.print.map"));
			imprimirMapaButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onImprimirButtonDo();
				}
			});
		}
		return imprimirMapaButton;
	}
	
	
	private void onImprimirButtonDo() {
//		GestionCiudadMapPrinter printer = new GestionCiudadMapPrinter();
		
		GeopistaEditor geopistaEditor =  ((PrintEditorPanel) AppContext.getApplicationContext().getBlackboard().get("GeopistaEditorPrint")).getEditor();
		if (geopistaEditor != null){
//			printer.setMapaGestionCiudad(geopistaEditor.getLayerViewPanel());
			
			if(!bajarPlantillaMapaFiles()){
				JOptionPane.showConfirmDialog(null, I18N.get("avisospanels","localgisgestionciudad.interfaces.print.panel.plantilla.map.notfound"));
			}
			
			GeopistaPrintPlugIn geopistaPrint = new GeopistaPrintPlugIn();
			GeopistaUtilLicenciaActividad.executePlugIn(geopistaPrint, geopistaEditor.getContext(),new TaskMonitorManager());
		      
		    
		}
		
//		try {
//			(new GeopistaPrintable()).printObjeto("",null , LocalGISGestionCiudad.class, geopistaEditor.getLayerViewPanel(), 0);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
	}
	
	
	private JButton getImprimirInformesActuaciones(){
		if (imprimirInformesActuaciones == null){
//			imprimirInformesActuaciones = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.printsearch.button.print.intervention"));
			imprimirInformesActuaciones = new JButton("<html> <center> Imprimir Informe <br>Actuaciones </center></html>");
			
			imprimirInformesActuaciones.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent e) {
					onImprimirInformesActuacionesDo();
				}
				});
			
			imprimirInformesActuaciones.setEnabled(false);
		}
		return imprimirInformesActuaciones;
	}
	

	private void onImprimirInformesActuacionesDo() {
		generarInformeIntervenciones();
	}
	
	
	private Map obtenerParametros(String idWarning){

		Map parametros = new HashMap();
		parametros.put("ID_ENTIDAD", Integer.toString(AppContext.getIdEntidad()));
		parametros.put("LOCALE", AppContext.getApplicationContext().getI18NResource().getLocale().toString());
		parametros.put("ID_WARNING",idWarning);
		return parametros;

	}
	
	
	private void generarInformeIntervenciones(){
		try{

			ArrayList<LocalGISNote> listaNotas = ((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).getData();
			ArrayList<LocalGISIntervention> intervenciones = new ArrayList<LocalGISIntervention>();
			
			if (listaNotas!= null && !listaNotas.isEmpty()){
				Iterator<LocalGISNote> iterNotes = listaNotas.iterator();
				while(iterNotes.hasNext()){
					LocalGISNote note = iterNotes.next();
					if (note instanceof LocalGISIntervention){
						intervenciones.add((LocalGISIntervention) note);
					}
				}
			}
			
			
			int selectedRow = getJTableListaNotas().getSelectedRow();
			Object selectedItem = null;
			try{
				selectedItem = ((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).getValueAt(selectedRow);
			} catch (ArrayIndexOutOfBoundsException exception){
			}
			
//			if ((_vExpedientes != null) && (_vExpedientes.size() > 0) && (_vSolicitudes != null) && (_vSolicitudes.size() > 0)){
			if ((intervenciones != null) && !intervenciones.isEmpty() && selectedItem != null && selectedItem instanceof LocalGISIntervention ){

				// Generamos el informe con la informacion
				this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				// bajamos del servidor la plantilla seleccionada por el usuario
				boolean encontrado = bajarInformeFiles("actuaciones_espacio_publico.jrxml");

				if (encontrado){          
					try {
						Map parametros = obtenerParametros(Integer.toString(((LocalGISIntervention)selectedItem).getId()));
						// Le pasamos el nombre del informe (ruta absoluta): 
						GenerarInformeExterno giep=new GenerarInformeExterno(reportFile, parametros);

					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				mostrarMensaje("Para generar el informe es necesario que la búsqueda tenga datos de intervencion.");
			}
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}
	
	 private JFrame desktop;
	 private Logger logger = Logger.getLogger(SearchListPanel.class);
	 private String reportFile = "";
	 
	 public void mostrarMensaje(String mensaje) {
	        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), mensaje);
	    }
	 
	 public boolean bajarInformeFiles(String name){
		 String path= "";
		 String url= "";
		 String pathDestino= "";
		 String plantillasURL= "";

		 try {

			 String localPath = aplicacion.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH, true);
			 
			 path= "plantillas"+ File.separator+ "espaciopublico" + File.separator;
			 pathDestino = localPath + aplicacion.REPORT_DIR_NAME + File.separator + path;
			 plantillasURL =  "http://localgis.grupotecopy.es:8081/plantillas/espaciopublico/";
			 
			 

			 // Comprobamos que el path de las plantillas exista 
			 if (!new File(pathDestino).exists()) {
				 new File(pathDestino).mkdirs();
			 }

			 // bajamos la plantilla            
			 url = plantillasURL + name;
			 pathDestino = pathDestino + name;
			 reportFile = pathDestino;
			 // Devolvemos verdadero si todo ha sido correcto:
			 return CUtilidadesComponentes.GetURLFile(url, pathDestino, "", 0);
		 } catch (Exception ex) {
			 StringWriter sw = new StringWriter();
			 PrintWriter pw = new PrintWriter(sw);
			 ex.printStackTrace(pw);
			 logger.error("Exception: " + sw.toString());
			 return false;
		 }

	 }
	 
	 
	 public boolean bajarPlantillaMapaFiles(){
		 String path= "";
		 String url= "";
		 String pathDestino= "";
		 String plantillasURL= "";
		 String name = "PlantillaEspacioPublico.jmp";

		 try {

			 String localPath = aplicacion.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH, true);
			 
			 path= "plantillas"+ File.separator+ "espaciopublico" + File.separator;
			 pathDestino = localPath;
			 plantillasURL =  "http://localgis.grupotecopy.es:8081/plantillas/espaciopublico/";
			 
			 

			 // Comprobamos que el path de las plantillas exista 
			 if (!new File(pathDestino).exists()) {
				 new File(pathDestino).mkdirs();
			 }

			 // bajamos la plantilla            
			 url = plantillasURL + name;
			 pathDestino = pathDestino + name;
			 reportFile = pathDestino;
			 // Devolvemos verdadero si todo ha sido correcto:
			 return CUtilidadesComponentes.GetURLFile(url, pathDestino, "", 0);
		 } catch (Exception ex) {
			 StringWriter sw = new StringWriter();
			 PrintWriter pw = new PrintWriter(sw);
			 ex.printStackTrace(pw);
			 logger.error("Exception: " + sw.toString());
			 return false;
		 }

	 }

}
