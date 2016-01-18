package com.geopista.ui.plugin.georreferenciacionExterna.paneles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.georreferenciacionExterna.ConsultaDatosBean;
import com.geopista.ui.plugin.georreferenciacionExterna.utils.UbicacionListCellRenderer;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.localgis.ws.servidor.SQLExceptionException0;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.ws.cliente.Test;

public class GeorreferenciacionExternaPanel02 extends javax.swing.JPanel implements WizardPanel
{
    private String localId = null;
    private String nextID = null;
    private PlugInContext   context;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private WizardContext wizardContext;
    private JRadioButton radioRefCat = null;
    private JRadioButton radioXY = null;
    private JRadioButton radioPostal = null;
    
    private JRadioButton radioGeomPoint = null;
    private JRadioButton radioGeomOrig = null;
    private JRadioButton radioGeomTabla = null;
    private JComboBox comboGeomTabla = null;
       
	private JPanel jPanelInfo = null;
	private JPanel jPanelReferences = null;
	private JPanel jPanelDatos = null;
	private JPanel jPanelGeometriContenida = null;
	private JPanel jPanelGeomCont = null;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private ButtonGroup buttonGroupGeom = new ButtonGroup();
	
	Hashtable listaTablasHashtable = new Hashtable();
	private ConsultaDatosBean consultaSelected = null;
	
	private boolean estado;
    
