package com.geopista.app.catastro.registroExpedientes.paneles;

import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp_LCGIII;
import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.border.TitledBorder;
import java.util.Hashtable;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 19-dic-2006
 * Time: 11:53:08
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario.
 */

public class PanelDatosNotificacionCrearExp extends JPanel implements IMultilingue {
    private String etiqueta;
    private JLabel nombreProvinciaLabel;
    private JComboBox nombreProvinciaJCBox;
    private JLabel nombreMunicipioLabel;
    private JComboBox nombreMunicipioJCBox;
    private JLabel nombreEntidadMenorLabel;
    private JTextField nombreEntidadMenorJTField;
    private JLabel codigoPostLabel;
    private JTextField codigoPostJTField;
    private JLabel apartadoCorreoLabel;
    private JTextField apartadoCorreoJTField;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public PanelDatosNotificacionCrearExp(String label)
    {
        etiqueta = label;
        inicializaPanel();
    }

    /**
     *  Inicializa todos los elementos del panel y los coloca en su posicion.
     */
    private void inicializaPanel()
    {
        nombreProvinciaLabel = new JLabel();
        nombreProvinciaJCBox = new JComboBox();
        nombreMunicipioLabel = new JLabel();
        nombreMunicipioJCBox = new JComboBox();
        nombreEntidadMenorLabel = new JLabel();
        nombreEntidadMenorJTField = new JTextField();
        codigoPostLabel = new JLabel();
        codigoPostJTField = new JTextField();
        apartadoCorreoLabel = new JLabel();
        apartadoCorreoJTField = new JTextField();

        nombreProvinciaJCBox.setEditable(false);
        nombreMunicipioJCBox.setEditable(false);

        inicializarEventos();
        setEditable(true);
        renombrarComponentes();

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(codigoPostLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 150, 20));
        this.add(codigoPostJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 15, 50, -1));
        this.add(apartadoCorreoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 15, 135, 20));
        this.add(apartadoCorreoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(415, 15, 65, -1));

        this.add(nombreProvinciaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 38, 150, 20));
        this.add(nombreProvinciaJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 38, 290, -1));
        this.add(nombreMunicipioLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 61, 150, 20));
        this.add(nombreMunicipioJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 61, 290, -1));

        this.add(nombreEntidadMenorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 84, 150, 20));
        this.add(nombreEntidadMenorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 84, 290, -1));
    }
    /**
     * Metodo que inicializa los eventos de los botones y de los campos editables para comprobar la valided de los
     * datos introducidos.
     */
    private void inicializarEventos()
    {
        codigoPostJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongInt(codigoPostJTField,5);
            }
        });

        apartadoCorreoJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongInt(apartadoCorreoJTField,5);
            }
        });

        nombreEntidadMenorJTField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(nombreEntidadMenorJTField,25);
            }
        });

    }

    /**
     * Checkea que el valor introducido en el campo no excede la longitud pasada
     * por parametro y que el valor es numerico.
     *
     * @param comp El componente editable
     * @param maxLong El tamaño maximo
     */
    private void chequeaLongInt(final JTextComponent comp,final int maxLong)
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
                UtilRegistroExp_LCGIII.retrocedeCaracter(comp,numAnno.length()-1);
            }
            if(numAnno.length()>maxLong )
            {
            	UtilRegistroExp_LCGIII.retrocedeCaracter(comp,maxLong);
            }
        }
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
     * Devuelve el comboBox de provincias
     *
     * @return JComboBox nombreProvinciaJCBox
     */
    public JComboBox getNombreProvinciaJCBox()
    {
        return nombreProvinciaJCBox;
    }

    /**
     * Devuelve el comboBox de municipios
     *
     * @return JComboBox nombreMunicipioJCBox
     */
    public JComboBox getNombreMunicipioJCBox()
    {
        return nombreMunicipioJCBox;
    }

   /**
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getDatosNotifPanel()
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
        nombreEntidadMenorJTField.setEditable(edit);
        codigoPostJTField.setEditable(edit);
        apartadoCorreoJTField.setEditable(edit);
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        nombreProvinciaLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosNotificacionCrearExp.nombreProvinciaLabel")));
        nombreMunicipioLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosNotificacionCrearExp.nombreMunicipioLabel")));
        nombreEntidadMenorLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosNotificacionCrearExp.nombreEntidadMenorLabel"));
        codigoPostLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosNotificacionCrearExp.codigoPostLabel")));
        apartadoCorreoLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosNotificacionCrearExp.apartadoCorreoLabel"));
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
            hashDatos.put(ConstantesRegistroExp.direccionNombreProvincia, nombreProvinciaJCBox.getSelectedItem());
            hashDatos.put(ConstantesRegistroExp.direccionNombreMunicipio, nombreMunicipioJCBox.getSelectedItem());
            hashDatos.put(ConstantesRegistroExp.direccionNombreEntidadMenor, nombreEntidadMenorJTField.getText());
            hashDatos.put(ConstantesRegistroExp.direccionCodigoPostal, codigoPostJTField.getText());
            hashDatos.put(ConstantesRegistroExp.direccionApartadoCorreos, apartadoCorreoJTField.getText());
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosNotificacionCrearExp.msg3"));
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
        return  (!codigoPostJTField.getText().equals(""))&&(nombreProvinciaJCBox.getSelectedItem()!=null)
                &&(!((String)nombreProvinciaJCBox.getSelectedItem()).equals(""))&&(nombreMunicipioJCBox.getSelectedItem()!=null)
                &&(!((String)nombreMunicipioJCBox.getSelectedItem()).equals(""));
    }

   /**
     * Metodo que recibe una hashTable en la que estan introducidos los datos a mostrar en el panel. Las keys de la hash
     * son constantes de la clase ConstantesRegistroExp que hacen referencia al nombre de los atributos del bean expediente
     *
     * @param hashDatos Hashtable con los datos almacenados
     */
    public void inicializaDatos(Hashtable hashDatos)
    {
        if(nombreProvinciaJCBox.getItemCount()==0)
            nombreProvinciaJCBox.addItem((String)hashDatos.get(ConstantesRegistroExp.direccionNombreProvincia ));
        
       
         nombreProvinciaJCBox.setSelectedItem((String)hashDatos.get(ConstantesRegistroExp.direccionNombreProvincia ));
       
        if(nombreMunicipioJCBox.getItemCount()==0)
            nombreMunicipioJCBox.addItem((String)hashDatos.get(ConstantesRegistroExp.direccionNombreMunicipio));
        
        nombreMunicipioJCBox.setSelectedItem((String)hashDatos.get(ConstantesRegistroExp.direccionNombreMunicipio));            
        
        nombreEntidadMenorJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionNombreEntidadMenor));
        codigoPostJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionCodigoPostal));

        String apartadoCorreos = (String)hashDatos.get(ConstantesRegistroExp.direccionApartadoCorreos);
        if(!apartadoCorreos.equalsIgnoreCase("-1"))
            apartadoCorreoJTField.setText((String)hashDatos.get(ConstantesRegistroExp.direccionApartadoCorreos));
    }

    /**
     * Carga las provincias pasadas por parametro en comboBox adecuado
     *
     * @param provincias HashTable con dos arrays, uno con los nombres y otro con los codigos. La key es nombres
     */
    public void cargaProvincias(Hashtable provincias)
    {
        nombreProvinciaJCBox.removeAllItems();
        ArrayList nombres = (ArrayList)provincias.get("nombres");
        
        if(nombres.size()>0)
        	nombreProvinciaJCBox.addItem("");
        
        for(int i = 0;i<nombres.size();i++)
            nombreProvinciaJCBox.addItem(nombres.get(i));
        
    }//fin del método

    /**
     * Carga los municipios pasadas por parametro en comboBox adecuado
     *
     * @param municipios HashTable con dos arrays, uno con los nombres y otro con los codigos. La key es nombres
     */
    public void cargaMunicipios(Hashtable municipios){
    	nombreMunicipioJCBox.removeAllItems();
	    
	    if(this.nombreProvinciaJCBox.getSelectedItem()!=null && 
	    		!((String)this.nombreProvinciaJCBox.getSelectedItem()).equals("")){
	       //System.out.println("Se rellenan los municipios en el combo\n");
	        ArrayList nombres = (ArrayList)municipios.get("nombres");
	        for(int i = 0;i<nombres.size();i++)
	            nombreMunicipioJCBox.addItem(nombres.get(i));
	       
	    }//fin if
	    /*else
	    	System.out.println("No se rellenan los municipios en el combo, porque no hay provincia seleccionada\n");*/
    }//fin del método

    /**
     * Devuelve el indice de la provincia selecionada
     *
     * @return int El indice de la provincia selecionada
     */
    public int getProvinciaSelect(){
    	//System.out.println("Index seleccionado en provincia: "+nombreProvinciaJCBox.getSelectedIndex());
        return nombreProvinciaJCBox.getSelectedIndex()-1;
    }

    /**
     * Devuelve el indice del municipio selecionada
     *
     * @return int El indice del municipio selecionada
     */
    public int getMunicipioSelect(){
    	//System.out.println("Index seleccionado en municipio: "+nombreMunicipioJCBox.getSelectedIndex());
        return nombreMunicipioJCBox.getSelectedIndex();
    }

}
