/**
 * MetaInfoDetailScreenComposite.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.screens;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.deegree.framework.xml.XMLTools;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.japisoft.fastparser.ParseException;
import com.japisoft.fastparser.Parser;
import com.japisoft.fastparser.document.Document;
import com.japisoft.fastparser.dom.DomNodeFactory;
import com.japisoft.fastparser.dom.TextImpl;
import com.tinyline.svg.SVGMetadataElem;
import com.tinyline.svg.SVGNode;

import es.satec.localgismobile.core.Applications;
import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.global.Utils;
import es.satec.localgismobile.ui.utils.ControlsDomainFactory;
import es.satec.localgismobile.ui.utils.impl.W3CElementItemImpl;
import es.satec.localgismobile.ui.widgets.ScrolledComposite;

public class MetaInfoDetailScreenComposite extends Composite {

	private MetaInfoScreen screenMetaInfo;
	// Nodo que contiene todos los datos en xml
	private SVGNode datosCDATA = null;
	private SVGNode parentNode;
	private TabFolder tabFolder = null;
	// Documento que se genera con el parseo del xml
	private Document documentMetadata = null;
	// metadata del svgVode que nos pasan por parametros
	private SVGMetadataElem metadataLicencia = null;
	// Elemento del documento generado por xml
	private Element elementDocumentMetadata = null;
	//
	GeopistaSchema geoSchema = null;

	private Label informacion = null;

	/** Indica si es un nuevo elemento a insertar */
	private boolean newElement = false;

	private boolean editable = false;
	
	private String layerId;

	private static Logger logger = Global
			.getLoggerFor(MetaInfoDetailScreenComposite.class);

	public MetaInfoDetailScreenComposite(MetaInfoScreen screenMetaInfo,
			SVGNode nodeDatos, SVGNode parentNode,
			GeopistaSchema geoSche, boolean newElement, boolean editable, String layerId) {

		super(screenMetaInfo.getShell(), SWT.NONE);
		this.screenMetaInfo = screenMetaInfo;
		this.datosCDATA = nodeDatos;
		this.parentNode = parentNode;
		this.geoSchema = geoSche;
		this.newElement = newElement;
		this.editable = editable;
		this.layerId = layerId;
		initialize();
	}

	private void initialize() {
		setLayout(new GridLayout());

		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.verticalAlignment = GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setLayoutData(gridData1);
		// parsea el documento
		inicializarDatos();
		// Recarga el vector con los datos del xml
		cargaVector();

		// Botones
		Composite buttonsComposite = new Composite(this, SWT.NONE);
		buttonsComposite.setLayout(new RowLayout());
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.CENTER;
		gridData2.verticalAlignment = GridData.CENTER;
		buttonsComposite.setLayoutData(gridData2);

		// Si no se puede editar el componente desactivamos el boton de aceptar
		if (editable) {
			Button acceptButton = new Button(buttonsComposite, SWT.NONE);
			acceptButton.setText(Messages.getMessage("botones.aceptar"));
			acceptButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent arg0) {
					Vector erroresProducidos = (Vector) informacion.getData();
					if (erroresProducidos.size() != 0) {
						MessageBox mb = new MessageBox(LocalGISMobile
								.getMainWindow().getShell(), SWT.ICON_WARNING
								| SWT.OK);
						mb.setMessage(Messages
								.getMessage("MetaInfoDetailComposite_Error"));
						mb.open();
					} else {
						accept();
					}
				}

				public void widgetDefaultSelected(SelectionEvent arg0) {
				}
			});
		}

		Button backButton = new Button(buttonsComposite, SWT.NONE);
		backButton.setText(Messages.getMessage("botones.cancelar"));
		backButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				back();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}

	private void cargaVector() {

		NodeList listaNodosTab = elementDocumentMetadata
				.getElementsByTagName("tab");
		for (int i = 0; i < listaNodosTab.getLength(); i++) {
			/* Se obtiene la informacion correspondiente a cada tab */
			Node tab = listaNodosTab.item(i);
			if (tab.getChildNodes().getLength() > 0) {

				NamedNodeMap nameTag = tab.getAttributes();
				/* Nombre que va a llevar el Tab */
				Node nameTab = nameTag.getNamedItem("label");

				/* creacion de los datos que va a contener el tab */
				TabItem itemTab = new TabItem(tabFolder, SWT.NONE);
				itemTab.setText(nameTab.getNodeValue());
				
				NodeList listaNodosItem = tab.getChildNodes();

				GridData gridData = new GridData();
				gridData.horizontalAlignment = GridData.FILL;
				gridData.grabExcessHorizontalSpace = true;
				gridData.grabExcessVerticalSpace = true;
				gridData.verticalAlignment = GridData.FILL;				
						
				GridLayout gridLayout2 = new GridLayout();
				gridLayout2.numColumns = 3;				
				
				// Creacion del scrolledcomposite
				ScrolledComposite scrolledComposite = new ScrolledComposite(
						tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
				GridData gridData1 = new GridData();
				gridData1.grabExcessHorizontalSpace = true;
				gridData1.horizontalAlignment = GridData.FILL;
				gridData1.verticalAlignment = GridData.FILL;
				gridData1.grabExcessVerticalSpace = true;
				
				scrolledComposite.setLayoutData(gridData1);
				scrolledComposite.setLayout(new GridLayout());

				Composite compositeDatos = new Composite(scrolledComposite,
						SWT.PUSH);
				compositeDatos.setLayoutData(gridData);
				compositeDatos.setLayout(gridLayout2);

				scrolledComposite.setContent(compositeDatos);

				GridData gridData3 = new GridData();
				gridData3.horizontalSpan = 3;
				informacion = new Label(compositeDatos, SWT.WRAP);
				informacion.setData(new Vector());

				informacion.setLayoutData(gridData3);

				for (int j = 0; j < listaNodosItem.getLength(); j++) {
					/* Nombre del atributo */
					Node item = listaNodosItem.item(j);				
					NamedNodeMap nameItems = item.getAttributes();
					Node labelItem = nameItems.getNamedItem("label");
					Node editItem = nameItems.getNamedItem("edit");
					Node modifItem = nameItems.getNamedItem("modif");

					NodeList children = item.getChildNodes();
					Label label = new Label(compositeDatos, SWT.NONE);
					String etiquetaAtributo = labelItem.getNodeValue();
					if (etiquetaAtributo.indexOf(":") <= 0)
						etiquetaAtributo += ":";
					label.setText(etiquetaAtributo);

					/* Valor del atributo */
					if (item.getNodeName().equals("itemlist")) {
						GridData gridData5 = new GridData();
						gridData5.horizontalSpan = 2;
						gridData5.verticalAlignment = GridData.FILL;
						gridData5.grabExcessHorizontalSpace = true;
						gridData5.grabExcessVerticalSpace = true;
						gridData5.horizontalAlignment = GridData.FILL;
						Composite composite2 = new Composite(compositeDatos,
								SWT.NONE);
						composite2.setLayout(null);
						composite2.setLayoutData(gridData5);

						/**
						 * Creo un composite con el layout null porque asi se le
						 * puede poner un valor fijo a la lista
						 **/

						final List list = new List(composite2, SWT.BORDER
								| SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
						list.setBounds(new Rectangle(0, 0, 100, 55));
						// list.setLayoutData(gridData2);
						list.setEnabled(false);

						if (children != null) {
							if (children.getLength() != 0) {
								for (int k = 0; k < children.getLength(); k++) {
									Node nodo = children.item(k);
									NodeList itemList = nodo.getChildNodes();
									for (int n = 0; n < itemList.getLength(); n++) {
										if (!nodo.getNodeName().equals(
												"itemvalue")) {
											Node item1 = itemList.item(n);
											list.add(item1.getNodeValue());
										}
									}
								}
							}
						}
					} else {
						NodeList listHijos = item.getChildNodes();
						boolean fin = false;
						int k = 0;
						Node valorNodo = null;
						while (!fin && (k < listHijos.getLength())) {
							valorNodo = listHijos.item(k);
							if (valorNodo.getNodeType() == Node.TEXT_NODE) {
								fin = true;
							}
							k++;
						}
						
						if (valorNodo == null) {
							if(Utils.containInArray(Applications.getInstance().getApplicationByLayerId(layerId).getKeyAttribute().toArray(), item.getAttributes().getNamedItem("label").getNodeValue()))
									valorNodo = new TextImpl(datosCDATA.getValueLayertAtt(parentNode.getPosByContainsNameLayertAtt(item.getAttributes().getNamedItem("label").getNodeValue())));
							else
								valorNodo = new TextImpl("");
							item.appendChild(valorNodo);

						}
						final String nameAttr = XMLTools.getAttrValue(item,
								"name");
						final W3CElementItemImpl itemImpl = new W3CElementItemImpl(
								valorNodo, nameAttr);
						Domain domain = null;
						try {
							domain = this.geoSchema
									.getAttributeDomain(nameAttr);
						} catch (Exception e) {
						}
						if (domain != null) {
							ControlsDomainFactory.createControl(compositeDatos,
									this.geoSchema, nameAttr,
									valorNodo.getNodeValue(), editable,
									newElement, modifItem, informacion,
									itemImpl);
						} else {
							creaControlSinDominio(compositeDatos, valorNodo,
									modifItem, editItem, itemImpl);
						}

					}
				}
				itemTab.setControl(scrolledComposite);
				Point pSize = compositeDatos.computeSize(SWT.DEFAULT,
						SWT.DEFAULT);
				compositeDatos.setSize(pSize);
				scrolledComposite.setMinSize(pSize);				
			}
		}
		tabFolder.pack();

	}

	private void creaControlSinDominio(Composite compositeDatos,
			Node valorNodo, Node modifItem, Node editItem,
			final W3CElementItemImpl itemImpl) {
		GridData gridData2 = new GridData();
		gridData2.horizontalSpan = 2;
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.verticalAlignment = GridData.CENTER;
		final Node valorAtributo = valorNodo;

		/**
		 * Si el dominio es nulo se crea una campo text y se pone un foco para
		 * que almacene la informacion
		 **/
		final Text text1 = new Text(compositeDatos, SWT.BORDER);
		text1.setLayoutData(gridData2);

		// Verificamos si el elemento se puede modificar.
		if (editable) {
			if (editItem.getNodeValue().equals("false"))
				text1.setEnabled(false);
		} else
			text1.setEnabled(false);

		// Para las modificaciones no dejamos tocar determinados
		// elementos porque tendrian comportamientos erroneos.
		if (editable) {
			if (!newElement)
				if ((modifItem != null)
						&& (modifItem.getNodeValue().equals("false")))
					text1.setEnabled(false);
		} else {
			text1.setEnabled(false);
		}
		if (valorNodo.getNodeValue() != null) {
			text1.setText(valorNodo.getNodeValue());
		}
		text1.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				text1.setBackground(Display.getDefault().getSystemColor(
						SWT.COLOR_WHITE));
			}

			public void focusLost(FocusEvent e) {
				String val = text1.getText();
				System.out.println(val);
				if (valorAtributo.getNodeValue() != null) {
					if (!valorAtributo.getNodeValue().equals(
							text1.getText().trim())) {
						try {
							itemImpl.setContent(text1.getText().trim());
						} catch (Exception e1) {

							logger.error(e1);
						}
					}
				}
			}

		});
	}

	/**
	 * Metodo que almacena en un combo la lista de item que existe en el xml y
	 * almacena en el itemvalue el valor que ha sido seleccionado por el usuario
	 **/
	/*
	 * private void rellenarComboItemList(){ final Combo combo = new
	 * Combo(compositeDatos, SWT.READ_ONLY); String [] datos= new
	 * String[item.getChildNodes().getLength()-1];
	 * System.out.println(item.getChildNodes().getLength()); int
	 * posicionSeleccion = 0; String seleccion = ""; Node nodeSel= null; if
	 * (children != null) { if(children.getLength()!=0){ for (int k=0;
	 * k<children.getLength(); k++) { Node nodo=children.item(k); NodeList
	 * itemList = nodo.getChildNodes(); System.out.println(nodo.getNodeName());
	 * System.out.println(itemList.getLength()); for(int
	 * n=0;n<itemList.getLength();n++){ Node item1=itemList.item(n);
	 * if(nodo.getNodeName().equals("itemvalue")){ seleccion =
	 * item1.getNodeValue(); nodeSel = item1; } else{ datos[k] =
	 * item1.getNodeValue(); //System.out.println(item1.getNodeValue()); } }
	 * if(itemList.getLength() == 0 && nodo.getNodeName().equals("itemvalue")){
	 * nodeSel = new TextImpl(""); nodo.appendChild(nodeSel); } } } } final Node
	 * nodeSelection = nodeSel; if(seleccion!=null){ for(int k = 0;k <
	 * datos.length; k++){ if(seleccion.equals(datos[k])){ posicionSeleccion =
	 * k; } } } combo.setItems(datos); combo.select(posicionSeleccion);
	 * combo.addFocusListener(new FocusListener (){
	 * 
	 * public void focusGained(FocusEvent arg0) { }
	 * 
	 * public void focusLost(FocusEvent arg0) { final W3CElementItemImpl
	 * itemImpl = new W3CElementItemImpl(nodeSelection, "itemvalue");
	 * itemImpl.setContent(combo.getText()); }
	 * 
	 * }); }
	 */

	private void inicializarDatos() {
		metadataLicencia = (SVGMetadataElem) this.datosCDATA.children.data[0];
		String contenidoMetadata = new String(metadataLicencia.content.data);

		try {
			InputStream isMetadata = new ByteArrayInputStream(
					contenidoMetadata.getBytes());

			Parser p = new Parser();
			p.setNodeFactory(new DomNodeFactory());
			p.setInputStream(isMetadata);
			p.parse();

			this.documentMetadata = p.getDocument();

			this.elementDocumentMetadata = (Element) documentMetadata.getRoot();

		} catch (ParseException e) {

			logger.error(e);
		}
	}

	private void accept() {
		logger.debug("Estableciendo metadatos modificados");
		// Serializar el contenido del documento xml actualizado
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			documentMetadata.write(baos);
			String metadata = baos.toString();
			// logger.debug("Metadatos modificados: " + metadata);
			// System.out.println(metadata);
			// Establecer el metadata en el nodo
			datosCDATA.setMetadataContentAndRecordEvent(metadata);

			// Si es un nodo nuevo, añadirlo al arbol
			if (parentNode != null) {
				parentNode.addChildAndRecordEvent(datosCDATA, -1);
			}

			back();
		} catch (IOException e) {
			logger.error("Error al modificar los metadatos", e);
			MessageBox mb = new MessageBox(screenMetaInfo.getShell(),
					SWT.ICON_ERROR | SWT.OK);
			mb.setMessage(Messages.getMessage("MetaInfoScreen.updateError"));
			mb.open();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void back() {
		screenMetaInfo.goList();
	}
}
