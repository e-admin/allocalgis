/**
 * SVGEvent.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tinyline.app;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

public class SVGEvent
    implements Event
{

    public static String EVENT_NAMES[] = {
        "animate", "begin", "click", "end", "error", "focushide", "focusin", "focusnext", "focusout", "focuspressed", 
        "focusprev", "focusshow", "load", "origview", "pauseresume", "quality", "repeat", "scroll", "unload", "update", 
        "zoom"
    };
    public static final int EVENT_ANIMATE = 0;
    public static final int EVENT_BEGIN = 1;
    public static final int EVENT_CLICK = 2;
    public static final int EVENT_END = 3;
    public static final int EVENT_ERROR = 4;
    public static final int EVENT_FOCUSHIDE = 5;
    public static final int EVENT_FOCUSIN = 6;
    public static final int EVENT_FOCUSNEXT = 7;
    public static final int EVENT_FOCUSOUT = 8;
    public static final int EVENT_FOCUSPRESSED = 9;
    public static final int EVENT_FOCUSPREV = 10;
    public static final int EVENT_FOCUSSHOW = 11;
    public static final int EVENT_LOAD = 12;
    public static final int EVENT_ORIGVIEW = 13;
    public static final int EVENT_PAUSERESUME = 14;
    public static final int EVENT_QUALITY = 15;
    public static final int EVENT_REPEAT = 16;
    public static final int EVENT_SCROLL = 17;
    public static final int EVENT_UNLOAD = 18;
    public static final int EVENT_UPDATE = 19;
    public static final int EVENT_ZOOM = 20;
    public static final int EVENT_UNKNOWN = 21;
    public int id;
    public Object data;
    EventTarget eventTarget;
    SVGEvent next;

    public String getType()
    {
        if(id >= 21)
        {
            return "unknown";
        } else
        {
            return EVENT_NAMES[id];
        }
    }

    public EventTarget getCurrentTarget()
    {
        return eventTarget;
    }

    public SVGEvent(int i, Object obj)
    {
        id = i;
        data = obj;
    }

}
