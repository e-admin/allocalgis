/*
 * Created on 17.08.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: SliderPanel.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:46 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/SliderPanel.java,v $
 */
package pirolPlugIns.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeListener;

import pirolPlugIns.i18n.PirolPlugInMessages;

/**
 * Panel has contains a label for a given text and a slider. 
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 */
public class SliderPanel extends JPanel {

    private static final long serialVersionUID = 8834312350626699575L;
    protected String textResourceKey = null;
    
    protected JSlider slider = new JSlider();
    protected Dictionary sliderLabelDictionary = new Hashtable();
    
    protected int min, max, defaultValue;

    protected String unitString = "%";
    
    
    public SliderPanel(String key, int min, int max, int value, String unitString) {
        super();
        this.textResourceKey = key;
        this.min = min;
        this.max = max;
        this.defaultValue = value;
        this.unitString = unitString;
        
        this.setupGUI();
    }

    protected void setupGUI(){
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.setLayout(new BorderLayout());
        this.add(DialogTools.getPanelWithLabels(PirolPlugInMessages.getString(this.textResourceKey ), 50), BorderLayout.CENTER); //$NON-NLS-1$
        
        for (int i=this.min; i<=this.max; i+=Math.round((this.max-this.min)/5.0)){
            this.sliderLabelDictionary.put(new Integer(i), new JLabel(i + this.unitString)); //$NON-NLS-1$
        }
        
        this.slider.setLabelTable(this.sliderLabelDictionary);
        this.slider.setPaintLabels(true);
        
        this.slider.setMaximum(this.max);
        this.slider.setMinimum(this.min);
        this.slider.setMajorTickSpacing((this.max-this.min)/5);
        this.slider.setMinorTickSpacing((this.max-this.min)/10);
        this.slider.setPaintTicks(true);

        this.slider.setMinimumSize(new Dimension(150, 20));
        this.slider.setValue(this.defaultValue);
        
        this.add(this.slider, BorderLayout.SOUTH);
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public String getUnitString() {
        return unitString;
    }

    public void addChangeListener(ChangeListener arg0) {
        slider.addChangeListener(arg0);
    }

    public int getMaximum() {
        return slider.getMaximum();
    }

    public int getMinimum() {
        return slider.getMinimum();
    }

    public int getValue() {
        return slider.getValue();
    }

    public boolean isEnabled() {
        return slider.isEnabled();
    }

    public boolean isVisible() {
        return slider.isVisible();
    }

    public void setEnabled(boolean arg0) {
        slider.setEnabled(arg0);
    }

    public void setMaximum(int arg0) {
        slider.setMaximum(arg0);
    }

    public void setMinimum(int arg0) {
        slider.setMinimum(arg0);
    }

    public void setMinorTickSpacing(int arg0) {
        slider.setMinorTickSpacing(arg0);
    }

    public void setValue(int arg0) {
        slider.setValue(arg0);
    }

    public void setVisible(boolean arg0) {
        slider.setVisible(arg0);
    }

}
