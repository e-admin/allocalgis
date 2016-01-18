/**
 * MetaInfoListScreenComposite.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.tinyline.svg.SVGChangeEvent;
import com.tinyline.svg.SVGGroupElem;
import com.tinyline.svg.SVGNode;

import es.satec.localgismobile.core.Applications;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.global.Constants;
import es.satec.localgismobile.global.Utils;
import es.satec.svgviewer.localgis.MetaInfo;

public class MetaInfoListScreenComposite extends Composite {

	private static final String ATT_NUM_BIENES = "Número de Bienes";
	private MetaInfoScreen screenMetaInfo;
	private SVGNode currentNode;
	private MetaInfo metaInfo;
	private Hashtable params;
	
	private Table table;
	private HashMap buttons;

	private static final String ACTION_BUTTON_ADD = "ADD";
	private static final String ACTION_BUTTON_UPDATE = "UPDATE";
	private static final String ACTION_BUTTON_DELETE = "DELETE";
	private static final String ACTION_BUTTON_VIEW= "VIEW";
	//private static final String ACTION_BUTTON_ADD_FROM_LIST = "ADD_FROM_LIST";
	
	private boolean editable=false;
	
	private static Logger logger = Global.getLoggerFor(MetaInfoListScreenComposite.class);

	public MetaInfoListScreenComposite(MetaInfoScreen screenMetaInfo, SVGNode currentNode, MetaInfo metaInfo, Hashtable params) {
		super(screenMetaInfo.getShell(), SWT.NONE);
		this.screenMetaInfo = screenMetaInfo;
		this.currentNode = currentNode;
		this.metaInfo = metaInfo;
		this.params = params;
		this.editable=currentNode.parent.editable;
		this.buttons = new HashMap();
		initialize();
	}

	private void initialize() {
		setLayout(new GridLayout());
		
		String titleKey = (String) params.get("title");
		if (titleKey != null) {
			String title = Messages.getMessage(titleKey);
			Label titleLabel = new Label(this, SWT.NONE);
			titleLabel.setText(title);
			screenMetaInfo.getShell().setText(title);
		}

		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table.setHeaderVisible(true);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		table.setLayoutData(gridData);
		
		loadTable();

		Composite buttonsComposite = new Composite(this, SWT.NONE);
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.CENTER;
		gridData2.verticalAlignment = GridData.CENTER;
		buttonsComposite.setLayoutData(gridData2);
		RowLayout rowLayout = new RowLayout();
		buttonsComposite.setLayout(rowLayout);
		
		loadButtons(buttonsComposite);
	}
	
	private HashMap getCurrentIdFeature(){	
		
		return currentNode.getValueLayertAtt(currentNode.parent.getPosByNameLayertAtt(metaInfo.getKeyAttribute()));

	}


	private void loadTable() {
		table.removeAll();

		// Obtener nodos con la metainformacion
		Vector metaInfoNodes = null;
		try {
			metaInfoNodes = metaInfo.getElementsByIdFeature(getCurrentIdFeature());
			SVGNode parent = metaInfo.getMetaInfoLayerElement();
						
			String layerId = ((SVGGroupElem)parent).getSystemId();
			if (parent != null && parent.nameAtts != null) {
				
				;
				
				ArrayList visibleAttrs = new ArrayList();
				
				if (table.getColumnCount() == 0) {
					Enumeration e = parent.nameAtts.elements();
					while (e.hasMoreElements()) {
						String columnName = (String) e.nextElement();
						//Iterator it = metaInfo.getKeyAttribute().iterator();
						//while(it.hasNext()){							
							//if(columnName.equals((String)it.next())){
							if(Utils.isInArray(Constants.TIPOS_EIEL, layerId) && !Utils.isInArray(Applications.getInstance().getApplicationByLayerId(layerId).getKeyAttribute().toArray(),columnName)){
								visibleAttrs.add(Boolean.valueOf(false));
							}
							else{ 
								TableColumn column = new TableColumn(table, SWT.NONE);
								column.setText(columnName);
								column.pack();
								visibleAttrs.add(Boolean.valueOf(true));
							}
					}
				}
				
				if (metaInfoNodes != null) {
					Enumeration e = metaInfoNodes.elements();
					while (e.hasMoreElements()) {
						SVGNode metaInfoNode = (SVGNode) e.nextElement();
						// Filtrar los marcados para eliminar
						if (metaInfoNode.changeEvent != null &&
							(metaInfoNode.changeEvent.getChangeType() & SVGChangeEvent.CHANGE_TYPE_DELETED) != 0)
							continue;
						TableItem tableItem = new TableItem(table, SWT.CENTER);
						tableItem.setData(metaInfoNode);
						Iterator it = visibleAttrs.iterator();
						int j=0;
						for (int i=0; i<metaInfoNode.nameAtts.size(); i++) {							
							if(visibleAttrs.get(i).equals(Boolean.valueOf(true))){
								tableItem.setText(j, (String) metaInfoNode.nameAtts.elementAt(i));
								j++;
							}							
						}
						//for (int i=0; i<metaInfoNode.nameAtts.size(); i++) {
							//if(idFeature.get(i)!=null)						
							//	tableItem.setText(i, (String) metaInfoNode.nameAtts.elementAt(i));
							//else 
							//	tableItem.setText(i, "");
						//}						
					}
					
					if(Utils.isInArray(Constants.TIPOS_INVENTARIO, layerId)){
						//en inventario de patrimonio para el repintado del nodo según el número de bienes
						int numAttBienes = currentNode.parent.getPosByNameLayertAtt(ATT_NUM_BIENES);
						if(numAttBienes>=0){
							String metadataSize = String.valueOf(table.getItemCount());
							currentNode.setExtendedAttributeAndRecordEvent(numAttBienes, metadataSize);
							//para evitar que se envíe el nodo
							currentNode.changeEvent = null;
							//para inventario de calles propagamos la información del nodo para el resto de nodos del mismo grupo
							int featGroup = currentNode.getGroup();
							if(featGroup!=-1){
								Object[] nodeList = currentNode.parent.children.data;
								SVGNode nodeChild = null;
								Vector nameAtts = currentNode.nameAtts;
								for (int i = 0; i < nodeList.length; i++) {
									nodeChild = (SVGNode) nodeList[i];
									if(nodeChild!=null && !currentNode.equals(nodeChild) && nodeChild.getGroup()==featGroup){
										//igualamos cada nodo a los del nodo actual
										for (int j = 0; j < nameAtts.size(); j++) {
											try {
												nodeChild.setExtendedAttributeAndRecordEvent(j, (String) nameAtts.get(j));
											} catch (Exception e2) {
												logger.error("Error al igualar el nodo " + currentNode + " :" +e2, e2);
											}
										}		
										nodeChild.changeEvent = null;
									}
								}
							}
						}
					}
					
				}
			}
			
			
		} catch (Exception e) {
			logger.error("Error al obtener los nodos de metainformacion", e);
		}
	}
	
	
	private void loadButtons(Composite buttonsComposite){
		String actions = (String) params.get("actions");
		if (actions != null) {
			//try {
				if ((actions.indexOf(ACTION_BUTTON_ADD) != -1) && (editable)){
					Button addButton = new Button(buttonsComposite, SWT.NONE);
					//if(Utils.isInArray(Constants.TIPOS_EIEL, ((SVGGroupElem)metaInfo.getMetaInfoLayerElement()).getSystemId()) && metaInfo.getElementsByIdFeature(getCurrentIdFeature()).size()>0) 
					//	addButton.setEnabled(false);
					addButton.setText(Messages.getMessage("MetaInfoScreen.buttons.add"));
					addButton.addSelectionListener(new SelectionListener() {
						public void widgetSelected(SelectionEvent arg0) {							
							addItem();							
						}

						public void widgetDefaultSelected(SelectionEvent arg0) {
						}
					});		
					 buttons.put(ACTION_BUTTON_ADD,addButton);
				}
			//} catch (IOException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
//			if ((actions.indexOf(ACTION_BUTTON_ADD_FROM_LIST) != -1) && (editable)) {
//				Button addFromListButton = new Button(buttonsComposite, SWT.NONE);
//				addFromListButton.setText(Messages.getMessage("MetaInfoScreen.buttons.addFromList"));
//				addFromListButton.addSelectionListener(new SelectionListener() {
//					public void widgetSelected(SelectionEvent arg0) {
//						addItemFromList();
//					}
//
//					public void widgetDefaultSelected(SelectionEvent arg0) {
//					}
//				});
//			}
			if ((actions.indexOf(ACTION_BUTTON_UPDATE) != -1) && (editable)) {
				Button updateButton = new Button(buttonsComposite, SWT.NONE);
				updateButton.setText(Messages.getMessage("MetaInfoScreen.buttons.update"));
				updateButton.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent arg0) {
						updateItem();
					}

					public void widgetDefaultSelected(SelectionEvent arg0) {
					}
				});
				buttons.put(ACTION_BUTTON_UPDATE,updateButton);
			}
			if (actions.indexOf(ACTION_BUTTON_VIEW) != -1) {
				Button viewButton = new Button(buttonsComposite, SWT.NONE);
				viewButton.setText(Messages.getMessage("MetaInfoScreen.buttons.view"));
				viewButton.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent arg0) {
						viewItem();
					}

					public void widgetDefaultSelected(SelectionEvent arg0) {
					}
				});
				buttons.put(ACTION_BUTTON_VIEW,viewButton);
			}
			if ((actions.indexOf(ACTION_BUTTON_DELETE) != -1) && (editable)) {
				Button deleteButton = new Button(buttonsComposite, SWT.NONE);
				deleteButton.setText(Messages.getMessage("MetaInfoScreen.buttons.delete"));
				deleteButton.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent arg0) {
						deleteItem();
					}

					public void widgetDefaultSelected(SelectionEvent arg0) {
					}
				});
				buttons.put(ACTION_BUTTON_DELETE,deleteButton);
			}
		}	
		
		updateButtonsState();
	}
	
	private void updateButtonsState(){		
		try {
			String layerId = ((SVGGroupElem)metaInfo.getMetaInfoLayerElement()).getSystemId();				
			if(Utils.isInArray(Constants.TIPOS_EIEL, layerId)){
				if(buttons.get(ACTION_BUTTON_ADD)!=null){
					if(metaInfo.getElementsByIdFeature(getCurrentIdFeature()).size()>0)				
						((Control) buttons.get(ACTION_BUTTON_ADD)).setEnabled(false);	
					else
						((Control) buttons.get(ACTION_BUTTON_ADD)).setEnabled(true);	
				}
			}
		} catch (IOException e) {
			logger.error("Error al crear nuevo elemento", e);
		}
	}

	private void addItem() {
		try {
				SVGNode prototype = metaInfo.getPrototypeElement();
				// Clonar el prototipo con sus metadatos
				SVGNode newElem = prototype.copyExtendedNode();
				// Copiar la geometria
				currentNode.copyGeometryTo(newElem);
	
				// Rellenar el id de la feature
	//			int pos = currentNode.parent.getPosByNameLayertAtt(metaInfo.getKeyAttribute());
	//			String idFeature = (String) currentNode.nameAtts.elementAt(pos);			
				ArrayList pos = currentNode.parent.getPosByNameLayertAtt(metaInfo.getKeyAttribute());
				HashMap idFeatures = currentNode.getValueLayertAtt(pos);
				
				pos = prototype.parent.getPosByNameLayertAtt(metaInfo.getKeyAttribute());
				Iterator it = idFeatures.keySet().iterator();
				while(it.hasNext()){
					Integer key = (Integer) it.next();
					newElem.nameAtts.setElementAt(idFeatures.get(key), key.intValue());
				}
				screenMetaInfo.goDetail(newElem, prototype.parent,true,true);
				
		} catch (Exception e) {
			logger.error("Error al crear nuevo elemento", e);
			MessageBox mb = new MessageBox(screenMetaInfo.getShell(), SWT.ICON_ERROR | SWT.OK);
			mb.setMessage(Messages.getMessage("MetaInfoScreen.errorAdd"));
			mb.open();
		}
	}
	
//	private void addItemFromList() {
//		try {
//			System.out.println("PRUEBA AÑADIR DE UNA LISTA");
////			SVGNode prototype = metaInfo.getPrototypeElement();
////			// Clonar el prototipo con sus metadatos
////			SVGNode newElem = prototype.copyExtendedNode();
////			// Copiar la geometria
////			currentNode.copyGeometryTo(newElem);
////
////			// Rellenar el id de la feature
////			int pos = currentNode.parent.getPosByNameLayertAtt(metaInfo.getKeyAttribute());
////			String idFeature = (String) currentNode.nameAtts.elementAt(pos);
////			pos = prototype.parent.getPosByNameLayertAtt(metaInfo.getKeyAttribute());
////			newElem.nameAtts.setElementAt(idFeature, pos);
////			
////			screenMetaInfo.goDetail(newElem, prototype.parent,true,true);
//		} catch (Exception e) {
//			logger.error("Error al crear nuevo elemento", e);
//			MessageBox mb = new MessageBox(screenMetaInfo.getShell(), SWT.ICON_ERROR | SWT.OK);
//			mb.setMessage(Messages.getMessage("MetaInfoScreen.errorAdd"));
//			mb.open();
//		}
//	}
	
	private void updateItem() {
		if (table.getSelectionCount() == 0) {
			MessageBox mb = new MessageBox(screenMetaInfo.getShell(), SWT.ICON_WARNING | SWT.OK);
			mb.setMessage(Messages.getMessage("MetaInfoScreen.noSelection"));
			mb.open();
		}
		else {
			SVGNode selectedNode = (SVGNode) table.getSelection()[0].getData();
			screenMetaInfo.goDetail(selectedNode, null,false,true);
		}
	}
	private void viewItem() {
		if (table.getSelectionCount() == 0) {
			MessageBox mb = new MessageBox(screenMetaInfo.getShell(), SWT.ICON_WARNING | SWT.OK);
			mb.setMessage(Messages.getMessage("MetaInfoScreen.noSelection"));
			mb.open();
		}
		else {
			SVGNode selectedNode = (SVGNode) table.getSelection()[0].getData();
			screenMetaInfo.goDetail(selectedNode, null,false,false);
		}
	}
	
	private void deleteItem() {
		if (table.getSelectionCount() == 0) {
			MessageBox mb = new MessageBox(screenMetaInfo.getShell(), SWT.ICON_WARNING | SWT.OK);
			mb.setMessage(Messages.getMessage("MetaInfoScreen.noSelection"));
			mb.open();
		}
		else {
			SVGNode selectedNode = (SVGNode) table.getSelection()[0].getData();
			SVGNode parent = selectedNode.parent;
			if (parent != null) {
				int pos = parent.children.indexOf(selectedNode, 0);
				if (pos != -1) {
					parent.removeChildAndRecordEvent(pos);
					loadTable();
					updateButtonsState();
				}
			}
		}
	}
}
