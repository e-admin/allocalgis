/**
 * 
 */
package com.geopista.ui.plugin.geometrytovolumepoint;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;


/**
 * @author seilagamo
 *
 */
public class GeometryToVolumePointLayerPanel extends JPanel {

    AppContext appContext = (AppContext) AppContext.getApplicationContext();
    private static final Log logger = LogFactory.getLog(GeometryToVolumePointLayerPanel.class);
    private PlugInContext context = null;

    private JPanel jPanel = null;
    private JLabel labelLayerOrigen = null;
    private JComboBox comboBoxLayerOrigen = null;
    private JLabel labelLayerDestino = null;
    private JComboBox comboBoxLayerDestino = null;
    private JLabel labelCampoDestino = null;
    private JComboBox comboBoxCampoDestino = null;

    
    public static final String SELECTEDSOURCELAYER = GeometryToVolumePointLayerPanel.class.getName()
            + "_sourceLayer";
    public static final String SELECTEDTARGETLAYER = GeometryToVolumePointLayerPanel.class.getName()
            + "_targetLayer";
    public static final String SELECTEDCAMPODESTINO = GeometryToVolumePointLayerPanel.class.getName()
            + "_campoDestino";

    private static final String CAPA_ORIGEN = "PG-LI";
    private static final String CAPA_DESTINO = "PG-AA";
    
