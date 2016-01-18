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

package org.deegree_impl.services;

import java.util.*;
import org.deegree.services.*;

/**
 *
 *
 * <p>-------------------------------------------------------------------------</p>
 *
 * @author Andreas Poth
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 * <p>
 */
abstract public class AbstractHandler implements Handler {
    
    protected List handlerList 			= null;
    protected List requestList 			= null;
    protected List responseList			= null;
    /** list of request that are performed actualy */
    protected Map inProgressList 		= null;
    /**
     * maximal time (millis) the perfomrmance of a request is
     * allowed to take
     */
    protected int responseTimeout	 	= 2*1000*60;
    /**
     * maximal time a loop runs without existing a request or
     * response to handle
     */
    private int loopTimeout 			= 2*1000*60;
    /**
     * time (millis) the loop 'sleep' before looking for new
     * requests or responses
     */
    private int loopFrequency 			= 100;
    protected long timestamp                    = System.currentTimeMillis();
    
    public AbstractHandler() {
        handlerList = Collections.synchronizedList( new ArrayList() );
        requestList = Collections.synchronizedList( new ArrayList() );
        responseList = Collections.synchronizedList( new ArrayList() );
        inProgressList = Collections.synchronizedMap( new HashMap() );
    }
    
    public void dispose() {
        requestList.clear();
        responseList.clear();
        inProgressList.clear();
        for (int i = handlerList.size()-1; i >= 0; i -= 1) {
            removeHandler( (Handler)handlerList.get(i) );
        }
    }
    
    /**
     * registers a Handler so this Handler is able to act as a proxy
     * to the registered handler
     */
    public void registerHandler(Handler handler) {
        handlerList.add( handler );
    }
    
    /**
     * @see registerHandler
     */
    public void removeHandler(Handler handler) {
        handlerList.remove(  handler );
    }
    
    /**
     * fires a request by notifiy all interested (responsible) <tt>Handler</tt>
     * If for minimum one Handler was found that is responsible the method
     * returns <tt>true</tt> otherwise <tt>false</tt> will be returned.
     */
    public boolean fireRequest(OGCWebServiceEvent event) {
        boolean fired = false;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = handlerList.size()-1; i >= 0; i -= 1) { 
            Handler handler = (Handler)handlerList.get(i);
            if ( handler.isInterested( event ) ) {
                handler.handleRequest( event );                
                fired = true;
            } 
            
        }
        return fired;
    }
    
    /**
     * fires a response by notifiy all interested (responsible) <tt>Handler</tt>
     * If for minimum one Handler was found that is responsible the method
     * returns <tt>true</tt> otherwise <tt>false</tt> will be returned.
     */
    public boolean fireResponse(OGCWebServiceEvent event) {
        boolean fired = false;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = handlerList.size()-1; i >= 0; i -= 1) { 
            Handler handler = (Handler)handlerList.get(i);

            if ( handler.isInterested( event ) ) {
                handler.handleResponse( event );                
                fired = true;
            } 
            
        }
        return fired;
    }
    
    /**
     * sets time in seconds the waits for a response of a WMSService
     * that serves a request (default: 2 seconds).
     */
    public void setResponseTimeout(int seconds) {
        this.responseTimeout = seconds * 1000;
    }
    
    /**
     * sets the time in seconds the handler keeps on running the request and response
     * loops when no more requests or responses are registered to be performed.
     * A value of -1 (default) indicates that the loops will be stopped immidiatly
     * if no more requests or responses are registered to be performed.
     */
    public void setLoopTimeout(int seconds) {
        this.loopTimeout = seconds * 1000;
    }
    
    /**
     * sets the frequency/time a loop looks for new requests/responses
     * to perform.
     */
    public void setLoopFrequency(int millisec) {
        this.loopFrequency = millisec;
    }
    
    /**
     * stops the processing loop(s)
     */
    public abstract void stopLoops();
    
    /**
     * The loop have to be started by the classes that extends
     * <tt>AbstractHandler</tt>. This may be realized by overriding the methods
     * <tt>handleRequest</tt> and <tt>handleResponse</tt>.
     */
    abstract public class Loop extends Thread {
        
        protected boolean stop = false;
        
        /**
         * overrides the <tt>run</tt>-methode declared within the
         * <tt>Runnable</tt>-interface
         */
        public final void run() {
            stop = false;
            
            while (!stop) {
                try {
                    Thread.sleep( loopFrequency );
                } catch(Exception e) { stopLoops(); }
                
                process();
                
                if ( (System.currentTimeMillis() - timestamp) > loopTimeout ) {
                    stopLoops();
                }
            }
            onStopped();
        }
        
        /**
         * this method will be called after a loop has been stopped.
         * As default no action is performed; but the method can be overwritten
         * by an extending class to perform some clean-up actions for example.
         */
        protected void onStopped() {
        }
        
        /**
         * this method is called within the loop. An extending class have to
         * implement this method.
         */
        abstract protected void process();
        
        /**
         * stops the loop resp. the Thread
         */
        public void stopLoop() {
            stop = true;
        }
        
    }
}
