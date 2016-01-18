/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.app.layerutil.exception.DataException;
import com.localgis.app.gestionciudad.ConstantessLocalGISObraCivil;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.utils.GestionCiudadOperaciones;
import com.localgis.app.gestionciudad.utils.OperacionesConFechasObraCivil;
import com.vividsolutions.jump.I18N;

/**
 * @author javieraragon
 *
 */
public class AvisosViewPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4414020503855826130L;
	private LocalGISIntervention aviso = null;
	private JLabel descripcionLabel = null;
	private JLabel actuacionLabel = null;
	private JLabel causaLabel = null;
	private JLabel fechaAltaLabel = null;
	private JLabel fechaProximoAvisoLabel = null;
	private JLabel retrasoLabel = null;
//	private TextArea listaDocumentosTextArea = null;
	
	private static Font fuenteLabels = new Font(Font.DIALOG_INPUT, 0, 12);


	public AvisosViewPanel(LocalGISIntervention aviso){
		super(new GridBagLayout());
		this.aviso = aviso;
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		this.initialize();
		loadAviso(this.aviso);
	}


	public void loadAviso(LocalGISIntervention aviso){
		if (aviso != null){
			this.aviso = aviso;
			try{
				String title = I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.caducadosdialog.datosbordertitle")  + aviso.getId();
				((TitledBorder)this.getBorder()).setTitle(title);
				this.updateUI();
			}catch (Exception e) {
				e.printStackTrace();
			}

			if (aviso.getDescription()!=null){
				this.getDescripcionLabel().setText(
						I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.descripcion") + 
						aviso.getDescription() );
			}
			else{
				this.getDescripcionLabel().setText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.nodescripcion"));
			}

			if (aviso.getActuationType() != null){
				GestionCiudadOperaciones op = new GestionCiudadOperaciones();
				String traduccion = "";
				try {
					traduccion = op.obtenerTraduccionDeActuacion(aviso.getActuationType());
				} catch (DataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (traduccion!=null){
					this.getActuacionLabel().setText(traduccion);
				}
			}
			else{
				this.getActuacionLabel().setText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.noactuacion"));
			}

			if (aviso.getCauses()!= null){
				this.getCausaLabel().setText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.causa")
						+ aviso.getCauses());
			}
			else{
				this.getCausaLabel().setText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.nocausa"));
			}

			String campoFechaAlta = I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.fechaalta");
			if (aviso.getStartWarning() != null){
				try{
					this.getFechaAltaLabel().setText(campoFechaAlta
							+ ConstantessLocalGISObraCivil.DateFormat.format(aviso.getStartWarning().getTime()));
				}catch (IllegalArgumentException e) {
					e.printStackTrace();
					this.getFechaAltaLabel().setText(campoFechaAlta);
				}
			} else{this.getFechaAltaLabel().setText(campoFechaAlta);}

			
			String campoProxFecha = I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.proximoaviso");
			if (aviso.getNextWarning() != null){
				try{
					this.getFechaProximoAvisoLabel().setText(campoProxFecha
							+ ConstantessLocalGISObraCivil.DateFormat.format(aviso.getNextWarning().getTime()));
				} catch ( IllegalArgumentException e) {
					e.printStackTrace();
					this.getFechaProximoAvisoLabel().setText(campoProxFecha);
				}
			} else{this.getFechaProximoAvisoLabel().setText(campoProxFecha);}

			String campoRetraso = I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.retraso");
			if (aviso.getPattern()!= null){
				if (aviso.getNextWarning()!=null){
					String retraso = OperacionesConFechasObraCivil.obtenerDuracionVencimiento((GregorianCalendar) aviso.getNextWarning());
					if (!retraso.equals("HOY") && ((new GregorianCalendar()).getTimeInMillis()-aviso.getNextWarning().getTimeInMillis()) > 0 ){
						campoRetraso = campoRetraso + I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.hace") + " ";
					}
					this.getRetrasoLabel().setText(campoRetraso + retraso);
				} else{
					this.getRetrasoLabel().setText(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.finalizada"));
				}
			} else{
				this.getRetrasoLabel().setText("");
			}


//			this.getListaDocumentosTextArea().setText("");
//			if (aviso.getListaDeDocumentos() != null && aviso.getListaDeDocumentos().length > 0){
//				this.getListaDocumentosTextArea().append(UtilidadesAvisosPanels.DocumentListToParsedString(aviso.getListaDeDocumentos()));
//			}
		}
	}


	private void initialize() {
		this.setBorder(BorderFactory.createTitledBorder
				(null,I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.caducadosdialog.datosbordertitle"), 
						TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));

		JPanel linea1 = new JPanel(new GridBagLayout());
		linea1.add(this.getDescripcionLabel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));

		linea1.add(this.getCausaLabel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		linea1.add(this.getActuacionLabel(), 
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
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
		linea4.add(this.getFechaProximoAvisoLabel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		linea4.add(this.getRetrasoLabel(), 
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
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


	private JLabel getActuacionLabel(){
		if (actuacionLabel == null){
			actuacionLabel = new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.actuacion"));
			actuacionLabel.setFont(fuenteLabels);
		}
		return actuacionLabel;
	}

	private JLabel getCausaLabel(){
		if (causaLabel == null){
			causaLabel = new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.causa"));
			causaLabel.setFont(fuenteLabels);		
		}
		return causaLabel;
	}

	private JLabel getFechaAltaLabel(){
		if (fechaAltaLabel == null){
			fechaAltaLabel = new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.fechaalta"));
			fechaAltaLabel.setFont(fuenteLabels);
		}
		return fechaAltaLabel;
	}

	private JLabel getFechaProximoAvisoLabel(){
		if (fechaProximoAvisoLabel == null){
			fechaProximoAvisoLabel = new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.proximoaviso"));
			fechaProximoAvisoLabel.setFont(fuenteLabels);
		}
		return fechaProximoAvisoLabel;
	}
	
	private JLabel getRetrasoLabel(){
		if (retrasoLabel == null){
			retrasoLabel =new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.fields.retraso"));
			retrasoLabel.setFont(fuenteLabels);
		}
		return retrasoLabel;
	}
	
//	private TextArea getListaDocumentosTextArea(){
//		if (listaDocumentosTextArea == null){
//			listaDocumentosTextArea =  new TextArea("",2,10,
//					TextArea.SCROLLBARS_VERTICAL_ONLY );
//			listaDocumentosTextArea.setEditable(false);
//		}
//		return listaDocumentosTextArea;
//	}


	public void setAviso(LocalGISIntervention aviso){
		this.aviso = aviso;
	}
	public LocalGISIntervention getAviso(){
		return aviso;
	}
}
