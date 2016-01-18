package com.tinyline.svg;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.tinyline.tiny2d.*;

//Referenced classes of package com.tinyline.svg:
//SVGGradientElem, SVG, SVGDocument, SVGRaster

public abstract class SVGNode
{

	public int helem;
	public SVGDocument ownerDocument;
	public SVGNode parent;
	public TinyVector children;
	public TinyVector animatedVals;
	public TinyString id;
	public int xmlSpace;
	public TinyColor color;
	public TinyColor fill;
	public int fillRule;
	public TinyColor stroke;
	public int strokeDashArray[];
	public int strokeDashOffset;
	public int strokeLineCap;
	public int strokeLineJoin;
	public int strokeMiterLimit;
	public int strokeWidth;
	public int visibility;
	public int display;
	public TinyString fontFamily;
	public int fontSize;
	public int fontStyle;
	public int fontWeight;
	public int textAnchor;
	public int fillOpacity;
	public int stopOpacity;
	public int strokeOpacity;
	public int opacity;
	public TinyColor stopColor;
	public TinyTransform transform;
	TinyString _flddo;
	TinyString _fldif;
	TinyString a;
	public TinyRect bounds;
	public boolean outlined;
	public Vector nameAtts = new Vector();
	/** Evento para almacenar el registro de cambios en el nodo */
	public SVGChangeEvent changeEvent;
	/** Indica si se permite hacer cambios en el nodo */
	public boolean editable = false;
	
	/** Indica si el grupo esta activo*/
	public boolean active = false;

	/** Atributo que marca al grupo que pertenece una feature en caso de tener alguno 
	 *  Dicho atributo aparecería en caso de dividir una calle en segmentos x ejemplo 
	 *  donde en caso de modificar un segmento el cambio se debería de propagar a todo el grupo
	 **/
	public int group = -1;
	/** URLs de imagenes asociadas a un nodo */
	public Vector imageURLs;

	public SVGNode()
	{
		helem = 35;
		ownerDocument = null;
		parent = null;
		children = new TinyVector(4);
		id = null;
		animatedVals = new TinyVector(4);
		xmlSpace = 18;
		color = TinyColor.INHERIT;
		fill = TinyColor.INHERIT;
		fillRule = 26;
		stroke = TinyColor.INHERIT;
		stopColor = TinyColor.INHERIT;
		strokeDashArray = SVG.VAL_STROKEDASHARRAYINHERIT;
		strokeDashOffset = 0x80000000;
		strokeLineCap = 26;
		strokeLineJoin = 26;
		strokeMiterLimit = 0x80000000;
		strokeWidth = 0x80000000;
		stopOpacity = 0x80000000;
		strokeOpacity = 0x80000000;
		fillOpacity = 0x80000000;
		opacity = 0x80000000;
		visibility = 26;
		display = 26;
		fontFamily = null;
		fontSize = 0x80000000;
		fontStyle = 26;
		fontWeight = 26;
		textAnchor = 26;
		transform = new TinyTransform();
	}

