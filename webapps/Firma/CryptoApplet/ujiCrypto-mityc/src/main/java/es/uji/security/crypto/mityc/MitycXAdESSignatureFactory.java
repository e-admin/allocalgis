package es.uji.security.crypto.mityc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import es.mityc.firmaJava.configuracion.Configuracion;
import es.mityc.firmaJava.configuracion.EnumAlmacenCertificados;
import es.mityc.firmaJava.configuracion.EnumFormatoFirma;
import es.mityc.firmaJava.libreria.errores.ClienteError;
import es.mityc.firmaJava.libreria.utilidades.UtilidadFirmaElectronica;
import es.mityc.firmaJava.libreria.xades.FirmaXML;
import es.mityc.firmaJava.libreria.xades.InterfazObjetoDeFirma;
import es.uji.dsign.crypto.ISignFormatProvider;
import es.uji.dsign.crypto.SignatureOptions;
import es.uji.dsign.util.Base64;

public class MitycXAdESSignatureFactory implements ISignFormatProvider
{
	
	public static String BROWSER_IEXPLORER  = "IEXPLORER";
	public static String BROWSER_MOZILLA    = "MOZILLA";
	public static String BROWSER_OTHERS     = "OTHERS";

	private String _strerr= "";

	
	public byte[] formatSignature(SignatureOptions signatureOptions) throws Exception
    {
        //byte[] data = OS.inputStreamToByteArray(signatureOptions.getDataToSign());
        byte[] data= signatureOptions.getToSignByteArray(); 
        X509Certificate certificate = signatureOptions.getCertificate();
        PrivateKey privateKey = signatureOptions.getPrivateKey();

        Configuracion configuracion = new Configuracion();
        configuracion.cargarConfiguracion();
        
        String signFormat=signatureOptions.getSignFormat();
        if ((signFormat!=null) && (signFormat.equalsIgnoreCase("XADES-BES")))
        	configuracion.setFormatoXades(EnumFormatoFirma.XAdES_BES);
        else if ((signFormat!=null) && (signFormat.equalsIgnoreCase("XADES-T")))
        	configuracion.setFormatoXades(EnumFormatoFirma.XAdES_T);
        else if ((signFormat!=null) && (signFormat.equalsIgnoreCase("XADES-C")))
        	configuracion.setFormatoXades(EnumFormatoFirma.XAdES_C);
        else if ((signFormat!=null) && (signFormat.equalsIgnoreCase("XADES-X")))
        	configuracion.setFormatoXades(EnumFormatoFirma.XAdES_X);
        else if ((signFormat!=null) && (signFormat.equalsIgnoreCase("XADES-XL")))
        	configuracion.setFormatoXades(EnumFormatoFirma.XAdES_XL);
        else
        	configuracion.setFormatoXades(EnumFormatoFirma.XAdES_BES);
        
    	
		// Nodos que contendrán las firmas del fichero (se incluye Certificate1 porque será donde irá el certificado de firma)
		String nodesToSign = "Certificate1,fichero1";
		configuracion.setValor("xmlNodeToSign", nodesToSign);

        // Para firmar con un certificado del almacén de Internet Explorer
        InterfazObjetoDeFirma soi = 
        	UtilidadFirmaElectronica.getSignatureObject(EnumAlmacenCertificados.ALMACEN_EXPLORER, 
        		certificate, 
        		"",
        		configuracion);
        
        // Para firmar con un certificado del almacén de Mozilla
        //InterfazObjetoDeFirma soi = UtilidadFirmaElectronica.getSignatureObject(EnumAlmacenCertificados.ALMACEN_EXPLORER, 
        //		cert.getSerialNumber(), 
        //		cert.getIssuerDN().toString(), 
        //		"Poner aqui la ruta al perfil de Mozilla",
        //		configuracion);
        
        // Se prepara e inicializa el interfaz de firma
        try {
			soi.initSign();
		} catch (ClienteError e1) {
			e1.printStackTrace();
		}

		/*File f1=new File(rutaFicheroAFirmar);
        FileInputStream fileInputStream = new FileInputStream(rutaFicheroAFirmar);		
		byte[] signData = OS.inputStreamToByteArray(fileInputStream);
		StringBuffer xmlToSign=new StringBuffer();
		//xmlToSign = new String(Base64Coder.encode(signData));
		*/
		StringBuffer xmlToSign=new StringBuffer();
		xmlToSign.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		xmlToSign.append("<documento>");
		xmlToSign.append("<parts Encoding=\"urn:ietf-org:base64\" FileName=\"" + signatureOptions.getFileName().getName() +	"\" " +	"Id=\"fichero1\">");
		xmlToSign.append(new String(Base64.encode(data)));
		xmlToSign.append("</parts>");
		xmlToSign.append("</documento>\n");
		byte[] dataXML=xmlToSign.toString().getBytes();
        

        FirmaXML sxml = new FirmaXML(configuracion);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        boolean navigator=true;
    	/*if (signatureOptions.getNavigator().equals(BROWSER_MOZILLA))
		{
    		navigator=false;
		}
    	else{
    		navigator=true;
    	}*/
        
        sxml.signFile(certificate.getSerialNumber(), certificate.getIssuerDN().toString(),
                certificate, new ByteArrayInputStream(dataXML), null, "Certificate1,fichero1,<root>",
                privateKey, bos, navigator);

        return bos.toByteArray();
        
        /*SignatureResult signatureResult = new SignatureResult();

        signatureResult.setValid(true);
        signatureResult.setSignatureData(new ByteArrayInputStream(bos.toByteArray()));

        return signatureResult;*/
    }
	
	public String getError(){	
		return _strerr;
	}
    
}