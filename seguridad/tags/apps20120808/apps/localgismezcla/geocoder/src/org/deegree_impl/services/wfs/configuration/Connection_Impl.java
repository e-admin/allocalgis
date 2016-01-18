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

import org.deegree.services.wfs.configuration.Connection;


/**
 * The interface describes the connection to a database
 *
 * <p>---------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 */
public class Connection_Impl implements Connection {
    private String driver = null;
    private String logon = null;
    private String password = null;
    private String sdeDatabase = null;
    private String spatialVersion = null;
    private String user = null;

    /**
     * Creates a new Connection_Impl object.
     *
     * @param driver 
     * @param logon 
     * @param user 
     * @param password 
     */
    Connection_Impl( String driver, String logon, String user, String password ) {
        setDriver( driver );
        setLogon( logon );
        setUser( user );
        setPassword( password );
    }

    /**
     * returns the name of the jdbc driver class
     */
    public String getDriver() {
        return driver;
    }

    /**
     * @see getDriver
     */
    public void setDriver( String driver ) {
        this.driver = driver;
    }

    /**
     * returns the logon (address, port etc.) to a database
     */
    public String getLogon() {
        return logon;
    }

    /**
     * @see getLogon
     */
    public void setLogon( String logon ) {
        this.logon = logon;
    }

    /**
     * returns the name of the user who shall be used for connecting
     * a database
     * @return user name or an empty string is no user shall be used
     */
    public String getUser() {
        return user;
    }

    /**
     * @see getUser
     */
    public void setUser( String user ) {
        if ( user == null ) {
            this.user = "";
        } else {
            this.user = user;
        }
    }

    /**
     * returns the users's password
     * @return password or an empty string is no password shall be used
     */
    public String getPassword() {
        return password;
    }

    /**
     * @see getPassword
     */
    public void setPassword( String password ) {
        if ( password == null ) {
            this.password = "";
        } else {
            this.password = password;
        }
    }

    /** returns the version number of the spatial extension if an Oracle Spatial
     * database is used.
     *
     */
    public String getSpatialVersion() {
        return spatialVersion;
    }

    /**
     * @see getSpatialVersion
     */
    public void setSpatialVersion( String spatialVersion ) {
        this.spatialVersion = spatialVersion;
    }

    /** returns the name of the sde database that is assigned to a SDE connection.
     *
     */
    public String getSDEDatabase() {
        return sdeDatabase;
    }

    /** 
     * @see getSDEDatabase
     */
    public void setSDEDatabase( String sdeDatabase ) {
        this.sdeDatabase = sdeDatabase;
    }
}