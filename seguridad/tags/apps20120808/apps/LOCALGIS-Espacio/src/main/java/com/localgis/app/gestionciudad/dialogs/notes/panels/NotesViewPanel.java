/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.notes.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.localgis.app.gestionciudad.ConstantessLocalGISObraCivil;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.vividsolutions.jump.I18N;

/**
 * @author javieraragon
 *
 */
public class NotesViewPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4414020503855826130L;
	private LocalGISNote note = null;
	private JLabel descripcionLabel = null;
	private TextArea descripcionTextArea = null;
	private JLabel fechaAltaLabel = null;
//	private TextArea listaDocumentosTextArea = null;
	
	private static Font fuenteLabels = new Font(Font.DIALOG_INPUT, 0, 12);


	public NotesViewPanel(LocalGISNote note){
		super(new GridBagLayout());
		this.note = note;
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		this.initialize();
		loadNote(this.note);
	}


	public void loadNote(LocalGISNote note){
		if (note != null){
			this.note = note;
			try{
				String title = I18N.get("avisospanels","localgisgestionciudad.interfaces.notes.caducadosdialog.datosbordertitle")  + note.getId();
				((TitledBorder)this.getBorder()).setTitle(title);
				this.updateUI();
			}catch (Exception e) {
				e.printStackTrace();
			}

			if (note.getDescription()!=null){
				this.getDescripcionTextArea().setText(note.getDescription());
			}
			else{
				this.getDescripcionLabel().setText("");
			}

			
			String campoFechaAlta = I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.fechaalta");
			if (note.getStartWarning() != null){
				try{
					this.getFechaAltaLabel().setText(campoFechaAlta
							+ ConstantessLocalGISObraCivil.DateFormat.format(note.getStartWarning().getTime()));
				}catch (IllegalArgumentException e) {
					e.printStackTrace();
					this.getFechaAltaLabel().setText(campoFechaAlta);
				}
			} else{this.getFechaAltaLabel().setText(campoFechaAlta);}

			
//			this.getListaDocumentosTextArea().setText("");
//			if (note.getListaDeDocumentos() != null && note.getListaDeDocumentos().length > 0){
//				this.getListaDocumentosTextArea().append(UtilidadesAvisosPanels.DocumentListToParsedString(note.getListaDeDocumentos()));
//			}
		}
	}


	private void initialize() {
		this.setBorder(BorderFactory.createTitledBorder
				(null,I18N.get("avisospanels","localgisgestionciudad.interfaces.notes.caducadosdialog.datosbordertitle"), 
						TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));

		JPanel linea1 = new JPanel(new GridBagLayout());
		linea1.add(this.getDescripcionLabel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		linea1.add(this.getDescripcionTextArea(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		this.add(linea1, 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		JPanel linea4 = new JPanel(new GridBagLayout());
		linea4.add(this.getFechaAltaLabel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		this.add(linea4, 
				new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

//		JPanel linea5 = new JPanel(new GridBagLayout());
//		linea5.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.listadomunetos")), 
//				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
//						GridBagConstraints.HORIZONTAL, 
//						new Insets(0, 5, 0, 5), 0, 0));
//		linea5.add(this.getListaDocumentosTextArea(), 
//				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
//						GridBagConstraints.HORIZONTAL, 
//						new Insets(0, 5, 0, 5), 0, 0));
//		this.add(linea5, 
//				new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
//						GridBagConstraints.HORIZONTAL, 
//						new Insets(0, 5, 0, 5), 0, 0));
	}


	private JLabel getDescripcionLabel(){
		if (descripcionLabel == null){
			descripcionLabel = new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.descripcion"));
			descripcionLabel.setFont(fuenteLabels);
		}
		return descripcionLabel;
	}




	private JLabel getFechaAltaLabel(){
		if (fechaAltaLabel == null){
			fechaAltaLabel = new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.fechaalta"));
			fechaAltaLabel.setFont(fuenteLabels);
		}
		return fechaAltaLabel;
	}

	
//	private TextArea getListaDocumentosTextArea(){
//		if (listaDocumentosTextArea == null){
//			listaDocumentosTextArea =  new TextArea("",2,10,
//					TextArea.SCROLLBARS_VERTICAL_ONLY );
//			listaDocumentosTextArea.setEditable(false);
//		}
//		return listaDocumentosTextArea;
//	}
	
	private TextArea getDescripcionTextArea(){
		if (descripcionTextArea == null){
			descripcionTextArea  =  new TextArea("",4,10,
					TextArea.SCROLLBARS_VERTICAL_ONLY );
			descripcionTextArea.setEditable(false);
		}
		return descripcionTextArea;
	}


	public void setAviso(LocalGISNote aviso){
		this.note = aviso;
	}
	public LocalGISNote getAviso(){
		return note;
	}
}
