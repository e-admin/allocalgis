package com.geopista.ui.dialogs.licencias;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.geopista.app.licencias.CConstantesLicencias_LCGIII;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.CreditoDerechoBean;
import com.geopista.protocol.inventario.DerechoRealBean;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.MuebleBean;
import com.geopista.protocol.inventario.SemovienteBean;
import com.geopista.protocol.inventario.ValorMobiliarioBean;
import com.geopista.protocol.inventario.VehiculoBean;
import com.geopista.protocol.inventario.ViaBean;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.actividad.DatosActividad;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.dialogs.global.FilesUtils;

/**
 * Clase de gestión de ficheros de licencias
 * @author irodriguez
 *
 */
public class LicenseFilesUtils extends FilesUtils{

	private static Logger logger = Logger.getLogger(LicenseFilesUtils.class);
	
	public static final String filesParentPath = "/licenses_mb_files";
	public static String licObraMayorSkeletonFile = filesParentPath + "/lic_obra_mayor_skeleton.xml";
	public static String licObraMenorSkeletonFile = filesParentPath + "/lic_obra_menor_skeleton.xml";
	public static String licActividadSkeletonFile = filesParentPath + "/lic_actividad_skeleton.xml";
	public static String invParcelasSkeletonFile = filesParentPath + "/inv_parcelas_skeleton.xml";
	public static String invViasSkeletonFile = filesParentPath + "/inv_vias_skeleton.xml";
	public static String actContaminantesSkeletonFile = filesParentPath + "/act_contaminantes_skeleton.xml";
	
	private String licObraMayorSkeletonStr;
	private String licObraMenorSkeletonStr;
	private String licActividadSkeletonStr;
	private String invParcelasSkeletonStr;
	private String invViasSkeletonStr;
	private String actContaminantesSkeletonStr;

	//literales para traducir los ficheros de licencias
	private ResourceBundle literalesLicObra;
	private ResourceBundle literalesLicActividad;
	private ResourceBundle literalesInvPatrimonio;
	private ResourceBundle literalesActividadesContaminantes;
	
	public LicenseFilesUtils(String locale){
		initResourceBundle(locale);
		licObraMayorSkeletonStr = getLicenseI18NStr(licObraMayorSkeletonFile);
		licObraMenorSkeletonStr = getLicenseI18NStr(licObraMenorSkeletonFile);
		licActividadSkeletonStr = getLicenseI18NStr(licActividadSkeletonFile);
		invParcelasSkeletonStr = getLicenseI18NStr(invParcelasSkeletonFile);
		invViasSkeletonStr = getLicenseI18NStr(invViasSkeletonFile);
		actContaminantesSkeletonStr = getLicenseI18NStr(actContaminantesSkeletonFile);
	}
	
	/**
	 * Crea los ficheros de licencias necesarios para la pda
	 * @param dirBaseMake 
	 * @param numFichLic 
	 * @param capasMetadata 
	 * @param hashSvgCellsHeader 
	 */
	public List<File> createLicensesFilesSkeleton(List<File> ficherosSVG, int numFichLic, List<String> capasMetadata, 
			HashMap<File, String> hashSvgCellsHeader, Map<String, List<LicenseMetadataSVG>> parseLicencias){
		Iterator<File> fileIt = ficherosSVG.iterator();
		File licenseFile = null;
		File svgCellFile = null;
		List<File> listaFicherosLicenciasCreados = new ArrayList<File>();
		String fileContent = null;
		FileOutputStream fOut = null;
		String metadataLayerName = null;
		String licenseFileName = null;
		String licenseSkeletonFileName = null;
		LicenseMetadataSVG licSVG = null;
		while (fileIt.hasNext()) {
			svgCellFile = (File) fileIt.next();
			licenseFileName = svgCellFile.getAbsolutePath();
			
			//por cada fichero del tipo XlicObrasY crearemos XlicObras0, XlicObras1, XlicObras2, ... XlicObrasNumFichLic
			for (int i = 0; i < numFichLic; i++) {
				for (int j = 0; j < capasMetadata.size(); j++) {
					metadataLayerName = capasMetadata.get(j);
					licenseSkeletonFileName = licenseFileName.substring(0, licenseFileName.length()-4) + metadataLayerName 
								+ "_meta" + i + ".svg"; //creamos los nuevos
					licenseFile = new File(licenseSkeletonFileName);
					if(!licenseFile.exists()){
						try {
							//creación del skeleton
							licSVG = searchElement(parseLicencias, metadataLayerName);							
							if (licSVG!=null){
								licenseFile.createNewFile();
								fileContent = createSkeleton(hashSvgCellsHeader.get(svgCellFile), licSVG.getGrupo(), licSVG.getPath(), locateSkeletonStr(licSVG.getNombreMetadato()));
								fOut = new FileOutputStream(licenseFile);
								fOut.write(fileContent.getBytes("UTF-8"));
								fOut.close();
							}
							else{
								licenseFile.createNewFile();
								fOut = new FileOutputStream(licenseFile);
								fOut.close();
							}
							//añadimos el fichero a la lista de creados
							listaFicherosLicenciasCreados.add(licenseFile);

							
						} catch (IOException e) {
							logger.error("No se ha podido crear el fichero de licencias: " + licenseFile);
						}
					}
				}
			}
		}
		
		return listaFicherosLicenciasCreados;
	}
	
