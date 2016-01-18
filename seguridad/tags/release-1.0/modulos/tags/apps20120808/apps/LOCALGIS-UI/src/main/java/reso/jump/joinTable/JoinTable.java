/**
 * @author Olivier BEDEL
 * 	Laboratoire RESO UMR 6590 CNRS
 * 	Bassin Versant du Jaudy-Guindy-Bizien
 * 	26 oct. 2004
 * 
 */
package reso.jump.joinTable;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

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
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.plugin.georeference.FileGeoreferencePlugIn;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author Olivier BEDEL
 * 	Laboratoire RESO UMR 6590 CNRS
 * 	Bassin Versant du Jaudy-Guindy-Bizien
 * 	26 oct. 2004
 * 
 */
public class JoinTable {
	// TODO: obedel voir pour la definition d'un type enumere
	//static Object DataSourceType =  CSV ;
	
	private JoinTableDataSource dataSource = null;
	private ArrayList fieldNames = null;
	private ArrayList fieldTypes = null;
	private Hashtable table = null;
    private Hashtable tabledata = null;
    private AbstractValidator validator = null;
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private int keyIndex = -1;
	private int fieldCount = 0;
	//	TODO: obedel voir pour la prise en compte du type de source de donnee
	public JoinTable(String filePath ) { 
		dataSource = new JoinTableDataSourceCSV(filePath);
		fieldNames = dataSource.getFieldNames();
		fieldCount = fieldNames.size();
	}
	
	public List getFieldNames()
	{
		return fieldNames;
	}
	public String getFieldName(int indice)
	{
		return (String) fieldNames.get(indice);
	}
	
	public AttributeType getFieldType(int indice)
	{
		return (AttributeType) fieldTypes.get(indice);
	}
    public List getFieldTypes()
    {
        return  fieldTypes;
    }
	
	public int getFieldCount()
	{
		return fieldCount;
	}
	
	public void setKeyIndex(int keyIndex)
	{
		this.keyIndex =keyIndex;
	}
	
	public int getKeyIndex() {
		return keyIndex;
	}
	
	public void build()
	{
		if (keyIndex>-1)
		{
			table = dataSource.buildTable(keyIndex);
			fieldTypes = dataSource.getFieldTypes();
		}
			 
	}
    public void buildMultipleKey()
    {
        if (keyIndex>-1)
        {
            tabledata = dataSource.buildTableMultipleKey(keyIndex);
            fieldTypes = dataSource.getFieldTypes();
        }
             
    }
	
	public void join(GeopistaLayer layer, int attributeIndex) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		layer.setEditable(true);
		
