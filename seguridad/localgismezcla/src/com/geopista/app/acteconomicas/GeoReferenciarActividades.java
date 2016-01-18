package com.geopista.app.acteconomicas;

import com.geopista.ui.wizard.WizardPanel;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.images.IconLoader;
import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.feature.SearchableFeatureCollection;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.GeopistaFeature;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.Feature;

import javax.swing.*;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;
import java.sql.*;
import java.math.BigDecimal;


/**
 * Creado por SATEC.
 * User: angeles
 * Date: 05-jun-2006
 * Time: 10:27:17
 */
/**
 * Clase utilizada para georreferenciar las actividades economicas
 *
 */
public class GeoReferenciarActividades extends JPanel implements  WizardPanel
{
    Logger logger =Logger.getLogger(GeoReferenciarActividades.class);
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = aplicacion.getBlackboard();
    private WizardContext wizardContext;
    ArrayList listaActividades= new ArrayList();


    //Componentes de la pantalla
    private JLabel jLabelImagen = new JLabel();
    private JLabel jLabelComent = new JLabel();
    private JPanelFusionarVias jPanelFusionarVias= new JPanelFusionarVias();


    /**
     * Constructor de la clase
     */
    public GeoReferenciarActividades()
    {
           try{
                jbInit();
            } catch (Exception e){
                logger.error("Error al importar actividades economicas",e);
            }
    }
    /**
     * Inicializa los componenetes de la pantalla
     * @throws Exception
     */
    private void jbInit() throws Exception
    {
        setLayout(new BorderLayout());
        setName(aplicacion.getI18nString("importar.asistente.elementos.acteconomicas.titulo1"));
        jLabelImagen.setIcon(IconLoader.icon("inf_referencia.png"));
        jLabelImagen.setBounds(new Rectangle(15, 20, 110, 490));
        jLabelImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        add(jLabelComent, BorderLayout.NORTH);
        add(jLabelImagen, BorderLayout.WEST);
        add(jPanelFusionarVias,BorderLayout.CENTER);
        addAyudaOnline();
    }
 	/**
  	 * Ayuda Online
  	 * 
  	 */
  	private void addAyudaOnline() {
		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke("F1"), "action F1");

