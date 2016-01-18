/**
 * SaveImageAsAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 22.06.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: SaveImageAsAction.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/SaveImageAsAction.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.sun.media.jai.codec.PNGEncodeParam;
import com.sun.media.jai.codecimpl.PNGCodec;
import com.sun.media.jai.codecimpl.PNGImageEncoder;

/**
 * Generic action (to be added to GUI controlls such as buttons) to save the contents of an AWT component to an image file.
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
public class SaveImageAsAction extends AbstractAction {

    private static final long serialVersionUID = -3490102728600683489L;
    protected Component component2Draw = null;
    protected Dialog parentDialog;
    protected PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    

    public SaveImageAsAction(Dialog parentDialog, Component component2Draw) {
        super(PirolPlugInMessages.getString("save-image"));
        this.component2Draw = component2Draw;
        this.parentDialog = parentDialog;
        
        this.putValue(AbstractAction.NAME, PirolPlugInMessages.getString("save-image"));
        this.putValue(AbstractAction.SHORT_DESCRIPTION, PirolPlugInMessages.getString("save-image-tooltip"));
    }

    /**
     *@param titleText
     */
    public SaveImageAsAction(Dialog parentDialog, Component component2Draw, String titleText) {
        super(titleText);
        this.component2Draw = component2Draw;
        this.parentDialog = parentDialog;
        
        this.putValue(AbstractAction.NAME, titleText);
        this.putValue(AbstractAction.SHORT_DESCRIPTION, PirolPlugInMessages.getString("save-image-tooltip"));
    }

    protected BufferedImage getScreenImage() throws AWTException{
        Robot robot = new Robot();
        Point location = this.component2Draw.getLocationOnScreen();
        
        Rectangle visibleRect = new Rectangle(location, this.component2Draw.getSize());
        
        return robot.createScreenCapture(visibleRect);
    }
    
    /**
     * Defines what is supposed to happen if the control using this action is activated (e.g. clicked if it's a button)
     *@param actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        BufferedImage bim = null;
        
        try {
            if (!javax.media.j3d.Canvas3D.class.isInstance(this.component2Draw)){
                bim = new BufferedImage(this.component2Draw.getWidth(), this.component2Draw.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D grph = bim.createGraphics();
                this.component2Draw.paint(grph);
                grph.dispose();
            } else {
                try {
                    bim = this.getScreenImage();
                } catch (AWTException e1) {
                    this.logger.printError(e1.getMessage());
                }
            }
        } catch (Throwable e) {
            // javax.media.j3d.Canvas3D not found -> no Java3D -> it is a 2D component
            try {
                bim = this.getScreenImage();
            } catch (AWTException e1) {
                this.logger.printError(e1.getMessage());
            }
        }
        
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(
                new FileFilter(){
                    public boolean accept(File f) {
					    if (f.isDirectory()) {
					        return true;
					    }
					
					    String extension = "";
					    if (f.getName().lastIndexOf(".")>-1) {
					        extension = f.getName().substring(f.getName().lastIndexOf("."));
						    if (extension != null) {
								if (extension.toLowerCase().equals(".png")) {
								        return true;
								}
							    return false;
						    }
					    } 
					    return false;
                    }

                    public String getDescription() {
                        return "*.png";
                    }
                });
        int result = chooser.showSaveDialog(this.parentDialog);
        
        if (result != JFileChooser.APPROVE_OPTION) return;
        
        String fileName = chooser.getSelectedFile().getAbsolutePath();
        
        if (!fileName.toLowerCase().endsWith(".png")){
            fileName += ".png";
        }
        
        try {
            FileOutputStream imgOut = new FileOutputStream(fileName);
            
            PNGEncodeParam param = new PNGEncodeParam.RGB();

            PNGImageEncoder encoder = (PNGImageEncoder) PNGCodec.createImageEncoder("png", imgOut, param);
            encoder.encode(bim);
            imgOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

 
}