		// rajout des attributs de la table dans la couche
        GeopistaSchema schema;
		String nomChamp;
        FileGeoreferencePlugIn refePlug=null;
		String suffixe="";
		int nbOldAttributes;
        validator = new SchemaValidator(null);
        GeopistaSchema newSchema=new GeopistaSchema();
        boolean firingEvents=false;
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        
        try{
            if(layer.isLocal()){
                schema = (GeopistaSchema) layer.getFeatureCollectionWrapper().getFeatureSchema();
                GeopistaSchema oldSchema=(GeopistaSchema) schema.clone();
        		nbOldAttributes = schema.getAttributeCount();
                Table tableDummy = new Table();
        		for (int i=0; i<fieldNames.size();i++) {
        			if (i!=keyIndex){
        				nomChamp = (String) fieldNames.get(i);
                        try{
                            int att=schema.getAttributeIndex(nomChamp);
                        }catch(Exception e){
                            AttributeType atributeType = (AttributeType) fieldTypes.get(i);
                            String atributeName=(String)fieldNames.get(i);
                            if (schema.getAttributeByColumn(atributeName)==null){
                                Domain domainDummy = null;
                                if(atributeType.equals(AttributeType.STRING)){
                                    domainDummy= new StringDomain("?[.*]","");
                                }else if(atributeType.equals(AttributeType.INTEGER) || atributeType.equals(AttributeType.LONG )){
                                    domainDummy= new NumberDomain("?[-INF:INF]","");
                                }else if(atributeType.equals(AttributeType.DOUBLE) || atributeType.equals(AttributeType.FLOAT)){
                                    domainDummy= new NumberDomain("?[-INF:INF]","");
                                }else if(atributeType.equals(AttributeType.DATE)){
                                    domainDummy= new DateDomain("?[*:*]","");
                                }else
                                    domainDummy= new StringDomain("?[.*]","");
                                
                                Column columnDummy = new Column(atributeName, "", tableDummy,
                                        domainDummy);                  
                                schema.addAttribute(atributeName, atributeType, columnDummy,
                                        GeopistaSchema.READ_WRITE);
                            }
                        }
        			}
        		}
                GeopistaFeature f, fNew;
        		String keyValue;
        		Object value;
        		ArrayList newFeatures;
        		String[] valeurs;
        		boolean boOldSchema=false;
        		FeatureCollection fc = layer.getFeatureCollectionWrapper();
                
        		List features = fc.getFeatures(); 
                ILayerManager layerManager =layer.getLayerManager();
                
                
        		newFeatures = new ArrayList(features.size());
        		for (Iterator i = features.iterator(); i.hasNext();){
                    
                    fNew=new GeopistaFeature(schema);
                    
                    f = (GeopistaFeature) i.next();
                    
        			fNew.setAttributes(new Object[schema.getAttributeCount()]);
        			int j=0;
        			while(j<nbOldAttributes){
        				fNew.setAttribute(j, f.getAttribute(j));
        				j++;
        			}
        			keyValue = fNew.getString(attributeIndex).trim();
        			valeurs = (String[]) table.get(keyValue);
                    if (valeurs != null){
                        for (j=0; j<fieldCount; j++){
            				if (j!=keyIndex) {
            					if(valeurs[j]!=null)
            						value = castValue((String) valeurs[j], (AttributeType) fieldTypes.get(j));
            					else 
            						value =null;					
            					fNew.setAttribute((String) fieldNames.get(j), value);
            				}
            			}
                        boolean validateResult = false;
                        boolean geometryError = false;
                        if (!(validateResult = validator
                                .validateFeature(fNew)))
                        {
                            Iterator errorsIterator = validator
                                    .getErrorListIterator();
                            while (errorsIterator.hasNext())
                            {
                                ValidationError actualError = (ValidationError) errorsIterator
                                        .next();
            
                                AttributeType attributeType = fNew
                                        .getSchema()
                                        .getAttributeType(
                                                actualError.attName);
            
                                if (attributeType == AttributeType.GEOMETRY)
                                {
                                    geometryError = true;
                                    break;
                                }
                            }
                            if (geometryError == false)
                            {
                                boolean resultAttributes = GeopistaValidatePlugin.showFeatureDialog(
                                        fNew, layer);
                                if (resultAttributes == false)
                                {
                                    Object[] possibleValues = { aplicacion.getI18nString("cancel.featureac"),
                                            aplicacion.getI18nString("cancel.allfeatures") };
                                    int cancelproces = JOptionPane
                                            .showOptionDialog(
                                                    aplicacion.getMainFrame(),
                                                    aplicacion
                                                            .getI18nString("cancelatordescrip"),
                                                    aplicacion
                                                            .getI18nString("cancelator"),
                                                    0, JOptionPane.QUESTION_MESSAGE, null, possibleValues,
                                                    possibleValues[0]);
                                    
                                    if (cancelproces == 0)
                                    {
                                        layerManager
                                        .getUndoableEditReceiver()
                                        .setAborted(
                                                true);
                                        newFeatures.add(f);
                                    }else{
                                        newFeatures.add(f);
                                        break;
                                    }
                                }else{
                                    boOldSchema=false;
                                    newFeatures.add(fNew);
                                }
                            } else
                            {
                                JOptionPane
                                        .showMessageDialog(
                                                (Component) aplicacion
                                                        .getMainFrame(),
                                                aplicacion
                                                        .getI18nString("GeometriaNoValida"));
                                layerManager
                                .getUndoableEditReceiver()
                                .setAborted(
                                        true);
                                break;
                            }
                        }else{
                        //guardamos todas las features dentro de un arrayList
                        newFeatures.add(fNew);
                        }
            		}else{
                        newFeatures.add(f);
                    }
                }
                layer.setFeatureCollection(new FeatureDataset(newFeatures, schema));
           }else{//cuando la capa es de SISTEMA
                GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) layer.getDataSourceQuery().getDataSource();
                firingEvents = layer.getLayerManager().isFiringEvents();
                layer.getLayerManager().setFiringEvents(false);
                
                //Si la capa es de sistema el schema no se puede tocar
                schema = (GeopistaSchema) layer.getFeatureCollectionWrapper().getFeatureSchema();
                boolean update=false;
                String keyValue;
                Object value;
                ArrayList newFeatures;
                String[] valeurs;
                boolean boOldSchema=false;
                FeatureCollection fc = layer.getFeatureCollectionWrapper();
                FeatureCollection newfc = null;
                List features = fc.getFeatures(); 
                ILayerManager layerManager =layer.getLayerManager();
                newFeatures = new ArrayList(features.size());
          
//                for (Iterator i = features.iterator(); i.hasNext();){
//                    GeopistaFeature f = (GeopistaFeature) i.next();
                  for(int i=0;i<features.size();i++){
                    GeopistaFeature f = (GeopistaFeature) features.get(i);
                    int j=0;
                    
                    keyValue = f.getString(attributeIndex).trim();
                    valeurs = (String[]) table.get(keyValue);
                    GeopistaFeature   fclone=(GeopistaFeature)f.clone();
                    if(valeurs!=null){
                        for (j=0; j<fieldCount; j++){
                            if (j!=keyIndex) {
                                if(valeurs[j]!=null)
                                    value = castValue((String) valeurs[j], (AttributeType) fieldTypes.get(j));
                                else
                                    value=null;
                                
                                f.setAttribute((String) fieldNames.get(j), value);
                            }
                        }
                        fclone.setNew(false);
                        fclone.setDirty(true);
                        
                        boolean validateResult = false;
                        boolean geometryError = false;
                        

                        if (!(validateResult = validator.validateFeature(f)))
                        {
                            Iterator errorsIterator = validator
                                    .getErrorListIterator();
                            while (errorsIterator.hasNext())
                            {
                                ValidationError actualError = (ValidationError) errorsIterator
                                        .next();
            
                                AttributeType attributeType = f
                                        .getSchema()
                                        .getAttributeType(
                                                actualError.attName);
            
                                if (attributeType == AttributeType.GEOMETRY)
                                {
                                    geometryError = true;
                                    break;
                                }
                            }
                            if (geometryError == false)
                            {
                                boolean resultAttributes = GeopistaValidatePlugin.showFeatureDialog(
                                        f, layer);
                                if (resultAttributes == false)
                                {
                                    Object[] possibleValues = { aplicacion.getI18nString("cancel.featureac"),
                                            aplicacion.getI18nString("cancel.allfeatures") };
                                    int cancelproces = JOptionPane
                                            .showOptionDialog(
                                                    aplicacion.getMainFrame(),
                                                    aplicacion
                                                            .getI18nString("cancelatordescrip"),
                                                    aplicacion
                                                            .getI18nString("cancelator"),
                                                    0, JOptionPane.QUESTION_MESSAGE, null, possibleValues,
                                                    possibleValues[0]);
                                    
                                    if (cancelproces == 0)
                                    {
                                        layerManager
                                        .getUndoableEditReceiver()
                                        .setAborted(
                                                true);
                                        newFeatures.add(fclone);
                                        //newfc.add(f);
                                        ///layer.getFeatureCollectionWrapper().add(f);
                                    }else{
                                        newFeatures.add(fclone);
                                        layer.getLayerManager().setFiringEvents(firingEvents);
                                        //newfc.add(f);
                                        //layer.getFeatureCollectionWrapper().add(f);
                                        break;
                                    }
                                    
                                    //break;
                                }else{
                                    boOldSchema=false;
                                    newFeatures.add(f);
                                    //layer.getFeatureCollectionWrapper().add(fclone);
                                }
            
                            } else
                            {
                                JOptionPane
                                        .showMessageDialog(
                                                (Component) aplicacion
                                                        .getMainFrame(),
                                                aplicacion
                                                        .getI18nString("GeometriaNoValida"));
                                layerManager
                                .getUndoableEditReceiver()
                                .setAborted(true);
                                break;
                            }
                        }else{
                        //guardamos todas las features dentro de un arrayList
                        newFeatures.add(f);
                        //layer.getFeatureCollectionWrapper().add(fclone);
                        }
                    }else{
                        newFeatures.add(f);
                    }
                }
//                fc.removeAll(features);
//                fc.addAll(newFeatures);
                progressDialog.report(aplicacion.getI18nString("GrabandoDatosBaseDatos"));
                
                
                Map driverProperties = geopistaServerDataSource.getProperties();
                Object lastResfreshValue = driverProperties.get(Constantes.REFRESH_INSERT_FEATURES);
                try
                {
                    driverProperties.put(Constantes.REFRESH_INSERT_FEATURES, new Boolean(false));
                    
                    geopistaServerDataSource.getConnection().executeUpdate(layer.getDataSourceQuery().getQuery(),fc,progressDialog);
                    
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
    		layer.setEditable(false);
            
        }catch(Exception e){
            e.printStackTrace();
        } finally
        {
            if(!layer.isLocal()){
                layer.getLayerManager().setFiringEvents(firingEvents);
            }
        }
		

	}

