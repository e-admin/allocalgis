/*
 * Created on 28-sep-2004
 *
 */
package com.geopista.style.sld.ui.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.filechooser.FileFilter;

/**
 * @author enxenio s.l.
 *
 */
public class FilterGraphicFiles extends FileFilter {

	String _extension;
	String _description;
	HashMap _extensions;
	
	public FilterGraphicFiles() {
		_extensions = new HashMap();
		_extensions.put("jpg", this);
		_extensions.put("png", this);
		_extensions.put("bmp", this);
		_extensions.put("gif", this);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		
		boolean correctFile = false;
		if (f.isDirectory()) {
		  	correctFile = true;
		}
		_extension = getExtension(f);
		if (_extension != null) {
		   	if (_extension.equals("jpg") ||
			   	_extension.equals("gif") ||
			   _extension.equals("jpeg") ||
			   _extension.equals("png") ||
			   _extension.equals("bmp")) {
				correctFile = true;
		   }		
		}
		return correctFile;
	}

	public void addExtension(String extension) {
		_extensions.put(extension.toLowerCase(), this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		_description = "Gráficos tipo: ";
		if (!_extensions.isEmpty()) {
			Set extensions = _extensions.keySet();
			Iterator extensionsIterator = extensions.iterator();			
			while (extensionsIterator.hasNext()){
				_description += "." + (String) extensionsIterator.next()+", ";
			}
		}
		return _description;
	}
	
	private static String getExtension(File f) {
			String ext = null;
			String s = f.getName();
			int i = s.lastIndexOf('.');

			if (i > 0 &&  i < s.length() - 1) {
				ext = s.substring(i+1).toLowerCase();
			}
			return ext;
		}

}
