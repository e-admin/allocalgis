/**
 * PirolPlugInMessages.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 02.08.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: PirolPlugInMessages.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:06 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/i18n/PirolPlugInMessages.java,v $
 */
package pirolPlugIns.i18n;

import java.util.MissingResourceException;

import pirolPlugIns.utilities.HandlerToMakeYourLifeEasier;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;


/**
 * Handles i18n stuff for PIROL plugIns.<br>
 * Class that Eclipse generates, if the "Externalize Strings" command is used. Was renamed (from <code>Messages.java</code>) and modified to use the openJump i18n plug - the interface stayed the same!<br>
 * Wrapper for the i18N to make work with PIROL labels easier.
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
public class PirolPlugInMessages implements HandlerToMakeYourLifeEasier {
    private static final String BUNDLE_NAME = "pirolPlugIns.resources.PirolPlugIns";
    
    private static boolean inited = false;

    protected static PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    /**
     * We don't need instances of the class!
     */
    private PirolPlugInMessages(){}

    /**
     * Get a translated PIROL text from the i18N system.
     *@param key the key (name) for the the text 
     *@return the translated text
     */
    public static String getString(String key) {
        if (!PirolPlugInMessages.inited){
            I18NPlug.setPlugInRessource("pirolPlugIns", PirolPlugInMessages.BUNDLE_NAME);
            PirolPlugInMessages.inited = true;
        }
        
        try {
            return I18NPlug.get("pirolPlugIns", key);
        } catch (MissingResourceException e) {
            logger.printMinorError("i18n key not found for: \"" + key + "\"");
            return "!" + key + "!";
        }
    }

    public static boolean isInited() {
        return inited;
    }
    
}
