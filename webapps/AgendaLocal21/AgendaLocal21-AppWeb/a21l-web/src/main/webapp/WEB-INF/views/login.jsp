<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 

<html>
<head>
	<script src="js/util/md5.js"></script>
</head>
<body>

<div style="padding-top:10%; text-align:center; margin-left:auto; margin-right:auto;">
      <form id="formulario" action="<c:url value='j_spring_security_check' />" method="post">
     		<input type="hidden" id="userPasswordH" name="j_password" value="">
          <fieldset style="width:400px;margin-left:auto; margin-right:auto;" >
       		<c:if test="${errorAutentificacion}">
			<div class="controlAlertas">	
				<div class="ui-widget">
					<div class="ui-state-error ui-corner-all"> 
						<p><span class="ui-icon ui-icon-alert fizq"></span> 
						<strong><spring:message code="jsp.login.error.identificarse"/></strong> 
						</p>
					</div>
				</div>
			</div>
			</c:if>
			
           <div class="linea" style="margin-top: 10px;">
               <label class="label_login" for="userName" ><spring:message code="jsp.login.identificador"/></label>
               <input class="input_login" id="userName" name="j_username" type="text" class="input_pequeno"  accesskey="1"/>
           </div>
           <div class="clear"></div>
           <div class="linea" style="margin-top: 10px; margin-bottom: 10px;">
               <label class="label_login" for="userPassword"><spring:message code="jsp.login.contrasenha"/></label>
               <input class="input_login" id="userPasswordF" type="password" class="input_pequeno"  accesskey="2"/>
           </div>
           <div class="clear"></div>
           <div class="linea">
           		 <input type="checkbox" name="_spring_security_remember_me" id="_spring_security_remember_me" /> 
           		 <label for="_spring_security_remember_me"><spring:message code="jsp.login.recordar"/></label>
           </div> 
           <div class="clear"></div>
           	<div class="linea">
<%--         		<c:if test="${estadoPublicacion}"> --%>
<%--         			<a href="#" id="btnInvitado" class="boton fizq izq10 sup10"><spring:message code="jsp.login.boton.invitado.entrar"/></a> --%>
<%--         		</c:if> --%>
        		<a href="#" onclick="$('#formulario').submit();return false;" class="boton fder sup10" style="margin-right: 120px;"><spring:message code="jsp.login.boton.entrar"/></a>
        		<input type="submit" class="oculto_login" tabindex="-1"/>
           </div>  
            
          </fieldset>
          <div>
          	<br/>
          	<span><spring:message code="jsp.version.aplicacion"/></span>
          </div>
	</form>
</div>
<script type="text/javascript">
$(document).ready(function() {
	$('#userName').focus();
});

$(function(){
	  $("#formulario").submit(function() {
	        var password; 
	        var md5;
	        
	        password = $('#userPasswordF').val();
	        md5 = calcMD5(password);
	        $('#userPasswordH').val(md5);
	        
	    	return true;
	    });
	  
	  $("#btnInvitado").click(function(){
		  $("#userName").val("Invitado");
		  $('#userPasswordF').val("invitado");
		  $("#_spring_security_remember_me").val($("#_spring_security_remember_me").val());
		  $("#formulario").submit();
		  return false;
	  });
});
</script>
</body>
</html>
