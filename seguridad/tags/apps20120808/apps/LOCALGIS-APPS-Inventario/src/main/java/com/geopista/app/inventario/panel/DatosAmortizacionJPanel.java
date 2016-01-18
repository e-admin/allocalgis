package com.geopista.app.inventario.panel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.inventario.HistoricoAmortizacionBienTableModel;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.CuentaAmortizacion;
import com.geopista.protocol.inventario.CuentaContable;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.MuebleBean;
import com.geopista.protocol.inventario.VehiculoBean;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 31-jul-2006
 * Time: 9:48:15
 * To change this template use File | Settings | File Templates.
 */
public class DatosAmortizacionJPanel extends JPanel{
	
	public static final String POR_PORCENTAJE = "Por Porcentaje";
	public static final String POR_AÑOS = "Por Años";
	public static final String POR_NADA = "Ninguno";


	/**
	 * 
	 */
	
	static Logger logger= Logger.getLogger(DatosAmortizacionJPanel.class);
	
	private static final long serialVersionUID = 1L;
	private AppContext aplicacion;
    private String locale;
    private static javax.swing.JFrame desktop;
    private CuentaJDialog cuentaJDialog;
    private CuentaContable cuentaContable;
    private CuentaAmortizacion cuentaAmortizacion;
    private static InventarioClient inventarioClient = null;
	private Date fechaAdquisicion;
	private double costeAdquisicion;
	private javax.swing.JScrollPane listaHAJScrollPane;
	private long idBien;

    /**
     * Método que genera el panel de los datos de Amortizacion de un bien inmueble
     */
    public DatosAmortizacionJPanel(JFrame desktop, String locale) throws Exception {
        this.locale= locale;
        this.desktop= desktop;
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() throws Exception{
        cuentaContableJLabel = new InventarioLabel();
        cuentaAmortizacionJLabel = new InventarioLabel();
        tipoAmortizacionJLabel = new InventarioLabel();
        annosJLabel = new InventarioLabel();
        porcentajeJLabel = new InventarioLabel();
        totalAmortizadoJLabel = new InventarioLabel();
        planAmortizacionJLabel=new InventarioLabel();
        inventarioClient= new InventarioClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
        		Constantes.INVENTARIO_SERVLET_NAME);


        initCContableCBox(inventarioClient.getCuentasContables());
        initCAmortizacionCBox(inventarioClient.getCuentasAmortizacion());

        cuentaContableJTField= new InventarioTextField();
        cuentaContableJTField.setEnabled(false);

        cuentaAmortizacionJTField= new InventarioTextField();
        cuentaAmortizacionJTField.setEnabled(false);

        annosJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Long(999), true, 2);
        annosJTField.setSignAllowed(false);
        porcentajeJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(99), true, 2);
        porcentajeJTField.setSignAllowed(false);
        totalAmortizadoJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        totalAmortizadoJTField.setSignAllowed(false);

		tipoAmortizacionEJCBox = new ComboBoxEstructuras(
				Estructuras.getListaTipoAmortizacion(), null, locale, true);
		tipoAmortizacionEJCBox
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
							;
							if(isShowing()){
								try {
						    		if (fechaAdquisicion != null && costeAdquisicion > 0) {
						    			totalAmortizadoJTField.setNumber(calcularTotalAmortizado(null,fechaAdquisicion, costeAdquisicion,cuentaAmortizacion,tipoAmortizacionEJCBox.getSelectedItem().toString()));
						    		}else if(fechaAdquisicion == null){
						    			JOptionPane.showMessageDialog(desktop,"No se puede calcular la Amortización porque falta la Fecha de Adquisición","Cálculo Erróneo",JOptionPane.ERROR_MESSAGE);
						    		}
						    		else if (costeAdquisicion <= 0) {
						    			JOptionPane.showMessageDialog(desktop,"No se puede calcular la Amortización porque falta el Valor de Adquisición","Cálculo Erróneo",JOptionPane.ERROR_MESSAGE);
						    		}
								} catch (Exception e) {
	
									e.printStackTrace();
								}
							}
					}
				});
		
		jTableHistoricoAmortizacion = new javax.swing.JTable();

		listaHAJScrollPane = new javax.swing.JScrollPane();
