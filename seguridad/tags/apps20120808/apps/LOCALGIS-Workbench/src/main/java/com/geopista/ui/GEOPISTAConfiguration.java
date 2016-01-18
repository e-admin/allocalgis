package com.geopista.ui;


import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.cursortool.GeopistaMeasureTool;
import com.geopista.ui.cursortool.GeopistaSelectFeaturesTool;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.AboutPlugIn;
import com.geopista.ui.plugin.SugerenciasPlugIn;
import com.geopista.ui.plugin.ViewFilteredAttributesPlugIn;
import com.geopista.ui.plugin.analysis.GeopistaBufferPlugIn;
import com.geopista.ui.plugin.analysis.GeopistaCalculateAreasAndLengthsPlugIn;
import com.geopista.ui.plugin.analysis.GeopistaGeometryFunctionPlugIn;
import com.geopista.ui.plugin.analysis.GeopistaOverlayPlugIn;
import com.geopista.ui.plugin.analysis.GeopistaUnionPlugIn;
import com.geopista.ui.plugin.clipboard.GeopistaCutSelectedItemsPlugIn;
import com.geopista.ui.plugin.scalebar.GeopistaScaleBarPlugIn;
import com.geopista.ui.plugin.scalebar.GeopistaScaleBarRenderer;
import com.geopista.ui.warp.GeopistaAffineTransformPlugIn;
import com.geopista.ui.warp.GeopistaWarpingPlugIn;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.Setup;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datasource.InstallStandardDataSourceQueryChoosersPlugIn;
import com.vividsolutions.jump.workbench.datasource.LoadDatasetPlugIn;
import com.vividsolutions.jump.workbench.datasource.SaveDatasetAsPlugIn;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.AttributeTab;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.cursortool.DrawPolygonFenceTool;
import com.vividsolutions.jump.workbench.ui.cursortool.DrawRectangleFenceTool;
import com.vividsolutions.jump.workbench.ui.cursortool.FeatureInfoTool;
import com.vividsolutions.jump.workbench.ui.cursortool.OrCompositeTool;
import com.vividsolutions.jump.workbench.ui.cursortool.QuasimodeTool;
import com.vividsolutions.jump.workbench.ui.cursortool.UpdateDynamicClipTool;
import com.vividsolutions.jump.workbench.ui.cursortool.UpdateDynamicFenceTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewCategoryPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewFeaturesPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.AddWMSDemoBoxEasterEggPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.ClearSelectionPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.CombineSelectedFeaturesPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.DeleteAllFeaturesPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.EditablePlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.ExplodeSelectedFeaturesPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInfoPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureStatisticsPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.GenerateLogPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.InstallStandardFeatureTextWritersPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.LayerStatisticsPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.MapToolTipsPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.OptionsPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.OutputWindowPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.PersistentBlackboardPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.RemoveSelectedCategoriesPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.RemoveSelectedLayersPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.SaveImageAsPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.SelectFeaturesInFencePlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.ShortcutKeysPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.ValidateSelectedLayersPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.VerticesInFencePlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.ViewSchemaPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.clipboard.CopyImagePlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.clipboard.CopySelectedItemsPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.clipboard.CopySelectedLayersPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.clipboard.CopyThisCoordinatePlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.clipboard.CutSelectedLayersPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.clipboard.PasteLayersPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.skin.InstallSkinsPlugIn;
import com.vividsolutions.jump.workbench.ui.renderer.style.ArrowLineStringEndpointStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.CircleLineStringEndpointStyle;
import com.vividsolutions.jump.workbench.ui.snap.InstallGridPlugIn;
import com.vividsolutions.jump.workbench.ui.snap.SnapToVerticesPolicy;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.zoom.InstallZoomBarPlugIn;
import com.vividsolutions.jump.workbench.ui.zoom.PanTool;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomBarPlugIn;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomNextPlugIn;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomPreviousPlugIn;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToClickPlugIn;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToCoordinatePlugIn;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFencePlugIn;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomTool;


/**
 *  Initializes the Workbench with various menus and cursor tools. Accesses the
 *  Workbench structure through a WorkbenchContext.
 */
public class GEOPISTAConfiguration implements Setup {
 
    private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();
 
