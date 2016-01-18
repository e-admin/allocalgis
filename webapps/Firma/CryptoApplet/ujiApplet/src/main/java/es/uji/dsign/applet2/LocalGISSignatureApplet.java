package es.uji.dsign.applet2;

/**
 * Applet de firma de PDF. Utiliza el applet de firma modificado por SATEC. Las
 * modificaciones consisten fundamentalmente en que se pinte el interfaz gráfico
 * en el propio applet y no en un frame externo. Al haber hecho estos cambios
 * podría pasar que no funcione correctamente otro tipo de firma que no sea la
 * PDF, debido al interfaz gráfico, no a la algoritmica. Además no funcionaría
 * la apertura desde la barra de menu de ficheros PKCS12 ya que no tenemos un
 * frame, por tanto no tenemos menubar y no tenemos esta funcionalidad
 * 
 * @author albegarcia
 * 
 */
public class LocalGISSignatureApplet extends SignatureApplet {

    private static final long serialVersionUID = 1L;

    public void init() {
        super.init();
        //setSignatureOutputFormat("PDF");
        //setSignatureOutputFormat("XADES_MITYC");
        setInputDataEncoding("PLAIN");
        signDataPDFLocalToPDFLocal();
    }


}
