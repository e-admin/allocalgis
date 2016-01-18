<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"%>
   


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="es"> 
<head>
<title><bean:message key="welcome.title"/></title>
<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="../favicon.ico" type="image/x-icon" />

<script type="text/javascript" src='../dwr/engine.js'></script>
<script type="text/javascript" src='../dwr/util.js'></script>
<script type="text/javascript" src='../dwr/interface/SSOAuthCheckService.js'></script>
<script type="text/javascript" src='../js/ssoAuth/ssoAuth.js'></script>
<script type="text/javascript">
checkSSOAuth(<%= (Boolean)request.getSession().getAttribute("SSOActive") %>, '<%= (String)request.getSession().getAttribute("TokenAttribute") %>');
</script>
<script type="text/javascript">
<!--
function MM_preloadImages() { //v3.0
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
function goHome(){
	document.location.href="${pageContext.request.contextPath}";
}
//-->
</script>
<bean:define id="nombreEntidad" name="nombreEntidad" scope="session"/>

</head>
<body onload="MM_preloadImages()">
<div id="wrap">
<div id="top" class="top">
<img src="../img/top_left.gif" alt="Esquina superior izquierda" class="esquina_sup_izq" />
<img src="../img/top_right.gif" alt="Esquina superior derecha" class="esquina_sup_der" />

</div>
<div id="content" class="content">
<div id="boxcontrol" class="boxcontrol">
<div id="bannerSelec"></div>

<bean:define id="publico" value="publico"/> 
<bean:define id="privado" value="privado"/> 


	 <logic:equal value="mapas" name="gestion" scope="request">	 
	 	<div id="bannerSelect" onclick="goHome()"></div>
		 	<table >
				<tr>
					<td><h1>Gestión de Mapas (<bean:write name="nombreEntidad"/>)</h1></td>
					<td align="right" style="width:38em"></td>
					<td><html:img styleClass="imageButton" onclick="document.location.href='./menu.do'"   src="../img/btn_menu.gif"/></td>
					<td><html:img styleClass="imageButton" onclick="document.location.href='./logoff.do'" src="../img/btn_cerrar_sesion.gif" alt="Cerrar Sesión" /></td>
				</tr>	
				<tr><td style="height:3em"></td></tr>	
			</table>
			<table style="margin-left:auto;margin-right:auto;text-align:center;">
				<tr style="height:4em;">
					<td><img style="" src="../img/arrow.gif" alt="Arrow"/></td>
					<td><html:link style="font-weight:bold;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:12px;" action="/admin/gestionMapas" title="Mapas Públicos" paramId="type" paramName="publico" >Mapas públicos</html:link></td>
				</tr>
				<tr style="height:4em;">
					<td><img src="../img/arrow.gif" alt="Arrow"/></td>
					<td><html:link style="font-weight:bold;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:12px;" action="/admin/gestionMapas" title="Mapas Privados" paramId="type" paramName="privado">Mapas privados</html:link></td>
				</tr>
				<tr>
					<td style="height:3em;"></td>
				</tr>
			</table>			
		 </logic:equal>
 	 	 <logic:equal value="capas" name="gestion" scope="request">	
  	 	 <div id="bannerCapas" onClick="goHome()"></div>
		 	<table >
				<tr>
					<td><h1>Gestión de Capas (<bean:write name="nombreEntidad"/>)</h1></td>
					<td align="right" style="width:38em"></td>
					<td><html:img styleClass="imageButton" onclick="document.location.href='./menu.do'"   src="../img/btn_menu.gif"/></td>
					<td><html:img styleClass="imageButton" onclick="document.location.href='./logoff.do'" src="../img/btn_cerrar_sesion.gif" alt="Cerrar Sesión" /></td>
				</tr>	
				<tr><td style="height:3em"></td></tr>	
			</table>
			<table style="margin-left:auto;margin-right:auto;text-align:center;">
				<tr style="height:4em;">
					<td><img style="" src="../img/arrow.gif" alt="Arrow"/></td>
					<td><html:link style="font-weight:bold;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:12px;" action="/admin/gestionCapas?parameter=listLayers" title="Mapas Publicos" paramId="type" paramName="publico" >Mapas públicos</html:link></td>
				</tr>
				<tr style="height:4em;">
					<td><img style="" src="../img/arrow.gif" alt="Arrow"/></td>
					<td><html:link style="font-weight:bold;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:12px;" action="/admin/gestionCapas?parameter=listLayers" title="Mapas Privados" paramId="type" paramName="privado">Mapas privados</html:link></td>
				</tr>
				<tr>
					<td style="height:3em;"></td>
				</tr>
			</table>
 	 </logic:equal>
 	 
</div>
</div>


<div id="bottom" class="bottom">
<img src="../img/btm_left.gif" alt="Esquina inferior izquierda" class="esquina_inf_izq" />
<img src="../img/btm_right.gif" alt="Esquina inferior derecha" class="esquina_inf_der" />
</div>

</div>

     <!-- Recuadro inferior de la pagina -->
        <div id="wrap">
            <div id="top" class="top">
                <img src="../img/top_left.gif" alt="Esquina superior izquierda" class="esquina_sup_izq" />
                <img src="../img/top_right.gif" alt="Esquina superior derecha" class="esquina_sup_der" />
            </div>
            <div id="content" class="content">
                <div id="boxcontrol" class="boxcontrol">
                    <!-- Contenido del recuadro -->
                    <div id="footer" class="footer"></div>
                    <!--  Fin del contenido del recuadro inferior -->
                </div>
            </div>
            <div id="bottom" class="bottom">
                <img src="../img/btm_left.gif" alt="Esquina inferior izquierda" class="esquina_inf_izq" />
                <img src="../img/btm_right.gif" alt="Esquina inferior derecha" class="esquina_inf_der" />
            </div>
        </div>
        <!-- Fin del recuadro inferior de la pagina -->

</body>
</html>