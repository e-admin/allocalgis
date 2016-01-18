package ejemplosgeopista;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaListener;
import com.geopista.ui.GeopistaLayerNamePanel;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.GeopistaLoadMapPlugIn;
import com.geopista.ui.plugin.GeopistaSaveMapPlugIn;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.plugin.SeriePrintPlugIn;
import com.geopista.util.GeopistaUtil;
import com.geopista.ui.plugin.*;

import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerTreeModel;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.TreeLayerNamePanel;
import com.vividsolutions.jump.workbench.ui.plugin.DeleteSelectedItemsPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.OpenProjectPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.RedoPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.SaveProjectAsPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.SaveProjectPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.UndoPlugIn;
import com.vividsolutions.jump.workbench.ui.style.ChangeStylesPlugIn;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;

import java.awt.FlowLayout;
import javax.swing.JSplitPane;
import com.geopista.model.*;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import java.io.File;

import java.util.HashMap;

import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.geopista.style.sld.ui.plugin.ChangeSLDStylesPlugIn;
import com.geopista.ui.plugin.analysis.GeopistaFusionPlugIn;
import com.geopista.ui.plugin.edit.GeopistaFeatureSchemaPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.EditablePlugIn;
import com.geopista.ui.wizard.*;
import com.geopista.app.planeamiento.*;
import com.geopista.ui.plugin.GeopistaPrintPlugIn;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.util.Blackboard;


public class GeopistaFrame extends JFrame 
{
  private GeopistaEditor geopistaEditor1 ;
  private JButton jButton1 = new JButton();
  private JButton jButton2 = new JButton();
  private JButton jButton3 = new JButton();

  public JLabel eventLabel = new JLabel();
  public JPanel eventPanel = new JPanel();

