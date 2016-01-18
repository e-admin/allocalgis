package com.geopista.app.inventario;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.deegree.xml.XMLTools;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.geopista.app.metadatos.xml.XMLTranslator_LCGIII;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.administrador.dominios.Domain;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.administrador.dominios.ListaDomain;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 14-mar-2005
 * Time: 19:15:03
 */
public class LoadCatalogo extends Load {
	private String url=null;
        /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private static final String TIPO_FICHERO="catalogo";
        private static final String PATRIMONIO_CATEGORY="4";
        private Entidad entidad;
        private int nuevo;
       
        /**
         * Constructor de la clase   
         * @param parent ventana padre
         * @param modal indica si es modal o no 
         * @param messages textos de la aplicación
         */
        public LoadCatalogo(java.awt.Frame parent, boolean modal,
                            ResourceBundle messages, Municipio municipio) {
            super(parent, modal, messages, municipio);
            NOMBRE_ESQUEMA="catalogo.xsd";
            url= aplicacion.getString("geopista.conexion.servidorurl")+"administracion";
            
            try{
            	entidad =  (new OperacionesAdministrador(url))
				.getEntidad(com.geopista.security.SecurityManager.getIdEntidad());
            }catch (Exception ex){
            	logger.error("Error al obtener la entidad", ex);
            }	
            changeScreenLang(messages);

        }

               
        /**
        * Funciòn que se ejecuta cuando el usuario pincha la opción 
        * aceptar.
        */
       protected void aceptar(){
          try{ 
	           if(okCancelPanel.wasOKPressed())
	           {
	        	   if (jTextFieldFichero.getText().trim().length()==0){
	        		   JOptionPane optionPane= new JOptionPane(messages.getString("inventario.load.seleccionar"),JOptionPane.WARNING_MESSAGE);
		        	   JDialog dialog =optionPane.createDialog(this,messages.getString("inventario.loadcatalogo.title"));
		        	   dialog.setVisible(true);
		        	   return;   
	        	   }
                  String [] literalMunicipio={municipio.getNombre()};     	        		   
	        	   int opcionElegida = JOptionPane.showConfirmDialog(this, getStringWithParameters(messages, "inventario.loadcatalogo.atencion", literalMunicipio));
	
	               if (opcionElegida == JOptionPane.YES_OPTION)
	               {
	            	  
	            	  if (validateSAXdocumentWithXSD(jTextFieldFichero.getText(),TIPO_FICHERO)<=0){
	            		  JOptionPane optionError= new JOptionPane(messages.getString("inventario.loadinventario.nofiles")+": "+TIPO_FICHERO,JOptionPane.ERROR_MESSAGE);
	   	        	      JDialog dialogError =optionError.createDialog(this,messages.getString("inventario.loadinventario.error"));
	   	        	      dialogError.setVisible(true);
	   	        	      return;
           		  }
	            	  initProgressDialog(messages.getString("inventario.loadinventario.dialogo.title"));
	            	  Object []resultados={ new Integer(ok), new Integer(total), new Integer(nuevo)};
	            	  JOptionPane optionPane= new JOptionPane(getStringWithParameters(messages, "inventario.loadcatalogo.ok", resultados),JOptionPane.INFORMATION_MESSAGE);
	        	      JDialog dialog =optionPane.createDialog(this,messages.getString("inventario.loadinventario.title"));
	        	      dialog.setVisible(true);
	        	      Estructuras.setCargada(false);
	        	      Estructuras.setIniciada(false);
	        	      Estructuras.cargarEstructuras();
	        	      salir();
	        	 	}
	           }else{
	        	   salir(); 
	           }
          }catch (org.xml.sax.SAXException saex){
       	   logger.error("Se ha producido un error al fichero con su esquema",saex);
       	   ErrorDialog.show(this, "ERROR", messages.getString("inventario.loadinventario.error.esquema"), StringUtil.stackTrace(saex));
          }catch (Exception ex){
       	   logger.error("Se ha producido un error al cargar el fichero",ex);
       	   ErrorDialog.show(this, "ERROR", messages.getString("inventario.loadinventario.error"), StringUtil.stackTrace(ex));
          }
       }
        
