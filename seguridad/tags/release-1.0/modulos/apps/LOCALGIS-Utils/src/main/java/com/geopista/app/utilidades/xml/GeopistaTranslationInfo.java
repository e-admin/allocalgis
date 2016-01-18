package com.geopista.app.utilidades.xml;


import java.io.Reader;
import java.io.OutputStream;
import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 06-sep-2004
 * Time: 16:27:49
 */


public class GeopistaTranslationInfo {

    public GeopistaTranslationInfo(Reader input, boolean validating_mode)
        throws Exception
    {
        _url = null;
        _url_as_string = null;
        _useDefaultValues = false;
        _dtd = null;
        _encoding = "ISO-8859-1";
        _xml_version = "1.0";
        _xsl = null;
        _dtdValidated = false;
        setInputReader(input);
        setValidatingMode(validating_mode);
    }
    public GeopistaTranslationInfo(java.net.URL url, boolean validating_mode)
    {
         _url = url;
        _url_as_string = url.toString();
        _useDefaultValues = false;
        _dtd = null;
        _encoding = "ISO-8859-1";
        _xml_version = "1.0";
        _xsl = null;
        _dtdValidated = false;
        setValidatingMode(validating_mode);

    }

    public GeopistaTranslationInfo(OutputStream output, boolean validating_mode)
        throws Exception
    {
        _url = null;
        _url_as_string = null;
        _useDefaultValues = false;
        _dtd = null;
        _encoding = "ISO-8859-1";
        _xml_version = "1.0";
        _xsl = null;
        _dtdValidated = false;
        setOutputWriter(output);
        setValidatingMode(validating_mode);
    }

    public GeopistaTranslationInfo(String nombreFich, boolean validating_mode) throws Exception
    {
        _url = null;
        _url_as_string = null;
        _useDefaultValues = false;
        _dtd = null;
        _encoding = "ISO-8859-1";
        _xml_version = "1.0";
        _xsl = null;
        _dtdValidated = false;
        try
        {
            _url = createURL(nombreFich);
            _url_as_string = nombreFich;
        }catch(Exception e)
        {
            System.out.println(" Excepci\363n al obtener el URL a partir del fichero.");
            throw e;
        }
        setValidatingMode(validating_mode);
        return;
    }

    public static URL createURL(String fileName)
        throws MalformedURLException
    {
        try
        {
          URL url = new URL(fileName);
          return url;
        }
        catch(MalformedURLException ex)
        {
                File f = new File(fileName);
                String path = f.getAbsolutePath();
                String fs = System.getProperty("file.separator");
                if(fs.length() == 1)
                {
                     char sep = fs.charAt(0);
                     if  (sep != '/')
                         path = path.replace(sep, '/');
                    if  (path.charAt(0) != '/')
                     path = '/' + path;
                }
                path = "file://" + path;
                URL url = new URL(path);
                return url;
           }
    }

    public String getDTD()
    {
        return _dtd;
    }

    public String getDataEncoding()
    {
        return _encoding;
    }

    public Reader getInputReader()
    {
        return _inputReader;
    }

    public OutputStream getOutputWriter()
    {
        return _outputWriter;
    }

    public URL getURL()
    {
        return _url;
    }

    public String getURLasString()
    {
        return _url_as_string;
    }

    public boolean getUseDefaultValues()
    {
        return _useDefaultValues;
    }

    public boolean getValidatingMode()
    {
        return _dtdValidated;
    }

    public String getXSL()
    {
        return _xsl;
    }

    public String getXmlVersion()
    {
        return _xml_version;
    }

    public void setDTD(String value)
    {
        _dtd = value;
    }

    public void setDataEncoding(String data_encoding)
    {
        _encoding = data_encoding;
    }

    public void setInputReader(Reader inputReader)
    {
        _inputReader = inputReader;
    }

    public void setOutputWriter(OutputStream outputWriter)
    {
        _outputWriter = outputWriter;
    }

    public void setUseDefaultValues(boolean defaultValues)
    {
        _useDefaultValues = defaultValues;
    }

    public void setValidatingMode(boolean validating_mode)
    {
        _dtdValidated = validating_mode;
    }

    public void setXSL(String value)
    {
        _xsl = value;
    }

    public void setXmlVersion(String xml_version)
    {
        _xml_version = xml_version;
    }

    
    public static final String ISO_8859_1 = "ISO-8859-1";
    private String _dtd;
    private boolean _dtdValidated;
    private String _encoding;
    private Reader _inputReader;
    private OutputStream _outputWriter;
    private URL _url;
    private String _url_as_string;
    private boolean _useDefaultValues;
    private String _xml_version;
    private String _xsl;

}