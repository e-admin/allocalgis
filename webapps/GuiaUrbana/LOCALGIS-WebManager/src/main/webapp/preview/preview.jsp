<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html xhtml="true" lang="es">
<head>
<html:base ref="page" />
<link href="staticStyles.css" type="text/css" rel="stylesheet"/>
<link href="stylesOpenLayersDefault.css" type="text/css" rel="stylesheet"/>
<link href="stylesOpenLayersLocalgis.css" type="text/css" rel="stylesheet"/>
<style type="text/css">

<logic:present name="css" scope="request">
	<bean:write name="css" scope="request"/>
</logic:present>

</style>
</head>
    <body>
        <div id="wrapShowMap">
            <div id="top" class="top">
                <img src="staticImg/top_left.gif" alt="Esquina superior izquierda" class="esquina_sup_izq"/>
                <img src="staticImg/top_right.gif" alt="Esquina superior derecha" class="esquina_sup_der"/>
            </div>
            <div id="content" class="content">
                <div id="boxcontrol" class="boxcontrol">
                    <div id="bannerMapaContainer">
                        <div id="bannerMapa">
                            <div id="bannerMapaLeft">
                                <div id="bannerMapaRight">
                                    <div id="searchDiv">
                                        <form action="#" method="post" id="search">
                                            <table cellpadding="3">
                                                <tbody>
                                                    <tr>
                                                        <td>
                                                            Vía
                                                        </td>
                                                        <td>
                                                            <label>
                                                                <input type="text" class="inputTextField" id="via" name="via"/>
                                                            </label>
                                                            <label>
                                                                Nº <input type="text" class="inputTextField" style="width: 20px;" id="numero" name="numero"/>
                                                            </label>
                                                            <a href="javascript:void(0);">Buscar</a>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            X,Y
                                                        </td>
                                                        <td>
                                                            <label>
                                                                <input type="text" class="inputTextField" style="width: 100px;" size="15" id="x" name="x"/>
                                                            </label> 
                                                            <label>
                                                                <input type="text" class="inputTextField" style="width: 100px;" size="15" id="y" name="y"/>
                                                            </label>
                                                            <a href="javascript:void(0);">Buscar</a>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                       </form>
                                    </div>
                                    <div id="toolbar">  
                                        <div id="OpenLayers.Control.ToolbarLocalgis_12" style="position: absolute; z-index: 1013;" class="olControlToolbarLocalgis">
                                            <div class="olControlZoomInLocalgisItemInactive" title="Acercar"></div>
                                            <div class="olControlZoomOutLocalgisItemInactive" title="Alejar"></div>
                                            <div class="olControlZoomBoxItemInactive" title="Zoom a recuadro"></div>
                                            <div class="olControlZoomToMaxExtentItemInactive" title="Zoom a España"></div>
                                            <div class="olControlGetFeatureInfoLocalgisItemInactive" title="Obtener información"></div>
                                            <div class="olControlNavigationLocalgisItemInactive" title="Mover"></div>
                                            <div class="olControlMarkerLocalgisItemInactive" title="Crear marca de posición"></div>
                                            <div class="olControlPrintLocalgisItemInactive" title="Imprimir"></div>
                                            <div class="olControlSearchLocalgisItemInactive" title="Buscar topónimos"></div>
                                            <div class="olControlSaveLocalgisItemInactive" title="Exportar WMC"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="map">
                        <div id="nombreMunicipio">  Herrera de Pisuerga - Mapa Callejero (Con Catastro) </div>
                        <div id="layerswitcher">
                            <div id="OpenLayers.Control.LayerSwitcherLocalgis_4" style="position: relative; font-family: Verdana; font-weight: bold; font-size: 12px; color: rgb(33, 33, 33); background-color: transparent; padding-top: 0pt; padding-bottom: 0pt; z-index: 1001;" class="olControlLayerSwitcherLocalgis">
                                <div id="layersDiv" style="padding: 0px; opacity: 0.75;">
                                    <div style="margin-top: 3px; margin-left: 3px; margin-bottom: 3px;"><u>Capas</u></div>
                                    <div style="padding-left: 10px;">
                                        <div class="olLayerSwitcherLocalgisLayerInactive">
                                            <input type="checkbox" id="input_Ortofoto" name="Ortofoto" value="Ortofoto" checked="checked"/>
                                            <div class="olLayerSwitcherLocalgisLayerWithoutLegend">
                                                <div class="olLayerSwitcherLocalgisLayerLabel" style="vertical-align: baseline;"><img src="staticImg/ortofoto.gif"/> Ortofoto</div>
                                            </div>
                                        </div>
                                        <div class="olLayerSwitcherLocalgisLegendVisible"></div>
                                        <div class="olLayerSwitcherLocalgisLayerInactive">
                                            <input type="checkbox" id="input_Provincias" name="Provincias" value="Provincias" checked="checked" disabled="disabled"/>
                                            <div class="olLayerSwitcherLocalgisLayerWithoutLegend">
                                                <div style="color: gray; vertical-align: baseline;" class="olLayerSwitcherLocalgisLayerLabel"><img src="staticImg/layerProvincias.gif"/> Provincias</div>
                                            </div>
                                        </div>
                                        <div class="olLayerSwitcherLocalgisLegendVisible"></div>
                                        <div class="olLayerSwitcherLocalgisLayerInactive">
                                            <input type="checkbox" id="input_Parcelas" name="Parcelas" value="Parcelas" checked="checked"/>
                                            <div class="olLayerSwitcherLocalgisLayerExpanded">
                                                <div class="olLayerSwitcherLocalgisLayerLabel" style="vertical-align: baseline;">&nbsp;&nbsp;&nbsp;&nbsp;Parcelas</div>
                                            </div>
                                        </div>
                                        <div class="olLayerSwitcherLocalgisLegendVisible">
                                            <img alt="Leyenda capa Parcelas" src="staticImg/leyendaParcelas.png"/>
                                        </div>
                                        <div class="olLayerSwitcherLocalgisLayerInactive">
                                            <input type="checkbox" id="input_Tramos de calle" name="Tramos de calle" value="Tramos de calle" checked="checked"/>
                                            <div class="olLayerSwitcherLocalgisLayerExpanded">
                                                <div class="olLayerSwitcherLocalgisLayerLabel" style="vertical-align: baseline;">&nbsp;&nbsp;&nbsp;&nbsp;Tramos de calle</div>
                                            </div>
                                        </div>
                                        <div class="olLayerSwitcherLocalgisLegendVisible">
                                            <img alt="Leyenda capa Tramos de calle" src="staticImg/leyendaTramosCalle.png"/>
                                        </div>
                                        <div class="olLayerSwitcherLocalgisLayerInactive">
                                            <input type="checkbox" id="input_Calles" name="Calles" value="Calles" checked="checked"/>
                                            <div class="olLayerSwitcherLocalgisLayerExpanded">
                                                <div class="olLayerSwitcherLocalgisLayerLabel" style="vertical-align: baseline;">&nbsp;&nbsp;&nbsp;&nbsp;Calles</div>
                                            </div>
                                        </div>
                                        <div class="olLayerSwitcherLocalgisLegendVisible">
                                            <img alt="Leyenda capa Calles" src="staticImg/leyendaCalles.png"/>
                                        </div>
                                        <div class="olLayerSwitcherLocalgisLayerInactive">
                                            <input type="checkbox" id="input_Números de policía" name="Números de policía" value="Números de policía" checked=""/>
                                            <div class="olLayerSwitcherLocalgisLayerExpanded">
                                                <div class="olLayerSwitcherLocalgisLayerLabel" style="vertical-align: baseline;">&nbsp;&nbsp;&nbsp;&nbsp;Números de policía</div>
                                            </div>
                                        </div>
                                        <div class="olLayerSwitcherLocalgisLegendVisible">
                                            <img alt="Leyenda capa Números de policía" src="staticImg/leyendaNumerosPolicia.png"/>
                                        </div>
                                        <div class="olLayerSwitcherLocalgisLayerInactive">
                                            <input type="checkbox" id="input_Catastro" name="Catastro" value="Catastro"/>
                                            <div class="olLayerSwitcherLocalgisLayerWithoutLegend">
                                                <div class="olLayerSwitcherLocalgisLayerLabel" style="vertical-align: baseline;"><img src="staticImg/layerWMS.gif"/>Catastro</div>
                                            </div>
                                        </div>
                                        <div class="olLayerSwitcherLocalgisLegendVisible"></div>
                                        <div class="olLayerSwitcherLocalgisLayerInactive">
                                            <input type="checkbox" id="input_Marcas de posición" name="Marcas de posición" value="Marcas de posición" checked=""/>
                                            <div class="olLayerSwitcherLocalgisLayerExpanded">
                                                <div class="olLayerSwitcherLocalgisLayerLabel" style="vertical-align: baseline;">&nbsp;&nbsp;&nbsp;&nbsp;Marcas de posición</div>
                                            </div>
                                        </div>
                                        <div class="olLayerSwitcherLocalgisLegendVisible" style="margin-top: 5px;"></div>
                                   </div>
                               </div>
                            </div>
                        </div>
                        <div id="mapDetail" style="">
                            <div id="mapDetail_OpenLayers_ViewPort" style="overflow: hidden; position: relative; width: 100%; height: 100%;" class="olMapViewport">
                                <div id="mapDetail_OpenLayers_Container" style="position: absolute; z-index: 749; left: 0px; top: 0px;">
                                    <div id="OpenLayers.Layer.WMS_57" style="position: absolute; width: 100%; height: 100%; z-index: 100;">
                                        <div style="overflow: hidden; position: absolute; left: -160px; top: -118px; width: 960px; height: 709px;">
                                            <img id="OpenLayersDiv355" style="width: 960px; height: 709px; position: relative;" class="olTileImage" src="staticImg/white.png"/>
                                        </div>
                                    </div>
                                <div id="OpenLayers.Layer.WMSLocalgis_63" style="position: absolute; width: 100%; height: 100%; z-index: 330; display: block;">
                                    <div style="overflow: hidden; position: absolute; left: -160px; top: -118px; width: 960px; height: 709px;">
                                        <img id="OpenLayersDiv358" style="width: 960px; height: 709px; position: relative;" class="olTileImage" src="staticImg/capaOrtofoto.jpg"/>
                                    </div>
                                </div>
                                <div id="OpenLayers.Layer.WMSLocalgis_78" style="position: absolute; width: 100%; height: 100%; z-index: 335; display: none;">
                                    <div style="overflow: hidden; position: absolute; left: -160px; top: -118px; width: 960px; height: 709px;">
                                        <img id="OpenLayersDiv388" style="width: 960px; height: 709px; position: relative;" class="olTileImage" src="staticImg/capaProvincias.png"/>
                                    </div>
                                </div>
                                <div id="OpenLayers.Layer.WMSLocalgis_99" style="position: absolute; width: 100%; height: 100%; z-index: 340; display: block;">
                                    <div style="overflow: hidden; position: absolute; left: -160px; top: -118px; width: 960px; height: 709px;">
                                        <img id="OpenLayersDiv418" style="left: -10px; top: -10px; width: 980px; height: 729px; position: relative; opacity: 0.85;" class="olTileImage" src="staticImg/capaParcelas.png"/>
                                    </div>
                                </div>
                                <div id="OpenLayers.Layer.WMSLocalgis_126" style="position: absolute; width: 100%; height: 100%; z-index: 345; display: block;">
                                    <div style="overflow: hidden; position: absolute; left: -160px; top: -118px; width: 960px; height: 709px;">
                                        <img id="OpenLayersDiv448" style="left: -10px; top: -10px; width: 980px; height: 729px; position: relative; opacity: 0.85;" class="olTileImage" src="staticImg/capaTramosCalle.png"/>
                                    </div>
                                </div>
                                <div id="OpenLayers.Layer.WMSLocalgis_159" style="position: absolute; width: 100%; height: 100%; z-index: 350; display: block;">
                                    <div style="overflow: hidden; position: absolute; left: -160px; top: -118px; width: 960px; height: 709px;">
                                        <img id="OpenLayersDiv478" style="left: -10px; top: -10px; width: 980px; height: 729px; position: relative; opacity: 0.85;" class="olTileImage" src="staticImg/capaCalles.png"/>
                                    </div>
                                </div>
                                <div id="OpenLayers.Layer.WMSLocalgis_198" style="position: absolute; width: 100%; height: 100%; z-index: 355; display: block;">
                                    <div style="overflow: hidden; position: absolute; left: -160px; top: -118px; width: 960px; height: 709px;">
                                        <img id="OpenLayersDiv508" style="left: -10px; top: -10px; width: 980px; height: 729px; position: relative; opacity: 0.85;" class="olTileImage" src="staticImg/capaNumerosPolicia.png"/>
                                    </div>
                                </div>
                                <div id="OpenLayers.Layer.WMSLocalgis_243" style="position: absolute; width: 100%; height: 100%; z-index: 360; display: none;">
                                    <div style="overflow: hidden; position: absolute; left: -160px; top: -118px; width: 960px; height: 709px;">
                                        <img id="OpenLayersDiv538" style="left: -10px; top: -10px; width: 980px; height: 729px; position: relative; opacity: 0.85;" class="olTileImage" src="staticImg/capaCatastro.png"/>
                                    </div>
                                </div>
                                <div id="OpenLayers.Layer.Markers_294" style="position: absolute; width: 100%; height: 100%; z-index: 365; display: block;"></div>
                                <div id="OpenLayers.Layer.Markers_297" style="position: absolute; width: 100%; height: 100%; z-index: 370;"></div>
                            </div>
                            <div id="OpenLayers.Control.ZoomInLocalgis_13" style="position: absolute; z-index: 1003;" class="olControlZoomInLocalgis"></div>
                            <div id="OpenLayers.Control.ZoomOutLocalgis_14" style="position: absolute; z-index: 1004;" class="olControlZoomOutLocalgis"></div>
                            <div id="OpenLayers.Control.ZoomToMunicipioLocalgis_16" style="position: absolute; z-index: 1006;" class="olControlZoomToMunicipioLocalgis"></div>
                            <div id="OpenLayers.Control.ZoomToMaxExtent_17" style="position: absolute; z-index: 1007;" class="olControlZoomToMaxExtent"></div>
                            <div id="OpenLayers.Control.PrintLocalgis_21" style="position: absolute; z-index: 1011;" class="olControlPrintLocalgis"></div>
                            <div id="OpenLayers.Control.SearchLocalgis_22" style="position: absolute; z-index: 1012;" class="olControlSearchLocalgis"></div>
                            <div id="OpenLayers.Control.SaveLocalgis_23" style="position: absolute; z-index: 1013;" class="olControlSaveLocalgis"></div>
                            <div id="OpenLayers.Control.ScaleBarLocalgis_53" style="position: absolute; bottom: 40px; left: 5px; z-index: 1014;" class="sbContainer">
                                    <div style="height: 0px;" title="Escala 1:3,200">
                                        <div style="position: absolute;" class="sbGraphicsContainer">
                                            <div style="overflow: hidden; position: absolute; width: 3px; left: 1px;" class="scaleBarLeft"> </div>
                                            <div style="overflow: hidden; position: absolute; width: 35px; left: 4px;" class="olScaleBarBar"> </div>
                                            <div style="overflow: hidden; position: absolute; width: 35px; left: 40px;" class="olScaleBarBarAlt"> </div>
                                            <div style="overflow: hidden; position: absolute; width: 35px; left: 75px;" class="olScaleBarBar"> </div>
                                            <div style="overflow: hidden; position: absolute; width: 35px; left: 110px;" class="olScaleBarBarAlt"> </div>
                                            <div style="overflow: hidden; position: absolute; width: 35px; left: 147px;" class="olScaleBarBar"> </div>
                                            <div style="overflow: hidden; position: absolute; width: 3px; left: 181px;" class="scaleBarRight"> </div>
                                        </div>
                                        <div class="labelSpan" style="position: absolute;">
                                            <div style="overflow: hidden; position: absolute; text-align: center; width: 30px; left: 181px;" class="olScaleBarLabelBox"> m</div>
                                        </div>
                                        <div style="position: absolute;" class="sbNumbersContainer">
                                            <div class="olScaleBarNumbersBox" style="overflow: hidden; position: absolute; text-align: center; left: 4px;">0</div>
                                            <div class="olScaleBarNumbersBox" style="overflow: hidden; position: absolute; text-align: center; left: 75px;">80</div>
                                            <div class="olScaleBarNumbersBox" style="overflow: hidden; position: absolute; text-align: center; left: 146px;">160</div>
                                        </div>
                                        <div><span>Escala 1: 3200</span></div>
                                    </div>
                                </div>
                                <div id="OpenLayers.Control.MousePosition_55" style="position: absolute; z-index: 1015;" class="olControlMousePosition">390802.00394, 4717354.52270</div>
                                <div id="OpenLayers.Control.OverviewMapLocalgis_361" style="position: absolute; z-index: 1016;" class="olControlOverviewMapLocalgisContainer">
                                    <div class="olControlOverviewMapLocalgisElement" style="">
                                        <div style="overflow: hidden; width: 180px; height: 133px; position: relative;" id="overviewMap363">
                                            <div style="overflow: hidden; position: absolute; z-index: 1000; background-image: url(staticImg/blank.gif); top: 41px; left: 78px; height: 16px; width: 22px;" class="olControlOverviewMapLocalgisExtentRectangle"></div>
                                            <div id="overviewMap363_OpenLayers_ViewPort" style="overflow: hidden; position: relative; width: 100%; height: 100%;" class="olMapViewport">
                                                <div id="overviewMap363_OpenLayers_Container" style="position: absolute; z-index: 749; left: 0px; top: 0px;">
                                                    <div id="OpenLayers.Layer.WMSLocalgis_356" style="position: absolute; width: 100%; height: 100%; z-index: 100;">
                                                        <div style="overflow: hidden; position: absolute; left: -45px; top: -33px; width: 270px; height: 199px;">
                                                            <img id="OpenLayersDiv375" style="width: 270px; height: 199px; position: relative;" class="olTileImage" src="staticImg/overviewMap.png"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="olControlOverviewMapLocalgisMaximizeButton" style="position: absolute; width: 18px; height: 18px; display: none;" class="olControlOverviewMapLocalgisMaximizeButton">
                                        <img id="olControlOverviewMapLocalgisMaximizeButton_innerImage" style="position: relative; width: 18px; height: 18px;" src="staticImg/overview-map-maximize-grey.gif"/>
                                    </div>
                                    <div id="OpenLayers_Control_minimizeDiv" style="position: absolute; width: 18px; height: 18px;" class="olControlOverviewMapLocalgisMinimizeButton">
                                        <img id="OpenLayers_Control_minimizeDiv_innerImage" style="position: relative; width: 18px; height: 18px;" src="staticImg/overview-map-minimize-grey.gif"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="bottom" class="bottom">
                <img src="staticImg/btm_left.gif" alt="Esquina inferior izquierda" class="esquina_inf_izq"/>
                <img src="staticImg/btm_right.gif" alt="Esquina inferior derecha" class="esquina_inf_der"/>
            </div>
        </div>
    </body>
 </html:html>
