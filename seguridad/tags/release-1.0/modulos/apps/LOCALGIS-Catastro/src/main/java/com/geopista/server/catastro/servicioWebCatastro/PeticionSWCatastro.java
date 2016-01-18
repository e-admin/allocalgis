package com.geopista.server.catastro.servicioWebCatastro;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.server.administradorCartografia.ACException;
import com.vividsolutions.jump.util.java2xml.Java2XMLCatastro;

public class PeticionSWCatastro
{
    private static StringBuffer sCadena = new StringBuffer();
    private static org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(PeticionSWCatastro.class);
    private static final int SERVEROK = 200;

    public static void  peticionCrearExpSinParcelas(Expediente exp, String userName)
    {
         sCadena.append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soap:Header></soap:Header><soap:Body Id=\"MsgBody\">");

        sCadena.append("<Consultar_RC_In xmlns=\"http://www.catastro.meh.es/\">");
        sCadena.append("<CreacionExpedientesRequest><control>");

        sCadena.append("<ieg>");
        sCadena.append("<teg>").append("A").append("</teg>");
        sCadena.append("<ceg>");
        sCadena.append("<engf>");
        sCadena.append("<cd>").append(exp.getEntidadGeneradora().getCodigo()).append("</cd>");
        sCadena.append("<eng>" + "Municipio" + "</eng>");
        sCadena.append("</engf>");
        sCadena.append("</ceg>");
        sCadena.append("<neg>").append(exp.getEntidadGeneradora().getNombre()).append("</neg>");
        sCadena.append("</ieg>");

        sCadena.append("<ifi>");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String fechaFormat = formatter.format(new Date(System.currentTimeMillis()));
        SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
        String horaGene = horaFormat.format(new Time(System.currentTimeMillis()));
        sCadena.append("<ffi>").append(fechaFormat).append("</ffi>");
        sCadena.append("<hfi>").append(horaGene).append("</hfi>");
        sCadena.append("<tfi>").append("CFIE").append("</tfi>");
        sCadena.append("</ifi>");

        sCadena.append("<sol>");
        sCadena.append("<nif>").append("48394748L").append("</nif>");
        sCadena.append("<nom>").append(userName).append("</nom>");
        sCadena.append("</sol>");


        sCadena.append("</control><udem><exp>");
        sCadena.append("<tint>").append("R").append("</tint>");
        sCadena.append("<texp>").append(exp.getTipoExpediente().getCodigoTipoExpediente()).append("</texp>");
        sCadena.append("<expg>");
        if(exp.getAnnoExpedienteGerencia()!= null)
        {
            sCadena.append("<aexpg>").append(exp.getAnnoExpedienteGerencia()).append("</aexpg>");
        }
        else
        {
            sCadena.append("<aexpg></aexpg>");
        }
        if(exp.getReferenciaExpedienteGerencia()!=null)
        {
            sCadena.append("<rexpg>").append(exp.getReferenciaExpedienteGerencia()).append("</rexpg>");
        }
        else
        {
            sCadena.append("<rexpg></rexpg>");
        }
        if(exp.getCodigoEntidadRegistroDGCOrigenAlteracion()!=null)
        {
            sCadena.append("<ero>").append(exp.getCodigoEntidadRegistroDGCOrigenAlteracion()).append("</ero>");
        }
        else
        {
            sCadena.append("<ero></ero>");
        }
        sCadena.append("</expg>");
        
        sCadena.append("<expec><aexpec>").append(exp.getAnnoExpedienteAdminOrigenAlteracion()).append("</aexpec>");
        sCadena.append("<rexpec>").append(exp.getReferenciaExpedienteAdminOrigen()).append("</rexpec>");
        sCadena.append("<eoa>").append(exp.getEntidadGeneradora().getCodigo()).append("</eoa></expec>");

        formatter = new SimpleDateFormat("dd-MM-yyyy");
        fechaFormat = formatter.format(exp.getFechaRegistro());
        sCadena.append("<fre>").append(fechaFormat).append("</fre>");
        sCadena.append("<fhm>");
        if(exp.getFechaMovimiento()!=null)
        {
            fechaFormat = formatter.format(exp.getFechaMovimiento());
            sCadena.append("<fm>").append(fechaFormat).append("</fm>");
        }
        else
        {
            sCadena.append("<fm></fm>");
        }
        if(exp.getHoraMovimiento()!=null)
        {
            sCadena.append("<hm>").append(exp.getHoraMovimiento()).append("</hm>");
        }
        else
        {
            sCadena.append("<hm></hm>");
        }
        sCadena.append("</fhm>");


        sCadena.append("<inr>");
        sCadena.append("<npn>");
        
