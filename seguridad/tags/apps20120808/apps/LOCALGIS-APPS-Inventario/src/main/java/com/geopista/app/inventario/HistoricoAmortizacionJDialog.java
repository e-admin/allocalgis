package com.geopista.app.inventario;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import oracle.sql.DATE;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.app.inventario.panel.DatosAmortizacionJPanel;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.inventario.ConfigParameters;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.HistoricoAmortizacionesBean;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.InventarioEIELBean;
import com.geopista.protocol.inventario.ListaHistoricoAmortizaciones;

import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.GUIUtil.FileChooserWithOverwritePrompting;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class HistoricoAmortizacionJDialog extends JDialog {
	/**
 * Logger for this class
 */
private static final Logger logger = Logger.getLogger(HistoricoAmortizacionJDialog.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private  HistoricoAmortizacionTableModel  historicoAmortizacionModel;// IntegracionEIELTableModel
	
	private TableSorted sorter;
	
	private InventarioClient inventarioClient = null;
	
	private javax.swing.JTable jTableHistoricoAmortizacion; 

	private javax.swing.JPanel jPanelListaBienesAmortizables;
	
	private HistoricoAmortizacionesBean haSelected = null;
	
	private ListaHistoricoAmortizaciones listaHA = null;
	
	private javax.swing.JButton jButtonAsociar;
	private javax.swing.JButton jButtonCancelar;
	
	private javax.swing.JScrollPane listaHAJScrollPane;
	
	private ResourceBundle messages;

	private Municipio municipio;

	private AppContext aplicacion;
	private JTextArea jTextAreaDetalles=null;

	private JScrollPane scroll=null;

	private javax.swing.JButton printButton;

	private FileChooserWithOverwritePrompting fileChooser;
	

	/**
	 * Constructor de la clase
	 * 
	 * @param parent
	 *            ventana padre
	 * @param modal
	 *            indica si es modal o no
	 * @param messages
	 *            textos de la aplicación
	 */
	public HistoricoAmortizacionJDialog(java.awt.Frame parent, boolean modal,
			ResourceBundle messages,Municipio municipio) {
		super(parent, modal);
		this.messages = messages;
		this.municipio = municipio;
		this.aplicacion = (AppContext) AppContext.getApplicationContext();
		inventarioClient = new InventarioClient(
				aplicacion
						.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA)
						+ Constantes.INVENTARIO_SERVLET_NAME);
			initComponents();
			insertarHistoricoAmortizado();
			actualizarModelo();
			renombrarComponentes();


	}

	private void insertarHistoricoAmortizado() {
		try {
			listaHA=inventarioClient.insertBienesAmortizables(Integer.parseInt(AppContext.getApplicationContext().getUserPreference(Const.KEY_ANIO_AMORTIZACION,"0000",true)));
		} catch (NumberFormatException e) {
			ErrorDialog.show(this.getParent(), "ERROR",
					messages.getString("inventario.historicoamortizacion.calcularhistoricoamortizado.numbererror"),
					StringUtil.stackTrace(e));
		} catch (Exception e) {
			ErrorDialog.show(this.getParent(), "ERROR",
					messages.getString("inventario.historicoamortizacion.calcularhistoricoamortizado.error"),
					StringUtil.stackTrace(e));
		}

	}

	/**
	 * Inicializa los componentes de la ventana
	 */
	private void initComponents() {// GEN-BEGIN:initComponents
		
		
		jPanelListaBienesAmortizables = new javax.swing.JPanel();
//		jButtonAsociar = new javax.swing.JButton();
		jButtonCancelar = new javax.swing.JButton();
		
		jPanelListaBienesAmortizables.setLayout(null);
		jTableHistoricoAmortizacion = new javax.swing.JTable();
		listaHAJScrollPane = new javax.swing.JScrollPane();
		
		listaHAJScrollPane.setBounds(new Rectangle(10, 20, 860, 420));
		getJscrollPanelDetalles().setBounds(new Rectangle(8, 440, 865, 110));             
		
//		jButtonAsociar.setBounds(new Rectangle(200, 320, 85, 20));
//		jButtonAsociar.addActionListener(new java.awt.event.ActionListener() {
//			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				asociarActionPerformed();
//			}
//		});
//		jButtonAsociar.setEnabled(false);
//
		
		

		listaHAJScrollPane.setViewportView(jTableHistoricoAmortizacion);

		jPanelListaBienesAmortizables.add(listaHAJScrollPane, null);
		
    	printButton= new javax.swing.JButton("Guardar");
		printButton.setBounds(new Rectangle(690, 523, 85, 20));
//	    printButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
	    printButton.addActionListener(new java.awt.event.ActionListener(){
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                saveToFileJButtonActionPerformed();
	            }
	        });
	    jButtonCancelar= new javax.swing.JButton("Cancelar");
	    jButtonCancelar.setBounds(new Rectangle(780, 523, 85, 20));
		jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelarActionPerformed();
			}
		});
