package com.tinyline.svg;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.tinyline.tiny2d.*;


// Referenced classes of package com.tinyline.svg:
//            SVGUnknownElem, SVGGroupElem, SVGSVGElem, SVGUseElem, 
//            SVGEllipseElem, SVGLineElem, SVGPolygonElem, SVGRectElem, 
//            SVGPathElem, SVGImageElem, SVGTextElem, SVGFontElem, 
//            SVGFontFaceElem, SVGGlyphElem, SVGMPathElem, SVGStopElem, 
//            SVGGradientElem, SVGAnimationElem, SVGNode, c, 
//            AnimationCallback, SMILTime, SVGRaster

public class SVGDocument
{

    int _fldif;
    int _fldfor;
    boolean _flddo;
    public SVGNode root;
    public SVGRaster renderer;
    public TinyHash fontTable;
    public static SVGFontElem defaultFont;
    public int nActiveAnimations;
    public TinyVector animTargets;
    public int currentTime;
    public AnimationCallback acb;
    public TinyVector linkTargets;
    public int linkIndex;
    private boolean a;
	/** Clase donde se serializan todos los elementos. **/
	private AttrParser attrParser;
	/** Flag que indica si el svg ha sido modificado */
	private boolean modified;
	/** Flag que indica si el svg ha sido modificado y no se ha guardado */
	private boolean savePending;
	
	private static Logger logger = (Logger) Logger.getInstance(SVGDocument.class);

    public SVGDocument()
    {
        a = false;
        _fldif = 0;
        _fldfor = 0;
        _flddo = false;
        root = createElement(30);
        renderer = null;
        fontTable = new TinyHash(1, 11);
        nActiveAnimations = 0;
        animTargets = new TinyVector(4);
        linkTargets = new TinyVector(4);
        linkIndex = 0;
		attrParser = new AttrParser();
		modified = false;
		savePending = false;
    }

    public SVGNode createElement(int i)
    {
        Object obj = new SVGUnknownElem();
        switch(i)
        {
        case 7: // '\007'
        case 11: // '\013'
        case 12: // '\f'
        case 13: // '\r'
        case 16: // '\020'
        case 33: // '!'
        default:
            break;

        case 20: // '\024'
        	obj = new SVGMetadataElem();
        	break;
        case 0: // '\0'
        case 14: // '\016'
            obj = new SVGGroupElem();
            break;

        case 30: // '\036'
            obj = new SVGSVGElem();
            break;

        case 6: // '\006'
        	obj = new SVGGroupElem();
            ((SVGGroupElem)obj).display = 35;
            break;

        case 34: // '"'
            obj = new SVGUseElem();
            break;

        case 31: // '\037'
            obj = new SVGGroupElem();
            ((SVGGroupElem)obj).visibility = 24;
            break;

        case 5: // '\005'
        case 8: // '\b'
            obj = new SVGEllipseElem();
            break;

        case 18: // '\022'
            obj = new SVGLineElem();
            break;

        case 24: // '\030'
        case 25: // '\031'
            obj = new SVGPolygonElem();
            break;

        case 27: // '\033'
            obj = new SVGRectElem();
            break;

        case 23: // '\027'
            obj = new SVGPathElem();
            break;

        case 17: // '\021'
            obj = new SVGImageElem();
            break;

        case 32: // ' '
            obj = new SVGTextElem();
            break;

        case 9: // '\t'
            obj = new SVGFontElem();
            break;

        case 10: // '\n'
            obj = new SVGFontFaceElem();
            break;

        case 15: // '\017'
        case 21: // '\025'
            obj = new SVGGlyphElem();
            break;

        case 22: // '\026'
            obj = new SVGMPathElem();
            break;

        case 29: // '\035'
            obj = new SVGStopElem();
            ((SVGStopElem)obj).visibility = 24;
            break;

        case 19: // '\023'
        case 26: // '\032'
            obj = new SVGGradientElem();
            ((SVGGradientElem)obj).helem = i;
            ((SVGGradientElem)obj).setDefaults();
            ((SVGGradientElem)obj).visibility = 24;
            break;

        case 1: // '\001'
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 28: // '\034'
            obj = new SVGAnimationElem();
            if(i == 3)
            {
                ((SVGAnimationElem)obj).calcMode = 40;
            }
            ((SVGAnimationElem)obj).display = 35;
            break;
        }
        ((SVGNode)obj).helem = i;
        ((SVGNode)obj).ownerDocument = this;
        return ((SVGNode) (obj));
    }