	private LicenseMetadataSVG searchElement(Map<String, List<LicenseMetadataSVG>> parseElement, String metadataLayerName) {
		for (Iterator iterator = parseElement.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			if(key.contains(metadataLayerName)){
				return parseElement.get(key).get(0);
			}			
		}
		return null;
	}

	/**
	 * Rellena los ficheros de licencias
	 * @param parseLicencias
	 * @param dirBaseMake
	 * @param locale 
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public void fillLicenseFiles(Map<String, List<LicenseMetadataSVG>> parseLicencias, File dirBaseMake) {
		try {			
			Iterator<String> keyIt = parseLicencias.keySet().iterator();
			String ficheroLicencias = null;
			File f = null;
			List<LicenseMetadataSVG> listLicencias = null;
			while (keyIt.hasNext()) {
				ficheroLicencias = (String) keyIt.next();
				//si esta vacío metemos el eskeleton
				f = new File(dirBaseMake, ficheroLicencias);
				listLicencias = parseLicencias.get(ficheroLicencias);
				fillXmlFile(f, listLicencias);
			}			
			
		}catch (Exception e) {
			logger.error(e,e);
		}
	}
	
	/**
	 * Añade el skeleton y el contenido al fichero de licencias
	 * @param f
	 * @param listLicencias
	 * @param locale 
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	private void fillXmlFile(File f, List<LicenseMetadataSVG> listLicencias) throws JDOMException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String fileContent = "";
		
		if(listLicencias==null || listLicencias.size()==0){return;}
		
//		//creación del skeleton
//		if(f.exists() && f.length()==0){
//			fileContent = createSkeleton(listLicencias.get(0));
//		}
		
		LicenseMetadataSVG licenciaSVG = null;
		InputStream skeletonInput = null;
		int i=0;
		String path = "";
		List<BienBean> inventarios = null;
		BienBean bienBean = null;
		CExpedienteLicencia licencia = null;
		for (Iterator iterator = listLicencias.iterator(); iterator.hasNext(); i++) {
			skeletonInput = locateSkeletonInput(listLicencias.get(i).getNombreMetadato());
			licenciaSVG = (LicenseMetadataSVG) iterator.next();
			path = licenciaSVG.getPath();
			
			//licencias
			licencia = licenciaSVG.getExpLicencia();
			if(licencia!=null){
				fileContent += path.replace("/>", ">") + "\n";
				fileContent += fillFeatureSkeleton(licencia, skeletonInput);
				if(path.toLowerCase().startsWith("<path")){
					fileContent += "</path>\n";
				}else if(path.toLowerCase().startsWith("<polyline")){
					fileContent += "</polyline>\n";
				}else if(path.toLowerCase().startsWith("<line")){
					fileContent += "</line>\n";
				}else if(path.toLowerCase().startsWith("<point")){
					fileContent += "</point>\n";
				}else if(path.toLowerCase().startsWith("<circle")){
					fileContent += "</circle>\n";
				}
				skeletonInput.reset();
			}
			
			//cada uno de los inventarios
			inventarios = licenciaSVG.getInventarios();
			for (Iterator iterator2 = inventarios.iterator(); iterator2.hasNext();) {
				bienBean = (BienBean) iterator2.next();
				fileContent += path.replace("/>", ">") + "\n";
				fileContent += fillFeatureSkeleton(bienBean, skeletonInput);
				if(path.toLowerCase().startsWith("<path")){
					fileContent += "</path>\n";
				}else if(path.toLowerCase().startsWith("<polyline")){
					fileContent += "</polyline>\n";
				}else if(path.toLowerCase().startsWith("<line")){
					fileContent += "</line>\n";
				}else if(path.toLowerCase().startsWith("<point")){
					fileContent += "</point>\n";
				}else if(path.toLowerCase().startsWith("<circle")){
					fileContent += "</circle>\n";
				}
				skeletonInput.reset();
			}			
		}
		fileContent += "</g></svg>";
		
		InputStream fin = null;
		InputStreamReader inread = null;
		BufferedReader buffread = null;
		OutputStream fout = null;
		try {
			fin = new FileInputStream(f);
			inread = new InputStreamReader(fin, "UTF-8");
			buffread = new BufferedReader(inread);
			String strLine = null;
			String finalStr = "";
			while((strLine = buffread.readLine()) != null){
				if(strLine.equals("</g></svg>")){
					break;
				}
				finalStr += strLine + "\n";
			}
			finalStr += fileContent;
			
			fout = new FileOutputStream(f);
			fout.write(finalStr.getBytes("UTF-8"));
			
		}catch (Exception e) {
			logger.error(e,e);
		}finally {
			try{
				buffread.close();
				inread.close();
				fin.close();
				fout.close();
			}catch (Exception e) {}
		}
	}

//	/**
//	 * Borra las etiquetas que sobran para construir el fichero de licencias
//	 * @param fileContent
//	 * @return
//	 */
//	private String deleteBadTags(String fileContent) {
//		String fileContentAux = fileContent;
//		fileContentAux = fileContentAux.replaceAll("<skeleton>", "");
//		fileContentAux = fileContentAux.replaceAll("</skeleton>", "");
//		return fileContentAux;
//	}

