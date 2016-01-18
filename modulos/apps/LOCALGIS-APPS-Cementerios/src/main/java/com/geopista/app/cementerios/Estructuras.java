/**
 * Estructuras.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

import com.geopista.app.AppContext;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.geopista.app.UserPreferenceConstants;

/**
 * 
 * class Estructuras 
 *
 */
public class Estructuras {
    private static boolean cargada=false;
    private static boolean iniciada=false;
    private static ListaEstructuras listaClasificacionGestionCementerios= new ListaEstructuras("Gestión de Cementerios");
//    private static ListaEstructuras listaClasificacionGestionIntervenciones= new ListaEstructuras("Tipo Intervenciones");
    
    private static ListaEstructuras listaSubtipogestionCementerios= new ListaEstructuras("Tipo de Elementos");
    private static ListaEstructuras listaSubtipogestionPropiedad= new ListaEstructuras("Tipo G.Propiedad");
    
    private static ListaEstructuras listaSubtipogestionServicios= new ListaEstructuras("Tipo Servicios");
    private static ListaEstructuras listaSubtipogestionDifuntos= new ListaEstructuras("Tipo Gestion Difuntos");
    private static ListaEstructuras listaSubtipogestionIntervenciones= new ListaEstructuras("Tipo Intervenciones");
    
    private static ListaEstructuras listaComboTipoUnidades= new ListaEstructuras("Tipo Unidades");
    private static ListaEstructuras listaComboTipoConcesiones= new ListaEstructuras("Tipo Concesiones");
    private static ListaEstructuras listaComboTipoContenedores= new ListaEstructuras("Tipo contenedores difunto");
    private static ListaEstructuras listaComboTipoInhumaciones= new ListaEstructuras("Tipo Inhumacion");
    private static ListaEstructuras listaComboTipoExhumaciones= new ListaEstructuras("Tipo Exhumación");
    
    private static ListaEstructuras listaComboServicios= new ListaEstructuras("Tipo Gestion Difuntos");

	private static ListaEstructuras listaDocumentos= new ListaEstructuras("Tipo Documento");
    private static ListaEstructuras listaFormatoInforme= new ListaEstructuras("Formato Informe");
    

	public static Vector getListaTiposSorted(String locale){
    	Vector listaTiposSorted = new Vector();
    	listaTiposSorted.addAll(listaClasificacionGestionCementerios.getListaSorted(locale));
		return listaTiposSorted;
    }

	public static Vector getListaSubtiposSorted(String locale){
    	Vector listaSubTiposSorted = new Vector();

    	listaSubTiposSorted.addAll(listaSubtipogestionCementerios.getListaSorted(locale));
    	listaSubTiposSorted.addAll(listaSubtipogestionPropiedad.getListaSorted(locale));
    	listaSubTiposSorted.addAll(listaSubtipogestionServicios.getListaSorted(locale));
    	listaSubTiposSorted.addAll(listaSubtipogestionDifuntos.getListaSorted(locale));
    	listaSubTiposSorted.addAll(listaSubtipogestionIntervenciones.getListaSorted(locale));
    	
//       	listaSubTiposSorted.addAll(listaComboTipoUnidades.getListaSorted(locale));
		return listaSubTiposSorted;
    }

    
    /**
     * getListaCombosSorted
     * @param locale
     * @return la lista de los diferentes valores posibles para los combos.
     */
    public static Vector getListaCombosSorted(String locale){
    	Vector listaCombosSorted = new Vector();
    	listaCombosSorted.addAll(listaComboTipoUnidades.getListaSorted(locale));
    	
    	return listaCombosSorted;
    }

    public static Vector getListaCombosBloqueSorted(String locale){
    	Vector listaCombosSorted = new Vector();
    	listaCombosSorted.addAll(getListaCombosBloque(locale).getListaSorted(locale));
    	
    	return listaCombosSorted;
    }
    
    public static Vector getListaCombosServiciosSorted(String locale){
    	Vector listaCombosSorted = new Vector();
    	listaCombosSorted.addAll(getListaCombosServicios(locale).getListaSorted(locale));
    	
    	return listaCombosSorted;
    }

