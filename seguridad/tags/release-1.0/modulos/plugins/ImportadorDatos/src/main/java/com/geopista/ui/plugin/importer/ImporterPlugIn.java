package com.geopista.ui.plugin.importer;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.components.FeatureExpressionPanel;
import com.geopista.ui.plugin.importer.beans.ConstantesImporter;
import com.geopista.ui.plugin.importer.images.IconLoader;
import com.geopista.ui.plugin.importer.panels.ImporterEditorPanel;
import com.geopista.ui.plugin.importer.panels.SelectLayerPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;


public class ImporterPlugIn extends AbstractPlugIn 
{

	ApplicationContext appContext=AppContext.getApplicationContext();
	PlugInContext localContext;
	private SelectLayerPanel selectLayerPanel = null;
	private SelectLayerPanel selectLocalLayerPanel = null;
	private static final Log logger = LogFactory.getLog(ImporterPlugIn.class);
	public static final String SELECTEDTARGETLAYER = ImporterPlugIn.class.getName()+"_targetLayer";
	public static final String SELECTEDSOURCELAYER = ImporterPlugIn.class.getName()+"_sourceLayer";

	private static final String EXPRESION_FORMULA = ImporterPlugIn.class.getName()+"_expresionFormula";;
	private static final String ATTRIBUTE_ID_ON_TARGET = ImporterPlugIn.class.getName()+"ATTRIBUTE_ID_ON_TARGET"; 

	public static Blackboard bk=new Blackboard();// for testing only


	private JTable reportTable = null;
	private FeatureExpressionPanel featureExpressionPanel;

	private PropertyChangeSupport eventPropertyAdapter=new PropertyChangeSupport(this);

	private JLabel capasOrigenJLabel;
	private JLabel capasDestinoJLabel;
	private ImporterEditorPanel importerPanel;
	private JDialog d;


