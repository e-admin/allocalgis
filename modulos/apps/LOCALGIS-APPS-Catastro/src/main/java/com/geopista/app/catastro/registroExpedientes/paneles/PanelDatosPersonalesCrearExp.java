/**
 * PanelDatosPersonalesCrearExp.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.paneles;


import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.intercambio.edicion.DatosPersonalesSistemaDialog;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.geopista.protocol.datosPersonales.DatosPersonales;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.I18N;

/**
 * Created by IntelliJ IDEA.
 * 
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelDatosPersonalesCrearExp extends JPanel implements IMultilingue
{
    private String etiqueta;
    private JFrame desktop;
    private JLabel nifPresentadoLabel;
    private JTextField nifPresentadoJTField;
    private JLabel digitoControlNifPresentadoLabel;
    private JTextField digitoControlNifPresentadoJTField;
    private JButton titularesButton;
	private JLabel nombreCompletoPresentadorLabel;
    private JTextField nombreCompletoPresentadorJTField;
    
    private Hashtable digitoContolDniHash= new Hashtable();

    private DatosPersonales datosPersonales=null;

	/**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public PanelDatosPersonalesCrearExp(String label)
    {
        etiqueta= label;
        iniciaPanel();
    }

    private void iniciaPanel()
    {
        //Creamos los objetos.
        nifPresentadoLabel= new JLabel();
        nifPresentadoJTField= new JTextField();
        digitoControlNifPresentadoLabel= new JLabel();
        digitoControlNifPresentadoJTField= new JTextField();
        titularesButton = new JButton();
        nombreCompletoPresentadorLabel= new JLabel();
        nombreCompletoPresentadorJTField= new JTextField();
        
        titularesButton.setIcon(UtilRegistroExp.iconoZoom);

        nifPresentadoJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
            	limpiarDigitoControlNifPresentadoJTField(nifPresentadoJTField,digitoControlNifPresentadoJTField,8);
                chequeaLongCampoEdit(nifPresentadoJTField, 8);
               // chequeaLongCampo(nifPresentadoJTField,8);
                chequeaCompletadoCampoNif(nifPresentadoJTField.getText(), 8);
            }
        });
        

        nombreCompletoPresentadorJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(nombreCompletoPresentadorJTField, 60);
            }
        });

        digitoControlNifPresentadoJTField.setEditable(false);
        setEditable(true);
        renombrarComponentes();

        //Inicializamos el panel con los elementos
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.setAutoscrolls(true);
        this.add(nifPresentadoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 150, 20));
        this.add(nifPresentadoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 15, 80, -1));
        this.add(digitoControlNifPresentadoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 15, 100, 30));
        this.add(digitoControlNifPresentadoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 15, 30, -1));
        this.add(titularesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 15, 20, 20));
        this.add(nombreCompletoPresentadorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 42, 150, 30));
        this.add(nombreCompletoPresentadorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 42, 290, 20));
    }
    
  
    /**
     * Cuando se completa el valor del dni con 8 digitos, calcula el digito de control correcpondiente.
     *
     * @param comp El componente editable
     * @param maxLong El tamaño maximo
     */
    private void limpiarDigitoControlNifPresentadoJTField(final JTextComponent dni,final JTextComponent digitoControl,final int maxLong)
    {
    	if(dni.getText().length() < maxLong){
    		
    		digitoControl.setText("");
    	}
      
    }
    
    /**
     * Cuando se completa el valor del dni con 8 digitos, calcula el digito de control correcpondiente.
     *
     * @param comp El componente editable
     * @param maxLong El tamaño maximo
     */
    private void chequeaCompletadoCampoNif(String valorDni ,final int maxLong)
    {
    	boolean isBien_cif_nif = false;
    	if(valorDni.length()>0){
	    	char valorChar[] = {valorDni.charAt(0)};
	    	String str = new String(valorChar);
	    	
	    	if(valorDni.length()>1  && valorDni.length()<=maxLong){
	    		if(isNumeric(str)){
	    			// se comprueba que todos los digitos introducidos sean numericos ya que es un NIF
	    			if(!isNumeric(valorDni)){
	    				JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
	                    "Catastro.RegistroExpedientes.PanelDatosNotificacionCrearExp.msg1")+ " " +  maxLong + " " + I18N.get("RegistroExpedientes",
	                    "Catastro.RegistroExpedientes.PanelDatosNotificacionCrearExp.msg2"));
	    				 UtilRegistroExp.retrocedeCaracter(nifPresentadoJTField,valorDni.length()-1);
	    				 isBien_cif_nif= false;
	    			}
	    			else{
	    				isBien_cif_nif = true;
	    			}
	    		}
	    		else{
	    			//se comprueba que entre los digitos 1 y 8 sean numericos
	    			String  codigoCif = valorDni.substring(1, valorDni.length());
	    			if(!isNumeric(codigoCif)){
	    				JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
	                    "Catastro.RegistroExpedientes.PanelDatosNotificacionCrearExp.cif.msg2"));
	    				 UtilRegistroExp.retrocedeCaracter(nifPresentadoJTField,valorDni.length()-1);
	    				 isBien_cif_nif= false;
	    			}
	    			else{
	    				isBien_cif_nif = true;
	    			}
	    		}
	
	    	}
	
	        if(valorDni.length() == maxLong && isBien_cif_nif)
	        {
	 
	        	if(isNumeric(str)){
	        		// es un DNI/NIF
	        		digitoControlNifPresentadoJTField.setText(calcularDigitoControlNIF(Integer.valueOf(valorDni)));
	        	}
	        	else{
	        		// es un CIF
	        		digitoControlNifPresentadoJTField.setText(calcularDigitoControlCIF(valorDni));
	        	}
	        	
	            
	        }
    	}
    }
    
    /**
     * Calcula el digito de control que corresponde con el dni introducido.
     *
     * @param dni dni introducido
     * @return digitoControl letra correcpondiente al dni introducido
     */
    private String calcularDigitoControlNIF(Integer dni){
    	
    	String digitoControl = "";
    	Integer resto = new Integer( dni.intValue()%23);
    	
    	digitoControl = (String)getDigitoContolDniHash().get(resto.toString());

    	return digitoControl;
    }
    

    
    
    
    private static final String LETRAS_VALIDAS = "ABCDEFGHJKLMNPRQSUVW";

	private static final String LETRAS_VALIDAS_NUMERO = "ABEH";

	private static final String LETRAS_VALIDAS_LETRA = "CKLMNPQRSW";

	private static final Map<Integer, Character> RELACION_LETRAS_NUMEROS = new HashMap<Integer, Character>();
	static {
		RELACION_LETRAS_NUMEROS.put(0,'J');
		RELACION_LETRAS_NUMEROS.put(1,'A');
		RELACION_LETRAS_NUMEROS.put(2,'B');
		RELACION_LETRAS_NUMEROS.put(3,'C');
		RELACION_LETRAS_NUMEROS.put(4,'D');
		RELACION_LETRAS_NUMEROS.put(5,'E');
		RELACION_LETRAS_NUMEROS.put(6,'F');
		RELACION_LETRAS_NUMEROS.put(7,'G');
		RELACION_LETRAS_NUMEROS.put(8,'H');
		RELACION_LETRAS_NUMEROS.put(9,'I');
		RELACION_LETRAS_NUMEROS.put(10,'J');
	}
    /**
     * Calcula el digito de control asociado al CIF.
     *
     * @param cif CIF introducido
     * @return digitoControl letra o número que correcponde como digito de control
     */

	public String calcularDigitoControlCIF(String cif) {

		String digitoControl = "";
		int par = 0;
		int non = 0;

		int parcial;
		Integer control;
		
		String letraIni = String.valueOf(cif.charAt(0));

		if (LETRAS_VALIDAS.indexOf(letraIni.toUpperCase()) == -1) {
			 JOptionPane.showMessageDialog(nifPresentadoJTField, I18N.get("RegistroExpedientes",
			 	"Catastro.RegistroExpedientes.PanelDatosNotificacionCrearExp.cif.msg1"));
		}

		for (int i = 2; i < 8; i += 2) {
			par = par + Integer.parseInt(String.valueOf(cif.charAt(i)));
		}

		for (int i = 1; i < 9; i += 2) {
			int nn = 2 * Integer.parseInt(String.valueOf(cif.charAt(i)));
			if (nn > 9) {
				nn = 1 + (nn - 10);
			}
			non = non + nn;
		}

		parcial = par + non;

		control = (10 - (parcial % 10));

		if (LETRAS_VALIDAS_LETRA.indexOf(letraIni.toUpperCase()) != -1) {
			digitoControl = String.valueOf(RELACION_LETRAS_NUMEROS.get(control));
		}
		if (LETRAS_VALIDAS_NUMERO.indexOf(letraIni.toUpperCase()) != -1) {

			if (control == 10)
				control = 0;

			digitoControl = String.valueOf(control);
		}
		
		if ((LETRAS_VALIDAS_LETRA.indexOf(letraIni.toUpperCase()) == -1)
				&& (LETRAS_VALIDAS_NUMERO.indexOf(letraIni.toUpperCase()) == -1)) {
			if (control == 10) {
				control = 0;
			}
			digitoControl = String.valueOf(control);
		}

		return digitoControl;
	}
    
    
    
    
    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro y que el valor es numerico.
     *
     * @param comp El componente editable
     * @param maxLong El tamaño maximo
     */
    private void chequeaLongCampo(final JTextComponent comp,final int maxLong)
    {
        String numAnno= comp.getText();
        if(!numAnno.equals(""))
        {
            try
            {
                int x= Integer.parseInt(numAnno);
            }
            catch(NumberFormatException nFE)
            {
                JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosNotificacionCrearExp.msg1")+ " " +  maxLong + " " + I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosNotificacionCrearExp.msg2"));
                UtilRegistroExp.retrocedeCaracter(comp,numAnno.length()-1);
            }
            if(numAnno.length()>maxLong )
            {
                UtilRegistroExp.retrocedeCaracter(comp,maxLong);
            }
            
        }
    }
    
    private boolean isNumeric(String valor)
    {
    	boolean isNumeric = false;
        if(!valor.equals(""))
        {
            try
            {
            	Integer.parseInt(valor);
            	isNumeric = true;
            }
            catch(NumberFormatException nFE)
            {
            	isNumeric = false;
            }
            
        }
        return isNumeric;
    }
    
    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro.
     *
     * @param comp El componente editable
     * @param maxLong El tamaño maximo
     */
    private void chequeaLongCampoEdit(final JTextComponent comp,final int maxLong)
    {
        String numAnno= comp.getText();
        if(numAnno.length()>maxLong)
        {
            UtilRegistroExp.retrocedeCaracter(comp,maxLong);
        }
    }


   /**
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getDatosPersonalesPanel()
    {
        return this;
    }

    /**
     * Cambia los campos editables del panel segun el parametro pasado.
     *
     * @param edit
     * */
    public void setEditable(boolean edit)
    {
        nifPresentadoJTField.setEditable(edit);
        titularesButton.setEnabled(edit);
        nombreCompletoPresentadorJTField.setEditable(edit);
    }

    /**
     * Asigna al atributo desktop el parametro pasado al metodo.
     *
     * @param desktop JFrame
     */
    public void setDesktop(JFrame desktop)
    {
        this.desktop = desktop;
    }

    
    /**
     * Metodo que trata el evento lanzado por el boton de la busqueda de titulares y muestra 
     * un nuevo panel con los titulares encontrados con el patron de búsqueda del dni y nombre
     *
     * @param evt Evento lanzado
     * */
    public void busquedaTitularesButtonActionPerformed()
    {
        if(!nifPresentadoJTField.getText().equals("") || ! nombreCompletoPresentadorJTField.getText().equals("")){
	        DatosPersonalesSistemaDialog dialog = new DatosPersonalesSistemaDialog(nifPresentadoJTField.getText(),nombreCompletoPresentadorJTField.getText().toUpperCase());
		                   
	    	dialog.setVisible(true);  
	    	if (dialog.getDatosPersonales()!= null){
	    		if (dialog.getDatosPersonales().getNif()!=null){
	    			if (dialog.getDatosPersonales().getNif().length()== 9){
	    				nifPresentadoJTField.setText(dialog.getDatosPersonales().getNif().substring(0,8));
	    				digitoControlNifPresentadoJTField.setText(dialog.getDatosPersonales().getNif().substring(8,9));
	    			}
	    			else{
	    				nifPresentadoJTField.setText(dialog.getDatosPersonales().getNif());
	    			}
	    		}
	
	    		if (dialog.getDatosPersonales().getNombreApellidos()!=null){
	    			nombreCompletoPresentadorJTField.setText(dialog.getDatosPersonales().getNombreApellidos());
	    		}
	
	    		setDatosPersonales(dialog.getDatosPersonales());
	    	}
        }
        else{
        	JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
            	"Catastro.RegistroExpedientes.PanelDatosPersonalesCrearExp.introduzca.patronBusqueda.datosPersonales"));
        }
    }
    
    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        nifPresentadoLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.PanelDatosPersonalesCrearExp.nifPresentadoLabel")));
        digitoControlNifPresentadoLabel.setText(I18N.get("RegistroExpedientes",
        "Catastro.RegistroExpedientes.PanelDatosPersonalesCrearExp.digitoControlnifPresentadoLabel"));
        nombreCompletoPresentadorLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.PanelDatosPersonalesCrearExp.nombreCompletoPresentador")));
        titularesButton.setToolTipText(I18N.get("RegistroExpedientes",
        "Catastro.RegistroExpedientes.PanelDatosPersonalesCrearExp.titularesBusquedaButton.hint"));
    }

    /**
     * Recopila los datos que el usuario ha introducido. Los datos los almacena en la hash pasada por parametro, en
     * la que la clave es una constante en ConstantesRegistroExp que hace referencia a un atributo del bean
     * expediente y el valor lo que el usuario haya introducido.
     *
     * @param hashDatos Hashtable donde se introducen los datos con keys almacenadas en la clase contantesRegistroExp
     * @return boolean Indica si no se ha producido ningun fallo
     */
    public boolean recopilaDatosPanel(Hashtable hashDatos)
    {
        if(checkeaParametrosNecesarios())
        {
        	String dniCompletado = GeopistaUtil.completarConCeros(nifPresentadoJTField.getText(),8);
        	chequeaCompletadoCampoNif(dniCompletado, 8);
            hashDatos.put(ConstantesCatastro.expedienteNifPresentador, dniCompletado);
        	hashDatos.put(ConstantesCatastro.expedienteDigitoControlNifPresentador, digitoControlNifPresentadoJTField.getText());
            hashDatos.put(ConstantesCatastro.expedienteNombreCompletoPresentador, nombreCompletoPresentadorJTField.getText());
            
            return true;
        }
        else
        {
        		JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosPersonalesCrearExp.msg1"));
            return false;
        }
    }

    /**
     * Comprueba que los campos necesarios, marcados con asteriscos rojos, no son null o vacios.
     *
     * @return boolean El resultado de la comprobacion
     */
    private boolean checkeaParametrosNecesarios()
    {
//    	boolean okNif=this.checkeaNif(nifPresentadoJTField.getText());
    	boolean okNif=true;
    	if(!okNif)
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.PanelDatosPersonalesCrearExp.msg2"));
        return   okNif &&(!nombreCompletoPresentadorJTField.getText().equals(""));
    }
    
    /**
     * Comprueba que el campo NIF es correcto, incluida la letra del mismo si está presente.
     *
     * @return boolean El resultado de la comprobacion
     */
    private boolean checkeaNif(String sNif)
    {
    	boolean okNif=false;
//    	char letraAux;
//    	StringBuffer sNumeros= new StringBuffer();
//    	int i,ndni;
//    	char tabla[]={'T','R','W','A','G','M','Y','F','P','D','X','B','N','J','Z','S','Q','V','H','L','C','K','E'};
//    	if(sNif!=null && sNif.length()>0){
//	    	for (i=0;i<sNif.length();i++) {
//		    	if ("1234567890".indexOf(sNif.charAt(i))!=-1) {
//		    	sNumeros.append(sNif.charAt(i));
//		    	}else{
//		    		break;
//		    	}
//	    	}
//	    	if(i==sNif.length()-1 || i==sNif.length()){ //si no hay letra o si sí la hay y está en última posición
//	    		if(i==sNif.length()-1){ //si hay letra al final
//			    	try{
//				    	ndni=Integer.parseInt(sNumeros.toString());
//				    	letraAux=Character.toUpperCase(sNif.charAt(sNif.length()-1));		
//				    	char letra=tabla[ndni%23];
//				    	if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(letraAux)!=-1) {
//				    	   if((sNumeros.toString()).length()<=8){
//				    	   if (letraAux==letra && (sNumeros.toString()).length()<=8) // letra adecuada para el NIF
//				    		   okNif=true;
//				    	   }
//				    	} 
//				    }catch(Exception e){  
//				    	okNif=false;
//				    }
//	    		}
//	    		if(i==sNif.length() && (sNumeros.toString()).length()<=8){//si no hay letra y el tamaño es menor o igual a 8
//	    			okNif=true;
//	    		}
//	    	}
//    	}
    	return true;
    }
    
    public Hashtable getDigitoContolDniHash() {
		return digitoContolDniHash;
	}

	public void setDigitoContolDniHash(Hashtable digitoContolDniHash) {
		this.digitoContolDniHash = digitoContolDniHash;
	}

    /**
     * Metodo que recibe una hashTable en la que estan introducidos los datos a mostrar en el panel. Las keys de la hash
     * son constantes de la clase ConstantesRegistroExp que hacen referencia al nombre de los atributos del bean expediente
     *
     * @param hashDatos Hashtable con los datos almacenados
     */    
    public void inicializaDatos(Hashtable hashDatos)
    {
        nifPresentadoJTField.setText((String)hashDatos.get(ConstantesCatastro.expedienteNifPresentador));
        digitoControlNifPresentadoJTField.setText((String)hashDatos.get(ConstantesCatastro.expedienteDigitoControlNifPresentador));
        nombreCompletoPresentadorJTField.setText((String)hashDatos.get(ConstantesCatastro.expedienteNombreCompletoPresentador));
    }


	public DatosPersonales getDatosPersonales() {
		return datosPersonales;
	}

	public void setDatosPersonales(DatosPersonales datosPersonales) {
		this.datosPersonales = datosPersonales;
	}
	
	public JButton getTitularesButton() {
		return titularesButton;
	}

	public void setTitularesButton(JButton titularesButton) {
		this.titularesButton = titularesButton;
	}


}
