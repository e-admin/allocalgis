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
 * JRTxtExporter.java
 * 
 * Created on 16 aprile 2004, 10.27
 *
 */

package it.businesslogic.ireport.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;

/**
 *
 * @author  giulio toffoli
 *
 *  This is a really simple exporter with many assumptions on report.
 *  The report page is divided in a grid of 10 pixel x 10 pixel. In each of this
 *  pixels there is a char. 
 *  Elements must be inserted into this grid (so elements must have coords and dims
 *  that are multiple of 10px).
 *   
 */
public class JRTxtExporter extends JRAbstractExporter {
    
        /** Creates a new instance of JRPdfExporter */
        public JRTxtExporter() {
   
        }
        
        int PAGE_ROWS = 61;
        int PAGE_COLUMNS = 255;
        int CHAR_UNIT_H = 10;
        int CHAR_UNIT_V = 20;
        boolean ADD_FORM_FEED = true;
        
	/**
	 *
	 */
	protected String delimiter = null;

	/**
	 *
	 */
	protected Writer writer = null;
	protected JRExportProgressMonitor progressMonitor = null;

	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		
		/*   */
		setInput();

		/*   */
		setPageRange();
                

		String encoding = (String)parameters.get(JRExporterParameter.CHARACTER_ENCODING);
		if (encoding == null)
		{
			encoding = "ISO-8859-1";
		}
		
                
                String page_rows = (String)parameters.get(JRTxtExporterParameter.PAGE_ROWS);
		if (page_rows != null)
		{
			PAGE_ROWS = Integer.parseInt(page_rows);;
		}
                else
                {
                    PAGE_ROWS = this.jasperPrint.getPageHeight()/20;                    
                }
                
                String page_cols = (String)parameters.get(JRTxtExporterParameter.PAGE_COLUMNS);
		if (page_cols != null)
		{
			PAGE_COLUMNS = Integer.parseInt(page_cols);
		}
                else
                {
                    PAGE_COLUMNS = this.jasperPrint.getPageWidth() / this.CHAR_UNIT_H;
                }
                
                Object add_form_feed = parameters.get(JRTxtExporterParameter.ADD_FORM_FEED);
                if ( add_form_feed != null ){
                	
			ADD_FORM_FEED = (add_form_feed+"").equals("true");
		} else{
			ADD_FORM_FEED = true;
		} 

                
                
		delimiter = ",";
		