//		jPanelListaBienesAmortizables.add(jButtonAsociar,	null);
//		jPanelListaBienesAmortizables.add(jButtonCancelar, null);
		jPanelListaBienesAmortizables.add(getJscrollPanelDetalles(),	null);
	    add(printButton,BorderLayout.EAST);
	    add(jButtonCancelar,BorderLayout.EAST);

		getContentPane().setLayout(new java.awt.BorderLayout());
		getContentPane().add(jPanelListaBienesAmortizables, java.awt.BorderLayout.CENTER);
		pack();
		
		// Para seleccionar una fila
		ListSelectionModel rowSM = jTableHistoricoAmortizacion.getSelectionModel();
//		rowSM.addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent e) {
//				seleccionarEIEL(e);
//			}
//		});
	}
	
	
	private JScrollPane getJscrollPanelDetalles() {
		if(scroll==null){
			scroll = new JScrollPane();
	        add(scroll, BorderLayout.CENTER);
	        scroll.setViewportView(getJTextAreaDetalles());
	    	scroll.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.historicoamortizacion.datos.detalles.title")));
			
		}
		return scroll;
	}
	private JTextArea getJTextAreaDetalles() {
		if(jTextAreaDetalles==null){
			jTextAreaDetalles= new JTextArea();
			jTextAreaDetalles.setEditable(false);
			jTextAreaDetalles.setLineWrap(true);
		}
		return jTextAreaDetalles;
	}

	private void actualizarModelo() {
		int totalBienes=-1;
		int totalAmortizados=-1;
		
		historicoAmortizacionModel = new HistoricoAmortizacionTableModel(messages.getLocale().toString());	
		historicoAmortizacionModel.setModelData(listaHA);
		sorter = new TableSorted(historicoAmortizacionModel);
		sorter.setTableHeader(jTableHistoricoAmortizacion.getTableHeader());
		jTableHistoricoAmortizacion.setModel(sorter);
		jTableHistoricoAmortizacion.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTableHistoricoAmortizacion.setAutoscrolls(true);

		
		totalBienes=listaHA.getTotalInmueblesUrbanos();
		totalAmortizados=listaHA.getTotalAmortizadosInmueblesUrbanos();
		String [] valoresAmortizacionUrbana={ Integer.toString(totalAmortizados),"Inmuebles Urbanos", Integer.toString(totalBienes)};
		getJTextAreaDetalles().append(getStringWithParameters(messages, "inventario.historicoamortizacion.datos.detallesbienes",valoresAmortizacionUrbana)+"\n");

		totalBienes=listaHA.getTotalInmueblesRusticos();
		totalAmortizados=listaHA.getTotalAmortizadosInmueblesRusticos();
		String [] valoresAmortizacionRustica={ Integer.toString(totalAmortizados),"Inmuebles Rusticos", Integer.toString(totalBienes)};
		getJTextAreaDetalles().append(getStringWithParameters(messages, "inventario.historicoamortizacion.datos.detallesbienes",valoresAmortizacionRustica)+"\n");

		totalBienes=listaHA.getTotalMuebles();
		totalAmortizados=listaHA.getTotalAmortizadosMuebles();
		String [] valoresAmortizacionMuebles={ Integer.toString(totalAmortizados),"Muebles", Integer.toString(totalBienes)};
		getJTextAreaDetalles().append(getStringWithParameters(messages, "inventario.historicoamortizacion.datos.detallesbienes",valoresAmortizacionMuebles)+"\n");

		totalBienes=listaHA.getTotalVehiculos();
		totalAmortizados=listaHA.getTotalAmortizadosVehiculos();
		String [] valoresAmortizacionVehiculos={ Integer.toString(totalAmortizados),"Vehiculos", Integer.toString(totalBienes)};
		getJTextAreaDetalles().append(getStringWithParameters(messages, "inventario.historicoamortizacion.datos.detallesbienes",valoresAmortizacionVehiculos));

}
	   


	private void renombrarComponentes(){
        String [] anioAmort={AppContext.getApplicationContext().getUserPreference(Const.KEY_ANIO_AMORTIZACION,"0000",true)};     	        		   
    	jPanelListaBienesAmortizables.setBorder(new javax.swing.border.TitledBorder(getStringWithParameters(messages, "inventario.historicoamortizacion.datos.menu.title", anioAmort)));
//    	jButtonAsociar.setText(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.asociar", messages));
//    	jButtonCancelar.setText(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.cancelar", messages));
//    	
//		TableColumn tableColumn = jTableHistoricoAmortizacionEIEL.getColumnModel().getColumn(0);
//		tableColumn.setHeaderValue(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.lista.elemento1", messages));
//		tableColumn = jTableHistoricoAmortizacionEIEL.getColumnModel().getColumn(1);
//		tableColumn.setHeaderValue(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.lista.elemento2", messages));
//		tableColumn = jTableHistoricoAmortizacionEIEL.getColumnModel().getColumn(2);
//		tableColumn.setHeaderValue(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.lista.elemento3", messages));
//		tableColumn = jTableHistoricoAmortizacionEIEL.getColumnModel().getColumn(3);
//		tableColumn.setHeaderValue(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.lista.elemento4", messages));
//		tableColumn = jTableHistoricoAmortizacionEIEL.getColumnModel().getColumn(4);
//		tableColumn.setHeaderValue(GeopistaUtil.i18n_getname("inventario.datosEIEL.menu.lista.elemento5", messages));		
    }
	
