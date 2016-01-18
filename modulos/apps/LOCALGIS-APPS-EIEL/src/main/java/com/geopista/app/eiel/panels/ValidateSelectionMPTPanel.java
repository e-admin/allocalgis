/**
 * ValidateSelectionMPTPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.InitEIEL;
import com.geopista.app.eiel.models.CheckTreeManager;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.HideableNode;
import com.geopista.app.eiel.utils.HideableTreeModel;
import com.geopista.app.utilidades.ProcessCancel;
import com.geopista.plugin.Constantes;
import com.geopista.server.database.validacion.beans.ValidacionesMPTBean;
import com.geopista.server.database.validacion.beans.ValidacionesPorCuadrosMPTBean;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ValidateSelectionMPTPanel extends JPanel{

	private JFileChooser fileChooser;   
	private JLabel jLabelTitle = null;
	private JTable jTableValidaciones;
	
	private JScrollPane jScrollPaneValidaciones;
	
	private DefaultTableModel datosValidaciones ;
	
	private ArrayList lstCuadrosTxt = new ArrayList();
	private ArrayList  lstValidacionesBBDD = null;
	private JLabel jLabelFase = null;
	private JTextField jFieldFase = null;
	private JButton validarJButton;


	private JSplitPane splitPane = null;
	private JPanel panelValidaciones = null;
	private JPanel panelVal = null;
	private JPanel resultadVal = null;
	private JScrollPane jScrollPaneresultad;
	private JTextArea jTextArearesultad;


	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

	private JScrollPane scrollTreePane = null;
	private JTree tree =null;
	
	private CheckTreeManager checkTreeManager = null;

	public ValidateSelectionMPTPanel(GridBagLayout layout){
		 super(layout);
		 rellenarCuadros();
		 initializeTree();
	     initialize();
	}

	 private void initialize(){      
	        Locale loc=I18N.getLocaleAsObject();         
	        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
	        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);


	        this.add(getPanelValidaciones(),new GridBagConstraints(0, 0, 1, 1, 0.0001, 0.0001,
	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                new Insets(5, 5, 0, 5), 0, 0));
	            
	 }
	 
	 private JFileChooser getFileChooser() {
	        if (fileChooser == null) {
	            fileChooser = new GUIUtil.FileChooserWithOverwritePrompting();
	             fileChooser.setDialogTitle(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.expMPT.save"));
	            GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
	            filter.addExtension("txt");
	            filter.setDescription("Texto");
	    		fileChooser.setFileHidingEnabled(false);
	    		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	            fileChooser.setFileFilter(filter);
	            File currentDirectory = (File) aplicacion.getBlackboard().get(Constantes.LAST_IMPORT_DIRECTORY);
	            fileChooser.setCurrentDirectory(currentDirectory);
	            Calendar calendar = new GregorianCalendar();
	            String currentdate = calendar.get(Calendar.DATE)+"-"+
				(calendar.get(Calendar.MONTH)+1)+"-"+
				calendar.get(Calendar.YEAR);
	            fileChooser.setSelectedFile(new File(File.separator+ConstantesLocalGISEIEL.FILENAME_VALIDACION_MPT+"_"+currentdate+".txt"));
	        }
	        return (JFileChooser) fileChooser;
	    }
	    
	

	 public String getDirectorySelected() {
		 String directory=null;
		 File file=null;
		 if(getFileChooser().getSelectedFile()!=null){
			 file=getFileChooser().getSelectedFile();
			 directory=file.getAbsolutePath();
			 aplicacion.getBlackboard().put(Constantes.LAST_IMPORT_DIRECTORY,file);
			 
		 }
		 
		return directory;
	}
	 
	public JTable getjTableValidaciones() {
		if(jTableValidaciones == null){
	    
		    String [] columnNames = {""}; 
		    Object [][] data = new Object[lstCuadrosTxt.size()][1];
    
		    for(int i=0; i< lstCuadrosTxt.size(); i++){
		    	data[i][0] = (String)lstCuadrosTxt.get(i);			
		   }

		    datosValidaciones = new tableDefaultTableModel(data,columnNames);
		    jTableValidaciones = new JTable(datosValidaciones);
			jTableValidaciones.setRowSelectionAllowed(true);
			jTableValidaciones.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		    jTableValidaciones.setPreferredScrollableViewportSize(new Dimension(100, 100));
		    jTableValidaciones.setEnabled(false);
		    jTableValidaciones.selectAll();
		}
		return jTableValidaciones;
	}

	public void setjTableCuadros(JTable jTableCuadros) {
		this.jTableValidaciones = jTableCuadros;
	}
	
	

	public JScrollPane getjScrollPaneValidaciones() {
		if(jScrollPaneValidaciones == null){
			 jScrollPaneValidaciones = new JScrollPane(getjTableValidaciones());
			 jScrollPaneValidaciones.setPreferredSize(new Dimension(100, 250));
			 jScrollPaneValidaciones.setEnabled(true);

		}
		return jScrollPaneValidaciones;
	}

	public void setjScrollPaneNucleo(JScrollPane jScrollPaneCuadro) {
		this.jScrollPaneValidaciones = jScrollPaneCuadro;
	}

	
	public ArrayList getLstCuadrosSelected(){
		ArrayList lstCuadrosSelected = new ArrayList();
		int[] cuadrosSelected = jTableValidaciones.getSelectedRows();
		
		for (int i= 0; i<cuadrosSelected.length; i++){
			lstCuadrosSelected.add(lstValidacionesBBDD.get(cuadrosSelected[i]));
		}
		return lstCuadrosSelected;
	}

	
	private void rellenarCuadros(){
		
		try {
			lstValidacionesBBDD =   InitEIEL.clienteLocalGISEIEL.obtenerValidacionesMPT();
			lstCuadrosTxt = new ArrayList();
			for (int i=0; i<lstValidacionesBBDD.size(); i++){		
				String nameCuadro = ((ValidacionesMPTBean)lstValidacionesBBDD.get(i)).getNombre() +
				 " - ("+((ValidacionesMPTBean)lstValidacionesBBDD.get(i)).getTabla()+")";
				lstCuadrosTxt.add(nameCuadro);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}
	
	 private JTextField getJTextFieldFase()
		{
		 if (jFieldFase  == null)
			{
			 jFieldFase  = new JTextField();
			 jFieldFase.setText(UserPreferenceStore.getUserPreference("FASE","", true));
			 jFieldFase.addCaretListener(new CaretListener()
				{
					public void caretUpdate(CaretEvent evt)
					{
						EdicionUtils.chequeaLongYNumCampoEdit(jFieldFase, 4, aplicacion.getMainFrame());
					}
				});
			}
			return jFieldFase;
		}
	 
	 public String getAnioEncuesta() {
			String anio=getJTextFieldFase().getText();
			UserPreferenceStore.setUserPreference("FASE",anio);
			return anio;
	}

	 class tableDefaultTableModel extends DefaultTableModel {  
		 
		 public tableDefaultTableModel(Object[][] data, Object[] columnNames) {  
		   super(data, columnNames);  
		 }  
		 
		 public boolean isCellEditable(int row, int col) {  
		   return false;  
		 }  
	}  

	private void initializeTree(){
		
		 DefaultMutableTreeNode top = new DefaultMutableTreeNode("CUADROS");
		 createNodes(top);
		 HideableTreeModel ml = new HideableTreeModel( top );
		tree = new JTree(ml);

	    scrollTreePane = new JScrollPane(tree);
	    scrollTreePane.setPreferredSize(new Dimension(200,350));
	   
	    checkTreeManager = new CheckTreeManager(tree); 
	}
	
	 private void createNodes(DefaultMutableTreeNode top) 
	    {   
	        for(int i=0; i<lstValidacionesBBDD.size();i++){
	        	ValidacionesMPTBean valmptbean = (ValidacionesMPTBean)lstValidacionesBBDD.get(i);
	        	HideableNode padre = new HideableNode(valmptbean);
	        	 top.add(padre);
	        	
	        	  for(int j=0; j<valmptbean.getLstvalidacuadros().size();j++){
	        		  ValidacionesPorCuadrosMPTBean vcmptbean = (ValidacionesPorCuadrosMPTBean)valmptbean.getLstvalidacuadros().get(j);
	        		  DefaultMutableTreeNode hijo = new DefaultMutableTreeNode(vcmptbean);
	        		  padre.add(hijo);
	        	 }
	        }        
	    }
	 public CheckTreeManager getCheckTreeManager() {
			return checkTreeManager;
	}

	public void setCheckTreeManager(CheckTreeManager checkTreeManager) {
		this.checkTreeManager = checkTreeManager;
	}
	
	public ArrayList getLstValidacionesBBDD() {
		return lstValidacionesBBDD;
	}

	public void setLstValidacionesBBDD(ArrayList lstValidacionesBBDD) {
		this.lstValidacionesBBDD = lstValidacionesBBDD;
	}

	 public int abrirJFileChooser(){
		 return getFileChooser().showSaveDialog(this);
	 }
	 
	 public JButton getValidarJButton() {
		 if(validarJButton == null){
			 validarJButton = new JButton();
			 validarJButton.setEnabled(true);
			 validarJButton.setText(I18N.get("LocalGISEIEL", "localgiseiel.mpt.validate.panel.title"));
			 validarJButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    
                	final TaskMonitorDialog progressDialog= new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
					progressDialog.setTitle("TaskMonitorDialog.Wait");
					progressDialog.addComponentListener(new ComponentAdapter()
					{
						public void componentShown(ComponentEvent e)
						{
							new Thread(new Runnable()
							{
								public void run()
								{
									
									try
									{  
										progressDialog.report(I18N.get("LocalGISEIEL", "localgiseiel.mpt.validate.panel.validando"));  
										
										ProcessCancel processCancel=null;			
				        				if (progressDialog!=null){				
				        					if (progressDialog!=null){
				        						processCancel=new ProcessCancel(progressDialog);
				        						processCancel.start();
				        					}
				        				}
										validarActionPerformed(evt);
										
									}
									catch(Exception e)
									{
										//logger.error("Error ", e);
										ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
										return;
									}
									finally
									{
										JOptionPane.showMessageDialog(
												ValidateSelectionMPTPanel.this,
				                                "Proceso de validación finalizado",
				                                null,
				                                JOptionPane.INFORMATION_MESSAGE);
										
										System.out.println((new StringBuilder("CERRAMOS  VALIDACION MPT--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
										progressDialog.setVisible(false);                                
										progressDialog.dispose();								
										//dispose();
									}
								}
							}).start();
						}
					});
					GUIUtil.centreOnWindow(progressDialog);
					progressDialog.setVisible(true);

					show();
                }
            });
		 }
		return validarJButton;
	}

	public void setValidarJButton(JButton validarJButton) {
		this.validarJButton = validarJButton;
	}

	public void validarActionPerformed( java.awt.event.ActionEvent evt){
		ArrayList lstIdValidaciones = new ArrayList();
		ArrayList lstValCuados = new ArrayList();
		Hashtable hs = new Hashtable();
		TreePath checkedPaths[] = getCheckTreeManager().getSelectionModel().getSelectionPaths(); 
		if(checkedPaths != null){
			for(int i=0; i<checkedPaths.length; i++){
				
				if (((DefaultMutableTreeNode)checkedPaths[i].getLastPathComponent()).getUserObject() instanceof
						ValidacionesMPTBean){
					// Seleccionados todas las validaciones del cuardro
					lstIdValidaciones.add(((ValidacionesMPTBean)((HideableNode)checkedPaths[i].getPath()[1]).getUserObject()).getId());
					ValidacionesMPTBean vmpt = (ValidacionesMPTBean) getLstValidacionesBBDD().get(
								((ValidacionesMPTBean)((HideableNode)checkedPaths[i].getPath()[1]).getUserObject()).getId());
					lstValCuados = new ArrayList();
					for (int z=0; z<vmpt.getLstvalidacuadros().size(); z++){
						lstValCuados.add( ((ValidacionesPorCuadrosMPTBean)vmpt.getLstvalidacuadros().get(z)).getNombre());
					}
					hs.put(vmpt.getId(),lstValCuados);
				}
				else if (((DefaultMutableTreeNode)checkedPaths[i].getLastPathComponent()).getUserObject() instanceof
						ValidacionesPorCuadrosMPTBean){
					
					if(!lstIdValidaciones.contains(((ValidacionesMPTBean)((HideableNode)checkedPaths[i].getPath()[1]).getUserObject()).getId())){
						lstValCuados = new ArrayList();
	
						lstIdValidaciones.add((((ValidacionesMPTBean)((HideableNode)checkedPaths[i].getPath()[1]).getUserObject()).getId()));
	
						lstValCuados.add( ((ValidacionesPorCuadrosMPTBean)((DefaultMutableTreeNode)checkedPaths[i].getLastPathComponent()).getUserObject()).getNombre());
	
					}
					else{				
						lstValCuados.add( ((ValidacionesPorCuadrosMPTBean)((DefaultMutableTreeNode)checkedPaths[i].getLastPathComponent()).getUserObject()).getNombre());
					}
					
					hs.put(lstIdValidaciones.get(lstIdValidaciones.size()-1),
							lstValCuados);
					
				}
				else{
					//estan seleccionados todos
				
					for (int h=0; h<getLstValidacionesBBDD().size(); h++){
						ValidacionesMPTBean vmpt = (ValidacionesMPTBean) getLstValidacionesBBDD().get(h);
						lstIdValidaciones.add(vmpt.getId());
						lstValCuados = new ArrayList();
						for (int z=0; z<vmpt.getLstvalidacuadros().size(); z++){
							
							lstValCuados.add( ((ValidacionesPorCuadrosMPTBean)vmpt.getLstvalidacuadros().get(z)).getNombre());
							
						}
						hs.put(vmpt.getId(),lstValCuados);
					}
				}
			}
		
			StringBuffer strb = new StringBuffer();
			
			Collections.sort(lstIdValidaciones);
			
			for(int i=0; i<lstIdValidaciones.size(); i++){
				
				byte[] bloque = null;
	
				try {
					bloque = InitEIEL.clienteLocalGISEIEL.validacionMPT(getJTextFieldFase().getText(),
								(Integer)lstIdValidaciones.get(i), (ArrayList)hs.get(lstIdValidaciones.get(i)));
					String strbloque = new String(bloque, "UTF8");
					strb.append(strbloque );
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}		

			getjTextArearesultad().setText(strb.toString());
		}
		else{
			JOptionPane.showMessageDialog(
                    this,
                    I18N.get("LocalGISEIEL", "localgiseiel.mpt.validate.seleccione.validaciones"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public JPanel getPanelVal() {
		if(panelVal == null){
			panelVal = new JPanel();
			panelVal.setLayout(new GridBagLayout());
			
	        jLabelFase = new JLabel("");
	        jLabelFase.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.anioencuesta"));  
	        
	        panelVal.setName(I18N.get("LocalGISEIEL","localgiseiel.mpt.validate.panel.title"));
	        
	        panelVal.setLayout(new GridBagLayout());
	        jLabelTitle = new JLabel("", JLabel.LEFT);
	        jLabelTitle.setText(I18N.get("LocalGISEIEL", "localgiseiel.mpt.validate.seleccion"));       
    	
	        panelVal.add(jLabelFase,  
	        		new GridBagConstraints(0, 0, 1, 1, 1, 0.1,
	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                new Insets(5, 5, 0, 5), 0, 0));
    	
    	
	        panelVal.add(getJTextFieldFase(),  
	    	        		new GridBagConstraints(0, 1, 1, 1, 1.0, 0.1,
	    	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	    	                new Insets(5, 5, 0, 5), 0, 0));
	    	
	        panelVal.add(jLabelTitle,  
		        		new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
		                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
		                new Insets(5, 5, 0, 5), 0, 0));
	    	
	        panelVal.add(scrollTreePane,  
	        		new GridBagConstraints(0, 3, 1, 1, 1,0.9,
	                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
	                new Insets(5, 5, 0, 5), 0, 0));
	    	
	        panelVal.add(getValidarJButton(),  
	        		new GridBagConstraints(0, 4, 1, 1, 1,0.9,
	                GridBagConstraints.CENTER, GridBagConstraints.NONE,
	                new Insets(5, 5, 0, 5), 0, 0));
			
		}
		return panelVal;
	}

	public void setPanelVal(JPanel panelVal) {
		this.panelVal = panelVal;
	}
	
	
	
	public JPanel getResultadVal() {
		if(resultadVal == null){
			resultadVal = new JPanel();
			resultadVal.setLayout(new GridBagLayout());
	  
			resultadVal.add(getjScrollPaneresultad(),	new GridBagConstraints(0, 0, 1, 1, 1,1,
	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                new Insets(5, 5, 5, 5), 0, 0));
			
		}
		return resultadVal;
	}

	public void setResultadVal(JPanel resultadVal) {
		this.resultadVal = resultadVal;
	}


	public JTextArea getjTextArearesultad() {
		if(jTextArearesultad == null){
			jTextArearesultad = new JTextArea();
			jTextArearesultad.setEditable(false);	
			jTextArearesultad.setLineWrap(true);
			jTextArearesultad.setWrapStyleWord(true);
			jTextArearesultad.setRows(24);
			jTextArearesultad.setColumns(51);
		}
		return jTextArearesultad;
	}

	public void setjTextArearesultad(JTextArea jTextArearesultad) {
		this.jTextArearesultad = jTextArearesultad;
	}

	public JScrollPane getjScrollPaneresultad() {
		if(jScrollPaneresultad == null){
			jScrollPaneresultad = new JScrollPane(getjTextArearesultad());
			jScrollPaneresultad.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		}
		return jScrollPaneresultad;
	}

	public void setjScrollPaneresultad(JScrollPane jScrollPaneresultad) {
		this.jScrollPaneresultad = jScrollPaneresultad;
	}
	
	public JPanel getPanelValidaciones() {
		if(panelValidaciones == null){
			panelValidaciones = new JPanel();
			panelValidaciones.setLayout(new GridBagLayout());
			panelValidaciones.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			panelValidaciones.add(getPanelVal(),new GridBagConstraints(0, 0, 1, 1, 1.0, 0.1,
		                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                new Insets(5, 5, 0, 5), 0, 0));
		        		
			panelValidaciones.add(getResultadVal(),new GridBagConstraints(1, 0, 1, 1, 1.0, 0.1,
	 	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	 	                new Insets(5, 5, 0, 5), 0, 0));
		}
		return panelValidaciones;
	}

	public void setPanelValidaciones(JPanel panelValidaciones) {
		this.panelValidaciones = panelValidaciones;
	}


}
