package com.geopista.app.catastro.intercambio.importacion.xml.handlers;


import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.exception.ErrorNewAttemptException;
import com.geopista.app.catastro.model.intercambio.importacion.xml.contents.UnidadDatosIntercambio;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.PadronFile;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.util.ApplicationContext;
import com.geopista.util.UserCancellationException;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Parsea una Lista de Unidades de Datos del Fin de Salida  
 * 
 * @author COTESA
 *
 */
public class UnidadDatosIntercambioXMLHandler extends DefaultHandler 
{ 
    /**
     * UnidadDatosIntercambio que se esta procesando
     */
    private UnidadDatosIntercambio actual = new UnidadDatosIntercambio();
    /**
     * Numero de fincas que se esperan en el fichero
     */
    private int numFincas=0;
    
    private EntidadGeneradora entidadGeneradora;
    /**
     * Valor contenido entre las etiquetas de un elemento
     */
    private StringBuffer valor = new StringBuffer();
    private XMLReader parser;
    
    private FincaXMLHandler handlerFinca;
    private SueloXMLHandler handlerSuelo;
    private UnidadConstructivaXMLHandler handlerUC;
    private ConstruccionXMLHandler handlerConstruccion;
    private CultivoXMLHandler handlerCultivo;
    private RepartoXMLHandler handlerReparto;
    private BienInmuebleJuridicoXMLHandler handlerBIJ;
    private FxccXMLHandler handlerFxcc;
    private ImagenXMLHandler handlerImagen;
    private FincaCatastro fincaCat;
    
    
    private ArrayList lstSuelos = new ArrayList();
    private ArrayList lstUC = new ArrayList();
    private ArrayList lstConstrucciones = new ArrayList();
    private ArrayList lstCultivos = new ArrayList();
    private ArrayList lstRepartos = new ArrayList();
    private ArrayList lstBienesInmuebles = new ArrayList();
    private ArrayList lstImagenes = new ArrayList();
    
    private TaskMonitorDialog progressDialog;
    private ApplicationContext application = AppContext.getApplicationContext();    
    private Blackboard blackboard = application.getBlackboard();
    private int n=1;
    private int insertedRows=0;
    private int notInsertedRows=0;
    
    /**
     * Datos informativos del fichero de padrón, en caso de estar parseando 
     * un fichero de este tipo
     */
    private PadronFile pf;
    
    /**
     * Lista de instancias de UnidadDatosIntercambio, en caso de tratarse de lectura
     * de ficheros para carga de sus datos
     */
    private ArrayList instancias;

	private int numUdsa = 0;
    
    /**
     * 
     * @param parser
     * @param v
     */
    public UnidadDatosIntercambioXMLHandler (XMLReader parser, ArrayList v)
    {
        this.parser = parser;
        this.instancias = v;        
    }
    
    /**
     * 
     * @param parser
     * @param progressDialog
     */
    public UnidadDatosIntercambioXMLHandler (XMLReader parser, TaskMonitorDialog progressDialog)
    {
        this.parser = parser;        
        this.progressDialog = progressDialog;        
    }
    
