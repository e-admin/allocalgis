/**
 * CLocalGISAppFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * CAplicacionesFrame.java
 *
 * Created on 16 de junio de 2004, 15:13
 */

package com.geopista.app.centralizadorsso.localgisapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Principal;
import java.security.acl.AclNotFoundException;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.centralizadorsso.beans.LocalGISApp;
import com.geopista.app.centralizadorsso.client.AppsClient;
import com.geopista.app.centralizadorsso.utils.Utils;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.security.sso.protocol.control.SesionBean;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * 
 * @author dcaaveiro
 */
public class CLocalGISAppFrame extends javax.swing.JInternalFrame {

	Logger logger = Logger.getLogger(CLocalGISAppFrame.class);
	LocalGISAppTableModel modelLocalGISApp;
	LocalGISAppTableModel modelLocalGISWeb;
	HashMap<String,LocalGISApp> localGISApps;
	private TableSorted sorter;
	ResourceBundle messages;
	String serverHost;

	public final static String APP_TITLE_MARKER = "               "
			+ (new Character('\u25CF')) + "    ";
	private static final String IMAGE_SSO_TITLE_TOP = "/img/sso_title_top.png";
	private static final String IMAGE_SSO_TITLE_LEFT = "/img/sso_title_left.png";

	/** Creates new form CAplicacionesFrame */
	public CLocalGISAppFrame(ResourceBundle messages) {
		this.messages = messages;
		initComponents();
	}

