/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */
package com.gestorfip.ui.fichaurbanistica.tool;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.editor.GeopistaEditor;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.gestorfip.ui.fichaurbanistica.images.IconLoader;
import com.gestrofip.ui.plugin.ign.ConversionIgnClient;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.workbench.ui.cursortool.DragTool;

import es.gestorfip.serviciosweb.ServicesStub.CRSGestor;
import es.gestorfip.serviciosweb.ServicesStub.ConfiguracionGestor;
import es.gestorfip.serviciosweb.ServicesStub.VersionesUER;


public class FichaUrbanisticaTool extends DragTool {
	
	 boolean doubleClick = false;
	 
	private static String linuxDesktop = null;
	private static AppContext appContext = (AppContext) AppContext.getApplicationContext();
	 public static final ImageIcon ICON = IconLoader.icon("fichaUrb.gif");
 
	 
	 public void mouseClicked(MouseEvent e) {
	        try {
	            super.mouseClicked(e);
	            setViewSource(e.getPoint());
	            setViewDestination(e.getPoint());
	            if (e.getClickCount() == 2){
	                 setDoubleClick(true);
	            }
	            fireGestureFinished();
	        } catch (Throwable t) {
	            getPanel().getContext().handleThrowable(t);
	        }
	    }
		    

	    public boolean isDoubleClick()
	    {
	      return doubleClick;
	    }

	    public void setDoubleClick(boolean doubleClick)
	    {
	      this.doubleClick=doubleClick;
	    }
	    

	    public Icon getIcon() {
	        return ICON;
	    }

	    protected void gestureFinished() throws Exception {
	    	 Coordinate c = getModelSource();
	    	 GeopistaEditor geopistaEditor = (GeopistaEditor)appContext.getBlackboard().get(ConstantesGestorFIP.GEOPISTA_EDITOR_ASOCIACION_DETERMINACIONES_ENTIDADES);
	    	 String targetCRS = "";
	    	 CRSGestor[] crslst = (CRSGestor[]) appContext.getBlackboard().get(ConstantesGestorFIP.LST_CRS_GESTOR);
	    	 
 			 ConfiguracionGestor config = (ConfiguracionGestor)appContext.getBlackboard().get(ConstantesGestorFIP.CONFIG_VERSION_CONSOLE_UER);
 			 for(int i=0; i< crslst.length; i++){
	 			if(crslst[i].getId() == config.getIdCrs()){
	 				targetCRS = String.valueOf(crslst[i].getCrs());
	 			}
 			 }
 			 String sourceCRS =  String.valueOf(geopistaEditor.getLayerManager().getCoordinateSystem().getEPSGCode());

	    	 Geometry geometry = ConversionIgnClient.transformIGN_IDEE(c, sourceCRS, targetCRS);
	    	
	    	 String urlConsoleUERWS = appContext.getString(UserPreferenceConstants.URL_CONSOLEUER_WS);
	    	 String url = null;
	    	 Integer  idAmbitoTrabajo = Integer.valueOf((String)appContext.getBlackboard().get(ConstantesGestorFIP.AMBITO_TRABAJO));
	    	 VersionesUER[] versiones= (VersionesUER[])appContext.getBlackboard().get(ConstantesGestorFIP.VERSIONES_CONSOLE_UER);
	    	
	    	 for(int i=0; i<versiones.length; i++){
	    		 if(versiones[i].getId() == config.getIdVersion()){
	    			 
	    			 if(versiones[i].getVersion() == ConstantesGestorFIP.VERSIONCONSOLA_UER_1_86){
	    				//Consola 1.86v
	    				url = urlConsoleUERWS+"/FichaUrbanistica?X="+geometry.getCoordinate().x+"&Y="+geometry.getCoordinate().y+"&SRS=EPSG:"+targetCRS;  
	    			 }
	    			 else  if(versiones[i].getVersion() == ConstantesGestorFIP.VERSIONCONSOLA_UER_2_00){
	    				 //Consola 2.00v
	    				  url = urlConsoleUERWS+"/FichaUrbanistica?X="+geometry.getCoordinate().x+"&Y="+geometry.getCoordinate().y+"&SRS=EPSG:"+targetCRS+"&idAmbito="+String.valueOf(idAmbitoTrabajo.intValue());
	    			 }
	    		 }
	    	 }

	     	 String osName = System.getProperty("os.name");
	          try {
	              if (osName.toUpperCase().startsWith("WINDOWS")) {
	                  Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
	              } else if (osName.toUpperCase().startsWith("MAX OS X")) {
	                  Runtime.getRuntime().exec("open " + url);
	              } 
	              else if (osName.toUpperCase().indexOf("LINUX") != -1 ) {
	                  if(getLinuxDesktop().equals("kde"))
	                 	 Runtime.getRuntime().exec( new String[]{"kfmclient", "exec", url} );
	                  else
	                 	 Runtime.getRuntime().exec( new String[]{"gnome-open", url} );
	              }
	              else {
	                  System.out.println("Please open a browser and go to "+ url);
	              }

	          } catch (IOException e1) {
	              System.out.println("Failed to start a browser to open the url " + url);
	              e1.printStackTrace();
	          }
	    }
	
    private static String getLinuxDesktop(){

	      //solo se averigua el entorno de escritorio una vez, despues se almacena en la variable estatica
	      if(linuxDesktop!=null) return linuxDesktop;
	      if(!getEnv("KDE_FULL_SESSION").equals("") || !getEnv("KDE_MULTIHEAD").equals("")){
	          linuxDesktop="kde";
	      }
	      else if(!getEnv("GNOME_DESKTOP_SESSION_ID").equals("") || !getEnv("GNOME_KEYRING_SOCKET").equals("")){
	          linuxDesktop="gnome";
	      }
	      else linuxDesktop="";
	   
	      return linuxDesktop;
	  }
    private static String getEnv(String envvar){
	      try{
	          Process p = Runtime.getRuntime().exec("/bin/sh echo $"+envvar);
	          BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	          String value = br.readLine();
	          if(value==null) return "";
	          else return value.trim();
	      }
	      catch(Exception error){
	          return "";
	      }
	  }
    
    
}
