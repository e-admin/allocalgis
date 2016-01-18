/**
 * LogEvent.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;
import java.util.Date;

public class LogEvent 
{
  private String featureId;
  private Date timeStamp;
  private String user;
  
  private String eventType;

  public LogEvent()
  {
  }

  public String getFeatureId()
  {
    return featureId;
  }

  public void setFeatureId(String newFeature)
  {
    featureId = newFeature;
  }

  public Date getTimeStamp()
  {
    return timeStamp;
  }

  public void setTimeStamp(Date newTimeStamp)
  {
    timeStamp = newTimeStamp;
  }

  public String getEventType()
  {
    return eventType;
  }

  public void setEventType(String newEventType)
  {
    eventType = newEventType;
  }
/**
 * @return Returns the user.
 */
public String getUser()
{
    return user;
}
/**
 * @param user The user to set.
 */
public void setUser(String user)
{
    this.user = user;
}
}