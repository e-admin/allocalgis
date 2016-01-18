/**
 * 
 */
package com.geopista.app.administrador.entidades;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;


/**
 * @author seilagamo
 *
 */
public class JPanelMunicipios extends JPanel {
    
    Logger logger = Logger.getLogger(JPanelMunicipios.class);
    
    private static final String IDMUNICIPIO_CODE = "id_municipio";
    private Map municipiosCheckBoxes = null;
    private Collection listaMunicipios;
    private Collection listaMunicipiosString;
    private boolean modoEdicion = false;
    private JFrame framePadre;
    private int contadorMunicipios = 0;
    private ResourceBundle messages;
    
    public JPanelMunicipios(ResourceBundle messages, JFrame framePadre, Collection listaMunicipios,
            boolean modoEdicion) {
        this.framePadre = framePadre;
        this.listaMunicipios = listaMunicipios;
        Iterator it = listaMunicipios.iterator();
        listaMunicipiosString = new HashSet();
        while (it.hasNext()) {
            listaMunicipiosString.add(((Municipio)it.next()).getId());
        }
        this.modoEdicion = modoEdicion;
        this.messages = messages;
        initialize();        
    }
    
    private void initialize() {
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.setBackground(new Color(255, 255, 255));
    }
    
    
    public void pintarMunicipios() {
        try{
            this.removeAll();
            if (listaMunicipios == null) {
                this.repaint();
                this.invalidate();
                this.validate();
            }
            municipiosCheckBoxes = new HashMap();
            contadorMunicipios = 0;
            Iterator it = listaMunicipios.iterator();
            while (it.hasNext()) {
                Municipio municipio = (Municipio) it.next();
                JCheckBox jCheckBoxMunicipio = new JCheckBox();
                jCheckBoxMunicipio.setBackground(new java.awt.Color(255, 255, 255));
                jCheckBoxMunicipio.setEnabled(modoEdicion);
                jCheckBoxMunicipio.putClientProperty(IDMUNICIPIO_CODE, municipio);
                jCheckBoxMunicipio.setText(municipio.getId() + " " + municipio.getNombre());
                municipiosCheckBoxes.put(municipio.getId(), jCheckBoxMunicipio);            
                this.add(jCheckBoxMunicipio, new org.netbeans.lib.awtextra.AbsoluteConstraints(5,
                        20 + (contadorMunicipios * 20), -1, -1));
                
                contadorMunicipios++;
            }
            this.repaint();
            this.invalidate();
            this.validate();
        } catch (Exception e) {
            
        }
    }
    
    
    
    public JCheckBox aniadirMunicipio(String id_municipio) {
        String sMensaje = "";
        JCheckBox nuevoMunicipioCheckBox = null;
        try {

            Municipio municipio = new OperacionesAdministrador(Constantes.url).getMunicipio(id_municipio);
            if (municipio == null) {
                sMensaje = messages.getString("JPanelMunicipios.mensajes.errorNoExisteMunicipio");
                JOptionPane optionPane = new JOptionPane(sMensaje, JOptionPane.ERROR_MESSAGE);
                JDialog dialog = optionPane.createDialog(this, "");
                dialog.setVisible(true);
            } else {
                nuevoMunicipioCheckBox = new JCheckBox();
                nuevoMunicipioCheckBox.setBackground(new java.awt.Color(255, 255, 255));
                nuevoMunicipioCheckBox.setEnabled(modoEdicion);
                nuevoMunicipioCheckBox.putClientProperty(IDMUNICIPIO_CODE, id_municipio);
                if (!listaMunicipiosString.contains(municipio.getId())) {
                    nuevoMunicipioCheckBox.setText(municipio.getId() + " " + municipio.getNombre());
                    municipiosCheckBoxes.put(municipio.getId(), nuevoMunicipioCheckBox);
                    this.add(nuevoMunicipioCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(5,
                            20 + (contadorMunicipios * 20), -1, -1));
                    listaMunicipiosString.add(municipio.getId());
                    contadorMunicipios++;
                    this.repaint();
                    this.invalidate();
                    this.validate();
                }
            }
        } catch(Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println("ERROR: " + sw.toString());
            logger.error("Exception: " + sw.toString());
        }
        return nuevoMunicipioCheckBox;
    }
    
    
    public void checkMunicipio (String municipio) {        
        ((JCheckBox) municipiosCheckBoxes.get(municipio)).setSelected(true);                
    }
    
    public void unCheckMunicipio (String municipio) {
        ((JCheckBox) municipiosCheckBoxes.get(municipio)).setSelected(false);
    }
    
    public void editable(boolean b) {
        Iterator it = municipiosCheckBoxes.keySet().iterator();
        while (it.hasNext()) {
            ((JCheckBox) municipiosCheckBoxes.get(it.next())).setEnabled(b);
        }
    }
        
    /**
     * @return the municipiosCheckBoxes
     */
    public Map getMunicipiosCheckBoxes() {
        return municipiosCheckBoxes;
    }


}
