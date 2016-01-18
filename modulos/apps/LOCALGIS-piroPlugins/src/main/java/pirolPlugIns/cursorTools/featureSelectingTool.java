/**
 * featureSelectingTool.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 08.12.2004
 *
 * CVS header information:
 *  $RCSfile: featureSelectingTool.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:56 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/cursorTools/featureSelectingTool.java,v $
 */
package pirolPlugIns.cursorTools;

import java.awt.Color;
import java.awt.Shape;
import java.util.List;
import java.util.Map;

import pirolPlugIns.diagrams.InterativeDiagram;
import pirolPlugIns.diagrams.diagramCalculator;
import pirolPlugIns.plugIns.StandardPirolPlugIn;
import pirolPlugIns.utilities.FeatureCollectionTools;
import pirolPlugIns.utilities.colors.ColorGenerator;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Range;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.ColorThemingStyle;

/**
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public abstract class featureSelectingTool extends StandardPirolCursorTool implements featureSelector {
	protected int numCathegory = 0;
	
	protected PlugInContext context = null;
	protected LayerViewPanel layerViewPanel = null;
	
	protected Layer resultLayer = null;
	protected Layer layer = null;
	
	protected ColorGenerator colorGenerator = null;

	protected BasicStyle basicStyle = new BasicStyle(Color.yellow);
	
	protected boolean active = false;
	
	protected InterativeDiagram diagramm = null;
	
	protected diagramCalculator calculator = null;
	
	public featureSelectingTool(String iconString){
		super(new PersonalLogger(DebugUserIds.USER_Ole),iconString);
	}
	
	protected Shape getShape() throws Exception {
		// no shapes there...
		return null;
	}

	protected boolean check(EnableCheck check) {
		try {
			return super.check(check);
		} catch (Exception e ){
			return false;
		}
	}
	
	public void activate(ILayerViewPanel layerViewPanel) {
		super.activate(layerViewPanel);
	}
	
	protected static Feature getFeatureFromCollection( List features, int fid ){
		return FeatureCollectionTools.getFeatureFromCollection( features, fid );		
	}
	
	public void cancelGesture(){
	   
	    if ( this.diagramm != null ){
			if (!this.diagramm.isVisible()){
			    this.diagramm = null;
				this.context = null;
				this.calculator = null;
				this.resultLayer = null;
				active = false;
			}
		}
	}
	
	public void deactivate() {
		if (active){
			super.deactivate();
		}
	}

	protected void gestureFinished() throws Exception {
		active = false;
	}

	protected void makeColorArray( int numKat ){
		
	    this.colorGenerator = ColorGenerator.getTrafficLightColors(numKat);

	}
	
	public abstract void selectFeatures(List features, int kat);
	
	protected void identifyFeaturesInLayer( Layer layer, String Attribute, Object value, Color color){
		BasicStyle bs = new BasicStyle(color);
		bs.setLineColor(color);
		bs.setLineWidth(2);
		bs.setEnabled(true);

		Range.RangeTreeMap styleMap = new  Range.RangeTreeMap();
		
		styleMap.put(value,bs);
		
		Map oldMap = null;
		
		if (ColorThemingStyle.get(layer)!=null){
			oldMap = ColorThemingStyle.get(layer).getAttributeValueToBasicStyleMap();
			layer.removeStyle(ColorThemingStyle.get(layer));
		}
		
		if (oldMap != null){
			styleMap.putAll(oldMap);
		}
		
		ColorThemingStyle cts = null;
		
		cts = new ColorThemingStyle(Attribute, styleMap,this.basicStyle);
		
		cts.setAttributeName(Attribute);
		
		layer.getBasicStyle().setEnabled(false);
		cts.setEnabled(true);	
		layer.addStyle(cts);
		
		if (!cts.isEnabled())
			cts.setEnabled(true);
		
		if (!layer.isVisible()){
			layer.setVisible(true);
		}
	}
    
    public static Feature[] getFeaturesInFenceOrInLayer(PlugInContext context,Layer layer){
        return StandardPirolPlugIn.getFeaturesInFenceOrInLayer(context, layer);
    }
    
    

}
