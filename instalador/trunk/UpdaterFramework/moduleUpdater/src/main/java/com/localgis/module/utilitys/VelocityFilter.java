package com.localgis.module.utilitys;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.VelocityException;

public class VelocityFilter  {

	private VelocityContext context;

	

	public VelocityFilter(Properties templateValues)
	{
		//Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM, this);

		try
		    {
			Velocity.init();
			this.context = new VelocityContext();
			addPropertiesToContext(this.context, templateValues);
		    } catch (Exception e)
		    {
			throw new RuntimeException(e);
		    }
	}

	private void addPropertiesToContext(VelocityContext context, Properties prop) {
		//getLog().debug("Exporting properties to context: " + prop);
		Enumeration propEnumeration;
		if (prop != null) {
			propEnumeration = prop.propertyNames();
			while (propEnumeration.hasMoreElements()) {
				String key = (String) propEnumeration.nextElement();
				String value = prop.getProperty(key);
				//getLog().debug(key + "=" + value);
				context.put(key, value);
			}
		}
	}

//	private void translateFile(File inputFile, VelocityContext context)
//			throws ResourceNotFoundException, VelocityException, IOException {
//		Template template = null;
//		try {
//			template = Velocity.getTemplate(inputFile.g);
//		} catch (Exception e) {
//			//getLog().info("Failed to load: " + inputFile);
//			throw new IOException("Get template failed: " + inputFile, e);
//		}
//		StringWriter sw = new StringWriter();
//		try {
//			template.merge(context, sw);
//		} catch (Exception e) {
//			//getLog().info(
//			//		"Failed to merge: " + inputFile + ":" + e.getMessage());
//			throw new RuntimeException("Fail to merge template: " + inputFile,
//					e);
//
//		}
//
//		File result = new File(outputDirectory, templateFile.getName());
//		File dir = result.getParentFile();
//		if (!dir.exists()) {
//			if (!dir.mkdirs()) {
//				throw new RuntimeException("Failed to create outputDirectory");
//			}
//		}
//
//		FileOutputStream os = new FileOutputStream(result);
//		os.write(sw.toString().getBytes(encoding == null ? "UTF-8" : encoding));
//	}
	public void translateStream(Reader input, Writer output)
			throws ResourceNotFoundException, VelocityException, IOException 
	{
		try {
			VelocityEngine velocity= new VelocityEngine();
			velocity.evaluate(this.context, output, "Filtering:", input);
		} catch (Exception e) {
			//getLog().info(
			//		"Failed to merge: " + inputFile + ":" + e.getMessage());
			throw new RuntimeException("Fail to merge template stream",e);
		}
	}

}
/**
 * VelocityFilter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
