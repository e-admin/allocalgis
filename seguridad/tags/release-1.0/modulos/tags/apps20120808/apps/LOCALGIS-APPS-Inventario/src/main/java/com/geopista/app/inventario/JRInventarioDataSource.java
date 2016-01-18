package com.geopista.app.inventario;

import com.geopista.app.AppContext;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.protocol.document.Documentable;
import com.geopista.protocol.inventario.*;
import com.geopista.ui.plugin.edit.DocumentManagerPlugin;



import org.apache.log4j.Logger;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Vector;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 18-oct-2006
 * Time: 11:22:20
 * To change this template use File | Settings | File Templates.
 */
public class JRInventarioDataSource implements net.sf.jasperreports.engine.JRDataSource {
	protected Logger logger = Logger.getLogger(JRInventarioDataSource.class);
    private String locale;
     private DocumentClient documentClient= null;
     private InventarioClient inventarioClient= null;

    private Object[] bienes;
    private int index= -1;

    public JRInventarioDataSource(Collection bienes,  String locale) {
        this.locale= locale;
        try{
        	Vector<Versionable> bienesSinVersion= new Vector<Versionable>();
        	
        	for (java.util.Iterator<Versionable> it=  bienes.iterator();it.hasNext();){
        		Versionable bien = (Versionable)it.next();
        		if (!bien.isVersionado()){
        			bienesSinVersion.add(bien);
        		}
        	}
        	this.bienes= bienesSinVersion!=null?bienesSinVersion.toArray():null;
//        	Arrays.sort(bienes.toArray(),new BienComparator());TODO :¿SE necesita ordenar?
        }catch(Exception ex){
        	this.bienes= bienes!=null?bienes.toArray():null;
        }
        documentClient= new DocumentClient(((AppContext) AppContext.getApplicationContext()).getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
                DocumentManagerPlugin.DOCUMENT_SERVLET_NAME);
        inventarioClient= new InventarioClient(((AppContext) AppContext.getApplicationContext()).getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
                Constantes.INVENTARIO_SERVLET_NAME);
 

    }
    
    public JRInventarioDataSource(Object bien,  String locale) {
        this.locale= locale;
        Vector bienes= new Vector();
  	    bienes.add(bien);
        this.bienes= bienes.toArray();
        documentClient= new DocumentClient(((AppContext) AppContext.getApplicationContext()).getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
                DocumentManagerPlugin.DOCUMENT_SERVLET_NAME);
        inventarioClient= new InventarioClient(((AppContext) AppContext.getApplicationContext()).getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
        		Constantes.INVENTARIO_SERVLET_NAME);
 
    }

   
    

