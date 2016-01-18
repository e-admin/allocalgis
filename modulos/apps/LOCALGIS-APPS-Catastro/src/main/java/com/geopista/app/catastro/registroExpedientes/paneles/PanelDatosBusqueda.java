/**
 * PanelDatosBusqueda.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.registroExpedientes.paneles;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
import com.geopista.app.catastro.model.beans.TipoExpediente;
import com.geopista.app.catastro.registroExpedientes.utils.Estructuras;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExpCatastro_LCGIII;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.vividsolutions.jump.I18N;


/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 22-ene-2007
 * Time: 12:19:45
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones, o metodos para recoger los datos que ha introducido el usuario. Posee una hashTable con los
 * datos de los usuarios para ser visualizados, y dos arrayList con los codigos de las entidades generadoras y
 * y los codigos de estado.
 */

public class PanelDatosBusqueda extends JPanel implements IMultilingue {
    private String etiqueta;
    private JFrame desktop;
    private JLabel numExpedienteLabel;
    private JTextField numExpedienteJTfield;
    private JLabel tipoDeExpedienteLabel;
    private JComboBox tipoDeExpedienteCBoxE;
    private JLabel idEstadoLabel;
    private JComboBox idEstadoCBox;
    private JLabel tecnicoCatastroLabel;
    private JComboBox tecnicoCatastroCBoxE;
    private JLabel fechaRegistroLabel;
    private JTextField fechaRegistroInicialJTfield;
    private JButton fechaRegistroInicialJButton;
    private JTextField fechaRegistroFinalJTfield;
    private JButton fechaRegistroFinalJButton;
    private JLabel annoExpedienteAdminOrigenAlt_EjercicioLabel;
    private JTextField annoExpedienteAdminOrigenAlt_EjercicioJTfield;
    private JLabel codigoEntidadGeneradora_ControlLabel;
    private JComboBox codigoEntidadGeneradora_ControlCBoxE;
    private JLabel referenciaCatastralParcela1Label;
    private JTextField referenciaCatastralParcela1JTfield;
    private JLabel referenciaCatastralParcela2Label;
    private JTextField referenciaCatastralParcela2JTfield;
    private JLabel nifPresentadorLabel;
    private JTextField nifPresentadorJTfield;
    private JLabel nombreCompletoPresentadorLabel;
    private JTextField nombreCompletoPresentadorJTfield;

    private Hashtable usuarios;
    private ArrayList entGen;
    private ArrayList codigoEst;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama. Tambien recibe por parametro las hash con los usuarios
     * y los arrayList de los codigos de las entidades Generadoras y los estados. Inicializa y gui y carga los datos
     * en los componentes de la clase.
     *
     * @param label  Etiqueta del borde del panel
     * @param usuarios Hash con el nombre de usuarios y el id de usuario como key
     * @param entGen   Nombres de las entidades generadoras almacenadas en BD
     * @param codigoEst Codigos de los estados de expediente
     */
    public PanelDatosBusqueda(String label, Hashtable usuarios, ArrayList entGen, ArrayList codigoEst)
    {
        etiqueta= label;
        this.usuarios= usuarios;
        this.entGen= entGen;
        this.codigoEst= codigoEst;
        inicializaPanel();
        if(this.usuarios!=null&&this.entGen!=null && this.codigoEst!=null)
        {
            cargaEstructurasBD();
        }
    }