    public static Vector getListaCombosTipoInhumacionesSorted(String locale){
    	Vector listaCombosSorted = new Vector();
    	listaCombosSorted.addAll(listaComboTipoInhumaciones.getListaSorted(locale));
    	return listaCombosSorted;
    }
    
    public static Vector getListaCombosTipoExhumacionesSorted(String locale){
    	Vector listaCombosSorted = new Vector();
    	listaCombosSorted.addAll(listaComboTipoExhumaciones.getListaSorted(locale));
    	return listaCombosSorted;
    }

    public static Vector getListaComboConcesiones(String locale){
    	Vector listaComboConcesiones = new Vector();
    	listaComboConcesiones.addAll(listaComboTipoConcesiones.getListaSorted(locale));
    	return listaComboConcesiones;
    }

    public static Vector getListaComboContenedores(String locale){
    	Vector listaComboContenedores = new Vector();
    	listaComboContenedores.addAll(listaComboTipoContenedores.getListaSorted(locale));
    	return listaComboContenedores;
    }

    public static Vector getListaComboServicios(String locale){
    	Vector listaComboServicios = new Vector();
    	listaComboServicios.addAll(listaSubtipogestionServicios.getListaSorted(locale));
    	return listaComboServicios;
    }
    
    public static Vector getListaComboGestionDifuntos(String locale){
    	Vector listaComboServicios = new Vector();
    	listaComboServicios.addAll(listaSubtipogestionDifuntos.getListaSorted(locale));
    	return listaComboServicios;
    }
    
    public static ListaEstructuras getListaCombosBloque(String locale){
    	ListaEstructuras listaCombos = new ListaEstructuras();
    	ListaEstructuras listaEstructura = getListaComboTipoUnidades();
    	DomainNode node = (DomainNode) listaEstructura.getDomainNode("4");
    		if (node!= null)listaCombos.add(node);
    	node = (DomainNode)listaEstructura.getDomainNode("5");
    		if (node!= null)listaCombos.add(node);
        return listaCombos;
    }

    public static ListaEstructuras getListaCombosServicios(String locale){
    	ListaEstructuras listaCombos = new ListaEstructuras();
    	ListaEstructuras listaEstructura = getListaComboServicios();
    	DomainNode node = (DomainNode) listaEstructura.getDomainNode("GD-2");
    		if (node!= null)listaCombos.add(node);
    	node = (DomainNode)listaEstructura.getDomainNode("GD-3");
    		if (node!= null)listaCombos.add(node);
        return listaCombos;
    }
    
	public static ListaEstructuras getListaSubtipogestionDifuntos() {
		return listaSubtipogestionDifuntos;
	}
	public static void setListaSubtipogestionDifuntos(
			ListaEstructuras listaSubtipogestionDifuntos) {
		Estructuras.listaSubtipogestionDifuntos = listaSubtipogestionDifuntos;
	}

	public static ListaEstructuras getListaSubtipogestionServicios() {
		return listaSubtipogestionServicios;
	}

	public static void setListaSubtipogestionServicios(
			ListaEstructuras listaSubtipogestionServicios) {
		Estructuras.listaSubtipogestionServicios = listaSubtipogestionServicios;
	}

	public static ListaEstructuras getListaSubtipogestionCementerios() {
		return listaSubtipogestionCementerios;
	}
	public static void setListaSubtipogestionCementerios(
			ListaEstructuras listaSubtipogestionCementerios) {
		Estructuras.listaSubtipogestionCementerios = listaSubtipogestionCementerios;
	}
	public static ListaEstructuras getListaClasificacionGestionCementerios() {
		return listaClasificacionGestionCementerios;
	}
	public static void setListaClasificacionGestionCementerios(
			ListaEstructuras listaClasificacionGestionCementerios) {
		Estructuras.listaClasificacionGestionCementerios = listaClasificacionGestionCementerios;
	}
	
