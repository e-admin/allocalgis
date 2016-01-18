/**
 * FileGeoreferenceXYPanel06.java
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
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import reso.jump.joinTable.JoinTable;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.Column;
import com.geopista.feature.DateDomain;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.NumberDomain;
import com.geopista.feature.SchemaValidator;
import com.geopista.feature.StringDomain;
import com.geopista.feature.Table;
import com.geopista.feature.ValidationError;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author rubengomez
 *
 *	Panel que genera las features a través de los datos almacenados anteriormente. 
 *	Presenta los errores encontrados por el validador de features o por la captura de errores.
 *
 */
public class FileGeoreferenceXYPanel06 extends javax.swing.JPanel implements WizardPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String localId = null;
    private String nextID = null;
    private PlugInContext   context;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private WizardContext wizardContext;  
	private JTextArea jTextArea = null;
	private JLabel jLabel = null;    
    private AbstractValidator validator = null;
    private ArrayList localizado = null;  
    private GeopistaLayer capa = null;  
    private JoinTable joinTable = null;
    private JScrollPane jScrollPanel = null;
    private int noEncontrados = 0;
	private JPanel jPanelDatos = null;
	private JPanel jPanelInfo = null;
	private HashMap dataType;
	private ArrayList noLocalizado = null;
    
    
    public FileGeoreferenceXYPanel06(String id, String nextId, PlugInContext context2) {

		Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georeference.language.Georreferenciacioni18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("Georreferenciacion",bundle);
    	    
        this.context=context2;
        this.nextID = nextId;
        this.localId = id;
        try
        {
            setName(I18N.get("Georreferenciacion","georeference.panel06.titlePanel"));
            initialize();
        }catch(Exception e)
        {
            e.printStackTrace();
        }   
    }    
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
					(null,I18N.get("Georreferenciacion","georeference.panel06.errors"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
    		
    		jPanelDatos.add(getJLabel(), 
    				new GridBagConstraints(0,0,1,1, 1, 0.05,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

    		jPanelDatos.add(getJScrollPanel(), 
    				new GridBagConstraints(0,1,1,1, 1, 0.95,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    		
    	}
    	return jPanelDatos;
    }
    
    private JPanel getJPanelInfo(){

		if (jPanelInfo == null){

			jPanelInfo     = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("Georreferenciacion","georeference.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("Georreferenciacion","georeference.Info6"));
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
    
	public void add(InputChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void enteredFromLeft(Map dataMap) {
		
		if((String)wizardContext.getData("completar") == null){
			localizado = (ArrayList) wizardContext.getData("localizado");
			noLocalizado  = (ArrayList) wizardContext.getData("noLocalizado");
			capa = (GeopistaLayer)wizardContext.getData("layer");
			if (capa.isLocal()){
				setDataType(localizado);
			}
			try {
				addLayerAttribute();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		else{
			capa = (GeopistaLayer)wizardContext.getData("layer");
			boolean editable = capa.isEditable();
			if (capa.isLocal()){
				setDataType(localizado);
				joinTable = (JoinTable)wizardContext.getData("JoinTableObjet");
				
				try {

					joinTable.join(capa,(new Integer(wizardContext.getData("indiceCapa").toString())).intValue());
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				String campoCapa = (String)wizardContext.getData("indexCapa");
				String campoFichero = (String)wizardContext.getData("indexFichero");
				capa.getLayerManager().setFiringEvents(false);
				ArrayList datos = (ArrayList)wizardContext.getData("datos");
				List lista = (List)capa.getFeatureCollectionWrapper().getFeatures();
				Iterator recorreDatos2 = datos.iterator();
				boolean encontrado = false;
				
				while(recorreDatos2.hasNext()){// recorre todos los datos
					Hashtable lineaDatos = (Hashtable)recorreDatos2.next();
					Iterator recorreFeatures = lista.iterator();
					while(recorreFeatures.hasNext()){//recorre todas las features
						GeopistaFeature feature = (GeopistaFeature)recorreFeatures.next();
						if(lineaDatos.get(campoFichero).equals(feature.getAttribute(campoCapa).toString())){// Si coincide el valor con el fichero
							encontrado = true;
							break;
	
						}
						
					}
					if (!encontrado)
						noEncontrados++;
				}
				if (noEncontrados == 0){
					this.jTextArea.append(I18N.get("Georreferenciacion", "georeference.panel06.correctRefreshedFeatures"));
				}else{
					this.jTextArea.append(noEncontrados+" "+I18N.get("Georreferenciacion", "georeference.panel06.badRefreshedFeatures")+"\n");
							this.jTextArea.append(I18N.get("Georreferenciacion", "georeference.panel06.badKey"));
				}
				
			}
			
			else{
			
				String campoCapa = (String)wizardContext.getData("indexCapa");
				String campoFichero = (String)wizardContext.getData("indexFichero");

				capa.getLayerManager().setFiringEvents(false);
				ArrayList datos = (ArrayList)wizardContext.getData("datos");
				List lista = (List)capa.getFeatureCollectionWrapper().getFeatures();				
				Iterator recorreDatos2 = datos.iterator();		
				while(recorreDatos2.hasNext()){// recorre todos los datos
					Hashtable lineaDatos = (Hashtable)recorreDatos2.next();
					Iterator recorreFeatures = lista.iterator();
					while(recorreFeatures.hasNext() && lineaDatos.get(campoFichero) != null){//recorre todas las features
						GeopistaFeature feature = (GeopistaFeature)recorreFeatures.next();
						if(lineaDatos.get(campoFichero).equals(feature.getAttribute(campoCapa).toString())){// Si coincide el valor con el fichero
							String  error = (String)lineaDatos.get(campoFichero);
							Enumeration listaCamposFichero = lineaDatos.keys();
							while(listaCamposFichero.hasMoreElements()){
								String valor = (String)listaCamposFichero.nextElement();
								if(feature.getSchema().hasAttribute(valor)){
									//necesito saber aqui el cast a lo que voy a transformar los datos que se encuentran en la capa
									// Al cambiar el esquema de la feature, no permite el uso de setAttribute
									try {
										if(feature.getSchema().getAttributeType(valor).equals(AttributeType.STRING)){
											feature.setAttribute(valor,(String)lineaDatos.get(valor));
										}
										if(feature.getSchema().getAttributeType(valor).equals(AttributeType.INTEGER)){
											feature.setAttribute(valor,new Integer((String)lineaDatos.get(valor)));
										}
										if(feature.getSchema().getAttributeType(valor).equals(AttributeType.DOUBLE)){
											feature.setAttribute(valor,(Double)lineaDatos.get(valor));
										}
										if(feature.getSchema().getAttributeType(valor).equals(AttributeType.DATE)){
											feature.setAttribute(valor,getDate((String)lineaDatos.get(valor)));
										}
									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										this.jTextArea.append(I18N.get("Georreferenciacion","georeference.panel06.badAttributes")+" "+error+"\n");
									}
								}
							}
						}
						
					}
				}
				}
			capa.setEditable(editable);
			}
    	if(this.jTextArea.getText().length() == 0){
    		this.jTextArea.append(I18N.get("Georreferenciacion","georeference.panel06.textMessageRight"));
    	}
    	this.jTextArea.append("\n\n");
    	if (localizado!=null){
			this.jTextArea.append(localizado.size() + " " + I18N.get("Georreferenciacion","georreference.panelxy06.message1") + "\n");    			
		}
		if (noLocalizado!=null){    			
			this.jTextArea.append(noLocalizado.size() + " " + I18N.get("Georreferenciacion","georreference.panelxy06.message2")+ "\n");
		}
		
	}

	public void exiting() {
		// TODO Auto-generated method stub
		
	}

	public void exitingToRight() throws Exception {
		// TODO Auto-generated method stub
		capa.getLayerManager().setFiringEvents(true);
	}

	public String getID() {
		// TODO Auto-generated method stub
		return localId;
	}

	public String getInstructions() {
		// TODO Auto-generated method stub
		return "Resultados";
	}

	public String getNextID() {
		// TODO Auto-generated method stub
		return nextID;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return this.getName();
	}

	public boolean isInputValid() {
		// TODO Auto-generated method stub
		return true;
	}

	public void remove(InputChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void setNextID(String nextID) {
		// TODO Auto-generated method stub
		
	}

	public void setWizardContext(WizardContext wd) {
		// TODO Auto-generated method stub
		wizardContext =wd;
	}
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setBounds(new Rectangle(46, 90, 492, 288));
		}
		return jTextArea;
	}
    private JScrollPane getJScrollPanel()
    {
        if (jScrollPanel == null)
        {
            jScrollPanel = new JScrollPane(getJTextArea());
            jScrollPanel.setBounds(new Rectangle(46, 90, 492, 288));
            jScrollPanel.setAutoscrolls(true);
            jScrollPanel.createHorizontalScrollBar();
            
            
        }
        return jScrollPanel;
    }
	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(46, 44, 246, 33));
			jLabel.setText(I18N.get("Georreferenciacion","georeference.panel06.errorLabel"));
		}
		return jLabel;
	}
	public ArrayList validateFeature(GeopistaFeature feature,Layer capaActual){
		ArrayList actualError = new ArrayList();
        validator = new SchemaValidator(null);

        if (!validator.validateFeature(feature))
               
        {
        	validator.getErrorCount();
            Iterator errorsIterator = validator.getErrorListIterator();
            while (errorsIterator.hasNext())
            {
            	ValidationError error = (ValidationError) errorsIterator.next(); 
                actualError.add(error);

                AttributeType attributeType = feature
                        .getSchema()
                        .getAttributeType(
                        		error.attName);

                if (attributeType == AttributeType.GEOMETRY)
                {
                    actualError.add("geometryError");
                    break;
                }
            }
            if(actualError.isEmpty())
            	actualError.add("validationError");
            
        }
		return actualError;
	}
	
	private void setDataType(ArrayList localizado){
		
		if (dataType == null){
			dataType = new HashMap();
			Enumeration listaClaves = ((Hashtable)localizado.get(0)).keys();
			while (listaClaves.hasMoreElements()){
				String key = (String) listaClaves.nextElement();
				AttributeType atributo = null;
				if (key.equalsIgnoreCase("GEOMETRY")){
					atributo = AttributeType.GEOMETRY;
				}
				else{
					atributo = typeOfData((String)((Hashtable)localizado.get(0)).get(key));
				}
				dataType.put(key, atributo);
			}
		}

	}


	public void addLayerAttribute() throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{

		ArrayList fieldNames=new ArrayList();
		ArrayList fieldTypes=new ArrayList();

		validator = new SchemaValidator(null);

		GeopistaSchema newSchema=new GeopistaSchema();
		boolean firingEvents=false;
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);


		try{

			if(capa.isLocal() && dataType != null){
				Set valores = dataType.keySet();
				Iterator listaCampos = valores.iterator();

				while (listaCampos.hasNext()){
					String key = (String)listaCampos.next();
					fieldNames.add(key);
					if (dataType.get(key) == null)
						fieldTypes.add(AttributeType.STRING);
					else
						fieldTypes.add(dataType.get(key));
				}
			}
			capa.setEditable(true);
			GeopistaSchema schema=(GeopistaSchema)capa.getFeatureCollectionWrapper().getFeatureSchema();
			if(capa.isLocal() && dataType != null){//SOLO CUANDO LA CAPA ES LOCAL###############################################################
				newSchema=completSchema(schema,fieldNames,fieldTypes);
				Collection featureCollection= new ArrayList();
				ILayerManager layerManager =capa.getLayerManager();
				boolean exitLoop=false;

				Iterator recorreLocalizado = localizado.iterator();

				while(recorreLocalizado.hasNext()){
					GeopistaFeature fNew=null;
					Hashtable dataLine = (Hashtable) recorreLocalizado.next();
					fNew = new GeopistaFeature(newSchema);
					Set values = dataType.keySet();
					Iterator listaCamposDatos = values.iterator();
					while(listaCamposDatos.hasNext()){
						String key= (String)listaCamposDatos.next();
						if(dataType.get(key) == null)
							dataType.put(key, AttributeType.STRING);
						AttributeType attribute=(AttributeType)dataType.get(key);
						if(!dataLine.get(key).equals("")){
							if(attribute.equals(AttributeType.INTEGER)){
								fNew.setAttribute(key,new Integer((String)dataLine.get(key)));
							}else if(attribute.equals(AttributeType.DOUBLE)){
								fNew.setAttribute(key,(Double)dataLine.get(key));
							}else if(attribute.equals(AttributeType.DATE)){
								fNew.setAttribute(key,getDate((String)dataLine.get(key)));
							}else if(newSchema.getAttributeType(key).equals(AttributeType.GEOMETRY)){
								fNew.setGeometry((Geometry) dataLine.get(key));
							}else{
								fNew.setAttribute(key,(String)dataLine.get(key));
							}
						}
					}

					fNew.setLayer(capa);
					ArrayList errores = validateFeature(fNew,capa);

					if (errores.size() > 0){
						String nuevoError = I18N.get("Georreferenciacion","georeference.panel06.insertionError")+" ==> \n";
						this.jTextArea.append(nuevoError);
						Iterator recorreErrores = errores.iterator();
						while (recorreErrores.hasNext()){
							Object aux = recorreErrores.next();
							if (aux instanceof String){
								if(aux.equals("validationError"))
									nuevoError += "\t"+I18N.get("Georreferenciacion","georeference.panel06.validationError");
								else
									nuevoError += "\t"+I18N.get("Georreferenciacion","georeference.panel06.geometryError");

							}
							else{
								ValidationError auxiliar = (ValidationError) aux;
								nuevoError += "\t"+auxiliar.attName+" -> "+auxiliar.message+"";	
							}
							this.jTextArea.append(nuevoError);
						}
					}else
						featureCollection.add(fNew);         
				}
				capa.getFeatureCollectionWrapper().addAll(featureCollection);

			}else {
				Collection featureCollection= new ArrayList();
				GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) capa.getDataSourceQuery().getDataSource();
				firingEvents = capa.getLayerManager().isFiringEvents();
				capa.getLayerManager().setFiringEvents(false);
				Iterator recorreLocalizado = ((ArrayList)localizado).iterator();
				while(recorreLocalizado.hasNext()){
					Hashtable datos = (Hashtable)recorreLocalizado.next();

					newSchema=(GeopistaSchema)schema.clone();
					// Elimino aquellos campos que no pertenecen al esquema
					sesgaAtributos(newSchema,datos);
					GeopistaFeature fNew=null;
					fNew = new GeopistaFeature(newSchema);
					Enumeration listaCamposDatos=datos.keys();
					while(listaCamposDatos.hasMoreElements()){
						String key= (String)listaCamposDatos.nextElement();    
						if (!key.equalsIgnoreCase("GEOMETRY")){
							newSchema.getAttributeType(key);	
							fNew.setAttribute(key, datos.get(key));

							if(datos.get(key).equals("")){
								fNew.setAttribute(key,(String)datos.get(key));
							}
							else{
								if(newSchema.getAttributeType(key).equals(AttributeType.INTEGER)){
									fNew.setAttribute(key,new Integer((String)datos.get(key)));
								}else if(newSchema.getAttributeType(key).equals(AttributeType.DOUBLE)){
									fNew.setAttribute(key,(Double)datos.get(key));
								}else if(newSchema.getAttributeType(key).equals(AttributeType.DATE)){
									fNew.setAttribute(key,getDate((String)datos.get(key)));
								}else if(newSchema.getAttributeType(key).equals(AttributeType.GEOMETRY)){
									fNew.setGeometry((Geometry) datos.get(key));
								}else{
									fNew.setAttribute(key,(String)datos.get(key));
								}}
						}
						else{
							fNew.setGeometry((Geometry) datos.get(key));
						}

					}

					fNew.setLayer(capa);
					ArrayList errores = null;
					errores = validateFeature(fNew,capa);

					if (errores.size() > 0){
						String nuevoError = "";
						nuevoError = I18N.get("Georreferenciacion","georeference.panel06.insertionError")+" ==> ";
						this.jTextArea.append(nuevoError);
						Iterator recorreErrores = errores.iterator();
						while (recorreErrores.hasNext()){
							nuevoError = "\n\t";
							Object aux = recorreErrores.next();
							if (aux instanceof String){
								if(aux.equals("validationError"))
									nuevoError += "\t"+I18N.get("Georreferenciacion","georeference.panel06.validationError");
								else
									nuevoError += "\t"+I18N.get("Georreferenciacion","georeference.panel06.geometryError");

							}
							else{
								ValidationError auxiliar = (ValidationError) aux;
								nuevoError += auxiliar.attName+" -> "+auxiliar.message+"";	
							}
							this.jTextArea.append(nuevoError);


						}
						this.jTextArea.append("\n");
					}else
						featureCollection.add(fNew);                    
				}
				capa.getFeatureCollectionWrapper().addAll(featureCollection);          	
				if(this.jTextArea.getText().length() == 0){
					this.jTextArea.append(I18N.get("Georreferenciacion","georeference.panel06.textMessageRight"));
				}
				progressDialog.report(I18N.get("Georreferenciacion","georeference.panel06.progressDialog.reportLabel"));
				Map driverProperties = geopistaServerDataSource.getProperties();
				Object lastResfreshValue = driverProperties.get(Constantes.REFRESH_INSERT_FEATURES);
				try
				{
					driverProperties.put(Constantes.REFRESH_INSERT_FEATURES, new Boolean(true));

					geopistaServerDataSource.getConnection().executeUpdate(capa.getDataSourceQuery().getQuery(), capa.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
				} finally
				{
					if (lastResfreshValue != null)
					{
						driverProperties.put(Constantes.REFRESH_INSERT_FEATURES, lastResfreshValue);
					} else
					{
						driverProperties.remove(Constantes.REFRESH_INSERT_FEATURES);
					}
				}
			}
			capa.setEditable(false);
		}catch(Exception e){
			this.jTextArea.append(e.toString());
		}finally
		{
			if(!capa.isLocal()){
				capa.getLayerManager().setFiringEvents(firingEvents);
			}
		}

	}
	private void sesgaAtributos(GeopistaSchema newSchema, Hashtable dataLine) {
		List listaAtributos = newSchema.getAttributes();
		Enumeration listaCampos = dataLine.keys();
		String nombreCampo,nombreAtributo;
		boolean noCoincide = true;
		while(listaCampos.hasMoreElements()){
			nombreCampo = (String)listaCampos.nextElement();
			Iterator recorreListaAtributos = listaAtributos.iterator();
			while (recorreListaAtributos.hasNext()){
				com.geopista.feature.Attribute atributo = ((com.geopista.feature.Attribute)recorreListaAtributos.next());
				if (nombreCampo.equals("GEOMETRY")){
					noCoincide = false;
					break;
				}
				else{
					nombreAtributo = (String)atributo.getName();
					if (nombreAtributo.equals(nombreCampo)){
						noCoincide = false;
						break;
					}
					noCoincide = true;
				}
			}
			if(noCoincide)
				dataLine.remove(nombreCampo);
		}
	}

	public Hashtable getType(Hashtable data){
		Hashtable newData = new Hashtable();
		Enumeration listaCampos = data.keys();
		while (listaCampos.hasMoreElements()){
			String campo = (String)listaCampos.nextElement();
			newData.put(campo,typeOfData((String)data.get(campo)));
		}
		return newData;
    	
    }
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
                sdf.parse(s);
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
    public Date getDate(String val){
        Date value=null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            value=sdf.parse(val);
        }catch(Exception e){
            e.printStackTrace();
        }
        return value;
    }
    public GeopistaSchema completSchema(GeopistaSchema schema,ArrayList fieldNames,ArrayList fieldTypes){
        
        List atributo= schema.getAttributes();
        AttributeType attributeType,typeSchema= null;
        fieldNames.remove("");
        //recorremos el schema
        int numOldattr=schema.getAttributeCount();
        Table tableDummy = new Table();
        for(int i=0;i<fieldNames.size();i++){

            String attName=(String)fieldNames.get(i);
            attributeType =(AttributeType)fieldTypes.get(i);
            Iterator atributoIter=atributo.iterator();
            
            try{
                int att=schema.getAttributeIndex(attName);
                typeSchema=schema.getAttributeType(att);
//				En este algoritmo se establecen los atributos de la capa para que se
//              puedan insertar todos los datos del archivo
               
                if(!attributeType.equals(typeSchema)){
    				if (((typeSchema.equals(AttributeType.DOUBLE) || typeSchema.equals(AttributeType.INTEGER)) &&
    					attributeType.equals(AttributeType.DATE) ) ||
    					((attributeType.equals(AttributeType.DOUBLE) || attributeType.equals(AttributeType.INTEGER)) &&
    					typeSchema.equals(AttributeType.DATE))){
    				}else{
	                	if (
	                		attributeType.equals(AttributeType.STRING) ||
	                		(typeSchema.equals(AttributeType.INTEGER) && attributeType.equals(AttributeType.DOUBLE))
	                		){
	                	}
    				}
                }
            }catch(Exception e){
                if (schema.getAttributeByColumn(attName)==null){
                    Domain domainDummy = null;
                    if(attributeType.equals(AttributeType.STRING)){
                        domainDummy= new StringDomain("?[.*]","");
                    }else if(attributeType.equals(AttributeType.INTEGER) || attributeType.equals(AttributeType.LONG )){
                        domainDummy= new NumberDomain("?[-INF:INF]","");
                    }else if(attributeType.equals(AttributeType.DOUBLE) || attributeType.equals(AttributeType.FLOAT)){
                        domainDummy= new NumberDomain("?[-INF:INF]","");
                    }else if(attributeType.equals(AttributeType.DATE)){
                        domainDummy= new DateDomain("?[*:*]","");
                    }else
                        domainDummy= new StringDomain("?[.*]","");
                    
                    Column columnDummy = new Column(attName, "", tableDummy,
                            domainDummy);                  
                    schema.addAttribute(attName, attributeType, columnDummy,
                            GeopistaSchema.READ_WRITE);
                }
         
            }  
        }
        return schema;
    }
}