	private OptionsPlugIn optionsPlugIn = new OptionsPlugIn();
    private InstallGridPlugIn installGridPlugIn = new InstallGridPlugIn();
    private PersistentBlackboardPlugIn persistentBlackboardPlugIn = new PersistentBlackboardPlugIn();
    private InstallSkinsPlugIn installSkinsPlugIn = new InstallSkinsPlugIn();
    private InstallZoomBarPlugIn installZoomBarPlugIn = new InstallZoomBarPlugIn();
    private InstallStandardDataSourceQueryChoosersPlugIn installStandardDataSourceQueryChoosersPlugIn =
        new InstallStandardDataSourceQueryChoosersPlugIn();
    private InstallStandardFeatureTextWritersPlugIn installStandardFeatureTextWritersPlugIn = new InstallStandardFeatureTextWritersPlugIn();
    private ShortcutKeysPlugIn shortcutKeysPlugIn = new ShortcutKeysPlugIn();
    private ClearSelectionPlugIn clearSelectionPlugIn = new ClearSelectionPlugIn();
 //   private EditWMS111QueryPlugIn editWMSQueryPlugIn = new EditWMS111QueryPlugIn();
    //private EditWMSQueryPlugIn editWMSQueryPlugIn = new EditWMSQueryPlugIn();
    //private AddWMSQueryPlugIn	addWMSQueryPlugIn=new AddWMSQueryPlugIn();
//    private AddWMS111QueryPlugIn addWMSQueryPlugIn = new AddWMS111QueryPlugIn();
    private AddNewFeaturesPlugIn addNewFeaturesPlugIn = new AddNewFeaturesPlugIn();
    
    private AddNewCategoryPlugIn addNewCategoryPlugIn = new AddNewCategoryPlugIn();
    
    private CopySelectedItemsPlugIn copySelectedItemsPlugIn = new CopySelectedItemsPlugIn();
    private CopyThisCoordinatePlugIn copyThisCoordinatePlugIn = new CopyThisCoordinatePlugIn();
    private CopyImagePlugIn copyImagePlugIn = new CopyImagePlugIn();
    private MapToolTipsPlugIn toolTipsPlugIn = new MapToolTipsPlugIn();
    private CopySelectedLayersPlugIn copySelectedLayersPlugIn = new CopySelectedLayersPlugIn();
    private AddWMSDemoBoxEasterEggPlugIn addWMSDemoBoxEasterEggPlugIn = new AddWMSDemoBoxEasterEggPlugIn();
    private FeatureStatisticsPlugIn featureStatisticsPlugIn = new FeatureStatisticsPlugIn();
    private LayerStatisticsPlugIn layerStatisticsPlugIn = new LayerStatisticsPlugIn();
    private LoadDatasetPlugIn loadDatasetPlugIn = new LoadDatasetPlugIn();
    private SaveDatasetAsPlugIn saveDatasetAsPlugIn = new SaveDatasetAsPlugIn();
    private SaveImageAsPlugIn saveImageAsPlugIn = new SaveImageAsPlugIn();
    private GenerateLogPlugIn generateLogPlugIn = new GenerateLogPlugIn();
    private GeopistaOverlayPlugIn overlayPlugIn = new GeopistaOverlayPlugIn();
    private GeopistaUnionPlugIn unionPlugIn = new GeopistaUnionPlugIn();
    private GeopistaGeometryFunctionPlugIn geometryFunctionPlugIn = new GeopistaGeometryFunctionPlugIn();
    private GeopistaBufferPlugIn bufferPlugIn = new GeopistaBufferPlugIn();
    
    private PasteLayersPlugIn pasteLayersPlugIn = new PasteLayersPlugIn();
    private DeleteAllFeaturesPlugIn deleteAllFeaturesPlugIn = new DeleteAllFeaturesPlugIn();
    private RemoveSelectedLayersPlugIn removeSelectedLayersPlugIn = new RemoveSelectedLayersPlugIn();
    private RemoveSelectedCategoriesPlugIn removeSelectedCategoriesPlugIn = new RemoveSelectedCategoriesPlugIn();
    private SelectFeaturesInFencePlugIn selectFeaturesInFencePlugIn = new SelectFeaturesInFencePlugIn();
    private GeopistaScaleBarPlugIn scaleBarPlugIn = new GeopistaScaleBarPlugIn();
    private ZoomBarPlugIn zoomBarPlugIn = new ZoomBarPlugIn();
    private ValidateSelectedLayersPlugIn validateSelectedLayersPlugIn = new ValidateSelectedLayersPlugIn();
    private GeopistaCalculateAreasAndLengthsPlugIn calculateAreasAndLengthsPlugIn = new GeopistaCalculateAreasAndLengthsPlugIn();
    private ViewFilteredAttributesPlugIn viewFilteredAttributesPlugIn = new ViewFilteredAttributesPlugIn();
    private FeatureInfoPlugIn featureInfoPlugIn = new FeatureInfoPlugIn();
    private OutputWindowPlugIn outputWindowPlugIn = new OutputWindowPlugIn();
    private VerticesInFencePlugIn verticesInFencePlugIn = new VerticesInFencePlugIn();
    private ZoomNextPlugIn zoomNextPlugIn = new ZoomNextPlugIn();
    private ZoomToClickPlugIn zoomToClickPlugIn = new ZoomToClickPlugIn(0.5);
    private ZoomToClickPlugIn zoomInToClickPlugIn = new ZoomToClickPlugIn(2);
    private ZoomPreviousPlugIn zoomPreviousPlugIn = new ZoomPreviousPlugIn();
    private ZoomToFencePlugIn zoomToFencePlugIn = new ZoomToFencePlugIn();
    private ZoomToCoordinatePlugIn zoomToCoordinatePlugIn = new ZoomToCoordinatePlugIn();
    private ZoomToSelectedItemsPlugIn zoomToSelectedItemsPlugIn = new ZoomToSelectedItemsPlugIn();
    private GeopistaCutSelectedItemsPlugIn cutSelectedItemsPlugIn = new GeopistaCutSelectedItemsPlugIn();
    private CutSelectedLayersPlugIn cutSelectedLayersPlugIn = new CutSelectedLayersPlugIn();
    private CombineSelectedFeaturesPlugIn combineSelectedFeaturesPlugIn = new CombineSelectedFeaturesPlugIn();
    private ExplodeSelectedFeaturesPlugIn explodeSelectedFeaturesPlugIn = new ExplodeSelectedFeaturesPlugIn();
    private GeopistaEditingPlugIn editingPlugIn = new GeopistaEditingPlugIn();
    private EditablePlugIn editablePlugIn = new EditablePlugIn(editingPlugIn);
    private ViewSchemaPlugIn viewSchemaPlugIn = new ViewSchemaPlugIn(editingPlugIn);
	private TaskMonitor	monitor;    
 
    
    
