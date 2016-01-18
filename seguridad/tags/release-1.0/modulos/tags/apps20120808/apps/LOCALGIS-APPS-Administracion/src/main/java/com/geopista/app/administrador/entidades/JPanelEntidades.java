/**
 * 
 */
package com.geopista.app.administrador.entidades;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;

/**
 * @author seilagamo
 * 
 */
public class JPanelEntidades extends JPanel {

    Logger logger = Logger.getLogger(JPanelEntidades.class);
    private static final String IDENTIDAD_CODE = "id_entidad";

    private Collection listaEntidades;
    private boolean modoEdicion = false;
    private JFrame framePadre;
    private Map entidadesCheckBoxes;
    private int contadorEntidades = 0;
    ResourceBundle messages;
    private Map relacionesEntidadesMunicpios = null;
    
    private JCheckBox entidadSelected = null;

    public JPanelEntidades(ResourceBundle messages, JFrame framePadre, Collection listaEntidades,
            boolean modoEdicion) {
        this.framePadre = framePadre;
        this.listaEntidades = listaEntidades;
        this.modoEdicion = modoEdicion;
        this.messages = messages;
        initialize();
    }

    private void initialize() {        
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.setBackground(new java.awt.Color(255, 255, 255));
    }

    public void pintarEntidades() {
        try {
            this.removeAll();
            if (listaEntidades == null) {
                this.repaint();
                this.invalidate();
                this.validate();
            }
            entidadesCheckBoxes = new HashMap();
            relacionesEntidadesMunicpios = new HashMap();
            contadorEntidades = 0;
            Iterator it = listaEntidades.iterator();
            while (it.hasNext()) {
                Entidad entidad = (Entidad) it.next();
                final JCheckBox jCheckBoxEntidad = new JCheckBox();
                jCheckBoxEntidad.setBackground(new java.awt.Color(255, 255, 255));
                jCheckBoxEntidad.setEnabled(modoEdicion);
                jCheckBoxEntidad.putClientProperty(IDENTIDAD_CODE, entidad.getId());
                jCheckBoxEntidad.setText(entidad.getId() + " " + entidad.getNombre() + " - " + entidad.getSrid());                
                jCheckBoxEntidad.setToolTipText(entidad.getId() + " " + entidad.getNombre() + " - " + entidad.getSrid());
                Set municipios;
                if (entidad.getMunicipios()==null)
                	municipios = new OperacionesAdministrador(Constantes.url).getMunicipiosEntidad(entidad.getId());
                else
                	municipios=entidad.getMunicipios();
                Iterator it2 = municipios.iterator();
                Set municipiosString = new HashSet();
                while (it2.hasNext()) {
                    municipiosString.add(((Municipio)it2.next()).getId());
                }
                
                relacionesEntidadesMunicpios.put(entidad.getId(), municipiosString);
                entidadesCheckBoxes.put(entidad.getId(), jCheckBoxEntidad);
                
                this.add(jCheckBoxEntidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(5,
                        20 + (contadorEntidades * 20), -1, -1));
                contadorEntidades++;                
            }
            this.repaint();
            this.invalidate();
            this.validate();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println("ERROR: " + sw.toString());
            logger.error("Exception: " + sw.toString());
        }
    }

    public Map getEntidadesCheckBoxes() {
        return entidadesCheckBoxes;
    }
    
    /**
     * @return the relacionesEntidadesMunicpios
     */
    public Map getRelacionesEntidadesMunicpios() {
        return relacionesEntidadesMunicpios;
    }    
    
    /**
     * @param relacionesEntidadesMunicpios the relacionesEntidadesMunicpios to set
     */
    public void setRelacionesEntidadesMunicpios(Map relacionesEntidadesMunicpios) {
        this.relacionesEntidadesMunicpios = relacionesEntidadesMunicpios;
    }

    /**
     * @return the entidadSelected
     */
    public JCheckBox getEntidadSelected() {
        return entidadSelected;
    }

    
    /**
     * @param entidadSelected the entidadSelected to set
     */
    public void setEntidadSelected(JCheckBox entidadSelected) {
        this.entidadSelected = entidadSelected;
    }

