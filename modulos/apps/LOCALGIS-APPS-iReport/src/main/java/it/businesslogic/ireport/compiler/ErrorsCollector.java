/*
 * Copyright (C) 2005-2007 JasperSoft http://www.jaspersoft.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 * ErrorsCollector.java
 *
 * Created on March 14, 2007, 2:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.compiler;

import it.businesslogic.ireport.compiler.xml.SourceLocation;
import it.businesslogic.ireport.gui.logpane.ProblemItem;

import org.eclipse.jdt.core.compiler.IProblem;

/**
 *
 * @author gtoffoli
 */
public class ErrorsCollector implements JasperReportErrorHandler {
    
    private java.util.List problemItems = null;
    /** Creates a new instance of ErrorsCollector */
    public ErrorsCollector() {
        setProblemItems(new java.util.ArrayList());
    }

    public void addMarker(Throwable e) {
        e.printStackTrace();
        addMarker( e.getMessage(), null);
    }

    public void addMarker(String message, SourceLocation location) {
        if (location == null)
        {
            getProblemItems().add( new ProblemItem(ProblemItem.ERROR, message, location, null) );
        }
        else
        {
            getProblemItems().add( new ProblemItem(ProblemItem.ERROR, message, location, location.getXPath()) );
        }
    }

    public void addMarker(IProblem problem, SourceLocation location) {
        addMarker( problem.getMessage(), location);
    }

    public java.util.List getProblemItems() {
        return problemItems;
    }

    public void setProblemItems(java.util.List problemItems) {
        this.problemItems = problemItems;
    }
    
}
