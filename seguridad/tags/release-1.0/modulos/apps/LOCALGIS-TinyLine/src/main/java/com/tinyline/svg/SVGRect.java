package com.tinyline.svg;


public class SVGRect
{

    public int x;
    public int y;
    public int width;
    public int height;

    public SVGRect()
    {
    }

    public SVGRect(SVGRect svgrect)
    {
        x = svgrect.x;
        y = svgrect.y;
        width = svgrect.width;
        height = svgrect.height;
    }
}
