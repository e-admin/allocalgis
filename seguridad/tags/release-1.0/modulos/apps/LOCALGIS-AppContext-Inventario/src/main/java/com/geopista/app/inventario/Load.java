package com.geopista.app.inventario;

import java.awt.BorderLayout;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.SAXParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.importacion.utils.ErrorHandlerImpl;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils_LCGIII;
import com.geopista.app.catastro.intercambio.importacion.xml.handlers.FindTagXMLHandler;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


/**
 * Esta clase tiene las funciones comunes de LoadInventario y Load 
 * Catalogo.
 * @author angeles
 *
 */
public abstract class Load extends JDialog {
	protected Logger logger = Logger.getLogger(Load.class);
    
	private static final long serialVersionUID = 1L;
	protected ResourceBundle messages;
	protected String NOMBRE_ESQUEMA="";
	protected static File lastDirectory;
    protected Municipio municipio;
    protected int ok=0;
    protected int mal=0;
    protected int total=0;
    protected AppContext aplicacion;
   
	
    
	public Load (java.awt.Frame parent, boolean modal, ResourceBundle messages, Municipio municipio){
		super (parent, modal);
		this.messages=messages;
        this.municipio=municipio;
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        
		initComponents();
        changeScreenLang(messages);
	}
	
	public Load (Municipio municipio,ResourceBundle messages){
        this.municipio=municipio;
        this.messages=messages;
	}
	