	public SVGNode(SVGNode svgnode)
	{
		helem = svgnode.helem;
		ownerDocument = svgnode.ownerDocument;
		parent = null;
		id = null;
		children = new TinyVector(4);
		int j = svgnode.children.count;
		for(int k = 0; k < j; k++)
		{
			SVGNode svgnode1 = (SVGNode)svgnode.children.data[k];
			if(svgnode1 != null)
			{
				addChild(svgnode1.copyNode(), -1);
			}
		}

		animatedVals = new TinyVector(4);
		color = svgnode.color.copyColor();
		fill = svgnode.fill.copyColor();
		fillRule = svgnode.fillRule;
		stroke = svgnode.stroke.copyColor();
		stopColor = svgnode.stopColor.copyColor();
		if(svgnode.strokeDashArray != null)
		{
			strokeDashArray = svgnode.strokeDashArray;
			strokeDashArray = new int[svgnode.strokeDashArray.length];
			System.arraycopy(svgnode.strokeDashArray, 0, strokeDashArray, 0, strokeDashArray.length);
		}
		strokeDashOffset = svgnode.strokeDashOffset;
		strokeLineCap = svgnode.strokeLineCap;
		strokeLineJoin = svgnode.strokeLineJoin;
		strokeMiterLimit = svgnode.strokeMiterLimit;
		strokeWidth = svgnode.strokeWidth;
		fillOpacity = svgnode.fillOpacity;
		stopOpacity = svgnode.stopOpacity;
		strokeOpacity = svgnode.strokeOpacity;
		opacity = svgnode.opacity;
		visibility = svgnode.visibility;
		display = svgnode.display;
		if(svgnode.fontFamily != null)
		{
			fontFamily = new TinyString(svgnode.fontFamily.data);
		}
		fontSize = svgnode.fontSize;
		fontStyle = svgnode.fontStyle;
		fontWeight = svgnode.fontWeight;
		textAnchor = svgnode.textAnchor;
		transform = new TinyTransform(svgnode.transform);
		_flddo = svgnode._flddo;
		_fldif = svgnode._fldif;
		a = svgnode.a;
	}
	//Metodo que devuelve la posicion en el vector del nombre que se ha pasado
	public int getPosByNameLayertAtt (String name) {
		int val = -1;
		boolean exist=false;
		for(int i=0;i<nameAtts.size() && !exist;i++){
			if(nameAtts.elementAt(i).equals(name)){
				val=i;
				exist=true;
			}
		}
		return val;
	}
	
	//NUEVO
	//BORRAR getPosByContainsNameLayertAtt
	public int getPosByContainsNameLayertAtt (String name) {
		int val = -1;
		boolean exist=false;
		for(int i=0;i<nameAtts.size() && !exist;i++){
			if(nameAtts.elementAt(i).toString().contains(name)){
				val=i;
				exist=true;
			}
		}
		return val;
	}
	
	//Metodo que devuelve las posiciones en el ArrayList de los nombres que se han pasado
	public ArrayList getPosByNameLayertAtt (ArrayList names) {
		ArrayList pos = new ArrayList();
		boolean exist=false;
		Iterator it = names.iterator();
		while(it.hasNext()){
			String name = (String) it.next();
			for(int i=0;i<nameAtts.size() && !exist;i++){	
				if(nameAtts.elementAt(i).equals(name)){
					pos.add(Integer.valueOf(i)); //i+1 (por que emoieza en 0)
					exist=true;
				}
			}
			exist=false;
		}
		return pos;
	}
	
	//me devuelve los valores del vector en esas posiciones
	public HashMap getValueLayertAtt (ArrayList positions) {
		HashMap values = new HashMap();
		Iterator it = positions.iterator();
		while(it.hasNext()){
			Integer pos = (Integer) it.next();
		    try {
		    	values.put((pos), this.nameAtts.elementAt(pos.intValue()).toString());
		    }
		    catch (NullPointerException e) {		    	
		    }
		    catch (ArrayIndexOutOfBoundsException e) {		    	
		    }
		}
		return values;
	}
	//FIN NUEVO
	
	//me devuelve el valor del vector en esa posicion
	public String getValueLayertAtt (int pos) {		
	    try {
	    	return (String) this.nameAtts.elementAt(pos);
	    }
	    catch (ArrayIndexOutOfBoundsException e) {
	    	return null;
	    }	
	}

	public void addChild(SVGNode svgnode, int j)
	{
		if(j < 0)
		{
			children.addElement(svgnode);
		} else
		{
			children.insertElementAt(svgnode, j);
		}
		svgnode.parent = this;
		svgnode.ownerDocument = ownerDocument;
		ownerDocument._flddo = true;
	}

	public int removeChild(int j)
	{
		int k = children.removeElementAt(j);
		if(k == 0)
		{
			ownerDocument._flddo = true;
		}
		return k;
	}

	public abstract void paint(SVGRaster svgraster);

	public abstract int createOutline();

	public SVGNode copyExtendedNode() {
		SVGNode copy = copyNode();
		// Copiar atributos extendidos
		Enumeration e = nameAtts.elements();
		while (e.hasMoreElements()) {
			copy.nameAtts.addElement(e.nextElement());
		}
		copy.editable = editable;
		return copy;
	}
	
	public abstract SVGNode copyNode();

