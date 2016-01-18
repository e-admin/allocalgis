
function checkSSOAuth(ssoActive, idSesion){
	if(ssoActive==true){
		if(idSesion!=null){
			checkDWR(idSesion);
			setInterval("checkDWR('" + idSesion + "')", 30000);
		}
		else logout();
	}
}

function checkDWR(idSesion){
	SSOAuthCheckService.checkSSOAuth(
		idSesion,			
		checkSSOAuthReplyServer = {
			callback: function(data){
				if(data==null || data==false){
					logout();
				}
			},
			timeout:20000
		}
	);
}

function logout(){
	window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/localgis-wmsmanager/admin/logoff.do";
}