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
 * FontListLoader.java
 * 
 * Created on 22 maggio 2003, 18.45
 *
 */

package it.businesslogic.ireport;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.I18n;
import java.util.*;
/**
 *
 * @author  Administrator
 */
public class FontListLoader {

    /** Creates a new instance of FontListLoader */
    public FontListLoader() {
    }

    public static java.awt.Font loadFont(String file)
    {
        java.awt.Font f = null;
        try {
            f = java.awt.Font.createFont(f.TRUETYPE_FONT,  new java.io.FileInputStream(file));
        }
        catch (IllegalArgumentException ett)
        {
            System.out.println(ett.getMessage() +" No TrueType font");
        }
        catch (java.awt.FontFormatException ef)
        {
            System.out.println(ef.getMessage() +" FontFormatException");
        }
         catch (java.io.IOException ioex)
        {
            System.out.println(ioex.getMessage() +" IOException");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage() +" General Exception");
        }

        return f;
    }

    public static Vector loadTTFFonts()
    {
        return loadTTFFonts(null);
    }
    
    
    public static Vector loadTTFFonts(FontsLoaderMonitor monitor)
    {
        if (monitor != null) monitor.fontsLoadingStarted();
        
        if (monitor != null)
        {
             monitor.fontsLoadingStatusUpdated( I18n.getString("fontsLoader.loadingFontsInit", "Initializing font loader"));
        }
        
        Vector fonts = new Vector();
        
        Vector fontsPaths = MainFrame.getMainInstance().getFontspath();

        System.out.flush();
        if (fontsPaths == null || fontsPaths.size() == 0)
        {
            String path = System.getProperty("java.class.path");
            //path += System.getProperty("path.separator") + "C:\\winnt\\fonts";
            if (path != null && path.length()>0)
            {
               //if (monitor != null)
               //{
               //     monitor.fontsLoadingStatusUpdated( I18n.getString("fontsLoader.loadingFontsFromClasspath", "Loading fonts from classpath"));
               //}
               
               StringTokenizer st = new StringTokenizer(path,  System.getProperty("path.separator") );

                while (st.hasMoreTokens())
                {
                    String path_str = (String)st.nextToken();
                            
                    try { addFont(fonts, path_str, monitor); } catch (Exception ex) { 
                 
                        MainFrame.getMainInstance().logOnConsole(
                                I18n.getFormattedString("fontListLoader.invalidFont", "Invalid font found in: {0}", new Object[]{path_str}));
                    }
                }
            }
        }
        else
        {
            Enumeration e_fonts = fontsPaths.elements();
            //if (monitor != null)
            //{
            //    monitor.fontsLoadingStatusUpdated( I18n.getString("fontsLoader.loadingFontsFromFontpath", "Loading fonts from font path"));
            //}
            
            while (e_fonts.hasMoreElements())
                {
                    String path_str = (String)e_fonts.nextElement();

                    try { addFont(fonts, path_str, monitor); } catch (Exception ex) { 
                 
                        MainFrame.getMainInstance().logOnConsole(
                                I18n.getFormattedString("fontListLoader.invalidFont", "Invalid font found in: {0}", new Object[]{path_str}));
                    }
                }
        }
        
        if (monitor != null) monitor.fontsLoadingFinished();
        return fonts;
    }
    
    
    public static void addFont(Vector fonts, String path_str, FontsLoaderMonitor monitor)
    {
                java.text.MessageFormat formatter = new java.text.MessageFormat(
                        I18n.getString("fontsLoader.loadedFont", "Loaded font {0}"),
                        I18n.getCurrentLocale());
        
                java.io.File file = new java.io.File(path_str);
                if (!file.exists()) {

                    return;
                }
                if (file.isDirectory())
                {
                    String[] files = file.list( new java.io.FilenameFilter(){
                        public boolean accept(java.io.File dir, String filename)
                        {
// modified                          	
//                            return filename.toUpperCase().endsWith(".TTF") ;
                            return filename.toUpperCase().endsWith(".TTF") || filename.toUpperCase().endsWith(".TTC");
                        }
                    });

                    //added begin 
                    com.lowagie.text.pdf.FontMapper fontMapper = new com.lowagie.text.pdf.DefaultFontMapper();
                    
                    if (files == null)
                    {
                        MainFrame.getMainInstance().logOnConsole(
                                I18n.getFormattedString("fontListLoader.unableToListFiles", "Unable to list files in: {0}", new Object[]{""+file}));
                        return;
                    }
                    for (int i=0; i<files.length; ++i)
                    {
			if (files[i].toUpperCase().endsWith(".TTC")) {
    				try {
        				String[] names = com.lowagie.text.pdf.BaseFont.enumerateTTCNames(file.getPath() + file.separator + files[i]);
        				for (int a = 0; a < names.length; a++) {
            				       
                                                java.awt.Font f = fontMapper.pdfToAwt( com.lowagie.text.FontFactory.getFont( file.getPath() + file.separator + files[i]).getBaseFont(), 10 );
            					if (f != null) {
	                				fonts.addElement(new IRFont(f, files[i]));
        	    				}
            					else
                                                {
                                                    MainFrame.getMainInstance().logOnConsole(
                                                        I18n.getFormattedString("fontListLoader.failedLoadingFont", "Failed to load font {0}", new Object[]{""+file.getPath() + file.separator +files[i]}));
                                                }
                					
                                                        
                                                if (monitor != null)
                                                {
                                                    monitor.fontsLoadingStatusUpdated( 
                                                            formatter.format( new Object[]{  file.getPath() + file.separator + files[i] }));
                                                }
                                        }
    				} catch(com.lowagie.text.DocumentException de) {
	        			System.out.println(de);
    				} catch(java.io.IOException ioe) {
        				System.out.println(ioe);
                                }
			} else {
			//added end
                            
                            
                            java.awt.Font f = loadFont( file.getPath() + file.separator + files[i]);
                            if (f != null)
                            {
                                fonts.addElement(new IRFont(f, files[i]));
                                //System.out.println(""+ f.getFamily() + " " + f.getFontName() +" ("+file.getPath() + file.separator +files[i]+")");
                            }
                            else
                            {
                                MainFrame.getMainInstance().logOnConsole(
                                                        I18n.getFormattedString("fontListLoader.failedLoadingFont", "Failed to load font {0}", new Object[]{""+file.getPath() + file.separator +files[i]}));
                            }
                            
                                
                            if (monitor != null)
                            {
                                monitor.fontsLoadingStatusUpdated( 
                                        formatter.format( new Object[]{  file.getPath() + file.separator + files[i] }));
                            }
                        }
                        
                        
                    }
                } else if (path_str.toUpperCase().endsWith(".TTF"))
                {
                    // Try to load this file...
                    System.out.println("m"+ path_str);
                }
                else if (path_str.toUpperCase().endsWith(".TTC"))
                {
                    // Try to load this file...
                    System.out.println("TTC: "+ path_str);
                }
    }

}
