package com.geopista.ui.plugin;
import java.util.Date;

import com.vividsolutions.jump.feature.Feature;

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