    public static int addFont(SVGDocument svgdocument, SVGFontElem svgfontelem)
    {
        if(svgfontelem == null)
        {
            return 2;
        } else
        {
            svgdocument.fontTable.put(svgfontelem._fldelse.fontFamily, svgfontelem);
            return 0;
        }
    }

    public static SVGFontElem getFont(SVGDocument svgdocument, TinyString tinystring)
    {
        SVGFontElem svgfontelem = null;
        if(svgdocument == null || tinystring == null)
        {
            return null;
        }
        svgfontelem = (SVGFontElem)svgdocument.fontTable.get(tinystring);
        if(svgfontelem == null)
        {
            svgfontelem = defaultFont;
        }
        return svgfontelem;
    }

    public boolean isZoomAndPanAnable()
    {
        return root != null && ((SVGSVGElem)root).zoomAndPan != 19;
    }

    public void addLinks(SVGNode svgnode)
    {
        if(svgnode.helem == 0 && ((SVGGroupElem)svgnode).xlink_href != null)
        {
            linkTargets.addElement(svgnode);
        } else
        {
            int j = svgnode.children.count;
            for(int i = 0; i < j; i++)
            {
                SVGNode svgnode1 = (SVGNode)svgnode.children.data[i];
                if(svgnode1 != null)
                {
                    addLinks(svgnode1);
                }
            }

        }
    }

    public void addAnimations(SVGNode svgnode)
    {
        switch(svgnode.helem)
        {
        case 1: // '\001'
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 28: // '\034'
            a((SVGAnimationElem)svgnode);
            break;

        default:
            int j = svgnode.children.count;
            for(int i = 0; i < j; i++)
            {
                SVGNode svgnode1 = (SVGNode)svgnode.children.data[i];
                if(svgnode1 != null)
                {
                    addAnimations(svgnode1);
                }
            }

            break;
        }
    }

    private void a(SVGAnimationElem svganimationelem)
    {
        if(svganimationelem == null)
        {
            return;
        }
        if(svganimationelem.createOutline() != 0)
        {
            return;
        }
        int i = animTargets.indexOf(svganimationelem._fldgoto, 0);
        SVGNode svgnode;
        if(i == -1)
        {
            i = animTargets.count;
            svgnode = svganimationelem._fldgoto;
            animTargets.addElement(svgnode);
        } else
        {
            svgnode = (SVGNode)animTargets.data[i];
        }
        if(svgnode == null)
        {
            return;
        }
        int j = svganimationelem.attributeName;
        TinyVector tinyvector = svgnode.animatedVals;
        boolean flag = false;
        for(int k = 0; k < tinyvector.count; k++)
        {
            c c1 = (c)tinyvector.data[k];
            if(c1._flddo != j)
            {
                continue;
            }
            c1.a(svganimationelem);
            flag = true;
            break;
        }

        if(!flag)
        {
            c c2 = new c(j, svganimationelem.b);
            c2.a(svganimationelem);
            tinyvector.addElement(c2);
        }
    }

    public int getCurrentTime()
    {
        return currentTime;
    }

    public TinyRect animate(int i)
    {
        currentTime = i;
        TinyRect tinyrect = new TinyRect();
        tinyrect.setEmpty();
        nActiveAnimations = 0;
        for(int j = 0; j < animTargets.count; j++)
        {
            SVGNode svgnode = (SVGNode)animTargets.data[j];
            TinyRect tinyrect1 = svgnode.getDevBounds(renderer);
            tinyrect.union(tinyrect1);
            TinyVector tinyvector = svgnode.animatedVals;
            int k = tinyvector.count;
            for(int l = 0; l < k; l++)
            {
                c c1 = (c)tinyvector.data[l];
                Object obj = c1.a();
                if(obj != null)
                {
                    try
                    {
                        svgnode.setAttribute(c1._flddo, obj);
                        svgnode.createOutline();
                    }
                    catch(Exception exception) { }
                }
            }

            TinyRect tinyrect2 = svgnode.getDevBounds(renderer);
            tinyrect.union(tinyrect2);
        }

        return tinyrect;
    }

