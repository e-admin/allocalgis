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

import com.geopista.app.AppContext;
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
			
			String texturesDirectoryName = AppContext.getApplicationContext().getUserPreference(TEXTURES_DIRECTORY_PARAMETER,"./com/geopista/ui/images/textures/",true);
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
				result.add(new Texture("file:"+texturesDirectoryName+"/"+theFiles[i].getName(), "image/" + fileType, paint, bufferedImage.getWidth(), bufferedImage.getHeight()));
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
