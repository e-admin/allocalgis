/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
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
 */

package it.businesslogic.ireport.compiler;

import it.businesslogic.ireport.compiler.xml.SourceLocation;

import org.eclipse.jdt.core.compiler.IProblem;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JasperReportErrorHandler.java,v 1.1 2011/12/14 18:15:23 satec Exp $
 */
public interface JasperReportErrorHandler
{

	void addMarker(Throwable e);

	void addMarker(String message, SourceLocation location);

	void addMarker(IProblem problem, SourceLocation location);

}

