/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * TimeZoneWrapper.java
 * 
 * Created on June 16, 2006, 12:26 PM
 *
 */

package it.businesslogic.ireport.gui.locale;
import it.businesslogic.ireport.util.I18n;

import java.util.TimeZone;

/**
 *
 * @author gtoffoli
 */
public class TimeZoneWrapper {
    
    private TimeZone timeZone = null;
    
    /** Creates a new instance of TimeZoneWrapper */
    public TimeZoneWrapper(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
    
    public String toString()
    {
        if (timeZone != null)
        {
            return timeZone.getID()  + " (" + timeZone.getDisplayName(I18n.getCurrentLocale()) + ")";
        }
        return "Null time zone";
    }
    
}
