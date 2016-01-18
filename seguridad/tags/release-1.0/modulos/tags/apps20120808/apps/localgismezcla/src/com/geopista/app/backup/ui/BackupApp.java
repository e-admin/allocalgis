/**
 * Módulo para la creación de Backup en modo gráfico
 *
*/
package com.geopista.app.backup.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.geopista.app.backup.BackupAdapter;
import com.geopista.app.backup.BackupConfig;
import com.geopista.app.backup.BackupOracle;
import com.geopista.app.backup.BackupPostgres;
import com.geopista.app.backup.BackupPreferences;
import com.geopista.app.backup.InformacionTabla;
import com.geopista.app.backup.SecurityManager;
import com.vividsolutions.jump.I18N;

public class BackupApp extends JFrame{
	
    private BackupAdapter backupAdapter;
    private final BackupPreferences backupPreferences = BackupPreferences.getInstance();
    public static void main(String[] args) {

    	BackupApp backupApp = new BackupApp();
    	backupApp.execute();
    	backupApp.dispose();
    	System.exit(0);

    }
    
    public BackupApp() {
    
    }
    
    public void execute() {
        System.out.println("Inicio Backup...");
        String url = backupPreferences.getUrl();
        
        /*
         *  Determinamos a traves de la información de preferencia 
         *  el tipo de base de datos de geopista y en funcion de 
         *  ello instanciamos un tipo de backup u otro
         *  
         */
        
        if (url.indexOf("jdbc:postgresql")>=0) {
            backupAdapter = new BackupPostgres();
        }else { 
        	if (url.indexOf("jdbc:oracle")>=0) {
        		backupAdapter = new BackupOracle();
                    
            }
        }
        /*
         * Cargamos la clase del driver que vamos a usar para
         * la conexión jdbc
         * 
         */
        try {
            Class.forName(backupAdapter.obtenerClaseDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        
        /*
         * Inicializamos el interfaz de usuario
         * 
         */
        
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.showGrowBox", "true");
        if (UIManager.getLookAndFeel() != null
                && UIManager.getLookAndFeel().getClass().getName().equals(UIManager.getSystemLookAndFeelClassName())) {
            return;
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        //*******************************
        //Cargamos los recursos
        //*******************************
        Locale loc = I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.backup.ui.language.BackupSupraMunicipali18n", loc, this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("BackupSupraMunicipal", bundle);
        

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             
        LoginDialog dialog = new LoginDialog(this);
        centreOnScreen(dialog);
        dialog.setVisible(true);
        int i = 0;
        boolean isAuthorized = false;
        while (!dialog.isCanceled()&&(i<3)&&(!isAuthorized)) {
            isAuthorized = SecurityManager.isAuthorized(dialog.getLogin(), dialog.getPassword());
            if (!isAuthorized) {
                dialog.setErrorLabel(I18N.get("BackupSupraMunicipal", "geopista.app.backup.noAutorizado"));
                dialog.setVisible(true);
            }
            i++;
        }
        if (!isAuthorized) {
            return;
        }
        backupPreferences.setUser(dialog.getLogin());
        backupPreferences.setPassword(dialog.getPassword());
        Connection connection = getConnection();
        if (connection==null) {
            return;
        }   
        dialog = null;     
        
        String[] entidades = new String[0];
        Hashtable hashtable = null;
        try {
            hashtable = backupAdapter.obtenerEntidades(connection);
            hashtable.put(I18N.get("BackupSupraMunicipal", "geopista.app.backup.todasEntidades"), new Integer(-1));
            Collection collection = hashtable.keySet();
            entidades = (String[])collection.toArray(entidades);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        EntidadDialog entidadDialog = new EntidadDialog(this, entidades);
        centreOnScreen(entidadDialog);
        entidadDialog.setVisible(true);
        if (entidadDialog.isCancelled()) {
            return;
        }
        String entidadSelected = entidadDialog.getEntidadSelected();
        Integer id = (Integer) hashtable.get(entidadSelected);
        try {

            File directorySelected = entidadDialog.getDirectorySelected();
            System.out.println(directorySelected.getAbsolutePath());

            File error = new File(directorySelected, BackupConfig.FILEERROR);

			error.createNewFile();

            File backupMunicipio = new File(directorySelected, BackupConfig.FILEENTIDAD);
            backupMunicipio.createNewFile();
            
            File backupComun = new File(directorySelected, BackupConfig.FILECOMUN);
            backupComun.createNewFile();

            FileOutputStream backupErrorFile = new FileOutputStream(error);
            FileOutputStream backupMunicipioFile = new FileOutputStream(backupMunicipio);
            FileOutputStream backupComunFile = new FileOutputStream(backupComun);

            PrintStream printStreamError = new PrintStream(backupErrorFile);
            PrintStream printStreamSalidaMunicipio = new PrintStream(backupMunicipioFile);
            PrintStream printStreamSalidaComun = new PrintStream(backupComunFile);

            doBackup(connection, printStreamSalidaMunicipio, printStreamSalidaComun, printStreamError, id.intValue());

            entidadDialog = null;

            backupErrorFile.close();
            printStreamError.close();

            generarFicherosBackupUTF8(directorySelected);
            
            connection.close();
            JOptionPane.showMessageDialog(this, I18N.get("BackupSupraMunicipal", "geopista.app.backup.backupFinalizado"), I18N.get("BackupSupraMunicipal", "geopista.app.backup.informacion"), JOptionPane.INFORMATION_MESSAGE);           
		}   catch (Exception e)  {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, I18N.get("BackupSupraMunicipal", "geopista.app.backup.backupAbortado"), I18N.get("BackupSupraMunicipal", "geopista.app.backup.error"), JOptionPane.ERROR_MESSAGE);
		} 
        System.out.println("...Fin Backup."); 
    }
    
    /**
     * Generamos los ficheros en formato UTF-8 por si se quieren 
     * @param directorySelected
     */
    private void generarFicherosBackupUTF8(File directorySelected){
		try {
			File backupMunicipio = new File(directorySelected, BackupConfig.FILEENTIDAD);			
			File backupMunicipio_UTF8 = new File(directorySelected, BackupConfig.FILEENTIDAD_UTF8);
			backupMunicipio_UTF8.createNewFile();
			File backupComun = new File(directorySelected, BackupConfig.FILECOMUN);
			File backupComun_UTF8 = new File(directorySelected, BackupConfig.FILECOMUN_UTF8);
			backupComun.createNewFile();
			convertFile(backupMunicipio,backupMunicipio_UTF8);			        
			convertFile(backupComun,backupComun_UTF8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			        
    }
    
    /**
     * Lo convertimos a UTF-8
     * @param backupFile
     * @param backupFileUTF8
     */
    private void convertFile(File backupFile,File backupFileUTF8){
    	
    	try {
			BufferedReader reader= new BufferedReader(new FileReader(backupFile));
			
			Writer out = new BufferedWriter(new OutputStreamWriter(
		            new FileOutputStream(backupFileUTF8), "UTF8"));

			String linea= reader.readLine();
			while(linea!=null) { 
				out.write(linea+"\n");
				linea= reader.readLine(); 
			}
			out.close();
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }
    private  void centreOnScreen(Component componentToMove) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        componentToMove.setLocation((screenSize.width -
            componentToMove.getWidth()) / 2,
            (screenSize.height - componentToMove.getHeight()) / 2);
    }
    
    private Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(backupPreferences.getUrl(),backupPreferences.getUser(),backupPreferences.getPassword());
            return connection;
        } catch (SQLException e) {          
            e.printStackTrace();
        }
        return null;
    }
    
      
    private void doBackup(Connection connection, PrintStream printStreamSalidaMunicipio, PrintStream printStreamSalidaComun, PrintStream printStreamError, int idEntidad) throws SQLException {
    	
    	// Ponemos el título a la ventana: 	
    	this.setTitle(I18N.get("BackupSupraMunicipal", "geopista.app.backup.titulo"));
    	
    	// Creamos un area de texto para mostrar la información:
    	JTextArea texto = new JTextArea(10,100);
    	
    	// Lo ponemos para que no se pueda escribir en él:
    	texto.setEditable(false);
    	
    	// Creamos las barras de Scroll en el texto anterior:
        JScrollPane pScroll = new JScrollPane(texto, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        // Colocamos el texto en el panel: 
        getContentPane().add(pScroll, BorderLayout.CENTER);
        
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Escribimos texto y hacemos un salto de linea:
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.realizandoBackup") + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());
        texto.append(BackupConfig.MSGLINEA1 + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength()); 
        /*
         * 1. Obtenemos las tablas comunes de las que hay que hacer backup
         */
        InformacionTabla[] tablasComunes = backupAdapter.obtenerTablasComunes(connection);
        
        // Escribimos texto y hacemos un salto de linea:
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasComunes") + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());
    	
        /*
         * 2. Obtenemos las tablas dependientes del municipio de las que hay que
         * hacer backup
         */
        InformacionTabla[] tablasMunicipio = backupAdapter.obtenerTablasMunicipio(connection);

        // Escribimos texto y hacemos un salto de linea:
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasMunicipio") + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());
 
  	 	printStreamSalidaComun.println(BackupConfig.SQLBEGIN);
  	 	printStreamSalidaMunicipio.println(BackupConfig.SQLBEGIN);
  	 	
  	 	/*
  	 	 * 3. Creamos el script de cosas comunes que puedan hacer falta para realizar el resto de las operaciones 
  	 	 */
        backupAdapter.crearScriptInicial(connection, printStreamSalidaComun, printStreamError);
        
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.creando") + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());
    	    	
        /*
         * 4. Creamos el script correspondiente a las tablas comunes
         */
        
        if ((tablasComunes == null )||(tablasMunicipio == null)) {
            return;
        }
        
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.scriptTablasComunes") + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());
    	
    	
		String municipios = backupAdapter.obtenerStringMunicipiosEntidad(connection, idEntidad);

		
        for (int i = 0; i < tablasComunes.length; i++) {
            InformacionTabla tabla = tablasComunes[i];
            String deshabilitarConstraints = backupAdapter.obtenerSentenciaDeshabilitarConstraints(tabla,connection);
            printStreamSalidaComun.println(deshabilitarConstraints);
            texto.append(deshabilitarConstraints + BackupConfig.NUEVALINEA);
            // Nos posicionamos en la última línea
        	texto.setCaretPosition(texto.getDocument().getLength());        	
        }
        
        for (int i = 0; i < tablasComunes.length; i++) {
            InformacionTabla tabla = tablasComunes[i];
            backupAdapter.crearScript(connection, printStreamSalidaComun, printStreamError, tabla, false, municipios);
            texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.creando")+" "+i+" "+I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasComunes") + BackupConfig.NUEVALINEA);
            // Nos posicionamos en la última línea
        	texto.setCaretPosition(texto.getDocument().getLength());  
        }

        for (int i = 0; i < tablasComunes.length; i++) {
            InformacionTabla tabla = tablasComunes[i];
            String habilitarConstraints = backupAdapter.obtenerSentenciaHabilitarConstraints(tabla,connection);
            printStreamSalidaComun.println(habilitarConstraints);
            texto.append(habilitarConstraints + BackupConfig.NUEVALINEA);
            // Nos posicionamos en la última línea
        	texto.setCaretPosition(texto.getDocument().getLength());                       
        }

        /*
         * Actualizamos las secuencias de las tablas comunes a los ultimos valores
         */
        
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.actualizandoTablasComunes") + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());
    	
        for (int i = 0; i < tablasComunes.length; i++) {
            String sentencia = backupAdapter.obtenerSentenciaActualizarSecuencia(tablasComunes[i]);
            if (sentencia != null) {
                printStreamSalidaComun.println(sentencia);
                texto.append(sentencia + BackupConfig.NUEVALINEA);
                // Nos posicionamos en la última línea
            	texto.setCaretPosition(texto.getDocument().getLength());  
            }
        }

        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.scriptTablasMunicipio") + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());
    	
        for (int i = 0; i < tablasMunicipio.length; i++) {
            InformacionTabla tabla = tablasMunicipio[i];
            String deshabilitarConstraints = backupAdapter.obtenerSentenciaDeshabilitarConstraints(tabla,connection);
            printStreamSalidaMunicipio.println(deshabilitarConstraints);
            texto.append(deshabilitarConstraints + BackupConfig.NUEVALINEA);
            // Nos posicionamos en la última línea
        	texto.setCaretPosition(texto.getDocument().getLength());              
        }       
        
        for (int i = 0; i < tablasMunicipio.length; i++) {
            InformacionTabla tabla = tablasMunicipio[i];
            backupAdapter.crearScript(connection, printStreamSalidaMunicipio, printStreamError, tabla, true, municipios);
            texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.creando")+" "+i+" "+I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasMunicipio")+ BackupConfig.NUEVALINEA);
            // Nos posicionamos en la última línea
        	texto.setCaretPosition(texto.getDocument().getLength());              
        }      
        
        for (int i = 0; i < tablasMunicipio.length; i++) {
            InformacionTabla tabla = tablasMunicipio[i];
            String habilitarConstraints = backupAdapter.obtenerSentenciaHabilitarConstraints(tabla,connection);
            printStreamSalidaMunicipio.println(habilitarConstraints);
            texto.append(habilitarConstraints + BackupConfig.NUEVALINEA);
            // Nos posicionamos en la última línea
        	texto.setCaretPosition(texto.getDocument().getLength());     
        
        }

        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.actualizandoTablasMunicipio") + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());
    	
        for (int i = 0; i < tablasMunicipio.length; i++) {
            String sentencia = backupAdapter.obtenerSentenciaActualizarSecuencia(tablasMunicipio[i]);
            if (sentencia != null) {
                printStreamSalidaMunicipio.println(sentencia);
                
                texto.append(sentencia + BackupConfig.NUEVALINEA);
                // Nos posicionamos en la última línea
            	texto.setCaretPosition(texto.getDocument().getLength());                  
            }
        }    
        texto.append(BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());     
        texto.append(BackupConfig.MSGLINEA2 + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());     
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.exito"));
        
        printStreamSalidaMunicipio.println(BackupConfig.SQLCOMMIT);
        printStreamSalidaComun.println(BackupConfig.SQLCOMMIT); 
    }    
}
