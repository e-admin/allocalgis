/*
 * Created on 03-jun-2004
 */
package ejemplosgeopista;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaListener;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.GeopistaLoadMapPlugIn;
import com.geopista.ui.plugin.GeopistaSaveMapPlugIn;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.plugin.edit.GeopistaFeatureSchemaPlugIn;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.plugin.DeleteSelectedItemsPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.OpenProjectPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.RedoPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.SaveProjectAsPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.SaveProjectPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.UndoPlugIn;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;
import com.geopista.model.GeopistaLayer;
import java.util.ResourceBundle;

/**
 * @author Enxenio, S.L.
 */
public class GeoPISTAFramePintadoSLD extends JFrame {
  private GeopistaEditor geopistaEditor1 = new GeopistaEditor();
  private JButton jButton1 = new JButton();
  private JButton jButton2 = new JButton();
  private JButton jButton3 = new JButton();

  public JLabel eventLabel = new JLabel();
  public JPanel eventPanel = new JPanel();

  //private GeopistaEventFrame eventFrame = new GeopistaEventFrame();
  Layer layer09 = null;
  private JButton jButton4 = new JButton();
  ResourceBundle Rb=null;

  public GeoPISTAFramePintadoSLD()
  {
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
	this.getContentPane().add(jButton4, null);
	this.getContentPane().add(jButton3, null);
	this.getContentPane().add(jButton2, null);
	this.getContentPane().add(jButton1, null);
	this.getContentPane().add(geopistaEditor1, null);

	this.addWindowListener(new WindowAdapter() {
	  	public void windowClosing(WindowEvent evt){System.exit(0);}
	 });

	geopistaEditor1.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
	geopistaEditor1.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
	geopistaEditor1.addCursorTool("Measure", "com.vividsolutions.jump.workbench.ui.cursortool.MeasureTool");
	geopistaEditor1.addCursorTool("Feature Info", "com.geopista.ui.cursortool.GeopistaFeatureInfoTool");

	com.geopista.style.sld.ui.plugin.ChangeSLDStylesPlugIn changeStylesPlugIn = new com.geopista.style.sld.ui.plugin.ChangeSLDStylesPlugIn();  
	ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
	GeopistaEditingPlugIn geopistaEditingPlugIn = new GeopistaEditingPlugIn();
	UndoPlugIn undoPlugIn = new UndoPlugIn();
	RedoPlugIn redoPlugIn = new RedoPlugIn();
	GeopistaValidatePlugin geopistaValidatePlugin = new GeopistaValidatePlugin();
	DeleteSelectedItemsPlugIn deleteSelectedItemsPlugIn = new DeleteSelectedItemsPlugIn();
	SaveProjectAsPlugIn saveProjectAsPlugIn = new SaveProjectAsPlugIn();
	SaveProjectPlugIn saveProjectPlugIn = new SaveProjectPlugIn(saveProjectAsPlugIn);
	GeopistaFeatureSchemaPlugIn geopistaFeatureSchemaPlugIn = new GeopistaFeatureSchemaPlugIn();
	GeopistaSaveMapPlugIn geopistaSaveMapPlugIn = new GeopistaSaveMapPlugIn();
	GeopistaLoadMapPlugIn geopistaLoadMapPlugIn = new GeopistaLoadMapPlugIn();

	OpenProjectPlugIn openProjectPlugIn = new OpenProjectPlugIn();

	geopistaEditor1.addPlugIn(changeStylesPlugIn);
	geopistaEditor1.addPlugIn(zoomToFullExtentPlugIn);
	geopistaEditor1.addPlugIn(deleteSelectedItemsPlugIn);
	geopistaEditor1.addPlugIn(undoPlugIn);
	geopistaEditor1.addPlugIn(redoPlugIn);
	geopistaEditor1.addPlugIn(geopistaValidatePlugin);
	geopistaEditor1.addPlugIn(geopistaEditingPlugIn);
	geopistaEditor1.addPlugIn(geopistaFeatureSchemaPlugIn);
	geopistaEditor1.addPlugIn(openProjectPlugIn);
	geopistaEditor1.addPlugIn(geopistaSaveMapPlugIn);
	geopistaEditor1.addPlugIn(geopistaLoadMapPlugIn);

	geopistaEditor1.addPlugIn("com.geopista.ui.plugin.scalebar.GeopistaInstallScaleBarPlugIn");    
	geopistaEditor1.addPlugIn("com.geopista.ui.snap.GeopistaInstallGridPlugIn");
	geopistaEditor1.addPlugIn("com.vividsolutions.jump.workbench.ui.plugin.ViewAttributesPlugIn","Row.gif");
	geopistaEditor1.addPlugIn("com.geopista.ui.plugin.GeopistaOptionsPlugIn");
	geopistaEditor1.addPlugIn("com.vividsolutions.jump.workbench.ui.plugin.skin.InstallSkinsPlugIn");

	this.setLocation(150,50);
	this.setVisible(true);
	// DEFINICION DE CAPAS
  
  Rb = ResourceBundle.getBundle("GeoPista");
  
	String basepath = Rb.getString("url.capas");

	GeopistaLayer reddedistribucionpormaterial = (GeopistaLayer) geopistaEditor1.loadData(basepath + "redesdis.shp", "Temáticos");
	reddedistribucionpormaterial.removeStyle(reddedistribucionpormaterial.getBasicStyle());
	reddedistribucionpormaterial.addStyle(SLDFactory.createSLDStyle("styles.xml", "RedDeDistribucionPorMaterial", reddedistribucionpormaterial.getName()));
	reddedistribucionpormaterial.setName("Red de distribución por material");
	reddedistribucionpormaterial.setActiva(true);

	GeopistaLayer reddedistribucion = (GeopistaLayer) geopistaEditor1.loadData(basepath + "redesdis.shp", "Abastecimiento");
	reddedistribucion.removeStyle(reddedistribucion.getBasicStyle());
	reddedistribucion.addStyle(SLDFactory.createSLDStyle("styles.xml", "default:RedDeDistribucion", reddedistribucion.getName()));
	reddedistribucion.setName("Redes de distribución");
	
	GeopistaLayer depositos = (GeopistaLayer) geopistaEditor1.loadData(basepath + "depositos.shp", "Abastecimiento");
	depositos.removeStyle(depositos.getBasicStyle());
	depositos.addStyle(SLDFactory.createSLDStyle("styles.xml", "default:Depositos", depositos.getName()));
	depositos.setName("Depósitos");

	GeopistaLayer conducciones = (GeopistaLayer) geopistaEditor1.loadData(basepath + "conducciones.shp", "Abastecimiento");
	conducciones.removeStyle(conducciones.getBasicStyle());
	conducciones.addStyle(SLDFactory.createSLDStyle("styles.xml", "default:Conducciones", conducciones.getName()));
	conducciones.setName("Conducciones");

	GeopistaLayer captaciones = (GeopistaLayer) geopistaEditor1.loadData(basepath + "captaciones.shp", "Abastecimiento");
	captaciones.removeStyle(captaciones.getBasicStyle());
	captaciones.addStyle(SLDFactory.createSLDStyle("styles.xml", "default:Captaciones", captaciones.getName()));
	captaciones.setName("Captaciones");

	GeopistaLayer emisarios = (GeopistaLayer) geopistaEditor1.loadData(basepath + "emisarios.shp", "Saneamiento");
	emisarios.removeStyle(emisarios.getBasicStyle());
	emisarios.addStyle(SLDFactory.createSLDStyle("styles.xml", "default:Emisario", emisarios.getName()));
	emisarios.setName("Emisarios");

	GeopistaLayer depuradoras = (GeopistaLayer) geopistaEditor1.loadData(basepath + "depurado.shp", "Saneamiento");
	depuradoras.removeStyle(depuradoras.getBasicStyle());
	depuradoras.addStyle(SLDFactory.createSLDStyle("styles.xml", "default:Depuradoras", depuradoras.getName()));
	depuradoras.setName("Depuradoras");

	GeopistaLayer colectores = (GeopistaLayer) geopistaEditor1.loadData(basepath + "colectores.shp", "Saneamiento");
	colectores.removeStyle(colectores.getBasicStyle());
	colectores.addStyle(SLDFactory.createSLDStyle("styles.xml", "default:Colector", colectores.getName()));
	colectores.setName("Colectores");

	GeopistaLayer ramales = (GeopistaLayer) geopistaEditor1.loadData(basepath + "ramales.shp", "Saneamiento");
	ramales.removeStyle(ramales.getBasicStyle());
	ramales.addStyle(SLDFactory.createSLDStyle("styles.xml", "default:RamalDeSaneamiento", ramales.getName()));
	ramales.setName("Ramales");

	GeopistaLayer areasConstruidas = (GeopistaLayer) geopistaEditor1.loadData(basepath + "masa.shp", "Contexto");
	areasConstruidas.removeStyle(areasConstruidas.getBasicStyle());
	areasConstruidas.addStyle(SLDFactory.createSLDStyle("styles.xml", "default:AreaConstruida", areasConstruidas.getName()));
	areasConstruidas.setName("Áreas Construidas");

	GeopistaLayer calles = (GeopistaLayer) geopistaEditor1.loadData(basepath + "ejes.shp", "Contexto");
	calles.removeStyle(calles.getBasicStyle());
	calles.addStyle(SLDFactory.createSLDStyle("styles.xml", "default:Calles", calles.getName()));
	calles.setName("Calles");

	GeopistaLayer limites = (GeopistaLayer) geopistaEditor1.loadData(basepath + "limites.shp", "Contexto");
	limites.removeStyle(limites.getBasicStyle());
	limites.addStyle(SLDFactory.createSLDStyle("styles.xml", "default:Limites", limites.getName()));
	limites.setName("Núcleos");

	GeopistaLayer alumbrado = (GeopistaLayer) geopistaEditor1.loadData(basepath + "alumbrado.shp", "Contexto");
	alumbrado.removeStyle(alumbrado.getBasicStyle());
	alumbrado.addStyle(SLDFactory.createSLDStyle("styles.xml", "default:Alumbrado", alumbrado.getName()));
	alumbrado.setName("Alumbrado");


	//Layer layer07 = geopistaEditor1.loadData(basepath+"/parcelas/parus.shp","Catastro");
	//ayer07.setActiva(true);
	//layer07.addStyle(new BasicStyle(new Color(255,255,255)));

	//Layer layer08 = geopistaEditor1.loadData(basepath+"/parcelas/rus.shp","Catastro");
	jButton4.addActionListener(new ActionListener()
	  {
		public void actionPerformed(ActionEvent e)
		{
		  jButton4_actionPerformed(e);
		}
	  });
	jButton4.setBounds(new Rectangle(400, 485, 120, 40));
	jButton4.setText("Cargar Mapa");
	//layer08.setActiva(false);
	//layer08.addStyle(new BasicStyle(new Color(64,64,64)));


	/*
	Layer layer10 = geopistaEditor1.loadData("C:/data/Herrera de Pisuerga/Catastro/SUBPARCE.SHP","Catastro");
	layer10.setActiva(true);
	*/

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
    String dirbase = AppContext.getApplicationContext().getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH,true);;
	  geopistaEditor1.loadMap(dirbase+"/Mapa de Prueba de Grabacion/geopistamap.gpc");
    
	}catch(Exception ex)
	{
	  ex.printStackTrace();
	}
  }
}