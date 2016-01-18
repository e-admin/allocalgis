/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.documents.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.protocol.document.DocumentBean;
import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.beans.DocumentTypes;
import com.localgis.app.gestionciudad.dialogs.documents.GestionDocumentalJDialog;
import com.localgis.app.gestionciudad.dialogs.documents.utils.DocumentsPanelUtils;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.NotesInterventionsEditionTypes;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;

/**
 * @author javieraragon
 *
 */
public class AsociatedDocumentsPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7968903561284755274L;
	
//	private TextArea documentListTextArea = null;
	private JButton documentDialogButton = null;
	
	private LocalGISNote note = null;
	private NotesInterventionsEditionTypes editionType = NotesInterventionsEditionTypes.EDIT;
	
	public AsociatedDocumentsPanel(LocalGISNote note, NotesInterventionsEditionTypes editType){
		super(new GridBagLayout());
		this.note = note;
		this.editionType = editType;
		DocumentsPanelUtils.initializeDocumentsPanelLanguages();

		this.initialize();
		loadDocementListData();

	}
	
	private void loadDocementListData(){
		if (this.note != null){
			if (this.note.getListaDeDocumentos() != null){
				if (this.note.getListaDeDocumentos().length > 0){
					// Cargar la lista de documentos en el textarea
//					this.getDocumentListTextArea().setText(UtilidadesAvisosPanels.DocumentListToParsedString(note.getListaDeDocumentos()));
				}
			}
		}
	}

	private void initialize() {
		
//		this.setBorder(BorderFactory.createTitledBorder
//				(null,I18N.get("documentspanel","localgisgestionciudad.interfaces.documents.asociated.panel.title"), 
//						TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
		
//		this.add(this.getDocumentListTextArea(), 
//				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
//						GridBagConstraints.HORIZONTAL, 
//						new Insets(0, 5, 0, 5), 0, 0));
		
		this.add(this.getDocumentDialogButton(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		
	}
	
	

//	private TextArea getDocumentListTextArea(){
//		if (documentListTextArea == null){
//			documentListTextArea =  new TextArea("",3,20,
//					TextArea.SCROLLBARS_VERTICAL_ONLY );
//			documentListTextArea.setEditable(false);
//			
//			documentListTextArea.setText(I18N.get("documentspanel","localgisgestionciudad.interfaces.documents.asociated.panel.nodocuments"));
//		}
//		return documentListTextArea;
//	}
	
	
	private JButton getDocumentDialogButton(){
		if (documentDialogButton == null){
			documentDialogButton = new JButton(I18N.get("documentspanel","localgisgestionciudad.interfaces.documents.asociated.panel.dialogbutton"));
			
			if(this.editionType.equals(NotesInterventionsEditionTypes.NEW)){
				documentDialogButton.setEnabled(false);
			}
			
			documentDialogButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onDocumentDialogButtonDo();
				}
			});
		}
		return documentDialogButton;
	}
	
	
	
	private void onDocumentDialogButtonDo() {
		 try {
			GestionDocumentalJDialog documentDialog = new GestionDocumentalJDialog(
					 AppContext.getApplicationContext().getMainFrame(),
					 this.note,
					 "", this.editionType);
			documentDialog.setVisible(true);
			if (documentDialog.getListaDocumentos() != null){
				Collection a = documentDialog.getListaDocumentos();
				if (!a.isEmpty()){
					Iterator<DocumentBean> it = a.iterator();
					while (it.hasNext()){
						DocumentBean documentFromDialog = it.next();
						boolean encontrado = false;
						for(int i=0;i<note.getListaDeDocumentos().length;i++){
						//	if (documentFromDialog.getId()==note.getListaDeDocumentos()[i].getIdDocumento()){ (String==int)
							//----NUEVO---->
							//No se si es necesario finalmente, hace falta para las pruebas
							if (documentFromDialog.getId().equals(String.valueOf(note.getListaDeDocumentos()[i].getIdDocumento()))){
							//--FIN NUEVO-->	
								actualizarDocumento(documentFromDialog,i);
								encontrado = true;
							}
						}
						
						if (!encontrado){
							anniadirDocuemnto(documentFromDialog);
						}
					}
					System.out.println(UtilidadesAvisosPanels.DocumentListToParsedString(note.getListaDeDocumentos()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadDocementListData();
	}

	private void anniadirDocuemnto(DocumentBean documentFromDialog) {
		ArrayList<Document> listaDocumentos = new ArrayList<Document>();
		if (this.note != null){
			if (this.note.getListaDeDocumentos()!= null){
				listaDocumentos = UtilidadesAvisosPanels.documentListToArrayList(note.getListaDeDocumentos());
				String extension = "";
				try{
					extension = documentFromDialog.getFileName().substring(
							documentFromDialog.getFileName().lastIndexOf('.'),
							documentFromDialog.getFileName().length()); 
				}catch (Exception e) {
					e.printStackTrace();
					extension = "";
				}
				DocumentTypes tipodocumento = DocumentTypes.TXT;
				if (extension.toUpperCase().equals(".PDF")){
					tipodocumento = DocumentTypes.PDF;
				} else if (extension.toUpperCase().equals(".DOC") || 
						extension.toUpperCase().equals(".DOCX")){
					tipodocumento = DocumentTypes.DOC;
				} else if (extension.toUpperCase().equals(".JPG") ||
						extension.toUpperCase().equals(".JPEG") ||
						extension.toUpperCase().equals(".BMP") ||
						extension.toUpperCase().equals(".JPE") ||
						extension.toUpperCase().equals(".JFIF") ||
						extension.toUpperCase().equals(".GIF") ||
						extension.toUpperCase().equals(".TIF") ||
						extension.toUpperCase().equals(".TIFF") ||
						extension.toUpperCase().equals(".PNG")){
					tipodocumento = DocumentTypes.IMAGEN;
				}
				listaDocumentos.add(new Document(
						documentFromDialog.getFileName(),
						tipodocumento,
						extension,
						null));
			}
		}
		this.note.setListaDeDocumentos(listaDocumentos.toArray(new Document[listaDocumentos.size()]));
	}

	private void actualizarDocumento(DocumentBean documentFromDialog, int i) {
		if (this.note != null){
			if (this.note.getListaDeDocumentos()!= null && this.note.getListaDeDocumentos().length > 0){
				
				ArrayList<Document> listaDocumentos = UtilidadesAvisosPanels.documentListToArrayList(note.getListaDeDocumentos());
				String extension = "";
				try{
					extension = documentFromDialog.getFileName().substring(
							documentFromDialog.getFileName().lastIndexOf('.'),
							documentFromDialog.getFileName().length()); 
				}catch (Exception e) {
					e.printStackTrace();
					extension = "";
				}
				DocumentTypes tipodocumento = DocumentTypes.TXT;
				if (extension.toUpperCase().equals("PDF")){
					tipodocumento = DocumentTypes.PDF;
				} else if (extension.toUpperCase().equals("DOC") || 
						extension.toUpperCase().equals("DOCX")){
					tipodocumento = DocumentTypes.DOC;
				} else if (extension.toUpperCase().equals("JPG") ||
						extension.toUpperCase().equals("JPEG") ||
						extension.toUpperCase().equals("BMP") ||
						extension.toUpperCase().equals("JPE") ||
						extension.toUpperCase().equals("JFIF") ||
						extension.toUpperCase().equals("GIF") ||
						extension.toUpperCase().equals("TIF") ||
						extension.toUpperCase().equals("TIFF") ||
						extension.toUpperCase().equals("PNG")){
					tipodocumento = DocumentTypes.IMAGEN;
				} else{
					tipodocumento = DocumentTypes.TXT;
				}
				
				this.note.getListaDeDocumentos()[i].setNombre(documentFromDialog.getFileName());
				this.note.getListaDeDocumentos()[i].setExtension(extension);
				this.note.getListaDeDocumentos()[i].setTipo(tipodocumento);
			}
			}
	}

}
