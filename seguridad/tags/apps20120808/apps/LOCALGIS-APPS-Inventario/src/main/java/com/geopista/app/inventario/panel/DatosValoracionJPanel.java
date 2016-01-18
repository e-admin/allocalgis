package com.geopista.app.inventario.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.InmuebleRusticoBean;
import com.geopista.protocol.inventario.InmuebleUrbanoBean;
import com.geopista.protocol.inventario.InventarioClient;

import com.geopista.ui.plugin.io.dgn.impl.double64;

import javax.swing.*;

import org.apache.log4j.Logger;
/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 26-jul-2006
 * Time: 13:05:42
 * To change this template use File | Settings | File Templates.
 */
public class DatosValoracionJPanel extends JPanel{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean urb ;
	private boolean rus ;
	private static double valorMetro = 0;
	private AppContext aplicacion;
    private JPanel aux1JPanel;
    private JPanel aux2JPanel;
    private JPanel aux3JPanel;
	private InventarioClient inventarioClient;

    Logger logger= Logger.getLogger(DatosValoracionJPanel.class);
    /**
     * Método que genera el panel de los datos de Valoracion de un bien inmueble
     */

    public DatosValoracionJPanel() {
        aplicacion= (AppContext) AppContext.getApplicationContext();
        inventarioClient = new InventarioClient(
				aplicacion
						.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA)
						+ Constantes.INVENTARIO_SERVLET_NAME);
        initComponents();
//        calculaValorMetro();
        renombrarComponentes();
    }

    private void initComponents() {
    	
        sueloSuperficeRegistralJLabel= new InventarioLabel();
        sueloSuperficeCatastralJLabel= new InventarioLabel();
        sueloSuperficeRealJLabel= new InventarioLabel();
        sueloFechaAdquisicionJLabel= new InventarioLabel();
        sueloValorAdquisicionJLabel= new InventarioLabel();
        sueloValorCatastralJLabel= new InventarioLabel();
        sueloValorActualJLabel= new InventarioLabel();
        sueloValorRegularizadoJLabel= new InventarioLabel();
        suelolongitudJLabel = new InventarioLabel();
        sueloanchoJLabel = new InventarioLabel();
        sueloMetrosPavJLabel = new InventarioLabel();
        sueloMetrosNoPavJLabel = new InventarioLabel();
        

        sueloSuperficeRegistralJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999), true, 2);
        sueloSuperficeRegistralJTField.setSignAllowed(false);
        sueloSuperficeCatastralJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999), true, 2);
        sueloSuperficeCatastralJTField.setSignAllowed(false);
        sueloSuperficeRealJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999), true, 2);
        sueloSuperficeRealJTField.setSignAllowed(false);
        sueloFechaAdquisicionJTField= new JFormattedTextField(Constantes.df);
        sueloFechaAdquisicionJTField.setEnabled(false);
        sueloFechaAdquisicionJButton= new CalendarButton(sueloFechaAdquisicionJTField);
        sueloValorAdquisicionJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        sueloValorAdquisicionJTField.setSignAllowed(false);
        sueloValorCatastralJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        sueloValorCatastralJTField.setSignAllowed(false);
        sueloValorActualJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        sueloValorActualJTField.setSignAllowed(false);
        
        sueloValorRegularizadoJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        sueloValorRegularizadoJTField.setSignAllowed(false);
        sueloValorRegularizadoJTField.setEditable(false);
        sueloLongitudJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
        sueloLongitudJTField.setSignAllowed(false);
        sueloAnchoJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
        sueloAnchoJTField.setSignAllowed(false);
        sueloMetrosPavJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
        sueloMetrosPavJTField.setSignAllowed(false);
        sueloMetrosNoPavJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
        sueloMetrosNoPavJTField.setSignAllowed(false);
           
        
        constSuperficieOcupadaJLabel= new InventarioLabel();
        constSuperficiePlantaJLabel= new InventarioLabel();
        constSuperficieConstruidaJLabel= new InventarioLabel();
        constValorAdquisicionJLabel= new InventarioLabel();
        constValorCatastralJLabel= new InventarioLabel();
        constValorActualJLabel= new InventarioLabel();

        constSuperficieOcupadaJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999), true, 2);
        constSuperficieOcupadaJTField.setSignAllowed(false);
        constSuperficiePlantaJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999), true, 2);
        constSuperficiePlantaJTField.setSignAllowed(false);
        constSuperficieConstruidaJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999), true, 2);
        constSuperficieConstruidaJTField.setSignAllowed(false);
        constValorAdquisicionJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        constValorAdquisicionJTField.setSignAllowed(false); 
        constValorCatastralJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        constValorCatastralJTField.setSignAllowed(false);
        constValorActualJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        constValorActualJTField.setSignAllowed(false);

        inmuebleValorAdquisicionJLabel= new InventarioLabel();
        inmuebleValorActualJLabel= new InventarioLabel();
        inmuebleValorCatastralJLabel= new InventarioLabel();
        anioValorCatastralJLabel= new InventarioLabel();

        inmuebleValorAdquisicionJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        inmuebleValorAdquisicionJTField.setSignAllowed(false);
        inmuebleValorActualJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        inmuebleValorActualJTField.setSignAllowed(false);
        inmuebleValorCatastralJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        inmuebleValorCatastralJTField.setSignAllowed(false);
        anioValorCatastralJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(9999), true, 0);
        anioValorCatastralJTField.setSignAllowed(false);
        
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        aux1JPanel= new JPanel();
        aux1JPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        aux1JPanel.add(sueloSuperficeRegistralJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 130, 20));
        aux1JPanel.add(sueloSuperficeCatastralJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 20));
        aux1JPanel.add(sueloSuperficeRealJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 130, 20));
        aux1JPanel.add(sueloFechaAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 130, 20));
        aux1JPanel.add(sueloValorAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 130, 20));
        aux1JPanel.add(sueloValorCatastralJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 130, 20));
        aux1JPanel.add(sueloValorActualJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 130, 20));
        aux1JPanel.add(sueloValorRegularizadoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 130, 20));
        aux1JPanel.add(suelolongitudJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 130, 20));
        aux1JPanel.add(sueloanchoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 130, 20));
        aux1JPanel.add(sueloMetrosPavJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 130, 20));
        aux1JPanel.add(sueloMetrosNoPavJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 130, 20));

        aux1JPanel.add(sueloSuperficeRegistralJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 160, 20));
        aux1JPanel.add(sueloSuperficeCatastralJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 160, 20));
        aux1JPanel.add(sueloSuperficeRealJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 160, 20));
        aux1JPanel.add(sueloFechaAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 140, 20));
        add(sueloFechaAdquisicionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 90, 20, 20));
        aux1JPanel.add(sueloValorAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 160, 20));
        aux1JPanel.add(sueloValorCatastralJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 160, 20));
        aux1JPanel.add(sueloValorActualJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 160, 20));
        aux1JPanel.add(sueloValorRegularizadoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 160, 20));
        aux1JPanel.add(sueloLongitudJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 160, 20));
        aux1JPanel.add(sueloAnchoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 160, 20));
        aux1JPanel.add(sueloMetrosPavJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 230, 160, 20));
        aux1JPanel.add(sueloMetrosNoPavJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 160, 20));


        aux2JPanel= new JPanel();
        aux2JPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        aux2JPanel.add(constSuperficieOcupadaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 130, 20));
        aux2JPanel.add(constSuperficiePlantaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 20));
        aux2JPanel.add(constSuperficieConstruidaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 130, 20));
        aux2JPanel.add(constValorAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 130, 20));
        aux2JPanel.add(constValorCatastralJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 130, 20));
        aux2JPanel.add(constValorActualJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 130, 20));

        aux2JPanel.add(constSuperficieOcupadaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 160, 20));
        aux2JPanel.add(constSuperficiePlantaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 160, 20));
        aux2JPanel.add(constSuperficieConstruidaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 160, 20));
        aux2JPanel.add(constValorAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 160, 20));
        aux2JPanel.add(constValorCatastralJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 160, 20));
        aux2JPanel.add(constValorActualJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 160, 20));


        aux3JPanel= new JPanel();
        aux3JPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        aux3JPanel.add(inmuebleValorAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 130, 20));
        aux3JPanel.add(inmuebleValorActualJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 20));
        aux3JPanel.add(inmuebleValorCatastralJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 130, 20));
        aux3JPanel.add(anioValorCatastralJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 130, 20));


        aux3JPanel.add(inmuebleValorAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 160, 20));
        aux3JPanel.add(inmuebleValorActualJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 160, 20));
        aux3JPanel.add(inmuebleValorCatastralJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 160, 20));
        aux3JPanel.add(anioValorCatastralJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 160, 20));


        add(aux1JPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 455, 280));
        add(aux2JPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 455, 165));
        add(aux3JPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 445, 455, 150));

    }

    public void setEnabled(boolean b){
        sueloFechaAdquisicionJTField.setEnabled(false);
        sueloSuperficeRegistralJTField.setEnabled(b);
        sueloSuperficeCatastralJTField.setEnabled(b);
        sueloSuperficeRealJTField.setEnabled(b);
        sueloValorAdquisicionJTField.setEnabled(b);
        sueloValorCatastralJTField.setEnabled(b);
        sueloValorActualJTField.setEnabled(b);
        sueloValorRegularizadoJTField.setEnabled(b);
        sueloFechaAdquisicionJButton.setEnabled(b);
        sueloLongitudJTField.setEnabled(b);
        sueloAnchoJTField.setEnabled(b);
        sueloMetrosPavJTField.setEnabled(b);
        sueloMetrosNoPavJTField.setEnabled(b);
        constSuperficieOcupadaJTField.setEnabled(b);
        constSuperficiePlantaJTField.setEnabled(b);
        constSuperficieConstruidaJTField.setEnabled(b);
        constValorAdquisicionJTField.setEnabled(b);
        constValorCatastralJTField.setEnabled(b);
        constValorActualJTField.setEnabled(b);
        inmuebleValorAdquisicionJTField.setEnabled(b);
        inmuebleValorActualJTField.setEnabled(b);
        inmuebleValorCatastralJTField.setEnabled(b);
        anioValorCatastralJTField.setEnabled(b);
        
    }

    public void load(InmuebleBean inmueble){
        
    	if (inmueble==null) return;
        
        if (inmueble.getSuperficieRegistralSuelo()!=-1)
            try{
            	sueloSuperficeRegistralJTField.setNumber(new Double(inmueble.getSuperficieRegistralSuelo()));
            }catch(Exception e){}
        else 
        	sueloSuperficeRegistralJTField.setText("");
        
        if (inmueble.getSuperficieCatastralSuelo()!=-1)
            try{
            	sueloSuperficeCatastralJTField.setNumber(new Double(inmueble.getSuperficieCatastralSuelo()));
            }catch(Exception e){}
        else 
        	sueloSuperficeCatastralJTField.setText("");
        
        if (inmueble.getSuperficieRealSuelo()!=-1)
            try{
            	sueloSuperficeRealJTField.setNumber(new Double(inmueble.getSuperficieRealSuelo()));
            }catch(Exception e){}
        else 
        	sueloSuperficeRealJTField.setText("");
        
        try{
        	sueloFechaAdquisicionJTField.setText(Constantes.df.format(inmueble.getFechaAdquisicionSuelo()));
        }catch(Exception e){}
        
        if (inmueble.getValorAdquisicionSuelo()!=-1)
            try{
            	sueloValorAdquisicionJTField.setNumber(new Double(inmueble.getValorAdquisicionSuelo()));
            }catch(Exception e){}
        else 
        	sueloValorAdquisicionJTField.setText("");
        
        if (inmueble.getValorCatastralSuelo()!=-1)
            try{
            	sueloValorCatastralJTField.setNumber(new Double(inmueble.getValorCatastralSuelo()));
            }catch(Exception e){}
        else 
        	sueloValorCatastralJTField.setText("");
        
        if (inmueble.getValorActualSuelo()!=-1)
            try{
            	sueloValorActualJTField.setNumber(new Double(inmueble.getValorActualSuelo()));
            }catch(Exception e){}
        else 
        	sueloValorActualJTField.setText("");
        
        if (inmueble.getSuperficieOcupadaConstruccion()!=-1)
            try{
            	constSuperficieOcupadaJTField.setNumber(new Double(inmueble.getSuperficieOcupadaConstruccion()));
            }catch(Exception e){}
        else 
        	constSuperficieOcupadaJTField.setText("");
        
        if (inmueble.getSuperficieEnPlantaConstruccion()!=-1)
            try{
            	constSuperficiePlantaJTField.setNumber(new Double(inmueble.getSuperficieEnPlantaConstruccion()));
            }catch(Exception e){}
        else 
        	constSuperficiePlantaJTField.setText("");
        
        if (inmueble.getSuperficieConstruidaConstruccion()!=-1)
            try{
            	constSuperficieConstruidaJTField.setNumber(new Double(inmueble.getSuperficieConstruidaConstruccion()));
            }catch(Exception e){}
        else 
        	constSuperficieConstruidaJTField.setText("");
        
        if (inmueble.getValorAdquisicionConstruccion()!=-1)
            try{
            	constValorAdquisicionJTField.setNumber(new Double(inmueble.getValorAdquisicionConstruccion()));
            }catch(Exception e){}
        else 
        	constValorAdquisicionJTField.setText("");
        
        if (inmueble.getValorCatastralConstruccion()!=-1)
            try{
            	constValorCatastralJTField.setNumber(new Double(inmueble.getValorCatastralConstruccion()));
            }catch(Exception e){}
        else 
        	constValorCatastralJTField.setText("");
        
        if (inmueble.getValorActualConstruccion()!=-1)
            try{
            	constValorActualJTField.setNumber(new Double(inmueble.getValorActualConstruccion()));
            }catch(Exception e){}
        else 
        	constValorActualJTField.setText("");
        
        if (inmueble.getValorAdquisicionInmueble()!=-1)
            try{
            	inmuebleValorAdquisicionJTField.setNumber(new Double(inmueble.getValorAdquisicionInmueble()));
            }catch(Exception e){}
        else 
        	inmuebleValorAdquisicionJTField.setText("");
        
        if (inmueble.getValorActualInmueble()!=-1)
            try{
            	inmuebleValorActualJTField.setNumber(new Double(inmueble.getValorActualInmueble()));
            }catch(Exception e){}
        else 
        	inmuebleValorActualJTField.setText("");
        
        if (inmueble.getValorCatastralInmueble()!=-1)
            try{
            	inmuebleValorCatastralJTField.setNumber(new Double(inmueble.getValorCatastralInmueble()));
            }catch(Exception e){}
        else 
        	inmuebleValorCatastralJTField.setText("");
        
        if (inmueble.getAnioValorCatastral()!=null && inmueble.getAnioValorCatastral().intValue()!=-1)
            try{
            	anioValorCatastralJTField.setNumber(inmueble.getAnioValorCatastral());
            }catch(Exception e){}
        else 
        	anioValorCatastralJTField.setText("");

		urb = inmueble.isUrbano();
		rus = inmueble.isRustico();

//		// Si es inmueble urbano
		if (urb
				&& inmueble.getInmuebleUrbano() != null
				&& (inmueble.getInmuebleUrbano().getSueloMetrosPav() >= 0 || inmueble
						.getInmuebleUrbano().getSueloMetrosNoPav() >= 0)) {

			try {
				sueloAnchoJTField.setNumber(inmueble.getInmuebleUrbano()
						.getSueloAncho());
				sueloLongitudJTField.setNumber(inmueble.getInmuebleUrbano()
						.getSueloLong());
				sueloMetrosPavJTField.setNumber(inmueble.getInmuebleUrbano()
						.getSueloMetrosPav());
				sueloMetrosNoPavJTField.setNumber(inmueble.getInmuebleUrbano()
						.getSueloMetrosNoPav());
			} catch (Exception e) {
				logger.error("No se ha podido cargar ciertas propiedades del panel de Datos de Valoracion.");
				e.printStackTrace();
			}
			calculaRegularizado(inmueble.getInmuebleUrbano()
					.getSueloMetrosPav(), inmueble.getInmuebleUrbano()
					.getSueloMetrosNoPav());
		}

		// Si es inmueble rustico
		if (rus&& inmueble.getInmuebleRustico() != null){
			
			  //Se añaden listener, para que cuando cambie algun campo, se recalcule el valor de regulacion
	        sueloLongitudJTField.addFocusListener(new MyFocusListenerAncho());
	        sueloAnchoJTField.addFocusListener(new MyFocusListenerAlto());
	        
	        sueloMetrosPavJTField.addFocusListener(new MyFocusListenerPav());
	        sueloMetrosNoPavJTField.addFocusListener(new MyFocusListenerPav());
	        
			if(inmueble.getInmuebleRustico().getSueloMetrosPav() >= 0 || inmueble
						.getInmuebleRustico().getSueloMetrosNoPav() >= 0) {
				try {
			        
					sueloAnchoJTField.setNumber(inmueble.getInmuebleRustico()
							.getSueloAncho());
					sueloLongitudJTField.setNumber(inmueble.getInmuebleRustico()
							.getSueloLong());
					sueloMetrosPavJTField.setNumber(inmueble.getInmuebleRustico()
							.getSueloMetrosPav());
					sueloMetrosNoPavJTField.setNumber(inmueble.getInmuebleRustico()
							.getSueloMetrosNoPav());
				} catch (Exception e) {
					logger.error("No se ha podido cargar ciertas propiedades del panel de Datos de Valoracion.");
					e.printStackTrace();
				}
				calculaRegularizado(inmueble.getInmuebleRustico()
						.getSueloMetrosPav(), inmueble.getInmuebleRustico()
						.getSueloMetrosNoPav());
			}
		}
    }
	
    private void calculaValorMetro() {
    	try{
    		
    		if (urb) //Valor del metro Urbano
    			valorMetro= inventarioClient.getValorMetro(Const.PATRON_INMUEBLES_RUSTICOS);

    		else if (rus)//Valor del metro Rustico
    			valorMetro= inventarioClient.getValorMetro(Const.PATRON_INMUEBLES_RUSTICOS);//aplicacion.getBlackboard().getDouble("valor_metro_rustico");
    		else
    			valorMetro=0;
    		
    		
    	}catch(Exception e){
    		logger.error("No se ha podido convertir el valor del metro cuadrado"+e.getMessage());
    		e.printStackTrace();
    	}
		
	}



	/**
     * Método que actualiza los datos de valoracion para un bien inmueble
     * @param bien a actualizar sus datos de valoracion
     */
	public void actualizarDatos(Object bien) {
		if (bien == null)
			return;
		if (bien instanceof InmuebleBean) {
			try {
				if (sueloSuperficeRegistralJTField.getText() != null
						&& !sueloSuperficeRegistralJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setSuperficieRegistralSuelo(((Double) sueloSuperficeRegistralJTField
									.getNumber()).doubleValue());
				if (sueloSuperficeCatastralJTField.getText() != null
						&& !sueloSuperficeCatastralJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setSuperficieCatastralSuelo(((Double) sueloSuperficeCatastralJTField
									.getNumber()).doubleValue());
				if (sueloSuperficeRealJTField.getText() != null
						&& !sueloSuperficeRealJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setSuperficieRealSuelo(((Double) sueloSuperficeRealJTField
									.getNumber()).doubleValue());

				if (sueloFechaAdquisicionJTField.getText().trim() != null
						&& !sueloFechaAdquisicionJTField.getText().trim()
								.equalsIgnoreCase("")) {
					((InmuebleBean) bien)
							.setFechaAdquisicionSuelo(Constantes.df
									.parse(sueloFechaAdquisicionJTField
											.getText().trim()));
				}
				if (sueloValorAdquisicionJTField.getText() != null
						&& !sueloValorAdquisicionJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setValorAdquisicionSuelo(((Double) sueloValorAdquisicionJTField
									.getNumber()).doubleValue());
				if (sueloValorCatastralJTField.getText() != null
						&& !sueloValorCatastralJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setValorCatastralSuelo(((Double) sueloValorCatastralJTField
									.getNumber()).doubleValue());
				if (sueloValorActualJTField.getText() != null
						&& !sueloValorActualJTField.getText().equalsIgnoreCase(
								""))
					((InmuebleBean) bien)
							.setValorActualSuelo(((Double) sueloValorActualJTField
									.getNumber()).doubleValue());
				if (constSuperficieOcupadaJTField.getText() != null
						&& !constSuperficieOcupadaJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setSuperficieOcupadaConstruccion(((Double) constSuperficieOcupadaJTField
									.getNumber()).doubleValue());
				if (constSuperficiePlantaJTField.getText() != null
						&& !constSuperficiePlantaJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setSuperficieEnPlantaConstruccion(((Double) constSuperficiePlantaJTField
									.getNumber()).doubleValue());
				if (constSuperficieConstruidaJTField.getText() != null
						&& !constSuperficieConstruidaJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setSuperficieConstruidaConstruccion(((Double) constSuperficieConstruidaJTField
									.getNumber()).doubleValue());
				if (constValorAdquisicionJTField.getText() != null
						&& !constValorAdquisicionJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setValorAdquisicionConstruccion(((Double) constValorAdquisicionJTField
									.getNumber()).doubleValue());
				if (constValorCatastralJTField.getText() != null
						&& !constValorCatastralJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setValorCatastralConstruccion(((Double) constValorCatastralJTField
									.getNumber()).doubleValue());
				if (constValorActualJTField.getText() != null
						&& !constValorActualJTField.getText().equalsIgnoreCase(
								""))
					((InmuebleBean) bien)
							.setValorActualConstruccion(((Double) constValorActualJTField
									.getNumber()).doubleValue());
				if (inmuebleValorAdquisicionJTField.getText() != null
						&& !inmuebleValorAdquisicionJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setValorAdquisicionInmueble(((Double) inmuebleValorAdquisicionJTField
									.getNumber()).doubleValue());
				if (inmuebleValorActualJTField.getText() != null
						&& !inmuebleValorActualJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setValorActualInmueble(((Double) inmuebleValorActualJTField
									.getNumber()).doubleValue());
				if (inmuebleValorCatastralJTField.getText() != null
						&& !inmuebleValorCatastralJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setValorCatastralInmueble(((Double) inmuebleValorCatastralJTField
									.getNumber()).doubleValue());
				if (anioValorCatastralJTField.getText() != null
						&& !anioValorCatastralJTField.getText()
								.equalsIgnoreCase(""))
					((InmuebleBean) bien)
							.setAnioValorCatastral(((Long) anioValorCatastralJTField
									.getNumber()).intValue());

				if (((InmuebleBean) bien).isUrbano()) {
					setDatosSueloUrb(((InmuebleBean) bien).getInmuebleUrbano());
				}

				if (((InmuebleBean) bien).isRustico()) {
					setDatosSueloRus(((InmuebleBean) bien).getInmuebleRustico());
				}

			} catch (Exception e) {
				logger.error("Ha fallado la actualización de los datos de valoracion ");
				e.printStackTrace();
			}
            
        }
    }

	
	private void setDatosSueloRus(InmuebleRusticoBean bien)
			throws ParseException {
		if (!sueloAnchoJTField.getText().equalsIgnoreCase("")) {
			bien.setSueloAncho(((Double) sueloAnchoJTField.getNumber())
					.doubleValue());
		}
		if (!sueloLongitudJTField.getText().equalsIgnoreCase("")) {
			bien.setSueloLong(((Double) sueloLongitudJTField.getNumber())
					.doubleValue());
		}
		if (!sueloMetrosPavJTField.getText().equalsIgnoreCase("")) {
			bien.setSueloMetrosPav(((Double) sueloMetrosPavJTField.getNumber())
					.doubleValue());
		}
		if (!sueloMetrosNoPavJTField.getText().equalsIgnoreCase("")) {
			bien.setSueloMetrosNoPav(((Double) sueloMetrosNoPavJTField
					.getNumber()).doubleValue());
		}
	}
	
	private void setDatosSueloUrb(InmuebleUrbanoBean bien)
			throws ParseException {
		if (!sueloAnchoJTField.getText().equalsIgnoreCase("")) {
			bien.setSueloAncho(((Double) sueloAnchoJTField.getNumber())
					.doubleValue());
		}
		if (!sueloLongitudJTField.getText().equalsIgnoreCase("")) {
			bien.setSueloLong(((Double) sueloLongitudJTField.getNumber())
					.doubleValue());
		}
		if (!sueloMetrosPavJTField.getText().equalsIgnoreCase("")) {
			bien.setSueloMetrosPav(((Double) sueloMetrosPavJTField.getNumber())
					.doubleValue());
		}
		if (!sueloMetrosNoPavJTField.getText().equalsIgnoreCase("")) {
			bien.setSueloMetrosNoPav(((Double) sueloMetrosNoPavJTField
					.getNumber()).doubleValue());
		}
	}

    public void clear(){
        sueloFechaAdquisicionJTField.setText("");
        sueloSuperficeRegistralJTField.setText("");
        sueloSuperficeCatastralJTField.setText("");
        sueloSuperficeRealJTField.setText("");
        sueloValorAdquisicionJTField.setText("");
        sueloValorCatastralJTField.setText("");
        sueloValorActualJTField.setText("");
        sueloValorRegularizadoJTField.setText("");
        sueloLongitudJTField.setText("");
        sueloAnchoJTField.setText("");
        sueloMetrosPavJTField.setText("");
        sueloMetrosNoPavJTField.setText("");
        constSuperficieOcupadaJTField.setText("");
        constSuperficiePlantaJTField.setText("");
        constSuperficieConstruidaJTField.setText("");
        constValorAdquisicionJTField.setText("");
        constValorCatastralJTField.setText("");
        constValorActualJTField.setText("");
        inmuebleValorAdquisicionJTField.setText("");
        inmuebleValorActualJTField.setText("");
        inmuebleValorCatastralJTField.setText("");
        anioValorCatastralJTField.setText("");
    }

    private void renombrarComponentes(){
        try{aux1JPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosValoracion.tag1")));}catch(Exception e){}
        try{sueloSuperficeRegistralJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag2"));}catch(Exception e){}
        try{sueloSuperficeCatastralJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag3"));}catch(Exception e){}
        try{sueloSuperficeRealJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag4"));}catch(Exception e){}
        try{sueloFechaAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag5"));}catch(Exception e){}
        try{sueloValorAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag6"));}catch(Exception e){}
        try{sueloValorCatastralJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag7"));}catch(Exception e){}
        try{sueloValorActualJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag8"));}catch(Exception e){}
        
        try{sueloValorRegularizadoJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag21"));}catch(Exception e){}
        try{suelolongitudJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag22"));}catch(Exception e){}
        try{sueloanchoJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag23"));}catch(Exception e){}
        try{sueloMetrosPavJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag24"));}catch(Exception e){}
        try{sueloMetrosNoPavJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag25"));}catch(Exception e){}
        
        try{aux2JPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosValoracion.tag9")));}catch(Exception e){}
        try{constSuperficieOcupadaJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag10"));}catch(Exception e){}
        try{constSuperficiePlantaJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag11"));}catch(Exception e){}
        try{constSuperficieConstruidaJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag12"));}catch(Exception e){}
        try{constValorAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag13"));}catch(Exception e){}
        try{constValorCatastralJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag14"));}catch(Exception e){}
        try{constValorActualJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag15"));}catch(Exception e){}
        try{aux3JPanel.setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosValoracion.tag16")));}catch(Exception e){}
        try{inmuebleValorAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag17"));}catch(Exception e){}
        try{inmuebleValorActualJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag18"));}catch(Exception e){}
        try{inmuebleValorCatastralJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag19"));}catch(Exception e){}
        try{anioValorCatastralJLabel.setText(aplicacion.getI18nString("inventario.datosValoracion.tag20"));}catch(Exception e){}

    }


    private javax.swing.JLabel sueloSuperficeRegistralJLabel;
    private javax.swing.JLabel sueloSuperficeCatastralJLabel;
    private javax.swing.JLabel sueloSuperficeRealJLabel;
    private javax.swing.JLabel sueloFechaAdquisicionJLabel;
    private javax.swing.JLabel sueloValorAdquisicionJLabel;
    private javax.swing.JLabel sueloValorCatastralJLabel;
    private javax.swing.JLabel sueloValorActualJLabel;
    
    private javax.swing.JLabel sueloValorRegularizadoJLabel;
    private javax.swing.JLabel suelolongitudJLabel;
    private javax.swing.JLabel sueloanchoJLabel;
    private javax.swing.JLabel sueloMetrosPavJLabel;
    private javax.swing.JLabel sueloMetrosNoPavJLabel;

    private com.geopista.app.utilidades.JNumberTextField sueloSuperficeRegistralJTField;
    private com.geopista.app.utilidades.JNumberTextField sueloSuperficeCatastralJTField;
    private com.geopista.app.utilidades.JNumberTextField sueloSuperficeRealJTField;
    private javax.swing.JFormattedTextField sueloFechaAdquisicionJTField;
    private com.geopista.app.utilidades.JNumberTextField sueloValorAdquisicionJTField;
    private com.geopista.app.utilidades.JNumberTextField sueloValorCatastralJTField;
    private com.geopista.app.utilidades.JNumberTextField sueloValorActualJTField;
    private com.geopista.app.utilidades.JNumberTextField sueloValorRegularizadoJTField;
    private com.geopista.app.utilidades.JNumberTextField sueloLongitudJTField;
    private com.geopista.app.utilidades.JNumberTextField sueloAnchoJTField;
    private com.geopista.app.utilidades.JNumberTextField sueloMetrosPavJTField;
    private com.geopista.app.utilidades.JNumberTextField sueloMetrosNoPavJTField;

    private javax.swing.JLabel constSuperficieOcupadaJLabel;
    private javax.swing.JLabel constSuperficiePlantaJLabel;
    private javax.swing.JLabel constSuperficieConstruidaJLabel;
    private javax.swing.JLabel constValorAdquisicionJLabel;
    private javax.swing.JLabel constValorCatastralJLabel;
    private javax.swing.JLabel constValorActualJLabel;

    private com.geopista.app.utilidades.JNumberTextField constSuperficieOcupadaJTField;
    private com.geopista.app.utilidades.JNumberTextField constSuperficiePlantaJTField;
    private com.geopista.app.utilidades.JNumberTextField constSuperficieConstruidaJTField;
    private com.geopista.app.utilidades.JNumberTextField constValorAdquisicionJTField;
    private com.geopista.app.utilidades.JNumberTextField constValorCatastralJTField;
    private com.geopista.app.utilidades.JNumberTextField constValorActualJTField;

    private javax.swing.JLabel inmuebleValorAdquisicionJLabel;
    private javax.swing.JLabel inmuebleValorActualJLabel;
    private javax.swing.JLabel inmuebleValorCatastralJLabel;
    private javax.swing.JLabel anioValorCatastralJLabel;

    private com.geopista.app.utilidades.JNumberTextField inmuebleValorAdquisicionJTField;
    private com.geopista.app.utilidades.JNumberTextField inmuebleValorActualJTField;
    private com.geopista.app.utilidades.JNumberTextField inmuebleValorCatastralJTField;
    private com.geopista.app.utilidades.JNumberTextField anioValorCatastralJTField;

    
    private JButton sueloFechaAdquisicionJButton;

	class MyFocusListenerAncho implements FocusListener {

		@Override
		public void focusLost(FocusEvent e) {

			// Si el ancho introducido es válido
			double ancho=0;
			try {
				ancho = (Double) sueloAnchoJTField.getNumber();
			} catch (ParseException exc) {
				logger.error("Fallo en la lectura del ancho");
//				exc.printStackTrace();
			}
			
			if (ancho > 0) {
				// Si long!=null, calculo los metros Pav
				double largo=0;
				try {
					largo = (Double) sueloLongitudJTField.getNumber();
				} catch (ParseException exc) {
					logger.error("Fallo en la lectura del largo");
//					exc.printStackTrace();
				}
				double pav=0;
				try {
					pav = (Double) sueloMetrosPavJTField.getNumber();
				} catch (ParseException exc) {
					logger.error("Fallo en la lectura de los metros pavimentados");
//					exc.printStackTrace();
				}
				double noPav=0;
				try {
					noPav = (Double) sueloMetrosNoPavJTField.getNumber();
				} catch (ParseException exc) {
					logger.error("Fallo en la lectura de los no metros pavimentados");
//					exc.printStackTrace();
				}
				if (largo > 0) {
					// Si es urbano ajustamos el Pavimentado
					if (urb) {
						pav = calculaPav(ancho, largo, noPav);
						if (pav > 0) {
							sueloMetrosPavJTField.setNumber(pav);
						} else {
							// Si es negativo, ponemos el no pavimentado a 0, y
							// recalculamos el pavimentado
							sueloMetrosNoPavJTField.setNumber(0);
							noPav=0;
							pav = calculaPav(ancho, largo, noPav);
							sueloMetrosPavJTField.setNumber(pav);
						}
					}
					// Si es rustico ajustamos el NoPavimentado
					if (rus) {
						noPav = calculaPav(ancho, largo, pav);
						if (noPav > 0) {
							sueloMetrosNoPavJTField.setNumber(noPav);
						} else {
							// Si es negativo, ponemos el no pavimentado a 0, y
							// recalculamos el pavimentado
							sueloMetrosPavJTField.setNumber(0);
							pav=0;
							noPav = calculaPav(ancho, largo, pav);
							sueloMetrosNoPavJTField.setNumber(noPav);
						}

					}

					calculaRegularizado(pav, noPav);
				} else if (pav > 0 || noPav > 0) {
					largo = calculaLong(ancho, pav, noPav);
					sueloLongitudJTField.setNumber(largo);
				}
			} else {
				sueloAnchoJTField.setNumber(ancho);
			}
		}

		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub

		}
	}
	
	class MyFocusListenerAlto implements FocusListener {

		@Override
		public void focusLost(FocusEvent e) {

			// Si el ancho introducido es válido
			double largo = 0;
			try {
				largo = (Double) sueloLongitudJTField.getNumber();
			} catch (ParseException exc) {
				logger.error("Fallo en la lectura del largo");
//				exc.printStackTrace();
			}
			if (largo > 0) {

				// Si long!=null, calculo los metros Pav
				double ancho = 0;
				try {
					ancho = (Double) sueloAnchoJTField.getNumber();
				} catch (ParseException exc) {
					logger.error("Fallo en la lectura del ancho");
//					exc.printStackTrace();
				}
				double pav = 0;
				try {
					pav = (Double) sueloMetrosPavJTField.getNumber();
				} catch (ParseException exc) {
					logger.error("Fallo en la lectura de los metros pavimentados");
//					exc.printStackTrace();
				}
				double noPav = 0;
				try {
					noPav = (Double) sueloMetrosNoPavJTField.getNumber();
				} catch (ParseException exc) {
					logger.error("Fallo en la lectura de los metros no pavimentados");
//					exc.printStackTrace();
				}

				if (ancho > 0) {
					// Si es urbano ajustamos el Pavimentado
					if (urb) {
						pav = calculaPav(ancho, largo, noPav);
						if (pav > 0) {
							sueloMetrosPavJTField.setNumber(pav);
						} else {
							// Si es negativo, ponemos el no pavimentado a 0, y
							// recalculamos el pavimentado
							sueloMetrosNoPavJTField.setNumber(0);
							noPav = 0;
							pav = calculaPav(ancho, largo, noPav);
							sueloMetrosPavJTField.setNumber(pav);
						}
					}
					// Si es rustico ajustamos el NoPavimentado
					if (rus) {
						noPav = calculaPav(ancho, largo, pav);
						if (noPav > 0) {
							sueloMetrosNoPavJTField.setNumber(noPav);
							;
						} else {
							// Si es negativo, ponemos el no pavimentado a 0, y
							// recalculamos el pavimentado
							sueloMetrosPavJTField.setNumber(0);
							pav = 0;
							noPav = calculaPav(ancho, largo, pav);
							sueloMetrosNoPavJTField.setNumber(noPav);
						}

					}

					calculaRegularizado(pav, noPav);
				} else if (pav > 0 || noPav > 0) {
					ancho = calculaLong(largo, pav, noPav);
					sueloAnchoJTField.setNumber(ancho);
				}
			} else {
				sueloLongitudJTField.setNumber(largo);
			}

		}

		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub

		}
	}

	class MyFocusListenerPav implements FocusListener {

		@Override
		public void focusLost(FocusEvent e) {

			double ancho = 0;
			try {
				ancho = (Double) sueloAnchoJTField.getNumber();
			} catch (ParseException exc) {
				logger.error("Fallo en la lectura del ancho");
//				exc.printStackTrace();
			}
			double largo = 0;
			try {
				largo = (Double) sueloLongitudJTField.getNumber();
			} catch (ParseException e1) {
				logger.error("Fallo en la lectura del largo");
//				e1.printStackTrace();
			}
			double pav = 0;
			try {
				pav = (Double) sueloMetrosPavJTField.getNumber();
			} catch (ParseException e1) {
				logger.error("Fallo en la lectura de los metros pavimentados");
//				e1.printStackTrace();
			}
			double noPav = 0;
			try {
				noPav = (Double) sueloMetrosNoPavJTField.getNumber();
			} catch (ParseException e1) {
				logger.error("Fallo en la lectura de los no metros pavimentados");
//				e1.printStackTrace();
			}

			if (ancho > 0) {
				if (largo > 0) {
					ancho = calculaLong(largo, pav, noPav);
					sueloAnchoJTField.setNumber(ancho);
				} else {
					largo = calculaLong(ancho, pav, noPav);
					sueloLongitudJTField.setNumber(largo);
				}
			} else if (largo > 0) {
				ancho = calculaLong(largo, pav, noPav);
				sueloAnchoJTField.setNumber(ancho);
			}

			calculaRegularizado(pav, noPav);

		}

		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub

		}
	}

	public double calculaLong(double ancho, double pav, double noPav) {
		double num = 0;
		num = (pav + noPav) / ancho;

		return num;
	}

	public double calculaPav(double ancho, double largo, double metros) {
		double num = 0;
		num = (ancho * largo) - metros;
		return num;
	}

	private void calculaRegularizado(double dato1, double dato2) {
		calculaValorMetro();
		double res = (dato1 + dato2) * valorMetro;
		sueloValorRegularizadoJTField.setNumber(res);
	}

	public JNumberTextField getinmuebleValorAdquisicionJTField() {
		return inmuebleValorAdquisicionJTField;
	}
	
	/**
	 * @param inmuebleValorAdquisicionJTField the inmuebleValorAdquisicionJTField to set
	 */
	public void setInmuebleValorAdquisicionJTField(JNumberTextField inmuebleValorAdquisicionJTField) {
		this.inmuebleValorAdquisicionJTField = inmuebleValorAdquisicionJTField;
	}
	
	


}
