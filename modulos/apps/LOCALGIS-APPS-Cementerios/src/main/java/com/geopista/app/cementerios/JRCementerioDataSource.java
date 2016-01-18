/**
 * JRCementerioDataSource.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios;

import java.util.Collection;
import java.util.Vector;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.global.ServletConstants;
import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.document.CementerioDocumentClient;


public class JRCementerioDataSource implements net.sf.jasperreports.engine.JRDataSource {

	protected Logger logger = Logger.getLogger(JRCementerioDataSource.class);
	private String locale;
   
    private CementerioDocumentClient documentClient= null;

    private Object[] elementosCementerio;
    private int index= -1;

    public JRCementerioDataSource(Collection<ElemCementerioBean> elementosCementerio,  String locale) {
        this.locale= locale;
        Vector<ElemCementerioBean> elemsSinVersion= new Vector<ElemCementerioBean>();
        
        for (java.util.Iterator<ElemCementerioBean> it=  elementosCementerio.iterator();it.hasNext();){
        	ElemCementerioBean elemento = (ElemCementerioBean)it.next();
//    		if (!bien.isVersionado()){
//    			bienesSinVersion.add(bien);
//    		}
        	elemsSinVersion.add(elemento);
    	}
        this.elementosCementerio= elemsSinVersion!=null?elemsSinVersion.toArray():null;
        documentClient= new CementerioDocumentClient(((AppContext) AppContext.getApplicationContext()).getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
                ServletConstants.DOCUMENT_SERVLET_NAME);

    }
    
    public JRCementerioDataSource(ElemCementerioBean elem,  String locale) {
        this.locale= locale;
        Vector<ElemCementerioBean> elementos= new Vector<ElemCementerioBean>();
  	    elementos.add(elem);
        this.elementosCementerio= elementos.toArray();
        documentClient= new CementerioDocumentClient(((AppContext) AppContext.getApplicationContext()).getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
        		ServletConstants.DOCUMENT_SERVLET_NAME);

    }
    
    

    /**
     * Gets the current field index based on the position in
     * the mapped fields passed to the constructor. Then gets
     * the current object value held at the field index.
     *
     * @return the value of a field
     */
