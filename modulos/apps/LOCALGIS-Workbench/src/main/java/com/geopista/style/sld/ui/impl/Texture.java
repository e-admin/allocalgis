/**
 * Texture.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 13-ago-2004
 */
package com.geopista.style.sld.ui.impl;

import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import com.geopista.util.config.UserPreferenceStore;
import com.sun.media.jai.codec.MemoryCacheSeekableStream;

/**
 * @author Enxenio, SL
 */
public class Texture {
	public final static String TEXTURES_DIRECTORY_PARAMETER =
			"SLD.Textures.Directory";
				
	public static Object[] createTextures() {
		List result = new ArrayList(); 
		
		result.add(new Texture("", "", null, 0, 0));
		
		try {
			
			String texturesDirectoryName = UserPreferenceStore.getUserPreference(TEXTURES_DIRECTORY_PARAMETER,"./com/geopista/ui/images/textures/",true);
			texturesDirectoryName=texturesDirectoryName+File.separator+"textures";
			File texturesDirectory = new File(texturesDirectoryName);
			createTextures(result, texturesDirectoryName, "jpg");
			createTextures(result, texturesDirectoryName, "png");
			createTextures(result, texturesDirectoryName, "gif");
			createTextures(result, texturesDirectoryName, "bmp");
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return result.toArray();
	}
	
	private static void createTextures(List result, String texturesDirectoryName, final String fileType) {

		File texturesDirectory = new File(texturesDirectoryName);
		File[] theFiles = texturesDirectory.listFiles(new FileFilter(){
			public boolean accept(File file) {
				if (file.isFile()) {
					if (file.getName().toLowerCase().endsWith("." + fileType)) {
						return true;
					}
				}
				return false;
			}
		});
			
		for (int i=0; i<theFiles.length; i++) {
			try {
				MemoryCacheSeekableStream mcss = new MemoryCacheSeekableStream(new FileInputStream(theFiles[i]));
				RenderedOp rop = JAI.create( "stream", mcss );
				BufferedImage bufferedImage = rop.getAsBufferedImage();
				mcss.close();
				Paint paint = new TexturePaint(bufferedImage, new Rectangle2D.Double(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight()));
				//result.add(new Texture("file:"+texturesDirectoryName+"/"+theFiles[i].getName(), "image/" + fileType, paint, bufferedImage.getWidth(), bufferedImage.getHeight()));
				result.add(new Texture("file:"+"textures/"+theFiles[i].getName(), "image/" + fileType, paint, bufferedImage.getWidth(), bufferedImage.getHeight()));
			}
			catch (FileNotFoundException e){
				e.printStackTrace();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}
			
	public Texture(String url, String format, Paint paint, int width, int height) {
		_paint = paint;
		_url = url;
		_format = format;
		_width = width;
		_height = height;
	}
	
	public Paint getPaint() {
		return _paint;
	}

	public String getURL() {
		return _url;
	}

	public String getFormat() {
		return _format;
	}

	public int getWidth() {
		return _width;
	}

	public int getHeight() {
		return _height;
	}
	
	private Paint _paint;
	private String _url;
	private String _format;
	private int _width;
	private int _height;
}
