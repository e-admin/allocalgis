package com.geopista.ui.plugin.georreferenciacionExterna.paneles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.georreferenciacionExterna.ConsultaDatosBean;
import com.geopista.ui.plugin.georreferenciacionExterna.FiltroData;
import com.geopista.ui.plugin.georreferenciacionExterna.GeorreferenciacionExternaData;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.ws.cliente.Test;

public class GeorreferenciacionExternaPanel04 extends javax.swing.JPanel implements WizardPanel, AdjustmentListener
{
	
	private int contador=0;
	
	private int i=0,y=0,j=1,k=0,l=0,m=0;
	
	private String localId = null;
    private String nextID = null;
    private PlugInContext   context;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();    
    private WizardContext wizardContext;     
    
    private JPanel jPanelInfo = null;	
	private JPanel jPanelOtrCampo = null;	
	private JPanel jPanelCampo = null;
	private JPanel jPanelCampo1 = null;
	
	private JScrollPane jScrollPanel=null;
	
	private JLabel jLabelOtrCampo = null;
	
	private JCheckBox chkbox;
	private ButtonGroup grupoRadio = new ButtonGroup();
    
	private GeorreferenciacionExternaData datos=null;
	
    private Hashtable listaColumnas=new Hashtable();
    
    private ArrayList combos = new ArrayList();    
    private ArrayList key1 =new ArrayList();
    private ArrayList keyData =new ArrayList();    
    private ArrayList filtroOperacion = new ArrayList();
    private ArrayList filtroValor = new ArrayList();
    private ArrayList literalesFiltros = new ArrayList();
    private ArrayList lstRadioEtiqueta = new ArrayList();
    
    ArrayList listaType = new ArrayList();
    private boolean estado;
    private ConsultaDatosBean consultaSelected = null;
    
    int columnaCheckCampo = 0;
	int columnaOperador = 2;
	int columnaValor = 3;
	int columnaRadioEtiqueta = 4;
    
