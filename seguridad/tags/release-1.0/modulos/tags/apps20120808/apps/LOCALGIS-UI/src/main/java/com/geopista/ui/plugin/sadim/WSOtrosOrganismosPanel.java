
package com.geopista.ui.plugin.sadim;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.geopista.app.AppContext;
import com.geopista.sql.GEOPISTAConnection;
import com.geopista.ui.plugin.sadim.ws.WSOtrosOrganismos;
import com.geopista.ui.plugin.sadim.ws.WSOtrosOrganismosImplServiceLocator;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;



public class WSOtrosOrganismosPanel extends JPanel
{
	// prefijo usado en las tablas de test cuando el modo test está activo
	private static String PREFIJO_TABLAS_TEST="sadim_";

	// si el modo test está activado entonces todas las operaciones se hacen
	// sobre tablas con el prefijo "sadim"
	private static boolean MODO_TEST=true;

	// si se fuerza la inserción entonces cuando se intenta actualizar y no
	// existe un registro en la base de datos entonces se crea
	private static boolean FORZAR_INSERCION=true;

	// url del servicio web de demo
	private static String URL_WS_INE="http://localhost:8080/ModeloWSOtrosOrganismosDemo/WSOtrosOrganismos";
	private static String URL_WS_EDUCACION="http://localhost:8080/ModeloWSOtrosOrganismosDemo/WSOtrosOrganismos";
	private static String URL_WS_TURISMO="http://localhost:8080/ModeloWSOtrosOrganismosDemo/WSOtrosOrganismos";
	private static String URL_WS_SANIDAD="http://localhost:8080/ModeloWSOtrosOrganismosDemo/WSOtrosOrganismos";

	private static final Log logger=LogFactory.getLog(GeopistaUtil.class);

	private Properties props=new Properties();

	private String idProvincia="";
	private String idMunicipio="";
	private int anio=0;

	private JButton btnCancelar=new JButton();
	private JButton btnImportar=new JButton();
	private PlugInContext context;
	private JLabel pnlEntidadesSeleccionadas;
	private JLabel lblMensaje;
	private JLabel lblProgreso;
	private JProgressBar progressImportacion;
	private JTextField txtFecha;
	private JLabel lblFecha;
	private JComboBox comboWebService;
	private JPanel pnlDatosIncorporar;
	private JLabel lblDatosIncorporar;
	private JTextField txtMunicipio;
	private JTextField txtProvincia;
	private JLabel lblEntidadesSeleccionadas;
	private JLabel lblTitulo=new JLabel();
	private JLabel lbProvincia=new JLabel();

	private ApplicationContext appContext=AppContext.getApplicationContext();