        protected void abrirFichero(){
        	abrirFichero(messages.getString("inventario.loadinventario.fichero"));
        }
        
        /***
         * Carga los valores del inventario
         * @param fileName
         */
        protected void cargaDatos(TaskMonitorDialog progressDialog) throws Exception{
        	
        	 ListaDomain listaDominiosGeneral=new ListaDomain();
         	 ListaDomain listaDominiosEntidad=new ListaDomain();
        	 
        	 (new OperacionesAdministrador(url)).getDominios(PATRIMONIO_CATEGORY,listaDominiosGeneral,listaDominiosEntidad);
        	 
        	 Document doc = parseXmlFile(jTextFieldFichero.getText());
        	 total = numeroTotalCatalogo(doc);
        	 int numeroElemento=0;
        	 ok=0;
        	 mal=0;
        	 nuevo=0;
        	 Element root = (Element)doc.getDocumentElement();;
        	 NodeList catalogo = root.getChildNodes();
 			 for (int i=0; i<catalogo.getLength() && !progressDialog.isCancelRequested(); i++) {
 				if (catalogo.item(i) instanceof Element) {
 					numeroElemento++;
 					Object[] resultados= {new Integer(numeroElemento), new Integer(total)};
 					jLabelInformacion.setText(getStringWithParameters(messages, "inventario.load.dialogo.report2",resultados));
 					progressDialog.report(getStringWithParameters(messages, "inventario.load.dialogo.report2",resultados));
 		 			
 					try{
 						Element elementoCatalogo = (Element) catalogo.item(i);
                        
 						DomainNode nodoBook = getCatalogo(elementoCatalogo);
 	 	 				
 						
 						//Buscamos el dominio en la Base de datos para ver si existe. Nos
 						//devuelve el objeto superior del dominio
 						Domain dominioInicial=getDominio(listaDominiosEntidad, nodoBook.getIdNode());
 					
 						
 						//Los dominios los metemos debajo del Dominio Padre "Patrimonio"
 						if (dominioInicial== null){
 						   //Es necesario crear el dominio inicial porque no existe
 						   logger.warn("Dominio Inicial "+ nodoBook.getFirstTerm()+ " no encontrado es necesario crearlo");
 						   dominioInicial= new Domain("", nodoBook.getFirstTerm());
 						   dominioInicial.setName(nodoBook.getFirstTerm());
 						   dominioInicial.setIdCategory(PATRIMONIO_CATEGORY);
 						   CResultadoOperacion resultado=(new OperacionesAdministrador(url)).nuevoDomain(dominioInicial);
 						   if (resultado.getResultado()){
 							   dominioInicial=(Domain)resultado.getVector().get(0);
 							   dominioInicial.setIdDomain(resultado.getDescripcion());
 							   
 	 						}else{
 	 							logger.error("Error al añadir el dominio "+dominioInicial.getName());
 	 							mal++;
 	 							continue;
 	 						}
 	 						nuevo++;
 						}
 							
 						
 						//Borramos los elementos debajo del dominio. El dominio padre no lo borramos.
 						//porque podría estar referenciado en algun otro sitio.
 						for (Enumeration<DomainNode> e= dominioInicial.getListaNodes().gethDom().elements();e.hasMoreElements();){
 							DomainNode domain=(DomainNode)e.nextElement();
 							logger.info("Borrando DomainNode:"+domain.getIdNode()+"-"+domain.getFirstTerm());
 							eliminarDomainNodeBD(domain);
 			
 						}	
 						
 						//Le ponemos el identificador de dominio al nuevo dominio creado.
 						actualizarIdDomain(nodoBook, dominioInicial.getIdDomain());
 						 					
 						//Añadimos el nodo code book al sistema.
 						CResultadoOperacion resultado=(new OperacionesAdministrador(url)).nuevoDomainNode(nodoBook);
 						if (resultado.getResultado()){
 							DomainNode auxNode=(DomainNode)resultado.getVector().get(0);
 							logger.info("Nodo añadido:"+auxNode.getIdNode()+" "+nodoBook.getFirstTerm());
 						    ok++;
 						}
 						else{
 							logger.info("Error al añadir el nodo:"+nodoBook.getFirstTerm());
 							logger.info("Resultado:"+resultado.getDescripcion());
 							mal++;
 						}
 					}catch(Exception ex){
 						logger.error("Error al exportar un elemento del catalogo",ex);
 						mal++;
 					}
 				}
 			}
        }
        