	public int setAttribute(int j, Object obj)
	throws Exception
	{
		outlined = false;
		switch(j)
		{
		case 15: // '\017'
		color = (TinyColor)obj;
		break;

		case 25: // '\031'
		fill = (TinyColor)obj;
		break;

		case 27: // '\033'
			fillRule = ((TinyNumber)obj).val;
			break;

		case 82: // 'R'
			stroke = (TinyColor)obj;
			break;

		case 83: // 'S'
			strokeDashArray = (int[])obj;
			break;

		case 84: // 'T'
			strokeDashOffset = ((TinyNumber)obj).val;
			break;

		case 85: // 'U'
			strokeLineCap = ((TinyNumber)obj).val;
			break;

		case 86: // 'V'
			strokeLineJoin = ((TinyNumber)obj).val;
			break;

		case 87: // 'W'
			strokeMiterLimit = ((TinyNumber)obj).val;
			break;

		case 89: // 'Y'
			strokeWidth = ((TinyNumber)obj).val;
			break;

		case 26: // '\032'
			fillOpacity = ((TinyNumber)obj).val;
			break;

		case 88: // 'X'
			strokeOpacity = ((TinyNumber)obj).val;
			break;

		case 56: // '8'
			opacity = ((TinyNumber)obj).val;
			break;

		case 78: // 'N'
			stopColor = (TinyColor)obj;
			break;

		case 79: // 'O'
			stopOpacity = ((TinyNumber)obj).val;
			break;

		case 22: // '\026'
			display = ((TinyNumber)obj).val;
			break;

		case 106: // 'j'
			visibility = ((TinyNumber)obj).val;
			break;

		case 28: // '\034'
			fontFamily = (TinyString)obj;
			break;

		case 29: // '\035'
			fontSize = ((TinyNumber)obj).val;
			break;

		case 92: // '\\'
			textAnchor = ((TinyNumber)obj).val;
			break;

		case 94: // '^'
			transform = (TinyTransform)obj;
			break;

		case 90: // 'Z'
			a = (TinyString)obj;
			break;

		case 68: // 'D'
			_fldif = (TinyString)obj;
			break;

		case 69: // 'E'
			_flddo = (TinyString)obj;
			break;

		case 44: // ','
			id = (TinyString)obj;
			break;

		case 122: // 'z'
			xmlSpace = ((TinyNumber)obj).val;
			break;

		case 17: // '\021'
		case 18: // '\022'
		case 19: // '\023'
		case 20: // '\024'
		case 21: // '\025'
		case 23: // '\027'
		case 24: // '\030'
		case 30: // '\036'
		case 32: // ' '
		case 34: // '"'
		case 35: // '#'
		case 36: // '$'
		case 37: // '%'
		case 38: // '&'
		case 39: // '\''
		case 40: // '('
		case 41: // ')'
		case 42: // '*'
		case 43: // '+'
		case 45: // '-'
		case 46: // '.'
		case 47: // '/'
		case 48: // '0'
		case 49: // '1'
		case 50: // '2'
		case 51: // '3'
		case 52: // '4'
		case 53: // '5'
		case 54: // '6'
		case 55: // '7'
		case 57: // '9'
		case 58: // ':'
		case 59: // ';'
		case 60: // '<'
		case 61: // '='
		case 62: // '>'
		case 63: // '?'
		case 64: // '@'
		case 65: // 'A'
		case 66: // 'B'
		case 67: // 'C'
		case 70: // 'F'
		case 71: // 'G'
		case 72: // 'H'
		case 73: // 'I'
		case 74: // 'J'
		case 75: // 'K'
		case 76: // 'L'
		case 77: // 'M'
		case 80: // 'P'
		case 81: // 'Q'
		case 91: // '['
		case 93: // ']'
		case 95: // '_'
		case 96: // '`'
		case 97: // 'a'
		case 98: // 'b'
		case 99: // 'c'
		case 100: // 'd'
		case 101: // 'e'
		case 102: // 'f'
		case 103: // 'g'
		case 104: // 'h'
		case 105: // 'i'
		case 107: // 'k'
		case 108: // 'l'
		case 109: // 'm'
		case 110: // 'n'
		case 111: // 'o'
		case 112: // 'p'
		case 113: // 'q'
		case 114: // 'r'
		case 115: // 's'
		case 116: // 't'
		case 117: // 'u'
		case 118: // 'v'
		case 119: // 'w'
		case 120: // 'x'
		case 121: // 'y'
		default:
			return 8;

		case 16: // '\020'
		case 31: // '\037'
		case 33: // '!'
			break;
		}
		return 0;
	}

