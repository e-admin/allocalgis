/**
 * ExportEnvelopeAsGeometryPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 09.01.2006 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ExportEnvelopeAsGeometryPlugIn.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:31 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/plugIns/PirolRasterImage/ExportEnvelopeAsGeometryPlugIn.java,v $
 */
package pirolPlugIns.plugIns.PirolRasterImage;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.plugIns.StandardPirolPlugIn;
import pirolPlugIns.utilities.LayerTools;
import pirolPlugIns.utilities.FeatureCollection.RoleOutline;
import pirolPlugIns.utilities.RasterImageSupport.RasterImageLayer;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * PlugIn to export the bounding box of the RasterImageLayer as a geometry layer, so 
 * it can be changed, transformed to a fence and be re-applied to the RasterImage.<br>
 * This enables all geometry operations for RasterImages. 
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
public class ExportEnvelopeAsGeometryPlugIn extends StandardPirolPlugIn {
    
    protected static FeatureSchema defaultSchema = null;

    public ExportEnvelopeAsGeometryPlugIn() {
        super(new PersonalLogger(DebugUserIds.USER_Ole));
        
        if (ExportEnvelopeAsGeometryPlugIn.defaultSchema==null){
            ExportEnvelopeAsGeometryPlugIn.defaultSchema = new FeatureSchema();
            
            ExportEnvelopeAsGeometryPlugIn.defaultSchema.addAttribute(PirolPlugInMessages.getString("geometry"), AttributeType.GEOMETRY);
        }
    }

    /**
     *@inheritDoc
     */
    public String getIconString() {
        return null;
    }

    /**
     *@inheritDoc
     */
    public boolean execute(PlugInContext context) throws Exception {
        RasterImageLayer rLayer = (RasterImageLayer) LayerTools.getSelectedLayerable(context, RasterImageLayer.class);
        
        if (rLayer==null){
            StandardPirolPlugIn.warnUser(context,PirolPlugInMessages.getString("no-layer-selected"));
            return this.finishExecution(context, false);
        }
        
        Geometry geom = rLayer.getEnvelopeAsGeometry();
        
        if (geom==null){
            return this.finishExecution(context, false);
        }
        
        FeatureCollection newFeaturecollection = new FeatureDataset((FeatureSchema)ExportEnvelopeAsGeometryPlugIn.defaultSchema.clone());
        
        BasicFeature feature = new BasicFeature((FeatureSchema)ExportEnvelopeAsGeometryPlugIn.defaultSchema.clone());
        
        feature.setAttribute(PirolPlugInMessages.getString("geometry"), geom);
        
        newFeaturecollection.add(feature);
        
        LayerTools.addStandardResultLayer(PirolPlugInMessages.getString("geometry") + ": " + rLayer.getName(), newFeaturecollection, context, new RoleOutline() );
        
        return false;
    }

}
