<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"%>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="es">
<head>
<title><bean:message key="welcome.title"/></title>

<script type='text/javascript' src='../dwr/interface/JCSS.js'></script>
<script type='text/javascript' src='../dwr/engine.js'></script>
<script type='text/javascript' src='../dwr/util.js'></script>


<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="../favicon.ico" type="image/x-icon" />

<bean:define id="entidad" name="idEntidad" scope="session"/>

<script type="text/javascript" src='../dwr/interface/SSOAuthCheckService.js'></script>
<script type="text/javascript" src='../js/ssoAuth/ssoAuth.js'></script>
<script type="text/javascript">
checkSSOAuth(<%= (Boolean)request.getSession().getAttribute("SSOActive") %>, '<%= (String)request.getSession().getAttribute("TokenAttribute") %>');
</script>
<script type="text/javascript">
<!--
	
	var entidad = <bean:write name="entidad"/>;
	
//-->
</script>

<script type="text/javascript">
 <!--
 
 	   
 	   function preview() {
 	   		window.open('./preview.do');
 	   		/*var divResultUpload = document.getElementById('resultUpload');
 	   		if (document.getElementById('saveimg') == null){
 	   				divResultUpload.innerHTML = divResultUpload.innerHTML + '<p>&nbsp;</p><img id="saveimg" class="imageButton" src="../img/btn_guardar.gif" alt="Guardar" onclick="save();"/>'; 
 	    	}*/
 	   }
 	   
 	   function check() {
 	   		JCSS.check(checkReplyServer);
 	   }
 	   
 	   var checkReplyServer = {
  		    callback:function(data) { 
						; 	    				
  					},
  		    timeout:60000,
  		    errorHandler:function(message,ex) { 
  			  	  //problema de acceso a datos
			  			if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisDBException"){
				  			texto="Se ha producido un error, contacte con el administrador del sistema.";
				  			alert(texto);
			  				}
			  			//error de configuración	
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisConfigurationException"){
			  				texto="Se ha producido un error de configuración";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//un error de inicialización	
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInitiationExceptionn"){
			  				texto="Se ha producido un error de inicialización";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//parámetro inválido
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInvalidParameterException"){
			  				texto="Se ha recibido un parámetro inválido";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//mapa no encontrado
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisMapNotFoundException"){
			  				texto="Se ha producido un error al no encontrar el mapa";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//operación no permitida
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisWMSException"){
			  				texto="Error realizando operación con el servidor de mapas de la guía urbana";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//timeout
			  			else if(message=="Timeout"){
			  				texto="No ha sido posible realizar la operación, debido a que se ha sobrepasado el tiempo máximo para la misma.";
				  			alert(texto);
			  			}
			  			//otros errores
			  			else{ 		
			  				texto="Se ha producido un error";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
	   	}};
 	   
 	   
       function asyncUpload(elemento,tipoElemento,entidad) {
           check();
           var fileInput = document.getElementById(elemento);
           if (fileInput.value == '') {
           		alert('Seleccione un fichero');
           		return;
           }
           var entidadInput = document.getElementById(entidad);
           entidadInput.value = entidad; 
           var divResultUpload = document.getElementById('resultUpload');
           divResultUpload.innerHTML = "";
           var fileParent = fileInput.parentNode;
           var divHidden = document.getElementById('hidden'+tipoElemento);       	   
           
           if (divHidden == null) {
	           	divHidden = document.createElement('div');
	           	divHidden.setAttribute('id','hidden');
	           	divHidden.style.display = 'none';
	           	divHidden.innerHTML = '<iframe id="hidden_frame" name="hidden_frame" src=""></iframe>' +
	               				   '<form id="hidden_form'+tipoElemento+'" target="hidden_frame" action="./uploadFile.do?ext='+tipoElemento+'" enctype="multipart/form-data" method="post"></form>';
	           	document.body.appendChild(divHidden);
           }           
           var hiddenForm = document.getElementById("hidden_form"+tipoElemento);
           fileParent.removeChild(fileInput);
           fileParent.removeChild(entidadInput);
           hiddenForm.appendChild(fileInput);
       	   hiddenForm.appendChild(entidadInput);
       	   
           hiddenForm.submit();
           var divResultUpload = document.getElementById('resultUpload');
           divResultUpload.style.display = "";
           divResultUpload.style.color = "#FE9900";
           divResultUpload.innerHTML = "Subiendo ...";
           
           hiddenForm.removeChild(entidadInput);
           hiddenForm.removeChild(fileInput);
           fileParent.appendChild(fileInput);
       	   fileParent.appendChild(entidadInput);
       }
       
       var saveReplyServer = {
  		    callback:function(data) { 
						var divResultUpload = document.getElementById('resultUpload');
 	   					divResultUpload.innerHTML = data; 
 	    				
  					},
  		    timeout:60000,
  		    errorHandler:function(message, ex) { 
  			  	  //problema de acceso a datos
			  			if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisDBException"){
				  			texto="Se ha producido un error, contacte con el administrador del sistema.";
				  			alert(texto);
			  				}
			  			//error de configuración	
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisConfigurationException"){
			  				texto="Se ha producido un error de configuración";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//un error de inicialización	
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInitiationExceptionn"){
			  				texto="Se ha producido un error de inicialización";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//parámetro inválido
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInvalidParameterException"){
			  				texto="Se ha recibido un parámetro inválido";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//mapa no encontrado
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisMapNotFoundException"){
			  				texto="Se ha producido un error al no encontrar el mapa";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//operación no permitida
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisWMSException"){
			  				texto="Error realizando operación con el servidor de mapas de la guía urbana";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//timeout
			  			else if(message=="Timeout"){
			  				texto="No ha sido posible realizar la operación, debido a que se ha sobrepasado el tiempo máximo para la misma.";
				  			alert(texto);
			  			}
			  			//otros errores
			  			else{ 		
			  				texto="Se ha producido un error";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
	   		}};    
    
    	function save() {
    		JCSS.save(saveReplyServer);
    	}
    
	    function textHtmlHandler(){
	    	document.location.href= 'index.jsp';
	   	}
	   	
	    function init() {
			DWREngine.setTextHtmlHandler(textHtmlHandler);
	    }
	    
	    function removeCSS() {
            JCSS.removeCSS(entidad, removeCSSReplyServer);
	    }    

        var removeCSSReplyServer = {
            callback:function(data) { 
                var divEliminarHojaEstilos = document.getElementById('eliminarHojaEstilos');
                var divAdjuntarHojaEstilos = document.getElementById('adjuntarHojaEstilos');
                divEliminarHojaEstilos.style.display = 'none';
                divAdjuntarHojaEstilos.style.display = '';
            },
            timeout:60000,
            errorHandler:function(message,ex) { 
                  //problema de acceso a datos
			  			if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisDBException"){
				  			texto="Se ha producido un error, contacte con el administrador del sistema.";
				  			alert(texto);
			  				}
			  			//error de configuración	
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisConfigurationException"){
			  				texto="Se ha producido un error de configuración";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//un error de inicialización	
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInitiationExceptionn"){
			  				texto="Se ha producido un error de inicialización";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//parámetro inválido
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisInvalidParameterException"){
			  				texto="Se ha recibido un parámetro inválido";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//mapa no encontrado
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisMapNotFoundException"){
			  				texto="Se ha producido un error al no encontrar el mapa";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//operación no permitida
			  			else if(ex.javaClassName=="com.localgis.web.core.exceptions.LocalgisWMSException"){
			  				texto="Error realizando operación con el servidor de mapas de la guía urbana";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
			  			//timeout
			  			else if(message=="Timeout"){
			  				texto="No ha sido posible realizar la operación, debido a que se ha sobrepasado el tiempo máximo para la misma.";
				  			alert(texto);
			  			}
			  			//otros errores
			  			else{ 		
			  				texto="Se ha producido un error";
				  			if(message!='')
				  			texto+="\r\n Descripción: "+message;
				  			alert(texto);
			  				}
            }
        };    
       
       function selectFile() {
           var inputFile = document.getElementById("file");
           inputFile.click();
           openInputFile();
       }
       
       function goHome(){
		document.location.href="${pageContext.request.contextPath}";
		}