	/**
	 * Devuelve la cadena correspondiente a rellenar el skeleton para la licencia solicitada
	 * @param licenciaSVG
	 * @param skeletonInput
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	private String fillFeatureSkeleton(Object licOInv, InputStream skeletonInput) throws JDOMException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(skeletonInput);
		Element rootElement = doc.getRootElement();	
		List children = rootElement.getChildren();
		Element elem = null;
		List items = null;
		List itemLists = null;
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			elem = (Element) iterator.next();
			items = elem.getChildren(Constants.TAG_ITEM);
			addDBValues(licOInv, items);
			itemLists = elem.getChildren(Constants.TAG_ITEMLIST);
			addDBValues(licOInv, itemLists);
		}		
		
		String str = "<metadata>\n<![CDATA[\n";
		str+=printDoc(doc);
		str+="]]>\n</metadata>\n";
		return str;
	}
	
	/**
	 * Rellena la información con los valores
	 * @param licOInv
	 * @param items
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	private void addDBValues(Object licOInv, List items) {
		Element elem = null;
		String strMethod = null;
		CExpedienteLicencia expLicencia = null;
		CSolicitudLicencia solLicencia = null;
		String parentClassID = null;
		DatosActividad datosAct = null;
		BienBean inventario = null;
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			elem = (Element) iterator.next();
			strMethod = "get"+elem.getAttribute(Constants.ATT_REFLECTMETHOD).getValue();
			if(strMethod!=null && strMethod.length()>0){
				parentClassID = elem.getParentElement().getAttribute(Constants.ATT_CLASSID).getValue();
				if(licOInv instanceof CExpedienteLicencia){
					expLicencia = (CExpedienteLicencia) licOInv;
					if(parentClassID.equals(Constants.CLASSID_EXPEDIENTE)){
						reflectXmlMethod(elem, strMethod, expLicencia);
					}
					else if(parentClassID.equals(Constants.CLASSID_SOLICITUD)){
						solLicencia = expLicencia.getSolicitud();
						reflectXmlMethod(elem, strMethod, solLicencia);
					}
					else if(parentClassID.equals(Constants.CLASSID_ACTIVIDAD)){
						solLicencia = expLicencia.getSolicitud();
						if(solLicencia!=null){
							datosAct = solLicencia.getDatosActividad();
							reflectXmlMethod(elem, strMethod, datosAct);
						}
					}
				}
				else if(licOInv instanceof BienBean){
					if(parentClassID.equals(Constants.CLASSID_INVENTARIO)){
						inventario = (BienBean) licOInv;
						reflectXmlMethod(elem, strMethod, inventario);
					}
					if(parentClassID.equals(Constants.CLASSID_INVENTARIO_INMUEBLE)){
						if(licOInv instanceof InmuebleBean)
							reflectXmlMethod(elem, strMethod, licOInv);
					}
					if(parentClassID.equals(Constants.CLASSID_INVENTARIO_SEMOVIENTE)){
						if(licOInv instanceof SemovienteBean)
							reflectXmlMethod(elem, strMethod, licOInv);
					}
					if(parentClassID.equals(Constants.CLASSID_INVENTARIO_SEMOVIENTE)){
						if(licOInv instanceof VehiculoBean)
							reflectXmlMethod(elem, strMethod, licOInv);
					}
					if(parentClassID.equals(Constants.CLASSID_INVENTARIO_VALORMOBILIARIO)){
						if(licOInv instanceof ValorMobiliarioBean)
							reflectXmlMethod(elem, strMethod, licOInv);
					}
					if(parentClassID.equals(Constants.CLASSID_INVENTARIO_DERECHOREAL)){
						if(licOInv instanceof DerechoRealBean)
							reflectXmlMethod(elem, strMethod, licOInv);
					}
					if(parentClassID.equals(Constants.CLASSID_INVENTARIO_CREDITODERECHO)){
						if(licOInv instanceof CreditoDerechoBean)
							reflectXmlMethod(elem, strMethod, licOInv);
					}
					if(parentClassID.equals(Constants.CLASSID_INVENTARIO_MUEBLE)){
						if(licOInv instanceof MuebleBean)
							reflectXmlMethod(elem, strMethod, licOInv);
					}
					if(parentClassID.equals(Constants.CLASSID_INVENTARIO_VIA)){
						if(licOInv instanceof ViaBean)
							reflectXmlMethod(elem, strMethod, licOInv);
					}
										
											
				}
			}
		}
	}

	/**
	 * Mete en un nodo XML el resultado de ejecutar por reflexion un método sobre un objeto
	 * @param itemValue
	 * @param elem
	 * @param strMethod
	 * @param objectInvoke
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void reflectXmlMethod(Element elem, String strMethod, Object objectInvoke)  {
		try {
			if(objectInvoke==null){return;}
			Method method = objectInvoke.getClass().getMethod(strMethod, new Class[0]);
			String itemValue = null;
			List<String> itemListValues = null;
			String strValue = null;
			if(method!=null){
				if(elem.getName().toLowerCase().equals(Constants.TAG_ITEM)){
					itemValue = (String) method.invoke(objectInvoke);
					elem.setText(itemValue);
				}
				else if(elem.getName().toLowerCase().equals(Constants.TAG_ITEMLIST)){
					itemListValues = (List<String>) method.invoke(objectInvoke);
					for (Iterator iterator = itemListValues.iterator(); iterator.hasNext();) {
						strValue = (String) iterator.next();
						elem.addContent(new Element(Constants.TAG_SUBITEM).setText(strValue));
					}
				}
			}
		}catch (Exception e) {
			logger.error("No se ha podido acceder al método "+objectInvoke.getClass()+"." + strMethod, e);
		}
	}
	
	/**
	 * Localiza el skeleton correspondiente para un nombre de licencia determinado
	 * @param nombreLicencia
	 * @return
	 */
	private String locateSkeletonStr(String nombreLicencia){
		if(nombreLicencia.equals(Constants.LIC_OBRA_MAYOR)){
			return licObraMayorSkeletonStr;
		}
		else if(nombreLicencia.equals(Constants.LIC_OBRA_MENOR)){
			return licObraMenorSkeletonStr;
		}
		else if(nombreLicencia.equals(Constants.LIC_ACTIVIDAD)){
			return licActividadSkeletonStr;
		}
		else if(nombreLicencia.equals(Constants.INV_PARCELAS)){
			return invParcelasSkeletonStr;
		}
		else if(nombreLicencia.equals(Constants.INV_VIAS)){
			return invViasSkeletonStr;
		}
		else if(nombreLicencia.equals(Constants.ACTIVIDADES_CONTAMINANTES)){
			return actContaminantesSkeletonStr;
		}
		
		return null;
	}
	
