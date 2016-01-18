/*
 * Created on 18.01.2006 for PIROL
 *
 * CVS header information:
 *  $RCSfile: RasterImageContextMenu.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:31 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/plugIns/PirolRasterImage/RasterImageContextMenu.java,v $
 */
package pirolPlugIns.plugIns.PirolRasterImage;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.MenuElement;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import pirolPlugIns.utilities.RasterImageSupport.RasterImageLayer;

import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.TitledPopupMenu;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.plugin.MoveLayerablePlugIn;

/**
 * TODO: comment class
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2006),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class RasterImageContextMenu extends TitledPopupMenu {
    
    private static final long serialVersionUID = -8757500299734680615L;

    /** singleton */
    private static RasterImageContextMenu contextMenu = null;
    
    /**
     * use this method to get an instance of the context menu.
     * The menu will be instantiated when this method is called the first time.
     *@return an instance of the context menu
     */
    public static RasterImageContextMenu getInstance(PlugInContext context){
        if (contextMenu==null){
            contextMenu = new RasterImageContextMenu(context);
        }
        return contextMenu;
    }

    /**
     * @see #getInstance(PlugInContext)
     */
    private RasterImageContextMenu(PlugInContext context) {
        super();
        
        FeatureInstaller featureInstaller = context.getFeatureInstaller();
        final WorkbenchFrame wbFrame = context.getWorkbenchFrame();
        
        this.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                LayerNamePanel panel = ((LayerNamePanelProxy) wbFrame.getActiveInternalFrame())
                        .getLayerNamePanel();
                setTitle(((Layerable) panel.selectedNodes(RasterImageLayer.class).iterator().next()).getName());
            }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        
        
        ToggleRasterImageVisibility toggleRasterImageVisibility = new ToggleRasterImageVisibility(context.getWorkbenchContext());
        this.addPopupMenuListener(toggleRasterImageVisibility);
        featureInstaller.addPopupMenuItem(this,
                toggleRasterImageVisibility, toggleRasterImageVisibility.getName(),
                true, null, null);
        
        MenuElement[] elements = this.getSubElements();
        JCheckBoxMenuItem menuItem = null;
        
        for (int i=0; i<elements.length; i++){
            if ( JCheckBoxMenuItem.class.isInstance(elements[i]) ){
                if ( ((JCheckBoxMenuItem)elements[i]).getText().startsWith(toggleRasterImageVisibility.getName()) ){
                    ((JCheckBoxMenuItem)elements[i]).setSelected(true);
                    menuItem = (JCheckBoxMenuItem)elements[i];
                }
            }
        }
        
        toggleRasterImageVisibility.setMenuItem(menuItem);
        
        this.addSeparator(); // ===================
        
        ChangeRasterImagePropertiesPlugIn changeRasterImagePropertiesPlugIn = new ChangeRasterImagePropertiesPlugIn();
        featureInstaller.addPopupMenuItem(this,
                changeRasterImagePropertiesPlugIn, changeRasterImagePropertiesPlugIn.getName() + "...",
                false, null, null);
        
        ExtractSelectedPartOfImage extractPartPlugIn = new ExtractSelectedPartOfImage();
        featureInstaller.addPopupMenuItem(this,
                extractPartPlugIn, extractPartPlugIn.getName() + "...", false,
                GUIUtil.toSmallIcon((ImageIcon) extractPartPlugIn.getIcon()),
                ExtractSelectedPartOfImage.createEnableCheck(context.getWorkbenchContext()));
        
        SaveRasterImageAsImagePlugIn saveRasterImageAsImagePlugIn = new SaveRasterImageAsImagePlugIn();
        featureInstaller.addPopupMenuItem(this, 
                saveRasterImageAsImagePlugIn, saveRasterImageAsImagePlugIn.getName() + "...",false,
                null,SaveRasterImageAsImagePlugIn.createEnableCheck(context.getWorkbenchContext()));
        
        this.addSeparator(); // ===================
        
        ZoomToRasterImagePlugIn zoomToRasterImagePlugIn = new ZoomToRasterImagePlugIn();
        featureInstaller.addPopupMenuItem(this, 
                zoomToRasterImagePlugIn, zoomToRasterImagePlugIn.getName() + "...",false,
                null,null);
        
        this.addSeparator(); // ===================
        
        WarpImageToFencePlugIn warpImageToFencePlugIn = new WarpImageToFencePlugIn();
        featureInstaller.addPopupMenuItem(this, 
                warpImageToFencePlugIn, warpImageToFencePlugIn.getName() + "...",false,
                null,WarpImageToFencePlugIn.createEnableCheck(context.getWorkbenchContext()));
        
        ExportEnvelopeAsGeometryPlugIn exportEnvelopeAsGeometryPlugIn = new ExportEnvelopeAsGeometryPlugIn();
        featureInstaller.addPopupMenuItem(this, 
                exportEnvelopeAsGeometryPlugIn, exportEnvelopeAsGeometryPlugIn.getName() + "...",false,
                null,null);
        
        this.addSeparator(); // ===================
        
        MoveLayerablePlugIn moveUpPlugIn = MoveLayerablePlugIn.UP;
        featureInstaller.addPopupMenuItem(this, moveUpPlugIn,
                moveUpPlugIn.getName() + "...", false, null, moveUpPlugIn.createEnableCheck(context.getWorkbenchContext()));
        
        MoveLayerablePlugIn moveDownPlugIn = MoveLayerablePlugIn.DOWN;
        featureInstaller.addPopupMenuItem(this, moveDownPlugIn,
                moveDownPlugIn.getName() + "...", false, null, moveDownPlugIn.createEnableCheck(context.getWorkbenchContext()));
        
        this.addSeparator(); // ===================
        
        CutSelectedRasterImageLayersPlugIn cutSelectedRasterImageLayersPlugIn = new CutSelectedRasterImageLayersPlugIn();
        featureInstaller.addPopupMenuItem(this,
                cutSelectedRasterImageLayersPlugIn, cutSelectedRasterImageLayersPlugIn.getName() + "...", false, null,
                cutSelectedRasterImageLayersPlugIn.createEnableCheck(context.getWorkbenchContext()));
        
        CopySelectedRasterImageLayersPlugIn copySelectedRasterImageLayersPlugIn = new CopySelectedRasterImageLayersPlugIn();
        featureInstaller.addPopupMenuItem(this,
                copySelectedRasterImageLayersPlugIn, copySelectedRasterImageLayersPlugIn.getName() + "...", false, null,
                copySelectedRasterImageLayersPlugIn.createEnableCheck(context.getWorkbenchContext()));
        
        RemoveSelectedRasterImageLayersPlugIn removeSelectedLayersPlugIn = new RemoveSelectedRasterImageLayersPlugIn();
        featureInstaller.addPopupMenuItem(this,
                removeSelectedLayersPlugIn, removeSelectedLayersPlugIn.getName() + "...", 
                false, null, removeSelectedLayersPlugIn.createEnableCheck(context.getWorkbenchContext()));
    }

}