//		listaHAJScrollPane.setBounds(new Rectangle(10, 20, 150, 40));
		listaHAJScrollPane.setViewportView(jTableHistoricoAmortizacion);
		
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(cuentaContableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 20));
        add(cuentaAmortizacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 110, 20));
        add(tipoAmortizacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 110, 20));
        add(annosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 110, 20));
        add(porcentajeJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 110, 20));
        add(totalAmortizadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 110, 20));

        add(cuentaContableJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 310, -1));
//        add(cuentaContableJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 20, 20));
        add(cuentaContableJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 310, -1));

        add(cuentaAmortizacionJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 310, -1));
//        add(cuentaAmortizacionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 20, 20));
        add(cuentaAmortizacionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 310, -1));

        add(tipoAmortizacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 160, -1));
        add(annosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 160, -1));
        add(porcentajeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 160, -1));
        add(totalAmortizadoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 160, -1));
        add(planAmortizacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 150, 20));
        add(listaHAJScrollPane,new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 440, 300));
        

    }

//    private void abrirDialogoJButtonActionPerformed(final int tipo) {
//        if (tipo==CuentaJDialog.CUENTA_CONTABLE){
//            cuentaJDialog= new CuentaJDialog(desktop, aplicacion.getI18nString("inventario.cuentaJDialog.tag4"),
//                                             aplicacion.getI18nString("inventario.datosAmortizacion.tag2"), CuentaJDialog.CUENTA_CONTABLE);
//        }else if (tipo==CuentaJDialog.CUENTA_AMORTIZACION){
//            cuentaJDialog= new CuentaJDialog(desktop, aplicacion.getI18nString("inventario.cuentaJDialog.tag5"),
//                                             aplicacion.getI18nString("inventario.datosAmortizacion.tag3"), CuentaJDialog.CUENTA_AMORTIZACION);
//        }
//
//        cuentaJDialog.addActionListener(new java.awt.event.ActionListener(){
//            public void actionPerformed(ActionEvent e){
//				cuentaJDialog_actionPerformed(tipo);
//			}
//		});
//        cuentaJDialog.setVisible(true);
//    }
//
//    private void cuentaJDialog_actionPerformed(int tipo){
//        if (tipo==CuentaJDialog.CUENTA_CONTABLE){
//            if (cuentaJDialog.getCuenta()!=null) cuentaContableJCBox.setSelectedIndex(0);
//            if (cuentaJDialog.getCuenta() instanceof CuentaContable){
//                cuentaContable= (CuentaContable)cuentaJDialog.getCuenta();
//                cuentaContableJTField.setText(cuentaContable.getCuenta());
//            }
//        }else if (tipo==CuentaJDialog.CUENTA_AMORTIZACION){
//            if (cuentaJDialog.getCuenta()!=null) cuentaAmortizacionJCBox.setSelectedIndex(0);
//            if (cuentaJDialog.getCuenta() instanceof CuentaAmortizacion){
//                cuentaAmortizacion= (CuentaAmortizacion)cuentaJDialog.getCuenta();
//                cuentaAmortizacionJTField.setText(cuentaAmortizacion.getCuenta());
//                /** actualizamos campos */
//                tipoAmortizacionEJCBox.setSelectedPatron(null);
//                annosJTField.setText("");
//                porcentajeJTField.setText("");
//                totalAmortizadoJTField.setText("");
//            }
//        }
//
//       cuentaJDialog.dispose();
//    }

    private void boxActionPerformed(int i){
        if (i==CuentaJDialog.CUENTA_CONTABLE){
            if (cuentaContableJCBox.getSelectedIndex()!=0){
                cuentaContable= (CuentaContable)cuentaContableJCBox.getSelected();
                if (cuentaContable!=null)
                	cuentaContableJTField.setText(cuentaContable.getDescripcion());
                else
                	cuentaContableJTField.setText("");
            }else cuentaContable= null;
        }else if (i==CuentaJDialog.CUENTA_AMORTIZACION){
            if (cuentaAmortizacionJCBox.getSelectedIndex()!=0){
                cuentaAmortizacion= (CuentaAmortizacion)cuentaAmortizacionJCBox.getSelected();
                if (cuentaAmortizacion != null){
                    if (cuentaAmortizacion.getAnnos()!=-1)
                        try{annosJTField.setNumber(new Integer(cuentaAmortizacion.getAnnos()));}catch(Exception e){}
                    else annosJTField.setText("");
                    if (cuentaAmortizacion.getPorcentaje()!=-1)
                        try{porcentajeJTField.setNumber(new Double(cuentaAmortizacion.getPorcentaje()));}catch(Exception e){}
                    else porcentajeJTField.setText("");
                    tipoAmortizacionEJCBox.setSelectedPatron(cuentaAmortizacion.getTipoAmortizacion());
                    if (cuentaAmortizacion.getTotalAmortizado()!=-1){
                        try{totalAmortizadoJTField.setNumber(new Double(cuentaAmortizacion.getTotalAmortizado()));}catch(Exception e){}
                    }else totalAmortizadoJTField.setText("");
                }
                if (cuentaAmortizacion!=null)
                	cuentaAmortizacionJTField.setText(cuentaAmortizacion.getDescripcion());
                else
                	cuentaAmortizacionJTField.setText("");
                
            }else cuentaAmortizacion= null;
        }
        
        if (cuentaAmortizacion!=null){
        	
        	
        }
    }
    /**
     * TODO: Metodo que obtiene el valor de la amortizacion desde la fecha de adquisicion hasta la fecha de amortizacion
     * @throws Exception 
     */
    	public static double calcularTotalAmortizado(Date fechaAmortizado, Date fechaAdq, double costeAdq,CuentaAmortizacion cuentaAmort,String tipoAmort) throws Exception {
    		logger.info("Calculando Amortizado");
    		if (tipoAmort == null
    				|| tipoAmort == null
    				|| tipoAmort.equalsIgnoreCase("")
    				|| tipoAmort.equalsIgnoreCase(POR_NADA)
    				|| fechaAdq==null) {
    			return 0;
    		}
    		DecimalFormat formateador8= new DecimalFormat("########.##");	
    		DecimalFormatSymbols dfs = formateador8.getDecimalFormatSymbols();
    		dfs.setDecimalSeparator('.');	
    		formateador8.setDecimalFormatSymbols(dfs);
    		
    		double totalAmor = 0;
    		// if (tipoAmortizacionEJCBox.getSelectedItem())
    		if (cuentaAmort != null)
    			cuentaAmort.setTipoAmortizacion(tipoAmort);
    		// Se obtiene los dias que han pasado desde la fecha e adquisicin a la
    		// fecha actual
    		Date fechaActual=null;
    		if (fechaAmortizado==null)
    			fechaActual =  (Date) inventarioClient.getDate(Const.ACTION_GET_DATE);
    		else
    			fechaActual = fechaAmortizado;

    		int ndias = 0;
    		try {
    			ndias = (int) Math.floor((fechaActual.getTime() - fechaAdq
    					.getTime()) / (1000 * 3600 * 24));
    		} catch (Exception e) {
    			e.printStackTrace();
    			logger.error("No se puede calcular la amortización con la fecha de amortización nula");
    		}
    		if(ndias>0){
	    		if (tipoAmort.equalsIgnoreCase(POR_AÑOS)) {
	    				
	    			// totalAmor =
	    			// (costeAdquisicion/(cuentaAmortizacion.getAnnos()*365))*ndias;
	    			// Se obtiene lo que se amortiza cada año
	    			totalAmor = costeAdq / cuentaAmort.getAnnos();
	    			// Se obtiene lo que se amortiza cada dia
	    			totalAmor = totalAmor / (365);
	    			// Se obtiene lo que se ha amortizado ya
	    			totalAmor = totalAmor * ndias;
	
	    		} else if (tipoAmort.equalsIgnoreCase(POR_PORCENTAJE)) {
	    			// totalAmor =
	    			// (costeAdquisicion/(cuentaAmortizacion.getPorcentaje()*365))*ndias;
	    			// Se obtiene lo que se amortiza cada año
	    			totalAmor = costeAdq / cuentaAmort.getPorcentaje();
	    			// Se obtiene lo que se amortiza cada dia
	    			totalAmor = totalAmor / 365;
	    			// Se obtiene lo que se ha amortizado ya
	    			totalAmor = totalAmor * ndias;
	    		} else {
	    			totalAmor = 0;
	    		}
				totalAmor =Double.parseDouble(formateador8.format(totalAmor));
				if(totalAmor>=costeAdq)
					totalAmor=costeAdq;
	    		cuentaAmort.setTotalAmortizado(totalAmor);
    		}else{
    			logger.error("No se puede calcular la amortización para una fecha de adquisición posterior a la fecha de amoritzación");
    			throw new RuntimeException("No se puede calcular la amortización para una fecha de adquisición posterior a la fecha de amoritzación");

    		}
    		return (cuentaAmort.getTotalAmortizado());

    	}
    	/**
    	 * Calcula el valor amortizado por año
    	 * @param anio
    	 * @param fechaAdq
    	 * @param costeAdq
    	 * @param cuentaAmort
    	 * @param tipoAmort
    	 * @return
    	 * @throws Exception
    	 */
   	public static double calcularTotalAmortizadoPorAnio(Date fechaAdq,Integer anio,double costeAdq,CuentaAmortizacion cuentaAmort,String tipoAmort) throws Exception {
   		SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy"); 
   		String fechaAmortizadoString="31-12-"+anio;
   		Date fecha=fechaAdq;
   		Date fechaAmortizado=sdf.parse(fechaAmortizadoString); 
   		if(fechaAdq.getYear()+1900<anio){
   			String fechaAdqString="01-12-"+anio;
   			fecha=sdf.parse(fechaAdqString);
   		}
   		return calcularTotalAmortizado(fechaAmortizado, fecha, costeAdq, cuentaAmort, tipoAmort);
   		
   	}


	private void initCContableCBox(Collection c){
        cuentaContableJCBox= new com.geopista.app.inventario.CuentasJComboBox(c.toArray(), null, true);
        cuentaContableJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxActionPerformed(CuentaJDialog.CUENTA_CONTABLE);
            }
        });
    }


    private void initCAmortizacionCBox(Collection c){
        cuentaAmortizacionJCBox= new com.geopista.app.inventario.CuentasJComboBox(c.toArray(), null, true);
        cuentaAmortizacionJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxActionPerformed(CuentaJDialog.CUENTA_AMORTIZACION);
            }
        });
    }

    /**
     * Carga los datos de amortizacion de un bien de inventario
     * @param bien
     */
    public void load(BienBean bien){
        if (bien == null) return;
        fechaAdquisicion = bien.getFechaAdquisicion();
        idBien=bien.getId();
        patron=bien.getTipo();
        if (bien instanceof InmuebleBean ){
        	costeAdquisicion= ((InmuebleBean)bien).getValorAdquisicionInmueble();

        }else if (bien instanceof MuebleBean &&((MuebleBean)bien).getCosteAdquisicion()!=null){
        	costeAdquisicion= ((MuebleBean)bien).getCosteAdquisicion();
        }else if (bien instanceof VehiculoBean && ((VehiculoBean)bien).getCosteAdquisicion()!=null){
        	costeAdquisicion= ((VehiculoBean)bien).getCosteAdquisicion();
        }
        if (bien.getCuentaContable() != null){
            cuentaContableJCBox.setSelected(bien.getCuentaContable().getId());
        }
        load(bien.getCuentaAmortizacion());
        actualizarModelo();
 
    }
    /**
     * carga los dtos de una cuanta
     * @param cuentaAmortizacion
     */
    public void load (CuentaAmortizacion cuentaAmortizacion){
    	  if (cuentaAmortizacion==null) return;
    	  cuentaAmortizacionJCBox.setSelected(cuentaAmortizacion.getId());
          if (cuentaAmortizacion.getAnnos()!=-1)
              try{annosJTField.setNumber(new Integer(cuentaAmortizacion.getAnnos()));}catch(Exception e){}
          else annosJTField.setText("");
          if (cuentaAmortizacion.getPorcentaje()!=-1)
              try{porcentajeJTField.setNumber(new Double(cuentaAmortizacion.getPorcentaje()));}catch(Exception e){}
          else porcentajeJTField.setText("");
          tipoAmortizacionEJCBox.setSelectedPatron(cuentaAmortizacion.getTipoAmortizacion());
          if (cuentaAmortizacion.getTotalAmortizado()!=-1)
              try{totalAmortizadoJTField.setNumber(new Double(cuentaAmortizacion.getTotalAmortizado()));}catch(Exception e){}
          else totalAmortizadoJTField.setText("");
 
    }

    /**
     * Método que actualiza los datos de amortizacion para un bien de inventario
     * @param bien a actualizar sus datos de amortizacion
     */
    public void actualizarDatos(BienBean bien){
        if (bien == null) return;
        bien.setCuentaContable(cuentaContable);
        bien.setCuentaAmortizacion(getCuentaAmortizacion());
        
    }
    /*
     * Devuelve los datos de la cuenta de Amortizacion
     */
    public CuentaAmortizacion getCuentaAmortizacion(){
    	if (cuentaAmortizacion != null){
            try{cuentaAmortizacion.setPorcentaje(((Double)porcentajeJTField.getNumber()).doubleValue());}catch(Exception e){}
            try{cuentaAmortizacion.setAnnos((Integer.parseInt(/** al ser de 3 dig. no formatea */annosJTField.getText())));}catch(Exception e){}
            cuentaAmortizacion.setTipoAmortizacion(tipoAmortizacionEJCBox.getSelectedPatron());
            try{cuentaAmortizacion.setTotalAmortizado(((Double)totalAmortizadoJTField.getNumber()).doubleValue());}catch(Exception e){}
        }
    	return cuentaAmortizacion;
    }
    public void setEnabled(boolean b){
        cuentaContableJCBox.setEnabled(b);
        cuentaContableJTField.setEnabled(false);
        cuentaAmortizacionJCBox.setEnabled(b);
        cuentaAmortizacionJTField.setEnabled(false);
        tipoAmortizacionEJCBox.setEnabled(b);
        annosJTField.setEnabled(b);
        porcentajeJTField.setEnabled(b);
        totalAmortizadoJTField.setEnabled(false);
//        cuentaContableJButton.setEnabled(b);
//        cuentaAmortizacionJButton.setEnabled(b);        
    }

    public void clear(){
        cuentaContableJCBox.setSelectedIndex(0);
        cuentaContableJTField.setText("");
        cuentaAmortizacionJCBox.setSelectedIndex(0);
        cuentaAmortizacionJTField.setText("");
        tipoAmortizacionEJCBox.setSelectedPatron(null);
        annosJTField.setText("");
        porcentajeJTField.setText("");
        totalAmortizadoJTField.setText("");
    }

    public void renombrarComponentes(){
        try{setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosAmortizacion.tag1")));}catch(Exception e){}
        try{cuentaContableJLabel.setText(aplicacion.getI18nString("inventario.datosAmortizacion.tag2"));}catch(Exception e){}
        try{cuentaAmortizacionJLabel.setText(aplicacion.getI18nString("inventario.datosAmortizacion.tag3"));}catch(Exception e){}
        try{tipoAmortizacionJLabel.setText(aplicacion.getI18nString("inventario.datosAmortizacion.tag4"));}catch(Exception e){}
        try{annosJLabel.setText(aplicacion.getI18nString("inventario.datosAmortizacion.tag5"));}catch(Exception e){}
        try{porcentajeJLabel.setText(aplicacion.getI18nString("inventario.datosAmortizacion.tag6"));}catch(Exception e){}
        try{totalAmortizadoJLabel.setText(aplicacion.getI18nString("inventario.datosAmortizacion.tag7"));}catch(Exception e){}
        try{planAmortizacionJLabel.setText(aplicacion.getI18nString("inventario.datosAmortizacion.tag10"));}catch(Exception e){}

//        try{cuentaContableJButton.setToolTipText(aplicacion.getI18nString("inventario.datosAmortizacion.tag8"));}catch(Exception e){}
//        try{cuentaAmortizacionJButton.setToolTipText(aplicacion.getI18nString("inventario.datosAmortizacion.tag9"));}catch(Exception e){}

    }


    private javax.swing.JLabel cuentaContableJLabel;
    private com.geopista.app.inventario.CuentasJComboBox cuentaContableJCBox;
    private javax.swing.JTextField cuentaContableJTField;
    private javax.swing.JLabel cuentaAmortizacionJLabel;
    private com.geopista.app.inventario.CuentasJComboBox cuentaAmortizacionJCBox;
    private javax.swing.JTextField cuentaAmortizacionJTField;
    private javax.swing.JLabel tipoAmortizacionJLabel;
    private ComboBoxEstructuras tipoAmortizacionEJCBox;
    private javax.swing.JLabel annosJLabel;
    private com.geopista.app.utilidades.JNumberTextField annosJTField;
    private javax.swing.JLabel porcentajeJLabel;
    private com.geopista.app.utilidades.JNumberTextField porcentajeJTField;
    private javax.swing.JLabel totalAmortizadoJLabel;
    private javax.swing.JLabel planAmortizacionJLabel;
    private com.geopista.app.utilidades.JNumberTextField totalAmortizadoJTField;
	private javax.swing.JTable jTableHistoricoAmortizacion; 
//    private JButton cuentaContableJButton;
//    private JButton cuentaAmortizacionJButton;
	private String patron;

    public void setCosteAdquisicion(double costeAdquisicion) {
		this.costeAdquisicion = costeAdquisicion;
	}

	/**
	 * @param fechaAdquisicion the fechaAdquisicion to set
	 */
	public void setFechaAdquisicion(Date fechaAdquisicion) {
		this.fechaAdquisicion = fechaAdquisicion;
	}
	
	private void actualizarModelo() {
		HistoricoAmortizacionBienTableModel historicoAmortizacion = new HistoricoAmortizacionBienTableModel(getLocale().toString());	
		try {
			historicoAmortizacion.setModelData(inventarioClient.getHistoricoBienAmortizable(idBien,patron));
		} catch (Exception e) {	
			logger.error(aplicacion.getI18NResource().getString("inventario.getinventario.error"));
			ErrorDialog.show(this.getParent(), "ERROR",
					aplicacion.getI18NResource().getString("inventario.getinventario.error"),
					StringUtil.stackTrace(e));
		
		}
		TableSorted sorter = new TableSorted(historicoAmortizacion);
		sorter.setTableHeader(jTableHistoricoAmortizacion.getTableHeader());
		jTableHistoricoAmortizacion.setModel(sorter);
		jTableHistoricoAmortizacion.setAutoscrolls(true);
		TableColumn column = jTableHistoricoAmortizacion.getColumnModel().getColumn(
				historicoAmortizacion.findColumn(I18N.get("LocalGISInventario","inventario.historicoamortizacion.tabla.columna.anio")));
		column.setPreferredWidth(30);
		column = jTableHistoricoAmortizacion.getColumnModel().getColumn(
				historicoAmortizacion.findColumn(I18N.get("LocalGISInventario","inventario.historicoamortizacion.tabla.columna.base")));
		column.setPreferredWidth(55);
		jTableHistoricoAmortizacion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

}