    public GeorreferenciacionExternaPanel04(String id, String nextId, PlugInContext context2, ConsultaDatosBean consultaSelected ) {
    	
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georreferenciacionExterna.languages.GeorreferenciacionExternai18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("GeorreferenciacionExterna",bundle);        
        
    	this.context=context2;
        this.nextID = nextId;
        this.localId = id;
        this.consultaSelected  = consultaSelected;
        try
        {
        setName(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel04.titlePanel"));
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
    	
    	if (contador!=0){
    		this.setLayout(new GridBagLayout());
        	this.setSize(new Dimension(600, 550));
        	this.setPreferredSize(new Dimension(600, 550));
        	
        	this.add(getJPanelInfo(), 
    				new GridBagConstraints(0,0,1,1, 1, 0.05,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

    		this.add(getOtrCamposPanel(), 
    				new GridBagConstraints(0,1,1,1, 1, 0.95,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    	}
    	
    }
    
    private JPanel getOtrCamposPanel(){

		if (jPanelOtrCampo == null){
			
			jPanelOtrCampo = new JPanel(new GridBagLayout());
	        
			jLabelOtrCampo = new JLabel();
    		jLabelOtrCampo.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel04.selectOtrCampo1"));
    		
			jPanelOtrCampo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel04.selectOtrCampo"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			jPanelOtrCampo.add(getJPanelBD(),
	      			  new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
	      					  GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
		}else if(jPanelOtrCampo!=null){
			jPanelOtrCampo.removeAll();	
			jLabelOtrCampo = new JLabel();
    		jLabelOtrCampo.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel04.selectOtrCampo1"));
    		
			jPanelOtrCampo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel04.selectOtrCampo"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			jPanelOtrCampo.add(getJPanelBD(),
	      			  new GridBagConstraints(0,0,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
	      					  GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
		}
		return jPanelOtrCampo;
	}
    
    public JPanel getJPanelBD(){
    	
    	int num=0;   	
    	
    	if (jPanelCampo1 == null){
    		
    		jPanelCampo1  = new JPanel();
    		jPanelCampo1.setLayout(new GridBagLayout());   
    		jPanelCampo1.setSize(300, jPanelCampo1.getHeight());
    		try {
    			listaColumnas.putAll(Test.obtenerColumnasBbdd(datos.getBbdd(), datos.getTabla()));    			
    			
    			Set valoresData = datos.getTipoElegido().keySet();
    			Iterator listaCamposData = valoresData.iterator();
    			while (listaCamposData.hasNext()){							
    				keyData.add(listaCamposData.next());							
    			}
    			
    			Collection type = (Collection) listaColumnas.values();
    			Iterator iterType = type.iterator();
    			while (iterType.hasNext()){
    				listaType.add((String)iterType.next());
    			}
    			
    			
    			Vector v = new Vector(listaColumnas.keySet());
    			
    		   // Collections.sort(v);
    			Iterator listaCampos = v.iterator();    			
    			while (listaCampos.hasNext()){							
    				String campo=listaCampos.next().toString();
    				if(!campo.equals("GEOMETRY")){
    					key1.add(campo);
    				}							
    			}
    			
    			num=listaColumnas.size();
    			int index = 0; 
    			if ((num>2) || (num==2 && !datos.getTipoDatos().equals("Coordenadas X,Y")) ){
    				Iterator it = keyData.iterator();
    				String result = "";
    				while(it.hasNext()){
    					result=it.next().toString();
    					index = key1.indexOf(result);
    					key1.remove(result);
    					listaType.remove(index);
    					index++;
    				}    				
    			}
    			
        		
        		for(int b=0;b<key1.size();b++){
        			//TODO: Cambiarlo para que no haga la peticion de columnas por bucle for
        			combos.add(realizarChkBoxes(b, key1,keyData));
        			
        			literalesFiltros.add( new JLabel(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel04.filtro")));
        			
        			filtroOperacion.add(realizarFiltroOperador((String)listaType.get(b), b));
        			filtroValor.add(realizarFiltroValor((String)listaType.get(b)));
        			
        			lstRadioEtiqueta.add(realizarRadioEtiqueta(b, key1,keyData));
        		}
        		
        		do{
        			jPanelCampo1.add((JCheckBox)combos.get(i), 
                			new GridBagConstraints(0,j,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
                					GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0)); 
        			
        			jPanelCampo1.add((JLabel)literalesFiltros.get(i), 
                			new GridBagConstraints(1,j,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
                					GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0)); 
        			
        			jPanelCampo1.add((JComboBox)filtroOperacion.get(i), 
                			new GridBagConstraints(2,j,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
                					GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0)); 
        			
        			jPanelCampo1.add((JTextField)filtroValor.get(i), 
                			new GridBagConstraints(3,j,1,1, 0.4, 0.4,GridBagConstraints.NORTHWEST,
                					GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0)); 
        			
        			jPanelCampo1.add((JRadioButton)lstRadioEtiqueta.get(i), 
                			new GridBagConstraints(4,j,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
                					GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));
        			
        			i++;
        			j++;
        		}while(i<key1.size());
			} catch (com.localgis.ws.servidor.SQLExceptionException0 e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			key1.clear();
			keyData.clear();
			combos.clear();
    		i=0;
    		k=0;
    		j=1;    		
			jPanelCampo1.removeAll();
			
			jPanelCampo1.setLayout(new GridBagLayout());   
    		jPanelCampo1.setSize(300, jPanelCampo1.getHeight());
    		try {
    			listaColumnas.clear();
    			listaColumnas.putAll(Test.obtenerColumnasBbdd(datos.getBbdd(), datos.getTabla()));

    			Set valoresData = datos.getTipoElegido().keySet();
    			Iterator listaCamposData = valoresData.iterator();
    			while (listaCamposData.hasNext()){							
    				keyData.add(listaCamposData.next());							
    			}
    			
    			Vector v = new Vector(listaColumnas.keySet());
    		   // Collections.sort(v);
    			Iterator listaCampos = v.iterator();
    			while (listaCampos.hasNext()){
    				String campo=listaCampos.next().toString();
    				if(!campo.equals("GEOMETRY")){
    					key1.add(campo);
    				}    											
    			}
    			
    			num=listaColumnas.size();  	
    			
    			
    			if ((num>2) || (num==2 && !datos.getTipoDatos().equals("Coordenadas X,Y")) ){
    				Iterator it = keyData.iterator();
    				String result = "";
    				while(it.hasNext()){
    					result=it.next().toString();
    					key1.remove(result);
    				}    				
    			}
    			
        		
        		for(int b=0;b<key1.size();b++){    			
        			combos.add(realizarChkBoxes(b, key1, keyData));
        			literalesFiltros.add( new JLabel(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel04.filtro"))+ ":");
        			
        			
        		}
        		
        		do{    			
        			
        			jPanelCampo1.add((JCheckBox)combos.get(i), 
                			new GridBagConstraints(0,j,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
                					GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0)); 
        			
        			jPanelCampo1.add((JLabel)literalesFiltros.get(i), 
                			new GridBagConstraints(1,j,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
                					GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0)); 
        			
        			jPanelCampo1.add((JComboBox)filtroOperacion.get(i), 
                			new GridBagConstraints(2,j,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
                					GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0)); 
        			
        			jPanelCampo1.add((JTextField)filtroValor.get(i), 
                			new GridBagConstraints(3,j,1,1, 0.4, 0.4,GridBagConstraints.NORTHWEST,
                					GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0)); 
        			
        			jPanelCampo1.add((JRadioButton)lstRadioEtiqueta.get(i), 
                			new GridBagConstraints(4,j,1,1, 0.1, 0.1,GridBagConstraints.NORTHWEST,
                					GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));
        			
        			i++;
        			j++;
        		}while(i<key1.size()); 
			} catch (com.localgis.ws.servidor.SQLExceptionException0 e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
    	
    	if(jScrollPanel==null){
    		jScrollPanel=new JScrollPane();
    		jScrollPanel.setViewportView(jPanelCampo1);
    	}else{ 
    		jScrollPanel.remove(jPanelCampo1);   		
    		jScrollPanel.setViewportView(jPanelCampo1); 
    	}
				
    	if (jPanelCampo == null){
    		jPanelCampo  = new JPanel();
    		jPanelCampo.setLayout(new GridBagLayout());
    		
    		jPanelCampo.add(jLabelOtrCampo,
					  new GridBagConstraints(0,0,1,1, 0.3, 0.1,GridBagConstraints.NORTHWEST,
							  GridBagConstraints.NONE, new Insets(15,20,0,5),0,0));
    		jPanelCampo.add(jScrollPanel, new GridBagConstraints(0,2,1,1, 0.3, 0.1,GridBagConstraints.NORTHWEST,
							  GridBagConstraints.BOTH, new Insets(15,20,0,5),0,250));
    	}else{
    		jPanelCampo.removeAll();
    		jPanelCampo.setLayout(new GridBagLayout());         		
    		
    		jPanelCampo.add(jLabelOtrCampo,
					  new GridBagConstraints(0,0,1,1, 0.3, 0.1,GridBagConstraints.NORTHWEST,
							  GridBagConstraints.NONE, new Insets(15,20,0,5),0,0));
    		jPanelCampo.add(jScrollPanel, new GridBagConstraints(0,2,1,1, 0.3, 0.1,GridBagConstraints.NORTHWEST,
							  GridBagConstraints.BOTH, new Insets(15,20,0,5),0,250));
    	}   		
    	return jPanelCampo;
    }
    
   public JPanel getJPanelInfo(){

	    
		if (jPanelInfo == null){			
			JTextArea jTextAreaInfo=null;
			jPanelInfo  = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
			jTextAreaInfo = new JTextArea(I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info4"));
			jTextAreaInfo.setEnabled(false);
			jTextAreaInfo.setFont(new JLabel().getFont());
			jTextAreaInfo.setOpaque(false);
			jTextAreaInfo.setDisabledTextColor(Color.black);
			jTextAreaInfo.setEditable(false);
			jTextAreaInfo.setWrapStyleWord(true);
			jPanelInfo.add(jTextAreaInfo);
		}else{			
			jPanelInfo.removeAll();
			JTextArea jTextAreaInfo=null;			
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
			jTextAreaInfo = new JTextArea(I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info4"));
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
    		datos=new GeorreferenciacionExternaData();
    		datos.setBbdd(this.consultaSelected.getNombre_bbdd_ext());
    		datos.setTabla(this.consultaSelected.getNombre_tabla_ext());
    		//datos.setTipoElegido(tipo_elegido);
    		datos.setTipoDatos(this.consultaSelected.getMetodo_georeferencia());
    		
    		contador++;   
        	initialize();
    		// es la modificacion de la consulta
    		rellenarDatosPanel();
    		
    	}
    	else{
	    	datos=new GeorreferenciacionExternaData();
	    	datos.setTabla((String)wizardContext.getData("TB"));
	    	datos.setBbdd((String)wizardContext.getData("BD")); 
	    	datos.setTipoElegido((Hashtable)wizardContext.getData("cmp"));
	    	datos.setTipoDatos((String)wizardContext.getData("tipo"));
	    	contador++;    	
			initialize();
    	}
		
		
    }
    
    private void rellenarDatosPanel(){
    	String [] nombre_campos_operadores = null;
    	String [] nombre_campos_valores = null;
    	String [] campos_operadores = null;
    	String [] campos_valores = null;
    	
    	HashMap hashOperadores = new HashMap();
    	HashMap hashValores = new HashMap();
    	
    	if(!this.consultaSelected.getFiltro_operador().equals("") &&
				!this.consultaSelected.getFiltro_valor().equals("")){
			
			campos_operadores = this.consultaSelected.getFiltro_operador().split(":");
			for(int j=0; j<campos_operadores.length;j++){

				nombre_campos_operadores = campos_operadores[j].split("#");
				hashOperadores.put(nombre_campos_operadores[0], nombre_campos_operadores[1]);
			}
			
			campos_valores = this.consultaSelected.getFiltro_valor().split(":");
			for(int j=0; j<campos_valores.length;j++){
				nombre_campos_valores = campos_valores[j].split("#");
				hashValores.put(nombre_campos_valores[0], nombre_campos_valores[1]);
			}
			
		}
    	
    	String [] camposMostrar = this.consultaSelected.getCampos_mostrar().split(":");
    	for(int i=0; i<camposMostrar.length; i++){
    		String [] campo_type = camposMostrar[i].split("=");
    		
    		ArrayList key =new ArrayList();
    		
    		
    		int filaActual = 0;
        	int numColumnas = 5;
    		
    		int num=jPanelCampo1.getComponentCount()/numColumnas;
        	l=0;
        	m=0;
        	
        	Set valores = listaColumnas.keySet();
    		Iterator listaCampos = valores.iterator();

    		while (listaCampos.hasNext()){
    			key.add(listaCampos.next());			
    		}
    		
    		while (l<(num)){
        		filaActual = l* numColumnas;
        		JCheckBox j=new JCheckBox();
        		j=(JCheckBox)jPanelCampo1.getComponent(filaActual + columnaCheckCampo);
        		if(campo_type[0].equals(j.getText())){
        			j.setSelected(true);
        			//se habilitan los filtros
        			// se rellenan los filtros
        			JComboBox comboOperator = (JComboBox) jPanelCampo1.getComponent(filaActual + columnaOperador);
        			JTextField textFieldValor = (JTextField) jPanelCampo1.getComponent(filaActual + columnaValor);
        			comboOperator.setEnabled(true);
        			
        			if(hashOperadores.containsKey(campo_type[0])){
        				comboOperator.setSelectedItem((String)hashOperadores.get(campo_type[0]));
        			}
        			
        			if(hashValores.containsKey(campo_type[0])){
        				textFieldValor.setText((String)hashValores.get(campo_type[0]));
        				textFieldValor.setEnabled(true);
        			}
        			
        		}
        		
        		l++;
    		}
    		
    	}
    	
    	// se rellenan el radio de etiqueta y se habilitan
    	l=0;
    	int filaActual = 0;
    	int numColumnas = 5;
    	int num=jPanelCampo1.getComponentCount()/numColumnas;
    	while (l<(num)){
    		filaActual = l* numColumnas;
    		JCheckBox j=new JCheckBox();
    		JRadioButton radio = new JRadioButton();
    		j=(JCheckBox)jPanelCampo1.getComponent(filaActual + columnaCheckCampo);
    		radio = (JRadioButton)jPanelCampo1.getComponent(filaActual + columnaRadioEtiqueta);
    		if(j.isSelected()){
    			
    			radio.setEnabled(true);
    		}
    		if(j.getText().equals(this.consultaSelected.getCampo_etiqueta())){
    			radio.setSelected(true);
    		}
    		l++;
    	}
    	
    	estado = true;
		wizardContext.inputChanged(); 

    }
    
    private void comprobacionFiltroEstablecido(){

    	int numColumnas = 5;
    	int filaActual = 0;
    	boolean radioSeleccionado = false;
    	boolean operadorSeleccionado = false;
    	boolean valorSeleccionado = false;
    	
    	for(int i=0; i<combos.size() && !radioSeleccionado; i++){
    		filaActual = i* numColumnas;
    		JCheckBox checkBox=(JCheckBox)jPanelCampo1.getComponent(filaActual+columnaCheckCampo);
    		if(checkBox.isSelected()){
    			JRadioButton radio = (JRadioButton)jPanelCampo1.getComponent(filaActual+columnaRadioEtiqueta);
    			if(radio.isSelected()){
    				radioSeleccionado = true;

    			}
    		}

    	}
    	
    	if(radioSeleccionado){
    		estado = true;
    		wizardContext.inputChanged(); 
    	}
    	else{
    		estado = false;
    		wizardContext.inputChanged(); 
    	}
    }
    
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    	
    	ArrayList key =new ArrayList();
    	Hashtable campos=new Hashtable();    	
    	JCheckBox j=new JCheckBox();
    	int filaActual = 0;
    	int numColumnas = 5;
    	int num=jPanelCampo1.getComponentCount()/numColumnas;
    	l=0;
    	m=0;
    	
    	Set valores = listaColumnas.keySet();
		Iterator listaCampos = valores.iterator();

		while (listaCampos.hasNext()){
			key.add(listaCampos.next());			
		}
		
		FiltroData filtro = null;
		ArrayList lstFiltro = new ArrayList();
    	while (l<(num)){
    		filtro = new FiltroData();
    		filaActual = l* numColumnas;
    		j=(JCheckBox)jPanelCampo1.getComponent(filaActual + columnaCheckCampo);
    		if (j.isSelected()){ 
    			for (int i=0;i<key.size();i++){
	    			if (key.get(i).equals(j.getText())){
	    				
	    			
	    				campos.put(key.get(i), listaColumnas.get(key.get(i)));
	    			
	    				if(!((String)((JComboBox)jPanelCampo1.getComponent(filaActual + columnaOperador)).getSelectedItem()).equals("") && 
	    						!((JTextField)jPanelCampo1.getComponent(filaActual + columnaValor)).getText().equals("")){
	    					filtro.setCampo(((JCheckBox)jPanelCampo1.getComponent(filaActual + columnaCheckCampo)).getText());
		    				filtro.setOperador((String)((JComboBox)jPanelCampo1.getComponent(filaActual + columnaOperador)).getSelectedItem());
		    				filtro.setValor(((JTextField)jPanelCampo1.getComponent(filaActual + columnaValor)).getText());
		    				
		    				for(int index=0; index<key1.size(); index++){
		    					String clave = (String)key1.get(index);
		    					if(filtro.getCampo().equals(clave)){
			    					String typeCamp = (String)listaType.get(index);
			    					filtro.setTipoCampo(typeCamp);

		    					}
		    				}
		    				
	    				}
	    				else{
	    					filtro = null;
	    				}

	    				lstFiltro.add(filtro);
	    				
	    				if(((JRadioButton)jPanelCampo1.getComponent(filaActual +columnaRadioEtiqueta)).isSelected()){
	                		
    	           		 	wizardContext.setData("ETQSLD",key.get(i));
    	    			}
	    			}
    			}
    			
    			
    		}
    		l++;
    	};    	
    	
        wizardContext.setData("cmps",campos);
        wizardContext.setData("FLT",lstFiltro);

        
        //Desactivo los check, seleccionados
    	//se limpian los datos del filtro
        JCheckBox j1=new JCheckBox();    
        JComboBox jc = new JComboBox();
        JTextField jtf = new JTextField();
    	int num_datos=jPanelCampo1.getComponentCount()/numColumnas;
    	while (m<(num_datos-1)){
    		j1=(JCheckBox)jPanelCampo1.getComponent(m *numColumnas +columnaCheckCampo);
    		if (j1.isSelected()){ 
    			j1.setSelected(false);
    		}
    		
    		jc = (JComboBox)jPanelCampo1.getComponent(m *numColumnas +columnaOperador);
    		jc.setSelectedIndex(0);
    		
    		jtf = (JTextField)jPanelCampo1.getComponent(m * numColumnas + columnaValor);
    		jtf.setText("");
    		
    		m++;

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
    	
    }
    
    public String getTitle()
    {
      return this.getName();
    }
    
    public String getInstructions()
    {
        return I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel04.instructions");
    }
    
    /**
     * Este metodo valida y activa o no el boton de next
     */
    public boolean isInputValid()
    {
    	boolean validado=false;   	
    	
    	if(estado==true){
	    	validado= true;
	    }else{
	    	validado=false;
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
    
    private JRadioButton realizarRadioEtiqueta(int tamanio, ArrayList claves, ArrayList clavesEliminadas){
    	
    	final JRadioButton radioEtiqueta = new JRadioButton();
    	if(radioEtiqueta != null){

    		radioEtiqueta.setText(I18N.get("GeorreferenciacionExterna", "georreferenciacionExterna.panel04.etiqueta"));
    		if ((claves.size()>0 && claves.size()<=2) && (clavesEliminadas.contains(claves.get(tamanio)))){
    			radioEtiqueta.setEnabled(false);
        	}else{
        		radioEtiqueta.setEnabled(false);
        	}
    		
    		// se añade evento para controlar que al seleccionar o no el check se active o no
        	// el radio asociado al filtro
    		radioEtiqueta.addActionListener(new java.awt.event.ActionListener()
            {
        		public void actionPerformed(java.awt.event.ActionEvent e)
		        {
		        	comprobacionFiltroEstablecido();
		        }
            });
    	}
    	// se añade el radio para que forme parte del grupo y solo pueda estar seleccionado uno
    	if(grupoRadio != null){
    		grupoRadio.add(radioEtiqueta);
    	}
    	else{
    		grupoRadio = new ButtonGroup();
    		grupoRadio.add(radioEtiqueta);
    	}
    	return radioEtiqueta;
    }
    
    private JComboBox realizarFiltroOperador(String type, final int fila){
    	final JComboBox comboboxOperador = new JComboBox();
    	if (comboboxOperador != null){
    		 
    		if(type.equals("VARCHAR") || type.equals("CHAR") || type.equals("LONGVARCHAR")){
    			comboboxOperador.addItem("");
    			comboboxOperador.addItem(FiltroData.TEXTO_COINCIDIR);
    			comboboxOperador.addItem(FiltroData.TEXTO_PATRON);
    			comboboxOperador.addItem(FiltroData.TEXTO_IGUAL);
    		}
    		else if (type.equals("INTEGER") || type.equals("NUMERIC") ||
    				type.equals("DECIMAL") || type.equals("SMALLINT") ||
    				type.equals("FLOAT") || type.equals("REAL") ||
    				type.equals("DOUBLE") || type.equals("BIGINT")){
    			comboboxOperador.addItem("");
    			comboboxOperador.addItem(FiltroData.NUMERICO_COINCIDIR);
    			comboboxOperador.addItem(FiltroData.NUMERICO_MAYOR);
    			comboboxOperador.addItem(FiltroData.NUMERICO_MAYOR_IGUAL);
    			comboboxOperador.addItem(FiltroData.NUMERICO_MENOR);
    			comboboxOperador.addItem(FiltroData.NUMERICO_MENOR_IGUAL);
    			
    		}
    		else if(type.equals("DATE")){
    			comboboxOperador.addItem("");
    			comboboxOperador.addItem(FiltroData.NUMERICO_IGUAL);
    			comboboxOperador.addItem(FiltroData.NUMERICO_MAYOR);
    			comboboxOperador.addItem(FiltroData.NUMERICO_MENOR);
    		}
    		comboboxOperador.setEnabled(false);
    		
    		// se añade evento para controlar que al seleccionar o no el check se active o no
        	// el radio asociado al filtro

    		comboboxOperador.addActionListener(new java.awt.event.ActionListener()
            {
        		public void actionPerformed(java.awt.event.ActionEvent e)
		        {
        			gestionFiltroOperador(comboboxOperador.getSelectedIndex(), fila);
		        	comprobacionFiltroEstablecido();
		        }
            });
    		
    	}

    	return comboboxOperador;
    }
    
    private void gestionFiltroOperador(int indiceSeleccionado, int fila){
    	int columnaValor = 3;
    	int numColumnas = 5;
    	
    	if(indiceSeleccionado != 0){
    		
    		((JTextField)jPanelCampo1.getComponent(fila*numColumnas + columnaValor)).setEnabled(true);
    		((JTextField)jPanelCampo1.getComponent(fila*numColumnas + columnaValor)).setText("");
    	}
    	else{
    		((JTextField)jPanelCampo1.getComponent(fila*numColumnas + columnaValor)).setEnabled(false);
    	}
    	jPanelCampo1.repaint();	
    }
    private JTextField realizarFiltroValor(final String type){
    	final JTextField textFieldValor = new JTextField();
    	if (textFieldValor != null){
   		 
    		textFieldValor.setEnabled(false);
    		// se añade evento para controlar que al seleccionar o no el check se active o no
        	// el radio asociado al filtro
    		

    		textFieldValor.addCaretListener(new CaretListener()
	        {
		        public void caretUpdate(CaretEvent evt)
		        {
		        	
		        	comprobacionFiltroEstablecido();
		        
		        }
            });

    		
    	}
    	
    	return textFieldValor;
    }

    		
   private JCheckBox realizarChkBoxes(int tamanio, ArrayList claves, ArrayList clavesEliminadas){	   
	   chkbox = new JCheckBox();	   
	   if (chkbox != null)
	   {	    		
        	chkbox.setLocation(new Point(238, y));
        	chkbox.setMnemonic(KeyEvent.VK_UNDEFINED);
        	chkbox.setSize(new java.awt.Dimension(21,21));	        	
        	if ((claves.size()>0 && claves.size()<=2) && (clavesEliminadas.contains(claves.get(tamanio)))){
        		chkbox.setText(claves.get(tamanio).toString());
				chkbox.setEnabled(false);
        	}else{
        		chkbox.setText(claves.get(tamanio).toString());
        	}
        	
        	// se añade evento para controlar que al seleccionar o no el check se active o no
        	// el radio asociado al filtro
        	chkbox.addActionListener(new java.awt.event.ActionListener()
            {
        		public void actionPerformed(java.awt.event.ActionEvent e)
		        {
		        	gestionCheckBox(chkbox);
		        }
            });
        	
	   } 
	   y+=10;	   
	   return chkbox;
    }   
   
   private void gestionCheckBox(JCheckBox chkbox){
       	
   	   JCheckBox j=new JCheckBox();

   	   // del panel jPanelCampo1 se obtiene un arraylist con todos los objetos
   	   // del 0 al 4 comprende la fila uno, del 5 al 9 comprende la fila 2.....
   	   int numColumnas = 5;
   	   int filaActual = 0;
   	   int columnaOperador = 2;
 	   int columnaValor = 3;
   	   int columnaRadioEtiqueta = 4;
   	  
   		  
	   for(int i=0; i<combos.size(); i++){
		   filaActual = i* numColumnas;
		   j=(JCheckBox)jPanelCampo1.getComponent(filaActual);
		   if(j.isSelected()){
			   ((JRadioButton)jPanelCampo1.getComponent(filaActual + columnaRadioEtiqueta)).setEnabled(true);
			   ((JComboBox)jPanelCampo1.getComponent(filaActual + columnaOperador)).setEnabled(true);
			   String valorSeleccionadoCombo = (String)((JComboBox)jPanelCampo1.getComponent(filaActual + columnaOperador)).getSelectedItem();
			   if(!valorSeleccionadoCombo.equals("")){
				   ((JTextField)jPanelCampo1.getComponent(filaActual + columnaValor)).setEnabled(true);
			   }
			   else{
				   ((JTextField)jPanelCampo1.getComponent(filaActual + columnaValor)).setEnabled(false);
				   ((JTextField)jPanelCampo1.getComponent(filaActual + columnaValor)).setText("");
			   }
		   }
		   else{
			   ((JRadioButton)jPanelCampo1.getComponent(filaActual + columnaRadioEtiqueta)).setSelected(false);
			   ((JRadioButton)jPanelCampo1.getComponent(filaActual + columnaRadioEtiqueta)).setEnabled(false);
			   ((JComboBox)jPanelCampo1.getComponent(filaActual + columnaOperador)).setEnabled(false);
			   ((JTextField)jPanelCampo1.getComponent(filaActual + columnaValor)).setEnabled(false);
			   
		   }
	   }
	   jPanelCampo1.repaint();		

	   comprobacionFiltroEstablecido();
   }
   
   
	public void adjustmentValueChanged(AdjustmentEvent e) {		
		jPanelCampo1.setLocation((-e.getValue()*2), jPanelCampo1.getY());
		jPanelCampo1.repaint();		
	}
}
