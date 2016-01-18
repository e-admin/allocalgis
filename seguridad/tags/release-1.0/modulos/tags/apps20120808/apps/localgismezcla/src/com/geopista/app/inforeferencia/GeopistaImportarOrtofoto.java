package com.geopista.app.inforeferencia;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.ortofoto.COperacionesOrtofoto;
import com.geopista.protocol.ortofoto.CSolicitudEnvioOrtofoto;
import com.geopista.protocol.ortofoto.CSolicitudImportacionOrtofoto;
import com.geopista.security.GeopistaPermission;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.util.Blackboard;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import java.awt.*;
import java.io.File;
import java.util.Map;

public class GeopistaImportarOrtofoto extends JPanel implements WizardPanel
{

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JLabel lblErrores;

    private JScrollPane scpErrores;

    private JLabel lblImagen;

    public int errorFich = 0;

    public int permisoAcceso = 0;

    public boolean acceso=true;

    /*private int returnVal = 0;

    private String rutaFichero;

    private boolean continuar;*/

    private JTextField txtPath;

    private JLabel lblPath;

    private JComboBox txtSRS;

    private JLabel lblfuente;

    //private WizardContext wizardContext;
    private ApplicationContext aplicacion = AppContext.getApplicationContext();

    private JLabel lbltipo;


    private JSeparator jSeparator5 = new JSeparator();

    private JLabel lblDatos;

	private JButton btnAbrirOrtofoto = null;

	private JRadioButton jRadioButtonMrSid = null;

	private JRadioButton jRadioButtonTiff = null;

	private JLabel jLabelMrSid = null;

	private JLabel jLabelTiff = null;

	private JLabel jLabelECW = null;

	private JFileChooser fcSelectorOrtofoto = null;  //  @jve:decl-index=0:visual-constraint="-27,654"

    private JSeparator jSeparator1 = new JSeparator();

    private JSeparator jSeparator4 = new JSeparator();

	private JRadioButton jRadioButtonECW = null;

	private String fileType = null;

    private ButtonGroup group;
    private JTextPane textPaneErrores;
    private String fileTypeWF;
    private JLabel jLabelMrSidYWF;
    private JLabel jLabelTiffWF;
    private JRadioButton jRadioButtonMrSidYWF;
    private JRadioButton jRadioButtonTiffYWF;
    private JLabel jLabelWFPath;
    private JTextField jTFieldWFPath;
	private JButton jButtonAbrirWF;
    private WizardContext wizardContext;
    private boolean permiso;
    private Blackboard blackboard = aplicacion.getBlackboard();
    private GeopistaImportarOrtofoto2 proxForm = null;
    
    
    private DefaultStyledDocument doc = null;
    private StyleContext sc = new StyleContext();
	private Style estiloNegro = sc.addStyle(null, null);

    public GeopistaImportarOrtofoto(GeopistaImportarOrtofoto2 proxForm)
    {
    	try
        {
    		this.proxForm = proxForm;
            jbInit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }                          
    }

