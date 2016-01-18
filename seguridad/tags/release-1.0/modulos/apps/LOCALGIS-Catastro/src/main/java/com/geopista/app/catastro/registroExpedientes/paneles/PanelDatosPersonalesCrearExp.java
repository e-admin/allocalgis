package com.geopista.app.catastro.registroExpedientes.paneles;


import java.util.Hashtable;

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
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp_LCGIII;
import com.geopista.util.GeopistaFunctionUtils;
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
    private JLabel nombreCompletoPresentadorLabel;
    private JTextField nombreCompletoPresentadorJTField;

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
        nombreCompletoPresentadorLabel= new JLabel();
        nombreCompletoPresentadorJTField= new JTextField();

        nifPresentadoJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(nifPresentadoJTField, 9);
            }
        });

        nombreCompletoPresentadorJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(nombreCompletoPresentadorJTField, 60);
            }
        });

        setEditable(true);
        renombrarComponentes();

        //Inicializamos el panel con los elementos
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.setAutoscrolls(true);
        this.add(nifPresentadoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 150, 20));
        this.add(nifPresentadoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 15, 150, -1));
        this.add(nombreCompletoPresentadorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 42, 150, 30));
        this.add(nombreCompletoPresentadorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 42, 290, 20));
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
        	UtilRegistroExp_LCGIII.retrocedeCaracter(comp,maxLong);
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
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        nifPresentadoLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.PanelDatosPersonalesCrearExp.nifPresentadoLabel")));
        nombreCompletoPresentadorLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.PanelDatosPersonalesCrearExp.nombreCompletoPresentador")));
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
            hashDatos.put(ConstantesRegistroExp.expedienteNifPresentador, GeopistaFunctionUtils.completarConCeros(nifPresentadoJTField.getText(), 9));
            hashDatos.put(ConstantesRegistroExp.expedienteNombreCompletoPresentador, nombreCompletoPresentadorJTField.getText());

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

    /**
     * Metodo que recibe una hashTable en la que estan introducidos los datos a mostrar en el panel. Las keys de la hash
     * son constantes de la clase ConstantesRegistroExp que hacen referencia al nombre de los atributos del bean expediente
     *
     * @param hashDatos Hashtable con los datos almacenados
     */    
    public void inicializaDatos(Hashtable hashDatos)
    {
        nifPresentadoJTField.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteNifPresentador));
        nombreCompletoPresentadorJTField.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteNombreCompletoPresentador));
    }

}
