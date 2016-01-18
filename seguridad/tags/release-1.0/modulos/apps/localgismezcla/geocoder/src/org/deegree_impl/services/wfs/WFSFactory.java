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
package org.deegree_impl.services.wfs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;

import org.deegree.services.Handler;
import org.deegree.services.OGCWebService;
import org.deegree.services.wfs.DataStore;
import org.deegree.services.wfs.Dispatcher;
import org.deegree.services.wfs.capabilities.FeatureType;
import org.deegree.services.wfs.capabilities.Operation;
import org.deegree.services.wfs.capabilities.WFSCapabilities;
import org.deegree_impl.tools.Debug;

/**
 *
 * factory class for creating an instance of a <tt>DataStore</tt>s from its
 * class name and its configuration file.
 *
 * <p>-------------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 * <p>
 */
public class WFSFactory {
    
    /**
     * Factory method for creating an instance of a WFSService.
     * @param capabilities name of the (XML-) file containing the capabilities
     *		 of the WFS.
     * @param dispatcher instance of the dispatcher that should manage the
     *		 WFS requests and responses. If <tt>null</tt> is submitted to the
     *		 mehtod instead a new instance will be created.
     */
    public static synchronized OGCWebService createWFSService(WFSCapabilities capa,
                                              Dispatcher dispatcher) throws Exception {
        Debug.debugMethodBegin( "org.deegree_impl.services.wfs.WFSFactory", "createWFSService" );

        WFSService_Impl service 	= null;
        DataStore datastore 		= null;
        HashMap stores              = new HashMap();
      
                
        if ( dispatcher == null ) {
            dispatcher = new Dispatcher_Impl();
        }

        service = new WFSService_Impl( capa );

        // register dispatcher as request handler to the service
        service.registerHandler( dispatcher );
        // register service as response handler to the dispatcher
        dispatcher.registerHandler( service );

        FeatureType[] ft = capa.getFeatureTypeList().getFeatureTypes();
        for (int i = 0; i < ft.length; i++) {
            String name = ft[i].getName();
            Operation[] operation = ft[i].getOperations();
            String resClass = ft[i].getResponsibleClassName();
            URL config = ft[i].getConfigURL();

            if ( stores.get( resClass+config ) == null ) {
                datastore = createDataStrore( resClass, config );
                stores.put( resClass+config, datastore );                
                // register datastore as requesthandler to the dispatcher
                dispatcher.registerHandler( datastore );
                // register dispatcher as responsehandler to the datastore
                ((Handler)datastore).registerHandler( dispatcher );
            } else {
                datastore = (DataStore)stores.get( resClass+config );
                datastore.registerFeatureType( name );
            }
            
        }
        
        Debug.debugMethodEnd();
        
        return service;
    }
    
    /**
     *
     */
    public synchronized static DataStore createDataStrore(String className, URL configuration)
                                            throws Exception {
        Debug.debugMethodBegin( "org.deegree_impl.services.wfs.WFSFactory", "createDataStrore" );
        
        // describes the signature of the required constructor
        Class[] cl = new Class[1];
        cl[0] = URL.class;
        
        // set parameter to submitt to the constructor
        Object[] o = new Object[1];
        o[0] = configuration;
        
        DataStore dataStore = null;

        try {
            // get constructor
            Class creator = Class.forName( className );
            Constructor con = creator.getConstructor( cl );
            // call constructor and instantiate a new DataStore
            dataStore = (DataStore) con.newInstance( o );            
        } catch(InvocationTargetException ite) {
            Throwable thro = ite.getTargetException();
            throw new Exception("Couldn't instantiate " + className + 
                                "! \n" + thro.toString() );
        } catch(Exception e) {
            Debug.debugException( e, "" );
            throw new Exception("Couldn't instantiate " + className + 
                                "! \n" + e.toString() );
        }
        
        Debug.debugMethodEnd();
        
        return dataStore;
    }
    
}