    public void unCheckOthers(JCheckBox entidadCheckBox) {
        Iterator it = entidadesCheckBoxes.keySet().iterator();
        while (it.hasNext()) {
            JCheckBox entidad = (JCheckBox) entidadesCheckBoxes.get((String) it.next());
            if (!entidad.getText().equals(entidadCheckBox.getText())) {
                entidad.setSelected(false);
            }
        }
    }

    public void editable(boolean b) {     
    }

    public Entidad aniadirEntidad(String nombreEntidad, String srid, boolean isBackup, int aviso, int periodicidad, int intentos, String entidadExt) {
        CResultadoOperacion result = null;
        Entidad entidad = null;
        String sMensaje = "";
        if (nombreEntidad != null && !nombreEntidad.equals("")) {
            try {
                entidad = new Entidad();
                entidad.setNombre(nombreEntidad);
                entidad.setSrid(srid);
                entidad.setBackup(isBackup);
                entidad.setAviso(aviso);
                entidad.setPeriodicidad(periodicidad);
                entidad.setIntentos(intentos);
                entidad.setEntidadExt(entidadExt);
                result = new OperacionesAdministrador(Constantes.url).nuevaEntidad(entidad);
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("Exception al grabar en base de datos una entidad: " + sw.toString());
                result = new CResultadoOperacion(false, e.getMessage());
            }
            if (result.getResultado()) {
                final JCheckBox nuevaEntidadCheckBox = new JCheckBox();
                nuevaEntidadCheckBox.setBackground(new java.awt.Color(255, 255, 255));
                nuevaEntidadCheckBox.setEnabled(modoEdicion);
                nuevaEntidadCheckBox.putClientProperty(IDENTIDAD_CODE, entidad.getId());
                nuevaEntidadCheckBox.setText(entidad.getId() + " " + entidad.getNombre() + " - " + entidad.getSrid());                
                nuevaEntidadCheckBox.setToolTipText(entidad.getId() + " " + entidad.getNombre() + " - " + entidad.getSrid());
                entidadesCheckBoxes.put(entidad.getId(), nuevaEntidadCheckBox);
                listaEntidades.add(entidad);
                relacionesEntidadesMunicpios.put(entidad.getId(), new HashSet());
                nuevaEntidadCheckBox.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent evt) {
                        if (nuevaEntidadCheckBox.isSelected()) {
                            entidadSelected = nuevaEntidadCheckBox;
                        }
                    }
                });
                
