/**
 * JRBienesDataSource.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario;

import java.util.Collection;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.global.ServletConstants;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.CreditoDerechoBean;
import com.geopista.protocol.inventario.DerechoRealBean;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.MuebleBean;
import com.geopista.protocol.inventario.SemovienteBean;
import com.geopista.protocol.inventario.UsoFuncional;
import com.geopista.protocol.inventario.ValorMobiliarioBean;
import com.geopista.protocol.inventario.VehiculoBean;
import com.geopista.protocol.inventario.ViaBean;
/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 23-oct-2006
 * Time: 14:27:25
 * To change this template use File | Settings | File Templates.
 */
public class JRBienesDataSource {
    public static Object getInmuebleValue(JRField field, InmuebleBean inmueble, String locale) throws Exception{
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("bienes_inventario.num_orden"))
            value= inmueble.getNumeroOrden()!=null?inmueble.getNumeroOrden():"";
        else if (field.getName().equalsIgnoreCase("bienes_inventario.num_propiedad"))
            value= inmueble.getNumeroPropiedad()!=null?inmueble.getNumeroPropiedad():"";
        else if (field.getName().equalsIgnoreCase("bienes_inventario.ref_catastral"))
            value= inmueble.getRefCatastral()!=null?inmueble.getRefCatastral():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.adquisicion"))
            try{value= inmueble.getAdquisicion()!=null?Estructuras.getListaFormaAdquisicion().getDomainNode(inmueble.getAdquisicion()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("inmuebles.calificacion"))
            value= inmueble.getCalificacion()!=null?inmueble.getCalificacion():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.carpinteria"))
            try{value= inmueble.getCarpinteria()!=null?Estructuras.getListaTipoCarpinteria().getDomainNode(inmueble.getCarpinteria()).getTerm(locale):inmueble.getCarpinteriaDesc()!=null?inmueble.getCarpinteriaDesc():"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("inmuebles.cubierta"))
            try{value= inmueble.getCubierta()!=null?Estructuras.getListaTipoCubierta().getDomainNode(inmueble.getCubierta()).getTerm(locale):inmueble.getCubiertaDesc()!=null?inmueble.getCubiertaDesc():"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("inmuebles.estadoconservacion_desc"))
            value= inmueble.getEstadoConservacionDesc()!=null?inmueble.getEstadoConservacionDesc():inmueble.getEstadoConservacion()!=null?Estructuras.getListaEstadoConservacion().getDomainNode(inmueble.getEstadoConservacion()).getTerm(locale):"";
        else if (field.getName().equalsIgnoreCase("inmuebles.tipoconstruccion_desc"))
            value= inmueble.getTipoConstruccionDesc()!=null?inmueble.getTipoConstruccionDesc():inmueble.getTipoConstruccion()!=null?Estructuras.getListaTipoConstruccion().getDomainNode(inmueble.getTipoConstruccion()).getTerm(locale):"";
        else if (field.getName().equalsIgnoreCase("inmuebles.carpinteria_desc"))
            value= inmueble.getCarpinteriaDesc()!=null?inmueble.getCarpinteriaDesc():inmueble.getCarpinteria()!=null?Estructuras.getListaTipoCarpinteria().getDomainNode(inmueble.getCarpinteria()).getTerm(locale):"";
        else if (field.getName().equalsIgnoreCase("inmuebles.cubierta_desc"))
            value= inmueble.getCubiertaDesc()!=null?inmueble.getCubiertaDesc():inmueble.getCubierta()!=null?Estructuras.getListaTipoCubierta().getDomainNode(inmueble.getCubierta()).getTerm(locale):"";
        else if (field.getName().equalsIgnoreCase("inmuebles.fachada_desc"))
            value= inmueble.getFachadaDesc()!=null?inmueble.getFachadaDesc():inmueble.getFachada()!=null?Estructuras.getListaTipoFachada().getDomainNode(inmueble.getFachada()).getTerm(locale):"";
        else if (field.getName().equalsIgnoreCase("inmuebles.derechospersonales"))
            value= inmueble.getDerechosPersonales()!=null?inmueble.getDerechosPersonales():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.derechosrealescontra"))
            value= inmueble.getDerechosRealesContra()!=null?inmueble.getDerechosRealesContra():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.derechosrealesfavor"))
            value= inmueble.getDerechosRealesFavor()!=null?inmueble.getDerechosRealesFavor():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.destino"))
            value= inmueble.getDestino()!=null?inmueble.getDestino():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.direccion"))
            value= inmueble.getDireccion()!=null?inmueble.getDireccion():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.edificabilidad"))
            value= (inmueble.getEdificabilidad()!=null && inmueble.getEdificabilidad()!=-1)?new Double(inmueble.getEdificabilidad()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.estadoconservacion"))
            try{value= inmueble.getEstadoConservacion()!=null?Estructuras.getListaEstadoConservacion().getDomainNode(inmueble.getEstadoConservacion()).getTerm(locale):inmueble.getEstadoConservacionDesc()!=null?inmueble.getCarpinteriaDesc():"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("inmuebles.fachada"))
            try{value= inmueble.getFachada()!=null?Estructuras.getListaTipoFachada().getDomainNode(inmueble.getFachada()).getTerm(locale):inmueble.getFachadaDesc()!=null?inmueble.getFachadaDesc():"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("inmuebles.fecha_adquisicion"))
            try{value= Constantes.df.format(inmueble.getFechaAdquisicion());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("inmuebles.fecha_adquisicion_suelo"))
            try{value= Constantes.df.format(inmueble.getFechaAdquisicionSuelo());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("inmuebles.fechaobra"))
            try{value= Constantes.df.format(inmueble.getFechaObra());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("inmuebles.frutos"))
            value= inmueble.getFrutos()!=null?inmueble.getFrutos():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.importe_frutos"))
            value= inmueble.getImporteFrutos()!=-1?new Double(inmueble.getImporteFrutos()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.lindero_norte"))
            value= inmueble.getLinderoNorte()!=null?inmueble.getLinderoNorte():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.lindero_sur"))
            value= inmueble.getLinderoSur()!=null?inmueble.getLinderoSur():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.lindero_este"))
            value= inmueble.getLinderoEste()!=null?inmueble.getLinderoEste():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.lindero_oeste"))
            value= inmueble.getLinderoOeste()!=null?inmueble.getLinderoOeste():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.num_plantas"))
            value= inmueble.getNumPlantas()!=null?inmueble.getNumPlantas():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.propiedad"))
            try{value= inmueble.getPropiedad()!=null?Estructuras.getListaPropiedadPatrimonial().getDomainNode(inmueble.getPropiedad()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("inmuebles.registro_finca"))
            value= inmueble.getRegistroFinca()!=null?inmueble.getRegistroFinca():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.registro_folio"))
            value= inmueble.getRegistroFolio()!=null?inmueble.getRegistroFolio():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.registro_inscripcion"))
            value= inmueble.getRegistroInscripcion()!=null?inmueble.getRegistroInscripcion():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.registro_libro"))
            value= inmueble.getRegistroLibro()!=null?inmueble.getRegistroLibro():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.registro_notario"))
            value= inmueble.getRegistroNotario()!=null?inmueble.getRegistroNotario():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.registro_propiedad"))
            value= inmueble.getRegistroPropiedad()!=null?inmueble.getRegistroPropiedad():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.registro_protocolo"))
            value= inmueble.getRegistroProtocolo()!=null?inmueble.getRegistroProtocolo():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.registro_tomo"))
            value= inmueble.getRegistroTomo()!=null?inmueble.getRegistroTomo():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.superficie_catastral_suelo"))
            value= inmueble.getSuperficieCatastralSuelo()!=-1?new Double(inmueble.getSuperficieCatastralSuelo()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.superficie_real_construccion"))
            value= inmueble.getSuperficieRealConstruccion()!=-1?new Double(inmueble.getSuperficieRealConstruccion()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.superficie_real_suelo"))
            value= inmueble.getSuperficieRealSuelo()!=-1?new Double(inmueble.getSuperficieRealSuelo()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.superficie_catastral_construccion"))
            value= inmueble.getSuperficieCatastralConstruccion()!=-1?new Double(inmueble.getSuperficieCatastralConstruccion()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.superficie_construida_construccion"))
            value= inmueble.getSuperficieConstruidaConstruccion()!=-1?new Double(inmueble.getSuperficieConstruidaConstruccion()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.superficie_enplanta_construccion"))
            value= inmueble.getSuperficieEnPlantaConstruccion()!=-1?new Double(inmueble.getSuperficieEnPlantaConstruccion()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.superficie_ocupada_construccion"))
            value= inmueble.getSuperficieOcupadaConstruccion()!=-1?new Double(inmueble.getSuperficieOcupadaConstruccion()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.superficie_registral_construccion"))
            value= inmueble.getSuperficieRegistralConstruccion()!=-1?new Double(inmueble.getSuperficieRegistralConstruccion()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.superficie_registral_suelo"))
            value= inmueble.getSuperficieRegistralSuelo()!=-1?new Double(inmueble.getSuperficieRegistralSuelo()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.tipoconstruccion"))
            try{value= inmueble.getTipoConstruccion()!=null?Estructuras.getListaTipoConstruccion().getDomainNode(inmueble.getTipoConstruccion()).getTerm(locale):inmueble.getTipoConstruccionDesc()!=null?inmueble.getTipoConstruccionDesc():"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("inmuebles.valor_actual_construccion"))
            value= inmueble.getValorActualConstruccion()!=-1?new Double(inmueble.getValorActualConstruccion()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.valor_actual_inmueble"))
            value= inmueble.getValorActualInmueble()!=-1?new Double(inmueble.getValorActualInmueble()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.valor_actual_suelo"))
            value= inmueble.getValorActualSuelo()!=-1?new Double(inmueble.getValorActualSuelo()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.valor_adquisicion_construccion"))
            value= inmueble.getValorAdquisicionConstruccion()!=-1?new Double(inmueble.getValorAdquisicionConstruccion()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.valor_adquisicion_inmueble"))
            value= inmueble.getValorAdquisicionInmueble()!=-1?new Double(inmueble.getValorAdquisicionInmueble()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.valor_adquisicion_suelo"))
            value= inmueble.getValorAdquisicionSuelo()!=-1?new Double(inmueble.getValorAdquisicionSuelo()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.valor_catastral_construccion"))
            value= inmueble.getValorCatastralConstruccion()!=-1?new Double(inmueble.getValorCatastralConstruccion()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.valor_catastral_suelo"))
            value= inmueble.getValorCatastralSuelo()!=-1?new Double(inmueble.getValorCatastralSuelo()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.valor_derechos_contra"))
            value= inmueble.getValorDerechosContra()!=-1?new Double(inmueble.getValorDerechosContra()).toString():"";
        else if (field.getName().equalsIgnoreCase("inmuebles.valor_derechos_favor"))
            value= inmueble.getValorDerechosFavor()!=-1?new Double(inmueble.getValorDerechosFavor()).toString():"";
        /** inmuebles rusticos */
        else if (field.getName().equalsIgnoreCase("inmuebles_rusticos.paraje"))
            value= inmueble.getInmuebleRustico()!=null?(inmueble.getInmuebleRustico().getParaje()!=null?inmueble.getInmuebleRustico().getParaje():""):"";
        else if (field.getName().equalsIgnoreCase("inmuebles_rusticos.poligono"))
            value= inmueble.getInmuebleRustico()!=null?(inmueble.getInmuebleRustico().getPoligono()!=null?inmueble.getInmuebleRustico().getPoligono():""):"";
        else if (field.getName().equalsIgnoreCase("inmuebles_rusticos.aprovechamiento"))
            try{value= inmueble.getInmuebleRustico()!=null?(inmueble.getInmuebleRustico().getAprovechamiento()!=null?Estructuras.getListaAprovechamiento().getDomainNode(inmueble.getInmuebleRustico().getAprovechamiento()).getTerm(locale):""):"";}catch(Exception e){value= "";}
        /** inmuebles urbanos */
        else if (field.getName().equalsIgnoreCase("inmuebles_urbanos.manzana"))
            value= inmueble.getInmuebleUrbano()!=null?inmueble.getInmuebleUrbano().getManzana()!=null?inmueble.getInmuebleUrbano().getManzana():"":"";
        

        if (value == null) value= getCContableValue(field, (BienBean)inmueble);
        if (value == null) value= getCAmortizacionValue(field, (BienBean)inmueble, locale);
        if (value == null) value= getSeguroValue(field, (BienBean)inmueble);
        if (value == null) value= getObservacionValue(field, (BienBean)inmueble);
        if (value == null) value= getMejoraValue(field, (BienBean)inmueble);
        if (value == null) value= getRefCatastralValue(field, inmueble);
        if (value == null) value= getUsoFuncionalValue(field, inmueble, locale);
        if (value == null) value= getDocumentoValue(field, (BienBean)inmueble);

        return value;

    }

    public static Object getMuebleValue(JRField field, MuebleBean mueble, String locale) throws Exception{
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("muebles.adquisicion"))
            try{value= mueble.getAdquisicion()!=null?Estructuras.getListaFormaAdquisicion().getDomainNode(mueble.getAdquisicion()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("muebles.caracteristicas"))
            value= mueble.getCaracteristicas()!=null?mueble.getCaracteristicas():"";
        else if (field.getName().equalsIgnoreCase("muebles.coste_adquisicion"))
            value= mueble.getCosteAdquisicion()!=-1?new Double(mueble.getCosteAdquisicion()).toString():"";
        else if (field.getName().equalsIgnoreCase("muebles.destino"))
            value= mueble.getDestino()!=null?mueble.getDestino():"";
        else if (field.getName().equalsIgnoreCase("muebles.estadoconservacion"))
            value= mueble.getEstadoConservacion()!=null?Estructuras.getListaEstadoConservacion().getDomainNode(mueble.getEstadoConservacion()).getTerm(locale):"";
        else if (field.getName().equalsIgnoreCase("muebles.fecha_adquisicion"))
            try{value= Constantes.df.format(mueble.getFechaAdquisicion());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("muebles.frutos"))
            value= mueble.getFrutos()!=null?mueble.getFrutos():"";
        else if (field.getName().equalsIgnoreCase("muebles.importe_frutos"))
            value= mueble.getImporteFrutos()!=-1?new Double(mueble.getImporteFrutos()).toString():"";
        else if (field.getName().equalsIgnoreCase("muebles.propiedad"))
            try{value= mueble.getPropiedad()!=null?Estructuras.getListaPropiedadPatrimonial().getDomainNode(mueble.getPropiedad()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("muebles.ubicacion"))
            value= mueble.getUbicacion()!=null?mueble.getUbicacion():"";
        else if (field.getName().equalsIgnoreCase("muebles.valor_actual"))
            value= mueble.getValorActual()!=-1?new Double(mueble.getValorActual()).toString():"";
        else if (field.getName().equalsIgnoreCase("muebles.fecha_fin_garantia"))
            try{value= Constantes.df.format(mueble.getFechaFinGarantia());}catch(Exception e){value= "";}
        /** muebles */
        else if (field.getName().equalsIgnoreCase("muebles.marca"))
            value= mueble.getMarca()!=null?mueble.getMarca():"";
        else if (field.getName().equalsIgnoreCase("muebles.num_serie"))
            value= mueble.getNumSerie()!=null?mueble.getNumSerie():"";
        else if (field.getName().equalsIgnoreCase("muebles.modelo"))
            value= mueble.getModelo()!=null?mueble.getModelo():"";
        /** muebles historico artisticos */
        else if (field.getName().equalsIgnoreCase("muebles.autor"))
            value= mueble.getAutor()!=null?mueble.getAutor():"";
        else if (field.getName().equalsIgnoreCase("muebles.direccion"))
            value= mueble.getDireccion()!=null?mueble.getDireccion():"";
        else if (field.getName().equalsIgnoreCase("muebles.material"))
            value= mueble.getMaterial()!=null?mueble.getMaterial():"";

        if (value==null) value= getCAmortizacionValue(field, (BienBean)mueble, locale);
        if (value==null) value= getSeguroValue(field, (BienBean)mueble);
        if (value==null) value= getObservacionValue(field, (BienBean)mueble);
        if (value==null) value= getDocumentoValue(field, (BienBean)mueble);

        return value;
    }

    public static Object getCreditoDerechoValue(JRField field, CreditoDerechoBean credito, String locale) throws Exception{
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("credito_derecho.adquisicion"))
            try{value= credito.getAdquisicion()!=null?Estructuras.getListaFormaAdquisicion().getDomainNode(credito.getAdquisicion()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("credito_derecho.credito_derecho"))
            try{value= credito.getConcepto()!=null?Estructuras.getListaConceptosCreditosDerechos().getDomainNode(credito.getConcepto()).getTerm(locale):credito.getConceptoDesc()!=null?credito.getConceptoDesc():"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("credito_derecho.concepto_desc"))
            value= credito.getConceptoDesc()!=null?credito.getConceptoDesc():credito.getConcepto()!=null?Estructuras.getListaConceptosCreditosDerechos().getDomainNode(credito.getConcepto()).getTerm(locale):"";
        else if (field.getName().equalsIgnoreCase("credito_derecho.descripcion"))
            value= credito.getCaracteristicas()!=null?credito.getCaracteristicas():"";
        else if (field.getName().equalsIgnoreCase("credito_derecho.destino"))
            value= credito.getDestino()!=null?credito.getDestino():"";
        else if (field.getName().equalsIgnoreCase("credito_derecho.deudor"))
            value= credito.getDeudor()!=null?credito.getDeudor():"";
        else if (field.getName().equalsIgnoreCase("credito_derecho.importe"))
            value= credito.getImporte()!=-1?new Double(credito.getImporte()).toString():"";
        else if (field.getName().equalsIgnoreCase("credito_derecho.fecha_adquisicion"))
            try{value= Constantes.df.format(credito.getFechaAdquisicion());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("credito_derecho.fecha_vencimiento"))
            try{value= Constantes.df.format(credito.getFechaVencimiento());}catch(Exception e){value= "";}

        if (value == null) value= getCContableValue(field, (BienBean)credito);
        if (value == null) value= getObservacionValue(field, (BienBean)credito);
        if (value == null) value= getDocumentoValue(field, (BienBean)credito);

        return value;
    }


    public static Object getDerechoRealValue(JRField field, DerechoRealBean derecho, String locale) throws Exception{
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("derechos_reales.adquisicion"))
            try{value= derecho.getAdquisicion()!=null?Estructuras.getListaFormaAdquisicion().getDomainNode(derecho.getAdquisicion()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("derechos_reales.bien"))
            value= derecho.getBien()!=null?derecho.getBien():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.coste"))
            value= derecho.getCosteAdquisicion()!=-1?new Double(derecho.getCosteAdquisicion()).toString():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.destino"))
            value= derecho.getDestino()!=null?derecho.getDestino():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.fecha_adquisicion"))
            try{value= Constantes.df.format(derecho.getFechaAdquisicion());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("derechos_reales.frutos"))
            value= derecho.getFrutos()!=null?derecho.getFrutos():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.importe_frutos"))
            value= derecho.getImporteFrutos()!=-1?new Double(derecho.getImporteFrutos()).toString():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.registro_finca"))
            value= derecho.getRegistroFinca()!=null?derecho.getRegistroFinca():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.registro_folio"))
            value= derecho.getRegistroFolio()!=null?derecho.getRegistroFolio():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.registro_inscripcion"))
            value= derecho.getRegistroInscripcion()!=null?derecho.getRegistroInscripcion():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.registro_libro"))
            value= derecho.getRegistroLibro()!=null?derecho.getRegistroLibro():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.registro_notario"))
            value= derecho.getRegistroNotario()!=null?derecho.getRegistroNotario():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.registro_propiedad"))
            value= derecho.getRegistroPropiedad()!=null?derecho.getRegistroPropiedad():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.registro_protocolo"))
            value= derecho.getRegistroProtocolo()!=null?derecho.getRegistroProtocolo():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.registro_tomo"))
            value= derecho.getRegistroTomo()!=null?derecho.getRegistroTomo():"";
        else if (field.getName().equalsIgnoreCase("derechos_reales.valor"))
            value= derecho.getValorActual()!=-1?new Double(derecho.getValorActual()).toString():"";

        if (value == null) value= getCContableValue(field, (BienBean)derecho);
        if (value == null) value= getObservacionValue(field, (BienBean)derecho);
        if (value == null) value= getDocumentoValue(field, (BienBean)derecho);

        return value;
    }


    public static Object getValorMobiliarioValue(JRField field, ValorMobiliarioBean valor, String locale) throws Exception{
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("valor_mobiliario.adquisicion"))
            try{value= valor.getAdquisicion()!=null?Estructuras.getListaFormaAdquisicion().getDomainNode(valor.getAdquisicion()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.fecha_adquisicion"))
            try{value= Constantes.df.format(valor.getFechaAdquisicion());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.depositado_en"))
            value= valor.getDepositadoEn()!=null?valor.getDepositadoEn():"";
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.emitido_por"))
            value= valor.getEmitidoPor()!=null?valor.getEmitidoPor():"";
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.numero"))
            value= valor.getNumero()!=null?valor.getNumero():"";
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.serie"))
            value= valor.getSerie()!=null?valor.getSerie():"";
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.num_titulos"))
            value= valor.getNumTitulos()!=-1?new Integer(valor.getNumTitulos()).toString():"";
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.destino"))
            value= valor.getDestino()!=null?valor.getDestino():"";
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.clase"))
            try{value= valor.getClase()!=null?Estructuras.getListaClasesValorMobiliario().getDomainNode(valor.getClase()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.coste_adquisicion"))
            value= valor.getCosteAdquisicion()!=-1?new Double(valor.getCosteAdquisicion()).toString():"";
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.valor_actual"))
            value= valor.getValorActual()!=-1?new Double(valor.getValorActual()).toString():"";
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.precio"))
            value= valor.getPrecio()!=-1?new Double(valor.getPrecio()).toString():"";
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.capital"))
            value= valor.getCapital()!=-1?new Double(valor.getCapital()).toString():"";
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.importe_frutos"))
            value= valor.getImporteFrutos()!=-1?new Double(valor.getImporteFrutos()).toString():"";
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.frutos"))
            value= valor.getFrutos()!=null?valor.getFrutos():"";
        else if (field.getName().equalsIgnoreCase("valor_mobiliario.fecha_acuerdo"))
            try{value= Constantes.df.format(valor.getFechaAcuerdo());}catch(Exception e){value= "";}

        if (value == null) value= getCContableValue(field, (BienBean)valor);
        if (value == null) value= getSeguroValue(field, (BienBean)valor);
        if (value == null) value= getObservacionValue(field, (BienBean)valor);
        if (value == null) value= getDocumentoValue(field, (BienBean)valor);

        return value;
    }

    public static Object getSemovienteValue(JRField field, SemovienteBean semoviente, String locale) throws Exception{
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("semoviente.adquisicion"))
            try{value= semoviente.getAdquisicion()!=null?Estructuras.getListaFormaAdquisicion().getDomainNode(semoviente.getAdquisicion()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("semoviente.conservacion"))
            try{value= semoviente.getConservacion()!=null?Estructuras.getListaEstadoConservacion().getDomainNode(semoviente.getConservacion()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("semoviente.coste_adquisicion"))
            value= semoviente.getCosteAdquisicion()!=-1?new Double(semoviente.getCosteAdquisicion()).toString():"";
        else if (field.getName().equalsIgnoreCase("semoviente.descripcion"))
            value= semoviente.getCaracteristicas()!=null?semoviente.getCaracteristicas():"";
        else if (field.getName().equalsIgnoreCase("semoviente.destino"))
            value= semoviente.getDestino()!=null?semoviente.getDestino():"";
        else if (field.getName().equalsIgnoreCase("semoviente.especie"))
            value= semoviente.getEspecie()!=null?semoviente.getEspecie():"";
        else if (field.getName().equalsIgnoreCase("semoviente.fecha_adquisicion"))
            try{value= Constantes.df.format(semoviente.getFechaAdquisicion());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("semoviente.fecha_nacimiento"))
            try{value= Constantes.df.format(semoviente.getFechaNacimiento());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("semoviente.frutos"))
            value= semoviente.getFrutos()!=null?semoviente.getFrutos():"";
        else if (field.getName().equalsIgnoreCase("semoviente.identificacion"))
            value= semoviente.getIdentificacion()!=null?semoviente.getIdentificacion():"";
        else if (field.getName().equalsIgnoreCase("semoviente.importe_frutos"))
            value= semoviente.getImporteFrutos()!=-1?new Double(semoviente.getImporteFrutos()).toString():"";
        else if (field.getName().equalsIgnoreCase("semoviente.propiedad"))
            try{value= semoviente.getPropiedad()!=null?Estructuras.getListaPropiedadPatrimonial().getDomainNode(semoviente.getPropiedad()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("semoviente.raza"))
            try{value= semoviente.getRaza()!=null?Estructuras.getListaRazaSemoviente().getDomainNode(semoviente.getRaza()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("semoviente.valor_actual"))
            value= semoviente.getValorActual()!=-1?new Double(semoviente.getValorActual()).toString():"";

        if (value == null) value= getCContableValue(field, (BienBean)semoviente);
        if (value == null) value= getSeguroValue(field, (BienBean)semoviente);
        if (value == null) value= getObservacionValue(field, (BienBean)semoviente);
        if (value == null) value= getDocumentoValue(field, (BienBean)semoviente);

        return value;
    }

    public static Object getVehiculoValue(JRField field, VehiculoBean vehiculo, String locale) throws Exception {
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("vehiculo.adquisicion"))
            try{value= vehiculo.getAdquisicion()!=null?Estructuras.getListaFormaAdquisicion().getDomainNode(vehiculo.getAdquisicion()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("vehiculo.fecha_adquisicion"))
            try{value= Constantes.df.format(vehiculo.getFechaAdquisicion());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("vehiculo.matricula_vieja"))
            value= vehiculo.getMatriculaVieja()!=null?vehiculo.getMatriculaVieja():"";
        else if (field.getName().equalsIgnoreCase("vehiculo.matricula_nueva"))
            value= vehiculo.getMatriculaNueva()!=null?vehiculo.getMatriculaNueva():"";
        else if (field.getName().equalsIgnoreCase("vehiculo.num_bastidor"))
            value= vehiculo.getNumBastidor()!=null?vehiculo.getNumBastidor():"";
        else if (field.getName().equalsIgnoreCase("vehiculo.marca"))
            value= vehiculo.getMarca()!=null?vehiculo.getMarca():"";
        else if (field.getName().equalsIgnoreCase("vehiculo.motor"))
            value= vehiculo.getMotor()!=null?vehiculo.getMotor():"";
        else if (field.getName().equalsIgnoreCase("vehiculo.fuerza"))
            value= vehiculo.getFuerza()!=null?vehiculo.getFuerza():"";
        else if (field.getName().equalsIgnoreCase("vehiculo.servicio"))
            value= vehiculo.getServicio()!=null?vehiculo.getServicio():"";
        else if (field.getName().equalsIgnoreCase("vehiculo.destino"))
            value= vehiculo.getDestino()!=null?vehiculo.getDestino():"";
        else if (field.getName().equalsIgnoreCase("vehiculo.tipo"))
            try{value= vehiculo.getTipoVehiculo()!=null?Estructuras.getListaTiposVehiculo().getDomainNode(vehiculo.getTipoVehiculo()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("vehiculo.estado_conservacion"))
            try{value= vehiculo.getEstadoConservacion()!=null?Estructuras.getListaEstadoConservacion().getDomainNode(vehiculo.getEstadoConservacion()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("vehiculo.traccion"))
            try{value= vehiculo.getTraccion()!=null?Estructuras.getListaTraccion().getDomainNode(vehiculo.getTraccion()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("vehiculo.propiedad"))
            try{value= vehiculo.getPropiedad()!=null?Estructuras.getListaPropiedadPatrimonial().getDomainNode(vehiculo.getPropiedad()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("vehiculo.frutos"))
            value= vehiculo.getFrutos()!=null?vehiculo.getFrutos():"";
        else if (field.getName().equalsIgnoreCase("vehiculo.importe_frutos"))
            value= vehiculo.getImporteFrutos()!=-1?new Double(vehiculo.getImporteFrutos()).toString():"";
        else if (field.getName().equalsIgnoreCase("vehiculo.coste_adquisicion"))
            value= vehiculo.getCosteAdquisicion()!=-1?new Double(vehiculo.getCosteAdquisicion()).toString():"";
        else if (field.getName().equalsIgnoreCase("vehiculo.valor_actual"))
            value= vehiculo.getValorActual()!=-1?new Double(vehiculo.getValorActual()).toString():"";

        if (value == null) value= getCContableValue(field, (BienBean)vehiculo);
        if (value == null) value= getCAmortizacionValue(field, (BienBean)vehiculo, locale);
        if (value == null) value= getSeguroValue(field, (BienBean)vehiculo);
        if (value == null) value= getObservacionValue(field, (BienBean)vehiculo);
        if (value == null) value= getDocumentoValue(field, (BienBean)vehiculo);

        return value;
    }


    public static Object getViaValue(JRField field, ViaBean via, String locale) throws Exception{
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("vias_inventario.adquisicion"))
            try{value= via.getAdquisicion()!=null?Estructuras.getListaFormaAdquisicion().getDomainNode(via.getAdquisicion()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("vias_inventario.fecha_adquisicion"))
            try{value= Constantes.df.format(via.getFechaAdquisicion());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("vias_inventario.categoria"))
            value= via.getCategoria()!=null?via.getCategoria():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.codigo"))
            try{value= via.getCodigo()!=null?Estructuras.getListaTiposViaINE().getDomainNode(via.getCodigo()).getTerm(locale):"";}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("vias_inventario.nombre"))
            value= via.getNombreVia()!=null?via.getNombreVia():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.inicio"))
            value= via.getInicioVia()!=null?via.getInicioVia():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.fin"))
            value= via.getFinVia()!=null?via.getFinVia():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.destino"))
            value= via.getDestino()!=null?via.getDestino():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.num_apliques"))
            value= via.getNumApliques()!=-1?""+via.getNumApliques():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.num_bancos"))
            value= via.getNumBancos()!=-1?""+via.getNumBancos():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.num_papeleras"))
            value= via.getNumPapeleras()!=-1?""+via.getNumPapeleras():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.metros_pavimentados"))
            value= via.getMetrosPavimentados()!=-1?""+via.getMetrosPavimentados():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.metros_no_pavimentados"))
            value= via.getMetrosNoPavimentados()!=-1?""+via.getMetrosNoPavimentados():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.zonas_verdes"))
            value= via.getZonasVerdes()!=-1?""+via.getZonasVerdes():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.longitud"))
            value= via.getLongitud()!=-1?""+via.getLongitud():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.ancho"))
            value= via.getAncho()!=-1?""+via.getAncho():"";
        else if (field.getName().equalsIgnoreCase("vias_inventario.valor_actual"))
            value= via.getValorActual()!=-1?""+via.getValorActual():"";

        if (value == null) value= getCContableValue(field, (BienBean)via);
        if (value == null) value= getSeguroValue(field, (BienBean)via);
        if (value == null) value= getObservacionValue(field, (BienBean)via);
        if (value == null) value= getMejoraValue(field, (BienBean)via);
        if (value == null) value= getDocumentoValue(field, (BienBean)via);

        return value;
    }

    private static Object getCContableValue(JRField field, BienBean bien){
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("contable.descripcion"))
            value= bien.getCuentaContable()!=null?bien.getCuentaContable().getDescripcion()!=null?bien.getCuentaContable().getDescripcion():"":"";
        else if (field.getName().equalsIgnoreCase("contable.cuenta"))
            value= bien.getCuentaContable()!=null?bien.getCuentaContable().getCuenta()!=null?bien.getCuentaContable().getCuenta():"":"";

        return value;
    }

    private static Object getCAmortizacionValue(JRField field, BienBean bien, String locale){
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("amortizacion.cuenta"))
            value= bien.getCuentaAmortizacion()!=null?bien.getCuentaAmortizacion().getCuenta()!=null?bien.getCuentaAmortizacion().getCuenta():"":"";
        else if (field.getName().equalsIgnoreCase("amortizacion.descripcion"))
            value= bien.getCuentaAmortizacion()!=null?bien.getCuentaAmortizacion().getDescripcion()!=null?bien.getCuentaAmortizacion().getDescripcion():"":"";
        else if (field.getName().equalsIgnoreCase("amortizacion.anios"))
            value= bien.getCuentaAmortizacion()!=null?bien.getCuentaAmortizacion().getAnnos()!=-1?new Integer(bien.getCuentaAmortizacion().getAnnos()).toString():"":"";
        else if (field.getName().equalsIgnoreCase("amortizacion.porcentaje"))
            value= bien.getCuentaAmortizacion()!=null?bien.getCuentaAmortizacion().getPorcentaje()!=-1?new Double(bien.getCuentaAmortizacion().getPorcentaje()).toString():"":"";
        else if (field.getName().equalsIgnoreCase("amortizacion.total_amortizado"))
            value= bien.getCuentaAmortizacion()!=null?bien.getCuentaAmortizacion().getTotalAmortizado()!=-1?new Double(bien.getCuentaAmortizacion().getTotalAmortizado()).toString():"":"";
        else if (field.getName().equalsIgnoreCase("amortizacion.tipo_amortizacion"))
            try{value= bien.getCuentaAmortizacion()!=null?(bien.getCuentaAmortizacion().getTipoAmortizacion()!=null?Estructuras.getListaTipoAmortizacion().getDomainNode(bien.getCuentaAmortizacion().getTipoAmortizacion()).getTerm(locale):""):"";}catch(Exception e){value= "";}


        return value;
    }

    private static Object getSeguroValue(JRField field, BienBean bien){
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("seguros_inventario.descripcion"))
            value= bien.getSeguro()!=null?bien.getSeguro().getDescripcion()!=null?bien.getSeguro().getDescripcion():"":"";
        else if (field.getName().equalsIgnoreCase("seguros_inventario.fecha_inicio"))
            try{value= Constantes.df.format(bien.getSeguro().getFechaInicio());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("seguros_inventario.fecha_vencimiento"))
            try{value= Constantes.df.format(bien.getSeguro().getFechaVencimiento());}catch(Exception e){value= "";}
        else if (field.getName().equalsIgnoreCase("seguros_inventario.poliza"))
            value= bien.getSeguro()!=null?bien.getSeguro().getPoliza()!=-1?new Double(bien.getSeguro().getPoliza()).toString():"":"";
        else if (field.getName().equalsIgnoreCase("seguros_inventario.prima"))
            value= bien.getSeguro()!=null?bien.getSeguro().getPrima()!=-1?new Double(bien.getSeguro().getPrima()).toString():"":"";
        else if (field.getName().equalsIgnoreCase("compannia_seguros.nombre"))
            value= bien.getSeguro()!=null?bien.getSeguro().getCompannia()!=null?bien.getSeguro().getCompannia().getNombre()!=null?bien.getSeguro().getCompannia().getNombre():"":"":"";
        else if (field.getName().equalsIgnoreCase("compannia_seguros.descripcion"))
            value= bien.getSeguro()!=null?bien.getSeguro().getCompannia()!=null?bien.getSeguro().getCompannia().getDescripcion()!=null?bien.getSeguro().getCompannia().getDescripcion():"":"":"";

        return value;
    }

    private static Object getObservacionValue(JRField field, BienBean bien) throws Exception{
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("listaObservaciones")){
            value= new JRBeanCollectionDataSource(bien.getObservaciones());
        }
        return value;
    }


    private static Object getMejoraValue(JRField field, BienBean bien) throws Exception{
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("listaMejoras")){
            value= new JRBeanCollectionDataSource(bien.getMejoras());
        }
        return value;
    }


    private static Object getRefCatastralValue(JRField field, InmuebleBean inmueble) throws Exception{
        if (field == null) return null;
        Object value= null;

        if (field.getName().equalsIgnoreCase("listaReferenciasCatastrales")){
            value= new JRBeanCollectionDataSource(inmueble.getReferenciasCatastrales());
        }
        return value;
    }


    private static Object getUsoFuncionalValue(JRField field, InmuebleBean inmueble, String locale){
        if (field == null) return null;
        Object value= null;

        Object[] objs= inmueble.getUsosFuncionales()!=null?inmueble.getReferenciasCatastrales().toArray():null;
        if (field.getName().equalsIgnoreCase("usos_funcionales_inventario.uso")){
            if (objs != null){
                String aux= "";
                for (int i=0; i<objs.length; i++){
                    UsoFuncional uso= (UsoFuncional)objs[i];
                    try{aux+= uso.getUso()!=null?Estructuras.getListaUsosFuncionales().getDomainNode(uso.getUso()).getTerm(locale)+"\n":"\n";}catch(Exception e){aux+= "\n";}
                }
                value= aux;
            }
        }else if (field.getName().equalsIgnoreCase("usos_funcionales_inventario.superficie")){
            if (objs != null){
                String aux= "";
                for (int i=0; i<objs.length; i++){
                    UsoFuncional uso= (UsoFuncional)objs[i];
                    try{aux+= uso.getSuperficie()!=-1?new Double(uso.getSuperficie()).toString():"\n";}catch(Exception e){aux+= "\n";}
                }
                value= aux;
            }
        }else if (field.getName().equalsIgnoreCase("usos_funcionales_inventario.fecha")){
            if (objs != null){
                String aux= "";
                for (int i=0; i<objs.length; i++){
                    UsoFuncional uso= (UsoFuncional)objs[i];
                    try{aux+= Constantes.df.format(uso.getFecha())+"\n";}catch(Exception e){aux+= "\n";}
                }
                value= aux;
            }
        }

        return value;
    }

    private static Object getDocumentoValue(JRField field, BienBean bien) throws Exception{
        if (field == null) return null;
        Object value= null;
        DocumentClient documentClient= new DocumentClient(((AppContext) AppContext.getApplicationContext()).getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
        		ServletConstants.DOCUMENT_SERVLET_NAME);

        Collection c= documentClient.getAttachedDocuments(bien.getId());
        if (field.getName().equalsIgnoreCase("listaDocumentos")){
            value= new JRBeanCollectionDataSource(c);
        }
        return value;
    }



}