	{
		// Set Look & Feel
		try
		{
			javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}



	public WSOtrosOrganismosPanel()
	{

	}



	public WSOtrosOrganismosPanel(PlugInContext context, Properties props)
	{
		this.props=props;
		this.context=context;

		MODO_TEST=Boolean.parseBoolean(props.get("modo_test").toString());
		FORZAR_INSERCION=Boolean.parseBoolean(props.get("forzar_insercion").toString());
		PREFIJO_TABLAS_TEST=props.get("prefijo_tablas_test").toString();
		URL_WS_INE=props.get("url_ws_ine").toString();
		URL_WS_EDUCACION=props.get("url_ws_educacion").toString();
		URL_WS_TURISMO=props.get("url_ws_turismo").toString();
		URL_WS_SANIDAD=props.get("url_ws_sanidad").toString();

		int idMunicipioCompleto=AppContext.getIdMunicipio();

		try
		{
			jbInit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		txtProvincia.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.noSeleccion")+" (0)");
		txtMunicipio.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.noSeleccion")+" (0)");

		Connection conexion;

		try
		{
			conexion=appContext.getConnection();

			PreparedStatement sql=conexion.prepareStatement("select nombreoficial, id_provincia, id_ine from municipios where id=?");
			sql.setInt(1,idMunicipioCompleto);

			ResultSet rset=sql.executeQuery();

			while (rset.next())
			{
				txtMunicipio.setText(rset.getString(1)+" ("+idMunicipioCompleto+")");
				idProvincia=rset.getString(2);
				idMunicipio=rset.getString(3);
			}

			rset.close();
			sql.close();

			sql=conexion.prepareStatement("select nombreoficial from provincias where id=?");
			sql.setString(1,idProvincia);

			rset=sql.executeQuery();

			while (rset.next())
			{
				txtProvincia.setText(rset.getString(1)+" ("+idProvincia+")");
			}

			rset.close();
			sql.close();

			if (idMunicipio.length()<=0||idProvincia.length()<=0)
			{
				progressImportacion.setVisible(false);
				lblProgreso.setVisible(false);
				btnImportar.setVisible(false);

				lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.entidadNoDetectada")+"</html>");
				lblMensaje.setVisible(true);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			progressImportacion.setVisible(false);
			lblProgreso.setVisible(false);
			btnImportar.setVisible(false);

			lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorInterno")+": "+ex.getMessage()+"</html>");
			lblMensaje.setVisible(true);
		}
	}



	public PlugInContext getContext()
	{
		return context;
	}



	public void setContext(PlugInContext context)
	{
		this.context=context;
	}



	private void jbInit() throws Exception
	{
		this.setLayout(null);
		this.setSize(375,350);
		this.setPreferredSize(new java.awt.Dimension(375,350));

		lblTitulo.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.explicacion")+"</html>");
		lblTitulo.setBounds(13,9,350,50);
		lblTitulo.setFont(new Font("Dialog",1,11));
		this.add(lblTitulo,null);
		lblTitulo.setBackground(new java.awt.Color(255,255,255));
		lblTitulo.setForeground(new java.awt.Color(64,128,128));

		lbProvincia.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.provincia"));
		lbProvincia.setBounds(9,8,76,20);

		btnCancelar.setText(appContext.getI18nString("btnCancelar"));
		btnCancelar.setBounds(275,314,90,25);
		btnCancelar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				btnCancelar_actionPerformed(e);
			}
		});
		btnImportar.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.importar"));
		btnImportar.setBounds(180,314,90,25);
		btnImportar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				btnImportarActionPerformed(e);
			}
		});

		this.add(btnImportar,null);
		this.add(btnCancelar,null);
		final JPanel jPanelDatos=new JPanel();
		this.add(jPanelDatos);
		jPanelDatos.setBounds(12,79,353,63);
		jPanelDatos.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		jPanelDatos.setLayout(null);
		jPanelDatos.add(lbProvincia);
		{
			pnlEntidadesSeleccionadas=new JLabel();
			jPanelDatos.add(pnlEntidadesSeleccionadas);
			pnlEntidadesSeleccionadas.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.municipio"));
			pnlEntidadesSeleccionadas.setBounds(9,34,76,20);
		}
		{
			txtProvincia=new JTextField();
			jPanelDatos.add(txtProvincia);
			txtProvincia.setBounds(95,8,243,21);
			txtProvincia.setEditable(false);
			txtProvincia.setBorder(BorderFactory.createTitledBorder(""));
		}
		{
			txtMunicipio=new JTextField();
			jPanelDatos.add(txtMunicipio);
			txtMunicipio.setBounds(96,34,242,21);
			txtMunicipio.setBorder(BorderFactory.createTitledBorder(""));
			txtMunicipio.setEditable(false);
		}

		lblEntidadesSeleccionadas=new JLabel();
		this.add(lblEntidadesSeleccionadas);
		lblEntidadesSeleccionadas.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.entidadesSeleccionadas"));
		lblEntidadesSeleccionadas.setBounds(12,66,303,14);
		lblEntidadesSeleccionadas.setFont(new java.awt.Font("Tahoma",1,11));

		lblDatosIncorporar=new JLabel();
		this.add(lblDatosIncorporar);
		lblDatosIncorporar.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.datosIncorporar"));
		lblDatosIncorporar.setFont(new java.awt.Font("Tahoma",1,11));
		lblDatosIncorporar.setBounds(12,149,157,14);

		pnlDatosIncorporar=new JPanel();
		this.add(pnlDatosIncorporar);
		pnlDatosIncorporar.setOpaque(true);
		pnlDatosIncorporar.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		pnlDatosIncorporar.setToolTipText("");
		pnlDatosIncorporar.setName("pnlDatosIncorporar");
		pnlDatosIncorporar.setBounds(10,163,353,63);
		pnlDatosIncorporar.setLayout(null);
		{
			progressImportacion=new JProgressBar();
			this.add(progressImportacion);
			progressImportacion.setBounds(13,266,346,27);
			progressImportacion.setMaximum(100);
		}
		{
			lblProgreso=new JLabel();
			this.add(lblProgreso);
			lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.importacionNoIniciada"));
			lblProgreso.setBounds(13,250,346,14);
			lblProgreso.setForeground(new java.awt.Color(0,0,255));
		}
		{
			lblMensaje=new JLabel();
			this.add(lblMensaje);
			lblMensaje.setText("");
			lblMensaje.setBounds(12,240,351,62);
			lblMensaje.setFont(new java.awt.Font("Tahoma",1,11));
			lblMensaje.setForeground(new java.awt.Color(255,0,0));
			lblMensaje.setVisible(false);
		}

		ComboBoxModel comboWebServiceModel=new DefaultComboBoxModel(new String[] { appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.comboPoblacion"), appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.comboEnsenianza"), appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.comboTurismo"), appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.comboSanidad") });
		comboWebService=new JComboBox();
		pnlDatosIncorporar.add(comboWebService);
		comboWebService.setModel(comboWebServiceModel);
		comboWebService.setBounds(14,9,325,21);
		{
			lblFecha=new JLabel();
			pnlDatosIncorporar.add(lblFecha);
			lblFecha.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.anioDatos"));
			lblFecha.setBounds(14,39,80,14);
		}
		{
			txtFecha=new JTextField();
			pnlDatosIncorporar.add(txtFecha);
			txtFecha.setText("2012");
			txtFecha.setBounds(106,36,51,21);
		}
	}



	private void btnCancelar_actionPerformed(ActionEvent e)
	{
		JDialog dialogPadre=(JDialog) SwingUtilities.getAncestorOfClass(JDialog.class,this);
		dialogPadre.setVisible(false);
	}



	private void btnImportarActionPerformed(ActionEvent e)
	{

		lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.conectarServicioWeb"));

		try
		{
			anio=Integer.parseInt(txtFecha.getText());
		}
		catch (Exception ex)
		{
			progressImportacion.setVisible(false);
			lblProgreso.setVisible(false);

			lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorFechaIncorrecta")+"</html>");
			lblMensaje.setVisible(true);
			return;
		}

		btnImportar.setVisible(false);
		btnCancelar.setVisible(false);

		try
		{

			switch (comboWebService.getSelectedIndex())
			{
			case 0:

				ConectarConIneSadei tareaIne=new ConectarConIneSadei();

				// Agrego un Listener para el cambio de la propiedad "progress"
				tareaIne.addPropertyChangeListener(new PropertyChangeListener()
				{
					public void propertyChange(PropertyChangeEvent evt)
					{
						if ("progress".equals(evt.getPropertyName()))
						{
							progressImportacion.setValue((Integer) evt.getNewValue());
						}
					}
				});

				tareaIne.execute();
				break;

			case 1:

				ConectarConEducacion tareaEducacion=new ConectarConEducacion();

				// Agrego un Listener para el cambio de la propiedad "progress"
				tareaEducacion.addPropertyChangeListener(new PropertyChangeListener()
				{
					public void propertyChange(PropertyChangeEvent evt)
					{
						if ("progress".equals(evt.getPropertyName()))
						{
							progressImportacion.setValue((Integer) evt.getNewValue());
						}
					}
				});

				tareaEducacion.execute();
				break;

			case 2:

				ConectarConTurismo tareaTurismo=new ConectarConTurismo();

				// Agrego un Listener para el cambio de la propiedad "progress"
				tareaTurismo.addPropertyChangeListener(new PropertyChangeListener()
				{
					public void propertyChange(PropertyChangeEvent evt)
					{
						if ("progress".equals(evt.getPropertyName()))
						{
							progressImportacion.setValue((Integer) evt.getNewValue());
						}
					}
				});

				tareaTurismo.execute();
				break;

			case 3:

				ConectarConSanidad tareaSanidad=new ConectarConSanidad();

				// Agrego un Listener para el cambio de la propiedad "progress"
				tareaSanidad.addPropertyChangeListener(new PropertyChangeListener()
				{
					public void propertyChange(PropertyChangeEvent evt)
					{
						if ("progress".equals(evt.getPropertyName()))
						{
							progressImportacion.setValue((Integer) evt.getNewValue());
						}
					}
				});

				tareaSanidad.execute();
				break;
			}

		}
		catch (Exception ex)
		{
			progressImportacion.setVisible(false);
			lblProgreso.setVisible(false);

			lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorImportacion")+" "+ex.getMessage()+"</html>");
			lblMensaje.setVisible(true);

			btnImportar.setVisible(true);
			btnCancelar.setVisible(true);

			return;
		}

	}

	class ConectarConIneSadei extends SwingWorker<String, Void>
	{

		@Override
		protected String doInBackground() throws Exception
		{
			progressImportacion.setVisible(true);
			lblProgreso.setVisible(true);

			lblMensaje.setText("");
			lblMensaje.setVisible(false);

			int contador_updates=0;
			int contador_inserts=0;
			int total=0;

			try
			{
				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.conectarServicioWeb"));

				WSOtrosOrganismosImplServiceLocator wsOtrosOrganismosServiceLocator=new WSOtrosOrganismosImplServiceLocator();

				wsOtrosOrganismosServiceLocator.setWSOtrosOrganismosImplPortEndpointAddress(URL_WS_INE);

				WSOtrosOrganismos otrosOrganismos=wsOtrosOrganismosServiceLocator.getWSOtrosOrganismosImplPort();

				String resultadoWS="";

				try
				{
					resultadoWS=otrosOrganismos.obtenerInformacionIneSadei(idProvincia,idMunicipio,anio);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();

					progressImportacion.setVisible(false);
					lblProgreso.setVisible(false);
					btnImportar.setVisible(false);

					lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorConectando")+" "+URL_WS_INE+"</html>");
					lblMensaje.setVisible(true);

					return "error";
				}

				System.out.println(resultadoWS);

				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.procesandoRespuestaXML"));

				DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
				DocumentBuilder builder=builderFactory.newDocumentBuilder();

				InputStream is=new ByteArrayInputStream(resultadoWS.getBytes("UTF-8"));
				Document xml=(Document) builder.parse(is);

				xml.getDocumentElement().normalize();

				NodeList nodos=xml.getElementsByTagName("resultado");
				Element raiz=(Element) nodos.item(0);

				String codprov=raiz.getAttribute("codprov");
				String codmunic=raiz.getAttribute("codmunic");

				NodeList nodosDatos=raiz.getElementsByTagName("dato");

				GEOPISTAConnection conexion;

				conexion=(GEOPISTAConnection) appContext.getConnection();

				int registros_afectados;

				total=nodosDatos.getLength();
				
				Element estado=(Element)raiz.getElementsByTagName("estado").item(0);
				if (!estado.getAttribute("nivel").equalsIgnoreCase("exito"))
				{
					btnImportar.setVisible(true);
					btnCancelar.setVisible(true);
					return(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorServicioRemoto")+": "+filterString(estado.getNodeValue()));
				}

				for (int i=0; i<total; i++)
				{
					int progress=(int) ((i+1)*100/nodosDatos.getLength());

					setProgress(progress);

					Element element=(Element) nodosDatos.item(i);

					String codentidad=element.getAttribute("codentidad");
					String codpoblamiento=element.getAttribute("codpoblamiento");

					int n_hombres_a1=Integer.parseInt(getXMLKeyValue(element,"n_hombres_a1"));
					int n_mujeres_a1=Integer.parseInt(getXMLKeyValue(element,"n_mujeres_a1"));
					int fecha_a1=Integer.parseInt(getXMLKeyValue(element,"fecha_a1").toString());
					int viviendas_total=Integer.parseInt(getXMLKeyValue(element,"viviendas_total").toString());
					int pob_estacional=Integer.parseInt(getXMLKeyValue(element,"pob_estacional").toString());
					int padron=Integer.parseInt(getXMLKeyValue(element,"padron").toString());

					boolean existe_eiel_c_abast_rd=existeRegistroEnTabla(conexion,"eiel_c_abast_rd",codprov,codmunic,codentidad,codpoblamiento);
					boolean existe_eiel_c_saneam_rs=existeRegistroEnTabla(conexion,"eiel_c_saneam_rs",codprov,codmunic,codentidad,codpoblamiento);

					lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_padron_nd");

					// Actualizar tabla eiel_t_padron_nd
					// *****************************************************************
					PreparedStatement sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_padron_nd set n_hombres_a1=?,n_mujeres_a1=?,total_poblacion_a1=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and fecha_a1=?");

					sql.setInt(1,n_hombres_a1);
					sql.setInt(2,n_mujeres_a1);
					sql.setInt(3,n_hombres_a1+n_mujeres_a1);
					sql.setString(4,codprov);
					sql.setString(5,codmunic);
					sql.setString(6,codentidad);
					sql.setString(7,codpoblamiento);
					sql.setInt(8,fecha_a1);

					registros_afectados=sql.executeUpdate();

					sql.close();

					if (registros_afectados>0) contador_updates++;

					if (FORZAR_INSERCION&&registros_afectados<=0)
					{

						sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_padron_nd(codprov,codmunic,codentidad,codpoblamiento,n_hombres_a1,n_mujeres_a1,total_poblacion_a1,fecha_a1) values (?,?,?,?,?,?,?,?)");

						sql.setString(1,codprov);
						sql.setString(2,codmunic);
						sql.setString(3,codentidad);
						sql.setString(4,codpoblamiento);
						sql.setInt(5,n_hombres_a1);
						sql.setInt(6,n_mujeres_a1);
						sql.setInt(7,n_hombres_a1+n_mujeres_a1);
						sql.setInt(8,fecha_a1);

						registros_afectados=sql.executeUpdate();

						sql.close();

						contador_inserts++;
					}

					lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_nucl_encuest_1");

					// Actualizar tabla eiel_t_nucl_encuest_1
					// *****************************************************************
					sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_nucl_encuest_1 set padron=?,viviendas_total=?,pob_estacional=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?");

					sql.setInt(1,padron);
					sql.setInt(2,viviendas_total);
					sql.setInt(3,pob_estacional);
					sql.setString(4,codprov);
					sql.setString(5,codmunic);
					sql.setString(6,codentidad);
					sql.setString(7,codpoblamiento);

					registros_afectados=sql.executeUpdate();

					sql.close();

					if (registros_afectados>0) contador_updates++;

					if (FORZAR_INSERCION&&registros_afectados<=0)
					{

						sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_nucl_encuest_1(codprov,codmunic,codentidad,codpoblamiento,padron,viviendas_total,pob_estacional) values (?,?,?,?,?,?,?)");

						sql.setString(1,codprov);
						sql.setString(2,codmunic);
						sql.setString(3,codentidad);
						sql.setString(4,codpoblamiento);
						sql.setInt(5,padron);
						sql.setInt(6,viviendas_total);
						sql.setInt(7,pob_estacional);

						registros_afectados=sql.executeUpdate();

						sql.close();

						contador_inserts++;
					}

					lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_abast_serv");

					// Actualizar tabla eiel_t_abast_serv
					// *****************************************************************

					if (!existe_eiel_c_abast_rd)
					{
						sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_abast_serv set pobl_res_afect=?,pobl_est_afect=?,viv_deficitarias=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?");

						sql.setInt(1,padron);
						sql.setInt(2,pob_estacional);
						sql.setInt(3,viviendas_total);
						sql.setString(4,codprov);
						sql.setString(5,codmunic);
						sql.setString(6,codentidad);
						sql.setString(7,codpoblamiento);

						registros_afectados=sql.executeUpdate();

						sql.close();

						if (registros_afectados>0) contador_updates++;

						if (FORZAR_INSERCION&&registros_afectados<=0)
						{

							sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_abast_serv(codprov,codmunic,codentidad,codpoblamiento,pobl_res_afect,pobl_est_afect,viv_deficitarias) values (?,?,?,?,?,?,?)");

							sql.setString(1,codprov);
							sql.setString(2,codmunic);
							sql.setString(3,codentidad);
							sql.setString(4,codpoblamiento);
							sql.setInt(5,padron);
							sql.setInt(6,pob_estacional);
							sql.setInt(7,viviendas_total);

							registros_afectados=sql.executeUpdate();

							sql.close();

							contador_inserts++;
						}

					}
					else
					{
						sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_abast_serv set pobl_res_afect=?,pobl_est_afect=?,viv_deficitarias=?,viviendas_c_conex=?,consumo_inv=?,consumo_verano=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?");

						sql.setInt(1,0);
						sql.setInt(2,0);
						sql.setInt(3,0);
						sql.setInt(4,viviendas_total);
						sql.setInt(5,(padron*200)/1000);
						sql.setInt(6,(pob_estacional*250)/1000);
						sql.setString(4,codprov);
						sql.setString(5,codmunic);
						sql.setString(6,codentidad);
						sql.setString(7,codpoblamiento);

						registros_afectados=sql.executeUpdate();

						sql.close();

						if (registros_afectados>0) contador_updates++;

						if (FORZAR_INSERCION&&registros_afectados<=0)
						{

							sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_abast_serv(codprov,codmunic,codentidad,codpoblamiento,pobl_res_afect,pobl_est_afect,viv_deficitarias,viviendas_c_conex,consumo_inv,consumo_verano) values (?,?,?,?,?,?,?,?,?,?)");

							sql.setString(1,codprov);
							sql.setString(2,codmunic);
							sql.setString(3,codentidad);
							sql.setString(4,codpoblamiento);
							sql.setInt(5,0);
							sql.setInt(6,0);
							sql.setInt(7,0);
							sql.setInt(8,viviendas_total);
							sql.setInt(9,(padron*200)/1000);
							sql.setInt(10,(pob_estacional*250)/1000);

							registros_afectados=sql.executeUpdate();

							sql.close();

							contador_inserts++;
						}

					}

					lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_saneam_serv");

					// Actualizar tabla eiel_t_saneam_serv
					// *****************************************************************

					if (!existe_eiel_c_saneam_rs)
					{
						sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_saneam_serv set pobl_res_def_afect=?,pobl_est_def_afect=?,viviendas_def_conex=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?");

						sql.setInt(1,padron);
						sql.setInt(2,pob_estacional);
						sql.setInt(3,viviendas_total);
						sql.setString(4,codprov);
						sql.setString(5,codmunic);
						sql.setString(6,codentidad);
						sql.setString(7,codpoblamiento);

						registros_afectados=sql.executeUpdate();

						sql.close();

						if (registros_afectados>0) contador_updates++;

						if (FORZAR_INSERCION&&registros_afectados<=0)
						{

							sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_saneam_serv(codprov,codmunic,codentidad,codpoblamiento,pobl_res_def_afect,pobl_est_def_afect,viviendas_def_conex) values (?,?,?,?,?,?,?)");

							sql.setString(1,codprov);
							sql.setString(2,codmunic);
							sql.setString(3,codentidad);
							sql.setString(4,codpoblamiento);
							sql.setInt(5,padron);
							sql.setInt(6,pob_estacional);
							sql.setInt(7,viviendas_total);

							registros_afectados=sql.executeUpdate();

							sql.close();

							contador_inserts++;
						}

					}
					else
					{
						sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_saneam_serv set pobl_res_def_afect=?,pobl_est_def_afect=?,viviendas_def_conex=?,viviendas_c_conex=?,caudal_total_desaguado=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?");

						sql.setInt(1,0);
						sql.setInt(2,0);
						sql.setInt(3,0);
						sql.setInt(4,viviendas_total);
						sql.setInt(5,(int) ((((padron*200)/1000)*273+((pob_estacional*250)/1000)*92)*0.8));
						sql.setString(6,codprov);
						sql.setString(7,codmunic);
						sql.setString(8,codentidad);
						sql.setString(9,codpoblamiento);

						registros_afectados=sql.executeUpdate();

						sql.close();

						if (registros_afectados>0) contador_updates++;

						if (FORZAR_INSERCION&&registros_afectados<=0)
						{

							sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_saneam_serv(codprov,codmunic,codentidad,codpoblamiento,pobl_res_def_afect,pobl_est_def_afect,viviendas_def_conex,viviendas_c_conex,caudal_total_desaguado) values (?,?,?,?,?,?,?,?,?)");

							sql.setString(1,codprov);
							sql.setString(2,codmunic);
							sql.setString(3,codentidad);
							sql.setString(4,codpoblamiento);
							sql.setInt(5,0);
							sql.setInt(6,0);
							sql.setInt(7,0);
							sql.setInt(8,viviendas_total);
							sql.setInt(9,(int) ((((padron*200)/1000)*273+((pob_estacional*250)/1000)*92)*0.8));

							registros_afectados=sql.executeUpdate();

							sql.close();

							contador_inserts++;
						}

					}

					lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_abast_au");

					// Actualizar tabla eiel_t_abast_au
					// *****************************************************************

					sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_abast_au set aau_pob_re=?,aau_pob_es=?,aau_vivien=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?");

					if (!existe_eiel_c_abast_rd)
					{
						sql.setInt(1,padron);
						sql.setInt(2,pob_estacional);
						sql.setInt(3,viviendas_total);
					}
					else
					{
						sql.setInt(1,0);
						sql.setInt(2,0);
						sql.setInt(3,0);
					}

					sql.setString(4,codprov);
					sql.setString(5,codmunic);
					sql.setString(6,codentidad);
					sql.setString(7,codpoblamiento);

					registros_afectados=sql.executeUpdate();

					sql.close();

					if (registros_afectados>0) contador_updates++;

					if (FORZAR_INSERCION&&registros_afectados<=0)
					{

						sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_abast_au(codprov,codmunic,codentidad,codpoblamiento,aau_pob_re,aau_pob_es,aau_vivien,clave) values (?,?,?,?,?,?,?,?)");

						sql.setString(1,codprov);
						sql.setString(2,codmunic);
						sql.setString(3,codentidad);
						sql.setString(4,codpoblamiento);

						if (!existe_eiel_c_abast_rd)
						{
							sql.setInt(5,padron);
							sql.setInt(6,pob_estacional);
							sql.setInt(7,viviendas_total);
						}
						else
						{
							sql.setInt(5,0);
							sql.setInt(6,0);
							sql.setInt(7,0);
						}

						sql.setString(8,"AU");

						registros_afectados=sql.executeUpdate();

						sql.close();

						contador_inserts++;
					}

					lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_saneam_au");

					// Actualizar tabla iel_t_saneam_au
					// *****************************************************************

					sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_saneam_au set sau_pob_re=?,sau_pob_es=?,sau_vivien=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?");

					if (!existe_eiel_c_saneam_rs)
					{
						sql.setInt(1,padron);
						sql.setInt(2,pob_estacional);
						sql.setInt(3,viviendas_total);
					}
					else
					{
						sql.setInt(1,0);
						sql.setInt(2,0);
						sql.setInt(3,0);
					}

					sql.setString(4,codprov);
					sql.setString(5,codmunic);
					sql.setString(6,codentidad);
					sql.setString(7,codpoblamiento);

					registros_afectados=sql.executeUpdate();

					sql.close();

					if (registros_afectados>0) contador_updates++;

					if (FORZAR_INSERCION&&registros_afectados<=0)
					{

						sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_saneam_au(codprov,codmunic,codentidad,codpoblamiento,sau_pob_re,sau_pob_es,sau_vivien,clave) values (?,?,?,?,?,?,?,?)");

						sql.setString(1,codprov);
						sql.setString(2,codmunic);
						sql.setString(3,codentidad);
						sql.setString(4,codpoblamiento);

						if (!existe_eiel_c_abast_rd)
						{
							sql.setInt(5,padron);
							sql.setInt(6,pob_estacional);
							sql.setInt(7,viviendas_total);
						}
						else
						{
							sql.setInt(5,0);
							sql.setInt(6,0);
							sql.setInt(7,0);
						}

						sql.setString(8,"AU");

						registros_afectados=sql.executeUpdate();

						sql.close();

						contador_inserts++;
					}

				}


				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_mun_diseminados");

				// Actualizar tabla eiel_t_mun_diseminados
				// *****************************************************************

				PreparedStatement sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_mun_diseminados set padron_dis=(select sum(padron) from "+PREFIJO_TABLAS_TEST+"eiel_t_nucl_encuest_1 where codprov=? and codmunic=?),pob_estaci=(select sum(viviendas_total) from "+PREFIJO_TABLAS_TEST+"eiel_t_nucl_encuest_1 where codprov=? and codmunic=?), viv_total=(select sum(pob_estacional) from "+PREFIJO_TABLAS_TEST+"eiel_t_nucl_encuest_1 where codprov=? and codmunic=?) where codprov=? and codmunic=?");

				sql.setString(1,codprov);
				sql.setString(2,codmunic);
				sql.setString(3,codprov);
				sql.setString(4,codmunic);
				sql.setString(5,codprov);
				sql.setString(6,codmunic);
				sql.setString(7,codprov);
				sql.setString(8,codmunic);

				registros_afectados=sql.executeUpdate();

				if (registros_afectados>0) contador_updates++;

				if (FORZAR_INSERCION&&registros_afectados<=0)
				{
					// Hacer una inserción para tener el registro y luego volver a intentar el update
					PreparedStatement sql2=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_mun_diseminados(codprov,codmunic) values (?,?)");

					sql2.setString(1,codprov);
					sql2.setString(2,codmunic);

					registros_afectados=sql2.executeUpdate();
					
					sql2.close();

					contador_inserts++;
					
					sql.executeUpdate();
				}
				
				sql.close();

				
				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_padron_ttmm");

				// Actualizar tabla eiel_t_padron_ttmm
				// *****************************************************************

				sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_padron_ttmm set n_hombres_a1=(select sum(n_hombres_a1) from "+PREFIJO_TABLAS_TEST+"eiel_t_padron_nd where codprov=? and codmunic=? and fecha_a1=?),n_mujeres_a1=(select sum(n_mujeres_a1) from "+PREFIJO_TABLAS_TEST+"eiel_t_padron_nd where codprov=? and codmunic=? and fecha_a1=?), total_poblacion_a1=(select sum(total_poblacion_a1) from "+PREFIJO_TABLAS_TEST+"eiel_t_padron_nd where codprov=? and codmunic=? and fecha_a1=?) where codprov=? and codmunic=? and fecha_a1=?");

				sql.setString(1,codprov);
				sql.setString(2,codmunic);
				sql.setInt(3,anio);
				sql.setString(4,codprov);
				sql.setString(5,codmunic);
				sql.setInt(6,anio);
				sql.setString(7,codprov);
				sql.setString(8,codmunic);
				sql.setInt(9,anio);				
				sql.setString(10,codprov);
				sql.setString(11,codmunic);
				sql.setInt(12,anio);

				registros_afectados=sql.executeUpdate();

				if (registros_afectados>0) contador_updates++;

				if (FORZAR_INSERCION&&registros_afectados<=0)
				{
					// Hacer una inserción para tener el registro y luego volver a intentar el update
					PreparedStatement sql2=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_padron_ttmm(codprov,codmunic,fecha_a1) values (?,?,?)");

					sql2.setString(1,codprov);
					sql2.setString(2,codmunic);
					sql2.setInt(3,anio);

					registros_afectados=sql2.executeUpdate();
					
					sql2.close();

					contador_inserts++;
					
					sql.executeUpdate();
				}
				
				sql.close();
				
				
				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.operacionFinalizada")+" "+contador_updates+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizaciones")+" "+contador_inserts+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.inserciones")+" ("+total+")");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();

				progressImportacion.setVisible(false);
				lblProgreso.setVisible(false);
				btnImportar.setVisible(false);

				lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorInterno")+": "+ex.getMessage()+"</html>");
				lblMensaje.setVisible(true);
			}

			btnImportar.setVisible(true);
			btnCancelar.setVisible(true);

			return appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.operacionFinalizada")+" "+contador_updates+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizaciones")+" "+contador_inserts+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.inserciones")+" ("+total+")";
		}



		@Override
		public void done()
		{
			try
			{
				lblProgreso.setText(get());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	class ConectarConEducacion extends SwingWorker<String, Void>
	{

		@Override
		protected String doInBackground() throws Exception
		{
			progressImportacion.setVisible(true);
			lblProgreso.setVisible(true);

			lblMensaje.setText("");
			lblMensaje.setVisible(false);

			int contador_updates=0;
			int contador_inserts=0;
			int total=0;

			try
			{
				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.conectarServicioWeb"));

				WSOtrosOrganismosImplServiceLocator wsOtrosOrganismosServiceLocator=new WSOtrosOrganismosImplServiceLocator();

				wsOtrosOrganismosServiceLocator.setWSOtrosOrganismosImplPortEndpointAddress(URL_WS_EDUCACION);

				WSOtrosOrganismos otrosOrganismos=wsOtrosOrganismosServiceLocator.getWSOtrosOrganismosImplPort();

				String resultadoWS="";

				try
				{
					resultadoWS=otrosOrganismos.obtenerInformacionEducacion(idProvincia,idMunicipio,anio);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();

					progressImportacion.setVisible(false);
					lblProgreso.setVisible(false);
					btnImportar.setVisible(false);

					lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorConectando")+" "+URL_WS_EDUCACION+"</html>");
					lblMensaje.setVisible(true);

					return "error";
				}

				System.out.println(resultadoWS);

				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.procesandoRespuestaXML"));

				DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
				DocumentBuilder builder=builderFactory.newDocumentBuilder();

				InputStream is=new ByteArrayInputStream(resultadoWS.getBytes("UTF-8"));
				Document xml=(Document) builder.parse(is);

				xml.getDocumentElement().normalize();

				NodeList nodos=xml.getElementsByTagName("resultado");
				Element raiz=(Element) nodos.item(0);

				String codprov=raiz.getAttribute("codprov");
				String codmunic=raiz.getAttribute("codmunic");

				NodeList nodosDatos=raiz.getElementsByTagName("dato");

				GEOPISTAConnection conexion;

				conexion=(GEOPISTAConnection) appContext.getConnection();

				int registros_afectados;

				total=nodosDatos.getLength();

				Element estado=(Element)raiz.getElementsByTagName("estado").item(0);
				if (!estado.getAttribute("nivel").equalsIgnoreCase("exito"))
				{
					btnImportar.setVisible(true);
					btnCancelar.setVisible(true);
					return(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorServicioRemoto")+": "+filterString(estado.getNodeValue()));
				}
				
				for (int i=0; i<total; i++)
				{

					int progress=(int) ((i+1)*100/nodosDatos.getLength());

					setProgress(progress);

					Element element=(Element) nodosDatos.item(i);

					String codentidad=element.getAttribute("codentidad");
					String codpoblamiento=element.getAttribute("codpoblamiento");
					String clave=element.getAttribute("clave");
					String orden_en=element.getAttribute("orden_en");

					String nombre=getXMLKeyValue(element,"nombre").toString();
					String ambito=getXMLKeyValue(element,"ambito").toString();
					String titular=getXMLKeyValue(element,"titular").toString();
					int s_cubierta=Integer.parseInt(getXMLKeyValue(element,"s_cubierta"));
					int s_aire=Integer.parseInt(getXMLKeyValue(element,"s_aire"));
					int s_solar=Integer.parseInt(getXMLKeyValue(element,"s_solar"));
					String acceso_s_ruedas=getXMLKeyValue(element,"acceso_s_ruedas").toString();

					lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_en");

					// Actualizar tabla eiel_t_en
					// *****************************************************************
					PreparedStatement sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_en set nombre=?,ambito=?,titular=?,s_cubierta=?,s_aire=?,s_solar=?,acceso_s_ruedas=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and clave=? and orden_en=?");

					sql.setString(1,nombre);
					sql.setString(2,ambito);
					sql.setString(3,titular);
					sql.setInt(4,s_cubierta);
					sql.setInt(5,s_aire);
					sql.setInt(6,s_solar);
					sql.setString(7,acceso_s_ruedas);
					sql.setString(8,codprov);
					sql.setString(9,codmunic);
					sql.setString(10,codentidad);
					sql.setString(11,codpoblamiento);
					sql.setString(12,clave);
					sql.setString(13,orden_en);

					registros_afectados=sql.executeUpdate();

					sql.close();

					if (registros_afectados>0) contador_updates++;

					if (FORZAR_INSERCION&&registros_afectados<=0)
					{

						sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_en(codprov,codmunic,codentidad,codpoblamiento,clave,orden_en,nombre,ambito,titular,s_cubierta,s_aire,s_solar,acceso_s_ruedas) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");

						sql.setString(1,codprov);
						sql.setString(2,codmunic);
						sql.setString(3,codentidad);
						sql.setString(4,codpoblamiento);
						sql.setString(5,clave);
						sql.setString(6,orden_en);
						sql.setString(7,nombre);
						sql.setString(8,ambito);
						sql.setString(9,titular);
						sql.setInt(10,s_cubierta);
						sql.setInt(11,s_aire);
						sql.setInt(12,s_solar);
						sql.setString(13,acceso_s_ruedas);

						registros_afectados=sql.executeUpdate();

						sql.close();

						contador_inserts++;
					}

					lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_en_nivel");

					NodeList nodosNiveles=element.getElementsByTagName("nivel");

					for (int j=0; j<nodosNiveles.getLength(); j++)
					{
						Element elementNivel=(Element) nodosNiveles.item(j);

						String nivel=elementNivel.getAttribute("codigo");

						int unidades=Integer.parseInt(getXMLKeyValue(element,"unidades"));
						int plazas=Integer.parseInt(getXMLKeyValue(element,"plazas"));
						int alumnos=Integer.parseInt(getXMLKeyValue(element,"alumnos"));
						String fecha_curso=getXMLKeyValue(element,"fecha_curso").toString();
						if (fecha_curso.length()<=4) fecha_curso="01-01-"+fecha_curso;

						SimpleDateFormat formatoFecha=new SimpleDateFormat("dd-MM-yyyy");
						Date fecha=formatoFecha.parse(fecha_curso);

						sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_en_nivel set nivel=?,unidades=?,plazas=?,alumnos=?,fecha_curso=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and clave=? and orden_en=?");

						sql.setString(1,nivel);
						sql.setInt(2,unidades);
						sql.setInt(3,plazas);
						sql.setInt(4,alumnos);
						sql.setDate(5,new java.sql.Date(fecha.getTime()));
						sql.setString(6,codprov);
						sql.setString(7,codmunic);
						sql.setString(8,codentidad);
						sql.setString(9,codpoblamiento);
						sql.setString(10,clave);
						sql.setString(11,orden_en);

						registros_afectados=sql.executeUpdate();

						sql.close();

						if (registros_afectados>0) contador_updates++;

						if (FORZAR_INSERCION&&registros_afectados<=0)
						{
							sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_en_nivel(codprov,codmunic,codentidad,codpoblamiento,clave,orden_en,nivel,unidades,plazas,alumnos,fecha_curso) values (?,?,?,?,?,?,?,?,?,?,?)");

							sql.setString(1,codprov);
							sql.setString(2,codmunic);
							sql.setString(3,codentidad);
							sql.setString(4,codpoblamiento);
							sql.setString(5,clave);
							sql.setString(6,orden_en);
							sql.setString(7,nivel);
							sql.setInt(8,unidades);
							sql.setInt(9,plazas);
							sql.setInt(10,alumnos);
							sql.setDate(11,new java.sql.Date(fecha.getTime()));

							registros_afectados=sql.executeUpdate();

							sql.close();

							contador_inserts++;
						}

					}

				}

				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.operacionFinalizada")+" "+contador_updates+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizaciones")+" "+contador_inserts+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.inserciones")+" ("+total+")");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();

				progressImportacion.setVisible(false);
				lblProgreso.setVisible(false);
				btnImportar.setVisible(false);

				lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorInterno")+": "+ex.getMessage()+"</html>");
				lblMensaje.setVisible(true);
			}

			btnImportar.setVisible(true);
			btnCancelar.setVisible(true);

			return appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.operacionFinalizada")+" "+contador_updates+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizaciones")+" "+contador_inserts+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.inserciones")+" ("+total+")";
		}



		@Override
		public void done()
		{
			try
			{
				lblProgreso.setText(get());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	class ConectarConTurismo extends SwingWorker<String, Void>
	{

		@Override
		protected String doInBackground() throws Exception
		{
			progressImportacion.setVisible(true);
			lblProgreso.setVisible(true);

			lblMensaje.setText("");
			lblMensaje.setVisible(false);

			int contador_updates=0;
			int contador_inserts=0;
			int total=0;

			try
			{
				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.conectarServicioWeb"));

				WSOtrosOrganismosImplServiceLocator wsOtrosOrganismosServiceLocator=new WSOtrosOrganismosImplServiceLocator();

				wsOtrosOrganismosServiceLocator.setWSOtrosOrganismosImplPortEndpointAddress(URL_WS_TURISMO);

				WSOtrosOrganismos otrosOrganismos=wsOtrosOrganismosServiceLocator.getWSOtrosOrganismosImplPort();

				String resultadoWS="";

				try
				{
					resultadoWS=otrosOrganismos.obtenerInformacionTurismo(idProvincia,idMunicipio,anio);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();

					progressImportacion.setVisible(false);
					lblProgreso.setVisible(false);
					btnImportar.setVisible(false);

					lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorConectando")+" "+URL_WS_TURISMO+"</html>");
					lblMensaje.setVisible(true);

					return "error";
				}

				System.out.println(resultadoWS);

				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.procesandoRespuestaXML"));

				DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
				DocumentBuilder builder=builderFactory.newDocumentBuilder();

				InputStream is=new ByteArrayInputStream(resultadoWS.getBytes("UTF-8"));
				Document xml=(Document) builder.parse(is);

				xml.getDocumentElement().normalize();

				NodeList nodos=xml.getElementsByTagName("resultado");
				Element raiz=(Element) nodos.item(0);

				String codprov=raiz.getAttribute("codprov");
				String codmunic=raiz.getAttribute("codmunic");

				NodeList nodosDatos=raiz.getElementsByTagName("dato");

				GEOPISTAConnection conexion;

				conexion=(GEOPISTAConnection) appContext.getConnection();

				int registros_afectados;

				total=nodosDatos.getLength();

				Element estado=(Element)raiz.getElementsByTagName("estado").item(0);
				if (!estado.getAttribute("nivel").equalsIgnoreCase("exito"))
				{
					btnImportar.setVisible(true);
					btnCancelar.setVisible(true);
					return(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorServicioRemoto")+": "+filterString(estado.getNodeValue()));
				}
				
				for (int i=0; i<total; i++)
				{

					int progress=(int) ((i+1)*100/nodosDatos.getLength());

					setProgress(progress);

					Element element=(Element) nodosDatos.item(i);

					String codentidad=element.getAttribute("codentidad");
					String codpoblamiento=element.getAttribute("codpoblamiento");

					int hoteles=Integer.parseInt(getXMLKeyValue(element,"hoteles"));
					int casas_rural=Integer.parseInt(getXMLKeyValue(element,"casas_rural"));

					lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_nucl_encuest_1");

					// Actualizar tabla eiel_t_nucl_encuest_1
					// *****************************************************************
					PreparedStatement sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_nucl_encuest_1 set hoteles=?, casas_rural=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?");

					sql.setInt(1,hoteles);
					sql.setInt(2,casas_rural);
					sql.setString(3,codprov);
					sql.setString(4,codmunic);
					sql.setString(5,codentidad);
					sql.setString(6,codpoblamiento);

					registros_afectados=sql.executeUpdate();

					sql.close();

					if (registros_afectados>0) contador_updates++;

					if (FORZAR_INSERCION&&registros_afectados<=0)
					{

						sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_nucl_encuest_1(codprov,codmunic,codentidad,codpoblamiento,hoteles,casas_rural) values (?,?,?,?,?,?)");

						sql.setString(1,codprov);
						sql.setString(2,codmunic);
						sql.setString(3,codentidad);
						sql.setString(4,codpoblamiento);
						sql.setInt(5,hoteles);
						sql.setInt(6,casas_rural);

						registros_afectados=sql.executeUpdate();

						sql.close();

						contador_inserts++;
					}

				}

				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.operacionFinalizada")+" "+contador_updates+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizaciones")+" "+contador_inserts+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.inserciones")+" ("+total+")");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();

				progressImportacion.setVisible(false);
				lblProgreso.setVisible(false);
				btnImportar.setVisible(false);

				lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorInterno")+": "+ex.getMessage()+"</html>");
				lblMensaje.setVisible(true);
			}

			btnImportar.setVisible(true);
			btnCancelar.setVisible(true);

			return appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.operacionFinalizada")+" "+contador_updates+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizaciones")+" "+contador_inserts+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.inserciones")+" ("+total+")";
		}



		@Override
		public void done()
		{
			try
			{
				lblProgreso.setText(get());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	class ConectarConSanidad extends SwingWorker<String, Void>
	{

		@Override
		protected String doInBackground() throws Exception
		{
			progressImportacion.setVisible(true);
			lblProgreso.setVisible(true);

			lblMensaje.setText("");
			lblMensaje.setVisible(false);

			int contador_updates=0;
			int contador_inserts=0;
			int total=0;

			try
			{
				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.conectarServicioWeb"));

				WSOtrosOrganismosImplServiceLocator wsOtrosOrganismosServiceLocator=new WSOtrosOrganismosImplServiceLocator();

				wsOtrosOrganismosServiceLocator.setWSOtrosOrganismosImplPortEndpointAddress(URL_WS_SANIDAD);

				WSOtrosOrganismos otrosOrganismos=wsOtrosOrganismosServiceLocator.getWSOtrosOrganismosImplPort();

				String resultadoWS="";

				try
				{
					resultadoWS=otrosOrganismos.obtenerInformacionCentroSanitario(idProvincia,idMunicipio,anio);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();

					progressImportacion.setVisible(false);
					lblProgreso.setVisible(false);
					btnImportar.setVisible(false);

					lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorConectando")+" "+URL_WS_SANIDAD+"</html>");
					lblMensaje.setVisible(true);

					return "error";
				}

				System.out.println(resultadoWS);

				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.procesandoRespuestaXML"));

				DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
				DocumentBuilder builder=builderFactory.newDocumentBuilder();

				InputStream is=new ByteArrayInputStream(resultadoWS.getBytes("UTF-8"));
				Document xml=(Document) builder.parse(is);

				xml.getDocumentElement().normalize();

				NodeList nodos=xml.getElementsByTagName("resultado");
				Element raiz=(Element) nodos.item(0);

				String codprov=raiz.getAttribute("codprov");
				String codmunic=raiz.getAttribute("codmunic");

				NodeList nodosDatos=raiz.getElementsByTagName("dato");

				GEOPISTAConnection conexion;

				conexion=(GEOPISTAConnection) appContext.getConnection();

				int registros_afectados;

				total=nodosDatos.getLength();

				Element estado=(Element)raiz.getElementsByTagName("estado").item(0);
				if (!estado.getAttribute("nivel").equalsIgnoreCase("exito"))
				{
					btnImportar.setVisible(true);
					btnCancelar.setVisible(true);
					return(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorServicioRemoto")+": "+filterString(estado.getNodeValue()));
				}

				for (int i=0; i<total; i++)
				{

					int progress=(int) ((i+1)*100/nodosDatos.getLength());

					setProgress(progress);

					Element element=(Element) nodosDatos.item(i);

					String codentidad=element.getAttribute("codentidad");
					String codpoblamiento=element.getAttribute("codpoblamiento");
					String clave=element.getAttribute("clave");
					String orden_sa=element.getAttribute("orden_sa");

					String nombre=getXMLKeyValue(element,"nombre").toString();
					String tipo=getXMLKeyValue(element,"tipo").toString();
					String titular=getXMLKeyValue(element,"titular").toString();
					String gestor=getXMLKeyValue(element,"gestor").toString();
					int s_cubierta=Integer.parseInt(getXMLKeyValue(element,"s_cubierta"));
					int s_aire=Integer.parseInt(getXMLKeyValue(element,"s_aire"));
					int s_solar=Integer.parseInt(getXMLKeyValue(element,"s_solar"));
					String uci=getXMLKeyValue(element,"uci").toString();
					int n_camas=Integer.parseInt(getXMLKeyValue(element,"n_camas"));
					String acceso_s_ruedas=getXMLKeyValue(element,"acceso_s_ruedas").toString();

					lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_sa");

					// Actualizar tabla eiel_t_sa
					// *****************************************************************
					PreparedStatement sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_sa set nombre=?,tipo=?,titular=?,gestor=?,s_cubierta=?,s_aire=?,s_solar=?,uci=?,n_camas=?,acceso_s_ruedas=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and clave=? and orden_sa=?");

					sql.setString(1,nombre);
					sql.setString(2,tipo);
					sql.setString(3,titular);
					sql.setString(4,gestor);
					sql.setInt(5,s_cubierta);
					sql.setInt(6,s_aire);
					sql.setInt(7,s_solar);
					sql.setString(8,uci);
					sql.setInt(9,n_camas);
					sql.setString(10,acceso_s_ruedas);
					sql.setString(11,codprov);
					sql.setString(12,codmunic);
					sql.setString(13,codentidad);
					sql.setString(14,codpoblamiento);
					sql.setString(15,clave);
					sql.setString(16,orden_sa);

					registros_afectados=sql.executeUpdate();

					sql.close();

					if (registros_afectados>0) contador_updates++;

					if (FORZAR_INSERCION&&registros_afectados<=0)
					{
						sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_sa(codprov,codmunic,codentidad,codpoblamiento,clave,orden_sa,nombre,tipo,titular,gestor,s_cubierta,s_aire,s_solar,uci,n_camas,acceso_s_ruedas) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

						sql.setString(1,codprov);
						sql.setString(2,codmunic);
						sql.setString(3,codentidad);
						sql.setString(4,codpoblamiento);
						sql.setString(5,clave);
						sql.setString(6,orden_sa);
						sql.setString(7,nombre);
						sql.setString(8,tipo);
						sql.setString(9,titular);
						sql.setString(10,gestor);
						sql.setInt(11,s_cubierta);
						sql.setInt(12,s_aire);
						sql.setInt(13,s_solar);
						sql.setString(14,uci);
						sql.setInt(15,n_camas);
						sql.setString(16,acceso_s_ruedas);

						registros_afectados=sql.executeUpdate();

						sql.close();

						contador_inserts++;
					}

				}

				try
				{
					resultadoWS=otrosOrganismos.obtenerInformacionCentroAsistencial(idProvincia,idMunicipio,anio);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();

					progressImportacion.setVisible(false);
					lblProgreso.setVisible(false);
					btnImportar.setVisible(false);

					lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorConectando")+" "+URL_WS_SANIDAD+"</html>");
					lblMensaje.setVisible(true);

					return "error";
				}

				System.out.println(resultadoWS);

				lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.procesandoRespuestaXML"));

				builderFactory=DocumentBuilderFactory.newInstance();
				builder=builderFactory.newDocumentBuilder();

				is=new ByteArrayInputStream(resultadoWS.getBytes("UTF-8"));
				xml=(Document) builder.parse(is);

				xml.getDocumentElement().normalize();

				nodos=xml.getElementsByTagName("resultado");
				raiz=(Element) nodos.item(0);

				codprov=raiz.getAttribute("codprov");
				codmunic=raiz.getAttribute("codmunic");

				nodosDatos=raiz.getElementsByTagName("dato");

				total+=nodosDatos.getLength();

				estado=(Element)raiz.getElementsByTagName("estado").item(0);
				if (!estado.getAttribute("nivel").equalsIgnoreCase("exito"))
				{
					btnImportar.setVisible(true);
					btnCancelar.setVisible(true);
					return(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorServicioRemoto")+": "+filterString(estado.getNodeValue()));
				}
				
				for (int i=0; i<nodosDatos.getLength(); i++)
				{

					int progress=(int) ((i+1)*100/nodosDatos.getLength());

					setProgress(progress);

					Element element=(Element) nodosDatos.item(i);

					String codentidad=element.getAttribute("codentidad");
					String codpoblamiento=element.getAttribute("codpoblamiento");
					String clave=element.getAttribute("clave");
					String orden_as=element.getAttribute("orden_as");

					String nombre=getXMLKeyValue(element,"nombre").toString();
					String tipo=getXMLKeyValue(element,"tipo").toString();
					String titular=getXMLKeyValue(element,"titular").toString();
					String gestor=getXMLKeyValue(element,"gestor").toString();
					int plazas=Integer.parseInt(getXMLKeyValue(element,"plazas"));
					int s_cubierta=Integer.parseInt(getXMLKeyValue(element,"s_cubierta"));
					int s_aire=Integer.parseInt(getXMLKeyValue(element,"s_aire"));
					int s_solar=Integer.parseInt(getXMLKeyValue(element,"s_solar"));
					String acceso_s_ruedas=getXMLKeyValue(element,"acceso_s_ruedas").toString();

					lblProgreso.setText(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizandoTabla")+" eiel_t_as");

					// Actualizar tabla eiel_t_as
					// *****************************************************************
					PreparedStatement sql=conexion.prepareStatement("update "+PREFIJO_TABLAS_TEST+"eiel_t_as set nombre=?,tipo=?,titular=?,gestor=?,plazas=?,s_cubierta=?,s_aire=?,s_solar=?,acceso_s_ruedas=? where codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and clave=? and orden_as=?");

					sql.setString(1,nombre);
					sql.setString(2,tipo);
					sql.setString(3,titular);
					sql.setString(4,gestor);
					sql.setInt(5,plazas);
					sql.setInt(6,s_cubierta);
					sql.setInt(7,s_aire);
					sql.setInt(8,s_solar);
					sql.setString(9,acceso_s_ruedas);
					sql.setString(10,codprov);
					sql.setString(11,codmunic);
					sql.setString(12,codentidad);
					sql.setString(13,codpoblamiento);
					sql.setString(14,clave);
					sql.setString(15,orden_as);

					registros_afectados=sql.executeUpdate();

					sql.close();

					if (registros_afectados>0) contador_updates++;

					if (FORZAR_INSERCION&&registros_afectados<=0)
					{
						sql=conexion.prepareStatement("insert into "+PREFIJO_TABLAS_TEST+"eiel_t_as(codprov,codmunic,codentidad,codpoblamiento,clave,orden_as,nombre,tipo,titular,gestor,plazas,s_cubierta,s_aire,s_solar,acceso_s_ruedas) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

						sql.setString(1,codprov);
						sql.setString(2,codmunic);
						sql.setString(3,codentidad);
						sql.setString(4,codpoblamiento);
						sql.setString(5,clave);
						sql.setString(6,orden_as);
						sql.setString(7,nombre);
						sql.setString(8,tipo);
						sql.setString(9,titular);
						sql.setString(10,gestor);
						sql.setInt(11,plazas);
						sql.setInt(12,s_cubierta);
						sql.setInt(13,s_aire);
						sql.setInt(14,s_solar);
						sql.setString(15,acceso_s_ruedas);

						registros_afectados=sql.executeUpdate();

						sql.close();

						contador_inserts++;
					}
				}

			}
			catch (Exception ex)
			{
				ex.printStackTrace();

				progressImportacion.setVisible(false);
				lblProgreso.setVisible(false);
				btnImportar.setVisible(false);

				lblMensaje.setText("<html>"+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.errorInterno")+": "+ex.getMessage()+"</html>");
				lblMensaje.setVisible(true);
			}

			btnImportar.setVisible(true);
			btnCancelar.setVisible(true);

			return appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.operacionFinalizada")+" "+contador_updates+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.actualizaciones")+" "+contador_inserts+" "+appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.inserciones")+" ("+total+")";
		}



		@Override
		public void done()
		{
			try
			{
				lblProgreso.setText(get());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}



	// Get XML key value
	// ***********************************************************
	private static String getXMLKeyValue(Element element, String key)
	{
		NodeList n1, n2;
		String s="";

		try
		{
			n1=element.getElementsByTagName(key);
			n2=((Element) n1.item(0)).getChildNodes();
			s=((Node) n2.item(0)).getNodeValue().trim();
		}
		catch (Exception ex)
		{
		}
		;

		return (filterString(s));
	}
	

	// Comprueba si existe un registro en una tabla
	// ***************************************************************
	public boolean existeRegistroEnTabla(GEOPISTAConnection conexion, String tabla, String codprov, String codmunic, String codentidad, String codpoblamiento) throws Exception
	{
		PreparedStatement sql=conexion.prepareStatement("select count(*) from "+tabla+" where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?");

		sql.setString(1,codprov);
		sql.setString(2,codmunic);
		sql.setString(3,codentidad);
		sql.setString(4,codpoblamiento);

		ResultSet rset=sql.executeQuery();

		while (rset.next())
		{
			if (rset.getInt(1)>0) return true;
		}

		rset.close();
		sql.close();

		return false;
	}



	// Filter string
	// ***************************************************************
	private static String filterString(Object obj)
	{
		if ((obj==null)||(obj.toString().length()<=0))
			return ("");
		else
			return (obj.toString());
	}
}