    /**
     * Opciones de configuracion de Geopista
     * @param workbenchContext 
     * @param monitor 
     * @throws java.lang.Exception 
     */
    public void setup(WorkbenchContext workbenchContext, TaskMonitor monitor)
        throws Exception {
        this.monitor=monitor;
        configureStyles(workbenchContext);
        workbenchContext.getIWorkbench().getBlackboard().put(SnapToVerticesPolicy.ENABLED_KEY,
            true);

        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        FeatureInstaller featureInstaller = new FeatureInstaller(workbenchContext);
        configureToolBar(workbenchContext, checkFactory);
        configureMainMenus(workbenchContext, checkFactory, featureInstaller);
        configureLayerPopupMenu(workbenchContext, featureInstaller, checkFactory);
        configureAttributePopupMenu(workbenchContext, featureInstaller,
            checkFactory);
        configureWMSQueryNamePopupMenu(workbenchContext, featureInstaller,
            checkFactory);
        configureCategoryPopupMenu(workbenchContext, featureInstaller);
        configureLayerViewPanelPopupMenu(workbenchContext, checkFactory,
            featureInstaller);
/**Shortcuts**/
       
        workbenchContext.getIWorkbench().getGuiComponent().addKeyboardShortcut(KeyEvent.VK_COPY,
            0, copySelectedItemsPlugIn, CopySelectedItemsPlugIn.createEnableCheck(workbenchContext));
    
        workbenchContext.getIWorkbench().getGuiComponent().addKeyboardShortcut(KeyEvent.VK_CUT,
                0, cutSelectedItemsPlugIn, GeopistaCutSelectedItemsPlugIn.createEnableCheck(workbenchContext));
    
        //Call #initializeBuiltInPlugIns after #configureToolBar so that any plug-ins that
        //add items to the toolbar will add them to the *end* of the toolbar. [Jon Aquino]
        initializeBuiltInPlugIns(workbenchContext);
      //  PlugInContext context=new PlugInContext(workbenchContext,null,null,null, null);
    
   }

