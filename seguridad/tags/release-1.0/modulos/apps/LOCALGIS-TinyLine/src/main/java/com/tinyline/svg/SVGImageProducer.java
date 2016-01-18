package com.tinyline.svg;


public interface SVGImageProducer
{

    public abstract boolean hasConsumer();

    public abstract void sendPixels();

    public abstract void imageComplete();
}