  //private GeopistaEventFrame eventFrame = new GeopistaEventFrame();
  Layer layer09 = null;
  private JButton jButton4 = new JButton();
  private JButton jButton5 = new JButton();
  //private JButton jButton5 = new JButton();
  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  private Blackboard blackboardInformes = aplicacion.getBlackboard();

public GeopistaFrame(Task task)
{
	geopistaEditor1= new GeopistaEditor(task);
	 try
	    {
	      jbInit();
	    }
	    catch(Exception e)
	    {
	      e.printStackTrace();
	    }
}
  public GeopistaFrame()
  {
  	geopistaEditor1= new GeopistaEditor();
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

  private void jbInit() throws Exception
  {
    this.getContentPane().setLayout(null);
    this.setSize(new Dimension(681, 553));
    geopistaEditor1.setBounds(new Rectangle(30, 10, 615, 460));
    jButton1.setText("Reset");
    jButton1.setBounds(new Rectangle(45, 485, 75, 40));
    jButton1.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton1_actionPerformed(e);
        }
      });
    jButton2.setText("Mostrar Nombres");
    jButton2.setBounds(new Rectangle(130, 485, 120, 40));
    jButton2.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton2_actionPerformed(e);
        }
      });
    jButton3.setText("Ocultar Nombres");
    jButton3.setBounds(new Rectangle(260, 485, 115, 40));
    jButton3.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton3_actionPerformed(e);
        }
      });
    //this.getContentPane().add(jButton5, null);
    this.getContentPane().add(jButton5, null);
    this.getContentPane().add(jButton4, null);
    this.getContentPane().add(jButton3, null);
    this.getContentPane().add(jButton2, null);
    this.getContentPane().add(jButton1, null);
    this.getContentPane().add(geopistaEditor1, null);

    this.addWindowListener(new Conclusion());

    geopistaEditor1.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
    geopistaEditor1.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
    geopistaEditor1.addCursorTool("Measure", "com.vividsolutions.jump.workbench.ui.cursortool.MeasureTool");
    geopistaEditor1.addCursorTool("Feature Info", "com.geopista.ui.cursortool.GeopistaFeatureInfoTool");
    geopistaEditor1.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");

    //ChangeStylesPlugIn changeStylesPlugIn = new ChangeStylesPlugIn();  
    //ChangeSLDStylesPlugIn changeSLDStylesPlugIn = new ChangeSLDStylesPlugIn();
    //ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
    GeopistaEditingPlugIn geopistaEditingPlugIn = new GeopistaEditingPlugIn();
    
    //UndoPlugIn undoPlugIn = new UndoPlugIn();
    //RedoPlugIn redoPlugIn = new RedoPlugIn();
    //GeopistaValidatePlugin geopistaValidatePlugin = new GeopistaValidatePlugin();
    //DeleteSelectedItemsPlugIn deleteSelectedItemsPlugIn = new DeleteSelectedItemsPlugIn();
    //SaveProjectAsPlugIn saveProjectAsPlugIn = new SaveProjectAsPlugIn();
    //SaveProjectPlugIn saveProjectPlugIn = new SaveProjectPlugIn(saveProjectAsPlugIn);
    //GeopistaFeatureSchemaPlugIn geopistaFeatureSchemaPlugIn = new GeopistaFeatureSchemaPlugIn();
    //GeopistaSaveMapPlugIn geopistaSaveMapPlugIn = new GeopistaSaveMapPlugIn();
    //GeopistaLoadMapPlugIn geopistaLoadMapPlugIn = new GeopistaLoadMapPlugIn();
   GeopistaPrintPlugIn geopistaPrintPlugIn = new GeopistaPrintPlugIn();
    SeriePrintPlugIn geopistaSeriesPrintPlugin= new SeriePrintPlugIn();
    
    
    //GeopistaBookmarksPlugIn geopistaBookmarksPlugIn = new GeopistaBookmarksPlugIn();
    //OpenProjectPlugIn openProjectPlugIn = new OpenProjectPlugIn();
    //GeopistaSearchByAttributePlugIn geopistaSearchByAttributePlugIn = new GeopistaSearchByAttributePlugIn();
    //GeopistaNumerosPoliciaPlugIn geopistaNumerosPoliciaPlugIn = new GeopistaNumerosPoliciaPlugIn();
    //GeopistaFusionPlugIn fusionPlugIn = new GeopistaFusionPlugIn();
    //GeopistaActivateLayerPlugIn geopistaActivateLayerPlugIn = new GeopistaActivateLayerPlugIn();
    EditablePlugIn editablePlugIn = new EditablePlugIn(geopistaEditingPlugIn);
    //LogEventPlugIn logEventPlugIn = new LogEventPlugIn();

    //geopistaEditor1.addPlugIn(logEventPlugIn);
    //geopistaEditor1.addPlugIn(changeStylesPlugIn);
    //geopistaEditor1.addPlugIn(zoomToFullExtentPlugIn);
    //geopistaEditor1.addPlugIn(deleteSelectedItemsPlugIn);
    //geopistaEditor1.addPlugIn(undoPlugIn);
    //geopistaEditor1.addPlugIn(redoPlugIn);
    //geopistaEditor1.addPlugIn(geopistaValidatePlugin);
    geopistaEditor1.addPlugIn(geopistaEditingPlugIn);
    //geopistaEditor1.addPlugIn(geopistaFeatureSchemaPlugIn);
    //geopistaEditor1.addPlugIn(openProjectPlugIn);
    //geopistaEditor1.addPlugIn(geopistaSaveMapPlugIn);
    //geopistaEditor1.addPlugIn(geopistaLoadMapPlugIn);
    geopistaEditor1.addPlugIn(geopistaPrintPlugIn);
    geopistaEditor1.addPlugIn(geopistaSeriesPrintPlugin);
    //geopistaEditor1.addPlugIn(geopistaBookmarksPlugIn);
    //geopistaEditor1.addPlugIn(geopistaSearchByAttributePlugIn);
    //geopistaEditor1.addPlugIn(geopistaNumerosPoliciaPlugIn);
    //geopistaEditor1.addPlugIn(fusionPlugIn);
    //geopistaEditor1.addPlugIn(changeSLDStylesPlugIn);
    //geopistaEditor1.addPlugIn(geopistaActivateLayerPlugIn);
    //geopistaEditor1.addPlugIn(geopistaEditingPlugIn);
    geopistaEditor1.addPlugIn(editablePlugIn);
    

    geopistaEditor1.addPlugIn("com.geopista.ui.plugin.scalebar.GeopistaScaleBarPlugIn");    
    geopistaEditor1.addPlugIn("com.geopista.ui.snap.GeopistaInstallGridPlugIn");
    geopistaEditor1.addPlugIn("com.vividsolutions.jump.workbench.ui.plugin.ViewAttributesPlugIn","Row.gif");
    geopistaEditor1.addPlugIn("com.geopista.ui.plugin.GeopistaOptionsPlugIn");
    geopistaEditor1.addPlugIn("com.vividsolutions.jump.workbench.ui.plugin.skin.InstallSkinsPlugIn");

    this.setLocation(150,50);
    this.setVisible(true);
    // DEFINICION DE CAPAS

    jButton5.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton5_actionPerformed(e);
        }
      });
    jButton5.setBounds(new Rectangle(540, 485, 110, 40));
    jButton5.setText("Imprimir");
   
     //layer09.setEditable(true);
    /*GeopistaLayer layer10= (GeopistaLayer) geopistaEditor1.loadData(basepath+"/NumerosPolicia/numeros_policia_con_join.shp","Catastro");
    layer10.setActiva(true);*/
    
    //layer07.addStyle(new BasicStyle(new Color(255,255,255)));

    //Layer layer08 = geopistaEditor1.loadData(basepath+"/parcelas/rus.shp","Catastro");
    /*jButton5.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton5_actionPerformed(e);
        }
      });*/
    //jButton5.setBounds(new Rectangle(535, 485, 105, 40));
    //jButton5.setText("Imprimir");
    jButton4.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton4_actionPerformed(e);
        }
      });
    jButton4.setBounds(new Rectangle(400, 485, 120, 40));
    jButton4.setText("Cargar Mapa");
  //  layer08.setActiva(false);
    //layer08.addStyle(new BasicStyle(new Color(64,64,64)));
    
 //  Layer layer09 = geopistaEditor1.loadData(basepath+"/carreteras/municipios.shp","Parcelas");
 //   layer09.setActiva(true);
    //layer09.addStyle(new BasicStyle(new Color(220,220,220)));

    
 //   Layer layer10 = geopistaEditor1.loadData(basepath+"/calificacion_suelo.jml","Parcelas");
 //   layer10.setActiva(true);
    
    
    //Layer layer01 = geopistaEditor1.loadData(basepath+"/data/Herrera de Pisuerga/Catastro/ALTIPUN.SHP","Catastro");
    //layer01.setActiva(true);
    ///layer01.setActiva(true);
    //layer01.addStyle(new BasicStyle(new Color(255,255,128)));
    //layer01.setVisible(false);
    
    //Layer layer02 = geopistaEditor1.loadData(basepath+"/data/Herrera de Pisuerga/Catastro/CONSTRU.SHP","Catastro");
   //layer02.setActiva(true);
    //BasicStyle estilo = new BasicStyle(new Color(220,220,220));
    //estilo.setAlpha(50);
    //estilo.setLineColor(new Color(0,0,128));
    
    //layer02.setVisible(false);
    //layer02.addStyle((Style) estilo);
    

    //Layer layer03 = geopistaEditor1.loadData(basepath+"/data/Herrera de Pisuerga/Catastro/EJES.SHP","Catastro");
    //layer03.setActiva(true);

    //layer03.setActiva(true);
    //layer03.addStyle(new BasicStyle(new Color(220,32,0)));
    
    //Layer layer04 = geopistaEditor1.loadData(basepath+"/data/Herrera de Pisuerga/Catastro/ELEMLIN.SHP","Catastro");
    //layer04.setActiva(true);
    //layer04.setVisible(false);

    //Layer layer05 = geopistaEditor1.loadData(basepath+"/data/Herrera de Pisuerga/Catastro/ELEMPUN.SHP","Catastro");
    //layer05.setActiva(true);
    //layer05.addStyle(new BasicStyle(new Color(246,197,103)));

    //layer05.setActiva(true);
    //layer05.setVisible(false);

    

    //Layer layer06 = geopistaEditor1.loadData(basepath+"/data/Herrera de Pisuerga/Infraestructuras/redesdis.shp","Catastro");


    //layer06.setActiva(true);
    //layer06.setVisible(false);

    // CAPA DE LABELS : NO SE VISUALIZA
    //Layer layer6 = geopistaEditor1.loadData("C:/data/Herrera de Pisuerga/Catastro/ELEMTEX.SHP","Catastro");
    //layer6.setActiva(true);
    //layer6.setVisible(false);
    


    geopistaEditor1.addGeopistaListener(new GeopistaListener(){

      public void selectionChanged(AbstractSelection abtractSelection)
      {
          System.out.println("Recibiendo en cliente evento de cambio de seleccion de feature: "+abtractSelection.getSelectedItems());
          //eventFrame.setAttribute(abtractSelection, layer09);

      }

      public void featureAdded(FeatureEvent e)
      {
          System.out.println("Recibiendo en cliente evento de nueva Feature: "+e.getType());
      }

      public void featureRemoved(FeatureEvent e)
      {
          System.out.println("Recibiendo en cliente evento de borrado de Feature: "+e.getType());

      }

      public void featureModified(FeatureEvent e)
      {
        System.out.println("Recibiendo en cliente evento de Modificacion de Feature: "+e.getType());
      }

      public void featureActioned(AbstractSelection abtractSelection)
      {
        System.out.println("Recibiendo en cliente evento de cambio de accion en feature: "+abtractSelection.getSelectedItems());
      }
      
    });


  }
