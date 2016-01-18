/**
 * JPanelMunicipios.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.app.administrador.entidades;

import java.awt.Color;
import java.awt.Component;
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

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.protocol.CResultadoOperacion;
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
    
    private Municipio municipioAlta;
    private String sridSelected;
    
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
    
    
    //DEPRECATED
    public JCheckBox aniadirMunicipio(String id_municipio) {
        String sMensaje = "";
        JCheckBox nuevoMunicipioCheckBox = null;

        try {
        	
            Municipio municipio = new OperacionesAdministrador(Constantes.url).getMunicipio(id_municipio);
            if (municipio == null) {          
                sMensaje = messages.getString("JPanelMunicipios.mensajes.errorNoExisteMunicipio")+" ¿Desea crear el municipio?";
                
                int result = JOptionPane.showConfirmDialog((Component) null, sMensaje,
                        "Municipio", JOptionPane.OK_CANCEL_OPTION);
                
                if (result==0){
                	municipio=crearNuevoMunicipio(id_municipio);
                	sridSelected=municipio.getSrid();
                }
                
  
            }
            
            if (municipio != null) {  
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
    
    
    public Municipio aniadirMunicipio(String idMunicipio, String nombreMunicipio,String sridMunicipio) {
        CResultadoOperacion result = null;
        Municipio municipio= null;
        String sMensaje = "";
        if (nombreMunicipio != null && !nombreMunicipio.equals("")) {
            try {
            	municipio = new Municipio();
            	municipio.setNombre(nombreMunicipio);
            	municipio.setId(idMunicipio);
            	municipio.setSrid(sridMunicipio);
                result = new OperacionesAdministrador(Constantes.url).nuevoMunicipio(municipio);
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("Exception al grabar en base de datos un municipio: " + sw.toString());
                result = new CResultadoOperacion(false, e.getMessage());
            }
          
        }
        else{
        	municipio = new Municipio();
        	municipio.setNombre(nombreMunicipio);
        	municipio.setId(idMunicipio);
        	municipio.setSrid(sridMunicipio);
        }
        return municipio;
    }
    
    private Municipio crearNuevoMunicipio(String id_municipio){
    	final JPanelNuevoMunicipio panelNuevoMunicipio=new JPanelNuevoMunicipio(messages,id_municipio);
    	
    	final JDialog dialog=new JDialog(framePadre);
    	
    	panelNuevoMunicipio.getBotonAniadir().addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e) {
            	 municipioAlta = aniadirMunicipio(panelNuevoMunicipio.getIdMunicipio(),
            			 	panelNuevoMunicipio.getNombreMunicipio(),panelNuevoMunicipio.getSridMunicipio());
            	 //System.out.println("Municipio añadido");
            	 if ((municipioAlta.getNombre()==null)|| (municipioAlta.getNombre().equals(""))){
            		 JOptionPane.showMessageDialog(framePadre, "Debe rellenar el campo nombre del municipio");
            	 }
            	 else if ((municipioAlta.getSrid()==null) || (municipioAlta.getSrid().equals(""))){
            		 JOptionPane.showMessageDialog(framePadre, "Debe rellenar el campo SRID");
            	 }
            	 else
            		 dialog.dispose();
             }             
    	});
    	
    	dialog.setTitle("Alta Municipios");
    	dialog.setSize(400,200);
    	dialog.setLocation(200,200);
    	dialog.setModal(true);
    	dialog.add(panelNuevoMunicipio);
    	dialog.setVisible(true);
    	
    	/*if (municipioAlta!=null)
    		System.out.println("DATOS:"+municipioAlta.getId()+municipioAlta.getNombre());*/

    	return municipioAlta;
    }
    
    public void checkMunicipio (String municipio) {        
        ((JCheckBox) municipiosCheckBoxes.get(municipio)).setSelected(true);                
    }
    
    public void unCheckMunicipio (String municipio) {
        ((JCheckBox) municipiosCheckBoxes.get(municipio)).setSelected(false);
    }
    
    public void editable(boolean b) {
    	if (municipiosCheckBoxes!=null){
	        Iterator it = municipiosCheckBoxes.keySet().iterator();
	        while (it.hasNext()) {
	            ((JCheckBox) municipiosCheckBoxes.get(it.next())).setEnabled(b);
	        }
    	}
    }
        
    /**
     * @return the municipiosCheckBoxes
     */
    public Map getMunicipiosCheckBoxes() {
        return municipiosCheckBoxes;
    }

	public String getSridSelected() {
		return sridSelected;
	}


}
