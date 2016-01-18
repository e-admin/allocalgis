package com.tinyline.svg;

import com.tinyline.tiny2d.*;

// Referenced classes of package com.tinyline.svg:
//            SMILTime, SVGRect

public class SVG
{

    public static final int DATATYPE_UNKNOWN = 0;
    public static final int DATATYPE_COLOR = 1;
    public static final int DATATYPE_DASHARRAY = 2;
    public static final int DATATYPE_ENUM = 3;
    public static final int DATATYPE_NUMBER = 4;
    public static final int DATATYPE_NUMBERLIST = 5;
    public static final int DATATYPE_PATH = 6;
    public static final int DATATYPE_PATHLIST = 7;
    public static final int DATATYPE_POINTLIST = 8;
    public static final int DATATYPE_STRING = 9;
    public static final int DATATYPE_STRINGLIST = 10;
    public static final int DATATYPE_TRANSFORM = 11;
    public static final int DATATYPE_TIME = 12;
    public static final int DATATYPE_VIEWBOX = 14;
    public static final int ERR_OK = 0;
    public static final int ERR_NUMBER_FORMAT = 1;
    public static final int ERR_INVALID_ARG = 2;
    public static final int ERR_NOT_FOUND = 4;
    public static final int ERR_NOT_SUPPORTED = 8;
    public static final int ERR_HIERARCHY_REQUEST = 16;
    public static char ELEMENTS[][] = {
        "a".toCharArray(), "animate".toCharArray(), "animateColor".toCharArray(), "animateMotion".toCharArray(), "animateTransform".toCharArray(), "circle".toCharArray(), "defs".toCharArray(), "desc".toCharArray(), "ellipse".toCharArray(), "font".toCharArray(), 
        "font-face".toCharArray(), "font-face-name".toCharArray(), "font-face-src".toCharArray(), "foreignObject".toCharArray(), "g".toCharArray(), "glyph".toCharArray(), "hkern".toCharArray(), "image".toCharArray(), "line".toCharArray(), "linearGradient".toCharArray(), 
        "metadata".toCharArray(), "missing-glyph".toCharArray(), "mpath".toCharArray(), "path".toCharArray(), "polygon".toCharArray(), "polyline".toCharArray(), "radialGradient".toCharArray(), "rect".toCharArray(), "set".toCharArray(), "stop".toCharArray(), 
        "svg".toCharArray(), "switch".toCharArray(), "text".toCharArray(), "title".toCharArray(), "use".toCharArray()
    };
    public static final int ELEM_A = 0;
    public static final int ELEM_ANIMATE = 1;
    public static final int ELEM_ANIMATECOLOR = 2;
    public static final int ELEM_ANIMATEMOTION = 3;
    public static final int ELEM_ANIMATETRANSFORM = 4;
    public static final int ELEM_CIRCLE = 5;
    public static final int ELEM_DEFS = 6;
    public static final int ELEM_DESC = 7;
    public static final int ELEM_ELLIPSE = 8;
    public static final int ELEM_FONT = 9;
    public static final int ELEM_FONT_FACE = 10;
    public static final int ELEM_FONT_FACE_NAME = 11;
    public static final int ELEM_FONT_FACE_SRC = 12;
    public static final int ELEM_FOREIGNOBJECT = 13;
    public static final int ELEM_G = 14;
    public static final int ELEM_GLYPH = 15;
    public static final int ELEM_HKERN = 16;
    public static final int ELEM_IMAGE = 17;
    public static final int ELEM_LINE = 18;
    public static final int ELEM_LINEARGRADIENT = 19;
    public static final int ELEM_METADATA = 20;
    public static final int ELEM_MISSING_GLYPH = 21;
    public static final int ELEM_MPATH = 22;
    public static final int ELEM_PATH = 23;
    public static final int ELEM_POLYGON = 24;
    public static final int ELEM_POLYLINE = 25;
    public static final int ELEM_RADIALGRADIENT = 26;
    public static final int ELEM_RECT = 27;
    public static final int ELEM_SET = 28;
    public static final int ELEM_STOP = 29;
    public static final int ELEM_SVG = 30;
    public static final int ELEM_SWITCH = 31;
    public static final int ELEM_TEXT = 32;
    public static final int ELEM_TITLE = 33;
    public static final int ELEM_USE = 34;
    public static final int ELEM_UNKNOWN = 35;
    public static final int ELEM_DOCUMENT = 300;
    public static char ATTRIBUTES[][] = {
        "accent-height".toCharArray(), "accumulate".toCharArray(), "additive".toCharArray(), "alphabetic".toCharArray(), "arabic-form".toCharArray(), "ascent".toCharArray(), "attributeName".toCharArray(), "attributeType".toCharArray(), "baseProfile".toCharArray(), "baseline".toCharArray(), 
        "bbox".toCharArray(), "begin".toCharArray(), "by".toCharArray(), "calcMode".toCharArray(), "cap-height".toCharArray(), "color".toCharArray(), "color-rendering".toCharArray(), "content".toCharArray(), "cx".toCharArray(), "cy".toCharArray(), 
        "d".toCharArray(), "descent".toCharArray(), "display".toCharArray(), "dur".toCharArray(), "end".toCharArray(), "fill".toCharArray(), "fill-opacity".toCharArray(), "fill-rule".toCharArray(), "font-family".toCharArray(), "font-size".toCharArray(), 
        "font-stretch".toCharArray(), "font-style".toCharArray(), "font-variant".toCharArray(), "font-weight".toCharArray(), "from".toCharArray(), "g1".toCharArray(), "g2".toCharArray(), "glyph-name".toCharArray(), "gradientTransform".toCharArray(), "gradientUnits".toCharArray(), 
        "hanging".toCharArray(), "height".toCharArray(), "horiz-adv-x".toCharArray(), "horiz-origin-x".toCharArray(), "id".toCharArray(), "ideographic".toCharArray(), "k".toCharArray(), "keyPoints".toCharArray(), "keySplines".toCharArray(), "keyTimes".toCharArray(), 
        "lang".toCharArray(), "mathematical".toCharArray(), "max".toCharArray(), "min".toCharArray(), "name".toCharArray(), "offset".toCharArray(), "opacity".toCharArray(), "origin".toCharArray(), "overline-position".toCharArray(), "overline-thickness".toCharArray(), 
        "panose-1".toCharArray(), "path".toCharArray(), "pathLength".toCharArray(), "points".toCharArray(), "preserveAspectRatio".toCharArray(), "r".toCharArray(), "repeatCount".toCharArray(), "repeatDur".toCharArray(), "requiredExtensions".toCharArray(), "requiredFeatures".toCharArray(), 
        "restart".toCharArray(), "rotate".toCharArray(), "rx".toCharArray(), "ry".toCharArray(), "slope".toCharArray(), "spreadMethod".toCharArray(), "stemh".toCharArray(), "stemv".toCharArray(), "stop-color".toCharArray(), "stop-opacity".toCharArray(), 
        "strikethrough-position".toCharArray(), "strikethrough-thickness".toCharArray(), "stroke".toCharArray(), "stroke-dasharray".toCharArray(), "stroke-dashoffset".toCharArray(), "stroke-linecap".toCharArray(), "stroke-linejoin".toCharArray(), "stroke-miterlimit".toCharArray(), "stroke-opacity".toCharArray(), "stroke-width".toCharArray(), 
        "systemLanguage".toCharArray(), "target".toCharArray(), "text-anchor".toCharArray(), "to".toCharArray(), "transform".toCharArray(), "type".toCharArray(), "u1".toCharArray(), "u2".toCharArray(), "underline-position".toCharArray(), "underline-thickness".toCharArray(), 
        "unicode".toCharArray(), "unicode-range".toCharArray(), "units-per-em".toCharArray(), "values".toCharArray(), "version".toCharArray(), "viewBox".toCharArray(), "visibility".toCharArray(), "width".toCharArray(), "widths".toCharArray(), "x".toCharArray(), 
        "x-height".toCharArray(), "x1".toCharArray(), "x2".toCharArray(), "xlink:actuate".toCharArray(), "xlink:arcrole".toCharArray(), "xlink:href".toCharArray(), "xlink:role".toCharArray(), "xlink:show".toCharArray(), "xlink:title".toCharArray(), "xlink:type".toCharArray(), 
        "xml:base".toCharArray(), "xml:lang".toCharArray(), "xml:space".toCharArray(), "y".toCharArray(), "y1".toCharArray(), "y2".toCharArray(), "zoomAndPan".toCharArray()
    };
    public static final int ATT_ACCENT_HEIGHT = 0;
    public static final int ATT_ACCUMULATE = 1;
    public static final int ATT_ADDITIVE = 2;
    public static final int ATT_ALPHABETIC = 3;
    public static final int ATT_ARABIC_FORM = 4;
    public static final int ATT_ASCENT = 5;
    public static final int ATT_ATTRIBUTENAME = 6;
    public static final int ATT_ATTRIBUTETYPE = 7;
    public static final int ATT_BASEPROFILE = 8;
    public static final int ATT_BASELINE = 9;
    public static final int ATT_BBOX = 10;
    public static final int ATT_BEGIN = 11;
    public static final int ATT_BY = 12;
    public static final int ATT_CALCMODE = 13;
    public static final int ATT_CAP_HEIGHT = 14;
    public static final int ATT_COLOR = 15;
    public static final int ATT_COLOR_RENDERING = 16;
    public static final int ATT_CONTENT = 17;
    public static final int ATT_CX = 18;
    public static final int ATT_CY = 19;
    public static final int ATT_D = 20;
    public static final int ATT_DESCENT = 21;
    public static final int ATT_DISPLAY = 22;
    public static final int ATT_DUR = 23;
    public static final int ATT_END = 24;
    public static final int ATT_FILL = 25;
    public static final int ATT_FILL_OPACITY = 26;
    public static final int ATT_FILL_RULE = 27;
    public static final int ATT_FONT_FAMILY = 28;
    public static final int ATT_FONT_SIZE = 29;
    public static final int ATT_FONT_STRETCH = 30;
    public static final int ATT_FONT_STYLE = 31;
    public static final int ATT_FONT_VARIANT = 32;
    public static final int ATT_FONT_WEIGHT = 33;
    public static final int ATT_FROM = 34;
    public static final int ATT_G1 = 35;
    public static final int ATT_G2 = 36;
    public static final int ATT_GLYPH_NAME = 37;
    public static final int ATT_GRADIENTTRANSFORM = 38;
    public static final int ATT_GRADIENTUNITS = 39;
    public static final int ATT_HANGING = 40;
    public static final int ATT_HEIGHT = 41;
    public static final int ATT_HORIZ_ADV_X = 42;
    public static final int ATT_HORIZ_ORIGIN_X = 43;
    public static final int ATT_ID = 44;
    public static final int ATT_IDEOGRAPHIC = 45;
    public static final int ATT_K = 46;
    public static final int ATT_KEYPOINTS = 47;
    public static final int ATT_KEYSPLINES = 48;
    public static final int ATT_KEYTIMES = 49;
    public static final int ATT_LANG = 50;
    public static final int ATT_MATHEMATICAL = 51;
    public static final int ATT_MAX = 52;
    public static final int ATT_MIN = 53;
    public static final int ATT_NAME = 54;
    public static final int ATT_OFFSET = 55;
    public static final int ATT_OPACITY = 56;
    public static final int ATT_ORIGIN = 57;
    public static final int ATT_OVERLINE_POSITION = 58;
    public static final int ATT_OVERLINE_THICKNESS = 59;
    public static final int ATT_PANOSE_1 = 60;
    public static final int ATT_PATH = 61;
    public static final int ATT_PATHLENGTH = 62;
    public static final int ATT_POINTS = 63;
    public static final int ATT_PRESERVEASPECTRATIO = 64;
    public static final int ATT_R = 65;
    public static final int ATT_REPEATCOUNT = 66;
    public static final int ATT_REPEATDUR = 67;
    public static final int ATT_REQUIREDEXTENSIONS = 68;
    public static final int ATT_REQUIREDFEATURES = 69;
    public static final int ATT_RESTART = 70;
    public static final int ATT_ROTATE = 71;
    public static final int ATT_RX = 72;
    public static final int ATT_RY = 73;
    public static final int ATT_SLOPE = 74;
    public static final int ATT_SPREADMETHOD = 75;
    public static final int ATT_STEMH = 76;
    public static final int ATT_STEMV = 77;
    public static final int ATT_STOP_COLOR = 78;
    public static final int ATT_STOP_OPACITY = 79;
    public static final int ATT_STRIKETHROUGH_POSITION = 80;
    public static final int ATT_STRIKETHROUGH_THICKNESS = 81;
    public static final int ATT_STROKE = 82;
    public static final int ATT_STROKE_DASHARRAY = 83;
    public static final int ATT_STROKE_DASHOFFSET = 84;
    public static final int ATT_STROKE_LINECAP = 85;
    public static final int ATT_STROKE_LINEJOIN = 86;
    public static final int ATT_STROKE_MITERLIMIT = 87;
    public static final int ATT_STROKE_OPACITY = 88;
    public static final int ATT_STROKE_WIDTH = 89;
    public static final int ATT_SYSTEMLANGUAGE = 90;
    public static final int ATT_TARGET = 91;
    public static final int ATT_TEXT_ANCHOR = 92;
    public static final int ATT_TO = 93;
    public static final int ATT_TRANSFORM = 94;
    public static final int ATT_TYPE = 95;
    public static final int ATT_U1 = 96;
    public static final int ATT_U2 = 97;
    public static final int ATT_UNDERLINE_POSITION = 98;
    public static final int ATT_UNDERLINE_THICKNESS = 99;
    public static final int ATT_UNICODE = 100;
    public static final int ATT_UNICODE_RANGE = 101;
    public static final int ATT_UNITS_PER_EM = 102;
    public static final int ATT_VALUES = 103;
    public static final int ATT_VERSION = 104;
    public static final int ATT_VIEWBOX = 105;
    public static final int ATT_VISIBILITY = 106;
    public static final int ATT_WIDTH = 107;
    public static final int ATT_WIDTHS = 108;
    public static final int ATT_X = 109;
    public static final int ATT_X_HEIGHT = 110;
    public static final int ATT_X1 = 111;
    public static final int ATT_X2 = 112;
    public static final int ATT_XLINK_ACTUATE = 113;
    public static final int ATT_XLINK_ARCROLE = 114;
    public static final int ATT_XLINK_HREF = 115;
    public static final int ATT_XLINK_ROLE = 116;
    public static final int ATT_XLINK_SHOW = 117;
    public static final int ATT_XLINK_TITLE = 118;
    public static final int ATT_XLINK_TYPE = 119;
    public static final int ATT_XML_BASE = 120;
    public static final int ATT_XML_LANG = 121;
    public static final int ATT_XML_SPACE = 122;
    public static final int ATT_Y = 123;
    public static final int ATT_Y1 = 124;
    public static final int ATT_Y2 = 125;
    public static final int ATT_ZOOMANDPAN = 126;
    public static final int ATT_UNKNOWN = 127;

