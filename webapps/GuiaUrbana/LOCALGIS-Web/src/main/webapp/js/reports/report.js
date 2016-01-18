function generateReport(idLayer,idFeature,idEntidad,idMunicipio,locale,plantillas){
	
	//OpenLayers.LocalgisUtils.showSearchingPopup();
	//document.location.href="public/report.do?idLayers="+idLayer+"\"&idFeatures=\""+idFeature+"\"&x="+x+"&y="+y;
	
	
	this.report = new OpenLayers.Control.ReportLocalgis();
	report.showPopupReport(idLayer,idFeature,idEntidad,idMunicipio,locale,plantillas);
	
}