	private void initComponents() {// GEN-BEGIN:initComponents
		serverHost = AppContext.getApplicationContext().getString(UserPreferenceConstants.TOMCAT_URL);
		final JFrame parent = (JFrame) getRootPane().getTopLevelAncestor();
		final JInternalFrame parentInternal = (JInternalFrame) getRootPane()
				.getParent();
				
		jPanelTopImg = new javax.swing.JPanel();
		jPanelLeftImg = new javax.swing.JPanel();
		jPanelContent = new javax.swing.JPanel();
		jPanelRightLine = new javax.swing.JPanel();
		jPanelBottomLine = new javax.swing.JPanel();

		jPanelInfo = new javax.swing.JPanel();
		jPanelAplications = new javax.swing.JPanel();
		jPanelWeb = new javax.swing.JPanel();

		getContentPane().setLayout(new BorderLayout(0, 0));
		// this.setBackground(Color.WHITE);

		getContentPane().add(jPanelTopImg, BorderLayout.NORTH);

		// jPanelTopImg.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		jPanelTopImg.setLayout(new BorderLayout(0, 0));
		jPanelTopImg.setBackground(Color.WHITE);

		ImageIcon imageIconTitleTop = new ImageIcon(
				CLocalGISAppFrame.class.getResource(IMAGE_SSO_TITLE_TOP));
		imageTitleTop = new JLabel(imageIconTitleTop);

//		jPanelTopImg.setSize(900, 275);
//		jPanelTopImg.setPreferredSize(new Dimension(900, 275));
		jPanelTopImg.setSize(900, 215);
		jPanelTopImg.setPreferredSize(new Dimension(900, 215));
		imageTitleTop.setPreferredSize(new Dimension(imageIconTitleTop
				.getIconWidth(), imageIconTitleTop.getIconHeight()));
		imageTitleTop.setSize(imageIconTitleTop.getIconWidth(),
				imageIconTitleTop.getIconHeight());
		jPanelTopImg.add(imageTitleTop, BorderLayout.CENTER);

		getContentPane().add(jPanelLeftImg, BorderLayout.WEST);
		jPanelLeftImg.setLayout(new BorderLayout(0, 0));
		jPanelLeftImg.setBackground(Color.WHITE);

		ImageIcon imageIconTitleLeft = new ImageIcon(
				CLocalGISAppFrame.class.getResource(IMAGE_SSO_TITLE_LEFT));
		imageTitleLeft = new JLabel(imageIconTitleLeft);

//		jPanelLeftImg.setSize(200, 325);
//		jPanelLeftImg.setPreferredSize(new Dimension(200, 325));
		jPanelLeftImg.setSize(275, 325);
		jPanelLeftImg.setPreferredSize(new Dimension(275, 325));
		imageTitleLeft.setPreferredSize(new Dimension(imageIconTitleLeft
				.getIconWidth(), imageIconTitleLeft.getIconHeight()));
		imageTitleLeft.setSize(imageIconTitleLeft.getIconWidth(),
				imageIconTitleLeft.getIconHeight());
		jPanelLeftImg.add(imageTitleLeft, BorderLayout.NORTH);

		getContentPane().add(jPanelContent, BorderLayout.CENTER);

		jPanelContent.setLayout(new BorderLayout(0, 0));

		jPanelContent.add(jPanelInfo, BorderLayout.NORTH);
		jPanelInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		// jPanelInfo.setBorder(BorderFactory.createLoweredBevelBorder());
		jPanelInfo.setBackground(Color.WHITE);
		jButtonCloseSession = new javax.swing.JButton();
		jButtonCloseSession.setFont(new Font("Arial", Font.BOLD, 12));
		jButtonCloseSession.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if(jButtonCloseSession.getComponentAt(e.getPoint())==null)
					jButtonCloseSession.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				else
					jButtonCloseSession.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		jPanelInfo.add(jButtonCloseSession);

		
		jButtonCloseSession
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jButtonCloseSessionActionPerformed(evt);
					}
				});

		jPanelContent.add(jPanelAplications, BorderLayout.CENTER);
		jPanelAplications.setLayout(new BorderLayout(0, 0));
		jPanelTitleAplications = new JPanel();
		jPanelAplications.add(jPanelTitleAplications, BorderLayout.NORTH);
		jPanelTitleAplications.setLayout(new BorderLayout(0, 0));

		jLabelTitleAplications = new JLabel("");
		jLabelTitleAplications.setBackground(new Color(230, 230, 250));
		jLabelTitleAplications.setOpaque(true);
		
		jLabelTitleAplications.setFont(new Font("Arial", Font.BOLD, 12));
		jLabelTitleAplications.setPreferredSize(new Dimension(jLabelTitleAplications.getWidth(),20));
		// jLabelTitleAplicaciones.setHorizontalAlignment(SwingConstants.CENTER);
		jPanelTitleAplications.add(jLabelTitleAplications);
		jPanelTitleAplications.setBorder(BorderFactory
				.createLineBorder(Color.BLACK));

		jPanelTableAplications = new JPanel();
		jPanelAplications.add(jPanelTableAplications, BorderLayout.CENTER);

		jPanelTableAplications.setLayout(new BorderLayout(0, 0));
		jTableAplication = new javax.swing.JTable();
		jTableAplication.setBackground(Color.WHITE);

		jTableAplication.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				jTableWeb.clearSelection();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				int row = jTableAplication.rowAtPoint(e.getPoint());
				if (row > -1) {
					jTableWeb.clearSelection();
					jTableAplication.clearSelection();
					jTableAplication.setRowSelectionInterval(row, row);
					jTableAplication.setSelectionBackground(new Color(102, 153, 204));				
				}
			}
		});
		jScrollPaneTableAplications = new javax.swing.JScrollPane();

		jPanelTableAplications.add(jScrollPaneTableAplications,
				BorderLayout.CENTER);
		

		jTableAplication.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				jTableWeb.clearSelection();
				if (e.getClickCount() == 2) {	
					String selectedAppName = jTableAplication.getValueAt(jTableAplication.getSelectedRow(), 0).toString().replace(APP_TITLE_MARKER, "");
					String locale = "es_es";
					if(messages!=null && messages.getLocale()!=null){
						locale = messages.getLocale().toString().toLowerCase(); 
					}
					String path = "";
					Iterator<String> localgisAppIt = localGISApps.keySet().iterator();
					while(localgisAppIt.hasNext()){
						String key = localgisAppIt.next();
						if(localGISApps.get(key).getDictionary().get(locale).equals(selectedAppName)){
							path = localGISApps.get(key).getPath();
						}						
					}					
					final String rutaApp = serverHost + path;
					// Utils.openDesktopApp(rutaApp);

					final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
							parent, null);
					progressDialog.setModal(true);
					// progressDialog.setTitle(literales.getString("CAuthDialog.title"));
					progressDialog.setTitle("Información");
					progressDialog.report("Cargando: "
							+ jTableAplication
									.getValueAt(jTableAplication.getSelectedRow(), 0)
									.toString().replace(APP_TITLE_MARKER, ""));

					progressDialog.addComponentListener(new ComponentAdapter() {
						public void componentShown(ComponentEvent e) {
							new Thread(new Runnable() {
								public void run() {
									try {
										Utils.openDesktopApp(rutaApp);
									} catch (Exception e) {
										logger.error(e);
									} finally {
										progressDialog.setVisible(false);
										parentInternal.setCursor(Cursor
												.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
										progressDialog.dispose();
									}
								}
							}).start();
						}
					});
					Utils.centreComponent(progressDialog, parentInternal);
					progressDialog.setVisible(true);

				}
			}			
			@Override
			public void mouseExited(MouseEvent e) {
				jTableAplication.clearSelection();
			}
		});

		jPanelContent.add(jPanelWeb, BorderLayout.SOUTH);
		jPanelWeb.setLayout(new BorderLayout());

		jPanelTitleWeb = new JPanel();
		jPanelWeb.add(jPanelTitleWeb, BorderLayout.NORTH);
		jPanelTitleWeb.setLayout(new BorderLayout(0, 0));

		jLabelTitleWeb = new JLabel("");
		jLabelTitleWeb.setBackground(new Color(230, 230, 250));
		jLabelTitleWeb.setOpaque(true);

		jLabelTitleWeb.setFont(new Font("Arial", Font.BOLD, 12));
		jLabelTitleWeb.setPreferredSize(new Dimension(jLabelTitleWeb.getWidth(),20));
		// jLabelTitleWeb.setHorizontalAlignment(SwingConstants.CENTER);
		jPanelTitleWeb.add(jLabelTitleWeb);
		jPanelTitleWeb.setBorder(BorderFactory.createLineBorder(Color.black));
		//jLabelTitleWeb.setBackground(new Color(102, 153, 204));
		//jLabelTitleWeb.setOpaque(true);
		
		jPanelTableWeb = new JPanel();
		jPanelWeb.add(jPanelTableWeb, BorderLayout.CENTER);

		jPanelTableWeb.setLayout(new BorderLayout(0, 0));
		jScrollPaneTableWeb = new javax.swing.JScrollPane();
		jPanelTableWeb.add(jScrollPaneTableWeb, BorderLayout.CENTER);
		jTableWeb = new javax.swing.JTable();
		jTableWeb.setBackground(Color.WHITE);

		jTableWeb.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				jTableAplication.clearSelection();
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				int row = jTableWeb.rowAtPoint(e.getPoint());
				if (row > -1) {
					jTableAplication.clearSelection();
					jTableWeb.clearSelection();
					jTableWeb.setRowSelectionInterval(row, row);
					jTableWeb.setSelectionBackground(new Color(102, 153, 204));				
				}
			}
		});

		jTableWeb.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				jTableAplication.clearSelection();
				if (e.getClickCount() == 2) {
					String selectedAppName = jTableWeb.getValueAt(jTableWeb.getSelectedRow(), 0).toString().replace(APP_TITLE_MARKER, "");
					String locale = "es_es"; ;
					if(messages!=null && messages.getLocale()!=null){
						locale = messages.getLocale().toString().toLowerCase(); 
					}
					String path = "";
					Iterator<String> localgisAppIt = localGISApps.keySet().iterator();
					while(localgisAppIt.hasNext()){
						String key = localgisAppIt.next();
						if(localGISApps.get(key).getDictionary().get(locale).equals(selectedAppName)){
							path = localGISApps.get(key).getPath();
						}						
					}			
					final String rutaApp = serverHost + path;
					// Utils.openWebApp(rutaApp, SecurityManager.getIdSesion());

					final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
							parent, null);
					progressDialog.setModal(true);
					// progressDialog.setTitle(literales.getString("CAuthDialog.title"));
					progressDialog.setTitle("Información");
					progressDialog.report("Cargando: "
							+ jTableWeb
									.getValueAt(jTableWeb.getSelectedRow(), 0)
									.toString().replace(APP_TITLE_MARKER, ""));

					progressDialog.addComponentListener(new ComponentAdapter() {
						public void componentShown(ComponentEvent e) {
							new Thread(new Runnable() {
								public void run() {
									try {

										Utils.openWebApp(rutaApp,
												SecurityManager.getIdSesion());

									} catch (Exception e) {
										logger.error(e);
									} finally {
										progressDialog.setVisible(false);
										parentInternal.setCursor(Cursor
												.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
										progressDialog.dispose();
									}
								}
							}).start();
						}
					});
					Utils.centreComponent(progressDialog, parentInternal);
					progressDialog.setVisible(true);

				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				jTableWeb.clearSelection();
			}
		});

		getContentPane().add(jPanelRightLine, BorderLayout.EAST);
		jPanelRightLine.setSize(8,300);
		jPanelRightLine.setPreferredSize(new Dimension(8,300));
		jPanelRightLine.setBackground(Color.WHITE);
		
		getContentPane().add(jPanelBottomLine, BorderLayout.SOUTH);
		jPanelBottomLine.setSize(900,5);
		jPanelBottomLine.setPreferredSize(new Dimension(900,5));
		jPanelBottomLine.setBackground(Color.WHITE);
		
		jScrollPaneTableAplications.setViewportView(jTableAplication);
		jScrollPaneTableWeb.setViewportView(jTableWeb);

		changeScreenLang(messages);

		pack();

		this.setBorder(new LineBorder(Color.BLACK, 0));
		((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI())
				.setNorthPane(null);

	}

	private void jButtonCloseSessionActionPerformed(
			java.awt.event.ActionEvent evt) {
		try {
			logger.info("Desconectando al usuario");
			SecurityManager.logout();
			AppContext.releaseResources();
			SSOAuthManager.clearRegistrySesion();
			setClosed(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*finally{
			SSOAuthManager.clearRegistrySesion();
		}*/
	}

	private HashMap<String,LocalGISApp> peticionBD(){
		if(localGISApps == null){
			localGISApps = new HashMap<String,LocalGISApp>();			
			String url = AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.PRINCIPAL_WEBAPP_NAME + ServletConstants.GETAPPS_SERVLET;
			try {
				AppsClient appsClient = new AppsClient(url);
				localGISApps = appsClient.getAllApps();                 
			} catch (Exception e) {
				logger.error(e);
			}

			GeopistaAcl defaultAcl=null;
        	String permisoApp;
        	Iterator<String> itKeys = localGISApps.keySet().iterator();
        	String keyName=null;
			while (itKeys.hasNext()) {
				try {
					keyName = (String) itKeys.next(); 	        	
					defaultAcl = com.geopista.security.SecurityManager.getPerfil(localGISApps.get(keyName).getAcl());
					permisoApp = localGISApps.get(keyName).getPerm();
		        	if(!defaultAcl.checkPermission(new GeopistaPermission(permisoApp))){
		        		localGISApps.get(keyName).setActive(false);
		        	}    
		        	defaultAcl=null;
	        	} catch (Exception ex) {
	        		if (defaultAcl!=null)
	        			logger.error("Error al obtener el ACL:"+defaultAcl.getName());
	        		else
	        			logger.error("Error al obtener el ACL por permiso:"+keyName+" ACL:"+localGISApps.get(keyName).getAcl());
				}
			}
		
		}
		return localGISApps;
	}	
	
	private void actualizarModelo() {
		//Peticion de apps a la bd o fichero
		localGISApps = peticionBD();
		
		//listaLocalGISApp = new ListaLocalGISApps(this.messages);
		modelLocalGISApp = new LocalGISAppTableModel();
		modelLocalGISApp.setModelData(localGISApps,
				LocalGISApp.TIPO_APP_DESKTOP, messages.getLocale());
		
		sorter = new TableSorted(modelLocalGISApp);
		sorter.setTableHeader(jTableAplication.getTableHeader());
		sorter.setSortingStatus(0, TableSorted.ASCENDING);
		jTableAplication.setTableHeader(null);
		jTableAplication.setCursor(new Cursor(Cursor.HAND_CURSOR));
		// jTableAplication.setRowHeight(50);
		jTableAplication.setRowHeight(30);
		//jPanelTableAplications.setBackground(Color.WHITE);

		jTableAplication.setFont(new Font("Arial Bold", 0, 12));
		// jTableAplication.setModel(modelLocalGISApp);
		jTableAplication.setModel(sorter);
		jTableAplication.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableAplication.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		jScrollPaneTableAplications.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneTableAplications.getViewport().setOpaque(true);
		jScrollPaneTableAplications.getViewport().setBackground(Color.WHITE);
		jScrollPaneTableAplications.setBorder(BorderFactory.createEmptyBorder());
		
		modelLocalGISWeb = new LocalGISAppTableModel();
		modelLocalGISWeb
				.setModelData(localGISApps, LocalGISApp.TIPO_APP_WEB, messages.getLocale());
	
		sorter = new TableSorted(modelLocalGISWeb);
		sorter.setTableHeader(jTableAplication.getTableHeader());
		sorter.setSortingStatus(0, TableSorted.ASCENDING);
		
		jTableWeb.setTableHeader(null);
		jTableWeb.setCursor(new Cursor(Cursor.HAND_CURSOR));
		// jTableWeb.setRowHeight(50);
		jTableWeb.setRowHeight(30);
		
		jTableWeb.setFont(new Font("Arial Bold", 0, 12));
		// jTableWeb.setModel(modelLocalGISWeb);
		jTableWeb.setModel(sorter);
		jTableWeb.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		jTableWeb.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//		jPanelTableWeb.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		jPanelTableWeb.setPreferredSize(new Dimension(
				jPanelTableWeb.getWidth(), (jTableWeb.getRowCount()*30)+5));
		jPanelTableWeb.setMaximumSize(new Dimension(
				jPanelTableWeb.getWidth(), (4*30)+5));
//		jPanelTableWeb.setPreferredSize(new Dimension(
//				jPanelTableWeb.getWidth(), 100));
		jScrollPaneTableWeb.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneTableWeb.getViewport().setOpaque(true);
		jScrollPaneTableWeb.getViewport().setBackground(Color.WHITE);
		jScrollPaneTableWeb.setBorder(BorderFactory.createEmptyBorder());

		
	}

	public void changeScreenLang(ResourceBundle messages) {
		this.messages = messages;
		
		actualizarModelo();
		
		jButtonCloseSession.setText(messages.getString("cerrarSesion"));
		jButtonCloseSession.setToolTipText(messages.getString("cerrarSesion"));
		jLabelTitleAplications.setText("          "
				+ messages.getString("aplicacionesEscritorio"));
		jLabelTitleWeb.setText("          "
				+ messages.getString("aplicacionesWeb"));
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanelTopImg;
	private javax.swing.JPanel jPanelLeftImg;
	private javax.swing.JPanel jPanelContent;
	private javax.swing.JPanel jPanelRightLine;
	private javax.swing.JPanel jPanelBottomLine;	
	private javax.swing.JPanel jPanelInfo;
	private javax.swing.JPanel jPanelAplications;
	private javax.swing.JButton jButtonCloseSession;
	private javax.swing.JScrollPane jScrollPaneTableAplications;
	private javax.swing.JTable jTableAplication;
	private JScrollPane jScrollPaneTableWeb;
	private JTable jTableWeb;
	private JPanel jPanelWeb;
	private JPanel jPanelTitleAplications;
	private JLabel jLabelTitleAplications;
	private JPanel jPanelTitleWeb;
	private JLabel jLabelTitleWeb;
	private JPanel jPanelTableAplications;
	private JPanel jPanelTableWeb;
	private JLabel imageTitleTop;
	private JLabel imageTitleLeft;

}
