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
 * TimeSeriesDataset.java
 * 
 * Created on 15 agosto 2005, 17.49
 *
 */

package it.businesslogic.ireport.chart;
import java.util.*;
/**
 *
 * @author Administrator
 */
public class TimeSeriesDataset extends Dataset {
    
    private Vector timeSeries = null;
    
    private String timePeriod = "Day";
        
    /** Creates a new instance of TimeSeriesDataset */
    public TimeSeriesDataset() {
        super();
        setTimeSeries(new Vector());
    }   

    public Vector getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(Vector timeSeries) {
        this.timeSeries = timeSeries;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }
    
    public Dataset cloneMe()
    {
        TimeSeriesDataset obj = new TimeSeriesDataset();
        copyBaseDataset(obj);
        for (int i=0; i<getTimeSeries().size(); ++i)
        {
            obj.getTimeSeries().addElement( ((TimeSeries)getTimeSeries().elementAt(i)).cloneMe() );
        }
        obj.setTimePeriod( getTimePeriod() );
        
        return obj;
    }
}
