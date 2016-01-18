/**
 * MigracionAsistidaDeterminacionesPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.migracionasistida.panel;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.gestorfip.app.planeamiento.dialogs.accesocomun.images.IconLoader;
import com.gestorfip.app.planeamiento.dialogs.migracionasistida.TableRenderDeterminacionesMA;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

import es.gestorfip.serviciosweb.ServicesStub.ConfLayerBean;

public class MigracionAsistidaDeterminacionesPanel extends JPanel implements WizardPanel
{
	private AppContext application = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard = application.getBlackboard();
	public static final int DIM_X=700;
	public static final int DIM_Y=450;
	private JTabbedPane tabbedPane;
	private WizardContext wizardContext; 
	private String nextID = null;
	private String nextIDSave = null;
	private String localId = null;
	private boolean permiso = true;
	private ConfLayerBean[] lstConfLayerBean;
	private int numLayers = 12;
	private TableRenderDeterminacionesMA[] lstTableRenderer = null;
	private boolean[] lstAplicaLayer = null;
	private JButton selectedAllButton = null;
	private ArrayList<Boolean> lstStates = new ArrayList<Boolean>();
	
    public MigracionAsistidaDeterminacionesPanel(String localId, String nextId)
    {   
    	this.nextID = nextId;
    	this.nextIDSave = nextId;
		this.localId = localId;
		
		this.lstConfLayerBean =  (ConfLayerBean[]) application.getBlackboard().get(ConstantesGestorFIP.GESTORFIP_DATA_MIGRACION_ASISTIDA);
		this.numLayers = this.lstConfLayerBean.length;
		this.lstTableRenderer = new TableRenderDeterminacionesMA[numLayers];
		this.lstAplicaLayer = new boolean[numLayers];
		
		comprobarAplicacionCapas();
		
        try
        {     
            Locale loc=I18N.getLocaleAsObject();         
            ResourceBundle bundle = ResourceBundle.getBundle("com.gestorfip.app.planeamiento.dialogs.migracionasistida.language.GestorFip_MigracionasistidaLayersPaneli18n",loc,this.getClass().getClassLoader());
            I18N.plugInsResourceBundle.put("MigracionAsistidaLayers",bundle);

            jbInit();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception
    {  
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(DIM_X, DIM_Y));
        TitledBorder title;
        title = BorderFactory.createTitledBorder(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.title.determinaciones"));
        this.setBorder(title);
  
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(application.getMainFrame(), null);
        
        progressDialog.setTitle(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.cargadatosiniciales"));
        progressDialog.report(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.cargadatosiniciales"));
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
	            	  
		            	  JLabel desc = new JLabel(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.determinaciones.descripcion"));
		            	  JLabel desc1 = new JLabel(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.determinaciones.descripcion1"));
		            	  JLabel desc2 = new JLabel(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.determinaciones.descripcion2"));
		            	  JLabel desc3 = new JLabel(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.determinaciones.descripcion3"));
		            	  
		            	  add(desc,  
                                  new GridBagConstraints(0, 0, 1, 1, 1, 0.001, GridBagConstraints.CENTER,
                                          GridBagConstraints.HORIZONTAL, new Insets(10,10,0,0) ,0,0));
		            	  add(desc1,  
                                  new GridBagConstraints(0, 1, 1, 1, 1, 0.001, GridBagConstraints.CENTER,
                                          GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0) ,0,0));
	
		            	  add(desc2,  
                                  new GridBagConstraints(0, 2, 1, 1, 1, 0.001, GridBagConstraints.CENTER,
                                          GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0) ,0,0));
		            	  
		            	  add(desc3,  
                                  new GridBagConstraints(0, 3, 1, 1, 1, 0.001, GridBagConstraints.CENTER,
                                          GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0) ,0,0));
	
		            	  add(getSelectedAllButton(),  
                                  new GridBagConstraints(0, 4, 1, 1, 1, 0.001, GridBagConstraints.EAST,
                                          GridBagConstraints.NONE, new Insets(0,10,0,0) ,0,0));
		            	  
		            	  add(getTabbedPane(),  
                                  new GridBagConstraints(0, 5, 1, 1, 1, 1, GridBagConstraints.CENTER,
                                          GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
	
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
    
    
    public JTabbedPane getTabbedPane( ) {
		
		if(tabbedPane == null){
			tabbedPane = new JTabbedPane();
			for(int i=0; i<lstAplicaLayer.length; i++){
				
				if(lstAplicaLayer[i]){
					lstTableRenderer[i] = new TableRenderDeterminacionesMA(lstConfLayerBean[i].getLstConfLayerDeterminacionAplicada(), application);
					lstTableRenderer[i].setOpaque(true); //content panes must be opaque
					tabbedPane.addTab(I18N.get("MigracionAsistidaLayers",lstConfLayerBean[i].getNameLayer()), 
						null, lstTableRenderer[i]);
				}
			}
		}
		return tabbedPane;
	}
 
  	private void guardarDatos(){
  		
  		for(int i=0; i<lstAplicaLayer.length; i++){
  			if(lstAplicaLayer[i]){
	  			for (int j=0; j<this.lstConfLayerBean[i].getLstConfLayerDeterminacionAplicada().length; j++){
	  				this.lstConfLayerBean[i].getLstConfLayerDeterminacionAplicada()[j].setSelected(false);
	  			}
  			}
  		}
  		
  		
  		for(int i=0; i<lstAplicaLayer.length; i++){
  			if(lstAplicaLayer[i]){
  				 for (int h = 0; h < lstTableRenderer[i].getData().length; h++) {
  					 Object[] obj = lstTableRenderer[i].getData()[h];
  					 if((Boolean)obj[TableRenderDeterminacionesMA.POSCAMPOSEL]){
  						 //elemento seleccionado

  						for (int j=0; j<this.lstConfLayerBean[i].getLstConfLayerDeterminacionAplicada().length; j++){
  							if(this.lstConfLayerBean[i].getLstConfLayerDeterminacionAplicada()[j].getDeterminacionLayer().getCodigo()
  										.equals((String)obj[TableRenderDeterminacionesMA.POSCAMPOCODIGO])){
  								this.lstConfLayerBean[i].getLstConfLayerDeterminacionAplicada()[j].setAliasDeterminacion((String)obj[TableRenderDeterminacionesMA.POSCAMPOALIAS]);
  								this.lstConfLayerBean[i].getLstConfLayerDeterminacionAplicada()[j].setSelected(true);
  							}
  						}
  					 }
  					 else{
  						 
  					 }
  				 }
  			}
  		}
  		
  		blackboard.put(ConstantesGestorFIP.GESTORFIP_DATA_MIGRACION_ASISTIDA, this.lstConfLayerBean);

  	}
  	
  	/**
  	 * @param alias
  	 * @return
  	 */
  	private boolean validateAlias(String alias){	
  		 Pattern p = Pattern.compile("[a-z_]*");
    	 Matcher m = p.matcher(alias);
    	 return m.matches();
  	}
  	
  	private boolean validateData(){
  		for(int i=0; i<lstTableRenderer.length; i++){
	  		if(lstTableRenderer[i] != null &&lstTableRenderer[i].getTable() != null && lstTableRenderer[i].getTable().isEditing()){
	  			String alias = (String)lstTableRenderer[i].getTable().getCellEditor().getCellEditorValue();
	  			if(alias != null && !alias.equals("")){
	  				if(alias.length() > 50){
	  					JOptionPane.showMessageDialog(application.getMainFrame(), 
	  							I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.validate.limitecaracter"));
	  					return false;
	  				}
	  			}
	  			lstTableRenderer[i].getTable().getCellEditor().stopCellEditing();
	  			
	  		}
  		}
  		
  		boolean validateData = true;
  		boolean validateLayer[] = new boolean[numLayers];
  		for(int i=0; i<validateLayer.length; i++){
  			validateLayer[i] = true;
  		}
  		
  		for(int i=0; i<lstAplicaLayer.length; i++){
  			if(lstAplicaLayer[i]){
  				 for (int h = 0; h < lstTableRenderer[i].getData().length; h++) {
  					 Object[] obj = lstTableRenderer[i].getData()[h];
  					 if((Boolean)obj[TableRenderDeterminacionesMA.POSCAMPOSEL]){
  						 //elemento seleccionado
  						 if("".equals((String)obj[TableRenderDeterminacionesMA.POSCAMPOALIAS]) || 
  								 ((String)obj[TableRenderDeterminacionesMA.POSCAMPOALIAS]).length() > 50){
  							 validateLayer[i] = false;
  							 break;
  						 }
  						 else{
  							 //comprobar que los datos introducidos son correctos
  							 if(validateAlias((String)obj[TableRenderDeterminacionesMA.POSCAMPOALIAS])){
  								 if(validateLayer[i]){
  									 validateLayer[i] = true;
  								 }
  							 }
  							 else{
  								 validateLayer[i] = false;
  							 }

  						 }
  					 }
  				 }
  			}
  		}
  		
  		StringBuffer txtMsgLayers = new StringBuffer();
  		for(int i=0; i<validateLayer.length; i++){
  			if(!validateLayer[i] && lstAplicaLayer[i]){			
  				txtMsgLayers.append("   *").append(lstConfLayerBean[i].getNameLayer()); 
  				validateData = false;
  			}
  		}
  		
  		if(!validateData){
  			StringBuffer txtMsgValidate = new StringBuffer();
  			txtMsgValidate.append(I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.validate.txtnovalida"))
  				.append("\n").append(I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.validate.alias.condiciones")).append(":")
  				.append("\n").append(I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.validate.alias.vacio"))
  				.append("\n").append(I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.validate.alias.caracteres"))
  				.append("\n").append("\n").append(I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.layers"))
  				.append(":").append(txtMsgLayers);
  				
  			JOptionPane.showMessageDialog(this,txtMsgValidate.toString());
  		}
  		
  		return validateData;
  	}
  	
    public void enteredFromLeft(Map dataMap)
    {
        if(!application.isLogged())
        {
            application.login();
        }
        if(!application.isLogged())
        {
            wizardContext.cancelWizard();
            return;
        }        
        
//        this.layerBean = (ServicesStub.LayerConfigBean)blackboard.get(ConstantesGestorFIP.GESTORFIP_GET_DATA_LAYER_IMPORT);

    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    	boolean validate = validateData();
    	if (validate)
		{
    		this.nextID = this.nextIDSave;
    		guardarDatos();
		}
		else
		{
			this.nextID = this.localId;
		}
    }
    
    /**
     * Tip: Delegate to an InputChangedFirer.
     * 
     * @param listener
     *            a party to notify when the input changes (usually the
     *            WizardDialog, which needs to know when to update the enabled
     *            state of the buttons.
     */
    public void add(InputChangedListener listener)
    {
        
    }
    
    public void remove(InputChangedListener listener)
    {
        
    }
    
    public String getTitle()
    {
        return this.getName();
    }
    
    public String getID()
    {
        return this.localId;
    }
    
    public String getInstructions()
    {
        return " ";
    }
    
    public boolean isInputValid()
    {        
        if (!permiso)
        {
            JOptionPane.showMessageDialog(application.getMainFrame(), application
                    .getI18nString("NoPermisos"));
            return false;
        } 
        else
        {
        	return true;
        }
    }
    
    
    
    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }
    
    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {
        return nextID;
    }
    
    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }
    
    public void exiting()
    {   
    	
    }
    
    private void comprobarAplicacionCapas(){

    	for (int i=0; i < this.lstConfLayerBean.length; i++) {
    		lstStates.add(false);
    		ConfLayerBean confLayer = (ConfLayerBean) this.lstConfLayerBean[i];
    		if(confLayer!= null && confLayer.getLstConfLayerDeterminacionAplicada() != null && 
    				confLayer.getLstConfLayerDeterminacionAplicada()[0] != null){
    			// existen datos en la layer
    			this.lstAplicaLayer[i]= true;	
    		}
    		else{
    			this.lstAplicaLayer[i]= false;
    		}
		}
    }

    /**
     * Called when the user presses Back on this panel
     */
	public void backToLeft() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public JButton getSelectedAllButton() {
		if(selectedAllButton ==null){
			selectedAllButton = new JButton();
			selectedAllButton.setText(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.button.selectall"));
			selectedAllButton.setToolTipText(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.button.selectalltip"));
			
			selectedAllButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent evt)
				{			
					int indexPanelActivo = tabbedPane.getSelectedIndex();
					int indexTab = -1;
					for(int i=0; i<lstTableRenderer.length; i++){
						if(lstTableRenderer[i]!= null){
							indexTab++;
							if(indexPanelActivo == indexTab){
								lstStates.add(i,!lstStates.get(i));
								for(int j=0; j<lstTableRenderer[i].getTable().getModel().getRowCount(); j++){
									lstTableRenderer[i].getTable().getModel().setValueAt(lstStates.get(i), j, TableRenderDeterminacionesMA.POSCAMPOSEL);
								}
								
							}
						}
					}
					
					
				}
			});
		}
		return selectedAllButton;
	}

	public void setSelectedAllButton(JButton selectedAllButton) {
		this.selectedAllButton = selectedAllButton;
	}
    
	
}  

