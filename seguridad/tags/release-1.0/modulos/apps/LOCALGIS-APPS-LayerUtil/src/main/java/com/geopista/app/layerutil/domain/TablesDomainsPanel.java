/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.layerutil.domain;


/**
 * Panel que permite realizar operaciones de manipulación sobre las tablas
 * de sistema y los dominios asociados a las columnas
 * 
 * @author cotesa
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;


import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.dominios.JPanelDominios;
import com.geopista.app.layerutil.GestorCapas;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.layer.LayerOperations;
import com.geopista.app.layerutil.schema.table.JPanelTables;
import com.geopista.feature.Column;
import com.geopista.feature.Domain;
import com.geopista.feature.Table;
import com.geopista.global.WebAppConstants;

import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;


public class TablesDomainsPanel extends JPanel implements FeatureExtendedPanel, TreeSelectionListener{
    
    private JPanelTables jPanelTablas;
    private JPanelDominios jPanelDominios;
    private JPanel jPanelLevel = new JPanel();
    
    private JLabel lblLevel = new JLabel();
    private JTextField txtLevel = new JTextField();
    
    private JButton jButtonSalir = new JButton();;
    private JButton jButtonAsociar = new JButton();
    private JButton jButtonDesasociar = new JButton();
    private JPanel templateJPanel = new JPanel();
    private JScrollPane templateJScrollPane = new JScrollPane();
    
    private ResourceBundle messages;
    
    private JTree treeDominios = new JTree();
    private JTree treeDominiosParticular = new JTree();
    private JTree treeTablas = new JTree();
    
    boolean nodoParticular = false;
    boolean nodoGeneral = false;
    boolean nodoTablas = false;
    
    private com.geopista.protocol.administrador.dominios.Domain auxDomain = new com.geopista.protocol.administrador.dominios.Domain();
    private Column auxColumn = new Column();
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    boolean levelActivado = false;
    int idMunicipio=0;
    
    /**
     * Constructor de la clase
     */
    public TablesDomainsPanel()
    {
        //Operaciones para añadir el panel de tablas
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());            
            initComponents();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't use system look and feel.");
        }
    }
    
    /**
     * Inicialización de los componentes
     *
     */
    private void initComponents()
    {
        
        this.setBounds(new Rectangle(10, 10, 1000, 830));
        this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        this.setLayout(null);        
        
        //Operaciones para añadir el panel de dominios        
        try{
            
            //aplicacion.login();            
            com.geopista.app.administrador.init.Constantes.url= aplicacion.getString("geopista.conexion.servidorurl")+ WebAppConstants.ADMINISTRACION_WEBAPP_NAME;
//            com.geopista.app.administrador.init.Constantes.idMunicipio=Integer.parseInt(AppContext.getApplicationContext().getString("geopista.DefaultCityId"));
            com.geopista.app.administrador.init.Constantes.idEntidad=aplicacion.getIdEntidad();
            //com.geopista.security.SecurityManager.setHeartBeatTime(100000);
            com.geopista.security.SecurityManager.setHeartBeatTime(10000);
            com.geopista.security.SecurityManager.setsUrl(com.geopista.app.administrador.init.Constantes.url);
            com.geopista.app.administrador.init.Constantes.Locale = aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, GestorCapas.DEFAULT_LOCALE, false);
            
            /*Municipio municipio =
                (new OperacionesAdministrador(com.geopista.app.administrador.init.Constantes.url))
                .getMunicipio(new Integer(com.geopista.app.administrador.init.Constantes.idMunicipio).toString());
            if (municipio!=null)
            {
                com.geopista.app.administrador.init.Constantes.Municipio = municipio.getNombre();
                com.geopista.app.administrador.init.Constantes.Provincia= municipio.getProvincia();
            }*/
			Entidad entidad = 
            (new OperacionesAdministrador(com.geopista.app.administrador.init.Constantes.url))
            .getEntidad(new Integer(com.geopista.app.administrador.init.Constantes.idEntidad).toString());
			if (entidad!=null)
			{
				com.geopista.app.administrador.init.Constantes.Entidad = entidad.getNombre();
				//com.geopista.app.administrador.init.Constantes.Provincia= municipio.getProvincia();
			}
            
            try
            {
                messages = ResourceBundle.getBundle("config.administrador", new Locale(aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, GestorCapas.DEFAULT_LOCALE, false)));
            }catch (Exception e)
            {
                messages = ResourceBundle.getBundle("config.administrador", new Locale(GestorCapas.DEFAULT_LOCALE));
            }            
            
            jPanelDominios = new JPanelDominios(messages,aplicacion.getMainFrame());                             
            jPanelDominios.hideLeyenda();
            
        }
        catch(Exception e){
            
            e.printStackTrace();
        }
        
        
        templateJScrollPane = new JScrollPane();
        templateJPanel = new JPanel();       
        templateJPanel.setLayout(new BorderLayout());
        
        //jButtonSalir.setBounds(new Rectangle(350, 555, 100, 25));
        jButtonSalir.setBounds(new Rectangle(875,555,100,25));
        
        jButtonSalir.setMaximumSize(new java.awt.Dimension(100, 25));
        jButtonSalir.setPreferredSize(new java.awt.Dimension(75, 25));
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed();
            }
        });
        
        //aplicacion.loadI18NResource("GestorCapasi18n");
        jButtonSalir.setText(I18N.get("GestorCapas","general.boton.salir"));                        
        
        
        templateJScrollPane.setViewportView(templateJPanel);       
        
        jPanelDominios.setBounds(new Rectangle(260, 5, 740, 550));      
        
        jPanelLevel.setBounds(new java.awt.Rectangle(15,490,200,25));
        lblLevel.setText(I18N.get("GestorCapas","tablas.level.dominio"));
        txtLevel.setText("0 "); 
        txtLevel.setBounds(new Rectangle(50, 5, 40, 15));
        txtLevel.setEnabled(false);
        lblLevel.setBackground(Color.GRAY);
        jPanelLevel.add(lblLevel, null);
        jPanelLevel.add(txtLevel, null);
        
        jButtonAsociar.setBounds(new java.awt.Rectangle(35,525,180,25));
        jButtonAsociar.setEnabled(false);        
        jButtonAsociar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAsociarActionPerformed();
            }
        });
        jButtonAsociar.setText(I18N.get("GestorCapas","tablas.boton.asociar"));
        
        jButtonDesasociar.setBounds(new java.awt.Rectangle(35,555,180,25));
        jButtonDesasociar.setEnabled(false);        
        jButtonDesasociar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDesasociarActionPerformed();
            }
        });
        jButtonDesasociar.setText(I18N.get("GestorCapas","tablas.boton.desasociar"));
        
        jPanelTablas = new JPanelTables(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jPanelTablas.setBorder(BorderFactory.createTitledBorder(I18N.get("GestorCapas","tablas.arbol.titulo")));
        jPanelTablas.setBounds(new java.awt.Rectangle(5,5,250,481));
        
        jPanelDominios.changeScreenLang(messages);
        
        treeDominios = jPanelDominios.getJTreeArbol();
        treeDominios.addTreeSelectionListener(this);
        
        treeDominiosParticular = jPanelDominios.getJTreeArbolParticular();
        treeDominiosParticular.addTreeSelectionListener(this);
        
        treeTablas = jPanelTablas.getTree();
        treeTablas.addTreeSelectionListener(this);
        
        this.add(jPanelDominios, null);
        this.add(jButtonSalir, null);
        this.add(jPanelTablas, null);
        this.add(jButtonAsociar, null);
        this.add(jButtonDesasociar, null);
        this.add(jPanelLevel, null);
        
    }	
    
    /**
     * Acción realizada al pulsar el botón de salir de la aplicación
     *
     */
    private void jButtonSalirActionPerformed()
    {
        String string1 = I18N.get("GestorCapas","general.si"); 
        String string2 = I18N.get("GestorCapas","general.no"); 
        Object[] options = {string1, string2};
        
        int n = JOptionPane.showOptionDialog(this,
                I18N.get("GestorCapas","general.salir.mensaje"),
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if (n==JOptionPane.NO_OPTION) return;
        
        aplicacion.getMainFrame().dispose();
        System.exit(0);
        
    }
    
    /**
     * Acción realizada al pulsar el botón de asociar de la aplicación
     *
     */
    private void jButtonAsociarActionPerformed()
    {	
        try{
            //Convierte com.geopista.protocol.administrador.dominios.Domain 
            //en com.geopista.features.Domain
            
            int idDomain = Integer.parseInt(auxDomain.getIdDomain());
            String nombreDominio = auxDomain.getName();
            LayerOperations operaciones = new LayerOperations();
            Domain dominio= operaciones.obtenerDominioTipo(idDomain, nombreDominio);
            
            // asocia el dominio a la columna
            int iRes = operaciones.actualizarDominioColumna(auxColumn, dominio, Integer.parseInt(txtLevel.getText()));
            //auxColumn.setDomain(dominio);
            
            if (iRes>0)
            {
                JOptionPane optionPane= 
                    new JOptionPane(I18N.get("GestorCapas","general.mensaje.fin.operacion"),
                            JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                
                Identificadores.put("DomainsModificados", true);  
                jButtonDesasociar.setEnabled(true);                
            }
            else
            {
                JOptionPane optionPane= 
                    new JOptionPane(I18N.get("GestorCapas","general.mensaje.error.operacion"),
                            JOptionPane.ERROR_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
            }
            
            repaint();            
            
        }catch(DataException de)
        {
            de.printStackTrace();
        }
    }
    
    /**
     * Acción realizada al pulsar el botón desasociar de la aplicación
     *
     */
    private void jButtonDesasociarActionPerformed()
    {    
        try{
            LayerOperations operaciones = new LayerOperations();
            
            // desasocia el dominio de la columna
            int iDeleted = operaciones.desasociarColumna(auxColumn);
            
            if (iDeleted>0)
            {
                JOptionPane optionPane= 
                    new JOptionPane(I18N.get("GestorCapas","general.mensaje.fin.operacion"),
                            JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                Identificadores.put("DomainsModificados", true);
                jButtonAsociar.setEnabled(true);
                jButtonDesasociar.setEnabled(false);
            }
            else
            {
                JOptionPane optionPane= 
                    new JOptionPane(I18N.get("GestorCapas","general.mensaje.error.operacion"),
                            JOptionPane.ERROR_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
            };
            
            repaint();
            
        }catch(DataException de)
        {
            de.printStackTrace();
        }
    }
    
    /**
     * Escucha los posibles cambios producidos en la selección de elementos del árbol
     * de tables y columns de GeoPISTA
     */
    public void valueChanged (TreeSelectionEvent e){
        
        if (e==null || !(e.getSource() instanceof JTree)) return;
        JTree arbol= (JTree)e.getSource();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)arbol.getLastSelectedPathComponent();
        if (node == null) return;
        Object nodeInfo = node.getUserObject();
                
        if (arbol.getName().indexOf("Particular")>=0)
        {
            levelActivado=false;
            idMunicipio = Integer.parseInt(AppContext.getApplicationContext().getString("geopista.DefaultCityId"));
            
            if (nodeInfo instanceof com.geopista.protocol.administrador.dominios.Domain)
            {
                nodoParticular = true;
            }
            else
            {
                nodoParticular = false;
            }
            nodoGeneral = false;
        } 
        
        else if (arbol.getName().indexOf("General")>=0)
        {
            levelActivado=false;
            idMunicipio = 0;
            
            if (nodeInfo instanceof com.geopista.protocol.administrador.dominios.Domain)
            {
                nodoGeneral = true;
            }
            else{
                nodoGeneral = false;
            }
            nodoParticular = false;
        } 
        else if (arbol.getName().indexOf("Tablas")>=0)
        {
            //comprobar q es una columna lo seleccionado
            if(nodeInfo instanceof Column)
            {
                nodoTablas = true;                     
            }
            else
            {
                nodoTablas = false;  
            }
        }
        
        if (nodeInfo instanceof Column)
        {
            jButtonDesasociar.setEnabled(true);
            
            auxColumn= (Column)nodeInfo;
            
            if (auxColumn.getDomain()==null)
                jButtonDesasociar.setEnabled(false);
            else
                jButtonDesasociar.setEnabled(true);
            
            if (nodoParticular || nodoGeneral){
                //System.out.println ("tengo dominio y detecto columna");
                jButtonAsociar.setEnabled(true);
                return;
            }
        }
        
        if (nodeInfo instanceof Table)
        {
            jButtonAsociar.setEnabled(false);
            jButtonDesasociar.setEnabled(false);
        }
        
        if (nodeInfo instanceof com.geopista.protocol.administrador.dominios.Domain)
        {
            levelActivado = false;
            auxDomain = (com.geopista.protocol.administrador.dominios.Domain)nodeInfo;
            if (nodoTablas){
                //System.out.println ("tengo columna y detecto dominio");
                jButtonAsociar.setEnabled(true);
            }
            
            LayerOperations operaciones = new LayerOperations();
            
            try
            {
                if (operaciones.obtenerTipoDominio(auxDomain, idMunicipio)==Domain.TREE)
                    levelActivado =true;
            } catch (DataException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
            if ( arbol.getName().indexOf("General")>=0 
                    && auxDomain.getListaNodes().getFirst()!=null 
                    &&auxDomain.getListaNodes().getFirst().getType() == Domain.TREE){
                levelActivado = true;
                
            }
            else if (arbol.getName().indexOf("Particular")>=0){
                levelActivado=true;
            }
        }
        else
        {
            jButtonAsociar.setEnabled(false);
        }
                
        if (levelActivado){
            //Se activa la caja de edicion de level
            txtLevel.setEnabled(true);
            lblLevel.setBackground(Color.BLACK);
            txtLevel.setText("0");
        }
        else
        {
            txtLevel.setEnabled(false);
            txtLevel.setText("0");
            lblLevel.setBackground(Color.GRAY);             
        }        
    }
    
    /**
     * Acciones realizas al entrar en la pantalla
     */
    public void enter()
    {   
        if (((Boolean)Identificadores.get("TablasModificadas")).booleanValue()
               && !((Boolean)Identificadores.get("TablasDominiosActualizada")).booleanValue())        
        {
            //Cada vez que se entra, se actualiza el arbol de tablas del sistema con
            //los posibles cambios realizados 
            Identificadores.put("Tablas", null);
            Identificadores.put("Columnas", null);
            
            this.remove(jPanelTablas);
            jPanelTablas = new JPanelTables(TreeSelectionModel.SINGLE_TREE_SELECTION);
            jPanelTablas.setBorder(BorderFactory.createTitledBorder(I18N.get("GestorCapas","tablas.arbol.titulo")));
            jPanelTablas.setBounds(new java.awt.Rectangle(5,5,250,481));
            this.add(jPanelTablas);
            treeTablas = jPanelTablas.getTree();
            treeTablas.addTreeSelectionListener(this);
            
            //Si se modifica algo en la pantalla de tablas de base de datos, habrá 
            //que actualizar la información tanto en el panel de tablasdominios como 
            //en el de layers. 
            Identificadores.put("TablasDominiosActualizada", true);
            if(((Boolean)Identificadores.get("LayersActualizada")).booleanValue())
            {
                Identificadores.put("TablasModificadas", false);
                Identificadores.put("TablasDominiosActualizada", false);   
                Identificadores.put("LayersActualizada", false);
            }            
        }        
        
        try
        {
            // Iniciamos la ayuda
        	String helpHS = "help/catastro/gestordecapas/GestorCapasHelp_es.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            // fin de la ayuda
            hb.enableHelpKey(this,"Pestania2Dominios", hs);
        } 
        catch (Exception excp)
        {
            excp.printStackTrace();
        }
    }
    
    /**
     * Acciones realizadas al salir de la pantalla
     */
    public void exit()
    {        
    }    
}
