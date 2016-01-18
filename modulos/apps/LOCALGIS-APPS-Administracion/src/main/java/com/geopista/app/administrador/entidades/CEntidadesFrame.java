/**
 * CEntidadesFrame.java
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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.administrador.EntidadMunicipio;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;

/**
 * @author seilagamo
 * 
 */
public class CEntidadesFrame extends JInternalFrame {

    private Logger logger = Logger.getLogger(CEntidadesFrame.class);

    public final static int INTENTOS_MINIMOS = 5;
    
    private boolean modoEdicion = true;
    private ResourceBundle messages;
    private JFrame framePadre;

    
    private JLabel entidadesJLabel;
    private JScrollPane jScrollPanelEntidades;
    private JPanelEntidades jPanelEntidades;
    
    private JPanelNuevaEntidad jPanelNuevaEntidad;
    private JPanelEliminarEntidad jPanelEliminarEntidad;
    private JPanelModificarEntidad jPanelModificarEntidad;
    
    private JSeparator separador;
    
    private JLabel municipiosJLabel;
    private JScrollPane jScrollPanelMunicipios;
    private JPanelMunicipios jPanelMunicipios;
    
//    private JButton aniadirMunicipioJButton;     
        
    private JPanelNuevoMunicipio jPanelNuevoMunicipio;
    
    private JPanel botoneraJPanel;
    private JButton guardarJButton;
    private JButton salirJButton;
    
    private JButton marcarTodosMunicipiosJButton;
    private JButton desmarcarTodosMunicipiosJButton;
    
    
    private Collection listaEntidades;
    private Collection listaMunicipios;
    private Collection listaMunicipiosString;
    private Collection municipiosSeleccionados = null;
    private JScrollPane mainJScrollPane;
    private JPanel mainJPanel;
    private final int TIEMPO_AVISO = 15;
    private final int TIEMPO_PERIODICIDAD = 365;

    public CEntidadesFrame(ResourceBundle messages, JFrame framePadre) {
        this.framePadre = framePadre;
        this.messages = messages;
        initListas();
        initComponentes();
    }

    private void initListas(){
    	listaEntidades = new OperacionesAdministrador(Constantes.url).getEntidadesSortedByIdNotAssigned();
    	if (listaEntidades==null)    	
        listaEntidades = new OperacionesAdministrador(Constantes.url).getEntidadesSortedById();
        listaMunicipios = new OperacionesAdministrador(Constantes.url).getMunicipiosAsociadosEntidades();
        Iterator it = listaMunicipios.iterator();
        listaMunicipiosString = new HashSet();
        while (it.hasNext()) {
            listaMunicipiosString.add(((Municipio)it.next()).getId());
        }                
    }
    