    public GeometryToVolumePointLayerPanel(PlugInContext context) {
        super();
        this.context = context;
        initialize();

        Locale loc = Locale.getDefault();
        ResourceBundle bundle2 = ResourceBundle.getBundle(
                "com.geopista.ui.plugin.geometryToVolumePoint.languages.GeometryToVolumePointi18n", loc,
                this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("GeometryToVolumePoint", bundle2);
    }

    private void initialize() {

        this.setLayout(new GridBagLayout());

        this.add(getJPanel(), new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
        setup();
    }    
    
    private void setup() {
        try {
            Layer capaDefecto = null;
            String nombreCapaOrigen = null;
            for (int i = 0; i < context.getActiveTaskComponent().getLayerManager().getLayers().size(); i++) {
                if (context.getActiveTaskComponent().getLayerManager().getLayer(i).getName().equals(CAPA_ORIGEN)){
                    capaDefecto = context.getActiveTaskComponent().getLayerManager().getLayer(i);
                    nombreCapaOrigen = context.getActiveTaskComponent().getLayerManager().getLayer(i).getName();
                    break;
                }
            }
            if (capaDefecto == null) {
                capaDefecto = (Layer) context.getCandidateLayer(0);
            }
            
            List lstCapas = new ArrayList();;
            for (Iterator iterator = context.getActiveTaskComponent().getLayerManager().getLayers().iterator(); 
            							iterator.hasNext();) {
				GeopistaLayer layer = (GeopistaLayer) iterator.next();
				if(layer.isLocal()){
					lstCapas.add(layer);
				}
			}
            
            GeopistaUtil.initializeLayerComboBox(null, comboBoxLayerOrigen, capaDefecto, I18N.get(
                    "GeometryToVolumePoint", "geometryToVolumePoint.label.capaOrigen"), lstCapas);
            putToBlackboard(SELECTEDSOURCELAYER, (Layer) comboBoxLayerOrigen.getSelectedItem());
            
            capaDefecto = null;
            if (nombreCapaOrigen != null && nombreCapaOrigen.equals(CAPA_ORIGEN)) {
                for (int i = 0; i < context.getActiveTaskComponent().getLayerManager().getLayers().size(); i++) {
                    if (context.getActiveTaskComponent().getLayerManager().getLayer(i).getName().equals(CAPA_DESTINO)){
                        capaDefecto = context.getActiveTaskComponent().getLayerManager().getLayer(i);
                        break;
                    }
                }                
            }
            if (capaDefecto == null) {
                capaDefecto = (Layer) context.getCandidateLayer(0);
            }
            
           
            GeopistaUtil.initializeLayerComboBox(null, comboBoxLayerDestino, capaDefecto, I18N.get(
                    "GeometryToVolumePoint", "geometryToVolumePoint.label.capaDestino"), lstCapas);
            putToBlackboard(SELECTEDTARGETLAYER, (Layer) comboBoxLayerDestino.getSelectedItem());

        } catch (Exception e) {
            logger.error("setup(PlugInContext)", e);
        }
    }

    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            if (labelLayerOrigen == null) {
                labelLayerOrigen = new JLabel(I18N.get("GeometryToVolumePoint", "geometryToVolumePoint.label.capaOrigen"));                
            }
            
            jPanel.add(labelLayerOrigen, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                    0, 0));
            jPanel.add(getComboBoxLayerOrigen(), new GridBagConstraints(1, 0, 1, 1, 0.5, 0.5,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                    0, 0));
            if (labelLayerDestino == null) {
                labelLayerDestino = new JLabel(I18N.get("GeometryToVolumePoint", "geometryToVolumePoint.label.capaDestino"));                
            }
            jPanel.add(labelLayerDestino, new GridBagConstraints(0, 1, 1, 1, 0.5, 0.5,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                    0, 0));
            jPanel.add(getComboBoxLayerDestino(), new GridBagConstraints(1, 1, 1, 1, 0.5, 0.5,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                    0, 0));            
            if (labelCampoDestino == null) {
                labelCampoDestino = new JLabel(I18N.get("GeometryToVolumePoint", "geometryToVolumePoint.label.campoDestino"));
            }
            jPanel.add(labelCampoDestino, new GridBagConstraints(0, 2, 1, 1, 0.5, 0.5,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                    0, 0));
            jPanel.add(getComboBoxCampoDestino(), new GridBagConstraints(1, 2, 1, 1, 0.5, 0.5,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                    0, 0)); 
            
        }
        return jPanel;
    }

    private JComboBox getComboBoxLayerOrigen() {
        if (comboBoxLayerOrigen == null) {
            comboBoxLayerOrigen = new JComboBox();
            comboBoxLayerOrigen.setName("sourceLayerCombo");
            comboBoxLayerOrigen.setPreferredSize(new java.awt.Dimension(160, 21));            
            comboBoxLayerOrigen.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    putToBlackboard(SELECTEDSOURCELAYER, (Layer) ((JComboBox) e.getSource())
                            .getSelectedItem());
                }
            });
        }
        return comboBoxLayerOrigen;
    }

    private JComboBox getComboBoxLayerDestino() {
        if (comboBoxLayerDestino == null) {
            comboBoxLayerDestino = new JComboBox();
            comboBoxLayerDestino.setPreferredSize(new java.awt.Dimension(160, 21));
            comboBoxLayerDestino.setName("targetLayerCombo");            
            comboBoxLayerDestino.addActionListener(new ActionListener() {

                private Layer lastLayer = null;

                public void actionPerformed(ActionEvent e) {
                    Layer targetLayer = (Layer) ((JComboBox) e.getSource()).getSelectedItem();
                    putToBlackboard(SELECTEDTARGETLAYER, targetLayer);

                    if (lastLayer == targetLayer) {
                        return;
                    }
                    lastLayer = targetLayer;
                    comboBoxCampoDestino.setModel(new DefaultComboBoxModel(new Vector(
                            candidateAttributeNames(targetLayer))));
                    if (!candidateAttributeNames(targetLayer).isEmpty()) {
                        comboBoxCampoDestino.setSelectedItem(attributeName(
                                candidateAttributeNames(targetLayer), 0));
                        putToBlackboard(SELECTEDCAMPODESTINO, (String)comboBoxCampoDestino.getSelectedItem());
                    } else {
                        putToBlackboard(SELECTEDCAMPODESTINO, null);
                    }
                }
            });
        }
        return comboBoxLayerDestino;
    }
    
    private JComboBox getComboBoxCampoDestino() {
        if (comboBoxCampoDestino == null) {
            comboBoxCampoDestino = new JComboBox();
            comboBoxCampoDestino.setPreferredSize(new Dimension(160, 21));
            comboBoxCampoDestino.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String campoDestino = (String) ((JComboBox) e.getSource()).getSelectedItem();
                    putToBlackboard(SELECTEDCAMPODESTINO, campoDestino);
                }
            });
        }
        return comboBoxCampoDestino;
    }
    
    private List candidateAttributeNames(Layer layer) {
        ArrayList candidateAttributeNames = new ArrayList();
        FeatureSchema schema = layer.getFeatureCollectionWrapper().getFeatureSchema();
        for (int i = 0; i < schema.getAttributeCount(); i++) {
            if (typeToConverterMap.keySet().contains(schema.getAttributeType(i))) {
                candidateAttributeNames.add(schema.getAttributeName(i));
            }
        }
        return candidateAttributeNames;
    }

    private String attributeName(List attributeNames, int preferredIndex) {
        return (String) attributeNames.get(
            attributeNames.size() > preferredIndex ? preferredIndex : 0);
    }
    
    private static interface Converter {
        public Object convert(double d);
    }
    
    private Object convert(double d, AttributeType attributeType) {
        return ((Converter) typeToConverterMap.get(attributeType)).convert(d);
    }
    
    private Map typeToConverterMap = new HashMap() {
        {
            put(AttributeType.STRING, new Converter() {
                public Object convert(double d) {
                    return "" + d;
                }
            });
            put(AttributeType.INTEGER, new Converter() {
                public Object convert(double d) {
                    return new Integer((int) d);
                }
            });
            put(AttributeType.DOUBLE, new Converter() {
                public Object convert(double d) {
                    return new Double(d);
                }
            });
        }
    };

    
    // Métodos del Blackboard

    private Object getFromBlackboard(String key) {
        try {
            Blackboard bk = context.getWorkbenchGuiComponent().getActiveTaskComponent()
                    .getLayerViewPanel().getBlackboard();
            return bk.get(key);
        } catch (NullPointerException e) {// ocurre cuando la ventana activa no es de GIS
            return null;
        }
    }

    private void putToBlackboard(String key, Object value) {
        try {
            Blackboard bk = context.getWorkbenchGuiComponent().getActiveTaskComponent()
                    .getLayerViewPanel().getBlackboard();
            bk.put(key, value);
        } catch (NullPointerException e) {// ocurre cuando la ventana activa no es de GIS
            return;
        }
    }
    
    public String validateInput() {
        if ((String)getFromBlackboard(SELECTEDCAMPODESTINO) == null || ((String)getFromBlackboard(SELECTEDCAMPODESTINO)).equals("")) {
            return I18N.get("GeometryToVolumePoint", "geometryToVolumePoint.error.noCampoDestino");
        }
        return null;
    }
}
