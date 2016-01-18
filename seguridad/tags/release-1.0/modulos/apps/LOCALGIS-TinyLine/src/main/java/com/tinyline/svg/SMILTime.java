package com.tinyline.svg;

import com.tinyline.tiny2d.TinyString;

public final class SMILTime
{

    public static final int SMIL_TIME_INDEFINITE = 0;
    public static final int SMIL_TIME_OFFSET = 1;
    public static final int SMIL_TIME_EVENT_BASED = 2;
    public int type;
    public int timeValue;
    public int offset;
    public TinyString idValue;

    public SMILTime()
    {
        type = 0;
        offset = -1;
    }

    public SMILTime(int i, int j)
    {
        type = i;
        switch(i)
        {
        case 1: // '\001'
            offset = j;
            break;

        default:
            offset = -1;
            break;
        }
    }

    public SMILTime(SMILTime smiltime)
    {
        smiltime.copyTo(this);
    }

    public void copyTo(SMILTime smiltime)
    {
        smiltime.type = type;
        smiltime.timeValue = timeValue;
        smiltime.offset = offset;
        if(idValue != null)
        {
            smiltime.idValue = new TinyString(idValue.data);
        } else
        {
            smiltime.idValue = null;
        }
    }

    public int getResolvedOffset()
    {
        return timeValue + offset;
    }

    public final boolean greaterThan(SMILTime smiltime)
    {
        return getResolvedOffset() > smiltime.getResolvedOffset();
    }

    public final boolean equalTo(SMILTime smiltime)
    {
        return getResolvedOffset() == smiltime.getResolvedOffset();
    }
}
