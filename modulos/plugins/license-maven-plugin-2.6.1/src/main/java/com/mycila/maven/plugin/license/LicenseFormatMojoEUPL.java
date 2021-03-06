/**
 * Copyright (C) 2008 Mycila (mathieu.carbou@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycila.maven.plugin.license;

import com.mycila.maven.plugin.license.document.Document;
import com.mycila.maven.plugin.license.header.Header;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;

/**
 * Reformat files with a missing header to add it
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Mojo(name = "format_eupl", threadSafe = true)
public final class LicenseFormatMojoEUPL extends AbstractLicenseMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

    	
    	for (int i=0;i<listaProyectosExcluidos.size();i++){
    		String proyecto=(String)listaProyectosExcluidos.get(i);
    		if (basedir.getAbsolutePath().toUpperCase().contains(proyecto.toUpperCase())){
    			getLog().info("Proyecto "+proyecto+" excluido de las lista de proyectos a procesar");
    			return;
    		}
    	}
      	for (int i=0;i<listaProyectosExcluidosGPL.size();i++){
    		String proyecto=(String)listaProyectosExcluidosGPL.get(i);
    		System.out.println("Proyecto:"+proyecto);
    		System.out.println("Base:"+basedir.getAbsolutePath());
    		if (basedir.getAbsolutePath().toUpperCase().contains(proyecto.toUpperCase())){
    			break;
    		}
    		else{
    			getLog().info("Proyecto "+basedir.getAbsolutePath()+" excluido de las lista de proyectos a procesar por ser GPL");
    		}
    	}
    	getLog().info("Updating license headers...");

        //System.out.println("basedir:"+basedir);
        execute(new Callback() {
            @Override
            public void onHeaderNotFound(Document document, Header header) {
                document.parseHeader();
	            if (document.headerDetected()) {
                    if (skipExistingHeaders) {
                        debug("Keeping license header in: %s", document.getFile());
                        return;
                    }
                    document.removeHeader();
                }
                info("Updating license header in: %s", document.getFile());
                //System.out.println("Updating license header in:, document.getFile());
                document.updateHeader(header);
                if (!dryRun) {
                    document.save();
                } else {
                    String name = document.getFile().getName() + ".licensed";
                    File copy = new File(document.getFile().getParentFile(), name);
                    info("Result saved to: %s", copy);
                    document.saveTo(copy);
                }
            }

            @Override
            public void onExistingHeader(Document document, Header header) {
                debug("Header OK in: %s", document.getFile());
            }
        });
    }

}