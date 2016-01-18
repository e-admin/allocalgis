package com.localgis.app.gestionciudad.dialogs.interventions.dialogs;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.dialogs.interventions.panels.AvisosViewPanel;
import com.localgis.app.gestionciudad.dialogs.interventions.tables.AvisosCaducadosTable;
import com.localgis.app.gestionciudad.dialogs.interventions.tables.models.AvisoCaducadoTableModel;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.NotesInterventionsEditionTypes;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.utils.LocalGISObraCivilUtils;
import com.localgis.app.gestionciudad.utils.TableSorted;
import com.localgis.app.gestionciudad.webservicesclient.wrapper.WSInterventionsWrapper;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author javieraragon
 *
 */
public class AvisosCaducadosDialog  extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5064138054539907229L;
	//	private PlugInContext context = null;

	private AvisosViewPanel datosAvisoPanel = null;
	private JPanel listaAvisosCaducadosPanel = null;
	private JPanel botoneraPanel = null;	
	private JButton posponerButton = null;
	private JButton descartarButton = null;
	private JButton descartarTodosButton = null;
	private JButton modificarButton = null;
	private JButton cerrarButton = null;
	private JScrollPane jScrollPaneListaAvisos = null;
	private AvisosCaducadosTable jTableListaAvisos = null;

	private NotesInterventionsEditionTypes tipoEdicion = null;

	public static AvisosCaducadosDialog createAvisosCaducadosDialog(Component parentComponent, NotesInterventionsEditionTypes tipoEdicion){
		Window window = LocalGISObraCivilUtils.getWindowForComponent(parentComponent);
		if (window instanceof Frame) {
			return new AvisosCaducadosDialog((Frame)window, tipoEdicion);
		} else {
			return new AvisosCaducadosDialog((Dialog)window, tipoEdicion);
		}			
	}


	public AvisosCaducadosDialog(Frame parentFrame, NotesInterventionsEditionTypes edicion){
		super(parentFrame,"",true);
		this.tipoEdicion = edicion;
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		this.setTitle(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.caducadosdialog.title"));

		this.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());

		this.initialize();
		this.pack();
		this.setSize(800,450);
		this.setVisible(true);
	}

	private AvisosCaducadosDialog(Dialog parentDialog, NotesInterventionsEditionTypes edicion){
		super(parentDialog,"",true);
		this.tipoEdicion = edicion;
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		this.setTitle(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.caducadosdialog.title"));

		this.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());

		this.initialize();
		this.pack();
		this.setSize(800,450);
		this.setVisible(true);
	}

	private ArrayList<LocalGISIntervention> expiredInterventions = null;

	public AvisosCaducadosDialog(Frame threadAvisosCaducadosFrame,
			NotesInterventionsEditionTypes edit,
			ArrayList<LocalGISIntervention> expiredInterventions) {
		super(threadAvisosCaducadosFrame,"",true);
		this.tipoEdicion = edit;
		this.expiredInterventions = expiredInterventions; 
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		this.setTitle(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.caducadosdialog.title"));

		this.initialize();
		this.pack();
		this.setSize(800,450);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}


	private void initialize() {
		this.setLayout(new GridBagLayout());

		this.add(this.getDatosAvisoPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 20));

		this.add(this.getListaAvisosCaducadosPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 90));

		this.add(this.getBotneraPanel(), 
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));

	}


	private AvisosViewPanel getDatosAvisoPanel() {
		if (datosAvisoPanel == null){
			datosAvisoPanel = new AvisosViewPanel(null);
		}
		return this.datosAvisoPanel;
	}


	private JPanel getListaAvisosCaducadosPanel() {
		if (this.listaAvisosCaducadosPanel == null){
			this.listaAvisosCaducadosPanel = new JPanel(new GridBagLayout());

			listaAvisosCaducadosPanel.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.caducadosdialog.listabordertitle"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));

			listaAvisosCaducadosPanel.add(this.getJScrollPaneListaAvisos(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.BOTH, 
							new Insets(0, 5, 0, 5), 0, 10));

		}
		return this.listaAvisosCaducadosPanel;
	}


	private Component getBotneraPanel() {
		if (botoneraPanel == null){
			botoneraPanel = new JPanel(new GridBagLayout());

			botoneraPanel.add(this.getDescartarButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 5));

			botoneraPanel.add(this.getModificarButton(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 5));

			botoneraPanel.add(this.getDescartarTodosButton(), 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 5));

			botoneraPanel.add(this.getPosponerButton(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 5));

			botoneraPanel.add(this.getCerrarButton(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 5));
		}
		return this.botoneraPanel;
	}


	private JButton getPosponerButton(){
		if (this.posponerButton == null){
			posponerButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.caducadosdialog.posponerbutton"));
			posponerButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onPosponerButtonDo();
				}			
			});
		}
		return posponerButton;
	}

	private void onPosponerButtonDo() {
		int row = this.getJTableListaAvisos().getSelectedRow();
		if (row >= 0){
			Object selectedItem = ((AvisoCaducadoTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISIntervention){
				String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
				int idMunicipio = AppContext.getIdMunicipio();
				if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
					GregorianCalendar nextWarning = new GregorianCalendar();
					nextWarning.add(GregorianCalendar.DATE, 1);
					if (confirmarPosponer(nextWarning)){
						try {
							if (WSInterventionsWrapper.changeInterventionDate( usuario, idMunicipio,(LocalGISIntervention) selectedItem,nextWarning)){
								reloadExpiredsInterventionList();
								getJTableListaAvisos().updateUI();
								getJScrollPaneListaAvisos().updateUI();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}	



	private boolean confirmarPosponer(GregorianCalendar calendar) {

		String mensaje ="<html>¿Desea realmente posponer la actuación un día? <br>" +
				"Si pulsa aceptar esta actuación caducará el: <b>"+ calendar.getTime().toString() +"</b></html>";
		
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


	private JButton getDescartarButton(){
		if (this.descartarButton == null){
			descartarButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.caducadosdialog.descartarbutton"));
			descartarButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onDescartarButtonDo();
				}			
			});
		}
		return descartarButton;
	}

	private void onDescartarButtonDo() {
		int row = this.getJTableListaAvisos().getSelectedRow();
		if (row >= 0){
			Object selectedItem = ((AvisoCaducadoTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISIntervention){
				String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
				int idMunicipio = AppContext.getIdMunicipio();
				if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
					try {
						if (WSInterventionsWrapper.cancelIntervention((LocalGISIntervention) selectedItem,usuario,idMunicipio)){
							reloadExpiredsInterventionList();
							getJTableListaAvisos().updateUI();	
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}	

	private JButton getDescartarTodosButton(){
		if (this.descartarTodosButton == null){
			descartarTodosButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.caducadosdialog.descartartodosbutton"));
			descartarTodosButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onDescartarTodosButtonDo();
				}			
			});
		}
		return descartarTodosButton;
	}
	
	
	private void onDescartarTodosButtonDo() {;
		ArrayList<LocalGISIntervention> interventionList = ((AvisoCaducadoTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getData();
		boolean toReload = false;
		if (interventionList!=null && !interventionList.isEmpty()){
			Iterator<LocalGISIntervention> iterator = interventionList.iterator();
			while(iterator.hasNext()){
				LocalGISIntervention interventionToDiscard = iterator.next();
				if (interventionToDiscard != null){
					String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
					int idMunicipio = AppContext.getIdMunicipio();
					if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
						try {
							if (WSInterventionsWrapper.cancelIntervention(interventionToDiscard, usuario,idMunicipio)){
									toReload = true;
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		if (toReload){
			reloadExpiredsInterventionList();
			getJTableListaAvisos().updateUI();
		}
	}	


	private JButton getModificarButton(){
		if (this.modificarButton == null){
			modificarButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.caducadosdialog.modificarbutton"));
			modificarButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onModificarButtonDo();
				}			
			});
		}
		return modificarButton;
	}

	private void onModificarButtonDo() {
		int row = this.getJTableListaAvisos().getSelectedRow();
		if (row >= 0){
			Object selectedItem = ((AvisoCaducadoTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISIntervention){
				AvisosEdicionDialog dialog = AvisosEdicionDialog.createAvisosEdicionDialog(this,NotesInterventionsEditionTypes.EDIT,(LocalGISIntervention) selectedItem);
				if (dialog.wasOKPressed()){
					String usuario = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
					int idMunicipio = AppContext.getIdMunicipio();
					if( (usuario != null && !usuario.equals("")) && (idMunicipio > 0) ){
						try {
							WSInterventionsWrapper.modifyIntervention(dialog.getAviso(),usuario,idMunicipio);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						reloadExpiredsInterventionList();
						getJTableListaAvisos().updateUI();
					}
					
					
					
				}
			}
		}
	}


	private JButton getCerrarButton(){
		if (this.cerrarButton == null){
			cerrarButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.caducadosdialog.cerrarbutton"));
			cerrarButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onCerrarButtonDo();
				}			
			});
		}
		return cerrarButton;
	}
	private void onCerrarButtonDo() {
		this.setVisible(false);
		this.dispose();
	}



	public JScrollPane getJScrollPaneListaAvisos(){

		if (jScrollPaneListaAvisos == null){
			jScrollPaneListaAvisos = new JScrollPane();
			if (expiredInterventions!= null && !expiredInterventions.isEmpty()){
				jScrollPaneListaAvisos.setViewportView(getJTableListaAvisos(expiredInterventions));
			} else{
				jScrollPaneListaAvisos.setViewportView(getJTableListaAvisos());	
			}
			jScrollPaneListaAvisos.setSize(10,10);

		}
		return jScrollPaneListaAvisos;
	}

	public JTable getJTableListaAvisos()
	{
		if (jTableListaAvisos  == null)
		{
					
			try {
				jTableListaAvisos = new AvisosCaducadosTable(WSInterventionsWrapper.getAllInterventionList(
						AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true), 
						AppContext.getIdMunicipio()));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				jTableListaAvisos = new AvisosCaducadosTable(new ArrayList<LocalGISIntervention>());
			}
			jTableListaAvisos.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent evt) {
					loadSelectedItem();
				}
				public void mouseClicked(MouseEvent e){
					if (e.getClickCount() == 2){
						onTableDoubleClickDo();
					}
				}
			});
			jTableListaAvisos.addKeyListener(new KeyListener(){
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
		return jTableListaAvisos;
	}

	public JTable getJTableListaAvisos(ArrayList<LocalGISIntervention> expiredInterventions)
	{
		if (jTableListaAvisos  == null)
		{		
			jTableListaAvisos = new AvisosCaducadosTable(expiredInterventions);
			jTableListaAvisos.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent evt) {
					loadSelectedItem();
				}
				public void mouseClicked(MouseEvent e){
					if (e.getClickCount() == 2){
						onTableDoubleClickDo();
					}
				}
			});
			jTableListaAvisos.addKeyListener(new KeyListener(){
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
		return jTableListaAvisos;
	}

	private void onTableEnterKeyPressedDo(){
		int row = this.getJTableListaAvisos().getSelectedRow();
		if (row >= 0){
			Object selectedItem = ((AvisoCaducadoTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISIntervention){
				AvisosEdicionDialog dialog = AvisosEdicionDialog.createAvisosEdicionDialog(this,this.tipoEdicion,(LocalGISIntervention) selectedItem);
				dialog.dispose();
			}
			try{
				Object newItem = ((AvisoCaducadoTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(row +1);

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
			Object selectedItem = ((AvisoCaducadoTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(row);
			if (selectedItem!=null && selectedItem instanceof LocalGISIntervention){
				AvisosEdicionDialog dialog = AvisosEdicionDialog.createAvisosEdicionDialog(this,NotesInterventionsEditionTypes.VIEW,(LocalGISIntervention) selectedItem);
				dialog.dispose();
			}
		}
	}

	private void loadSelectedItem(){

		int selectedRow = getJTableListaAvisos().getSelectedRow();
		Object selectedItem = ((AvisoCaducadoTableModel)((TableSorted)getJTableListaAvisos().getModel()).getTableModel()).getValueAt(selectedRow);

		if (selectedItem != null && selectedItem instanceof LocalGISIntervention){
			this.getDatosAvisoPanel().loadAviso((LocalGISIntervention)selectedItem);
		}    	
	}

	private void reloadExpiredsInterventionList() {
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
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
							String userName = AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true);
							int idMunicipio = AppContext.getIdMunicipio();
							if ((userName != null && !userName.equals("")) && idMunicipio > 0 ){
								((AvisoCaducadoTableModel)((TableSorted)(getJTableListaAvisos()).getModel()).getTableModel()).
								setData(
										WSInterventionsWrapper.getExpiredInterventions(
												userName, 
												AppContext.getIdMunicipio(),
												new GregorianCalendar())
								);
								getJTableListaAvisos().updateUI();
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

	}


}
