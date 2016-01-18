<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html xhtml="true" lang="es">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>AL LocalGIS Guia Urbana_Select</title>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
<link href="${pageContext.request.contextPath}/css/staticStyles.css" rel="stylesheet" type="text/css" />

<logic:present name="customCSS" scope="request">
	<style type="text/css">
	<bean:write name="customCSS" scope="request"/>
	</style>
</logic:present>

<script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/engine.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/util.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/dwr/interface/SSOAuthCheckService.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/ssoAuth/ssoAuth.js'></script>
<script type="text/javascript">
if('${requestScope.configurationLocalgisWeb}'=='private')
	checkSSOAuth(<%= (Boolean)request.getSession().getAttribute("SSOActive") %>, '<%= (String)request.getSession().getAttribute("TokenAttribute") %>');
</script>
<script type="text/javascript">
<!--
function MM_preloadImages(  ) { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
</head>

    <body>
        <!-- Recuadro superior de la pagina -->
        <div id="wrap">
            <div id="top" class="top">
                <img src="${pageContext.request.contextPath}/img/top_left.gif" alt="Esquina superior izquierda" class="esquina_sup_izq" />
                <img src="${pageContext.request.contextPath}/img/top_right.gif" alt="Esquina superior derecha" class="esquina_sup_der" />
            </div>
            <div id="content" class="content">
                <div id="boxcontrol" class="boxcontrol">

                    <!-- Contenido del recuadro superior -->
                    <div id="bannerGeneral"></div>
                                        
                    <div id="titlePage"> 
                        <table width="100%">
                            <tr>
                                <td>
                                    <h1>Selección de Mapa. (<bean:write name="nombreEntidad"/>)</h1>
                                </td>
                                <logic:equal name="configurationLocalgisWeb" value="private">
                                    <td align="right">
                                        <img class="imageButton" src="${pageContext.request.contextPath}/img/btn_cerrar_sesion.gif" alt="Cerrar Sesión" onclick="document.location.href='${pageContext.request.contextPath}/${requestScope.configurationLocalgisWeb}/logoff.do'"/>
                                    </td>
                                </logic:equal>
                            </tr>
                        </table>
                    </div>
                    
                    <div id="contentPage">
                        <html:form action="/${requestScope.configurationLocalgisWeb}/showMap.do">
                            <input type="hidden" name="idEntidad" value="<bean:write name="idEntidad"/>"/>
                            <table id="centeredTable">
                                <tr>
                                    <td align="center">
                                        Mapa
                                    </td>
                                    <td align="center">
                                        Idioma
                                    </td>
                                    <td align="center">
                                        &nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center" valign="middle">
                                        <html:select property="idMap">
                                            <logic:notEmpty name="maps">
                                                <logic:iterate id="map" name="maps">
                                                    <logic:notEmpty name="map" property="name">
                                                        <option value="<bean:write name="map" property="mapid"/>"><bean:write name="map" property="name"/></option>
                                                    </logic:notEmpty>
                                                    <logic:empty name="map" property="name">
                                                        <option value="<bean:write name="map" property="mapid"/>">Nombre Desconocido ID <bean:write name="map" property="mapid"/></option>
                                                    </logic:empty>
                                                </logic:iterate>
                                            </logic:notEmpty>
                                        </html:select>
                                    </td>
                                    <td align="center" valign="middle">
                                    	<label for="language">language</label>
                                        <select name="language" id="language">
                                            <option value="es_ES">Castellano</option>
                                            <option value="ca_ES">Catalán</option>
                                            <option value="gl_ES">Gallego</option>
                                            <option value="eu_ES">Euskera</option>
                                        </select>
                                    </td>
                                    <logic:empty name="maps">
                                      <td align="center" valign="middle">
                                        <html:img styleClass="imageButton"  src="${pageContext.request.contextPath}/img/btn_aceptar_deshabilitado.gif" alt="Ir al Municipio" border="0" imageName="Image2" style="vertical-align:bottom" />    
                                    </td>
                                    </logic:empty>
                                    <logic:notEmpty name="maps">
                                    <td align="center" valign="middle">
                                        <html:img styleClass="imageButton" src="${pageContext.request.contextPath}/img/btn_aceptar.gif" alt="Ir al Municipio" border="0" imageName="Image1" style="vertical-align:bottom" onclick="document.forms[0].submit();"/>    
                                    </td>
                                    </logic:notEmpty>
                                </tr>
                                <tr>
                                    <td align="center" valign="middle">
                                        <span class="errorMessage"><html:errors property="idMap" /></span>
                                    </td>
                                    <td align="center" valign="middle">
                                        <span class="errorMessage"><html:errors property="language" /></span>
                                    </td>
                                    <td align="center" valign="middle">
                                        &nbsp;
                                    </td>
                                </tr>
                            </table>
                        </html:form>
                    </div>
                    <!--  Fin del contenido de la pagina -->
                </div>
            </div>
            <div id="bottom" class="bottom">
                <img src="${pageContext.request.contextPath}/img/btm_left.gif" alt="Esquina inferior izquierda" class="esquina_inf_izq" />
                <img src="${pageContext.request.contextPath}/img/btm_right.gif" alt="Esquina inferior derecha" class="esquina_inf_der" />
            </div>
        </div>
        <!-- Fin del recuadro superior de la pagina -->

        <!-- Recuadro inferior de la pagina -->
        <div id="wrap">
            <div id="top" class="top">
                <img src="${pageContext.request.contextPath}/img/top_left.gif" alt="Esquina superior izquierda" class="esquina_sup_izq" />
                <img src="${pageContext.request.contextPath}/img/top_right.gif" alt="Esquina superior derecha" class="esquina_sup_der" />
            </div>
            <div id="content" class="content">
                <div id="boxcontrol" class="boxcontrol">
                    <!-- Contenido del recuadro -->
                    <div id="footer" class="footer"></div>
                    <!--  Fin del contenido del recuadro inferior -->
                </div>
            </div>
            <div id="bottom" class="bottom">
                <img src="${pageContext.request.contextPath}/img/btm_left.gif" alt="Esquina inferior izquierda" class="esquina_inf_izq" />
                <img src="${pageContext.request.contextPath}/img/btm_right.gif" alt="Esquina inferior derecha" class="esquina_inf_der" />
            </div>
        </div>
        <!-- Fin del recuadro inferior de la pagina -->

    </body>
</html:html>