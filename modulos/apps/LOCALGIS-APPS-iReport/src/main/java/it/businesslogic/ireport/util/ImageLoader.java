/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * ImageLoader.java
 * 
 */

package it.businesslogic.ireport.util;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.w3c.tools.codec.Base64Decoder;

public class ImageLoader {

		public static java.awt.Image loadFromBase64String( String imageSource ) throws net.sf.jasperreports.engine.JRException
		{
			Image _image = null;
			try {
		
				ByteArrayInputStream bais = new ByteArrayInputStream(imageSource.getBytes("UTF-8"));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				Base64Decoder decoder = new Base64Decoder(bais, baos);
				decoder.process();
						
				_image = Toolkit.getDefaultToolkit().createImage( baos.toByteArray() );
		
				MediaTracker traker = new MediaTracker(new Panel());
				traker.addImage(_image, 0);
			
				traker.waitForID(0);
			}
			catch (Exception e)
			{
				//image = null;
				e.printStackTrace();
				System.out.flush();
				throw new net.sf.jasperreports.engine.JRException(e);
			}
			
			return _image;
		}
		
}		
