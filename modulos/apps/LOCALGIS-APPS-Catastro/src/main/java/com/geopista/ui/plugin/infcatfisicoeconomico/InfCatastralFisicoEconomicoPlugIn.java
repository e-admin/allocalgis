package com.geopista.ui.plugin.infcatfisicoeconomico;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.feature.GeopistaFeature;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.ui.plugin.infcatfisicoeconomico.dialogs.DialogoVerInfCatastralFisicoEconomico;
import com.geopista.ui.plugin.infcatfisicoeconomico.images.IconLoader;
import com.geopista.ui.plugin.infcatfisicoeconomico.plugin.InfCastatralEnableCheckFactory;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class InfCatastralFisicoEconomicoPlugIn extends AbstractPlugIn{

	private boolean selectZoomToAreaAdded = false;
	private Hashtable permisos= new Hashtable();
	private ApplicationContext aplicacion;
	
	private JDesktopPane desktopPane = new JDesktopPane(); 
	
	public InfCatastralFisicoEconomicoPlugIn() {
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.infcatfisicoeconomico.languages.InfCatastralFisicoEconomicoPlugIni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("InfCatastralFisicoEconomicoPlugIn",bundle2);
    }
	
	public Icon getIcon() {
        return IconLoader.icon("InfCatFisicoEconomico.png");
    }
    
    public String getName(){
    	String name = I18N.get("InfCatastralFisicoEconomicoPlugIn","InfCatastralFisicoEconomicoPlugInTooltip");
    	return name;
    }
        
    public void initialize(PlugInContext context) throws Exception {    	

    	aplicacion= (AppContext)AppContext.getApplicationContext();
    	
    	//Código necesario para incluir el PlugIn en la barra de herramientas
    	// se comprueba si tiene permisos para ver la informacion catastral
    	
		GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("RegistroExpedientes");
        if(applySecurityPolicy( acl, com.geopista.security.SecurityManager.getPrincipal())){
        	context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar().addPlugIn(this.getIcon(),
                    this, this.createEnableCheck(context.getWorkbenchContext()), context.getWorkbenchContext()); 
        }
    }  
    
    public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal)
    {
		try
        {
            if ((acl == null) || (principal == null))
            {
				return false;
			}
			ConstantesRegistroExp.principal = principal;
            setPermisos(acl.getPermissions(principal));
            if (tienePermisos("Catastro.RegistroExpediente.Login"))
            {
            	
            	return true;
            }
            else{
            	return false;
            }

		}
        catch (Exception ex)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
			return false;
		}

	}
    
    /**
     * Funcion que comprueba si el usuario tiene permisos para una tarea en especial.
     */
    private boolean tienePermisos(String permiso)
    {
        if (permisos.containsKey(permiso))
        {
            return true;
        }
        return false;
    }
    /**
     * Crea hash de permisos del usuario.
     */
    private void setPermisos(Enumeration e)
    {
        permisos= new Hashtable();
        while (e.hasMoreElements())
        {
            GeopistaPermission geopistaPermission = (GeopistaPermission) e.nextElement();
            String permissionName = geopistaPermission.getName();
            if (!permisos.containsKey(permissionName))
            {
                permisos.put(permissionName, "");
            }
        }
    }
 
    public boolean execute(final PlugInContext context) throws Exception {
    	
    	//llamamos al servidor para comprobar si el usuario tiene los permisos necesarios
    	// para acceder a ver la informacion catastral
    	Boolean isPermisosConsultarInformacionCatastral = false;
    	isPermisosConsultarInformacionCatastral = ConstantesRegExp.clienteCatastro.isPermisosConsultarInformacionCatastralFisicoEconomico();
      	if(isPermisosConsultarInformacionCatastral.booleanValue()){

	    	final JFrame desktop= new JFrame();
	    	final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
	        progressDialog.setTitle("TaskMonitorDialog.Wait");
	        progressDialog.report(I18N.get("InfCatastralFisicoEconomicoPlugIn","cargando.datos.informacion.catastral"));
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
						    	JFrame desktop = new JFrame();
						    	ArrayList listIdParcelas = new ArrayList();
						    	// se obtiene la informacino de las parcelas seleccionadas
						    	Collection coll = context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();
						    	if(!coll.isEmpty()){

							    	for (Iterator iterator = coll.iterator(); iterator.hasNext();) {
										GeopistaFeature geoFeature = (GeopistaFeature) iterator.next();
										Integer idParcela = new Integer(geoFeature.getSystemId());
										listIdParcelas.add(idParcela);
	
									}
							    	String title = I18N.get("InfCatastralFisicoEconomicoPlugIn","infCatastralFisicoEconomico.panel.title");
							    	
							    	DialogoVerInfCatastralFisicoEconomico dialogo = new DialogoVerInfCatastralFisicoEconomico(desktop,true,
							    			listIdParcelas, DatosConfiguracion.CONVENIO_FISICO_ECONOMICO,title);
							 		dialogo.setLocation(100, 100/2);
							 		dialogo.setResizable(false);
							 		dialogo.setSize(1000, 700);
							 		progressDialog.setVisible(false);     
							 		dialogo.show();
						    	}
						    	else{
						    		JOptionPane.showMessageDialog(aplicacion.getMainFrame(),I18N.get("InfCatastralFisicoEconomicoPlugIn","infCatastralFisicoEconomico.seleccionar.parcela"));
						    	}
						 	
                        	}
	                        catch(Exception e)
	                        {
	                            ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
	                            return;
	                        }
	                        finally
	                        {
	                            progressDialog.setVisible(false);                                
	                            progressDialog.dispose();
	                        }
	                    }
	              }).start();
	           }
	        });
	        GUIUtil.centreOnScreen(progressDialog);
	        progressDialog.setVisible(true);
      	}
      	else{
      		 JOptionPane.showMessageDialog(aplicacion.getMainFrame(),I18N.get("InfCatastralFisicoEconomicoPlugIn","no.permisos.ver.informacion.catastral"),"ERROR",0);
      	}
        return true;
    }
    
    
	public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
		
		InfCastatralEnableCheckFactory checkFactory = new InfCastatralEnableCheckFactory(workbenchContext);
		return new MultiEnableCheck().add(checkFactory.createAtLeastConvenioFisicoEconomicoMustBeSelectedCheck());

    }


}
