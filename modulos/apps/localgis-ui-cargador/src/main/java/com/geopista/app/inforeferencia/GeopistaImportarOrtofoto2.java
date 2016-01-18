/**
 * GeopistaImportarOrtofoto2.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inforeferencia;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarOrtofoto2 extends JPanel implements WizardPanel
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
    
    
    private DefaultStyledDocument doc = null;
    private StyleContext sc = new StyleContext();
	private Style estiloNegro = sc.addStyle(null, null);

    public GeopistaImportarOrtofoto2()
    {
    	try
        {
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
									txtSRS.addItem("25828");
                                    txtSRS.addItem("25829");
                                    txtSRS.addItem("25830");
                                    txtSRS.addItem("25831");
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
                                    jLabelMrSid.setBounds(new Rectangle(280, 195, 30, 20));
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
                                    jLabelMrSidYWF.setBounds(new Rectangle(390,195,150,20));
                                    jLabelTiffWF = new JLabel();
                                    jLabelTiffWF.setText(aplicacion
                                            .getI18nString("importador.labelTiffWF"));
                                    jLabelTiffWF.setBounds(new Rectangle(390,225,150,20));

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
        return "2";
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

    private String nextID = null;

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

    public JTextPane getTextPaneErrores() {
		return textPaneErrores;
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
