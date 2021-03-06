/**
 * EditWMSQueryPanel.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.io.wms;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.geopista.io.datasource.wms.WMService;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.TransparencyPanel;
import com.vividsolutions.jump.workbench.ui.plugin.wms.MapLayerPanel;
import com.vividsolutions.jump.workbench.ui.plugin.wms.MapLayerWizardPanel;

public class EditWMSQueryPanel extends JPanel {
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private MapLayerPanel mapLayerPanel = new MapLayerPanel();
    private JLabel srsLabel = new JLabel();
    private DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
    private JComboBox srsComboBox = new JComboBox(comboBoxModel);
    private EnableCheck[] enableChecks =
        new EnableCheck[] {
            new EnableCheck() { public String check(JComponent component) {
                return mapLayerPanel.getChosenMapLayers().isEmpty()
                    ? "At least one WMS layer must be chosen"
                    : null;
            }
        }, new EnableCheck() {
            public String check(JComponent component) {
                return srsComboBox.getSelectedItem() == null
                    ? MapLayerWizardPanel.NO_COMMON_SRS_MESSAGE
                    : null;
            }
        }
    };
    private Border border1;
    private TransparencyPanel transparencyPanel = new TransparencyPanel();
    private JLabel transparencyLabel = new JLabel();
    private JLabel urlLabel = new JLabel();
    private JTextField urlTextField = new JTextField();
    public EditWMSQueryPanel(
        WMService service,
        List initialChosenMapLayers,
        String initialSRS,
        int alpha) {
        try {
            jbInit();
            String url = service.getServerUrl();
            if (url.endsWith("?") || url.endsWith("&")) {
                url = url.substring(0, url.length() - 1);
            }
            urlTextField.setText(url);
            mapLayerPanel.init(service, initialChosenMapLayers);
            updateComboBox();
            srsComboBox.setSelectedItem(initialSRS);
            mapLayerPanel.add(new InputChangedListener() {
                public void inputChanged() {
                    updateComboBox();
                }
            });
            setAlpha(alpha);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public int getAlpha() {
        return 255 - transparencyPanel.getSlider().getValue();
    }
    private void setAlpha(int alpha) {
        transparencyPanel.getSlider().setValue(255 - alpha);
    }
    public String getSRS() {
        return (String) srsComboBox.getSelectedItem();
    }
    /**
    * Method updateComboBox.
    */
    private void updateComboBox() {
        String selectedSRS = (String) srsComboBox.getSelectedItem();
        comboBoxModel.removeAllElements();
        for (Iterator i = mapLayerPanel.commonSRSList().iterator(); i.hasNext();) {
            String commonSRS = (String) i.next();
            comboBoxModel.addElement(commonSRS);
        }
        //selectedSRS might no longer be in the combobox, in which case nothing will be selected. [Jon Aquino]
        srsComboBox.setSelectedItem(selectedSRS);
        if ((srsComboBox.getSelectedItem() == null)
            && (srsComboBox.getItemCount() > 0)) {
            srsComboBox.setSelectedIndex(0);
        }
    }
    void jbInit() throws Exception {
        border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        this.setLayout(gridBagLayout1);
        srsLabel.setText("Coordinate Reference System:");
        this.setBorder(border1);
        this.setToolTipText("");
        srsComboBox.setMinimumSize(new Dimension(125, 21));
        srsComboBox.setToolTipText("");
        transparencyLabel.setText("Transparency:");
        urlLabel.setText("URL:");
        urlTextField.setBorder(null);
        urlTextField.setOpaque(false);
        urlTextField.setEditable(false);
        this.add(
            mapLayerPanel,
            new GridBagConstraints(
                1,
                2,
                3,
                1,
                1.0,
                1.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(10, 0, 10, 0),
                0,
                0));
        this.add(
            srsLabel,
            new GridBagConstraints(
                1,
                3,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 10, 5),
                0,
                0));
        this.add(
            srsComboBox,
            new GridBagConstraints(
                3,
                3,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 10, 0),
                0,
                0));
        this.add(
            transparencyPanel,
            new GridBagConstraints(
                3,
                6,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        this.add(
            transparencyLabel,
            new GridBagConstraints(
                1,
                6,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0),
                0,
                0));
        this.add(
            urlLabel,
            new GridBagConstraints(
                1,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 5),
                0,
                0));
        this.add(
            urlTextField,
            new GridBagConstraints(
                2,
                0,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0),
                0,
                0));
    }
    public List getChosenMapLayers() {
        return mapLayerPanel.getChosenMapLayers();
    }
    public EnableCheck[] getEnableChecks() {
        return enableChecks;
    }
}
