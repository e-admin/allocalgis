/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/



package com.geopista.ui.plugin;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.agil.core.jump.coverage.CoverageLayer;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.model.GeopistaMap;
import com.geopista.model.GeopistaMapCustomConverter;
import com.geopista.model.WMSLayerImpl;
import com.vividsolutions.jump.util.FileUtil;
import com.vividsolutions.jump.util.java2xml.Java2XML;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.ITask;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;




public abstract class GeopistaAbstractSaveMapPlugIn extends ThreadedBasePlugIn {


    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    public GeopistaAbstractSaveMapPlugIn() {
    }

    protected void save(GeopistaMap map, File file, WorkbenchGuiComponent frame)
        throws Exception {
        //First use StringWriter to make sure no errors occur before we touch the
        //original file -- we don't want to damage the original if an error occurs.
        //[Jon Aquino]
        StringWriter stringWriter = new StringWriter();

        try {
            Java2XML converter = new Java2XML();
            converter.addCustomConverter(Date.class, GeopistaMapCustomConverter.getMapDateCustomConverter());
            converter.write(map, "GeopistaMap", stringWriter);
        } finally {
            stringWriter.flush();
        }

        FileUtil.setContentsUTF8(file.getAbsolutePath(), stringWriter.toString());
       

        ArrayList ignoredLayers = new ArrayList(ignoredLayers(map));

        if (!ignoredLayers.isEmpty()) {
            String warning = aplicacion.getI18nString("GeopistaAbstractSaveMapPlugIn.CapaNoGuardadas");

            for (int i = 0; i < ignoredLayers.size(); i++) {
                Layer ignoredLayer = (Layer) ignoredLayers.get(i);

                if (i > 0) {
                    warning += "; ";
                }

                warning += ignoredLayer.getName();
            }
            
            warning += " "+aplicacion.getI18nString("GeopistaAbstractSaveMapPlugIn.SoloEscritura");

            frame.warnUser(warning);
        }
    }
    
    
    protected void saveISO88591(GeopistaMap map, File file, WorkbenchGuiComponent frame)
    throws Exception {
    //First use StringWriter to make sure no errors occur before we touch the
    //original file -- we don't want to damage the original if an error occurs.
    //[Jon Aquino]
    StringWriter stringWriter = new StringWriter();

    try {
        Java2XML converter = new Java2XML();
        converter.addCustomConverter(Date.class, GeopistaMapCustomConverter.getMapDateCustomConverter());
        converter.writeISO88591(map, "GeopistaMap", stringWriter);
    } finally {
        stringWriter.flush();
    }

   FileUtil.setContentsISO88591(file.getAbsolutePath(), stringWriter.toString());

    ArrayList ignoredLayers = new ArrayList(ignoredLayers(map));

    if (!ignoredLayers.isEmpty()) {
        String warning = aplicacion.getI18nString("GeopistaAbstractSaveMapPlugIn.CapaNoGuardadas");

        for (int i = 0; i < ignoredLayers.size(); i++) {
            Layer ignoredLayer = (Layer) ignoredLayers.get(i);

            if (i > 0) {
                warning += "; ";
            }

            warning += ignoredLayer.getName();
        }
        
        warning += " "+aplicacion.getI18nString("GeopistaAbstractSaveMapPlugIn.SoloEscritura");

        frame.warnUser(warning);
    }
}

    
    
    

    private Collection ignoredLayers(GeopistaMap map) {
        ArrayList ignoredLayers = new ArrayList();

        for (Iterator i = map.getLayerManager().getLayers().iterator();
                i.hasNext();) {
                
                Object layerable = i.next();
                if (layerable instanceof Layer)
                	{
                	Layer layer = (Layer) layerable;
                	
                	if (!layer.hasReadableDataSource())
                		{
                		ignoredLayers.add(layer);
                		}
                	}
                else
                if (layerable instanceof WMSLayerImpl || layerable instanceof CoverageLayer)
                	{
                	// Do nothing  Just don't ignore raster layers and store them
                	}
        }

        return ignoredLayers;
    }
    
    protected void saveSchema(StringWriter stringWriter, File file)
        throws Exception {

        FileUtil.setContents(file.getAbsolutePath(), stringWriter.toString());
     }
    
    protected void saveSchema(String str, File file)
    throws Exception {

    FileUtil.setContents(file.getAbsolutePath(), str);
 }

    protected void saveSchemaUTF8(StringWriter stringWriter, File file)
    throws Exception {

    	FileUtil.setContentsUTF8(file.getAbsolutePath(), stringWriter.toString());
    }
    
    protected void saveSchemaUTF8(String str, File file)
    throws Exception {

    	FileUtil.setContentsUTF8(file.getAbsolutePath(),str);
    }

    protected void saveTask(ITask iTask, File file, WorkbenchGuiComponent frame)
        throws Exception {
        //First use StringWriter to make sure no errors occur before we touch the
        //original file -- we don't want to damage the original if an error occurs.
        //[Jon Aquino]

        GeopistaMap map = new GeopistaMap();
        map.setExtracted(true);
        map.setName(iTask.getName());
        map.setTimeStamp(new Date());
        map.setDescription(aplicacion.getI18nString("GeopistaAbstractSaveMapPlugIn.GeopistaMapa"));
        map.setProjectFile(file);
        
        Collection categories = iTask.getCategories();
        Iterator categoriesIter = categories.iterator();
        while(categoriesIter.hasNext())
        {
          map.addCategory((Category) categoriesIter.next());
        }

        



        
        StringWriter stringWriter = new StringWriter();

        try {
            Java2XML converter = new Java2XML();
            converter.addCustomConverter(Date.class, GeopistaMapCustomConverter.getMapDateCustomConverter());
            converter.write(map, "GeopistaMap", stringWriter);
            FileUtil.setContents(file.getAbsolutePath(), stringWriter.toString());
        } finally {
            stringWriter.flush();
            stringWriter.close();
        }

 

        ArrayList ignoredLayers = new ArrayList(ignoredLayers(map));

        if (!ignoredLayers.isEmpty()) {
            String warning = aplicacion.getI18nString("GeopistaAbstractSaveMapPlugIn.CapaNoGuardadas");

            for (int i = 0; i < ignoredLayers.size(); i++) {
                Layer ignoredLayer = (Layer) ignoredLayers.get(i);

                if (i > 0) {
                    warning += "; ";
                }

                warning += ignoredLayer.getName();
            }
            
            warning += " "+aplicacion.getI18nString("GeopistaAbstractSaveMapPlugIn.SoloEscritura");

            frame.warnUser(warning);
        }
    }
}
