/**
 * ImportarFXCCMasivo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.intercambio.importacion.dialogs.FileSelectPanelFXCCMasivo;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportacionOperations;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.ImagenCatastro;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author alvarosanz
 *
 */
public class ImportarFXCCMasivo extends JPanel implements WizardPanel
{
    private boolean permiso = true;
    
    private AppContext application = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = application.getBlackboard();
    private WizardContext wizardContext; 
    private String nextID = "2";
    
    private static StringBuffer strBuf = new StringBuffer();  
    
    private static JEditorPane jEditorPaneErrores = new JEditorPane();
    private JFileChooser fc = null;
    private JTextField jTextFieldDirectoryName = null;
    
    //  Variables utilizadas para las validaciones
    private boolean continuar = false;       
  
    
    public static final int DIM_X=700;
    public static final int DIM_Y=450;
    
    private static String nameDXF = null;
    private static String nameASC = null;
    
    
    //private static String refCatastralFinca = null;
    //static HashMap hashFicheros_ASC_DFX = new HashMap();
    
    private final static String NOMBRE_FICHERO_ASC = "ficheroASC";
    private final static String NOMBRE_FICHERO_DXF = "ficheroDXF";
    private final static String LST_FICHERO_IMG = "lstIMG";
    
    private static ArrayList listParcelas_BD = new ArrayList();
    private static ArrayList listFX_CC_BD = new ArrayList();
    private static ArrayList listIMAGES_BD = new ArrayList();
    private static ArrayList listParcelas_No_BD = new ArrayList();

   
   
