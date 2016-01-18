/*
 * Created on 28-mar-2005 by juacas
 */
package com.geopista.bean;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jts.geom.Geometry;
/**
 * Implementación del ActiveX para usar servicios de Geopista desde aplicaciones
 * Automation
 * 
 * @author juacas
 */
public class GEOPISTABean extends JPanel 
{
	/**
	 * Logger for this class
	 */
//	private static final Log	logger				= LogFactory
//															.getLog(GEOPISTABean.class);

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 1L;

	private JLabel				iconoGeopista		= null;
	/**
	 * ProxyBean
	 */
	IGEOPISTABean				beanProxy;

	/**
	 * 
	 */
	public GEOPISTABean()
	{

	initialize();

	setConnectionStatusMessage(AppContext.getApplicationContext().isOnline());
	setLoginStatusMessage(AppContext.getApplicationContext().isLogged());
//	 listen to connection state
	AppContext.getApplicationContext().addAppContextListener(
			new AppContextListener()
				{

					public void connectionStateChanged(GeopistaEvent e)
					{
					switch (e.getType())
					{
						case GeopistaEvent.DESCONNECTED :
							setConnectionStatusMessage(false);
							break;
						case GeopistaEvent.RECONNECTED :
							setConnectionStatusMessage(true);
							break;
						case GeopistaEvent.LOGGED_IN :
							setLoginStatusMessage(true);
							break;
						case GeopistaEvent.LOGGED_OUT :
							setLoginStatusMessage(false);
							break;
					}
					}
				});
//	getBeanProxy();
	}