    private void configureCategoryPopupMenu(WorkbenchContext workbenchContext,
        FeatureInstaller featureInstaller) {
        featureInstaller.addPopupMenuItem(workbenchContext.getIWorkbench()
                                                          .getGuiComponent()
                                                          .getCategoryPopupMenu(),
            loadDatasetPlugIn, aplicacion.getI18nString(loadDatasetPlugIn.getName()) + "...", false,
            null, LoadDatasetPlugIn.createEnableCheck(workbenchContext));
         
        featureInstaller.addPopupMenuItem(workbenchContext.getIWorkbench()
                                                          .getGuiComponent()
                                                          .getCategoryPopupMenu(),
            pasteLayersPlugIn, aplicacion.getI18nString(pasteLayersPlugIn.getNameWithMnemonic()), false, null,
            pasteLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(workbenchContext.getIWorkbench()
                                                          .getGuiComponent()
                                                          .getCategoryPopupMenu(),
            removeSelectedCategoriesPlugIn,
            aplicacion.getI18nString(removeSelectedCategoriesPlugIn.getName()), false, null,
            removeSelectedCategoriesPlugIn.createEnableCheck(workbenchContext));
    }

    private void configureWMSQueryNamePopupMenu(
        final WorkbenchContext workbenchContext,
        FeatureInstaller featureInstaller, EnableCheckFactory checkFactory) {
        JPopupMenu wmsLayerNamePopupMenu = workbenchContext.getIWorkbench()
                                                           .getGuiComponent()
                                                           .getWMSLayerNamePopupMenu();
        wmsLayerNamePopupMenu.addSeparator(); // ===================
        wmsLayerNamePopupMenu.addSeparator(); // ===================
        featureInstaller.addPopupMenuItem(wmsLayerNamePopupMenu,
            cutSelectedLayersPlugIn, aplicacion.getI18nString(cutSelectedLayersPlugIn.getNameWithMnemonic()), false,
            null, cutSelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(wmsLayerNamePopupMenu,
            copySelectedLayersPlugIn, aplicacion.getI18nString(copySelectedLayersPlugIn.getNameWithMnemonic()),
            false, null,
            copySelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(wmsLayerNamePopupMenu,
            removeSelectedLayersPlugIn, aplicacion.getI18nString(removeSelectedLayersPlugIn.getName()),
            false, null,
            removeSelectedLayersPlugIn.createEnableCheck(workbenchContext));
    }

    private void configureAttributePopupMenu(
        final WorkbenchContext workbenchContext,
        FeatureInstaller featureInstaller, EnableCheckFactory checkFactory) {
        
        AttributeTab.addPopupMenuItem(workbenchContext, featureInfoPlugIn,
            aplicacion.getI18nString(featureInfoPlugIn.getName()), false,
            GUIUtil.toSmallIcon(FeatureInfoTool.ICON),
            FeatureInfoPlugIn.createEnableCheck(workbenchContext));
        AttributeTab.addPopupMenuItem(workbenchContext, viewSchemaPlugIn,
            aplicacion.getI18nString(viewSchemaPlugIn.getName()), false, ViewSchemaPlugIn.ICON,
            ViewSchemaPlugIn.createEnableCheck(workbenchContext));
        AttributeTab.addPopupMenuItem(workbenchContext, cutSelectedItemsPlugIn,
            aplicacion.getI18nString(cutSelectedItemsPlugIn.getName()), false, null,
            cutSelectedItemsPlugIn.createEnableCheck(workbenchContext));
        AttributeTab.addPopupMenuItem(workbenchContext,
            copySelectedItemsPlugIn, aplicacion.getI18nString(copySelectedItemsPlugIn.getNameWithMnemonic()), false,
            null, CopySelectedItemsPlugIn.createEnableCheck(workbenchContext));
        
    }

    private void configureLayerPopupMenu(
        final WorkbenchContext workbenchContext,
        FeatureInstaller featureInstaller, EnableCheckFactory checkFactory) {
        JPopupMenu layerNamePopupMenu = workbenchContext.getIWorkbench()
                                                        .getGuiComponent()
                                                        .getLayerNamePopupMenu();
        
        layerNamePopupMenu.addSeparator(); // ===================
            featureInstaller.addPopupMenuItem(layerNamePopupMenu,
            viewFilteredAttributesPlugIn, aplicacion.getI18nString(viewFilteredAttributesPlugIn.getName()), false,
            GUIUtil.toSmallIcon(viewFilteredAttributesPlugIn.getIcon()),
            viewFilteredAttributesPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(layerNamePopupMenu, viewSchemaPlugIn,
            aplicacion.getI18nString(viewSchemaPlugIn.getName()), false, ViewSchemaPlugIn.ICON,
            ViewSchemaPlugIn.createEnableCheck(workbenchContext));
        layerNamePopupMenu.addSeparator(); // ===================
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
            saveDatasetAsPlugIn, aplicacion.getI18nString(saveDatasetAsPlugIn.getName()) + "...", false,
            null, SaveDatasetAsPlugIn.createEnableCheck(workbenchContext));
        layerNamePopupMenu.addSeparator(); // ===================
        layerNamePopupMenu.addSeparator(); // ===================
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
            cutSelectedLayersPlugIn, aplicacion.getI18nString(cutSelectedLayersPlugIn.getNameWithMnemonic()), false,
            null, cutSelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
            copySelectedLayersPlugIn, aplicacion.getI18nString(copySelectedLayersPlugIn.getNameWithMnemonic()),
            false, null,
            copySelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
            removeSelectedLayersPlugIn, aplicacion.getI18nString(removeSelectedLayersPlugIn.getName()),
            false, null,
            removeSelectedLayersPlugIn.createEnableCheck(workbenchContext));
        layerNamePopupMenu.addSeparator(); // ===================
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
            addNewFeaturesPlugIn, aplicacion.getI18nString(addNewFeaturesPlugIn.getName()) + "...",
            false, null,
            AddNewFeaturesPlugIn.createEnableCheck(workbenchContext));

        //<<TODO:REFACTORING>> JUMPConfiguration is polluted with a lot of EnableCheck
        //logic. This logic should simply be moved to the individual PlugIns. [Jon Aquino]
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
            deleteAllFeaturesPlugIn, aplicacion.getI18nString(deleteAllFeaturesPlugIn.getName()), false,
            null, deleteAllFeaturesPlugIn.createEnableCheck(workbenchContext));
    }