	public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		.add(checkFactory.createAtLeastNLayersMustExistCheck(2));

	}

	public void initialize(PlugInContext context) throws Exception
	{	
		configureApp();
		this.localContext = context;
		Locale loc=Locale.getDefault();      	 
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.importer.languages.ImporterPlugIni18n",
				loc,this.getClass().getClassLoader());    	
		I18N.plugInsResourceBundle.put("ImporterPlugIn",bundle);

		
		capasDestinoJLabel = new JLabel(I18N.get("ImporterPlugIn","ImporterPlugIn.capadestino.nombre"));
		capasOrigenJLabel = new JLabel(I18N.get("ImporterPlugIn","ImporterPlugIn.capaorigen.nombre"));

		
		context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(appContext.getString("CalculateFeature.category")).addPlugIn(
				getIcon(), this,
				createEnableCheck(context.getWorkbenchContext()),
				context.getWorkbenchContext());

	}
	/**
	 * execute (PlugInContext context)
	 * 
	 * @param context : Contexto del PlugIn
	 * @return Devuelve false si no se ha podido ejecutar la operación, true en caso contrario
	 */
	public boolean execute (PlugInContext context) throws Exception
	{
		List listaMunicipios = AppContext.getAlMunicipios();
		if (listaMunicipios != null && listaMunicipios.size() > 0)
			AppContext.setIdMunicipio(((Municipio)listaMunicipios.get(0)).getId());
		//Creamos el dialogo
		if (d!=null)
			d.dispose();
		d=new JDialog(context.getWorkbenchFrame(), true);

		d.setLayout(new GridBagLayout());	
		d.setSize(1000, 745);

		d.add(capasDestinoJLabel,new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.NONE, new Insets(10, 5, 0, 0),0,0) );
		d.add(getSelectSystemLayerPanel(),new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0),0,0) );

		d.add(capasOrigenJLabel,new GridBagConstraints(2, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.NONE, new Insets(10, 5, 0, 0),0,0) );		
		d.add(getSelectLocalLayerPanel(),new GridBagConstraints(3, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 10, 0),0,0) );


		FeatureTransformer transf=new FeatureTransformer();
		GeopistaSchema sch=new GeopistaSchema();
		GeopistaSchema t_sch=new GeopistaSchema();
		transf.setSourceSchema(sch);
		transf.setTargetSchema(t_sch);	

		d.add(getImporterPanel (transf), new GridBagConstraints(0, 1, 4, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(5, 0, 10, 0),0,0));
		
		//d.show();
		GUIUtil.centreOnScreen(d);
		d.setVisible(true);

		//Cuando se cierra la ventana, se borran del blackboard las variables que contienen
		//las capas origen y destino de la importación
		d.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				putToBlackboard(ImporterPlugIn.SELECTEDTARGETLAYER, null);
				putToBlackboard(ImporterPlugIn.SELECTEDSOURCELAYER, null);
			}
		});


		execute(new UndoableCommand(getName()) {
			public void execute() {
			}
			public void unexecute() {
			}
		}, context);

		return false;
	}

	public ImageIcon getIcon() {
		return IconLoader.icon("importer.gif");
	}

	public String getName() {
		return I18N.get("ImporterPlugIn","ImporterPlugIn.plugin.nombre");
	}


	private JPanel getSelectSystemLayerPanel() {
		//if (selectLayerPanel == null) {
		selectLayerPanel = new SelectLayerPanel(localContext, SelectLayerPanel.SYSTEM_LAYER);
//		selectLayerFieldPanel.setContext(localContext);
		selectLayerPanel.addPropertyChangeListener(
				new PropertyChangeListener()
				{						
					public void propertyChange(PropertyChangeEvent e)
					{ 
						if (logger.isDebugEnabled())
						{
							logger.debug("propertyChange(PropertyChangeEvent)"
									+ e.getPropertyName());
						}
						if (e.getPropertyName().equals("LAYERSELECTED_"+SelectLayerPanel.SYSTEM_LAYER))
						{
							Layer targetLayer = (Layer) e.getNewValue();
							putToBlackboard(SELECTEDTARGETLAYER,targetLayer);
							updateTargetAttributeList(targetLayer);
							importerPanel.updateTable();

						}
						else if (e.getPropertyName().equals("FIELDVALUE"))
						{
							String attName=(String) e.getNewValue();
							putToBlackboard(ATTRIBUTE_ID_ON_TARGET, attName);
						}
					}	

				});
		selectLayerPanel.addActionListener( new ActionListener() { 
			public void actionPerformed(ActionEvent e)
			{
				SelectLayerPanel pan=(SelectLayerPanel) e.getSource();
			}
		});

		selectLayerPanel.setPreferredSize(new Dimension (200, 120));
		selectLayerPanel.setMinimumSize(selectLayerPanel.getPreferredSize());
		selectLayerPanel.setMaximumSize(selectLayerPanel.getPreferredSize());

		//}
		return selectLayerPanel;
	}


	private JPanel getSelectLocalLayerPanel() {
		
		selectLocalLayerPanel = new SelectLayerPanel(localContext, SelectLayerPanel.LOCAL_LAYER);
		selectLocalLayerPanel.addPropertyChangeListener(
				new PropertyChangeListener()
				{
					public void propertyChange(PropertyChangeEvent e)
					{ 
						if (logger.isDebugEnabled())
						{
							logger.debug("propertyChange(PropertyChangeEvent)"
									+ e.getPropertyName());
						}
						if (e.getPropertyName().equals("LAYERSELECTED_"+SelectLayerPanel.LOCAL_LAYER))
						{
							Layer sourceLayer = (Layer) e.getNewValue();
							putToBlackboard(SELECTEDSOURCELAYER,sourceLayer);

							updateSourceAttributeList(sourceLayer);
							if (getFromBlackboard(SELECTEDTARGETLAYER)!=null)
								importerPanel.updateTable();

						}
						else if (e.getPropertyName().equals("FIELDVALUE"))
						{
							String attName=(String) e.getNewValue();
							putToBlackboard(ATTRIBUTE_ID_ON_TARGET, attName);
						}
					}	

				});
		selectLocalLayerPanel.addActionListener( new ActionListener() { 
			public void actionPerformed(ActionEvent e)
			{
				SelectLayerPanel pan=(SelectLayerPanel) e.getSource();
			}
		});

		selectLocalLayerPanel.setPreferredSize(new Dimension (200, 120));
		selectLocalLayerPanel.setMinimumSize(selectLocalLayerPanel.getPreferredSize());
		selectLocalLayerPanel.setMaximumSize(selectLocalLayerPanel.getPreferredSize());

		//}
		return selectLocalLayerPanel;
	}	


	private void putToBlackboard(String key, Object value)
	{
		Blackboard bk;
		if (localContext!=null)
			bk=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();
		else
			bk=this.bk;

		bk.put(key,value);
	}

	private Object getFromBlackboard(String key)
	{
		Blackboard bk;
		if (localContext!=null)
			bk=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();
		else
			bk=this.bk;

		return bk.get(key);

	}

	private void updateSourceAttributeList(Layer layer)
	{	
		if (layer!=null && layer.getFeatureCollectionWrapper()!=null)
		{
			FeatureSchema fsch=layer.getFeatureCollectionWrapper().getFeatureSchema();

			FeatureTransformer transf=new FeatureTransformer();

			d.remove(importerPanel);
			transf.setSourceSchema(fsch);
			transf.setTargetSchema(importerPanel.getTransformer().getTargetSchema());

			d.add(getImporterPanel (transf), new GridBagConstraints(0, 1, 4, 1, 1, 1, GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(5, 0, 10, 0),0,0));

			d.show();		
		}
		else
		{
			FeatureTransformer transf=new FeatureTransformer();
			selectLocalLayerPanel.setSelectedIndex(-1);
			d.remove(importerPanel);
			transf.setSourceSchema(null);
			transf.setTargetSchema(importerPanel.getTransformer().getTargetSchema());

			d.add(getImporterPanel (transf), new GridBagConstraints(0, 1, 4, 1, 1, 1, GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(5, 0, 10, 0),0,0));

			putToBlackboard(SELECTEDSOURCELAYER, null);
			//((SelectLayerPanel)getSelectLocalLayerPanel()).setSelectedIndex(-1);
			d.show();
		}
	}

	private ImporterEditorPanel getImporterPanel(FeatureTransformer transf) {

		if (transf!=null)
		{
			importerPanel = new ImporterEditorPanel(transf, localContext);
		}
		else
		{
			importerPanel = new ImporterEditorPanel(localContext);
		}	
		
		importerPanel.addPropertyChangeListener(
				new PropertyChangeListener()
				{						
					public void propertyChange(PropertyChangeEvent e)
					{ 
						if (logger.isDebugEnabled())
						{
							logger.debug("propertyChange(PropertyChangeEvent)"
									+ e.getPropertyName());
						}
						
						if (e.getPropertyName().equals("LAYER_ERROR"))
						{
							String codError=(String) e.getNewValue();
							putToBlackboard("ERROR", codError);
							updateSourceAttributeList(null);
							//importerPanel.updateTable();							
						}
					}	
				});

		return importerPanel;
	}

	private void updateTargetAttributeList(Layer layer)
	{	
		if (layer!=null && layer.getFeatureCollectionWrapper()!=null)
		{
			FeatureSchema fsch=layer.getFeatureCollectionWrapper().getFeatureSchema();

			FeatureTransformer transf=new FeatureTransformer();
			if (fsch instanceof GeopistaSchema)
			{
				d.remove(importerPanel);
				transf.setTargetSchema((GeopistaSchema)fsch);
				transf.setSourceSchema(importerPanel.getTransformer().getSourceSchema());

				d.add(getImporterPanel (transf), new GridBagConstraints(0, 1, 4, 1, 1, 1, GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(5, 0, 10, 0),0,0));
				d.repaint();		
				d.show();
			}
		}
	}

	private boolean configureApp()
	{
		try
		{
			try
			{
				PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.ini", 3000);

			}catch(Exception e){};

			try
			{
				ConstantesImporter.clienteImporter = new ImporterClient(
						appContext.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +"/CServletImporter");
				ConstantesImporter.Locale = appContext.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES");

				try
				{
					ConstantesImporter.IdMunicipio=new Integer(appContext.getString("geopista.DefaultCityId")).intValue();
				}
				catch (Exception e)
				{
					System.out.println("Valor de id municipio no valido:"+e.toString()+appContext.getString("geopista.DefaultCityId"));
					logger.error("Valor de id municipio no valido:"+e.toString()+appContext.getString("geopista.DefaultCityId"));
					System.exit(-1);
				}
			}
			catch(Exception e)
			{
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				System.out.println("Excepcion al cargar el fichero de configuración:\n"+sw.toString());
				logger.error("Exception: " + sw.toString());
				System.exit(-1);
				return false;
			}
			return true;
		}
		catch (Exception ex)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}
}
