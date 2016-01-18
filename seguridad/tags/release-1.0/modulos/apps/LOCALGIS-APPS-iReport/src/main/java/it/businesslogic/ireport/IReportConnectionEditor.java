/*
 * IReportConnectionEditor.java
 *
 * Created on March 27, 2007, 9:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport;

/**
 * A IReportConnectionEditor class provides a complete custom GUI for customizing a target IReportConnection.<br>
 * Each IReportConnectionEditor should inherit from the java.awt.Component class so it can be instantiated inside an AWT dialog or panel.<br>
 * Each IReportConnectionEditor should have a null constructor.<br>
 * 
 * @author gtoffoli
 */
public interface IReportConnectionEditor {
    
    /**
     * Set the IReportConnection to edit. Actually it is a copy of the original IReportConnection.
     * It can be modifed by the user interface.<br><br>
     * 
     * The copy of an IReportConnection is done instancing a new class of the same type and loading
     * the properties stored by the first object
     * @param c IReportConnection to edit
     */
    public void setIReportConnection(IReportConnection c);
    
    /**
     * This method is called when the user completes to edit the datasource or when a datasource test is required.
     * @return IReportConnection modified. IT can be the same instance get in input with setIReportConnection or a new one.
     */
    public IReportConnection getIReportConnection();
    
}
