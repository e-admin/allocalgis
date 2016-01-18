/*
 * Created on 16.08.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: StandardPirolDragTool.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:56 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/cursorTools/StandardPirolDragTool.java,v $
 */
package pirolPlugIns.cursorTools;

import java.awt.Image;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import pirolPlugIns.PirolPlugInSettings;
import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.plugIns.StandardPirolPlugIn;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.cursortool.DragTool;

/**
 * Standard CursorTool with specuial support for mouse dragging in the PIROL context, offers standard implementations for most purposes.
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
public abstract class StandardPirolDragTool extends DragTool implements ErrorHandler {


    protected ImageIcon icon = null;
    
    /**
     * the icon's filename (in the same directory/package as the derived class)
     * without path information to enable the getIcon() method. This also
     * works within jar files and handles all related file system issues.
     */
    protected String iconString = "" ;
    
    protected boolean active = false;
    
    protected StringBuffer messages = new StringBuffer();
    protected boolean bringUpMessages = false;
    
    protected PersonalLogger logger = null;
    protected boolean useToolIconSize = true;
    
    /**
     * if true a line will be drawn between the point where the mouse was pressed and it's current position, if false
     * the Stadard DragTool behavior is used. 
     */
    protected boolean useSimpleLineDragging = false;
    
    /**
     *@param logger the PersonalLogger to be used with this tool
     *@param iconString file name of the icon
     */
    public StandardPirolDragTool(PersonalLogger logger, String iconString) {
        super();
        this.iconString = iconString;
        this.logger = logger;
    }
    
    public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        MyEnableCheckFactory checkFactory = new MyEnableCheckFactory(workbenchContext);
        MultiEnableCheck multiEnableCheck = new MultiEnableCheck();
        
        multiEnableCheck.add( checkFactory.createAtLeastNLayersMustExistCheck(1) );
        multiEnableCheck.add( checkFactory.createExactlyNLayersMustBeSelectedCheck(1) );
        //multiEnableCheck.add( checkFactory.createFenceMustBeDrawnCheck() );
        return multiEnableCheck;
    }

    public void activate(ILayerViewPanel layerViewPanel) {
        super.activate(layerViewPanel);
        this.active = true;
    }
    public void deactivate() {
        
        try {
            super.deactivate();
        } catch (RuntimeException e) {
            this.logger.printWarning(e.getMessage());
        }
        this.active = false;
    }

    public Icon getIcon() {
        
        if ( icon == null && this.iconString != null && this.iconString.length()!=0){
            InputStream in = this.getClass().getResourceAsStream(this.iconString);
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
    
    protected void setActive(boolean active) {
        this.active = active;
    }
    protected boolean isActive(){
        //return (context.getLayerViewPanel().getCurrentCursorTool().getName().equals(this.getName()) || context.getLayerViewPanel().getCurrentCursorTool().equals(this));
        return this.active;
    }
    
    
    protected static void warnUser(PlugInContext context, String msg){
        StandardPirolPlugIn.warnUser(context, msg);
    }
    
    protected boolean finishExecution( PlugInContext context, boolean retVal ){
        this.postMessagesToGui(context);
        return retVal;
    }
    
    protected void postMessagesToGui(PlugInContext context){
        if (messages.length()!=0 && context!=null){
            context.getOutputFrame().createNewDocument();
            context.getOutputFrame().addText( messages.toString() );
            if (bringUpMessages)
                context.getOutputFrame().surface();
            messages.delete(0,messages.length());
        } else if (context==null){
            this.logger.printError("-> context == null!");
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

    public void mousePressed(MouseEvent e) {
        if (!this.isActive()) return;
        this.logger.printDebug("pressed");
        try {
            super.mousePressed(e);
        } catch (RuntimeException e1) {
            this.logger.printWarning(e1.getMessage());
        }
    }

    

    public void mouseDragged(MouseEvent e) {
        if (!this.isActive()) return;
        super.mouseDragged(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (!this.isActive()) return;
        try {
            super.mouseReleased(e);
        } catch (RuntimeException e1) {
            this.logger.printWarning(e1.getMessage());
        }
    }


    /**
     *@return    null if nothing should be drawn
     */
    protected Shape getShape(Point2D source, Point2D destination)
        throws Exception {
        
        this.logger.printDebug(source + ", " + destination, false);
        
        if (this.useSimpleLineDragging)
            return new Line2D.Double(source, destination);
        
        return super.getShape(source, destination);
    }

    
    public void cancelGesture() {
        try {
            super.cancelGesture();
        } catch (RuntimeException e) {
        }
        
        this.setActive(false);        
    }

    /**
     *@inheritDoc
     */
    protected abstract void gestureFinished() throws Exception;

}