	public Object getAttribute(int j)
	{
		int k = 0;
		Object obj = null;
		switch(j)
		{
		case 15: // '\017'
			obj = color;
			break;

		case 25: // '\031'
			obj = fill;
			break;

		case 27: // '\033'
			k = fillRule;
			break;

		case 82: // 'R'
			obj = stroke;
			break;

		case 83: // 'S'
			obj = strokeDashArray;
			break;

		case 84: // 'T'
			k = strokeDashOffset;
			break;

		case 85: // 'U'
			k = strokeLineCap;
			break;

		case 86: // 'V'
			k = strokeLineJoin;
			break;

		case 87: // 'W'
			k = strokeMiterLimit;
			break;

		case 89: // 'Y'
			k = strokeWidth;
			break;

		case 26: // '\032'
			k = fillOpacity;
			break;

		case 88: // 'X'
			k = strokeOpacity;
			break;

		case 56: // '8'
			k = opacity;
			break;

		case 78: // 'N'
			obj = stopColor;
			break;

		case 79: // 'O'
			k = strokeOpacity;
			break;

		case 22: // '\026'
			k = display;
			break;

		case 106: // 'j'
			k = visibility;
			break;

		case 28: // '\034'
			obj = fontFamily;
			break;

		case 29: // '\035'
			k = fontSize;
			break;

		case 92: // '\\'
			k = textAnchor;
			break;

		case 94: // '^'
			obj = transform;
			break;

		case 90: // 'Z'
			obj = a;
			break;

		case 68: // 'D'
			obj = _fldif;
			break;

		case 69: // 'E'
			obj = _flddo;
			break;

		case 44: // ','
			obj = id;
			break;

		case 122: // 'z'
			k = xmlSpace;
			break;

		case 17: // '\021'
		case 18: // '\022'
		case 19: // '\023'
		case 20: // '\024'
		case 21: // '\025'
		case 23: // '\027'
		case 24: // '\030'
		case 30: // '\036'
		case 32: // ' '
		case 34: // '"'
		case 35: // '#'
		case 36: // '$'
		case 37: // '%'
		case 38: // '&'
		case 39: // '\''
		case 40: // '('
		case 41: // ')'
		case 42: // '*'
		case 43: // '+'
		case 45: // '-'
		case 46: // '.'
		case 47: // '/'
		case 48: // '0'
		case 49: // '1'
		case 50: // '2'
		case 51: // '3'
		case 52: // '4'
		case 53: // '5'
		case 54: // '6'
		case 55: // '7'
		case 57: // '9'
		case 58: // ':'
		case 59: // ';'
		case 60: // '<'
		case 61: // '='
		case 62: // '>'
		case 63: // '?'
		case 64: // '@'
		case 65: // 'A'
		case 66: // 'B'
		case 67: // 'C'
		case 70: // 'F'
		case 71: // 'G'
		case 72: // 'H'
		case 73: // 'I'
		case 74: // 'J'
		case 75: // 'K'
		case 76: // 'L'
		case 77: // 'M'
		case 80: // 'P'
		case 81: // 'Q'
		case 91: // '['
		case 93: // ']'
		case 95: // '_'
		case 96: // '`'
		case 97: // 'a'
		case 98: // 'b'
		case 99: // 'c'
		case 100: // 'd'
		case 101: // 'e'
		case 102: // 'f'
		case 103: // 'g'
		case 104: // 'h'
		case 105: // 'i'
		case 107: // 'k'
		case 108: // 'l'
		case 109: // 'm'
		case 110: // 'n'
		case 111: // 'o'
		case 112: // 'p'
		case 113: // 'q'
		case 114: // 'r'
		case 115: // 's'
		case 116: // 't'
		case 117: // 'u'
		case 118: // 'v'
		case 119: // 'w'
		case 120: // 'x'
		case 121: // 'y'
		default:
			return null;

		case 16: // '\020'
		case 31: // '\037'
		case 33: // '!'
			break;
		}
		if(obj != null)
		{
			return obj;
		} else
		{
			return new TinyNumber(k);
		}
	}

