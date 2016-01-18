/**
 * NotesFieldsPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.dialogs.notes.panels;

import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.ConfigurationException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.geopista.ui.components.DateField;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.NotesInterventionsEditionTypes;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.toedter.calendar.JTextFieldDateEditor;
import com.vividsolutions.jump.I18N;

/**
 * @author javieraragon
 *
 */
public class NotesFieldsPanel  extends JPanel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -689473355522947949L;

	private NotesInterventionsEditionTypes tipoEdicion = null;

	private TextArea descripcionTextArea = null;
	private DateField fechaAltaDateField = null;

	private JPanel panelFechaAlta = null;

	private LocalGISNote note = null;

	public NotesFieldsPanel(NotesInterventionsEditionTypes edicion, LocalGISNote note){
		super(new GridBagLayout());
		this.tipoEdicion = edicion;
		this.note = note;

		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();

		this.initialize();

		loadAviso(note);
	}

	private void loadAviso(LocalGISNote note){
		if (note != null){
			if (note.getDescription() != null){
				this.getDescripcionTextArea().setText(note.getDescription());
			}

			if (note.getStartWarning() != null){
				this.getFechaAltaDateField().setDate(note.getStartWarning().getTime());
			}
		}
	}

	private void initialize(){

		this.setBorder(BorderFactory.createTitledBorder
				(null,I18N.get("avisospanels","localgisgestionciudad.interfaces.notes.panel.bordertitle"), 
						TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));

		this.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.descripcion")), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		this.add(this.getDescripcionTextArea(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		this.add(getPanelFechas(), 
				new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));


	}

	private JPanel getPanelFechas(){
		if (panelFechaAlta == null){
			panelFechaAlta = new JPanel(new GridBagLayout());

			panelFechaAlta.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.fechaalta")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_END, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			panelFechaAlta.add(this.getFechaAltaDateField(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
		}
		return panelFechaAlta;
	}

	private TextArea getDescripcionTextArea(){
		if (descripcionTextArea == null){
			descripcionTextArea = new TextArea("",2,100,
					TextArea.SCROLLBARS_VERTICAL_ONLY );

			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				descripcionTextArea.setEditable(false);
			}
		}
		return this.descripcionTextArea;
	}

	private DateField getFechaAltaDateField(){
		if (fechaAltaDateField == null){
			fechaAltaDateField = new DateField(new Date(), 0);

			if (this.tipoEdicion.equals(NotesInterventionsEditionTypes.VIEW)){
				((JTextFieldDateEditor)fechaAltaDateField.getDateEditor().getUiComponent()).setEditable(false);
				fechaAltaDateField.getCalendarButton().setEnabled(false);
			}
		}
		return fechaAltaDateField;
	}

	public LocalGISNote getNoteData(){
		LocalGISNote resultado = null;
		if (this.note != null){
			resultado = note;
		} else{
			resultado = new LocalGISNote();
		}
		
		
		
		String descripcion = this.getDescripcionTextArea().getText();
		if (descripcion != null){
			resultado.setDescription(descripcion);
		} else{
			resultado.setDescription("");	
		}
		
		if (this.getFechaAltaDateField().getDate()!=null && this.getFechaAltaDateField().getText()!=null 
				&& this.getFechaAltaDateField().getText() != ""){
				GregorianCalendar fehcaAlta = new GregorianCalendar();
				fehcaAlta.setTime(this.getFechaAltaDateField().getDate());
				resultado.setStartWarning(fehcaAlta);
		} else{
			resultado.setStartWarning(new GregorianCalendar());
		}
		
		
		return resultado;
	}
	
	public LocalGISNote loadDataToNote(LocalGISNote note){
		if (note == null){
			note = new LocalGISNote();
		}
			
		String descripcion = this.getDescripcionTextArea().getText();
		if (descripcion != null){
			note.setDescription(descripcion);
		} else{
			note.setDescription("");	
		}
		
		if (this.getFechaAltaDateField().getDate()!=null && this.getFechaAltaDateField().getText()!=null 
				&& this.getFechaAltaDateField().getText() != ""){
				GregorianCalendar fehcaAlta = new GregorianCalendar();
				fehcaAlta.setTime(this.getFechaAltaDateField().getDate());
				note.setStartWarning(fehcaAlta);
		} else{
			note.setStartWarning(new GregorianCalendar());
		}
		
				
		return note;
	}
	
	public void setNote(LocalGISNote aviso){
		this.note = aviso;
	}
	
	public LocalGISNote getAviso(){
		return this.note;
	}
}
