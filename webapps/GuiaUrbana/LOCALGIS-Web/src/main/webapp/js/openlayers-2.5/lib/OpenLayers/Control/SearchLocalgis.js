/* Copyright (c) 2006 ASO crea este botón para hacer el zoomin */

/**
 * @class
 *
 * Imlements a very simple button control.
 * @requires OpenLayers/Control.js
 */
 
OpenLayers.Control.SearchLocalgis  = 
  OpenLayers.Class(OpenLayers.Control, {
    /** @type OpenLayers.Control.TYPE_* */
    type: OpenLayers.Control.TYPE_BUTTON,
 
    /** 
     * Property: targetCRSCode 
     * {String} 
     */
    targetCRSCode: null,

    /** 
     * Property: idEntidad 
     * {String} 
     */
    idEntidad: null,
    
    /**
     * Property: elementSelected
     * {li}
     */
    elementSelected: null,
    
    /**
     * Property: posX
     * {float}
     */
    posX: null,
    
    /**
     * Property: posY
     * {float}
     */
    posY: null,
    
    /**
    * @constructor
    *
    * @param {String} urlPlaceNameInfoService Url del servicio de toponimos
    */
    initialize: function(targetCRSCode, idEntidad) {
        OpenLayers.Control.prototype.initialize.apply(this, arguments);
        this.targetCRSCode = targetCRSCode;
        this.idEntidad = idEntidad;
    },
    
    /**
     * Evento de click en el control
     */
    trigger: function() {
        this.elementSelected = null;
        var getPlaceNameServicesReplyServer = {
            callback: function(data) {
                var contentHTML;
                if (data != undefined && data.length > 0) {
                    contentHTML = '<form name="searchPlaceNameForm">';
                    contentHTML += '<table style="margin-left:auto;margin-right:auto;text-align:center" cellpadding="3">';
                    contentHTML += '<tr>';
                    contentHTML += '<td align="left">Servicio a Consultar:</td>';
                    contentHTML += '<td align="left">';
                    contentHTML += '<select name="serviceAndType">';
                    for (var i = 0; i < data.length; i++) {
                        contentHTML += '<option value="'+data[i].service+';'+data[i].type+';'+data[i].featureType+'">'+data[i].name+'</option>';
                    }
                    contentHTML += '</select>';
                    contentHTML += '</td>';
                    contentHTML += '</tr>';
                    contentHTML += '<tr><td align="left">Criterio de búsqueda:</td>';
                    contentHTML += '<td align="left"><input type="text" class="inputTextField" name="query" value="" size="20"/></td>';
                    contentHTML += '</tr>';
                    contentHTML += '</tr>';
                    contentHTML += '<td align="center" colspan="2"><label><input type="checkbox" name="withoutSpacialRestriction"/> <span style="position: relative; top: -4px;">Sin restricción espacial</span></label></td>';
                    contentHTML += '</tr>';
                    contentHTML += '<tr>';
                    contentHTML += '<td align="center" colspan="2"><div id="divButtonSearchPlaceName" style="margin-top: 6px;"><img class="imageButton" src="img/btn_aceptar.gif" alt="Aceptar" onclick="OpenLayers.Control.SearchLocalgis.searchInfo(document.searchPlaceNameForm.serviceAndType.options[document.searchPlaceNameForm.serviceAndType.selectedIndex].value, document.searchPlaceNameForm.query.value, \''+getPlaceNameServicesReplyServer.searchLocalgis.idEntidad+'\',\''+getPlaceNameServicesReplyServer.searchLocalgis.targetCRSCode+'\', document.searchPlaceNameForm.withoutSpacialRestriction.checked);"/></div>';
                    contentHTML += '</td>';
                    contentHTML += '</tr>';
                    contentHTML += '</table>';
                    contentHTML += '</form>';
                } else {
                    contentHTML += 'No existe ningún servicio de búsqueda definido';
                }
                OpenLayers.LocalgisUtils.showPopup(contentHTML);
            },
            timeout:30000,
            errorHandler:function(message) { 
                OpenLayers.LocalgisUtils.showError();
            },
            searchLocalgis: this
        };
        
        WFSGService.getPlaceNameServices(getPlaceNameServicesReplyServer);
    },

    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.SearchLocalgis"
});

/**
 * Método para realizar la búsqueda de una query en un servicio determinado
 */
OpenLayers.Control.SearchLocalgis.searchInfo = function(serviceAndType, query, idEntidad, targetCRSCode, withoutSpacialRestriction) {
    var getPlaceNameResultsReplyServer = {
        callback: function(data) {
            var contentHTML;
            if (data != undefined && data.length > 0) {
                contentHTML = '<div id="popupLocalgisListResults">';
                contentHTML += '<ul class="popupLocalgisList">';
                for (var i = 0; i < data    .length; i++) {
                    contentHTML += '<li class="popupLocalgisListItem" onclick="OpenLayers.LocalgisUtils.selectResult(this,'+data[i].posX+', '+data[i].posY+')">'+data[i].name+'</li>';
                }
                contentHTML += '</ul>';
                contentHTML += '</div>';
                contentHTML += '<div id="popupLocalgisListGoResult" style="margin-top: 10px;" onclick="';
                if (withoutSpacialRestriction) {
                    contentHTML += 'OpenLayers.LocalgisUtils.goToResultWithoutSpacialRestriction();';
                } else {
                    contentHTML += 'OpenLayers.LocalgisUtils.goToResultEntidad();';
                }
                contentHTML += '"></div>';
            } else {
                contentHTML = '<br>No se ha encontrado ningún elemento.<br><br>';
            }
            OpenLayers.LocalgisUtils.showPopup(contentHTML);
        },
        timeout:30000,
        errorHandler:function(message) { 
            OpenLayers.LocalgisUtils.showError();
        }
    };
    var divButtonSearchPlaceName = document.getElementById('divButtonSearchPlaceName');
    divButtonSearchPlaceName.innerHTML = '<img src="'+OpenLayers.Util.getImagesLocation()+'ajax-loader.gif" alt="Buscando"/>';
    var serviceProperties = serviceAndType.split(";");
    var service = serviceProperties[0];
    var type = serviceProperties[1];
    var featureType = serviceProperties[2];
    WFSGService.getEntitiesWFS(service, type, featureType, query, idEntidad, targetCRSCode, withoutSpacialRestriction, getPlaceNameResultsReplyServer);
};
    