        sCadena.append("<not>");
        if(exp.getCodProvinciaNotaria()!=null && !exp.getCodProvinciaNotaria().equalsIgnoreCase(""))
        {
            sCadena.append("<cp>").append(exp.getCodProvinciaNotaria()).append("</cp>");
        }
        else
        {
            sCadena.append("<cp></cp>");
        }
        if(exp.getCodPoblacionNotaria()!=null && !exp.getCodPoblacionNotaria().equalsIgnoreCase(""))
        {
            sCadena.append("<cpb>").append(exp.getCodPoblacionNotaria()).append("</cpb>");
        }
        else
        {
            sCadena.append("<cpb></cpb>");
        }
        if(exp.getCodNotaria()!=null && !exp.getCodNotaria().equalsIgnoreCase(""))
        {
            sCadena.append("<cnt>").append(exp.getCodNotaria()).append("</cnt>");
        }
        else
        {
            sCadena.append("<cnt></cnt>");
        }
        sCadena.append("</not>");

        sCadena.append("<pn>");
        if(exp.getAnnoProtocoloNotarial() != null) //PREV-NOV
        {
            sCadena.append("<aprt>").append(exp.getAnnoProtocoloNotarial()).append("</aprt>");
        }
        else
        {
            sCadena.append("<aprt></aprt>");
        }
        if(exp.getProtocoloNotarial()!=null && !exp.getProtocoloNotarial().equalsIgnoreCase(""))
        {
            sCadena.append("<prt>").append(exp.getProtocoloNotarial()).append("</prt>");
        }
        else
        {
            sCadena.append("<prt></prt>");
        }
        sCadena.append("</pn>");
        
        sCadena.append("</npn>");
        sCadena.append("</inr>");

        sCadena.append("<decl>");
        if(exp.getTipoDocumentoOrigenAlteracion()!=null && !exp.getTipoDocumentoOrigenAlteracion().equalsIgnoreCase(""))
        {
            sCadena.append("<doco>").append(exp.getTipoDocumentoOrigenAlteracion()).append("</doco>");
        }
        else
        {
            sCadena.append("<doco></doco>");
        }
        if(exp.getInfoDocumentoOrigenAlteracion()!=null && !exp.getInfoDocumentoOrigenAlteracion().equalsIgnoreCase(""))
        {
            sCadena.append("<idoco>").append(exp.getInfoDocumentoOrigenAlteracion()).append("</idoco>");
        }
        else
        {
            sCadena.append("<idoco></idoco>");
        }
        sCadena.append("<nbi>");
        if(exp.getNumBienesInmueblesUrbanos()>0)
        {
            sCadena.append("<nbu>").append(exp.getNumBienesInmueblesUrbanos()).append("</nbu>");
        }
        else
        {
            sCadena.append("<nbu></nbu>");
        }
        if(exp.getNumBienesInmueblesRusticos()>0)
        {
            sCadena.append("<nbr>").append(exp.getNumBienesInmueblesRusticos()).append("</nbr>");
        }
        else
        {
            sCadena.append("<nbr></nbr>");
        }
        if(exp.getNumBienesInmueblesCaractEsp()>0)
        {
            sCadena.append("<nbce>").append(exp.getNumBienesInmueblesCaractEsp()).append("</nbce>");
        }
        else
        {
            sCadena.append("<nbce></nbce>");
        }
        sCadena.append("</nbi>");
        sCadena.append("</decl>");

        if(exp.getCodigoDescriptivoAlteracion()!=null && !exp.getCodigoDescriptivoAlteracion().equalsIgnoreCase(""))
        {
            sCadena.append("<cdeac>").append(exp.getCodigoDescriptivoAlteracion()).append("</cdeac>");
        }
        else
        {
            sCadena.append("<cdeac></cdeac>");
        }

        if(exp.getDescripcionAlteracion()!=null && !exp.getDescripcionAlteracion().equalsIgnoreCase(""))
        {
            sCadena.append("<deac>").append(exp.getDescripcionAlteracion()).append("</deac>");
        }
        else
        {
            sCadena.append("<deac></deac>");
        }
        sCadena.append("<dec>");

        sCadena.append("<idp>");
        sCadena.append("<nif>").append(exp.getNifPresentador()).append("</nif>");
        sCadena.append("<nom>").append(exp.getNombreCompletoPresentador()).append("</nom>");
        sCadena.append("</idp>");
        
        sCadena.append("<dfn>");

