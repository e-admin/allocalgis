/**
 * DocumentSheetPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * DocumentSheetPanel.java
 *
 * Created on March 6, 2007, 12:08 PM
 */

package it.businesslogic.ireport.gui.sheet;

import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.event.ReportBandChangedEvent;
import it.businesslogic.ireport.gui.event.ReportBandsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;
import it.businesslogic.ireport.gui.event.ReportElementsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportFrameActivatedEvent;
import it.businesslogic.ireport.gui.event.ReportFrameActivatedListener;
import it.businesslogic.ireport.gui.event.ReportListener;
import it.businesslogic.ireport.gui.event.ReportObjectsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportSubDatasetChangedEvent;
import it.businesslogic.ireport.gui.event.ReportSubDatasetChangedListener;
import it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent;
import it.businesslogic.ireport.gui.event.SubDatasetObjectChangedListener;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;

import java.awt.BorderLayout;

import javax.swing.JLabel;

/**
 *
 * @author  gtoffoli
 */
public class DocumentSheetPanel extends javax.swing.JPanel implements 
        ReportListener, LanguageChangedListener, 
        ReportFrameActivatedListener,
        SubDatasetObjectChangedListener,
        ReportSubDatasetChangedListener {
    
    private ReportElementSheetPanel reportElementSheetPanel = null;
    private ReportBandSheetPanel reportBandSheetPanel = null;
    private ReportSheetPanel reportSheetPanel = null;
    private ReportObjectSheetPanel reportObjectSheetPanel = null;

    
    
    public static final int SHOW_NOTHING = -1;
    public static final int SHOW_DOCUMENT_SHEET = 0;
    public static final int SHOW_BAND_SHEET = 1;
    public static final int SHOW_REPORTELEMENT_SHEET = 2;
    public static final int SHOW_REPORTOBJECT_SHEET = 3;
    
    
    int currentMode = -2;
    
    
    private JReportFrame jReportFrame = null;
    
    /**
     * Creates new form DocumentSheetPanel
     */
    public DocumentSheetPanel() {
        initComponents();
        
        setReportElementSheetPanel(new ReportElementSheetPanel());
        setReportBandSheetPanel(new ReportBandSheetPanel());
        setReportSheetPanel(new ReportSheetPanel());
        setReportObjectSheetPanel(new ReportObjectSheetPanel());
        
        getReportBandSheetPanel().setResizeWeight(0.4);
        getReportElementSheetPanel().setResizeWeight(0.4);
        getReportSheetPanel().setResizeWeight(0.4);
        getReportObjectSheetPanel().setResizeWeight(0.4);
        
        MainFrame mf = MainFrame.getMainInstance();
        mf.addReportListener( this );
        mf.addReportFrameActivatedListener( this);
        
        this.setCurrentMode(SHOW_NOTHING);
        
    }

    public JReportFrame getJReportFrame() {
        return jReportFrame;
    }

    public void setJReportFrame(JReportFrame jReportFrame) {
        
        if (this.jReportFrame != jReportFrame)
        {
            this.jReportFrame = jReportFrame;
            updateView();
        }
    }
    
    public void updateView()
    {
        if (jReportFrame == null) {
            setCurrentMode(SHOW_NOTHING);
            return;
        }
       
        if ((jReportFrame.getSelectedCrosstabEditorPanel() != null &&
             jReportFrame.getSelectedCrosstabEditorPanel().getSelectedElements().size() > 0) ||
             jReportFrame.getSelectedCrosstabEditorPanel() == null &&
             jReportFrame.getSelectedElements().size() > 0)
        {
            setCurrentMode(SHOW_REPORTELEMENT_SHEET);
        }
        else if (jReportFrame.getSelectedObjects().size() > 0)
        {
            setCurrentMode(SHOW_REPORTOBJECT_SHEET);
        }
        else if (jReportFrame.getSelectedBands().size() > 0)
        {
            setCurrentMode(SHOW_BAND_SHEET);
        }
        else
        {
            setCurrentMode(SHOW_DOCUMENT_SHEET);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void reportElementsSelectionChanged(ReportElementsSelectionEvent evt) {
        updateView();
    }
    
    public void reportBandsSelectionChanged(ReportElementsSelectionEvent evt) {
        updateView();
    }
    
    public void reportObjectsSelectionChanged(ReportObjectsSelectionEvent evt) {
        updateView();
    }

    public void reportElementsChanged(ReportElementChangedEvent evt) {
    }

    public void reportBandChanged(ReportBandChangedEvent evt) {
    }

    public void languageChanged(LanguageChangedEvent evt) {
    }

    public void reportFrameActivated(ReportFrameActivatedEvent evt) {
        
        this.setJReportFrame( evt.getReportFrame() );
    }
    
     public void reportBandsSelectionChanged(ReportBandsSelectionEvent evt) {
        updateView();
    }
     
     public int getCurrentMode() {
         return currentMode;
     }
     
     public void setCurrentMode(int newMode)
     {
         if (newMode == currentMode) return;
         else
         {
             this.removeAll();
             this.currentMode = newMode;
             
             switch (currentMode)
             {
                 case SHOW_NOTHING:
                     JLabel label = new JLabel(it.businesslogic.ireport.util.I18n.getString("properties","Properties"));
                     label.setHorizontalAlignment( JLabel.CENTER);
                     this.add(label , BorderLayout.CENTER );
                     break;
                 case SHOW_DOCUMENT_SHEET:
                     this.add( getReportSheetPanel(), BorderLayout.CENTER );
                     break;
                 case SHOW_BAND_SHEET:
                     this.add( getReportBandSheetPanel(), BorderLayout.CENTER );
                     break;
                case SHOW_REPORTELEMENT_SHEET:
                    this.add( getReportElementSheetPanel(), BorderLayout.CENTER );
                     break;
                case SHOW_REPORTOBJECT_SHEET:
                    this.add( getReportObjectSheetPanel(), BorderLayout.CENTER );
                     break;
             }
             
             this.updateUI();
         }
         
     }

    public ReportElementSheetPanel getReportElementSheetPanel() {
        return reportElementSheetPanel;
    }

    public void setReportElementSheetPanel(ReportElementSheetPanel reportElementSheetPanel) {
        this.reportElementSheetPanel = reportElementSheetPanel;
    }

    public ReportBandSheetPanel getReportBandSheetPanel() {
        return reportBandSheetPanel;
    }

    public void setReportBandSheetPanel(ReportBandSheetPanel reportBandSheetPanel) {
        this.reportBandSheetPanel = reportBandSheetPanel;
    }

    public ReportSheetPanel getReportSheetPanel() {
        return reportSheetPanel;
    }

    public void setReportSheetPanel(ReportSheetPanel reportSheetPanel) {
        this.reportSheetPanel = reportSheetPanel;
    }
    
    public ReportObjectSheetPanel getReportObjectSheetPanel() {
        return reportObjectSheetPanel;
    }

    public void setReportObjectSheetPanel(ReportObjectSheetPanel reportObjectSheetPanel) {
        this.reportObjectSheetPanel = reportObjectSheetPanel;
    }
    
    public void setPropertyLabelError(String propertyName, String error)
    {
        switch (getCurrentMode())
             {
                 case SHOW_DOCUMENT_SHEET:
                     getReportSheetPanel().setPropertyLabelError(propertyName, error);
                     break;
                 case SHOW_BAND_SHEET:
                     getReportBandSheetPanel().setPropertyLabelError(propertyName, error);
                     break;
                case SHOW_REPORTELEMENT_SHEET:
                     getReportElementSheetPanel().setPropertyLabelError(propertyName, error);
                     break;
                case SHOW_REPORTOBJECT_SHEET:
                     getReportObjectSheetPanel().setPropertyLabelError(propertyName, error);
                     break;
             }
    }

    public void subDatasetObjectChanged(SubDatasetObjectChangedEvent evt) {
        if (getCurrentMode() == SHOW_REPORTOBJECT_SHEET &&
            getReportObjectSheetPanel().getSelection().contains(evt.getNewValue()))
        {
            // this prevent to update for an event generate by the
            // ReportObjectSheetPanel itself
            if (!getReportObjectSheetPanel().isInit()) 
            {
              getReportObjectSheetPanel().updateSelection();  
            }
        }
    }

    public void reportSubDatasetChanged(ReportSubDatasetChangedEvent evt) {
        if (getCurrentMode() == SHOW_REPORTOBJECT_SHEET && evt.getAction() == evt.CHANGED)
        {
            boolean needUpdate = false;
            if (getReportObjectSheetPanel().getSelection().contains(evt.getSubDataset()))
            {
                needUpdate = true;
            }
            
            for (int i=0; needUpdate==false && i<evt.getElements().size(); ++i)
            {
                if (getReportObjectSheetPanel().getSelection().contains( evt.getElements().get(i) ))
                {
                    needUpdate = true;
                }
            }
            
            if (needUpdate)
            {
              // this prevent to update for an event generate by the
              // ReportObjectSheetPanel itself
              if (!getReportObjectSheetPanel().isInit())
              {
                getReportObjectSheetPanel().updateSelection();  
              }
            }
        }
    }
    
}