		StringBuffer sb = (StringBuffer)parameters.get(JRXmlExporterParameter.OUTPUT_STRING_BUFFER);
		if (sb != null)
		{
			try
			{
				writer = new StringWriter();
				exportReportToWriter();
				sb.append(writer.toString());
			}
			catch (IOException e)
			{
				throw new JRException("Error writing to StringBuffer writer : " + jasperPrint.getName(), e);
			}
			finally
			{
				if (writer != null)
				{
					try
					{
						writer.close();
					}
					catch(IOException e)
					{
					}
				}
			}
		}
		else
		{
			writer = (Writer)parameters.get(JRExporterParameter.OUTPUT_WRITER);
			if (writer != null)
			{
				try
				{
					exportReportToWriter();
				}
				catch (IOException e)
				{
					throw new JRException("Error writing to writer : " + jasperPrint.getName(), e);
				}
			}
			else
			{
				OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
				if (os != null)
				{
					try
					{
						writer = new OutputStreamWriter(os, encoding); 
						exportReportToWriter();
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to OutputStream writer : " + jasperPrint.getName(), e);
					}
				}
				else
				{
					File destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
					if (destFile == null)
					{
						String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
						if (fileName != null)
						{
							destFile = new File(fileName);
						}
						else
						{
							throw new JRException("No output specified for the exporter.");
						}
					}

					try
					{
						os = new FileOutputStream(destFile);
						writer = new OutputStreamWriter(os, encoding);
						exportReportToWriter();
					}
					catch (IOException e)
					{
						throw new JRException("Error writing to file writer : " + jasperPrint.getName(), e);
					}
					finally
					{
						if (writer != null)
						{
							try
							{
								writer.close();
							}
							catch(IOException e)
							{
							}
						}
					}
				}
			}
		}
	}


	/**
	 *
	 */
	protected void exportReportToWriter() throws JRException, IOException
	{
		List pages = jasperPrint.getPages();
		if (pages != null && pages.size() > 0)
		{ 
			JRPrintPage page = null;
			
			for(int i = startPageIndex; i <= endPageIndex; i++)
			{
				if (Thread.currentThread().isInterrupted())
				{
					throw new JRException("Current thread interrupted.");
				}
				
				page = (JRPrintPage)pages.get(i);

				/*   */
				exportPage(page);
			}
		}
		
		writer.flush();
	}


	/**
	 *
	 */
	protected void exportPage(JRPrintPage page) throws JRException, IOException
	{
		Vector lines = layoutGrid(page);
                
                int y = 0;
		for(y = 0; y < lines.size(); y++)
		{		
                        String s = (""+lines.elementAt(y));
                        while (s.endsWith(" "))
                        {
                            s = s.substring(0,s.length()-1);
                        }
			writer.write( s ) ;
			writer.write("\n");
		}
                
                while (y < PAGE_ROWS)
                {
                    writer.write("\n");
                    y++;
                }
                if (ADD_FORM_FEED)
                {
                    writer.write("\f");
                }

		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
	}


	/**
	 *      
	 */
	protected Vector layoutGrid(JRPrintPage page)
        {         
                Vector v_lines = new Vector();
                
                String void_line = "";
                for (int i=0; i<PAGE_COLUMNS; ++i)
                {
                    void_line += " ";
                }
                
                for (int i=0; i< PAGE_ROWS ; ++i)
                {
                    v_lines.add(void_line += " ");
                }
                
                List yCuts = yCuts = new ArrayList();
                yCuts.add(new Integer(0));
                yCuts.add(new Integer(jasperPrint.getPageHeight()));

                Integer y = null;

                Collection elems = page.getElements();
                for(Iterator it = elems.iterator(); it.hasNext();)
                {
                            JRPrintElement element = ((JRPrintElement)it.next());

                            if (element instanceof JRPrintText)
                            {
                                    y = new Integer(element.getY());
                                    if (!yCuts.contains(y))
                                    {
                                            yCuts.add(y);
                                    }
                                    y = new Integer(element.getY() + element.getHeight());
                                    if (!yCuts.contains(y))
                                    {
                                            yCuts.add(y);
                                    }
                            }
                }

                Collections.sort(yCuts);
		int yCellCount = yCuts.size() - 1;

                int real_line_num = 0;
                
		for(int j = 0; j < yCellCount; j++)
		{ 
                        if (v_lines.size() <= real_line_num)
                        {
                            v_lines.add( new String( void_line) );
                        }
                        
                        int actual_cut =  ((Integer)(yCuts.get(j))).intValue();
                        
                        int lines_used = 1;
                        // Look for all elements that have Y = actual cut
			for(Iterator it = elems.iterator(); it.hasNext();)
                        {
                            JRPrintElement element = ((JRPrintElement)it.next());
			
                            if (element instanceof JRPrintText)
                            {
                                JRPrintText pt = (JRPrintText)element;
                                
                                if ( pt.getY() == actual_cut)
                                {
                                    // 1. take starting char...
                                    int start_at = pt.getX()/CHAR_UNIT_H;
                                    int len = pt.getWidth()/CHAR_UNIT_H;
                                    
                                    String str = pt.getText();
                                    int line_height = 0;
                                    if (pt.getHeight() <= CHAR_UNIT_V) // Single line object
                                    {   
                                        
                                        if (str.length() > len)
                                        {
                                           str = str.substring(0,len);
                                        }
                                        
                                        if (pt.getY()/CHAR_UNIT_V > real_line_num)
                                        {
                                            real_line_num = pt.getY()/CHAR_UNIT_V;
                                        }
                                        if (v_lines.size() <= real_line_num)
                                        {
                                            v_lines.add( new String( void_line) );
                                        }
                                        String line = (String)v_lines.elementAt( real_line_num );
                                        
                                       
                                        line = replaceLineRegion(line,start_at,len,str,pt.getTextAlignment());
                                        v_lines.setElementAt(line, real_line_num);
                                    }
                                    else // is a multiline...
                                    {
                                        int max_field_lines = 1;

                                        //Vector field_lines = getFieldLines(line_height + " " + pt.getHeight() + " " + max_field_lines + str,len);
                                        Vector field_lines = getFieldLines(str,len);
                                        
                                        // For each line, write the right text...
                                       int nl = 0;
                                       for (nl = 0; nl < field_lines.size(); nl++)
                                       {
                                           String text = (String)field_lines.elementAt(nl);
                                           //text = text.replace(' ','*');
                                           
                                           //text = field_lines.size()+  " " +text;
                                           while (v_lines.size() <= (real_line_num + nl))  v_lines.add( new String( void_line) );
                                           
                                           String line = (String)v_lines.elementAt( real_line_num + nl );
                                           line = replaceLineRegion(line,start_at,len,text,pt.getTextAlignment());
                                           v_lines.setElementAt(line, real_line_num + nl );
                                           if (nl > lines_used) lines_used = nl;
                                       }
                                       
                                       
                                           
                                    }
                                }
                                
				//isRowUsed[y1] = true;
				//isColUsed[x1] = true;
                            }
                        }
                        
                        real_line_num += lines_used;
		}  
                
                return v_lines;
	}
	
        protected String replaceLineRegion(String line,int start_at, int len, String str, byte alignment)
        {
            int side = 0;
            if (alignment == net.sf.jasperreports.engine.JRAlignment.HORIZONTAL_ALIGN_CENTER)
            {
                side = len - str.length();
                if (side > 0)
                {
                    side = side/2; 
                }
            }
            
            for (int k=0; k < side; ++k)
            {
                str = " "+str; 
            }
            
            if (alignment == net.sf.jasperreports.engine.JRAlignment.HORIZONTAL_ALIGN_RIGHT)
            {
                while (str.length() < len) str = " "+str;                            
            }
            
            while (str.length() < len) str += " ";   

            return  line.substring(0,start_at) + str + line.substring(start_at+len);            
        }
        
        
        public Vector getFieldLines(String str, int row_len)
        {
            Vector lines = new Vector();
            
            StringTokenizer st = new StringTokenizer(str,"\n",false);
            while (st.hasMoreTokens())
            {
                String token = st.nextToken();
                while (token.length() > row_len)
                {
                    // Find the last space before character row_len....
                    String tmp_line = token.substring(0, row_len);
                    int li = tmp_line.lastIndexOf(' ');
                    if (token.charAt(row_len) == ' ')
                    {
                        lines.addElement(tmp_line);
                        token = token.substring(row_len).trim();
                    }
                    else if (li == -1)
                    {
                        lines.addElement(tmp_line);
                        token = token.substring(row_len).trim();
                    }
                    else
                    {
                        tmp_line =  token.substring(0, li);
                        lines.addElement(tmp_line);
                        token = token.substring(li+1).trim();
                    }
                    
                }
                if (token.trim().length() > 0)
                {
                    if (lines.size() == 0) lines.addElement(token);
                    else lines.addElement(token.trim());
                }
            }
            //lines.addElement("" +row_len);
            
            return lines;
        }
}