                this.add(nuevaEntidadCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(5,
                        20 + (contadorEntidades * 20), -1, -1));
                contadorEntidades++;
                this.repaint();
                this.invalidate();
                this.validate();
            } else {
                sMensaje = messages.getString("JPanelEntidades.mensaje.errorNewEntidad");
                JOptionPane optionPane = new JOptionPane(sMensaje, JOptionPane.ERROR_MESSAGE);
                JDialog dialog = optionPane.createDialog(this, "");
                dialog.setVisible(true);
            }
        }
        return entidad;
    }
    
    
    private void eliminaEntidadListaEntidades(String codigoEntidad) {
        Iterator it = listaEntidades.iterator();
        Entidad entidadEliminar = null;
        while (it.hasNext()){
            Entidad entidad = (Entidad)it.next();
            if (entidad.getId().equals(codigoEntidad)){
                entidadEliminar = entidad;
            }            
        }
        listaEntidades.remove(entidadEliminar);
    }
    
    private void modificarEntidadListaEntidades(String codigoEntidad, String nombreEntidad, String srid, String entidadExt) {
        Iterator it = listaEntidades.iterator();
        while (it.hasNext()){
            Entidad entidad = (Entidad)it.next();
            if (entidad.getId().equals(codigoEntidad)){
                entidad.setNombre(nombreEntidad);
                entidad.setSrid(srid);
                entidad.setEntidadExt(entidadExt);
            }            
        }
    }
    
    
    public CResultadoOperacion eliminarEntidad(String codigoEntidad) {
        CResultadoOperacion result = null;
        Entidad entidad = null;
        String sMensaje = "";
        if (codigoEntidad != null && !codigoEntidad.equals("")) {
            try {
                entidad = new Entidad();
                entidad.setId(codigoEntidad);
                result = new OperacionesAdministrador(Constantes.url).eliminarEntidad(entidad);
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("Exception al eliminar de base de datos una entidad: " + sw.toString());
                result = new CResultadoOperacion(false, e.getMessage());
            }
            if (result.getResultado()) {
                final JCheckBox entidadEliminadaCheckBox = (JCheckBox)entidadesCheckBoxes.get(codigoEntidad);
                if (entidadEliminadaCheckBox != null) {
                    this.remove(entidadEliminadaCheckBox);
                    entidadesCheckBoxes.remove(codigoEntidad);
                    eliminaEntidadListaEntidades(codigoEntidad);
                    recolocarLista();
                }
            } else {
                sMensaje = messages.getString("JPanelEntidades.mensaje.errorDeleteEntidad");
                JOptionPane optionPane = new JOptionPane(sMensaje, JOptionPane.ERROR_MESSAGE);
                JDialog dialog = optionPane.createDialog(this, "");
                dialog.setVisible(true);            
            }
        }    
        return result;
    }
    
    public void modificarEntidad(String codigoEntidad, String nombreEntidad, String srid, boolean isBackup, int aviso, int periodicidad, int intentos, String entidadExt) {
        CResultadoOperacion result = null;
        Entidad entidad = null;
        String sMensaje = "";
        if (codigoEntidad != null && !codigoEntidad.equals("")) {
            try {
                entidad = new Entidad();
                entidad.setId(codigoEntidad);
                entidad.setNombre(nombreEntidad);
                entidad.setSrid(srid);
                entidad.setBackup(isBackup);
                entidad.setAviso(aviso);
                entidad.setPeriodicidad(periodicidad);
                entidad.setIntentos(intentos);
                entidad.setEntidadExt(entidadExt);
                result = new OperacionesAdministrador(Constantes.url).modificarEntidad(entidad);
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("Exception al modificar en base de datos una entidad: "
                        + sw.toString());
                result = new CResultadoOperacion(false, e.getMessage());
            }
            if (result.getResultado()) {
                final JCheckBox entidadEliminadaCheckBox = (JCheckBox) entidadesCheckBoxes
                        .get(codigoEntidad);
                if (entidadEliminadaCheckBox != null) {
                    entidadEliminadaCheckBox.setText(entidad.getId() + " " + entidad.getNombre() + " - " + entidad.getSrid());
                    entidadEliminadaCheckBox.setToolTipText(entidad.getId() + " "
                            + entidad.getNombre() + " - " + entidad.getSrid());
                    modificarEntidadListaEntidades(entidad.getId(), entidad.getNombre(), entidad.getSrid(), entidad.getEntidadExt());
                    this.repaint();
                    this.invalidate();
                    this.validate();
                }
            } else {
                sMensaje = messages.getString("JPanelEntidades.mensaje.errorModificarEntidad");
                JOptionPane optionPane = new JOptionPane(sMensaje, JOptionPane.ERROR_MESSAGE);
                JDialog dialog = optionPane.createDialog(this, "");
                dialog.setVisible(true);
            }
        }
    }
    
    private void recolocarLista() {
        try {
            this.removeAll();
            if (listaEntidades == null) {
                this.repaint();
                this.invalidate();
                this.validate();
            }
            entidadesCheckBoxes = new HashMap();
            contadorEntidades = 0;
            Iterator it = listaEntidades.iterator();
            while (it.hasNext()) {
                Entidad entidad = (Entidad) it.next();
                final JCheckBox jCheckBoxEntidad = new JCheckBox();
                jCheckBoxEntidad.setBackground(new java.awt.Color(255, 255, 255));
                jCheckBoxEntidad.setEnabled(modoEdicion);
                jCheckBoxEntidad.putClientProperty(IDENTIDAD_CODE, entidad.getId());
                jCheckBoxEntidad.setText(entidad.getId() + " " + entidad.getNombre() + " - " + entidad.getSrid());                
                jCheckBoxEntidad.setToolTipText(entidad.getId() + " " + entidad.getNombre() + " - " + entidad.getSrid());
                entidadesCheckBoxes.put(entidad.getId(), jCheckBoxEntidad);          
                this.add(jCheckBoxEntidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 20+(contadorEntidades*20), -1, -1));
                contadorEntidades++;                
            }
            this.repaint();
            this.invalidate();
            this.validate();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.out.println("ERROR: " + sw.toString());
            logger.error("Exception: " + sw.toString());
        }
        
    }
}
