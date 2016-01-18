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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import reso.jump.joinTable.JoinTable;

import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.plugin.Constantes;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author jvaca
 * Modificaciones rubengomez
 *
 */


public class FileGeoreferencePanel01 extends javax.swing.JPanel implements WizardPanel
{
    //Variables del plugin joinTable INICIO
    protected static String name = "JoinTable"; 
    public static FileFilter JOIN_TABLE_FILE_FILTER = null; 
    private Layer layer;
    private JFileChooser fc = new JFileChooser();
    private MultiInputDialog dialog;
    private String LAYER_ATTRIBUTES = null;
    private String TABLE_ATTRIBUTES = null;
    private int interruptor=0;
    //FIN
    
    List fieldTypes =new ArrayList();
    private String localId = null;
    private String nextID = null;  //  @jve:decl-index=0:
    private PlugInContext   context;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard  = aplicacion.getBlackboard();
    private WizardContext wizardContext;  //  @jve:decl-index=0:
    private JTextField txtfileName = null;
    private JButton cmdOpen = null;
    private GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
    private String DELIMITADORES = ",|;|\t";  //  @jve:decl-index=0:
    private String DEFAULT_DELEM = "\t";
    private int fieldCount = 0;
    private JTextPane txtPanelDebug = null;
    private JScrollPane jScrollPanel = null;
    private JComboBox cmbLayerName = null;  //  @jve:decl-index=0:visual-constraint="539,17"
    private JLabel jLabelLayer = null;
	private JLabel jLabelResults = null;
	private AppContext aplicacion1 = null;  //  @jve:decl-index=0:
	private JPanel jPanelInfo = null;
	private JPanel layerFilePanel = null;
	private JPanel jPanelLayer = null;
	private JPanel jPanelFile = null;
    
    public FileGeoreferencePanel01(String id, String nextId, PlugInContext context2) {
    	
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);        
        
    	this.context=context2;
        this.nextID = nextId;
        this.localId = id;
        try
        {
        setName(I18N.get("Georreferenciacion", "georeference.panel01.titlePanel"));
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

		this.add(getLayerFilePanel(), 
				new GridBagConstraints(0,1,1,1, 1, 0.95,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    }
    
    private JPanel getLayerFilePanel(){

		if (layerFilePanel == null){

			layerFilePanel = new JPanel(new GridBagLayout());
	        
	        jLabelLayer = new JLabel();
	        jLabelLayer.setText(I18N.get("Georreferenciacion", "georeference.panel01.layerList"));

			layerFilePanel.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("Georreferenciacion","georeference.panel01.SelectLayerFile"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			layerFilePanel.add(getJPanelLayer(),
	      			  new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.WEST,
	      					  GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));
						
			layerFilePanel.add(getJPanelFile(), 
					new GridBagConstraints(0,1,1,1,0.1, 0.1,GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));
						
			layerFilePanel.add(jLabelResults,
	      			  new GridBagConstraints(0,2,1,1, 1.0, 0.1,GridBagConstraints.WEST,
	      					  GridBagConstraints.NONE, new Insets(5,20,0,5),0,0));

			layerFilePanel.add(getJScrollPanel(), 
					new GridBagConstraints(0,3,3,3,1.0, 1.0,GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(10,10,10,10),0,0));

		}
		return layerFilePanel;
	}
    
    public JPanel getJPanelLayer(){
    	
    	if (jPanelLayer == null){
    		
    		jPanelLayer  = new JPanel();
    		jPanelLayer.setLayout(new GridBagLayout());
    		
    		jLabelResults = new JLabel();
	        jLabelResults.setText(I18N.get("Georreferenciacion","georeference.panel01.layerText"));
	        
    		jPanelLayer.add(jLabelLayer,
					  new GridBagConstraints(0,0,1,1, 0.3, 0.1,GridBagConstraints.CENTER,
							  GridBagConstraints.NONE, new Insets(5,20,0,5),0,0));
			
    		jPanelLayer.add(getCmbLayerName(),
					new GridBagConstraints(1,0,1,1,0.7, 0.1,GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(0,0,0,30),0,0));
    		
    	}
    	return jPanelLayer;
    }
    
    public JPanel getJPanelFile(){
    	
    	if (jPanelFile == null){
    		
    		jPanelFile   = new JPanel();
    		jPanelFile.setLayout(new GridBagLayout());
    		
    		jPanelFile.add(cmdOpen(),
					  new GridBagConstraints(0,0,1,1, 0.2, 0.1,GridBagConstraints.CENTER,
							  GridBagConstraints.NONE, new Insets(5,20,0,5),0,0));
			
    		jPanelFile.add(getTxtfileName(),
					new GridBagConstraints(1,0,1,1,0.8, 0.1,GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(5,0,0,15),0,0));
    		
    	}
    	return jPanelFile;
    }
    
    public JPanel getJPanelInfo(){

		if (jPanelInfo == null){

			jPanelInfo  = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("Georreferenciacion","georeference.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("Georreferenciacion","georeference.Info1"));
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
        String select =(String)wizardContext.getData("Select00");

        cmbLayerName.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                wizardContext.inputChanged();
            }
        });
        
    }
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        GeopistaLayer capa=(GeopistaLayer)getCmbLayerName().getSelectedItem();
        wizardContext.setData("layer",capa);
        interruptor=1;
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
        if(interruptor!=1){
            this.remove(cmbLayerName);
        }
        
        txtPanelDebug.setText("");
        txtfileName.setText("");
        cmbLayerName.setSelectedIndex(0);
    }
    