		this.getActionMap().put("action F1", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
	 			String uriRelativa = "/Geocuenca:ImportarActividadesEconomicas#Asociar_direcciones";
				GeopistaBrowser.openURL(aplicacion.getString("ayuda.geopista.web")
						+ uriRelativa);
			}
		});
  	}    
    /**
     * Metodo que se ejecuta al inicio y que intenta georrefenrenciar las
     * actividades economicas
     * @param dataMap
     */
    public void enteredFromLeft(Map dataMap)
    {
         wizardContext.previousEnabled(false);
        /*srid = Integer.parseInt((String) blackboardInformes
                .get(GeopistaImportarCallesSeleccionElemento.MUNICIPIOSRID));*/

        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion
                .getI18nString("GeopistaMostrarActEconomicas.ImportandoDatos"));
        progressDialog.report(aplicacion
                .getI18nString("GeopistaMostrarActEconomicas.ImportandoDatos"));
        progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {

                    // Wait for the dialog to appear before starting the
                    // task. Otherwise
                    // the task might possibly finish before the dialog
                    // appeared and the
                    // dialog would never close. [Jon Aquino]
                    new Thread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                {
                                    progressDialog.report(aplicacion.getI18nString("acteconomicas.report.obtenerdatos"));
                                    Vector vias=obtenerViasBD();

                                    ArrayList actividadesNOGeo=null;
                                    if (blackboard.get("georreferenciarLocal")==null ||
                                            !((Boolean)blackboard.get("georreferenciarLocal")).booleanValue())
                                        actividadesNOGeo = geoReferenciarActividadesBD(progressDialog);
                                    else
                                        actividadesNOGeo = geoReferenciarActividadesLocal(progressDialog);
                                    jPanelFusionarVias.cargarTabla(vias);
                                    jPanelFusionarVias.cargarActividadesEconomicas(actividadesNOGeo);
                                    jPanelFusionarVias.cargarActividadesGeorreferenciadas(listaActividades);
                                } catch (Exception e)
                                {
                                    logger.error("Error",e);
                                }
                                finally
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

    //Primero tratamos de georeferencia las actividade economicas
    private ArrayList geoReferenciarActividadesBD(TaskMonitorDialog progressDialog)
    {
        ArrayList actividadeEco=((ArrayList)blackboard.get("listaActividades"));
        try
        {
                ArrayList actividadesNoGeo= new ArrayList();
                progressDialog.report(aplicacion.getI18nString("acteconomicas.report.georreferenciar"));
                int i=0;
                final int total=actividadeEco.size();
                for (Iterator it=actividadeEco.iterator();it.hasNext();)
                {
                    DatosImportarActividades datos= (DatosImportarActividades)it.next();
                    FeatureCollection fc=aplicacion.getClient().getGeoRefAddress(
                            datos.getTipoviaine()!=null?datos.getTipoviaine():"%",datos.getNombreviaine(),
                            datos.getRotulo()!=null?datos.getRotulo():"%",Locale.getDefault().toString());
                    if (fc==null|| fc.getFeatures().size()==0)
                    {
                        //SI no lo encuentro con todos los datos intento buscarlo sin numero ni tipo
                        fc=aplicacion.getClient().getGeoRefAddress("%",datos.getNombreviaine(),"%",Locale.getDefault().toString());
                        if (fc==null|| fc.getFeatures().size()==0)
                            actividadesNoGeo.add(datos);
                        else
                        {
                            datos.setGeometria(((Feature)fc.getFeatures().get(0)).getGeometry());
                            listaActividades.add(datos);
                        }
                    }
                    else
                    {
                        datos.setGeometria(((Feature)fc.getFeatures().get(0)).getGeometry());
                        listaActividades.add(datos);
                    }
                    i++;
                    progressDialog.report(i +" de "+total);
                }
                return actividadesNoGeo ;
        }catch(Exception e)
        {
             logger.error("Se ha producido un error al georreferenciar ",e);
             return actividadeEco;
        }

    }

     //Primero tratamos de georeferencia las actividade economicas
    private ArrayList geoReferenciarActividadesLocal(TaskMonitorDialog progressDialog)
    {

        ArrayList actividadeEco=((ArrayList)blackboard.get("listaActividades"));
        try
        {
                //Cargamos el mapa numeros
                progressDialog.report(aplicacion.getI18nString("acteconomicas.report.cargando"));
                GeopistaEditor geopistaEditor = new GeopistaEditor("workbench-properties-simple.xml");
                geopistaEditor.loadMap(aplicacion.getString("url.mapa.catastro"));
                progressDialog.report(aplicacion.getI18nString("acteconomicas.report.georreferenciar"));

                //Datos necesarios para buscar la calle
                GeopistaLayer capaCalles = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("vias");
                SearchableFeatureCollection searchableSourceFeatureCollection = new SearchableFeatureCollection(
                                       capaCalles.getFeatureCollectionWrapper()
                                       .getWrappee());
                GeopistaSchema featureSchema = (GeopistaSchema)capaCalles.getFeatureCollectionWrapper().getFeatureSchema();
                String nombreVia = featureSchema.getAttributeByColumn("nombrecatastro");

                //Datos necesarios para buscar el numero de policia
                GeopistaLayer capaNumPoli = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("numeros_policia");
                GeopistaSchema featureSchemaNumPoli = (GeopistaSchema)capaNumPoli.getFeatureCollectionWrapper().getFeatureSchema();
                SearchableFeatureCollection searchableSourceNumPoli = new SearchableFeatureCollection(
                                                  capaNumPoli.getFeatureCollectionWrapper()
                                                  .getWrappee(), featureSchemaNumPoli);

                String attRotulo = featureSchemaNumPoli.getAttributeByColumn("rotulo");
                String attIdVia = featureSchemaNumPoli.getAttributeByColumn("id_via");

                ArrayList actividadesNoGeo= new ArrayList();
                progressDialog.report(aplicacion.getI18nString("acteconomicas.report.georreferenciar"));
                int i=0;
                final int total=actividadeEco.size();
                for (Iterator it=actividadeEco.iterator();it.hasNext();)
                {
                    //Primero buscamos la calle
                    DatosImportarActividades datos= (DatosImportarActividades)it.next();
                    try
                    {
                        Feature fVia=(Feature)searchableSourceFeatureCollection.query(nombreVia, datos.getNombreviaine());
                        if (fVia==null)
                            actividadesNoGeo.add(datos);
                        else
                        {
                            //buscamos el numero de policia
                            FeatureCollection gcNumeros=(FeatureCollection)searchableSourceNumPoli.queryMulti(attIdVia, (new BigDecimal(((GeopistaFeature)fVia).getSystemId())));
                            if (gcNumeros==null)
                                actividadesNoGeo.add(datos);
                            else
                            {
                                boolean encontrado=false;
                                for (Iterator iter=gcNumeros.getFeatures().iterator();iter.hasNext();)
                                {
                                    Feature fNumPoli=(Feature)iter.next();
                                    if (fNumPoli.getAttribute(attRotulo)!=null && fNumPoli.getAttribute(attRotulo).equals(datos.getRotulo()))
                                    {
                                       datos.setGeometria(fNumPoli.getGeometry());
                                       encontrado=true;
                                       break;
                                    }
                                }
                                if (!encontrado) datos.setGeometria(((Feature)gcNumeros.getFeatures().get(0)).getGeometry());
                                listaActividades.add(datos);
                            }
                        }
                    }catch (Exception ex)
                    {
                        logger.error("Error al georreferenciar : "+datos.toString(),ex);
                    }
                    i++;
                    progressDialog.report(i +" de "+total);
                }
                 geopistaEditor=null;
                return actividadesNoGeo ;
        }catch(Exception e)
        {
             logger.error("Se ha producido un error al georreferenciar ",e);
             return actividadeEco;
        }

    }
    /**
     * Obtiene las vias del municipio
     */
     private Vector obtenerViasBD()
     {
            Connection con=null;
            Vector vias= new java.util.Vector();
            try {
                  //Mostramos el resto
                 con = getDBConnection();
                 PreparedStatement ps = con.prepareStatement("loadViasNumPolicia");
                 ps.setString(1,com.geopista.security.SecurityManager.getIdMunicipio());
                 ps.execute();
                 ResultSet rs=ps.getResultSet();
                 Via  auxVia=null;
                 while (rs.next()) {
                        if (auxVia == null || auxVia.getId()!=rs.getInt("id_vias"))
                        {
                           String nombreVia=  rs.getString("nombreviaine")!=null&&rs.getString("nombreviaine").length()>0?rs.getString("nombreviaine"):rs.getString("nombrecatastro");
                           auxVia = new Via(rs.getInt("id_vias"), rs.getString("tipoviaine"),
                                   nombreVia);
                           vias.add(auxVia);
                        }
                        if (rs.getString("id_numeros_policia")!=null && rs.getString("id_numeros_policia").length()>0)
                                            auxVia.addNumeroPolicia(new NumeroPolicia(rs.getInt("id_numeros_policia"),
                                                                                      rs.getString("rotulo"),
                                                                                      rs.getString("geometria")));
                 }

            } catch (Exception e)
            {
                 logger.error("Error ",e);
            }
            finally
            {
                 aplicacion.closeConnection(con, null, null, null);
            }
            return vias;
    }
    /**
     * Lo que queremos hacer antes de salir de la pantalla
     */
    public void exitingToRight() throws Exception
    {
        // Salimos y ponemos en el blackBoard la lista de actividades
       for (Enumeration e=jPanelFusionarVias.getActEcoGeorreferenciadas().elements();e.hasMoreElements();)
       {
           listaActividades.add(e.nextElement());
       }
       blackboard.put("listaActividades", listaActividades);
       exiting();
    }

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

    public String getID()
    {
        return "2";
    }

    public String getInstructions()
    {
        return " ";
    }

    public boolean isInputValid()
    {
        //TODO implementar este metodo
        /*if (tienePermiso)
        {
            return (!hayErroresFilas);
        } else
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("NoPermisos"));
            return false;
        } */
        return true;
    }

    private String nextID = "3";

    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }
    public String getNextID()
    {
        return nextID;
    }

    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }




     /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
         //System.out.println("GeoRreferenciarActividades.exiting DESTRUIDO");
         if (jPanelFusionarVias!=null)
         {jPanelFusionarVias.destroy();
           jPanelFusionarVias.removeAll();
            jPanelFusionarVias.setLayout(null);
         }
         jPanelFusionarVias=null;
         jLabelImagen=null;
         jLabelComent=null;
         //if (listaActividades!=null) listaActividades.clear();
         //blackboard.put("listaActividades", null);
         logger =null;
         aplicacion = null;
         blackboard = null;
         wizardContext=null;
         listaActividades= null;
         jLabelImagen = null;
         jLabelComent = null;
    }
    public Connection getDBConnection()
    {
        Connection conn = null;
        try
        {
            // Quitamos los drivers
            Enumeration e = DriverManager.getDrivers();
            while (e.hasMoreElements())
            {
                DriverManager.deregisterDriver((Driver) e.nextElement());
            }
            DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());
            String sConn = aplicacion.getString("geopista.conexion.url");
            conn = DriverManager.getConnection(sConn);
            AppContext app = (AppContext) AppContext.getApplicationContext();
            conn = app.getConnection();
            conn.setAutoCommit(false);
        } catch (Exception e)
        {
            return null;
        }

        return conn;

   }

} // de la clase general.


