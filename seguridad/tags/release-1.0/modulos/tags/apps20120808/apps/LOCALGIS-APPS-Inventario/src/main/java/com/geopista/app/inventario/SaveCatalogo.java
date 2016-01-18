package com.geopista.app.inventario;


import java.awt.Frame;
import java.io.File;
import java.io.Writer;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.administrador.dominios.ListaDomain;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Esta clase exporta el catalogo del inventario de patrimonio a un fichero xml siguiendo un
 * esquema. 
 */

public class SaveCatalogo extends Save {

	private static final long serialVersionUID = 1L;
	private String url=null;
    private static final String PATRIMONIO_CATEGORY="4";
    
	private Document doc;

	public SaveCatalogo(Frame parent, boolean modal, ResourceBundle messages,
			Municipio municipio) {
		 super(parent, modal, messages, municipio);
         NOMBRE_ESQUEMA="catalogo.xsd";
         url= aplicacion.getString("geopista.conexion.servidorurl")+"administracion";
         
//         try{
//         	entidad =  (new OperacionesAdministrador(url))
//				.getEntidad(com.geopista.security.SecurityManager.getIdEntidad());
//         }catch (Exception ex){
//         	logger.error("Error al obtener la entidad", ex);
//         }	
         changeScreenLang(messages);
	}

	@Override
	public void changeScreenLang(ResourceBundle messages) {
		try {
			setTitle(GeopistaUtil.i18n_getname(
					"inventario.saveinventario.title", messages));
			jButtonCancelar.setText(GeopistaUtil.i18n_getname(
					"OKCancelPanel.Cancel", messages));
			jButtonCancelar.setToolTipText(GeopistaUtil.i18n_getname(
					"OKCancelPanel.Cancel", messages));
			jButtonGuardar.setText(GeopistaUtil.i18n_getname(
					"document.infodocument.botones.guardar", messages)
					+ " "+GeopistaUtil.i18n_getname(
							"catalogo", messages));
			jButtonGuardar.setToolTipText(GeopistaUtil.i18n_getname(
					"document.infodocument.botones.guardar", messages)
					+ " "+GeopistaUtil.i18n_getname(
							"catalogo", messages));

		} catch (Exception ex) {
			logger.error("Falta algun recurso:", ex);
		}
		
	}
	
	/**
	 * Devuelve el contenido de un documento xml en formato String 
	 */

	protected String getContentOfDocument(TaskMonitorDialog progressDialog) throws ParserConfigurationException, TransformerException {

		doc = createDocument();
		
		Element catalogo = (Element) doc.createElement("catalogo");
		doc.appendChild(catalogo);

		catalogo.setAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		catalogo
				.setAttribute("xsi:noNamespaceSchemaLocation", NOMBRE_ESQUEMA);
		catalogo.setAttribute("id_municipio", municipio.getId());
		
		try {
			insertarDominios(progressDialog,catalogo);
		} catch (Exception ex) {
			ErrorDialog.show(this, "ERROR",
					messages.getString(ex.getMessage()),
					StringUtil.stackTrace(ex));
		}
		
		return parseDocumentToXML(doc);
	}

