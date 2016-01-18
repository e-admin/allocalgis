/* Copyright (c) 2006 ASO crea este botón para hacer el zoomin */

/**
 * @class
 *
 * Imlements a very simple button control.
 * @requires OpenLayers/Control.js
 */
 var dhxWins, nucleosWin;

OpenLayers.Control.ZoomNucleo  = 
  OpenLayers.Class(OpenLayers.Control, {
    /** @type OpenLayers.Control.TYPE_* */
  //  type: OpenLayers.Control.TYPE_BUTTON,
    type: OpenLayers.Control.TYPE_BUTTON,
    
    /** 
     * Property: targetCRSCode 
     * {String} 
     */
    
    
   
    initialize: function() {
        OpenLayers.Control.prototype.initialize.apply(this, arguments);
      
    },
    
    /**
     * Evento de click en el control
     */
    trigger: function() {
    	  var url;
    	  url = "public/obtenerInformacionZoomNucleo.do" + "?";
          url += OpenLayers.Util.getParameterString(
                     {
                      REQUEST: "showZoomNucleo"
                     });
        OpenLayers.LocalgisUtils.showSearchingPopup();
          
    	OpenLayers.loadURL(url, '', this, this.showZoomNucleo, this.showErrorZoomNucleo);
           
    },
    
    showZoomNucleo: function(xmlHTTPRequest) {

        if (xmlHTTPRequest.status == 200) {

            var contentHTML;;
            var result;
            //cerramos el Popups de espera.
            this.map.removeAllPopups();
            if (xmlHTTPRequest.responseText != '') {
                result = true;
                contentHTML = "<div id=\"nucleosList\" style=\"width:100%;height:100%;overflow:auto;\"> "
                				+xmlHTTPRequest.responseText
                				+"</div>";
               
            } else {
                result = false;
                contentHTML = "<br>No se ha encontrado ningún elemento.<br><br>";
            }
          // OpenLayers.LocalgisUtils.showPopup(contentHTML);
                  
            dhxWins = new dhtmlXWindows();
            dhxWins.setImagePath("js/dhtmlx_Windows/imgs/");
            //dhxWins.enableAutoViewport(false);
           // dhxWins.attachViewportTo("wrapShowMap");
            nucleosWin=dhxWins.createWindow("Nucleos", 200, 200, 300, 400);
            dhxWins.window("Nucleos").allowMove();
            nucleosWin.zi=9999;
            nucleosWin.setText("Listado Núcleos");
            //nucleosWin.button("close").enable();          
            nucleosWin.setModal(true);                      
            nucleosWin.attachHTMLString(contentHTML);
       
        }
    },
    
    showErrorZoomNucleo: function(xmlHTTPRequest) {

        var contentHTML;;
        contentHTML = "<br>No se ha encontrado ningún elemento.<br><br>";

        OpenLayers.LocalgisUtils.showPopup(contentHTML);
    },
    
    
    
    
    
 
    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.ZoomNucleo"
});