        sCadena.append("<loine>");
        sCadena.append("<cp>").append(exp.getDireccionPresentador().getProvinciaINE()).append("</cp>");
        sCadena.append("<cm>").append(exp.getDireccionPresentador().getMunicipioINE()).append("</cm>");
        sCadena.append("</loine>");
        if(exp.getDireccionPresentador().getCodigoMunicipioDGC()!=null)
        {
            sCadena.append("<cmc>").append(exp.getDireccionPresentador().getCodigoMunicipioDGC()).append("</cmc>");
        }
        else
        {
            sCadena.append("<cmc></cmc>");
        }
        sCadena.append("<np>").append(exp.getDireccionPresentador().getNombreProvincia()).append("</np>");
        sCadena.append("<nm>").append(exp.getDireccionPresentador().getNombreMunicipio()).append("</nm>");
        if(exp.getDireccionPresentador().getNombreEntidadMenor()!=null && !exp.getDireccionPresentador().
                getNombreEntidadMenor().equalsIgnoreCase(""))
        {
            sCadena.append("<nem>").append(exp.getDireccionPresentador().getNombreEntidadMenor()).append("</nem>");
        }
        else
        {
            sCadena.append("<nem></nem>");
        }        
        sCadena.append("<dir>");
        if(exp.getDireccionPresentador().getCodigoVia()>0)
        {
            sCadena.append("<cv>").append(exp.getDireccionPresentador().getCodigoVia()).append("</cv>");
        }
        else
        {
            sCadena.append("<cv></cv>");
        }
        sCadena.append("<tv>").append(exp.getDireccionPresentador().getTipoVia()).append("</tv>");
        sCadena.append("<nv>").append(exp.getDireccionPresentador().getNombreVia()).append("</nv>");
        if(exp.getDireccionPresentador().getPrimerNumero()>0)
        {
            sCadena.append("<pnp>").append(exp.getDireccionPresentador().getPrimerNumero()).append("</pnp>");
        }
        else
        {
            sCadena.append("<pnp></pnp>");
        }
        if(exp.getDireccionPresentador().getPrimeraLetra()!=null && !exp.getDireccionPresentador().
                getPrimeraLetra().equalsIgnoreCase(""))
        {
            sCadena.append("<plp>").append(exp.getDireccionPresentador().getPrimeraLetra()).append("</plp>");
        }
        else
        {
            sCadena.append("<plp></plp>");
        }
        if(exp.getDireccionPresentador().getSegundoNumero()>0)
        {
            sCadena.append("<snp>").append(exp.getDireccionPresentador().getSegundoNumero()).append("</snp>");
        }
        else
        {
            sCadena.append("<snp></snp>");
        }
        if(exp.getDireccionPresentador().getSegundaLetra()!=null && !exp.getDireccionPresentador().
                getSegundaLetra().equalsIgnoreCase(""))
        {
            sCadena.append("<slp>").append(exp.getDireccionPresentador().getSegundaLetra()).append("</slp>");
        }
        else
        {
            sCadena.append("<slp></slp>");
        }
        if(exp.getDireccionPresentador().getKilometro()>0)
        {
            sCadena.append("<km>").append(exp.getDireccionPresentador().getKilometro()).append("</km>");
        }
        else
        {
            sCadena.append("<km></km>");
        }
        if(exp.getDireccionPresentador().getDireccionNoEstructurada()!=null && !exp.getDireccionPresentador().
                getDireccionNoEstructurada().equalsIgnoreCase(""))
        {
            sCadena.append("<td>").append(exp.getDireccionPresentador().getDireccionNoEstructurada()).append("</td>");
        }
        else
        {
            sCadena.append("<td></td>");
        }        
        sCadena.append("</dir>");

        sCadena.append("<loint>");
        if(exp.getDireccionPresentador().getBloque()!=null && !exp.getDireccionPresentador().
                getBloque().equalsIgnoreCase(""))
        {
            sCadena.append("<bq>").append(exp.getDireccionPresentador().getBloque()).append("</bq>");
        }
        else
        {
            sCadena.append("<bq></bq>");
        }
        if(exp.getDireccionPresentador().getEscalera()!=null && !exp.getDireccionPresentador().
                getEscalera().equalsIgnoreCase(""))
        {
            sCadena.append("<es>").append(exp.getDireccionPresentador().getEscalera()).append("</es>");
        }
        else
        {
            sCadena.append("<es></es>");
        }
        if(exp.getDireccionPresentador().getPlanta()!=null && !exp.getDireccionPresentador().
                getPlanta().equalsIgnoreCase(""))
        {
            sCadena.append("<pt>").append(exp.getDireccionPresentador().getPlanta()).append("</pt>");
        }
        else
        {
            sCadena.append("<pt></pt>");
        }
        if(exp.getDireccionPresentador().getPuerta()!=null && !exp.getDireccionPresentador().
                getPuerta().equalsIgnoreCase(""))
        {
            sCadena.append("<pu>").append(exp.getDireccionPresentador().getPuerta()).append("</pu>");
        }
        else
        {
            sCadena.append("<pu></pu>");
        }
        sCadena.append("</loint>");

        sCadena.append("<pos>");
        if(exp.getDireccionPresentador().getCodigoPostal() != null)
        {
            sCadena.append("<dp>").append(exp.getDireccionPresentador().getCodigoPostal()).append("</dp>");
        }
        else
        {
            sCadena.append("<dp></dp>");
        }
        if(exp.getDireccionPresentador().getApartadoCorreos()>0)
        {
            sCadena.append("<ac>").append(exp.getDireccionPresentador().getApartadoCorreos()).append("</ac>");
        }
        else
        {
            sCadena.append("<ac></ac>");
        }
        sCadena.append("</pos>");

        sCadena.append("</dfn>");

        sCadena.append("</dec>");
        sCadena.append("</exp>");

