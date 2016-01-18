/**
 * MigracionAsistidaRegulacionesPanel.java
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
import com.gestorfip.app.planeamiento.dialogs.migracionasistida.TableRenderDeterminacionesMA;
import com.gestorfip.app.planeamiento.dialogs.migracionasistida.TableRenderRegulacionesMA;
import com.gestorfip.app.planeamiento.dialogs.migracionasistida.TableRenderValoresMA;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import es.gestorfip.serviciosweb.ServicesStub.ConfLayerBean;

public class MigracionAsistidaRegulacionesPanel extends JPanel implements WizardPanel
{
	private AppContext application = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard = application.getBlackboard();
	public static final int DIM_X=700;
	public static final int DIM_Y=450;
	private int numLayers = 12;
	private TableRenderRegulacionesMA[] lstTableRenderer = null;
	private JTabbedPane tabbedPane;
	private WizardContext wizardContext; 
	private String nextID = null;
	private String nextIDSave = null;
	private String localId = null;
	private boolean permiso = true;
	private ConfLayerBean[] lstConfLayerBean;
	private boolean[] lstAplicaLayer = null;
	private JButton selectedAllButton = null;
	private ArrayList<Boolean> lstStates = new ArrayList<Boolean>();
    
    public MigracionAsistidaRegulacionesPanel(String localId, String nextId)
    {   
    	this.nextID = nextId;
    	this.nextIDSave = nextId;
		this.localId = localId;
			
		this.lstConfLayerBean =  (ConfLayerBean[]) application.getBlackboard().get(ConstantesGestorFIP.GESTORFIP_DATA_MIGRACION_ASISTIDA);
		this.numLayers = this.lstConfLayerBean.length;
		this.lstTableRenderer = new TableRenderRegulacionesMA[numLayers];
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
        title = BorderFactory.createTitledBorder(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.title.regulaciones"));
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
		            	  
		            	  JLabel desc = new JLabel(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.regulaciones.descripcion"));
		            	  JLabel desc1 = new JLabel(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.regulaciones.descripcion1"));
		            	  JLabel desc2 = new JLabel(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.regulaciones.descripcion2"));
		            	  JLabel desc3 = new JLabel(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.regulaciones.descripcion3"));
		            	  JLabel desc4 = new JLabel(I18N.get("MigracionAsistidaLayers","gestorFip.migracionasistida.regulaciones.descripcion4"));
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
		            	  
		            	  add(desc4,  
                                  new GridBagConstraints(0, 4, 1, 1, 1, 0.001, GridBagConstraints.CENTER,
                                          GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0) ,0,0));
	
		            	  add(getSelectedAllButton(),  
                                  new GridBagConstraints(0, 5, 1, 1, 1, 0.001, GridBagConstraints.EAST,
                                          GridBagConstraints.NONE, new Insets(0,10,0,0) ,0,0));
		            	  
		            	  add(getTabbedPane(),  
                                  new GridBagConstraints(0, 6, 1, 1, 1, 1, GridBagConstraints.CENTER,
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
    
    
    public JTabbedPane getTabbedPane() {
		
    	if(tabbedPane == null){
			tabbedPane = new JTabbedPane();
			for(int i=0; i<lstAplicaLayer.length; i++){
				
				if(lstAplicaLayer[i]){
					lstTableRenderer[i] = new TableRenderRegulacionesMA(lstConfLayerBean[i].getLstConfUsosRegulaciones(), application);
					lstTableRenderer[i].setOpaque(true); //content panes must be opaque
					tabbedPane.addTab(I18N.get("MigracionAsistidaLayers",lstConfLayerBean[i].getNameLayer()), 
						null, lstTableRenderer[i]);
				}
			}
		}
    	
		return tabbedPane;
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
        
        this.lstConfLayerBean =  (ConfLayerBean[]) application.getBlackboard().get(ConstantesGestorFIP.GESTORFIP_DATA_MIGRACION_ASISTIDA);
        
        for(int i=0; i< lstAplicaLayer.length; i++){
        	if(lstAplicaLayer[i]){
        		lstTableRenderer[i].setLstUsosRegulaciones(this.lstConfLayerBean[i].getLstConfUsosRegulaciones());
        		lstTableRenderer[i].getTable().removeAll();
        		lstTableRenderer[i].getTableModel().initData();
        	}
        }
    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    	boolean validate =  validateData();
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
    		if(confLayer!= null && confLayer.getLstConfUsosRegulaciones() != null && 
    				confLayer.getLstConfUsosRegulaciones()[0] != null){
    			this.lstAplicaLayer[i]= true;	
    		}
    		else{
    			this.lstAplicaLayer[i]= false;
    		}
		}
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
  		
  		boolean validateDataRegulacionesPanel = true;
  		boolean validateRegulacionesLayer[] = new boolean[numLayers];
  		for(int i=0; i<validateRegulacionesLayer.length; i++){
  			validateRegulacionesLayer[i] = true;
  		}
  		
  		for(int i=0; i<lstAplicaLayer.length; i++){
  			if(lstAplicaLayer[i]){
  				 for (int h = 0; h < lstTableRenderer[i].getData().length; h++) {
  					Object[] obj = lstTableRenderer[i].getData()[h];
  					if((Boolean)obj[TableRenderRegulacionesMA.POSCAMPOSEL]){
  						//elemento seleccionado
  						
  						 String alias = (String)obj[TableRenderValoresMA.POSCAMPOALIAS];
  						if("".equals(alias.toString())){
 							 validateRegulacionesLayer[i] = false;
 							 break;
 						}
  						else{
  							 //comprobar que los datos introducidos son correctos
  							 if(validateAlias((String)obj[TableRenderValoresMA.POSCAMPOALIAS])){
  								 if(validateRegulacionesLayer[i]){
  									validateRegulacionesLayer[i] = true;
  								 }
  							 }
  							 else{
  								validateRegulacionesLayer[i] = false;
  							 }

  						 }
  					}
  				 }
  			}
  		}
  		
  		StringBuffer txtMsgLayers = new StringBuffer();
  		for(int i=0; i<validateRegulacionesLayer.length; i++){
  			if(!validateRegulacionesLayer[i] && lstAplicaLayer[i]){			
  				txtMsgLayers.append("   *").append(lstConfLayerBean[i].getNameLayer()); 
  				validateDataRegulacionesPanel = false;
  			}
  		}
  		
  		if(!validateDataRegulacionesPanel){
  			StringBuffer txtMsgValidate = new StringBuffer();
  			txtMsgValidate.append(I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.validate.txtnovalida"))
  				.append("\n").append(I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.validate.alias.condiciones"))
  				.append("\n").append(I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.validate.alias.vacio"))
  				.append("\n").append("\n").append(I18N.get("MigracionAsistidaLayers", "gestorFip.migracionasistida.layers"))
  				.append(":").append(txtMsgLayers);
  				
  			JOptionPane.showMessageDialog(this,txtMsgValidate.toString());
  		}  				
  		return validateDataRegulacionesPanel;
  	}
  	
  	
	private void guardarDatos(){
  		
		for(int i=0; i<lstAplicaLayer.length; i++){
  			if(lstAplicaLayer[i]){
  				
  				for (int j=0; j<this.lstConfLayerBean[i].getLstConfUsosRegulaciones().length; j++){
  					// ponemos todos los usos a false para que luego solo esten en estado true los que
  					// tengan alguna regulaciÃ³n seleccionada.
  					this.lstConfLayerBean[i].getLstConfUsosRegulaciones()[j].setSelected(false);

					if (this.lstConfLayerBean[i].getLstConfUsosRegulaciones()[j].getLstRegulaciones() != null &&
								this.lstConfLayerBean[i].getLstConfUsosRegulaciones()[j].getLstRegulaciones()[0] != null){
						for (int t=0; t<this.lstConfLayerBean[i].getLstConfUsosRegulaciones()[j].getLstRegulaciones().length; t++){
							this.lstConfLayerBean[i].getLstConfUsosRegulaciones()[j].getLstRegulaciones()[t].setSelected(false);
						}
					}
  				}

  				 for (int h = 0; h < lstTableRenderer[i].getData().length; h++) {
  					Object[] obj = lstTableRenderer[i].getData()[h];
  					if((Boolean)obj[TableRenderRegulacionesMA.POSCAMPOSEL]){
  						//elemento seleccionado
  						
  						for (int j=0; j<this.lstConfLayerBean[i].getLstConfUsosRegulaciones().length; j++){
  						
  							String codigoRegula  = (String)obj[TableRenderRegulacionesMA.POSCAMPOCODIGO];
  							String codigoUso = (String)obj[TableRenderRegulacionesMA.POSCAMPOCODDETSEL];
  							if(this.lstConfLayerBean[i].getLstConfUsosRegulaciones()[j].getLstRegulaciones() != null &&
  									this.lstConfLayerBean[i].getLstConfUsosRegulaciones()[j].getLstRegulaciones()[0] != null){
								for (int t=0; t<this.lstConfLayerBean[i].getLstConfUsosRegulaciones()[j].getLstRegulaciones().length; t++){
							
	
									if(this.lstConfLayerBean[i].getLstConfUsosRegulaciones()[j].getLstRegulaciones()[t].getRegulacionValor().getCodigo().equals(codigoRegula)){
										// quedamos seleccionado el uso.
										this.lstConfLayerBean[i].getLstConfUsosRegulaciones()[j].setSelected(true);
										this.lstConfLayerBean[i].getLstConfUsosRegulaciones()[j].getLstRegulaciones()[t].setAlias((String)obj[TableRenderRegulacionesMA.POSCAMPOALIAS]);
										this.lstConfLayerBean[i].getLstConfUsosRegulaciones()[j].getLstRegulaciones()[t].setSelected(true);
										this.lstConfLayerBean[i].setAplicada(true);
									}
								}
  							}
  						}
  					}
  				 }
  			}
  		}

		blackboard.put(ConstantesGestorFIP.GESTORFIP_DATA_MIGRACION_ASISTIDA, this.lstConfLayerBean);
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
    
}  
