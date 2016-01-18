
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

package com.vividsolutions.jump.workbench.driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.workbench.WorkbenchProperties;
import com.vividsolutions.jump.workbench.ui.BasicFileDriverPanel;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;

/**
 * Gestor de los drivers  de entrada y salida.<br>
 * Tiene una serie de drivers predefinidos, y admite cualquier otro driver
 * cargado en tiempo de ejecución de la carpeta de drivers.
 *
 * */
public class DriverManager {
    private ArrayList inputDrivers = new ArrayList();
    private ArrayList outputDrivers = new ArrayList();

    /**
     * Drivers de entrada predefinidos
     * */
    private List builtInInputDriverClasses = Arrays.asList(new Class[] {
                FMEFileInputDriver.class, GMLFileInputDriver.class,
                JMLFileInputDriver.class, ShapeFileInputDriver.class,
                WKTFileInputDriver.class
            });

      /**
     * Drivers de salida predefinidos
     * */
    private List builtInOutputDriverClasses = Arrays.asList(new Class[] {
                FMEFileOutputDriver.class, GMLFileOutputDriver.class,
                JMLFileOutputDriver.class, ShapefileOutputDriver.class,
                WKTFileOutputDriver.class
            });
    private BasicFileDriverPanel sharedOpenBasicFileDriverPanel;
    private BasicFileDriverPanel sharedSaveBasicFileDriverPanel;
    private ErrorHandler errorHandler;

    /**
     * Recibe un ErrorHandler para notificarle de errores que se produzcan al abrir
     * o salvar ficheros.
     * El ErrorHandler que se utiliza es WorkbenchFrame, que implementa ErrorHandler.
     * */
    public DriverManager(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        sharedOpenBasicFileDriverPanel = new SharedFileDriverPanel("File To Open",
                errorHandler);
        sharedSaveBasicFileDriverPanel = new SharedFileDriverPanel("File To Save",
                errorHandler);
        sharedOpenBasicFileDriverPanel.setFileMustExist(true);
        sharedSaveBasicFileDriverPanel.setFileMustExist(false);
    }

    public List getInputDrivers() {
        return inputDrivers;
    }

    public List getOutputDrivers() {
        return outputDrivers;
    }

    /**
     *  Need to share the file chooser; otherwise, whenever the user switched
     *  drivers, he would lose the filename he typed in.
     */
    public BasicFileDriverPanel getSharedOpenBasicFileDriverPanel() {
        return sharedOpenBasicFileDriverPanel;
    }

    public BasicFileDriverPanel getSharedSaveBasicFileDriverPanel() {
        return sharedSaveBasicFileDriverPanel;
    }

    public void loadDrivers(WorkbenchProperties properties)
        throws ClassNotFoundException, IllegalAccessException, 
            InstantiationException {
        ArrayList newInputDriverClasses = new ArrayList();
        newInputDriverClasses.addAll(builtInInputDriverClasses);
        newInputDriverClasses.addAll(properties.getInputDriverClasses());
        loadDrivers(newInputDriverClasses, inputDrivers);

        ArrayList newOutputDriverClasses = new ArrayList();
        newOutputDriverClasses.addAll(builtInOutputDriverClasses);
        newOutputDriverClasses.addAll(properties.getOutputDriverClasses());
        loadDrivers(newOutputDriverClasses, outputDrivers);
    }
    
    /**
     * Loads an instantiated, but not yet initialized, InputDriver, and adds it to the list.
     * @param driver an instantiated but not yet initialed, InputDriver 
     */
    public void loadInputDriver( AbstractInputDriver driver ) {
    	driver.initialize( this, errorHandler );
    	inputDrivers.add( driver );
    }

	/**
	 * Loads an instantiated, but not yet initialized OutputDriver, and adds it to the list.
	 * @param driver an instantiated but not yet initialized OutputDriver 
	 */
	public void loadOutputDriver( AbstractOutputDriver driver ) {
		driver.initialize( this, errorHandler );
		outputDrivers.add( driver );
	}

    private void loadDrivers(List driverClasses, List drivers)
        throws ClassNotFoundException, IllegalAccessException, 
            InstantiationException {
        for (Iterator i = driverClasses.iterator(); i.hasNext();) {
            Class driverClass = (Class) i.next();
            AbstractDriver driver = (AbstractDriver) driverClass.newInstance();
            driver.initialize(this, errorHandler);
            drivers.add(driver);
        }
    }

    private static class SharedFileDriverPanel extends BasicFileDriverPanel {
        public SharedFileDriverPanel(String description,
            ErrorHandler errorHandler) {
            super(errorHandler);
            fileNamePanel.setUpperDescription(description);
        }

        public void setFileDescription(String description) {
            Assert.shouldNeverReachHere(
                "Panel is shared; thus description cannot be changed");
        }
    }
}