    /**
     * Inicializa los componentes de la pantalla de entidades.
     */
    private void initComponentes() {
    	mainJScrollPane = new javax.swing.JScrollPane();
        mainJPanel = new javax.swing.JPanel();
       
        mainJPanel.setLayout(new java.awt.BorderLayout());
        mainJScrollPane.setViewportView(mainJPanel);
        getContentPane().add(mainJScrollPane, java.awt.BorderLayout.CENTER);
      
        JPanel operacionesJPanel= new JPanel();
        operacionesJPanel.setLayout(new java.awt.BorderLayout());
       //Panel de nuevas entidades
        operacionesJPanel.add(getJPanelNuevaEntidad(messages), java.awt.BorderLayout.NORTH);
        operacionesJPanel.add(getJPanelEliminarEntidad(), java.awt.BorderLayout.CENTER);
        operacionesJPanel.add(getJPanelModificarEntidad(),java.awt.BorderLayout.SOUTH);
        
        mainJPanel.add(operacionesJPanel, java.awt.BorderLayout.WEST);
        
        //Listado de entidades
        JPanel entidadesJPanel= new JPanel();
        entidadesJPanel.setLayout(new java.awt.BorderLayout());
        entidadesJLabel = new JLabel();
        entidadesJPanel.add(entidadesJLabel, java.awt.BorderLayout.NORTH);
        //Se obtienen las entidades        
        jPanelEntidades = new JPanelEntidades(messages, framePadre, listaEntidades, modoEdicion);
        jPanelEntidades.pintarEntidades();             
        Iterator it=null;
        Map entidadesCheckBoxes = jPanelEntidades.getEntidadesCheckBoxes();
        if (entidadesCheckBoxes!=null){
	         it = entidadesCheckBoxes.keySet().iterator();
	        while (it.hasNext()){
	            final JCheckBox entidad = (JCheckBox) entidadesCheckBoxes.get((String)it.next());
	            entidad.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent evt) {
	                    refrescaListaMunicipios(entidad);
	                }
	            });
	        }
        }
        jScrollPanelEntidades = new JScrollPane();
        jScrollPanelEntidades.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPanelEntidades.setPreferredSize(new Dimension (200, 300));
        jScrollPanelEntidades.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPanelEntidades.setViewportView(jPanelEntidades);
        jScrollPanelEntidades.getViewport().setAlignmentY(JViewport.TOP_ALIGNMENT);
 
        entidadesJPanel.add(jScrollPanelEntidades, java.awt.BorderLayout.CENTER);
        
        JPanel entiMuniJPanel = new JPanel();
        entiMuniJPanel.setLayout(new java.awt.BorderLayout());
        entiMuniJPanel.add(entidadesJPanel, java.awt.BorderLayout.WEST);
      
        separador = new JSeparator(SwingConstants.VERTICAL);
        separador.setPreferredSize(new Dimension(10, 600));
        JPanel separadorJPanel = new JPanel();
        separadorJPanel.setLayout(new FlowLayout());
        separadorJPanel.add(separador);
        entiMuniJPanel.add(separadorJPanel, java.awt.BorderLayout.CENTER);
        
        //Listado de municipios
        JPanel muniJPanel = new JPanel();
        muniJPanel.setLayout(new java.awt.BorderLayout());
        
        municipiosJLabel = new JLabel();
        muniJPanel.add(municipiosJLabel, java.awt.BorderLayout.NORTH);
       
        //Se obtienen los municipios
        
        jPanelMunicipios = new JPanelMunicipios(messages, framePadre, listaMunicipios, modoEdicion);
        jPanelMunicipios.pintarMunicipios();   
        Map municipiosCheckBoxes = jPanelMunicipios.getMunicipiosCheckBoxes();
        if (municipiosCheckBoxes!=null){
	        it = municipiosCheckBoxes.keySet().iterator();
	        while (it.hasNext()) {
	            final JCheckBox municipio = (JCheckBox) municipiosCheckBoxes.get((String)it.next());
	            municipio.addActionListener(new ActionListener(){
	               public void actionPerformed (ActionEvent evt) {
	                   grabarRelacionEntidadMunicipio(municipio);
	               }
	            });
	        }
        }
            
        
        jScrollPanelMunicipios = new JScrollPane();
        jScrollPanelMunicipios.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPanelMunicipios.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPanelMunicipios.setPreferredSize(new Dimension (200, 300));
        jScrollPanelMunicipios.setViewportView(jPanelMunicipios);
        
        muniJPanel.add(jScrollPanelMunicipios, java.awt.BorderLayout.CENTER);
        
        entiMuniJPanel.add(muniJPanel, java.awt.BorderLayout.EAST);
        
        mainJPanel.add(entiMuniJPanel, java.awt.BorderLayout.CENTER);
        
        
        JPanel municipioJPanel = new JPanel();
        municipioJPanel.setLayout(new java.awt.BorderLayout());
        municipioJPanel.add(getJPanelNuevoMunicipio(messages), java.awt.BorderLayout.NORTH);
        mainJPanel.add(municipioJPanel, java.awt.BorderLayout.EAST);        
        
        JPanel marcarJPanel= new JPanel();
        //marcarJPanel.setLayout(new FlowLayout());
        //marcarJPanel.setLayout(new BoxLayout(marcarJPanel, BoxLayout.Y_AXIS));
        marcarJPanel.setLayout(new GridBagLayout());
        
        marcarJPanel.add(getMarcarTodosMunicipiosJButton(),new GridBagConstraints(0, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5,
                        5, 5), 0, 0));
                
        marcarJPanel.add(getDesmarcarTodosMunicipiosJButton(),new GridBagConstraints(0, 4, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5,
                        5, 5), 0, 0));           
        
        entiMuniJPanel.add(marcarJPanel, java.awt.BorderLayout.CENTER);
        mainJPanel.add(getBotoneraJPanel(), java.awt.BorderLayout.SOUTH);

        changeScreenLang(messages);
    }
    
    /**
     * Cambia el lenguaje
     * @param messages
     */
    public void changeScreenLang(ResourceBundle messages) {
        this.messages = messages;
        setTitle(messages.getString("CEntidadesFrame.title"));
//        aniadirEntidadJButton.setToolTipText(messages.getString("CEntidadesFrame.jButtonAniadirEntidad"));      
//        eliminarEntidadJButton.setToolTipText(messages.getString("CEntidadesFrame.jButtonEliminarEntidad"));
//        modificarEntidadJButton.setText(messages.getString("CEntidadesFrame.jButtonModificarEntidad"));
//        modificarEntidadJButton.setToolTipText(messages.getString("CEntidadesFrame.jButtonModificarEntidad"));
        entidadesJLabel.setText(messages.getString("CEntidadesFrame.jLabelEntidades"));
        municipiosJLabel.setText(messages.getString("CEntidadesFrame.jLabelMunicipios"));
//        aniadirMunicipioJButton.setToolTipText(messages.getString("CEntidadesFrame.jButtonAniadirMunicipio"));
        marcarTodosMunicipiosJButton.setText(messages.getString("CEntidadesFrame.jButtonMarcarTodosMunicipios"));
        marcarTodosMunicipiosJButton.setToolTipText(messages.getString("CEntidadesFrame.jButtonMarcarTodosMunicipios"));
        desmarcarTodosMunicipiosJButton.setText(messages.getString("CEntidadesFrame.jButtonDesmarcarTodosMunicipios"));
        desmarcarTodosMunicipiosJButton.setToolTipText(messages.getString("CEntidadesFrame.jButtonDesmarcarTodosMunicipios"));
        guardarJButton.setText(messages.getString("CEntidadesFrame.guardarJButton"));
        guardarJButton.setToolTipText(messages.getString("CEntidadesFrame.guardarJButton"));
        salirJButton.setText(messages.getString("CEntidadesFrame.salirJButton"));
        salirJButton.setToolTipText(messages.getString("CEntidadesFrame.salirJButton"));
        if (jPanelNuevaEntidad!=null)
        	((JPanelNuevaEntidad)jPanelNuevaEntidad).changeScreenLang(messages);
        if (jPanelEliminarEntidad!=null)
        	((JPanelEliminarEntidad)jPanelEliminarEntidad).changeScreenLang(messages);
        if (jPanelModificarEntidad!=null)
        	((JPanelModificarEntidad) jPanelModificarEntidad).changeScreenLang(messages);
        ((JPanelNuevoMunicipio)jPanelNuevoMunicipio).changeScreenLang(messages);             
    }
    
    /**
     * Modifica los municipios seleccionados dependiendo de le entidad que se escoja
     * @param jCheckBoxEntidad
     */
    private void refrescaListaMunicipios(JCheckBox jCheckBoxEntidad) {
        boolean entidadSeleccionada = jCheckBoxEntidad.isSelected();
        
        //Primero hay que borrar la anterior selección
        if (listaMunicipiosString != null) {
            Iterator it = listaMunicipiosString.iterator();
            while (it.hasNext()){
                String municipio = (String) it.next();
                jPanelMunicipios.unCheckMunicipio(municipio);
            }            
        }
        if (entidadSeleccionada) {            
            String entidad = jCheckBoxEntidad.getText().split(" ")[0];
            Set municipiosAsociados = (Set)jPanelEntidades.getRelacionesEntidadesMunicpios().get(entidad);
            Iterator it = municipiosAsociados.iterator();
            municipiosSeleccionados = new ArrayList();
            while (it.hasNext()) {
                String municipio = (String) it.next();
                if (listaMunicipiosString.contains(municipio)) {
                    jPanelMunicipios.checkMunicipio(municipio);
                    municipiosSeleccionados.add(municipio);
                } else {
                    jPanelMunicipios.unCheckMunicipio(municipio);                
                }
            }    
            ((JPanelEntidades)jPanelEntidades).unCheckOthers(jCheckBoxEntidad);
            ((JPanelEntidades)jPanelEntidades).setEntidadSelected(jCheckBoxEntidad);
        } else {
            municipiosSeleccionados = null;
            ((JPanelEntidades)jPanelEntidades).setEntidadSelected(null);
        }
    }
    
    
    /**
     * Crea el botón para añadir entidades
     * @return
     */