    /**
     * Gets the current field index based on the position in
     * the mapped fields passed to the constructor. Then gets
     * the current object value held at the field index.
     *
     * @return the value of a field
     */
      public Object getFieldValue(JRField field) throws JRException{

        Object bien= bienes[index];
        if (field.getName().equals("ayuntamiento")){
            return "EXCMO. AYUNTAMIENTO DE "+Constantes.Provincia.toUpperCase();
        }
        if (field.getName().equalsIgnoreCase("municipio")){
            //return Constantes.IdMunicipio + " - " + Constantes.Municipio + " (" + Constantes.Provincia + ")";
        	return AppContext.getIdMunicipio() + " - " + Constantes.Municipio + " (" + Constantes.Provincia + ")";
        }
        if (field.getName().equalsIgnoreCase("titulo")){
            return "FICHA DE INVENTARIO DEL PATRIMONIO";
        }
        if (field.getName().startsWith("IMG_")){
            /** En la plantilla jrxml, el campo de una imagen tiene que tener el siguien formato:
             *  IMG_nombreFichero.ext (IMG_logo_geopista.gif) */
            String nombreFichero= field.getName().substring(4);
            return Constantes.PATH_PLANTILLAS_INVENTARIO + File.separator +"img" + File.separator + nombreFichero;
        }
        if (field.getName().startsWith("LOGO_")){
            /** En la plantilla jrxml, el campo de una imagen tiene que tener el siguien formato:
             *  IMG_nombreFichero.ext (IMG_logo_geopista.gif) */
            String nombreFichero= field.getName().substring(4);
            return Constantes.PATH_PLANTILLAS_INVENTARIO + File.separator +"img" + File.separator + AppContext.getIdMunicipio() + ".png";
        }
        if (bien instanceof BienBean) {
	        if (field.getName().equalsIgnoreCase("bienes_inventario.tipo"))
	            try{return ((BienBean)bien).getTipo()!=null?Estructuras.getListaSubtipoBienesPatrimonio().getDomainNode(((BienBean)bien).getTipo()).getTerm(locale).toUpperCase():"";}catch(Exception e){ return "";}
	        
	        if (field.getName().equalsIgnoreCase("bienes_inventario.uso"))
	            try{return ((BienBean)bien).getUso()!=null?Estructuras.getListaUsoJuridico().getDomainNode(((BienBean)bien).getUso()).getTerm(locale):"";}catch(Exception e){ return "";}
	        
	        if (field.getName().equalsIgnoreCase("bienes_inventario.adquisicion"))
	            try{return ((BienBean)bien).getAdquisicion()!=null?Estructuras.getListaFormaAdquisicion().getDomainNode(((BienBean)bien).getAdquisicion()).getTerm(locale):"";}catch(Exception e){return "";}
	        
	        if (field.getName().equalsIgnoreCase("cuentaContable")){
	        	return ((BienBean)bien).getCuentaContable();
	        }
	        if (field.getName().equalsIgnoreCase("bienes_inventario.numinventario")){
	        	return ((BienBean)bien).getNumInventario();
	        }
	        if (field.getName().equalsIgnoreCase("bienes_inventario.nombre")){
	        	return ((BienBean)bien).getNombre();
	        }
	        if (field.getName().equalsIgnoreCase("bienes_inventario.fecha_alta")){
	        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        	if(((BienBean)bien).getFechaAlta()!=null&&!((BienBean)bien).getFechaAlta().equals(""))
	        		return sdf.format(((BienBean)bien).getFechaAlta());
    	    	else
    	    		return null;
	        	
	        }
    	    if (field.getName().equalsIgnoreCase("bienes_inventario.fecha_ultima_modificacion"))
    	    {	
    	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	    	if(((BienBean)bien).getFechaUltimaModificacion()!=null&&
    	    			!((BienBean)bien).getFechaUltimaModificacion().equals(""))
    	    		return sdf.format(((BienBean)bien).getFechaUltimaModificacion());
    	    	else
    	    		return null;
    	    }
	        if (bien instanceof CreditoDerechoBean){
	        	CreditoDerechoBean bienCD=(CreditoDerechoBean)bien;
	        	if (field.getName().equalsIgnoreCase("bienes_inventario.concepto"))
	        		try{return bienCD.getConcepto()!=null?Estructuras.getListaConceptosCreditosDerechos().getDomainNode(bienCD.getConcepto()).getTerm(locale):"";}catch(Exception e){return "";}
	        }
	        if (bien instanceof VehiculoBean){
	        	VehiculoBean bienV=(VehiculoBean)bien;
	        	if (field.getName().equalsIgnoreCase("bienes_inventario.tipo_vehiculo"))
	        		try{return bienV.getTipoVehiculo()!=null?Estructuras.getListaTiposVehiculo().getDomainNode(bienV.getTipoVehiculo()).getTerm(locale):"";}catch(Exception e){return "";}
	        	if (field.getName().equalsIgnoreCase("bienes_inventario.conservacion"))
	            	try{return bienV.getEstadoConservacion()!=null?Estructuras.getListaEstadoConservacion().getDomainNode(bienV.getEstadoConservacion()).getTerm(locale):"";}catch(Exception e){return "";}
	          	if (field.getName().equalsIgnoreCase("bienes_inventario.traccion"))
	          		try{return bienV.getTraccion()!=null?Estructuras.getListaTraccion().getDomainNode(bienV.getTraccion()).getTerm(locale):"";}catch(Exception e){return "";}
	           	if (field.getName().equalsIgnoreCase("bienes_inventario.propiedad"))
	          		try{return bienV.getPropiedad()!=null?Estructuras.getListaPropiedadPatrimonial().getDomainNode(bienV.getPropiedad()).getTerm(locale):"";}catch(Exception e){return "";}
	        }
	        if (bien instanceof SemovienteBean){
	        	SemovienteBean bienS=(SemovienteBean)bien;
	        	if (field.getName().equalsIgnoreCase("bienes_inventario.raza"))
	        		try{return bienS.getRaza()!=null?Estructuras.getListaRazaSemoviente().getDomainNode(bienS.getRaza()).getTerm(locale):"";}catch(Exception e){return "";}
	        	if (field.getName().equalsIgnoreCase("bienes_inventario.conservacion"))
	            	try{return bienS.getConservacion()!=null?Estructuras.getListaEstadoConservacion().getDomainNode(bienS.getConservacion()).getTerm(locale):"";}catch(Exception e){return "";}
	           	if (field.getName().equalsIgnoreCase("bienes_inventario.propiedad"))
	          		try{return bienS.getPropiedad()!=null?Estructuras.getListaPropiedadPatrimonial().getDomainNode(bienS.getPropiedad()).getTerm(locale):"";}catch(Exception e){return "";}
	        }
	        if (bien instanceof ValorMobiliarioBean){
	        	ValorMobiliarioBean bienVM=(ValorMobiliarioBean)bien;
	        	if (field.getName().equalsIgnoreCase("bienes_inventario.clase_valor"))
	        		try{return bienVM.getClase()!=null?Estructuras.getListaClasesValorMobiliario().getDomainNode(bienVM.getClase()).getTerm(locale):"";}catch(Exception e){return "";}
	        }
	          
	        if (bien instanceof MuebleBean){
	        	MuebleBean bienM=(MuebleBean)bien;
	        	if (field.getName().equalsIgnoreCase("bienes_inventario.conservacion"))
	            	try{return bienM.getEstadoConservacion()!=null?Estructuras.getListaEstadoConservacion().getDomainNode(bienM.getEstadoConservacion()).getTerm(locale):"";}catch(Exception e){return "";}
	           	if (field.getName().equalsIgnoreCase("bienes_inventario.propiedad"))
	          		try{return bienM.getPropiedad()!=null?Estructuras.getListaPropiedadPatrimonial().getDomainNode(bienM.getPropiedad()).getTerm(locale):"";}catch(Exception e){return "";}
	        }
	      
	        if (bien instanceof ViaBean){
	        	ViaBean bienV=(ViaBean)bien;
	        	if (field.getName().equalsIgnoreCase("bienes_inventario.codigo"))
	            	try{return bienV.getCodigo()!=null?Estructuras.getListaTiposViaINE().getDomainNode(bienV.getCodigo()).getTerm(locale):"";}catch(Exception e){return "";}
	        }
	        if (bien instanceof InmuebleBean){
	        	InmuebleBean bienI=(InmuebleBean)bien;
	        	if (field.getName().equalsIgnoreCase("bienes_inventario.conservacion"))
	            	try{return bienI.getEstadoConservacion()!=null?Estructuras.getListaEstadoConservacion().getDomainNode(bienI.getEstadoConservacion()).getTerm(locale):"";}catch(Exception e){return "";}
	            if (field.getName().equalsIgnoreCase("bienes_inventario.propiedad"))
	          		try{return bienI.getPropiedad()!=null?Estructuras.getListaPropiedadPatrimonial().getDomainNode(bienI.getPropiedad()).getTerm(locale):"";}catch(Exception e){return "";}
	          	if (field.getName().equalsIgnoreCase("bienes_inventario.tipo_construccion"))
	            	try{return bienI.getTipoConstruccion()!=null?Estructuras.getListaTipoConstruccion().getDomainNode(bienI.getTipoConstruccion()).getTerm(locale):"";}catch(Exception e){return "";}
	            if (field.getName().equalsIgnoreCase("bienes_inventario.fachada"))
	              	try{return bienI.getFachada()!=null?Estructuras.getListaTipoFachada().getDomainNode(bienI.getFachada()).getTerm(locale):"";}catch(Exception e){return "";}
	            if (field.getName().equalsIgnoreCase("bienes_inventario.cubierta"))
	               	try{return bienI.getCubierta()!=null?Estructuras.getListaTipoCubierta().getDomainNode(bienI.getCubierta()).getTerm(locale):"";}catch(Exception e){return "";}
	            if (field.getName().equalsIgnoreCase("bienes_inventario.carpinteria"))
	               	try{return bienI.getCarpinteria()!=null?Estructuras.getListaTipoCarpinteria().getDomainNode(bienI.getCarpinteria()).getTerm(locale):"";}catch(Exception e){return "";}
	   	    }
        }else if (bien instanceof BienRevertible){
          	BienRevertible bienR=(BienRevertible)bien;
      	    if (field.getName().equalsIgnoreCase("bienes_inventario.transmision"))
               	try{return bienR.getCatTransmision()!=null?Estructuras.getListaTransmision().getDomainNode(bienR.getCatTransmision()).getTerm(locale):"";}catch(Exception e){return "";}
            if (field.getName().equalsIgnoreCase("bienes_inventario.tipo"))
    	       try{return Estructuras.getListaClasificacionBienesPatrimonio().getDomainNode(Const.SUPERPATRON_REVERTIBLES).getTerm(locale).toUpperCase();}catch(Exception e){ return "";}
    	     
   	        if (field.getName().equalsIgnoreCase("bienes_inventario.numInventario"))
    	    	return bienR.getNumInventario();
    	    if (field.getName().equalsIgnoreCase("bienes_inventario.fecha_inicio")){
    	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	    	return sdf.format(bienR.getFechaInicio());
    	    }
    	    if (field.getName().equalsIgnoreCase("bienes_inventario.fecha_vencimiento"))
    	    {
    	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	    	return sdf.format(bienR.getFechaVencimiento());
    	    }
    	    if (field.getName().equalsIgnoreCase("bienes_inventario.poseedor"))
    	    	return bienR.getPoseedor();
    	    if (field.getName().equalsIgnoreCase("bienes_inventario.titulo_posesion"))
    	    	return bienR.getTituloPosesion();
    	       
   	        
        }else if (bien instanceof Lote){
        	  if (field.getName().equalsIgnoreCase("bienes_inventario.tipo"))
       	       try{return Estructuras.getListaClasificacionBienesPatrimonio().getDomainNode(Const.SUPERPATRON_LOTES).getTerm(locale).toUpperCase();}catch(Exception e){ return "";}
       	  
       	    if (field.getName().equalsIgnoreCase("bienes_inventario.nombre")){
	        	return ((Lote)bien).getNombre_lote();
	        }
	        if (field.getName().equalsIgnoreCase("bienes_inventario.fecha_alta"))
	        {
	        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	    	return sdf.format(((Lote)bien).getFecha_alta());
	        }
	        
    	    if (field.getName().equalsIgnoreCase("bienes_inventario.fecha_ultima_modificacion"))
	    	{
	    	   	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    	   	return sdf.format(((Lote)bien).getFecha_ultima_modificacion());
    	    }
    	   
    	    if (field.getName().equalsIgnoreCase("lote.seguro"))
    	    	return ((Lote)bien).getSeguro();
    	    
        }
        if (field.getName().equalsIgnoreCase("bien"))
             return bien;
          return "NO ENCONTRADO";
    }