    private void jbInit() throws Exception
    {
    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {

                    // Wait for the dialog to appear before starting the
                    // task. Otherwise
                    // the task might possibly finish before the dialog
                    // appeared and the
                    // dialog would never close. [Jon Aquino]
                    new Thread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                {
                                	setLookAndFeel();
                                    fileType= "sid";
                                    
                                    setLayout(null);
                                    setName(aplicacion
                                            .getI18nString("importar.ortofoto.titulo1"));
                                    lblImagen = new JLabel();
                                    lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
                                    lblImagen.setBounds(new Rectangle(15, 18, 110, 487));
                                    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                                    add(lblImagen, null);

                                    lblDatos = new JLabel();
                                    lblDatos.setBounds(new Rectangle(135, 36, 333, 20));
                                    lblDatos.setText(aplicacion
                                            .getI18nString("importador.lblDatos"));
                                    jSeparator4.setBounds(new Rectangle(135, 20, 605, 2));
                                    lblPath = new JLabel();
                                    lblPath.setText(aplicacion
                                            .getI18nString("importador.lblPath"));
                                    lblPath.setBounds(new Rectangle(135, 110, 106, 20));
                                    lblfuente = new JLabel();
                                    lblfuente.setText(aplicacion
                                            .getI18nString("importador.lblfuente"));
                                    lblfuente.setBounds(new Rectangle(135, 75, 95, 20));
                                    txtPath = new JTextField();
                                    txtPath.setBounds(new Rectangle(252, 110, 433, 20));
                                    txtPath.setEditable(false);
                                    
                                    txtSRS = new JComboBox();
                                    txtSRS.addItem("23028");
                                    txtSRS.addItem("23029");
                                    txtSRS.addItem("23030");
                                    txtSRS.addItem("23031");
                                    txtSRS.addItem("32614");
                                    txtSRS.setEditable(false);
                                    txtSRS.setBounds(new Rectangle(252, 75, 478, 20));
                                    fcSelectorOrtofoto = getFcSelectorOrtofoto();
                                    jLabelWFPath = new JLabel();
                                    jLabelWFPath.setText(aplicacion
                                            .getI18nString("importador.labelWFPath"));
                                    jLabelWFPath.setBounds(new Rectangle(135, 145, 106, 20));
                                    jTFieldWFPath = new JTextField();
                                    jTFieldWFPath.setBounds(new Rectangle(252, 145, 433, 20));
                                    jTFieldWFPath.setEditable(false);
                                    jTFieldWFPath.setEnabled(false);
                                    jButtonAbrirWF = getjButtonAbrirWF();
                                    jButtonAbrirWF.setEnabled(false);
                                    
                                    jSeparator1.setBounds(new Rectangle(135, 310, 605, 2));
                                    lbltipo= new JLabel();
                                    lbltipo.setText(aplicacion
                                            .getI18nString("importador.lbltipo"));
                                    lbltipo.setBounds(new Rectangle(135, 180, 97, 20));



                                    jLabelMrSid = new JLabel();
                                    jLabelMrSid.setBounds(new Rectangle(280, 195, 40, 20));
                                    jLabelMrSid.setText("MrSid");
                                    jLabelECW = new JLabel();
                                    jLabelTiff = new JLabel();
                                    jLabelTiff.setBounds(new Rectangle(280, 225, 40, 20));
                                    jLabelTiff.setText("GeoTiff");
                                    jLabelECW.setBounds(new Rectangle(280, 255, 40, 20));
                                    jLabelECW.setText("ECW");
                                    jLabelMrSidYWF = new JLabel();
                                    jLabelMrSidYWF.setText(aplicacion
                                            .getI18nString("importador.labelMrSidYWF"));
                                    jLabelMrSidYWF.setBounds(new Rectangle(390,195,70,20));
                                    jLabelTiffWF = new JLabel();
                                    jLabelTiffWF.setText(aplicacion
                                            .getI18nString("importador.labelTiffWF"));
                                    jLabelTiffWF.setBounds(new Rectangle(390,225,70,20));

                                    jRadioButtonMrSid = getJRadioButtonMrSid();
                                    jRadioButtonTiff = getJRadioButtonTiff();
                                    jRadioButtonECW = getJRadioButtonECW();
                                    jRadioButtonMrSidYWF = getJRadioButtonMrSidYWF();
                                    jRadioButtonTiffYWF = getJRadioButtonTiffYWF();

                                    group = new ButtonGroup();
                                    group.add(jRadioButtonECW);
                                    group.add(jRadioButtonMrSid);
                                    group.add(jRadioButtonTiff);
                                    group.add(jRadioButtonMrSidYWF);
                                    group.add(jRadioButtonTiffYWF);
                                    group.setSelected(jRadioButtonMrSid.getModel(), true);


                                    lblErrores = new JLabel();
                                    lblErrores.setText(aplicacion
                                            .getI18nString("importador.lblErrores"));
                                    lblErrores.setBounds(new Rectangle(135, 340, 595, 20));
                    
                                	StyleConstants.setForeground(estiloNegro, Color.black);
                                	doc = new DefaultStyledDocument(sc);

                                	textPaneErrores = new JTextPane(doc);
            
                                    //textPaneErrores = new JTextPane();
                                    textPaneErrores.setBounds(new Rectangle(135, 365, 595, 120));
                                    textPaneErrores.setEditable(false);

                                    scpErrores = new JScrollPane();
                                    scpErrores.setBounds(new Rectangle(135, 365, 595, 120));        
                                    scpErrores.setViewportView(textPaneErrores);
                                    jSeparator5.setBounds(new Rectangle(135, 505, 605, 2));

                                    add(lblDatos, null);
                                    add(jSeparator5, null);
                                    add(jSeparator4, null);
                                    add(jSeparator1, null);
                                    add(lbltipo, null);
                                    add(lblfuente, null);
                                    add(txtSRS, null);
                                    add(lblPath, null);
                                    add(jLabelWFPath, null);
                                    add(jTFieldWFPath, null);
                                    add(jButtonAbrirWF,null);
                                    add(txtPath, null);
                                    add(lblImagen, null);
                                    add(scpErrores, null);
                                    add(lblErrores, null);
                                    add(jRadioButtonMrSid, null);
                                    add(jRadioButtonTiff, null);
                                    add(jRadioButtonECW, null);
                                    add(jRadioButtonMrSidYWF, null);
                                    add(jRadioButtonTiffYWF, null);
                                    add(jLabelMrSid, null);
                                    add(jLabelTiff, null);
                                    add(jLabelECW, null);
                                    add(jLabelMrSidYWF, null);
                                    add(jLabelTiffWF, null);
                                    add(getBtnAbrirOrtofoto(), null);
                                    setSize(750, 600);

                                } catch (Exception e)
                                {

                                } finally
                                {
                                    progressDialog.setVisible(false);
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
    }


	/**
	 * This method initializes btnAbrirOrtofoto
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnAbrirOrtofoto() {
		if (btnAbrirOrtofoto == null) {
			btnAbrirOrtofoto = new JButton();
			btnAbrirOrtofoto.setIcon(IconLoader.icon("abrir.gif"));
			btnAbrirOrtofoto.setBounds(new Rectangle(693, 110, 28, 23));
			btnAbrirOrtofoto.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(group.getSelection()!=null)
                    {
                        btnAbrir_actionPerformed(e);
                    }
                    else
                    {
                        //TODO cuando se integre dara error, cambiarlo
                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), "Seleccione antes un tipo de fichero.");
                    }
                }
			});
		}
		return btnAbrirOrtofoto;
	}

	/**
	 * This method initializes fcSelectorOrtofoto
	 *
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getFcSelectorOrtofoto() {
		if (fcSelectorOrtofoto == null) {
			fcSelectorOrtofoto = new JFileChooser();
		}
		return fcSelectorOrtofoto;
	}

	private JButton getjButtonAbrirWF() {
		if (jButtonAbrirWF == null) {
			jButtonAbrirWF = new JButton();
			jButtonAbrirWF.setIcon(IconLoader.icon("abrir.gif"));
			jButtonAbrirWF.setBounds(new Rectangle(693, 145, 28, 23));
			jButtonAbrirWF.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(group.getSelection()!=null)
                    {
                        btnAbrirWF_actionPerformed(e);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), "Seleccione antes un tipo de fichero.");
                    }
				}
			});
		}
		return jButtonAbrirWF;
	}

 

	private void btnAbrir_actionPerformed(ActionEvent e)
    {
        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        filter.addExtension(fileType);
        filter.setDescription(aplicacion.getI18nString("fichero."+fileType));
        fcSelectorOrtofoto.resetChoosableFileFilters();
        fcSelectorOrtofoto.setFileFilter(filter);
        fcSelectorOrtofoto.setFileSelectionMode(0);
        fcSelectorOrtofoto.setAcceptAllFileFilterUsed(false);

        //returnVal = fcSelectorOrtofoto.showOpenDialog(this);

        // Handle open button action.
        if (e.getSource() == btnAbrirOrtofoto)
        {
            int returnVal = fcSelectorOrtofoto.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = fcSelectorOrtofoto.getSelectedFile();
                txtPath.setText(file.getPath());
                //This is where a real application would open the file.
            }
       }
    }

	private void btnAbrirWF_actionPerformed(ActionEvent e)
    {  
        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        filter.addExtension(fileTypeWF);
        filter.setDescription(aplicacion.getI18nString("fichero."+fileTypeWF));
        fcSelectorOrtofoto.resetChoosableFileFilters();
        fcSelectorOrtofoto.setFileFilter(filter);
        fcSelectorOrtofoto.setFileSelectionMode(0);
        fcSelectorOrtofoto.setAcceptAllFileFilterUsed(false);

        //returnVal = fcSelectorOrtofoto.showOpenDialog(this);

        // Handle open button action.
        if (e.getSource() == jButtonAbrirWF)
        {
            int returnVal = fcSelectorOrtofoto.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = fcSelectorOrtofoto.getSelectedFile();
                jTFieldWFPath.setText(file.getPath());
                //This is where a real application would open the file.
            }
       }
    }

    /**
	 * This method initializes jRadioButtonECW
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRadioButtonECW() {
		if (jRadioButtonECW == null) {
			jRadioButtonECW = new JRadioButton();
			jRadioButtonECW.setBounds(new Rectangle(250, 255, 21, 21));
			jRadioButtonECW.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					fileType = "ecw";
                    jTFieldWFPath.setEnabled(false);
                    jButtonAbrirWF.setEnabled(false);
                    jTFieldWFPath.setText("");
                    txtPath.setText("");
                }
			});
		}
		return jRadioButtonECW;
	}

	/**
	 * This method initializes jRadioButtonMrSid1
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRadioButtonMrSid() {
		if (jRadioButtonMrSid == null) {
			jRadioButtonMrSid = new JRadioButton();
			jRadioButtonMrSid.setBounds(new Rectangle(250, 195, 20, 20));
			jRadioButtonMrSid.addActionListener(new java.awt.event.ActionListener() {
	        	public void actionPerformed(java.awt.event.ActionEvent e) {
	        		fileType = "sid";
                    jTFieldWFPath.setEnabled(false);
                    jButtonAbrirWF.setEnabled(false);
                    jTFieldWFPath.setText("");
                    txtPath.setText("");
                }
	        });
		}
		return jRadioButtonMrSid;
	}

	/**
	 * This method initializes jRadioButtonTiff1
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRadioButtonTiff() {
		if (jRadioButtonTiff == null) {
			jRadioButtonTiff = new JRadioButton();
			jRadioButtonTiff.setBounds(new Rectangle(250, 225, 20, 20));
            jRadioButtonTiff.addActionListener(new java.awt.event.ActionListener() {
	        	public void actionPerformed(java.awt.event.ActionEvent e) {
	        		fileType = "tif";
                    jTFieldWFPath.setEnabled(false);
                    jButtonAbrirWF.setEnabled(false);
                    jTFieldWFPath.setText("");
                    txtPath.setText("");
                }
	        });
		}
		return jRadioButtonTiff;
	}


	private JRadioButton getJRadioButtonTiffYWF() {
		if (jRadioButtonTiffYWF == null) {
			jRadioButtonTiffYWF = new JRadioButton();
			jRadioButtonTiffYWF.setBounds(new Rectangle(360, 225, 20, 20));
			jRadioButtonTiffYWF.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					fileType = "tif";
                    fileTypeWF= "tfw";
                    jTFieldWFPath.setEnabled(true);
                    jButtonAbrirWF.setEnabled(true);
                    jTFieldWFPath.setText("");
                    txtPath.setText("");
                }
			});
		}
		return jRadioButtonTiffYWF;
	}

	private JRadioButton getJRadioButtonMrSidYWF() {
		if (jRadioButtonMrSidYWF == null) {
			jRadioButtonMrSidYWF = new JRadioButton();
			jRadioButtonMrSidYWF.setBounds(new Rectangle(360, 195, 20, 20));
			jRadioButtonMrSidYWF.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					fileType = "sid";
                    fileTypeWF= "sdw";
                    jTFieldWFPath.setEnabled(true);
                    jButtonAbrirWF.setEnabled(true);
                    jTFieldWFPath.setText("");
                    txtPath.setText("");  
                }
			});
		}
		return jRadioButtonMrSidYWF;
	}


    public void enteredFromLeft(Map dataMap)
    {

        if (!aplicacion.isLogged())
        {
            aplicacion.login();
        }
        if (!aplicacion.isLogged())
        {
            wizardContext.cancelWizard();
            return;
        }
        GeopistaPermission paso = new GeopistaPermission(
        "Geopista.InfReferencia.ImportarOrtofoto");
        permiso = aplicacion.checkPermission(paso, "Informacion de Referencia");

    }
    
    
    public void exitingToRight() throws Exception
    {
	
    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion.getI18nString("importando.ortofoto.title"));
        progressDialog.report(aplicacion.getI18nString("importando.ortofoto"));
        progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {

                    // Wait for the dialog to appear before starting the
                    // task. Otherwise
                    // the task might possibly finish before the dialog
                    // appeared and the
                    // dialog would never close. [Jon Aquino]
                    new Thread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                { 	
                                	String progressMsg = "";
                                	textPaneErrores.setText(progressMsg);
                                	
                                	if ((!txtPath.getText().equals("")) && txtPath.getText()!=null) {
								    	CResultadoOperacion resultado = null;
								    	CSolicitudEnvioOrtofoto sEnvio = new CSolicitudEnvioOrtofoto();
								    	
								    	if (jRadioButtonMrSidYWF.isSelected() || jRadioButtonTiffYWF.isSelected()) {
		                                	boolean isWorldfileAttached = jTFieldWFPath.isEnabled() && 
		                                		(!jTFieldWFPath.getText().equals("")) && 
		                                			jTFieldWFPath.getText()!=null;
		                                	
		                                	if (isWorldfileAttached) {
		                                		progressMsg = aplicacion.getI18nString("importador.info.tiempo");
		                                		sEnvio.setImage(new File(txtPath.getText()));
		                                		sEnvio.setWorldfile(new File(jTFieldWFPath.getText()));
		                                		sEnvio.setWorldfileAttached(true);
		                                		
		                                		textPaneErrores.setText(progressMsg);
										    	resultado = COperacionesOrtofoto.enviarOrtofoto(sEnvio);
										    	progressMsg = progressMsg.concat(resultado.getDescripcion()+"\n");
										    	textPaneErrores.setText(progressMsg);
										    	
										    	CSolicitudImportacionOrtofoto sImportacion = new CSolicitudImportacionOrtofoto();
										    	sImportacion.setEpsg((String)txtSRS.getSelectedItem());
										    	sImportacion.setImageName(new File(txtPath.getText()).getName());
									    		sImportacion.setWorldfileAttached(true);
									    		sImportacion.setWorldfileName(new File(jTFieldWFPath.getText()).getName());
									    		sImportacion.setExtension(GeopistaImportarOrtofoto.this.getOrtofotoExtension());
										    	
										    	progressMsg = progressMsg.concat(aplicacion.getI18nString("importador.info.progreso"));
										    	textPaneErrores.setText(progressMsg);
										    	resultado = COperacionesOrtofoto.importarOrtofoto(sImportacion);
										    	progressMsg = progressMsg.concat(resultado.getDescripcion()+"\n");
										    	textPaneErrores.setText(progressMsg);
										    	progressMsg = progressMsg.concat(aplicacion.getI18nString("importador.info.fin"));
										    	textPaneErrores.setText(progressMsg);
		                                	} else {
		                                		progressMsg = progressMsg.concat(aplicacion.getI18nString("importador.noexiste.worldfile")+"\n");
		                                		progressMsg = progressMsg.concat(aplicacion.getI18nString("importador.parametros.incorrectos")+"\n");
		                                		textPaneErrores.setText(progressMsg);
		                                	}
								    	} else {
								    		progressMsg = aplicacion.getI18nString("importador.info.tiempo");
								    		sEnvio.setImage(new File(txtPath.getText()));
	                                		sEnvio.setWorldfileAttached(false);
	                                		
	                                		textPaneErrores.setText(progressMsg);
									    	resultado = COperacionesOrtofoto.enviarOrtofoto(sEnvio);
									    	progressMsg = progressMsg.concat(resultado.getDescripcion()+"\n");
									    	textPaneErrores.setText(progressMsg);
									    	
									    	CSolicitudImportacionOrtofoto sImportacion = new CSolicitudImportacionOrtofoto();
									    	sImportacion.setEpsg((String)txtSRS.getSelectedItem());
									    	sImportacion.setImageName(new File(txtPath.getText()).getName());
									    	sImportacion.setWorldfileAttached(false);
                                            sImportacion.setExtension(GeopistaImportarOrtofoto.this.getOrtofotoExtension());
									    	
									    	progressMsg = progressMsg.concat(aplicacion.getI18nString("importador.info.progreso"));
									    	textPaneErrores.setText(progressMsg);
									    	resultado = COperacionesOrtofoto.importarOrtofoto(sImportacion);
									    	progressMsg = progressMsg.concat(resultado.getDescripcion()+"\n");
									    	textPaneErrores.setText(progressMsg);
									    	progressMsg = progressMsg.concat(aplicacion.getI18nString("importador.info.fin"));
									    	textPaneErrores.setText(progressMsg);
								    	} 	
                                	} else {
                                		progressMsg = progressMsg.concat(aplicacion.getI18nString("importador.noexiste.imagen")+"\n");
                                		progressMsg = progressMsg.concat(aplicacion.getI18nString("importador.parametros.incorrectos")+"\n");
                                		textPaneErrores.setText(progressMsg);
                                	}
                                } catch (OutOfMemoryError e) {
                                	textPaneErrores.setText(aplicacion.getI18nString("importador.info.memoria"));
                                } catch (Exception e)
                                {

                                } finally
                                {
                                	proxForm.getTextPaneErrores().setText(textPaneErrores.getText());
                                    progressDialog.setVisible(false);
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
    }
    
    private String getOrtofotoExtension() {
        if (jRadioButtonMrSidYWF.isSelected() || jRadioButtonMrSidYWF.isSelected()) {
            return "sid";
        } else if (jRadioButtonTiff.isSelected() || jRadioButtonTiffYWF.isSelected()) {
            return "tif";
        } else if (jRadioButtonECW.isSelected()) {
            return "ecw";
        }
        return null;
    }
    public void add(InputChangedListener listener)
    {

    }

    public void remove(InputChangedListener listener)
    {

    }
    public String getTitle()
    {
        return this.getName();
    }

    public String getID()
    {
        return "1";
    }

    public String getInstructions()
    {
        return " ";
    }

    public boolean isInputValid()
    {
       /* if (permiso)
        {
            return (!hayErroresFilas);
        }
        else
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("NoPermisos"));
            return false;
        } */
        return true;
    }

    private String nextID = "2";

    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }

    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {
        return nextID;
    }

    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }

    public void exiting()
    {

    }

    public static void setLookAndFeel()
    {
        try
        {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.showGrowBox", "true");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e)
        {
            System.out.println("Error setting native LAF: " + e);
        }
    }

	


//    public static void main(String args[])
//    {
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//            GeopistaImportarOrtofoto aux = new GeopistaImportarOrtofoto();
//            }
//        });
//    }

}//  @jve:decl-index=0:visual-constraint="-31,30"// de la clase general.
