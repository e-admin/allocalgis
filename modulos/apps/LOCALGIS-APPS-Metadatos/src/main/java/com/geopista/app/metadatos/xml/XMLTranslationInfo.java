/**
 * XMLTranslationInfo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.xml;

import java.io.File;
import java.io.OutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import com.geopista.app.metadatos.xml.destino.DestinoXMLDom;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 06-sep-2004
 * Time: 16:27:49
 */
public class XMLTranslationInfo
{

    public XMLTranslationInfo(Reader input, boolean validating_mode)
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

    public XMLTranslationInfo(OutputStream output, boolean validating_mode)
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

    public XMLTranslationInfo(String nombreFich, boolean validating_mode) throws Exception
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

    public DestinoXMLDom getDestinoXML()
    {
            return _destinoXML;
     }



    public static final String DTD_FGDC = "http://www.fgdc.gov/metadata/fgdc-std-001-1998.dtd";
    public static final String DTD_IAAA = "http://iaaa.cps.unizar.es/XMLMetaData/fgdc-std-001-1998.dtd";
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
    private DestinoXMLDom _destinoXML;


}