public GeopistaEditor  getEditor()
{
	return geopistaEditor1;
}
public void initEditor()
{

	String basepath = "C:/Geopista/datos";
	    GeopistaLayer layer07;
		try
		{
		layer07 = (GeopistaLayer) geopistaEditor1.loadData(basepath+"/ambitos_gestion.jml","Catastro");
		
		layer07.setActiva(false);
	     //layer07.setEditable(true);
	    GeopistaLayer layer08 = (GeopistaLayer) geopistaEditor1.loadData(basepath+"/entidadessingulares.jml","Catastro");
	    layer08.setActiva(false);
	    //layer08.setEditable(true);
	    GeopistaLayer layer09= (GeopistaLayer) geopistaEditor1.loadData(basepath+"/parcelas.jml","Catastro");
	    layer09.setActiva(false);
		} catch (Exception e)
		{
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
}
  private void jButton1_actionPerformed(ActionEvent e)
  {
     geopistaEditor1.reset();
  }

  private void jButton2_actionPerformed(ActionEvent e)
  {
     geopistaEditor1.showLayerName(true);
  }

  private void jButton3_actionPerformed(ActionEvent e)
  {
    geopistaEditor1.showLayerName(false);
  }

  private void jButton4_actionPerformed(ActionEvent e)
  {
    geopistaEditor1.reset();
    try
    {
      geopistaEditor1.loadMap("geopista:///2");
   
      //GeopistaPrintPlugIn geopistaPrint = new GeopistaPrintPlugIn();
      //GeopistaUtil.executePlugIn(geopistaPrint, geopistaEditor1.getContext(),new TaskMonitorManager());
      //geopistaPrint.execute(new PlugInContext(geopistaEditor1.getContext(),geopistaEditor1.getTask(),(LayerManagerProxy) geopistaEditor1.getFrame() , geopistaEditor1.getLayerNamePanel(), geopistaEditor1.getLayerViewPanel()));
    }catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void jButton5_actionPerformed(ActionEvent e)
  {
    JDialog a1 = new JDialog(aplicacion.getMainFrame(),true);
    aplicacion.setMainFrame(this);
    WizardComponent d = new WizardComponent(aplicacion, "Generador de Listados", null);
               d.init(new WizardPanel[] {
            new GeopistaImportarAmbitos01Panel(), new GeopistaImportarAmbitos02Panel(), new GeopistaImportacionPanel()});

    SeriePrintPlugIn.addPanels(d,geopistaEditor1.getContext());
    blackboardInformes.put("geopistaSeriePrintPlugInPrintDialog",d);

    a1.getContentPane().add(d);               
    a1.setSize(400,400);
    a1.setVisible(true);
    SeriePrintPlugIn geopistaPrint = new SeriePrintPlugIn();
    GeopistaUtil.executePlugIn(geopistaPrint,geopistaEditor1.getContext(),new TaskMonitorManager());
   //geopistaEditor1.printMap();
  }

  /*private void jButton5_actionPerformed(ActionEvent e)
  {

    Image image = null;
    try
    {
      image = GeopistaUtil.printMap(400,400,geopistaEditor1.getLayerViewPanel());
    }catch(Exception e1){}

    GeopistaLayerNamePanel leyendas = new GeopistaLayerNamePanel(
                geopistaEditor1.getLayerViewPanel(),
                new GeopistaLayerTreeModel(geopistaEditor1.getLayerViewPanel()),
                geopistaEditor1.getLayerViewPanel().getRenderingManager(),
                new HashMap());
   

    
   
    PrintCanvas source = new PrintCanvas(image,leyendas,false);
    

    JFrame j = new JFrame();
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                           leyendas, source);
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerLocation(150);    

        

    j.getContentPane().add(splitPane,null);
    j.setSize(900,500);
    j.setVisible(true); 

    PrintCanvas source2 = new PrintCanvas(image,leyendas,true);

    JFrame j2 = new JFrame();
      

    j2.getContentPane().add(source2,null);
    j2.setSize(900,500);
    j2.setVisible(true); 
    
    //PagePrint imprimir = new PagePrint();
    //imprimir.printIt(source);
  }*/
}

class Conclusion extends WindowAdapter
{
  public void windowClosing(WindowEvent evt)
  {
    System.exit(0);
  }
}