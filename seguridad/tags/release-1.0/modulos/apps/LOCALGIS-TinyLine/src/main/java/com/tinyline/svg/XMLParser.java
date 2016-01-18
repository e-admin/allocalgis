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