    /** Atributos incorporados */
	public static final String ATT_CHANGE_TYPE = "changeType";
	public static final String ATT_CHANGE_TIMESTAMP = "changeTimestamp";
	public static final String ATT_EDITABLE = "editable";
	public static final String ATT_ACTIVE = "active";
	public static final String ATT_IMAGEURLS = "imageURLs";
	public static final String ATT_MODIFIED = "modified";
	public static final String ATT_SYSTEMID = "systemId";
	public static final String ATT_GEOMETRYTYPE = "geometryType";
	public static final String ATT_GROUP = "g";

    public static char VALUES[][] = {
        "100".toCharArray(), "200".toCharArray(), "300".toCharArray(), "400".toCharArray(), "500".toCharArray(), "600".toCharArray(), "700".toCharArray(), "800".toCharArray(), "900".toCharArray(), "always".toCharArray(), 
        "auto".toCharArray(), "auto-reverse".toCharArray(), "bevel".toCharArray(), "bold".toCharArray(), "bolder".toCharArray(), "butt".toCharArray(), "collapse".toCharArray(), "currentColor".toCharArray(), "default".toCharArray(), "disable".toCharArray(), 
        "discrete".toCharArray(), "end".toCharArray(), "evenodd".toCharArray(), "freeze".toCharArray(), "hidden".toCharArray(), "indefinite".toCharArray(), "inherit".toCharArray(), "inline".toCharArray(), "italic".toCharArray(), "lighter".toCharArray(), 
        "linear".toCharArray(), "magnify".toCharArray(), "middle".toCharArray(), "miter".toCharArray(), "never".toCharArray(), "none".toCharArray(), "nonzero".toCharArray(), "normal".toCharArray(), "objectBoundingBox".toCharArray(), "oblique".toCharArray(), 
        "paced".toCharArray(), "pad".toCharArray(), "preserve".toCharArray(), "reflect".toCharArray(), "remove".toCharArray(), "repeat".toCharArray(), "replace".toCharArray(), "rotate".toCharArray(), "round".toCharArray(), "scale".toCharArray(), 
        "skewX".toCharArray(), "skewY".toCharArray(), "spline".toCharArray(), "square".toCharArray(), "start".toCharArray(), "sum".toCharArray(), "translate".toCharArray(), "userSpaceOnUse".toCharArray(), "visible".toCharArray(), "whenNotActive".toCharArray(), 
        "xMidYMid meet".toCharArray()
    };
    public static final int VAL_STROKEDASHARRAYNONE[] = new int[0];
    public static final int VAL_STROKEDASHARRAYINHERIT[] = new int[0];
    public static final TinyString VAL_DEFAULT_FONTFAMILY = new TinyString("Helvetica".toCharArray());
    static final int a = 3072;
    public static final int VAL_100 = 0;
    public static final int VAL_200 = 1;
    public static final int VAL_300 = 2;
    public static final int VAL_400 = 3;
    public static final int VAL_500 = 4;
    public static final int VAL_600 = 5;
    public static final int VAL_700 = 6;
    public static final int VAL_800 = 7;
    public static final int VAL_900 = 8;
    public static final int VAL_ALWAYS = 9;
    public static final int VAL_AUTO = 10;
    public static final int VAL_AUTO_REVERSE = 11;
    public static final int VAL_BEVEL = 12;
    public static final int VAL_BOLD = 13;
    public static final int VAL_BOLDER = 14;
    public static final int VAL_BUTT = 15;
    public static final int VAL_COLLAPSE = 16;
    public static final int VAL_CURRENTCOLOR = 17;
    public static final int VAL_DEFAULT = 18;
    public static final int VAL_DISABLE = 19;
    public static final int VAL_DISCRETE = 20;
    public static final int VAL_END = 21;
    public static final int VAL_EVENODD = 22;
    public static final int VAL_FREEZE = 23;
    public static final int VAL_HIDDEN = 24;
    public static final int VAL_INDEFINITE = 25;
    public static final int VAL_INHERIT = 26;
    public static final int VAL_INLINE = 27;
    public static final int VAL_ITALIC = 28;
    public static final int VAL_LIGHTER = 29;
    public static final int VAL_LINEAR = 30;
    public static final int VAL_MAGNIFY = 31;
    public static final int VAL_MIDDLE = 32;
    public static final int VAL_MITER = 33;
    public static final int VAL_NEVER = 34;
    public static final int VAL_NONE = 35;
    public static final int VAL_NONZERO = 36;
    public static final int VAL_NORMAL = 37;
    public static final int VAL_OBJECTBOUNDINGBOX = 38;
    public static final int VAL_OBLIQUE = 39;
    public static final int VAL_PACED = 40;
    public static final int VAL_PAD = 41;
    public static final int VAL_PRESERVE = 42;
    public static final int VAL_REFLECT = 43;
    public static final int VAL_REMOVE = 44;
    public static final int VAL_REPEAT = 45;
    public static final int VAL_REPLACE = 46;
    public static final int VAL_ROTATE = 47;
    public static final int VAL_ROUND = 48;
    public static final int VAL_SCALE = 49;
    public static final int VAL_SKEWX = 50;
    public static final int VAL_SKEWY = 51;
    public static final int VAL_SPLINE = 52;
    public static final int VAL_SQUARE = 53;
    public static final int VAL_START = 54;
    public static final int VAL_SUM = 55;
    public static final int VAL_TRANSLATE = 56;
    public static final int VAL_USERSPACEONUSE = 57;
    public static final int VAL_VISIBLE = 58;
    public static final int VAL_WHENNOTACTIVE = 59;
    public static final int VAL_XMIDYMID_MEET = 60;
    public static final int VAL_UNKNOWN = 61;