	private void insertarDominios(TaskMonitorDialog progressDialog,Element catalogo) throws Exception{
		
		 ListaDomain listaDominiosGeneral=new ListaDomain();
     	 ListaDomain listaDominiosEntidad=new ListaDomain();
     	 
       	new OperacionesAdministrador(url).getDominios(PATRIMONIO_CATEGORY,listaDominiosGeneral,listaDominiosEntidad);
		int numeroElemento=0;
		Object[] resultados= {new Integer(numeroElemento), new Integer(listaDominiosEntidad.getDom().size())};
		
		for (Enumeration<com.geopista.protocol.administrador.dominios.Domain> e= listaDominiosEntidad.getDom().elements();e.hasMoreElements();){
					
			com.geopista.protocol.administrador.dominios.Domain domain=e.nextElement();
			
			numeroElemento++;
	       
			Element dominio = (Element) doc.createElement("dominio");
			catalogo.appendChild(dominio);
			dominio.setAttribute("name",domain.getName());
			
			for (Enumeration<com.geopista.protocol.administrador.dominios.DomainNode> enodes= domain.getListaNodes().gethDom().elements();enodes.hasMoreElements();) {
				com.geopista.protocol.administrador.dominios.DomainNode node=enodes.nextElement();
				Element nodo = (Element) doc.createElement("nodo");
				dominio.appendChild(nodo);
				nodo.setAttribute("id", node.getIdNode());
				for (Enumeration<com.geopista.protocol.administrador.dominios.DomainNode> enodesHijos= node.getlHijos().gethDom().elements();enodesHijos.hasMoreElements();) {
					com.geopista.protocol.administrador.dominios.DomainNode nodehijo=enodesHijos.nextElement();
					Element descripcion = (Element) doc.createElement("descripcion");
					nodo.appendChild(descripcion );
					descripcion.setAttribute("valor", nodehijo.getTerm("es_ES"));
					descripcion.setAttribute("locale","es_ES");
				}
			}	
		
	       	 progressDialog.report(getStringWithParameters(messages,
					"inventario.save.dialogo.report2", resultados));
		
		}
	}

	@Override
	public void guardarFichero() {
		Writer output = null;
     	
	   	 try {
	   		 GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
	   		 filter.addExtension("xml");
	   		 filter.setDescription(messages.getString("inventario.loadcatalogo.fichero"));
	   		 JFileChooser fc= new JFileChooser();
	   		 fc.setFileFilter(filter);
	   		 fc.setAcceptAllFileFilterUsed(false);
	   		 if (lastDirectory!= null){
	   			 File currentDirectory = lastDirectory;
	   			 fc.setCurrentDirectory(currentDirectory);
	   		 }
	   		 fc.setName(NOMBRE_ESQUEMA);
	   		 fc.setDialogTitle(messages.getString("inventario.savecatalogo.title"));
	   		 if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
	   			 return;
	   		 lastDirectory= fc.getCurrentDirectory(); 
	   		String contenidoString =saveCatalogo();


			 String nombreFichero= fc.getSelectedFile().getName();
             /** quitamos la extension que haya puesto el usuario */
             int index= nombreFichero.indexOf(".");
             if (index != -1){
            	 nombreFichero= nombreFichero.substring(0, index);
             }
             /** nos quedamos sólo con el path, sin el nombre del fichero */
             String pathDestino= fc.getSelectedFile().getPath();
             index= pathDestino.indexOf(nombreFichero);
             if (index != -1){
                 pathDestino= pathDestino.substring(0, index);
             }
             /** al nombre del fichero le añadimos la extension correspondiente al formato del fichero xml*/
             nombreFichero+=".xml";
           /** añadimos al path, el nombre del fichero con extension */
             pathDestino+= nombreFichero;
  							               
	   		output = new java.io.BufferedWriter(new java.io.FileWriter(pathDestino,false));
	   		output.write( contenidoString );
	   		
	   		JOptionPane optionOk= new JOptionPane(messages.getString("inventario.load.fichero.saved.ok")+ ":\n "+fc.getSelectedFile().getPath(),JOptionPane.INFORMATION_MESSAGE);
	       	 JDialog dialogOk =optionOk.createDialog(this,messages.getString("inventario.savecatalogo.title"));
	       	 dialogOk.setVisible(true);
	    	 } catch(Exception ex){
//	           ErrorDialog.show(this, "ERROR", messages.getString("inventario.load.fichero.saved.error"), StringUtil.stackTrace(ex));
	    		 logger.error(messages.getString("inventario.saveinventario.error"));
	        } finally {
	    		 try{output.close();}catch(Exception ex){}
	    	 }
	}

	private String saveCatalogo() {
		initProgressDialog(GeopistaUtil.i18n_getname(
				"inventario.savecatalogo.runDialogo.title", messages));

		return getContentOfDoc();
	}

	@Override
	protected void addEspecificComponents() {
		
	}
}

