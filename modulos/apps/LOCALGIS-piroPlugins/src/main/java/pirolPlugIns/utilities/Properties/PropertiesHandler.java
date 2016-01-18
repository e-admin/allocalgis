/**
 * PropertiesHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 12.07.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: PropertiesHandler.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:06 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/Properties/PropertiesHandler.java,v $
 */
package pirolPlugIns.utilities.Properties;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

import pirolPlugIns.PirolPlugInSettings;
import pirolPlugIns.utilities.HandlerToMakeYourLifeEasier;

/**
 * Class that enables easy access for reading and writing properties files.
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
public class PropertiesHandler implements HandlerToMakeYourLifeEasier {
    
    protected String propertiesFile = null;
    protected Properties properties = null;
    
    public final static String propertiesFileEnding = ".properties";
    
    /**
     * Constructor
     *@param propertiesFileName the file name (with out path!) of the properties file, that is to be read or written. It will automatically be placed in the config directory.
     *@see PirolPlugInSettings#configDirectory()
     */
    public PropertiesHandler(String propertiesFileName){
        this.propertiesFile = PirolPlugInSettings.configDirectory().getPath() + File.separator + propertiesFileName;
        this.properties = new Properties();
    }

    /**
     *@see Properties
     */
    public boolean contains(Object value) {
        return properties.contains(value);
    }
    /**
     *@see Properties
     */
    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }
    /**
     *@see Properties
     */
    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }
    /**
     *@see Properties
     */
    public Enumeration elements() {
        return properties.elements();
    }
    /**
     *@see Properties
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    /**
     *@see Properties
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    /**
     *@see Properties
     */
    public boolean isEmpty() {
        return properties.isEmpty();
    }
    /**
     *@see Properties
     */
    public Enumeration keys() {
        return properties.keys();
    }
    /**
     *@see Properties
     */
    public Set keySet() {
        return properties.keySet();
    }

    /**
     * load the properties from the file
     *@throws IOException
     */
    public void load() throws IOException {
        if (!this.properties.isEmpty()){
            this.properties.clear();
        }
        FileInputStream fis = new FileInputStream(this.propertiesFile);
        this.properties.load(fis);
        fis.close();
    }
    /**
     *@see Properties
     */
    public void putAll(Map arg0) {
        properties.putAll(arg0);
    }
    /**
     *@see Properties
     */
    public Object remove(Object arg0) {
        return properties.remove(arg0);
    }
    /**
     * Sets a property key-value pair, replaces a pair with the same key!
     *@param key the key for the pair
     *@param value the value
     *@return return value like Properties would return
     *@see Properties
     */
    public Object setProperty(String key, String value) {
        if (this.properties.containsKey(key))
            this.properties.remove(key);
        return properties.setProperty(key, value);
    }
    
    /**
     * Sets a property key-value pair, replaces a pair with the same key!
     *@param key the key for the pair
     *@param value the value
     *@return return value like Properties would return
     *@see Properties
     */
    public Object setProperty(String key, Color value) {
        if (this.properties.containsKey(key))
            this.properties.remove(key);
        return properties.setProperty(key, Integer.toString( value.getRGB()));
    }

    /**
     * Stores the current properties map to the file.
     *@param comments comments that will appear in the first lines of the file
     *@throws IOException
     */
    public void store(String comments) throws IOException {
        File propFile = new File(this.propertiesFile);
        
        String pathString = this.propertiesFile.indexOf(File.separator)>-1?this.propertiesFile.substring(0, this.propertiesFile.lastIndexOf(File.separator)):"";
        
        File propFilePath = new File(pathString);
        if (!propFile.exists()){
            try {
                propFilePath.mkdirs();
            } catch (RuntimeException e) { }
        }
        
        // TODO: find a way store the properties in alphabetical order (keys)        
        
        FileOutputStream fos = new FileOutputStream(propFile);
        this.properties.store(fos, comments);
        fos.close();
    }
    
    /**
     * Stores the current properties map to the file.
     *@throws IOException
     */
    public void store() throws IOException {
        this.store(null);
    }
    
    /**
     * Gets the property value with the key <code>key</code> and parses it
     * to an <code>int</code> if possible. An exception will be thrown,
     * if this key is not found within the properties and if the value could not
     * be parsed as desired.
     *@param key the key to get the value of
     *@return the value of the property
     */
    public int getPropertyAsInt(String key){
        if (this.properties.containsKey(key)){
            return Integer.parseInt( this.properties.get(key).toString() );
        }
        throw new NoSuchElementException("key: \"" + key + "\"");
    }
    
    /**
     * Gets the property value with the key <code>key</code> and parses it
     * to an <code>int</code> if possible. 
     * If this key is not found within the properties the given default-Value will be returned. 
     * An exception will be thrown, if the value is existent, but could not be parsed as desired.
     *@param key the key to get the value of
     *@param defaultValue value to be filled in, if the given key wasn't found
     *@return the value of the property
     */
    public int getPropertyAsInt(String key, int defaultValue){
        if (this.properties.containsKey(key)){
            return this.getPropertyAsInt(key);
        }
        this.properties.setProperty(key, new Integer(defaultValue).toString() );
        return defaultValue;
    }
    
    /**
     * Gets the property value with the key <code>key</code> and parses it
     * to an <code>boolean</code> if possible. An exception will be thrown,
     * if this key is not found within the properties and if the value could not
     * be parsed as desired.
     *@param key the key to get the value of
     *@return the value of the property
     */
    public boolean getPropertyAsBoolean(String key){
        if (this.properties.containsKey(key)){
            return Boolean.valueOf( this.properties.get(key).toString() ).booleanValue();
        }
        throw new NoSuchElementException("key: \"" + key + "\"");
    }
    
    /**
     * Gets the property value with the key <code>key</code> and parses it
     * to an <code>boolean</code> if possible. 
     * If this key is not found within the properties the given default-Value will be returned. 
     * An exception will be thrown, if the value is existent, but could not be parsed as desired.
     *@param key the key to get the value of
     *@param defaultValue value to be filled in, if the given key wasn't found
     *@return the value of the property
     */
    public boolean getPropertyAsBoolean(String key, boolean defaultValue){
        if (this.properties.containsKey(key)){
            return this.getPropertyAsBoolean(key);
        }
        this.properties.setProperty(key, new Boolean(defaultValue).toString() );
        return defaultValue;
    }
    
    /**
     * Gets the property value with the key <code>key</code> and parses it
     * to a <code>double</code> if possible. An exception will be thrown,
     * if this key is not found within the properties and if the value could not
     * be parsed as desired.
     *@param key the key to get the value of
     *@return the value of the property
     */
    public double getPropertyAsDouble(String key){
        if (this.properties.containsKey(key)){
            return Double.parseDouble( this.properties.get(key).toString() );
        }
        throw new NoSuchElementException("key: \"" + key + "\"");
    }
    
    /**
     * Gets the property value with the key <code>key</code> and parses it
     * to a <code>double</code> if possible. 
     * If this key is not found within the properties the given default-Value will be returned. 
     * An exception will be thrown, if the value is existent, but could not be parsed as desired.
     *@param key the key to get the value of
     *@param defaultValue value to be filled in, if the given key wasn't found
     *@return the value of the property
     */
    public double getPropertyAsDouble(String key, double defaultValue){
        if (this.properties.containsKey(key)){
            return this.getPropertyAsDouble(key);
        }
        this.properties.setProperty(key, new Double(defaultValue).toString() );
        return defaultValue;
    }
    
    /**
     * Gets the property value with the key <code>key</code> and parses it
     * to a <code>Color</code> if possible. An exception will be thrown,
     * if this key is not found within the properties and if the value could not
     * be parsed as desired.
     *@param key the key to get the value of
     *@return the value of the property
     */
    public Color getPropertyAsColor(String key){
        if (this.properties.containsKey(key)){
            return Color.decode( this.properties.get(key).toString() );
        }
        throw new NoSuchElementException("key: \"" + key + "\"");
    }
    
    /**
     * Gets the property value with the key <code>key</code> and parses it
     * to a <code>Color</code> if possible. 
     * If this key is not found within the properties the given default-Value will be returned. 
     * An exception will be thrown, if the value is existent, but could not be parsed as desired.
     *@param key the key to get the value of
     *@param defaultValue value to be filled in, if the given key wasn't found
     *@return the value of the property
     */
    public Color getPropertyAsColor(String key, Color defaultValue){
        if (this.properties.containsKey(key)){
            return this.getPropertyAsColor(key);
        }
        this.setProperty(key, defaultValue );
        return defaultValue;
    }

    /**
     * 
     *@return the file name of the properties file handled by this instance
     */
    public String getPropertiesFile() {
        return propertiesFile;
    }
    
    
}
