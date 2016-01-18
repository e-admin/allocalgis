/**
 * PersonalLogger.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 17.05.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: PersonalLogger.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:05 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/debugOutput/PersonalLogger.java,v $
 */
package pirolPlugIns.utilities.debugOutput;

import java.io.PrintStream;

/**
 * Class to post personalized debug statements. Holds user information
 * and uses the GenericDebugLogger itself.
 *
 * @author Ole Rahn
 * @author FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * Project: PIROL (2005),
 * Subproject: Daten- und Wissensmanagement
 * @see GenericDebugLogger
 * @see DebugUserIds
 */
public class PersonalLogger {
    
    protected String userId = null;
    
    protected GenericDebugLogger genericLogger = GenericDebugLogger.getInstance();
    /**
     * Constructor
     * @param userName - should be taken from class DebugUserIds (in this package)
     */
    public PersonalLogger( String userName ){
        this.userId = userName;
    }
    
    
    /**
     * 
     * @param message message to print
     */
    public final void printDebug(String message) {
        genericLogger.printDebug(this.userId, message);
    }
    /**
     * 
     * @param message message to print
     */
    public final void printError(String message) {
        genericLogger.printError(this.userId, message);
    }
    /**
     * 
     * @param message message to print
     */
    public final void printMinorError(String message) {
        genericLogger.printMinorError(this.userId, message);
    }
    /**
     * 
     * @param message message to print
     */
    public final void printSevereError(String message) {
        genericLogger.printSevereError(this.userId, message);
    }
    /**
     * 
     * @param message message to print
     */
    public final void printWarning(String message) {
        genericLogger.printWarning(this.userId, message);
    }
    
    /**
     * 
     *@param message message to print
     *@param isActive is this statement active? (message won't be printed, if not)
     */
    public final void printDebug(String message, boolean isActive) {
        if (isActive)
            genericLogger.printDebug(this.userId, message);
    }
    /**
     * 
     *@param message message to print
     *@param isActive is this statement active? (message won't be printed, if not)
     */
    public final void printError(String message, boolean isActive) {
        if (isActive)
            genericLogger.printError(this.userId, message);
    }
    /**
     * 
     *@param message message to print
     *@param isActive is this statement active? (message won't be printed, if not)
     */
    public final void printMinorError(String message, boolean isActive) {
        if (isActive)
            genericLogger.printMinorError(this.userId, message);
    }
    /**
     * 
     *@param message message to print
     *@param isActive is this statement active? (message won't be printed, if not)
     */
    public final void printSevereError(String message, boolean isActive) {
        if (isActive)
            genericLogger.printSevereError(this.userId, message);
    }
    /**
     * 
     *@param message message to print
     *@param isActive is this statement active? (message won't be printed, if not)
     */
    public final void printWarning(String message, boolean isActive) {
        if (isActive)
            genericLogger.printWarning(this.userId, message);
    }
    
    /**
     * 
     * @return true or false //TODO specify the return value
     */
    public final boolean isPrintFileAndLine() {
        return genericLogger.isPrintFileAndLine();
    }
    /**
     * 
     * @return true or false //TODO specify the return value
     */
    public final boolean isPrintNewLineFirst() {
        return genericLogger.isPrintNewLineFirst();
    }
    /**
     * 
     * @return true or false //TODO specify the return value
     */
    public final boolean isPrintTimeStamp() {
        return genericLogger.isPrintTimeStamp();
    }
    /**
     * 
     * @param logLevel
     */
    public final void setLogLevel(int logLevel) {
        genericLogger.setLogLevel(logLevel);
    }
    /**
     * 
     * @param out
     */
    public final void setOutputStream(PrintStream out) {
        genericLogger.setOutputStream(out);
    }
}
