/**
 * AttrParser.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.svg;

import com.tinyline.tiny2d.TinyPath;
import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyString;
import com.tinyline.tiny2d.TinyVector;


public class AttrParser {
	/**Para la transformacion del numero al numero natural**/
	static final float DIV = 256f;
	static final float DIVDIV = 256f*256f;

	public AttrParser(){

	}

	public String attributeValue(int atributo, Object ac)
	{
		String i1 = "";
		String obj = null;
		switch(atributo)
		{
		case 5: // ATT_ASCENT
		case 9: // ATT_BASELINE
		case 21: // ATT_DESCENT
		case 42: // ATT_HORIZ_ADV_X
		case 102: // ATT_UNITS_PER_EM

			i1 = parseInt(((Integer)ac).intValue());
			break;

		case 100: // ATT_UNICODE
			i1 = ""+ac+"";
			break;

		case 105: // ATT_VIEWBOX
			obj = viewBox((SVGRect)ac);
			break;

		case 1: // ATT_ACCUMULATE
		case 2: // ATT_ADDITIVE
		case 13: // ATT_CALCMODE
		case 22: // ATT_DISPLAY
		case 27: // ATT_FILL_RULE
		case 39: // ATT_GRADIENTUNITS
		case 64: // ATT_PRESERVEASPECTRATIO
		case 70: // ATT_RESTAR
		case 75: // ATT_SPREADMETHOD
		case 85: // ATT_STROKE_LINECAP
		case 86: // ATT_STROKE_LINEJOIN
		case 92: // ATT_TEXT_ANCHOR
		case 95: // ATT_TYPE
		case 106: // ATT_VISIBILITY
		case 122: // ATT_XML_SPACE
		case 126: // ATT_ZOOMANDPAN
			obj = new String(((TinyString)ac).data);
			break;

		case 8: // ATT_BASEPROFILE
		case 12: // ATT_BY
		case 28: // ATT_FONT_FAMILY
		case 34: // ATT_FROM
		case 44: // ATT_ID
		case 68: // ATT_REQUIREDEXTENSIONS
		case 69: // ATT_REQUIREDFEATURES
		case 90: // ATT_SYSTEMLANGUAGE
		case 93: // ATT_TO
		case 104: // ATT_VERSION
		case 115: // ATT_XLINK_HREF
			obj = new String(((TinyString)ac).data);
			break;

		case 63: // ATT_POINTS
			obj = parsePoints((TinyVector)ac);
			break;

		case 18: // ATT_CX
		case 19: // ATT_CY
		case 65: // ATT_R
		case 72: // ATT_RX
		case 73: // ATT_RY
		case 109: // ATT_X
		case 111: // ATT_X1
		case 112: // ATT_X2
		case 123: // ATT_Y
		case 124: // ATT_Y1
		case 125: // ATT_Y2
			i1 = parseFix(((Integer)ac).intValue());
			break;

		case 107: // ATT_WIDTH

			i1 = parseFix(((Integer)ac).intValue());
			break;

		case 41: // ATT_HEIGHT
			i1 = parseFix(((Integer)ac).intValue());
			break;

		case 20: // ATT_D

			obj = parsePath((TinyPath)ac);
			break;

		case 15: // ATT_COLOR
		case 78: // ATT_STOP_COLOR
		case 82: // ATT_STROKE
		case 25: // ATT_FILL
			obj = parseColor(((Integer)ac).intValue());
			break;

		case 83: // ATT_STROKE_DASHARRAY
			obj = parseVector((int[])ac);
			break;

		case 29: // ATT_FONT_SIZE
		case 84: // ATT_STROKE_DASHOFFSET
		case 87: // ATT_STROKE_MITERLIMIT
		case 89: // ATT_STROKE_WIDTH
			i1 = parseFix(((Integer)ac).intValue());
			break;

		case 26: // ATT_FILL_OPACITY
		case 55: // ATT_OFFSET
		case 56: // ATT_OPACITY
		case 79: // ATT_STOP_OPACITY
		case 88: // ATT_STROKE_OPACITY
			i1 = parseFix(((Integer)ac).intValue());
			break;

		case 66: // 'B'
			i1 = parseFix(((Integer)ac).intValue());
			break;



		case 71: // ATT_ROTATE
			i1 = parseFix(((Integer)ac).intValue());
			break;


		case 3: // ATT_ALPHABETIC
		case 4: // ATT_ARABIC_FORM
		case 7: // ATT_ATTRIBUTETYPE
		case 10: // ATT_BBOX
		case 14: // ATT_CAP_HEIGHT
		case 17: // ATT_CONTENT
		case 30: // ATT_FONT_STRETCH
		case 32: // ATT_FONT_VARIANT
		case 35: // ATT_G1
		case 36: // ATT_G2
		case 37: // ATT_GLYPH_NAME
		case 40: // ATT_HANGING
		case 43: // ATT_HORIZ_ORIGIN_X
		case 45: // ATT_IDEOGRAPHIC
		case 46: // ATT_K
		case 50: // ATT_LANG
		case 51: // ATT_MATHEMATICAL
		case 54: // ATT_NAME
		case 57: // ATT_ORIGIN
		case 58: // ATT_OVERLINE_POSITION
		case 59: // ATT_OVERLINE_THICKNESS
		case 60: // ATT_PANOSE_1
		case 62: // ATT_PATHLENGTH
		case 74: // ATT_SLOPE
		case 76: // ATT_STEMH
		case 77: // ATT_STEMV
		case 80: // ATT_STRIKETHROUGH_POSITION
		case 81: // ATT_STRIKETHROUGH_THICKNESS
		case 91: // ATT_TARGET
		case 96: // ATT_U1
		case 97: // ATT_U2
		case 98: // ATT_UNDERLINE_POSITION
		case 99: // ATT_UNDERLINE_THICKNESS
		case 101: // ATT_UNICODE_RANGE
		case 108: // ATT_WIDTHS
		case 110: // ATT_X_HEIGHT
		case 113: // ATT_XLINK_ACTUATE
		case 114: // ATT_XLINK_ARCROLE
		case 116: // ATT_XLINK_ROLE
		case 117: // ATT_XLINK_SHOW
		case 118: // ATT_XLINK_TITLE
		case 119: // ATT_XLINK_TYPE
		case 120: // ATT_XML_BASE
		case 121: // ATT_XML_LANG
		default:
			return "";

		case 16: // ATT_COLOR_RENDERING
		case 31: // ATT_FONT_STYLE
		case 33: // ATT_FONT_WEIGHT
			break;
		case 127:
			obj = new String(((TinyString)ac).data);
			break;
		}
		if(obj != null)
		{
			return obj;
		} else
		{
			return i1 = parseFix(((Integer)ac).intValue());
		}
	}

	private String parseVector(int[] ac) {
		String resultado="";
		for(int i=0;i<ac.length;i++) {
			resultado+=ac[i]/DIV+" ";
		}
		return resultado;
	}
	private static String parseColor(int alpha) {
		//String hex1 = Integer.toHexString(alpha); 
		//int hex=Integer.parseInt(hex1);
		int r=(alpha & 0xFF0000) >> 16;
		int g= (alpha & 0x00FF00) >> 8;
		int b=alpha & 0x0000FF;

		return "rgb("+r+","+g+","+b+")";
	}
	private String parsePath(TinyPath v) {
		// TODO Auto-generated method stub
		String res="";
		boolean entradoL= false;
		boolean entradoC= false;

		for(int i=0; i<v.numPoints();i++){
			if(v.getType(i)==1){
				res+="M";
			}
			if(v.getType(i)==2 && !entradoL){
				res+="L";
				entradoL=true;
				entradoC=false;
			}
			if((v.getType(i)==3 || v.getType(i)==4)&&!entradoC){
				res+="C";
				entradoC=true;
				entradoL=false;
			}
			float ini2=v.getX(i)/DIV;
			float fin2=v.getY(i)/DIV;

			if(ini2!=0.0 || fin2!=0.0){
				res+=ini2+" "+fin2+" ";
			}
		}
		if(v.getType(v.numPoints()-1)==5){
			res+="z";
		}
		else{
			res+="\" fill=\"none";
		}

		return res;
	}

	private String parseFix(int Value) {
		return Value/DIV+"";
	}
	private String parsePoints(TinyVector ac) {
		String res="";
		for(int i=0;i<ac.count;i++){
			res+=(((TinyPoint)ac.data[i]).x)/DIV+" "+(((TinyPoint)ac.data[i]).y)/DIV+" ";
		}
		return res;
	}
	//Parsea un entero 
	private String parseInt(int ac) {
		return ""+(int) (ac/DIV)+"";
	}

	//Parsea un rectangulo a string
	private String viewBox(SVGRect ac)
	{		
		return ac.x/DIV+" "+ac.y/DIV+" "+ac.width/DIV+" "+ac.height/DIV;
	}	
}
