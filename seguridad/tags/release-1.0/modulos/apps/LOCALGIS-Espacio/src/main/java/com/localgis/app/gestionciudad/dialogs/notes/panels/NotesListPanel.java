package com.localgis.app.gestionciudad.dialogs.notes.panels;

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
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.dialogs.interventions.renderers.NotePageSelectComboBoxRenderer;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.NotesInterventionsEditionTypes;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.dialogs.main.GeopistaEditorPanel;
import com.localgis.app.gestionciudad.dialogs.notes.dialogs.NotesEdicionDialog;
import com.localgis.app.gestionciudad.dialogs.notes.tables.NotesListTable;
import com.localgis.app.gestionciudad.dialogs.notes.tables.models.NoteSimpleTableModel;
import com.localgis.app.gestionciudad.images.IconLoader;
import com.localgis.app.gestionciudad.utils.TableSorted;
import com.localgis.app.gestionciudad.webservicesclient.wrapper.WSInterventionsWrapper;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author javieraragon
 *
 */
public class NotesListPanel extends JPanel{

	/**
	 * 
	 * 
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

	private JButton eliminarNotaButton = null;
	private JButton modificarAvisoButton = null;
	private JButton nuevaNotaButton = null;

	private JPanel paginacionPanel = null;
	private JPanel edicionPanel = null;
	private JPanel botoneraPanel = null;
	private NotesViewPanel datosNotasPanel = null;

	private NotesListTable notasTable = null;
	private JScrollPane tableScrollPane = null;

	private JCheckBox filtrarListaPorGeometriaCheckBox = null;

	public NotesListPanel(){
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
						GridBagConstraints.BOTH, 
						new Insets(0, 5, 0, 5), 0, 0));

		this.add(this.getJScrollPaneListaNotas(),
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.BOTH, 
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
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

		}
		return botoneraPanel;
	}

	private JPanel getEdicionPanel(){
		if (edicionPanel == null){
			edicionPanel = new JPanel(new GridBagLayout());

			edicionPanel.add(this.getNuevNotaButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			edicionPanel.add(this.getModificarAvisoButton(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			edicionPanel.add(this.getEliminarNotaButton(), 
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
							new Insets(0, 5, 0, 5), 0, 0));

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
			int numNotes = 0;
			try {
				numNotes = WSInterventionsWrapper.getNumNotesByConditions(usuario, idMunicipio, getFiltrarFeaturesSelected());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (numNotes > 0){
				int numPaginas = numNotes / NUM_NOTAS_POR_TABLA;
				if (numPaginas > 0){
					if((this.pagina+1>0) && (this.pagina+1 <= numPaginas)){
						this.pagina = pagina+1;

						this.AnniadirSeleccionarPaginaComboBox(pagina);
						reloadNoteList();						
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
				reloadNoteList();
			}
		}
	}

	private JComboBox getSeleccionarPaginaComboBox(){
		if (seleccionarPaginaComboBox == null){
			seleccionarPaginaComboBox = new JComboBox();
			seleccionarPaginaComboBox.setRenderer(new NotePageSelectComboBoxRenderer());

			String userName = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
			int idMunicipio = AppContext.getIdMunicipio();
			if( (userName!=null && !userName.equals("")) && (idMunicipio >0) ){
				int numNotes = 0;
				try {
					numNotes = WSInterventionsWrapper.getNumNotes(userName, idMunicipio);
				} catch (Exception e) {
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
						numPaginas = WSInterventionsWrapper.getNumNotesByConditions(userName, idMunicipio, getFiltrarFeaturesSelected())/NUM_NOTAS_POR_TABLA;
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (paginaSeleccionada>=0 && paginaSeleccionada<=numPaginas ){
						this.pagina = paginaSeleccionada;
						reloadNoteList();
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
					String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
					int idMunicipio = AppContext.getIdMunicipio();
					if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
						try {
							WSInterventionsWrapper.deleteNote((LocalGISNote)selectedItem, usuario, idMunicipio);
						} catch (Exception e) {
							e.printStackTrace();
						}

						reloadNoteList();
						getJTableListaNotas().updateUI();
					}
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

	private JButton getModificarAvisoButton(){
		if (modificarAvisoButton == null){
			modificarAvisoButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listbuttons.modify"));
			modificarAvisoButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onModificarNotaButtonDo();
				}
			});
			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				modificarAvisoButton.setEnabled(false);
			}
		}
		return modificarAvisoButton;
	}

	private void onModificarNotaButtonDo() {
		int row = this.getJTableListaNotas().getSelectedRow();
		if (row >= 0){
			Object selectedItem = ((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISNote){
				NotesEdicionDialog dialog = NotesEdicionDialog.createNotesEdicionDialog(this,NotesInterventionsEditionTypes.EDIT,(LocalGISNote) selectedItem);
				if (dialog.wasOKPressed()){

					String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
					int idMunicipio = AppContext.getIdMunicipio();
					if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
						try {
							WSInterventionsWrapper.modifyNote(dialog.getNote(),usuario,idMunicipio);
						} catch (Exception e) {
							e.printStackTrace();
						}
						reloadNoteList();
						getJTableListaNotas().updateUI();
					}

				}
			}
		}
	}

	private JButton getNuevNotaButton(){
		if (nuevaNotaButton == null){
			nuevaNotaButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listbuttons.new"));
			nuevaNotaButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onNuevaNotaButtonDo();
				}
			});

			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				nuevaNotaButton.setEnabled(false);
			}
		}
		return nuevaNotaButton;
	}

	private void onNuevaNotaButtonDo() {
		GeopistaEditor editorGeopista = ((GeopistaEditorPanel) AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor")).getEditor();
		if (editorGeopista!=null && !editorGeopista.getSelectionManager().getSelectedItems().isEmpty()){
			NotesEdicionDialog dialog = NotesEdicionDialog.createNotesEdicionDialog(this,NotesInterventionsEditionTypes.NEW,new LocalGISNote());
			if (dialog.wasOKPressed()){
				ArrayList<LayerFeatureBean> layersAndFeatures = new ArrayList<LayerFeatureBean>();
				Iterator it = editorGeopista.getSelectionManager().getFeatureSelection().getFeaturesWithSelectedItems().iterator();

				while (it.hasNext()){
					GeopistaFeature feature = (GeopistaFeature) it.next();
					layersAndFeatures.add(new LayerFeatureBean(feature.getLayer().getId_LayerDataBase(),UtilidadesAvisosPanels.getFeatureSystemId(feature)));
				}


				LocalGISNote nuevaNota = dialog.getNote();
				nuevaNota.setFeatureRelation(layersAndFeatures.toArray(new LayerFeatureBean[layersAndFeatures.size()]));

				try {
					WSInterventionsWrapper.addNote(nuevaNota, 
							AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true), 
							AppContext.getIdMunicipio());
				} catch (Exception e) {
					e.printStackTrace();
				}

				this.reloadNoteList();
				this.getJTableListaNotas().updateUI();

			}
		}else{
			JOptionPane.showMessageDialog(this, "Error! Seleccione alguna feature del Editor.");
		}
	}

	private void reloadNoteList() {
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
		//progressDialog.setTitle(I18N.get("Expedientes","fxcc.panel.CargandoFicheroDXF"));
		progressDialog.setTitle("TaskMonitorDialog.Wait");
		progressDialog.report(I18N.get("avisospanels","localgisgestionciudad.interfaces.notes.panel.reloadingnotes"));
		progressDialog.addComponentListener(new ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{   
				new Thread(new Runnable()
				{
					public void run()
					{
						try {   
							GUIUtil.centreOnWindow(progressDialog);
							progressDialog.setVisible(true);
							
							LocalGISNote selectedNote = null;
							int row = getJTableListaNotas().getSelectedRow();
							if (row >= 0){
								Object selectedItem =null;
								try{
									selectedItem = ((NoteSimpleTableModel)((TableSorted)getJTableListaNotas().getModel()).getTableModel()).getValueAt(row);
								}catch (Exception e) {
									e.printStackTrace();
								}
								if (selectedItem!=null){
									try{
										selectedNote = (LocalGISNote) selectedItem;
									}catch (ClassCastException e) {
										e.printStackTrace();
										selectedNote = null;
									}
								}
							}
							
							String userName = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
							int idMunicipio = AppContext.getIdMunicipio();
							if ((userName != null && !userName.equals("")) && idMunicipio > 0 ){
							
								notasTable = null;
								
								((NoteSimpleTableModel)((TableSorted)(getJTableListaNotas()).getModel()).getTableModel()).
								setData(
										WSInterventionsWrapper.getRangeOrderNotes(
												userName, 
												AppContext.getIdMunicipio(),
												pagina,
												NUM_NOTAS_POR_TABLA,
												getFiltrarFeaturesSelected()
												)
								);

								tableScrollPane.setViewportView(getJTableListaNotas());
								
								try{
									if (selectedNote != null){
										Iterator<LocalGISNote> it = ((NoteSimpleTableModel)((TableSorted)(getJTableListaNotas()).getModel()).getTableModel()).getData().iterator();
										int i = 0;
										while(it.hasNext()){
											LocalGISNote intervention = it.next();
											if (intervention.getId() == selectedNote.getId()){
												break;
											}
											i ++;
										}
										getJTableListaNotas().getSelectionModel().setSelectionInterval(i, i);
									}
								}catch (Exception e) {
									e.printStackTrace();
								}
								
								

								int numNotes = WSInterventionsWrapper.getNumNotesByConditions(userName, idMunicipio, getFiltrarFeaturesSelected());
								if (numNotes/NUM_NOTAS_POR_TABLA > 0){
									int numPaginas = numNotes/NUM_NOTAS_POR_TABLA;
									if (getSeleccionarPaginaComboBox().getItemCount()-1 < numPaginas){
										for (int i = getSeleccionarPaginaComboBox().getItemCount()-1; i < numPaginas; i ++){
											getSeleccionarPaginaComboBox().addItem(i+1);
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

		getJScrollPaneListaNotas().updateUI();
		getJTableListaNotas().updateUI();
	}

	public JScrollPane getJScrollPaneListaNotas(){
		if (tableScrollPane == null){
			tableScrollPane = new JScrollPane();
			tableScrollPane.setViewportView(getJTableListaNotas());
			tableScrollPane.setSize(10,10);
		}
		return tableScrollPane;
	}

	public JTable getJTableListaNotas()
	{
		if (notasTable  == null)
		{		
			try {
				notasTable = new NotesListTable(WSInterventionsWrapper.getRangeNoteList(
						AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true), 
						AppContext.getIdMunicipio(),
						0,
						NUM_NOTAS_POR_TABLA));
			} catch (Exception e1) {
				e1.printStackTrace();
				notasTable = new NotesListTable(new ArrayList<LocalGISNote>());
			}
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
			if (selectedItem!=null && selectedItem instanceof LocalGISNote){
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
			if (selectedItem!=null && selectedItem instanceof LocalGISNote){
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
				selectAndZoomToFeatureSelection(layersAndFeatures);
			}
		}    	
	}

	private NotesViewPanel getDatosNotesPanel() {
		if (datosNotasPanel == null){
			datosNotasPanel = new NotesViewPanel(null);
		}
		return this.datosNotasPanel;
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
									if (UtilidadesAvisosPanels.getFeatureSystemId(((GeopistaFeature)feature)) == LayersFeaturesIds.get(i).getIdFeature()){
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
	
	
	
	private JCheckBox getFiltrarListaPorGeometriaCheckBox(){
		if (filtrarListaPorGeometriaCheckBox == null){
			filtrarListaPorGeometriaCheckBox = new JCheckBox(
					I18N.get("avisospanels","localgisgestionciudad.interfaces.notes.list.filtrar.checkbox"));
			
//			filtrarListaPorGeometriaCheckBox.addItemListener(new ItemListener() {
//				@Override public void itemStateChanged(ItemEvent e) {
//					if (e.getItem().equals(getFiltrarListaPorGeometriaCheckBox())){
//						pagina = 0;
//						reloadNoteList();
//					}
//				}
//			});
			
			filtrarListaPorGeometriaCheckBox.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) {
					if (e.getSource().equals(getFiltrarListaPorGeometriaCheckBox())){
						pagina = 0;
						reloadNoteList();
					}
				}
			});
		}
		return filtrarListaPorGeometriaCheckBox;
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
	
	public void onEditorAcctionPerformedDo(){
		if ( getFiltrarListaPorGeometriaCheckBox()!=null &&
				getFiltrarListaPorGeometriaCheckBox().isSelected() ){
			pagina = 0;
			reloadNoteList();
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
