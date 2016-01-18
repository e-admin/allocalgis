package net.surveyos.sourceforge.openjump.plugins.gpx;

import java.awt.Color;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import org.geotools.gpx2.io.GpxFileFilter;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;

import net.surveyos.sourceforge.utilities.exceptions.ShouldNeverReachHereException;

/**
 * The main plug-in class for the SurveyOS GPX Plug-In for OpenJUMP.
 * This plug-in provides the ability to import tracks and waypoints
 * from a GPX file into OpenJUMP as simple features. Waypoints are imported
 * as Point features. Tracks are imported as LineString features.
 */
public class GpxReaderPlugIn extends AbstractPlugIn 
//implements PlugIn
{

	private File targetFile;
	private PlugInContext plugInContext;
	private boolean userSelectedFile;
	private String waypointsLayerName = "Waypoints From GPX";
	private String tracksLayerName = "Tracks From GPX";
	
	private String toolBarCategory = "GeopistaLoadMapPlugIn.category";
	PlugInContext localContext;
	
	@Override
	public boolean execute(PlugInContext argPlugInContext) throws Exception 
	{
		this.plugInContext = argPlugInContext;
		JFileChooser chooser = new JFileChooser();
		GpxFileFilter filter = new GpxFileFilter(true, true);
		
		chooser.setFileFilter(filter);
		
		this.showGui(chooser);
		
		if(this.userSelectedFile == true)
		{
			GpxFileDataSource dataSource = new 
			GpxFileDataSource(this.targetFile);
			
			Connection connection = dataSource.getConnection();
			GpxFileConnection gpxConnection = (GpxFileConnection) connection; 
			
			boolean hasWaypoints = false;
			boolean hasTracks = false;
			
			FeatureCollection waypoints = null;
			FeatureCollection tracks = null;
			
			if(gpxConnection.targetFileHasWaypoints() == true)
			{
				hasWaypoints = true;
				waypoints = connection
				.executeQuery("Waypoints", null);
			}
			
			if(gpxConnection.targetFileHasTracks() == true)
			{
				hasTracks = true;
				tracks = connection
				.executeQuery("Tracks", null);
			}
			
			if(hasWaypoints == false)
			{
				if(hasTracks == false)
				{
					WorkbenchFrame frame = 
						this.plugInContext.getWorkbenchFrame();
					
					frame.warnUser("No waypoints or tracks were contained " +
							"in the selected file.");
					
					return false;
				}
			}
			
			// Create color for new layers.
			Color blue = Color.BLUE;
			
			// Get LayerManager to which the Layer objects will be added.
			LayerManager manager = this.plugInContext.getLayerManager();
			
			if(hasWaypoints == true)
			{
				GeopistaLayer waypointsLayer = new GeopistaLayer(this.waypointsLayerName,
					blue, waypoints, manager);
				manager.addLayer("Working", waypointsLayer);
			}
			
			if(hasTracks == true)
			{
				GeopistaLayer tracksLayer = new GeopistaLayer(this.tracksLayerName,
					blue, tracks, manager);
				manager.addLayer("Working", tracksLayer);
			}
		}
		
		return true;
	}

	@Override
	public String getName() 
	{
		return "GPX Reader";
	}

	/**
	 * Not implemented.
	 */
	@Override
	public void initialize(PlugInContext arg0) throws Exception 
	{
		// Not implemented.		
		ApplicationContext appContext=AppContext.getApplicationContext();
		PlugInContext localContext;
		
		
		arg0.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(appContext.getString("CalculateFeature.category")).addPlugIn(
				getIcon(), this,
				createEnableCheck(arg0.getWorkbenchContext()),
				arg0.getWorkbenchContext());
		

		
		
	}

	public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

//		return new MultiEnableCheck()
//		.add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
//		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
//		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
//		.add(checkFactory.createAtLeastNLayersMustExistCheck(2));

		return new MultiEnableCheck();

	}
	public ImageIcon getIcon() {
		return IconLoader.icon("importer.gif");
	}
	
	public void showGui(JComponent argComponentToShow) 
	{
		if(argComponentToShow instanceof JFileChooser == true)
		{
			JFileChooser chooser = (JFileChooser) argComponentToShow;
			WorkbenchFrame frame = this.plugInContext.getWorkbenchFrame();
			
			int returnValue = chooser.showOpenDialog(frame);
			
			if(returnValue == JFileChooser.APPROVE_OPTION)
			{
				this.targetFile = chooser.getSelectedFile();
				this.userSelectedFile = true;
			}
			
			else if(returnValue == JFileChooser.CANCEL_OPTION)
			{
				frame.warnUser("You did not select a GPX file to import.");
				this.userSelectedFile = false;
			}
			
			else if(returnValue == JFileChooser.ERROR_OPTION)
			{
				frame.warnUser("There was an error selecting the GPX file to " +
						"import.");
				this.userSelectedFile = false;
			}
			
			else
			{
				ShouldNeverReachHereException toThrow = new 
				ShouldNeverReachHereException("Please report this bug to the " +
						"OpenJUMP programmers.");
				
				throw toThrow;
			}
		}
	}
}
