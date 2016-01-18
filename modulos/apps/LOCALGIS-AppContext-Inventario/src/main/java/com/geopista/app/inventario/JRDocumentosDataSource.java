/**
 * JRDocumentosDataSource.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario;

import java.util.Collection;
import java.util.Vector;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.global.ServletConstants;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.Lote;

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
		   documentClient= new DocumentClient(((AppContext) AppContext.getApplicationContext()).getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
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
