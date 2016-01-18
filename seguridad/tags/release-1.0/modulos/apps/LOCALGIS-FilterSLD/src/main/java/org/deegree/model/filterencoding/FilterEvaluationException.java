//$HeadURL: https://svn.wald.intevation.org/svn/deegree/base/trunk/src/org/deegree/model/filterencoding/FilterEvaluationException.java $
/*----------------    FILE HEADER  ------------------------------------------

This file is part of deegree.
Copyright (C) 2001-2006 by:
EXSE, Department of Geography, University of Bonn
http://www.giub.uni-bonn.de/deegree/
lat/lon GmbH
http://www.lat-lon.de

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact:

Andreas Poth  
lat/lon GmbH
Aennchenstr. 19
53115 Bonn
Germany
E-Mail: poth@lat-lon.de

Prof. Dr. Klaus Greve
Department of Geography
University of Bonn
Meckenheimer Allee 166
53115 Bonn
Germany
E-Mail: greve@giub.uni-bonn.de

                 
 ---------------------------------------------------------------------------*/
package org.deegree.model.filterencoding;

/**
 * This exception is thrown when the evaluation of a <tt>Filter</tt>
 * expression (or subexpression) is impossible, e.g. a PropertyIsLessThan-
 * Comparison is to be applied against a <tt>String</tt>-value.
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:47:32 $
 */
public class FilterEvaluationException extends Exception {
    
    /**
     * Constructs a new <tt>FilterEvaluationException</tt> with the
     * given detail message.
     * @param msg the detail message
     */
    public FilterEvaluationException (String msg) {
        super (msg);
    }
}
/* ********************************************************************
Changes to this class. What the people have been up to:
$Log: FilterEvaluationException.java,v $
Revision 1.1  2011/09/19 13:47:32  satec
MODELO EIEL

Revision 1.3  2010/05/03 08:41:19  satec
*** empty log message ***

Revision 1.1  2009/03/31 15:54:49  roger
Creación de módulo FIlter SLD que implementa los filtros OGC sobre Features SVG

Revision 1.5  2006/07/12 14:46:14  poth
comment footer added

********************************************************************** */