    public SVG()
    {
    }

    public static int elementName(char ac[], int i, int j)
    {
        return TinyString.getIndex(ELEMENTS, ac, i, j);
    }

    public static int attributeName(char ac[], int i, int j)
    {
        return TinyString.getIndex(ATTRIBUTES, ac, i, j);
    }

    public static int attributeValue(char ac[], int i, int j)
    {
        return TinyString.getIndex(VALUES, ac, i, j);
    }

    public static boolean isElementAnimatable(int i)
    {
        switch(i)
        {
        case 0: // '\0'
        case 5: // '\005'
        case 6: // '\006'
        case 8: // '\b'
        case 14: // '\016'
        case 17: // '\021'
        case 18: // '\022'
        case 19: // '\023'
        case 23: // '\027'
        case 24: // '\030'
        case 25: // '\031'
        case 26: // '\032'
        case 27: // '\033'
        case 29: // '\035'
        case 30: // '\036'
        case 31: // '\037'
        case 32: // ' '
        case 34: // '"'
            return true;

        case 1: // '\001'
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 7: // '\007'
        case 9: // '\t'
        case 10: // '\n'
        case 11: // '\013'
        case 12: // '\f'
        case 13: // '\r'
        case 15: // '\017'
        case 16: // '\020'
        case 20: // '\024'
        case 21: // '\025'
        case 22: // '\026'
        case 28: // '\034'
        case 33: // '!'
        default:
            return false;
        }
    }