        if(exp.getListaReferencias()!=null)
        {
            sCadena.append("<lelem>");
            for(int i = 0; i<exp.getListaReferencias().size();i++)
            {
                sCadena.append("<elemr>");
                sCadena.append("<bicreg>");
                sCadena.append("<idreg>");
                sCadena.append("<locat>");

                sCadena.append("<cd>").append(exp.getEntidadGeneradora().getCodigo()).append("</cd>");
                sCadena.append("<cmc>").append(exp.getCodigoEntidadRegistroDGCOrigenAlteracion()).append("</cmc>");

                sCadena.append("</locat>");
                if(exp.getListaReferencias().get(i) instanceof FincaCatastro)
                {
                    sCadena.append("<rcl>");
                    sCadena.append("<pc1>").append(((FincaCatastro)exp.getListaReferencias().get(i)).getRefFinca().getRefCatastral1()).append("</pc1>");
                    sCadena.append("<pc2>").append(((FincaCatastro)exp.getListaReferencias().get(i)).getRefFinca().getRefCatastral2()).append("</pc2>");
                    sCadena.append("</rcl>");
                }
                else if(exp.getListaReferencias().get(i) instanceof BienInmuebleCatastro)
                {
                    sCadena.append("<rcl>");
                    sCadena.append("<pc1>").append(((BienInmuebleCatastro)exp.getListaReferencias().get(i)).getIdBienInmueble().getParcelaCatastral().getRefCatastral1()).append("</pc1>");
                    sCadena.append("<pc2>").append(((BienInmuebleCatastro)exp.getListaReferencias().get(i)).getIdBienInmueble().getParcelaCatastral().getRefCatastral2()).append("</pc2>");
                    sCadena.append("<car>").append(((BienInmuebleCatastro)exp.getListaReferencias().get(i)).getIdBienInmueble().getNumCargo()).append("</car>");
                    sCadena.append("<cc1>").append(((BienInmuebleCatastro)exp.getListaReferencias().get(i)).getIdBienInmueble().getDigControl1()).append("</cc1>");
                    sCadena.append("<cc2>").append(((BienInmuebleCatastro)exp.getListaReferencias().get(i)).getIdBienInmueble().getDigControl2()).append("</cc2>");
                    sCadena.append("</rcl>");
                }
                sCadena.append("</idreg>");
                sCadena.append("</bicreg>");
                sCadena.append("</elemr>");
            }
            sCadena.append("</lelem>");
        }
        sCadena.append("</udem>");
        sCadena.append("</CreacionExpedientesRequest>");
        sCadena.append("</Consultar_RC_In></soap:Body></soap:Envelope>");

