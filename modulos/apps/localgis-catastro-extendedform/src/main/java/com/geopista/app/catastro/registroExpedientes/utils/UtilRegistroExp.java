/**
 * UtilRegistroExp.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

import com.geopista.app.catastro.gestorcatastral.IMainCatastro;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.TipoExpediente;
import com.geopista.app.catastro.registroExpedientes.paneles.DialogoCalendario;
import com.geopista.app.catastro.utils.ConstantesCatastro;
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

public class UtilRegistroExp
{
	private static Logger logger = Logger.getLogger(UtilRegistroExp.class);
    public static Icon iconoExpediente;
    public static Icon iconoPersona;
    public static Icon iconoZoom;
    public static Icon iconoFlechaDerecha;
    public static Icon iconoFlechaIzquierda;
    public static Icon iconoDeleteParcela;
    public static Icon iconoAbrirDirectorio;

    /**
     * Metodo que devuelve un String con la fecha del dia actual, con formato DD/MM/AAAA.
     *
     * @return String La fecha actual.
     */
    public static String showToday()
    {
        Calendar cal = new GregorianCalendar();
        Locale locale = Locale.getDefault();
        cal = Calendar.getInstance(TimeZone.getTimeZone(locale.toString()));
        int anno = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH) + 1; // 0 == Enero
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        String sMes = "";
        String sDia = "";
        if (mes < 10)
            sMes = "0" + mes;
        else
            sMes = "" + mes;
        if (dia < 10)
            sDia = "0" + dia;
        else
            sDia = "" + dia;

        return sDia + "/" + sMes + "/" + anno;
    }

    /**
     * Metodo que no permite que la pantalla se minimize mas de 600 en largo y del ancho pasado por parametro.
     *
     * @param desktop La ventana a analizar.
     * @param wid El ancho pasado.
     * @param hig El largo pasado.
     */
    public static void ajustaVentana(JFrame desktop, int wid, int hig)
    {
        if (desktop.getWidth()<wid || desktop.getHeight()<600)
        {
            desktop.setSize(wid, hig);
        }
    }

    /**
     * Metodo que habilita o deshabilita el menu principal y que checkea los permisos del usuario cada vez que eso
     * sucede, para comprobar los menus que se pueden activar o no.
     *
     * @param b Booleano que indica la accion a realizar, activar o desactivar.
     * @param desktop La ventana en la que se quiere activar o desactivar el menu
     */
    public static void menuBarSetEnabled(boolean b, JFrame desktop)
    {
        try
        {
            desktop.getJMenuBar().getMenu(0).setEnabled(b);
            desktop.getJMenuBar().getMenu(1).setEnabled(b);
            desktop.getJMenuBar().getMenu(2).setEnabled(b);
            desktop.getJMenuBar().getMenu(3).setEnabled(b);

            if (b)
            {		
                    GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("RegistroExpedientes");
                    ((IMainCatastro)desktop).applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
            }

        } catch (Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
        }
    }

    /**
     * Metodo que añade un asterisco a la derecha a un String.
     *
     * @param s String al que añadir el asterisco.
     * @return String El resultado de añadir el asterisco.
     */
    public static String annadirAsterisco(String s)
    {
        return "<html><p>" + s + " "+  "<FONT COLOR=\"#FF0000\" size=\"5\"><b>*</b></FONT></p></html>";
    }

    /**
     * Metodo que añade un asterisco a la izquierda de un label.
     *
     * @param s String al que añadir el asterisco.
     * @return String El resultado de añadir el asterisco
     */
    public static String getLabelConAsterisco(String s)
    {
        return "<html><p><FONT COLOR=\"#FF0000\">*</FONT>" + " " + s + "</p></html>";
    }

    /**
     * Metodo que inicializa en memoria los iconos que se van a visualizar en la aplicacion.
     */
    public static void inicializarIconos()
    {
        ClassLoader cl= (new UtilRegistroExp()).getClass().getClassLoader();
        iconoExpediente= new javax.swing.ImageIcon(cl.getResource("com/geopista/app/catastro/registroExpedientes/images/expediente.jpg"));
        iconoZoom = new javax.swing.ImageIcon(cl.getResource("com/geopista/app/catastro/registroExpedientes/images/zoom.gif"));
        iconoPersona= new javax.swing.ImageIcon(cl.getResource("com/geopista/app/catastro/registroExpedientes/images/persona.jpg"));
        iconoFlechaDerecha = new javax.swing.ImageIcon(cl.getResource("com/geopista/app/catastro/registroExpedientes/images/flecha_derecha.gif"));
        iconoFlechaIzquierda =  new javax.swing.ImageIcon(cl.getResource("com/geopista/app/catastro/registroExpedientes/images/flecha_izquierda.gif"));
        iconoDeleteParcela =  new javax.swing.ImageIcon(cl.getResource("com/geopista/app/catastro/registroExpedientes/images/delete_parcela.gif"));
        iconoAbrirDirectorio =  new javax.swing.ImageIcon(cl.getResource("com/geopista/app/catastro/registroExpedientes/images/abrir.gif"));
    }

    /**
     * Metodo que carga en memoria la imagen del estado actual del expediente. Segun este en el modo acoplado o desacoplado
     * se carga una imagen o otra.
     *
     * @param nombre String con el nombre del estado actual del expediente.
     * @return Icon El icono cargado.
     */
    public static Icon getIconoEstado(String nombre)
    {
        ClassLoader cl= (new UtilRegistroExp()).getClass().getClassLoader();
        String path = "com/geopista/app/catastro/registroExpedientes/images/";
        if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
        {
            path = path + "acoplado/";
        }
        else
        {
            path = path + "desacoplado/";
        }
        return new javax.swing.ImageIcon(cl.getResource(path + nombre + ".JPG"));
    }

    /**
     * Metodo que borra caracteres de componentes editables.
     *
     * @param comp El componente en el que borrar los caracteres.
     * @param maxLong El tamaño maximo que se quiere en el componente.
     * */
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

    /**
     * Convierte la fecha pasada por parametro de String a Date. Comprueba si el String tiene formato de fecha.
     *
     * @param fecha String de la fecha.
     * @return Date Fecha con formato Dato.
     * */
    public static Date getDate(String fecha)
    {
		try {
			if (esDate(fecha))
				return ConstantesCatastro.df.parse(fecha);
			else
				return null;
		} catch (Exception e)
        {
			return null;
		}
	}

    /**
     * Metodo que lanza el dialogo calendario para que el usuario elija una fecha.
     *
     * @param frame Frame que lanza el dialogo.
     * @return Date Fecha elegida por el usuario.
     */
    public static java.util.Date showCalendarDialog(JFrame frame)
    {
		DialogoCalendario dialog = new DialogoCalendario(frame, true);
		dialog.setLocation(20, 20);
		dialog.setResizable(false);
		dialog.show();
		return dialog.getFecha();
	}

    /**
     * Devuelve un booleano indicando si un String tiene formato de fecha o no.
     *
     * @param fecha String de la fecha.
     * @return boolean Indica si es una fecha o no.
     * */
    public static boolean esDate(String fecha)
    {
		try
        {
			Date date = ConstantesCatastro.df.parse(fecha);
		}
        catch (ParseException pe)
        {
			return false;
		}
		return true;
	}

    /**
     * Metodo que convierte una fecha en formato Date a una fecha en formato String de la forma DD/MM/AAAA
     *
     * @param fecha La fecha que se desea convertir.
     * @return String El resultado.
     * */
    public static String formatFecha(Date fecha)
    {
		if (fecha == null)
        {
			return "";
		}
		return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
	}

    /**
     * Metodo que convierte una hora en formato Time a una hora en formato String de la forma HH:MM:SS
     *
     * @param hora La hora que se desea convertir.
     * @return String El resultado.
     * */
    public static String formatHora(Time hora)
    {
		if (hora == null)
        {
			return "";
		}
		return new SimpleDateFormat("HH:MM:SS").format(hora);
	}

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
    public static boolean showGeopistaMap(JFrame padre, JPanel panel, GeopistaEditor geopistaEditor, int id_map,
                                          boolean editable, String visible, String novisible,TaskMonitor monitor)
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
                 panel.add(geopistaEditor);
                 geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
                 return true;
             }
             if (ShowMaps.showMap(CConstantesComando.adminCartografiaUrl,geopistaEditor, id_map, editable,padre)!=null)
             {
                try
                {
                    for(Iterator it=geopistaEditor.getLayerManager().getLayers().iterator();it.hasNext();)
                    {
                        GeopistaLayer layer=(GeopistaLayer)it.next();
                        layer.setEditable(false);
                        layer.setActiva(false);
                    }
                }catch(Exception e){}
                ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
                geopistaEditor.getSelectionManager().clear();
                panel.add(geopistaEditor);
                geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
                return true;
            }
            return false;

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
                	//geopistaEditor.loadMap("geopista:///"+id_map,monitor);
                	geopistaEditor.loadMap("geopista:///"+id_map);
                	
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
     * Metodo que devuelve un arrayList con features que concuerden con los parametros pasados.
     *
     * @param geopistaLayer Capa geopista donde buscar.
     * @param attributeName El nombre del atributo buscado
     * @param value El valor del atributo buscado.
     * @return Collection El resultado.
     * */
    public static Collection searchByAttribute(GeopistaLayer geopistaLayer, String attributeName, String value)
    {
		Collection finalFeatures = new ArrayList();

		List allFeaturesList = geopistaLayer.getFeatureCollectionWrapper().getFeatures();
		Iterator allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext())
        {
			Feature localFeature = (Feature) allFeaturesListIter.next();

			String nombreAtributo = localFeature.getString(attributeName).trim();

			if (nombreAtributo.equals(value))
            {
				finalFeatures.add(localFeature);
			}
		}
		return finalFeatures;
	}

    /**
     * Metodo que comprueba si un objeto es null, si es devuelve "", si no devuelve el objeto en formato string.
     *
     * @param o Objeto a analizar.
     * @return String El obejto en formato string.
     * */
    public static String checkNull(Object o)
    {
		if (o == null)
        {
			return "";
		}
		return o.toString();
	}

    /**
     * Inicializa los botones de la pantalla de gestion de expediente.
     * */
    public static void inicializaBotonesGestionExp()
    {
        ConstantesCatastro.botonesGestExp[0] = false;
        ConstantesCatastro.botonesGestExp[1] = false;
        ConstantesCatastro.botonesGestExp[2] = false;
        ConstantesCatastro.botonesGestExp[3] = false;
        ConstantesCatastro.botonesGestExp[4] = false;
        ConstantesCatastro.botonesGestExp[5] = false;
    }

    /**
     * Metodo que devuelve un objeto tipoExpediente que coincida el codigo con el parametro.
     *
     * @param codigoTipoExp El codigo con el que queremos comparar
     * @return TipoExpediente El obejto devuelto.
     * */
    public static TipoExpediente getTipoExpediente(String codigoTipoExp)
    {
        ArrayList aux = ConstantesCatastro.tiposExpedientes;
        for(int i=0;i< aux.size();i++)
        {
            if(((TipoExpediente)aux.get(i)).getCodigoTipoExpediente().equalsIgnoreCase(codigoTipoExp))
            {
                return (TipoExpediente)aux.get(i);
            }
        }
        return null;
    }
}