    public void postSMILEvent(int i, TinyString tinystring)
    {
        if(acb != null)
        {
            acb.postSMILEvent(i, tinystring);
        }
    }

    public boolean resolveEventBased(TinyString tinystring)
    {
        if(tinystring == null)
        {
            return false;
        } else
        {
            a = false;
            a(root, tinystring, false);
            return a;
        }
    }

    public boolean resolveLinkBased(TinyString tinystring)
    {
        if(tinystring == null)
        {
            return false;
        } else
        {
            a = false;
            a(root, tinystring, true);
            return a;
        }
    }

    private void a(SVGNode svgnode, TinyString tinystring, boolean flag)
    {
        if(svgnode.animatedVals != null)
        {
            TinyVector tinyvector = svgnode.animatedVals;
            for(int j = 0; j < tinyvector.count; j++)
            {
                c c1 = (c)tinyvector.data[0];
                for(int l = 0; l < c1._fldfor.count; l++)
                {
                    SVGAnimationElem svganimationelem = (SVGAnimationElem)c1._fldfor.data[l];
                    if(flag)
                    {
                        if(((SVGNode) (svganimationelem)).id != null && ((SVGNode) (svganimationelem)).id.equals(tinystring))
                        {
                            svganimationelem.beginElementAt(0);
                            a = true;
                        }
                    } else
                    {
                        if(svganimationelem.begin.idValue != null && svganimationelem.begin.idValue.equals(tinystring))
                        {
                            svganimationelem.beginElementAt(0);
                            a = true;
                        }
                        if(svganimationelem.end.idValue != null && svganimationelem.end.idValue.equals(tinystring))
                        {
                            svganimationelem.endElementAt(0);
                            a = true;
                        }
                    }
                }

            }

        }
        if(svgnode instanceof SVGGroupElem)
        {
            int i = svgnode.children.count;
            for(int k = 0; k < i; k++)
            {
                SVGNode svgnode1 = (SVGNode)svgnode.children.data[k];
                if(svgnode1 != null)
                {
                    a(svgnode1, tinystring, flag);
                }
            }

        }
    }
    
    /////////////////////////////////////////////////////////////////
    // Metodos para serializar el SVG y salvarlo en un OutputStream
    /////////////////////////////////////////////////////////////////
	public void serializeSVG2XML(OutputStream outputStream) throws IOException {
		serializeSVG2XML(outputStream, false, false, true, null);
	}

	public Vector serializeModifiedNodes2XML(OutputStream outputStream,
											boolean generateIds, 
											boolean generateHeader,
											//valor del atributo id que no queremos que se serialize
											//usado para la capa de errores.
											String idValueNotSerialized) throws IOException {
		return serializeSVG2XML(outputStream, true, generateIds, generateHeader,idValueNotSerialized);
	}

	private Vector serializeSVG2XML(OutputStream outputStream, 
									boolean onlyModified, 
									boolean generateIds, 
									boolean generateHeader,
									String idValueNotSerialized) throws IOException {
		XmlBuilder xml = new XmlBuilder();
		Vector nodesWithImage = projectSerialize(xml, onlyModified, generateIds, generateHeader,idValueNotSerialized);
		logger.debug("Escribiendo a disco");
		OutputStreamWriter out = new OutputStreamWriter(outputStream, "UTF-8");
		BufferedWriter bw = new BufferedWriter(out);
		bw.write(xml.getXML());
		bw.flush();
		return nodesWithImage;
	}
	
