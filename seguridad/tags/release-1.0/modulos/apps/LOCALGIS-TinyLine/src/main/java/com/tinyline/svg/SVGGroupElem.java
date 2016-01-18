package com.tinyline.svg;

import com.tinyline.tiny2d.*;

// Referenced classes of package com.tinyline.svg:
//            SVGNode, SVGRaster

public class SVGGroupElem extends SVGNode
{

    public TinyString xlink_href;
    public boolean showBounds;
    private String systemId;
    private String geometryType;

    SVGGroupElem()
    {
    }

    public SVGGroupElem(SVGGroupElem svggroupelem)
    {
        super(svggroupelem);
        if(svggroupelem.xlink_href != null)
        {
            xlink_href = new TinyString(svggroupelem.xlink_href.data);
        }
        showBounds = svggroupelem.showBounds;
    }

    public SVGNode copyNode()
    {
        return new SVGGroupElem(this);
    }

    public int setAttribute(int j, Object obj)
        throws Exception
    {
        super.outlined = false;
        switch(j)
        {
        case 115: // 's'
            xlink_href = (TinyString)obj;
            break;

        default:
            return super.setAttribute(j, obj);
        }
        return 0;
    }


    public Object getAttribute(int j)
    {
        int k = 0;
        TinyString tinystring = null;
        switch(j)
        {
        case 115: // 's'
            tinystring = xlink_href;
            break;

        default:
            return super.getAttribute(j);
        }
        if(tinystring != null)
        {
            return tinystring;
        } else
        {
            return new TinyNumber(k);
        }
    }

    public int createOutline()
    {
        int l = super.children.count;
        if(super.helem == 31)
        {
            for(int j = 0; j < l; j++)
            {
                SVGNode svgnode1 = (SVGNode)super.children.data[j];
                if(svgnode1 == null)
                {
                    continue;
                }
                SVGNode svgnode = svgnode1;
                svgnode.visibility = 24;
                if(!svgnode.matchUserAgent())
                {
                    continue;
                }
                svgnode.visibility = 58;
                break;
            }

        } else
        {
            for(int k = 0; k < l; k++)
            {
                SVGNode svgnode2 = (SVGNode)super.children.data[k];
                if(svgnode2 != null && svgnode2.isDisplay())
                {
                    svgnode2.createOutline();
                }
            }

        }
        super.outlined = true;
        return 0;
    }

    public void paint(SVGRaster svgraster)
    {
        if(!super.outlined)
        {
            createOutline();
        }
        int k = super.children.count;
        if(k == 0 || !isDisplay())
        {
            return;
        }
        for(int j = 0; j < k; j++)
        {
            SVGNode svgnode = (SVGNode)super.children.data[j];
            if(svgnode != null)
            {
                svgnode.paint(svgraster);
            }
        }

        if(showBounds && super.helem == 0)
        {
            a(svgraster);
        }
    }

  public SVGNode nodeHitAt(SVGRaster svgraster, TinyPoint tinypoint)
  {
      int k = super.children.count;
      int minDistancia = Integer.MAX_VALUE;
      SVGNode svgNodeRet = null;
      if(k > 0)
      {
          for(int j=0; j < k; j++)
          {
              SVGNode svgnode = (SVGNode)super.children.data[j];
              if(svgnode != null)
              {
              	//para evitar que se seleccionen imágenes
              	if(svgnode instanceof SVGImageElem){ continue;}
              		
                  //si el punto está dentro del rectángulo que recubre la feature
                  SVGNode svgnode1 = svgnode.nodeHitAt(svgraster, tinypoint);
                  if(svgnode1 != null)
                  {
                  	//devolveremos un listado de las features cuyo centro del recubrimiento diste 
                  	//el mínimo de todos los recubrimientos. Decimos que pueden ser varias xq
                  	//puede que haya varias features superpuestas.
                  	//De este modo solucionamos el problema de que no se puedan escoger más features
                  	//si se pilla una línea que recorre transversalmente la cuadrícula
                  	TinyRect tinyrect = svgnode1.getDevBounds(svgraster);
                  	int alto = tinyrect.ymax - tinyrect.ymin;
                  	int ancho = tinyrect.xmax - tinyrect.xmin;
                  	int centerX = tinyrect.xmin + (ancho/2);
                  	int centerY = tinyrect.ymin + (alto/2);
                  	int curDistance = TinyUtil.fastDistance(tinypoint.x - centerX, tinypoint.y - centerY);
                  	if(curDistance <= minDistancia){
                  		svgNodeRet = svgnode1;
                  		minDistancia = curDistance;
                  	}
                  }                	
              }
          }

      }
      
      return svgNodeRet;
  }

    public TinyRect getDevBounds(SVGRaster svgraster)
    {
        int k = super.children.count;
        TinyRect tinyrect = new TinyRect();
        for(int j = 0; j < k; j++)
        {
            SVGNode svgnode = (SVGNode)super.children.data[j];
            if(svgnode != null)
            {
                TinyRect tinyrect1 = svgnode.getDevBounds(svgraster);
                if(tinyrect1 != null)
                {
                    tinyrect.union(tinyrect1);
                }
            }
        }

        return tinyrect;
    }

    private void a(SVGRaster svgraster)
    {
        TinyRect tinyrect = getDevBounds(svgraster);
        if(tinyrect == null || tinyrect.isEmpty())
        {
            return;
        } else
        {
            TinyPath tinypath = TinyPath.rectToPath(tinyrect.xmin << 8, tinyrect.ymin << 8, tinyrect.xmax << 8, tinyrect.ymax << 8);
            i j = svgraster._fldif;
            j._mthbyte(2);
            j.a(TinyColor.NONE);
            j._mthif(new TinyColor(0xa40000ff));
            j._mthcase(512);
            j.a(new TinyMatrix());
            j.a(svgraster.isAntialiased);
            j.a(svgraster.clipRect);
            j._mthdo(getDevBounds(svgraster));
            j.a(tinypath);
            return;
        }
    }
    
    public String getSystemId() {
    	return systemId;
    }
    
    public void setSystemId(String systemId) {
    	this.systemId = systemId;
    }

    public String getGeometryType() {
    	return geometryType;
    }
    
    public void setGeometryType(String geometryType) {
    	this.geometryType = geometryType;
    }

    public void copyGeometryTo(SVGNode destNode) {
	}
}
