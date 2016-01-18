/**
 * ControlsDomainFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Node;

import com.geopista.feature.Attribute;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;

import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.ui.utils.impl.ItemNode;
//import org.eclipse.swt.custom.StyledText;

public class ControlsDomainFactory {
	
	private static Logger logger = Global.getLoggerFor(ControlsDomainFactory.class);
	private static final int MAX_LENGTH=60;
	
	static HashMap controls;
	
	public ControlsDomainFactory(){		
	}
	
	public ControlsDomainFactory(HashMap controls){
		this.controls=controls;
	}
	
	static void creaSinDominio (Composite composite, String valorAtributo, Control controlDevolver) {
		
		Text text1 = new Text(composite, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.CENTER;
		text1.setLayoutData(gridData);
		if(valorAtributo!=null){
			text1.setText(valorAtributo);
		}
		//text1.setFont(font2);
		text1.setEnabled(false);
		controlDevolver=text1;
	}
	
	static public Control createControl(Composite composite, 
            GeopistaSchema geoSche, 
            final String nombreAtributo, 
            final String valorAtributo, 
            boolean editable, 
            final Label informacion, 
            final ItemNode nodeTarget){
		return createControl(composite,geoSche,nombreAtributo,valorAtributo,editable,true,null,informacion,nodeTarget);
	}
	
	static public Control createControl(Composite composite, 
			                            final GeopistaSchema geoSche, 
			                            final String nombreAtributo, 
			                            final String valorAtributo, 
			                            boolean editable, 
			                            boolean newElement,
			                            Node modifItem,
			                            final Label informacion, 
			                            final ItemNode nodeTarget){
		ArrayList listaHijos=null;
			
		Control controlDevolver = null;
		if (geoSche == null){
			creaSinDominio(composite, valorAtributo, controlDevolver);
			return controlDevolver;
		}
		
		// trapi por usar los Inner 
		
		final Domain domain = geoSche.getAttributeDomain(nombreAtributo);
		if(domain == null){
			//OJO si el campo no tiene dominio. Lo dejamos en no editable. Importante.
			creaSinDominio(composite, valorAtributo, controlDevolver);
		}
		else{
			final Attribute atrib=geoSche.getAttribute(nombreAtributo);
			String aceso=geoSche.getAttributeAccess(nombreAtributo);
			
			/*Cuando el dominio es tipo cadena o número*/
			if(campoText(atrib)){
				if(domain.getClass().getName().equals("com.geopista.feature.CodeBookDomain") || domain.getClass().getName().equals("com.geopista.feature.TreeDomain")){
					//ASO 30-03-2010 saltamos de linea para tener mas espacio
					new Label(composite, SWT.READ_ONLY);
					listaHijos=domain.getChildren();
					final Combo combo = new Combo(composite, SWT.READ_ONLY);
					GridData gridData = new GridData();
					gridData.horizontalSpan = 2;
					gridData.horizontalAlignment = GridData.FILL;
					gridData.verticalAlignment = GridData.CENTER;
					combo.setLayoutData(gridData);
			        //ASO 30-03-2010 volvemos a saltar para que quede bien
					new Label(composite, SWT.READ_ONLY);					
					
					combo.addFocusListener(new FocusListener (){

						public void focusGained(FocusEvent arg0) {
							informacion.setText("          ");
							combo.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							
						}

						public void focusLost(FocusEvent arg0) {
							
							if(!domain.validate( null, nombreAtributo, nombreCombo(combo.getText(), domain))){
								informacion.setText(Messages.getMessage(domain.getLastError()));
								informacion.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
								datosAddErrorInformacion(informacion, nombreAtributo);
								combo.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
							}
							else{
								datosDeleteErrorInformacion(informacion, nombreAtributo);
								ArrayList listaHijos=domain.getChildren();
								Iterator j = listaHijos.iterator();
								String [] datos= new String[listaHijos.size()];
								int r=0;
						        while (j.hasNext()) {
						        	Domain domi=(Domain) j.next();
						            datos[r]=getDescripcionBonita(domi.getDescription());
						            if(getDescripcionBonita(domi.getDescription()).equals(combo.getText())){
						            	if(valorAtributo!=null){
											if(!valorAtributo.equals(combo.getText().trim())){
												try {
													//nodeAtributo.setExtendedAttributeAndRecordEvent(nodeAtributo.getPosByNameLayertAtt(nombreAtributo), domi.getName());
													nodeTarget.setContent(domi.getName());	
								            		//Utils.fillEielControlsINE(informacion, nombreAtributo, controls, geoSche, "CA", false);
												} catch (Exception e) {
													
													logger.error(e);
												}
											}
						            	}
						            	else{
						            		nodeTarget.setContent(domi.getName());
						            		//Utils.fillEielControlsINE(informacion, nombreAtributo, controls, geoSche, "CA", false);
						            	}
						            }
						            r+=1;						            
						        }						      
							}
						}

						
					});

					Iterator j = listaHijos.iterator();
					//NUEVO (Quitamos el null)
					//String [] datos= new String[listaHijos.size()+1];					
					//datos[0]="";
					//int r=1;					
					String [] datos= new String[listaHijos.size()];
					int r=0;
					//FIN NUEVO
					int pos=0;
			        while (j.hasNext()) {
			        	Domain domi=(Domain) j.next();
			        	datos[r]=getDescripcionBonita(domi.getDescription());;
			            if(valorAtributo!=null){
				            if(domi.getName().equals(valorAtributo)){
				            	pos=r;
				            }
			            }
			            r+=1; 
			        }
			        //NUEVO
			        if(datos.length>0)
			        //FIN NUEVO
			        	combo.setItems(datos);		        	
			        
			        if(valorAtributo!=null){
				        combo.select(pos);
				        //if(!editable || atrib.getAccessType().equals(GeopistaSchema.READ_ONLY)){
				        if(!editable){
				        	combo.setEnabled(false);
						}
				        if (!newElement)
							if ((modifItem!=null) && (modifItem.getNodeValue().equals("false")))
								combo.setEnabled(false);
						
			        }
			        setSelectectedComboItem(domain, valorAtributo, nodeTarget, combo.getText());
			        controlDevolver=combo;
					
				}
				else{
					if(domain.getClass().getName().equals("com.geopista.feature.AutoFieldDomain")){
						final Text text1 = new Text(composite, SWT.BORDER);
						GridData gridData = new GridData();
						gridData.horizontalSpan = 2;
						gridData.horizontalAlignment = GridData.FILL;
						gridData.verticalAlignment = GridData.CENTER;
						text1.setLayoutData(gridData);
						if(valorAtributo!=null){
							if(!valorAtributo.equals("null")){
								text1.setText(valorAtributo);
							}
						}
						text1.setEnabled(false);
					}
					else{
						//Tipo de Datos TextArea. Ver como se puede mejorar.
						/*StringDomain domainString=(StringDomain)domain;
						if (domainString.getMaxLength()>5000){
							final StyledText text1 = new StyledText(composite, SWT.BORDER);							
							GridData gridData = new GridData();
							gridData.horizontalSpan = 2;
							gridData.horizontalAlignment = GridData.FILL;
							gridData.verticalAlignment = GridData.CENTER;
							text1.setLayoutData(gridData);
							//text1.setText("PRUEBA dfsdf fs \n\nsdfsdfsd fsd");
;							controlDevolver=text1;
						}
						else{*/
							//Font font2 = new Font(LocalGISMobile.getMainWindow().getShell().getDisplay(), "Arial", 7, SWT.NORMAL);
							final Text text1 = new Text(composite, SWT.BORDER);
							GridData gridData = new GridData();
							gridData.horizontalSpan = 2;
							gridData.horizontalAlignment = GridData.FILL;
							gridData.verticalAlignment = GridData.CENTER;
							text1.setLayoutData(gridData);
							if(valorAtributo!=null){
								if(!domain.validate(null, nombreAtributo, valorAtributo)){
									informacion.setText(domain.getLastError());
									informacion.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
									text1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
									datosAddErrorInformacion(informacion, nombreAtributo);
								}
								else{
									text1.setText(valorAtributo);
									//Utils.fillEielControlsClave(informacion, nombreAtributo, controls, geoSche, "CA", false);
								}
							}
							//text1.setFont(font2);
							if(atrib.getAccessType().equals(GeopistaSchema.READ_ONLY))
								text1.setEnabled(false);
							else if(valorAtributo!=null){
								if(!editable){
									text1.setEnabled(false);
								}
							}
							
							/*Para que se pongan los campos en rojo al aparecer la ventana sin ningún dato, obligando a 
							 * insertar los datos que no pueden ser null*/
							if(valorAtributo==null){
								if(!domain.isNullable()){
									if(text1.getText().equals("")){
										informacion.setText(Messages.getMessage("CreateElementInfoScreen_ErrorNulo"));
										informacion.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
										text1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
										datosAddErrorInformacion(informacion, nombreAtributo);
									}
									else{
										datosDeleteErrorInformacion(informacion, nombreAtributo);
										informacion.setText("                      ");
									}
								}
								//Compruebo si el valor esta correctamente escrito
								if(atrib.getType().equals("INTEGER")){
									try{
										Integer.parseInt(text1.getText());
										informacion.setText("                      ");
										datosDeleteErrorInformacion(informacion, nombreAtributo);
									}
									catch(NumberFormatException e1){
										informacion.setText(Messages.getMessage("CreateElementInfoScreen_ErrorString"));
										informacion.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
										text1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
										datosAddErrorInformacion(informacion, nombreAtributo);
									}
								}
							}
							text1.addFocusListener(new FocusListener(){
		
								public void focusGained(FocusEvent e) {
		
									//System.out.println("focusGained");
									//System.out.println(text1.getText());
									text1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
									
									
								}
		
								public void focusLost(FocusEvent e) {
									if(!domain.validate(null, nombreAtributo, text1.getText())){
										informacion.setText(Messages.getMessage(domain.getLastError()));
										informacion.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
										text1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
										datosAddErrorInformacion(informacion, nombreAtributo);
									}
									else{
										datosDeleteErrorInformacion(informacion, nombreAtributo);
										informacion.setText("                      ");
										if(valorAtributo!=null){
											if(!valorAtributo.equals(text1.getText().trim())){
												try {											
													nodeTarget.setContent( text1.getText().trim());
													//Utils.fillEielControlsClave(informacion, nombreAtributo, controls, geoSche, "CA", false);
												} catch (Exception e1) {
			
													logger.error(e1);
												}
											}
										}
										else{
											System.out.println(text1.getText().trim());
											nodeTarget.setContent( text1.getText().trim());
											//Utils.fillEielControlsClave(informacion, nombreAtributo, controls, geoSche, "CA", false);
										}
									}	
								}
								
							});
							if(aceso.equals("R")){
								text1.setEditable(false);
							}
							if(aceso.equals("NA")){
								text1.setEnabled(false);
								text1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
								informacion.setData("Error", "true");
		
							}
							controlDevolver=text1;
						
						
					} //Fin-else StringDomain
				}
			}
			
			/**Cuando el dominio es de tipo date**/
			if(atrib.getType().equals("DATE")){
				//Font font2 = new Font(LocalGISMobile.getMainWindow().getShell().getDisplay(), "Arial", 7, SWT.NORMAL);
				final Text textDate = new Text(composite,SWT.BORDER);
				textDate.setEditable(false);
				
				Button open = new Button (composite, SWT.PUSH);
				open.setText (Messages.getMessage("ControlsDomainFactory.Cambiar"));
				if(valorAtributo!=null){
					if(!editable){
						open.setEnabled(false);
					}
				}
				
				open.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
				
     			final String fecha= valorAtributo;
     			if(valorAtributo!= null){
     				textDate.setText(valorAtributo);
     				//open.setText (valorAtributo);
     			}
				if(valorAtributo!= null && !fecha.equals("")){
					open.addSelectionListener (new SelectionAdapter () {
						public void widgetSelected (SelectionEvent e) {
							String valorFecha = textDate.getText();
							final Shell dialog = new Shell (Display.getCurrent().getActiveShell(), SWT.APPLICATION_MODAL);
							dialog.setLayout (new GridLayout (2, false));
							
							GridData gridData = new GridData();
							gridData.horizontalSpan = 2;
							final DateTime calendar = new DateTime (dialog, SWT.CALENDAR | SWT.BORDER);
							calendar.setLayoutData(gridData);
							final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							try {
								Date date = sdf.parse(valorFecha);
								Calendar cal = Calendar.getInstance();
								cal.setTime(date);
								calendar.setYear(cal.get(Calendar.YEAR));
								calendar.setMonth(cal.get(Calendar.MONTH));
								calendar.setDay(cal.get(Calendar.DAY_OF_MONTH));
							} catch (Exception ex) {
								logger.error("Formato de fecha incorrecto", ex);
								textDate.setText("");
							}
							finally{
								new Label (dialog, SWT.NONE);
								new Label (dialog, SWT.NONE);
								
								GridData gridData1 = new GridData();
								gridData1.horizontalAlignment = GridData.CENTER;
								gridData1.grabExcessHorizontalSpace = true;
								gridData1.verticalAlignment = GridData.CENTER;
								Button ok = new Button (dialog, SWT.PUSH);
								ok.setText ("OK");
								ok.setLayoutData(gridData1);
								ok.addSelectionListener (new SelectionAdapter () {
									public void widgetSelected (SelectionEvent e) {
										Calendar cal = Calendar.getInstance();
										cal.set(calendar.getYear(), calendar.getMonth(), calendar.getDay());
										String dateFin = sdf.format(cal.getTime());
										System.out.println(dateFin);
										textDate.setText(dateFin);
										if(valorAtributo!=null){
											if(!valorAtributo.equals(dateFin)){
												try {											
													nodeTarget.setContent( dateFin);
												} catch (Exception e1) {
			
													logger.error(e1);
												}
											}
										}
										else{
											nodeTarget.setContent(dateFin);
										}
										dialog.close ();
									}
								});
								
								Button cancel = new Button (dialog, SWT.PUSH);
								cancel.setText (Messages.getMessage("botones.cancelar"));
								cancel.setLayoutData(gridData1);
								cancel.addSelectionListener (new SelectionAdapter () {
									public void widgetSelected (SelectionEvent e) {
										dialog.close ();
									}
								});
								
								
								dialog.pack ();
								dialog.open ();
								
							}
						}
					});
				}
				else{
					open.setText ("     "+Messages.getMessage("InfoScreen_Fecha")+"    ");
					if(!editable){
						open.setEnabled(false);
					}
					open.addSelectionListener (new SelectionAdapter () {
						public void widgetSelected (SelectionEvent e) {
							try{
								final Shell dialog = new Shell (Display.getCurrent().getActiveShell(), SWT.APPLICATION_MODAL);
								dialog.setLayout (new GridLayout (1, false));
	
								final DateTime calendar = new DateTime (dialog, SWT.CALENDAR | SWT.BORDER);
								new Label (dialog, SWT.NONE);
								new Label (dialog, SWT.NONE);
								Button ok = new Button (dialog, SWT.PUSH);
								ok.setText (Messages.getMessage("botones.aceptar"));
								ok.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
								ok.addSelectionListener (new SelectionAdapter () {
									public void widgetSelected (SelectionEvent e) {
										Calendar cal = Calendar.getInstance();
										cal.set(calendar.getYear(), calendar.getMonth(), calendar.getDay());
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
										String dateFin = sdf.format(cal.getTime());
										textDate.setText(dateFin);
										if(valorAtributo!=null){
											if(!valorAtributo.equals(dateFin)){
												try {											
													nodeTarget.setContent( dateFin);
												} catch (Exception e1) {
			
													logger.error(e1);
												}
											}
										}
										dialog.close ();
									}
								});
								
								Button cancel = new Button (dialog, SWT.PUSH);
								cancel.setText (Messages.getMessage("botones.cancelar"));
								cancel.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
								cancel.addSelectionListener (new SelectionAdapter () {
									public void widgetSelected (SelectionEvent e) {
										dialog.close ();
									}
								});
								
								dialog.pack ();
								dialog.open ();
							}catch (NumberFormatException ne){
								logger.error(ne);
								MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
								mb.setMessage(Messages.getMessage("ControlsDomainFactory.errorFecha"));
								mb.open();
							}
							catch(Exception e1){
								logger.error(e1);
							}
						}
					});
				}
				
				controlDevolver=open;    
			}
			if(atrib.getType().equals("TIMESTAMP")){
				
			}
			if(atrib.getType().equals("BOOLEAN")){
				final Combo combo = new Combo(composite, SWT.READ_ONLY);
				GridData gridData = new GridData();
				gridData.horizontalSpan = 2;
				gridData.horizontalAlignment = GridData.FILL;
				gridData.verticalAlignment = GridData.CENTER;
				combo.setLayoutData(gridData);
				String [] datos= {"Si", "No"};
				combo.setItems(datos);

				if(valorAtributo!=null){
					if(valorAtributo.equals("0")){
						 combo.select(1);
					}
					else{
						 combo.select(0);
					}
				}
				combo.addFocusListener(new FocusListener (){

					public void focusGained(FocusEvent arg0) {
						
						
					}

					public void focusLost(FocusEvent arg0) {
						if(combo.getText().equals("Si")){
							try {
								nodeTarget.setContent( "1");
							} catch (Exception e) {
								logger.error(e);
							}
						}
						else{
							try {
								nodeTarget.setContent("0");
							} catch (Exception e) {
								logger.error(e);
							}
						}
						
					}
				});
			}
			//}
			//shell.setSize(shell.getSize().x, shell.getSize().y+20);
		}
		return controlDevolver;
	}
	
	/**Agrega el atributo cuando se ha producido un error**/
	private static void datosAddErrorInformacion(Label informacion, String nombreAtributo) {
		Vector res = (Vector) informacion.getData();
		if(res != null){
			if(!res.contains(nombreAtributo)){
				res.add(nombreAtributo);
			}
		}
	}
	/**Elimina el atributo ya que se ha arreglado el error**/
	public static void datosDeleteErrorInformacion(Label informacion, String nombreAtributo) {
		Vector res = (Vector) informacion.getData();
		if(res != null){
			if(res.contains(nombreAtributo)){
				res.remove(nombreAtributo);
			}
		}
	}
	
	private static boolean campoText(Attribute atrib) {

		if(atrib.getType().equals("BYTEA") || atrib.getType().equals("CHAR") || atrib.getType().equals("VARCHAR") || atrib.getType().equals("NUMERIC") || atrib.getType().equals("INTEGER") || atrib.getType().equals("STRING") || atrib.getType().equals("DOUBLE")){
			return true;
		}
		else{
			return false;
		}
		
	}
	
	private static String nombreCombo(String textCombo, Domain domain) {
		String nameCombo = "";
		
		ArrayList listaHijos=domain.getChildren();
		Iterator j = listaHijos.iterator();
		String [] datos= new String[listaHijos.size()];
		int r=0;
        while (j.hasNext()) {
        	Domain domi=(Domain) j.next();
            datos[r]=domi.getDescription();
            if(domi.getDescription().equals(textCombo)){
            	nameCombo = domi.getName();
            }
        }
		return nameCombo;
	}
	
	 /**
	 * Devuelve la descripcion con un tamaño máximo para que no
	 * se agrande la pantalla en la PDA
	 * @param descripcion Descripcion que viene en el dominio
	 * @return el valor de la descripción
	 */
	private static String getDescripcionBonita(String descripcion){		
    	if (descripcion.length()>MAX_LENGTH)
    		return descripcion=descripcion.substring(0, MAX_LENGTH-2)+"..";
    	return descripcion;
	}
	
	private static void setSelectectedComboItem(Domain domain, String valorAtributo, ItemNode nodeTarget, String comboText){
		ArrayList listaHijos=domain.getChildren();
		Iterator j = listaHijos.iterator();
		String [] datos= new String[listaHijos.size()];
		int r=0;
        while (j.hasNext()) {
        	Domain domi=(Domain) j.next();
            datos[r]=getDescripcionBonita(domi.getDescription());
            if(getDescripcionBonita(domi.getDescription()).equals(comboText)){
            	if(valorAtributo!=null){
					if(!valorAtributo.equals(comboText.trim())){
						try {
							//nodeAtributo.setExtendedAttributeAndRecordEvent(nodeAtributo.getPosByNameLayertAtt(nombreAtributo), domi.getName());
							nodeTarget.setContent(domi.getName());	
		            		//Utils.fillEielControlsINE(informacion, nombreAtributo, controls, geoSche, "CA", false);
						} catch (Exception e) {							
							//logger.error(e);
						}
					}
            	}
            	else{
            		nodeTarget.setContent(domi.getName());
            		//Utils.fillEielControlsINE(informacion, nombreAtributo, controls, geoSche, "CA", false);
            	}
            }
            r+=1;						            
        }
	}
	
}