	public boolean matchUserAgent()
	{
		if(a != null)
		{
			return TinyString.compareTo(a.data, 0, a.count, "en".toCharArray(), 0, 2) == 0;
		}
		if(_flddo != null)
		{
			return true;
		}
		return _fldif == null;
	}

	public TinyColor getCurrentColor()
	{
		if(color != TinyColor.INHERIT)
		{
			return color;
		} else
		{
			return parent == null ? TinyColor.INHERIT : parent.getCurrentColor();
		}
	}

	public TinyColor getFillColor()
	{
		if(fill == TinyColor.CURRENT)
		{
			return getCurrentColor();
		}
		if(fill != TinyColor.INHERIT)
		{
			return resolveColor(fill);
		} else
		{
			return parent == null ? TinyColor.INHERIT : parent.getFillColor();
		}
	}

	public int getFillRule()
	{
		if(fillRule != 26)
		{
			return fillRule;
		} else
		{
			return parent == null ? 36 : parent.getFillRule();
		}
	}

	public TinyColor getStrokeColor()
	{
		if(stroke == TinyColor.CURRENT)
		{
			return getCurrentColor();
		}
		if(stroke != TinyColor.INHERIT)
		{
			return resolveColor(stroke);
		} else
		{
			return parent == null ? TinyColor.INHERIT : parent.getStrokeColor();
		}
	}

	public TinyColor resolveColor(TinyColor tinycolor)
	{
		if(tinycolor.fillType != 5)
		{
			return tinycolor;
		}
		SVGNode svgnode = getNodeById(ownerDocument.root, tinycolor.uri);
		if(svgnode == null)
		{
			return tinycolor;
		}
		if(svgnode.helem == 19 || svgnode.helem == 26)
		{
			SVGGradientElem svggradientelem = (SVGGradientElem)svgnode;
			if(!((SVGNode) (svggradientelem)).outlined)
			{
				svggradientelem.createOutline();
			}
			return svggradientelem.gcolor;
		} else
		{
			return tinycolor;
		}
	}

	public int[] getDashArray()
	{
		if(strokeDashArray != SVG.VAL_STROKEDASHARRAYINHERIT)
		{
			return strokeDashArray;
		} else
		{
			return parent == null ? SVG.VAL_STROKEDASHARRAYNONE : parent.getDashArray();
		}
	}

	public int getDashOffset()
	{
		if(strokeDashOffset != 0x80000000)
		{
			return strokeDashOffset;
		} else
		{
			return parent == null ? 0 : parent.getDashOffset();
		}
	}

	public int getCapStyle()
	{
		if(strokeLineCap != 26)
		{
			return strokeLineCap;
		} else
		{
			return parent == null ? 15 : parent.getCapStyle();
		}
	}

	public int getJoinStyle()
	{
		if(strokeLineJoin != 26)
		{
			return strokeLineJoin;
		} else
		{
			return parent == null ? 33 : parent.getJoinStyle();
		}
	}

	public int getMiterLimit()
	{
		if(strokeMiterLimit != 0x80000000)
		{
			return strokeMiterLimit;
		} else
		{
			return parent == null ? '\u0400' : parent.getMiterLimit();
		}
	}

	public int getLineThickness()
	{
		if(strokeWidth != 0x80000000)
		{
			return strokeWidth;
		} else
		{
			return parent == null ? '\u0100' : parent.getLineThickness();
		}
	}

	public int getFillOpacity()
	{
		if(fillOpacity != 0x80000000)
		{
			return fillOpacity;
		} else
		{
			return parent == null ? '\377' : parent.getFillOpacity();
		}
	}

	public int getStrokeOpacity()
	{
		if(strokeOpacity != 0x80000000)
		{
			return strokeOpacity;
		} else
		{
			return parent == null ? '\377' : parent.getStrokeOpacity();
		}
	}

	public int getOpacity()
	{
		if(opacity != 0x80000000)
		{
			return opacity;
		} else
		{
			return parent == null ? '\377' : parent.getOpacity();
		}
	}

