package com.geopista.app.catastro.registroExpedientes.utils;

import com.geopista.app.AppContext;
import com.geopista.protocol.ListaEstructuras;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.I18N;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 18-ene-2007
 * Time: 14:40:56
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que carga aquellos parametros que tienen dominios de idiomas. Esta clase ha sido implementa al igual que
 * las que se encuentran en licencias o en inventario. Al inicio de la aplicacion se cargan las estructuras de la
 * base de datos. Para poder manejar esta clase se necesita la clase ListaEstructuras del modulo licencias.
 */

public class Estructuras
{
    private static boolean cargada=false;
    private static boolean iniciada=false;
    private static ListaEstructuras listaEstadosExpediente= new ListaEstructuras("Estados de Expediente");

    /**
     * Devuelve la lista de traducciones de estados del expediente.
     *
     * @return ListaEstructuras
     */
    public static ListaEstructuras getListaEstadosExpediente()
    {
        return listaEstadosExpediente;
    }

    /**
     * Asigna una lista pasada como parametro a la lista de estados de expediente.
     *
     * @param listaEstadosExpediente
     */
    public static void setListaEstadosExpediente(ListaEstructuras listaEstadosExpediente)
    {
        Estructuras.listaEstadosExpediente = listaEstadosExpediente;
    }

    /**
     * Metodo utilizado para cargar las estructuras de base de datos. El metodo muestra un progress dialog para que el
     * usuario espere mientras se carga.
     */
    public static void cargarEstructuras()
    {
       if (iniciada || cargada) return;
       iniciada=true;
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

       final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);
         progressDialog.setTitle(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.Estructuras.msgDatosIniciales"));
        progressDialog.report(I18N.get("RegistroExpedientes","Catastro.RegistroExpedientes.Estructuras.msgDatosIniciales"));
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
                                   cargarLista(listaEstadosExpediente);

                                }
                                catch(Exception e){}
                                finally
                                {
                                        progressDialog.setVisible(false);
                                        progressDialog.dispose();
                                        cargada=true;
                                }
							}
						}).start();
					}
			});
		GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);
   }

    /**
     * Metodo que carga de base de datos los dominios de la listaEstructura pasada por parametro.
     *
     * @param lista
     */
   public static void cargarLista(ListaEstructuras lista)
   {
       //Todo aqui al final ponia licencias y tenias que darle permisos al user, he puesto catastro y creo qeu rula, pero mirarlo bien
      String url= ((AppContext) AppContext.getApplicationContext()).getString("geopista.conexion.servidorurl")+"catastro";
      if (!lista.loadDB(url))
              lista.loadFile();
      lista.saveFile();
   }

    /**
     * Metodo que devuelve un booleano diciendo si las estructuras han sido cargadas o no.
     *
     * @return boolean
     */
   public static boolean isCargada()
   {
       return cargada;
   }

    /**
     * Metodo que asigna un boolean a la variable cargada.
     *
     * @param cargada
     */
   public static void setCargada(boolean cargada)
   {
       Estructuras.cargada = cargada;
   }

    /**
     * Metodo que devuelve un boolean indicando si las estructuras han sido iniciadas.
     *
     * @return boolean
     */
   public static boolean isIniciada()
   {
       return iniciada;
   }

    /**
     * Metodo que asigna un boolean a la variable iniciada.
     *
     * @param iniciada
     */
   public static void setIniciada(boolean iniciada)
   {
       Estructuras.iniciada = iniciada;
   }
}