//	private void asociarActionPerformed() {
//		
//		eielSelected.setEpigInventario(Integer.parseInt(jComboBoxEpigInventario.getSelectedPatron()));
//		eielSelected.setIdBien(((Inventario) jComboBoxNumInventario.getSelectedItem()).getId());
//		try {
//			inventarioClient.returnInsertIntegEIELInventario(eielSelected);
//			jComboBoxEpigInventario.setSelectedIndex(0);
//			inicializaListaEIEL();
//			actualizarModelo();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	/**
	 * Función que se ejecuta al salir
	 */
	protected void cancelarActionPerformed() {
		dispose();
	}
	
/**
	 * Devuelve un String del resourceBundle con parametros
	 * 
	 * @param key
	 * @param valores
	 * @return
	 */
	protected String getStringWithParameters(ResourceBundle messages,
			String key, Object[] valores) {
		try {
			MessageFormat messageForm = new MessageFormat("");
			messageForm.setLocale(messages.getLocale());
			String pattern = messages.getString(key);
			messageForm.applyPattern(pattern);
			return messageForm.format(valores, new StringBuffer(), null)
					.toString();
		} catch (Exception ex) {
			logger.error("Excepción al recoger el recurso:" + key, ex);
			return "undefined";
		}
	}
	
	private void saveToFileJButtonActionPerformed() {
		if (abrirJFileChooser() == JFileChooser.APPROVE_OPTION){
			
    		final String path=getDirectorySelected();
    		if(path!=null && !path.equals("")){
//    			setVisible(false);               		
				final TaskMonitorDialog progressDialog= new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
				progressDialog.setTitle("TaskMonitorDialog.Wait");
				progressDialog.addComponentListener(new ComponentAdapter()
				{
					public void componentShown(ComponentEvent e)
					{
						new Thread(new Runnable()
						{
							public void run()
							{
								
								try
								{       
//									setVisible(false);
//									dispose();
									progressDialog.report("Guardando...");
									Map reportMap = null;
									HSSFWorkbook objWB = null;
									try{
										reportMap = getMap();
										objWB =writeToExcel(reportMap);
								
										FileOutputStream file=new FileOutputStream(path);
										BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(file);
										objWB.write(bufferedOutputStream);
										bufferedOutputStream.flush();
									
										if(bufferedOutputStream != null) { 
											try {
												bufferedOutputStream.close(); 
												
											}catch(Exception e) {
											//No se muestra nada
											}
										} 
										
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										logger.error("Error al generar Excel ",e1);
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										logger.error("Error al generar Excel ",e1);
									} 
				         			
				         			
								}
								catch(Exception e)
								{
									logger.error("Error ", e);
									ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
									return;
								}
								finally
								{
									progressDialog.setVisible(false);                                
									progressDialog.dispose();	
									setVisible(false);
									dispose();
								}
							}			
						}).start();
					}
				});
				GUIUtil.centreOnWindow(progressDialog);
				progressDialog.setVisible(true);

				show();
            	}   
  		}	
		
	}
	
	public int abrirJFileChooser(){
		 return getFileChooser().showSaveDialog(this);
	}
	private JFileChooser getFileChooser() {
        if (fileChooser == null) {
        	 fileChooser = new GUIUtil.FileChooserWithOverwritePrompting();
             fileChooser.setDialogTitle(I18N.get("LocalGISEIEL", "inventario.historicoamortizacion.save.amortizaciones"));
            GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
            filter.addExtension("xls");
            filter.setDescription("Xls");
    		fileChooser.setFileHidingEnabled(false);
    		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(filter);	 
			
			fileChooser.setSelectedFile(new File(File.separator+"amortizaciones"+"_"+AppContext.getApplicationContext().getUserPreference(Const.KEY_ANIO_AMORTIZACION,"0000",false)+".xls"));
        }
        File currentDirectory = (File) aplicacion.getBlackboard().get(Const.LAST_IMPORT_DIRECTORY_AMORT);
        fileChooser.setCurrentDirectory(currentDirectory);
        return (JFileChooser) fileChooser;
    }
	
	 public String getDirectorySelected() {
		 File file=getFileChooser().getSelectedFile();
		 String	 directory=file.getAbsolutePath();
		aplicacion.getBlackboard().put(Const.LAST_IMPORT_DIRECTORY_AMORT,file);
			 		 	 
		return directory;
	}
	 
	 
	private Map getMap() {
		Map reportMap= new HashMap();
		List cabecera= new ArrayList();
		for (int j = 0; j < historicoAmortizacionModel.getColumnCount(); j++) {
			cabecera.add(j,historicoAmortizacionModel.getColumnName(j));	
		}
		
		reportMap.put("cabecera",cabecera);
		
		for (int i = 0; i < historicoAmortizacionModel.getDataVector().size(); i++) {
			Vector element= (Vector) historicoAmortizacionModel.getDataVector().get(i);
			reportMap.put("fila"+i,element);
		} 
		
		
		int totalBienes = listaHA.getTotalInmueblesUrbanos();
		int totalAmortizados = listaHA.getTotalAmortizadosInmueblesUrbanos();
		String [] valoresAmortizacionUrbana={ Integer.toString(totalAmortizados),"Inmuebles Urbanos", Integer.toString(totalBienes)};
		reportMap.put("resumen1",getStringWithParameters(messages, "inventario.historicoamortizacion.datos.detallesbienes",valoresAmortizacionUrbana));

		totalBienes=listaHA.getTotalInmueblesRusticos();
		totalAmortizados=listaHA.getTotalAmortizadosInmueblesRusticos();
		String [] valoresAmortizacionRustica={ Integer.toString(totalAmortizados),"Inmuebles Rusticos", Integer.toString(totalBienes)};
		reportMap.put("resumen2",getStringWithParameters(messages, "inventario.historicoamortizacion.datos.detallesbienes",valoresAmortizacionRustica));

		totalBienes=listaHA.getTotalMuebles();
		totalAmortizados=listaHA.getTotalAmortizadosMuebles();
		String [] valoresAmortizacionMuebles={ Integer.toString(totalAmortizados),"Muebles", Integer.toString(totalBienes)};
		reportMap.put("resumen3",getStringWithParameters(messages, "inventario.historicoamortizacion.datos.detallesbienes",valoresAmortizacionMuebles));

		totalBienes=listaHA.getTotalVehiculos();
		totalAmortizados=listaHA.getTotalAmortizadosVehiculos();
		String [] valoresAmortizacionVehiculos={ Integer.toString(totalAmortizados),"Vehiculos", Integer.toString(totalBienes)};
		reportMap.put("resumen4",getStringWithParameters(messages, "inventario.historicoamortizacion.datos.detallesbienes",valoresAmortizacionVehiculos));

		
		return reportMap;
	}
	
	 private HSSFWorkbook writeToExcel(Map reportMap){
			// Proceso la información y genero el xls.

			HSSFWorkbook objWB = new HSSFWorkbook();
			
			// Creo la hoja

			HSSFSheet hoja1 = objWB.createSheet("hoja 1");
			
			// creo la fila para la cabecera.
			HSSFRow fila = hoja1.createRow((short)0);
			
			// Aunque no es necesario podemos establecer estilos a las celdas.
			// Primero, establecemos el tipo de fuente
			HSSFFont fuente = objWB.createFont();
			fuente.setFontHeightInPoints((short)11);
			fuente.setFontName(fuente.FONT_ARIAL);
			fuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// Luego creamos el objeto que se encargará de aplicar el estilo a la celda
			HSSFCellStyle estiloCelda = objWB.createCellStyle();
			estiloCelda.setWrapText(true);
			estiloCelda.setAlignment(HSSFCellStyle. ALIGN_JUSTIFY);
			estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			estiloCelda.setFont(fuente);

			// También, podemos establecer bordes...
			estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			estiloCelda.setBottomBorderColor((short)8);
			estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			estiloCelda.setLeftBorderColor((short)8);
			estiloCelda.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			estiloCelda.setRightBorderColor((short)8);
			estiloCelda.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			estiloCelda.setTopBorderColor((short)8);

			// Establecemos el tipo de sombreado de nuestra celda
			estiloCelda.setFillForegroundColor((short)22);
			estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			// Creamos la celda, aplicamos el estilo y definimos
			// el tipo de dato que contendrá la celda de cabecera

			// Finalmente, establecemos el valor de la celda de cabecera.
			// El valor será el nombre de los campos de las tablas
			List al1 = new ArrayList();
			al1 = (List)reportMap.get("cabecera");
			logger.debug("Escribiendo la cabecera del Excel "+ al1.toString());
			int n = al1.size();
			for (int i =0; i<n;i++){
				HSSFCell celda = fila.createCell(i);
				celda.setCellStyle(estiloCelda);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.getSheet().setDefaultColumnWidth(15);
				celda.setCellValue((String)al1.get(i));
			}
			
			Vector al2 = new Vector();
			int filaIndex = 0;
			while (reportMap.get("fila"+filaIndex) != null){
				fila = hoja1.createRow((short)filaIndex+1);
				al2 = (Vector)reportMap.get("fila"+(filaIndex));
				logger.debug("Escribiendo la fila "+ filaIndex +" del Excel "+al2.toString());
				for (int i =0; i<n;i++){
					HSSFCell celda = fila.createCell(i);
					celda.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (al2.get(i) instanceof String)
						celda.setCellValue((String)al2.get(i));
					else if (al2.get(i) instanceof Long)
						celda.setCellValue((Long)al2.get(i));
					else if (al2.get(i) instanceof Double)
						celda.setCellValue((Double)al2.get(i));
					else if (al2.get(i) instanceof Date){
						celda.setCellValue(((Date)al2.get(i)).toString());
					}
				}
				filaIndex++;
			}
			
			// creo la fila para la resumen.
			filaIndex=filaIndex+5;
			fila = hoja1.createRow((short)filaIndex);
			HSSFCell celda = fila.createCell(0);
			celda.setCellStyle(estiloCelda);
			celda.setCellType(HSSFCell.CELL_TYPE_STRING);
			celda.setCellValue("Resumen");
			filaIndex++;
			
			int k = 1;			
			while (reportMap.get("resumen"+k) != null){
				fila = hoja1.createRow((short)filaIndex);
				String resumen = (String)reportMap.get("resumen"+(k));
				logger.debug("Escribiendo la fila "+ filaIndex +" del Excel "+resumen.toString());
				celda = fila.createCell(0);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue((String)resumen);	
				k++;
				filaIndex++;
			}
			
			return objWB;
			
		}
}
