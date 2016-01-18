<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html xhtml="true">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
    <title>LocalGis</title>
    <html:base ref="site"/>
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
    <link href="css/staticStyles.css" rel="stylesheet" type="text/css" />

    <script type="text/javascript">

    function onSignOk(res) {
    }

    function onInitOk() {
    }

    function onSignCancel() {
    }
    
    </script>

</head>
    <body>
        <div id="wrapDigitalSignature">
            <div id="top" class="top">
                <img src="img/top_left.gif" alt="Esquina superior izquierda" class="esquina_sup_izq" />
                <img src="img/top_right.gif" alt="Esquina superior derecha" class="esquina_sup_der" />
            </div>
            <div id="content" class="content">
                <div id="boxcontrol" class="boxcontrol">
                    <div id="digitalSignature">
                        <div id="authorizationList">
                            <h1><bean:message key="certificates.title"/></h1>
                            <ul>
                                <li><bean:message key="certificates.dnie"/></li>
                                <li><bean:message key="certificates.camerfirma"/></li>
                                <li><bean:message key="certificates.fnmt"/></li>
                                <li><bean:message key="certificates.gva"/></li>
                                <li><bean:message key="certificates.verisign"/></li>
                            </ul>
                            <p><bean:message key="warning.pdfnotprotected"/></p>
                        </div>
                        <div id="digitalSignatureDetail">
<%-- Forma recomendada de incluir los applets, pero en IE no funciono
                        <!--[if !IE]>-->
                            <object id="CryptoApplet" 
                                    name="CryptoApplet" 
                                    classid="java:es.uji.dsign.applet2.LocalGISSignatureApplet.class" 
                                    codetype="application/x-java-applet" 
                                    codebase="cryptoApplet" 
                                    archive="ujiApplet-2.0.5-signed.jar, ujiConfig-2.0.5.jar, ujiCrypto-2.0.5-signed.jar, ujiUtils-2.0.5-signed.jar, log4j-1.2.14.jar, itext-1.4.jar, bctsp-jdk15-138.jar, bcmail-jdk15-138.jar, bcprov-jdk15-138.jar, xmlsec-1.4.1.jar" 
                                    width="582" 
                                    height="470">
                                <param name="signFormat" value="es.uji.dsign.crypto.PDFSignatureFactory"/>
                                <param name="progressBar" value="true"/>
                        <!--<![endif]-->
                                <object id="CryptoApplet" 
                                        name="CryptoApplet" 
                                        classid="clsid:CAFEEFAC-0016-0000-0000-ABCDEFFEDCBA"
                                        type="application/x-java-applet" 
                                        width="582" 
                                        height="470">
                                    <param name="codebase" value="cryptoApplet"/>
                                    <param name="archive" value="ujiApplet-2.0.5-signed.jar ujiConfig-2.0.5.jar ujiCrypto-2.0.5-signed.jar ujiUtils-2.0.5-signed.jar log4j-1.2.14.jar itext-1.4.jar bctsp-jdk15-138.jar bcmail-jdk15-138.jar bcprov-jdk15-138.jar xmlsec-1.4.1.jar"/>
                                    <param name="code" value="es.uji.dsign.applet2.LocalGISSignatureApplet.class"/>
                                    <param name="signFormat" value="es.uji.dsign.crypto.PDFSignatureFactory"/>
                                    <param name="progressBar" value="true"/>
                                </object>
                        <!--[if !IE]>-->
                            </object>
                        <!--<![endif]-->
