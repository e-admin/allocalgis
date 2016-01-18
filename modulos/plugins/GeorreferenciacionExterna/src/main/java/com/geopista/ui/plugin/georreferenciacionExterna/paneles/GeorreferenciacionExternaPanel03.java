/**
 * GeorreferenciacionExternaPanel03.java
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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.georreferenciacionExterna.ConsultaDatosBean;
import com.geopista.ui.plugin.georreferenciacionExterna.GeorreferenciacionExternaData;
import com.geopista.ui.plugin.georreferenciacionExterna.utils.UbicacionListCellRenderer;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.localgis.georreferenciaexterna.webservicesclient.GeorreferenciaExternaWSWrapper;
import com.localgis.ws.georreferenciaexterna.client.protocol.SQLExceptionException0;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class GeorreferenciacionExternaPanel03 extends javax.swing.JPanel implements WizardPanel
{
	private int contador=0;    
    private String localId = null;
    private String nextID = null;
    private PlugInContext   context;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();    
    private WizardContext wizardContext;  
    
    private JComboBox cmbCampoName = null;
    private JComboBox cmbCampo1Name = null;
    private JTextField jTextFieldPortal;
    
    private ButtonGroup grupoRadio = new ButtonGroup();
    private JRadioButton jRadioVia = null;
    private JRadioButton jRadioViaPortal = null;
    
    private JLabel jLabelCampo = null;  
    private JLabel jLabelCampo1 = null; 
    private JLabel jLabelPortal = null; 
	
	private JPanel jPanelInfo = null;	
	private JPanel jPanelCampoGeo = null;	
	private JPanel jPanelCampo = null;	
	
	private Hashtable listaColumnas=new Hashtable();
	private ConsultaDatosBean consultaSelected = null;
    
    private GeorreferenciacionExternaData datos=new GeorreferenciacionExternaData();
    private boolean estado = true;
    
    
    public GeorreferenciacionExternaPanel03(String id, String nextId, PlugInContext context2, ConsultaDatosBean consultaSelected ) {
    	
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georreferenciacionExterna.languages.GeorreferenciacionExternai18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("GeorreferenciacionExterna",bundle);        
        
    	this.context=context2;
        this.nextID = nextId;
        this.localId = id;
        this.consultaSelected  = consultaSelected;
        try
        {
        	setName(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel03.titlePanel"));
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
    private void initialize(){
    	//De esta manera conseguimos que me genere los paneles, solo cuando tengamos datos de las pantallas anteriores
    	//sino me podría dar error
    	
    	if (contador!=0){
    		this.setLayout(new GridBagLayout());
        	this.setSize(new Dimension(600, 550));
        	this.setPreferredSize(new Dimension(600, 550));
        	       
        	this.add(getJPanelInfo(), 
    				new GridBagConstraints(0,0,1,1, 1, 0.05,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

    		this.add(getCampoGeoPanel(), 
    				new GridBagConstraints(0,1,1,1, 1, 0.95,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    	}
    	
    }
    
    private JPanel getCampoGeoPanel(){

		if (jPanelCampoGeo == null && !datos.getTipoDatos().equals("")){			
			jPanelCampoGeo = new JPanel(new GridBagLayout());
			
			//Generamos las etiquetas
			
	        jLabelCampo = new JLabel();
	        jLabelCampo1 = new JLabel();
	        jLabelPortal = new JLabel();
	        
	        if (datos.getTipoDatos().equals("Referencia Catastral") || datos.getTipoDatos().equals("Dirección Postal")){
	        	jLabelCampo.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel03.selectCampo"));
	        	if (datos.getTipoDatos().equals("Dirección Postal")){
	        		jLabelPortal.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel03.selectPortal"));
	        	}
	        }else if (datos.getTipoDatos().equals("Coordenadas X,Y")){
	        	jLabelCampo.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel03.selectCampo1"));
	        	jLabelCampo1.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel03.selectCampo2"));
	        }
	        
	        //Aquí llamamos a los métodos que generan los paneles de paneles
	        
	        jPanelCampoGeo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel03.selectCampo3"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

	        jPanelCampoGeo.add(getJPanelBD(),
	      			  new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
	      					  GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
	        
		}else if(jPanelCampoGeo != null && !datos.getTipoDatos().equals("") && contador>1){
			//En el caso de que hubiéramos pulsado el botón 'Anterior' (del siguiente panel), para que me genere todo otra vez con los nuevos datos
			//creamos esta condición '(jPanelCampoGeo != null)', ya que los paneles pueden variar según unas opciones u otras
			
			jPanelCampoGeo.removeAll();	
	        
	        jLabelCampo = new JLabel();
	        jLabelCampo1 = new JLabel();
	        
	        if (datos.getTipoDatos().equals("Referencia Catastral") || datos.getTipoDatos().equals("Dirección Postal")){
	        	jLabelCampo.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel03.selectCampo"));
	        }else if (datos.getTipoDatos().equals("Coordenadas X,Y")){
	        	jLabelCampo.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel03.selectCampo1"));
	        	jLabelCampo1.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel03.selectCampo2"));
	        }
	        
	        jPanelCampoGeo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel03.selectCampo3"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

	        jPanelCampoGeo.add(getJPanelBD(),
	      			  new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.CENTER,
	      					  GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
		}
		
		return jPanelCampoGeo;
	}
    
    public JPanel getJPanelBD(){
    	
    	if (!datos.getTipoDatos().equals("")){
    		jPanelCampo  = new JPanel();
    		jPanelCampo.setLayout(new GridBagLayout());
    		//En el caso de ref_catastral y id_via, genero una sola etiqueta y un solo combo
    		
    		int insetstop = 0;
    		int insetsleft = 100;
    		int insetsbottom = 0;
    		int insetsright  = 100;
    		
    		if(datos.getTipoDatos().equals("Dirección Postal")){
    			insetstop = 10;
    			insetsleft = 0;
    			insetsbottom = 0;
        		insetsright  = 0;
    		}

    		if (datos.getTipoDatos().equals("Referencia Catastral") || datos.getTipoDatos().equals("Dirección Postal")){
    			jPanelCampo.add(jLabelCampo,
  					  new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
  							  GridBagConstraints.NONE, new Insets(15,20,0,5),0,0));
  			
    			jPanelCampo.add(getCmbCampoName(),
  					new GridBagConstraints(0,1,1,1,0.1, 0.1,GridBagConstraints.NORTH,
  							GridBagConstraints.HORIZONTAL, new Insets(insetstop,insetsleft,insetsbottom,insetsright),0,0));
    			
    			if(datos.getTipoDatos().equals("Dirección Postal")){
    				estado = true;
    				 wizardContext.inputChanged(); 
    				jPanelCampo.add(jLabelPortal,
    	  					  new GridBagConstraints(0,2,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
    	  							  GridBagConstraints.NONE, new Insets(15,20,0,5),0,0));
    	  			
    	    		jPanelCampo.add(getJTextFieldPortal(),
    	  					new GridBagConstraints(0,3,1,1,0.1, 0.1,GridBagConstraints.NORTH,
    	  							GridBagConstraints.HORIZONTAL, new Insets(insetstop,insetsleft,insetsbottom,insetsright),0,0));
    	    		
    	    		
    	    		// se añaden los radiobutton de seleccion
    	    		jPanelCampo.add(getJRadioVia(),
    	  					new GridBagConstraints(3,1,1,1,0.1, 0.1,GridBagConstraints.NORTH,
    	  							GridBagConstraints.HORIZONTAL, new Insets(0,100,0,100),0,0));
    	    		
    	    		jPanelCampo.add(getJRadioViaPortal(),
    	  					new GridBagConstraints(3,3,1,1,0.1, 0.1,GridBagConstraints.NORTH,
    	  							GridBagConstraints.HORIZONTAL, new Insets(0,100,0,100),0,0));
    	    		
    	    		
    			}
    			
    		}
    		else if(datos.getTipoDatos().equals("Coordenadas X,Y")){
    			//Para X,Y genero dos etiquetas y dos combos
    			
    			jPanelCampo.add(jLabelCampo,
  					  new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
  							  GridBagConstraints.NONE, new Insets(15,20,0,5),0,0));
  			
    			jPanelCampo.add(getCmbCampoName(),
  					new GridBagConstraints(0,1,1,1,0.1, 0.1,GridBagConstraints.NORTH,
  							GridBagConstraints.HORIZONTAL, new Insets(0,100,0,100),0,0));
    			
    			jPanelCampo.add(jLabelCampo1,
  					  new GridBagConstraints(0,2,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
  							  GridBagConstraints.NONE, new Insets(15,20,0,5),0,0));
  			
    			jPanelCampo.add(getCmbCampo1Name(),
  					new GridBagConstraints(0,3,1,1,0.1, 0.31,GridBagConstraints.NORTH,
  							GridBagConstraints.HORIZONTAL, new Insets(0,100,0,100),0,0));
    		}    	
    	}
    	return jPanelCampo;
   }
    
   public JPanel getJPanelInfo(){

	    
		if (jPanelInfo == null && !datos.getTipoDatos().equals("")){	
			JTextArea jTextAreaInfo=null;
			jPanelInfo  = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
			if(datos.getTipoDatos().equals("Referencia Catastral")){
				jTextAreaInfo = new JTextArea(I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info1"));
			}else if(datos.getTipoDatos().equals("Dirección Postal")){
				jTextAreaInfo = new JTextArea(I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info2"));
			}else if(datos.getTipoDatos().equals("Coordenadas X,Y")){
				jTextAreaInfo = new JTextArea(I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info3"));
			}
			
			jTextAreaInfo.setEnabled(false);
			jTextAreaInfo.setFont(new JLabel().getFont());
			jTextAreaInfo.setOpaque(false);
			jTextAreaInfo.setDisabledTextColor(Color.black);
			jTextAreaInfo.setEditable(false);
			jTextAreaInfo.setWrapStyleWord(true);
			jPanelInfo.add(jTextAreaInfo);
			
		}else if (jPanelInfo != null && !datos.getTipoDatos().equals("") && contador>1){
			//Lo mismo que antes, por sí apretamos el botón 'Anterior', del siguiente panel
			
			jPanelInfo.removeAll();	
			JTextArea jTextAreaInfo=null;
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
			if(datos.getTipoDatos().equals("Referencia Catastral")){
				jTextAreaInfo = new JTextArea(I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info1"));
			}else if(datos.getTipoDatos().equals("Dirección Postal")){
				jTextAreaInfo = new JTextArea(I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info2"));
			}else if(datos.getTipoDatos().equals("Coordenadas X,Y")){
				jTextAreaInfo = new JTextArea(I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info3"));
			}
			
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
     * @throws SQLExceptionException0 
     */
    public void enteredFromLeft(Map dataMap)
    {
    	datos.setBbdd((String)wizardContext.getData("BD"));
    	datos.setTabla((String)wizardContext.getData("TB"));
    	datos.setTipoDatos((String)wizardContext.getData("tipo"));  

    	datos.setTipoGeometria((String)wizardContext.getData("tipogeom"));  
    	datos.setTablaGeometria((String)wizardContext.getData("tablatipogeom")); 
    	
    	    	
    	//Aquí aumentamos el contador, puesto que al apretar 'Siguiente' en el panel anterior, accedemos a este método
    	//Así una vez que tenemos todos los datos necesarios, ya podemos generar los distintos elementos de este panel.
    	
    	contador++;
		initialize();	
		
		if(this.consultaSelected != null){
    		// es la modificacion de la consulta
    		rellenarDatosPanel();
    		
    	}
    }    
   
    private void rellenarDatosPanel(){
    	
    	if(this.consultaSelected.getMetodo_georeferencia().equals("Coordenadas X,Y")){
    		String [] camposGeorreferencia = this.consultaSelected.getCampo_georeferencia().split(":");
    		cmbCampoName.setSelectedItem(camposGeorreferencia[0]);
    		cmbCampoName.setVisible(true);
    		cmbCampo1Name.setSelectedItem(camposGeorreferencia[1]);
    		cmbCampo1Name.setVisible(true);
    	}
    	else{
    		for(int i=0; i<cmbCampoName.getItemCount(); i++){
    			String valorCombo = (String)cmbCampoName.getItemAt(i);
    			if(valorCombo.equals(this.consultaSelected.getCampo_georeferencia())){
    				cmbCampoName.setSelectedIndex(i);
    				cmbCampoName.setVisible(true);
    			}
    		}
    		
    		if(this.consultaSelected.getMetodo_georeferencia().equals("Dirección Postal")){
    			if(this.consultaSelected.getPortal() != null && 
    					!this.consultaSelected.getPortal().equals("")){
    				jTextFieldPortal.setText(this.consultaSelected.getPortal());
    				jTextFieldPortal.setEnabled(true);
    				jRadioViaPortal.setSelected(true);
    			}
    		}
    		
    	}
    	
    	
    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    	if ((datos.getTipoDatos().equals("Referencia Catastral") || datos.getTipoDatos().equals("Dirección Postal")) && cmbCampoName.getSelectedItem()!=null){
    		Hashtable campo=new Hashtable();
    		
    		Set valores = listaColumnas.keySet();
			Iterator listaCampos = valores.iterator();

			while (listaCampos.hasNext()){
				String key = listaCampos.next().toString();				
				if (key.equals(cmbCampoName.getSelectedItem())){
    				campo.put(cmbCampoName.getSelectedItem().toString(), listaColumnas.get(key));
       			}  
			}	   
			wizardContext.setData("cmp",campo);
			
			if(datos.getTipoDatos().equals("Dirección Postal")){
				String campoDP = "";
				if(jRadioViaPortal.isSelected()){
					campoDP = jTextFieldPortal.getText();
				
				}
				wizardContext.setData("cmpDP",campoDP);
				
			}
			
    	}else if(datos.getTipoDatos().equals("Coordenadas X,Y") && (cmbCampoName.getSelectedItem()!=null)&& cmbCampo1Name.getSelectedItem()!=null){
    		Hashtable campo=new Hashtable();
    		
    		Set valores = listaColumnas.keySet();
			Iterator listaCampos = valores.iterator();

			String coordeX  = "";
			String typeCoordeX = "";
			String coordeY = "";
			String typeCoordeY = "";
			while (listaCampos.hasNext()){
				String key = listaCampos.next().toString();				

				if (key.equals(cmbCampoName.getSelectedItem())){
					coordeX = cmbCampoName.getSelectedItem().toString();
					typeCoordeX= (String)listaColumnas.get(key);
       			} 
				if (key.equals(cmbCampo1Name.getSelectedItem())){
					coordeY = cmbCampo1Name.getSelectedItem().toString();
					typeCoordeY= (String)listaColumnas.get(key);
       			}
			}	
			ArrayList arrayCoordenadasXY = new ArrayList();
			Hashtable hashX = new Hashtable();
			Hashtable hashY = new Hashtable();
			hashX.put(coordeX, typeCoordeX);
			hashY.put(coordeY, typeCoordeY);
			arrayCoordenadasXY.add(hashX);
			arrayCoordenadasXY.add(hashY);
			wizardContext.setData("cmpElegidosXY",arrayCoordenadasXY); 
			
			campo.put(coordeX, typeCoordeX);
			campo.put(coordeY, typeCoordeY);
			
			
    		wizardContext.setData("cmp",campo); 
    		
    	}else if(( datos.getTipoDatos().equals("Referencia Catastral") || datos.getTipoDatos().equals("Dirección Postal")) && cmbCampoName.getSelectedItem()==null){
    		//Esta condición nos sirve para que cuando pinchamos en el botón 'Anterior' del siguiente panel, nos seleccione
    		//el mismo item que ya teníamos. Esto no afecta a la hora de poder elegir otro item.
    		Hashtable campo=new Hashtable();
    		getCmbCampoName();   
    		
    		Set valores = listaColumnas.keySet();
			Iterator listaCampos = valores.iterator();

			while (listaCampos.hasNext()){
				String key = listaCampos.next().toString();				
				if (key.equals(cmbCampoName.getSelectedItem())){
    				campo.put(cmbCampoName.getSelectedItem().toString(), listaColumnas.get(key));
       			} 
			}				
    		wizardContext.setData("cmp",campo);
    		
    	}else if(datos.getTipoDatos().equals("Coordenadas X,Y") && (cmbCampoName.getSelectedItem()==null)&& cmbCampo1Name.getSelectedItem()==null){
    		Hashtable campo=new Hashtable();
    		getCmbCampoName();   
    		getCmbCampo1Name();
    		
    		Set valores = listaColumnas.keySet();
			Iterator listaCampos = valores.iterator();

			while (listaCampos.hasNext()){
				String key = listaCampos.next().toString();				
				if (key.equals(cmbCampoName.getSelectedItem())){
    				campo.put("CX", cmbCampoName.getSelectedItem().toString());
       			} 
				if (key.equals(cmbCampo1Name.getSelectedItem())){
    				campo.put("CY", cmbCampo1Name.getSelectedItem().toString());
       			}

			}
    		wizardContext.setData("cmp",campo);    		
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
    	cmbCampoName.setSelectedIndex(cmbCampoName.getSelectedIndex());
        if (datos.getTipoDatos().equals("Coordenadas X,Y")){
        	cmbCampo1Name.setSelectedIndex(cmbCampo1Name.getSelectedIndex());
        }
        
    }
    
    public String getTitle()
    {
      return this.getName();
    }
    
    public String getInstructions()
    {
        return I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel03.instructions");
    }
    
    /**
     * Este metodo valida y activa o no el boton de next
     */
    public boolean isInputValid()
    {
       return estado;
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
     * This method initializes cmbCampoName	
     * 	
     * @return javax.swing.JComboBox	
     * @throws SQLExceptionException0 
     */
    private JComboBox getCmbCampoName()
    {
    	if (cmbCampoName == null)
        {
    		cmbCampoName  = new JComboBox();
    		cmbCampoName.setRenderer(new UbicacionListCellRenderer());
    		try {    			
    			listaColumnas.putAll(GeorreferenciaExternaWSWrapper.obtenerColumnasBbdd(datos.getBbdd(), datos.getTabla()));
    			
    			Vector v = new Vector(listaColumnas.keySet());
    		    //Collections.sort(v);

    			Iterator listaCampos = v.iterator();

        		while (listaCampos.hasNext()){
        			String key = listaCampos.next().toString();
        			if (!key.equals("GEOMETRY")){
        				cmbCampoName.addItem(key);
        			} 
        		}
			} catch (SQLExceptionException0 e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else{
        	cmbCampoName.removeAllItems();
        	try {
        		listaColumnas.clear();
        		listaColumnas.putAll(GeorreferenciaExternaWSWrapper.obtenerColumnasBbdd(datos.getBbdd(), datos.getTabla()));
        		
        		
        		Vector v = new Vector(listaColumnas.keySet());
    		    //Collections.sort(v);

    			Iterator listaCampos = v.iterator();

        		while (listaCampos.hasNext()){
        			String key = listaCampos.next().toString();
        			if (!key.equals("GEOMETRY")){
        				cmbCampoName.addItem(key);
        			} 
        		}
			} catch (SQLExceptionException0 e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			 	
        }
    	return cmbCampoName;
    }  
    
    private JRadioButton getJRadioVia(){
    	if(jRadioVia == null){
    		jRadioVia = new JRadioButton();
    		jRadioVia.setEnabled(true);
    		jRadioVia.setSelected(true);
    		jRadioVia.setText( I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel03.busquedaidvia"));

    		grupoRadio.add(jRadioVia);
    		
    		jRadioVia.addActionListener(new java.awt.event.ActionListener()
            {
        		public void actionPerformed(java.awt.event.ActionEvent e)
		        {
        			comprobacionSeleccionViaPortal();
		        }
            });
    	}
    	
    	return jRadioVia;
    }
    
    private JRadioButton getJRadioViaPortal(){
    	
    	if(jRadioViaPortal == null){
    		jRadioViaPortal = new JRadioButton();
    		jRadioViaPortal.setEnabled(true);
    		jRadioViaPortal.setText( I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel03.busquedaidviaportal"));
    		
    		grupoRadio.add(jRadioViaPortal);
    		
    		jRadioViaPortal.addActionListener(new java.awt.event.ActionListener()
            {
        		public void actionPerformed(java.awt.event.ActionEvent e)
		        {
		        	comprobacionSeleccionViaPortal();
		        }
            });
    	}
    	
    	return jRadioViaPortal;
    }
    
    
    private void comprobacionSeleccionViaPortal(){
    	
    	if(jRadioVia.isSelected()){
    		jTextFieldPortal.setEnabled(false);
    		
    	}
    	else if(jRadioViaPortal.isSelected()){
    		jTextFieldPortal.setEnabled(true);
    	}
    }
    
    
    private JTextField getJTextFieldPortal(){
    	
    	if(jTextFieldPortal == null){
    		jTextFieldPortal = new JTextField(100);
    		
    		jTextFieldPortal.setEnabled(false);

    		jTextFieldPortal.addCaretListener(new CaretListener()
            {
		         public void caretUpdate(CaretEvent evt)
		         {
		        	 String txt= jTextFieldPortal.getText();
		        	 if(!txt.equals("")){
		        		 final int maxLong = 100;
			        	 if (txt.length() >maxLong){
			        	
			        		 JOptionPane.showMessageDialog(aplicacion.getMainFrame(), 
			        				 I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel03.tamanioPortal"));
			        		 
			        		 Runnable checkLength = new Runnable()
			        	        {
			        	            public void run()
			        	            {
			        	            	jTextFieldPortal.setText(jTextFieldPortal.getText().substring(0,maxLong));
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
    	return jTextFieldPortal;
    	
    }
    
    
    
    private JComboBox getCmbCampo1Name()
    {
    	if (cmbCampo1Name == null)
        {
    		cmbCampo1Name  = new JComboBox();
    		cmbCampo1Name.setRenderer(new UbicacionListCellRenderer());
    		try {
    			listaColumnas.putAll(GeorreferenciaExternaWSWrapper.obtenerColumnasBbdd(datos.getBbdd(), datos.getTabla()));

    			Vector v = new Vector(listaColumnas.keySet());
    		    //Collections.sort(v);

    			Iterator listaCampos = v.iterator();

        		while (listaCampos.hasNext()){
        			String key = listaCampos.next().toString();
        			if (!key.equals("GEOMETRY")){
        				cmbCampo1Name.addItem(key);
        			} 
        		}
			} catch (SQLExceptionException0 e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			 	
        }else{
        	cmbCampo1Name.removeAllItems();
        	try {
        		listaColumnas.clear();
        		listaColumnas.putAll(GeorreferenciaExternaWSWrapper.obtenerColumnasBbdd(datos.getBbdd(), datos.getTabla()));
        		
        		Vector v = new Vector(listaColumnas.keySet());
    		   // Collections.sort(v);
    		    
    		    Iterator listaCampos = v.iterator();

        		while (listaCampos.hasNext()){
        			String key = listaCampos.next().toString();
        			if (!key.equals("GEOMETRY")){
        				cmbCampo1Name.addItem(key);
        			}        			
        		}
			} catch (SQLExceptionException0 e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			 	
        }
    	return cmbCampo1Name;
    }    
}
