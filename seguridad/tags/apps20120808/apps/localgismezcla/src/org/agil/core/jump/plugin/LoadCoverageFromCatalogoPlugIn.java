package org.agil.core.jump.plugin;

import java.util.Collection;
import java.util.Iterator;

import org.agil.core.coverage.GridCoverage;
import org.agil.core.jump.coverage.CoverageLayer;
import org.agil.core.jump.coverage.datasource.CatalogoLocalCoverageQueryChooser;

import com.geopista.editor.WorkBench;
import com.geopista.ui.GEOPISTAWorkbench;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

/**
 *  PlugIn que permite crear Layers del tipo CoverageLayer. Para ello, añade
 *  un dialogo que permite seleccionar una lista de Coverages disponibles
 *
 *@author    alvaro zabala 29-sep-2003
 */
public class LoadCoverageFromCatalogoPlugIn extends ThreadedBasePlugIn {
	/**
	 *  De momento solo se carga un catalogo de raster, y es éste. Cuando
	 *  tengamos un selector de catalogos, esta propiedad se recibirá desde el
	 *  componente selector de catalogos
	 */
	private final String PATH_CATALOGO = "gridcoverages-client.xml";


	/**
	 *  Este metodo se ejecuta normalmente dentro del thread de la GUI. (Es
	 *  decir, como consecuencia de que el usuario pulse un boton, o algo así).
	 *  En este caso, lo que hará es mostrar un dialogo del que se podrán elegir
	 *  orígenes de datos para cargar.
	 *
	 *@param  context        contexto del plugin
	 *@return                booleano que indica si se pulso aceptar o cancelar
	 *@exception  Exception  Description of Exception
	 */
	public boolean execute(PlugInContext context) throws Exception {
		CatalogoLocalCoverageQueryChooser queryChooser = getQueryChooser(context);
		GUIUtil.centreOnWindow(queryChooser.getComponent());
		queryChooser.getComponent().setVisible(true);
		return queryChooser.wasOkPressed();
		//solo si se devuelve true se lanza el thread
	}


	/**
	 *  Todos los plugines que extiendan de ThreadedBase realizan su proceso en
	 *  este metodo, que se ejecuta en un thread dedicado.
	 *
	 *@param  monitor        supervisa las tareas
	 *@param  context        contexto de ejecucion del plugin (como parte de
	 *      JUMP)
	 *@exception  Exception  Description of Exception
	 *@see                   com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn#run(com.vividsolutions.jump.task.TaskMonitor,
	 *      com.vividsolutions.jump.workbench.plugin.PlugInContext)
	 */
	public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
		Collection collection = getQueryChooser(context).getDataSourceQueries();
		Assert.isTrue(!collection.isEmpty());
		for (Iterator i = collection.iterator(); i.hasNext(); ) {
			GridCoverage coverage = (GridCoverage) i.next();
			//TODO de momento no consideramos mas que GridCoverages
			CoverageLayer layer = new CoverageLayer(coverage,
					"Capa-" + coverage.getIdentificador() + "-",
					context.getLayerManager());
			context.getLayerManager().addLayerable(chooseCategory(context), layer);
		}
		//for

	}


	/**
	 *@param  context
	 *@return          The QueryChooser value
	 */
	private CatalogoLocalCoverageQueryChooser getQueryChooser(PlugInContext context) {
		String KEY = getClass().getName() + " - DIALOG";
		CatalogoLocalCoverageQueryChooser queryChooser =
				(CatalogoLocalCoverageQueryChooser) context.getWorkbenchContext().
				getWorkbench().getBlackboard().get(KEY);
		if (queryChooser == null) {
			WorkBench workbench = context.getWorkbenchContext().getWorkbench();
			//obtenemos el directorio donde se guardan los catalogos de datos
			String directoryCatalog = (String) workbench.getBlackboard().
					get(GEOPISTAWorkbench.CATALOGS_DIRECTORY_OPTION);
			queryChooser = new CatalogoLocalCoverageQueryChooser(directoryCatalog + "/" + PATH_CATALOGO,
					GeopistaUtil.getFrame(context.getWorkbenchGuiComponent()));
			context.getWorkbenchContext().getWorkbench().getBlackboard().put(KEY, queryChooser);
		}
		//if
		return queryChooser;
	}


	/**
	 *  TODO ESTE METHODO HAY QUE HACERLO PUBLICO Y METERLO EN UTIL QUITAR ESTA
	 *  GUARRADA
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 */
	private String chooseCategory(PlugInContext context) {
		return context.getLayerNamePanel().getSelectedCategories().isEmpty()
				? StandardCategoryNames.WORKING
				: context.getLayerNamePanel().getSelectedCategories().iterator().next()
				.toString();
	}


	/**
	 *  Devuelve un chequeo que se debe comprobar antes de ejecutar un PlugIn
	 *  TODO Revisar como funcionan los MultiEnable y los PlugIns.
	 *
	 *@param  workbenchContext
	 *@return
	 */
	public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
		return new MultiEnableCheck().add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck());
	}

}