    /**
     * 
     * @param parser
     * @param progressDialog
     * @param pf
     */
    public UnidadDatosIntercambioXMLHandler (XMLReader parser, TaskMonitorDialog progressDialog, PadronFile pf)
    {
        this.parser = parser;        
        this.progressDialog = progressDialog;
        this.pf = pf;        
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {        
        valor = new StringBuffer();
        
        if (localName.equals("cabecera"))
        {
            entidadGeneradora = new EntidadGeneradora();
        }
        
        //comprobamos si empezamos un elemento udsa o uden
        else if (localName.equals("udsa") || localName.equals("uden")){
           
        	numUdsa ++;
        	actual.borrarCampos();
            if (entidadGeneradora!=null)
                actual.setEntidadGeneradora(entidadGeneradora);    
            
            blackboard.put("ListaBienes", new ArrayList());
            blackboard.put("ListaFincas", new ArrayList());
        }        
        
        else if (localName.equals("fincacs") || localName.equals("fincacef")){
            
            fincaCat = new FincaCatastro();
            actual.setFincaCatastro(fincaCat);
            handlerFinca = new FincaXMLHandler( parser, this, fincaCat, localName);
            parser.setContentHandler( handlerFinca );
        }
        
        //Lista de suelos
        else if (localName.equals("lsuelocs") || localName.equals("lsuelof")){
            
            lstSuelos.clear();
            actual.setLstSuelos(lstSuelos);  
            if (fincaCat!=null)
                fincaCat.setLstSuelos(lstSuelos);
            
            handlerSuelo = new SueloXMLHandler( parser, this, lstSuelos, localName);
            parser.setContentHandler( handlerSuelo );
        }
        
        //Lista de unidades constructivas
        else if (localName.equals("luccs") || localName.equals("lucf")){
            
            lstUC.clear();
            actual.setLstUCs(lstUC);  
            if (fincaCat!=null)
                fincaCat.setLstUnidadesConstructivas(lstUC);
            
            handlerUC = new UnidadConstructivaXMLHandler( parser, this, lstUC, localName);
            parser.setContentHandler( handlerUC );
        }
        
        
        //Lista de construcciones
        else if (localName.equals("lconscs") || localName.equals("lconsf")){
            
            lstConstrucciones.clear(); // = new ArrayList();
            actual.setLstConstrucciones(lstConstrucciones);     
            if (fincaCat!=null)
                fincaCat.setLstConstrucciones(lstConstrucciones);
            
            handlerConstruccion = new ConstruccionXMLHandler( parser, this, lstConstrucciones, localName);
            parser.setContentHandler( handlerConstruccion );
        }
        
        //Lista de cultivos
        else if (localName.equals("lsprcs") || localName.equals("lsprf")){
            
            lstCultivos.clear(); 
            actual.setLstCultivos(lstCultivos);   
            if (fincaCat!=null)
                fincaCat.setLstCultivos(lstCultivos);
            
            handlerCultivo = new CultivoXMLHandler( parser, this, lstCultivos, localName);
            parser.setContentHandler( handlerCultivo );
        }
        
        //Lista de repartos
        else if (localName.equals("lreparcs") || localName.equals("lreparf")){
            
            lstRepartos.clear();
            actual.setLstRepartos(lstRepartos);      
            if (fincaCat!=null)
                fincaCat.setLstReparto(lstRepartos);
            
            handlerReparto = new RepartoXMLHandler( parser, this, lstRepartos, localName);
            parser.setContentHandler( handlerReparto );
        }
        
        
        //Lista de bienes inmuebles
        else if (localName.equals("lbics") || localName.equals("lelem")
                || localName.equals("lbicenf")){
            
            lstBienesInmuebles.clear();
            actual.setLstBienesInmuebles(lstBienesInmuebles);  
            if (fincaCat!=null)
                fincaCat.setLstBienesInmuebles(lstBienesInmuebles);
            
            handlerBIJ = new BienInmuebleJuridicoXMLHandler( parser, this, lstBienesInmuebles, localName);
            parser.setContentHandler( handlerBIJ );
        }
        else if (localName.equals("fxcc"))
        {
            FX_CC fxcc = new FX_CC();
            actual.setFxcc(fxcc);
            if (fincaCat !=null)
                fincaCat.setFxcc(fxcc);
            
            handlerFxcc = new FxccXMLHandler( parser, this, fxcc, localName);
            parser.setContentHandler( handlerFxcc );
            
        } 
        else if (localName.equals("limg")){
        	
        	lstImagenes.clear();
        	actual.setLstImagenes(lstImagenes);
        	if (fincaCat != null){
        		//Añadir a la finca la lista de las imágenes
        		fincaCat.setLstImagenes(lstImagenes);       		
        	}
        	handlerImagen = new ImagenXMLHandler(parser, this, lstImagenes, localName);
        	parser.setContentHandler(handlerImagen);
        }
        
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
        
        if ((localName.equals("udsa") || localName.equals("uden"))
                && fincaCat !=null)
        {
            fincaCat.setLstSuelos(lstSuelos);
            fincaCat.setLstUnidadesConstructivas(lstUC);
            fincaCat.setLstConstrucciones(lstConstrucciones);
            fincaCat.setLstCultivos(lstCultivos);
            fincaCat.setLstReparto(lstRepartos);
            fincaCat.setLstBienesInmuebles(lstBienesInmuebles);    
        }
        if (localName.equals("udsa") && (fincaCat !=null || lstBienesInmuebles!=null))
        {
            importData();
        }
        else if (localName.equals("cufin"))
        {
            if(!valor.toString().trim().equals("")){
                numFincas = Integer.parseInt(valor.toString());
                blackboard.put("UnidadesTotales",new Integer(numFincas));
            }
        }
        
        else if (localName.equals("teg") && entidadGeneradora!=null)
            entidadGeneradora.setTipo(valor.toString());
        else if (localName.equals("neg") && entidadGeneradora!=null)
        {
            entidadGeneradora.setNombre(valor.toString());
            if (pf!=null)
                pf.setProcedencia(valor.toString());
        }
        else if ((localName.equals("cd") || localName.equals("cde"))&& entidadGeneradora!=null)
        {
            entidadGeneradora.setCodigo(new Integer(valor.toString()).intValue());
            if (pf!=null)
                pf.setCodigo(valor.toString());            
        }
        else if (localName.equals("ffi") && pf!=null)
            pf.setFecha(valor.toString());
        else if (localName.equals("hfi") && pf!=null)
            pf.setHora(valor.toString());
        else if (localName.equals("epad") && pf!=null)
            pf.setAnio(valor.toString());
        else if (localName.equals("dfi") && pf!=null)
            pf.setDescripcion(valor.toString());        
    }
    
    private void importData()
    {
    	
    	blackboard.put("newattempt",new Boolean(true));
    	
    	if((!((Boolean)blackboard.get("cancel")).booleanValue())&&(!progressDialog.isCancelRequested())){

    		while(((Boolean) blackboard.get("newattempt")).booleanValue()){

    			blackboard.put("newattempt",new Boolean(false));

    			try
    			{
    				if (numFincas==0)
    					progressDialog.report(n + " " + I18N.get("Importacion","importar.general.proceso.importando"));
    				else
    					progressDialog.report(n, numFincas,
    							I18N.get("Importacion","importar.general.proceso.importando"));

    				ConstantesRegistroExp.clienteCatastro.insertarDatosSalida(actual, false, null);
    				    				
    				insertedRows++;
    			} 
    			catch (Exception e)
    			{       				
    				ErrorNewAttemptException exception = new ErrorNewAttemptException(I18N.get("ErrorNewAttemptDialog.message"));    				
    				blackboard.put("Importacion", false);
    				e.printStackTrace();
    			}

    			if(!((Boolean) blackboard.get("newattempt")).booleanValue()){
    				n++;
    			}
    		}
    		
    		blackboard.put("UnidadesInsertadas",new Integer(insertedRows));
        	if(n==numFincas)
        		n=0;

    	}
    	else{
    		progressDialog.report(I18N.get("Importacion","importar.general.proceso.cancelando"));
    		throw new UserCancellationException();
    	}
    	
    	
    }

    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
    	String cadena = new String (ch, start, end).trim();
    	valor.append(cadena);
    	cadena = null;
    }

}

