package com.geopista.app.administrador.usuarios;

import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.protocol.administrador.Acl;
import com.geopista.protocol.administrador.App;
import com.geopista.protocol.administrador.ListaAcl;
import com.geopista.protocol.administrador.ListaApp;
import com.geopista.protocol.administrador.ListaPermisos;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.administrador.Permiso;

public class ShowPermisos extends javax.swing.JDialog  {
	
		private Logger logger = Logger.getLogger(ShowPermisos.class);
        private ResourceBundle messages;
        private SortedMutableTreeNode treeNodePermisos;
        private DefaultTreeModel treeModelPermisos;
        private ListaPermisos listaPermisos;        
        private String sIdUsuario;

        /**
         * Creates new form JSearch
         * @wbp.parser.constructor
         */
        public ShowPermisos(String sIdUsuario, java.awt.Frame parent, boolean modal,
                            ResourceBundle messages) {
            super(parent, modal);
            this.sIdUsuario = sIdUsuario;
            this.messages=messages;
            initComponents();
            changeScreenLang(messages);

        }

        /**
         * Cuando se muestran los permisos de un rol
         */
        public ShowPermisos(ListaPermisos listaPermisos, ListaAcl listaAcl, java.awt.Frame parent, boolean modal,
                            ResourceBundle messages) {
            super(parent, modal);
            this.listaPermisos = listaPermisos;
            this.messages=messages;
            initComponents();
            changeScreenLang(messages);

        }
       
        private void initComponents() {//GEN-BEGIN:initComponents
            jButtonSalir = new JButton();
            jPanelPermisos = new JPanel();
            jPanelBotonera = new JPanel();
            jScrollPaneTablaPermisos = new javax.swing.JScrollPane();
            actualizarModelo();
       	 	jTreePermisos = new DynamicTreePanel(treeModelPermisos);
            jScrollPaneTablaPermisos.setViewportView(jTreePermisos);

            jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    salir();
                }
            });
             jPanelPermisos.setLayout(new java.awt.BorderLayout());
             jPanelPermisos.add(jScrollPaneTablaPermisos,java.awt.BorderLayout.CENTER);
             jPanelBotonera.add(jButtonSalir);

             getContentPane().setLayout(new java.awt.BorderLayout());
             getContentPane().add(jPanelPermisos, java.awt.BorderLayout.CENTER);
             getContentPane().add(jPanelBotonera, java.awt.BorderLayout.SOUTH);



            pack();
        }
       
        private void salir() {
            dispose();
        }

         public void changeScreenLang(ResourceBundle messages) {
             setTitle(messages.getString("CUsuariosFrame.jLabelPermisos"));
             jPanelPermisos.setToolTipText(messages.getString("CUsuariosFrame.jLabelPermisos"));
             jButtonSalir.setText(messages.getString("ShowConexiones.jButtonSalir"));
             jButtonSalir.setToolTipText(messages.getString("ShowConexiones.jButtonSalir"));
             actualizarModelo();
         }

         private void actualizarModelo()
        {      
        	ListaApp listaApp; 
        	SortedMutableTreeNode appNode;
        	SortedMutableTreeNode aclNode;
        	
        	treeNodePermisos = new SortedMutableTreeNode();	 
        	try {
        		if(sIdUsuario != null && listaPermisos == null)
        			listaPermisos = new OperacionesAdministrador(Constantes.url).getPermisosUsuario(sIdUsuario);
				listaApp=new OperacionesAdministrador(Constantes.url).getApps();
				Iterator itApp = listaApp.gethApps().keySet().iterator();
				while(itApp.hasNext()){
					App app = listaApp.get((String) itApp.next());
					appNode = new SortedMutableTreeNode(app.getNombre());
					Iterator itAcl = app.getAcls().gethAcls().keySet().iterator();
					while(itAcl.hasNext()){
						Acl acl = app.getAcls().get((String) itAcl.next());
						aclNode = new SortedMutableTreeNode(acl.getNombre());	
						Iterator itPerm = acl.getPermisos().gethPermisos().keySet().iterator();
						while(itPerm.hasNext()){
							Permiso perm = (Permiso) listaPermisos.get(((Permiso)acl.getPermisos().gethPermisos().get(itPerm.next())).getIdPerm(), acl.getId());
							if(perm != null){
								perm = (Permiso) acl.getPermisos().get(perm.getIdPerm(), acl.getId());
								String permName = perm.getDef();
								if(perm.getHmName() != null)
									permName = perm.getHmName().get(messages.getLocale().getLanguage());
								aclNode.add(new SortedMutableTreeNode(permName));
							}
						}
						if(aclNode.getChildCount()>0)
							appNode.add(aclNode);
					}
					if(appNode.getChildCount()>0)
						treeNodePermisos.add(appNode);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
        	 
        	 treeModelPermisos = new DefaultTreeModel(treeNodePermisos);  
        	 treeModelPermisos.reload();
        }

        private JButton jButtonSalir;
        private JPanel jPanelBotonera;
        private JPanel jPanelPermisos;
        private JScrollPane jScrollPaneTablaPermisos;
        private DynamicTreePanel jTreePermisos;
        
    }

