package com.geopista.app.licencias.estructuras;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import com.geopista.app.AppContext;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.CConstantesComando;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 01-oct-2004
 * Time: 11:20:53
 */
public class Estructuras {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Estructuras.class);

	private static boolean cargada=false;
    private static boolean iniciada=false;
    private static boolean cancelada=false;

    private static ListaEstructuras listaTiposObra=new ListaEstructuras("TIPO_OBRA");
    private static ListaEstructuras listaTiposObraMenor=new ListaEstructuras("TIPO_OBRA_MENOR");
    private static ListaEstructuras listaTiposActividad=new ListaEstructuras("TIPO_ACTIVIDAD");
    private static ListaEstructuras listaTiposAnexo=new ListaEstructuras("TIPO_ANEXO");
    private static ListaEstructuras listaViasNotificacion=new ListaEstructuras("TIPO_VIA_NOTIFICACION");
    private static ListaEstructuras listaVias= new ListaEstructuras("TIPO_VIA");
    private static ListaEstructuras listaTiposFinalizacion= new ListaEstructuras("TIPO_FINALIZACION");
    private static ListaEstructuras listaTiposTramitacion= new ListaEstructuras("Tipo de tramitación de expedientes");
    private static ListaEstructuras listaEstadosNotificacion= new ListaEstructuras("TIPO_ESTADO_NOTIFICACION");
    private static ListaEstructuras listaEstados= new ListaEstructuras("ESTADOS");
    private static ListaEstructuras listaLicencias= new ListaEstructuras("TIPO_LICENCIA");
    private static ListaEstructuras listaFormatosSalida= new ListaEstructuras("FORMATO_SALIDA");
    private static ListaEstructuras listaTiposNotificacion= new ListaEstructuras("TIPO_NOTIFICACION");
    private static ListaEstructuras listaDocumentacionObligatoriaObraMayor= new ListaEstructuras("DOCUMENTACION_OBRA_MAYOR");
    private static ListaEstructuras listaDocumentacionObligatoriaObraMenor= new ListaEstructuras("DOCUMENTACION_OBRA_MENOR");
    private static ListaEstructuras listaTiposTecnico= new ListaEstructuras("TIPO_TECNICO");
    private static ListaEstructuras listaTiposInforme= new ListaEstructuras("TIPO_INFORME");
    private static ListaEstructuras listaTiposLicenciaActividad= new ListaEstructuras("TIPO_LICENCIA_ACTIVIDAD");
    private static ListaEstructuras listaTiposViaINE= new ListaEstructuras("Tipos de via normalizados del INE");

    private static TaskMonitorDialog progressDialog1;
    
    public static ListaEstructuras getListaTiposObra() {
        return listaTiposObra;
    }

    public static ListaEstructuras getListaTiposObraMenor() {
        return listaTiposObraMenor;
    }

    public static ListaEstructuras getListaTiposActividad() {
        return listaTiposActividad;
    }

    public static ListaEstructuras getListaTiposAnexo() {
        return listaTiposAnexo;
    }
    public static ListaEstructuras getListaViasNotificacion() {
        return listaViasNotificacion;
    }

    public static ListaEstructuras getListaVias() {
        return listaVias;
    }

    public static ListaEstructuras getListaTiposFinalizacion() {
        return listaTiposFinalizacion;
    }

    public static ListaEstructuras getListaTiposTramitacion() {
        return listaTiposTramitacion;
    }

    public static ListaEstructuras getListaEstadosNotificacion() {
        return listaEstadosNotificacion;
    }

    public static ListaEstructuras getListaEstados() {
        return listaEstados;
    }

    public static ListaEstructuras getListaLicencias() {
        return listaLicencias;
    }

    public static ListaEstructuras getListaFormatosSalida() {
        return listaFormatosSalida;
    }

    public static ListaEstructuras getListaTiposNotificacion() {
        return listaTiposNotificacion;
    }

    public static ListaEstructuras getListaDocumentacionObligatoriaObraMayor() {
        return listaDocumentacionObligatoriaObraMayor;
    }

    public static ListaEstructuras getListaDocumentacionObligatoriaObraMenor() {
        return listaDocumentacionObligatoriaObraMenor;
    }

    public static ListaEstructuras getListaTiposTecnico() {
        return listaTiposTecnico;
    }


    public static ListaEstructuras getListaTiposInforme() {
        return listaTiposInforme;
    }

    public static ListaEstructuras getListaTiposViaINE() {
        return listaTiposViaINE;
    }


    public static void setListaTiposInforme(ListaEstructuras listaTiposInforme) {
        Estructuras.listaTiposInforme = listaTiposInforme;
    }

    public static ListaEstructuras getListaTiposLicenciaActividad() {
        return listaTiposLicenciaActividad;
    }

    public static void setListaTiposLicenciaActividad(ListaEstructuras listaTiposLicenciaActividad) {
        Estructuras.listaTiposLicenciaActividad = listaTiposLicenciaActividad;
    }

    /*public static void cargarEstructuras(){
        cargarEstructuras(null);
    }*/

    public static void cargarLista(ListaEstructuras lista, String url) {
    	 /*if ((progressDialog1!=null) && (progressDialog1.isCancelRequested())){
    			throw new CancelException("Cancel");										
    		}*/
 	   
        lista.addTaskMonitor(progressDialog1);

       if (url == null){
           if (!lista.loadDB(CConstantesComando.loginLicenciasUrl))
                   lista.loadFile();
       }else{
           if (!lista.loadDB(url))
                   lista.loadFile();
       }
       lista.saveFile();
    }
    
    
    public static void cargarEstructuras(){
        if (iniciada || cargada) return;
        iniciada=true;
        final AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                 .getMainFrame(), null);
        
        
         progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
         progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
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
 						            	progressDialog1=progressDialog;
 						            	//Cargamos la lista de dominios disponibles en el sistema
 						            	cargarEstructuras(null);
                                        //Thread.sleep(30000);
                                        

                                }						       
 						        catch(Exception e){
 						        	if ((progressDialog!=null) && (progressDialog.isCancelRequested())){
 						        		logger.warn("Carga de estructuras cancelada");		
 		    							//JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de estructuras cancelada");
 		    							cancelada=true;
 						        	}						        	
 						        }
                                 finally
 								    {
 								        progressDialog.setVisible(false);
                                         cargada=true;
 								    }
 								}

 							
 								}).start();
 								}
 								});
 		GUIUtil.centreOnWindow(progressDialog);
 		progressDialog.setVisible(true);

    }

    public static void cargarEstructuras(String url){
       iniciada=true;
       try {
           cargarLista(listaTiposObra, url);
           cargarLista(listaTiposObraMenor, url);
           cargarLista(listaTiposActividad, url);
           cargarLista(listaTiposAnexo, url);
           cargarLista(listaViasNotificacion, url);
           cargarLista(listaVias, url);
           cargarLista(listaTiposFinalizacion, url);
           cargarLista(listaTiposTramitacion, url);
           cargarLista(listaEstadosNotificacion, url);
           cargarLista(listaEstados, url);
           cargarLista(listaLicencias, url);
           cargarLista(listaFormatosSalida, url);
           cargarLista(listaTiposNotificacion, url);
           cargarLista(listaDocumentacionObligatoriaObraMayor, url);
           cargarLista(listaDocumentacionObligatoriaObraMenor, url);
           cargarLista(listaTiposTecnico, url);
           cargarLista(listaTiposInforme, url);
           cargarLista(listaTiposViaINE, url);
           cargarLista(listaTiposLicenciaActividad, url);
       }catch(Exception e){}
       cargada=true;
   }

    public static boolean isCargada() {
        return cargada;
    }

    public static void setCargada(boolean cargada) {
        Estructuras.cargada = cargada;
    }

    public static boolean isIniciada() {
        return iniciada;
    }

    public static void setIniciada(boolean iniciada) {
        Estructuras.iniciada = iniciada;
    }
    public static boolean isCancelada() {
        return cancelada;
    }

}

