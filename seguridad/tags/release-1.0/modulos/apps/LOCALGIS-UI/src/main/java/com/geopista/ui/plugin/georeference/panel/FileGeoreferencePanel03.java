package com.geopista.ui.plugin.georeference.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import reso.jump.joinTable.JoinTable;
import com.geopista.app.AppContext;
import com.geopista.app.utilidades.EdicionUtils;
import com.geopista.editor.GeopistaEditor;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
/**
 * 
 * @author jvaca
 * Ventana accesible desde completar. Selecciona los campos que relacionan
 * la actualización de features con el fichero
 *
 */

public class FileGeoreferencePanel03 extends javax.swing.JPanel implements WizardPanel
{
    
    private String localId = null;
    private String nextID = null;
    private PlugInContext   context;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard  = aplicacion.getBlackboard();
    private GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
    private WizardContext wizardContext;
    private ButtonGroup bgOrden= new ButtonGroup();
    private JLabel lblcapa = null;
    private JLabel lblcsvFile = null;
    private JComboBox cmbLayer = null;
    private JComboBox cmbcsvFile = null;
    private FileGeoreferencePanel02 panel=null;
    
    private JoinTable jt =null;
    private Layer capa=null;
	private JPanel jPanelInfo = null;
	private JPanel jPanelDatos = null;
    
    public FileGeoreferencePanel03(String id, String nextId, PlugInContext context2) {
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);
        this.context=context2;
        this.nextID = nextId;
        this.localId = id;
        try
        {
            setName(I18N.get("Georreferenciacion","georeference.panel03.titlePanel"));
            initialize();
        }catch(Exception e)
        {
            e.printStackTrace();
        }   
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
    	
