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
package com.vividsolutions.jump.workbench.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.RegisterPlugInManager;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;

/**
 * Loads plug-ins (or more precisely, Extensions), and any JAR files that they
 * depend on, from the plug-in directory.
 */
public class PlugInManager {
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(PlugInManager.class);

    private TaskMonitor monitor;
    private WorkbenchContext context;
    private Collection configurations = new ArrayList();
    private List otherConfigurations = new ArrayList();
    

    /**
     * @param plugInDirectory null to leave unspecified
     */
    public PlugInManager(WorkbenchContext context, File plugInDirectory,
        TaskMonitor monitor) throws Exception {
        this.monitor = monitor;
        Assert.isTrue((plugInDirectory == null) ||
            plugInDirectory.isDirectory());
        this.context = context;

        //Find the configurations right away so they get reported to the splash
        //screen ASAP. [Jon Aquino]       
        configurations.addAll((plugInDirectory != null)
            ? findConfigurations(plugInDirectory) : new ArrayList());
        configurations.addAll(findConfigurations(context.getIWorkbench()
                                                        .getProperties()
                                                        .getConfigurationClasses()));
    }

    public void load() throws Exception {
        loadPlugInClasses(context.getIWorkbench().getProperties()
                                 .getPlugInClasses());
        loadConfigurations();
    }

    private void loadConfigurations() throws Exception {
        for (Iterator i = configurations.iterator(); i.hasNext();) {
        Configuration configuration = (Configuration) i.next();
            try
				{
				
				configuration.configure(new PlugInContext(context, null, null,
						null, null));
				}
			catch (Exception e)
				{
				logger.error("loadConfigurations(): Error loading extension:"+configuration.toString(), e);
				context.getErrorHandler().handleThrowable(e);
				}
        }
    }

    public static String name(Configuration configuration) {
        if (configuration instanceof Extension) {
            return ((Extension) configuration).getName();
        }

        return StringUtil.toFriendlyName(configuration.getClass().getName(),
            "Configuration") + " (" +
        configuration.getClass().getPackage().getName() + ")";
    }

    public static String version(Configuration configuration) {
        if (configuration instanceof Extension) {
            return ((Extension) configuration).getVersion();
        }

        return "";
    }

    private Collection findConfigurations(List classes)
        throws Exception {
        ArrayList configurations = new ArrayList();

        for (Iterator i = classes.iterator(); i.hasNext();) {
            Class c = (Class) i.next();

            if (!Configuration.class.isAssignableFrom(c)) {
                continue;
            }

            Configuration configuration = (Configuration) c.newInstance();
            configurations.add(configuration);
            monitor.report("Loading " +
                name(configuration) + " " + version(configuration));
        }

        return configurations;
    }

    private void loadPlugInClasses(List plugInClasses)
        throws Exception {
        for (Iterator i = plugInClasses.iterator(); i.hasNext();) {
            Class plugInClass = (Class) i.next();
           
            HashSet hs = new HashSet();
            PlugIn plugIn = (PlugIn) plugInClass.newInstance();
            plugIn.initialize(new PlugInContext(context, null, null, null, null));
            
            monitor.report("Initialized: " +	plugIn.getName() );
            hs.add(plugIn);
            
        }
        
    }

    private Collection findConfigurations(File plugInDirectory)
        throws Exception {
/**
 * Crea un cargador de clases con permisos totales para que sea utilizable
 * en un entorno protegido como JWS
 */
        ClassLoader classLoader = new URLClassLoader(toURLs(plugInDirectory.listFiles()),this.getClass().getClassLoader())
		{
		protected PermissionCollection getPermissions(CodeSource codesource)
		{
		Permission perm=new AllPermission();
		Permissions col = new Permissions();
		col.add(perm);
		
		return col;
		}
		};
//System.out.println("classloader Extension:"+classLoader+" con padre: "+classLoader.getParent());
        ArrayList configurations = new ArrayList();

        for (Iterator i = Arrays.asList(plugInDirectory.listFiles()).iterator();
                i.hasNext();) {
            File file = (File) i.next();

            if (file.isDirectory()) {
                configurations.addAll(findConfigurations(file));
            }

            if (!file.isFile()) {
                continue;
            }

            try {
                configurations.addAll(findConfigurations(classes(
                            new ZipFile(file), classLoader)));
            } catch (ZipException e) {
                //Might not be a zipfile. Eat it. [Jon Aquino]
            }
        }

        return configurations;
    }

    private URL[] toURLs(File[] files) {
        URL[] urls = new URL[files.length];

        for (int i = 0; i < files.length; i++) {
            try {
                urls[i] = new URL("jar:file:" + files[i].getPath() + "!/");
            } catch (MalformedURLException e) {
                Assert.shouldNeverReachHere(e.toString());
            }
        }

        return urls;
    }

    private List classes(ZipFile zipFile, ClassLoader classLoader) {
        ArrayList classes = new ArrayList();

        for (Enumeration e = zipFile.entries(); e.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) e.nextElement();

            //Filter by filename; otherwise we'll be loading all the classes, which takes
            //significantly longer [Jon Aquino]   
            if (!(entry.getName().endsWith("Extension.class") ||
                    entry.getName().endsWith("Configuration.class"))) {
                //Include "Configuration" for backwards compatibility. [Jon Aquino]
            	Class c = toClass(entry, classLoader);
            	if (c != null && c.getInterfaces().length>0)
            		otherConfigurations.add(toClass(entry, classLoader));
                continue;
            }

            Class c = toClass(entry, classLoader);

            if (c != null) {
                classes.add(c);
            }
        }

        return classes;
    }

    private Class toClass(ZipEntry entry, ClassLoader classLoader) {
        if (entry.isDirectory()) {
            return null;
        }

        if (!entry.getName().endsWith(".class")) {
            return null;
        }

        if (entry.getName().indexOf("$") != -1) {
            //I assume it's not necessary to load inner classes explicitly. [Jon Aquino]
            return null;
        }

        String className = entry.getName();
        className = className.substring(0,
                className.length() - ".class".length());
        className = StringUtil.replaceAll(className, "/", ".");

        Class candidate;

        try {
            candidate = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            Assert.shouldNeverReachHere("Class not found: " + className +
                ". Refine class name algorithm.");

            return null;
        } catch (Throwable t) {
			if (logger.isDebugEnabled())
				{
				logger
						.debug("toClass(ZipEntry, ClassLoader) - Throwable encountered loading "
								+ className + ":");
				}

            //e.g. java.lang.VerifyError: class org.apache.xml.serialize.XML11Serializer 
            //overrides final method [Jon Aquino]
            t.printStackTrace(System.out);

            return null;
        }

        return candidate;
    }

    public Collection getConfigurations() {
        return Collections.unmodifiableCollection(configurations);
    }
    
    public Collection getPlugins(){
    	return ((RegisterPlugInManager)((AppContext) AppContext.getApplicationContext())
    			.getBlackboard().get("RegisterPlugInManager")).getPlugins();
    }
 
    public List getOtherConfigurations(){
    	return otherConfigurations;
    }
}
