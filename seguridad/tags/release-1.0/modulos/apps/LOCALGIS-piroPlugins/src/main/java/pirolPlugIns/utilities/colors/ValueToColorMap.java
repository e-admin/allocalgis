/*
 * Created on 14.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: ValueToColorMap.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:05 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/colors/ValueToColorMap.java,v $
 */
package pirolPlugIns.utilities.colors;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import com.vividsolutions.jump.util.Range;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.ColorThemingStyle;

import pirolPlugIns.PirolPlugInSettings;
import pirolPlugIns.utilities.CollectionsTools;
import pirolPlugIns.utilities.HandlerToMakeYourLifeEasier;
import pirolPlugIns.utilities.ObjectComparator;
import pirolPlugIns.utilities.Properties.PropertiesHandler;
import pirolPlugIns.utilities.colors.Value.ColoredRange;
import pirolPlugIns.utilities.colors.Value.ColoredValue;
import pirolPlugIns.utilities.colors.Value.SingleValue;
import pirolPlugIns.utilities.colors.Value.ValueChangeListener;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

/**
 * A utility class that basically maps attribute values to colors...<br>
 * By default the value range is divided through the number of colors to get an initial mapping. 
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class ValueToColorMap implements HandlerToMakeYourLifeEasier, ValueChangeListener {
    
    protected static PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    public final static String storedColoringDirectory = "value2ColorMaps" + File.separator;
    
    protected HashMap value2Color = new HashMap();
    protected ColoredValue[] valuesInMap = null;
    
    protected int numberOfColors = 32;
    protected ColorGenerator colorGenerator = null;
    
    protected double minValue, maxValue;
    protected Object[] givenKeyObjects = null;
    protected boolean numericValues = true;
    
    protected ArrayList mapChangeListener = new ArrayList();

    protected boolean isFiringEvents = true;

    public ValueToColorMap(int numColors, double minVal, double maxVal) {
        super();
        numberOfColors = numColors;
        minValue = minVal;
        maxValue = maxVal;
    }
    
    public ValueToColorMap(Object[] values) {
        super();
        this.numericValues = false;
        this.numberOfColors = values.length;
        this.givenKeyObjects = values;
    }
    
    /**
     * load stored ValueToColorMap
     *@param propsHandler handle of the properties file containingthe value2Color map.
     */
    public ValueToColorMap(PropertiesHandler propsHandler) {
        super();
        
        if (propsHandler.isEmpty()){
            try {
                propsHandler.load();
            } catch (IOException e) {
                logger.printWarning(e.getMessage());
            }
        }
        
        String[] keyStrings = (String[])propsHandler.keySet().toArray(new String[0]);
        String key;
        ColoredValue value;
        Color color;
        
        ArrayList keyList = new ArrayList();
        
        boolean numeric = true;
        
        logger.printDebug("keys found: " + keyStrings.length, false);
        
        for (int i=0; i<keyStrings.length; i++){
            key = keyStrings[i];
            
            value = ColoredValue.readParseableString(key);
            
            if (value == null){
                logger.printError("value could not be parsed: " + key);
            }
            
            numeric = numeric && value.isNumeric();
            
            if (value.isNumeric()){
                if (ColoredRange.class.isInstance(value)){
                    if (this.minValue > ((ColoredRange)value).getMinValue())
                        this.minValue = ((ColoredRange)value).getMinValue();
                    else if (this.maxValue < ((ColoredRange)value).getMaxValue())
                        this.maxValue = ((ColoredRange)value).getMaxValue();
                } else if (SingleValue.class.isInstance(value)){
                    double val = ObjectComparator.getDoubleValue(((SingleValue)value).getValue());
                    if (this.minValue > val)
                        this.minValue = val;
                    else if (this.maxValue < val)
                        this.maxValue = val;
                }
            }
            
            this.numericValues = numeric;
            
            value.addValueChangeListener(this);
            color = propsHandler.getPropertyAsColor(key);
            
            this.put(value, color);
            keyList.add(value);
            
        }
        
        Collections.sort(keyList);
        this.valuesInMap = (ColoredValue[])keyList.toArray(new ColoredValue[0]);
        this.numberOfColors = keyStrings.length;
        this.getColorGenerator();
        
    }
    
    public static File getColoringDir(){
        logger.printDebug(PirolPlugInSettings.configDirectory().getName() + File.separator + ValueToColorMap.storedColoringDirectory);
        return new File(PirolPlugInSettings.configDirectory().getName() + File.separator + ValueToColorMap.storedColoringDirectory);
    }
    
    public ColoredValue[] getKeysArray(){
        return this.valuesInMap;        
    }
    
    public HashMap getValueToColor() {
        return value2Color;
    }
    public void setValueToColor(HashMap color2value) {
        this.value2Color = color2value;
    }
    public int getNumberOfColors() {
        return numberOfColors;
    }
    
    /**
     * set up the range of values, we want to create a color table for...
     *@param min
     *@param max
     */
    public void setValueRange( double min, double max ){
        if (this.numericValues){
            this.minValue = Math.min(min,max);
            this.maxValue = Math.max(min,max);
            this.rebuildColors(false);
        } else {
            logger.printWarning("range was set, even though the map does not use numeric values");
        }
    }
    
    public void setNumberOfColors(int numberOfColors) {
        this.numberOfColors = numberOfColors;
        this.colorGenerator.setSteps(this.numberOfColors);
        this.rebuildColors(false);
    }
    
    
    public ColorGenerator getColorGenerator() {
        if (this.colorGenerator == null && this.numberOfColors>0){
            // create default ColorGenerator, if there is none, yet...
            
            ColoredValue[] keys = this.getKeysArray();
            
            if (keys!=null && keys.length > 0){
                if (this.numberOfColors >= 3)
                    this.colorGenerator = new ColorGenerator(this.numberOfColors, new Color[]{this.getColorFor(keys[0]), this.getColorFor(keys[(int)Math.floor(keys.length/2.)]), this.getColorFor(keys[keys.length-1])});
                else if (this.numberOfColors == 2)
                    this.colorGenerator = new ColorGenerator(this.numberOfColors, new Color[]{this.getColorFor(keys[0]), this.getColorFor(keys[keys.length-1])});
                else if (this.numberOfColors == 1)
                    this.colorGenerator = new ColorGenerator(this.numberOfColors, new Color[]{this.getColorFor(keys[0])});
                
                for (int i=0; i<keys.length; i++){
                    this.colorGenerator.setColor(i, this.getColorFor(keys[i]));
                }
            } else {
                this.colorGenerator = ColorGenerator.getGreenToRedColors(this.numberOfColors);
            }
            
        }
        return this.colorGenerator;
    }
    
    public void setColorGenerator(ColorGenerator colorGenerator) {
        if (colorGenerator != this.colorGenerator){
            boolean justNewColors = this.numberOfColors == colorGenerator.getSteps();
            this.colorGenerator = colorGenerator;
            this.numberOfColors = this.colorGenerator.getSteps();
            this.rebuildColors(justNewColors);
        }
    }
    
    public void rebuildColors(boolean justNewColors){
        this.isFiringEvents = false;

        this.clear();
        
        if (this.colorGenerator == null){
            this.colorGenerator = this.getColorGenerator();
        }
        
        if (!justNewColors){
            if (this.numericValues){
                double range = this.maxValue - this.minValue;
                double valueStep = range / this.numberOfColors;
                
                double lowerVal = this.minValue;
                double upperVal;
                ColoredRange colRange;
                for (int i=0; i<this.numberOfColors; i++){
                    if (i < this.numberOfColors -1)
                        upperVal = lowerVal + valueStep;
                    else
                        upperVal = this.maxValue;
                    colRange = new ColoredRange(lowerVal, true, upperVal, !(i<this.numberOfColors-1), i);
                    colRange.addValueChangeListener(this);
                    this.put(colRange, this.colorGenerator.getColor(i));
                    lowerVal = upperVal;
                }
            } else {
                SingleValue newVal;
                for (int i=0; i<this.numberOfColors; i++){
                    newVal = new SingleValue(this.givenKeyObjects[i], i);
                    newVal.addValueChangeListener(this);
                    this.put(newVal, this.colorGenerator.getColor(i));               
                }
            }
            
            ArrayList keys = new ArrayList();
            CollectionsTools.addArrayToList(keys, this.value2Color.keySet().toArray());
            Collections.sort(keys);
            if (this.numericValues)
                this.valuesInMap = (ColoredRange[])keys.toArray(new ColoredRange[0]);
            else
                this.valuesInMap = (ColoredValue[])keys.toArray(new ColoredValue[0]);
            this.isFiringEvents = true;
        } else if (justNewColors) {
            for (int i=0; i<this.numberOfColors; i++){
                this.put(this.valuesInMap[i], this.colorGenerator.getColor(i));
            }
        }
        
        
        this.fireValueToColorMapChangeEvent();
    }
    
    
    public void clear() {
        value2Color.clear();
        this.fireValueToColorMapChangeEvent();
    }
    public boolean containsKey(ColoredValue key) {
        return value2Color.containsKey(key);
    }
    public boolean containsColor(Color color) {
        return value2Color.containsValue(color);
    }
    public Color getColorFor(ColoredValue key) {
        return (Color)value2Color.get(key);
    }
    public Color getColorFor(double value) {
        ColoredValue key = this.getColoredValueFor(value);
        return (Color)value2Color.get(key);
    }
    public ColoredValue getColoredValueFor(double val){
        for (int i=0; i<this.numberOfColors; i++){
            if (this.valuesInMap[i].isResponsibleForValue(val))
                return this.valuesInMap[i];
            //logger.printDebug(this.valuesInMap[i].toString());
        }
        logger.printWarning("value not found for " + val, false);
        return null;
    }
    
    public ColoredValue getColoredValueFor(Object val){
        if (this.numberOfColors > this.valuesInMap.length)
            logger.printDebug("this.numberOfColors > this.valuesInMap.length: " + this.numberOfColors +", "+ this.valuesInMap.length);
        for (int i=0; i<this.numberOfColors; i++){
            if (this.valuesInMap[i].isResponsibleForValue(val))
                return this.valuesInMap[i];
            //logger.printDebug(this.valuesInMap[i].toString());
        }
        logger.printWarning("value not found for " + val);
        return null;
    }
    
    public boolean isEmpty() {
        return value2Color.isEmpty();
    }
    public Set keySet() {
        return value2Color.keySet();
    }
    public Object remove(ColoredValue key) {
        Object obj = value2Color.remove(key);
        this.fireValueToColorMapChangeEvent();
        return obj;
    }
    public Object put(ColoredValue value, Color color) {
        Object obj = value2Color.put(value, color);
        this.fireValueToColorMapChangeEvent();
        return obj;
    }

    /**
     *@return the highest value of the given value range (to be divided into n classes)
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     *@return the lowest value of the given value range (to be divided into n classes)
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     *@param changedObject
     */
    public void valueChanged(ColoredValue changedObject) {
        if (this.numericValues){
        this.isFiringEvents = false;
            ColoredRange changedRange = (ColoredRange)changedObject;
            ColoredRange prev = (ColoredRange)this.getPreviousColoredValueInMap(changedRange);
            if (prev != null && prev.getMaxValue() > changedRange.getMinValue())
                prev.setMaxValue(changedRange.getMinValue());
            ColoredRange next = (ColoredRange)this.getNextColoredValueInMap(changedRange);
            if (next != null && next.getMinValue() < changedRange.getMaxValue())
                next.setMinValue(changedRange.getMaxValue());
            this.isFiringEvents = true;
        }
        this.fireValueToColorMapChangeEvent();
    }
    
    protected ColoredValue getNextColoredValueInMap(ColoredRange value){
        if (value.getIndex() < this.valuesInMap.length-1 && value.getIndex() > -1)
            return this.valuesInMap[value.getIndex()+1];
        return null;
    }
    
    protected ColoredValue getPreviousColoredValueInMap(ColoredRange value){
        if (value.getIndex() > 0)
            return this.valuesInMap[value.getIndex()-1];
        return null;
    }
    
    public void addValueToColorMapChangeListener(ValueToColorMapChangeListener listener){
        this.mapChangeListener.add(listener);        
    }
    
    public void removeValueToColorMapChangeListener(ValueToColorMapChangeListener listener){
        this.mapChangeListener.remove(listener);        
    }
    
    protected void fireValueToColorMapChangeEvent(){
        if (isFiringEvents ){
            ValueToColorMapChangeListener[] listener = (ValueToColorMapChangeListener[])this.mapChangeListener.toArray(new ValueToColorMapChangeListener[0]);
            for (int i=0; i<listener.length; i++){
                listener[i].valueToColorMapChanged(this);
            }
        }
    }

    public boolean isNumericValues() {
        return numericValues;
    }

    public void setNumericValues(boolean numericValues) {
        this.numericValues = numericValues;
    }
    
    /**
     * Store the current coloring schema represented by this ValueToColorMap to a properties file.
     *@param propsHandler properties file handler
     *@return true, if it worked, false if not
     */
    public boolean storeToFile(PropertiesHandler propsHandler){
        ColoredValue[] keys = this.getKeysArray();
        String keyStr;
        
        for ( int i=0; i<keys.length; i++){
            keyStr = keys[i].toParseableString();
            propsHandler.setProperty(keyStr, this.getColorFor(keys[i]));
        }
        
        try {
            propsHandler.store();
        } catch (IOException e) {
            logger.printError(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * sets the <code>nr</code>th color to the given value
     *@param nr index of the color to be changed
     *@param color color to be used in future
     */
    public void setColor(int nr, Color color){
        if (nr>-1 && nr<this.getKeysArray().length){
            this.setColorForValue(this.getKeysArray()[nr], color);
            this.colorGenerator.setColor(nr, color);
        }
    }
    
    public void setColorForValue(ColoredValue value, Color color){
        if (this.containsKey(value)){
            this.remove(value);
            this.put(value, color);
        }
    }
    
    public boolean applyValue2ColorMapToLayer(Layer layer, String attribute, int verticesSize){
        return ValueToColorMap.applyValue2ColorMapToLayer(layer, attribute, this, verticesSize);
    }
    
    public static boolean applyValue2ColorMapToLayer(Layer layer, String attribute, ValueToColorMap v2cMap, int verticesSize){
        
        BasicStyle newStyle;
        BasicStyle defaultStyle = (BasicStyle)layer.getBasicStyle().clone();
        
        Range.RangeTreeMap styleMap = new  Range.RangeTreeMap();
        
        Range range = null;
        ColoredRange colRange = null;
        
        ColoredValue[] keys = v2cMap.getKeysArray();
        
        for (int i=0; i<keys.length; i++){
            newStyle = new BasicStyle(v2cMap.getColorFor(keys[i]));
            newStyle.setAlpha(defaultStyle.getAlpha());
            newStyle.setLineColor(v2cMap.getColorFor(keys[i]).darker());
            newStyle.setLineWidth(verticesSize);
            if (ColoredRange.class.isInstance(keys[i])){
                colRange = (ColoredRange)keys[i];
                range = new Range(new Double(colRange.getMinValue()), colRange.isIncludingMin(), new Double(colRange.getMaxValue()), colRange.isIncludingMax());
                styleMap.put(range, newStyle);
            } else if (SingleValue.class.isInstance(keys[i])){
                styleMap.put(((SingleValue)keys[i]).getValue(), newStyle);
            }
        }
        
        if (ColorThemingStyle.get(layer)!=null){
            layer.removeStyle(ColorThemingStyle.get(layer));
        }
        
        ColorThemingStyle cts =  new ColorThemingStyle(attribute, styleMap,defaultStyle);
        
        //cts.setAttributeName(attribute);

        cts.setEnabled(true);   
        layer.addStyle(cts);
        
        BasicStyle bs = layer.getBasicStyle();
        bs.setEnabled(false);
        
        layer.fireAppearanceChanged();
        
        return true;
    }
}