	public TinyColor getStopColor()
	{
		if(stopColor == TinyColor.CURRENT)
		{
			return getCurrentColor();
		}
		if(stopColor != TinyColor.INHERIT)
		{
			return stopColor;
		} else
		{
			return parent == null ? TinyColor.INHERIT : parent.getStopColor();
		}
	}

	public int getStopOpacity()
	{
		if(stopOpacity != 0x80000000)
		{
			return stopOpacity;
		} else
		{
			return parent == null ? '\u0100' : parent.getStopOpacity();
		}
	}

	public int getDisplay()
	{
		if(display != 26)
		{
			return display;
		} else
		{
			return parent == null ? 27 : parent.getDisplay();
		}
	}

	public int getVisibility()
	{
		if(visibility != 26)
		{
			return visibility;
		} else
		{
			return parent == null ? 58 : parent.getVisibility();
		}
	}

	public int getFontSize()
	{
		if(fontSize != 0x80000000)
		{
			return fontSize;
		} else
		{
			return parent == null ? '\u0C00' : parent.getFontSize();
		}
	}

	public TinyString getFontFamily()
	{
		if(fontFamily != null)
		{
			return fontFamily;
		} else
		{
			return parent == null ? SVG.VAL_DEFAULT_FONTFAMILY : parent.getFontFamily();
		}
	}

	public int getTextAnchor()
	{
		if(textAnchor != 26)
		{
			return textAnchor;
		} else
		{
			return parent == null ? 54 : parent.getTextAnchor();
		}
	}

	public boolean isVisible()
	{
		return getVisibility() == 58;
	}

	public boolean isDisplay()
	{
		return getDisplay() == 27;
	}

	public boolean contains(SVGRaster svgraster, TinyPoint tinypoint)
	{
		TinyRect tinyrect = getDevBounds(svgraster);
		return tinyrect != null && tinyrect.contains(tinypoint);
	}

	public boolean intersects(SVGRaster svgraster, TinyRect tinyrect)
	{
		TinyRect tinyrect1 = getDevBounds(svgraster);
		if(tinyrect1 == null)
		{
			return false;
		} else
		{
			return tinyrect1.intersects(tinyrect);
		}
	}

	public SVGNode nodeHitAt(SVGRaster svgraster, TinyPoint tinypoint)
	{

		//if(isVisible() && getFillColor() != TinyColor.NONE && contains(svgraster, tinypoint))
			// Quitado lo del fill color para poder seleccionar un nodo
			if (isVisible() && (changeEvent==null || changeEvent.getChangeType()!=SVGChangeEvent.CHANGE_TYPE_DELETED)
					&& contains(svgraster, tinypoint))
			{
				return this;
			} else
			{
				return null;
			}
	}

	public SVGNode seekAElem()
	{
		for(SVGNode svgnode = this; svgnode != null; svgnode = svgnode.parent)
		{
			SVGNode svgnode1 = svgnode.parent;
			if(svgnode1 != null && svgnode1.helem == 0)
			{
				return svgnode1;
			}
		}

		return null;
	}

	public TinyMatrix getGlobalTransform()
	{
		SVGNode svgnode = this;
		TinyMatrix tinymatrix = new TinyMatrix();
		for(; svgnode != null; svgnode = svgnode.parent)
		{
			if(svgnode.transform != null)
			{
				tinymatrix.concatenate(svgnode.transform.matrix);
			}
		}

		return tinymatrix;
	}

	public TinyRect getBounds()
	{
		SVGNode svgnode = this;
		if(!svgnode.outlined || svgnode.bounds == null)
		{
			svgnode.createOutline();
		}
		return svgnode.bounds;
	}

	public TinyRect getDevBounds(SVGRaster svgraster)
	{
		TinyRect tinyrect = getBounds();
		if(tinyrect == null)
		{
			return null;
		} else
		{
			TinyRect tinyrect1 = new TinyRect();
			TinyMatrix tinymatrix = getGlobalTransform();
			tinyrect1 = svgraster._fldif.a(tinymatrix, getLineThickness(), tinyrect);
			return tinyrect1;
		}
	}

