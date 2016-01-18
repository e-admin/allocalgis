package com.geopista.ui.plugin.infcatfisicoeconomico.dialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.AscWriterCatastro;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.TableLinderosModel;
import com.geopista.app.catastro.intercambio.edicion.utils.TablePlantasModel;
import com.geopista.app.catastro.intercambio.edicion.utils.TableUsosModel;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils_LCGIII;
import com.geopista.app.catastro.model.beans.ASCCatastro;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.EstructuraDB;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.LinderoCatastro;
import com.geopista.app.catastro.model.beans.PlantaCatastro;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.UsoCatastro;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistro;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.LayerFamily;
import com.geopista.ui.plugin.plantasignificativa.info.PlantaSignificativaInfo;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.java2xml.Java2XMLCatastro;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ASCPanel extends JPanel implements FeatureExtendedPanel {

	/**
	 * Auto generated serial version id. 
	 */
	private static final long serialVersionUID = -7321865513182439496L;

	static private ASCPanel _instance = null; 

	public HashMap infoplantas = new HashMap();


	public static ASCPanel get_instance() {
		return _instance;
	}

	public static void set_instance(ASCPanel _instance) {
		ASCPanel._instance = _instance;
	}

	private JPanel jPanelIdentificacion = null;
	private JPanel jPanelPlantas = null;
	private JLabel jLabelCodDelegacion = null;
	private JLabel jLabelNombreGerencia = null;
	private JLabel jLabelCodMunicipio = null;
	private JLabel jLabelNombreMunicipio = null;
	private JTextField jTextFieldCodDelegacion = null;
	private JTextField jTextFieldNombreGerencia = null;
	private JTextField jTextFieldCodMunicipio = null;
	private JTextField jTextFieldNombreMunicipio = null;
	private JLabel jLabelCodViaPublica = null;
	private JLabel jLabelSiglas = null;
	private JLabel jLabelNombreVia = null;
	private JLabel jLabelNumPolicia = null;
	private JLabel jLabelLetraDuplicado = null;
	private JTextField jTextFieldCodVia = null;
	private JTextField jTextFieldSiglas = null;
	private JTextField jTextFieldNombreVia = null;
	private JTextField jTextFieldNumPolicia = null;
	private JTextField jTextFieldLetraDuplicado = null;
	private JLabel jLabelParcela1 = null;
	private JTextField jTextFieldParcelaCatastral1 = null;
	private JTextField jTextFieldParcelaCatastral2 = null;
	private JLabel jLabelParcelaCatastral2 = null;
	private JScrollPane jScrollPaneLinderos = null;
	private JTable jTableLinderos = null;

	private TableLinderosModel tablelinderosmodel;
	private TableUsosModel tableusosmodel;
	private TablePlantasModel tableplantasmodel;

	private JLabel jLabelEscalaCaptura = null;
	private JTextField jTextFieldEscalaCaptura = null;
	private JLabel jLabelFechaCaptura = null;
	private JTextField jTextFieldFechaCaptura = null;
	private JScrollPane jScrollPaneListaPlantas = null;
	private JTable jTableListaPlantas = null;
	private JScrollPane jScrollPaneListaUsos = null;
	private JTable jTableListaUsos = null;

	private static FincaCatastro finca = null;

	private JLabel jLabelSuperficieParcela = null;
	private JLabel jLabelSuperficieSR = null;
	private JLabel jLabelSuperficieBR = null;
	private JLabel jLabelSuperficieConstruida = null;
	private JTextField jTextFieldSuperficieParcela = null;
	private JPanel jPanelPlantaGeneral = null;
	private JPanel jPanelPlantasSignificativas = null;
	private JTextField jTextFieldSuperficieSR = null;
	private JTextField jTextFieldSuperficieBR = null;
	private JTextField jTextFieldSuperficieConstruida = null;

	private ASCCatastro asc;

	/**
	 * @return the asc
	 */
	public ASCCatastro getAsc() {
		return asc;
	}

	/**
	 * @param asc the asc to set
	 */
	public void setAsc(ASCCatastro asc) {
		this.asc = asc;
	}

	public String textoAscTemp = null;
	private FxccPanel fxccPanel;

	private GestionExpedientePanel gestionExpedientePanel = null;

	/**
	 * This method initializes
	 * 
	 */
	public ASCPanel() {
		super();
		asc = new ASCCatastro();
		initialize();
		EdicionUtils.editablePanel(this, true);
		ASCPanel.set_instance(this);
	}

	public ASCPanel(GestionExpedientePanel gestionExpedientePanel) {
		super();
		asc = new ASCCatastro();
		this.gestionExpedientePanel = gestionExpedientePanel;
		initialize();
		EdicionUtils.editablePanel(this, true);
		ASCPanel.set_instance(this);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setLayout(new GridBagLayout());
		// this.setSize(new java.awt.Dimension(734,376));
		this.setSize(new java.awt.Dimension(800, 575));
		this.add(getJPanelIdentificacion(), new GridBagConstraints(
				0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJPanelPlantas(), new GridBagConstraints(
				0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
	 * This method initializes jPanelIdentificacion
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelIdentificacion() {
		if (jPanelIdentificacion == null) {
			jLabelFechaCaptura = new JLabel();
			jLabelFechaCaptura.setText("Fecha de captura");

			jLabelEscalaCaptura = new JLabel();
			jLabelEscalaCaptura.setText("Denominador de la Escala de captura");

			jLabelParcelaCatastral2 = new JLabel();
			jLabelParcelaCatastral2.setText("Parcela Catastral 2");

			jLabelParcela1 = new JLabel();
			jLabelParcela1.setText("Parcela Catastral 1");

			jLabelLetraDuplicado = new JLabel();
			jLabelLetraDuplicado.setText("Letra duplicado");

			jLabelNumPolicia = new JLabel();
			jLabelNumPolicia.setText("Num. Policía");

			jLabelNombreVia = new JLabel();
			jLabelNombreVia.setText("Nombre de la vía pública");

			jLabelSiglas = new JLabel();
			jLabelSiglas.setText("Siglas");

			jLabelCodViaPublica = new JLabel();
			jLabelCodViaPublica.setText("Código via DGC");
			jLabelNombreMunicipio = new JLabel();
			jLabelNombreMunicipio.setText("Nombre del municipio");
			jLabelCodMunicipio = new JLabel();
			jLabelCodMunicipio.setText("Código municipio");
			jLabelNombreGerencia = new JLabel();
			jLabelNombreGerencia.setText("Nombre de la Gerencia");
			jLabelCodDelegacion = new JLabel();
			jLabelCodDelegacion.setText("Código delegación");
			jPanelIdentificacion = new JPanel(new GridBagLayout());
			jPanelIdentificacion.setSize(new Dimension(400, 250));
			jPanelIdentificacion.setPreferredSize(new Dimension(400, 250));
			jPanelIdentificacion.setMaximumSize(jPanelIdentificacion
					.getPreferredSize());
			jPanelIdentificacion.setMinimumSize(jPanelIdentificacion
					.getPreferredSize());

			jPanelIdentificacion.setBorder(BorderFactory.createTitledBorder(
					null, I18N.get("InfCatastralFisicoEconomicoPlugIn",
					"infCatastralFisicoEconomico.asc.panel.datosidentificacion.titulo"),
					TitledBorder.LEADING, TitledBorder.TOP, new Font(null,
							Font.BOLD, 12)));
			jPanelIdentificacion.add(jLabelCodDelegacion,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(jLabelNombreGerencia,
					new GridBagConstraints(1, 0, 2, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(jLabelCodMunicipio,
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(jLabelNombreMunicipio,
					new GridBagConstraints(4, 0, 3, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));

			jPanelIdentificacion.add(getJTextFieldCodDelegacion(),
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(getJTextFieldNombreGerencia(),
					new GridBagConstraints(1, 1, 2, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(getJTextFieldCodMunicipio(),
					new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(getJTextFieldNombreMunicipio(),
					new GridBagConstraints(4, 1, 3, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));

			jPanelIdentificacion.add(jLabelCodViaPublica,
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(jLabelSiglas,
					new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(jLabelNombreVia,
					new GridBagConstraints(2, 3, 2, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(jLabelNumPolicia,
					new GridBagConstraints(5, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(jLabelLetraDuplicado,
					new GridBagConstraints(6, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(getJTextFieldCodVia(),
					new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(getJTextFieldSiglas(),
					new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(getJTextFieldNombreVia(),
					new GridBagConstraints(2, 4, 2, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(getJTextFieldNumPolicia(),
					new GridBagConstraints(5, 4, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(getJTextFieldLetraDuplicado(),
					new GridBagConstraints(6, 4, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));

			jPanelIdentificacion.add(jLabelParcela1,
					new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(getJTextFieldParcelaCatastral1(),
					new GridBagConstraints(1, 5, 2, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(jLabelParcelaCatastral2,
					new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(getJTextFieldParcelaCatastral2(),
					new GridBagConstraints(4, 5, 3, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));

			jPanelIdentificacion.add(getJScrollPaneLinderos(),
					new GridBagConstraints(0, 6, 7, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 50));

			jPanelIdentificacion.add(jLabelEscalaCaptura,
					new GridBagConstraints(0, 7, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(getJTextFieldEscalaCaptura(),
					new GridBagConstraints(1, 7, 2, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(jLabelFechaCaptura,
					new GridBagConstraints(3, 7, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelIdentificacion.add(getJTextFieldFechaCaptura(),
					new GridBagConstraints(4, 7, 3, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));

		}
		return jPanelIdentificacion;
	}

	/**
	 * This method initializes jPanelPlantas
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelPlantas() {
		if (jPanelPlantas == null) {

			jLabelSuperficieConstruida = new JLabel();
			jLabelSuperficieConstruida.setText("Construida");

			jLabelSuperficieBR = new JLabel();
			jLabelSuperficieBR.setText("Bajo rasante");

			jLabelSuperficieSR = new JLabel();
			jLabelSuperficieSR.setText("Sobre rasante");

			jLabelSuperficieParcela = new JLabel();
			jLabelSuperficieParcela.setText("Superficie Parcela");

			jPanelPlantas = new JPanel(new GridBagLayout());
			jPanelPlantas.setSize(new Dimension(450, 230));
			jPanelPlantas.setPreferredSize(new Dimension(450, 230));
			jPanelPlantas.setMaximumSize(jPanelPlantas.getPreferredSize());
			jPanelPlantas.setMinimumSize(jPanelPlantas.getPreferredSize());

			jPanelPlantas.setBorder(BorderFactory.createTitledBorder(null, I18N.get("InfCatastralFisicoEconomicoPlugIn",
			"infCatastralFisicoEconomico.asc.panel.datosplantas.titulo"),
					TitledBorder.LEADING, TitledBorder.TOP, new Font(null,
							Font.BOLD, 12)));

			jPanelPlantas.add(getJPanelPlantaGeneral(),
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));

			jPanelPlantas.add(getJPanelPlantasSignificativas(),
					new GridBagConstraints(0, 1, 1, 1, 0.8, 0.8,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(0, 5, 0, 5), 0, 0));

		}
		jPanelPlantas.updateUI();
		return jPanelPlantas;
	}

	/**
	 * This method initializes jTextFieldCodDelegacion
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldCodDelegacion() {
		if (jTextFieldCodDelegacion == null) {
			jTextFieldCodDelegacion = new JTextField(2);
			
			jTextFieldCodDelegacion.addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	
			    	try{
			    		if (!jTextFieldCodDelegacion.getText().equals("")){
			    			int cod = Integer.parseInt(jTextFieldCodDelegacion.getText());
			    			asc.setCodDelegacion(String.valueOf(cod));
			    		} else{
				    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
		    				"El codigo de la delegación no puede estar vacío.");
			    		}
			    	} catch (NumberFormatException ex) {
						// TODO: handle exception
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    			"El codigo de la delegación debe ser un número.");
					}
			    }
			  });
		}
		return jTextFieldCodDelegacion;
	}

	/**
	 * This method initializes jTextFieldNombreGerencia
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldNombreGerencia() {
		if (jTextFieldNombreGerencia == null) {
			jTextFieldNombreGerencia = new JTextField(20);
			
			jTextFieldNombreGerencia.addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	if (!jTextFieldNombreGerencia.getText().equals("")){
			    		asc.setNombreGerencia(jTextFieldNombreGerencia.getText());
			    	} else{
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    				"El nombre de la gerencia no puede estar vacío.");
			    	}
			    }
			  });
		}
		return jTextFieldNombreGerencia;
	}

	/**
	 * This method initializes jTextFieldCodMunicipio
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldCodMunicipio() {
		if (jTextFieldCodMunicipio == null) {
			jTextFieldCodMunicipio = new JTextField(3);

			jTextFieldCodMunicipio.addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	
			    	try{
			    		if (!jTextFieldCodMunicipio.getText().equals("")){
			    			int cod = Integer.parseInt(jTextFieldCodMunicipio.getText());
			    			asc.setCodMunicipio(String.valueOf(cod));
			    		} else{
				    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
		    				"El codigo del municipio no puede estar vacío.");
			    		}
			    	} catch (NumberFormatException ex) {
						// TODO: handle exception
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    			"El codigo del municipio debe ser un número.");
					}
			    }
			  });
		}
		return jTextFieldCodMunicipio;
	}

	/**
	 * This method initializes jTextFieldNombreMunicipio
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldNombreMunicipio() {
		if (jTextFieldNombreMunicipio == null) {
			jTextFieldNombreMunicipio = new JTextField(30);
			
			jTextFieldNombreMunicipio.addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	
			    	if (!jTextFieldNombreMunicipio.getText().equals("")){
			    		asc.setNombreMunicipio(jTextFieldNombreMunicipio.getText());
			    	} else{
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    				"El nombre del municipio no puede estar vacío.");
			    	}

			    }
			  });
			
		}
		return jTextFieldNombreMunicipio;
	}

	/**
	 * This method initializes jTextFieldCodVia
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldCodVia() {
		if (jTextFieldCodVia == null) {
			jTextFieldCodVia = new JTextField(5);
			
			jTextFieldCodVia.addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	
			    	try{
			    			int cod = Integer.parseInt(jTextFieldCodVia.getText());
			    			asc.setCodVia(String.valueOf(cod));
			    	} catch (NumberFormatException ex) {
						// TODO: handle exception
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    				"El codigo de la via debe ser un número.");
					}
			    }
			  });
		}
		return jTextFieldCodVia;
	}

	/**
	 * This method initializes jTextFieldSiglas
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldSiglas() {
		if (jTextFieldSiglas == null) {
			jTextFieldSiglas = new JTextField(2);
			
			jTextFieldSiglas.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					asc.setSigla(jTextFieldSiglas.getText());
				}
			});
			
		}
		return jTextFieldSiglas;
	}

	/**
	 * This method initializes jTextFieldNombreVia
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldNombreVia() {
		if (jTextFieldNombreVia == null) {
			jTextFieldNombreVia = new JTextField(25);
			
			jTextFieldNombreVia.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					asc.setNomVia(jTextFieldNombreVia.getText());
				}
			});
			
		}
		return jTextFieldNombreVia;
	}

	/**
	 * This method initializes jTextFieldNumPolicia
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldNumPolicia() {
		if (jTextFieldNumPolicia == null) {
			jTextFieldNumPolicia = new JTextField(4);
			
			jTextFieldNumPolicia.addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	
			    	try{

			    			int cod = Integer.parseInt(jTextFieldNumPolicia.getText());
			    			asc.setNumPolicia(String.valueOf(cod));
	
			    	} catch (NumberFormatException ex) {
						// TODO: handle exception
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    				"El numero de policía debe ser un número.");
					}
			    }
			  });
			
		}
		return jTextFieldNumPolicia;
	}

	/**
	 * This method initializes jTextFieldLetraDuplicado
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldLetraDuplicado() {
		if (jTextFieldLetraDuplicado == null) {
			jTextFieldLetraDuplicado = new JTextField(1);
			
			jTextFieldLetraDuplicado.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					asc.setLetraDuplicado(jTextFieldLetraDuplicado.getText());
				}
			});
			
		}
		return jTextFieldLetraDuplicado;
	}

	/**
	 * This method initializes jTextFieldParcelaCatastral1
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldParcelaCatastral1() {
		if (jTextFieldParcelaCatastral1 == null) {
			jTextFieldParcelaCatastral1 = new JTextField(7);
			
			jTextFieldParcelaCatastral1.addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	if (!jTextFieldParcelaCatastral1.getText().equals("")){
			    		asc.getRefCatastral().setRefCatastral1(jTextFieldParcelaCatastral1.getText());
			    	} else{
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    				"La referencia catastral 1 no puede estar vacía.");
			    	}
			    }
			  });
			
		}
		return jTextFieldParcelaCatastral1;
	}

	/**
	 * This method initializes jTextFieldParcelaCatastral2
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldParcelaCatastral2() {
		if (jTextFieldParcelaCatastral2 == null) {
			jTextFieldParcelaCatastral2 = new JTextField(7);
			
			jTextFieldParcelaCatastral2.addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	if (!jTextFieldParcelaCatastral2.getText().equals("")){
			    		asc.getRefCatastral().setRefCatastral2(jTextFieldParcelaCatastral2.getText());
			    	} else{
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    				"La referencia catastral 2 no puede estar vacía.");
			    	}
			    }
			  });
			
		}
		return jTextFieldParcelaCatastral2;
	}


	/**
	 * This method initializes jScrollPaneListaUsos
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneLinderos() {
		if (jScrollPaneLinderos == null) {
			jScrollPaneLinderos = new JScrollPane();
			jScrollPaneLinderos.setViewportView(getJTableLinderos());
			jScrollPaneLinderos.setBorder(BorderFactory.createTitledBorder(I18N
					.get("InfCatastralFisicoEconomicoPlugIn",
					"infCatastralFisicoEconomico.asc.panel.datosidentificacion.listalinderos")));
			
		}
		return jScrollPaneLinderos;
	}

	/**
	 * This method initializes jTableLinderos
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getJTableLinderos() {
		if (jTableLinderos == null) {
			jTableLinderos = new JTable();
			tablelinderosmodel = new TableLinderosModel();

			TableSorted tblSorted = new TableSorted(tablelinderosmodel);
			tblSorted.setTableHeader(jTableLinderos.getTableHeader());
			jTableLinderos.setModel(tblSorted);
			jTableLinderos
			.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableLinderos.setCellSelectionEnabled(false);
			jTableLinderos.setColumnSelectionAllowed(false);
			jTableLinderos.setRowSelectionAllowed(true);
			jTableLinderos.getTableHeader().setReorderingAllowed(true);

			((TableLinderosModel) ((TableSorted) jTableLinderos.getModel())
					.getTableModel()).addTableModelListener(jTableLinderos);

		}
		return jTableLinderos;
	}

	/**
	 * This method initializes jTextFieldEscalaCaptura
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldEscalaCaptura() {
		if (jTextFieldEscalaCaptura == null) {
			jTextFieldEscalaCaptura = new JTextField();

			jTextFieldEscalaCaptura.addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	
			    	try{
			    		if (!jTextFieldEscalaCaptura.getText().equals("")){
			    			int cod = Integer.parseInt(jTextFieldEscalaCaptura.getText());
			    			asc.setEscalaCaptura(String.valueOf(cod));
			    		} else{
				    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
		    				"La escala de la captura no puede estar vacío.");
			    		}
			    	} catch (NumberFormatException ex) {
						// TODO: handle exception
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    			"La escala de la captura debe ser un número.");
					}
			    }
			  });

		}
		return jTextFieldEscalaCaptura;
	}

	/**
	 * This method initializes jTextFieldFechaCaptura
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldFechaCaptura() {
		if (jTextFieldFechaCaptura == null) {
			jTextFieldFechaCaptura = new JTextField(8);
			
			jTextFieldFechaCaptura.addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	if (!jTextFieldFechaCaptura.getText().equals("")){
			    		asc.setFechaCaptura(jTextFieldFechaCaptura.getText());
			    	} else{
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    				"la fecha de la captura no puede estar vacía.");
			    	}
			    }
			  });
			
		}
		return jTextFieldFechaCaptura;
	}

	/**
	 * This method initializes jScrollPaneListaPlantas
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneListaPlantas() {
		if (jScrollPaneListaPlantas == null) {
			jScrollPaneListaPlantas = new JScrollPane();
			jScrollPaneListaPlantas.setBorder(BorderFactory
					.createTitledBorder(I18N.get("InfCatastralFisicoEconomicoPlugIn",
					"infCatastralFisicoEconomico.asc.panel.datosplantas.listaplantas")));
			jScrollPaneListaPlantas.setViewportView(getJTableListaPlantas());

		}
		jScrollPaneListaPlantas.updateUI();
		return jScrollPaneListaPlantas;
	}

	/**
	 * This method initializes jTableListaPlantas
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getJTableListaPlantas() {
		if (jTableListaPlantas == null) {
			jTableListaPlantas = new JTable();
			tableplantasmodel = new TablePlantasModel();

			TableSorted tblSorted = new TableSorted(tableplantasmodel);
			tblSorted.setTableHeader(jTableListaPlantas.getTableHeader());
			jTableListaPlantas.setModel(tblSorted);
			jTableListaPlantas
			.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableListaPlantas.setCellSelectionEnabled(false);
			jTableListaPlantas.setColumnSelectionAllowed(false);
			jTableListaPlantas.setRowSelectionAllowed(true);
			jTableListaPlantas.getTableHeader().setReorderingAllowed(false);

			((TablePlantasModel) ((TableSorted) jTableListaPlantas.getModel())
					.getTableModel()).addTableModelListener(jTableListaPlantas);

			(jTableListaPlantas.getSelectionModel())
			.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					// Llamar al metodo correspondiente

					ListSelectionModel lsm = (ListSelectionModel) e
					.getSource();
					// Borrar tabla de usos
					((TableUsosModel) ((TableSorted) jTableListaUsos
							.getModel()).getTableModel())
							.borrarTodasFilas();

					if (!lsm.isSelectionEmpty()) {

						int indexSelectedColumnRow = lsm
						.getMinSelectionIndex();
						PlantaCatastro plantaCat = ((TablePlantasModel)((TableSorted)jTableListaPlantas.getModel()).getTableModel()).getValueAt(indexSelectedColumnRow);

						int usosSize = plantaCat.getLstUsos().size();
						for (int i = 0; i < usosSize; i++) {
							((TableUsosModel) ((TableSorted) jTableListaUsos
									.getModel()).getTableModel())
									.anniadirUso((UsoCatastro) plantaCat
											.getLstUsos().get(i));
						}
					}
				}
			});

		}
		
		return jTableListaPlantas;
	}

	/**
	 * This method initializes jScrollPaneListaUsos
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneListaUsos() {
		if (jScrollPaneListaUsos == null) {
			jScrollPaneListaUsos = new JScrollPane();
			jScrollPaneListaUsos.setViewportView(getJTableListaUsos());
			jScrollPaneListaUsos.setBorder(BorderFactory
					.createTitledBorder(I18N.get("InfCatastralFisicoEconomicoPlugIn",
					"infCatastralFisicoEconomico.asc.panel.datosplantas.listausos")));

		}
		return jScrollPaneListaUsos;
	}

	/**
	 * This method initializes jTableListaUsos
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getJTableListaUsos() {
		if (jTableListaUsos == null) {
			jTableListaUsos = new JTable();
			tableusosmodel = new TableUsosModel();

			TableSorted tblSorted = new TableSorted(tableusosmodel);
			tblSorted.setTableHeader(jTableListaUsos.getTableHeader());
			jTableListaUsos.setModel(tblSorted);
			jTableListaUsos
			.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableListaUsos.setCellSelectionEnabled(false);
			jTableListaUsos.setColumnSelectionAllowed(false);
			jTableListaUsos.setRowSelectionAllowed(true);
			jTableListaUsos.getTableHeader().setReorderingAllowed(false);

			((TableUsosModel) ((TableSorted) jTableListaUsos.getModel())
					.getTableModel()).addTableModelListener(jTableListaUsos);

		}
		return jTableListaUsos;
	}

	public void setDxfData(FincaCatastro finca) {
		this.finca = finca;
		
	}

	public void enter() {
		
		final FincaCatastro fincaCatastro = this.finca;

		if (this.gestionExpedientePanel != null) {
			this.gestionExpedientePanel.getJButtonValidar().setEnabled(false);

			if (this.gestionExpedientePanel.getExpediente().getIdEstado() >= ConstantesRegistro.ESTADO_FINALIZADO) {
				EdicionUtils.enablePanel(this.gestionExpedientePanel
						.getJPanelBotones(), false);
				this.gestionExpedientePanel.getJButtonEstadoModificado()
				.setEnabled(true);
			} else {
				EdicionUtils.enablePanel(this.gestionExpedientePanel
						.getJPanelBotones(), true);
				this.gestionExpedientePanel.getJButtonEstadoModificado()
				.setEnabled(false);
			}
		}
		

		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				AppContext.getApplicationContext().getMainFrame(), null);

		progressDialog.setTitle("TaskMonitorDialog.Wait");
		progressDialog.report(I18N.get("InfCatastralFisicoEconomicoPlugIn",
		"infCatastralFisicoEconomico.asc.panel.ComprobandoFicheroASC"));
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {

					public void run() {
						try {
							
							Java2XMLCatastro aux2 = new Java2XMLCatastro();
							String textFxcc = null;
							if(fincaCatastro.getFxcc() != null){
								textFxcc = aux2.write(fincaCatastro.getFxcc(), "fxcc");
							}
							
							if (textFxcc != null) {

								SAXBuilder builder = new SAXBuilder();
								Document docFxcc = builder.build(ImportarUtils_LCGIII
										.parseStringToIS(textFxcc));
								Element raizFxcc = docFxcc.getRootElement();

								Element asc = raizFxcc.getChild("asc");
								if (asc != null && !asc.equals("")) {
									if (asc != null)
										textoAscTemp = asc.getText();

									final String ascContent = asc.getText();

									try {
										FX_CC fxcc = new FX_CC();
										if ((fxccPanel.geopistaEditor != null)
												&& (fxccPanel.geopistaEditor
														.getLayerManager()
														.getLayers().size() > 0)) {
											
											ASCCatastro ascCatastro = getAsc();
											fxccPanel.setLstPlantasUsos(ascCatastro);
											setAsc();
											
											guardarASC(fxcc);
											
											guardarASC(ascContent, fxcc,fxccPanel.geopistaEditor);
											loadASC(progressDialog, fxcc
													.getASC());
										} else {
											loadASC(progressDialog, ascContent);
										}

									} catch (Exception e) {

										e.printStackTrace();
									}
								} else {
									// limpiarPanel();
								}
							} else {
								// limpiarPanel();
							}

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							progressDialog.setVisible(false);
						}
					}

				}).start();
			}
		});
		GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);
		
		/// Cargamos las plantas significativas y los usos del fxccPanel.
		if (this.fxccPanel != null) { 
			if (this.fxccPanel.getGeopistaEditor() != null){

				if (!((TablePlantasModel) ((TableSorted) jTableListaPlantas.getModel())
						.getTableModel()).getData().isEmpty()) {
					((TablePlantasModel) ((TableSorted) jTableListaPlantas.getModel())
							.getTableModel()).borrarTodasFilas();
				}

				if (!this.fxccPanel.getGeopistaEditor().getLayerManager().getLayers().isEmpty()){
					Iterator<LayerFamily> it =  this.fxccPanel.geopistaEditor.getLayerManager().getCategories().iterator(); 
					while(it.hasNext()){
						LayerFamily actualCategory = it.next(); 
						if (actualCategory.getName().startsWith("PS")){
							Layer actualLayerAU = null;
							Layer actualLayerAS = null;

							Iterator<Layer> layerIteraor = actualCategory.getLayerables().iterator();
							while(layerIteraor.hasNext()){
								Layer auxLayer = layerIteraor.next();
								if (auxLayer.getName().startsWith("PS") && auxLayer.getName().endsWith("AU")){
									actualLayerAU = auxLayer;
								} else if (auxLayer.getName().startsWith("PS") && auxLayer.getName().endsWith("AS")) {
									actualLayerAS = auxLayer;
								}
							}

							String plantaCategory = this.getPlantaCategory(actualLayerAU);
							
							String namesCategory[] = plantaCategory.split("-");
							String nameCategory = null;
							if (namesCategory != null && namesCategory.length>0){
								
								nameCategory= namesCategory[namesCategory.length-1];
							
							if (nameCategory != null && !nameCategory.equals("")){

								PlantaSignificativaInfo pInfo = (PlantaSignificativaInfo) this.infoplantas.get(nameCategory);
								if (pInfo != null){
									if (!this.listaPlantasContiene( ((TablePlantasModel)((TableSorted)jTableListaPlantas.getModel()).getTableModel()).getData()
											, pInfo.getNombrePlantas())){

										ArrayList usos = this.getUsosFromLayer(actualLayerAU, actualLayerAS);

										this.anniadirPlantaSignificativaConUsos(pInfo.getNombrePlantas(), pInfo.getNumPlantas(), usos);
									}
								}
							}
						}
						}
					}
					
					ArrayList lstPlantas = ((TablePlantasModel)((TableSorted)jTableListaPlantas.getModel()).getTableModel()).getData();
					asc.setLstPlantas(lstPlantas);
				}
			}
		}

	}

	private ArrayList getUsosFromLayer(Layer layerAU, Layer layerAS) {
		// TODO Auto-generated method stub

		ArrayList result = new ArrayList();


		for (Iterator iterAUFeatures = layerAU.getFeatureCollectionWrapper().getFeatures().iterator(); iterAUFeatures.hasNext();){

			Object objAU = iterAUFeatures.next();
			Object  sup = null;
			Object  nom = null;

			if (objAU instanceof GeopistaFeature){

				GeopistaFeature featureAU = (GeopistaFeature)objAU;
				nom = featureAU.getAttribute("TEXTO");

				for (Iterator iterASFeatures = layerAS.getFeatureCollectionWrapper().getFeatures().iterator(); iterASFeatures.hasNext();){

					Object objAS = iterASFeatures.next();
					if (objAS instanceof GeopistaFeature){

						GeopistaFeature featureAS = (GeopistaFeature)objAS;
						if (featureAU.getGeometry().equals(featureAS.getGeometry())){
							sup = featureAS.getAttribute("TEXTO");
							break;
						}
					}
				}
			}
			if ( (nom!=null) && (sup!=null) ){
				result.add( new UsoCatastro((String) nom, Long.parseLong((String) sup) ) );
			}

		}

		return result;
	}

	private String getPlantaCategory(Layer layer) {
		Iterator<LayerFamily> it = this.fxccPanel.getGeopistaEditor().getLayerManager().getCategories().iterator();
		while (it.hasNext()){
			LayerFamily lf =  it.next();
			if (lf.getLayerables().contains(layer)){
				System.out.println(lf.getName());
				return lf.getName();
			}

		}
		return null;
	}

	public void exit() {
		setAsc();
	}

	public void loadASC(TaskMonitorDialog progressDialog, String file) {
		load(file);
		loadData(asc);
	}

	private void load(String file) {

		BufferedReader br = new BufferedReader(new StringReader(file));
		String linea = null;

		try {
			int i = 0;
			int contPlantasSignificativas =  0;
			int numPlantasSignificativas = 0;
			int numUsos = 0;
			int posicionExpediente = 0;

			this.asc.setLstLinderos(new ArrayList());
			LinderoCatastro lindero = new LinderoCatastro();
			boolean fin = false;
			while ((linea = br.readLine()) != null) {

				switch (i) {
				case 0:
					asc.setCodDelegacion(linea);
					break;
				case 1:
					asc.setNombreGerencia(linea);
					break;
				case 2:
					asc.setCodMunicipio(linea);
					break;
				case 3:
					asc.setNombreMunicipio(linea);
					break;
				case 4:
					asc.setCodVia(linea);
					break;
				case 5:
					asc.setSigla(linea);
					break;
				case 6:
					asc.setNomVia(linea);
					break;
				case 7:
					asc.setNumPolicia(linea);
					break;
				case 8:
					asc.setLetraDuplicado(linea);
					break;
				case 9:
					asc.getRefCatastral().setRefCatastral1(linea);
					break;
				case 10:
					asc.getRefCatastral().setRefCatastral2(linea);
					break;


					// LIDERO DERECHO	
				case 11:
					lindero = new LinderoCatastro();
					if ( linea.equals("DR") ){
						lindero.setTipoLindero(linea);
					} else {
						lindero.setTipoLindero(LinderoCatastro.DR);
					}
					break;
				case 12:
					if (!linea.equals("")){
						lindero.setCodVia(Integer.valueOf(linea));
					}
					break;
				case 13:
					fin = true;

					if (linea.length() == 32) {

						lindero.setSiglaVia(linea.substring(0, 2));
						lindero.setNombreVia(linea.substring(2, 27));
						lindero.setNumVia(Integer.parseInt(linea.substring(27, 31)));
						lindero.setLetraDuplicado(linea.substring(31, 32));

					}
					break;
					/// fin LD///////////////////////	


					// LINDERO IZQUIERDO
				case 14:
					lindero = new LinderoCatastro();
					if ( linea.equals("IZ") ){
						lindero.setTipoLindero(linea);
					} else {
						lindero.setTipoLindero(LinderoCatastro.IZ);
					}
					break;
				case 15:
					if (linea != null && !linea.equals("")){
						lindero.setCodVia(Integer.parseInt(linea));
					}
					break;
				case 16:
					fin = true;
					if (linea!= null && !linea.equals("") && linea.length() == 32) {

						lindero.setSiglaVia(linea.substring(0, 2));
						lindero.setNombreVia(linea.substring(2, 27));
						lindero.setNumVia(Integer.parseInt(linea.substring(27, 31)));
						lindero.setLetraDuplicado(linea.substring(31, 32));
					}
					break;
					///// fin LI ////////////////


					// LINDERO FONDO
				case 17:
					lindero = new LinderoCatastro();
					if ( linea.equals("FD") ){
						lindero.setTipoLindero(linea);
					} else {
						lindero.setTipoLindero(LinderoCatastro.FD);
					}
					break;
				case 18:
					if (linea != null && !linea.equals("")){
						lindero.setCodVia(Integer.parseInt(linea));
					}
					break;
				case 19:
					fin = true;

					if (linea.length() == 32) {
						lindero.setSiglaVia(linea.substring(0, 2));
						lindero.setNombreVia(linea.substring(2, 27));
						lindero.setNumVia(Integer.parseInt(linea.substring(27, 31)));
						lindero.setLetraDuplicado(linea.substring(31, 32));
					}
					break;
					////// fin FD///////////////////////////



				case 20:
					asc.setEscalaCaptura(linea);
					break;
				case 21:
					asc.setFechaCaptura(linea);
					break;
				case 22:
					asc.setSupParcela(linea);
					break;
				case 23:
					asc.setSupSobreRasante(linea);
					break;
				case 24:
					asc.setSupBajoRasante(linea);
					break;
				case 25:
					asc.setSupConstruida(linea);
					break;



					// NUM plantas significativas
				case 26:
					
					numPlantasSignificativas = Integer.parseInt(linea);
					
					PlantaCatastro plantaCat = new PlantaCatastro();
					asc.setLstPlantas(new ArrayList());
					for (int j = 0; j < numPlantasSignificativas; j++) {

						
						linea = br.readLine();
						plantaCat.setNumPlantasReales(Integer.parseInt(linea));
						linea = br.readLine();
						plantaCat.setNombre(linea);
						
						this.ponerNombrePlantaSignificativaCategory(j, plantaCat.getNombre());
						PlantaSignificativaInfo pInfo = new PlantaSignificativaInfo(plantaCat.getNumPlantasReales(),linea);
						ASCPanel.get_instance().infoplantas.put(linea, pInfo);
						
						linea = br.readLine();
						numUsos = new Integer(linea).intValue();

						for (int k = 0; k < numUsos; k++) {

							UsoCatastro usoCat = new UsoCatastro();
							linea = br.readLine();
							usoCat.setCodigoUso(linea);
							linea = br.readLine();
							usoCat.setSuperficieDestinada(Long.parseLong(linea));
							plantaCat.getLstUsos().add(usoCat);
						}

						asc.getLstPlantas().add(plantaCat);
						((TablePlantasModel)((TableSorted)this.getJTableListaPlantas().getModel()).getTableModel()).anniadirPlanta(plantaCat);
//						
					}
					
					break;


				default:
				break;
				
				
				}
				
				if (linea.equalsIgnoreCase("EXPEDIENTE")) {
					posicionExpediente = i;
				}
				if (posicionExpediente != 0 && i == posicionExpediente + 1){
					if (!linea.toLowerCase().equals("null")) {
						asc.setEjercicioExpediente(linea);
					}
				}
				if (posicionExpediente != 0 && i == posicionExpediente + 2){
					if (!linea.toLowerCase().equalsIgnoreCase("null")) {
						asc.setRefExpedienteAdministrativo(linea);
					}
				}
				if (posicionExpediente != 0 && i == posicionExpediente + 3){
					if (!linea.toLowerCase().equalsIgnoreCase("null")) {
						asc.setCodEntidadColaboradora(linea);
					}
				}


				if (lindero != null && fin) {
					asc.getLstLinderos().add(lindero);
					lindero = null;
					fin = false;
				}


				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		AppContext.getApplicationContext().getBlackboard().put("ascObject", asc);

	}

	private void ponerNombrePlantaSignificativaCategory(int j, String nombre) {
		// TODO Auto-generated method stub
		String nombreCapa = "PS" + fillWithCeros(Integer.toString(j+1), 2);
		
		Iterator<LayerFamily> it = fxccPanel.geopistaEditor.getLayerManager().getCategories().iterator();
		
		while (it.hasNext()){
			LayerFamily lf = it.next();
			if (lf.getName().contains(nombreCapa)){
				lf.setName(nombreCapa + "-" + nombre); 
			}
		}
		
		
	}

	public void loadFincaCatastroData(FincaCatastro info) {
		// TODO Auto-generated method stub

		String codigoDelegacion = info.getCodDelegacionMEH();
		jTextFieldCodDelegacion.setText("");
		this.asc.setCodDelegacion(codigoDelegacion);
		// donde sacar el nombre de la gerencia		
		jTextFieldNombreGerencia.setText("");
		this.asc.setNombreGerencia("");

		String ref1 = info.getRefFinca().getRefCatastral1();
		jTextFieldParcelaCatastral1.setText("");
		String ref2 = info.getRefFinca().getRefCatastral2();
		jTextFieldParcelaCatastral2.setText("");
		this.asc.setRefCatastral(new ReferenciaCatastral(ref1, ref2));

		String codigoMunicipio = info.getDirParcela().getCodigoMunicipioDGC();
		jTextFieldCodMunicipio.setText("");
		this.asc.setCodMunicipio(codigoMunicipio);

		String nombreMunicipio = info.getDirParcela().getNombreMunicipio();
		jTextFieldNombreMunicipio.setText("");
		this.asc.setNombreMunicipio(nombreMunicipio);

		String codigoVia = Integer.toString(info.getDirParcela().getCodigoVia());
		jTextFieldCodVia.setText("");
		this.asc.setCodVia(codigoVia);

		String siglasVia = info.getDirParcela().getTipoVia();
		jTextFieldSiglas.setText("");
		this.asc.setSigla(siglasVia);

		String nombreVia = info.getDirParcela().getNombreVia();
		jTextFieldNombreVia.setText("");
		this.asc.setNomVia(nombreVia);


		jTextFieldNumPolicia.setText("");
		this.asc.setNumPolicia("");

		jTextFieldLetraDuplicado.setText("");
		this.asc.setLetraDuplicado("");

		((TableLinderosModel) ((TableSorted) jTableLinderos.getModel())
				.getTableModel()).inicializarLinderos();
		this.asc.setLstLinderos(((TableLinderosModel) ((TableSorted) jTableLinderos.getModel())
				.getTableModel()).getData());


		String superficie;
		if (info.getDatosFisicos().getSupFinca() != null)
			superficie = Long.toString(info.getDatosFisicos().getSupFinca());
		else
			superficie = Long.toString(0);
		jTextFieldSuperficieParcela.setText("");
		this.asc.setSupParcela(superficie);

		String superficieBajoRasante;
		if (info.getDatosFisicos().getSupBajoRasante() != null)
			superficieBajoRasante = Long.toString(info.getDatosFisicos().getSupBajoRasante());
		else
			superficieBajoRasante = Long.toString(0);
		jTextFieldSuperficieBR.setText("");
		this.asc.setSupBajoRasante(superficieBajoRasante);

		String superficieSobreRasante;
		if (info.getDatosFisicos().getSupSobreRasante() != null)
			superficieSobreRasante = Long.toString(info.getDatosFisicos().getSupSobreRasante());
		else
			superficieSobreRasante = Long.toString(0);
		jTextFieldSuperficieSR.setText("");
		this.asc.setSupSobreRasante(superficieSobreRasante);


		String superficieConstruida;
		if (info.getDatosFisicos().getSupTotal() != null)
			superficieConstruida = Long.toString(info.getDatosFisicos().getSupTotal());
		else
			superficieConstruida = Long.toString(0);
		jTextFieldSuperficieConstruida.setText("");
		this.asc.setSupConstruida(superficieConstruida);

		if (!((TablePlantasModel) ((TableSorted) jTableListaPlantas.getModel())
				.getTableModel()).getData().isEmpty()) {
			((TablePlantasModel) ((TableSorted) jTableListaPlantas.getModel())
					.getTableModel()).borrarTodasFilas();
		}

		jTextFieldFechaCaptura.setText("");
		this.asc.setFechaCaptura("");

		jTextFieldEscalaCaptura.setText("");
		this.asc.setEscalaCaptura("");

	}

	public void setAsc(){
		
		if (asc != null && asc instanceof ASCCatastro){
			
			if (jTextFieldCodDelegacion.getText() != null){
				asc.setCodDelegacion(jTextFieldCodDelegacion.getText());
			}
			if (jTextFieldNombreGerencia.getText() != null){
				asc.setNombreGerencia(jTextFieldNombreGerencia.getText());
			}
			if (jTextFieldCodMunicipio.getText()!=null){
				asc.setCodMunicipio(jTextFieldCodMunicipio.getText());
			}
			if (jTextFieldNombreMunicipio.getText()!=null){
				asc.setNombreMunicipio(jTextFieldNombreMunicipio.getText());
			}
			if (jTextFieldCodVia.getText()!=null){
				asc.setCodVia(jTextFieldCodVia.getText());
			}
			if (jTextFieldNombreVia.getText() !=  null){
				asc.setNomVia(jTextFieldNombreVia.getText());
			}
			if (jTextFieldNumPolicia.getText()!=null){
				asc.setNumPolicia(jTextFieldNumPolicia.getText());
			}
			if (jTextFieldLetraDuplicado.getText()!=null){
				asc.setLetraDuplicado(jTextFieldLetraDuplicado.getText());
			}
			if (jTextFieldSuperficieParcela.getText()!=null){
				asc.setSupParcela(jTextFieldSuperficieParcela.getText());
			}
			if (jTextFieldSuperficieBR.getText()!=null){
				asc.setSupBajoRasante(jTextFieldSuperficieBR.getText());
			}
			if (jTextFieldSuperficieSR.getText()!=null){
				asc.setSupSobreRasante(jTextFieldSuperficieSR.getText());
			}
			if (jTextFieldSuperficieConstruida.getText()!=null){
				asc.setSupConstruida(jTextFieldSuperficieConstruida.getText());
			}
			
			AppContext.getApplicationContext().getBlackboard().put("ascTemp", asc);
			
		}
	}

	public void loadData(ASCCatastro asc) {
		
		jTextFieldCodDelegacion.setText(asc.getCodDelegacion());
		jTextFieldNombreGerencia.setText(asc.getNombreGerencia());
		jTextFieldCodMunicipio.setText(asc.getCodMunicipio());
		jTextFieldNombreMunicipio.setText(asc.getNombreMunicipio());
		jTextFieldCodVia.setText(asc.getCodVia());
		jTextFieldSiglas.setText(asc.getSigla());
		jTextFieldNombreVia.setText(asc.getNomVia());
		jTextFieldNumPolicia.setText(asc.getNumPolicia());
		jTextFieldLetraDuplicado.setText(asc.getLetraDuplicado());
		jTextFieldParcelaCatastral1.setText(asc.getRefCatastral()
				.getRefCatastral1());
		jTextFieldParcelaCatastral2.setText(asc.getRefCatastral()
				.getRefCatastral2());
		((TableLinderosModel) ((TableSorted) jTableLinderos.getModel())
				.getTableModel()).setData(asc.getLstLinderos());

		jTableLinderos.updateUI();
		jTextFieldEscalaCaptura.setText(asc.getEscalaCaptura());
		jTextFieldFechaCaptura.setText(asc.getFechaCaptura());

		((TablePlantasModel) ((TableSorted) jTableListaPlantas.getModel())
				.getTableModel()).setData(asc
						.getLstPlantas());
		jTableListaPlantas.clearSelection();
		jTableListaPlantas.updateUI();

		((TableUsosModel) ((TableSorted) jTableListaUsos.getModel())
				.getTableModel()).setData(new ArrayList());
		jTableListaUsos.updateUI();

		jTextFieldSuperficieParcela.setText(asc.getSupParcela());
		jTextFieldSuperficieBR.setText(asc.getSupBajoRasante());
		jTextFieldSuperficieSR.setText(asc.getSupSobreRasante());
		jTextFieldSuperficieConstruida.setText(asc.getSupConstruida());

	}

	private void limpiarPanel() {
		if (jTableListaPlantas != null && jTableListaPlantas.getModel() != null)
			((TablePlantasModel) jTableListaPlantas.getModel())
			.setData(new ArrayList());
		if (jTableListaUsos != null
				&& jTableListaUsos.getModel() != null
				&& ((TableSorted) jTableListaUsos.getModel()).getTableModel() != null)
			((TableUsosModel) ((TableSorted) jTableListaUsos.getModel())
					.getTableModel()).setData(new ArrayList());
		if (jTableLinderos != null
				&& jTableLinderos.getModel() != null
				&& ((TableSorted) jTableLinderos.getModel()).getTableModel() != null)
			((TableLinderosModel) ((TableSorted) jTableLinderos.getModel())
					.getTableModel()).setData(new ArrayList());
		jTableListaPlantas.updateUI();
		jTableListaUsos.updateUI();
		EdicionUtils.clearPanel(this);
	}

	/**
	 * This method initializes jTextFieldSuperficieParcela
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldSuperficieParcela() {
		if (jTextFieldSuperficieParcela == null) {
			jTextFieldSuperficieParcela = new JTextField();
			

			jTextFieldSuperficieParcela.addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	
			    	try{
			    		if (!jTextFieldSuperficieParcela.getText().equals("")){
			    			int cod = Integer.parseInt(jTextFieldSuperficieParcela.getText());
			    			asc.setSupParcela(String.valueOf(cod));
			    		} else{
				    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
		    				"La superficie de la parcela no puede estar vacío.");
			    		}
			    	} catch (NumberFormatException ex) {
						// TODO: handle exception
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    				"La superficie de la parcela debe ser un número.");
					}
			    }
			  });

			
		}
		return jTextFieldSuperficieParcela;
	}

	/**
	 * This method initializes jPanelPlantaGeneral
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelPlantaGeneral() {
		if (jPanelPlantaGeneral == null) {

			jPanelPlantaGeneral = new JPanel(new GridBagLayout());
			jPanelPlantaGeneral.setBorder(BorderFactory.createTitledBorder(
					null, I18N.get("InfCatastralFisicoEconomicoPlugIn",
					"infCatastralFisicoEconomico.asc.panel.plantageneral.titulo")));

			jPanelPlantaGeneral.add(jLabelSuperficieParcela,
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelPlantaGeneral.add(jLabelSuperficieSR,
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelPlantaGeneral.add(jLabelSuperficieBR,
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelPlantaGeneral.add(jLabelSuperficieConstruida,
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelPlantaGeneral.add(getJTextFieldSuperficieParcela(),
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelPlantaGeneral.add(getJTextFieldSuperficieSR(),
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelPlantaGeneral.add(getJTextFieldSuperficieBR(),
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));
			jPanelPlantaGeneral.add(getJTextFieldSuperficieConstruida(),
					new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));

		}
		return jPanelPlantaGeneral;
	}

	/**
	 * This method initializes jPanelPlantasSignificativas
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelPlantasSignificativas() {
		if (jPanelPlantasSignificativas == null) {
			jPanelPlantasSignificativas = new JPanel(new GridBagLayout());
			jPanelPlantasSignificativas.setBorder(BorderFactory
					.createTitledBorder(null, I18N.get("InfCatastralFisicoEconomicoPlugIn",
					"infCatastralFisicoEconomico.asc.panel.plantassignificativas.titulo")));

			jPanelPlantasSignificativas.add(getJScrollPaneListaPlantas(),
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(0, 5, 0, 5), 0, 0));
			jPanelPlantasSignificativas.add(getJScrollPaneListaUsos(),
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(0, 5, 0, 5), 0, 0));
			jPanelPlantasSignificativas.updateUI();

		}
		jPanelPlantasSignificativas.updateUI();
		return jPanelPlantasSignificativas;
	}

	/**
	 * This method initializes jTextFieldSuperficieSR
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldSuperficieSR() {
		if (jTextFieldSuperficieSR == null) {
			jTextFieldSuperficieSR = new JTextField();
			

			jTextFieldSuperficieSR .addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	
			    	try{
			    		if (!jTextFieldSuperficieSR .getText().equals("")){
			    			int cod = Integer.parseInt(jTextFieldSuperficieSR .getText());
			    			asc.setSupSobreRasante(String.valueOf(cod));
			    		} else{
				    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
		    				"La superficie sobre rasante no puede estar vacío.");
			    		}
			    	} catch (NumberFormatException ex) {
						// TODO: handle exception
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    				"La superficie sobre rasante debe ser un número.");
					}
			    }
			  });

			
		}
		return jTextFieldSuperficieSR;
	}

	/**
	 * This method initializes jTextFieldSuperficieBR
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldSuperficieBR() {
		if (jTextFieldSuperficieBR == null) {
			jTextFieldSuperficieBR = new JTextField();
			

			jTextFieldSuperficieBR .addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	
			    	try{
			    		if (!jTextFieldSuperficieBR .getText().equals("")){
			    			int cod = Integer.parseInt(jTextFieldSuperficieBR .getText());
			    			asc.setSupBajoRasante(String.valueOf(cod));
			    		} else{
				    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
		    				"La superficie bajo resante no puede estar vacío.");
			    		}
			    	} catch (NumberFormatException ex) {
						// TODO: handle exception
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    			"La superficie bajo rasante debe ser un número.");
					}
			    }
			  });

			
			
		}
		return jTextFieldSuperficieBR;
	}

	/**
	 * This method initializes jTextFieldSuperficieConstruida
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldSuperficieConstruida() {
		if (jTextFieldSuperficieConstruida == null) {
			jTextFieldSuperficieConstruida = new JTextField();
			

			jTextFieldSuperficieConstruida .addFocusListener(new FocusAdapter() {
			    public void focusLost(FocusEvent e) {
			    	
			    	try{
			    		if (!jTextFieldSuperficieConstruida .getText().equals("")){
			    			int cod = Integer.parseInt(jTextFieldSuperficieConstruida .getText());
			    			asc.setSupConstruida(String.valueOf(cod));
			    		} else{
				    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
		    				"la superficie construida no puede estar vacío.");
			    		}
			    	} catch (NumberFormatException ex) {
						// TODO: handle exception
			    		JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
			    			"La superficie construida debe ser un número.");
					}
			    }
			  });

			
		}
		return jTextFieldSuperficieConstruida;
	}

	/**
	 * Metodo para guardar el ASC basandose en la informacion almacenada en el
	 * DXF.
	 * 
	 * @param fxcc
	 * @param geopistaEditor
	 */
	public void guardarASC(String textoAscTemp, FX_CC fxcc,
			GeopistaEditor geopistaEditor) {

		AscWriterCatastro ascWrite = new AscWriterCatastro();
		if (geopistaEditor.getLayerManager().getLayers().size() > 0) {
			// Si el asc no esta inicializado todavia, lo leemos del FX_CC
			// obtenido de la Base de Datos.
			// asc sera null la primera vez que se entre en este metodo siempre
			// y cuando no se haya pinchado en la pestaña ASC.
			if (asc == null) {
				try {
					if (fxcc.getASC() != null) {
						SAXBuilder builder = new SAXBuilder();
						Document docFxcc = builder.build(ImportarUtils_LCGIII
								.parseStringToIS(fxcc.toXML()));
						Element raizFxcc = docFxcc.getRootElement();

						Element asc = raizFxcc.getChild("asc");
						if (asc != null) {
							String texto = asc.getText();
							load(texto);
						}
					} else if (textoAscTemp != null)
						load(textoAscTemp);

				} catch (JDOMException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
//			ascWrite.write(asc, geopistaEditor.getLayerManager());
		}
		guardarASC(fxcc);
	}

	public void guardarASC(FX_CC fxcc) {

		String asc = getASC();
		fxcc.setASC(asc);
	}

	private String fillWithCeros(String campo, int longitud) {
		StringBuffer resultado = new StringBuffer(campo);
		int longitudCampo = campo.length();

		int resto = longitud - longitudCampo;
		for (int i = 0; i < resto; i++) {
			resultado.insert(0, "0");
		}
		resultado.setLength(resultado.length());
		return resultado.toString();
	}

	public String getASC() {
		
		if (asc != null) {
			
			StringWriter strWriter = new StringWriter();
			PrintWriter out = new PrintWriter(strWriter);
			out.println(asc.getCodDelegacion());
			out.println(asc.getNombreGerencia());
			out.println(asc.getCodMunicipio());
			out.println(asc.getNombreMunicipio());
			out.println(asc.getCodVia());
			out.println(asc.getSigla());
			out.println(asc.getNomVia());
			out.println(asc.getNumPolicia());
			out.println(asc.getLetraDuplicado());
			out.println(asc.getRefCatastral().getRefCatastral1());
			out.println(asc.getRefCatastral().getRefCatastral2());
			// TODO mirar en la especificacion si primero tiene que ir izq,
			// luego derecha y ultimo el fondo. Si siempre
			// TODO: se introduce el tipolindero no pasa nada, si no esto no
			// vale.
			for (int i = 0; i < asc.getLstLinderos().size(); i++) {
				LinderoCatastro lin = (LinderoCatastro) asc.getLstLinderos()
				.get(i);
				out.println(lin.getTipoLindero());
				out.println(lin.getCodVia());
				String cont = "";
				if (lin.getSiglaVia() != null) {
					cont = lin.getSiglaVia() + lin.getNombreVia()
					+ lin.getNumVia() + lin.getLetraDuplicado();
				}
				out.println(cont);
			}
			if (asc.getLstLinderos().size() != 3) {
				for (int i = asc.getLstLinderos().size(); i < 3; i++) {
					out.println("");
					out.println("");
					out.println("");
				}
			}

			out.println(asc.getEscalaCaptura());
			out.println(asc.getFechaCaptura());
			out.println(asc.getSupParcela());
			out.println(asc.getSupSobreRasante());
			out.println(asc.getSupBajoRasante());
			out.println(asc.getSupConstruida());
			out.println(fillWithCeros(String
					.valueOf(asc.getLstPlantas().size()), 2));
			for (int i = 0; i < asc.getLstPlantas().size(); i++) {
				PlantaCatastro plCas = (PlantaCatastro) asc.getLstPlantas()
				.get(i);
				out.println(fillWithCeros(String.valueOf(plCas
						.getNumPlantasReales()), 2));
				out.println(plCas.getNombre());
				out.println(fillWithCeros(String.valueOf(plCas.getLstUsos()
						.size()), 2));
				for (int j = 0; j < plCas.getLstUsos().size(); j++) {
					UsoCatastro uso = (UsoCatastro) plCas.getLstUsos().get(j);
					out.println(uso.getCodigoUso());
					out.println(fillWithCeros(String.valueOf(uso
							.getSuperficieDestinada()), 7));
				}
			}
			out.println("EXPEDIENTE");
			out.println(asc.getEjercicioExpediente());
			out.println(asc.getRefExpedienteAdministrativo());
			out.println(asc.getCodEntidadColaboradora());

			return strWriter.toString();
		}
		return null;

	}

	public void setFxccPanel(FxccPanel fxccPanel) {
		this.fxccPanel = fxccPanel;
		// TODO Auto-generated method stub

	}

	public void cleanup() {

		if (jPanelIdentificacion != null)
			jPanelIdentificacion.removeAll();
		if (jPanelPlantas != null)
			jPanelPlantas.removeAll();
		if (jPanelPlantaGeneral != null)
			jPanelPlantaGeneral.removeAll();
		if (jPanelPlantasSignificativas != null)
			jPanelPlantasSignificativas.removeAll();

		jTextFieldCodDelegacion = null;
		jTextFieldNombreGerencia = null;
		jTextFieldCodMunicipio = null;
		jTextFieldNombreMunicipio = null;
		jTextFieldCodVia = null;
		jTextFieldSiglas = null;
		jTextFieldNombreVia = null;
		jTextFieldNumPolicia = null;
		jTextFieldLetraDuplicado = null;
		jTextFieldParcelaCatastral1 = null;
		jTextFieldParcelaCatastral2 = null;
		jTextFieldEscalaCaptura = null;
		jTextFieldFechaCaptura = null;
		jTextFieldSuperficieParcela = null;
		jTextFieldSuperficieSR = null;
		jTextFieldSuperficieBR = null;
		jTextFieldSuperficieConstruida = null;


	}

	private boolean iniciarDatosASC() {

		return false;
	}

	private EstructuraDB obtenerElementoLista(ArrayList lst, String patron) {

		for (Iterator iteratorLista = lst.iterator(); iteratorLista.hasNext();) {
			EstructuraDB estructura = (EstructuraDB) iteratorLista.next();
			if (estructura.getPatron().equals(patron))
				return estructura;
		}
		return null;
	}


	private boolean listaPlantasContiene(ArrayList<PlantaCatastro> lst, String patron){

		Iterator<PlantaCatastro> it = lst.iterator();

		while (it.hasNext()){
			if(it.next().getNombre().endsWith(patron)){
				return true;
			}
		}

		return false;
	}



	private PlantaCatastro obtenerPlantaPorPatron (ArrayList<PlantaCatastro> lst, String patron){

		Iterator<PlantaCatastro> it = lst.iterator();
		PlantaCatastro posicion = null;
		int i = 0;

		while (it.hasNext()){
			if(it.next().getNombre().endsWith(patron)){
				return lst.get(i);
			}
			i ++;
		}

		return posicion;
	}

	/**
	 * @param nombrePlanta
	 * @param numplantas
	 * @param usos
	 * @return true if the planta is added correctly and false in any other case.
	 */
	public boolean anniadirPlantaSignificativaConUsos(String nombrePlanta, int numplantas, ArrayList usos){
		TablePlantasModel table = ((TablePlantasModel)((TableSorted)this.jTableListaPlantas.getModel()).getTableModel());
		PlantaCatastro planta = new PlantaCatastro(nombrePlanta, numplantas);

		if ( table != null){
			if (usos != null){
				planta.setLstUsos(usos);
			}
			return table.anniadirPlanta(planta);
		}

		return false;
	}




	private ArrayList<PlantaCatastro> obtenerPlantasCatastro(ArrayList<ConstruccionCatastro> construcciones){

		// Lista para las plantas resultantes.
		ArrayList<PlantaCatastro> listaResult = new ArrayList<PlantaCatastro>();
		// iterator para recorrer la lista de construcciones.
		Iterator itConstrucciones= construcciones.iterator();

		AppContext app = (AppContext) AppContext.getApplicationContext();
		Blackboard blackboard = app.getBlackboard();



		EstructuraDB ePlanta = new EstructuraDB();
		ArrayList lstplantas = (ArrayList) blackboard.get("ListaPlanta");

		EstructuraDB eDestino = new EstructuraDB();
		ArrayList lstDestinos = (ArrayList)blackboard.get("ListaTipoDestino");



		while (itConstrucciones.hasNext()) {

			ConstruccionCatastro cc = (ConstruccionCatastro) itConstrucciones.next();

			ePlanta = obtenerElementoLista(lstplantas, cc.getDomicilioTributario()
					.getPlanta());

			eDestino = obtenerElementoLista(lstDestinos, cc.getDatosFisicos().getCodDestino());
			String destino = eDestino.getPatron() + " " + eDestino.getDescripcion();
			UsoCatastro uso = new UsoCatastro(destino, cc.getDatosFisicos().getSupTotal());

			if (listaPlantasContiene(listaResult, ePlanta.getPatron())){
				PlantaCatastro planta = obtenerPlantaPorPatron(listaResult, ePlanta.getPatron());
				planta.getLstUsos().add(uso);
			} else {
				PlantaCatastro plantaCat = new PlantaCatastro(ePlanta.getDescripcion() + " "+ ePlanta.getPatron(), 1);
				plantaCat.getLstUsos().add(uso);
				listaResult.add(plantaCat);
			}
		}

		return listaResult;
	}
	
	/**
	 * Método que devuelve la cadena rellena con el caracter "0", hasta ocupar "lon" posiciones, desde el lado indicado.
	 *	<ul>
	 * 		<li>Utiliza el caracter como símbolo de relleno</li>
	 * 		<li>Valores posibles de lado
	 *			<ul>
	 *				<li>0: izquierda</li>
	 *				<li>1: derecha</li>
	 *			</ul>
	 * 		</li>
	 *	</ul>
	 *
	 * @param java.lang.String cadena: Cadena a ser formateada
	 * @param int longitud: Indica la longitud de la cadena resultante
	 * @return java.lang.String Cadena rellena
	 */
	public String fillnumplanta(String cadena, int longitud)
	{
		String salida = cadena;
		char caracterRelleno = '0';

		//Si tiene la misma longitud la devuelve
		if(salida.length() == longitud)	return salida;

		//Si es más larga la trunca
		if(salida.length() > longitud)	return salida;

		//Si es menor, entonces modificamos
		if(salida.length() < longitud)
		{

			//				//	Rellenar por la derecha
			//				for(int k=cadena.length();k<longitud;k++)
			//					salida = salida + caracterRelleno;

			//	Rellenar por la izquierda
			for(int k=cadena.length();k<longitud;k++)
				salida = caracterRelleno + salida;


			return salida;
		}

		return cadena;
	}
	
	private void saveASCTemp(){
		

	}



} // @jve:decl-index=0:visual-constraint="10,10"
