/**
 * JPanelFusionarVias.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.acteconomicas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.TableSorted;

/**
 * Created by IntelliJ IDEA.
 * User: lara
 * Date: 06-jun-2006
 * Time: 13:27:04
 * To change this template use File | Settings | File Templates.
 */

public class JPanelFusionarVias extends JPanel
{
    ActEcoTableModel modelCallejero;
    ActEcoTableModel modelViasOrigen;

    private static final Log logger = LogFactory.getLog(JPanelFusionarVias.class);
    public static Icon iconoCalle= null;


    AppContext application;

    /* Paneles */
    private JPanel jPanelResultados = new JPanel();
    private JPanel jPanelTabla = new JPanel();

    private JButton bAsociar = new JButton();
    private JButton bEliminar = new JButton();

    private JTable jTableViasOrigen;
    private JList jListViasAsociadas = new JList();
    private JList jListGeorreferenciadas= new JList();
    private JTable jTableCallejero;
    private Vector asociaciones = new Vector();
    private Vector viasOrigen = new Vector();

    private PintarAsociaciones selected = null;

    /* constructor de la clase q llama al método que inicializa el panel */
    public JPanelFusionarVias()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            logger.error("Error en el constructor ", e);
        }
    }

    /* método xa inicializar el panel */
    private void jbInit() throws Exception
    {

        application = (AppContext) AppContext.getApplicationContext();
        try
        {
            ClassLoader cl =this.getClass().getClassLoader();
            iconoCalle=new javax.swing.ImageIcon(cl.getResource("img/calle.jpg"));
        }catch(Exception e){logger.error("Error al cargar el icono: img/calle.jpg",e);}
        this.setName(application.getI18nString("acteconomicas.panel.listado"));
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(600, 550));

         /** Lista + Tabla
          * Panel */
        jPanelTabla.setLayout(new BorderLayout());

        /* Componentes */
        jTableCallejero = new JTable();
        jTableCallejero.setRowSelectionAllowed(true);
        jTableCallejero.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane jScrollPaneCallejero = new JScrollPane(jTableCallejero);
        jTableCallejero.setPreferredScrollableViewportSize(new Dimension(270, 250));
        jScrollPaneCallejero.setBorder(new TitledBorder(application.getI18nString("acteconomicas.listado.callejero")));

        actualizarModeloTabla(null);


        jTableViasOrigen= new JTable();
        actualizarModeloViasOrigen(null);

        JScrollPane jScrollPaneViasOrigen = new JScrollPane(jTableViasOrigen);
        jScrollPaneViasOrigen.setBorder(new TitledBorder(application.getI18nString("acteconomicas.listado.vias")));
        jTableViasOrigen.setPreferredScrollableViewportSize(new Dimension(270, 250));

        /* añadimos los componentes al panel*/
        jPanelTabla.add(jScrollPaneViasOrigen, BorderLayout.WEST);
        jPanelTabla.add(jScrollPaneCallejero, BorderLayout.EAST);
        /**
         * Listado de asociados
         * Paneles
        */
        jPanelResultados.setLayout(new BorderLayout());
        /* Componentes */
        bAsociar.setText(application.getI18nString("acteconomicas.botones.asociar"));
        bAsociar.setEnabled(true);
        bAsociar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                asociarVias();
            }
        });
        /* añadimos los componentes al panel */
        JPanel jPanelAux = new JPanel();
        jPanelAux.setLayout(new FlowLayout());
        jPanelAux.add(bAsociar);

        jListViasAsociadas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jListViasAsociadas.setCellRenderer(new RendererAsociaciones());

        ListSelectionModel row = jListViasAsociadas.getSelectionModel();
        row.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                escogerAsociacion();
            }
        });
        jListViasAsociadas.setEnabled(true);
        JScrollPane scrollViasAsociadas = new JScrollPane(jListViasAsociadas);
        scrollViasAsociadas.setBorder(new TitledBorder(application.getI18nString("acteconomicas.listado.vias.asociadas")));
        scrollViasAsociadas.setPreferredSize(new Dimension(420, 260));

        jListGeorreferenciadas.setCellRenderer(new RendererGeorreferenciadas());
        JScrollPane scrollGeorreferenciadas = new JScrollPane(jListGeorreferenciadas);
        scrollGeorreferenciadas.setBorder(new TitledBorder(application.getI18nString("acteconomicas.listado.georreferenciadas")));
        scrollGeorreferenciadas.setPreferredSize(new Dimension(150, 260));


        bEliminar.setText(application.getI18nString("acteconomicas.botones.eliminar"));
        bEliminar.setEnabled(false);
        bEliminar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                eliminarAsociacion();
            }
        });
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        jPanel.add(bEliminar);

        JPanel jPanelListado = new JPanel();
        jPanelListado.add(scrollGeorreferenciadas,BorderLayout.WEST);
        jPanelListado.add(scrollViasAsociadas, BorderLayout.CENTER);
        jPanelListado.add(jPanel, BorderLayout.EAST);

         jPanelTabla.add(jPanelAux, BorderLayout.CENTER);

        jPanelResultados.add(jPanelListado, BorderLayout.CENTER);


        this.add(jPanelTabla, BorderLayout.NORTH);
        this.add(jPanelResultados, BorderLayout.CENTER);
    }

    /* cargamos las actividades q se qieren georreferenciar en la lista */
    public void cargarActividadesEconomicas(ArrayList listActEco)
    {
        try
        {
            viasOrigen=new Vector();
            for(int i=0; i<listActEco.size(); i++)
            {
               viasOrigen.add(listActEco.get(i));
            }
            actualizarModeloViasOrigen(viasOrigen);
        }
        catch(Exception e)
        {
            logger.error("Error al cargar los datos en la lista ", e);
        }
    }

     /* cargamos las actividades q se qieren georreferenciar en la lista */
     public void cargarActividadesGeorreferenciadas(ArrayList listActEco)
    {
        try
        {
            DefaultListModel listModel = new DefaultListModel();
            for(int i=0; i<listActEco.size(); i++)
            {
                listModel.addElement(listActEco.get(i));
            }
            jListGeorreferenciadas.setModel(listModel);
        }
        catch(Exception e)
        {
            logger.error("Error al cargar los datos en la lista ", e);
        }
    }
    /* asociamos las vías de la lista de la dcha con las seleccionadas en la izqda.*/
    private void asociarVias()
    {
        if(jTableCallejero.getSelectedRow() < 0 || jTableViasOrigen.getSelectedRow() < 0) return;

	    int rowCallejero = jTableCallejero.getSelectedRow();
        Via auxVia = (Via)jTableCallejero.getValueAt(rowCallejero, ActEcoTableModel.ROW_VIA);
        int rowViaOrigen = jTableViasOrigen.getSelectedRow();
        DatosImportarActividades datos = (DatosImportarActividades) jTableViasOrigen.getValueAt(rowViaOrigen,ActEcoTableModel.ROW_VIA);
        
        Integer element=  ((Integer)jTableCallejero.getCellEditor(rowCallejero, ActEcoTableModel.ROW_NUMERO_POLICIA).getCellEditorValue());
        if (element ==null ) element=((Integer)jTableCallejero.getModel().getValueAt(rowCallejero, ActEcoTableModel.ROW_NUMERO_POLICIA));
        NumeroPolicia auxNumPoli=(NumeroPolicia)auxVia.getNumerosPolicia().elementAt(element.intValue());
        datos.setGeometria(auxNumPoli.getGeometria());
        asociaciones.add(new PintarAsociaciones(datos,auxVia,auxNumPoli));
        eliminar(datos,rowViaOrigen);
        DefaultListModel listModel = new DefaultListModel();
        for(Iterator it=asociaciones.iterator(); it.hasNext();)
            listModel.addElement(it.next());
        jListViasAsociadas.setModel(listModel);
        bEliminar.setEnabled(true);
    }

    /* actualizamos la lista cada vez q operamos sobre ella */
    private DefaultListModel actualizarModelo(Vector actualizar)
    {
        DefaultListModel listModel = new DefaultListModel();
        if (actualizar != null)
        {
            for(Iterator iterator = actualizar.iterator(); iterator.hasNext();)
                listModel.addElement(iterator.next());
        }
        return listModel;
    }

    /* actualizamos la tabla cada vez q operamos sobre ella */
    private void actualizarModeloTabla(Vector vias)
    {
	    modelCallejero= new ActEcoTableModel();
        modelCallejero.setModelData(vias);
        TableSorted sorter = new TableSorted(modelCallejero);
        sorter.setTableHeader(jTableCallejero.getTableHeader());
        jTableCallejero.setModel(sorter);
        jTableCallejero.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JComboBoxNumerosPolicia renderer = new JComboBoxNumerosPolicia();
        jTableCallejero.getColumnModel().getColumn(ActEcoTableModel.ROW_TIPO_VIA).setCellRenderer(new CellColorRenderer(Color.RED));
        jTableCallejero.getColumnModel().getColumn(ActEcoTableModel.ROW_VIA).setCellRenderer(new ViaCellRenderer());
        jTableCallejero.getColumnModel().getColumn(ActEcoTableModel.ROW_NUMERO_POLICIA).setCellRenderer(renderer);
        jTableCallejero.getColumnModel().getColumn(ActEcoTableModel.ROW_NUMERO_POLICIA).setCellEditor(new ComboBoxTableEditor(new JComboBoxNumerosPolicia()));
        jTableCallejero.getColumnModel().getColumn(ActEcoTableModel.ROW_TIPO_VIA).setPreferredWidth(40);
       // jTableCallejero.getColumnModel().getColumn(ActEcoTableModel.ROW_VIA).setWidth(100);
        jTableCallejero.getColumnModel().getColumn(ActEcoTableModel.ROW_NUMERO_POLICIA).setPreferredWidth(20);

        jTableCallejero.setEnabled(true);
    }

    /* actualizamos la tabla cada vez q operamos sobre ella */
    private void actualizarModeloViasOrigen(Vector viasOrigen)
    {
        modelViasOrigen= new ActEcoTableModel();
        modelViasOrigen.setModelData(viasOrigen);
        TableSorted sorter = new TableSorted(modelViasOrigen);
        sorter.setTableHeader(jTableViasOrigen.getTableHeader());
        jTableViasOrigen.setModel(sorter);
        jTableViasOrigen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableViasOrigen.setEnabled(true);
        jTableViasOrigen.getColumnModel().getColumn(ActEcoTableModel.ROW_TIPO_VIA).setPreferredWidth(40);
       // jTableViasOrigen.getColumnModel().getColumn(ActEcoTableModel.ROW_VIA).setWidth(100);
        jTableViasOrigen.getColumnModel().getColumn(ActEcoTableModel.ROW_NUMERO_POLICIA).setPreferredWidth(20);
        jTableViasOrigen.getColumnModel().getColumn(ActEcoTableModel.ROW_TIPO_VIA).setCellRenderer(new CellColorRenderer(Color.MAGENTA));
        jTableViasOrigen.getColumnModel().getColumn(ActEcoTableModel.ROW_VIA).setCellRenderer(new CellColorRenderer(Color.MAGENTA));
        jTableViasOrigen.getColumnModel().getColumn(ActEcoTableModel.ROW_NUMERO_POLICIA).setCellRenderer(new CellColorRenderer(Color.MAGENTA));


    }
    /* devolvemos la actividad economica y la geometria a la q va asociada */
    public Vector getActEcoGeorreferenciadas()
    {
        Vector geoActividades = new Vector();
        for (Enumeration e=asociaciones.elements();e.hasMoreElements();)
        {
             geoActividades.add(((PintarAsociaciones)e.nextElement()).getDatos());
        }
        return geoActividades;
    }


    /* seleccionamos una fila de la lista con las nuevas asociaciones */
    private void escogerAsociacion()
    {
        int selectedRow = jListViasAsociadas.getMinSelectionIndex();
        if(selectedRow < 0) return;
        ListModel auxList = jListViasAsociadas.getModel();
        PintarAsociaciones seleccionado = (PintarAsociaciones) auxList.getElementAt(selectedRow);
        selected = seleccionado;
    }

    /* borramos de la lista los documentos que han sido eliminados x el usuario */
    public void eliminar(DatosImportarActividades  datos,int row)
    {
        if(datos == null) return;
	    viasOrigen.remove(datos);
        datos = null;
        //actualizarModeloViasOrigen(viasOrigen);
	    if(viasOrigen.size() == 0)
	        bAsociar.setEnabled(false);
        ((TableSorted)jTableViasOrigen.getModel()).removeRow(row);

    }

    /* borramos de la lista la asociacion de documentos q ha sido eliminada x el usuario */
    public void eliminarAsociacion()
    {
        if(selected == null) return;
        asociaciones.remove(selected);
        jListViasAsociadas.setModel(actualizarModelo(asociaciones));
	    if(jListViasAsociadas == null)
	        bEliminar.setEnabled(false);
	    deshacerAsociacion();
        selected = null;
    }

    /* cd eliminamos la asociacion, los elementos pertenecientes a ella vuelven a estar disponibles */
    public void deshacerAsociacion()
    {
        viasOrigen.add(selected.getDatos());
        actualizarModeloViasOrigen(viasOrigen);
        if (viasOrigen.size()>0)
            bAsociar.setEnabled(true);
    }

    /* cargamos los datos en la tabla */
    public void cargarTabla(Vector vias)
    {
        try
        {
            actualizarModeloTabla(vias);
        }
        catch(Exception e)
        {
            logger.error("Error al cargar los datos en la tabla ", e);
        }
    }
    public void destroy()
    {
         modelCallejero=null;
         modelViasOrigen=null;
         iconoCalle= null;
         application=null;
         jPanelResultados.removeAll();
         jPanelResultados =  null;
         jPanelTabla.removeAll();
         jPanelTabla = null;
         bAsociar = null;
         bEliminar = null;
         jTableViasOrigen.removeAll();
         jTableViasOrigen =null;

         jListViasAsociadas.removeAll();
         jListViasAsociadas= null;
         jListGeorreferenciadas.removeAll();
         jListGeorreferenciadas= null;
         jTableCallejero.removeAll();
         jTableCallejero =null;
         asociaciones=null;
         viasOrigen = null;
         selected = null;
    }
}


