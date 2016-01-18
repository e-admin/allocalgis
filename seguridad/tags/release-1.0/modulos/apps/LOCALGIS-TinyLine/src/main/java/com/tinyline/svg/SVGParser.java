package com.tinyline.svg;

import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyVector;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.StringTokenizer;

//Referenced classes of package com.tinyline.svg:
//b, SVGNode, SVGFontElem, SVGTextElem, 
//XMLHandler, XMLParser, SVGDocument, SVG, 
//SVGAttr

public class SVGParser
implements XMLHandler
{
	int _fldnew;
	SVGAttr _flddo;
	SVGDocument _fldif;
	SVGNode a;
	int _fldfor;
	int _fldbyte;
	TinyVector _fldint;
	XMLParser _fldtry;
	private TinyString unknownAttName;

	public SVGParser(SVGAttr svgattr)
	{
		_fldtry = new b();
		_fldtry.setXMLHandler(this);
		_fldint = new TinyVector(10);
		_flddo = svgattr;
	}

	public static XMLParser createXMLParser()
	{
		return new b();
	}

	public final int load(SVGDocument svgdocument, InputStream inputstream)
	{
		int i = 0;
		_fldnew = 0;
		_fldfor = _fldbyte = 0;
		_fldtry.init();
		_fldtry.setInputStream(inputstream);
		_fldif = svgdocument;
		a = _fldif.root;
		_fldint.count = 0;
		a(a);
		do
		{
			_fldtry.getNext();
			if(_fldnew != 0)
			{
				break;
			}
			i = _fldtry.getError();
		} while(i == 0 && _fldtry.getType() != 8);
		if((_fldnew != 0 || i != 0) && i != 0)
		{
			_fldnew |= i << 10;
		}
		return _fldnew;
	}

	public void startDocument()
	{
	}

	public void endDocument()
	{
	}

	public void startElement(char ac[], int i, int j)
	{
		_fldfor = TinyString.getIndex(SVG.ELEMENTS, ac, i, j);
		if(_fldfor != 30)
		{
			a = _fldif.createElement(_fldfor);
		}
		switch(a.helem)
		{
		case 30: // '\036'
		return;
		}
		SVGNode svgnode = (SVGNode)_mthif();
		if(svgnode == null)
		{
			_fldnew |= 0x10;
			return;
		} else
		{
			svgnode.addChild(a, -1);
			a(a);
			return;
		}
	}

	public void endElement()
	{
		if(a.helem == 9)
		{
			SVGFontElem svgfontelem = (SVGFontElem)a;
			svgfontelem.init();
			SVGDocument.addFont(_fldif, svgfontelem);
		}
		if(a.helem != 30)
		{
			a = (SVGNode)a();
		}
		if(a == null)
		{
			_fldnew |= 0x10;
			return;
		} else
		{
			return;
		}
	}

	public void attributeName(char ac[], int i, int j)
	{
		_fldbyte = TinyString.getIndex(SVG.ATTRIBUTES, ac, i, j);

		if (_fldbyte == SVG.ATT_UNKNOWN) {
			unknownAttName = new TinyString (ac, i,j);
		}
	}

	public void attributeValue(char ac[], int i, int j)
	{
		int k = 0;

		if (_fldbyte==SVG.ATT_UNKNOWN) {
			// Recuperar el registro de cambios o los atributos propios
			if (unknownAttName != null) {
				String attName = new String(unknownAttName.data);
				String attValue = new String(ac, i, j);
				try {
					if (attName.equals(SVG.ATT_CHANGE_TYPE)) {
						if (a.changeEvent == null) a.changeEvent = new SVGChangeEvent();
						a.changeEvent.setChangeType(Integer.parseInt(attValue));
					}
					else if (attName.equals(SVG.ATT_CHANGE_TIMESTAMP)) {
						if (a.changeEvent == null) a.changeEvent = new SVGChangeEvent();
						a.changeEvent.setTimestamp(Long.parseLong(attValue));
					}
					else if (attName.equals(SVG.ATT_EDITABLE)) {
						a.setEditable(Boolean.valueOf(attValue).booleanValue());
					}
					else if (attName.equals(SVG.ATT_ACTIVE)) {
						a.setActive(Boolean.valueOf(attValue).booleanValue());
					}
					else if (attName.equals(SVG.ATT_GROUP)) {
						a.setGroup(Integer.parseInt(attValue));
					}
					else if (attName.equals(SVG.ATT_IMAGEURLS)) {
						StringTokenizer tokenizer = new StringTokenizer(attValue, " ");
						while (tokenizer.hasMoreTokens()) {
							String im = URLDecoder.decode(tokenizer.nextToken(), "UTF-8");
							a.addImageURL(im);
						}
					}
					else if (attName.equals(SVG.ATT_MODIFIED)) {
						a.ownerDocument.setModified(Boolean.valueOf(attValue).booleanValue());
					}
					else if (attName.equals(SVG.ATT_SYSTEMID) && a instanceof SVGGroupElem) {
						((SVGGroupElem) a).setSystemId(attValue);
					}
					else if (attName.equals(SVG.ATT_GEOMETRYTYPE) && a instanceof SVGGroupElem) {
						((SVGGroupElem) a).setGeometryType(attValue);
					}
					else {
						a.nameAtts.addElement(attValue);
					}
					unknownAttName = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else {
			Object obj = _flddo.attributeValue(_fldfor, _fldbyte, ac, i, j);
			try
			{
				k |= a.setAttribute(_fldbyte, obj);
			}
			catch(Exception exception) { }
		}
	}

	/*public void attributeValue(char ac[], int i, int j)
    {
           int k = 0;
        Object obj = _flddo.attributeValue(_fldfor, _fldbyte, ac, i, j);
        try
        {
            k |= a.setAttribute(_fldbyte, obj);
        }
        catch(Exception exception) { }
    }*/

	public void charData(char ac[], int i, int j)
	{
		if(a.helem == 32)
		{
			((SVGTextElem)a).setText(ac, 0, j);
		}
		else if (a.helem == 20) {
			((SVGMetadataElem)a).setContent(ac, 0, j);
		}
	}

	private final Object a(Object obj)
	{
		_fldint.addElement(obj);
		return obj;
	}

	private final Object a()
	{
		int i = _fldint.count;
		Object obj = _mthif();
		_fldint.removeElementAt(i - 1);
		return obj;
	}

	private final Object _mthif()
	{
		int i = _fldint.count;
		if(i == 0)
		{
			return null;
		} else
		{
			return _fldint.data[i - 1];
		}
	}
}
