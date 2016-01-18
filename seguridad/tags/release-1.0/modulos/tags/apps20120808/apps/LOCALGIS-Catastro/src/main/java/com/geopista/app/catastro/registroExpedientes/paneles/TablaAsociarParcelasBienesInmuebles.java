package com.geopista.app.catastro.registroExpedientes.paneles;

import com.geopista.app.catastro.gestorcatastral.IMultilingue;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.AppContext;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 01-feb-2007
 * Time: 9:46:15
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que extiende la clase panel. Es un elemento de gui y no interacciona con ningun dato del programa. Solo
 * muestra elementos guis y como mucho tiene metodos para hacer que se muestren en la gui datos pasados por parametro
 * a esas funciones. Esta clase implementa un panel que tiene una tabla y carga los datos en ella.
 */

public class TablaAsociarParcelasBienesInmuebles extends JPanel implements IMultilingue
{
    private String etiqueta;
    private JTable tablaRefCatasTabel;
    private String[] identificadores;
    private DefaultTableModel modelo;
    private JScrollPane tablaScrollPanel;

    /**
     * Constructor de la clase. Se le pasa por parametro el label que se mostrara como borde en el panel. El label es
     * un elemento del ResourceBundle de la clase que le llama.
     *
     * @param label Etiqueta del borde del panel
     */
    public TablaAsociarParcelasBienesInmuebles(String label)
    {
        etiqueta= label;
        inicializaPanel();
    }

    /**
     * Inicializa todos los elementos del panel y los coloca en su posicion. Carga la tabla y le asigna los modos
     * que queremos.
     */
    private void inicializaPanel()
    {
        tablaRefCatasTabel= new JTable();
        identificadores = new String[6];
        modelo= new DefaultTableModel()
        {
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        tablaRefCatasTabel.setModel(modelo);
		tablaRefCatasTabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaRefCatasTabel.setCellSelectionEnabled(false);
		tablaRefCatasTabel.setColumnSelectionAllowed(false);
		tablaRefCatasTabel.setRowSelectionAllowed(true);
        tablaScrollPanel= new JScrollPane(tablaRefCatasTabel);

        renombrarComponentes();

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.add(tablaScrollPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 480, 270));

    }

    /**
     * Funcion que carga los datos del arrayList pasado por parametro en la tabla. En este caso son o fincasCatastro
     * o Bienes inmuebles. Se hace un instanceof para saberlo.
     *
     * @param dirRefCatas La lista de referencias del expediente
     */
    public void cargaDatosTabla(ArrayList dirRefCatas)
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();        
        Estructuras.cargarEstructura("Tipos de via normalizados de Catastro");
        String[][] datos= new String[dirRefCatas.size()][6];
        for(int i=0; i< dirRefCatas.size();i++)
        {
            DireccionLocalizacion auxDir = null;
            if(dirRefCatas.get(i) instanceof FincaCatastro)
            {
                FincaCatastro parcela = (FincaCatastro)dirRefCatas.get(i);
                auxDir = parcela.getDirParcela();
                datos[i][0] = parcela.getRefFinca().getRefCatastral();
            }
            else if(dirRefCatas.get(i) instanceof BienInmuebleCatastro)
            {
                BienInmuebleCatastro bien = (BienInmuebleCatastro)dirRefCatas.get(i);
                auxDir = bien.getDomicilioTributario();
                datos[i][0] = bien.getIdBienInmueble().getIdBienInmueble();
            }
            if(auxDir!=null &&Estructuras.getListaTipos().getDomainNode(auxDir.getTipoVia())!=null)
            {
                datos[i][1] = Estructuras.getListaTipos().getDomainNode(auxDir.getTipoVia())
                        .getTerm(app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"));
            }
            else
            {
                datos[i][1] = "";    
            }
            if(auxDir!=null)
            {
                datos[i][2] = auxDir.getNombreVia();
                datos[i][3] = String.valueOf(auxDir.getPrimerNumero());
                datos[i][4] = auxDir.getCodPoligono();
                datos[i][5] = auxDir.getCodParcela();
            }
        }
        modelo.setDataVector(datos,identificadores);
        tablaRefCatasTabel.setModel(modelo);
    }

   /**
    * Devuelve el panel.
    *
    * @return this
    * */
    public JPanel getTablaPanel()
    {
        return this;
    }

    /**
     * Renombra las etiquetas, botones, nombre del panel, etc. Clase
     * necesaria para implementar IMultilingue
     * */
    public void renombrarComponentes()
    {
        this.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
                        etiqueta)));
        identificadores[0] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaAsociarParcelasBienesInmuebles.RefCatastralLabel"));
        identificadores[1] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaAsociarParcelasBienesInmuebles.tipoViaLabel"));
        identificadores[2] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaAsociarParcelasBienesInmuebles.NombreViaLabel"));
        identificadores[3] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaAsociarParcelasBienesInmuebles.NumeroLabel"));
        identificadores[4] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaAsociarParcelasBienesInmuebles.codigoPoligono"));
        identificadores[5] = (I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.TablaAsociarParcelasBienesInmuebles.codigoParcela"));        
    }

    /**
     * Devuelve el numero de la fila seleccionada
     *
     * @return int Numero de la fila seleccionada
     */
    public int getParcelaSeleccionada()
    {
        return tablaRefCatasTabel.getSelectedRow();
    }
}