	/**
	 * Localiza el skeleton correspondiente para un nombre de licencia determinado
	 * @param nombreLicencia
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public InputStream locateSkeletonInput(String nombreLicencia) throws UnsupportedEncodingException{
		if(nombreLicencia.equals(Constants.LIC_OBRA_MAYOR)){
			return new ByteArrayInputStream(licObraMayorSkeletonStr.getBytes("UTF-8"));
		}
		else if(nombreLicencia.equals(Constants.LIC_OBRA_MENOR)){
			return new ByteArrayInputStream(licObraMenorSkeletonStr.getBytes("UTF-8"));
		}
		else if(nombreLicencia.equals(Constants.LIC_ACTIVIDAD)){
			return new ByteArrayInputStream(licActividadSkeletonStr.getBytes("UTF-8"));
		}
		else if(nombreLicencia.equals(Constants.INV_PARCELAS)){
			return new ByteArrayInputStream(invParcelasSkeletonStr.getBytes("UTF-8"));
		}
		else if(nombreLicencia.equals(Constants.INV_VIAS)){
			return new ByteArrayInputStream(invViasSkeletonStr.getBytes("UTF-8"));
		}
		
		return null;
	}
	
	/** 
	 * Lee un fichero y devuelve un String con el skeleton ya internacionalizado
	 * @param f
	 * @return
	 */
	private String getLicenseI18NStr(String filePath){
		InputStream skFileIn = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			skFileIn = this.getClass().getResourceAsStream(filePath);
			Document doc = builder.build(skFileIn);
			Element rootElement = doc.getRootElement();
			List children = rootElement.getChildren();
			i18NElementList(children); //internacionalización de tab
			Element elem = null;
			List items = null;
			List itemLists = null;
			for (Iterator iterator = children.iterator(); iterator.hasNext();) {
				elem = (Element) iterator.next();
				items = elem.getChildren(Constants.TAG_ITEM); //internacionalización de item
				i18NElementList(items);
				itemLists = elem.getChildren(Constants.TAG_ITEMLIST); //internacionalización de itemlist
				i18NElementList(itemLists);
			}		
			
			String docStr = printDoc(doc);
			
//			InputStream in = new FileInputStream(f);
//			byte[] b = new byte[in.available()];
//			in.read(b);
//			in.close();
//			String res =  new String(b);
//			res = res.replace("<xml>", "");
//			res = res.replace("</xml>", "");
			return docStr;
		}catch (Exception e) {
			logger.error(e,e);
		}finally {
			if(skFileIn!=null){
				try { skFileIn.close(); } catch (IOException e) {}
			}
		}
		