	// liberation memoire organisee
	public void dispose() {
		if (table!=null) table.clear();
		if (fieldTypes!=null) fieldTypes.clear();
		if (fieldNames!=null) fieldNames.clear();
		keyIndex = -1;
		fieldCount = 0;
		table=null;
		fieldTypes=null;
		fieldNames=null;
		dataSource = null;
	}
	
	private Object castValue(String s, AttributeType t) {
		try 
		{
			Object res;
			if (t == AttributeType.DOUBLE){
				s = s.replace(',','.').replaceAll(" ","");
				res = s.length() == 0 ? null : Double.valueOf(s); // uniformisation du format numerique
			}
			else if (t == AttributeType.INTEGER) {
				s = s.replaceAll(" ","");
				res = s.length() == 0 ? null : Integer.valueOf(s);
			}else if (t == AttributeType.DATE) {
                s = s.replaceAll(" ","");
                res = new Date(s);
            }else if (t == AttributeType.LONG) {
                s = s.replaceAll(" ","");
                res = Long.valueOf(s);
            }else if (t == AttributeType.FLOAT) {
                s = s.replaceAll(" ","");
                res = Float.valueOf(s);
            }
			else
				res = s.toString();
			return res;
		}
		catch (Exception e)
		{
			// pour eviter les mauvaises surprises
			return null;
		}
	}

    /**
     * @return Returns the tabledata.
     */
    public Hashtable getTabledata()
    {
        return tabledata;
    }

    /**
     * @param tabledata The tabledata to set.
     */
    public void setTabledata(Hashtable tabledata)
    {
        this.tabledata = tabledata;
    }
}