/**
 * Clase para pintar las vias
 */
class ViaCellRenderer  implements TableCellRenderer
{
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column)
    {
        Via via = (Via)value;
        JLabel aux = new JLabel(via.getNombreviaine());
        if (isSelected)
        {
             aux.setBorder(BorderFactory.createLineBorder(Color.RED));
         }
        return aux;
    }
}

/**
 * Clase para pintar las vias
 */
class CellColorRenderer  implements TableCellRenderer
{
    Color color;
    public CellColorRenderer(Color color)
    {
        this.color=color;
    }
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column)
    {
        JLabel aux = new JLabel("");
        if (value!=null)
            aux = new JLabel(value.toString());
        if (isSelected)
        {
             aux.setBorder(BorderFactory.createLineBorder(color));
         }
        return aux;
    }
}

//Renderer para pintar las asociaciones
 class RendererAsociaciones extends DefaultListCellRenderer
{

    public Component getListCellRendererComponent(JList l,
           Object value, int i, boolean s, boolean f)
    {
            JLabel label =(JLabel) super.getListCellRendererComponent(l,value,i, s, f);
            if (JPanelFusionarVias.iconoCalle!=null)
                label.setIcon(JPanelFusionarVias.iconoCalle);

            return label;
    }
}

//Renderer para pintar las asociaciones
class RendererGeorreferenciadas extends DefaultListCellRenderer
   {

   public Component getListCellRendererComponent(JList l,
          Object value, int i, boolean s, boolean f)
   {
           JLabel label =(JLabel) super.getListCellRendererComponent(l,((DatosImportarActividades)value).toStringNew(),i, s, f);
           if (JPanelFusionarVias.iconoCalle!=null)
               label.setIcon(JPanelFusionarVias.iconoCalle);
           label.setBackground(Color.ORANGE);
           return label;
   }
   }

