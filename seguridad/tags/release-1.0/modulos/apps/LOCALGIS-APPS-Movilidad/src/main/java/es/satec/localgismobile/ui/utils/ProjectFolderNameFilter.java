package es.satec.localgismobile.ui.utils;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;

import es.satec.localgismobile.fw.Global;

public class ProjectFolderNameFilter implements FilenameFilter {

	private static Logger logger = Global.getLoggerFor(ProjectFolderNameFilter.class);
	
	public boolean accept(File dir, String name) {
		try {
			if (dir.isDirectory()) {
				int dot1 = name.indexOf(".", 0);
				if (dot1 > 0) {
					int dot2 = name.indexOf(".", dot1+1);
					if (dot2 - dot1 > 1) {
						String idMunicipio = name.substring(dot1+1, dot2);
						Integer.parseInt(idMunicipio);
						String timestamp = name.substring(dot2+1, name.length());
						Long.parseLong(timestamp);
						return true;
					}
				}
			}
		} catch (Exception e) {}
			logger.warn("Carpeta de proyecto no válida: " + name);
		return false;
	}

}
