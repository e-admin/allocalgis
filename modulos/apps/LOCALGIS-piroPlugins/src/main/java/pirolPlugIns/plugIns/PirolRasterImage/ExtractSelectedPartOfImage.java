/**
 * ExtractSelectedPartOfImage.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 29.06.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ExtractSelectedPartOfImage.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:31 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/plugIns/PirolRasterImage/ExtractSelectedPartOfImage.java,v $
 */
package pirolPlugIns.plugIns.PirolRasterImage;

import java.awt.image.BufferedImage;

import pirolPlugIns.cursorTools.MyEnableCheckFactory;
import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.plugIns.StandardPirolPlugIn;
import pirolPlugIns.utilities.LayerTools;
import pirolPlugIns.utilities.PlugInContextTools;
import pirolPlugIns.utilities.SelectionTools;
import pirolPlugIns.utilities.RasterImageSupport.CurrentLayerIsRasterImageLayerCheck;
import pirolPlugIns.utilities.RasterImageSupport.RasterImageLayer;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * PlugIn that extracts a selected part (fence) of a raster image to a new raster image layer.<br>
 * Some parts were taken from Stefan Ostermann's SaveInterpolationAsImagePlugIn.
 *
 * @author Ole Rahn, (Stefan Ostermann)
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class ExtractSelectedPartOfImage extends StandardPirolPlugIn {
    
    public ExtractSelectedPartOfImage(){
        super(new PersonalLogger(DebugUserIds.USER_Ole));
    }

    /**
     *@inheritDoc
     */
    public String getIconString() {
        return "extractPart.png"; //$NON-NLS-1$
    }

    /**
     *@inheritDoc
     */
    public boolean execute(PlugInContext context) throws Exception {
        RasterImageLayer rLayer = (RasterImageLayer) LayerTools.getSelectedLayerable(context, RasterImageLayer.class);
        
        if (rLayer==null){
            StandardPirolPlugIn.warnUser(context,PirolPlugInMessages.getString("no-layer-selected")); //$NON-NLS-1$
            return this.finishExecution(context, false);
        }
        
        Geometry fence = SelectionTools.getFenceGeometry(context);
        Envelope envWanted = fence.getEnvelopeInternal();
        
        BufferedImage partOfImageWanted = rLayer.getTileAsImage(envWanted);
        
        if (partOfImageWanted==null){
            StandardPirolPlugIn.warnUser(context,PirolPlugInMessages.getString("fence-in-wrong-region"));
            return this.finishExecution(context, false);
        }
        
        boolean returnVal = this.putImageIntoMap(partOfImageWanted, envWanted, rLayer, context);
        
        return this.finishExecution(context, returnVal);
    }
    
    protected boolean putImageIntoMap(BufferedImage image, Envelope envelope, RasterImageLayer rLayer, PlugInContext context){
		if (image==null) return false;
	
		String newLayerName = context.getLayerManager().uniqueLayerName(PirolPlugInMessages.getString("part-of") + rLayer.getName());
        
        RasterImageLayer newRasterLayer = new RasterImageLayer(newLayerName, (LayerManager)context.getLayerManager(), image, envelope);
		
		String catName = StandardCategoryNames.WORKING;
		
		try {
            catName = ((Category)context.getLayerNamePanel().getSelectedCategories().toArray()[0]).getName();
        } catch (RuntimeException e1) {}
        
        context.getLayerManager().addLayerable(catName, newRasterLayer);

        return true;
    }
    
    public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        if (StandardPirolPlugIn.checkFactory == null){
            StandardPirolPlugIn.checkFactory = new MyEnableCheckFactory(workbenchContext);
        }
        MultiEnableCheck multiEnableCheck = new MultiEnableCheck();
        
        multiEnableCheck.add( StandardPirolPlugIn.checkFactory.createExactlyNLayerablesMustBeSelectedCheck(1, RasterImageLayer.class) );
        multiEnableCheck.add( StandardPirolPlugIn.checkFactory.createFenceMustBeDrawnCheck() );
        
        EnableCheck enableCheck = new CurrentLayerIsRasterImageLayerCheck(PlugInContextTools.getContext(workbenchContext));		
		multiEnableCheck.add(enableCheck);
        
        return multiEnableCheck;
	}
    
    public void initialize(PlugInContext context) throws Exception {}

}