		return null;
	}
	
	/**
	 * Internacionaliza una lista de elementos
	 * @param items
	 * @param bundle
	 */
	private void i18NElementList(List items) {
		Element elem = null;
		String i18nStr = null;
		Attribute attribute = null;
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			elem = (Element) iterator.next();
			attribute = elem.getAttribute(Constants.ATT_I18NKEY);
			if(attribute!=null){
				i18nStr = "";
				if(literalesLicObra.containsKey(attribute.getValue())){
					i18nStr = literalesLicObra.getString(attribute.getValue());
				}
				else if(literalesLicActividad.containsKey(attribute.getValue())){
					i18nStr = literalesLicActividad.getString(attribute.getValue());
				}
				else if(literalesInvPatrimonio.containsKey(attribute.getValue())){
					i18nStr = literalesInvPatrimonio.getString(attribute.getValue());
				}
				
				elem.setAttribute(Constants.ATT_LABEL, i18nStr);
				elem.removeAttribute(Constants.ATT_I18NKEY);
			}
		}
	}

	/**
	 * Iniciliza los resources para el idioma
	 * @param locale
	 * @param licenseFileName
	 * @return
	 */
	private void initResourceBundle(String locale) {
        try
        {
			   try {
            	   literalesLicObra  = ResourceBundle.getBundle("config.licenciasObraMayor", new Locale(locale));
               }catch (Exception e)
               {
                   locale=CConstantesLicencias_LCGIII.LocalCastellano;
                   literalesLicObra = ResourceBundle.getBundle("config.licenciasObraMayor", new Locale(locale));
               }
          	
               try {
                   literalesLicActividad = ResourceBundle.getBundle("config.licenciasActividad", new Locale(locale));
               }catch (Exception e){
                   locale=CConstantesLicencias_LCGIII.LocalCastellano;
                   literalesLicActividad = ResourceBundle.getBundle("config.licenciasActividad", new Locale(locale));
               }
               
               try {
                   literalesInvPatrimonio = ResourceBundle.getBundle("GeoPistai18n", new Locale(locale));
               }catch (Exception e){
                   locale=CConstantesLicencias_LCGIII.LocalCastellano;
                   literalesInvPatrimonio = ResourceBundle.getBundle("GeoPistai18n", new Locale(locale));
               }
           
        } catch (Exception e) {
            logger.error("No se encuentra un resource para el locale: " + locale);
        } 
	}
	
}