    public String getTitle()
    {
      return this.getName();
    }
    public String getInstructions()
    {
        return I18N.get("Georreferenciacion", "georeference.panel01.instructions");
    }
    /**
     * Este metodo valida y activa o no el boton de next
     */
    public boolean isInputValid()
    {
        List fieldNames=new ArrayList();
        List errores=new ArrayList();
        JoinTable jt =(JoinTable)wizardContext.getData("JoinTableObjet");
        errores =(List)wizardContext.getData("errores");
        String texto=null;
        String textoerror=null;
        if(jt==null){
            return false;
        }else{
            fieldNames=jt.getFieldNames();
        }
        if(fieldNames.size()!=0 && errores==null){
            if(!getCmbLayerName().getSelectedItem().equals("")){
                if(getCmbLayerName().getSelectedItem().equals(I18N.get("Georreferenciacion", "georeference.panel01.emptyLayer"))){
                    return false;
                }
                if(txtfileName.getText().equals("")){
                    return false;
                }
                GeopistaLayer capa=(GeopistaLayer)getCmbLayerName().getSelectedItem();
                
              if(capa.isLocal()){
                  //se pueden añadir valores y atributos sin problemas
                  getTxtpaneldebug().setText(I18N.get("Georreferenciacion", "georeference.panel01.correctFile"));
                  return true;
              }else{
                  //solo acepta atributos ya existentes dentro de la layer no hace join
                  boolean check=checkLayer(capa,fieldNames,fieldTypes);
                  
                  if(!check){
                      capa.setEditable(true);
                      getTxtpaneldebug().setText(I18N.get("Georreferenciacion", "georeference.panel01.correctFile")+ "\n\n" + I18N.get("Georreferenciacion","georeference.panel01.selectedLayer")+
                              "\n"
                              +I18N.get("Georreferenciacion","georeference.panel01.errorLayer"));
                  }else{
                      
                      getTxtpaneldebug().setText(I18N.get("Georreferenciacion","georeference.panel01.selectedLayer")+
                              "\n"
                              +I18N.get("Georreferenciacion", "georeference.panel01.correctFile"));
                  }
                  
                  return check;
              }
                
            }else{
                return false;
            }
        }else{
            if(errores !=null){
                getTxtpaneldebug().setText("");
                textoerror=I18N.get("Georreferenciacion", "georeference.panel01.badFile")+"\n";
                textoerror=textoerror +fc.getSelectedFile().getAbsolutePath()+"\n";
                for(int i=0;i<errores.size();i++){
                    textoerror = textoerror +((String) errores.get(i)) + "\n";
                }      
                getTxtpaneldebug().setText(textoerror);
                errores=null;
            }else{            
                getTxtpaneldebug().setText(I18N.get("Georreferenciacion", "georeference.panel01.badFile"));
                texto=getTxtpaneldebug().getText();
                getTxtpaneldebug().setText(texto+ "\n" + I18N.get("Georreferenciacion","georeference.panel01.fileDataEmpty"));
            }
           return false;  
        }
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
     * Método qu egenera un explorador de archivos y valida el fichero
     * @param e
     */
    private void btnAbrir_actionPerformed(ActionEvent e)
    {
    	
        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        filter.addExtension("txt");
        filter.addExtension("csv");
        filter.setDescription(I18N.get("Georreferenciacion","georeference.panel01.fileFilter"));
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(true); 

        File currentDirectory = (File) blackboard.get(Constantes.LAST_IMPORT_DIRECTORY);
        fc.setCurrentDirectory(currentDirectory);
        int returnVal = fc.showOpenDialog(this);
        blackboard.put(Constantes.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory());
        if (returnVal != JFileChooser.APPROVE_OPTION)
            return;
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), 
                geopistaEditor1.getContext().getErrorHandler());

