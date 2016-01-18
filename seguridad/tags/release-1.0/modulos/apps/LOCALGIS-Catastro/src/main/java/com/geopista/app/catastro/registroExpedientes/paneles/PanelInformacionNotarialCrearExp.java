package com.geopista.app.catastro.registroExpedientes.paneles;

import java.util.Hashtable;

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
import com.vividsolutions.jump.I18N;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 09-ene-2007
 * Time: 16:16:13
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelInformacionNotarialCrearExp extends JPanel implements IMultilingue {
    private String etiqueta;
    private JLabel codigoProvinciaLabel;
    private JTextField codigoProvinciaJTfield;
    private JLabel codigoPoblacionLabel;
    private JTextField codigoPoblacionJTfield;
    private JLabel codigoNotariaLabel;
    private JTextField codigoNotariaJTfield;
    private JLabel annoLabel;
    private JTextField annoJTfield;
    private JLabel protocoloLabel;
    private JTextField protocoloJTfield;    

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public PanelInformacionNotarialCrearExp(String label)
    {
        etiqueta= label;
        inicializaPanel();
    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     */
    private void inicializaPanel()
    {
        codigoProvinciaLabel = new JLabel();
        codigoProvinciaJTfield = new JTextField();
        codigoPoblacionLabel = new JLabel();
        codigoPoblacionJTfield = new JTextField();
        codigoNotariaLabel = new JLabel();
        codigoNotariaJTfield = new JTextField();
        annoLabel = new JLabel();
        annoJTfield = new JTextField();
        protocoloLabel = new JLabel();
        protocoloJTfield = new JTextField();

        inicializarEventos();
        setEditable(true);
        renombrarComponentes();

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(codigoProvinciaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 130, 20));
        this.add(codigoProvinciaJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 15, 100, -1));
        this.add(annoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 15, 130, 20));
        this.add(annoJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 15, 100, -1));
        this.add(codigoPoblacionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 37, 130, 20));
        this.add(codigoPoblacionJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 37, 100, -1));
        this.add(protocoloLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 37, 130, 20));
        this.add(protocoloJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 37, 100, -1));
        this.add(codigoNotariaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 59, 130, 20));
        this.add(codigoNotariaJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 59, 100, -1));
    }

    /**
     * Metodo que inicializa los eventos que producen los botones y los paneles editables para comprobar que los datos
     * introducidos son correctos.
     */
    private void inicializarEventos()
    {
        codigoProvinciaJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(codigoProvinciaJTfield,2);
            }
        });

        codigoPoblacionJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(codigoPoblacionJTfield,4);
            }
        });

        codigoNotariaJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(codigoNotariaJTfield,3);
            }
        });

        annoJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaAnnoExpedienteOrigen(annoJTfield);
            }
        });

        protocoloJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(protocoloJTfield,6);
            }
        });
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
                        "Catastro.RegistroExpedientes.PanelInformacionNotarialCrearExp.msg1"));
                UtilRegistroExp_LCGIII.retrocedeCaracter(comp,numAnno.length()-1);
            }
            if(numAnno.length()>4 )
            {
            	UtilRegistroExp_LCGIII.retrocedeCaracter(comp,4);
            }
            if(numAnno.length()==4 && x<=1000 && x!=-1)
            {
                JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelInformacionNotarialCrearExp.msg2"));
            }
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
        codigoProvinciaJTfield.setEditable(edit);
        codigoPoblacionJTfield.setEditable(edit);
        codigoNotariaJTfield.setEditable(edit);
        annoJTfield.setEditable(edit);
        protocoloJTfield.setEditable(edit);
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        codigoProvinciaLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelInformacionNotarialCrearExp.codigoProvinciaLabel"));
        codigoPoblacionLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelInformacionNotarialCrearExp.codigoPoblacionLabel"));
        codigoNotariaLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelInformacionNotarialCrearExp.codigoNotariaLabel"));
        annoLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelInformacionNotarialCrearExp.annoLabel"));
        protocoloLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelInformacionNotarialCrearExp.protocoloLabel"));
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
    	if(!codigoProvinciaJTfield.getText().equals("") && 
    			!codigoPoblacionJTfield.getText().equals("") &&
    			!codigoNotariaJTfield.getText().equals("") &&
    			!annoJTfield.getText().equals("") &&
    			!protocoloJTfield.getText().equals("")){
    		
    		if(codigoProvinciaJTfield.getText().length()!=2){
    			JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
                "Catastro.RegistroExpedientes.PanelInformacionNotarialCrearExp.msg5"));
    			return false;
    		}
    		
    		if((annoJTfield.getText().length()==0) || (annoJTfield.getText().length()==4))
    	    {
    	            hashDatos.put(ConstantesRegistroExp.expedienteCodProvinciaNotaria, codigoProvinciaJTfield.getText());
    	            hashDatos.put(ConstantesRegistroExp.expedienteCodPoblacionNotaria, codigoPoblacionJTfield.getText());
    	            hashDatos.put(ConstantesRegistroExp.expedienteCodNotaria, codigoNotariaJTfield.getText());
    	            hashDatos.put(ConstantesRegistroExp.expedienteAnnoProtocoloNotarial, annoJTfield.getText());
    	            hashDatos.put(ConstantesRegistroExp.expedienteProtocoloNotarial, protocoloJTfield.getText());

    	            return true;
    	    }
    		JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
            "Catastro.RegistroExpedientes.PanelInformacionNotarialCrearExp.msg3"));
    	}
    	else{
    		if(codigoProvinciaJTfield.getText().equals("") && 
        			codigoPoblacionJTfield.getText().equals("") &&
        			codigoNotariaJTfield.getText().equals("") &&
        			annoJTfield.getText().equals("") &&
        			protocoloJTfield.getText().equals("")){
    			
    			 hashDatos.put(ConstantesRegistroExp.expedienteCodProvinciaNotaria, codigoProvinciaJTfield.getText());
    	         hashDatos.put(ConstantesRegistroExp.expedienteCodPoblacionNotaria, codigoPoblacionJTfield.getText());
    	         hashDatos.put(ConstantesRegistroExp.expedienteCodNotaria, codigoNotariaJTfield.getText());
    	         hashDatos.put(ConstantesRegistroExp.expedienteAnnoProtocoloNotarial, annoJTfield.getText());
    	         hashDatos.put(ConstantesRegistroExp.expedienteProtocoloNotarial, protocoloJTfield.getText());
    			
    	         return true;
    		}
    		else{
    			
    			  JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
    			  		"Catastro.RegistroExpedientes.PanelInformacionNotarialCrearExp.msg4"));
    		}
    		
    	}
    	
    	
    	
    	
