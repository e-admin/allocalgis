/**
 * SMILTime.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