    /**
     *  Inicializa todos los elementos del panel, asocia los eventos de los elementos y los coloca en su posicion.
     */
    private void inicializaPanel()
    {
        numExpedienteLabel = new JLabel();
        numExpedienteJTfield = new JTextField();
        tipoDeExpedienteLabel = new JLabel();
        tipoDeExpedienteCBoxE = new JComboBox();
        idEstadoLabel = new JLabel();
        idEstadoCBox = new JComboBox();
        tecnicoCatastroLabel = new JLabel();
        tecnicoCatastroCBoxE = new JComboBox();
        fechaRegistroLabel = new JLabel();
        fechaRegistroInicialJTfield = new JTextField();
        fechaRegistroInicialJButton= new JButton();
        fechaRegistroFinalJTfield = new JTextField();
        fechaRegistroFinalJButton= new JButton();
        annoExpedienteAdminOrigenAlt_EjercicioLabel = new JLabel();
        annoExpedienteAdminOrigenAlt_EjercicioJTfield = new JTextField();
        codigoEntidadGeneradora_ControlLabel = new JLabel();
        codigoEntidadGeneradora_ControlCBoxE = new JComboBox();
        referenciaCatastralParcela1Label = new JLabel();
        referenciaCatastralParcela1JTfield = new JTextField();
        referenciaCatastralParcela2Label = new JLabel();
        referenciaCatastralParcela2JTfield = new JTextField();
        nifPresentadorLabel = new JLabel();
        nifPresentadorJTfield = new JTextField();
        nombreCompletoPresentadorLabel = new JLabel();
        nombreCompletoPresentadorJTfield = new JTextField();

        tipoDeExpedienteCBoxE.setEditable(false);
        idEstadoCBox.setEditable(false);
        tecnicoCatastroCBoxE.setEditable(false);
        codigoEntidadGeneradora_ControlCBoxE.setEditable(false);
        fechaRegistroInicialJTfield.setEnabled(false);
        fechaRegistroInicialJTfield.setEditable(false);
        fechaRegistroInicialJButton.setIcon(UtilRegistroExp.iconoZoom);
        fechaRegistroFinalJTfield.setEnabled(false);
        fechaRegistroFinalJTfield.setEditable(false);
        fechaRegistroFinalJButton.setIcon(UtilRegistroExp.iconoZoom);

        asociarEventosCampos();
        setEditable(true);
        renombrarComponentes();

        //Inicializamos el panel con los elementos.
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(numExpedienteLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 250, 20));
        this.add(numExpedienteJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 30, 250, -1));

        this.add(annoExpedienteAdminOrigenAlt_EjercicioLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 250, 20));
        this.add(annoExpedienteAdminOrigenAlt_EjercicioJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 50, 150, -1));
        this.add(codigoEntidadGeneradora_ControlLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 250, 20));
        this.add(codigoEntidadGeneradora_ControlCBoxE, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 70, 150, -1));

        this.add(tipoDeExpedienteLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 250, 20));
        this.add(tipoDeExpedienteCBoxE, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 100, 250, -1));
        this.add(idEstadoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 250, 20));
        this.add(idEstadoCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 120, 250, -1));

        this.add(tecnicoCatastroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 250, 20));
        this.add(tecnicoCatastroCBoxE, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 150, 250, -1));
        this.add(fechaRegistroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 250, 20));
        this.add(fechaRegistroInicialJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 170, 100, -1));
        this.add(fechaRegistroInicialJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 170, 20, 20));
        this.add(fechaRegistroFinalJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 170, 100, -1));
        this.add(fechaRegistroFinalJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 170, 20, 20));        

        this.add(referenciaCatastralParcela1Label, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 250, 20));
        this.add(referenciaCatastralParcela1JTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 200, 150, -1));
        this.add(referenciaCatastralParcela2Label, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 250, 20));
        this.add(referenciaCatastralParcela2JTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 220, 150, -1));
        this.add(nifPresentadorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 250, 20));
        this.add(nifPresentadorJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 250, 250, -1));
        this.add(nombreCompletoPresentadorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 250, 20));
        this.add(nombreCompletoPresentadorJTfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 270, 250, -1));

    }

    /**
     * Muestra un dialogo para que el usuario elija una fecha y lo escribe en el atributo fechaRegistroInicialJTfield.
     */
    private void fechaRegistroInicialJButtonActionPerformed()
    {
        UtilRegistroExp.showCalendarDialog(desktop);

		if ((ConstantesCatastro.calendarValue != null) && (!ConstantesCatastro.calendarValue.trim().equals("")))
        {
			fechaRegistroInicialJTfield.setText(ConstantesCatastro.calendarValue);
		}
    }
    /**
     * Muestra un dialogo para que el usuario elija una fecha y  lo escribe en el atributo fechaRegistroFinalJTfield.
     */
    private void fechaRegistroFinalJButtonActionPerformed()
    {
        UtilRegistroExp.showCalendarDialog(desktop);

		if ((ConstantesCatastro.calendarValue != null) && (!ConstantesCatastro.calendarValue.trim().equals("")))
        {
			fechaRegistroFinalJTfield.setText(ConstantesCatastro.calendarValue);
		}
    }

    /**
     * Asocia los eventos a los botones del panel y a los JTestField para checkear los valores introducidos
     */
    private void asociarEventosCampos()
    {
        fechaRegistroFinalJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fechaRegistroFinalJButtonActionPerformed();
            }
        });

        fechaRegistroInicialJButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fechaRegistroInicialJButtonActionPerformed();
            }
        });

        numExpedienteJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(numExpedienteJTfield,13);
            }
        });

        nifPresentadorJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(nifPresentadorJTfield, 9);
            }
        });

        nombreCompletoPresentadorJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(nombreCompletoPresentadorJTfield, 60);
            }
        });

        referenciaCatastralParcela1JTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(referenciaCatastralParcela1JTfield, 7);
            }
        });

        referenciaCatastralParcela2JTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaLongCampoEdit(referenciaCatastralParcela2JTfield, 7);
            }
        });

        annoExpedienteAdminOrigenAlt_EjercicioJTfield.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent evt)
            {
                chequeaAnnoExpedienteOrigen(annoExpedienteAdminOrigenAlt_EjercicioJTfield);
            }
        });
    }

    /**
     * Metodo que checkea que los datos de un campo no superan un limite pasado por parametro. Si lo supera lo recorta
     * hasta el tamaño correcto.
     *
     * @param comp El componente
     * @param maxLong La lonfitud maxima
     */
    private void chequeaLongCampoEdit(final JTextComponent comp,final int maxLong)
    {
        String numAnno= comp.getText();
        if(numAnno.length()>maxLong)
        {
        	UtilRegistroExpCatastro_LCGIII.retrocedeCaracter(comp, maxLong);
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
                JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.MSG1"));
                UtilRegistroExpCatastro_LCGIII.retrocedeCaracter(comp,numAnno.length()-1);
            }
            if(numAnno.length()>4 )
            {
            	UtilRegistroExpCatastro_LCGIII.retrocedeCaracter(comp, 4);
            }
            if(numAnno.length()==4 && x<=1000 && x!=-1)
            {
                JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.MSG2"));
            }
        }
    }

   /**
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getPanel()
    {
        return this;
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
     * Carga los datos pasados al constructor en los atributos correspondientes de la clase. Para cargar los estados
     * se utiliza la clase Estructuras que tiene cargados los dominios de los estados. Se añade un valor nullo
     * (Selecione uno) para que al realizar la busqueda se considere como vacio ya que los combobox tienen que mostrar
     * valores.
     */
    public void cargaEstructurasBD()
    {
        idEstadoCBox.removeAllItems();
        idEstadoCBox.addItem(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.SelecioneUno"));
//        for(int i = 0; i< codigoEst.size();i++)
//        {
//        	ListaEstructuras listaEstructuras = Estructuras.getListaEstadosExpediente();
//        	DomainNode domainNode = listaEstructuras.getDomainNode((String)codigoEst.get(i));
//        	String aux = domainNode.getTerm(ConstantesCatastro.Locale);
////            String aux= Estructuras.getListaEstadosExpediente().getDomainNode((String)codigoEst.get(i))
////                    .getTerm(ConstantesCatastro.Locale);
//            idEstadoCBox.addItem(aux);
//        }

        tipoDeExpedienteCBoxE.removeAllItems();
        tipoDeExpedienteCBoxE.addItem(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.SelecioneUno"));
        ArrayList auxTiposExp = ConstantesCatastro.tiposExpedientes;
        if(auxTiposExp!=null)
        {
            for(int i = 0; i< auxTiposExp.size();i++)
            {
                tipoDeExpedienteCBoxE.addItem(((TipoExpediente)auxTiposExp.get(i)).getCodigoTipoExpediente());
            }
        }        

        tecnicoCatastroCBoxE.removeAllItems();
        tecnicoCatastroCBoxE.addItem(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.SelecioneUno"));
        Collection enumUsu = usuarios.values();
        Object usus[] = enumUsu.toArray();
        for(int i= 0;i<usus.length;i++)
        {
            tecnicoCatastroCBoxE.addItem(usus[i]);
        }

        codigoEntidadGeneradora_ControlCBoxE.removeAllItems();
        codigoEntidadGeneradora_ControlCBoxE.addItem(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.SelecioneUno"));
        for(int i= 0;i<entGen.size();i++)
        {
            codigoEntidadGeneradora_ControlCBoxE.addItem(entGen.get(i));
        }
    }

    /**
     * Cambia los campos editables del panel segun el parametro pasado.
     *
     * @param edit
     * */
    public void setEditable(boolean edit)
    {
        numExpedienteJTfield.setEditable(edit);
        annoExpedienteAdminOrigenAlt_EjercicioJTfield.setEditable(edit);
        referenciaCatastralParcela1JTfield.setEditable(edit);
        referenciaCatastralParcela2JTfield.setEditable(edit);
        nifPresentadorJTfield.setEditable(edit);
        nombreCompletoPresentadorJTfield.setEditable(edit);
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        cargaEstructurasBD();
        numExpedienteLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.numExpedienteLabel"));
        tipoDeExpedienteLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.tipoDeExpedienteLabel"));
        idEstadoLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.idEstadoLabel"));
        tecnicoCatastroLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.tecnicoCatastroLabel"));
        fechaRegistroLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.fechaRegistroLabel"));
        fechaRegistroInicialJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.fechaRegistroInicialJButton.hint"));
        fechaRegistroFinalJButton.setToolTipText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.fechaRegistroFinalJButton.hint"));
        annoExpedienteAdminOrigenAlt_EjercicioLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.annoExpedienteAdminOrigenAlt_EjercicioLabel"));
        codigoEntidadGeneradora_ControlLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.codigoEntidadGeneradora_ControlLabel"));
        referenciaCatastralParcela1Label.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.referenciaCatastralParcela1Label"));
        referenciaCatastralParcela2Label.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.referenciaCatastralParcela2Label"));
        nifPresentadorLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.nifPresentadorLabel"));
        nombreCompletoPresentadorLabel.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.nombreCompletoPresentadorLabel"));
    }

    /**
     * Recopila los datos que el usuario ha introducido para filtrar los expediente y seleccionar el que el desea.
     * Los datos se introducen el la hash pasada por parametro con la key correspondiente y el valor de atributo.
     * Se comprueban tambien si los campos de los combobox llevan valores nullos y si es asi se devuelve "".
     *
     * @param hashDatos Hashtable donde se introducen los datos con keys almacenadas en la clase contantesRegistroExp
     * @return boolean Indica si no se ha producido ningun fallo
     */
    public boolean recopilaDatosPanel(Hashtable hashDatos)
    {
        String idEstado = "";
        String tipoExpe = "";
        String tecnicoCatastro = "";
        String codigoEntidadGeneradora = "";
        if(!((String)idEstadoCBox.getSelectedItem()).equals(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.SelecioneUno")))
        {
            idEstado = Estructuras.getListaEstadosExpediente().getDomainNodeByTraduccion((String)idEstadoCBox.getSelectedItem()).getPatron();
        }
        if(!((String)tipoDeExpedienteCBoxE.getSelectedItem()).equals(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.SelecioneUno")))
        {
            tipoExpe = (String)tipoDeExpedienteCBoxE.getSelectedItem();
        }
        if(!((String)tecnicoCatastroCBoxE.getSelectedItem()).equals(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.SelecioneUno")))
        {
            tecnicoCatastro = (String)tecnicoCatastroCBoxE.getSelectedItem();
        }
        if(!((String)codigoEntidadGeneradora_ControlCBoxE.getSelectedItem()).equals(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.PanelDatosBusqueda.SelecioneUno")))
        {
            codigoEntidadGeneradora = (String)codigoEntidadGeneradora_ControlCBoxE.getSelectedItem();
        }

        hashDatos.put(ConstantesCatastro.expedienteNumeroExpediente, numExpedienteJTfield.getText());
        hashDatos.put(ConstantesCatastro.expedienteTipoExpediente, tipoExpe);
        hashDatos.put(ConstantesCatastro.expedienteIdEstado, idEstado);
        hashDatos.put(ConstantesCatastro.expedienteIdTecnicoCatastro, tecnicoCatastro);
        //TODO esto mirar como hacerlo, o si dejarlo asi.
        hashDatos.put(ConstantesCatastro.expedienteFechaRegistro + "Inicial",fechaRegistroInicialJTfield.getText());
        hashDatos.put(ConstantesCatastro.expedienteFechaRegistro+ "Final",fechaRegistroFinalJTfield.getText());
        hashDatos.put(ConstantesCatastro.expedienteAnnoExpedienteAdminOrigenAlteracion, annoExpedienteAdminOrigenAlt_EjercicioJTfield.getText());
        hashDatos.put(ConstantesCatastro.entidadGeneradoraCodigo, codigoEntidadGeneradora);
        String referenciaExp= referenciaCatastralParcela1JTfield.getText().concat(referenciaCatastralParcela2JTfield.getText());
        hashDatos.put(ConstantesCatastro.expedienteReferenciasCatastrales, referenciaExp);
        hashDatos.put(ConstantesCatastro.expedienteNifPresentador, nifPresentadorJTfield.getText());
        hashDatos.put(ConstantesCatastro.expedienteNombreCompletoPresentador, nombreCompletoPresentadorJTfield.getText());        

        return true;
    }    
}
