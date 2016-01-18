/*
 * Created on 10.03.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: StandardPirolPolygonTool.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:56 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/cursorTools/StandardPirolPolygonTool.java,v $
 */
package pirolPlugIns.cursorTools;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import pirolPlugIns.PirolPlugInSettings;
import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.plugIns.StandardPirolPlugIn;
import pirolPlugIns.utilities.LayerTools;
import pirolPlugIns.utilities.FeatureCollection.RoleOutline;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.cursortool.PolygonTool;

/**
 * 
 * TODO: comment class
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
public abstract class StandardPirolPolygonTool extends PolygonTool implements
        ErrorHandler {
    
    public ImageIcon icon = null;
    
    protected StringBuffer messages = new StringBuffer();
    protected boolean bringUpMessages = false;
    
    /**
     * @deprecated use logger instead!
     */
    private boolean debug = false;
    
    protected boolean useToolIconSize = true;
    
    protected PersonalLogger logger = null;

    protected abstract void gestureFinished() throws Exception;
    
    /**
     * If this method is overridden by a derived class, it may return null,
     * if no icon is needed for the derived plugin/tool. Otherwise it returns the
     * filename (in the same directory/package as the implementing class)
     * without path information to enable the getIcon() method. This also
     * works within jar files and handles all related file system issues.
     *@return the icon's filename without path information
     */
    protected abstract String getIconString();
    
    public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        MyEnableCheckFactory checkFactory = new MyEnableCheckFactory(workbenchContext);
        MultiEnableCheck multiEnableCheck = new MultiEnableCheck();
        
        multiEnableCheck.add( checkFactory.createAtLeastNLayersMustExistCheck(1) );

        return multiEnableCheck;
	}

    public static Layer putPolygonIntoMap(Polygon p, PlugInContext context, String title, Color color){
        FeatureSchema fs = new FeatureSchema();
        fs.addAttribute(PirolPlugInMessages.getString("geometry"), AttributeType.GEOMETRY);
        
        Feature feature = new BasicFeature(fs);
        //feature.setGeometry(p.getExteriorRing());
        
        feature.setGeometry(p);
        
        FeatureCollection fc = new FeatureDataset(fs);
        fc.add(feature);

        
        Layer layer = LayerTools.addStandardResultLayer(title, fc, color, context, new RoleOutline());
        layer.getBasicStyle().setFillColor(Color.yellow.brighter().brighter());
        layer.getBasicStyle().setRenderingFill(false);
        
        return layer;
    }

    public Icon getIcon() {
        
        if ( icon == null && this.getIconString() != null){
            InputStream in = this.getClass().getResourceAsStream(this.getIconString());
            BufferedImage img = null;
            Image scaledImg = null;
            try {
                img = ImageIO.read(in);
                scaledImg = img.getScaledInstance(this.useToolIconSize?PirolPlugInSettings.StandardToolIconWidth:PirolPlugInSettings.StandardPlugInIconWidth, this.useToolIconSize?PirolPlugInSettings.StandardToolIconHeight:PirolPlugInSettings.StandardPlugInIconHeight, img.getType());
            } catch (IOException e) {
                e.printStackTrace();
                img = null;
                icon = null;
            }
            if (scaledImg != null){
                icon = new ImageIcon(scaledImg);
            }
        }
        return icon;
    }

    public boolean isDebug() {
        return debug;
    }
    protected void setDebug(boolean debug) {
        this.debug = debug;
    }
    /**
     *@param function
     *@param message
     *@deprecated use logger instead!
     */
    protected void println( String function,  String message ){
        if (this.isDebug())
            StandardPirolPlugIn.println( this.getClass(), function, message );
            //System.out.println( this.getClass().getName() + "." + function + "(): " + msg );
	}
    
    /**
	 * Writes the given message and function string to the stdout.
	 * The output will be formated as "classname.function: message".
	 * @param c the calling class
	 * @param function the method from within this one is called.
	 * @param message the text with some useful information.
	 * @deprecated use logger instead!
	 */
	protected void println( Class c, String function, String message ){
	    if (this.isDebug())
	        StandardPirolPlugIn.println( c, function, message );
	}
	
	protected static void warnUser(PlugInContext context, String msg){
	    StandardPirolPlugIn.warnUser(context, msg);
	}
    
    protected boolean finishExecution( PlugInContext context, boolean retVal ){
	    this.postMessagesToGui(context);
	    return retVal;
	}
	/**
	 * Show messages (e.g. from Exceptions during execution) to the user.
	 * The parameter bringUpMessages decides if they are show in the status
	 * bar (false) or in an extra output window (true).
	 *@param context current PlugIn context
	 */
	protected void postMessagesToGui(PlugInContext context){
	    if (messages.length()!=0 && context!=null){
			context.getOutputFrame().createNewDocument();
			context.getOutputFrame().addText( messages.toString() );
			if (bringUpMessages)
			    context.getOutputFrame().surface();
			messages.delete(0,messages.length());
		} else if (context==null){
		    this.println( "postMessagesToGui", "-> context == null!");
		}
	}
	
	public void handleThrowable(Throwable t) {
        if (t.getMessage()==null || t.getMessage().equals("null")){
            StackTraceElement[] stre = t.getStackTrace();
            for (int i=0; i<stre.length; i++){
                messages.append(stre[i].toString() + "\n");
            }
        } else if (t.getMessage()!=null){
            messages.append(t.getMessage() + "\n");
        }
    }
	

    /**
     *@return the personal logger
     *@see PersonalLogger
     */
    public PersonalLogger getLogger() {
        return logger;
    }
    /**
     * Sets the personal logger - enables debug statement handling
     *@param logger the new personal logger
     *@see PersonalLogger
     */
    public void setLogger(PersonalLogger logger) {
        this.logger = logger;
    }

    
    /**
     * Name of the Tool to be shown in the menus or as a tooltip in JUMP.<br>
     * Looks for a key (the PlugIn's name with no path) in the i18n resources, if none is found
     * the standard jump name generation will be used.
     */
    public String getName() {
        try {
            return PirolPlugInMessages.getString(this.getShortClassName());
        } catch (RuntimeException e) {
            return super.getName();
        }
        
    }
    
    public String getShortClassName(){
        int pointPos = this.getClass().getName().lastIndexOf(".");
        if (pointPos > -1) {
            return this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1);
        }
        this.logger.printWarning("class name is very short: " + this.getClass().getName());
        return this.getClass().getName();
    }

}
