/**
 * GeorreferenciacionExternaPanel01.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.georreferenciacionExterna.paneles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.georreferenciacionExterna.ConsultaDatosBean;
import com.geopista.ui.plugin.georreferenciacionExterna.utils.UbicacionListCellRenderer;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.localgis.georreferenciaexterna.webservicesclient.GeorreferenciaExternaWSWrapper;
import com.localgis.ws.georreferenciaexterna.client.protocol.SQLExceptionException0;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class GeorreferenciacionExternaPanel01 extends javax.swing.JPanel implements WizardPanel
{
    private int interruptor=0;    
    private String localId = null;
    private String nextID = null;
    private PlugInContext   context;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();   
    private WizardContext wizardContext;     
    
    private JComboBox cmbBDName = null;
    private JComboBox cmbTablesName = null;
    private JTextField nomConsultaCapa = null;
    private JTextArea descripcion = null;
    
    private JLabel jLabelBD = null;    
    private JLabel jLabelTables = null;
    
    private JLabel jLabelNombreConsultaCapa = null; 
    private JLabel jLabelDescripcion = null;
	
	private JPanel jPanelInfo = null;	
	private JPanel jPanelBDTables = null;	
	private JPanel jPanelBD = null;	
	private JPanel jPanelTables = null;   
    
	private JPanel jPanelNombreConsultaCapa = null;	
	private JPanel jPanelNomConsultaCapa = null;	
	
	private ConsultaDatosBean consultaSelected = null;
	private Hashtable listaTablas=new Hashtable();
	private boolean estado;
    
    public GeorreferenciacionExternaPanel01(String id, String nextId, PlugInContext context2, ConsultaDatosBean consultaSelected ) {
    	
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georreferenciacionExterna.languages.GeorreferenciacionExternai18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("GeorreferenciacionExterna",bundle);        
        
    	this.context=context2;
        this.nextID = nextId;
        this.localId = id;
        this.consultaSelected  = consultaSelected;
        try
        {
        setName(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel01.titlePanel"));
            initialize();
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }   
    }
    
    private void initialize(){
    	
    	this.setLayout(new GridBagLayout());
    	this.setSize(new Dimension(600, 550));
    	this.setPreferredSize(new Dimension(600, 550));
    	       
    	this.add(getJPanelInfo(), 
				new GridBagConstraints(0,0,1,1, 1, 0.05,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
    	
    	this.add(getJPanelNombreConsultaCapa(), 
				new GridBagConstraints(0,1,1,1, 1, 0.05,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

		this.add(getBDTablesPanel(), 
				new GridBagConstraints(0,2,1,1, 1, 0.95,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    }
    
    private JPanel getJPanelNombreConsultaCapa(){
    	
    	if(jPanelNombreConsultaCapa == null){
    		
    		jPanelNombreConsultaCapa = new JPanel(new GridBagLayout());
    		
    		jPanelNombreConsultaCapa.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel01.nombreConsultaCapa"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
    		
    		jLabelNombreConsultaCapa = new JLabel();
    		jLabelNombreConsultaCapa.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel01.nombreConsultaCapa"));
    		jLabelDescripcion = new JLabel();
    		jLabelDescripcion.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel01.descripcion"));
    		    		
    		jPanelNombreConsultaCapa.add(jLabelNombreConsultaCapa,
					  new GridBagConstraints(0,0,1,1, 0.3, 0.1,GridBagConstraints.WEST,
							  GridBagConstraints.NONE, new Insets(1,20,0,5),0,0));
			
    		jPanelNombreConsultaCapa.add(getNomConsultaCapa(),
					new GridBagConstraints(1,0,1,1,0.4, 0.1,GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(0,0,0,30),0,0));
    		
    		jPanelNombreConsultaCapa.add(jLabelDescripcion,
					  new GridBagConstraints(0,1,1,1, 0.3, 0.4,GridBagConstraints.WEST,
							  GridBagConstraints.NONE, new Insets(1,20,0,5),0,0));
    		
    		jPanelNombreConsultaCapa.add(getDescripcion(),
					new GridBagConstraints(1,1,1,1,0.4, 0.4,GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(0,0,0,30),0,0));
    		
    	}
    	
    	return jPanelNombreConsultaCapa;
    }
    
    
    private JPanel getBDTablesPanel(){

		if (jPanelBDTables == null){

			jPanelBDTables = new JPanel(new GridBagLayout());
	        
	        jLabelBD = new JLabel();
	        jLabelBD.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel01.databaseList"));
	        
	        jLabelTables = new JLabel();
	        jLabelTables.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel01.TablesList"));

	        jPanelBDTables.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel01.SelectBDTable"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

	        jPanelBDTables.add(getJPanelBD(),
	      			  new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
	      					  GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
						
	        jPanelBDTables.add(getJPanelTables(), 
					new GridBagConstraints(0,1,1,1,0.1, 0.31,GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));			
			
		}
		return jPanelBDTables;
	}
    
    public JPanel getJPanelBD(){
    	
    	if (jPanelBD == null){
    		
    		jPanelBD  = new JPanel();
    		jPanelBD.setLayout(new GridBagLayout());
    		
    		jPanelBD.add(jLabelBD,
					  new GridBagConstraints(0,0,1,1, 0.3, 0.1,GridBagConstraints.SOUTHWEST,
							  GridBagConstraints.NONE, new Insets(1,20,0,5),0,0));
			
    		jPanelBD.add(getCmbBDName(),
					new GridBagConstraints(1,0,1,1,1.4, 0.1,GridBagConstraints.SOUTHWEST,
							GridBagConstraints.HORIZONTAL, new Insets(0,0,0,30),0,0));
    	}
    	return jPanelBD;
    }
    
    public JPanel getJPanelTables(){
    	
    	if (jPanelTables == null){
    		
    		jPanelTables   = new JPanel();
    		jPanelTables.setLayout(new GridBagLayout());
    		
    		jPanelTables.add(jLabelTables,
					  new GridBagConstraints(0,0,1,1, 0.3, 0.1,GridBagConstraints.WEST,
							  GridBagConstraints.NONE, new Insets(1,20,0,5),0,0));
			
    		jPanelTables.add(getCmbTablesName(),
					new GridBagConstraints(1,0,1,1,0.4, 0.1,GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(0,0,0,30),0,0));
    		
    		
    	}
    	return jPanelTables;
    }
    
    public JPanel getJPanelInfo(){

		if (jPanelInfo == null){

			jPanelInfo  = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info5"));
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
    	
		nomConsultaCapa.setText(this.consultaSelected.getNombreConsulta());
		descripcion.setText(this.consultaSelected.getDescripcion());
    	
		for(int i=0; i<cmbBDName.getItemCount(); i++){
			String valorCombo = (String)cmbBDName.getItemAt(i);
			if(valorCombo.equals(this.consultaSelected.getNombre_bbdd_ext())){
				cmbBDName.setSelectedIndex(i);
			}
		}
		for(int i=0; i<cmbTablesName.getItemCount(); i++){
			String valorCombo = (String)cmbTablesName.getItemAt(i);
			if(valorCombo.equals(this.consultaSelected.getNombre_tabla_ext())){
				cmbTablesName.setSelectedIndex(i);
			}
		}
		
    }
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    	String database=cmbBDName.getSelectedItem().toString();
    	String tabla=cmbTablesName.getSelectedItem().toString();
    	String nombreConsultaCapa = nomConsultaCapa.getText();
        wizardContext.setData("BD",database);
        wizardContext.setData("TB",tabla);
        wizardContext.setData("NCC",nombreConsultaCapa);
        wizardContext.setData("DES",descripcion.getText());
        
       // aplicacion.get
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
    	
    }
    
    public String getTitle()
    {
      return this.getName();
    }
    
    public String getInstructions()
    {
        return I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel01.instructions");
    }
    
    /**
     * Este metodo valida y activa o no el boton de next
     */
    public boolean isInputValid()
    {
    	boolean validado=false;
    	if(estado==true){
	    	if(cmbBDName !=null && cmbTablesName!=null){
	    		validado=true;
	    	}   
    	}
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
     * This method initializes cmbBDName	
     * 	
     * @return javax.swing.JComboBox     
     */
    private JComboBox getCmbBDName()
    {
    	if (cmbBDName == null)
        {    		
    		cmbBDName  = new JComboBox();
    		cmbBDName.setRenderer(new UbicacionListCellRenderer()); 
    		
    		ArrayList lstNombres = GeorreferenciaExternaWSWrapper.obtenerNombreBbdd();
    		
    		for (int i=0;i<lstNombres.size();i++){    			
    			cmbBDName.addItem(lstNombres.get(i));
    		} 
    		//Esto me sirve para que cuando cambie de BBDD seleccionado en este combo, me llame al método que modifica 
    		//al segundo combo.
    		
    		if (cmbBDName.getComponentCount()!=0){    			
    			getCmbTablesName();
    		}
        }
    	return cmbBDName;
    }    
    
    private JTextArea getDescripcion(){
    	if(descripcion == null){
    		descripcion = new JTextArea();
    		descripcion.setRows(5);
    		descripcion.setLineWrap(true);
    		descripcion.setWrapStyleWord(true);
    		
    		descripcion.setEnabled(true);
    		descripcion.setEditable(true);
    		descripcion.setFont(new JLabel().getFont());
    		//descripcion.setOpaque(false);
    		descripcion.setDisabledTextColor(Color.black);	
    		
    		descripcion.addCaretListener(new CaretListener()
            {
		         public void caretUpdate(CaretEvent evt)
		         {
		        	 if(descripcion.getText().length()>200){
		        		 final String texto = descripcion.getText().substring(0, descripcion.getText().length()-1);
		        		 
		        		 Runnable checkLength = new Runnable()
		        	        {
		        	            public void run()
		        	            {
		        	            	descripcion.setText(texto);
		        	            }
		        	        };
		        	        SwingUtilities.invokeLater(checkLength);
		        	 }

		         }
            });
		         
    	}
    	
    	
    	return descripcion;
    }
    /**
     * This method initializes nomConsultaCapa	
     * 	
     * @return JTextField          
     */
    private JTextField getNomConsultaCapa()
    {
    	if(nomConsultaCapa == null){
    		nomConsultaCapa = new JTextField(20);

    		nomConsultaCapa.addCaretListener(new CaretListener()
            {
		         public void caretUpdate(CaretEvent evt)
		         {
		        	 String txt= nomConsultaCapa.getText();
		        	 if(!txt.equals("")){
		        		 final int maxLong = 20;
			        	 if (txt.length() >maxLong){
			        	
			        		 JOptionPane.showMessageDialog(aplicacion.getMainFrame(), 
			        				 I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel01.tamanioNombreConsultaCapa")+": "+maxLong);
			        		 
			        		 Runnable checkLength = new Runnable()
			        	        {
			        	            public void run()
			        	            {
			        	            	nomConsultaCapa.setText(nomConsultaCapa.getText().substring(0,maxLong));
			        	            }
			        	        };
			        	        SwingUtilities.invokeLater(checkLength);

			        	 }
			        	 estado = true;
		        	 }
		        	 else{
		        		 estado = false;
		        	 }
		        	 wizardContext.inputChanged(); 
		        	 
		         }
            });
    	}

    	return nomConsultaCapa;	
    }
    /**
     * This method initializes cmbTablesName	
     * 	
     * @return javax.swing.JComboBox          
     */
    private JComboBox getCmbTablesName()
    {
    	if (cmbTablesName == null)
	    {
    			try {
					listaTablas.putAll(GeorreferenciaExternaWSWrapper.obtenerTablasBbdd(cmbBDName.getSelectedItem().toString()));
					cmbTablesName  = new JComboBox();
		    		cmbTablesName.setRenderer(new UbicacionListCellRenderer());    				
		        	for (int i=0;i<listaTablas.size();i++){    				
		    			cmbTablesName.addItem(listaTablas.get(i));
		    	    }  
				} catch (SQLExceptionException0 e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	    		  		     		
	    }else{
	    	cmbBDName.addItemListener(new ItemListener(){    		
	    		public void itemStateChanged(ItemEvent e) {      		    	
	    			listaTablas.clear();    
	    			try {
	    				listaTablas.putAll(GeorreferenciaExternaWSWrapper.obtenerTablasBbdd(cmbBDName.getSelectedItem().toString()));
						cmbTablesName.removeAllItems();
						if (listaTablas.isEmpty()){
							cmbTablesName.setEnabled(false);
						}else{
							for (int i=0;i<listaTablas.size();i++){    				
			        			cmbTablesName.addItem(listaTablas.get(i));        				
			        	    }
						} 
					} catch (SQLExceptionException0 e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
	    		}
	        			
	        });
	    }
		
    	return cmbTablesName;
    }     
}
