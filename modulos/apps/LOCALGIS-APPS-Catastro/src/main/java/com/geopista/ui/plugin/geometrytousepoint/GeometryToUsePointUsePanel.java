/**
 * 
 */
package com.geopista.ui.plugin.geometrytousepoint;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.EstructuraDBListCellRenderer;
import com.geopista.app.catastro.model.beans.EstructuraDB;
import com.geopista.app.catastro.model.beans.TipoDestino;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * @author seilagamo
 * 
 */
public class GeometryToUsePointUsePanel extends JPanel {

    private static AppContext app = (AppContext) AppContext.getApplicationContext();
    private PlugInContext context = null;

    private JLabel jLabelDestinoLocal = null;
    private JComboBox jComboBoxDestinoLocal = null;
    private JLabel jLabelVivienda = null;
    private ComboBoxEstructuras jComboBoxVivienda = null;
    private JLabel jLabelEscalera = null;
    private ComboBoxEstructuras jComboBoxEscalera = null;
    private JLabel jLabelCodigoUso = null;
    private JTextField jTextCodigoUso = null;
    private JCheckBox jCheckCodigoUsoTodosElementos = null;
    private JLabel jLabelFeature = null;
    private Feature feature = null;
    
    public final static String SELECTEDDESTINOLOCAL = GeometryToUsePointUsePanel.class.getName()
            + "_destinoLocal";
    public final static String SELECTEDVIVIENDA = GeometryToUsePointUsePanel.class.getName()
            + "_vivienda";
    public final static String SELECTEDESCALERA = GeometryToUsePointUsePanel.class.getName()
            + "_escalera";
    public final static String TEXTCODIGOUSO = GeometryToUsePointUsePanel.class.getName()
            + "_codigoUso";
    public final static String CHECKTODOSELEMENTOS = GeometryToUsePointUsePanel.class.getName()
            + "_todosElementos";
    
    public GeometryToUsePointUsePanel(PlugInContext context) {
        super();
        this.context = context;
        initialize();
    }
    
    public GeometryToUsePointUsePanel(PlugInContext context, Feature feature) {
        super();
        this.context = context;
        this.feature = feature;
        initialize();
    }
    
    

