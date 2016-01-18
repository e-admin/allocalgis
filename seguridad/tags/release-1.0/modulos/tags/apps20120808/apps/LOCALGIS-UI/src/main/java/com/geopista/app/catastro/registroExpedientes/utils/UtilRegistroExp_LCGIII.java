package com.geopista.app.catastro.registroExpedientes.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import com.geopista.app.utilidades.ShowMaps;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.CConstantesComando;
import com.geopista.security.GeopistaAcl;
import com.geopista.server.administradorCartografia.CancelException;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 19-dic-2006
 * Time: 11:09:28
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase de utilidades que implementa fuciones que pueden usar varias clases. Clase estatica
 * que puede ser accesible desde la parte cliente.
 * */

public class UtilRegistroExp_LCGIII
{
	private static Logger logger = Logger.getLogger(UtilRegistroExp_LCGIII.class);
    public static Icon iconoExpediente;
    public static Icon iconoPersona;
    public static Icon iconoZoom;
    public static Icon iconoFlechaDerecha;
    public static Icon iconoFlechaIzquierda;
    public static Icon iconoDeleteParcela;
    public static Icon iconoAbrirDirectorio;

    

    

    /**
     * Metodo que carga un mapa en un frame pasado por parametro. LLama a los metodos de geopista y carga los plugin
     * que no se pueden cargar desde el workbench.
     *
     * @param padre Frame que donde se carga.
     * @param panel Panel donde se carga.
     * @param geopistaEditor Editor de geopista.
     * @param id_map El id del mapa.
     * @param editable Booleano que indica si se podra editar el mapa.
     * @param visible
     * @param novisible
     * @return boolean Devuelve un boolean indicando si se ha realizado correctamente o no.
     * */
    //LCGIII.Desplazado del paquete LOCALGIS-Catastro
    public static boolean showGeopistaMap(JFrame padre, GeopistaEditor geopistaEditor, int id_map,
                                          boolean editable, String visible, String novisible,
                                          TaskMonitor monitor) throws CancelException
    {
          try
          {
            geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
            geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
            geopistaEditor.addCursorTool("Measure", "com.geopista.ui.cursortool.GeopistaMeasureTool");
            geopistaEditor.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");
            geopistaEditor.addCursorTool("Dynamic clip tool", "com.vividsolutions.jump.workbench.ui.cursortool.UpdateDynamicClipTool");
            geopistaEditor.addCursorTool("Dynamic fence tool", "com.vividsolutions.jump.workbench.ui.cursortool.UpdateDynamicFenceTool");
            
             /** comprobamos que ya haya sido cargado el mapa para no volver a hacerlo */
             if ((geopistaEditor.getLayerManager().getLayers() != null) && (geopistaEditor.getLayerManager().getLayers().size() > 0))
             {
                 ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
                 geopistaEditor.getSelectionManager().clear();
                 geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
                 return true;
             }
//             if (ShowMaps.showMap(CConstantesComando.adminCartografiaUrl,geopistaEditor, id_map, editable,padre)!=null)
//             {
                try
                {
                	geopistaEditor.loadMap("geopista:///"+id_map,monitor);
                	
                    for(Iterator it=geopistaEditor.getLayerManager().getLayers().iterator();it.hasNext();)
                    {
                        GeopistaLayer layer=(GeopistaLayer)it.next();
                        layer.setEditable(false);
                        layer.setActiva(true);
                    }
                }
                catch (CancelException e1){
                	throw e1;
                }
                catch(Exception e){
                	
                	return false;
                }
                
                ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
                geopistaEditor.getSelectionManager().clear();
                geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
                return true;
//            }
            

          }
          catch (CancelException e1){
        	  throw e1;
          }
          catch(Exception e)
          {
              String sMensaje=  (e.getMessage()!=null&&e.getMessage().length()>0?e.getMessage():e.toString());
              Throwable t=e.getCause();
              int i=0;
              while (t!=null&&i<10)
              {
                  sMensaje=  (t.getMessage()!=null&&t.getMessage().length()>0?t.getMessage():t.toString());
                  t=t.getCause();
                  i++;
              }
              JOptionPane.showMessageDialog(padre, "Error al mostrar el mapa.\nERROR: "+sMensaje);
              logger.error("Error al cargar el mapa de Registro de expediente: ",e);
              return false;
          }
    }
    
    /**
     * Metodo que borra caracteres de componentes editables.
     *
     * @param comp El componente en el que borrar los caracteres.
     * @param maxLong El tamaño maximo que se quiere en el componente.
     * */
    //LCGIII.Desplazado del paquete LOCALGIS-Catastro
    public static void retrocedeCaracter(final JTextComponent comp, final int maxLong)
    {
        Runnable checkLength = new Runnable()
        {
            public void run()
            {
                comp.setText(comp.getText().substring(0,maxLong));
            }
        };
        SwingUtilities.invokeLater(checkLength);
    }
}
