/**
 * InformeObraCiudadanoPrintable.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.dialogs.interventions.panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.geopista.ui.components.DateField;
import com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.PostalDataOT;
import com.localgis.app.gestionciudad.dialogs.interventions.images.IconLoader;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.ImageFileView;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.ImageFilter;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.ImagePreview;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.vividsolutions.jump.I18N;

public class InformeObraCiudadanoPrintable extends JPanel implements Printable{

	
	private static Font dirFont = new Font(Font.DIALOG_INPUT, 0, 10);
	
	private JTextArea informationTextArea = null;
	private JPanel membreteJPanel = null;
	private JPanel ciudadanoInfoPanel = null;
	private JPanel fechaCorteInfoPanel = null;
	private String interventionString = "";
	private String actuationString = "";
	private ArrayList<String> callesInfo = null;

	
	
	public InformeObraCiudadanoPrintable( 
			String actuationType, 
			String interventionType,
			ArrayList<String> calles){
		super(new GridBagLayout());
		
		this.interventionString = interventionType;
		this.actuationString = actuationType;
		this.callesInfo  = calles;
		
		this.setBackground(Color.WHITE);
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		this.initialize();
			
		loadData();
	}
	
	
	private void loadData() {
		String text = "";
		
		text = text + 
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.information.comunication") +
		"\"" + this.actuationString +"\"" + " (" + this.interventionString +") " +
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.information.comunication2") + "\n\n" +
		I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.information.viasselecteds.infos") + "\n";
		if (this.callesInfo!=null && !this.callesInfo.isEmpty()) {
			Iterator<String> it = callesInfo.iterator();
			while(it.hasNext()){
				text = text + "- " + it.next() +". \n";	
			}
		}
		
		text = text + "\n";

		this.getInformationTextArea().setText(text);
		
	}


	private void initialize(){
		
		this.add(this.getMembreteJPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, 
						GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(30, 5, 20, 5), 20, 0));
		
		this.add(this.getCiudadanoInfoPanel(), 
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, 
						GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(40, 5, 0, 5), 20, 0));
		
		this.add(this.getScrollPane(), 
				new GridBagConstraints(0, 1, 2, 1, 0.1, 0.1, 
						GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(40, 20, 20, 40), 20, 0));
		
		this.add(this.getFechaCorteInfoPanel(), 
				new GridBagConstraints(0, 2, 2, 1, 0.1, 0.1, 
						GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 20, 0));
		
		this.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.information.goodbye")), 
				new GridBagConstraints(0, 3, 2, 1, 0.1, 0.1, 
						GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(20, 5, 0, 5), 20, 0));
	}
	
	
	
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9160983863436475532L;

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		  if (pageIndex > 0)
	          return NO_SUCH_PAGE;

	        Graphics2D g2d = (Graphics2D)graphics;
	        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
	        //-------------------------ESCALAR LA IMPRESION-------------------------------//
	        // g2d.scale( ((Number) campo_escala.getValue()).doubleValue(), ((Number) campo_escala.getValue()).doubleValue());
	        //----------------------------------------------------------------------------//
	        this.printAll(graphics);

	        return PAGE_EXISTS;
	}
	
	
	
	private JScrollPane scrollPane = null;
	private JScrollPane getScrollPane(){
		if(scrollPane == null){
			scrollPane = new JScrollPane(getInformationTextArea());
			scrollPane.setBorder(BorderFactory.createEmptyBorder());
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return scrollPane;
	}
	
	private JTextArea getInformationTextArea(){
		if (informationTextArea == null){
			informationTextArea =  new JTextArea("",15,50);
			informationTextArea.setBorder(null);
			informationTextArea.setLineWrap(true);
			informationTextArea.setWrapStyleWord(true);
			informationTextArea.setEditable(true);
			informationTextArea.setEnabled(true);
		}
		return informationTextArea;
	}
	
	
	
	private JPanel getMembreteJPanel(){
		if (membreteJPanel == null){
			membreteJPanel = new JPanel(new GridBagLayout());
			membreteJPanel.setBackground(Color.WHITE);
					
			membreteJPanel.add(this.getMembreteImageLabel(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, 
							GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 20, 0));
			
			membreteJPanel.add(this.getMembreteTextArea(), 
					new GridBagConstraints(0, 2, 2, 1, 0.1, 0.1, 
							GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(10, 5, 0, 5), 20, 0));
		}
		return membreteJPanel;
	}
	

	private JLabel membreteimage = null;
	private JTextField membreteTextArea = null;
	

	
	private JLabel getMembreteImageLabel(){
		if (membreteimage == null){
			membreteimage = new JLabel(IconLoader.icon(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.membrete.image.defaulicon")));
			membreteimage.setSize(70, 70);
			membreteimage.setToolTipText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.membrete.image.tooltipTex"));
			membreteimage.setCursor(new Cursor(Cursor.HAND_CURSOR));
			membreteimage.addMouseListener(new MouseListener(){
				@Override public void mouseClicked(MouseEvent e) {
				}

				@Override public void mouseEntered(MouseEvent e) {
//					lastCursor = AppContext.getApplicationContext().getMainFrame().getCursor();
//					AppContext.getApplicationContext().getMainFrame().setCursor(new Cursor(
//							Cursor.HAND_CURSOR));
				}

				@Override public void mouseExited(MouseEvent e) { 
//					if (lastCursor != null){
//						try{
//							AppContext.getApplicationContext().getMainFrame().setCursor(lastCursor);
//						} catch (Exception ex) {
//							ex.printStackTrace();
//							AppContext.getApplicationContext().getMainFrame().setCursor(new Cursor(
//									Cursor.DEFAULT_CURSOR));
//						}
//					} else{
//						AppContext.getApplicationContext().getMainFrame().setCursor(new Cursor(
//							Cursor.DEFAULT_CURSOR));
//					}
				}

				@Override public void mousePressed(MouseEvent e) {
					// Abrir dialogo para cargar una imagen.
					onImageLabelMousePressedDo();
				}
				
				@Override public void mouseReleased(MouseEvent e) {}
			});
		}
		return membreteimage;
	}
	
	private void onImageLabelMousePressedDo() {
		 JFileChooser chooser = new JFileChooser();

		 chooser.setDialogTitle(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.membrete.image.select.dialog.title"));
		 chooser.setMultiSelectionEnabled(false);
		 chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		 
		 chooser.addChoosableFileFilter(new ImageFilter());
		 chooser.setFileView(new ImageFileView());
		 chooser.setAccessory(new ImagePreview(chooser));

		 
		  int sel = chooser.showOpenDialog(this);
		    if (sel == JFileChooser.APPROVE_OPTION){
		      File selectedFile = chooser.getSelectedFile();
		      try{
		    	  if (selectedFile != null){
		    		  ImageIcon image = new ImageIcon(selectedFile.getPath());
		    		  ImageIcon iconoEscala = new ImageIcon(image.getImage().getScaledInstance(70, 70, java.awt.Image.SCALE_DEFAULT));
		    		  this.getMembreteImageLabel().setIcon(iconoEscala);
		    		  membreteimage.setSize(70, 70);
		    	  }
		      }catch (Exception e) {
		    	  e.printStackTrace();
		      }
		    }
	}
	
	private JTextField getMembreteTextArea(){
		if (membreteTextArea == null){
			membreteTextArea = new JTextField("LocalGis 2.0");
			membreteTextArea.setBorder(BorderFactory.createEmptyBorder());
			membreteTextArea.setHorizontalAlignment(JTextField.CENTER);
			}
		return membreteTextArea;
	}
	
	private JTextArea ciudadanoInfoTextArea = null;
	
	private JPanel getCiudadanoInfoPanel(){
		if (ciudadanoInfoPanel == null){
			ciudadanoInfoPanel = new JPanel(new GridBagLayout());
			
			this.add(getCiudadanoInfoTextArea());
		}
		return ciudadanoInfoPanel;
	}
	
	private JTextArea getCiudadanoInfoTextArea(){
		if (ciudadanoInfoTextArea == null){
			ciudadanoInfoTextArea = new JTextArea(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.citizien.info"),8,30);
			ciudadanoInfoTextArea.setBorder(null);
			ciudadanoInfoTextArea.setLineWrap(true);
			ciudadanoInfoTextArea.setWrapStyleWord(true);
			ciudadanoInfoTextArea.setEditable(false);
			ciudadanoInfoTextArea.setEnabled(true);
			ciudadanoInfoTextArea.setFont(this.dirFont);
		}
		return ciudadanoInfoTextArea;
	}
	
		
	
	
	
	
	private JRadioButton onlyoneDayRadioButton = null;
	private JRadioButton betweenDatesRadioButton =null;
	private ButtonGroup buttonGroup = new ButtonGroup(); 
	private JLabel fromLabel = null;
	private JLabel toLabel = null;
	private DateField fromDateField = null;
	private DateField toDateField = null;
	
	
	private JPanel getFechaCorteInfoPanel(){
		if (fechaCorteInfoPanel == null){
			fechaCorteInfoPanel = new JPanel(new GridBagLayout());
			fechaCorteInfoPanel.setBackground(Color.WHITE);
			
			JPanel radioButtonsPanel = new JPanel(new GridBagLayout());
			radioButtonsPanel.setBackground(Color.WHITE);
			radioButtonsPanel.add(getBetweenDatesRadioButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			radioButtonsPanel.add(getOnlyoneDayRadioButton(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			fechaCorteInfoPanel.add(radioButtonsPanel, 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			JPanel infoPanel = new JPanel(new GridBagLayout());
			infoPanel.setBackground(Color.WHITE);
			infoPanel.add(getFromLabel(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			infoPanel.add(getFromDateField(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			infoPanel.add(getToLabel(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			infoPanel.add(getToDateField(), 
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			fechaCorteInfoPanel.add(infoPanel, 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
		}
		return fechaCorteInfoPanel;
	}
	
	private JRadioButton getOnlyoneDayRadioButton(){
		if (onlyoneDayRadioButton == null){
			onlyoneDayRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.information.viasselecteds.onedate.radiobuttonlabel"));
			buttonGroup.add(onlyoneDayRadioButton);
			onlyoneDayRadioButton.setBackground(Color.WHITE);
			onlyoneDayRadioButton.addItemListener(new ItemListener(){
				@Override public void itemStateChanged(ItemEvent e) {
					datesFieldsRenderer(e);
				}	
			});
		}
		return onlyoneDayRadioButton;
	}
	
	
	private JRadioButton getBetweenDatesRadioButton(){
		if (betweenDatesRadioButton == null){
			betweenDatesRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.information.viasselecteds.twodate.radiobuttonlabel"));
			buttonGroup.add(betweenDatesRadioButton);
			betweenDatesRadioButton.setSelected(true);
			betweenDatesRadioButton.setBackground(Color.WHITE);
			betweenDatesRadioButton.addItemListener(new ItemListener(){
				@Override public void itemStateChanged(ItemEvent e) {
					datesFieldsRenderer(e);
				}	
			});
		}
		return betweenDatesRadioButton;
	}
	
	private void datesFieldsRenderer(ItemEvent e) {
		if (e.getItem().equals(getBetweenDatesRadioButton()) || 
				e.getItem().equals(getOnlyoneDayRadioButton())){
			
			if (getBetweenDatesRadioButton().isSelected()){
				this.getToLabel().setVisible(true);
				this.getToDateField().setVisible(true);
				this.getFromLabel().setText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.information.viasselecteds.froms"));
			} else if (getOnlyoneDayRadioButton().isSelected()){
				this.getToLabel().setVisible(false);
				this.getToDateField().setVisible(false);
				this.getFromLabel().setText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.information.viasselecteds.onedate"));
			}
			
		}
	}	
	
	private JLabel getFromLabel(){
		if(fromLabel == null){
			fromLabel = new JLabel();
			fromLabel.setText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.information.viasselecteds.froms"));
		}
		return fromLabel;
	}
	
	private JLabel getToLabel(){
		if(toLabel == null){
			toLabel = new JLabel();
			toLabel.setText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.works.information.viasselecteds.to"));
		}
		return toLabel;
	}
	
	private DateField getFromDateField(){
		if (fromDateField == null){
			fromDateField = new DateField(new GregorianCalendar(),0);
			fromDateField.getDateEditor().getUiComponent().setBorder(BorderFactory.createEmptyBorder());
			fromDateField.setForeground(Color.WHITE);
		}
		return fromDateField;
	}
	private DateField getToDateField(){
		if (toDateField == null){
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(GregorianCalendar.DATE, 1);
			toDateField = new DateField(calendar,0);
			toDateField.getDateEditor().getUiComponent().setBorder(BorderFactory.createEmptyBorder());
			toDateField.setForeground(Color.WHITE);
		}
		return toDateField;
	}

	
	public void prepareToPrint() {
		this.getBetweenDatesRadioButton().setVisible(false);
		this.getOnlyoneDayRadioButton().setVisible(false);
		
		this.getToDateField().getCalendarButton().setVisible(false);
		
		
		this.getFromDateField().getCalendarButton().setVisible(false);
	}
	
	
	public void finishPrint() {
		this.getBetweenDatesRadioButton().setVisible(true);
		this.getOnlyoneDayRadioButton().setVisible(true);
				
		this.getToDateField().getCalendarButton().setVisible(true);
		
		this.getFromDateField().getCalendarButton().setVisible(true);

	}


	public void loadPostalData(PostalDataOT next) {
		if (next!=null){
			
			this.getCiudadanoInfoTextArea().setText("");
			
			String labelNomreApellidos = next.getNombre().trim() + " " +
			next.getApellido1().trim() + " " + next.getApellido2().trim();
			
			String labelDirección = next.getTipovia().trim() + " " +next.getNombrevia().trim()+", " + next.getNumero();
			
			this.getCiudadanoInfoTextArea().append(labelNomreApellidos + "\n");
			this.getCiudadanoInfoTextArea().append(labelDirección + "\n");
			
		}
	}

}