	private IGEOPISTABean getBeanProxy()
	{
	if (beanProxy == null)
		{
		System.out.println("getBeanProxy() - Inicio de carga de BeanProxy.");
			
		beanProxy = new GeopistaBeanProxy();
		System.out.println("getBeanProxy() - Instanciado el BeanProxy");
			
		}
	return beanProxy;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
	try
		{
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 1;  // Generated
		gridBagConstraints4.gridwidth = 2;  // Generated
		gridBagConstraints4.gridy = 1;  // Generated
		jLabel = new JLabel();
		jLabel.setText("Automation Component GEOPISTA");  // Generated
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;  // Generated
		gridBagConstraints.gridwidth = 2;  // Generated
		gridBagConstraints.gridy = 0;  // Generated
		connectionLabel = new JLabel();
		loginLabel = new JLabel();
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		this.setLayout(new GridBagLayout());

		this.setSize(new java.awt.Dimension(296,235));  // Generated
		gridBagConstraints1.gridx = 0; // Generated
		gridBagConstraints1.gridy = 0; // Generated
		gridBagConstraints1.gridwidth = 3; // Generated
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL; // Generated
		gridBagConstraints2.gridx = 1; // Generated
		gridBagConstraints2.gridy = 2; // Generated
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST; // Generated
		loginLabel.setText("Sesion"); // Generated
		loginLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM); // Generated
		gridBagConstraints3.gridx = 2; // Generated
		gridBagConstraints3.gridy = 2; // Generated
		gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST; // Generated
		connectionLabel.setText("Servidor"); // Generated
		connectionLabel
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM); // Generated
		this.add(loginLabel, gridBagConstraints2);  // Generated
		this.add(connectionLabel, gridBagConstraints3);  // Generated
		this.add(getIconoGeopista(), gridBagConstraints);  // Generated
		this.add(jLabel, gridBagConstraints4);  // Generated
		// this.add(getIconoGeopista(), gridBagConstraints1); // Generated
		}
	catch (RuntimeException e)
		{
		System.out.println("initialize()");
	e.printStackTrace();
		}

	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getIconoGeopista()
	{
	if (iconoGeopista == null)
		{
		iconoGeopista = new JLabel();
		iconoGeopista.setText("GEOPISTABean"); // Generated
		ImageIcon icon = IconLoader.icon("app-icon.gif");
		if (icon != null) iconoGeopista.setIcon(icon); // Generated
		iconoGeopista
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM); // Generated
		iconoGeopista.setToolTipText("GEOPISTABean v.0.0.1"); // Generated
		iconoGeopista.setVisible(true); // Generated
		}
	return iconoGeopista;
	}

	public static void main(String[] args)
	{
	JFrame fr=new JFrame("test");
	fr.setSize(300,200);
	
	GEOPISTABean bean = new GEOPISTABean();
	fr.getContentPane().add(bean);
	fr.setVisible(true);
	bean.createTestConfig();
	
	bean.edit();
	// bean.extractMap("test",new Double(0),new Double(0),new
	// Double(21232142),new Double(124141321));
	System.exit(0);
	}

	public boolean edit()
	{
	return getBeanProxy().edit();
	}

	public Object getAttribute(String name)
	{
	return getBeanProxy().getAttribute(name);
	}

	public String getMapName()
	{
	return getBeanProxy().getMapName();
	}

	public String getMapPath()
	{
	return getBeanProxy().getMapPath();
	}

	public GeopistaSchema getSchema()
	{
	return getBeanProxy().getSchema();
	}

	public void reset()
	{
	getBeanProxy().reset();
	}

	public boolean setAttribute(String name, Object value)
	{
	return getBeanProxy().setAttribute(name, value);
	}
	public boolean setAttributeAsString(String name, String value)
	{
	return getBeanProxy().setAttributeAsString(name, value);
	}
	public boolean setLayer(String Layer)
	{
	return getBeanProxy().setLayer(Layer);
	}

	public GeopistaFeature getFeature()
	{
	return getBeanProxy().getFeature();
	}

	public GeopistaMap getMap()
	{
	return getBeanProxy().getMap();
	}
	/**
	 * Decorative elements
	 */

	private JLabel	loginLabel		= null;
	private JLabel	connectionLabel	= null;

	public void setLoginStatusMessage(boolean logged)
	{
	if (logged)
		{
		loginLabel.setIcon(IconLoader.icon("lock_closed.png"));
		loginLabel.setToolTipText(AppContext.getApplicationContext()
				.getI18nString("geopista.LoggedInStatusMessage"));
		}
	else
		{
		loginLabel.setIcon(IconLoader.icon("lock_open.png"));
		loginLabel.setToolTipText(AppContext.getApplicationContext()
				.getI18nString("geopista.LoggedOutStatusMessage"));
		}
	}

	public void setConnectionStatusMessage(boolean connected)
	{
	if (!connected)
		{
		connectionLabel.setIcon(IconLoader.icon("no_network.png"));
		connectionLabel.setToolTipText(AppContext.getApplicationContext()
				.getI18nString("geopista.OffLineStatusMessage"));

		}
	else
		{
		connectionLabel.setIcon(IconLoader.icon("online.png"));
		connectionLabel.setToolTipText(AppContext.getApplicationContext()
				.getI18nString("geopista.OnLineStatusMessage"));

		}
	}

	private boolean testingMode=false;

	private JLabel jLabel = null;
	public void createTestConfig()
	{
	testingMode=true;
	getBeanProxy().createTestConfig();
	}

	public void setNotificationAreaVisible(Boolean visible)
	{
	connectionLabel.setVisible(visible.booleanValue());
	loginLabel.setVisible(visible.booleanValue());
	}

	public boolean checkFeature(Boolean interactive)
	{
	return beanProxy.checkFeature(interactive);
	}

	public boolean removeFeature(String sysId)
	{
	return beanProxy.removeFeature(sysId);
	}

	public GeopistaMap extractMap(String mapName, Double longIni,
			Double latIni, Double longEnd, Double latEnd, String format)
	{
	return beanProxy.extractMap(mapName, longIni, latIni, longEnd, latEnd,
			format);
	}

	public Geometry getGeometry()
	{
	return beanProxy.getGeometry();
	}

	public GeopistaLayer[] getLayers()
	{
	return beanProxy.getLayers();
	}

	public String getMapFormat()
	{
	return beanProxy.getMapFormat();
	}

	public String getWKTGeometry()
	{
	return beanProxy.getWKTGeometry();
	}

	public void modifiedFeature(String sysId)
	{
	beanProxy.modifiedFeature(sysId);
	}

	public boolean setGeometry(String geometryWKT)
	{
	return beanProxy.setGeometry(geometryWKT);
	}

	public boolean syncMap()
	{
	return beanProxy.syncMap();
	}

	public boolean checkGeometry(Geometry geom)
	{
	return beanProxy.checkGeometry(geom);
	}

	public boolean checkGeometry(String WKTgeom)
	{
	return beanProxy.checkGeometry(WKTgeom);
	}

	public String[] getErrorMessages()
	{
	return beanProxy.getErrorMessages();
	}

	public boolean newFeature(String handle)
	{
	return beanProxy.newFeature(handle);
	}

	public GeopistaMap selectMap()
	{
	return beanProxy.selectMap();
	}

	public boolean setSchema(String XMLGeopistaSchema)
	{
	return beanProxy.setSchema(XMLGeopistaSchema);
	}
} // @jve:decl-index=0:visual-constraint="30,10"