	public abstract void changeScreenLang(ResourceBundle messages) ;
	protected abstract void abrirFichero() ;
	protected abstract void aceptar() ;
	protected abstract void cargaDatos(TaskMonitorDialog progressDialog)throws Exception; 
	
	 
	   /**
     * Inicializa los componentes de la ventana    
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jButtonCancelar = new JButton();
        jButtonAceptar = new JButton();
        jButtonAbrir = new JButton();
        jPanelLoad = new JPanel();
        jPanelBotonera = new JPanel();
        jLabelFichero = new JLabel();
        jTextFieldFichero = new JTextField();
        jTextPaneComentario =  new JTextPane();
        okCancelPanel= new OKCancelPanel();
        jTextAreaFichero = new JTextArea();
        jScrollPane = new JScrollPane();
        jButtonObtenerSchema= new JButton();
        
        jScrollPane.setViewportView(jTextAreaFichero);
        jScrollPane.setBounds(35, 150, 400, 250) ; 
       
      
        
        jTextAreaFichero.setEditable(false);
        
        jButtonAbrir.setIcon(IconLoader.icon("abrir.gif"));
        jButtonAbrir.setBounds(new Rectangle(430, 115, 25, 20));
        jButtonAbrir.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                abrirFichero();
            }
        });

        jLabelFichero.setBounds(new Rectangle(35, 115, 240, 20));
        jTextFieldFichero.setBounds(new Rectangle(150, 115, 280, 20));
        jTextFieldFichero.setEditable(false);
        
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salir();
            }
        });
        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptar();
            }
        });
        jButtonObtenerSchema.addActionListener(new java.awt.event.ActionListener() {
        	 public void actionPerformed(java.awt.event.ActionEvent evt) {
                 obtenerEsquema();
             }
         });
        
         //jPanelLoad.setLayout(new java.awt.BorderLayout());
         jPanelLoad.setLayout(null);
           jTextPaneComentario.setBounds(new Rectangle(35, 20, 400, 90));
         jTextPaneComentario.setContentType("text/html");
         jTextPaneComentario.setEditable(false);
    
         // insertamos un JButton
         
         JPanel auxPanel = new JPanel();
         auxPanel.setLayout(new BorderLayout());
         auxPanel.add(jTextPaneComentario,BorderLayout.CENTER );
         auxPanel.setBounds(new Rectangle(35, 10, 400, 90));
         auxPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        // JPanel auxPanel2 = new JPanel();
        // auxPanel2.setLayout(new FlowLayout());
        // auxPanel2.add(jButtonObtenerSchema);
         //auxPanel.add(auxPanel2,BorderLayout.SOUTH);           
         jLabelInformacion = new JLabel();
         jLabelInformacion.setBounds(new Rectangle(35,410,400,15));
         
        
         
         jPanelLoad.add(auxPanel,null);
         jPanelLoad.add(jButtonAbrir, null);
         jPanelLoad.add(jTextFieldFichero, null);
         jPanelLoad.add(jLabelFichero, null);
         jPanelLoad.add(jScrollPane, null);
         jPanelLoad.add(jLabelInformacion,null);
         
         jPanelBotonera.setLayout(new java.awt.BorderLayout());
         jPanelBotonera.add(jButtonAceptar,java.awt.BorderLayout.WEST);
         okCancelPanel.addActionListener(new java.awt.event.ActionListener()
         {
 			public void actionPerformed(ActionEvent e)
             {
                 aceptar();
 			}
 		 });
         
         getContentPane().setLayout(new java.awt.BorderLayout());
         getContentPane().add(jPanelLoad, java.awt.BorderLayout.CENTER);
        // getContentPane().add(jScrollPane, java.awt.BorderLayout.NORTH);
         getContentPane().add(okCancelPanel, java.awt.BorderLayout.SOUTH);
         pack();
    }
    /**
     * Función que se ejecuta al salir   
     */
    protected void salir() {
    	dispose();
    }
    /**
     * Guarda en disco el esquema
     */
    private void obtenerEsquema(){
   	 Writer output = null;
     	
   	 try {
   		 GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
   		 filter.addExtension("xsd");
   		 filter.setDescription(messages.getString("inventario.load.fichero.esquema"));
   		 JFileChooser fc= new JFileChooser();
   		 fc.setFileFilter(filter);
   		 fc.setAcceptAllFileFilterUsed(false);
   		 if (lastDirectory!= null){
   			 File currentDirectory = lastDirectory;
   			 fc.setCurrentDirectory(currentDirectory);
   		 }
   		 fc.setName(NOMBRE_ESQUEMA);
   		 if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
   			 return;
   		 lastDirectory= fc.getCurrentDirectory(); 
   		 InputStream contenido= this.getClass().getResourceAsStream(NOMBRE_ESQUEMA);         	              
   		 ImportarUtils_LCGIII operations = new ImportarUtils_LCGIII();
   		 String contenidoString = operations.parseISToString(contenido); 
						               
    		 output = new java.io.BufferedWriter(new java.io.FileWriter(fc.getSelectedFile().getPath()));
    	     output.write( contenidoString );
    	     JOptionPane optionOk= new JOptionPane(messages.getString("inventario.load.fichero.saved.ok")+ ":\n "+fc.getSelectedFile().getPath(),JOptionPane.INFORMATION_MESSAGE);
       	 JDialog dialogOk =optionOk.createDialog(this,messages.getString("inventario.load.fichero.saved.title"));
       	 dialogOk.setVisible(true);
    	 } catch(Exception ex){
           ErrorDialog.show(this, "ERROR", messages.getString("inventario.load.fichero.saved.error"), StringUtil.stackTrace(ex));
        } finally {
    		 try{output.close();}catch(Exception ex){}
    	 }
    }
    /**
     * Devuelve un String del resourceBundle con parametros
     * @param key
     * @param valores
     * @return
     */
    protected String getStringWithParameters(ResourceBundle messages,String key, Object[] valores){
    	try{
    		MessageFormat messageForm = new MessageFormat("");
    		if (messages==null)
    			return null;
    		messageForm.setLocale(messages.getLocale());
    		String pattern = messages.getString(key);
    		messageForm.applyPattern(pattern);
    		return messageForm.format(valores, new StringBuffer(), null).toString();
    	}catch(Exception ex){
    		logger.error("Excepción al recoger el recurso:"+key, ex);
    		return "undefined";
    	}
    
    }
    /**
     * Selecciona y abre un fichero para ser exportado
     * 
     */
   protected void abrirFichero(String descripcionFichero){
	     GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
	  	 filter.addExtension("xml");
	  	 filter.setDescription(descripcionFichero);
	  	 final JFileChooser fc = new JFileChooser();
	  	 fc.setFileFilter(filter);
	  	 fc.setAcceptAllFileFilterUsed(false);
	  	 if (lastDirectory!= null){
	  		 File currentDirectory = lastDirectory;
	  		 fc.setCurrentDirectory(currentDirectory);
	  	 }
	  	 if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
	  	    return;
	  	
	  	 lastDirectory= fc.getCurrentDirectory(); 
	  	 jTextFieldFichero.setText(fc.getSelectedFile().getPath());
	  	 readFile(fc.getSelectedFile());
	}
    /***
     * Devuelve el texto de un fichero
     * @return
     */
    protected String readFile(File fichero)
    {
    	String linea;
    	jTextAreaFichero.setText("");
    	StringBuffer texto =new StringBuffer();
    	try {
             //FileReader fr =new FileReader(fichero,"utf-8");
             //BufferedReader br = new BufferedReader (fr);
             FileInputStream fr = new FileInputStream(fichero);
             InputStreamReader ir = new InputStreamReader(fr,"UTF8");
             BufferedReader br = new BufferedReader(ir);
             int lineas=0;
             int MAX_LINEAS=10;
             while ( (linea = br.readLine()) != null && lineas++<MAX_LINEAS)
             {
                texto.append("<p>"+linea+"</p>");
                jTextAreaFichero.append(linea+"\n");
              }
             br.close();
             ir.close();
             fr.close();
        }catch (Exception ex){
	         ex.printStackTrace();
	    }
        return texto.toString();    
   }
    /**
     * Muestra la pantalla de espera
     * @param titulo
     */
    protected void initProgressDialog(String titulo)
    {
    	 
    	 final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
    	 
         progressDialog.setTitle(titulo);
         progressDialog.report(messages.getString("inventario.load.dialogo.report1"));
         progressDialog.addComponentListener(new java.awt.event.ComponentAdapter()
         {
                 public void componentShown(java.awt.event.ComponentEvent e)
                 {
                     new Thread(new Runnable()
                         {
                             public void run()
                             {
                            	 try{            
                            	 	cargaDatos(progressDialog);
                             	 }catch (Exception ex){
                             		logger.error("Se ha producido un error al cargar el fichero",ex);
                             		ErrorDialog.show(progressDialog, "ERROR", messages.getString("inventario.loadinventario.error"), StringUtil.stackTrace(ex));
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
   

    /***
	     * Valida un fichero con su xsd
	     * @param xmlFilePath nombre del fichero
	     * @param type "Nombre del noto principal
	     * @return Devuelve el numero de nodos del nodo principal
	     * @throws Exception
	     */
	     public int validateSAXdocumentWithXSD(String xmlFilePath, String type) throws Exception{ 
	        XMLReader parser = new SAXParser();
	        ArrayList lst = new ArrayList();
	        parser.setFeature("http://apache.org/xml/features/validation/schema", true);
	        parser.setFeature("http://xml.org/sax/features/validation", true);
	        parser.setContentHandler(new FindTagXMLHandler(parser, lst, type));
	        parser.setErrorHandler(new ErrorHandlerImpl());
	        parser.setEntityResolver(new SchemaLoader(NOMBRE_ESQUEMA) );
	        //Suele haber problemas con espacios y acentos en el nombre del fichero
	        //utilizamos la conversion a URI para que funcione.
	        //parser.parse(xmlFilePath);
	        parser.parse(new File(xmlFilePath).toURI().toString());
	        parser=null;
	        return lst.size();
	        	        
	    }
	     
	     /**
	      * Calcula el numero total de elementos
	      * @param doc
	      */
	     protected int numeroTotalCatalogo(Document doc){
	     	int total=0;
	     	Element root = (Element)doc.getDocumentElement();;
	     	 
	     	NodeList patrimonio = root.getChildNodes();
	 		for (int i=0; i<patrimonio.getLength(); i++) {
	 			if (patrimonio.item(i) instanceof Element) {
	 				total++;
	 			}
	 		}
	 		return total;
	     }
	     
     /**
     * Calcula el numero total de elementos
     * @param doc
     */
    protected HashMap<String,Integer> numeroTotalInventario(Document doc){
    	int total=0;
    	HashMap<String,Integer> totalElementos=new HashMap<String,Integer>();
    	
    	Element root = (Element)doc.getDocumentElement();;
    	 
    	NodeList patrimonio = root.getChildNodes();
		for (int i=0; i<patrimonio.getLength(); i++) {
			if (patrimonio.item(i) instanceof Element) {
				for (int j=0;j<Constantes.TIPOS_INVENTARIOS.length;j++){
					if (patrimonio.item(i).getNodeName().equalsIgnoreCase(Constantes.TIPOS_INVENTARIOS[j])){
						if (totalElementos.get(Constantes.TIPOS_INVENTARIOS[j])!=null){
							int actual=totalElementos.get(Constantes.TIPOS_INVENTARIOS[j]);
							int numElementos=actual+1;
							totalElementos.put(Constantes.TIPOS_INVENTARIOS[j], numElementos);
						}
						else{
							totalElementos.put(Constantes.TIPOS_INVENTARIOS[j], 1);
						}
					}
				}				
				total++;
			}
		}
		totalElementos.put(Constantes.TIPOS_INVENTARIOS[0], total);
		return totalElementos;
    }
    
    
    /**
     * Devuelve el document de un fichero
     */
	 protected Document parseXmlFile(String fileName) throws Exception{
			//get the factory
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			
			
	        //Suele haber problemas con espacios y acentos en el nombre del fichero
	        //utilizamos la conversion a URI para que funcione.
			//return db.parse(fileName);
	        return db.parse(new File(fileName).toURI().toString());

			
		}
    protected javax.swing.JButton jButtonCancelar;
    protected javax.swing.JButton jButtonAceptar;
    protected javax.swing.JPanel jPanelBotonera;
    protected javax.swing.JPanel jPanelLoad;
    protected javax.swing.JTextPane jTextPaneComentario;
    protected OKCancelPanel okCancelPanel ;
    protected javax.swing.JButton jButtonAbrir;
    protected javax.swing.JLabel jLabelFichero;
    protected javax.swing.JTextField jTextFieldFichero;
    protected javax.swing.JScrollPane jScrollPane;
    protected javax.swing.JTextArea jTextAreaFichero;
    protected javax.swing.JButton jButtonObtenerSchema;
    protected javax.swing.JLabel jLabelInformacion;
          
 

}

class SchemaLoader implements EntityResolver {
      private String nombreEsquema;
      public SchemaLoader(String nombreEsquema){
    	  this.nombreEsquema=nombreEsquema;
      }	 

	  public InputSource resolveEntity(String publicId, String systemId) 
	      throws IOException, SAXException {
	    if (systemId.toUpperCase().endsWith("XSD")) {
	      InputStream is =this.getClass().getResourceAsStream(nombreEsquema);
	      return new InputSource(is);
	    } else {
	      return null;
	    }
	  }
}
