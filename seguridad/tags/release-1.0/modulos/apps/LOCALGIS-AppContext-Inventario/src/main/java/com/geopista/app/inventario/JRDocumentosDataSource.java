package com.geopista.app.inventario;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.geopista.app.AppContext;
import com.geopista.global.ServletConstants;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.Lote;
import com.geopista.ui.plugin.edit.DocumentManagerPlugin;
import com.lowagie.text.pdf.hyphenation.TernaryTree.Iterator;

public class JRDocumentosDataSource implements net.sf.jasperreports.engine.JRDataSource {
	 private Logger logger = Logger.getLogger(JRInventarioDataSource.class);
	  
	 private int index= -1;
	 private Object[] documentos;
	
	    
	 public JRDocumentosDataSource(Collection<com.geopista.protocol.document.DocumentBean>  documentos) {
	        this.documentos= documentos!=null?documentos.toArray():null;
	 }
     /***
      * Muestra los documentos de un tipo determinado
      * @param documentos
      * @param tipo el tipo puede ser IMG_CODE, DOC_CODE o ALL_CODE
      */
	 public JRDocumentosDataSource(Collection<com.geopista.protocol.document.DocumentBean>  documentos, char tipo) {
		     repartirDocumentos(documentos, tipo);
	 }
	 
	 /**
	  * Reparte los documentos en los diferentes tipos
	  * @param documentos
	  * @param tipo
	  */
	 private void repartirDocumentos(Collection<com.geopista.protocol.document.DocumentBean>  documentos, char tipo) {
         Vector<com.geopista.protocol.document.DocumentBean> auxDocumentos= new Vector<com.geopista.protocol.document.DocumentBean>();
         for (java.util.Iterator it=documentos.iterator();it.hasNext();){
         	DocumentBean documento=(DocumentBean) it.next();
         	switch (tipo){
         		case (DocumentBean.ALL_CODE):
         			auxDocumentos.add(documento);
         			break;
         		case (DocumentBean.IMG_CODE):
         			if (documento.isImagen())
         				auxDocumentos.add(documento);
         			break;
         		case (DocumentBean.DOC_CODE):
         			if (!documento.isImagen())
         				auxDocumentos.add(documento);
         			break;
                 default:
                 	auxDocumentos.add(documento);
                     break;
         	}
         }
	        this.documentos= auxDocumentos!=null?auxDocumentos.toArray():null;
	 }
	 /***
      * Muestra los documentos de un tipo determinado
      * @param documentos
      * @param tipo el tipo puede ser IMG_CODE, DOC_CODE o ALL_CODE
      */
	 public JRDocumentosDataSource(Long id, char tipo,String patron) {
		   DocumentClient documentClient= null;
		   documentClient= new DocumentClient(((AppContext) AppContext.getApplicationContext()).getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
				   ServletConstants.DOCUMENT_SERVLET_NAME);
		   
		   Object aux =null;
		   if (Const.SUPERPATRON_LOTES.equals(patron)){
			   Lote auxLote = new Lote();
			   auxLote.setId_lote(id);
			   aux=auxLote;
		   }else if (Const.SUPERPATRON_REVERTIBLES.equals(patron)){
			   BienRevertible auxBienRevertible = new BienRevertible();
			   auxBienRevertible.setId(id);
			   aux=auxBienRevertible;
		   } else{
			   BienBean auxBien = new BienBean();
			   auxBien.setId(id);
			   aux=auxBien;
		   }
		   Collection documentos=null;
		   try{
		      documentos= documentClient.getAttachedDocuments(aux);
		   }catch(Exception ex){
			  logger.error("Error al obtener los documentos para el bien "+id+" del tipo "+tipo);
		   }
   	       repartirDocumentos(documentos, tipo);
	
	 }
	 
	 /***
      * Muestra los documentos de un tipo determinado
      * @param documentos
      * @param tipo el tipo puede ser IMG_CODE, DOC_CODE o ALL_CODE
      */
	 public JRDocumentosDataSource(Long id, char tipo) {
		 this(id,tipo,Const.SUPERPATRON_BIENES);
	 }
     
	 public Object getFieldValue(JRField field) throws JRException{
	        DocumentBean documento= (DocumentBean)documentos[index];
            if (field.getName().equals("documento")) return documento;
            return null;
       }


	  /**
	   * @return true if there is another object in the result set.
	   */
	    public boolean next() throws JRException {
	      if (documentos == null) return false;
	      index++;
	      return (index < documentos.length);
	    }



}
