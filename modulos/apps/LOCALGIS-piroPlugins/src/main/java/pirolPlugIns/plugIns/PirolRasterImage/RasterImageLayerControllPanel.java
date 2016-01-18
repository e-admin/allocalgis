/**
 * RasterImageLayerControllPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 03.07.2005
 *
 * CVS information:
 *  $Author: miriamperez $
 *  $Date: 2009/07/03 12:31:31 $
 *  $ID$
 *  $Revision: 1.1 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/plugIns/PirolRasterImage/RasterImageLayerControllPanel.java,v $
 *
 */
package pirolPlugIns.plugIns.PirolRasterImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import pirolPlugIns.dialogs.ValueChecker;
import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.RasterImageSupport.RasterImageLayer;

/**
 * 
 * Panel that contains controlls to customize the appearance of
 * a RasterImage layer.
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class RasterImageLayerControllPanel extends JPanel implements ValueChecker, ActionListener {
    
    private static final long serialVersionUID = 619781257815627447L;

    protected JColorChooser colorChooser = new JColorChooser();
    protected RasterImageLayer rasterImageLayer = null;
    protected JSlider transparencySlider = new JSlider(), speedSlider = new JSlider();
    protected Dictionary sliderLabelDictionary = new Hashtable();
    protected JCheckBox useTransCB = null;

    public RasterImageLayerControllPanel(RasterImageLayer rasterImageLayer) {
        super(new BorderLayout());

        this.rasterImageLayer = rasterImageLayer;
        
        this.setupGui();
    }
    
    public void setupGui(){
        
        JPanel transparencyOnOffPanel = new JPanel();
        transparencyOnOffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        transparencyOnOffPanel.add(new JLabel(PirolPlugInMessages.getString("Do-you-want-a-color-to-be-transparent"))); //$NON-NLS-1$
        this.useTransCB = new JCheckBox();
        transparencyOnOffPanel.add(this.useTransCB);
        this.useTransCB.addActionListener(this);
        
        this.add(transparencyOnOffPanel, BorderLayout.NORTH);
        
        this.colorChooser.getSelectionModel().setSelectedColor(this.rasterImageLayer.getTransparentColor());
        this.colorChooser.setToolTipText(PirolPlugInMessages.getString("Choose-transparent-color")); //$NON-NLS-1$
        this.add(this.colorChooser, BorderLayout.CENTER);
        
        JPanel slidersPanel = new JPanel();
        slidersPanel.setLayout(new GridLayout(2,1));
        
        JPanel transparencySliderPanel = new JPanel();
        transparencySliderPanel.setLayout(new GridLayout(2,1));
        transparencySliderPanel.add(new JLabel(PirolPlugInMessages.getString("set-overall-transparency"))); //$NON-NLS-1$
        
        for (int i=0; i<=100; i+=25){
            this.sliderLabelDictionary.put(new Integer(i), new JLabel(i + "%")); //$NON-NLS-1$
        }
        this.transparencySlider.setLabelTable(this.sliderLabelDictionary);
        this.transparencySlider.setPaintLabels(true);
        
        this.transparencySlider.setMaximum(100);
        this.transparencySlider.setMinimum(0);
        this.transparencySlider.setMajorTickSpacing(10);
        this.transparencySlider.setMinorTickSpacing(5);
        this.transparencySlider.setPaintTicks(true);

        this.transparencySlider.setMinimumSize(new Dimension(150, 20));
        this.transparencySlider.setValue((int)(this.rasterImageLayer.getTransparencyLevel() * 100) );
        
        transparencySliderPanel.add(this.transparencySlider);
        
        slidersPanel.add(transparencySliderPanel);
        
        JPanel speedSliderPanel = new JPanel();
        speedSliderPanel.setLayout(new GridLayout(2,1));
        speedSliderPanel.add(new JLabel(PirolPlugInMessages.getString("processing-speed"))); //$NON-NLS-1$
        
        this.speedSlider.setLabelTable(this.sliderLabelDictionary);
        this.speedSlider.setPaintLabels(true);
        
        this.speedSlider.setMaximum(85);
        this.speedSlider.setMinimum(15);
        this.speedSlider.setMajorTickSpacing(10);
        this.speedSlider.setMinorTickSpacing(5);
        this.speedSlider.setPaintTicks(true);

        this.speedSlider.setMinimumSize(new Dimension(150, 20));
        this.speedSlider.setValue((int)((1.0 - RasterImageLayer.getFreeRamFactor()) * 100) );
        
        speedSliderPanel.add(this.speedSlider);
        
        slidersPanel.add(speedSliderPanel);
        
        this.add(slidersPanel, BorderLayout.SOUTH);
        
        this.setPreferredSize(new Dimension(300, this.colorChooser.getHeight() + 50));
        
        this.doLayout();
        
        // setup checkbox (and color chooser)
        this.useTransCB.setSelected(this.rasterImageLayer.getTransparentColor()!=null);
        this.actionPerformed(null);
    }


    /** 
     * @inheritDoc
     */
    public boolean areValuesOk() {
        
        this.rasterImageLayer.setFiringAppearanceEvents(false);
        
        if (this.useTransCB.isSelected()) {
            this.rasterImageLayer.setTransparentColor(this.colorChooser.getColor());
        } else {
            this.rasterImageLayer.setTransparentColor(null);
        }
        int newTransparencyValue = this.transparencySlider.getValue();
        this.rasterImageLayer.setTransparencyLevelInPercent(newTransparencyValue);
        
        int newFreeRamValue = this.speedSlider.getValue();
        RasterImageLayer.setFreeRamFactor(1.0 - newFreeRamValue/100d);
        
        this.rasterImageLayer.setFiringAppearanceEvents(true);
        
        return true;
    }

    /**
     *@inheritDoc
     */
    public void actionPerformed(ActionEvent event) {
        // the checkbox was toogled
        
        // diabling the color chooser has no effect!
        this.colorChooser.setEnabled(this.useTransCB.isSelected());
        this.colorChooser.setVisible(this.useTransCB.isSelected());
    }


}
