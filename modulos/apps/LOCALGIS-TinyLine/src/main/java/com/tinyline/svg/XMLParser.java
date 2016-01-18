/**
 * XMLParser.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.svg;

import java.io.InputStream;

// Referenced classes of package com.tinyline.svg:
//            XMLHandler

public interface XMLParser
{

    public static final int XML_START_DOCUMENT = 0;
    public static final int XML_COMMENT = 1;
    public static final int XML_DOCTYPE = 2;
    public static final int XML_END_DOCUMENT = 8;
    public static final int XML_END_TAG = 16;
    public static final int XML_PROCESSING_INSTRUCTION = 32;
    public static final int XML_START_TAG = 64;
    public static final int XML_TEXT = 128;
    public static final int XML_WHITESPACE = 256;
    public static final int XML_ERR_OK = 0;
    public static final int XML_ERR_IO_EXCEPTION = 1;
    public static final int XML_ERR_UNSUPPORTED_ENCODING = 2;
    public static final int XML_ERR_COMMENT = 8;
    public static final int XML_ERR_UNEXPECTED_EOF = 16;
    public static final int XML_ERR_NAME = 32;
    public static final int XML_ERR_END_TAG = 64;
    public static final int XML_ERR_CDATA = 128;
    public static final int XML_ERR_CHAR_ATTR = 256;
    public static final int XML_ERR_ELEM_TERM = 512;
    public static final int XML_ERR_EQU = 1024;

    public abstract void setInputStream(InputStream inputstream);

    public abstract void setXMLHandler(XMLHandler xmlhandler);

    public abstract void init();

    public abstract void getNext();

    public abstract int getType();

    public abstract int getError();
}
