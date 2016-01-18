package com.tinyline.svg;

import com.tinyline.tiny2d.TinyBitmap;
import com.tinyline.tiny2d.TinyString;

public interface ImageLoader
{

    public abstract TinyBitmap createTinyBitmap(TinyString tinystring);

    public abstract TinyBitmap createTinyBitmap(byte abyte0[], int i, int j);
}