    private void initialize() {
        Locale loc = I18N.getLocaleAsObject();
        ResourceBundle bundle = ResourceBundle.getBundle(
                "com.geopista.ui.plugin.geometryToUsePoint.languages.GeometryToUsePointi18n", loc,
                this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("GeometryToUsePoint", bundle);

        this.setLayout(new GridBagLayout());
        
        if (feature != null) {
            jLabelFeature = new JLabel(I18N.get("GeometryToUsePoint", "geometryToUsePoint.label.feature"));
            jLabelFeature.setText(jLabelFeature.getText() + " " + feature.getID());
            this.add(jLabelFeature, new GridBagConstraints(0, 0, 2, 1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                    0, 0));
        }
        jLabelDestinoLocal = new JLabel(I18N.get("GeometryToUsePoint",
                "geometryToUsePoint.label.uso"), JLabel.CENTER);
        this.add(jLabelDestinoLocal, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        this.add(getJComboBoxDestinoLocal(), new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        jLabelVivienda = new JLabel(I18N.get("GeometryToUsePoint",
                "geometryToUsePoint.label.vivienda"), JLabel.CENTER);
        this.add(jLabelVivienda, new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        this.add(getJComboBoxVivienda(), new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        jLabelEscalera = new JLabel(I18N.get("GeometryToUsePoint",
                "geometryToUsePoint.label.escalera"), JLabel.CENTER);
        this.add(jLabelEscalera, new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        this.add(getJComboBoxEscalera(), new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        jLabelCodigoUso = new JLabel(I18N.get("GeometryToUsePoint",
                "geometryToUsePoint.label.codigoUso"), JLabel.CENTER);
        this.add(jLabelCodigoUso, new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        this.add(getJTextCodigoUso(), new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                0, 0));
        if (feature == null) {
            this.add(getJCheckCodigoUsoTodosElementos(), new GridBagConstraints(0, 5, 2, 1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
                    0, 0));
        }
        initComboBoxes();
        initListeners();
        putToBlackboard(TEXTCODIGOUSO, "");
    }


    private void initComboBoxes() {
        // Inicializa los desplegables
        if (getFromBlackboard("ListaTipoDestino") == null) {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerTiposDestino();
            putToBlackboard("ListaTipoDestino", lst);
            EdicionUtils.cargarLista(getJComboBoxDestinoLocal(), lst);
        } else {
            EdicionUtils.cargarLista(getJComboBoxDestinoLocal(),
                    (ArrayList) getFromBlackboard("ListaTipoDestino"));
        }        
        putToBlackboard(SELECTEDDESTINOLOCAL, jComboBoxDestinoLocal.getSelectedItem());
    }

    private EstructuraDB obtenerElementoLista(ArrayList lst, String patron) {

        for (Iterator iteratorLista = lst.iterator(); iteratorLista.hasNext();) {
            EstructuraDB estructura = (EstructuraDB) iteratorLista.next();
            if (estructura.getPatron().equals(patron))
                return estructura;
        }
        return null;
    }
    
    /**
     * This method initializes jComboBoxDestinoLocal
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getJComboBoxDestinoLocal() {
        if (jComboBoxDestinoLocal == null) {
            jComboBoxDestinoLocal = new JComboBox();
            jComboBoxDestinoLocal.setRenderer(new EstructuraDBListCellRenderer());
            jComboBoxDestinoLocal.setKeySelectionManager(new ComboBoxTipoDestinoSelectionManager());
        }
        return jComboBoxDestinoLocal;
    }

    private JComboBox getJComboBoxVivienda() {
        if (jComboBoxVivienda == null) {
            Estructuras.cargarEstructura("Vivienda");
            jComboBoxVivienda = new ComboBoxEstructuras(Estructuras.getListaTipos(), null,
                    AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,
                            "es_ES"), true);
            putToBlackboard(SELECTEDVIVIENDA, jComboBoxVivienda.getSelectedItem());
        }
        return jComboBoxVivienda;
    }

    private JComboBox getJComboBoxEscalera() {
        if (jComboBoxEscalera == null) {
            Estructuras.cargarEstructura("Escalera");
            jComboBoxEscalera = new ComboBoxEstructuras(Estructuras.getListaTipos(), null,
                    AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,
                            "es_ES"), true);       
            putToBlackboard(SELECTEDESCALERA, jComboBoxEscalera.getSelectedItem());
        }
        return jComboBoxEscalera;
    }
    
    private JTextField getJTextCodigoUso() {
        if (jTextCodigoUso == null) {
            jTextCodigoUso = new JTextField("", 20);
            jTextCodigoUso.addFocusListener(new FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    putToBlackboard(TEXTCODIGOUSO, jTextCodigoUso.getText());
                }
            });
        }
        return jTextCodigoUso;
    }
    
    private JCheckBox getJCheckCodigoUsoTodosElementos() {
        if (jCheckCodigoUsoTodosElementos == null) {
            jCheckCodigoUsoTodosElementos = new JCheckBox(I18N.get("GeometryToUsePoint", "geometryToUsePoint.check.codigoUsoTodosElementos"), true);
            putToBlackboard(CHECKTODOSELEMENTOS, jCheckCodigoUsoTodosElementos.isSelected());
            jCheckCodigoUsoTodosElementos.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    putToBlackboard(CHECKTODOSELEMENTOS, jCheckCodigoUsoTodosElementos.isSelected());
                }
            });
        }
        return jCheckCodigoUsoTodosElementos;
    }

    /**
     * Añade acciones para cuando se modifica alguno de los combos
     */
    private void initListeners(){
        jComboBoxDestinoLocal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jTextCodigoUso.setText(getCodigoUso());      
                putToBlackboard(SELECTEDDESTINOLOCAL, ((JComboBox) e.getSource())
                        .getSelectedItem());
            }
        });  
        jComboBoxVivienda.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jTextCodigoUso.setText(getCodigoUso());
                putToBlackboard(SELECTEDVIVIENDA, ((JComboBox) e.getSource())
                        .getSelectedItem());
            }
        });  
        jComboBoxEscalera.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jTextCodigoUso.setText(getCodigoUso());         
                putToBlackboard(SELECTEDESCALERA, ((JComboBox) e.getSource())
                        .getSelectedItem());
            }
        }); 
    }
    
    /**
     * Calcula el código de uso
     * @return
     */
    private String getCodigoUso() {
        String codigoUso = "";
        if (jComboBoxDestinoLocal != null) {
            TipoDestino destino = (TipoDestino)jComboBoxDestinoLocal.getSelectedItem();
            codigoUso = codigoUso + destino.getPatron();
        }
        if (jComboBoxVivienda != null && jComboBoxVivienda.getSelectedPatron() != null) {
            if (!codigoUso.equals("")) {
                codigoUso = codigoUso + ".";
            }
            codigoUso = codigoUso + jComboBoxVivienda.getSelectedPatron();            
        }
        if (jComboBoxEscalera != null && jComboBoxEscalera.getSelectedPatron() != null) {
            if (!codigoUso.equals("")) {
                codigoUso = codigoUso + ".";
            }
            codigoUso = codigoUso + jComboBoxEscalera.getSelectedPatron();
        }
        if (feature == null) {
            putToBlackboard(TEXTCODIGOUSO, codigoUso);
        } else {
            putToBlackboard(Integer.toString(feature.getID()), codigoUso);
        }
        return codigoUso;
    }
    
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
}
