package com.tinyline.svg;


// Referenced classes of package com.tinyline.svg:
//            SVGNode, SVGRaster

public class SVGUnknownElem extends SVGNode
{

    public SVGUnknownElem()
    {
        super.helem = 35;
    }

    public void paint(SVGRaster svgraster)
    {
    }

    public SVGNode copyNode()
    {
        return new SVGUnknownElem();
    }

    public int createOutline()
    {
        super.outlined = true;
        return 0;
    }

	public void copyGeometryTo(SVGNode destNode) {
	}
}