	public static ListaEstructuras getListaSubtipogestionPropiedad() {
		return listaSubtipogestionPropiedad;
	}
	public static void setListaSubtipogestionPropiedad(
			ListaEstructuras listaSubtipogestionPropiedad) {
		Estructuras.listaSubtipogestionPropiedad = listaSubtipogestionPropiedad;
	}
	
	
	public static ListaEstructuras getListaComboTipoUnidades() {
		return listaComboTipoUnidades;
	}

	public static void setListaComboTipoUnidades(
			ListaEstructuras listaComboTipoUnidades) {
		Estructuras.listaComboTipoUnidades = listaComboTipoUnidades;
	}
	
	public static ListaEstructuras getListaComboTipoConcesiones() {
		return listaComboTipoConcesiones;
	}


	public static void setListaComboTipoConcesiones(
			ListaEstructuras listaComboTipoConcesiones) {
		Estructuras.listaComboTipoConcesiones = listaComboTipoConcesiones;
	}


    public static ListaEstructuras getListaComboTipoContenedores() {
		return listaComboTipoContenedores;
	}


	public static void setListaComboTipoContenedores(
			ListaEstructuras listaComboTipoContenedores) {
		Estructuras.listaComboTipoContenedores = listaComboTipoContenedores;
	}
	
	public static ListaEstructuras getListaDocumentos() {
		return listaDocumentos;
	}

	public static void setListaDocumentos(ListaEstructuras listaDocumentos) {
		Estructuras.listaDocumentos = listaDocumentos;
	}

	public static ListaEstructuras getListaFormatoInforme() {
		return listaFormatoInforme;
	}

	public static void setListaFormatoInforme(ListaEstructuras listaFormatoInforme) {
		Estructuras.listaFormatoInforme = listaFormatoInforme;
	}

    public static ListaEstructuras getListaComboTipoInhumaciones() {
		return listaComboTipoInhumaciones;
	}

	public static void setListaComboTipoInhumaciones(
			ListaEstructuras listaComboTipoInhumaciones) {
		Estructuras.listaComboTipoInhumaciones = listaComboTipoInhumaciones;
	}
	
	public static void setListaComboTipoExhumaciones(
			ListaEstructuras listaComboTipoExhumaciones) {
		Estructuras.listaComboTipoExhumaciones = listaComboTipoExhumaciones;
	}
	
	public static ListaEstructuras getListaComboServicios() {
		return listaComboServicios;
	}

	public static void setListaComboServicios(ListaEstructuras listaComboServicios) {
		Estructuras.listaComboServicios = listaComboServicios;
	}
	
	public static ListaEstructuras getListaComboTipoExhumaciones() {
		return listaComboTipoExhumaciones;
	}

	public static void cargarEstructuras(){
       if (iniciada || cargada) return;
       iniciada=true;
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

       final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);
         progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
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
                                       cargarLista(listaClasificacionGestionCementerios);
                                       cargarLista(listaSubtipogestionCementerios);
                                       cargarLista(listaSubtipogestionPropiedad);
                                       cargarLista(listaSubtipogestionServicios);
                                       cargarLista(listaSubtipogestionDifuntos);
                                       cargarLista(listaSubtipogestionIntervenciones);
                                       
                                       cargarLista(listaComboTipoUnidades);
                                       cargarLista(listaComboTipoConcesiones);
                                       cargarLista(listaComboTipoContenedores);
                                       cargarLista(listaComboTipoInhumaciones);
                                       cargarLista(listaComboTipoExhumaciones);
                                       cargarLista(listaComboServicios);
                                       
                                       cargarLista(listaDocumentos);
                                       cargarLista(listaFormatoInforme);
                               }catch(Exception e){}
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
   public static void cargarLista(ListaEstructuras lista){
      String url= ((AppContext) AppContext.getApplicationContext()).getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+WebAppConstants.CEMENTERIOS_WEBAPP_NAME;
      if (!lista.loadDBCem(url))
              lista.loadFile();
      lista.saveFile();
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


}
