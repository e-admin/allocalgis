/**
 * ConnectionStatus.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security.connect;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
import com.geopista.security.ISecurityPolicy;
import com.geopista.security.SecurityManager;
import com.geopista.security.TransparentLogin;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;

public class ConnectionStatus {

	static Logger logger = Logger.getLogger(ConnectionStatus.class);

	
	private ApplicationContext aplicacion;
	private JLabel connectionLabel;


	private ISecurityPolicy securityPolicy;


	private boolean fromInicio;
	
	private boolean global=false;
	
	private boolean solicitarEntidad=false;
	
    private JPanel jPanelStatus = null;

	public ConnectionStatus(ISecurityPolicy securityPolicy, boolean fromInicio){
		this.securityPolicy=securityPolicy;
		this.aplicacion=securityPolicy.getAplicacion();
		//this.connectionLabel=securityPolicy.getConnectionLabel();
		
		if (this.connectionLabel==null)
			this.connectionLabel=new JLabel();
		this.fromInicio=fromInicio;
	}

	
	public void setGlobal(boolean global){
		this.global=global;
	}
	
	public void setSolicitarEntidad(boolean solicitarEntidad){
		this.solicitarEntidad=solicitarEntidad;
	}
	
	/**
	 * Inicialización de la conexión.
	 */
	public void init(){
		
		setConnectionInitialStatusMessage(aplicacion.isOnline());
		aplicacion.addAppContextListener(new AppContextListener() {

			public void connectionStateChanged(GeopistaEvent e) {
				switch (e.getType()) {
				case GeopistaEvent.DESCONNECTED:
					setConnectionStatusMessage(false);
					break;
				case GeopistaEvent.RECONNECTED:
					setConnectionStatusMessage(true);
					break;
				}
			}
		});
	}
	
	
	private void setConnectionInitialStatusMessage(boolean connected) {
		if (!connected) {
			connectionLabel.setIcon(IconLoader.icon("no_network.png"));
			connectionLabel.setToolTipText(aplicacion
					.getI18nString("geopista.OffLineStatusMessage"));

		} else {
			connectionLabel.setIcon(IconLoader.icon("online.png"));
			connectionLabel.setToolTipText(aplicacion
					.getI18nString("geopista.OnLineStatusMessage"));

		}
	}
	
	public void setConnectionStatusMessage(boolean connected) {
		if (!connected) {

			connectionLabel.setIcon(IconLoader.icon("no_network.png"));
			connectionLabel.setToolTipText(aplicacion
					.getI18nString("geopista.OffLineStatusMessage"));
			
			if (aplicacion.isLogged())
				SecurityManager.unLogged();
			else {
				if (!aplicacion.isPartialLogged()){
					//No tengo claro porque teniamos puesto que había que hacer
					//un login.
					//aplicacion.login();
				}
			}
		} else {
			connectionLabel.setIcon(IconLoader.icon("online.png"));
			connectionLabel.setToolTipText(aplicacion
					.getI18nString("geopista.OnLineStatusMessage"));

			//System.out.println("PASO2"+aplicacion.isLogged());
			//System.out.println("PASO2.1"+aplicacion.isPartialLogged());

			//2013-09-23 No realizamos un login
			/*if (!aplicacion.isLogged()) {
				if (!aplicacion.isPartialLogged()) {
					new TransparentLogin().showAuth(securityPolicy,fromInicio,global,solicitarEntidad,true);
				}
			}*/

		}
	}
	
	public void transparentLogin(){
		new TransparentLogin().showAuth(securityPolicy,fromInicio,global,solicitarEntidad,true);
	}
	
	public JPanel getCurrentJPanelStatus(){
		return jPanelStatus;
	}
	
	public JPanel getJPanelStatus()
    {
		
        if (jPanelStatus == null)
        {
            jPanelStatus = new JPanel(new GridBagLayout());
            //jPanelStatus.setLayout(new BorderLayout());
            /*JLabel lblMessage = new JLabel("Mensaje");
            jPanelStatus.add(lblMessage, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
                    0, 0), 0, 0));*/
            jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());
            	
            
            if (connectionLabel==null)
            	connectionLabel=new JLabel();
            
            connectionLabel.setBorder(new javax.swing.border.SoftBevelBorder(
                    javax.swing.border.SoftBevelBorder.LOWERED));
            jPanelStatus.add(connectionLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0,
                            0, 0), 0, 0));
            jPanelStatus.add(new JPanel(),
                    new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 0, 5, 0), 0, 0));  
						
        }
        return jPanelStatus;
    }
}