        /**
         * Actualiza el idDomain
         * @param nodo
         * @param idDomain
         */
        private void actualizarIdDomain(DomainNode domainNode, String idDomain)
        {
        	domainNode.setIdDomain(idDomain);
        	//primero actualizamos los hijos
    		if (domainNode.getlHijos()!=null && domainNode.getlHijos().gethDom()!=null && domainNode.getlHijos().gethDom().size()>0 ){
    			for (Enumeration e=domainNode.getlHijos().gethDom().elements();e.hasMoreElements();){
    				actualizarIdDomain((DomainNode)e.nextElement(), idDomain);
    			}
    		}
        }
			
        /**
         * Elimina el nodo de la base de datos
         * @param domainNode
         */
        private void eliminarDomainNodeBD(DomainNode domainNode){
        	try{
        		//primero eliminamos los hijos. Los que tienen los diversos patrones.
        		if (domainNode.getlHijos()!=null && domainNode.getlHijos().gethDom()!=null && domainNode.getlHijos().gethDom().size()>0 ){
        			for (Enumeration e=domainNode.getlHijos().gethDom().elements();e.hasMoreElements();){
        				eliminarDomainNodeBD((DomainNode)e.nextElement());
        			}
        		}
        		
        		logger.info("Eliminando el nodo: "+domainNode.getIdNode()+" Patron:"+domainNode.getPatron());
        		domainNode.setForceDelete(true);
        		CResultadoOperacion resultadoOperacion=(new OperacionesAdministrador(url)).eliminarDomainNode(domainNode);
        		logger.info("Resultado Eliminacion: "+resultadoOperacion.getDescripcion());
        		
        	}catch(Exception ex){
        		logger.error("Excepcion al eliminar el domainNode: "+domainNode.getIdNode(),ex);
        	}
        }
        /**
         * Devuelve de una lista de dominios el nodo con un nombre determinado
         * @param lista
         * @param domainName
         * @return
         */
        private Domain getDominio(ListaDomain lista, String domainName){
        	if (lista==null || domainName == null) return null;
        	Hashtable <String,Domain> aux= lista.getDom();
        	for(Enumeration<Domain> e=aux.elements();e.hasMoreElements();){
        		Domain auxDomain=(Domain) e.nextElement();
        		//Si hubo algun problema podría haber varios elementos.
        		if (domainName.equalsIgnoreCase(auxDomain.getName()))
        		    return auxDomain;
        	}
        	
        	return null;
        	
        	
        }
        /**
         * Devuelve un catalogo a partir del element del documento.
         * 
         * @param element
         * @return
         */
        private DomainNode getCatalogo(Element element){
        	
        	 DomainNode nodoPadre = new DomainNode();
        	 nodoPadre.setIdNode(XMLTools.getAttrValue(element, "name"));
        	 nodoPadre.addTerm("es_ES", XMLTools.getAttrValue(element, "name"));
        	 nodoPadre.setIdMuni(entidad.getId());
        	 nodoPadre.setType(com.geopista.feature.Domain.CODEBOOK);
        	
        	 Vector<Element> nodos=XMLTranslator_LCGIII.recuperarHijosAsVector(element, "nodo");
             for (Enumeration <Element>e=nodos.elements();e.hasMoreElements();){
            		    Element nodo=(Element)e.nextElement();
            		    DomainNode nodoHijo = new DomainNode();
            		    nodoHijo.setPatron(XMLTools.getAttrValue(nodo, "id"));
            		    nodoHijo.setType(com.geopista.feature.Domain.CODEDENTRY);
            		    nodoHijo.setIdMuni(entidad.getId());
            		    nodoHijo.setIdNode(XMLTools.getAttrValue(nodo, "id"));
            		    Vector<Element> descripciones=XMLTranslator_LCGIII.recuperarHijosAsVector(nodo, "descripcion");
            		    for (Enumeration <Element>eDes=descripciones.elements();eDes.hasMoreElements();){
            		    	Element descripcion=(Element)eDes.nextElement();
                		    nodoHijo.addTerm(XMLTools.getAttrValue(descripcion, "locale"), XMLTools.getAttrValue(descripcion, "valor"));
            		    }
            		    nodoPadre.addHijo(nodoHijo);
             } 
        	 return nodoPadre;
        	/*   	 
        	<dominio name="Uso jurídico del bien">
    		<nodo id="1">
    			<descripcion valor="Dominio Público/Uso Público" locale="es_ES"/>
    		</nodo>
    		<nodo id="a">
    			<descripcion valor="Dominio Público/Servicio Público" locale="es_ES"/>
    		</nodo>
    		<nodo id="3">
    			<descripcion valor="Dominio Público/Comunal" locale="es_ES"/>
    		</nodo>
    		<nodo id="4">
    			<descripcion valor="Patrimonial con carácter General" locale="es_ES"/>
    		</nodo>
    		<nodo id="5">
    			<descripcion valor="Patrimonial/Parcelas Sobrantes" locale="es_ES"/>
    		</nodo>
    		<nodo id="6">
    			<descripcion valor="Patrimonial/Efectos no utilizables" locale="es_ES"/>
    		</nodo>
    	</dominio>
    	<dominio name="Tipo de Amortizacion">
    		<nodo id="1">
    			<descripcion valor="Por años" locale="es_ES"/>
    		</nodo>
    		<nodo id="2">
    			<descripcion valor="Por porcentaje" locale="es_ES"/>
    		</nodo>
    		<nodo id="3">
    			<descripcion valor="Ninguno" locale="es_ES"/>
    		</nodo>
    	</dominio>
    	<dominio name="Forma de adquisicion">
    		<nodo id="1">
    			<descripcion valor="Compra-Venta" locale="es_ES"/>
    		</nodo>
    		<nodo id="2">
    			<descripcion valor="Herencia" locale="es_ES"/>
    		</nodo>
    	</dominio>*/
        }
        
/**
 * Cambia el lenguaje de los textos
 * @param messages
 */
        public void changeScreenLang(ResourceBundle messages) {
        	try{
		        setTitle(GeopistaUtil.i18n_getname("inventario.loadcatalogo.title",messages));
		        jPanelLoad.setToolTipText(GeopistaUtil.i18n_getname("inventario.loadcatalogo.title",messages));
		        jButtonCancelar.setText(GeopistaUtil.i18n_getname("OKCancelPanel.Cancel",messages));
		        jButtonCancelar.setToolTipText(GeopistaUtil.i18n_getname("OKCancelPanel.Cancel",messages));
		        jButtonAceptar.setText(GeopistaUtil.i18n_getname("OKCancelPanel.OK",messages));
		        jButtonAceptar.setToolTipText(GeopistaUtil.i18n_getname("OKCancelPanel.OK",messages));
		        jButtonObtenerSchema.setText(GeopistaUtil.i18n_getname("inventario.load.esquema",messages));
		        jButtonObtenerSchema.setToolTipText(GeopistaUtil.i18n_getname("inventario.load.esquema",messages));
		        jLabelFichero.setText(GeopistaUtil.i18n_getname("inventario.loadcatalogo.fichero",messages)+":");
		        if (entidad!=null){
		        	String [] literalEntidad = { this.entidad.getNombre()};
		        	jTextPaneComentario.setText(getStringWithParameters(messages,"inventario.loadcatalogo.texto.esquema", literalEntidad));
		        }
		        jTextPaneComentario.setCaretPosition(jTextPaneComentario.getStyledDocument().getLength());
	            jTextPaneComentario.insertComponent(jButtonObtenerSchema);
		    }catch (Exception ex){
				logger.error("Falta algun recurso:",ex);
			}
          }

     
         

    }