    public ImportarFXCCMasivo()
    {   
        try
        {     
            Locale loc=I18N.getLocaleAsObject();         
            ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Importacioni18n",loc,this.getClass().getClassLoader());
            I18N.plugInsResourceBundle.put("Importacion",bundle);
            jbInit();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception
    {  
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(DIM_X, DIM_Y));
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(application.getMainFrame(), null);
        
        progressDialog.setTitle(I18N.get("Importacion","CargandoDatosIniciales"));
        progressDialog.report(I18N.get("Importacion","CargandoDatosIniciales"));
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
                        	FileSelectPanelFXCCMasivo panel = new FileSelectPanelFXCCMasivo();
                            add(panel,  
                                    new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
                            
                            jTextFieldDirectoryName = panel.getJTextFieldDirectorioImportar();
                            jEditorPaneErrores = panel.getJEditorPaneErrores();
                            
                            panel.getLabelImagen().setIcon(IconLoader.icon(MainCatastro.BIG_PICTURE_LOCATION));
                            panel.getJButtonDirectorioImportar().addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(ActionEvent e)
                                {
                                    btnOpen_actionPerformed(e);
                                }
                                    });
                           
                        } 
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        } 
                        finally
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
    
    public void enteredFromLeft(Map dataMap)
    {
        if(!application.isLogged())
        {
            application.login();
        }
        if(!application.isLogged())
        {
            wizardContext.cancelWizard();
            return;
        }        
               
    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    	// se envian las lista de parcelas y los ficheros ASC y DXF asociados para introducirlos en la 
    	//tabla fxcc
       blackboard.put(ImportarUtils.LISTA_PARCELAS, listParcelas_BD);     
       blackboard.put(ImportarUtils.FILE_TO_IMPORT, listFX_CC_BD);     
       blackboard.put(ImportarUtils.LISTA_IMAGENES, listIMAGES_BD);     
    }
    
    /**
     * Tip: Delegate to an InputChangedFirer.
     * 
     * @param listener
     *            a party to notify when the input changes (usually the
     *            WizardDialog, which needs to know when to update the enabled
     *            state of the buttons.
     */
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
        if (!permiso)
        {
            JOptionPane.showMessageDialog(application.getMainFrame(), application
                    .getI18nString("NoPermisos"));
            return false;
        } 
        else
        {
            if (!continuar)            
                return false;             
            else                         
                return true;
        }
    }
    
    
    
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
    
    private void findDirectorios(File file, ArrayList lstDirectorios){
    	// se almacenan todos los directorios que existan a partir de la ruta indicada
    	if (file.isDirectory()) 
		{
    		lstDirectorios.add(file.getPath());
    		File[] list = file.listFiles();
			for (int i=0; i<list.length; i++) 
			{
				findDirectorios(list[i], lstDirectorios);
			}
		}
    }
    
    private static void findFiles(File file, HashMap hashFicheros_ASC_DFX_LSTIMAGES) throws Exception 
	{
    	if (file.isFile()) 
		{
    		// es un fichero 
			almacenarFicheroEncontrado(file, hashFicheros_ASC_DFX_LSTIMAGES);
    		
		}
	}

    /**
     * se almacenan los ficheros por pares ASC, DXF encontrados en el directorio
     * @param fichero
     * @throws Exception 
     */
    private static void almacenarFicheroEncontrado( File fichero, HashMap hashFicheros_ASC_DFX_LSTIMAGES) throws Exception{
    	
    	if(fichero.getName().toUpperCase().endsWith(".ASC")){
    		hashFicheros_ASC_DFX_LSTIMAGES.put(NOMBRE_FICHERO_ASC,fichero);
    	}
    	else if(fichero.getName().toUpperCase().endsWith(".DXF")){
    		hashFicheros_ASC_DFX_LSTIMAGES.put(NOMBRE_FICHERO_DXF,fichero);
    	}	
    	else if(fichero.getName().toLowerCase().endsWith("jpg") || fichero.getName().toLowerCase().endsWith("gif") ||
    			fichero.getName().toLowerCase().endsWith("bmp") || fichero.getName().toLowerCase().endsWith("png")){
    		
    		//se obtine el fichero imagen
    		ImagenCatastro imagen = new ImagenCatastro();
    		String nombre = GUIUtil.nameWithoutExtension(fichero);
			String extension = getExtension(fichero);
			imagen.setNombre(nombre);
			imagen.setExtension(extension);
			imagen.setTipoDocumento(extension);
			imagen.setFoto(getContenido(fichero));
		
    		if(hashFicheros_ASC_DFX_LSTIMAGES.containsKey(LST_FICHERO_IMG)){
    			
    			ArrayList lstImagenesTmp = (ArrayList)hashFicheros_ASC_DFX_LSTIMAGES.get(LST_FICHERO_IMG);
    			lstImagenesTmp.add(imagen);
    			hashFicheros_ASC_DFX_LSTIMAGES.put(LST_FICHERO_IMG,lstImagenesTmp);
    		}
    		else{
    			
    			ArrayList listaImagenesTmp = new ArrayList();
    			listaImagenesTmp.add(imagen);
    			hashFicheros_ASC_DFX_LSTIMAGES.put(LST_FICHERO_IMG,listaImagenesTmp);
    		}
    	}
    }
    
    public static byte[] getContenido(File file) throws Exception{

        InputStream is= new FileInputStream(file);
        long length= file.length();

        if (length > Integer.MAX_VALUE) throw new ACException("El fichero es demasiado grande.");

        byte[] bytes= new byte[(int)length];

        // Leemos en bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Comprobamos haber leido todos los bytes del fichero
        if (offset < bytes.length) throw new IOException("No se ha podido leer completamente el fichero "+file.getName());

        is.close();

        return bytes;
    }
    
	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
	
    private static void almacenarFicheroBD(ArrayList lstFichAlmacenadosDirectorio){
    	
    	for (int i=0; i<lstFichAlmacenadosDirectorio.size();i++){
    		
    		HashMap hashFicheros_ASC_DFX_LSTIMAGES = (HashMap)lstFichAlmacenadosDirectorio.get(i);
    		
    		File fichASC = (File)hashFicheros_ASC_DFX_LSTIMAGES.get(NOMBRE_FICHERO_ASC);
        	File fichDXF = (File)hashFicheros_ASC_DFX_LSTIMAGES.get(NOMBRE_FICHERO_DXF);
        	ArrayList lstImagenes = (ArrayList)hashFicheros_ASC_DFX_LSTIMAGES.get(LST_FICHERO_IMG);
        	
        	String nombreFichASC = null;
        	if(fichASC != null){
        		nombreFichASC = fichASC.getName().substring(0, fichASC.getName().length()-4);
        	}
    		if(fichDXF!= null){
	        	
	        	String nombreFichDXF = fichDXF.getName().substring(0, fichDXF.getName().length()-4);
	        	
	        	if(nombreFichDXF.equals(nombreFichASC) || (nombreFichDXF != null && nombreFichASC == null)){
	        		
	        		// se busca que el nombre del fichero que corresponde con una referencia catastral
	    			//exista en la tabla de parcela
	    			ImportacionOperations oper = new ImportacionOperations();
	    			try {
	    				String parcela = oper.obtenerParcela(nombreFichDXF);
	        		
	    				if (parcela!= null && !parcela.equals("")){
	    					
	    					FX_CC fxcc = null;
	    		        	if(fichASC != null){
	    		        		fxcc = new FX_CC(fichASC,fichDXF);
	    		        	}
	    		        	else{
	    		        		fxcc = new FX_CC(" ",fichDXF);
	    		        	}
	    		        	

	    					FincaCatastro fincaCatastro = new FincaCatastro();
	    					ReferenciaCatastral refCatastral = new ReferenciaCatastral(nombreFichDXF);
	    					fincaCatastro.setRefFinca(refCatastral);
	    					
	    					listParcelas_BD.add(fincaCatastro);
	    					listFX_CC_BD.add(fxcc);
	    					
	    					// se almacena las imagenes
	    					ArrayList lstImgAux = new ArrayList();
	    					if(lstImagenes != null){
		    					for(int j=0; j< lstImagenes.size();j++){
		    						
		    						ImagenCatastro imagen = (ImagenCatastro)lstImagenes.get(j);
		    						
		    						if(imagen.getNombre().equals(nombreFichDXF)){
		    							lstImgAux.add(imagen);
		    						}
		    						
		    					}
	    					}
	    					listIMAGES_BD.add(lstImgAux);
	    				
	    					
	    				}
	    				else{
	    					
	    					FincaCatastro fincaCatastro = new FincaCatastro();
	    					ReferenciaCatastral refCatastral = new ReferenciaCatastral(nombreFichDXF);
	    					fincaCatastro.setRefFinca(refCatastral);
	    					
	    					listParcelas_No_BD.add(fincaCatastro);
	    				 
	    				}
	    				
	    				
	    			}
	    			catch (Exception e) {
						String aux = "";
					}
	        	}
	        }
    	}
    }
    
    private void btnOpen_actionPerformed(ActionEvent e)  
    {        
        // Se inicializa para cada proceso de importacion
        continuar = false;       
        
        listParcelas_BD.clear();
        listFX_CC_BD.clear();
        listIMAGES_BD.clear();
        listParcelas_No_BD.clear(); 
        
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        fc.setAcceptAllFileFilterUsed(false); // QUITA LA OPCION ALL FILES(*.*)
        String lastFolder=UserPreferenceStore.getUserPreference(ImportarUtils.LAST_IMPORT_DIRECTORY, null, false);
        File currentDirectory=null;
        if (lastFolder!=null)
        	currentDirectory=new File(lastFolder);
                
        fc.setCurrentDirectory(currentDirectory);
        
        int returnVal = fc.showOpenDialog(this);
        
        UserPreferenceStore.setUserPreference(ImportarUtils.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory().getAbsolutePath());
        
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
        	jEditorPaneErrores.removeAll();
        	strBuf = new StringBuffer();
        	
            // Cargamos el fichero que hemos obtenido
            String directorioSeleccionado = fc.getSelectedFile().getPath();
            
            jTextFieldDirectoryName.setText(directorioSeleccionado);
            
            // se obtienen todos los fichero contenidos en el directorio seleccionado
            final File listFich[] = fc.getSelectedFile().listFiles();
            // No hay ficheros ni directorios en el directorio seleccionado
            if(listFich == null || listFich.length == 0){
            	strBuf.append(ImportarUtils.HTML_NUEVO_PARRAFO)
            	.append(I18N.get("Importacion","importar.fichero.fxcc.importacion.no.ficheros.directorio"))
            	.append(" - ")
            	.append(directorioSeleccionado)
            	.append(ImportarUtils.HTML_FIN_PARRAFO);
            	jEditorPaneErrores.setText(strBuf.toString());
            }
            else{
            	final TaskMonitorDialog progressDialog = 
    				new TaskMonitorDialog(application.getMainFrame(), null);

    			progressDialog.setTitle(application.getI18nString("ValidandoDatos"));
    			progressDialog.report(application.getI18nString("ValidandoDatos"));
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
    								 // lista donde se almacenan las url de los directorios encontrados
    							    ArrayList lstDirectorios = new ArrayList(); 
    							    // se almacena el path del directorio seleccionado
    							    lstDirectorios.add(fc.getSelectedFile().getPath());
    							    
    							    // array donde se guardan los hashMap con todos los datos
    							    // de importacion
    							    ArrayList lstFichAlmacenadosDirectorio = new ArrayList();
    							    
						            for (int i= 0; i<listFich.length; i++){
						              	//findFiles(listFich[i]);
						               	
						               	findDirectorios(listFich[i], lstDirectorios);
						            }
						        
						            HashMap hashFicheros_ASC_DFX_LSTIMAGES = new HashMap();
						            
						            for(int j=0; j<lstDirectorios.size();j++){
						            	
						            	String path = (String)lstDirectorios.get(j);
						            	File directory = new File(path);
						            	File lstFicherosDirectorio[] = directory.listFiles();
						            	for (int h= 0; h<lstFicherosDirectorio.length; h++){
						            		findFiles(lstFicherosDirectorio[h], hashFicheros_ASC_DFX_LSTIMAGES);
						            	}
						            	
						            	// se han almacenado todos los ficheros encontrados en el directorio
						            	// que sean de tipo ASC, DXF, JPG, GIF, PNG, BMP
						            	
						            	// array que contiene el HashMap donde se guardan los ficheros
						            	//de tipo ASC, DXF, JPG, GIF, PNG, BMP
						            	// el HashMap contiene
						            		//NOMBRE_FICHERO_ASC - fichero asc
						            		//NOMBRE_FICHERO_DXF - fichero dxf
						                	//LST_FICHERO_IMG  - arryList de imagenes
						            	if(!hashFicheros_ASC_DFX_LSTIMAGES.isEmpty()){
						            		HashMap hashFichAlmacenados = (HashMap)hashFicheros_ASC_DFX_LSTIMAGES.clone();
						            		lstFichAlmacenadosDirectorio.add(hashFichAlmacenados);
						            	}

						            	hashFicheros_ASC_DFX_LSTIMAGES.clear();
						            	
						            }
						            almacenarFicheroBD(lstFichAlmacenadosDirectorio);
						            
    							} 
    							catch (Exception e)
    							{
    								//hayErroresFilas = true;
    								JOptionPane.showMessageDialog(null,I18N.get("Importacion","importar.infografica.seleccion.ficheros.masivo.error"),
    										null,JOptionPane.ERROR_MESSAGE);
    								
    							} 
    							finally
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
            
            if(!listParcelas_BD.isEmpty()){
            	continuar = true;
            	strBuf.append(ImportarUtils.HTML_NUEVO_PARRAFO)
            	.append(I18N.get("Importacion","importar.fichero.fxcc.importacion.lista.parcelas.encontradas"))
            	.append(ImportarUtils.HTML_FIN_PARRAFO);
            	
            	for(int i=0; i<listParcelas_BD.size(); i++){
                	strBuf.append(ImportarUtils.HTML_NUEVO_PARRAFO)
                	.append(I18N.get("Importacion","importar.fichero.fxcc.importacion.parcela"))
                	.append(" ")
                	.append(((FincaCatastro)listParcelas_BD.get(i)).getRefFinca().getRefCatastral())
                	.append(ImportarUtils.HTML_FIN_PARRAFO);
                	
                }
            }
            
            if(!listParcelas_No_BD.isEmpty()){
            	strBuf.append(ImportarUtils.HTML_NUEVO_PARRAFO)
            	.append(I18N.get("Importacion","importar.fichero.fxcc.importacion.lista.parcelas.no.encontradas"))
            	.append(ImportarUtils.HTML_FIN_PARRAFO);
	            for(int j=0; j<listParcelas_No_BD.size(); j++){
	            	
	            	strBuf.append(ImportarUtils.HTML_NUEVO_PARRAFO)
	            	.append(I18N.get("Importacion","importar.fichero.fxcc.importacion.parcela"))
	            	.append(" ")
	            	.append(((FincaCatastro)listParcelas_No_BD.get(j)).getRefFinca().getRefCatastral())
	            	.append(ImportarUtils.HTML_FIN_PARRAFO);
	            }
            }

            jEditorPaneErrores.setText(strBuf.toString());             
            wizardContext.inputChanged();
        }        
    }
    

    public void exiting()
    {   
    }
}  
