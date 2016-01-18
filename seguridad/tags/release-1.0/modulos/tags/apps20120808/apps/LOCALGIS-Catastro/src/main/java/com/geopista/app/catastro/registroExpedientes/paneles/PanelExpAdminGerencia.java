package com.geopista.app.catastro.registroExpedientes.paneles;

import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp_LCGIII;
import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 25-ene-2007
 * Time: 10:14:47
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelExpAdminGerencia  extends JPanel implements IMultilingue {
    private String etiqueta;
    private JLabel annoExpedienteGerenciaLabel;
    private JTextField annoExpedienteGerenciaJTField;
    private JLabel referenciaExpedienteGerenciaLabel;
    private JTextField referenciaExpedienteGerenciaJTField;
    private JLabel codigoEntidadRegistroDGCOrigenAltLabel;
    private JTextField codigoEntidadRegistroDGCOrigenAltJTField;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public PanelExpAdminGerencia(String label)
    {
        etiqueta= label;
        inicializaPanel();
    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     */
    private void inicializaPanel()
    {
        //creacion de objetos.
        annoExpedienteGerenciaLabel = new JLabel();
        annoExpedienteGerenciaJTField = new JTextField();
        referenciaExpedienteGerenciaLabel = new JLabel();
        referenciaExpedienteGerenciaJTField = new JTextField();
        codigoEntidadRegistroDGCOrigenAltLabel = new JLabel();
        codigoEntidadRegistroDGCOrigenAltJTField = new JTextField();

        //Inicializacion de objetos.
        inicializarEventos();
        setEditable(true);
        renombrarComponentes();

        //Inicializamos el panel con los elementos.
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(annoExpedienteGerenciaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 235, 20));
        this.add(annoExpedienteGerenciaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 15, 150, -1));
        this.add(referenciaExpedienteGerenciaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 37, 235, 20));
        this.add(referenciaExpedienteGerenciaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 37, 150, -1));
        this.add(codigoEntidadRegistroDGCOrigenAltLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 59, 235, 20));
        this.add(codigoEntidadRegistroDGCOrigenAltJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 59, 150, -1));
    }

    /**
     * Metodo que inicializa los eventos que producen los botones y los paneles editables para comprobar que los datos
     * introducidos son correctos.
     */
    private void inicializarEventos()
    {
        annoExpedienteGerenciaJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaAnnoExpedienteOrigen(annoExpedienteGerenciaJTField);
            }
        });

        referenciaExpedienteGerenciaJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(referenciaExpedienteGerenciaJTField,8);
            }
        });

        codigoEntidadRegistroDGCOrigenAltJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongYNumCampoEdit(codigoEntidadRegistroDGCOrigenAltJTField,3);
            }
        });
    }

    /**
     * Metodo que checkea si los datos introducidos en un campo editable son un año a o no. Comprueba que no supere el
     * tamaño 4, que sea numero y si son 4 digitos que sea mayor que 1000.
     *
     * @param comp El componente
     * */
    private void chequeaAnnoExpedienteOrigen(final JTextComponent comp)
    {
        String numAnno= comp.getText();
        if(!numAnno.equals(""))
        {
            int x=-1;
            try
            {
                x= Integer.parseInt(numAnno);
            }
            catch(NumberFormatException nFE)
            {
                JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg1"));
                UtilRegistroExp_LCGIII.retrocedeCaracter(comp,numAnno.length()-1);
            }
            if(numAnno.length()>4 )
            {
            	UtilRegistroExp_LCGIII.retrocedeCaracter(comp, 4);
            }
            if(numAnno.length()==4 && x<=1000 && x!=-1)
            {
                JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg2"));
            }
        }
    }

    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro y que solo contiene numeros.
     *
     * @param comp  El componente editable
     * @param maxLong El tamaño maximo
     */
    private void chequeaLongYNumCampoEdit(final JTextComponent comp, final int maxLong)
    {
        String numAnno= comp.getText();
        if(!numAnno.equals(""))
        {
            int x=-1;
            try
            {
                x= Integer.parseInt(numAnno);
            }
            catch(NumberFormatException nFE)
            {
                JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg3")+ " " +  maxLong + " " + I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg4"));
                UtilRegistroExp_LCGIII.retrocedeCaracter(comp,numAnno.length()-1);
            }
            if(numAnno.length()>maxLong )
            {
            	UtilRegistroExp_LCGIII.retrocedeCaracter(comp, maxLong);
            }
        }
    }

    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro.
     *
     * @param comp El componente
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
    public JPanel getDatosPanel()
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
        annoExpedienteGerenciaJTField.setEditable(edit);
        referenciaExpedienteGerenciaJTField.setEditable(edit);
        codigoEntidadRegistroDGCOrigenAltJTField.setEditable(edit);
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        annoExpedienteGerenciaLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelExpAdminGerencia.annoExpedienteGerenciaLabel"));
        referenciaExpedienteGerenciaLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelExpAdminGerencia.referenciaExpedienteGerenciaLabel"));
        codigoEntidadRegistroDGCOrigenAltLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelExpAdminGerencia.codigoEntidadRegistroDGCOrigenAltLabel"));
    }

    /**
     * Recopila los datos que el usuario ha introducido. Los datos los almacena en la hash pasada por parametro, en
     * la que la clave es una constante en ConstantesRegistroExp que hace referencia a un atributo del bean
     * expediente y el valor lo que el usuario haya introducido.
     *
     * @param hashDatos Hashtable donde se introducen los datos con keys almacenadas en la clase contantesRegistroExp
     * @return boolean Indica si no se ha producido ningun fallo
     */
    public boolean recopilaDatosPanel(Hashtable hashDatos) {

    	if(annoExpedienteGerenciaJTField.getText().length()==0 || annoExpedienteGerenciaJTField.getText().length()==4){
    		if(validateFields()){
    			if (annoExpedienteGerenciaJTField.getText().length()!=0){
    				Integer codigoEntidad = new Integer(codigoEntidadRegistroDGCOrigenAltJTField.getText());
    				if (codigoEntidad.intValue() == -1 || (codigoEntidad.intValue() >= 900 && codigoEntidad.intValue()<=999)){
    					hashDatos.put(ConstantesRegistroExp.expedienteAnnoExpedienteGerencia, annoExpedienteGerenciaJTField.getText());
    					hashDatos.put(ConstantesRegistroExp.expedienteReferenciaExpedienteGerencia, referenciaExpedienteGerenciaJTField.getText());
    					hashDatos.put(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion, codigoEntidadRegistroDGCOrigenAltJTField.getText());
    					return true;
    				}
    				else{
    					JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
    					"Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg7"));
    				}
    			}
    			else{
    				hashDatos.put(ConstantesRegistroExp.expedienteAnnoExpedienteGerencia, annoExpedienteGerenciaJTField.getText());
    				hashDatos.put(ConstantesRegistroExp.expedienteReferenciaExpedienteGerencia, referenciaExpedienteGerenciaJTField.getText());
    				hashDatos.put(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion, codigoEntidadRegistroDGCOrigenAltJTField.getText());
    				return true;

    			}    		
    		}
    		else JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
    		"Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg6"));
    	}

    	else JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
    	"Catastro.RegistroExpedientes.PanelExpAdminGerencia.msg5"));        
    	return false;
    }

    /**
     * Metodo que recibe una hashTable en la que estan introducidos los datos a mostrar en el panel. Las keys de la hash
     * son constantes de la clase ConstantesRegistroExp que hacen referencia al nombre de los atributos del bean expediente
     *
     * @param hashDatos Hashtable con los datos almacenados
     */
    public void inicializaDatos(Hashtable hashDatos)
    {
        annoExpedienteGerenciaJTField.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteAnnoExpedienteGerencia));
        referenciaExpedienteGerenciaJTField.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteReferenciaExpedienteGerencia));
        codigoEntidadRegistroDGCOrigenAltJTField.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteCodigoEntidadRegistroDGCOrigenAlteracion));
    }
    
    
    
    /**Comprueba que se han introducido o bien los tres campos o bien ninguno de los tres
     */
    private boolean validateFields(){
    	if(annoExpedienteGerenciaJTField.getText()!=null&&
    			referenciaExpedienteGerenciaJTField.getText()!=null&&
    			codigoEntidadRegistroDGCOrigenAltJTField!=null){
    		if((annoExpedienteGerenciaJTField.getText().equals("")&&
    				referenciaExpedienteGerenciaJTField.getText().equals("")&&
    				codigoEntidadRegistroDGCOrigenAltJTField.getText().equals(""))||
    				(!annoExpedienteGerenciaJTField.getText().equals("")&&
    	    				!referenciaExpedienteGerenciaJTField.getText().equals("")&&
    	    				!codigoEntidadRegistroDGCOrigenAltJTField.getText().equals("")))
    			return true;
    	}//fin del if    		
    	
    	else if (annoExpedienteGerenciaJTField.getText()==null&&
    			referenciaExpedienteGerenciaJTField.getText()==null&&
    			codigoEntidadRegistroDGCOrigenAltJTField==null)
    		return true;
    	
    	
    	return false;
    }//fin del método
    
    
}//fin de la clase
