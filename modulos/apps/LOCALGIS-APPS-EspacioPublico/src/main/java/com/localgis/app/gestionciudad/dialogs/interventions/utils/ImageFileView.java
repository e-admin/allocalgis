/**
 * ImageFileView.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.utils;

/**
 * @author javieraragon
 *
 */
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

public class ImageFileView extends FileView {
	ImageIcon jpgIcon = new ImageIcon("images/jpgIcon.gif");
	ImageIcon gifIcon = new ImageIcon("images/gifIcon.gif");
	ImageIcon tiffIcon = new ImageIcon("images/tiffIcon.gif");

	public String getName(File f) {
		return null; // let the L&F FileView figure this out
	}

	public String getDescription(File f) {
		return null; // let the L&F FileView figure this out
	}

	public Boolean isTraversable(File f) {
		return null; // let the L&F FileView figure this out
	}

	public String getTypeDescription(File f) {
		String extension = getExtension(f);
		String type = null;

		if (extension != null) {
			if (extension.equals("jpeg") ||
					extension.equals("jpg")) {
				type = "JPEG Image";
			} else if (extension.equals("gif")){
				type = "GIF Image";
			} else if (extension.equals("tiff") ||
					extension.equals("tif")) {
				type = "TIFF Image";
			} 
		}
		return type;
	}

	public Icon getIcon(File f) {
		String extension = getExtension(f);
		Icon icon = null;
		if (extension != null) {
			if (extension.equals("jpeg") ||
					extension.equals("jpg")) {
				icon = jpgIcon;
			} else if (extension.equals("gif")) {
				icon = gifIcon;
			} else if (extension.equals("tiff") ||
					extension.equals("tif")) {
				icon = tiffIcon;
			} 
		}
		return icon;
	}

	// Get the extension of this file. Code is factored out
	// because we use this in both getIcon and getTypeDescription
	private String getExtension(File f) {

		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 &&  i < s.length() - 1) {
			ext = s.substring(i+1).toLowerCase();
		}
		return ext;
	}
}

