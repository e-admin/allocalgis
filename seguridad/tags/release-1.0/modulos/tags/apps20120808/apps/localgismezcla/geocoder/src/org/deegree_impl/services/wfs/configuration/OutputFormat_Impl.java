/*----------------    FILE HEADER  ------------------------------------------

This file is part of deegree.
Copyright (C) 2001 by:
EXSE, Department of Geography, University of Bonn
http://www.giub.uni-bonn.de/exse/
lat/lon Fitzke/Fretter/Poth GbR
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
lat/lon Fitzke/Fretter/Poth GbR
Meckenheimer Allee 176
53115 Bonn
Germany
E-Mail: poth@lat-lon.de

Jens Fitzke
Department of Geography
University of Bonn
Meckenheimer Allee 166
53115 Bonn
Germany
E-Mail: jens.fitzke@uni-bonn.de

                 
 ---------------------------------------------------------------------------*/
package org.deegree_impl.services.wfs.configuration;

import java.net.URL;

import org.deegree.services.wfs.configuration.OutputFormat;
import org.deegree.tools.Parameter;
import org.deegree.tools.ParameterList;


/**
 * the interface describes the central configuration for generating
 * a output format known by the wfs.
 *
 * <p>---------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
public class OutputFormat_Impl implements OutputFormat {
    private ParameterList parameter = null;
    private String name = null;
    private String responsibleClass = null;
    private URL schemaLocation = null;

    /**
     * Creates a new OutputFormat_Impl object.
     *
     * @param name 
     * @param responsibleClass 
     * @param parameter 
     * @param schemaLocation 
     */
    OutputFormat_Impl( String name, String responsibleClass, ParameterList parameter, 
                       URL schemaLocation ) {
        setName( name );
        setResponsibleClass( responsibleClass );
        setParameter( parameter );
        setSchemaLocation( schemaLocation );
    }

    /**
     * returns the name of the format
     */
    public String getName() {
        return name;
    }

    /**
     * @see getName
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * returns the name of the java class that's responsible for
     * generating the format
     */
    public String getResponsibleClass() {
        return responsibleClass;
    }

    /**
     * @see getResponsibleClass
     */
    public void setResponsibleClass( String responsibleClass ) {
        this.responsibleClass = responsibleClass;
    }

    /**
     * returns a list of parameters that will be submitted to the
     * responsible class
     */
    public ParameterList getParameter() {
        return parameter;
    }

    /**
     * @see getParameter
     */
    public void setParameter( ParameterList parameter ) {
        this.parameter = parameter;
    }

    /**
     * @see getParameter
     */
    public void addParameter( Parameter parameter ) {
        this.parameter.addParameter( parameter );
    }

    /**
     * returns the location where a XML-schema description of a feature
     * type is accessable
     */
    public URL getSchemaLocation() {
        return schemaLocation;
    }

    /**
     * @see getSchemaLocation
     */
    public void setSchemaLocation( URL schemaLocation ) {
        this.schemaLocation = schemaLocation;
    }
}