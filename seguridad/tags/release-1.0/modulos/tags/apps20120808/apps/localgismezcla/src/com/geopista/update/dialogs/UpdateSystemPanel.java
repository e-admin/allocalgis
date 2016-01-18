/*
 * Created on 09-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.update.dialogs;

import javax.swing.JPanel;
import javax.swing.JTextField;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.geopista.update.UpdateDatabase;
import com.vividsolutions.jump.I18N;

import javax.swing.JPasswordField;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import java.lang.StringBuffer;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;


/**
 * @author jalopez
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class UpdateSystemPanel extends JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private JTextField installDirectoryPath = null;

    private JLabel installDirectoryPathLabel = null;

    private JLabel messageDirectoryLabel = null;

    private JButton selectDirectoryJButton = null;
    
    
    private JTextField jTextFieldTomcatDirectory = null;
    
    private JLabel jLabelTomcatDirectory = null;
    
    private JButton jButtonTomcatDirectory = null;
    

    private JButton selectJdkJButton = null;

    private JLabel geopistaPasswordJLabel = null;

    private JLabel messagePasswordJLabel = null;

    private JPasswordField geopistaPasswordJTextField = null;

    private UpdateDatabase updateDatabase = new UpdateDatabase();

    private UpdateSystemFiles updateSystemFiles = new UpdateSystemFiles();

    private String password = null;

    private JEditorPane progressMessageEditorPane = null;

    private String progressMessage = "";

    private JScrollPane jScrollPane = null;

    private JProgressBar jProgressBar = null;

    private JLabel cadenaConexionJLabel = null;

    private JLabel cadenaConexionPeticionJLabel = null;

    private JTextField cadenaConexionJTextField = null;

    private JLabel cadenaPuertoInstTomcatJLabel = null;

    private JTextField cadenaPuertoInstTomcatJTextField = null;
    
//    private JLabel cadenaPuertoControlTomcatJLabel = null;

//    private JTextField cadenaPuertoControlTomcatJTextField = null;

    private JLabel cadenaJdkJLabel = null;

    private JTextField cadenaJdkJTextField = null;
    
    private JLabel cadenaIPMapserverJLabel = null;

    private JTextField cadenaIPMapServerJTextField = null;
    
    private JLabel cadenaPortMapserverJLabel = null;

    private JTextField cadenaPortMapServerJTextField = null;
    
    private JLabel progresoJLabel = null;

	private JCheckBox jCheckBoxUpdateDatabase = null;

	private ButtonGroup jButtobGroupSystemWord = null;

	private JRadioButton jRadioButton32 = null;

	private JRadioButton jRadioButton64 = null;

	private JLabel jLabelSO = null;
    
    private static final String FULL_UPDATE = "Full Update";

    /**
     * This method initializes
     * 
     */
    public UpdateSystemPanel()
        {
            super();
            initialize();
        }

    public void okPressed() throws JDOMException, SQLException,
            ClassNotFoundException, IOException
    {
        SAXBuilder builder = new SAXBuilder();
        
        if (!getJRadioButton32().isSelected() && !getJRadioButton64().isSelected() ){
        	JOptionPane.showMessageDialog( aplicacion.getMainFrame(), aplicacion
                            .getI18nString("UpdateSystemPanel.ChooseSystemWord"));
        	return;
        }

        String updateFile="update.xml";
        String updateXMLFile=System.getProperty("localgis.update.xml");
        if (updateXMLFile!=null)
        	updateFile=updateXMLFile;
        
        URL sourceJarFile = UpdateSystemPanel.class.getResource(updateFile);

        Document doc = builder.build(sourceJarFile);
        Element raiz = doc.getRootElement();
        String httpBase = raiz.getAttributeValue("httpBase");
        String newVersion = raiz.getAttributeValue("version");
        
        versionInformation(newVersion);
        checkReinstalation();
        
        repaintElements();
        
        String actualSystemVersion = UpdateSystemPanel.getActualVersion();

        String geopistaDirectory = getInstallDirectoryPath().getText();

        System.out.println("Directorio de instalacion LocalGis:"+geopistaDirectory);
        //introducimos cadenas de sustitucion en un hastable
        
        Hashtable substitutionStrings = new Hashtable();
        
        if(geopistaDirectory!=null)
        {
            substitutionStrings.put(UpdateSystemFiles.GEOPISTA_ROOT, geopistaDirectory);
        }
        
        if(geopistaDirectory!=null)
        {
            substitutionStrings.put(UpdateSystemFiles.GEOPISTA_ROOT_ENCODED, geopistaDirectory.replaceAll(" ", "+"));
        }

        
        if(geopistaDirectory!=null)
        {
            substitutionStrings.put(UpdateSystemFiles.DIR_ORTOFOTOS, geopistaDirectory);
        }
            
        if(geopistaDirectory!=null)
        {
            String cadenaConexion = cadenaConexionJTextField.getText();
            substitutionStrings.put(UpdateSystemFiles.URL_BD, cadenaConexion);
            int indexHost = cadenaConexion.indexOf("//")+2;
            int lastIndexHost = cadenaConexion.indexOf(":", indexHost);
            int indexPort = lastIndexHost + 1;
            int lastIndexPort = cadenaConexion.indexOf("/", indexPort);
            int indexNameDB = lastIndexPort + 1;
            substitutionStrings.put(UpdateSystemFiles.HOST_DB, cadenaConexion.substring(indexHost, lastIndexHost));
            substitutionStrings.put(UpdateSystemFiles.PORT_DB, cadenaConexion.substring(indexPort, lastIndexPort));
            substitutionStrings.put(UpdateSystemFiles.NAME_DB, cadenaConexion.substring(indexNameDB));
        }

        if(geopistaDirectory!=null)
        {
            substitutionStrings.put(UpdateSystemFiles.PASSWORD, new String(geopistaPasswordJTextField.getPassword()));
        }

        if(geopistaDirectory!=null)
        {
            substitutionStrings.put(UpdateSystemFiles.PUERTO_INST, this.cadenaPuertoInstTomcatJTextField.getText());
        }

//        if(geopistaDirectory!=null)
//        {
//            substitutionStrings.put(UpdateSystemFiles.PUERTO_CONTROL, this.cadenaPuertoControlTomcatJTextField.getText());
//        }

//        if(geopistaDirectory!=null)
//        {
//            substitutionStrings.put(UpdateSystemFiles.TOMCAT, UpdateSystemFiles.TOMCAT_DIR);
//        }

        if(geopistaDirectory!=null)
        {
            substitutionStrings.put(UpdateSystemFiles.PATH_JDK, this.cadenaJdkJTextField.getText());
        }

        if(geopistaDirectory!=null)
        {
            substitutionStrings.put(UpdateSystemFiles.IP_MAPSERVER, this.cadenaIPMapServerJTextField.getText());
        }
        
        if(geopistaDirectory!=null)
        {
            substitutionStrings.put(UpdateSystemFiles.PORT_MAPSERVER, this.cadenaPortMapServerJTextField.getText());
        }
        if (geopistaDirectory!=null){
        	if (getJRadioButton32().isSelected()){
        		substitutionStrings.put(UpdateSystemFiles.MAPSERVER_FILE, aplicacion.getString("localgis.mapserver32.filename"));
        	}
        	else if (getJRadioButton64().isSelected()){
        		substitutionStrings.put(UpdateSystemFiles.MAPSERVER_FILE, aplicacion.getString("localgis.mapserver64.filename"));
        	}
        	
        }
        
        if (geopistaDirectory != null){
        	substitutionStrings.put(UpdateSystemFiles.TOMCAT_DIRECTORY, this.jTextFieldTomcatDirectory.getText());
        }

        aplicacion.getBlackboard().put(UpdateSystemFiles.SUBSTITUTIONS_STRINGS, substitutionStrings);

        String os = System.getProperty("os.name");
        if (os.toUpperCase().indexOf("WINDOWS") == -1)
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("UpdateSystemPanel.DetenerServicios"));
        }

        getJProgressBar().setStringPainted(true);

        updateProgressMessage("UpdateSystemPanel.ComenzandoActualizacion", true);

        updateSystemFiles.stopTomcatService();
        updateSystemFiles.stopMapServerService();
        //System.out.println("Mapserver parado");
        try
        {
        	
            //Tendré una serie de módulos. Algunos de estos habrá que instalar las versiones con
        	//install=yes y en los que no tenga este atributo tendré que actualizar. 
        	List modulos = raiz.getChildren();
        	Iterator itModulos = modulos.iterator();
            Connection connection = null;
        	while (itModulos.hasNext()){
        		Element modulo = (Element)itModulos.next();
        		try{
		        	if (modulo.getName().equals("Database")){
		        		updateSystemFiles.startTomcatService();
		        		updateDatabase.updateDatabase(doc, getCadenaConexionJTextField().getText()
		                                .trim(), password, getJProgressBar(), 23, getJCheckBoxUpdateDatabase().isSelected());
        	        	updateProgressMessage("UpdateSystemPanel."+modulo.getName()+ "Actualizado", true);
		        	}
		        	
		        		else{//Resto de módulos que no son la base de datos
			        	List versions = modulo.getChildren("version");
			        	Element fileVerify = modulo.getChild("fileVerify");
		        		String basePath = ((Element)modulo.getChild("basePath")).getText();
        	            String nombreModulo = ((Element)modulo.getChild("moduleName")).getText();
        	            Element webApps = modulo.getChild("webApp");
        	            /*
                         * Creamos una conexion, creamos una transaccion y se la pasamos al
                         * updateSystemFiles.updateFileVersion() porque es
                         * posible que se necesite alguna modificacion de datos
                         */
        	            try {
        	                connection = updateDatabase.getConnection(getCadenaConexionJTextField().getText().trim(), password);
        	                connection.setAutoCommit(false);
        	                String mensaje = updateSystemFiles.updateFileVersion(nombreModulo,webApps, fileVerify.getText(), versions, geopistaDirectory,
        	                        newVersion, httpBase, getJProgressBar(), 8, basePath, connection, this.jTextFieldTomcatDirectory.getText());
        	                /*
        	                 * Si el mensaje es vacion entonces ha ido bien la cosa y hacemos el commit. Si no es vacio hacemos el rollback
        	                 */
        	                if (!mensaje.equals("")) {
        	                    updateProgressMessage(mensaje, false);
        	                    connection.rollback();
        	                } else {
        	                    connection.commit();
        	                }
                        } catch (ClassNotFoundException e) {
                            System.out.println("Driver de la base de datos no encontrado");
                            e.printStackTrace();
                        } catch (SQLException e) {
                            System.out.println("Error de SQL");
                            e.printStackTrace();
        	            }
			        }
        			updateProgressMessage("UpdateSystemPanel."+modulo.getName()+ "Actualizado", true);
        		}catch (Exception e)
        		{
        			e.printStackTrace();
        			updateProgressMessage("UpdateSystemPanel.Problemas "+modulo.getName(), false);
        			UpdateSystemFiles.updateProgressBar(getJProgressBar(), 8);
        		}
        	}
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexion con la base de datos");
                e.printStackTrace();
            }
        	
		}catch (Exception e)
		{
			e.printStackTrace();
			updateProgressMessage("UpdateSystemPanel.Problemas en la actualización", false);
			UpdateSystemFiles.updateProgressBar(getJProgressBar(), 8);
		}

        UpdateSystemPanel.setNewSystemVersion(String.valueOf(newVersion));

        Object[] possibleValues = { aplicacion.getI18nString("UpdateSystemPanel.Borrar"),
                aplicacion.getI18nString("UpdateSystemPanel.NoBorrar") };
        int deleteTempFiles = JOptionPane
                .showOptionDialog(
                        aplicacion.getMainFrame(),
                        aplicacion
                                .getI18nString("UpdateSystemPanel.ActualizacionTerminadaExito"),
                        aplicacion
                                .getI18nString("UpdateSystemPanel.AplicacionActualizada"),
                        0, JOptionPane.QUESTION_MESSAGE, null, possibleValues,
                        possibleValues[0]);

        if (deleteTempFiles == 0)
        {
            try
            {
                updateSystemFiles.deleteTempFiles(geopistaDirectory);
            } catch (IOException e)
            {
                JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                        .getI18nString("UpdateSystemPanel.ErrorBorrandoTemporal"));
            }
        }

        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                .getI18nString("UpdateSystemPanel.ReiniciarSistema"), aplicacion
                .getI18nString("UpdateSystemPanel.ActualizacionTerminada"),
                JOptionPane.INFORMATION_MESSAGE);

    }

    public String validateInput()
    {
    	String validate=System.getProperty("localgis.update.validate");
    	if ((validate!=null) && (validate.equals("false")))
    			return null;
    			
        try
        {
            updateSystemFiles.testConnection();
        } catch (IOException e)
        {
            return aplicacion.getI18nString("UpdateSystemPanel.NoConexion");
        } catch (JDOMException e)
        {
            return aplicacion.getI18nString("UpdateSystemPanel.ErrorXMLActualizacion");
        }

        if (getInstallDirectoryPath().getText() == null
                || getInstallDirectoryPath().getText().trim().equals(""))
        {
            return aplicacion
                    .getI18nString("UpdateSystemPanel.ElDirectorioNoPuedeSerNulo");
        } else
        {
            String directoryPathString = getInstallDirectoryPath().getText().trim();
            File directoryPathStringFile = new File(directoryPathString);
            if (!directoryPathStringFile.exists())
            {
                return aplicacion
                        .getI18nString("UpdateSystemPanel.ElDirectorioEspecificadoNoExiste");
            }

           /* if (updateSystemFiles.verifyAllSystemInstall(directoryPathString) == false)
            {
                return aplicacion
                        .getI18nString("UpdateSystemPanel.ElDirectorioNoEsGeopista");
            }*/

        }
        if (this.getJdkJTextField().getText() == null
                || this.getJdkJTextField().getText().trim().equals(""))
        {
            return aplicacion
                    .getI18nString("UpdateSystemPanel.ElDirectorioJdkNoPuedeSerNulo");
        } else
        {
            String directoryPathString = getJdkJTextField().getText().trim();
            File directoryPathStringFile = new File(directoryPathString);
            if (!directoryPathStringFile.exists())
            {
                return aplicacion
                        .getI18nString("UpdateSystemPanel.ElDirectorioJdkEspecificadoNoExiste");
            }

        }

        if (getCadenaConexionJTextField().getText() == null
                || getCadenaConexionJTextField().getText().trim().equals(""))
        {
            return aplicacion.getI18nString("UpdateSystemPanel.CadenaConexionNoNula");
        }

        char[] passwordChar = getGeopistaPasswordJTextField().getPassword();

        if (passwordChar != null)
        {
            password = String.valueOf(passwordChar);
        }
        if (password == null || password.trim().equals(""))
        {
            return aplicacion.getI18nString("UpdateSystemPanel.ElPasswordNoPuedeSerNulo");
        } else
        {
            try
            {
                updateDatabase.getConnection(getCadenaConexionJTextField().getText()
                        .trim(), password);
            } catch (Exception e)
            {

                return aplicacion
                        .getI18nString("UpdateSystemPanel.problemasConexionBaseDatos");
            }
        }

        return null;
    }

    private void initialize()
    {
        cadenaConexionJLabel = new JLabel();
        cadenaConexionPeticionJLabel = new JLabel();
        cadenaPuertoInstTomcatJLabel = new JLabel();
//        cadenaPuertoControlTomcatJLabel = new JLabel();
        cadenaJdkJLabel = new JLabel();
        progresoJLabel = new JLabel();
        messagePasswordJLabel = new JLabel();
        geopistaPasswordJLabel = new JLabel();
        installDirectoryPathLabel = new JLabel();
        messageDirectoryLabel = new JLabel();
        cadenaIPMapserverJLabel = new JLabel();
        cadenaPortMapserverJLabel = new JLabel();
        this.setLayout(null);
        this.setSize(634, 535);
        installDirectoryPathLabel.setBounds(25, 39, 147, 23);
        installDirectoryPathLabel.setText(aplicacion
                .getI18nString("UpdateSystemPanel.DirectorioInstalacion"));
        messageDirectoryLabel.setBounds(25, 13, 331, 23);
        messageDirectoryLabel.setText(aplicacion
                .getI18nString("UpdateSystemPanel.SeleccioneDirectorioInstalacion"));
        geopistaPasswordJLabel.setBounds(25, 92, 144, 23);
        geopistaPasswordJLabel.setText(aplicacion
                .getI18nString("UpdateSystemPanel.PasswordBaseDatos"));
        messagePasswordJLabel.setBounds(25, 66, 473, 23);
        messagePasswordJLabel.setText(aplicacion
                .getI18nString("UpdateSystemPanel.IntroduzcaPasswordUsuarioGeopista"));
        cadenaConexionJLabel.setBounds(25, 119, 433, 23);
        cadenaConexionJLabel.setText(aplicacion
                .getI18nString("UpdateSystemPanel.IntroduzcaCadenaConexion"));
        cadenaConexionPeticionJLabel.setBounds(25, 147, 137, 23);
        cadenaConexionPeticionJLabel.setText(aplicacion
                .getI18nString("UpdateSystemPanel.PeticionCadenaConexion"));
        cadenaPuertoInstTomcatJLabel.setBounds(25, 203, 137, 23);
        cadenaPuertoInstTomcatJLabel.setText(aplicacion
                .getI18nString("UpdateSystemPanel.PeticionPuertoTomcat"));
//        cadenaPuertoControlTomcatJLabel.setBounds(25, 175, 137, 23);
//        cadenaPuertoControlTomcatJLabel.setText(aplicacion
//                .getI18nString("UpdateSystemPanel.PeticionPuertoControlTomcat"));
        cadenaJdkJLabel.setBounds(25, 231, 137, 23);
        cadenaJdkJLabel.setText(aplicacion
                .getI18nString("UpdateSystemPanel.DirectorioJDK"));
        cadenaIPMapserverJLabel.setBounds(25, 259, 137, 23);
        cadenaIPMapserverJLabel.setText(aplicacion
                .getI18nString("UpdateSystemPanel.IPMapserver"));
        cadenaPortMapserverJLabel.setBounds(25, 287, 137, 23);
        cadenaPortMapserverJLabel.setText(aplicacion
                .getI18nString("UpdateSystemPanel.PuertoMapserver"));
        
        //CheckBox para preguntar si se desea actualizar o no la base de datos
        
        
        
        
        
        progresoJLabel.setBounds(25, 370, 211, 18);
        progresoJLabel.setText(aplicacion
                .getI18nString("UpdateSystemPanel.ProgresoActualizacion"));
        this.add(getInstallDirectoryPath(), null);
        this.add(installDirectoryPathLabel, null);
        this.add(messageDirectoryLabel, null);
        this.add(getSelectDirectoryJButton(), null);
        this.add(getSelectJdkJButton(), null);
        this.add(geopistaPasswordJLabel, null);
        this.add(getGeopistaPasswordJTextField(), null);
        this.add(messagePasswordJLabel, null);

        this.add(getJScrollPane(), null);
        this.add(getJProgressBar(), null);
        this.add(cadenaConexionJLabel, null);
        this.add(cadenaConexionPeticionJLabel, null);
        this.add(getCadenaConexionJTextField(), null);
        this.add(cadenaPuertoInstTomcatJLabel, null);
        this.add(getJLabelTomcatDirectory(), null);
        this.add(getJTextFieldTomcatDirectory(), null);
        this.add(getJButtonTomcatDirectory(), null);
//        this.add(cadenaPuertoControlTomcatJLabel, null);
        this.add(getPuertoInstTomcatJTextField(), null);
//        this.add(getPuertoControlTomcatJTextField(), null);
        this.add(cadenaJdkJLabel, null);
        this.add(getJdkJTextField(), null);
        this.add(cadenaIPMapserverJLabel, null);
        this.add(getIPMapserverJTextField(), null);
        this.add(cadenaPortMapserverJLabel, null);
        this.add(getPortMapserverJTextField(), null);
        this.add(getJCheckBoxUpdateDatabase(), null);
        this.add(getJLabelSO(),null);
        this.add(getJRadioButton32(),null);
        this.add(getJRadioButton64(),null);
        this.add(progresoJLabel, null);

        getJProgressMessageEditorPane().setEditable(false);
        getJCheckBoxUpdateDatabase().setSelected(true);
        // getJTextArea().setEnabled(false);
        getJProgressMessageEditorPane().setBackground(this.getBackground());
        getJProgressMessageEditorPane().setAutoscrolls(true);
        getJProgressMessageEditorPane().setBorder(
                BorderFactory.createLineBorder(Color.BLACK));
        getJProgressMessageEditorPane().setContentType("text/html");
        getJProgressBar().setStringPainted(false);

        String url = aplicacion.getString("conexion.url");
        if (url != null)
        {
            getCadenaConexionJTextField().setText(url);
        }

        String os = System.getProperty("os.name");

        if (os.toUpperCase().indexOf("WINDOWS") != -1)
        {
            getInstallDirectoryPath().setText(
                    updateSystemFiles.DEFAULT_WINDOWS_INSTALL_PATH);
        } else
        {
            getInstallDirectoryPath().setText(
                    updateSystemFiles.DEFAULT_LINUX_INSTALL_PATH);
        }

    }
    
    private JCheckBox getJCheckBoxUpdateDatabase() {
		if (jCheckBoxUpdateDatabase == null) {
			jCheckBoxUpdateDatabase   = new JCheckBox();
			jCheckBoxUpdateDatabase.setBounds(25, 315, 250, 23);
			jCheckBoxUpdateDatabase.setText(aplicacion.getI18nString("UpdateSystemPanel.UpdateDatabase"));
		}
		return jCheckBoxUpdateDatabase;
    }
   
    private ButtonGroup getJButtobGroupSystemWord(){
    	
    	if (jButtobGroupSystemWord == null){
    		
    		jButtobGroupSystemWord  = new ButtonGroup();
    		jButtobGroupSystemWord.add(getJRadioButton32());
    		jButtobGroupSystemWord.add(getJRadioButton64());
    	}
    	return jButtobGroupSystemWord;
    }
    
    private JRadioButton getJRadioButton32(){
    	
    	if (jRadioButton32 == null){
    		
    		jRadioButton32 = new JRadioButton();
    		jRadioButton32.setBounds(150, 343, 70, 23);
    		jRadioButton32.setText(aplicacion.getI18nString("UpdateSystemPanel.32bits"));
    		jRadioButton32.setSelected(false);
    	}
    	return jRadioButton32;
    }
    
    private JRadioButton getJRadioButton64(){
    	
    	if (jRadioButton64 == null){
    		
    		jRadioButton64 = new JRadioButton();
    		jRadioButton64.setBounds(230, 343, 70, 23);
    		jRadioButton64.setText(aplicacion.getI18nString("UpdateSystemPanel.64bits"));
    		jRadioButton64.setSelected(false);
    	}
    	return jRadioButton64;
    }
    
    private JLabel getJLabelSO(){
    	
    	if (jLabelSO == null){
    		
    		jLabelSO  = new JLabel();
    		jLabelSO.setBounds(25,343,120,23);
    		jLabelSO.setText(aplicacion.getI18nString("UpdateSystemPanel.SO"));
    	}
    	return jLabelSO;
    }

    private JLabel getJLabelTomcatDirectory(){
    	
    	if (jLabelTomcatDirectory == null){
    		
    		jLabelTomcatDirectory  = new JLabel();
    		jLabelTomcatDirectory.setBounds(25,175,175,23);
    		jLabelTomcatDirectory.setText(aplicacion.getI18nString("UpdateSystemPanel.TomcatDirectory"));
    	}
    	return jLabelTomcatDirectory;
    }

    /**
     * This method initializes jTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextField getInstallDirectoryPath()
    {
        if (installDirectoryPath == null)
        {
            installDirectoryPath = new JTextField();
            installDirectoryPath.setBounds(174, 39, 327, 23);
        }
        return installDirectoryPath;
    }
    
    private JTextField getJTextFieldTomcatDirectory()
    {
        if (jTextFieldTomcatDirectory == null)
        {
        	jTextFieldTomcatDirectory = new JTextField();
        	jTextFieldTomcatDirectory.setBounds(205, 175, 300, 23);
        }
        return jTextFieldTomcatDirectory;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getSelectDirectoryJButton()
    {

        if (selectDirectoryJButton == null)
        {
            selectDirectoryJButton = new JButton();
            selectDirectoryJButton.setBounds(516, 39, 38, 23);
            selectDirectoryJButton.setIcon(IconLoader.icon("abrir.gif"));
            selectDirectoryJButton.addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent e)
                    {
                        File file = new File(getInstallDirectoryPath().getText());
                        JFileChooser fch = new JFileChooser(file);
                        fch.setMultiSelectionEnabled(false);
                        fch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int resp = fch.showOpenDialog(UpdateSystemPanel.this);
                        File selFil = fch.getSelectedFile();
                        if (resp == JFileChooser.APPROVE_OPTION && selFil != null)
                        {
                        	String path = selFil.getAbsolutePath();
                            getInstallDirectoryPath().setText(changePathToUnix(path));
                        }
                    }
                });
        }

        return selectDirectoryJButton;
    }
    
    private JButton getJButtonTomcatDirectory()
    {

        if (jButtonTomcatDirectory == null)
        {
        	jButtonTomcatDirectory = new JButton();
        	jButtonTomcatDirectory.setBounds(510, 175, 38, 23);
        	jButtonTomcatDirectory.setIcon(IconLoader.icon("abrir.gif"));
        	jButtonTomcatDirectory.addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent e)
                    {
                        File file = new File(getInstallDirectoryPath().getText() + "/apache-tomcat-6.0.14");
                        JFileChooser fch = new JFileChooser(file);
                        fch.setMultiSelectionEnabled(false);
                        fch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int resp = fch.showOpenDialog(UpdateSystemPanel.this);
                        File selFil = fch.getSelectedFile();
                        if (resp == JFileChooser.APPROVE_OPTION && selFil != null)
                        {
                        	String path = selFil.getAbsolutePath();
                            getJTextFieldTomcatDirectory().setText(changePathToUnix(path));
                        }
                    }
                });
        }

        return jButtonTomcatDirectory;
    }

    private JButton getSelectJdkJButton()
    {

        if (selectJdkJButton == null)
        {
        	selectJdkJButton = new JButton();
        	selectJdkJButton.setBounds(516, 231, 38, 23);
        	selectJdkJButton.setIcon(IconLoader.icon("abrir.gif"));
        	selectJdkJButton.addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent e)
                    {
                        File file = new File(getJdkJTextField().getText());
                        JFileChooser fch = new JFileChooser(file);
                        fch.setMultiSelectionEnabled(false);
                        fch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int resp = fch.showOpenDialog(UpdateSystemPanel.this);
                        File selFil = fch.getSelectedFile();
                        if (resp == JFileChooser.APPROVE_OPTION && selFil != null)
                        {
                        	getJdkJTextField().setText(selFil.getAbsolutePath());
                        }
                    }
                });
        }

        return selectJdkJButton;
    }
    
    /**
     * This method initializes jPasswordField
     * 
     * @return javax.swing.JPasswordField
     */
    private JPasswordField getGeopistaPasswordJTextField()
    {
        if (geopistaPasswordJTextField == null)
        {
            geopistaPasswordJTextField = new JPasswordField();
            geopistaPasswordJTextField.setBounds(173, 92, 203, 23);
        }
        return geopistaPasswordJTextField;
    }

    /**
     * This method initializes jTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JEditorPane getJProgressMessageEditorPane()
    {
        if (progressMessageEditorPane == null)
        {
            progressMessageEditorPane = new JEditorPane();
        }
        return progressMessageEditorPane;
    }

    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane()
    {
        if (jScrollPane == null)
        {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(25, 400, 540, 122);
            jScrollPane.setViewportView(getJProgressMessageEditorPane());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jProgressBar
     * 
     * @return javax.swing.JProgressBar
     */
    private JProgressBar getJProgressBar()
    {
        if (jProgressBar == null)
        {
            jProgressBar = new JProgressBar();
            jProgressBar.setBounds(25, 530, 545, 17);
        }
        return jProgressBar;
    }

    /**
     * This method initializes jTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCadenaConexionJTextField()
    {
        if (cadenaConexionJTextField == null)
        {
            cadenaConexionJTextField = new JTextField();
            cadenaConexionJTextField.setBounds(173, 147, 327, 23);
        }
        return cadenaConexionJTextField;
    }

    private JTextField getPuertoInstTomcatJTextField()
    {
        if (cadenaPuertoInstTomcatJTextField == null)
        {
        	cadenaPuertoInstTomcatJTextField = new JTextField();
        	cadenaPuertoInstTomcatJTextField.setBounds(173, 203, 50, 23);
        	cadenaPuertoInstTomcatJTextField.setText("8080");
        }
        return cadenaPuertoInstTomcatJTextField;
    }

//    private JTextField getPuertoControlTomcatJTextField()
//    {
//        if (cadenaPuertoControlTomcatJTextField == null)
//        {
//        	cadenaPuertoControlTomcatJTextField = new JTextField();
//        	cadenaPuertoControlTomcatJTextField.setBounds(173, 175, 50, 23);
//        	cadenaPuertoControlTomcatJTextField.setText("8006");
//        }
//        return cadenaPuertoControlTomcatJTextField;
//    }

    private JTextField getJdkJTextField()
    {
        if (cadenaJdkJTextField == null)
        {
        	cadenaJdkJTextField = new JTextField();
        	cadenaJdkJTextField.setBounds(173, 231, 327, 23);
        }
        return cadenaJdkJTextField;
    }
    
    private JTextField getIPMapserverJTextField()
    {
        if (cadenaIPMapServerJTextField== null)
        {
        	cadenaIPMapServerJTextField = new JTextField();
        	cadenaIPMapServerJTextField.setBounds(173, 259, 327, 23);
        	cadenaIPMapServerJTextField.setText("localhost");
        }
        return cadenaIPMapServerJTextField;
    }

    private JTextField getPortMapserverJTextField()
    {
        if (cadenaPortMapServerJTextField== null)
        {
            cadenaPortMapServerJTextField = new JTextField();
            cadenaPortMapServerJTextField.setBounds(173, 287, 50, 23);
            cadenaPortMapServerJTextField.setText("80");
        }
        return cadenaPortMapServerJTextField;
    }

    public static void main(String[] args)
    {

        JFrame fr = new JFrame("Test"); //$NON-NLS-1$
        // fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setSize(1000, 500);
        fr.getContentPane().add(new UpdateSystemPanel());
        fr.setVisible(true);
    }

    public static String getActualVersion()
    {

        Boolean fullUpdate = (Boolean)AppContext.getApplicationContext().getBlackboard().get(FULL_UPDATE);
        if(fullUpdate!=null && fullUpdate.booleanValue()) return "0";
       
        String actualSystemVersion = AppContext.getApplicationContext().getString(
                "SYSTEM_VERSION");
        if (actualSystemVersion == null)
        {
            AppContext.getApplicationContext().setUserPreference("SYSTEM_VERSION", "0");
            actualSystemVersion = "0";
        }

        return actualSystemVersion;
    }

    public static void setNewSystemVersion(String newSystemVersion)
    {
        AppContext.getApplicationContext().setUserPreference("SYSTEM_VERSION",
                newSystemVersion);
    }
    
    private void checkReinstalation()
    {
        
        aplicacion.getBlackboard().put(FULL_UPDATE,null);
        String actualVersion = getActualVersion();
        if(actualVersion.equals("0")) return;
        
        String showDate = actualVersion.substring(6,8) + "/" + actualVersion.substring(4,6) + "/" + actualVersion.substring(0,4);

        Object[] testArgs = {showDate};

        MessageFormat form = new MessageFormat(
                aplicacion.getI18nString("UpdateSystemPanel.ComprobacionReinstalación"));
        
        Object[] possibleValues = { aplicacion.getI18nString("UpdateSystemPanel.Completa"),
                aplicacion.getI18nString("UpdateSystemPanel.Pacial"), aplicacion.getI18nString("OKCancelPanel.Cancel") };
        int reinstalationSystem = JOptionPane
                .showOptionDialog(
                        aplicacion.getMainFrame(),
                        form.format(testArgs),
                        aplicacion
                                .getI18nString("UpdateSystemPanel.TipoActualizacion"),
                        0, JOptionPane.QUESTION_MESSAGE, null, possibleValues,
                        possibleValues[0]);
        
        if(reinstalationSystem==0)
        {
            aplicacion.getBlackboard().put(FULL_UPDATE,new Boolean(true));
        }else
        {
            if(reinstalationSystem==2) System.exit(0);
        }
    }

    public void updateProgressMessage(String message, boolean updateSuccessful)
    {
        String textColor = "#006600";
        if (!updateSuccessful)
        {
            textColor = "#FF0000";
        }

        progressMessage += "<font color=\"" + textColor + "\">"
                + aplicacion.getI18nString(message) + "</font><br>";
        getJProgressMessageEditorPane().setText(progressMessage);
        getJProgressMessageEditorPane().update(getJProgressMessageEditorPane().getGraphics());
   }
    
    private void repaintElements(){
        this.getPuertoInstTomcatJTextField().update(getPuertoInstTomcatJTextField().getGraphics());
        this.cadenaPuertoInstTomcatJLabel.update(cadenaPuertoInstTomcatJLabel.getGraphics());
//        this.getPuertoControlTomcatJTextField().update(getPuertoControlTomcatJTextField().getGraphics());
//        this.cadenaPuertoControlTomcatJLabel.update(cadenaPuertoControlTomcatJLabel.getGraphics());
        this.getJdkJTextField().update(getJdkJTextField().getGraphics());
        this.cadenaJdkJLabel.update(cadenaJdkJLabel.getGraphics());
        this.getSelectJdkJButton().update(getSelectJdkJButton().getGraphics());
        this.progresoJLabel.update(progresoJLabel.getGraphics());
    }
        
        private void versionInformation(String actualVersion)
        {
            if (Integer.parseInt(getActualVersion())<=20080204){           
            	Object[] possibleValues = { aplicacion.getI18nString("DatosPatrimonio.Aceptar"),
                aplicacion.getI18nString("DatosPatrimonio.Cancel")};
                int datosPatrimonio = JOptionPane
                    .showOptionDialog(
                            aplicacion.getMainFrame(),
                            aplicacion
                            .getI18nString("UpdateSystemPanel.DatosPatrimonio"),
                            aplicacion
                                    .getI18nString("LoadSystemLayerDialog"),
                            0, JOptionPane.QUESTION_MESSAGE, null, possibleValues,
                            possibleValues[0]);
                if (datosPatrimonio == 1){
                	System.exit(0);
                }
            }
            String showDate = actualVersion.substring(6,8) + "/" + actualVersion.substring(4,6) + "/" + actualVersion.substring(0,4);

            Object[] testArgs = {showDate};
            MessageFormat form = new MessageFormat(
                    aplicacion.getI18nString("UpdateSystemPanel.updateVersionInformation"));
            
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),form.format(testArgs));
        }
        
        /**
         * Cambia las barras de un path de formato de Windows a formato de Unix
         */
        private String changePathToUnix(String path){
        	int n = path.length();
        	StringBuffer st = new StringBuffer();
        	for (int i=0;i<n;i++){
        		if (path.charAt(i) == '\\')
        			st.append('/');
        		else
        			st.append(path.charAt(i));
        	}
        	return st.toString();
        }
   

} // @jve:decl-index=0:visual-constraint="7,10"

