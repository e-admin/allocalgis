package com.geopista.ui.dialogs;

import java.util.Vector;

import javax.swing.Icon;

import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

public interface IInventarioDialog {
  
    public boolean wasOKPressed();  
    public void setFeatures(Vector features);
    public void buildDialog();
    public void setSideBarImage(Icon icon);
    public void setSideBarDescription(String description);  
    public void setLock();
    public void setVisible(boolean visible);
    public void addPanel(FeatureExtendedPanel form);
    public void addPanel(FeatureExtendedPanel form, int pos);
    public void removePanel(FeatureExtendedPanel form);
    public void setPanelEnabled(int pos, boolean act);
    public Feature getFeature();
    public Vector getFeatures();
    public void setLayerViewPanel(LayerViewPanel layerViewPanel);
    
}