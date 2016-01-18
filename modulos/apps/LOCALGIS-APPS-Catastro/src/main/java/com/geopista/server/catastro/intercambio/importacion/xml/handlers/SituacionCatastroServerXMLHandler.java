/**
 * SituacionCatastroServerXMLHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.catastro.intercambio.importacion.xml.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.intercambio.importacion.xml.contents.UnidadDatosIntercambio;
import com.geopista.server.catastro.intercambio.importacion.utils.ImportacionOperationsServerCatastro;
import com.geopista.server.catastro.intercambio.importacion.xml.contents.SituacionCatastralServerCatastroNoFinalizada;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class SituacionCatastroServerXMLHandler extends DefaultHandler 
{  
    //vector de instancias
    //private ArrayList instancias;
    //SituacionCatastral que se esta procesando
    private UnidadDatosIntercambio actual;
    //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    
    //Como queremos pasar el control del proceso de un
    //Handler a otro, necesitamos tener el parser para
    //asignarle el Handler que necesite en cada instante.
    private XMLReader parser;
    //El Handler al que queremos volver
    private DefaultHandler handlerToReturn;
    
    private FincaCatastroServerXMLHandler handlerFinca;
    private SueloCatastroServerXMLHandler handlerSuelo;
    private UnidadConstructivaCatastroServerXMLHandler handlerUC;
    private ConstruccionCatastroServerXMLHandler handlerConstruccion;
    private CultivoCatastroServerXMLHandler handlerCultivo;
    private RepartoCatastroServerXMLHandler handlerReparto;
    private BienInmuebleJuridicoCatastroServerXMLHandler handlerBIJ;
    private BienInmuebleCatastroServerXMLHandler handlerBI;
    private FxccCatastroServerXMLHandler handlerFxcc;
    private String etiqXMLorigen;
    
    /**
     * Finca catastral a la que está asociada la información de la situación catastral
     */
    FincaCatastro fincaCat = null;
    private ArrayList lstSuelos;
    private ArrayList lstUC;
    private ArrayList lstConstrucciones;
    private ArrayList lstCultivos;
    private ArrayList lstRepartos;
    private ArrayList lstBienesInmuebles;
    
    
    private TaskMonitorDialog progressDialog;
    private ApplicationContext application = AppContext.getApplicationContext();    
    private Blackboard blackboard = application.getBlackboard();
    private int n;
    private int insertedRows=0;
    private int notInsertedRows=0;
    private boolean toImport=false;
    
    /**
     * Lista de instancias de UnidadDatosIntercambio, en caso de tratarse de lectura
     * de ficheros para carga de sus datos
     */
    private ArrayList instancias;
    
    public SituacionCatastroServerXMLHandler (XMLReader parser, DefaultHandler handlerToReturn,
            String etiqXMLorigen, TaskMonitorDialog progressDialog, int n, boolean toImport)
    {
        this.parser = parser;
        //this.instancias = v;
        this.handlerToReturn = handlerToReturn;
        this.etiqXMLorigen = etiqXMLorigen;
        this.progressDialog = progressDialog;
        this.n=n;
        this.toImport = toImport;
    }
    /**
     * 
     * @param parser
     * @param v
     */
    public SituacionCatastroServerXMLHandler (XMLReader parser, ArrayList v, String etiqXMLorigen)
    {
        this.parser = parser;
        this.instancias = v;   
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public void startElement( String namespaceURI, String localName, String qName,
            Attributes attr ) throws SAXException 
            {
        
        valor = new StringBuffer();
        
        //comprobamos si empezamos un elemento elemsf o elemsr
        if (localName.equals("elemsf") || localName.equals("elemv")
                || localName.equals("elemf")){
            //creamos la nueva instancia 
            actual = new UnidadDatosIntercambio ();
            //y la añadimos al Vector qie las almacena
            if (instancias!=null)
                instancias.add(actual);
        }
        else if (localName.equals("elemsr") || localName.equals("elemr")){
            //creamos la nueva instancia 
            actual = new SituacionCatastralServerCatastroNoFinalizada ();
            //y la añadimos al Vector qie las almacena
            //instancias.add (actual);
        }
        
        else if (localName.equals("fincacs") || localName.equals("fincacef") || localName.equals("fincacefb")){
            
            fincaCat = new FincaCatastro();
            actual.setFincaCatastro(fincaCat);
            handlerFinca = new FincaCatastroServerXMLHandler( parser, this, fincaCat, localName);
            parser.setContentHandler( handlerFinca );
        }
        
        //Lista de suelos
        else if (localName.equals("lsuelocs") || localName.equals("lsuelof")){
            
            lstSuelos = new ArrayList();
            actual.setLstSuelos(lstSuelos);    
            if (fincaCat !=null)
                fincaCat.setLstSuelos(lstSuelos);
            
            handlerSuelo = new SueloCatastroServerXMLHandler( parser, this, lstSuelos, localName);
            parser.setContentHandler( handlerSuelo );
        }
        
        //Lista de unidades constructivas
        else if (localName.equals("luccs") || localName.equals("lucf")){
            
            lstUC = new ArrayList();
            actual.setLstUCs(lstUC);    
            if (fincaCat !=null)
                fincaCat.setLstUnidadesConstructivas(lstUC);
            
            handlerUC = new UnidadConstructivaCatastroServerXMLHandler( parser, this, lstUC, localName);
            parser.setContentHandler( handlerUC );
        }
        
        
        //Lista de construcciones
        else if (localName.equals("lconscs") || localName.equals("lconsf")){
            
            lstConstrucciones = new ArrayList();
            actual.setLstConstrucciones(lstConstrucciones);  
            if (fincaCat !=null)
                fincaCat.setLstConstrucciones(lstConstrucciones);
            
            handlerConstruccion = new ConstruccionCatastroServerXMLHandler( parser, this, lstConstrucciones, localName);
            parser.setContentHandler( handlerConstruccion );
        }
        
        //Lista de cultivos
        else if (localName.equals("lsprcs") || localName.equals("lsprf")){
            
            lstCultivos = new ArrayList();
            actual.setLstCultivos(lstCultivos);  
            if (fincaCat !=null)
                fincaCat.setLstCultivos(lstCultivos);
            
            handlerCultivo = new CultivoCatastroServerXMLHandler( parser, this, lstCultivos, localName);
            parser.setContentHandler( handlerCultivo );
        }
        
        //Lista de repartos
        else if (localName.equals("lreparcs") || localName.equals("lreparf")){
            
            lstRepartos = new ArrayList();
            actual.setLstRepartos(lstRepartos);     
            if (fincaCat !=null)
                fincaCat.setLstReparto(lstRepartos);
            
            handlerReparto = new RepartoCatastroServerXMLHandler( parser, this, lstRepartos, localName);
            parser.setContentHandler( handlerReparto );           
        }
        
        
        //Lista de bienes inmuebles
        else if (localName.equals("lbics") || localName.equals("lbicenv")
                || localName.equals("lbicenf")){
            
            lstBienesInmuebles = new ArrayList();
            actual.setLstBienesInmuebles(lstBienesInmuebles);     
            if (fincaCat !=null)
                fincaCat.setLstBienesInmuebles(lstBienesInmuebles);
            
            handlerBIJ = new BienInmuebleJuridicoCatastroServerXMLHandler( parser, this, lstBienesInmuebles, localName);
            parser.setContentHandler( handlerBIJ );
        }
        
        
        //Datos de bienes inmuebles relacionados con un expediente no finalizado
        else if (localName.equals("bicreg"))
        {
            BienInmuebleCatastro biCat = new BienInmuebleCatastro();
            ((SituacionCatastralServerCatastroNoFinalizada)actual).setBienInmuebleNoFin(biCat);
            handlerBI = new BienInmuebleCatastroServerXMLHandler( parser, this, biCat, localName);
            parser.setContentHandler( handlerBI );
            
        }       
        
        //FXCC
        else if (localName.equals("fxcc"))
        {
            FX_CC fxcc = new FX_CC();
            actual.setFxcc(fxcc);
            if (fincaCat !=null)
                fincaCat.setFxcc(fxcc);
            
            handlerFxcc = new FxccCatastroServerXMLHandler( parser, this, fxcc, localName);
            parser.setContentHandler( handlerFxcc );
            
        } 
        
            }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
        
        if (localName.equals(etiqXMLorigen)){            
            
            if (fincaCat !=null)    			
            {
                fincaCat.setLstSuelos(lstSuelos);
                fincaCat.setLstUnidadesConstructivas(lstUC);
                fincaCat.setLstConstrucciones(lstConstrucciones);
                fincaCat.setLstCultivos(lstCultivos);
                fincaCat.setLstReparto(lstRepartos);
                fincaCat.setLstBienesInmuebles(lstBienesInmuebles);
            }            
            
            if (toImport)
            {
                importData();  
            } 
            
            if (handlerToReturn!=null)
                parser.setContentHandler (handlerToReturn);
        }
    }
    
    
    private void importData()
    {
        ImportacionOperationsServerCatastro oper = new ImportacionOperationsServerCatastro();
        try
        {
            ((Integer)blackboard.get("UnidadesInsertadas")).intValue();
            ((Integer)blackboard.get("UnidadesNoInsertadas")).intValue();
            progressDialog.report(
                    new Integer(1+ ((Integer)blackboard.get("UnidadesInsertadas")).intValue()+
                            ((Integer)blackboard.get("UnidadesNoInsertadas")).intValue()).toString()
                            + " " +
                            I18N.get("Importacion","importar.general.proceso.importando"));
            
            Expediente exp = null;
            if (handlerToReturn instanceof FinRetornoCatastroServerXMLHandler)
                exp = ((FinRetornoCatastroServerXMLHandler)handlerToReturn).getExpediente();
                
            oper.insertarDatosSalida(actual, true, exp);
            insertedRows++;
        } 
        catch (Exception e)
        {   
            notInsertedRows++;
            blackboard.put("Importacion", false);
            e.printStackTrace();
        }
        
        blackboard.put("UnidadesInsertadas", new Integer(
                ((Integer)blackboard.get("UnidadesInsertadas")).intValue()+insertedRows));
        blackboard.put("UnidadesNoInsertadas",new Integer(
                ((Integer)blackboard.get("UnidadesNoInsertadas")).intValue()+notInsertedRows));
    }
    
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
        valor.append(new String (ch, start, end).trim()); 
    }
    
}