//    private JButton getAniadirEntidadJButton(){
//        
//        if (aniadirEntidadJButton == null) {
//            aniadirEntidadJButton = new JButton();
//            aniadirEntidadJButton.setText("+");
//            aniadirEntidadJButton.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    ((JPanelNuevaEntidad)getJPanelNuevaEntidad(messages)).editable(true);
//                }                
//            });
//        }
//        return aniadirEntidadJButton;
//    }
    
    /**
     * Crea el botón para eliminar entidades
     * @return
     */
//    private JButton getEliminarEntidadJButton() {
//        
//        if (eliminarEntidadJButton == null) {
//            eliminarEntidadJButton = new JButton();
//            eliminarEntidadJButton.setText("-");
//            eliminarEntidadJButton.addActionListener(new ActionListener(){
//               public void actionPerformed(ActionEvent e) {
//                   ((JPanelEliminarEntidad)getJPanelEliminarEntidad()).editable(true);
//               }
//            });
//        }
//        return eliminarEntidadJButton;
//    }
    
    /**
     * Crea el botón para modificar entidades
     * @return
     */
//    private JButton getModificarEntidadJButton() {
//        if (modificarEntidadJButton == null) {
//            modificarEntidadJButton = new JButton();
//            modificarEntidadJButton.addActionListener(new ActionListener(){
//                public void actionPerformed(ActionEvent e) {
//                    ((JPanelModificarEntidad)getJPanelModificarEntidad()).editable(true);
//                }
//             });
//        }
//        return modificarEntidadJButton;
//    }
    
    /**
     * Crea el panel para añadir nuevas entidades
     * @param messages
     * @return
     */
    private JPanel getJPanelNuevaEntidad(ResourceBundle messages) {
        if (jPanelNuevaEntidad == null){            
            jPanelNuevaEntidad = new JPanelNuevaEntidad(messages, framePadre, modoEdicion);
            jPanelNuevaEntidad.getBotonAniadir().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final Entidad entidad = jPanelEntidades.aniadirEntidad( 
                    		 jPanelNuevaEntidad.getNombreEntidad(),
                    		 jPanelNuevaEntidad.getSridEntidad(), 
                    		 jPanelNuevaEntidad.isBackup(),
                    		 jPanelNuevaEntidad.getAviso(),
                    		 jPanelNuevaEntidad.getPeriodicidad(),
                    		 jPanelNuevaEntidad.getIntentos(), 
                    		 jPanelNuevaEntidad.getEntidadExt());
                    jPanelModificarEntidad.refrescarListaEntidades();
                    jPanelEliminarEntidad.refrescarListaEntidades();
                    final Map entidadesCheckBoxes = jPanelEntidades.getEntidadesCheckBoxes();
                    ((JCheckBox) entidadesCheckBoxes.get(entidad.getId())).addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent evt) {
                            refrescaListaMunicipios((JCheckBox) entidadesCheckBoxes.get(entidad.getId()));
                        }
                    });
                    
                    repintar();
                    jPanelNuevaEntidad.clean();
                }
            });
        }
        return jPanelNuevaEntidad;
    }
    
    /**
     * Crea el panel para eliminar entidades
     * @param messages
     * @return
     */
    private JPanelEliminarEntidad getJPanelEliminarEntidad() {
        if (jPanelEliminarEntidad == null){            
            jPanelEliminarEntidad = new JPanelEliminarEntidad(messages, framePadre, modoEdicion);
             jPanelEliminarEntidad.getBotonEliminar().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String idEntidad = jPanelEliminarEntidad.getCodigoEntidad();
                    if (!idEntidad.equals("")) {
                        eliminarEntidad();
                    }
                }
            });
        }
        return jPanelEliminarEntidad;
    }
    
    private JPanelModificarEntidad getJPanelModificarEntidad() {
        if (jPanelModificarEntidad == null) {
            jPanelModificarEntidad = new JPanelModificarEntidad(messages, framePadre, modoEdicion);
            jPanelModificarEntidad.getBotonModificar().addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    String idEntidad = jPanelModificarEntidad.getCodigoEntidad();
                    if (!idEntidad.equals("")) {
                         jPanelEntidades.modificarEntidad(idEntidad,
                                        jPanelModificarEntidad.getNombreEntidad(), 
                                        jPanelModificarEntidad.getSridEntidad(),
                                        jPanelModificarEntidad.isBackup(),
                                        jPanelModificarEntidad.getAviso(),
                                        jPanelModificarEntidad.getPeriodicidad(),
                                        jPanelModificarEntidad.getIntentos(),
                                        jPanelModificarEntidad.getEntidadExt());
                         jPanelModificarEntidad.refrescarListaEntidades();
                         jPanelEliminarEntidad.refrescarListaEntidades();
                    }
                }
            });
        }        
        return jPanelModificarEntidad;
    }
    
    private void eliminarEntidad() {
        int n = JOptionPane.showOptionDialog(this, messages
                .getString("CEntidadesFrame.mensaje.eliminarEntidad")
                + " " + ((JPanelEliminarEntidad) jPanelEliminarEntidad).getCodigoEntidad() + "?",
                "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (n == JOptionPane.NO_OPTION) {
            return;
        } else {
            CResultadoOperacion result = jPanelEntidades
                    .eliminarEntidad(jPanelEliminarEntidad.getCodigoEntidad());
            if (result.getResultado()) {
                 jPanelEliminarEntidad.refrescarListaEntidades();
                 jPanelModificarEntidad.refrescarListaEntidades();
                 Map entidadesCheckBoxes = jPanelEntidades.getEntidadesCheckBoxes();
                 Iterator it = entidadesCheckBoxes.keySet().iterator();
                 while (it.hasNext()){
                    final JCheckBox entidad = (JCheckBox) entidadesCheckBoxes.get((String)it.next());
                    entidad.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            refrescaListaMunicipios(entidad);
                        }
                    });
                }
            }
            repintar();
        }
    }
    
    
    /**
     * Crea el panel para añadir nuevos municipios
     * @param messages
     * @return
     */
    private JPanelNuevoMunicipio getJPanelNuevoMunicipio(ResourceBundle messages) {
        if (jPanelNuevoMunicipio == null) {
            jPanelNuevoMunicipio = new JPanelNuevoMunicipio(messages, framePadre, modoEdicion);
            jPanelNuevoMunicipio.getBotonAniadir().addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    
                  

                    //if (srid != null) {
                	    final JCheckBox nuevoMunicipioCheckBox = jPanelMunicipios.aniadirMunicipio(jPanelNuevoMunicipio.getIdMunicipio());
                        if (nuevoMunicipioCheckBox != null) {
                        	
                        	//jPanelMunicipios.pintarMunicipios();
                        	
                        	//Pedimos el srid para la nueva entidad que vamos a crear a partir del municipio
                        	String srid=null;
                        	if (jPanelMunicipios.getSridSelected()!=null)
                        		srid=jPanelMunicipios.getSridSelected();
                        	else
                        		srid = getJDialogSrid();
                            if (srid!=null){
                            //Para el municipio recién creado se crea también una entidad                                                            
                            final Entidad nuevaEntidad = jPanelEntidades.aniadirEntidad("Entidad " + jPanelNuevoMunicipio.getIdMunicipio(), 
                            			srid, true, TIEMPO_AVISO, TIEMPO_PERIODICIDAD, INTENTOS_MINIMOS,"");
                            jPanelModificarEntidad.refrescarListaEntidades();
                            jPanelEliminarEntidad.refrescarListaEntidades();
                            final Map entidadesCheckBoxes = jPanelEntidades.getEntidadesCheckBoxes();
                            
                            //Crea relación entre municipio y la entidad recién creada
                            final JCheckBox nuevaEntidadCheckBox = ((JCheckBox) entidadesCheckBoxes.get(nuevaEntidad.getId()));
                            nuevaEntidadCheckBox.setSelected(true);
                            jPanelEntidades.unCheckOthers(nuevaEntidadCheckBox);
                            jPanelEntidades.setEntidadSelected(nuevaEntidadCheckBox);
                            nuevoMunicipioCheckBox.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    grabarRelacionEntidadMunicipio(nuevoMunicipioCheckBox);
                                }
                            });
                            
                            nuevoMunicipioCheckBox.doClick();       
                            ((JCheckBox) entidadesCheckBoxes.get(nuevaEntidad.getId())).addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent evt) {
                                    refrescaListaMunicipios((JCheckBox) entidadesCheckBoxes.get(nuevaEntidad.getId()));
                                }
                            });
                                                                    
                            nuevoMunicipioCheckBox.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    grabarRelacionEntidadMunicipio(nuevoMunicipioCheckBox);
                                }
                            });
                            
                            repintar();
                        }
                    }
                }
            });
        }
        return jPanelNuevoMunicipio;
    }
    
    
    private JButton getMarcarTodosMunicipiosJButton() {
	if (marcarTodosMunicipiosJButton == null) {
	    marcarTodosMunicipiosJButton = new JButton();
	    marcarTodosMunicipiosJButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Map municipiosCheckBoxes = jPanelMunicipios.getMunicipiosCheckBoxes();
                    Iterator it = municipiosCheckBoxes.keySet().iterator();
                    while (it.hasNext()) {
                        final JCheckBox municipio = (JCheckBox) municipiosCheckBoxes.get((String)it.next());
                        if (!municipio.isSelected()) {
                            municipio.doClick();
                        }
                    }
                }
	    });
	}
	return marcarTodosMunicipiosJButton;
    }
    
    
    private JButton getDesmarcarTodosMunicipiosJButton() {
	if (desmarcarTodosMunicipiosJButton == null) {
	    desmarcarTodosMunicipiosJButton = new JButton();
	    desmarcarTodosMunicipiosJButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Map municipiosCheckBoxes = jPanelMunicipios.getMunicipiosCheckBoxes();
                    Iterator it = municipiosCheckBoxes.keySet().iterator();
                    while (it.hasNext()) {
                        final JCheckBox municipio = (JCheckBox) municipiosCheckBoxes.get((String)it.next());
                        if (municipio.isSelected()) {
                            municipio.doClick();
                        }
                    }
                }
	    });
	}
	return desmarcarTodosMunicipiosJButton;
    }
    
    
    
    /**
     * Añade una botonera al final de la pantalla
     * @return
     */
    private JPanel getBotoneraJPanel() {
        if (botoneraJPanel == null) {
            botoneraJPanel = new JPanel();
            botoneraJPanel.setLayout(new FlowLayout());
            guardarJButton = new JButton();
            botoneraJPanel.add(guardarJButton);
            guardarJButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent evt) {
                    guardar();
                }
            });
            
            salirJButton = new JButton();
            salirJButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    salir();
                }
            });
            botoneraJPanel.add(salirJButton);
            
        }
        return botoneraJPanel;
    }

    
    
    private void grabarRelacionEntidadMunicipio(JCheckBox municipio) {
        if (jPanelEntidades.getEntidadSelected() != null) {
            String entidadSeleccionada = jPanelEntidades.getEntidadSelected().getText().split(" ")[0];
            Set municipios = (Set) jPanelEntidades.getRelacionesEntidadesMunicpios().get(entidadSeleccionada);
            if (municipios == null) {
                municipios = new HashSet();
            }
            if (municipio.isSelected()) {
                municipios.add(municipio.getText().split(" ")[0]);
                if (municipiosSeleccionados == null) {
                    municipiosSeleccionados = new ArrayList();
                }
                municipiosSeleccionados.add(municipio.getText().split(" ")[0]);
            } else {
                municipios.remove(municipio.getText().split(" ")[0]);
                if (municipiosSeleccionados != null) {
                    municipiosSeleccionados.remove(municipio.getText().split(" ")[0]);
                }
            }
            Map relaciones = jPanelEntidades.getRelacionesEntidadesMunicpios();
            relaciones.put(entidadSeleccionada, municipios);
            jPanelEntidades.setRelacionesEntidadesMunicpios(relaciones);
        }
    }
    
    public void editable(boolean b) {
        this.modoEdicion = b;
        jPanelEntidades.editable(b);
        jPanelMunicipios.editable(b);        
        
        ((JPanelNuevaEntidad)getJPanelNuevaEntidad(messages)).editable(b);
        ((JPanelEliminarEntidad)getJPanelEliminarEntidad()).editable(b);
        ((JPanelModificarEntidad)getJPanelModificarEntidad()).editable(b);
        
//        getAniadirMunicipioJButton().setEnabled(b);
//        getAniadirEntidadJButton().setEnabled(b);
//        getEliminarEntidadJButton().setEnabled(b);
        guardarJButton.setEnabled(b);            
    }
    
    private void salir() {
        dispose();
    }
    
    private void repintar() {
        this.repaint();
        this.invalidate();
        this.validate();
    }
    
    private void guardar() {
        CResultadoOperacion result = null;
        String sMensaje="";
        Iterator it = listaEntidades.iterator();
        try {
            while (it.hasNext()) { 
                Entidad entidad = (Entidad) it.next();
                // Primera fase: Se miran los municipios a eliminar de la relación
                Set municipios = new OperacionesAdministrador(Constantes.url).getMunicipiosEntidad(entidad.getId());
                Set municipiosString = new HashSet();
                Iterator it2 = municipios.iterator();
                while (it2.hasNext()){
                    municipiosString.add(((Municipio)it2.next()).getId());
                }
            
                Set municipiosActuales = (Set) jPanelEntidades.getRelacionesEntidadesMunicpios().get(entidad.getId());
                it2 = municipiosString.iterator();
                while (it2.hasNext()){
                    String municipio = (String) it2.next();
                    if (!municipiosActuales.contains(municipio)){
                        EntidadMunicipio entidadMunicipio = new EntidadMunicipio (entidad.getId(), municipio);
                        result = new OperacionesAdministrador(Constantes.url).eliminarRelacionEntidadMunicipio(entidadMunicipio);
                        sMensaje = messages.getString("CEntidadesFrame.mensaje.modificadoExito");                    
                    }
                }
            
                //Segunda fase: Se miran los municipios a insertar en la relación
                it2 = municipiosActuales.iterator();
                while (it2.hasNext()) {
                    String municipio = (String) it2.next();
                    if (!municipiosString.contains(municipio)) {                    
                        EntidadMunicipio entidadMunicipio = new EntidadMunicipio (entidad.getId(), municipio, Integer.parseInt(entidad.getSrid()));
                        result = new OperacionesAdministrador(Constantes.url).nuevaRelacionEntidadMunicipio(entidadMunicipio);
                        sMensaje = messages.getString("CEntidadesFrame.mensaje.modificadoExito");                  
                    }
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception al eliminar de base de datos una relación entidad-municipio: " + sw.toString());
            result = new CResultadoOperacion(false, e.getMessage());
        } 
        if (result == null) {
            sMensaje = messages.getString("CEntidadesFrame.mensaje.modificadoExito");    
            JOptionPane optionPane = new JOptionPane(sMensaje, JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = optionPane.createDialog(this, "");
            dialog.setVisible(true);
        } else if (result.getResultado()) {
            JOptionPane optionPane = new JOptionPane(sMensaje, JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = optionPane.createDialog(this, "");
            dialog.setVisible(true);
        } else {
            JOptionPane optionPane = new JOptionPane(result.getDescripcion(),
                    JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog(this, "ERROR");
            dialog.setVisible(true);
        }
        salir();
    }

    /**
     * Pide al usuario que introduzca un SRID para la nueva entidad que se crea a partir del municipio
     * @return
     */
    private String getJDialogSrid() {
	String srid = null;
	
	do {
	    srid = (String) JOptionPane.showInputDialog(framePadre, messages
	        .getString("JDialgoSrid.mensajeSrid"), messages.getString("JDialogSrid.title"),
	        JOptionPane.PLAIN_MESSAGE, null, null, "");
	} while (srid != null && "".equals(srid));

	return srid;
	
    }
  
}