    public GeorreferenciacionExternaPanel02(String id, String nextId, PlugInContext context2, ConsultaDatosBean consultaSelected ) {
        
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georreferenciacionExterna.languages.GeorreferenciacionExternai18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("GeorreferenciacionExterna",bundle);
        this.context=context2;
        this.nextID = nextId;
        this.localId = id;
        this.consultaSelected  = consultaSelected;
        
        try
        {   
	        setName(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel02.titlePanel"));     	
            initialize();
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }   
    }

    /**
     * This method initializes this
     * @throws SQLExceptionException0 
     * 
     */
    private void initialize() {
        
    	this.setLayout(new GridBagLayout());
    	this.setSize(new Dimension(600, 550));
    	this.setPreferredSize(new Dimension(600, 550));
    	       
    	
    	this.add(getJPanelInfo(), 
				new GridBagConstraints(0,0,1,1, 1, 0.1,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

		this.add(getJPanelDatos(), 
				new GridBagConstraints(0,1,1,1, 1, 0.1,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
		
		this.add(getJPanelGeometriContenida(), 
				new GridBagConstraints(0,2,1,1, 1, 0.1,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    	    			
    }
    
    private JPanel getJPanelGeometriContenida(){
    	
    	if (jPanelGeometriContenida == null){   
    		
    		jPanelGeometriContenida = new JPanel();
    		jPanelGeometriContenida.setLayout(new GridBagLayout());
    		
    		jPanelGeometriContenida.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel02.selectGeometria"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
    		
    		jPanelGeometriContenida.add(getJPanelGeomCont(), 
    				new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
    						GridBagConstraints.BOTH, new Insets(10,10,10,10),0,0));
    		
    	}
    	
    	return jPanelGeometriContenida;
    }
    
    private JPanel getJPanelGeomCont(){
    	
    	if (jPanelGeomCont == null){
    		
    		jPanelGeomCont  = new JPanel();
    		jPanelGeomCont.setLayout(new GridBagLayout());
    		
    		jPanelGeomCont.add(getRdbGeomPoint(), 
    				new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
    						GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10),0,0));
    		jPanelGeomCont.add(getRdbGeomOrig(), 
    				new GridBagConstraints(0,1,1,1, 0.1, 0.1,GridBagConstraints.WEST,
    						GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10),0,0));
    		jPanelGeomCont.add(getRdbGeomTabla(), 
    				new GridBagConstraints(0,2,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
    						GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10),0,0));
    		jPanelGeomCont.add(getComboGeomTabla(), 
    				new GridBagConstraints(1,3,1,1, 0.99, 0.1,GridBagConstraints.NORTHWEST,
    						GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10),0,0));
    		
    	}
    	return jPanelGeomCont;
    }
    
    private JPanel getJPanelDatos(){
    	
    	if (jPanelDatos == null){    		
    		jPanelDatos  = new JPanel();
    		jPanelDatos.setLayout(new GridBagLayout());
    		
    		jPanelDatos.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel02.selectreferences"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
    		
    		jPanelDatos.add(getJPanelReferences(), 
    				new GridBagConstraints(0,0,1,1, 0.8, 0.8,GridBagConstraints.NORTHWEST,
    						GridBagConstraints.BOTH, new Insets(10,10,10,10),0,0));
    	}
    	return jPanelDatos;
    }
    
    private JPanel getJPanelReferences(){
    	
    	if (jPanelReferences == null){
    		
    		jPanelReferences  = new JPanel();
    		jPanelReferences.setLayout(new GridBagLayout());
    		
    		jPanelReferences.add(getRdbRefCat(), 
    				new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
    						GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10),0,0));
    		jPanelReferences.add(getRadioXY(), 
    				new GridBagConstraints(0,1,1,1, 0.1, 0.1,GridBagConstraints.WEST,
    						GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10),0,0));
    		jPanelReferences.add(getRadioPostal(), 
    				new GridBagConstraints(0,2,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
    						GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10),0,0));
    	}
    	return jPanelReferences;
    }
    
    private JPanel getJPanelInfo(){

		if (jPanelInfo == null){

			jPanelInfo   = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel02.instructions"));
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
    	if(this.consultaSelected != null){
    		// es la modificacion de la consulta
    		rellenarDatosPanel();
    		
    	}
    }
    
    private void rellenarDatosPanel(){

    	if(this.consultaSelected.getMetodo_georeferencia().equals("Referencia Catastral")){
    		radioRefCat.setSelected(true);
    	}
    	else if(this.consultaSelected.getMetodo_georeferencia().equals("Coordenadas X,Y")){
    		radioXY.setSelected(true);
    	}
    	else if(this.consultaSelected.getMetodo_georeferencia().equals("Dirección Postal")){
    		radioPostal.setSelected(true);
    	}
 
    	
    	if(this.consultaSelected.getTipo_geometria().equals("Mostrar Puntos")){
    		radioGeomPoint.setSelected(true);
    	}
    	else if(this.consultaSelected.getTipo_geometria().equals("Mostrar Geometría Original")){
    		radioGeomOrig.setSelected(true);
    	}
    	else if(this.consultaSelected.getTipo_geometria().equals("Mostrar Geometría Tabla")){
    		radioGeomTabla.setSelected(true);
    		for(int i=0; i<comboGeomTabla.getItemCount(); i++){
    			String valorCombo = (String)comboGeomTabla.getItemAt(i);
    			if(valorCombo.equals(this.consultaSelected.getTabla_cruce())){
    				comboGeomTabla.setSelectedIndex(i);
    			}
    		}
    	}
    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {   
    	if (radioRefCat.getSelectedObjects()!=null){
    		wizardContext.setData("tipo",radioRefCat.getText());
    	}else if (radioXY.getSelectedObjects()!=null){
    		wizardContext.setData("tipo",radioXY.getText());
    	}else if (radioPostal.getSelectedObjects()!=null){
    		wizardContext.setData("tipo",radioPostal.getText());
    	}else{
    		wizardContext.setData("tipo","");
    	}
    	
    	if(radioGeomPoint.getSelectedObjects()!=null){
    		wizardContext.setData("tipogeom",radioGeomPoint.getText());
    	}
    	else if(radioGeomOrig.getSelectedObjects()!=null){
    		wizardContext.setData("tipogeom",radioGeomOrig.getText());
    	}
    	else if(radioGeomTabla.getSelectedObjects()!=null){
    		wizardContext.setData("tipogeom",radioGeomTabla.getText());
    		
    		if(comboGeomTabla.getSelectedObjects()!=null){
    			wizardContext.setData("tablatipogeom",(String)comboGeomTabla.getSelectedItem());
    		}
    	}
    	
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
    	isInputValidNew(true);
    }

    public String getTitle()
    {
      return this.getName();
    }
    public String getInstructions()
    {
        return I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel02.instructions");
    }
   
    /**
     * Este metodo valida y activa o no el boton de next
     */
    public boolean isInputValid()
    {
    	boolean validado=true;   	
    	
    	return validado;
    } 
    
    public void isInputValidNew(boolean estado_boton)
    {
    	this.estado=estado_boton;    	
    	isInputValid();
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
     * This method initializes cmbCalle	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JRadioButton getRdbGeomPoint()
    {  
    	if ( radioGeomPoint== null)
        {
    		radioGeomPoint = new JRadioButton();
    		buttonGroupGeom.add(radioGeomPoint);
    		radioGeomPoint.setLocation(new Point(238, 177));
    		radioGeomPoint.setMnemonic(KeyEvent.VK_UNDEFINED);
    		radioGeomPoint.setSize(new java.awt.Dimension(21,21));
    		radioGeomPoint.setText("Mostrar Puntos");
    		radioGeomPoint.setSelected(true);
    		radioGeomPoint.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                	//comprobarEstadoPanel();
                	if(radioGeomPoint.isSelected()){                        
                        comboGeomTabla.setEnabled(false);
                    }
                }
            });
        }
        return radioGeomPoint;
    }
    
    /**
     * This method initializes cmbCalle	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JRadioButton getRdbGeomOrig()
    {
    	if (radioGeomOrig == null)
        {
    		radioGeomOrig = new JRadioButton();
    		buttonGroupGeom.add(radioGeomOrig);
    		radioGeomOrig.setLocation(new Point(238, 177));
    		radioGeomOrig.setMnemonic(KeyEvent.VK_UNDEFINED);
    		radioGeomOrig.setSize(new java.awt.Dimension(21,21));
    		radioGeomOrig.setText("Mostrar Geometría Original");
    		radioGeomOrig.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                	//comprobarEstadoPanel();
                	 if(radioGeomOrig.isSelected()){                        
                         comboGeomTabla.setEnabled(false);
                     }
                }
            });
        }
        return radioGeomOrig;
    }
    
    /**
     * This method initializes cmbCalle	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JRadioButton getRdbGeomTabla()
    {
    	if (radioGeomTabla == null)
        {
    		radioGeomTabla = new JRadioButton();
    		buttonGroupGeom.add(radioGeomTabla);
    		radioGeomTabla.setLocation(new Point(238, 177));
    		radioGeomTabla.setMnemonic(KeyEvent.VK_UNDEFINED);
    		radioGeomTabla.setSize(new java.awt.Dimension(21,21));
    		radioGeomTabla.setText("Mostrar Geometría Tabla");
    		radioGeomTabla.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                	//comprobarEstadoPanel();
                    if(radioGeomTabla.isSelected()){                        
                        comboGeomTabla.setEnabled(true);
                    }
                }
            });
        }
        return radioGeomTabla;
    }
    
    
    /**
     * This method initializes cmbCalle	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getComboGeomTabla()
    {
    	if(comboGeomTabla == null){
    		try {
    			
    			listaTablasHashtable.putAll(Test.obtenerTablasBBDDLocalGis());
    			
    			comboGeomTabla = new JComboBox();
        		comboGeomTabla.setEnabled(false);
        		comboGeomTabla.setRenderer(new UbicacionListCellRenderer());  
        		
    			for (int i=0;i<listaTablasHashtable.size();i++){    				
    				comboGeomTabla.addItem(listaTablasHashtable.get(i));
	    	    } 
    		
    		} catch (SQLExceptionException0 e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}	    
    		
    		
    	}
    	return comboGeomTabla;
    }
    
    /**
     * This method initializes cmbCalle	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JRadioButton getRdbRefCat()
    {
    	if (radioRefCat == null)
        {
    		radioRefCat = new JRadioButton();
    		buttonGroup.add(radioRefCat);
    		radioRefCat.setLocation(new Point(238, 177));
    		radioRefCat.setMnemonic(KeyEvent.VK_UNDEFINED);
    		radioRefCat.setSize(new java.awt.Dimension(21,21));
    		radioRefCat.setText("Referencia Catastral");
    		radioRefCat.setSelected(true);
    		radioRefCat.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                	//comprobarEstadoPanel();
                	if(!radioXY.isSelected()){
	                	radioGeomPoint.setSelected(false);
	                	radioGeomOrig.setEnabled(true);
	                	comboGeomTabla.setEnabled(false);
                	}
                	
                	if(radioGeomTabla.isSelected()){
                		comboGeomTabla.setEnabled(true);
                	}
                	else{
                		comboGeomTabla.setEnabled(false);
                	}
                }
            });
        }
        return radioRefCat;
    }

    /**
     * This method initializes radioXY	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getRadioXY()
    {
    	if (radioXY == null)
        {
    		radioXY = new JRadioButton();
    		buttonGroup.add(radioXY);
    		radioXY.setLocation(new Point(238, 177));
    		radioXY.setMnemonic(KeyEvent.VK_UNDEFINED);
    		radioXY.setSize(new java.awt.Dimension(21,21));
    		radioXY.setText("Coordenadas X,Y"); 
    		radioXY.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                	//comprobarEstadoPanel();
                	if(radioXY.isSelected()){
                		radioGeomOrig.setEnabled(false);
                		if(radioGeomOrig.isSelected()){
                			radioGeomPoint.setSelected(true);
                		}
	                	
                	}
                	if(radioGeomTabla.isSelected()){
                		comboGeomTabla.setEnabled(true);
                	}
                	else{
                		comboGeomTabla.setEnabled(false);
                	}
                }
            });
        }
        return radioXY;
    }
    
    private JRadioButton getRadioPostal()
    {
    	if (radioPostal == null)
        {
    		
    		radioPostal = new JRadioButton();
    		buttonGroup.add(radioPostal);
    		radioPostal.setLocation(new Point(238, 177));
    		radioPostal.setMnemonic(KeyEvent.VK_UNDEFINED);
    		radioPostal.setSize(new java.awt.Dimension(21,21));
    		radioPostal.setText("Dirección Postal");
    		radioPostal.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                	//comprobarEstadoPanel();
                	if(!radioXY.isSelected()){
	                	radioGeomPoint.setSelected(false);
	                	radioGeomOrig.setEnabled(true);
                	}
                	if(radioGeomTabla.isSelected()){
                		comboGeomTabla.setEnabled(true);
                	}
                	else{
                		comboGeomTabla.setEnabled(false);
                	}
                }
            });
        }
        return radioPostal;
    }       
}