//-->
</script>
<bean:define id="nombreEntidad" name="nombreEntidad" scope="session"/>


</head>
<body onload="init();">
<div id="wrap">
<div id="top" class="top">
<img src="../img/top_left.gif" alt="Esquina superior izquierda" class="esquina_sup_izq" />
<img src="../img/top_right.gif" alt="Esquina superior derecha" class="esquina_sup_der" />

</div>
<div id="content" class="content">
<div id="boxcontrol" class="boxcontrol">
<div id="bannerDiseno" onclick="goHome()"></div>
	<table >
		<tr>
			<td><h1>Gestión de Diseño (<bean:write name="nombreEntidad"/>)</h1></td>
			<td align="right" style="width:38em"></td>
			<td><html:img styleClass="imageButton" onclick="document.location.href='./menu.do'"   alt="menu" src="../img/btn_menu.gif"/></td>
			<td><html:img styleClass="imageButton" onclick="document.location.href='./logoff.do'" alt="salir" src="../img/btn_cerrar_sesion.gif"  /></td>
		</tr>
		
	</table>
 	<p>&nbsp;</p>
	<p>&nbsp;</p>
    <logic:notPresent name="css" scope="request">
        <div id="adjuntarHojaEstilos">
    </logic:notPresent>
    <logic:present name="css" scope="request">
        <div id="adjuntarHojaEstilos" style="display: none;">
    </logic:present>
			<table style="margin-left: auto;margin-right: auto;text-align:center">
                <logic:present name="dynamicCSS" scope="request">
                    <tr>
                        <td colspan="2" align="center" style="font-size: 10px; color: #A0A0A0">
                            Puede descargar la hoja de estilos de la Guía Urbana dinamica <a href="<bean:write name="dynamicCSS" scope="request"/>" onclick="window.open(this.href);return false;" style="color: #FF6600;">aquí</a>
                            <br>
                            Puede descargar la hoja de estilos de la Guía Urbana estatica <a href="<bean:write name="staticCSS" scope="request"/>" onclick="window.open(this.href);return false;" style="color: #FF6600;">aquí</a>
                            <br>
                            Puede descargar la hoja de estilos de la Guía Urbana (Openlayers) <a href="<bean:write name="dynamicCSSOpenLayers" scope="request"/>" onclick="window.open(this.href);return false;" style="color: #FF6600;">aquí</a>
                            <br>
                            Puede descargar un ejemplo de personalizacion <a href="<bean:write name="dynamicCSSSample" scope="request"/>" onclick="window.open(this.href);return false;" style="color: #FF6600;">aquí</a>
                            <br>
                            Puede descargar la cabecera actual desde aqui <a href="<bean:write name="cabeceraActual" scope="request"/>" onclick="window.open(this.href);return false;" style="color: #FF6600;">aquí</a>
                            <br/>
                            <br/>
                            <br/>
                            <br/>
                        </td>
                    </tr>
                </logic:present>
                <tr>
                    <td align="right">Adjuntar Hoja de Estilos</td>
                    <td>
                        <div><input type="file" name="file" id="file" size="40"/>
                             <input type="hidden" name="entidad" id="entidad"/>
                        </div>                      
                    </td>
                </tr>
				<tr>
					<td colspan="2"align="center">
						<p>&nbsp;</p>
						<html:img styleClass="imageButton" src="../img/btn_aceptar.gif" alt="aceptar" onclick="asyncUpload('file','css','entidad');"/>
						<p>&nbsp;</p>
					</td>
				</tr>
				
				 <tr>
                    <td align="right">Adjuntar Imagen Cabecera</td>
                    <td>
                        <div><input type="file" name="cabecera" id="cabecera" size="40"/>
                        	<input type="hidden" name="entidad_cabecera" id="entidad_cabecera"/>
                        </div>                      
                    </td>
                </tr>
				<tr>
					<td colspan="2"align="center">
						<p>&nbsp;</p>
						<html:img styleClass="imageButton" src="../img/btn_aceptar.gif" alt="aceptar" onclick="asyncUpload('cabecera','cabecera','entidad_cabecera');"/>
						<p>&nbsp;</p>
					</td>
				</tr>
				<tr>
					<td colspan="2"align="center">
						<div id="resultUpload" style="color:green;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:14px;style:block;display:none;">&nbsp;</div>
					</td>
				</tr>
				
				
			</table>
		</div>
    <logic:notPresent name="css" scope="request">
        <div id="eliminarHojaEstilos" style="display: none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:12px;color: #666666;font-weight:bold;margin-left: auto;margin-right: auto;text-align:center">
    </logic:notPresent>
    <logic:present name="css" scope="request">
        <div id="eliminarHojaEstilos" style="font-family:Verdana, Arial, Helvetica, sans-serif;font-size:12px;color: #666666;font-weight:bold;margin-left: auto;margin-right: auto;text-align:center">
    </logic:present>
            <table style="margin-left: auto;margin-right: auto;text-align:center">
                <tr>
                    <td colspan="2" align="left">Ya existe una hoja de estilos para la entidad supramunicipal</td>
                </tr>
                
                
                
                <tr>
                    <td colspan="2"align="center">
                        <p>&nbsp;</p>
                        <html:img styleClass="imageButton" src="../img/btn_eliminar.gif" alt="eliminar" onclick="removeCSS();"/>
                        <p>&nbsp;</p>
                        <div id="resultUpload" style="color:green;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:14px;style:block">&nbsp;</div>
                    </td>
                </tr>
                 <tr>
                    <td align="right">Adjuntar Imagen Cabecera</td>
                    <td>
                        <div><input type="file" name="cabecera2" id="cabecera2" size="40"/>
                        	<input type="hidden" name="entidad_cabecera2" id="entidad_cabecera2"/>
                        </div>                      
                    </td>
                </tr>
				<tr>
					<td colspan="2"align="center">
						<p>&nbsp;</p>
						<html:img styleClass="imageButton" src="../img/btn_aceptar.gif" alt="aceptar" onclick="asyncUpload('cabecera2','cabecera2','entidad_cabecera2');"/>
						<p>&nbsp;</p>
					</td>
				</tr>
            </table>
        </div>
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