        try
        {
            FileWriter file = new FileWriter(AppContext.getApplicationContext().getString("ruta.base.mapas") +"CrearExpedienteRequest.xml");
            file.write(sCadena.toString());
            file.close();            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String sendRequest(Document doc, String keyStorePath, String keyStorePassword, String trustStorePath,
                  String trustStorePassword) throws TransformerConfigurationException, TransformerException,
                  HttpException, IOException
    {
        // Vendría el mandar el xml
    	HttpClient cliente = AppContext.getHttpClient();

        PostMethod metodoPost = new PostMethod(AppContext.getApplicationContext()
                .getString("ovc.service"));
        metodoPost.setRequestHeader("POST", AppContext.getApplicationContext().getString(
                "ovc.post"));
        metodoPost.setRequestHeader("Host", AppContext.getApplicationContext().getString(
                "ovc.host"));
        metodoPost.setRequestHeader("Content-Type", "text/xml; charset=utf-8");
        metodoPost.setRequestHeader("SOAPAction", AppContext.getApplicationContext()
                .getString("ovc.soap.action"));

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XMLUtils.outputDOMc14nWithComments(doc, os);
        String request = os.toString();
        metodoPost.setRequestBody(request);

        System.out.println (" ----- PETICION -----  \n" + metodoPost.getRequestCharSet() +"\n ----- FIN PETICION -----");
        System.setProperty("javax.net.ssl.keyStore", keyStorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

        if (trustStorePath != null)
        {
            System.setProperty("javax.net.ssl.trustStore", trustStorePath);
            if (trustStorePassword != null)
                System
                        .setProperty("javax.net.ssl.trustStorePassword",
                                trustStorePassword);
        } else if (System.getProperty("javax.net.ssl.trustStore") == null)
        {
            logger
                    .info("sendRequest() - Debe configurar el path y la contraseña del almacén de certificados de autoridades de confianza en el panel de control de Java.\n Claves:  javax.net.ssl.trustStorePassword y javax.net.ssl.trustStore");
        }
       
        int resultado = cliente.executeMethod(metodoPost);

        if (resultado != PeticionSWCatastro.SERVEROK)
            throw new HttpException(metodoPost.getResponseBodyAsString());

        if (logger.isDebugEnabled())
        {
            logger
                    .debug("sendRequest(Document, String, String, String, String) - El resultado es");
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("sendRequest(Document, String, String, String, String)"
                    + resultado);
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("sendRequest(Document, String, String, String, String)"
                    + metodoPost.getResponseBodyAsString());
        }

        return metodoPost.getResponseBodyAsString();
    }

    public static void creaCabeceraFinEntradaMasivo(ObjectOutputStream oos, CabeceraFinEntrada cabecera,
                                                        String directorio, String nombreFichero) throws Exception {
        String cabeceraFichero="";
        if(cabecera!=null){
            Java2XMLCatastro parser = new Java2XMLCatastro();

            String UTF8Line = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
            cabeceraFichero = cabeceraFichero + UTF8Line + "<finentrada xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.catastro.meh.es/ http://www.catastro.meh.es/ws/esquemas/finentrada.xsd\" xmlns=\"http://www.catastro.meh.es/\">\n";

            try {
                String parserCabecera = parser.write(cabecera,"cab");
                cabeceraFichero = cabeceraFichero + parserCabecera;

                cabeceraFichero = cabeceraFichero + "<luden>\n";

                String filePathName = directorio + File.separator+nombreFichero + (nombreFichero.indexOf(".xml")==-1 ? ".xml" : "");
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePathName), "UTF8"));
                out.write(cabeceraFichero);
                out.close();
            }
            catch(Exception e){
                oos.writeObject(new ACException(e));
                e.printStackTrace();
                throw e;
            }
        }
    }

    public static void creaCabeceraVARPADMasivo(ObjectOutputStream oos,CabeceraVARPAD cabecera,
                                                           String directorio, String nombreFichero) throws Exception {
           String cabeceraFichero="";
           if(cabecera!=null){
               Java2XMLCatastro parser = new Java2XMLCatastro();

               String UTF8Line = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
               cabeceraFichero = cabeceraFichero + UTF8Line + "<varpad xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.catastro.meh.es/ http://www.catastro.meh.es/ws/esquemas/varpad.xsd\" xmlns=\"http://www.catastro.meh.es/\">\n";

               try {
                   String parserCabecera = parser.write(cabecera,"cab");
                   cabeceraFichero = cabeceraFichero + parserCabecera;

                   cabeceraFichero = cabeceraFichero + "<luden>\n";

                   String filePathName = directorio+ File.separator + nombreFichero + (nombreFichero.indexOf(".xml")==-1 ? ".xml" : "");
                   Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePathName), "UTF8"));
                   out.write(cabeceraFichero);
                   out.close();
               }
               catch(Exception e){
                   oos.writeObject(new ACException(e));
                   e.printStackTrace();
                   throw e;
               }
           }
       }

    public static void crearCuerpoFinEntradaRequest(ObjectOutputStream oos, Expediente expediente, ArrayList arrayXmlExp, String directorio, String nombreFichero) throws Exception{

        try{

            if(expediente!=null && arrayXmlExp!=null) {
                expediente.setTipoDeIntercambio(Expediente.TIPO_INTERCAMBIO_FINAL);

                StringBuffer finEntrada = new StringBuffer("<uden>\n");
                Java2XMLCatastro parser = new Java2XMLCatastro();
                String parserExp ="";

                if(arrayXmlExp.size()>1 && arrayXmlExp.get(0) instanceof ArrayList && arrayXmlExp.get(1) instanceof ArrayList) {

                    ArrayList xmlParcelasExp = (ArrayList)arrayXmlExp.get(0);
                    ArrayList xmlFX_CC= (ArrayList) arrayXmlExp.get(1);
                    ArrayList xmlIMG = null;
                    if (arrayXmlExp.size()>2){
                    	xmlIMG = (ArrayList) arrayXmlExp.get(2);
                    }

                    if(xmlFX_CC.isEmpty())
                        expediente.setExistenciaInformacionGrafica("N");
                    else
                        expediente.setExistenciaInformacionGrafica("S");

                    parserExp = parser.write(expediente,"exp");
                    finEntrada.append(parserExp);
                    finEntrada.append("<lelem>\n");


                    for(int j = 0; j<xmlParcelasExp.size();j++){

                        finEntrada.append((String)xmlParcelasExp.get(j));

                        String fxcc = (String)xmlFX_CC.get(j);
                        String img = null;
                        if (xmlIMG != null && xmlIMG.size()>j){
                        	img = (String)xmlIMG.get(j);
                        }

                        if(fxcc!=null){
                            int p = finEntrada.lastIndexOf("</elemf>");
                            finEntrada = finEntrada.delete(p, finEntrada.length());
                            finEntrada.append(fxcc);
                            if (img != null){
                            	finEntrada.append(img);
                            }
                            finEntrada.append("</elemf>\n");
                        }
                        else if (img != null){
                        	int p = finEntrada.lastIndexOf("</elemf>");
                            finEntrada = finEntrada.delete(p, finEntrada.length());                            
                            finEntrada.append(img);
                            finEntrada.append("</elemf>\n");
                        }                        
                        
                    }
                }
                else {
                    expediente.setTipoDeIntercambio(Expediente.TIPO_INTERCAMBIO_REGISTRO);
                    expediente.setFechaAlteracion(expediente.getFechaRegistro());                   

                    parserExp = parser.write(expediente,"exp");
                    finEntrada.append(parserExp);

                    finEntrada.append("<lelem>\n");

                    if(arrayXmlExp.isEmpty()){
                        finEntrada.append("<elemr>\n<bicreg>\n<idreg>\n<locat>\n");

                        finEntrada.append("<cd>");
                        finEntrada.append(expediente.getEntidadGeneradora().getCodigo());
                        finEntrada.append("</cd>");

                        finEntrada.append("<cmc>");                        
                        finEntrada.append(expediente.getDireccionPresentador().getCodigoMunicipioDGC());
                        finEntrada.append("</cmc>");

                        finEntrada.append("</locat>\n</idreg>\n</bicreg>\n</elemr>\n");
                    }
                    else{
                        for(int i = 0; i<arrayXmlExp.size();i++){
                            parserExp = parser.write(arrayXmlExp.get(i),"elemr");
                            finEntrada.append(parserExp);
                        }
                    }
                }

                finEntrada.append("</lelem>\n</uden>\n");

                String filePathName = directorio + File.separator+ nombreFichero + (nombreFichero.indexOf(".xml")==-1 ? ".xml" : "");
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePathName, true), "UTF8"));
                out.write(finEntrada.toString());
                out.close();
            }
        }
        catch(Exception e){
            oos.writeObject(new ACException(e));
            e.printStackTrace();
            throw e;
        }
    }

    public static void crearCuerpoVARPADRequest(ObjectOutputStream oos, Expediente expediente, ArrayList arrayXmlExp, String directorio, String nombreFichero) throws Exception{

        if(expediente!=null && arrayXmlExp!=null) {

            StringBuffer varpad = new StringBuffer("<uden>\n");
            Java2XMLCatastro parser = new Java2XMLCatastro();
            if(arrayXmlExp.size()>1 && arrayXmlExp.get(0) instanceof ArrayList && arrayXmlExp.get(1) instanceof ArrayList) {
            //if(arrayXmlExp.size()==2 && arrayXmlExp.get(0) instanceof ArrayList && arrayXmlExp.get(1) instanceof ArrayList) {

                ArrayList xmlBienesInmueblesExp = (ArrayList)arrayXmlExp.get(0);

                if(!xmlBienesInmueblesExp.isEmpty()){
                    expediente.setTipoDeIntercambio(Expediente.TIPO_INTERCAMBIO_VARIACIONES);

                    String parserExp = parser.write(expediente,"exp");
                    varpad.append(parserExp);
                    varpad.append("<lelem>\n");

                    for(int j = 0; j<xmlBienesInmueblesExp.size();j++){

                        varpad.append((String)xmlBienesInmueblesExp.get(j));
                    }
                }
            }
            else{
                expediente.setTipoDeIntercambio(Expediente.TIPO_INTERCAMBIO_REGISTRO);
                expediente.setFechaAlteracion(expediente.getFechaRegistro());

                String parserExp = parser.write(expediente,"exp");
                varpad.append(parserExp);
                varpad.append("<lelem>\n");

                if(arrayXmlExp.isEmpty()){
                    varpad.append("<elemr>\n<bicreg>\n<idreg>\n<locat>\n");

                    varpad.append("<cd>");
                    varpad.append(expediente.getEntidadGeneradora().getCodigo());
                    varpad.append("</cd>");

                    varpad.append("<cmc>");
                    //varpad.append(expediente.getCodigoINEmunicipio() == null ? "000" : expediente.getCodigoINEmunicipio());
                    varpad.append(expediente.getDireccionPresentador().getCodigoMunicipioDGC());
                    varpad.append("</cmc>");

                    varpad.append("</locat>\n</idreg>\n</bicreg>\n</elemr>\n");
                }
                else{
                    for(int i = 0; i<arrayXmlExp.size();i++){

                        parserExp = parser.write(arrayXmlExp.get(i),"elemr");
                        varpad.append(parserExp);
                    }
                }
            }

            varpad.append("</lelem>\n</uden>\n");

            try{
                String filePathName = directorio + File.separator+nombreFichero + (nombreFichero.indexOf(".xml")==-1 ? ".xml" : "");

                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePathName, true), "UTF8"));
                out.write(varpad.toString());
                out.close();

            }
            catch(Exception e){
                oos.writeObject(new ACException(e));
                e.printStackTrace();
                throw e;
            }
        }
    }

    public static void crearCierreFinEntradaRequest(ObjectOutputStream oos,String directorio, String nombreFichero) throws Exception {

        String finEntrada ="</luden>\n</finentrada>";
        try {
            String filePathName = directorio+ File.separator + nombreFichero + (nombreFichero.indexOf(".xml")==-1 ? ".xml" : "");

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePathName, true), "UTF8"));
            out.write(finEntrada);
            out.close();
        }
        catch(Exception e){
            oos.writeObject(new ACException(e));
            e.printStackTrace();
            throw e;
        }
    }

    public static void crearCierreVARPADRequest(ObjectOutputStream oos,String directorio, String nombreFichero) throws Exception {

        String varpad ="</luden>\n</varpad>";
        try {
            String filePathName = directorio+ File.separator + nombreFichero + (nombreFichero.indexOf(".xml")==-1 ? ".xml" : "");

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePathName, true), "UTF8"));
            out.write(varpad);
            out.close();
        }
        catch(Exception e){
            oos.writeObject(new ACException(e));
            e.printStackTrace();
            throw e;
        }
    }
    
    public static String creaCabeceraFinEntradaMasivo(ObjectOutputStream oos, CabeceraFinEntrada cabecera) throws Exception {

    	String cabeceraFichero="";

    	if(cabecera!=null){
    		Java2XMLCatastro parser = new Java2XMLCatastro();

    		String UTF8Line = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    		cabeceraFichero = cabeceraFichero + UTF8Line + "<finentrada xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.catastro.meh.es/ http://www.catastro.meh.es/ws/esquemas/finentrada.xsd\" xmlns=\"http://www.catastro.meh.es/\">\n";

    		try {
    			String parserCabecera = parser.write(cabecera,"cab");
    			cabeceraFichero = cabeceraFichero + parserCabecera;

    			cabeceraFichero = cabeceraFichero + "<luden>\n";


    		}
    		catch(Exception e){
    			oos.writeObject(new ACException(e));
    			e.printStackTrace();
    			throw e;
    		}
    	}
    	return cabeceraFichero;
    }
    
    public static String creaCabeceraVARPADMasivo(ObjectOutputStream oos,CabeceraVARPAD cabecera) throws Exception {

    	String cabeceraFichero="";

    	if(cabecera!=null){
    		Java2XMLCatastro parser = new Java2XMLCatastro();

    		String UTF8Line = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    		cabeceraFichero = cabeceraFichero + UTF8Line + "<varpad xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.catastro.meh.es/ http://www.catastro.meh.es/ws/esquemas/varpad.xsd\" xmlns=\"http://www.catastro.meh.es/\">\n";

    		try {
    			String parserCabecera = parser.write(cabecera,"cab");
    			cabeceraFichero = cabeceraFichero + parserCabecera;
    			cabeceraFichero = cabeceraFichero + "<luden>\n";

    		}
    		catch(Exception e){
    			oos.writeObject(new ACException(e));
    			e.printStackTrace();
    			throw e;
    		}
    	}
    	return cabeceraFichero;
    }
    
    public static String crearCierreFinEntradaRequest() {

        String finEntrada ="</luden>\n</finentrada>";
        
        return finEntrada;
    }
    
    public static String crearCierreVARPADRequest() {

        String varpad ="</luden>\n</varpad>";
        
        return varpad;
    }
    
    public static String crearCuerpoFinEntradaRequest(ObjectOutputStream oos, Expediente expediente, ArrayList arrayXmlExp) throws Exception{

    	String fin = null;
    	
        try{

            if(expediente!=null && arrayXmlExp!=null) {
                expediente.setTipoDeIntercambio(Expediente.TIPO_INTERCAMBIO_FINAL);

                StringBuffer finEntrada = new StringBuffer("<uden>\n");
                Java2XMLCatastro parser = new Java2XMLCatastro();
                String parserExp ="";

                if(arrayXmlExp.size()>1 && arrayXmlExp.get(0) instanceof ArrayList && arrayXmlExp.get(1) instanceof ArrayList) {

                    ArrayList xmlParcelasExp = (ArrayList)arrayXmlExp.get(0);
                    ArrayList xmlFX_CC= (ArrayList) arrayXmlExp.get(1);
                    ArrayList xmlIMG = null;
                    if (arrayXmlExp.size()>2){
                    	xmlIMG = (ArrayList) arrayXmlExp.get(2);
                    }

                    if(xmlFX_CC.isEmpty() || ConstantesRegistroExp.modoGeneracion.equalsIgnoreCase(DatosConfiguracion.MODO_GENERAR_FXCC_DIRECTORIO))
                        expediente.setExistenciaInformacionGrafica("N");
                    else
                        expediente.setExistenciaInformacionGrafica("S");

                    parserExp = parser.write(expediente,"exp");
                    finEntrada.append(parserExp);
                    finEntrada.append("<lelem>\n");


                    for(int j = 0; j<xmlParcelasExp.size();j++){

                        finEntrada.append((String)xmlParcelasExp.get(j));

                        if (ConstantesRegistroExp.modoGeneracion.equalsIgnoreCase(DatosConfiguracion.MODO_GENERAR_FXCC_FICHERO)){
                        	String fxcc = (String)xmlFX_CC.get(j);
                        	String img = null;
                        	if (xmlIMG != null && xmlIMG.size()>j){
                        		img = (String)xmlIMG.get(j);
                        	}

                        	if(fxcc!=null){
                        		int p = finEntrada.lastIndexOf("</elemf>");
                        		finEntrada = finEntrada.delete(p, finEntrada.length());
                        		finEntrada.append(fxcc);
                        		if (img != null){
                        			finEntrada.append(img);
                        		}
                        		finEntrada.append("</elemf>\n");
                        	}
                        	else if (img != null){
                        		int p = finEntrada.lastIndexOf("</elemf>");
                        		finEntrada = finEntrada.delete(p, finEntrada.length());                            
                        		finEntrada.append(img);
                        		finEntrada.append("</elemf>\n");
                        	}      
                        }

                    }
                }
                else {
                    expediente.setTipoDeIntercambio(Expediente.TIPO_INTERCAMBIO_REGISTRO);
                    expediente.setFechaAlteracion(expediente.getFechaRegistro());
                    expediente.setExistenciaInformacionGrafica("N");
                    
                    if(expediente.getFechaMovimiento() == null)
                        expediente.setFechaMovimiento(new Date());

                    if(expediente.getHoraMovimiento() == null) {
                        SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
                        Time horaMov = new Time(System.currentTimeMillis());
                        expediente.setHoraMovimiento(horaFormat.format(horaMov));
                    }
                    
                    parserExp = parser.write(expediente,"exp");
                    finEntrada.append(parserExp);

                    finEntrada.append("<lelem>\n");
                    
                    if(arrayXmlExp.isEmpty()){
                        finEntrada.append("<elemr>\n<bicreg>\n<idreg>\n<locat>\n");

                        finEntrada.append("<cd>");
                        finEntrada.append(String.valueOf(expediente.getIdMunicipio()).substring(0,2)) ;
                        finEntrada.append("</cd>");

                        finEntrada.append("<cmc>");
                        finEntrada.append(String.valueOf(expediente.getIdMunicipio()).substring(2,5)) ;
                        finEntrada.append("</cmc>");

                        finEntrada.append("</locat>\n</idreg>\n</bicreg>\n</elemr>\n");
                    }
                    else{
                        for(int i = 0; i<arrayXmlExp.size();i++){
                            parserExp = parser.write(arrayXmlExp.get(i),"elemr");
                            finEntrada.append(parserExp);
                        }
                    }
                }

                finEntrada.append("</lelem>\n</uden>\n");
                
                fin = finEntrada.toString();
                
            }
        }
        catch(Exception e){
            oos.writeObject(new ACException(e));
            e.printStackTrace();
            throw e;
        }
        
        return fin;
    }
    
    public static String crearCuerpoVARPADRequest(ObjectOutputStream oos, Expediente expediente, ArrayList arrayXmlExp) throws Exception{

    	StringBuffer varpad = null;
    	
        if(expediente!=null && arrayXmlExp!=null) {

            varpad = new StringBuffer("<uden>\n");
            Java2XMLCatastro parser = new Java2XMLCatastro();

            if(arrayXmlExp.size()>1 && arrayXmlExp.get(0) instanceof ArrayList && arrayXmlExp.get(1) instanceof ArrayList) {

                ArrayList xmlBienesInmueblesExp = (ArrayList)arrayXmlExp.get(0);

                if(!xmlBienesInmueblesExp.isEmpty()){
                    expediente.setTipoDeIntercambio(Expediente.TIPO_INTERCAMBIO_VARIACIONES);

                    String parserExp = parser.write(expediente,"exp");
                    varpad.append(parserExp);
                    varpad.append("<lelem>\n");

                    for(int j = 0; j<xmlBienesInmueblesExp.size();j++){

                        varpad.append((String)xmlBienesInmueblesExp.get(j));
                    }
                }
            }
            else{
                expediente.setTipoDeIntercambio(Expediente.TIPO_INTERCAMBIO_REGISTRO);
                expediente.setFechaAlteracion(expediente.getFechaRegistro());
                expediente.setExistenciaInformacionGrafica("N");

                if(expediente.getFechaMovimiento() == null)
                    expediente.setFechaMovimiento(new Date());

                if(expediente.getHoraMovimiento() == null) {
                    SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
                    Time horaMov = new Time(System.currentTimeMillis());
                    expediente.setHoraMovimiento(horaFormat.format(horaMov));
                }

                String parserExp = parser.write(expediente,"exp");
                varpad.append(parserExp);
                varpad.append("<lelem>\n");

                if(arrayXmlExp.isEmpty()){
                    varpad.append("<elemr>\n<bicreg>\n<idreg>\n<locat>\n");

                    varpad.append("<cd>");
                    varpad.append(expediente.getEntidadGeneradora().getCodigo());
                    varpad.append("</cd>");

                    varpad.append("<cmc>");
                    varpad.append(expediente.getDireccionPresentador().getCodigoMunicipioDGC());
                    varpad.append("</cmc>");

                    varpad.append("</locat>\n</idreg>\n</bicreg>\n</elemr>\n");
                }
                else{
                    for(int i = 0; i<arrayXmlExp.size();i++){

                        parserExp = parser.write(arrayXmlExp.get(i),"elemr");
                        varpad.append(parserExp);
                    }
                }
            }

            varpad.append("</lelem>\n</uden>\n");

            
        }
        return varpad!=null?varpad.toString():null;
    }

}
