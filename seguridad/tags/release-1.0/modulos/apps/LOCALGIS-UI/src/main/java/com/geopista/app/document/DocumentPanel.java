package com.geopista.app.document;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.protocol.document.DocumentBean;

/**
 * Created by IntelliJ IDEA.
 * User: lara
 * Date: 03-may-2006
 * Time: 12:20:10
 * To change this template use File | Settings | File Templates.
 */

public class DocumentPanel  extends JScrollPane
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GeopistaFeature gFeature;
    DocumentInterface docInt;
    DocumentBean documentSelected=null;

    /* Colección de documentos asociados a una feature */
    Collection documentos;

    /* Listado de  documentos */
    private JList jListDocuments = new JList();

    private DocumentBean auxDocument;

    /* constructor de la clase dd se pinta el panel */
    public DocumentPanel(GeopistaFeature feature, Collection collection, DocumentInterface docInt)
    {
        gFeature=feature;
        this.docInt=docInt;
        documentos=collection;
        setLayout(new ScrollPaneLayout());
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        jListDocuments.setCellRenderer(new RendererDocumentos());
        jListDocuments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel rowSM = jListDocuments.getSelectionModel();
        actualizarModelo();
        rowSM.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                escoger();
            }
        });
        setBorder(new TitledBorder(aplicacion.getI18nString("document.infodocument.documentos")));
        setViewportView(jListDocuments);
    }

    /* actualizamos la lista q se visualiza cada vez q modifiquemos algo */
    public void actualizarModelo()
    {
        DefaultListModel listModel = new DefaultListModel();

        if (documentos != null)
        {
            for(Iterator iterator= documentos.iterator(); iterator.hasNext();)
                listModel.addElement(iterator.next());
        }
        jListDocuments.setModel(listModel);
    }

    /* actualizamos la lista de documentos cada vez q se opere sobre ellos */
    public void update(DocumentBean documento)
    {
        if (documentos == null || documento == null) return;
        Vector newDocumentos = new Vector();
        for (Iterator it=documentos.iterator(); it.hasNext();)
        {
              DocumentBean doc = (DocumentBean)it.next();
              if (doc.getId() == documento.getId())
                  newDocumentos.add(documento);
              else
                  newDocumentos.add(doc);
        }
        documentos = newDocumentos;
        actualizarModelo();
    }

    /* método q nos permite seleccionar un elemento de la lista */
    public void seleccionar(DocumentBean documento)
    {
        if (documento == null)return;
        ListModel auxList = jListDocuments.getModel();
        for (int i=0;i<auxList.getSize();i++)
        {
            DocumentBean auxDocument=(DocumentBean)auxList.getElementAt(i);
            if (auxDocument.getId()==documento.getId())
            {
                jListDocuments.setSelectedIndex(i);
               return;
            }
        }
    }

    /* borramos de la lista los documentos que han sido eliminados x el usuario */
    public void borrar()
    {
        if (documentSelected==null) return;
        documentos.remove(documentSelected);
        documentSelected = null;
        actualizarModelo();
    }

    /* añadimos a la lista los documentos q haya agregado el usuario */
    public void add(DocumentBean documento)
    {
        if(documento == null) return;
        if (documentos==null) documentos= new Vector();
        documentos.add(documento);
        actualizarModelo();
        seleccionar(documento);
    }

    /* método q nos indica q' documento ha sido seleccionado */
    private void escoger()
    {
        int selectedRow = jListDocuments.getMinSelectionIndex();
        if(selectedRow < 0) return;
        ListModel auxList = jListDocuments.getModel();
        DocumentBean auxDocumento = (DocumentBean) auxList.getElementAt(selectedRow);
        if (docInt!=null) docInt.seleccionar(auxDocumento);
        documentSelected=auxDocumento;
    }

    /* método q nos devuelve la feature q ha sido seleccionada*/
    public GeopistaFeature getgFeature()
    {
        return gFeature;
    }

    /* método q determina q feature ha sido seleccionada */
    public void setgFeature(GeopistaFeature gFeature)
    {
        this.gFeature = gFeature;
    }

    /* método q nos devuelve el documento seleccionado */
    public DocumentBean getdocumentSelected()
    {
        return documentSelected;
    }

    /* determinamos el documento seleccionado */
    public void setdocumentSelected(DocumentBean documentSelected)
    {
        this.documentSelected = documentSelected;
    }

    /**
     * método q nos devuelve la coleccion de documentos asociados a la/s feature/s
     * seleccionada/s
     */
    public Collection getDocumentos()
    {
        return documentos;
    }

    /* determinamos la coleccion de documentos */
    public void setDocumentos(Collection documentos)
    {
        this.documentos = documentos;
    }

    public boolean existDocument(DocumentBean document)
    {
        for (int i=0; i<jListDocuments.getModel().getSize();i++)
        {
            if (((DocumentBean)jListDocuments.getModel().getElementAt(i)).getId()==document.getId())
                return true;
        }
        return false;
    }
    
 	public ArrayList<String> getFeatureDocumentsId() {
 		ArrayList<String> idDocuments = new ArrayList<String>();
 		for (int i=0; i<jListDocuments.getModel().getSize();i++)
        {
 			idDocuments.add(((DocumentBean)jListDocuments.getModel().getElementAt(i)).getId()); 			
        }
 		return idDocuments;
 	}

    /* comprobamos si el documento a modificar esta asociado a varias features */
    public DocumentBean modificarFeatures(DocumentBean documento)
    {
        Object[] array = new Object[documentos.size()];
        for(Iterator it = documentos.iterator(); it.hasNext();)
        {
            for(int i=0; i<documentos.size(); i++)
            {
                array[i] = (DocumentBean) it.next();
                auxDocument = (DocumentBean) array[i];
                if(auxDocument.getId() == documento.getId())
                    auxDocument = documento;
            }
        }
        return auxDocument;
    }

    public int getSelectedIndex(){
        return jListDocuments.getMinSelectionIndex();
    }
    public void addMouseListener(MouseListener ml)
    {
         jListDocuments.addMouseListener(ml);
    }

}