    private void configureLayerViewPanelPopupMenu(
        WorkbenchContext workbenchContext, EnableCheckFactory checkFactory,
        FeatureInstaller featureInstaller) {
        JPopupMenu popupMenu = workbenchContext.getIWorkbench().getGuiComponent().getLayerViewPopupMenu();
        featureInstaller.addPopupMenuItem(popupMenu, featureInfoPlugIn,
            aplicacion.getI18nString(featureInfoPlugIn.getName()), false,
            GUIUtil.toSmallIcon(FeatureInfoTool.ICON),
            FeatureInfoPlugIn.createEnableCheck(workbenchContext));
        
        featureInstaller.addPopupMenuItem(popupMenu, verticesInFencePlugIn,
            aplicacion.getI18nString(verticesInFencePlugIn.getName()), false, null,
            new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                                  .add(checkFactory.createFenceMustBeDrawnCheck()));
        popupMenu.addSeparator(); // ===================
        featureInstaller.addPopupMenuItem(popupMenu, zoomToFencePlugIn,
            aplicacion.getI18nString(zoomToFencePlugIn.getName()), false,
            GUIUtil.toSmallIcon(zoomToFencePlugIn.getIcon()),
            new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                                  .add(checkFactory.createFenceMustBeDrawnCheck()));
        featureInstaller.addPopupMenuItem(popupMenu, zoomToSelectedItemsPlugIn,
            aplicacion.getI18nString(zoomToSelectedItemsPlugIn.getName()), false,
            GUIUtil.toSmallIcon(zoomToSelectedItemsPlugIn.getIcon()),
            ZoomToSelectedItemsPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(popupMenu, zoomToClickPlugIn,
            aplicacion.getI18nString("Zoom Out"), false, null, null);
        featureInstaller.addPopupMenuItem(popupMenu, zoomInToClickPlugIn,
                aplicacion.getI18nString("Zoom In"), false, null, null);
        popupMenu.addSeparator(); // ===================
        featureInstaller.addPopupMenuItem(popupMenu,
            selectFeaturesInFencePlugIn, aplicacion.getI18nString(selectFeaturesInFencePlugIn.getName()),
            false, null,
            SelectFeaturesInFencePlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(popupMenu, cutSelectedItemsPlugIn,
            aplicacion.getI18nString(cutSelectedItemsPlugIn.getName()), false, null,
            cutSelectedItemsPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(popupMenu, copySelectedItemsPlugIn,
            aplicacion.getI18nString(copySelectedItemsPlugIn.getNameWithMnemonic()), false, null,
            CopySelectedItemsPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addPopupMenuItem(popupMenu, copyThisCoordinatePlugIn,
            aplicacion.getI18nString(copyThisCoordinatePlugIn.getName()), false, null,
            CopyThisCoordinatePlugIn.createEnableCheck(workbenchContext));
    }

    private void configureMainMenus(final WorkbenchContext workbenchContext,
        final EnableCheckFactory checkFactory, FeatureInstaller featureInstaller)
        throws Exception {
        
        featureInstaller.addMainMenuItem(loadDatasetPlugIn, "File",
            aplicacion.getI18nString(loadDatasetPlugIn.getName()) + "...", null,
            LoadDatasetPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(saveDatasetAsPlugIn, "File",
            aplicacion.getI18nString(saveDatasetAsPlugIn.getName()) + "...", null,
            SaveDatasetAsPlugIn.createEnableCheck(workbenchContext));
        //featureInstaller.addMenuSeparator(aplicacion.getI18nString("File")); // ===================
        featureInstaller.addMainMenuItem(saveImageAsPlugIn,"File",
                aplicacion.getI18nString(saveImageAsPlugIn.getName()) + "...", null,
                SaveImageAsPlugIn.createEnableCheck(workbenchContext));        
 
       featureInstaller.addMainMenuItem(addNewFeaturesPlugIn, "Edit",
            aplicacion.getI18nString(addNewFeaturesPlugIn.getName()) + "...", null,
            AddNewFeaturesPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(selectFeaturesInFencePlugIn, "Edit",
            aplicacion.getI18nString(selectFeaturesInFencePlugIn.getName()), null,
            SelectFeaturesInFencePlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(clearSelectionPlugIn, "Edit",
            aplicacion.getI18nString(clearSelectionPlugIn.getName()), null,
            clearSelectionPlugIn.createEnableCheck(workbenchContext));        
        featureInstaller.addMenuSeparator("Edit"); // ===================
        featureInstaller.addMainMenuItem(cutSelectedItemsPlugIn, "Edit",
            aplicacion.getI18nString(cutSelectedItemsPlugIn.getName()), null,
            cutSelectedItemsPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(copySelectedItemsPlugIn, ("Edit"),
            aplicacion.getI18nString(copySelectedItemsPlugIn.getNameWithMnemonic()), null,
            CopySelectedItemsPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(copyImagePlugIn, ("Edit"),
                aplicacion.getI18nString(copyImagePlugIn.getName()), null,
                CopyImagePlugIn.createEnableCheck(workbenchContext));        
        featureInstaller.addMenuSeparator(("Edit")); // ===================
        featureInstaller.addMenuSeparator(("Edit")); // ===================
        featureInstaller.addMainMenuItem(optionsPlugIn, ("Edit"),
            aplicacion.getI18nString(optionsPlugIn.getName()) + "...", null, null);
        featureInstaller.addMainMenuItem(
                combineSelectedFeaturesPlugIn,("Edit"),
                aplicacion.getI18nString(combineSelectedFeaturesPlugIn.getName()), null,
                combineSelectedFeaturesPlugIn.createEnableCheck(workbenchContext));
            featureInstaller.addMainMenuItem(
                explodeSelectedFeaturesPlugIn,("Edit"),
                aplicacion.getI18nString(explodeSelectedFeaturesPlugIn.getName()), null,
                explodeSelectedFeaturesPlugIn.createEnableCheck(workbenchContext));
        editingPlugIn.createMainMenuItem(new String[] { ("View") },
            GUIUtil.toSmallIcon(EditingPlugIn.ICON), workbenchContext);
        featureInstaller.addMenuSeparator(("View")); // ===================
        featureInstaller.addMainMenuItem(featureInfoPlugIn, ("View"),
            aplicacion.getI18nString(featureInfoPlugIn.getName()),
            GUIUtil.toSmallIcon(FeatureInfoTool.ICON),
            FeatureInfoPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(verticesInFencePlugIn, ("View"),
            aplicacion.getI18nString(verticesInFencePlugIn.getName()), null,
            new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                                  .add(checkFactory.createFenceMustBeDrawnCheck()));
        featureInstaller.addMenuSeparator(("View")); // ===================
        featureInstaller.addMainMenuItem(zoomToFencePlugIn,("View"),
            aplicacion.getI18nString(zoomToFencePlugIn.getName()),
            GUIUtil.toSmallIcon(zoomToFencePlugIn.getIcon()),
            new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                                  .add(checkFactory.createFenceMustBeDrawnCheck()));        
		featureInstaller.addMainMenuItem(zoomToSelectedItemsPlugIn, ("View"),
				aplicacion.getI18nString(zoomToSelectedItemsPlugIn.getName()), GUIUtil.toSmallIcon(
						zoomToSelectedItemsPlugIn.getIcon()),
				ZoomToSelectedItemsPlugIn.createEnableCheck(workbenchContext));
		featureInstaller.addMainMenuItem(zoomToCoordinatePlugIn, ("View"),
				aplicacion.getI18nString(zoomToCoordinatePlugIn.getName()) + "...", null,
				zoomToCoordinatePlugIn.createEnableCheck(workbenchContext));
		featureInstaller.addMainMenuItem(zoomPreviousPlugIn,("View"),
				aplicacion.getI18nString(zoomPreviousPlugIn.getName()), GUIUtil.toSmallIcon(
						zoomPreviousPlugIn.getIcon()),
				zoomPreviousPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(zoomNextPlugIn,("View"),
            aplicacion.getI18nString(zoomNextPlugIn.getName()),
            GUIUtil.toSmallIcon(zoomNextPlugIn.getIcon()),
            zoomNextPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMenuSeparator(("View")); // ===================
        featureInstaller.addMainMenuItem(scaleBarPlugIn,
            new String[] {("View")}, aplicacion.getI18nString(scaleBarPlugIn.getName()), true, null,
            new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                                  .add(new EnableCheck() {
                public String check(JComponent component) {
                    ((JCheckBoxMenuItem) component).setSelected(
                            GeopistaScaleBarRenderer.isEnabled(
                                workbenchContext.getLayerViewPanel()));

                    return null;
                }
            }));
        featureInstaller.addMainMenuItem(toolTipsPlugIn,
            new String[] { ("View") }, aplicacion.getI18nString(toolTipsPlugIn.getName()), true, null,
            MapToolTipsPlugIn.createEnableCheck(workbenchContext));
        zoomBarPlugIn.createMainMenuItem(new String[] { ("View") }, null,
            workbenchContext);
        featureInstaller.addMainMenuItem(outputWindowPlugIn,("View"),
            aplicacion.getI18nString(outputWindowPlugIn.getName()),
            GUIUtil.toSmallIcon(outputWindowPlugIn.getIcon()), null);
        featureInstaller.addMainMenuItem(generateLogPlugIn,("View"),
            aplicacion.getI18nString(generateLogPlugIn.getName()), null, null);
           featureInstaller.addMainMenuItem(addNewCategoryPlugIn, ("Layer"),
            aplicacion.getI18nString(addNewCategoryPlugIn.getName()), null,
            addNewCategoryPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMenuSeparator( ("Layer")); // ===================
        featureInstaller.addMainMenuItem(cutSelectedLayersPlugIn,  ("Layer"),
            aplicacion.getI18nString(cutSelectedLayersPlugIn.getNameWithMnemonic()), null,
            cutSelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(copySelectedLayersPlugIn, ("Layer"),
            aplicacion.getI18nString(copySelectedLayersPlugIn.getNameWithMnemonic()), null,
            copySelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(pasteLayersPlugIn,  ("Layer"),
            aplicacion.getI18nString(pasteLayersPlugIn.getNameWithMnemonic()), null,
            pasteLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(featureInfoPlugIn, ("View"),
                aplicacion.getI18nString(featureInfoPlugIn.getName()),
                GUIUtil.toSmallIcon(FeatureInfoTool.ICON),
                FeatureInfoPlugIn.createEnableCheck(workbenchContext));
        /****/
        featureInstaller.addMenuSeparator( ("Layer")); // ===================
        featureInstaller.addMainMenuItem(removeSelectedLayersPlugIn,  ("Layer"),
            aplicacion.getI18nString(removeSelectedLayersPlugIn.getName()), null,
            removeSelectedLayersPlugIn.createEnableCheck(workbenchContext));
        featureInstaller.addMainMenuItem(removeSelectedCategoriesPlugIn,
             aplicacion.getI18nString("Layer"), aplicacion.getI18nString(removeSelectedCategoriesPlugIn.getName()), null,
            removeSelectedCategoriesPlugIn.createEnableCheck(workbenchContext));
        
        
        featureInstaller.addMenuSeparator("Window"); // ===================
       new GeopistaWarpingPlugIn().initialize(new PlugInContext(workbenchContext,
                null, null, null, null));
        new GeopistaAffineTransformPlugIn().initialize(new PlugInContext(
                workbenchContext, null, null, null, null));
        
        featureInstaller.addMainMenuItem(validateSelectedLayersPlugIn,
            new String[] {("Tools"), ("QA") },
            aplicacion.getI18nString(validateSelectedLayersPlugIn.getName()) + "...", false, null,
            new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                                  .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(
                    1)));
        featureInstaller.addMainMenuItem(layerStatisticsPlugIn,
            new String[] { ("Tools"), ("QA") }, aplicacion.getI18nString(layerStatisticsPlugIn.getName()),
            false, null,
            new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                                  .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(
                    1)));
        featureInstaller.addMainMenuItem(featureStatisticsPlugIn,
            new String[] { "Tools", aplicacion.getI18nString("QA") }, aplicacion.getI18nString(featureStatisticsPlugIn.getName()),
            false, null,
            new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                                  .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(
                    1)));
        

        
        featureInstaller.addMainMenuItem(shortcutKeysPlugIn, aplicacion.getI18nString("Help"),
            aplicacion.getI18nString(shortcutKeysPlugIn.getName()) + "...", null, null);
        new FeatureInstaller(workbenchContext).addMainMenuItem(new SugerenciasPlugIn(),
                "Help", "Sugerencias/Incidencias", null, null);            
        
        new FeatureInstaller(workbenchContext).addMainMenuItem(new AboutPlugIn(),
            "Help", aplicacion.getI18nString("About"), null, null);     
    }

    private void configureStyles(WorkbenchContext workbenchContext) {
        WorkbenchGuiComponent frame = (WorkbenchGuiComponent) workbenchContext.getIWorkbench().getGuiComponent();
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.FeathersStart.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.FeathersEnd.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.OpenStart.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.OpenEnd.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.SolidStart.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.SolidEnd.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.NarrowSolidStart.class);
        frame.addChoosableStyleClass(ArrowLineStringEndpointStyle.NarrowSolidEnd.class);
        frame.addChoosableStyleClass(CircleLineStringEndpointStyle.Start.class);
        frame.addChoosableStyleClass(CircleLineStringEndpointStyle.End.class);
    }

    private QuasimodeTool add(CursorTool tool, WorkbenchContext context) {
        return context.getIWorkbench().getGuiComponent().getToolBar().addCursorTool(tool)
                      .getQuasimodeTool();
    }

    private void configureToolBar(final WorkbenchContext workbenchContext,
        EnableCheckFactory checkFactory) {
        WorkbenchGuiComponent frame = (WorkbenchGuiComponent)workbenchContext.getIWorkbench().getGuiComponent();
        add(new ZoomTool(), workbenchContext);
        add(new PanTool(), workbenchContext);
        frame.getToolBar().addSeparator();
        frame.getToolBar().addPlugIn(zoomToSelectedItemsPlugIn.getIcon(),
            zoomToSelectedItemsPlugIn,
            ZoomToSelectedItemsPlugIn.createEnableCheck(workbenchContext),
            workbenchContext);
        frame.getToolBar().addPlugIn(zoomToFencePlugIn.getIcon(),
            zoomToFencePlugIn,
            new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                                  .add(checkFactory.createFenceMustBeDrawnCheck()),
            workbenchContext);
        frame.getToolBar().addPlugIn(zoomPreviousPlugIn.getIcon(),
            zoomPreviousPlugIn,
            zoomPreviousPlugIn.createEnableCheck(workbenchContext),
            workbenchContext);
        frame.getToolBar().addPlugIn(zoomNextPlugIn.getIcon(), zoomNextPlugIn,
            zoomNextPlugIn.createEnableCheck(workbenchContext), workbenchContext);
        frame.getToolBar().addPlugIn(viewFilteredAttributesPlugIn.getIcon(),
            viewFilteredAttributesPlugIn,
            viewFilteredAttributesPlugIn.createEnableCheck(workbenchContext),
            workbenchContext);
        frame.getToolBar().addSeparator();

        //Null out the quasimodes for [Ctrl] because the Select tools will handle that case. [Jon Aquino]
        add(new QuasimodeTool(new GeopistaSelectFeaturesTool()).add(
                new QuasimodeTool.ModifierKeySpec(true, false, false), null),
            workbenchContext);
        add(new OrCompositeTool() {
                public String getName() {
                    return "Fence";
                }
            }.add(new DrawRectangleFenceTool()).add(new DrawPolygonFenceTool()),
            workbenchContext);
        add(new FeatureInfoTool(), workbenchContext);
        frame.getToolBar().addSeparator();
       // configureEditingButton(workbenchContext);
        frame.getToolBar().addSeparator();
        add(new GeopistaMeasureTool(), workbenchContext);
        frame.getToolBar().addSeparator();
        add(new UpdateDynamicFenceTool(), workbenchContext);
        add(new UpdateDynamicClipTool(), workbenchContext);
        
        
        frame.getToolBar().addSeparator();
        workbenchContext.getIWorkbench().getGuiComponent().getOutputFrame().setButton(frame.getToolBar()
                                                                                   .addPlugIn(outputWindowPlugIn.getIcon(),
                outputWindowPlugIn, new MultiEnableCheck(), workbenchContext));

        //Last of all, add a separator because some plug-ins may add CursorTools.
        //[Jon Aquino]
        frame.getToolBar().addSeparator();
    }

    private void configureEditingButton(final WorkbenchContext workbenchContext) {
        final JToggleButton toggleButton = new JToggleButton();
        workbenchContext.getIWorkbench().getGuiComponent().getToolBar().add(toggleButton,
            editingPlugIn.getName(), EditingPlugIn.ICON,
            AbstractPlugIn.toActionListener(editingPlugIn, workbenchContext,
                new TaskMonitorManager()), null);
        workbenchContext.getIWorkbench().getGuiComponent().addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent e) {
                    //Can't #getToolbox before Workbench is thrown. Otherwise, get 
                    //IllegalComponentStateException. Thus, do it inside #componentShown. [Jon Aquino]
                    editingPlugIn.getToolbox(workbenchContext)
                                 .addComponentListener(new ComponentAdapter() {
                            //There are other ways to show/hide the toolbox. Track 'em. [Jon Aquino]
                            public void componentShown(ComponentEvent e) {
                                toggleButton.setSelected(true);
                            }

                            public void componentHidden(ComponentEvent e) {
                                toggleButton.setSelected(false);
                            }
                        });
                }
            });
    }

    /**
     *  Call each PlugIn's #initialize() method. Uses reflection to build a list
     *  of plug-ins.
     *
     *@param  workbenchContext  Description of the Parameter
     *@exception  Exception     Description of the Exception
     */
    private void initializeBuiltInPlugIns(WorkbenchContext workbenchContext)
        throws Exception {
        Field[] fields = getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Object field = null;

            try {
                field = fields[i].get(this);
            } catch (IllegalAccessException e) {
                Assert.shouldNeverReachHere();
            }

            if (!(field instanceof PlugIn)) {
                continue;
            }

            PlugIn plugIn = (PlugIn) field;
            monitor.report("Loading:"+plugIn.getName()+ "(built-in)");
            plugIn.initialize(new PlugInContext(workbenchContext, null, null,
                    null, null));
        }
        
    }

    
}