--%>
                            <!--applet id="CryptoApplet" name="CryptoApplet" code="es.uji.dsign.applet2.LocalGISSignatureApplet" codebase="cryptoApplet" archive="ujiApplet-2.0.5-signed.jar, ujiConfig-2.0.5.jar, ujiCrypto-2.0.5-signed.jar, ujiUtils-2.0.5-signed.jar, log4j-1.2.14.jar, itext-1.4.jar, bctsp-jdk15-138.jar, bcmail-jdk15-138.jar, bcprov-jdk15-138.jar, xmlsec-1.4.1.jar" width="582" height="470" mayscript>-->
                            <!-- applet id="CryptoApplet" name="CryptoApplet" code="es.uji.dsign.applet2.LocalGISSignatureApplet" codebase="cryptoApplet" archive="ujiApplet-2.0.5-signed.jar, ujiConfig-2.0.5.jar, ujiCrypto-2.0.5-signed.jar, ujiCrypto-mityc-2.0.5-signed.jar, ujiUtils-2.0.5-signed.jar, log4j-1.2.14.jar, itext-1.4.jar, bctsp-jdk15-143.jar, bcmail-jdk15-143.jar, bcprov-jdk15-143.jar, xmlsec-1.4.1.jar,commons-logging-1.1.jar,commons-httpclient-3.0.1.jar,commons-codec-1.3.jar" width="582" height="470" mayscript-->
                            <!-- applet id="CryptoApplet" name="CryptoApplet" code="es.uji.dsign.applet2.LocalGISSignatureApplet" codebase="cryptoApplet" archive="LibreriaConfiguracion-0.9.jar,LibreriaOCSP-0.9.jar,LibreriaPolicy-0.9.jar,LibreriaTSA-0.9.jar,LibreriaXADES-0.9.jar,ujiApplet-2.0.5-signed.jar, ujiConfig-2.0.5.jar, ujiCrypto-2.0.5-signed.jar, ujiCrypto-mityc-2.0.5-signed.jar, ujiUtils-2.0.5-signed.jar, log4j-1.2.14.jar, itext-1.4.jar, bctsp-jdk15-138.jar, bcmail-jdk15-138.jar, bcprov-jdk15-138.jar, xmlsec-1.4.1.jar,commons-logging-1.1.jar,commons-httpclient-3.0.1.jar,commons-codec-1.3.jar" width="582" height="470" mayscript-->
                            <applet id="CryptoApplet" name="CryptoApplet" code="es.uji.dsign.applet2.LocalGISSignatureApplet" codebase="cryptoApplet" archive="LibreriaConfiguracion-0.9.jar,LibreriaOCSP-0.9.jar,LibreriaPolicy-0.9.jar,LibreriaTSA-0.9.jar,LibreriaXADES-0.9.jar,ujiApplet-2.0.5-signed.jar, ujiConfig-2.0.5.jar, ujiCrypto-2.0.5-signed.jar, ujiCrypto-mityc-2.0.5-signed.jar, ujiUtils-2.0.5-signed.jar, log4j-1.2.14.jar, itext-1.4.jar, bctsp-jdk15-143.jar, bcmail-jdk15-143.jar, bcprov-jdk15-143.jar, xmlsec-1.4.1.jar,commons-logging-1.1.jar,commons-httpclient-3.0.1.jar,commons-codec-1.3.jar" width="582" height="470" mayscript>
                                <!--param name="signFormat" value="es.uji.dsign.crypto.PDFSignatureFactory"/-->
                                <!-- param name="signFormat" value="es.uji.security.crypto.mityc.MitycXAdESSignatureFactory"/-->
                                <param name="progressBar" value="true"/>
                            </applet>
                        </div>
                    </div>
                    <!--  Fin del contenido de la pagina -->
                </div>
            </div>
            <div id="bottom" class="bottom">
                <img src="img/btm_left.gif" alt="Esquina inferior izquierda" class="esquina_inf_izq" />
                <img src="img/btm_right.gif" alt="Esquina inferior derecha" class="esquina_inf_der" />
            </div>
        </div>
        <!-- Recuadro inferior de la pagina -->
        <div id="wrapFooterPage">
            <div id="top" class="top">
                <img src="img/top_left.gif" alt="Esquina superior izquierda" class="esquina_sup_izq" />
                <img src="img/top_right.gif" alt="Esquina superior derecha" class="esquina_sup_der" />
            </div>
            <div id="content" class="content">
                <div id="boxcontrol" class="boxcontrol">
                    <!-- Contenido del recuadro -->
                    <div id="footer" class="footer"></div>
                    <!--  Fin del contenido del recuadro inferior -->
                </div>
            </div>
            <div id="bottom" class="bottom">
                <img src="img/btm_left.gif" alt="Esquina inferior izquierda" class="esquina_inf_izq" />
                <img src="img/btm_right.gif" alt="Esquina inferior derecha" class="esquina_inf_der" />
            </div>
        </div>
        <!-- Fin del recuadro inferior de la pagina -->
    </body>
</html:html>