	public static SVGNode getNodeById(SVGNode svgnode, TinyString tinystring)
	{
		if(svgnode.id != null && tinystring != null && TinyString.compareTo(svgnode.id.data, 0, svgnode.id.count, tinystring.data, 0, tinystring.count) == 0)
		{
			return svgnode;
		}
		for(int j = 0; j < svgnode.children.count; j++)
		{
			SVGNode svgnode2 = (SVGNode)svgnode.children.data[j];
			SVGNode svgnode1 = getNodeById(svgnode2, tinystring);
			if(svgnode1 != null)
			{
				return svgnode1;
			}
		}

		return null;
	}

	public SVGChangeEvent getChangeEvent() {
		return changeEvent;
	}

	public void setExtendedAttributeAndRecordEvent(int j, String str) throws Exception {
		ownerDocument.setModified(true);
		ownerDocument.setSavePending(true);
		if (changeEvent == null) {
			changeEvent = new SVGChangeEvent(SVGChangeEvent.CHANGE_TYPE_MODIFIED, System.currentTimeMillis());
		}
		else {
			int changeType = changeEvent.getChangeType();
			// Si el nodo es nuevo, no hacer nada, sigue siendo nuevo
			if ((changeType & SVGChangeEvent.CHANGE_TYPE_NEW) == 0) {
				if ((changeType & SVGChangeEvent.CHANGE_TYPE_METADATA_MODIFIED) != 0) {
					// Se marca tambien como modificado el nodo, ademas de los metadatos
					changeEvent.setChangeType(changeType | SVGChangeEvent.CHANGE_TYPE_MODIFIED);
				}
				changeEvent.setTimestamp(System.currentTimeMillis()); // Se registra el instante del ultimo cambio
			}
		}
		int size = nameAtts.size();
		nameAtts.setElementAt(str, j);
	}
	
	
	/**
	 * 
	 * Función a invocar sólo en nodos hijos porque añade el nombre del atributo extendido al padre
	 * @param name
	 * @return
	 */
	public int addNewExtendedAttribute(String name) {
		
	 
		
		if (parent == null) return -1;
		
		int index = parent.nameAtts.indexOf (name);
		if (index == -1) {
			parent.nameAtts.add(name);
			nameAtts.setSize(parent.nameAtts.size());
			for(int i=0; i < nameAtts.size();i++){
				if(nameAtts.elementAt(i)==null){
					nameAtts.setElementAt("", i);
				}
			}
			return nameAtts.size() -1;
		}
		else {
			nameAtts.setSize(index+1);
			for(int i=0; i < nameAtts.size();i++){
				if(nameAtts.elementAt(i)==null){
					nameAtts.setElementAt("", i);
				}
			}
			return index;
		}
	}

	 
	 
/*
	public void addExtendedAttributeAndRecordEvent(String str) throws Exception {
		ownerDocument.setModified(true);
		ownerDocument.setSavePending(true);
		if (changeEvent == null) {
			changeEvent = new SVGChangeEvent(SVGChangeEvent.CHANGE_TYPE_MODIFIED, System.currentTimeMillis());
		}
		else {
			int changeType = changeEvent.getChangeType();
			// Si el nodo es nuevo, no hacer nada, sigue siendo nuevo
			if ((changeType & SVGChangeEvent.CHANGE_TYPE_NEW) == 0) {
				if ((changeType & SVGChangeEvent.CHANGE_TYPE_METADATA_MODIFIED) != 0) {
					// Se marca tambien como modificado el nodo, ademas de los metadatos
					changeEvent.setChangeType(changeType | SVGChangeEvent.CHANGE_TYPE_MODIFIED);
				}
				changeEvent.setTimestamp(System.currentTimeMillis()); // Se registra el instante del ultimo cambio
			}
		}
		
		nameAtts.addElement(str);
	}
*/
	public void addChildAndRecordEvent(SVGNode svgnode, int j) {
		ownerDocument.setModified(true);
		ownerDocument.setSavePending(true);
		if (svgnode.changeEvent == null) {
			svgnode.changeEvent = new SVGChangeEvent(SVGChangeEvent.CHANGE_TYPE_NEW, System.currentTimeMillis());
		}
		else{
			svgnode.changeEvent.setChangeType(svgnode.changeEvent.getChangeType() | SVGChangeEvent.CHANGE_TYPE_NEW);
			svgnode.changeEvent.setTimestamp(System.currentTimeMillis());
		}

		addChild(svgnode, j);
	}

