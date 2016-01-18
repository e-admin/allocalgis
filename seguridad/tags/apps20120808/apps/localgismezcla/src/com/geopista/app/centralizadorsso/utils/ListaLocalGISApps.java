package com.geopista.app.centralizadorsso.utils;

import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import com.geopista.app.AppContext;
import com.geopista.app.centralizadorsso.beans.LocalGISApp;
import com.vividsolutions.jump.I18N;

/**
*
* @author  dcaaveiro
*/
public class ListaLocalGISApps {
	Hashtable<String,LocalGISApp> lista = new Hashtable<String,LocalGISApp>();

	public ListaLocalGISApps(ResourceBundle bundle) {
    	String serverHost = AppContext.getApplicationContext().getString(AppContext.URL_TOMCAT);           	

		I18N.plugInsResourceBundle.put("centralizadorsso", bundle);
		
    	//Aplicaciones Escritorio
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.editor"), new LocalGISApp("Administracion", "Geopista.Administracion.View", LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.informacionReferencia"), new LocalGISApp("Administracion", "Geopista.Administracion.View", LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_informacion_de_referencia.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.planeamiento"), new LocalGISApp("Administracion", "Geopista.Administracion.View", LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_planeamiento.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.infraestructuras"), new LocalGISApp("Administracion", "Geopista.Administracion.View", LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_infraestructuras.jnlp"));       
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.administracionUsuarios"), new LocalGISApp("Administracion", "Geopista.Administracion.View", LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_administrador.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.gestorMedioambiental"), new LocalGISApp("Administracion","Geopista.Administracion.View",LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_contaminantes.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.licenciasObra"), new LocalGISApp("Licencias de Obra","Geopista.Licencias.View",LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_licencias.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.licenciasActividad"), new LocalGISApp("Licencias de Obra","Geopista.Licencias.View",LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_licencias_actividad.jnlp"));       
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.metadatos"), new LocalGISApp("Metadatos","Geopista.Metadatos.View",LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_metadatos.jnlp"));       
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.ocupaciones"), new LocalGISApp("Ocupaciones","Geopista.Ocupaciones.View",LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_ocupaciones.jnlp"));       
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.catastro"), new LocalGISApp("ALP","LocalGis.ALP.Login",LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_catastro.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.gestorCapas"), new LocalGISApp("Administracion", "Geopista.Administracion.View", LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_gestor_capas.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.alp"), new LocalGISApp("ALP", "LocalGis.ALP.Login", LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_alp.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.backup"), new LocalGISApp("Administracion", "Geopista.Administracion.View", LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_backup.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.generadorInformes"), new LocalGISApp("Administracion", "Geopista.Administracion.View", LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_informes.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.inventarioPatrimonio"), new LocalGISApp("Inventario","Geopista.Inventario.Login",LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_inventario.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.espacioPublico"), new LocalGISApp("Gestion de Espacio Publico", "localgis.espaciopublico.login", LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_espaciopublico.jnlp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.eiel"), new LocalGISApp("EIEL", "LocalGis.EIEL.Login", LocalGISApp.TipoApp.DESKTOP,serverHost + "/software/localgis_eiel.jnlp"));
        
        
        //Aplicaciones Web
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.wmsManager"), new LocalGISApp("Visualizador","Geopista.Visualizador.Login",LocalGISApp.TipoApp.WEB,serverHost + "/localgis-wmsmanager/admin/index.jsp"));
        setLocalGISApp(I18N.get("centralizadorsso","CMainCentralizadorSSO.localGISApps.guiaUrbanaPrivada"), new LocalGISApp("Visualizador","Geopista.Visualizador.Login",LocalGISApp.TipoApp.WEB,serverHost + "/localgis-guiaurbana/private/"));

    }

    public Hashtable getLista() {
        return lista;
    }

    public void setLista(Hashtable lista) {
        this.lista = lista;
    }

    public void setLocalGISApp(String nombreApp, LocalGISApp localGISApp)
    {
        lista.put(nombreApp,localGISApp);
    } 
    
    public LocalGISApp getLocalGISApp(String nombreApp)
    {
        return (LocalGISApp)lista.get(nombreApp);
    }
   
}
