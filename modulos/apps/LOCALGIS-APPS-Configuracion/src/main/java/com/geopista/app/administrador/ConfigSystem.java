/**
 * ConfigSystem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 14-mar-2005 by juacas
 *
 * 
 */
package com.geopista.app.administrador;

import javax.jnlp.ServiceManager;
import javax.jnlp.SingleInstanceListener;
import javax.jnlp.SingleInstanceService;
import javax.jnlp.UnavailableServiceException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.geopista.app.AppContext;
import com.geopista.ui.dialogs.SystemConfigPanel;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

import org.apache.log4j.Logger;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ConfigSystem extends JDialog implements SingleInstanceListener
{
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext(false);
	private javax.swing.JPanel jContentPane = null;
	private SystemConfigPanel systemConfigPanel = null;
	private OKCancelPanel _okCancelPanel = null;
	
	private boolean showOpenAmSettings=false;

    static Logger logger = Logger.getLogger(ConfigSystem.class);
	
	/**
	 * This method initializes systemConfigPanel	
	 * 	
	 * @return com.geopista.ui.dialogs.SystemConfigPanel	
	 */    
	private SystemConfigPanel getSystemConfigPanel(boolean showOpenAmSettings) {
		if (systemConfigPanel == null) {
			systemConfigPanel = new SystemConfigPanel(showOpenAmSettings);
		}
		return systemConfigPanel;
	}

	private SystemConfigPanel getSystemConfigPanel() {
		return getSystemConfigPanel(false);
	}
	
	private ConfigSystem getConfigSystem(){
		return this;
	}
	/**
	 * This method initializes OKCancelPanel	
	 * 	
	 * @return com.vividsolutions.jump.workbench.ui.OKCancelPanel	
	 */    
	private OKCancelPanel getOkCancelPanel() {
		if (_okCancelPanel == null) {
			_okCancelPanel = new OKCancelPanel();
			_okCancelPanel.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {   
					if (_okCancelPanel.wasOKPressed()) {
						String errorMessage = getSystemConfigPanel(showOpenAmSettings).validateInput();
						if (errorMessage != null) {
							JOptionPane.showMessageDialog( ConfigSystem.this, errorMessage, "GEOPISTA", JOptionPane.ERROR_MESSAGE);
							return;
						} else {
							getSystemConfigPanel(showOpenAmSettings).okPressed();
							setVisible(false);
							if (fromInicio)
								getConfigSystem().dispose();
							else
								System.exit(0);
							return;
						}
					}
					setVisible(false);

					if (fromInicio)
						getConfigSystem().dispose();
					else	
						System.exit(0);

				}
			});
		}
		return _okCancelPanel;
	}
	public static void main(String[] args) {
		ConfigSystem cfg=new ConfigSystem(args);
		System.exit(0);
	}
	private boolean fromInicio;
	public ConfigSystem(String[] args) {
		this(false,args);
	}
	/**
	 * This is the default constructor
	 */
	public ConfigSystem(boolean fromInicio,String[] args) {
		super();
		this.fromInicio=fromInicio;
		
		if (args.length>0)
			showOpenAmSettings=true;
		
		addJnlpSingleInstanceListener();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		getConfigSystem().toFront();
    	getConfigSystem().repaint();
    	
		initialize();
		//show();
		
		getConfigSystem().toFront();
    	getConfigSystem().repaint();
		
    	/*
		java.awt.EventQueue.invokeLater(new Runnable() {
		    @Override
		    public void run() {
		    	getConfigSystem().toFront();
		    	getConfigSystem().repaint();
		    }
		});*/
	}
	
	@Override
	public void newActivation(String[] arg0) {
		setAlwaysOnTop(true);
		JOptionPane.showMessageDialog(this, "La aplicación de configuracion ya está abierta");
		setAlwaysOnTop(false);
	}
	
	public void addJnlpSingleInstanceListener() {
		try {
			SingleInstanceService singleInstanceService = (SingleInstanceService) ServiceManager
					.lookup("javax.jnlp.SingleInstanceService");
			singleInstanceService
					.addSingleInstanceListener((SingleInstanceListener) this);
		} catch (UnavailableServiceException e) {
			logger.info("No encontrado JNLP Single Instance Service: " + e);
		} catch (Exception e) {
			logger.info("Error: " + e);
		}
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);  // Generated
		this.setTitle(aplicacion.getString("localgis.release")+" Config");
		this.setModal(true);  // Generated
		this.setResizable(false);  // Generated
		this.setContentPane(getJContentPane());
		this.setSize(systemConfigPanel.getPreferredSize());
		this.setVisible(true);  // Generated
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getSystemConfigPanel(showOpenAmSettings), java.awt.BorderLayout.CENTER);  // Generated
			jContentPane.add(getOkCancelPanel(), java.awt.BorderLayout.SOUTH);  // Generated
		}
		return jContentPane;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