//      public Object getFieldValue(JRField field) throws JRException{
//
//        Object value= null;
//        ElemCementerioBean elem= (ElemCementerioBean)elementosCementerio[index];
//
//        if (field.getName().equals("ayuntamiento")){
//            value= "EXCMO. AYUNTAMIENTO DE "+Constantes.Provincia.toUpperCase();
//        }else if (field.getName().equalsIgnoreCase("municipio")){
//            value= Constantes.IdMunicipio + " - " + Constantes.Municipio + " (" + Constantes.Provincia + ")";
//        }else if (field.getName().equalsIgnoreCase("titulo")){
//            value= "FICHA DE INVENTARIO DEL PATRIMONIO";
//        }else if (field.getName().startsWith("IMG_")){
//            /** En la plantilla jrxml, el campo de una imagen tiene que tener el siguien formato:
//             *  IMG_nombreFichero.ext (IMG_logo_geopista.gif) */
//            String nombreFichero= field.getName().substring(4);
//            value= Constantes.PATH_PLANTILLAS_INVENTARIO + File.separator +"img" + File.separator + nombreFichero;
//        }else if (field.getName().equalsIgnoreCase("bienes_inventario.tipo"))
//            try{value= bien.getTipo()!=null?Estructuras.getListaSubtipoBienesPatrimonio().getDomainNode(bien.getTipo()).getTerm(locale).toUpperCase():"";}catch(Exception e){}
//        else if (field.getName().equalsIgnoreCase("bienes_inventario.numinventario"))
//            value= bien.getNumInventario();
//        else if (field.getName().equalsIgnoreCase("bienes_inventario.fecha_alta"))
//            try{value= Constantes.df.format(bien.getFechaAlta());}catch(Exception e){value= "";}
//        else if (field.getName().equalsIgnoreCase("bienes_inventario.fecha_ultima_modificacion"))
//            try{value= Constantes.df.format(bien.getFechaUltimaModificacion());}catch(Exception e){value= "";}
//        else if (field.getName().equalsIgnoreCase("bienes_inventario.nombre"))
//            value= bien.getNombre()!=null?bien.getNombre():"";
//        else if (field.getName().equalsIgnoreCase("bienes_inventario.uso"))
//            try{value= bien.getUso()!=null?Estructuras.getListaUsoJuridico().getDomainNode(bien.getUso()).getTerm(locale):"";}catch(Exception e){value="";}
//        else if (field.getName().equalsIgnoreCase("bienes_inventario.descripcion"))
//            value= bien.getDescripcion()!=null?bien.getDescripcion():"";
//        else if (field.getName().equalsIgnoreCase("bienes_inventario.organizacion"))
//            value= bien.getDescripcion()!=null?bien.getOrganizacion():"";
//        else if (field.getName().equalsIgnoreCase("creditoDerecho"))
//             value=bien;   
//        if ((value==null) && (bien instanceof InmuebleBean)){
//            try{value= JRBienesDataSource.getInmuebleValue(field, (InmuebleBean)bien, locale);}catch(Exception e){throw new JRException(e.toString());}
//        }
//        if ((value==null) && (bien instanceof MuebleBean)){
//            try{value= JRBienesDataSource.getMuebleValue(field, (MuebleBean)bien, locale);}catch(Exception e){throw new JRException(e.toString());}
//        }
//        if ((value==null) && (bien instanceof CreditoDerechoBean)){
//            try{value= JRBienesDataSource.getCreditoDerechoValue(field, (CreditoDerechoBean)bien, locale);}catch(Exception e){throw new JRException(e.toString());}
//        }
//        if ((value==null) && (bien instanceof DerechoRealBean)){
//            try{value= JRBienesDataSource.getDerechoRealValue(field, (DerechoRealBean)bien, locale);}catch(Exception e){throw new JRException(e.toString());}
//        }
//        if ((value==null) && (bien instanceof ValorMobiliarioBean)){
//             try{value= JRBienesDataSource.getValorMobiliarioValue(field, (ValorMobiliarioBean)bien, locale);}catch(Exception e){throw new JRException(e.toString());}
//        }
//        if ((value==null) && (bien instanceof SemovienteBean)){
//             try{value= JRBienesDataSource.getSemovienteValue(field, (SemovienteBean)bien, locale);}catch(Exception e){throw new JRException(e.toString());}
//        }
//        if ((value==null) && (bien instanceof VehiculoBean)){
//             try{value= JRBienesDataSource.getVehiculoValue(field, (VehiculoBean)bien, locale);}catch(Exception e){throw new JRException(e.toString());}
//        }
//        if ((value==null) && (bien instanceof ViaBean)){
//             try{value= JRBienesDataSource.getViaValue(field, (ViaBean)bien, locale);}catch(Exception e){throw new JRException(e.toString());}
//        }
//
//        return value;
//      }


  /**
   * @return true if there is another object in the result set.
   */
    public boolean next() throws JRException {
      if (elementosCementerio == null) return false;

      index++;
      try{
    	  if (index < elementosCementerio.length){
    		  //rellenamos la lista de documentos
    		  Collection c= documentClient.getAttachedDocuments(elementosCementerio[index]);
    		  ((ElemCementerioBean)elementosCementerio[index]).setDocumentos(c);
    		  return true;
      		}
      }catch(Exception ex){
    	  logger.error("Error al intentar obtener los datos del bien"+((ElemCementerioBean)elementosCementerio[index]).getId(),ex);
      }
      return index < elementosCementerio.length;
    }

	@Override
	public Object getFieldValue(JRField arg0) throws JRException {
		// TODO Auto-generated method stub
		return null;
	}

   
}