	private Vector projectSerialize(XmlBuilder xml,
									boolean onlyModified, 
									boolean generateIds, 
									boolean generateHeader,
									String idValueNotSerialized) {
		SVGSVGElem ele=(SVGSVGElem) root;
		if (generateHeader) {
			xml.writeHeader();
	
			String rootAttributes = XmlProjectTags.VIEWBOX + "=\"" + attrParser.attributeValue(105, ele.viewBox) + "\"";
			if (modified) {
				rootAttributes += " " + SVG.ATT_MODIFIED + "=\"true\"";
			}
			if (root.nameAtts != null && root.nameAtts.size() >= 2) {
				rootAttributes += " despX=\"" + root.nameAtts.elementAt(0) + "\" despY=\"" + root.nameAtts.elementAt(1) + "\" ";
			}
			xml.openTag(XmlProjectTags.SVG, rootAttributes, new Hashtable());
		}
		Vector nodesWithImage = new Vector();
		savedoc(xml, ele.children, onlyModified, generateIds, nodesWithImage,idValueNotSerialized);
		
		if (generateHeader) {
			xml.closeTag();
		}
		return nodesWithImage;
	}

	/**- No se admite poner en la especificacion del svgTiny 
	 *   style="fill:none; stroke:blue; stroke-width:0.00002"
	 *   se debe poner por separado
	 *- los width y los heigth solo admite almacenarlo en local los valores sin px, pc, pt,...
	 * - los valores que se pasan tienen q estas dentro del rango -32.767 a 32.767
	 * Se devuelve un vector con los nodos a los que se han asociado imagenes 
	 * Se le pasa el valor de un id que no se quiere serialziar se utiliza para no enviar
	 * al servidor la capa de errores
	 * **/
	private void savedoc(XmlBuilder xml,
						TinyVector children, 
						boolean onlyModified, 
						boolean generateIds, 
						Vector nodesWithImage,
						String idValueNotSerialized) {
		for(int i=0; i<children.count;i++){
			//logger.info("Tamaño del xml:"+xml.getXML().length());
			SVGNode child = (SVGNode) children.data[i];
			if ( (idValueNotSerialized!= null) 
				&& (child.id != null)
				&& (new String(child.id.data).equals(idValueNotSerialized))){
				
				// no hago nada no serializo ese elemento
			}
			// Generacion de identificadores si se requiere
			else { 
				if (generateIds && child.id == null) {
					String hashCode = String.valueOf(child.hashCode());
					String hourMillis = String.valueOf(System.currentTimeMillis() % 3600);
					child.id = new TinyString((hashCode + hourMillis).toCharArray());
				}
				
				if(child.getClass().getName().equals("com.tinyline.svg.SVGGroupElem")){
					SVGGroupElem g = (SVGGroupElem) children.data[i];
					Hashtable attributes=atributos(g);					
					xml.openTag(XmlProjectTags.GROUP,CalculoAtributos("l",g.nameAtts), attributes);
					savedoc(xml,g.children, onlyModified, generateIds, nodesWithImage,idValueNotSerialized);
					xml.closeTag();					
				}
				else {
					if (!onlyModified || child.getChangeEvent()!=null || child instanceof SVGMetadataElem) {
	
						// Incluir los nodos con imagenes en el vector
						if (child.imageURLs != null && !child.imageURLs.isEmpty()) {
							nodesWithImage.addElement(child);
						}
						
						if(children.data[i].getClass().getName().equals("com.tinyline.svg.SVGPathElem")){
							SVGPathElem p = (SVGPathElem) children.data[i];
							String attributes="";
							SVGNode padre=p.parent;
							if(p.path!=null){
								attributes+=XmlProjectTags.D+"=\""+attrParser.attributeValue(20,p.path)+"\" ";
							}
							attributes+=atributos((SVGNode)p, padre);
							xml.openTag(XmlProjectTags.PATH, attributes);
							savedoc(xml,p.children, onlyModified, generateIds, nodesWithImage,idValueNotSerialized);
							xml.closeTag();
						}
						else if(children.data[i].getClass().getName().equals("com.tinyline.svg.SVGRectElem")){
							SVGRectElem p= (SVGRectElem)children.data[i];
							SVGNode padre=p.parent;
							
							String attributes="";
							attributes+=XmlProjectTags.X+"=\""+attrParser.attributeValue(109, new Integer(p.x))+"\" ";				
							attributes+=XmlProjectTags.Y+"=\""+attrParser.attributeValue(123, new Integer(p.y))+"\" ";
							attributes+=XmlProjectTags.WIDTH+"=\""+attrParser.attributeValue(107, new Integer(p.width))+"\" ";		
							attributes+=XmlProjectTags.HEIGHT+"=\""+attrParser.attributeValue(41, new Integer(p.height))+"\" ";
							attributes+=atributos((SVGNode)p, padre);
							
							xml.openTag(XmlProjectTags.RECT, attributes);
							savedoc(xml,p.children, onlyModified, generateIds, nodesWithImage,idValueNotSerialized);
							xml.closeTag();
						}
						else if(children.data[i].getClass().getName().equals("com.tinyline.svg.SVGEllipseElem")){
							SVGEllipseElem p=(SVGEllipseElem)children.data[i];
							SVGNode padre=p.parent;
							String attributes="";
							
							attributes+=XmlProjectTags.CX+"=\""+attrParser.attributeValue(18, new Integer(p.cx))+"\" ";
							attributes+=XmlProjectTags.CY+"=\""+attrParser.attributeValue(19, new Integer(p.cy))+"\" ";
							attributes+=XmlProjectTags.RX+"=\""+attrParser.attributeValue(72, new Integer(p.rx))+"\" ";
							attributes+=XmlProjectTags.RY+"=\""+attrParser.attributeValue(73, new Integer(p.ry))+"\" ";
							
							attributes+=atributos((SVGNode)p, padre);
							xml.openTag(XmlProjectTags.ELLIPSE, attributes);
							savedoc(xml,p.children, onlyModified, generateIds, nodesWithImage,idValueNotSerialized);
							xml.closeTag();
						}
						else if(children.data[i].getClass().getName().equals("com.tinyline.svg.SVGLineElem")){
							SVGLineElem p = (SVGLineElem)children.data[i];
							String attributes="";
							SVGNode padre=p.parent;
							
							attributes+=XmlProjectTags.X1+"=\""+attrParser.attributeValue(111, new Integer(p.x1))+"\" ";
							attributes+=XmlProjectTags.X2+"=\""+attrParser.attributeValue(112, new Integer(p.x2))+"\" ";
							attributes+=XmlProjectTags.Y1+"=\""+attrParser.attributeValue(124, new Integer(p.y1))+"\" ";
							attributes+=XmlProjectTags.Y2+"=\""+attrParser.attributeValue(125, new Integer(p.y2))+"\" ";
							
							attributes+=atributos((SVGNode)p, padre);
							xml.openTag(XmlProjectTags.LINE, attributes);
							savedoc(xml,p.children, onlyModified, generateIds, nodesWithImage,idValueNotSerialized);
							xml.closeTag();
						}
						else if(children.data[i].getClass().getName().equals("com.tinyline.svg.SVGPolygonElem")){
							SVGPolygonElem p= (SVGPolygonElem)children.data[i];
							String attributes="";
							SVGNode padre=p.parent;
							
							attributes+=XmlProjectTags.POINTS+"=\""+attrParser.attributeValue(63, p.points)+"\" ";
							
							attributes+=atributos((SVGNode)p, padre);
							if (p.helem == SVG.ELEM_POLYGON) {
								xml.openTag(XmlProjectTags.POLYGON, attributes);
							}
							else {
								xml.openTag(XmlProjectTags.POLYLINE, attributes);
							}
							savedoc(xml,p.children, onlyModified, generateIds, nodesWithImage,idValueNotSerialized);
							xml.closeTag();
						}
						else if(children.data[i].getClass().getName().equals("com.tinyline.svg.SVGTextElem")){
							SVGTextElem p = (SVGTextElem)children.data[i];
							String attributes="";
							SVGNode padre=p.parent;
							
							attributes+=XmlProjectTags.X+"=\""+attrParser.attributeValue(109, new Integer(p.x))+"\" ";
							attributes+=XmlProjectTags.Y+"=\""+attrParser.attributeValue(123, new Integer(p.y))+"\" ";
							if(p.fontFamily!=null){
								attributes+=XmlProjectTags.FONTFAMILY+"=\""+attrParser.attributeValue(28, p.fontFamily)+"\" ";
							}
							if(p.fontSize!=-2147483648){
								attributes+=XmlProjectTags.FONTSIZE+"=\""+attrParser.attributeValue(29, new Integer(p.fontSize))+"\" ";
							}
							attributes+=atributos((SVGNode)p, padre);
							
							xml.TagText(XmlProjectTags.TEXT, attributes, new String(p.str.data));
						}
						else if(children.data[i].getClass().getName().equals("com.tinyline.svg.SVGImageElem")){
							SVGImageElem p = (SVGImageElem)children.data[i];
							String attributes="";
							SVGNode padre=p.parent;
							
							attributes+=XmlProjectTags.X+"=\""+attrParser.attributeValue(109, new Integer(p.x))+"\" ";
							attributes+=XmlProjectTags.Y+"=\""+attrParser.attributeValue(123, new Integer(p.y))+"\" ";
							attributes+=XmlProjectTags.WIDTH+"=\""+attrParser.attributeValue(107, new Integer(p.width))+"\" ";
							attributes+=XmlProjectTags.HEIGHT+"=\""+attrParser.attributeValue(41, new Integer(p.height))+"\" ";
							attributes+=XmlProjectTags.XLINKHREF+"=\""+attrParser.attributeValue(115, p.xlink_href)+"\" ";
							
							xml.openTag(XmlProjectTags.IMAGE, attributes);
							savedoc(xml,p.children, onlyModified, generateIds, nodesWithImage,idValueNotSerialized);
							xml.closeTag();
						}
						else if(children.data[i].getClass().getName().equals("com.tinyline.svg.SVGMetadataElem")){
							SVGMetadataElem p = (SVGMetadataElem)children.data[i];
							String content = "";
							if (p.content != null) {
								content = "<![CDATA[" + new String(p.content.data) + "]]>";
							}
							xml.TagMetadata(content);
						}
					}
				}	
			} //del else de comprobar que el id no coincide con el que no queremos serializar
		} //del for
	}

