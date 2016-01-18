package com.tinyline.svg;


public interface XMLHandler
{

    public abstract void startDocument();

    public abstract void endDocument();

    public abstract void startElement(char ac[], int i, int j);

    public abstract void endElement();

    public abstract void attributeName(char ac[], int i, int j);

    public abstract void attributeValue(char ac[], int i, int j);

    public abstract void charData(char ac[], int i, int j);
}