    	this.setLayout(new GridBagLayout());
    	this.setSize(new Dimension(600, 550));
    	this.setPreferredSize(new Dimension(600, 550));
    	       
    	
    	this.add(getJPanelInfo(), 
				new GridBagConstraints(0,0,1,1, 1, 0.05,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

		this.add(getJPanelDatos(), 
				new GridBagConstraints(0,1,1,1, 1, 0.95,GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
		    		
    }
    
    private JPanel getJPanelDatos(){
    	
    	if (jPanelDatos == null){
    		
    		jPanelDatos  = new JPanel();
    		jPanelDatos.setLayout(new GridBagLayout());
    		
    		jPanelDatos.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("Georreferenciacion","georeference.panel02.selectreferences"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
    		
    		lblcsvFile = new JLabel();
            lblcsvFile.setText(I18N.get("Georreferenciacion", "georeference.panel03.dataFileLabel"));
            
            lblcapa = new JLabel();
            lblcapa.setText(I18N.get("Georreferenciacion", "georeference.panel03.attributesLayerLabel"));
               		    		
            jPanelDatos.add(lblcapa,
	      			  new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
	      					  GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
          
            jPanelDatos.add(getCmbLayer(),
	      			  new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.CENTER,
	      					  GridBagConstraints.HORIZONTAL, new Insets(0,0,0,20),0,0));
          
            jPanelDatos.add(lblcsvFile,
	      			  new GridBagConstraints(0,1,1,1, 1, 1,GridBagConstraints.NORTH,
	      					  GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
          
            jPanelDatos.add(getCmbcsvFile(),
	      			  new GridBagConstraints(1,1,1,1, 1, 1,GridBagConstraints.NORTH,
	      					  GridBagConstraints.HORIZONTAL, new Insets(0,0,0,20),0,0));
    		
    	}
    	return jPanelDatos;
    }
    
    private JPanel getJPanelInfo(){

		if (jPanelInfo == null){

			jPanelInfo    = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("Georreferenciacion","georeference.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("Georreferenciacion","georeference.panel03.instructions"));
			jTextAreaInfo.setEnabled(false);
			jTextAreaInfo.setFont(new JLabel().getFont());
			jTextAreaInfo.setOpaque(false);
			jTextAreaInfo.setDisabledTextColor(Color.black);
			jTextAreaInfo.setEditable(false);
			jTextAreaInfo.setWrapStyleWord(true);
			jPanelInfo.add(jTextAreaInfo);
		}
		return jPanelInfo;
	}
    
    /**
     * Called when the user presses Next on this panel's previous panel
     */
    public void enteredFromLeft(Map dataMap)
    { 
        this.jt=(JoinTable) wizardContext.getData("JoinTableObjet");
        this.capa=(Layer)wizardContext.getData("layer");
        if(this.jt!=null){
        	
        	List lstCsvFile = new ArrayList();
        	lstCsvFile = jt.getFieldNames();
        	EdicionUtils.cargarLista(getCmbcsvFile(), (ArrayList)lstCsvFile);
        	
        	List lstAtrib=new ArrayList();
            for(int i=0;i<this.capa.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount();i++){
                lstAtrib.add(this.capa.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
            }
            EdicionUtils.cargarLista(getCmbLayer(), (ArrayList)lstAtrib);
        	            
        }  
    }
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {   
        wizardContext.setData("georeference",this);
        wizardContext.setData("completar", "1");
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), 
                null);

        progressDialog.setTitle(I18N.get("Georreferenciacion", "georeference.panel03.progressDialog.dataProcessLabel"));
        progressDialog.report(I18N.get("Georreferenciacion", "georeference.panel03.progressDialog.dataProcessLabel"));
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
                                    FileGeoreferencePanel03 fgp=(FileGeoreferencePanel03)wizardContext.getData("georeference");
                                    JoinTable jt=(JoinTable)wizardContext.getData("JoinTableObjet");
                                    //pasamos a JT el indice de la tabla donde estael campo por el que queremos hacer la join
                                    jt.setKeyIndex(fgp.cmbcsvFile.getSelectedIndex());
                                    jt.build();
                                    
                                    String campoCapaSeleccionado = (String)fgp.cmbLayer.getSelectedItem();
                                    String campoSeleccionado = (String)fgp.cmbcsvFile.getSelectedItem();
                                    wizardContext.setData("indexFichero",campoSeleccionado);
                                    wizardContext.setData("indexCapa",campoCapaSeleccionado);
                                    wizardContext.setData("indiceCapa",new Integer(fgp.cmbLayer.getSelectedIndex()));
                                    wizardContext.setData("JoinTableObjet",jt);
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }finally
                                {
                                    progressDialog.setVisible(false);
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        this.nextID = "6";
    }
    /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {
        
    }
    public void remove(InputChangedListener listener)
    {
    }

    public String getTitle()
    {
      return I18N.get("Georreferenciacion","georeference.panel03.title");
    }
    public String getInstructions()
    {
        return I18N.get("Georreferenciacion","georeference.panel03.instructions");
    }
    /**
     * Este metodo valida y activa o no el boton de next
     */
    public boolean isInputValid()
    {
       boolean validado=false;
       validado= true;
       return validado;
    }
    public void setWizardContext(WizardContext wd)
    {
        wizardContext =wd;
    }
    public String getID()
    {
      return localId;
    }
    public void setNextID(String nextID)
    {
        this.nextID=nextID;
    }
    public String getNextID()
    {
      return nextID;
    }
    public void exiting()
    {
        
    }

    /**
     * This method initializes cmbDireccion	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getCmbLayer()
    {
    	if (cmbLayer == null)
    	{           
    		cmbLayer = new JComboBox();

    		cmbLayer.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
    				wizardContext.inputChanged();
    			}
    		});
    	}
    	return cmbLayer;
    }
    /**
     * This method initializes cmbCalle	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getCmbcsvFile()
    {
    	if (cmbcsvFile == null)
    	{            
    		cmbcsvFile = new JComboBox();

    		cmbcsvFile.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
    				wizardContext.inputChanged();
    			}
    		});
    	}
    	return cmbcsvFile;
    }
}  //  @jve:decl-index=0:visual-constraint="19,7"