    public static int attributeDataType(int i, int j)
    {
        byte byte0 = 0;
        switch(j)
        {
        default:
            break;

        case 0: // '\0'
        case 3: // '\003'
        case 5: // '\005'
        case 9: // '\t'
        case 14: // '\016'
        case 18: // '\022'
        case 19: // '\023'
        case 21: // '\025'
        case 26: // '\032'
        case 29: // '\035'
        case 40: // '('
        case 41: // ')'
        case 42: // '*'
        case 43: // '+'
        case 45: // '-'
        case 46: // '.'
        case 51: // '3'
        case 55: // '7'
        case 56: // '8'
        case 58: // ':'
        case 59: // ';'
        case 60: // '<'
        case 62: // '>'
        case 65: // 'A'
        case 66: // 'B'
        case 71: // 'G'
        case 72: // 'H'
        case 73: // 'I'
        case 79: // 'O'
        case 87: // 'W'
        case 88: // 'X'
        case 89: // 'Y'
        case 98: // 'b'
        case 99: // 'c'
        case 100: // 'd'
        case 102: // 'f'
        case 107: // 'k'
        case 109: // 'm'
        case 110: // 'n'
        case 111: // 'o'
        case 112: // 'p'
        case 123: // '{'
        case 124: // '|'
        case 125: // '}'
            byte0 = 4;
            break;

        case 1: // '\001'
        case 2: // '\002'
        case 13: // '\r'
        case 16: // '\020'
        case 22: // '\026'
        case 27: // '\033'
        case 30: // '\036'
        case 31: // '\037'
        case 32: // ' '
        case 33: // '!'
        case 39: // '\''
        case 64: // '@'
        case 70: // 'F'
        case 74: // 'J'
        case 75: // 'K'
        case 76: // 'L'
        case 77: // 'M'
        case 80: // 'P'
        case 81: // 'Q'
        case 84: // 'T'
        case 85: // 'U'
        case 86: // 'V'
        case 92: // '\\'
        case 95: // '_'
        case 106: // 'j'
        case 122: // 'z'
        case 126: // '~'
            byte0 = 3;
            break;

        case 4: // '\004'
        case 6: // '\006'
        case 7: // '\007'
        case 8: // '\b'
        case 10: // '\n'
        case 12: // '\f'
        case 17: // '\021'
        case 28: // '\034'
        case 34: // '"'
        case 35: // '#'
        case 36: // '$'
        case 37: // '%'
        case 44: // ','
        case 50: // '2'
        case 54: // '6'
        case 57: // '9'
        case 68: // 'D'
        case 69: // 'E'
        case 90: // 'Z'
        case 91: // '['
        case 93: // ']'
        case 96: // '`'
        case 97: // 'a'
        case 101: // 'e'
        case 104: // 'h'
        case 108: // 'l'
        case 113: // 'q'
        case 114: // 'r'
        case 115: // 's'
        case 116: // 't'
        case 117: // 'u'
        case 118: // 'v'
        case 119: // 'w'
        case 120: // 'x'
        case 121: // 'y'
            byte0 = 9;
            break;

        case 11: // '\013'
        case 23: // '\027'
        case 24: // '\030'
        case 52: // '4'
        case 53: // '5'
        case 67: // 'C'
            byte0 = 12;
            break;

        case 15: // '\017'
        case 78: // 'N'
        case 82: // 'R'
            byte0 = 1;
            break;

        case 20: // '\024'
        case 61: // '='
            byte0 = 6;
            break;

        case 25: // '\031'
            switch(i)
            {
            case 1: // '\001'
            case 2: // '\002'
            case 3: // '\003'
            case 4: // '\004'
            case 28: // '\034'
                byte0 = 3;
                break;

            default:
                byte0 = 1;
                break;
            }
            break;

        case 47: // '/'
        case 49: // '1'
            byte0 = 5;
            break;

        case 48: // '0'
            byte0 = 7;
            break;

        case 63: // '?'
            byte0 = 8;
            break;

        case 83: // 'S'
            byte0 = 2;
            break;

        case 38: // '&'
        case 94: // '^'
            byte0 = 11;
            break;

        case 103: // 'g'
            byte0 = 10;
            break;

        case 105: // 'i'
            byte0 = 14;
            break;
        }
        return byte0;
    }

