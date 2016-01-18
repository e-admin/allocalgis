/**
 * FileGeoreferenceXYPanel02.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.transform.JDOMSource;
import org.jdom.xpath.XPath;

import reso.jump.joinTable.JoinTable;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.EdicionUtils;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.georeference.QueryXML;
import com.geopista.ui.plugin.georeference.beans.PoliciaCoincidencias;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
/**
 * 
 * @author jvaca
 * Modificaciones rubengomez
 * Ventana accesible desde la opción georreferenciación del primer panel.
 * Es el núcleo de la georreferenciación.
 * Se realiza la consulta de campos para enviar al geocoder, se envia, se procesa
 * y se devuelve almacenan en beans.
 * Toda la información se almacena en 
 * localizado (Coincidencias únicas encontradas en el geocoder)
 * noLocalizado (Sin coincidencias en el geocoder)
 * localizadoMultiple (Coincidencias múltiples en el geocoder) 
 *
 */

public class FileGeoreferenceXYPanel02 extends javax.swing.JPanel implements WizardPanel
{
    
    private String localId = null;
    private String nextID = null;
    private PlugInContext   context;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard  = aplicacion.getBlackboard();
    private GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
    private WizardContext wizardContext;
    private JLabel jLabelCoordX = null;
    private JLabel jLabelCoordY = null;
    private JComboBox jComboBoxCoordX = null;
    private JComboBox jComboBoxCoordY = null;
    private boolean correct=false; 

    
    //estas variables las tenemos que pasar a traves de los paneles
    private String layerName=null;
    private int radioB=0;
    private int radioB1=0;
    private GeopistaLayer capa=null;
    private JoinTable jt =null;
    
	private JPanel jPanelInfo = null;
	private JPanel jPanelReferences = null;
	private JPanel jPanelDatos = null;
    
    public FileGeoreferenceXYPanel02(String id, String nextId, PlugInContext context2) {
        
    	Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);
        this.context=context2;
        this.nextID = nextId;
        this.localId = id;
        