//        if((annoJTfield.getText().length()==0) || (annoJTfield.getText().length()==4))
//        {
//            hashDatos.put(ConstantesRegistroExp.expedienteCodProvinciaNotaria, codigoProvinciaJTfield.getText());
//            hashDatos.put(ConstantesRegistroExp.expedienteCodPoblacionNotaria, codigoPoblacionJTfield.getText());
//            hashDatos.put(ConstantesRegistroExp.expedienteCodNotaria, codigoNotariaJTfield.getText());
//            hashDatos.put(ConstantesRegistroExp.expedienteAnnoProtocoloNotarial, annoJTfield.getText());
//            hashDatos.put(ConstantesRegistroExp.expedienteProtocoloNotarial, protocoloJTfield.getText());
//
//            return true;
//        }
//        JOptionPane.showMessageDialog(this, I18N.get("RegistroExpedientes",
//                        "Catastro.RegistroExpedientes.PanelInformacionNotarialCrearExp.msg3"));
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
        codigoProvinciaJTfield.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteCodProvinciaNotaria));
        codigoPoblacionJTfield.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteCodPoblacionNotaria));
        codigoNotariaJTfield.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteCodNotaria));
        annoJTfield.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteAnnoProtocoloNotarial));
        protocoloJTfield.setText((String)hashDatos.get(ConstantesRegistroExp.expedienteProtocoloNotarial));
    }
}