	private String atributos(SVGNode p, SVGNode padre) {
		String attributes="";
		attributes+=CalculoAtributos("v",p.nameAtts);
		
		if(p.id!= null){
			attributes+=XmlProjectTags.ID+"=\""+attrParser.attributeValue(44, p.id)+"\" ";
		}

		if(p.fill.value!=0 && p.fill.value!=padre.fill.value){
			attributes+=XmlProjectTags.FILL+"=\""+attrParser.attributeValue(25, new Integer(p.fill.value))+"\" ";
		}
		if(p.fillOpacity!=0 && p.fillOpacity!=-2147483648 && p.fillOpacity!=padre.fillOpacity){
			attributes+=XmlProjectTags.FILL_OPACITY+"=\""+attrParser.attributeValue(26, new Integer(p.fillOpacity))+"\" ";
		}
		if(p.stroke.value!=0 && p.stroke.value!=padre.stroke.value){
			attributes+=XmlProjectTags.STROKE+"=\""+attrParser.attributeValue(82, new Integer(p.stroke.value))+"\" ";
		}
		if(p.strokeOpacity!=0 && p.strokeOpacity!=-2147483648  && p.strokeOpacity!=padre.strokeOpacity){
			attributes+=XmlProjectTags.STROKE_OPACITY+"=\""+attrParser.attributeValue(88, new Integer(p.strokeOpacity))+"\" ";
		}
		if(p.strokeWidth!=0 && p.strokeWidth!=-2147483648 && p.strokeWidth!=padre.strokeWidth){
			attributes+=XmlProjectTags.STROKE_WIDTH+"=\""+attrParser.attributeValue(89, new Integer(p.strokeWidth))+"\" ";
		}
		if(p.opacity!=0 && p.opacity != TinyNumber.INHERIT){
			attributes+=XmlProjectTags.OPACITY+"=\""+attrParser.attributeValue(56, new Integer(p.opacity))+"\" ";
		}
		
		if (p.changeEvent != null && p.changeEvent.getChangeType() != 0 && p.changeEvent.getTimestamp() != 0) {
			attributes += SVG.ATT_CHANGE_TYPE + "=\"" + p.changeEvent.getChangeType() + "\" " +
				SVG.ATT_CHANGE_TIMESTAMP + "=\"" + p.changeEvent.getTimestamp() + "\" ";
		}
		
		if(p.group != -1){
			attributes += SVG.ATT_GROUP + "=\""+ p.group +"\" ";
		}
		
		try {
			if (p.imageURLs != null && !p.imageURLs.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				Enumeration en = p.imageURLs.elements();
				while (en.hasMoreElements()) {
					String im = (String) en.nextElement();
					sb.append(URLEncoder.encode(im, "UTF-8") + " ");
				}
				attributes += SVG.ATT_IMAGEURLS + "=\"" + sb.toString() + "\" ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return attributes;
	}

	private Hashtable atributos(SVGGroupElem g) {
		Hashtable attributes= new Hashtable();
		
		if(g.id!= null){
			attributes.put(XmlProjectTags.ID,attrParser.attributeValue(44, g.id));
		}
		
		if(g.opacity!=0 && g.opacity!=TinyNumber.INHERIT){
			attributes.put(XmlProjectTags.OPACITY,attrParser.attributeValue(56, new Integer(g.opacity)));
		}
		
		if(g.fill.value!=0){
			attributes.put(XmlProjectTags.FILL,attrParser.attributeValue(25, new Integer(g.fill.value)));
		}
		// fill="none" (Juanda)
		if (g.fill == TinyColor.NONE) {
			attributes.put(XmlProjectTags.FILL, "none");
		}
		
		if(g.fillOpacity!=0 && g.fillOpacity!=-2147483648){
			System.out.println ("opacity " +g.fillOpacity + " int" +new Integer(g.fillOpacity));
			attributes.put(XmlProjectTags.FILL_OPACITY,attrParser.attributeValue(26, new Integer(g.fillOpacity)));
		}
		
		if(g.stroke.value!=0 ){
			attributes.put(XmlProjectTags.STROKE,attrParser.attributeValue(82, new Integer(g.stroke.value)));
		}
		
		if(g.strokeOpacity!=0 && g.strokeOpacity!=-2147483648){
			attributes.put(XmlProjectTags.STROKE_OPACITY,attrParser.attributeValue(88, new Integer(g.strokeOpacity)));
		}
		
		if(g.strokeWidth!=0 && g.strokeWidth!=-2147483648){
			attributes.put(XmlProjectTags.STROKE_WIDTH,attrParser.attributeValue(89, new Integer(g.strokeWidth)));
		}
		
		attributes.put(XmlProjectTags.STROKE_DASHARRAY,attrParser.attributeValue(83,g.strokeDashArray));
		
		// Atributo "editable"
		attributes.put(SVG.ATT_EDITABLE, String.valueOf(g.isEditable()));

		// Registro de cambios
		if (g.changeEvent != null && g.changeEvent.getChangeType() != 0 && g.changeEvent.getTimestamp() != 0) {
			attributes.put(SVG.ATT_CHANGE_TYPE, String.valueOf(g.changeEvent.getChangeType()));
			attributes.put(SVG.ATT_CHANGE_TIMESTAMP, String.valueOf(g.changeEvent.getTimestamp()));
		}
		
		// Imagenes
		try {
			if (g.imageURLs != null && !g.imageURLs.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				Enumeration en = g.imageURLs.elements();
				while (en.hasMoreElements()) {
					String im = (String) en.nextElement();
					sb.append(URLEncoder.encode(im, "UTF-8") + " ");
				}
				attributes.put(SVG.ATT_IMAGEURLS, sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (g.getSystemId() != null) {
			attributes.put(SVG.ATT_SYSTEMID, g.getSystemId());
		}

		if (g.getGeometryType() != null) {
			attributes.put(SVG.ATT_GEOMETRYTYPE, g.getGeometryType());
		}

		return attributes;
	}
	
	private String CalculoAtributos(String nombre,Vector nameAtts) {
		String result="";
		for(int i=0;i<nameAtts.size();i++){
			result+=nombre+(i+1)+"=\""+nameAtts.elementAt(i)+"\" ";
		}
		return result;
	}
	
	public boolean isModified() {
		return modified;
	}
	
	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public boolean isSavePending() {
		return savePending;
	}
	
	public void setSavePending(boolean savePending) {
		this.savePending = savePending;
	}
}