        try
        {   
	        setName(I18N.get("Georreferenciacion", "georeference.panelxy02.titlePanel"));     	
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
				new GridBagConstraints(0,1,1,1, 1, 0.95,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    	    		
    }
    
    private JPanel getJPanelDatos(){
    	
    	if (jPanelDatos == null){
    		
    		jPanelDatos  = new JPanel();
    		jPanelDatos.setLayout(new GridBagLayout());
    		
    		jPanelDatos.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("Georreferenciacion","georeference.panel02.selectreferences"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
    		
    		jPanelDatos.add(getJPanelReferences(), 
    				new GridBagConstraints(0,0,1,1, 0.8, 0.8,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(10,10,10,10),0,0));
    	}
    	return jPanelDatos;
    }
    
    private JPanel getJPanelReferences(){
    	
    	if (jPanelReferences == null){
    		
    		jPanelReferences  = new JPanel();
    		jPanelReferences.setLayout(new GridBagLayout());
    		
    		jLabelCoordY = new JLabel();
            jLabelCoordY.setText(I18N.get("Georreferenciacion", "georeference.panel02.labelcoordy"));

            jLabelCoordX = new JLabel();
            jLabelCoordX.setText(I18N.get("Georreferenciacion","georeference.panel02.labelcoordx"));
             
            jPanelReferences.add(jLabelCoordX,
	      			  new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
	      					  GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
            
            jPanelReferences.add(getJComboBoxCoordX(),
	      			  new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.WEST,
	      					  GridBagConstraints.HORIZONTAL, new Insets(0,0,0,20),0,0));
            
            jPanelReferences.add(jLabelCoordY,
	      			  new GridBagConstraints(0,1,1,1, 1, 1,GridBagConstraints.CENTER,
	      					  GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
            
            jPanelReferences.add(getJComboBoxCoordY(),
	      			  new GridBagConstraints(1,1,1,1, 1, 1,GridBagConstraints.WEST,
	      					  GridBagConstraints.HORIZONTAL, new Insets(0,0,0,20),0,0));
            
    	}
    	return jPanelReferences;
    }
    
    private JPanel getJPanelInfo(){

		if (jPanelInfo == null){

			jPanelInfo   = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("Georreferenciacion","georeference.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("Georreferenciacion","georeference.panelxy02.instructions"));
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
    	jt=(JoinTable) wizardContext.getData("JoinTableObjet");    
    	
    	List lstCalles = new ArrayList();
        lstCalles = jt.getFieldNames();
    	EdicionUtils.cargarLista(getJComboBoxCoordX(), (ArrayList)lstCalles);
           
        List lstNumeros = new ArrayList();
        lstNumeros = jt.getFieldNames();
        EdicionUtils.cargarLista(getJComboBoxCoordY(), (ArrayList)lstNumeros);
    	
        this.nextID = "6";
    }
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {   

    	wizardContext.setData("georeference",this);
    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), 
    			null);

    	progressDialog.setTitle(I18N.get("Georreferenciacion", "georeference.panel02.progressDialog.dataProcessLabel"));
    	progressDialog.report(I18N.get("Georreferenciacion", "georeference.panel02.progressDialog.dataProcessLabel"));
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

    						/*
    						 * Almacenamos todos los datos y las respuestas del geocoder en el ArrayList
    						 * de Beans, y lo guardamos en el WizardContext para su posterior tratamiento.
    						 */
    						FileGeoreferenceXYPanel02 fgp=(FileGeoreferenceXYPanel02)wizardContext.getData("georeference");   	
    						ArrayList listadoCompleto = (ArrayList)wizardContext.getData("datos");
    						Iterator iteraListadoCompleto = listadoCompleto.iterator();
    						GeopistaLayer capa = (GeopistaLayer)wizardContext.getData("layer");

    						ArrayList localizado = new ArrayList();
    						
    						ArrayList noLocalizado = new ArrayList();
    						int elementosProcesados = 0;

    						while (iteraListadoCompleto.hasNext()){
    							Hashtable data = (Hashtable)iteraListadoCompleto.next();
    							ArrayList coincidencias = new ArrayList();

    							elementosProcesados++;

    							try{
    								Double coordX = Double.valueOf((String) data.get(getJComboBoxCoordX().getSelectedItem()));
    								Double coordY = Double.valueOf((String) data.get(getJComboBoxCoordY().getSelectedItem()));
    								data.remove(getJComboBoxCoordX().getSelectedItem());
    								data.remove(getJComboBoxCoordY().getSelectedItem());
    								Point coordinates = new GeometryFactory().createPoint(new Coordinate(coordX,coordY));
    								data.put("GEOMETRY", coordinates);
    								localizado.add(data);
    							}
    							catch(Exception e){
    								noLocalizado.add(data);
    							}
    							
    							progressDialog.report(elementosProcesados, listadoCompleto.size(),
    							"");

    						}
    						wizardContext.setData("localizado",localizado);
    						
    						wizardContext.setData("noLocalizado",noLocalizado);

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
        return I18N.get("Georreferenciacion", "georeference.panel02.instructions");
    }
    /**
     * Este metodo valida y activa o no el boton de next
     */
    public boolean isInputValid()
    {
    	boolean validado=false;

    	if(getJComboBoxCoordY().getSelectedItem()!="" && getJComboBoxCoordX().getSelectedItem()!="" 
    		|| getJComboBoxCoordY().getSelectedIndex()!=0  && getJComboBoxCoordX().getSelectedIndex()!=0 )
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
     * This method initializes cmbCalle	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJComboBoxCoordX()
    {
    	if (jComboBoxCoordX == null)
    	{            
    		jComboBoxCoordX = new JComboBox();

    		jComboBoxCoordX.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
    				wizardContext.inputChanged();
    			}
    		});
    	}
    	return jComboBoxCoordX;
    }

    /**
     * This method initializes cmbNumero	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJComboBoxCoordY()
    {
    	if (jComboBoxCoordY == null)
    	{
    		jComboBoxCoordY = new JComboBox();

    		jComboBoxCoordY.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
    				wizardContext.inputChanged();
    			}
    		});
    	}
    	return jComboBoxCoordY;
    }
       
    public GeopistaLayer getCapa()
    {
        return capa;
    }

    public void setCapa(GeopistaLayer capa)
    {
        this.capa = capa;
    }

    public JoinTable getJt()
    {
        return jt;
    }

    public void setJt(JoinTable jt)
    {
        this.jt = jt;
    }

    public String getLayerName()
    {
        return layerName;
    }

    public void setLayerName(String layerName)
    {
        this.layerName = layerName;
    }

    public int getRadioB()
    {
        return radioB;
    }

    public void setRadioB(int radioB)
    {
        this.radioB = radioB;
    }

    public int getRadioB1()
    {
        return radioB1;
    }

    public void setRadioB1(int radioB1)
    {
        this.radioB1 = radioB1;
    }
  
    /**
     * Método que recibe el element respuesta del GEOCODER
     * (Element root) y devuelve un ArrayList con las coincidencias que ha encontrado
     * extrayendo la información por medio de una consulta de XPath
     * @param calle
     * @param numeroPolicia
     * @param root element respuesta del geocoder 
     * @return arrayList con las coincidencias del nodo
     */
    public ArrayList processXML(String calle,String numeroPolicia,Element root){
    	String mismaCalle = "geocodificacion/geocodificacion.vias/gml:Collection/gml:featureMember/vias/geocodificacion.street='"+calle+"'";
    	String mismoNumero = "geocodificacion/geocodificacion.streetNumber='"+numeroPolicia+"'";
    	String nodoQuery ="/ResultCollection/gml:featureMember["+mismaCalle+" and "+mismoNumero+"]";
    	try {
    		if ((XPath.selectNodes(root,nodoQuery) instanceof ArrayList)){
			ArrayList temp = (ArrayList) XPath.selectNodes(root,nodoQuery);
			if (temp != null)
        		return temp;
    		}
		} catch (JDOMException e) {			
			e.printStackTrace();
		}
		return null;
    }

    /**
     * Método que recibe el element consulta, se conecta el Geocoder 
     * y recibe el element respuesta
     * @param queryXML
     * @return
     */
    private Element responseQuery(Element queryXML){
    	Element responseXML = null;
    	Document doc=new Document(queryXML);
        try{
            JDOMSource domSource=new JDOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            String cadenaXML=writer.toString();
            URL url = new URL( AppContext.getApplicationContext().getString("geopista.WFS.Query"));
            URLConnection con = url.openConnection();
            con.setDoOutput( true );
            con.setDoInput( true );
            
            PrintStream ps = new PrintStream( con.getOutputStream());
            ps.print( cadenaXML );             
            ps.close();
            InputStream is = con.getInputStream();
            int c = 0;
            StringBuffer sb = new StringBuffer();
            while ( (c = is.read()) >= 0 ) {
                sb.append( (char)c );
            }
            String  gml = sb.toString();
            
            try{
                SAXBuilder builder = null;
                doc = null;
                builder = new SAXBuilder();
                doc = builder.build(new StringReader(gml));
                responseXML=doc.getRootElement();
            }catch(Exception e){
            	e.printStackTrace();
            }          
        }catch(Exception e){
            	e.printStackTrace();
        }
    	
		return responseXML;
    	
    }
    /**
     * Método que recibe la calle y el numero de policia , genera el QueryXML, llama al 
     * método processXML que le devuelve la respuesta del geocoder ya clasificada.
     * @param calle 
     * @param numeroPolicia
     * @return ArrayList de PoliciaCoincidencias
     */
    public ArrayList procesaConsulta(String calle, String numeroPolicia){
		Element root =new Element("GetFeature",Namespace.getNamespace("wfs","http://www.opengis.net/wfs"));
		root.setAttribute("outputFormat","GML2");
        root.addNamespaceDeclaration(Namespace.getNamespace("gml","http://www.opengis.net/gml"));
        root.addNamespaceDeclaration(Namespace.getNamespace("ogc","http://www.opengis.net/ogc"));
        QueryXML query= new QueryXML(calle,numeroPolicia,new Integer(aplicacion.getIdMunicipio()).toString());
        root.addContent(query);
        Element respuesta = responseQuery(root);
        
        ArrayList respuestaElements = processXML(calle,numeroPolicia,respuesta);
        ArrayList devuelve = new ArrayList();
        if (respuestaElements != null){
        	Iterator respuestaElementsItera = respuestaElements.iterator();
        	while (respuestaElementsItera.hasNext()){
        		Element analiza = (Element)respuestaElementsItera.next();

        		try {
        			String elemCalle = ((Element)XPath.selectSingleNode(analiza,"//geocodificacion.street")).getText();
        			String elemTipo = ((Element)XPath.selectSingleNode(analiza,"//geocodificacion.streetType")).getText();
        			String elemNumero = ((Element)XPath.selectSingleNode(analiza,"//geocodificacion.streetNumber")).getText();
        			String separador = ((org.jdom.Attribute)XPath.selectSingleNode(analiza,"//gml:coordinates/@cs")).getValue();
        			String coord = ((Element)XPath.selectSingleNode(analiza,"//gml:coordinates")).getText();
        			String[] coordSeparadas = coord.split(separador);
        			PoliciaCoincidencias coincide = new PoliciaCoincidencias(elemCalle,elemTipo);
        			coincide.setDatos(elemNumero, Double.parseDouble(coordSeparadas[0]), Double.parseDouble(coordSeparadas[1]));
        			devuelve.add(coincide);
        		} catch (JDOMException e) {
        			e.printStackTrace();
        		}

        	}
        }
        
    	return devuelve;
    }

}  
