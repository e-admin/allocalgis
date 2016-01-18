package com.geopista.ui.plugin.profiles3D;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.geopista.app.AppContext;
import com.geopista.ui.dialogs.SelectLayerFieldPanel;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.profiles3D.beans.ProfilesGeometry3D;
import com.geopista.ui.plugin.profiles3D.panels.ChartPanelTransferable;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.CloneableInternalFrame;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
/**
 * 
 * @author jvaca
 *
 */

public class Profiles3DPlugIn extends ThreadedBasePlugIn 
{
    /**
     * Logger for this class
     */
    private static final Log	logger	= LogFactory
    .getLog(Profiles3DPlugIn.class);
    
    private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();
     
    JInternalFrame frame;
    
    public Profiles3DPlugIn()
    {
        
    }
    
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
        .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
        .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
        .add(checkFactory.createTaskWindowMustBeActiveCheck())
        .add(checkFactory.createAtLeastNFeaturesMustBeSelectedCheck(1))
        ;
    }
    
    public String getName()
    {
        return "plugin3d.Profile3D";
    }
    
    public void initialize(PlugInContext context) throws Exception
    {
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(this,
                new String[]{"Tools", AppContext.getApplicationContext().getI18nString("ui.MenuNames.TOOLS.ANALYSIS")},
                AppContext.getApplicationContext().getI18nString(this.getName()), 
                false,
                null,
                Profiles3DPlugIn.createEnableCheck(context.getWorkbenchContext()));
    }
    
    public ImageIcon getIcon()
    {
        return IconLoader.icon("Open.gif");
    }
    
    public boolean execute(PlugInContext context) throws Exception
    {
        JComboBox comboPerfiles=null;
        MultiInputDialog multiDialogCotas = new MultiInputDialog(aplicacion.getMainFrame(),
                AppContext.getApplicationContext().getI18nString(this.getName()),
                true);
        
        Collection select =context.getLayerViewPanel().getSelectionManager().getLayersWithSelectedItems();
        
        multiDialogCotas.addLabel(AppContext.getApplicationContext().getI18nString("plugin3d.CapaPerfil"));
        if(select.size()!=0)
        {
            comboPerfiles = multiDialogCotas.addLayerComboBox(null,
                    null, 
                    AppContext.getApplicationContext().getI18nString("plugin3d.CapaPerfil"),
                    select);
            
            if (select.size()==1)
                comboPerfiles.setSelectedIndex(0);
        }
        
        SelectLayerFieldPanel panelAlturas =new SelectLayerFieldPanel(context);
        
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(context.getWorkbenchContext());
        EnableCheck enableCheck1 = checkFactory.createComboEnableCheck(comboPerfiles);
        
        EnableCheck enableCheck2 = checkFactory.createFieldPanelCheck(panelAlturas);
        Collection collectionObjet = new ArrayList();
        collectionObjet.add(enableCheck1);
        collectionObjet.add(enableCheck2);
        multiDialogCotas.setSize(500,500);
        panelAlturas.setPreferredSize(new java.awt.Dimension(200,350));
        multiDialogCotas.addEnableChecks("prueba",collectionObjet);
        multiDialogCotas.addLabel(AppContext.getApplicationContext().getI18nString("plugin3d.CapaAlturas"));
        multiDialogCotas.addRow("FRAME",new JLabel(),panelAlturas,null,AppContext.getApplicationContext().getI18nString("CapaAlturas"));
        multiDialogCotas.setVisible(true);
        if(multiDialogCotas.wasOKPressed())
        {
            //Se guardan en el Blackboard las capas seleccionadas por pantalla
            Blackboard loadLayer =  aplicacion.getBlackboard();
            loadLayer.put("layerCombo1",comboPerfiles.getSelectedItem());
            loadLayer.put("layerCombo2",panelAlturas.getSelectedLayer());
            loadLayer.put("fieldName",panelAlturas.getSelectedFieldName());
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void run(TaskMonitor monitor, PlugInContext context) throws Exception
    {
        try
        {
            //esta lista contendra todos los items de todas las features
            List itemsMaster=new ArrayList();
            //Guardamos la geometria de la feature seleccionada
            List geometryMaster=new ArrayList();
            //HashMap listaDataitemFeatures =new HashMap();
            HashMap xyseriesListas =new HashMap();
            XYSeriesCollection xyDataset= new XYSeriesCollection();
            //Recuperamos de el Blackboard los objetos seleccionados por pantalla 
            Blackboard blackboard =  aplicacion.getBlackboard();
            Layer capaPerfiles=(Layer) blackboard.get("layerCombo1");
            Layer capaAlturas= (Layer)blackboard.get("layerCombo2");
            
            Collection featuresColl =context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(capaPerfiles);
            
            Iterator FeaturesIter= featuresColl.iterator();
            //este bucle recorre todas las features seleccionadas y saca todos sus LineString
            while(FeaturesIter.hasNext()){
                List items = new ArrayList();
                //recuperamos Los Features de cada objeto seleccionado
                Feature localFeature = (Feature) FeaturesIter.next();
                Geometry geometryFeature=localFeature.getGeometry();
                
                if (geometryFeature.getClass() == Polygon.class)
                {                    
                    //llamamos al metodo que extrae los lineString del poligono
                    items=cutGeometryMultiLine((MultiLineString)geometryFeature.getBoundary(),localFeature);                    
                }
                else if (geometryFeature.getClass() == MultiPolygon.class)
                {                    
                    items=cutGeometryMultiPolygon((MultiPolygon) geometryFeature, localFeature);
                    
                }
                else if (geometryFeature.getClass() == LineString.class)
                {   
                    //Dentro de este objeto se almacenara el feature y un vector de geometrias de cada
                    ProfilesGeometry3D unidadGeometrica=new ProfilesGeometry3D();
                    unidadGeometrica.setFeatureSelect(localFeature);
                    unidadGeometrica.setLineString((LineString)geometryFeature);
                    items.add(unidadGeometrica);
                }
                else if (geometryFeature.getClass() == MultiLineString.class)
                {
                    //llamamos al metodo de separar Linestring y le pasamos la geometria
                    items=cutGeometryMultiLine((MultiLineString)geometryFeature,localFeature);
                    
                }
                else if (geometryFeature.getClass() == Point.class)
                {
                    //cuando el tipo de Feature es un punto no hacemos nada                     
                }
                else if (geometryFeature.getClass() == MultiPoint.class)
                {
                    //cuando es multipunto no tenemos nada que hacer                     
                }
                else if (geometryFeature.getClass() != GeometryCollection.class)
                {                    
                    items=cutGeometryCollection((GeometryCollection)geometryFeature, localFeature);
                }
                
                if(items.size()==0)
                {
                    return;
                }
                else
                {
                    Iterator itemsIter=items.iterator();
                    while(itemsIter.hasNext())
                    {
                        ProfilesGeometry3D unidadGeometricaglobal=(ProfilesGeometry3D) itemsIter.next();
                        ArrayList master = new ArrayList();
                        master.add(unidadGeometricaglobal.getLineString());
                        unidadGeometricaglobal.setLineStringMaster(master);
                        itemsMaster.add(unidadGeometricaglobal);
                    }
                    geometryMaster.add(geometryFeature);
                }
                
            }
            
            //obtiene los segmentos a pintar
            ArrayList segmentos = joinLines((ArrayList)itemsMaster);
            
            //calcula sus alturas y distancias
            List masterXyseries = calculateDrawingParameters(segmentos, geometryMaster, capaAlturas);
            
            ProfilesGeometry3D geometria=new ProfilesGeometry3D();
            
            //Se recorren las listas con las series para cargar el XYSeriesCollection
            for(int i=0;i<masterXyseries.size();i++){
                ProfilesGeometry3D series3D=(ProfilesGeometry3D)masterXyseries.get(i);
                xyDataset.addSeries(series3D.getSeries());
                xyseriesListas.put(series3D.getSeries(),series3D);
            }            
            
            //pinta el gráfico
            painterChart(xyDataset,context,xyseriesListas,geometria);
            
        } catch (Exception e)
        {
            logger.error("run(TaskMonitor, PlugInContext)", e);
            e.printStackTrace();
            throw e;
        }
        
    }
    private ArrayList joinLines(ArrayList lstLines)
    {
        ArrayList lstLinesNoRepet = new ArrayList();
        
        lstLinesNoRepet.addAll(lstLines);
        
        int tam = lstLines.size();
        for (int i=0;i< tam; i++)
        {                  
            SortedSet indexToRemove = new TreeSet();
            
            lstLines = orderLines(lstLinesNoRepet);            
            lstLinesNoRepet = new ArrayList();
            lstLinesNoRepet.addAll(lstLines);          
            
            //Eliminacion de elementos repetidos    
            Iterator itLines = lstLines.iterator();  
            
            int pos = 0;
            while (itLines.hasNext())
            {
                ProfilesGeometry3D pg = (ProfilesGeometry3D)itLines.next();
                
                Iterator itComparador = lstLines.iterator();  
                int j = 0;
                while (itComparador.hasNext())
                {
                    if (j>pos)
                    {
                        ProfilesGeometry3D pgAux = (ProfilesGeometry3D)itComparador.next();
                        if(pgAux.equals(pg) 
                                || pgAux.contains(pg))
                        {
                            //System.out.print(pos + " ");
                            indexToRemove.add(new Integer(pos));
                        }
                        
                        else if (pg.contains(pgAux))
                        {
                            //System.out.print(j + " ");
                            indexToRemove.add(new Integer(j));
                        }
                    }
                    else
                    {
                        itComparador.next();
                    }
                    j++;                    
                }                
                pos++;
                //System.out.println("");
            }  
            
            Integer [] array = (Integer[])indexToRemove.toArray(new Integer[indexToRemove.size()]);
            Arrays.sort(array, Collections.reverseOrder());
            for (int c =0; c<array.length; c++)
            {
                if (array[c].intValue()<lstLinesNoRepet.size())
                {
                    lstLinesNoRepet.remove(array[c].intValue());
                }
            }            
        }
        
        return lstLinesNoRepet;        
    }
    
    /**
     * Ordena la lista de linestrings
     * 
     * @param pgLine
     * @param segmentos
     * @return
     */
    private ArrayList orderLines(ArrayList segmentos)
    {
        ArrayList lstSegmentosFinal      =  new ArrayList(segmentos);
        Iterator  itSegmentosOriginal    =  segmentos.iterator();
        
        int posToCompare = 0;
        
        while (itSegmentosOriginal.hasNext())
        {   
            ProfilesGeometry3D pgSegmento = (ProfilesGeometry3D)itSegmentosOriginal.next();
            ArrayList lstSegmentos = (ArrayList)pgSegmento.getLineStringMaster();
            Point inicioSegmento = ((LineString)pgSegmento.getLineStringMaster().get(0)).getStartPoint();
            
            Iterator  itSegmentosComparador  =  segmentos.iterator();
            int i=0;           
            while (itSegmentosComparador.hasNext())
            {
                
                if (i!= posToCompare)
                {
                    ProfilesGeometry3D pgSegmentoComparador = (ProfilesGeometry3D)itSegmentosComparador.next();
                    List lstLinestring = pgSegmentoComparador.getLineStringMaster(); 
                    Point finSegmento = ((LineString)lstLinestring.get(lstLinestring.size()-1)).getEndPoint();
                    
                    //Si el final de un segmento coincide con el inicio del que se está comparando, se unen ambos
                    //Se elimina de la lista el pgSegmento una vez recorridos todos los segmentos
                    if (inicioSegmento.equals(finSegmento))
                    {
                        ProfilesGeometry3D pgSegmentoAux = new ProfilesGeometry3D();
                        List lstSegmentosAux = new ArrayList(lstLinestring);
                        lstSegmentosAux.addAll(lstSegmentos);                        
                        pgSegmentoAux.setLineStringMaster(lstSegmentosAux);  
                        lstSegmentosFinal.set(i, pgSegmentoAux); 
                    }
                    //Si el inicio del segmento que se está comparando, coincide con un punto intermedio de otro
                    //segmento, se hace una copia del segundo y se le añade el subsegmento que falta.
                    //Además, se elimina el que se está comparando de la lista
                    else
                    {
                        
                        for (int j=0; j< lstLinestring.size(); j++)
                        {
                            Point finSubsegmento = ((LineString)lstLinestring.get(j)).getEndPoint();
                            if (inicioSegmento.equals(finSubsegmento))
                            {
                                ProfilesGeometry3D pgSegmentoAux = new ProfilesGeometry3D();
                                List lstSegmentosAux = new ArrayList();
                                for (int k=0; k<=j; k++)
                                {
                                    lstSegmentosAux.add((LineString)lstLinestring.get(k));
                                }
                                lstSegmentosAux.addAll(lstSegmentos);
                                pgSegmentoAux.setLineStringMaster(lstSegmentosAux);                                
                                lstSegmentosFinal.set( posToCompare, pgSegmentoAux);                                                             
                            }
                        }
                    }                    
                    
                }
                else
                {
                    itSegmentosComparador.next();
                }
                i++;                
            }
            
            posToCompare++;
        }        
        return lstSegmentosFinal;
    }
    
    /**
     * Este metodo separa los multilineString en lineString y los almacena en un objeto de una clase Bean,
     * en este contenedor tendremos asociados cada feature con un LineString 
     * 
     * @return Devuelve una Lista de objetos de la clase ProfilesGeometry3D
     * */
    public List cutGeometryMultiLine(MultiLineString geometria,Feature feature) throws Exception
    {
        List items = new ArrayList();
        
        for(int n=0; n < geometria.getNumGeometries(); n++)
        {
            //Dentro de este objeto se almacenara el feature y un vector de geometrias de cada
            ProfilesGeometry3D unidadGeometrica=new ProfilesGeometry3D();
            unidadGeometrica.setFeatureSelect(feature);
            unidadGeometrica.setLineString((LineString) geometria.getGeometryN(n));
            items.add(unidadGeometrica);
        }
        return items;
    }
    
    /**
     * Este metodo separa los multiPolygon en Polygon y los almacena en un vector
     * 
     * @return Devuelve una Lista de objetos de la clase ProfilesGeometry3D
     * */
    public List cutGeometryMultiPolygon(MultiPolygon geometria,Feature feature) throws Exception
    {
        List items = new ArrayList();
        for (int i=0;i<geometria.getNumGeometries();i++){
            
            MultiLineString multilineGeo=(MultiLineString) geometria.getGeometryN(i).getBoundary();
            
            items=cutGeometryMultiLine(multilineGeo,feature);
            
        }
        return items;
    }
    
    /**
     * Este metodo Clasifica  un GeometryCollection en la diferentes posivilidades que podemos tener
     * 
     * @return Devuelve una HashMap de List que contienen objetos de la clase ProfilesGeometry3D
     * */
    public List cutGeometryCollection(GeometryCollection geometria,Feature feature) throws Exception
    {
        
        List itemsTotal = new ArrayList();
        Geometry[] vectorGeometry= new Geometry[] { geometria };
        //hay que ver si haciendo esto podriamos coger cada unidad geometrica por separado
        for (int i=0;i<vectorGeometry.length;i++){
            //esta lista la creamos para cada tipo de unidad geometrica
            List items = new ArrayList();
            if (vectorGeometry[i].getClass() == Polygon.class){
                //llamamos al metodo que extrae los lineString del poligono
                items=cutGeometryMultiLine((MultiLineString)vectorGeometry[i].getBoundary(),feature);
            }else if (vectorGeometry[i].getClass() == MultiPolygon.class){
                items=cutGeometryMultiPolygon((MultiPolygon) vectorGeometry[i], feature);
            }else if (vectorGeometry[i].getClass() == LineString.class){
                //Dentro de este objeto se almacenara el feature y un vector de geometrias de cada
                ProfilesGeometry3D unidadGeometrica=new ProfilesGeometry3D();
                unidadGeometrica.setFeatureSelect(feature);
                unidadGeometrica.setLineString((LineString)vectorGeometry[i]);
                items.add(unidadGeometrica);
            }else if (vectorGeometry[i].getClass() == MultiLineString.class){
                //llamamos al metodo de separar Linestring y le pasamos la geometria
                items=cutGeometryMultiLine((MultiLineString)vectorGeometry[i],feature);  
            }else if (vectorGeometry[i].getClass() == Point.class){
                
            }else if (vectorGeometry[i].getClass() == MultiPoint.class){
                
            }
            for(int j=0;j<items.size();j++){
                itemsTotal.add(items.get(j));
            }
            
        }
        return itemsTotal;
    }
    
    /**
     * Toma el envelope a partir de un geometry y busca todas las Isolineas que lo crucen
     * 
     *  @return Devuelve una Lista de objetos de la clase ProfilesGeometry3D
     * */
    public List getIsoline(List itemsMaster,Layer cmblayer) throws Exception
    {
        List isoLineMaster=new ArrayList();
        Iterator itemsMasterIter =itemsMaster.iterator();
        
        while(itemsMasterIter.hasNext()){
            
            List isoLines = new ArrayList();   
            Geometry GeoEnvelope=(Geometry)itemsMasterIter.next();           
            
            Envelope envelope=GeoEnvelope.getEnvelopeInternal();
            //FeatureDataset dataSet=new FeatureDataset( localFeature.getSchema());
            if(isoLines.size()==0){
                //tenemos el envelope para cada geometria
                double maxX=envelope.getMaxX();
                double maxy=envelope.getMaxY();
                double minX=envelope.getMinX();
                double miny=envelope.getMinY();
                //redimensionamos las coordenadas del envelope y se las asignamos
                maxX=((maxX-minX)/2)+ maxX;
                minX=minX -((maxX-minX)/2);
                maxy=((maxy-miny))/2 +maxy;
                miny=miny-((maxy-miny))/2;
                //en este punto tendremos que redimensionar el envelope 
                Envelope envelope2 = new Envelope();
                envelope2.init(minX,maxX,maxy,miny);
                //isoLines=dataSet.query(envelope2);
                isoLines=cmblayer.getFeatureCollectionWrapper().query(envelope2);
                //esta operacion se repetira tantas veces como se desee intentar para encontar una isolinea
                for(int i=0;i<4;i++){
                    if(isoLines.size()!=0){
                        // ha encontrado isolineas se deja de buscar
                        break;
                    }else{
                        //no ha encontrado nada en el envelope
                        //tenemos el envelope para cada geometria
                        maxX=envelope.getMaxX();
                        maxy=envelope.getMaxY();
                        minX=envelope.getMinX();
                        miny=envelope.getMinY();
                        //redimensionamos las coordenadas del envelope y se las asignamos
                        maxX=((maxX-minX)/2)+ maxX;
                        minX=minX -((maxX-minX)/2);
                        maxy=((maxy-miny))/2 +maxy;
                        miny=miny-((maxy-miny))/2;
                        //en este punto tendremos que redimensionar el envelope 
                        envelope2 = new Envelope();
                        envelope2.init(minX,maxX,maxy,miny);
                        // isoLines=dataSet.query(envelope2);
                        isoLines=cmblayer.getFeatureCollectionWrapper().query(envelope2);
                    }
                }
                for(int i=0;i<isoLines.size();i++){
                    Feature featureisolinea=(Feature)isoLines.get(i);
                    isoLineMaster.add(featureisolinea);
                }
            }else{
                //envelope para cada geometria
                double maxX=envelope.getMaxX();
                double maxy=envelope.getMaxY();
                double minX=envelope.getMinX();
                double miny=envelope.getMinY();
                //redimensionamos las coordenadas del envelope y se las asignamos
                maxX=((maxX-minX)/2)+ maxX;
                minX=minX -((maxX-minX)/2);
                maxy=((maxy-miny))/2 +maxy;
                miny=miny-((maxy-miny))/2;
                //Redimensionar el envelope 
                Envelope envelope2 = new Envelope();
                envelope2.init(minX,maxX,maxy,miny);
                isoLines=cmblayer.getFeatureCollectionWrapper().query(envelope2);
                
                //esta operacion se repetira tantas veces como se desee intentar para encontar una isolinea
                for(int i=0;i<4;i++){
                    if(isoLines.size()!=0){
                        // ha encontrado isolineas se deja de buscar
                        break;
                    }else{
                        //no ha encontrado nada en el envelope
                        //tenemos el envelope para cada geometria
                        maxX=envelope.getMaxX();
                        maxy=envelope.getMaxY();
                        minX=envelope.getMinX();
                        miny=envelope.getMinY();
                        //redimensionamos las coordenadas del envelope y se las asignamos
                        maxX=((maxX-minX)/2)+ maxX;
                        minX=minX -((maxX-minX)/2);
                        maxy=((maxy-miny))/2 +maxy;
                        miny=miny-((maxy-miny))/2;
                        //en este punto tendremos que redimensionar el envelope 
                        envelope2 = new Envelope();
                        envelope2.init(minX,maxX,maxy,miny);
                        // isoLines=dataSet.query(envelope2);
                        isoLines=cmblayer.getFeatureCollectionWrapper().query(envelope2);
                    }
                }
                for(int i=0;i<isoLines.size();i++){
                    Feature featureisolinea=(Feature)isoLines.get(i);
                    for(int j=0;j<isoLineMaster.size();j++){
                        Feature featureisolineaMaster=(Feature) isoLineMaster.get(j);
                        if(!featureisolineaMaster.equals(featureisolinea))
                            isoLineMaster.add(featureisolinea);
                    }
                }
            }
            
        }
        return isoLineMaster;
    }    
    /**
     * Este metodo encuentra los puntos mas cercanos a las isolineas de los features selecionados
     * 
     *  @return  TreeSet con la coordenada punto mas cercano ordenado por la medida de la distancia
     * */
    public HashMap getNearPoints(List isoLines,List unionLine,String atributo) throws Exception
    {
        //tendremos que recorrer la coleccion de items donde almacenamos objetos ProfilesGeometry3D
        Coordinate[] coordenadas;
        DistanceOp newDistance=null;
        HashMap puntosCercanos= new HashMap();
        //recorremos las Linestring almacenadas en items
        Iterator unionLineIter = unionLine.iterator();
        while(unionLineIter.hasNext()){
            List ListaTreeSets =new ArrayList();
            ProfilesGeometry3D unidadGeometrica = (ProfilesGeometry3D)unionLineIter.next();
            
            List pointMaster=unidadGeometrica.getPointLine();
            Iterator pointMasterIter = pointMaster.iterator();
            
            while(pointMasterIter.hasNext()){
                //Tiene que haber un TreeSet por punto
                TreeSet treeGeometryList = new TreeSet(new Comparator(){
                    public int compare(Object o1, Object o2) {
                        ProfilesGeometry3D f1 = (ProfilesGeometry3D) o1;
                        ProfilesGeometry3D f2 = (ProfilesGeometry3D) o2;
                        if(f1.getDistance()>f2.getDistance()){
                            return 1;
                        }else if(f1.getDistance()<f2.getDistance()){
                            return -1;
                        }else{
                            return 0;
                        }
                    }
                });
                Point punto=(Point)pointMasterIter.next();
                Iterator isoLinesIter = isoLines.iterator();
                while(isoLinesIter.hasNext()){
                    Feature isoLineFeature=(Feature)isoLinesIter.next();
                    Geometry geoIsoLines=isoLineFeature.getGeometry();
                    //debemos distinguir si la capa de las alturas (isolineas) esta formada por puntos o por LineString
                    if(geoIsoLines.getClass()==LineString.class){
                        //con esta funcion se recupera un vector de coordenadas con la coordenada mas cercana
                        coordenadas=newDistance.closestPoints(geoIsoLines,punto);
                        for(int i=0;i<coordenadas.length;i++){
                            //la funcion closestPoints devuelve el punto mas cercano y el propio punto 
                            //por lo que no tendremos que seleccionarlo
                            if(!coordenadas[i].equals(punto.getCoordinate())){
                                ProfilesGeometry3D profilesGeometry= new ProfilesGeometry3D();
                                profilesGeometry.setCoordenada(coordenadas[i]);
                                profilesGeometry.setDistance(coordenadas[i].distance(punto.getCoordinate()));
                                Double altura=null;
                                if(isoLineFeature.getAttribute(atributo)!=null){
                                    altura=  (Double)isoLineFeature.getAttribute(atributo);
                                }else{
                                    //si lo que nos pasan no contiene datos
                                    throw new Exception();
                                }
                                profilesGeometry.setAltitud(altura.doubleValue());
                                profilesGeometry.setPunto(punto);
                                //Almacenamos en un TreeSet  objetos ProfilesGeometry3D
                                treeGeometryList.add(profilesGeometry);
                            }
                        } 
                    }else if(geoIsoLines.getClass()== Point.class || geoIsoLines.getClass()== MultiPoint.class){
                        coordenadas=geoIsoLines.getCoordinates();
                        for(int i=0;i<coordenadas.length;i++){
                            ProfilesGeometry3D profilesGeometry= new ProfilesGeometry3D();
                            profilesGeometry.setCoordenada(coordenadas[i]);
                            profilesGeometry.setDistance(coordenadas[i].distance(punto.getCoordinate()));
                            Double altura=null;
                            if(isoLineFeature.getAttribute(atributo)!=null){
                                altura=  new Double(isoLineFeature.getAttribute(atributo).toString());
                            }else{
                                //si lo que nos pasan no coniene datos                                
                                throw new Exception();
                            }
                            
                            profilesGeometry.setAltitud(altura.doubleValue());
                            profilesGeometry.setPunto(punto);
                            treeGeometryList.add(profilesGeometry);
                        }
                    }
                }
                ListaTreeSets.add(treeGeometryList);
            }
            puntosCercanos.put(pointMaster,ListaTreeSets);
        }
        return puntosCercanos;
    }
    /**
     * Este metodo realiza una interpolacion entre las distancias obtenidas para averiguar una altura media
     * favoreciendo siempre a la isolinea mas cercana al punto
     * 
     *  @return devuelve una lista con las distancias mas probables para cada punto segun la proximidad a la isolinea
     * */
    public HashMap interpolacionDistancias(HashMap puntosCercanos) throws Exception
    {
        //recorremos el HashMap para ir leyendo las distancias por el punto de la feature seleccionada
        
        
        Set unionLine=puntosCercanos.keySet();
        Iterator unionLineIter =unionLine.iterator();
        //en este o primer bucle recorreremos Todos Los puntos de la Feature seleccionada
        HashMap geopuntos=new HashMap();
        while(unionLineIter.hasNext()){
            double sumaDividendo=0;
            double sumaDivisor=0;
            double alturaPonderada=0;
            
            List pointMaster=(List)unionLineIter.next();
            List treeList=(List)puntosCercanos.get(pointMaster);
            List alturasPonderadas=new ArrayList();
            
            Iterator treeListIter = treeList.iterator();
            //existira una lista por cada punto dentro del HashMap
            while(treeListIter.hasNext()){
                TreeSet tree=(TreeSet)treeListIter.next();
                Iterator treeIter = tree.iterator();
                double[] distancias = new double[tree.size()];
                double[] alturas = new double[tree.size()];
                int i=0;
                //en este segundo bucle recorreremos en cada punto y su distancia con el punto de la feature seleccionada
                while(treeIter.hasNext()){
                    ProfilesGeometry3D profilesGeometry=(ProfilesGeometry3D)   treeIter.next();
                    distancias[i]=profilesGeometry.getDistance();
                    alturas[i]=profilesGeometry.getAltitud();
                    i++;
                }
                sumaDividendo = 0;
                sumaDivisor   = 0;
                //este bucle se podria suprimir haciendo todas las operaciones en el anterios me parecio que quedaba mas claro
                for(int j=0;j<distancias.length;j++){
                    //la probabilidad de que exista una distancia 0 sera cuando dos puntos coincidan
                    //habra que tener en cuenta esa posibilidad
                    if(distancias[j]!=0){
                        double wi = 1/distancias[j];
                        sumaDividendo=(wi*alturas[j]) + sumaDividendo;
                        sumaDivisor=wi+sumaDivisor;
                        alturaPonderada=sumaDividendo/sumaDivisor;
                    }
                    //Si el punto coincide con una isolinea, se considera directamente que tiene su altura, 
                    //sin necesidad de ponderar en este caso (limite de la funcion de ponderacion)
                    else
                    {
                        alturaPonderada = alturas[j];
                        break;
                    }
                }
                
                Double alt= new Double(alturaPonderada);                
                alturasPonderadas.add(alt);                
            }
            geopuntos.put(pointMaster,alturasPonderadas);
        }
        return geopuntos;
    }
    /**
     * Calcula las distancias entre los puntos del feature y crea un objeto XYSeries
     * con lo que se obtiene una serie por cada objeto lineString que tengamos
     * 
     * @param alturasPuntos Geometrias por LineString o por LineString unidas
     * @param unionLine Puntos del LineString y sus alturas ponderadas
     * @return lista con objetos del bean con los XYSeries y las listas de puntos
     * */
    public List distanceFeatures(HashMap alturasPuntos,List unionLine) throws Exception
    {
        
        List masterXYSeries=new ArrayList();
        
        // List listaSeries=new ArrayList();
        Set alturasPuntosSet=alturasPuntos.keySet();
        Iterator alturasPuntosIter= alturasPuntosSet.iterator();
        
        ProfilesGeometry3D profilesGeometry= new ProfilesGeometry3D();
        
        Collection colalturas=new LinkedList();
        Collection coldistancias= new LinkedList();
        //Collections cols=null;
        while(alturasPuntosIter.hasNext()){
            //creamos una serie por cada LineString que tengamos separado
            XYSeries series=new XYSeries("PERFIL");
            
            //Estas dos listas tienen que tener el mismo numero de elementos
            List pointMaster=(List)alturasPuntosIter.next();
            List alturasPonderadas=(List)alturasPuntos.get(pointMaster);
            
            for(int i=0;i<unionLine.size();i++){
                ProfilesGeometry3D puntos =(ProfilesGeometry3D)unionLine.get(i);
                if(pointMaster.equals(puntos.getPointLine())){
                    profilesGeometry=puntos;
                }
            }
            
            //en estas dos matrices estan almacenadas los puntos y alturas para cada LineString
            Point[] matrizPuntos =new Point[pointMaster.size()];
            
            //cargamos la matriz de puntos
            for(int j=0;j<pointMaster.size();j++){
                matrizPuntos[j]=(Point)pointMaster.get(j);
            }
            
            double[] distancias= new double[alturasPonderadas.size()];
            double[] distanciasX= new double[alturasPonderadas.size()];
            //con el siguiente bucle se calculan las distancias entre los puntos
            for(int j=0;j<matrizPuntos.length;j++){
                if(j==0)
                    distancias[j]=0;
                if(j<(matrizPuntos.length -1)){
                    
                    distancias[j+1]= matrizPuntos[j].distance(matrizPuntos[j+1]);
                }
            }
            
            //con el siguiente bucle se suman las distancias para sacar las x del grafico
            for(int j=0;j<distancias.length;j++){
                if(j==0){
                    distanciasX[j]=distancias[j];
                }else{
                    double di=0;
                    for(int z=0;z<=j;z++){
                        di=distancias[z]+ di;
                    }
                    distanciasX[j]=di;
                }
            }
            
            //Se cargan las series            
            for(int j=0;j<alturasPonderadas.size();j++){
                XYDataItem xydata= new XYDataItem(distanciasX[j],((Double)alturasPonderadas.get(j)).doubleValue());
                series.add(xydata); 
                coldistancias.add(new Double(distanciasX[j]));
                colalturas.add((Double)alturasPonderadas.get(j));
            }
            
            profilesGeometry.setSeries(series);
            masterXYSeries.add(profilesGeometry);    
        }
 
        return masterXYSeries;
    }
    /**
     * Pinta el grafico con los datos dados 
     * 
     * @return void
     * */
    
    public void painterChart(XYSeriesCollection xyDataset,final PlugInContext context,final HashMap xySeriesList,ProfilesGeometry3D geoMaxMin) throws Exception
    {
        final List xySeries=xyDataset.getSeries();
        frame = ((CloneableInternalFrame) context.getActiveInternalFrame()).internalFrameClone();
        
        
        // Barra de menu
        JMenuBar menuBar = new JMenuBar();
        
        // Menu de edicion
        JMenu menu = new JMenu(aplicacion.getI18nString("plugin3d.chart.menu.edicion"));
        menuBar.add(menu);
        
        // Item de copiar dentro de edición
        JMenuItem item = new JMenuItem(aplicacion.getI18nString("plugin3d.chart.menu.edicion.copiar"));
        item.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e)
            {                
                // Portapapeles del sistema
                Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
              
                // Introduce el objeto en el portapapeles 
                if (frame.getContentPane() instanceof ChartPanel)
                {
                    ChartPanelTransferable trans = new ChartPanelTransferable((ChartPanel)frame.getContentPane());
                    systemClipboard.setContents(trans, null);
                }
                               
            }
            
        });
        
        
        menu.add(item);
        
        // añade el menu al internalframe del chart
        frame.setJMenuBar(menuBar);
 
        frame.setTitle(aplicacion.getI18nString("plugin3d.chart.titulo"));
        context.getWorkbenchGuiComponent().addInternalFrame(frame);        
        
        //Grafico es de lineas
        JFreeChart chart=ChartFactory.createXYLineChart(aplicacion.getI18nString("plugin3d.chart.titulo").toUpperCase(),
                aplicacion.getI18nString("plugin3d.chart.ejeX"),
                aplicacion.getI18nString("plugin3d.chart.ejeY"),
                xyDataset,
                PlotOrientation.VERTICAL,
                false,              //leyenda
                true,               //tooltips
                true);              //urls
        
        final ChartPanel chartPanel=new ChartPanel(chart);
        chart.getXYPlot().getRangeAxis().getRange();
        //Toma un rango del eje de coordenadas Y
        XYPlot xy=chart.getXYPlot();
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        xy.setRenderer(renderer);
        
        NumberAxis numberAxis= new NumberAxis();
        numberAxis=(NumberAxis)xy.getRangeAxis();
        numberAxis.setAutoRangeIncludesZero(false);
        Range rango=numberAxis.getRange();
        
        if(rango.getLength()<10){
            double ymax=rango.getUpperBound()+(10-rango.getLength())/2;
            double ymin=rango.getLowerBound()-(10-rango.getLength())/2;
            if(ymin<0){
                ymin=0;
                ymax=ymax+(10-rango.getLength())/2;
            }
            Range newrango=new Range(ymin,ymax);
            xy.getRangeAxis().setRange(newrango); 
        }
        
        
        //ilumina la feature cuyo extremo se ha seleccionado dentro del gráfico
        chartPanel.addChartMouseListener(new ChartMouseListener(){
            public void chartMouseClicked(ChartMouseEvent chartmouseevent){
                try{
                    LineString lineStringSeek=null;
                    ChartEntity entidad =chartmouseevent.getEntity();
                    XYItemEntity xy=(XYItemEntity) entidad;
                    if (xy !=null)
                    {
                        XYSeries series=(XYSeries)xySeries.get(xy.getSeriesIndex());
                        ProfilesGeometry3D unidadGeometricaglobal=(ProfilesGeometry3D)xySeriesList.get(series);
                        List pointMaster=(List)unidadGeometricaglobal.getPointLine();
                        Point punto=(Point)pointMaster.get(xy.getItem());
                        List LineStringMaster=(List)unidadGeometricaglobal.getLineStringMaster();
                        Iterator LineStringMasterIter=LineStringMaster.iterator();
                        while(LineStringMasterIter.hasNext()){
                            LineString lineString=(LineString) LineStringMasterIter.next();
                            for(int i=0;i<lineString.getNumPoints();i++){
                                if(punto.equals(lineString.getPointN(i))){
                                    lineStringSeek=lineString;
                                }
                            }
                        }
                        Geometry[] arrayGeometri = new Geometry[]{lineStringSeek};
                        GeometryFactory geofactory = new GeometryFactory();
                        context.getLayerViewPanel().flash(geofactory.createGeometryCollection(arrayGeometri));
                    }
                }catch(Exception e){
                    logger.error("run(TaskMonitor, PlugInContext)", e); 
                }
            }
            public void chartMouseMoved(ChartMouseEvent chartmouseevent){
                
            }
        });
        frame.setContentPane(chartPanel);        
        
        //Arrastrar el grafico puesto en pantalla        
        //DragSource dragsource=DragSource.getDefaultDragSource();
        //dragsource.createDefaultDragGestureRecognizer(chartPanel,DnDConstants.ACTION_COPY,new DragGestureListener(){
        //         
        //  public void dragGestureRecognized(DragGestureEvent dge){
        //      ChartPanelTransferable trans = new ChartPanelTransferable(chartPanel);
        //      dge.startDrag(null,trans,new DragSourceAdapter(){});
        //  }
        //});         
        
        
        frame.setBounds(50,50,800,500);
        frame.setVisible(true);
        
    }
    
    private List calculateDrawingParameters(ArrayList profiles3D, List geometryMaster, Layer capaAlturas) throws Exception
    {
        ArrayList completeProfiles3D = new ArrayList(profiles3D.size());        
        Iterator itProfiles = profiles3D.iterator();        
        while (itProfiles.hasNext())
        {
            int i=0;
            
            ProfilesGeometry3D pg = (ProfilesGeometry3D)itProfiles.next();
            List lstSegmentos = pg.getLineStringMaster();
            
            //Para cada lista de segmentos, se calculan sus puntos             
            ArrayList lstPuntos = new ArrayList();
            
            Iterator itSeg = lstSegmentos.iterator();            
            while (itSeg.hasNext())
            { 
                //toma todos los puntos de la linestring, incluidos los intermedios
                LineString line = (LineString)itSeg.next();
                if (i==0)
                {
                    //punto de inicio
                    Point startPoint = line.getStartPoint();
                    lstPuntos.add(startPoint);  
                    
                }   
                
                //El punto inicial solo se añade para los segmentos iniciales
                int numPoints=line.getNumPoints();
                for (int j=1; j< numPoints; j++)
                {
                    Point puntoN=line.getPointN(j);
                    if (puntoN !=null)
                        lstPuntos.add(puntoN);
                }
                
                i++;
              
            }
            pg.setPointLine(lstPuntos);
            completeProfiles3D.add(pg);
            
        }   
        
        //Para los puntos de cada segmento, se calcula la distancia al inicial y la altitud. 
        
        List isoLines=getIsoline(geometryMaster,capaAlturas);      
        
        //Si no hay isolineas, se sale del método
        if (isoLines.size()==0)
            throw new Exception();
        
        //Busca los puntos mas cercanos a las isolineas encontradas dentro del envelope
        Blackboard blackboard =  aplicacion.getBlackboard();
        HashMap puntoscercanos=getNearPoints(isoLines,completeProfiles3D,blackboard.get("fieldName").toString());
        
        //Alturas mas probables para cada punto, calculadas mediante interpolación basada en distancias
        HashMap alturasPuntos=interpolacionDistancias(puntoscercanos);
        
        //Obtiene lista de puntos a representar, a partir de las alturas y los perfiles
        List masterXyseries =distanceFeatures(alturasPuntos,completeProfiles3D);
        return masterXyseries;        
    }    
    
 
}
