<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="es">
<head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><bean:message key="welcome.title"/></title>
<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="../favicon.ico" type="image/x-icon" />
<script type='text/javascript' src='../dwr/interface/JAttribute.js'></script>
<script type='text/javascript' src='../dwr/engine.js'></script>
<script type='text/javascript' src='../dwr/util.js'></script>
<script type='text/javascript' src='../dwr/interface/JLegend.js'></script>

<script type="text/javascript" src='../dwr/interface/SSOAuthCheckService.js'></script>
<script type="text/javascript" src='../js/ssoAuth/ssoAuth.js'></script>
<script type="text/javascript">
if('${requestScope.configurationLocalgisWeb}'=='private')
	checkSSOAuth(<%= (Boolean)request.getSession().getAttribute("SSOActive") %>, '<%= (String)request.getSession().getAttribute("TokenAttribute") %>');
</script>
<script type="text/javascript">
 <!--
 	   function check() {
 	   		JAttribute.check(checkReplyServer);
 	   }
 	   
 	   var checkReplyServer = {
  		    callback:function(data) { 
						; 	    				
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
 	   
 	   
       function asyncUpload() {
           check();
           var fileInput = document.getElementById("file");
           var typeInput = document.getElementById("type");
           var entidadInput = document.getElementById("entidad");
           var layerInput =  document.getElementById("layer");
           layerInput.value = layerSelected();
           if (fileInput.value == '') {
           		alert('Seleccione un Fichero');
           		return false;
           }
           var divSeleccionLeyenda = document.getElementById('seleccionLeyenda');
           divSeleccionLeyenda.style.display = 'none';
           var divBotonAdjuntar = document.getElementById('botonAdjuntar');
           divBotonAdjuntar.style.display = 'none';
           var divResultUpload = document.getElementById('resultUpload');
           divResultUpload.style.display = '';
           divResultUpload.style.color = "green";
           divResultUpload.innerHTML = "Subiendo ...";
           var fileParent = fileInput.parentNode;
           var divHidden = document.getElementById('hidden');
           if (divHidden == null) {
	           	divHidden = document.createElement('div');
	           	divHidden.setAttribute('id','hidden');
	           	divHidden.style.display = 'none';
	           	divHidden.innerHTML = '<iframe id="hidden_frame" name="hidden_frame" src=""></iframe>' +
	               				   '<form id="hidden_form" target="hidden_frame" action="./uploadFile.do?ext=img" enctype="multipart/form-data" method="post"></form>';
	           	document.body.appendChild(divHidden);
           }           
           var hiddenForm = document.getElementById("hidden_form");
           fileParent.style.display = "none";
           fileParent.removeChild(fileInput);
           fileParent.removeChild(typeInput);
           fileParent.removeChild(entidadInput);
           fileParent.removeChild(layerInput);
           hiddenForm.appendChild(fileInput);
           hiddenForm.appendChild(typeInput);
       	   hiddenForm.appendChild(entidadInput);
       	   hiddenForm.appendChild(layerInput);
           hiddenForm.submit();
           
           hiddenForm.removeChild(typeInput);
           hiddenForm.removeChild(entidadInput);
           hiddenForm.removeChild(layerInput);
           hiddenForm.removeChild(fileInput);
           fileParent.appendChild(fileInput);
           fileParent.appendChild(typeInput);
       	   fileParent.appendChild(entidadInput);
       	   fileParent.appendChild(layerInput);
       }
       
//-->
</script>


<script type="text/javascript">
<!--

       function moveOptions(optionsSource,optionsTarget) {
			
               var length = optionsSource.length;
               var i = 0;
               var j = 0;
               var optionsToMove = new Array();
               while (i< optionsSource.length) {
               		if (optionsSource[i].selected) {
               			
               			optionsSource[i].selected = false;
                        optionsToMove[j++] = optionsSource[i];
                        try {
                            optionsSource[i--] = null;
                        } catch (e) {

                        }
                    }
                     i++;   
               }
               j = 0;
               for (i=optionsTarget.length ;i < optionsTarget.length + optionsToMove.length ;i++) {
                   try {
                            optionsTarget[i] = optionsToMove[j++];

                    }catch (e) {

                    }

               }
       }

	function previous() {
        var optionsTarget = document.getElementById('private').options;
        var optionsSource = document.getElementById('public').options;
        moveOptions(optionsSource,optionsTarget);
    }
      
    function next() {
	    var optionsSource = document.getElementById('private').options;
        var optionsTarget = document.getElementById('public').options;
        moveOptions(optionsSource,optionsTarget);
    }  
	
	var dataToSend;
	function chargeAttributes() {
		setTimeout('getAttributes();',500);	
	}
	
	function getAttributes() {
		var select = document.getElementById('layers');
		var selectedIndex = select.selectedIndex;
		var selectedValue = select.options[selectedIndex].value;
		try {
			if (type.indexOf('publico')<0) {
				JAttribute.getAttributesByLayer(selectedValue, entidad ,false ,attributesReplyServer);
				JLegend.getLegend(selectedValue,false,getLegendReplyServer);
			} else {
				JAttribute.getAttributesByLayer(selectedValue, entidad, true ,attributesReplyServer);
				JLegend.getLegend(selectedValue,true,getLegendReplyServer);
			}			
		} catch (e) {
			alert(e);
		}
	}

	var idLayer ;
	function saveAttributes() {	
		if (type.indexOf('publico')<0) {
			JAttribute.saveRestrictedAttributes(idLayer,entidad,dataToSend , false ,saveRestrictedAttributesReplyServer);
		} else {
			JAttribute.saveRestrictedAttributes(idLayer,entidad,dataToSend , true ,saveRestrictedAttributesReplyServer);
		}
	}
	
	function layerSelected() {
		var select = document.getElementById('layers');
		var selectedIndex = select.selectedIndex;
		return select.options[selectedIndex].value;
	}
	
	function save() {
		var restrictedAttributes = document.getElementById('private').options;
		var i = 0;
		dataToSend = new Array();
		var select = document.getElementById('layers');
		var selectedIndex = select.selectedIndex;
		idLayer = select.options[selectedIndex].value;
		for(i = 0;i<restrictedAttributes.length;i++) {
			dataToSend[i] = restrictedAttributes[i].value + ';' + restrictedAttributes[i].text;
		}
		var image = document.getElementById('image');
		image.src = '../img/ajax-bar.gif';
		saveAttributes();
	}
	
    var getLegendReplyServer = {
  			callback:function(data) { 
                        var divSeleccionLeyenda = document.getElementById('seleccionLeyenda');
                        var divResultUpload = document.getElementById('resultUpload');
                        var divBotonAdjuntar = document.getElementById('botonAdjuntar');
                        var divBotonEliminar = document.getElementById('botonEliminar');
                        if (data != null) {
                            divSeleccionLeyenda.style.display = 'none';
                            divBotonAdjuntar.style.display = 'none';
                            divBotonEliminar.style.display = '';
                            divResultUpload.style.display = '';
    						divResultUpload.innerHTML = '<img src="' + data +'" heigth="30px" width="40px"/>';
    				    } else {
                            divSeleccionLeyenda.style.display = '';
                            divBotonAdjuntar.style.display = '';
                            divResultUpload.style.display = 'none';
                            divBotonEliminar.style.display = 'none';
                            divResultUpload.innerHTML = '';
    				    }
  					},
  			timeout:60000,
  			errorHandler:function(message) { 
                        var divSeleccionLeyenda = document.getElementById('seleccionLeyenda');
                        divSeleccionLeyenda.style.display = 'none';
                        var divResultUpload = document.getElementById('resultUpload');
                        divResultUpload.innerHTML = "<img style='position:relative;top:30%;' src='../img/error.gif'/>"; }
	};      

    var removeLegendReplyServer = {
            callback:function(data) { 
                        var divSeleccionLeyenda = document.getElementById('seleccionLeyenda');
                        var divResultUpload = document.getElementById('resultUpload');
                        var divBotonAdjuntar = document.getElementById('botonAdjuntar');
                        var divBotonEliminar = document.getElementById('botonEliminar');
                        divSeleccionLeyenda.style.display = '';
                        divBotonAdjuntar.style.display = '';
                        divBotonEliminar.style.display = 'none';
                        divResultUpload.style.display = 'none';
                        divResultUpload.innerHTML = '';
                    },
            timeout:60000,
            errorHandler:function(message) { 
                        var divSeleccionLeyenda = document.getElementById('seleccionLeyenda');
                        var divResultUpload = document.getElementById('resultUpload');
                        var divBotonAdjuntar = document.getElementById('botonAdjuntar');
                        var divBotonEliminar = document.getElementById('botonEliminar');
                        divSeleccionLeyenda.style.display = 'none';
                        divBotonAdjuntar.style.display = 'none';
                        divBotonEliminar.style.display = 'none';
                        divResultUpload.style.display = '';
                        divResultUpload.innerHTML = "<img src='../img/error.gif'/>"; }
    };      

    function removeLegend() {
        var select = document.getElementById('layers');
        var selectedIndex = select.selectedIndex;
        var selectedValue = select.options[selectedIndex].value;
        var divBotonEliminar = document.getElementById('botonEliminar');
        var divResultUpload = document.getElementById('resultUpload');
        divBotonEliminar.style.display = 'none';
        divResultUpload.style.color = "green";
        divResultUpload.innerHTML = "Eliminando ...";
        try {
            if (type.indexOf('publico')<0) {
                JLegend.removeLegend(selectedValue,false,removeLegendReplyServer);
            } else {
                JLegend.removeLegend(selectedValue,true,removeLegendReplyServer);
            }           
        } catch (e) {
            alert(e);
        }
    }
    
    var attributesReplyServer = {
  			callback:function(data) { 
				    if (data != null && typeof data == 'object') {
				     	var i = 0;
				     	var layerAttributes = data['layerAttributes'];
				     	var restrictedAttributes = data['restrictedAttributes'];
				     	var publicOptions = new Array();
				     	var privateOptions = new Array();
				     	var i = 0;
				     	for(i = 0;i<layerAttributes.length;i++) {
				     		publicOptions[i] = {name:layerAttributes[i].idAttributeGeopista + ';' 
				     					+ layerAttributes[i].idAlias,value:layerAttributes[i].alias};
							
				     				     	
				     	}
				     	for (i = 0;i<restrictedAttributes.length;i++) {
				     			privateOptions[i] = {name:restrictedAttributes[i].attributeidgeopista + ';' 
				     					+ restrictedAttributes[i].idalias,value:restrictedAttributes[i].alias};
				     	}
				     	dwr.util.removeAllOptions("public");
				     	dwr.util.removeAllOptions("private");
				      	dwr.util.addOptions("public",publicOptions,'name','value');
				     	dwr.util.addOptions("private",privateOptions,'name','value');
                        var divCarga = document.getElementById('carga');
                        divCarga.style.display = 'none';
                        var divResultadoCarga = document.getElementById('resultadoCarga');
                        divResultadoCarga.style.display = '';
				     }
  					},
  			timeout:60000,
  			errorHandler:function(message) { 
                        dwr.util.removeAllOptions("public");
                        dwr.util.removeAllOptions("private");
                        var divResultadoCarga = document.getElementById('resultadoCarga');
                        divResultadoCarga.style.display = 'none';
  						var divCarga = document.getElementById('carga');
  						divCarga.style.display = '';
  						divCarga.innerHTML = "<img style='position:relative;top:30%;' src='../img/error.gif'/>"; }
	};
    
    var saveRestrictedAttributesReplyServer = {
  			callback:function(data) { 
  								var image = document.getElementById('image');
								image.src = '../img/btn_aceptar.gif';
  					},
  			timeout:60000,
  			errorHandler:function(message) { 
  						var image = document.getElementById('image');
  						image.src = '../img/btn_aceptar.gif';
                        dwr.util.removeAllOptions("public");
                        dwr.util.removeAllOptions("private");
                        var divResultadoCarga = document.getElementById('resultadoCarga');
                        divResultadoCarga.style.display = 'none';
                        var divCarga = document.getElementById('carga');
                        divCarga.style.display = '';
                        divCarga.innerHTML = "<img style='position:relative;top:30%;' src='../img/error.gif'/>"; }
	};    
    
    
    function textHtmlHandler(){
    	document.location.href= 'index.jsp';
   	}
   	
    function init() {
		DWREngine.setTextHtmlHandler(textHtmlHandler);
		chargeAttributes();
    }    

       
//-->
</script>
<bean:define id="nombreEntidad" name="nombreEntidad" scope="session"/>
<bean:define id="entidad" name="idEntidad" scope="session"/>
<bean:define id="tipoMapas" name="tipoMapas" scope="session"/>
<bean:define id="type" name="type" scope="request"/>

<script type="text/javascript">
<!--
	
	var type = '<bean:write name="type"/>';
	var entidad = <bean:write name="entidad"/>;
	
	
function goHome(){
	document.location.href="${pageContext.request.contextPath}";
}	
//-->
</script>
</head>
<body onload="init();">
<div id="wrap">
<div id="top" class="top">
<img src="../img/top_left.gif" alt="Esquina superior izquierda" class="esquina_sup_izq" />
<img src="../img/top_right.gif" alt="Esquina superior derecha" class="esquina_sup_der" />




</div>
<div id="content" class="content">
<div id="boxcontrol" class="boxcontrol">
<div id="bannerCapas" onclick="goHome()">
</div>
<logic:present name="layers">
	<table >
		<tr>
			<td><h1>Gestión de Capas (<bean:write name="nombreEntidad"/> - <bean:write name="tipoMapas"/>)</h1></td>
			<td align="right" style="width:38em"></td>
			<td><html:img styleClass="imageButton" alt="menu" onclick="document.location.href='./menu.do'"   src="../img/btn_menu.gif"/></td>
			<td><html:img styleClass="imageButton" alt="salir" onclick="document.location.href='./logoff.do'" src="../img/btn_cerrar_sesion.gif"  /></td>
		</tr>
	</table>

	<p>&nbsp;</p>
	<p>&nbsp;</p>

	<div style="color:green;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:14px;"><html:messages id="msg" property="message" message="true"><bean:write name="msg"/></html:messages></div>
 	<div style="width:100%;height:200px;margin-left:0;text-align:center;">
 	
		<!--<form action="" method="post"> -->
			<table style="">
				<tr>
					<td>
						<h1>Seleccione una capa</h1>
						<form action="" method="post">
							<br/>
							<label for="layers">Layers</label>
							<select id="layers" name="layers" style="width:18em" onchange="chargeAttributes();">
								<logic:iterate id="item" name="layers" scope="request">
									<option value="${item.idLayer}">
										<logic:empty name="item"  property="name">Mapa</logic:empty>
										<logic:notEmpty name="item"  property="name">
											<bean:write name="item" property="name" />
										</logic:notEmpty>										
									</option>
								</logic:iterate>
							</select>
						</form>
					</td>
                    <td align="center" rowspan="2" valign="top">
                        <div id="carga">
                        </div>
                    </td>
                    <td align="right" rowspan="2" valign="top">
						<div id="resultadoCarga">
						<table
							style="margin-left: 10%; margin-right: auto; text-align: center; width: 85%; height: 55%;">
							<tr>
								<td>
								<h1>Campos privados</h1>
								</td>
								<td></td>
								<td>
								<h1>Campos públicos</h1>
								</td>
							</tr>
							<tr>
								<td><select size="8" id="private" name="available"
									multiple="multiple"
									style="background-color: #F7F7F7; width: 14em;">
								</select></td>
			
								<td><html:img src="../img/btn_remove.gif"
									alt="Flecha izquierda" width="23" height="16"
									onclick="previous();" /> <html:img src="../img/btn_add.gif"
									alt="Flecha derecha" width="23" height="16" onclick="next();" /></td>
								<td><select size="8" id="public" name="published"
									multiple="multiple"
									style="background-color: #F7F7F7; width: 14em;">
								</select></td>
							</tr>
							<tr>
								<td colspan="3" align="center"><br />
								<html:img styleClass="imageButton" styleId="image" border="0" src="../img/btn_aceptar.gif"
									alt="Aceptar" onclick="save();" /></td>
							</tr>
						</table>
						</div>
					</td>
				</tr>
				<tr>
					<td align="center">
						<div>
							<h1>Adjuntar imagen para la leyenda</h1>
							<div id="seleccionLeyenda" style="margin-top: 10px; display: none;">
							    <input name="file" id="file" type="file"/>
							    <input name="entidad" id="entidad" type="hidden" value="<bean:write name="entidad"/>"/>
							    <input name="type" id="type" type="hidden" value="<bean:write name="type"/>"/>
							    <input name="layer" id="layer" type="hidden"/>
							</div>
                            <div id="botonAdjuntar" style="margin-top: 10px; display: none;">
                                <html:img styleClass="imageButton" alt="upload" src="../img/btn_adjuntar.gif" onclick="asyncUpload();"/>
                            </div>
                            <div id="botonEliminar" style="margin-top: 10px; display: none;">
                                <html:img styleClass="imageButton" alt="eliminar" src="../img/btn_eliminar.gif" onclick="removeLegend();"/>
                            </div>
						    <div id="resultUpload" style="margin-top: 10px; color:green;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:14px;style:block">
							</div> 
						</div>
					</td>
				</tr>
			</table>
		
		<!-- </form>  -->
	
	<!--  
		<div style="float:right;"><img alt="Logo" src="../img/footer.gif"/></div> 
	-->
	</div>
	</logic:present>
	
	<logic:notPresent name="layers">
	<table >
		<tr>
			<td><h1>No hay capas disponibles</h1></td>
			<td align="right" style="width:29em"></td>
			<td><html:img styleClass="imageButton" onclick="document.location.href='./menu.do'"   alt="menu" src="../img/btn_menu.gif"/></td>
			<td><html:img styleClass="imageButton" onclick="document.location.href='./logoff.do'" alt="salir" src="../img/btn_cerrar_sesion.gif" /></td>
		</tr>
	</table>
	</logic:notPresent>
	
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