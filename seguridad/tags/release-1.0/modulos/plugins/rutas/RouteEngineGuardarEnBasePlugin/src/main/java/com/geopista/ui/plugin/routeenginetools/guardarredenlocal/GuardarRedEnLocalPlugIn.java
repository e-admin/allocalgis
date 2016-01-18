package com.geopista.ui.plugin.routeenginetools.guardarredenlocal;

import java.awt.Component;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.uva.geotools.graph.structure.Graph;
import org.uva.route.network.NetworkManager;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.plugin.routeenginetools.guardarredenlocal.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToSaveInDataStore;
import com.geopista.ui.plugin.routeenginetools.routeutil.VentanaError;
import com.localgis.route.graph.io.LocalRouteReaderWriter;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelDialog;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.plugin.datastore.WithOutConnectionPanel;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class GuardarRedEnLocalPlugIn extends ThreadedBasePlugIn {

	private boolean guardarRedLocalButtonAdded = false;
	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	private OKCancelDialog dialog;

	public static TaskMonitor monitor = null;

	public boolean execute(final PlugInContext context) {

		OKCancelDialog dlg = getDialog(context);
		dlg.setVisible(true);
		
		return dlg.wasOKPressed();
	}

	public void initialize(PlugInContext context) {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.guardarredenlocal.language.RouteEngine_GuardarRedEnLocali18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("guardarredbase",bundle);

		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(
				this.aplicacion.getI18nString("ui.MenuNames.TOOLS.ROUTEENGINEADMINISTRATION")).addPlugIn(
				this.getIcon(),
				this,
				createEnableCheck(context.getWorkbenchContext()),
				context.getWorkbenchContext());


	}

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		MiEnableCheckFactory checkFactory = new MiEnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
		.add(
				checkFactory
				.createWindowWithLayerManagerMustBeActiveCheck())
				.add(
						checkFactory
						.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
						.add(checkFactory.createTaskWindowMustBeActiveCheck()).add(
								checkFactory.createBlackBoardMustBeElementsCheck());
	}

	protected boolean guardarGrafoenBase(PanelToSaveInDataStore panel,
			TaskMonitor monitor, PlugInContext context) throws Throwable {
		monitor.report("Guardando subred en Base de Datos");
		this.monitor = monitor;

		return guardarenLocal(monitor, panel, context);

	}

	protected boolean leeroguardarGrafoenBase(WithOutConnectionPanel panel,
			TaskMonitor monitor, PlugInContext context) throws Throwable {

		return guardarGrafoenBase((PanelToSaveInDataStore) panel, monitor, context);

	}

	public boolean guardarenLocal(TaskMonitor monitor,
			PanelToSaveInDataStore panel,
			PlugInContext context) {
		monitor.report("Guardando Subred en Base de Datos");
		
		boolean guardado = false;
		boolean existeNetworkDirectory = false;
		
		try{
			
			String base =  AppContext.getApplicationContext().getString("ruta.base.mapas");
			File dir = new File(base,"networks");
			if(! dir.exists() ){
				String folderPath = dir.getPath();
				File networkDir = new File(folderPath,panel.getNombreenBase());
				if(! networkDir.exists() ){
					existeNetworkDirectory = true;
				}
			}

			if (! existeNetworkDirectory ) {
				// si no hay ninguna subred con ese nombre
				guardado = guardarRedEnLocal(panel.getNombreenBase(), panel, context);
			} else {
				// hay subredes con el mismo nombre.
				// se muestra dialogo con el error y opcion de cambiarlo o sobreescribirlo
				guardado = cambiarSobreescribirRedEnLocal(panel, context, guardado);
			}
			
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return guardado;

	}

	private static String textFieldNuevaRedName = "Nuevared"; 

	private boolean cambiarSobreescribirRedEnLocal(PanelToSaveInDataStore panel,
			PlugInContext context,
			boolean guardado) throws Exception {
		VentanaError ventanaerror = createInitializaErrorWindow(context, panel);

		boolean canceled = false;
		while (!canceled && !guardado){
			ventanaerror.mostrar();
			if (ventanaerror.OK()){
				// si se da Ok en la ventana de error: SE DESEA MODIFICAR EL NOMBRE;
				if (panel.getNombreenBase().equals(ventanaerror.getTextField(textFieldNuevaRedName))){
					// se ha introducido el mismo nombre que hay en la base de datos.
					// se pide confirmacion para sobreescribir.
					if (confirmarSobreescribirNombreRed()){
						// se confirma y se sobreescribe.
						guardado = sobreEscribirRedEnLocal(ventanaerror.getTextField(textFieldNuevaRedName),panel, context);
					}
				} else{
					// se guarda el nuevo nombre de la red.
					guardado = guardarRedEnLocal(ventanaerror.getTextField(textFieldNuevaRedName), panel, context);
				}
			} else{
				canceled = true;
			}
		}
		return guardado;
	}

	public VentanaError createInitializaErrorWindow(PlugInContext context, PanelToSaveInDataStore panel){

		VentanaError createdWindowError = new VentanaError(context);
		createdWindowError.addText(I18N.get("guardarredbase","routeengine.guardarred.errormessage.existsubred1"));
		createdWindowError.addText(I18N.get("guardarredbase","routeengine.guardarred.errormessage.existsubred2"));

		createdWindowError.addTextField(textFieldNuevaRedName, panel.getNombreenBase(), 20, null, "Nuevo nombre para la red:");

		return createdWindowError;
	}

	private boolean sobreEscribirRedEnLocal(String nuevoNombreParaRed, PanelToSaveInDataStore panel,
			PlugInContext context) {
		// TODO Auto-generated method stub
		boolean resultado = false;
		try{
			
			NetworkManager networkMgr = FuncionesAuxiliares
			.getNetworkManager(context);
			String red = panel.getRedSeleccionada();
			String subred = panel.getSubredseleccionada();
			Graph graph = null;
			if (red != null && !red.equals("")){
				if (subred != null && !subred.equals("")){
					graph = networkMgr.getNetwork(red).getSubNetwork(subred).getGraph();
				} else{
					graph = networkMgr.getNetwork(red).getGraph();
				}
			}

			resultado = writeGraph(panel, resultado, graph, context);
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return resultado;

	}

	/**
	 * @param panel
	 * @param resultado
	 * @param graph
	 * @return
	 */
	private boolean writeGraph(PanelToSaveInDataStore panel, boolean resultado,
			Graph graph, PlugInContext context) {

		LocalRouteReaderWriter db;
		try {

			String base =  AppContext.getApplicationContext().getString("ruta.base.mapas");
			File dir = new File(base,"networks");
			if(! dir.exists() ){
				dir.mkdirs();
			}
			
			String folderPath = dir.getPath();
			File networkDir = new File(folderPath,panel.getNombreenBase());
			if(! networkDir.exists() ){
				networkDir.mkdirs();
			}
			
			File networkFile = new File(networkDir.getPath(),panel.getNombreenBase());
			
			db = new LocalRouteReaderWriter(networkFile.getPath());
//			RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
//			LocalGISStreetRouteReaderWriter streetDb = new LocalGISStreetRouteReaderWriter(routeConnection);

//			streetDb.setNetworkName(panel.getNombreenBase());

			if (graph != null) {
				db.write(graph);

				db.writeNetworkProperties(FuncionesAuxiliares.getNetworkManager(context).getNetwork(
						panel.getRedSeleccionada()).getProperties());
			}			

			resultado= true;

		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultado;
	}

	private boolean guardarRedEnLocal(String nombreRed, PanelToSaveInDataStore panel,
			PlugInContext context) throws Exception {

		boolean resultado = false;
		try{
			
			NetworkManager networkMgr = FuncionesAuxiliares
			.getNetworkManager(context);
			String red = panel.getRedSeleccionada();
			String subred = panel.getSubredseleccionada();
			Graph graph = null;
			if (red != null && !red.equals("")){
				if (subred != null && !subred.equals("")){
					graph = networkMgr.getNetwork(red).getSubNetwork(subred).getGraph();
				} else{
					graph = networkMgr.getNetwork(red).getGraph();
				}
			}

			writeGraph(panel, resultado, graph, context);

		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return resultado;
	}

	public boolean confirmarSobreescribirNombreRed(){
		// TODO Auto-generated method stub

		// mensaje del dialogo.
		String mensaje = I18N.get("guardarredbase","routeengine.guardarred.overwritemessage.title");
		String titulo =  I18N.get("guardarredbase","routeengine.guardarred.overwritemessage.text");

		int seleccion = JOptionPane.showOptionDialog(
				AppContext.getApplicationContext().getMainFrame(),
				mensaje, 
				titulo,
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,    // null para icono por defecto.
				new Object[] { "Aceptar", "cancelar"},   // null para YES, NO y CANCEL
		"cancelar");

		if (seleccion == 0)
			return true;

		return false;
	}
	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("guardarredbase","routeengine.guardarred.iconfile"));
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!guardarRedLocalButtonAdded)
		{
			toolbox.addToolBar();
			GuardarRedEnLocalPlugIn explode = new GuardarRedEnLocalPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			guardarRedLocalButtonAdded = true;
		}
	}

	@Override
	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {
		// TODO Auto-generated method stub
		try {
			leeroguardarGrafoenBase((WithOutConnectionPanel) dialog
					.getCustomComponent(), monitor, context);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	private OKCancelDialog createDialog(PlugInContext context) {

		WithOutConnectionPanel conPannel = createPanel(context);				
		OKCancelDialog dialog = new OKCancelDialog(context.getWorkbenchFrame(),
				getName(), 
				true, 
				conPannel,
				new OKCancelDialog.Validator() {
					public String validateInput(Component component) {
						return ((WithOutConnectionPanel) component).validateInput();
					}
				});
		dialog.pack();
		GUIUtil.centreOnWindow(dialog);
		
		return dialog;
	}
	
	
	private OKCancelDialog getDialog(PlugInContext context) {
		dialog = createDialog(context);
		return dialog;
	}
	
	protected PanelToSaveInDataStore createPanel(PlugInContext context) {
		return new PanelToSaveInDataStore(context);
	}

}