        progressDialog.setTitle(I18N.get("georreferenciacion","georeference.panel01.validateData"));
        progressDialog.report(I18N.get("georreferenciacion","georeference.panel01.validateData"));
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
                                    getTxtfileName().setText(fc.getSelectedFile().getAbsolutePath());
                                    JoinTable jt = new JoinTable(fc.getSelectedFile().getAbsolutePath());
                                    wizardContext.setData("path",fc.getSelectedFile().getAbsolutePath());
                                    //comprobamos los posibles fallos del fichero
                                    List errores=findErrorFile(jt,fc.getSelectedFile().getAbsolutePath());
                                    //Sacamos los atributos de la cabecera
                                    if(errores.size()==0){
                                        wizardContext.setData("JoinTableObjet",jt);
                                        getTxtpaneldebug().setText(I18N.get("Georreferenciacion","georeference.panel01.correctFile"));
                                    }else{
                                        wizardContext.setData("JoinTableObjet",jt);
                                        wizardContext.setData("errores",errores);
                                    }
                                    
                                } catch (Exception e)
                                {
                                    String texto= I18N.get("Georreferenciacion","georeference.panel01.badFile")+ "\n"+e.getMessage();
                                    getTxtpaneldebug().setText(texto);
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

        wizardContext.inputChanged();
    }

    /**
     * This method initializes txtfileName	
     * 	
     * @return javax.swing.JTextArea	
     */
    private JTextField getTxtfileName()
    {
        if (txtfileName == null)
        {
            txtfileName = new JTextField();

        }
        return txtfileName;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton cmdOpen()
    {
        if (cmdOpen == null)
        {
            cmdOpen = new JButton();
            cmdOpen.setIcon(IconLoader.icon("abrir.gif"));
            
            cmdOpen.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            btnAbrir_actionPerformed(e);
                        }
                    });
        }
        return cmdOpen;
    }
    
    /**
     * This method initializes jTextPane	
     * 	
     * @return javax.swing.JTextPane	
     */
    private JTextPane getTxtpaneldebug()
    {
        if (txtPanelDebug == null)
        {
            txtPanelDebug = new JTextPane();
            txtPanelDebug.setEnabled(false);
            txtPanelDebug.setBounds(new java.awt.Rectangle(25,114,381,155));
        }
        return txtPanelDebug;
    }
    /**
     * This method initializes jScrollPanel	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPanel()
    {
        if (jScrollPanel == null)
        {
            jScrollPanel = new JScrollPane(getTxtpaneldebug());
            jScrollPanel.setAutoscrolls(true);
            jScrollPanel.createHorizontalScrollBar();
            
            
        }
        return jScrollPanel;
    }
    /**
     * This method initializes cmbLayerName	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getCmbLayerName()
    {
        if(cmbLayerName==null){
            List layers=new ArrayList();
            layers=context.getLayerManager().getLayers();
            layers.add(0,"");
            
            if(layers!=null){
                List capas=fullData(layers);
                cmbLayerName = new JComboBox(new Vector(capas));
            }else{
                cmbLayerName = new JComboBox();
            }
        }
        return cmbLayerName;
    }
    /**
     * Método que encuentra posibles fallos dentro de el archivo de datos
     * y almacena los datos del archivo en un ArrayList de vectores. y en un 
     * JoinTable.
     * El JoinTable servirá sólo para el caso de completar capas locales.
     * @param jt
     */
    public List findErrorFile(JoinTable jt,String filePath){
        
        int nl=2;
        int nbCol;
        List errores =new ArrayList();
        String s, line;
        String[] val;
        ArrayList datosComprobados = new ArrayList();
        try{
        	int NumCampos=((List)jt.getFieldNames()).size();
            FileReader fileReader = new FileReader(filePath);
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            line= bufferedReader.readLine();
            //Nos interesa la segunda linea
            line= bufferedReader.readLine();
            if(line==null){
                //error de que no existen datos
                String error=I18N.get("Georreferenciacion","georeference.panel01.fileDataEmpty");
                errores.add(error);
                return errores;
            }  
            while (line!=null) {
                line=line.trim();
                //Vector linea = new Vector();
                val = line.split(DELIMITADORES);
                nbCol=val.length;
                Hashtable valor = new Hashtable();
    
                if ((nbCol)<NumCampos){
                    String error=I18N.get("Georreferenciacion", "georeference.panel01.fileLineError")+ " ( " + nl + " ) "+ I18N.get("Georreferenciacion", "georeference.panel01.fileLineLess") ;
                    errores.add(error);
                }else if((nbCol)>NumCampos){
                    String error=I18N.get("Georreferenciacion", "georeference.panel01.fileLineError")+ " ( " + nl + " ) "+ I18N.get("Georreferenciacion", "georeference.panel01.fileLineMore");
                    errores.add(error);
                }
                if(errores.size()==0){
                    for (int i=0; i<nbCol; i++) {
                        s = (String) val[i]; 
                        //linea.add(s);
                        
                       	valor.put(jt.getFieldName(i),val[i]);                        
                        // mise a jour du type du champ
                        if ((i+1)>fieldTypes.size())
                            fieldTypes.add(i,typeOfData(s));
                        else {
                            AttributeType newFieldType = typeOfData(s);
                            AttributeType fieldType = (AttributeType) fieldTypes.get(i);
                            if  (newFieldType!=fieldType)
                            {
                                if (newFieldType == AttributeType.STRING)
                                    fieldTypes.set(i,newFieldType);
                                else if (fieldType!= AttributeType.STRING && newFieldType==AttributeType.DOUBLE)
                                    fieldTypes.set(i,newFieldType);
                            }
                        }
                        
                    }
                }

                datosComprobados.add(valor);
                line= bufferedReader.readLine();
                nl++;
            }
            wizardContext.setData("datos",datosComprobados);
        }
        catch(Exception e) {

        }         
        return errores;
        
    }
    /**
     * método que indica el tipo de datos del valor recibido como string
     * @param s
     * @return
     */
    private AttributeType typeOfData(String s)
    {
        s=s.trim();
        AttributeType res = AttributeType.STRING;
        if (s.length()==0)
            res = AttributeType.STRING;
        else {
            try{
                //Lo primero que hacemos es intentarlo convertir a fecha

                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
                DateFormat d =sdf.getDateInstance();
                //DateFormat d = DateFormat.getDateInstance(DateFormat.MEDIUM);
                sdf.parse(s);
                //d.parse(s);
                //de no saltar la excepcion le pondriamos como fecha
                res = AttributeType.DATE;
            }catch(Exception e){
                try{
                    if (s.indexOf('.')== -1 && s.indexOf(',')== -1){
                        if (s.length()< 10){
                            new Integer(s);
                            res = AttributeType.INTEGER;
                        }else{
                            new Double(s);
                            res = AttributeType.DOUBLE; 
                        }
                    }else{
                        new Double(s);
                        res = AttributeType.DOUBLE;
                    }
                }catch(Exception f){
                    res = AttributeType.STRING;
                }
            }
            
        }
        return res; 
    }
   
    /**
     * Método retirado, pero puede servir para mas adelante
     * Método que compara los campos del fichero con los atributos de la capa de sistema 
     * Estructura del algoritmo:
     * 	Inicialmente comprueba que el número de campos del archivo sea igual o mayor
     * 	al número de campos de la capa de sistema.
     * 
     * 	Recorre todos los campos 
     * 
     * @param capa capa de sistema
     * @param names Lista de nombres de campos del fichero
     * @param types lista de tipos de campos del fichero
     * @return true si todos los campos de la capa se encuentran en los campos 
     * 		   del fichero, y false en el resto de los casos
     */
    public boolean checkLayer(GeopistaLayer capa,List names,List types){

    	return true;
    }
    /**
     *      * @param layers
     */
    public List  fullData(List layers){
        List capaDatos=new ArrayList();
        
        for(int i=0;i<layers.size();i++){
            if(!layers.get(i).equals("")){
                GeopistaLayer capa=(GeopistaLayer)layers.get(i);
                FeatureSchema schema=(FeatureSchema)capa.getFeatureCollectionWrapper().getFeatureSchema();
                //si queremos que solo lleguen capas que tengan datos pondremos en el if la condicion de abajo
                //|| capa.getFeatureCollectionWrapper().getFeatures().size()!=0
                if(schema.getAttributeCount()>=1 ){
                    capaDatos.add(capa);
                }
            }
        }
        if(capaDatos.size()==0){
            capaDatos.add(I18N.get("Georreferenciacion", "georeference.panel01.emptyLayer"));
        }else{
            capaDatos.add(0,"");
        }
        return capaDatos;
    }
	/**
	 * This method initializes aplicacion1	
	 * 	
	 * @return com.geopista.app.AppContext	
	 */

    
}  //  @jve:decl-index=0:visual-constraint="8,10"
