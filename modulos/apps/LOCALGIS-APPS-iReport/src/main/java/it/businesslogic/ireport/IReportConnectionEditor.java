/**
 * IReportConnectionEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
