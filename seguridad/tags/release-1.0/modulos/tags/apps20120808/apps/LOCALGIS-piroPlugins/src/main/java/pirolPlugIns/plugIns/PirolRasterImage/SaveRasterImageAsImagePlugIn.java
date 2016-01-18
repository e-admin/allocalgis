/*
 * Created on 29.06.2005 for Pirol
 *
 * CVS header information:
 * $RCSfile: SaveRasterImageAsImagePlugIn.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:31:31 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/plugIns/PirolRasterImage/SaveRasterImageAsImagePlugIn.java,v $
 */
package pirolPlugIns.plugIns.PirolRasterImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import pirolPlugIns.cursorTools.MyEnableCheckFactory;
import pirolPlugIns.plugIns.StandardPirolPlugIn;
import pirolPlugIns.utilities.LayerTools;
import pirolPlugIns.utilities.RasterImageSupport.RasterImageLayer;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.sun.media.jai.codec.TIFFEncodeParam;
import com.sun.media.jai.codecimpl.TIFFCodec;
import com.sun.media.jai.codecimpl.TIFFImageEncoder;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * This PlugIn saves a RasterImages to disk with its geographical position.
 * This class is based on Stefan Ostermanns SaveInterpolationAsImagePlugIn.
 * 
 * @author Ole Rahn, Stefan Ostermann,
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class SaveRasterImageAsImagePlugIn extends StandardPirolPlugIn {
    protected static final String TIFENDING = ".tif";
	protected static final String GEOENDING = ".tfw";
	
	private static PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    private Properties properties = null;
    private static String propertiesFile = RasterImagePlugIn.getPropertiesFile();
    private String lastPath;
	
    public SaveRasterImageAsImagePlugIn(){
        super(SaveRasterImageAsImagePlugIn.logger);
    }
    
	/**
	 *@inheritDoc
	 */
	public boolean execute(PlugInContext context) throws Exception {
		BufferedImage image;
		/* standard Java save-dialog: */
		JFileChooser fc = new JFileChooser();
		
		fc.setFileFilter(new FileFilter() {
				            public boolean accept(File f) {
				                return f.isDirectory()
				                        || f.getName().toLowerCase().endsWith(TIFENDING);
				            }
				
				            public String getDescription() {
				                return "TIFF Image";
				            }
				        }
					);
		
		this.properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(SaveRasterImageAsImagePlugIn.propertiesFile);
            this.properties.load(fis);
            this.lastPath = this.properties.getProperty(RasterImagePlugIn.KEY_PATH);
            fis.close();
        } catch (FileNotFoundException e) {
            SaveRasterImageAsImagePlugIn.logger.printDebug(e.getMessage());
        } catch (IOException e) {
            SaveRasterImageAsImagePlugIn.logger.printDebug(e.getMessage());
        }
        
        if (this.lastPath != null){
            fc.setCurrentDirectory(new File(this.lastPath));
        }
        fc.setMultiSelectionEnabled(false);
		
		fc.setDialogTitle(this.getName());
		int returnVal = fc.showSaveDialog(fc);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String tifFileName = fc.getSelectedFile().getAbsolutePath();
			
			if (!tifFileName.toLowerCase().endsWith(TIFENDING.toLowerCase())){
			    tifFileName = tifFileName + TIFENDING;
			}
			
			File tifFile = new File(tifFileName);

			FileOutputStream tifOut = new FileOutputStream(tifFile);

			
			/* save tif image: */
            RasterImageLayer rLayer = (RasterImageLayer) LayerTools.getSelectedLayerable(context, RasterImageLayer.class); 
			image = rLayer.getImage().getAsBufferedImage();
			TIFFEncodeParam param = new TIFFEncodeParam();
			param.setCompression(TIFFEncodeParam.COMPRESSION_NONE);
			TIFFImageEncoder encoder = (TIFFImageEncoder) TIFFCodec.createImageEncoder("tiff", tifOut, param);
			encoder.encode(image);
			tifOut.close();
			
			/* save geodata: */
			Envelope envelope = rLayer.getEnvelope();


			WorldFileHandler worldFileHandler = new WorldFileHandler(tifFileName, false);
			worldFileHandler.writeWorldFile(envelope, image.getWidth(), image.getHeight());
	        
	        // Switch RAM mode of the RasterImage
            rLayer.setImageFileName(tifFileName);
            rLayer.setNeedToKeepImage(false);
	        
	        
		}
		return true;

	}

	/**
	 *@inheritDoc
	 */
	public void initialize(PlugInContext context) throws Exception {}
	
	public static MultiEnableCheck createEnableCheck(
			final WorkbenchContext workbenchContext) {
		MyEnableCheckFactory checkFactory = new MyEnableCheckFactory(
				workbenchContext);
		MultiEnableCheck multiEnableCheck = new MultiEnableCheck();

        multiEnableCheck.add( checkFactory.createExactlyNLayerablesMustBeSelectedCheck(1, RasterImageLayer.class) );

		return multiEnableCheck;
	}
	
    /**
     *@inheritDoc
     */
    public String getIconString() {
        return null;
    }
}