	public void removeChildAndRecordEvent(int j) {
		ownerDocument.setModified(true);
		ownerDocument.setSavePending(true);
		if (children.count > j) {
			SVGNode child = (SVGNode) children.data[j];
			if (child.changeEvent == null) {
				child.changeEvent = new SVGChangeEvent(SVGChangeEvent.CHANGE_TYPE_DELETED, System.currentTimeMillis());
			}
			else {
				if ((child.changeEvent.getChangeType() & SVGChangeEvent.CHANGE_TYPE_NEW) != 0) {
					// Se elimina el nodo junto con su registro de cambio
					removeChild(j);
				}
				else {
					// Se marca para borrar, independientemente de las modificaciones anteriores
					child.changeEvent = new SVGChangeEvent(SVGChangeEvent.CHANGE_TYPE_DELETED, System.currentTimeMillis());
				}
			}
		}

		//return removeChild(j); No hay que borrarlo, solo marcarlo como borrado
	}
	
	public void setMetadataContentAndRecordEvent(String content) {
		ownerDocument.setModified(true);
		ownerDocument.setSavePending(true);
		// Se busca el nodo hijo de metadatos
		SVGMetadataElem metadata = null;
		if (children != null) {
			for (int i=0; i<children.count; i++) {
				if (children.data[i] instanceof SVGMetadataElem) {
					metadata = (SVGMetadataElem) children.data[i];
					break;
				}
			}
		}
		// Si no existe, se crea y se inserta
		if (metadata == null) {
			metadata = new SVGMetadataElem();
			addChild(metadata, -1);
		}
		
		// Se modifican los metadatos
		metadata.setContent(new TinyString(content.toCharArray()));
		
		// Se pone la marca de metadatos modificados
		if (changeEvent == null) {
			changeEvent = new SVGChangeEvent(SVGChangeEvent.CHANGE_TYPE_METADATA_MODIFIED, System.currentTimeMillis());
		}
		else {
			changeEvent.setChangeType(changeEvent.getChangeType() | SVGChangeEvent.CHANGE_TYPE_METADATA_MODIFIED);
		}
	}

	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Vector getImageURLs() {
		return imageURLs;
	}
	
	public void addImageURL(String imageURL) {
		ownerDocument.setModified(true);
		ownerDocument.setSavePending(true);
		if (changeEvent == null) {
			changeEvent = new SVGChangeEvent(SVGChangeEvent.CHANGE_TYPE_MODIFIED, System.currentTimeMillis());
		}
		else {
			switch(changeEvent.getChangeType()) {
			case SVGChangeEvent.CHANGE_TYPE_DELETED:
				// No puede ocurrir
				break;
			case SVGChangeEvent.CHANGE_TYPE_MODIFIED:
				changeEvent.setTimestamp(System.currentTimeMillis()); // Se registra el instante del ultimo cambio
				break;
			case SVGChangeEvent.CHANGE_TYPE_NEW:
				// No hacer nada, sigue siendo nuevo
				break;
			}
		}

		if (imageURLs == null) imageURLs = new Vector();
		imageURLs.addElement(imageURL);
	}
	
	public void removeImageURL(int index) {
		ownerDocument.setModified(true);
		ownerDocument.setSavePending(true);
		if (changeEvent == null) {
			changeEvent = new SVGChangeEvent(SVGChangeEvent.CHANGE_TYPE_MODIFIED, System.currentTimeMillis());
		}
		else {
			switch(changeEvent.getChangeType()) {
			case SVGChangeEvent.CHANGE_TYPE_DELETED:
				// No puede ocurrir
				break;
			case SVGChangeEvent.CHANGE_TYPE_MODIFIED:
				changeEvent.setTimestamp(System.currentTimeMillis()); // Se registra el instante del ultimo cambio
				break;
			case SVGChangeEvent.CHANGE_TYPE_NEW:
				// No hacer nada, sigue siendo nuevo
				break;
			}
		}

		if (imageURLs != null) {
			imageURLs.removeElementAt(index);
		}
	}
	
	public abstract void copyGeometryTo(SVGNode destNode);

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}
	
}