    public static Object copyAttributeValue(Object obj, int i)
    {
        Object obj1 = null;
        if(obj == null)
        {
            return obj1;
        }
        switch(i)
        {
        case 13: // '\r'
        default:
            break;

        case 1: // '\001'
            TinyColor tinycolor = (TinyColor)obj;
            if(tinycolor != null)
            {
                obj1 = new TinyColor(tinycolor);
            }
            break;

        case 2: // '\002'
            int ai[] = (int[])obj;
            if(ai != null)
            {
                int ai1[] = new int[ai.length];
                System.arraycopy(ai, 0, ai1, 0, ai1.length);
                obj1 = ai1;
            }
            break;

        case 3: // '\003'
        case 4: // '\004'
            TinyNumber tinynumber = (TinyNumber)obj;
            if(tinynumber != null)
            {
                obj1 = new TinyNumber(tinynumber.val);
            }
            break;

        case 6: // '\006'
            TinyPath tinypath = (TinyPath)obj;
            if(tinypath != null)
            {
                obj1 = new TinyPath(tinypath);
            }
            break;

        case 9: // '\t'
            TinyString tinystring = (TinyString)obj;
            if(tinystring != null)
            {
                obj1 = new TinyString(tinystring.data);
            }
            break;

        case 11: // '\013'
            TinyTransform tinytransform = (TinyTransform)obj;
            if(tinytransform != null)
            {
                obj1 = new TinyTransform(tinytransform);
            }
            break;

        case 12: // '\f'
            SMILTime smiltime = (SMILTime)obj;
            if(smiltime != null)
            {
                obj1 = new SMILTime();
                smiltime.copyTo((SMILTime)obj1);
            }
            break;

        case 14: // '\016'
            SVGRect svgrect = (SVGRect)obj;
            if(svgrect != null)
            {
                obj1 = new SVGRect(svgrect);
            }
            break;

        case 5: // '\005'
        case 7: // '\007'
        case 8: // '\b'
        case 10: // '\n'
            TinyVector tinyvector = (TinyVector)obj;
            if(tinyvector == null)
            {
                break;
            }
            TinyVector tinyvector1 = new TinyVector(4);
            int j = tinyvector.count;
            for(int k = 0; k < j; k++)
            {
                switch(i)
                {
                case 6: // '\006'
                case 9: // '\t'
                default:
                    break;

                case 5: // '\005'
                    TinyNumber tinynumber1 = (TinyNumber)tinyvector.data[k];
                    if(tinynumber1 != null)
                    {
                        TinyNumber tinynumber2 = new TinyNumber(tinynumber1.val);
                        tinyvector1.addElement(tinynumber2);
                    }
                    break;

                case 7: // '\007'
                    TinyPath tinypath1 = (TinyPath)tinyvector.data[k];
                    if(tinypath1 != null)
                    {
                        TinyPath tinypath2 = new TinyPath(tinypath1);
                        tinyvector1.addElement(tinypath2);
                    }
                    break;

                case 8: // '\b'
                    TinyPoint tinypoint = (TinyPoint)tinyvector.data[k];
                    if(tinypoint != null)
                    {
                        TinyPoint tinypoint1 = new TinyPoint(tinypoint.x, tinypoint.y);
                        tinyvector1.addElement(tinypoint1);
                    }
                    break;

                case 10: // '\n'
                    TinyString tinystring1 = (TinyString)tinyvector.data[k];
                    if(tinystring1 != null)
                    {
                        TinyString tinystring2 = new TinyString(tinystring1.data);
                        tinyvector1.addElement(tinystring2);
                    }
                    break;
                }
            }

            obj1 = tinyvector1;
            break;
        }
        return obj1;
    }

}
