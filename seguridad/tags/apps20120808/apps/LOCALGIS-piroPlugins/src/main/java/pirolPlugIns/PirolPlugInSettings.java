/*
 * Created on 30.11.2004
 *
 * CVS header information:
 *  $RCSfile: PirolPlugInSettings.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:57 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/PirolPlugInSettings.java,v $s
 */
package pirolPlugIns;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

/**
 * Holds general information, that have to be available everywhere in the project.
 * 
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class PirolPlugInSettings {
    
    protected static PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    
    private static final String KEY_PIROLMENUNAME = "PirolMenuName";
    
    private static final String KEY_RESULTCATEGORYNAME = "ResultCategoryName";
    
    private static final String KEY_COORDINATEMENUNAME = "CoordinateMenuName";
    private static final String KEY_ATTRIBUTEMENUNAME = "AttributeMenuName";
    private static final String KEY_SELECTIONMENUNAME = "SelectionMenuName";
    private static final String KEY_VISUALTOOLSMENUNAME = "VisualTools";
    private static final String KEY_CONTEXTMENUNAME = "ContextInformation";
    private static final String KEY_TRANSFERMENUNAME = "TransferTools";
    
    public final static int StandardPlugInIconWidth = 16, StandardPlugInIconHeight = 16;
    public final static int StandardToolIconWidth = 24, StandardToolIconHeight = 24;
    
    
    /**
     * Default key to store a workbench context in a blackboard.
     */
    public final static String KEY_WORKBENCHCONTEXT_IN_BLACKBOARD = "workbenchContext";

	public static String getName_PirolMenu(){
		return PirolPlugInMessages.getString(KEY_PIROLMENUNAME);
	}
	
	public static String getName_ProcessingMenu(){
		return "Data Processing";
	}
	
	public static String getName_CoordinateMenu(){
		return PirolPlugInMessages.getString(KEY_COORDINATEMENUNAME);
	}
    
    public static String getName_AttributeMenu(){
        return PirolPlugInMessages.getString(KEY_ATTRIBUTEMENUNAME);
    }
    
    public static String getName_ContextInformationMenu(){
        return PirolPlugInMessages.getString(KEY_CONTEXTMENUNAME);
    }
    
    public static String getName_TransferMenu(){
        return PirolPlugInMessages.getString(KEY_TRANSFERMENUNAME);
    }
    
    public static String getName_SelectionMenu(){
        return PirolPlugInMessages.getString(KEY_SELECTIONMENUNAME);
    }
    
    /**
     *@return Name for the tools sub menu containing tools that display something
     */
    public static String getName_VisualToolsMenu(){
        return PirolPlugInMessages.getString(KEY_VISUALTOOLSMENUNAME);
    }
	
	public static String resultLayerCategory(){
		return PirolPlugInMessages.getString(KEY_RESULTCATEGORYNAME);
	}
	
	public static File configDirectory(){
	    File dir = new File( "config" + File.separator );
	    
	    if (!dir.exists()){
            dir.mkdirs();
	    }
	    
	    return dir;
	}
	
	public static File tempDirectory(){
	    File dir = new File( "tmp" + File.separator );
	    
	    if (!dir.exists()){
            dir.mkdirs();
	    }
	    
	    return dir;
	}
    
    /**
     * 
     *@return the standard number format to be used in all dialogs, etc. (... from now on)
     */
    public static NumberFormat getDefaultNumberFormat(){
        DecimalFormat doubleFormat = new DecimalFormat();
        doubleFormat.setGroupingUsed(false);
        
        return doubleFormat;
    }
    
}