/**
 * Renderer para los numeros de policia
 */
class JComboBoxNumerosPolicia extends JComboBox implements TableCellRenderer
{
    public JComboBoxNumerosPolicia()
    {
        super();
        this.setRenderer(new RendererComboNumerosPolicia());
    }

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column)
    {
        JComboBoxNumerosPolicia aux=new JComboBoxNumerosPolicia();
        if (isSelected)
        {
            aux.setForeground(table.getSelectionForeground());
            aux.setBackground(Color.RED);
            aux.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        else
        {
            aux.setForeground(table.getForeground());
            aux.setBackground(table.getBackground());
        }
        Vector numerosPolicia=((Via) table.getModel().getValueAt(row,ActEcoTableModel.ROW_VIA)).getNumerosPolicia();
        if (numerosPolicia==null) return aux;
        for (Enumeration e=numerosPolicia.elements();e.hasMoreElements();)
        {
            aux.addItem(e.nextElement());
        }

        if (value!=null && value instanceof Integer)
            aux.setSelectedIndex(((Integer)value).intValue());
        return aux;
    }
}


class RendererComboNumerosPolicia  extends JLabel implements ListCellRenderer
 {
    public RendererComboNumerosPolicia()
    {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    /* listamos los numeros de policia en el combo */
    public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus)
    {
        if (value==null) return this;

        if (isSelected)
        {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            //setBorder(BorderFactory.createLineBorder(Color.red,2));
        }
        else
        {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setHorizontalAlignment(LEFT);
        setText((((NumeroPolicia)value).getRotulo()));
        return this;
    }
}


/**
 * Clase para editar los numeros de policia
 */
class ComboBoxTableEditor extends DefaultCellEditor
 {
     private JComboBox combo;
     public ComboBoxTableEditor(JComboBox combo)
     {
         super(combo);
     }

     public Component getTableCellEditorComponent(JTable table,
                                                  Object value,
                                                  boolean isSelected,
                                                  int rowIndex,
                                                  int vColIndex)
     {
         // 'value' is value contained in the cell located at (rowIndex, vColIndex)
         combo = new JComboBoxNumerosPolicia();
         if (value==null) return combo;
         Vector numerosPolicia=((Via) table.getModel().getValueAt(rowIndex,ActEcoTableModel.ROW_VIA)).getNumerosPolicia();
         for (Enumeration e= numerosPolicia.elements();e.hasMoreElements();)
         {
              combo.addItem(e.nextElement());
         }
         return combo;
     }

     public Component getEditorComponent()
     {
        return (JComboBox)this.getComponent();
     }

     /**
      * Devuelve el item seleccionado
      * @return
      */
     public Object getCellEditorValue()
     {
         if (combo==null) return null;
         return new Integer(combo.getSelectedIndex());
     }

     public void setEditable(boolean b)
     {
        JComboBox combo = (JComboBox)this.getComponent();
        combo.setEditable(b);
     }

     public void setEnabled(boolean b)
     {
        JComboBox combo = (JComboBox)this.getComponent();
        combo.setEnabled(b);
     }
}


class PintarAsociaciones
{
    Via via;
    NumeroPolicia numeroPolicia;
    DatosImportarActividades datos;

    public PintarAsociaciones(DatosImportarActividades datos, Via via, NumeroPolicia numeroPolicia)
    {
         this.datos=datos;
         this.via=via;
         this.numeroPolicia=numeroPolicia;
    }

    public Via getVia()
    {
        return via;
    }

    public NumeroPolicia getNumeroPolicia()
    {
        return numeroPolicia;
    }

    public DatosImportarActividades getDatos()
    {
        return datos;
    }

    public String toString()
    {
         return datos.toStringNew() + " ====> " + (via.getTipoviaine()==null?"":via.getTipoviaine()) + " " + via + " " +
                numeroPolicia;
    }
}