  /**
   * @return true if there is another object in the result set.
   */
    public boolean next() throws JRException {
      if (bienes == null) return false;

      index++;
      try{
    	  if (index < bienes.length){
    		  //Cargamos el bien con todos los datos de la BD
    		  bienes[index]=cargarBienInventario((bienes[index]));
    		  //rellenamos la lista de documentos
    		  if (bienes[index] instanceof Documentable){
    		        Collection c= documentClient.getAttachedDocuments(bienes[index]);
    		        ((Documentable)bienes[index]).setDocumentos(c);
    		  }
    		  return true;
      		}
      }catch(Exception ex){
    	  logger.error("Error al intentar obtener los datos del bien",ex);
      }
      return index < bienes.length;
    }
    
    private Object cargarBienInventario(Object cargable) throws Exception{
    	if (cargable instanceof BienBean){
    		BienBean bien= (BienBean)cargable;
	    	if (bien.getTipo().equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS) ||
	                    bien.getTipo().equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS))
	           return inventarioClient.getBienInventario(Const.ACTION_GET_INMUEBLE, bien.getTipo(), 
	                    bien.getId(),bien.getRevisionActual(),bien.getRevisionExpirada());
	        if (bien.getTipo().equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART) ||
	    			bien.getTipo().equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES))
	           return (BienBean)inventarioClient.getBienInventario(Const.ACTION_GET_MUEBLE, bien.getTipo(), 
	                    bien.getId(),bien.getRevisionActual(),bien.getRevisionExpirada());
	       if (bien.getTipo().equalsIgnoreCase(Const.PATRON_DERECHOS_REALES))
	           return (BienBean) inventarioClient.getBienInventario(Const.ACTION_GET_DERECHO_REAL, bien.getTipo(), 
	            		bien.getId(),bien.getRevisionActual(),bien.getRevisionExpirada());
	       if (bien.getTipo().equalsIgnoreCase(Const.PATRON_VALOR_MOBILIARIO))
	           	return (BienBean) inventarioClient.getBienInventario(Const.ACTION_GET_VALOR_MOBILIARIO, bien.getTipo(), 
	           			bien.getId(),bien.getRevisionActual(),bien.getRevisionExpirada());
	       if (bien.getTipo().equalsIgnoreCase(Const.PATRON_CREDITOS_DERECHOS_PERSONALES))
	            return (BienBean) inventarioClient.getBienInventario(Const.ACTION_GET_CREDITO_DERECHO, bien.getTipo(),
	            		bien.getId(),bien.getRevisionActual(),bien.getRevisionExpirada());
	        if (bien.getTipo().equalsIgnoreCase(Const.PATRON_SEMOVIENTES))
	          	return (BienBean) inventarioClient.getBienInventario(Const.ACTION_GET_SEMOVIENTE, bien.getTipo(), 
	          			bien.getId(),bien.getRevisionActual(),bien.getRevisionExpirada());
	        if (bien.getTipo().equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_URBANAS) ||
	    			bien.getTipo().equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_RUSTICAS))
	            return (BienBean) inventarioClient.getBienInventario(Const.ACTION_GET_VIA, bien.getTipo(), 
	            		bien.getId(),bien.getRevisionActual(),bien.getRevisionExpirada());
	        if (bien.getTipo().equalsIgnoreCase(Const.PATRON_VEHICULOS))
	            return (BienBean) inventarioClient.getBienInventario(Const.ACTION_GET_VEHICULO, bien.getTipo(), 
	                    			bien.getId(),bien.getRevisionActual(),bien.getRevisionExpirada());
	   }
       return cargable;
    
    }
   
}
