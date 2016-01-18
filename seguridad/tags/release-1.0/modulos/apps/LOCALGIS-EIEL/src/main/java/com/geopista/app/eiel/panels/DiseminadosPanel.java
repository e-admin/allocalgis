package com.geopista.app.eiel.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.DiseminadosEIEL;
import com.geopista.app.eiel.beans.MunicipioEIEL;
import com.geopista.app.eiel.dialogs.DiseminadosDialog;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.geopista.app.eiel.utils.UbicacionListCellRenderer;
import com.geopista.app.eiel.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;

public class DiseminadosPanel extends JPanel implements FeatureExtendedPanel
{
    
    private static final long serialVersionUID = 1L;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    private JPanel jPanelIdentificacion = null;
    private JPanel jPanelInformacion = null;
    private JPanel jPanelRevision = null;
    private JScrollPane jScroll = null;
    
    private boolean okPressed = false;	

	private JLabel jLabelCodProv = null;
	private JLabel jLabelCodMunic = null;
	private JLabel jLabelPadron = null;
    private JLabel jLabelPobEst = null;
    private JLabel jLabelVivTot = null;
    private JLabel jLabelHotel = null;
    private JLabel jLabelCasaRural = null;
    private JLabel jLabelLongAbast = null;
    private JLabel jLabelVivConAbast = null;
    private JLabel jLabelVivSinAbast = null;
    
	private JComboBox jComboBoxProvincia = null;
    private JComboBox jComboBoxMunicipio = null;
    private JTextField jTextFieldPadron = null;
    private JTextField jTextFieldPobEst = null;
    private JTextField jTextFieldVivTot = null;
    private JTextField jTextFieldHotel = null;
    private JTextField jTextFieldCasaRural = null;
    private JTextField jTextFieldLongAbast = null;
    private JTextField jTextFieldVivConAbast = null;
    private JTextField jTextFieldVivSinAbast = null;
    
    private JLabel jLabelConsInvierno = null;
    private JLabel jLabelConsVerano = null;
    private JLabel jLabelVivExcPresion = null;
    private JLabel jLabelVivDefPresion = null;
    private JLabel jLabelLongDefAbast = null;
    private JLabel jLabelVivDefAbast = null;
    private JLabel jLabelPobResDefAbast = null;
    private JLabel jLabelPobEstDefAbast = null;
    private JLabel jLabelPobResAbastAuto = null;
    private JLabel jLabelPobEstAbastAuto = null;
    
    private JTextField jTextFieldConsInvierno = null;
    private JTextField jTextFieldConsVerano = null;
    private JTextField jTextFieldVivExcPresion = null;
    private JTextField jTextFieldVivDefPresion = null;
    private JTextField jTextFieldLongDefAbast = null;
    private JTextField jTextFieldVivDefAbast = null;
    private JTextField jTextFieldPobResDefAbast = null;
    private JTextField jTextFieldPobEstDefAbast = null;
    private JTextField jTextFieldPobResAbastAuto = null;
    private JTextField jTextFieldPobEstAbastAuto = null;
    
    private JLabel jLabelVivDefAbastAuto = null;
    private JLabel jLabelPobResDefAbastAuto = null;
    private JLabel jLabelPobEstDefAbastAuto = null;
    private JLabel jLabelFuentesNo = null;
    private JLabel jLabelLongSaneam = null;
    private JLabel jLabelVivConSaneam = null;
    private JLabel jLabelVivSinSaneam = null;
    private JLabel jLabelLongDefSaneam = null;
    private JLabel jLabelVivDefSaneam = null;
    private JLabel jLabelPobResDefSaneam = null;
    
    private JTextField jTextFieldVivDefAbastAuto = null;
    private JTextField jTextFieldPobResDefAbastAuto = null;
    private JTextField jTextFieldPobEstDefAbastAuto = null;
    private JTextField jTextFieldFuentesNo = null;
    private JTextField jTextFieldLongSaneam = null;
    private JTextField jTextFieldVivConSaneam = null;
    private JTextField jTextFieldVivSinSaneam = null;
    private JTextField jTextFieldLongDefSaneam = null;
    private JTextField jTextFieldVivDefSaneam = null;
    private JTextField jTextFieldPobResDefSaneam = null;
    
    private JLabel jLabelPobEstDefSaneam = null;
    private JLabel jLabelCaudalDes = null;
    private JLabel jLabelCaudalTrat = null;
    private JLabel jLabelVivSaneamAuto = null;
    private JLabel jLabelPobEstSaneamAuto = null;
    private JLabel jLabelVivDefSaneamAuto = null;
    private JLabel jLabelPobResDefSaneamAuto = null;
    private JLabel jLabelPobEstDefSaneamAuto = null;
    private JLabel jLabelTmBasura = null;
    private JLabel jLabelContenedores = null;
    
    private JTextField jTextFieldPobEstDefSaneam = null;
    private JTextField jTextFieldCaudalDes = null;
    private JTextField jTextFieldCaudalTrat = null;
    private JTextField jTextFieldVivSaneamAuto = null;
    private JTextField jTextFieldPobEstSaneamAuto = null;
    private JTextField jTextFieldVivDefSaneamAuto = null;
    private JTextField jTextFieldPobResDefSaneamAuto = null;
    private JTextField jTextFieldPobEstDefSaneamAuto = null;
    private JTextField jTextFieldTmBasura = null;
    private JTextField jTextFieldContenedores = null;
    
    private JLabel jLabelVivSinBasura = null;
    private JLabel jLabelPobResSinBasura = null;
    private JLabel jLabelPobEstSinBasura = null;
    private JLabel jLabelPlanLimpieza = null;
    private JLabel jLabelPtosLuz = null;
    private JLabel jLabelVivSinAlumbrado = null;
    private JLabel jLabelLongDefAlumbrado = null;
    private JLabel jLabelVivAbastAuto = null;
    private JLabel jLabelFuentesCtrl = null;
    private JLabel jLabelPobResSaneamAuto = null;
    private JLabel jLabelFechaRev = null;
    private JLabel jLabelEstadoRev = null;

    private JTextField jTextFieldVivSinBasura = null;
    private JTextField jTextFieldPobResSinBasura = null;
    private JTextField jTextFieldPobEstSinBasura = null;
    private JTextField jTextFieldPlanLimpieza = null;
    private JTextField jTextFieldPtosLuz = null;
    private JTextField jTextFieldVivSinAlumbrado = null;
    private JTextField jTextFieldLongDefAlumbrado = null;
    private JTextField jTextFieldVivAbastAuto = null;
    private JTextField jTextFieldFuentesCtrl = null;
    private JTextField jTextFieldPobResSaneamAuto = null;
    private JTextField jTextFieldFechaRev = null;
    private ComboBoxEstructuras jComboBoxEstadoRev = null;
	
	
    public DiseminadosPanel(){
        super();
        initialize();
    }
  
    public DiseminadosPanel(DiseminadosEIEL dato){
        super();
        initialize();
        loadData (dato);
    }
    
    public void loadData(DiseminadosEIEL elemento){
        if (elemento!=null){
        	/* Campos Clave */
        	if (elemento.getCodINEProvincia() != null) {
				jComboBoxProvincia
						.setSelectedIndex(provinciaIndexSeleccionar(elemento
								.getCodINEProvincia()));
			} else {
				jComboBoxProvincia.setSelectedIndex(0);
			}

			if (elemento.getCodINEMunicipio() != null) {
				jComboBoxMunicipio
						.setSelectedIndex(municipioIndexSeleccionar(elemento
								.getCodINEMunicipio()));
			} else {
				jComboBoxMunicipio.setSelectedIndex(0);
			}
            /* Otros Campos */
            if (elemento.getPadron() != null){
            	jTextFieldPadron.setText(elemento.getPadron().toString());
        	} else{
        		jTextFieldPadron.setText("");
        	}
            if (elemento.getPoblacionEstacional() != null){
            	jTextFieldPobEst.setText(elemento.getPoblacionEstacional().toString());
        	} else{
        		jTextFieldPobEst.setText("");
        	}
            if (elemento.getViviendasTotales() != null){
            	jTextFieldVivTot.setText(elemento.getViviendasTotales().toString());
        	} else{
        		jTextFieldVivTot.setText("");
        	}
            if (elemento.getPlazasHoteleras() != null){
            	jTextFieldHotel.setText(elemento.getPlazasHoteleras().toString());
        	} else{
        		jTextFieldHotel.setText("");
        	}
            if (elemento.getPlazasCasasRurales() != null){
            	jTextFieldCasaRural.setText(elemento.getPlazasCasasRurales().toString());
        	} else{
        		jTextFieldCasaRural.setText("");
        	}
            if (elemento.getLongitudAbastecimiento() != null){
            	jTextFieldLongAbast.setText(elemento.getLongitudAbastecimiento().toString());
        	} else{
        		jTextFieldLongAbast.setText("");
        	}
            if (elemento.getViviendasConAbastecimiento() != null){
            	jTextFieldVivConAbast.setText(elemento.getViviendasConAbastecimiento().toString());
        	} else{
        		jTextFieldVivConAbast.setText("");
        	}
            if (elemento.getViviendasSinAbastecimiento() != null){
            	jTextFieldVivSinAbast.setText(elemento.getViviendasSinAbastecimiento().toString());
        	} else{
        		jTextFieldVivSinAbast.setText("");
        	}
            if (elemento.getConsumoInvierno() != null){
            	jTextFieldConsInvierno.setText(elemento.getConsumoInvierno().toString());
        	} else{
        		jTextFieldConsInvierno.setText("");
        	}
            if (elemento.getConsumoVerano() != null){
            	jTextFieldConsVerano.setText(elemento.getConsumoVerano().toString());
        	} else{
        		jTextFieldConsVerano.setText("");
        	}
            if (elemento.getViviendasExcesoPresion() != null){
            	jTextFieldVivExcPresion.setText(elemento.getViviendasExcesoPresion().toString());
        	} else{
        		jTextFieldVivExcPresion.setText("");
        	}
            if (elemento.getViviendasDefectoPresion() != null){
            	jTextFieldVivDefPresion.setText(elemento.getViviendasDefectoPresion().toString());
        	} else{
        		jTextFieldVivDefPresion.setText("");
        	}
            if (elemento.getLongDeficitariaAbast() != null){
            	jTextFieldLongDefAbast.setText(elemento.getLongDeficitariaAbast().toString());
        	} else{
        		jTextFieldLongDefAbast.setText("");
        	}
            if (elemento.getViviendasDeficitAbast() != null){
            	jTextFieldVivDefAbast.setText(elemento.getViviendasDeficitAbast().toString());
        	} else{
        		jTextFieldVivDefAbast.setText("");
        	}
            if (elemento.getPoblacionResidenteDefAbast() != null){
            	jTextFieldPobResDefAbast.setText(elemento.getPoblacionResidenteDefAbast().toString());
        	} else{
        		jTextFieldPobResDefAbast.setText("");
        	}
            if (elemento.getPoblacionEstacionalDefAbast() != null){
            	jTextFieldPobEstDefAbast.setText(elemento.getPoblacionEstacionalDefAbast().toString());
        	} else{
        		jTextFieldPobEstDefAbast.setText("");
        	}
            if (elemento.getPoblacionResidenteAbastAuto() != null){
            	jTextFieldPobResAbastAuto.setText(elemento.getPoblacionResidenteAbastAuto().toString());
        	} else{
        		jTextFieldPobResAbastAuto.setText("");
        	}
            if (elemento.getPoblacionEstacionalAbastAuto() != null){
            	jTextFieldPobEstAbastAuto.setText(elemento.getPoblacionEstacionalAbastAuto().toString());
        	} else{
        		jTextFieldPobEstAbastAuto.setText("");
        	}
            if (elemento.getViviendasDefAbastAuto() != null){
            	jTextFieldVivDefAbastAuto.setText(elemento.getViviendasDefAbastAuto().toString());
        	} else{
        		jTextFieldVivDefAbastAuto.setText("");
        	}
            if (elemento.getPoblacionResidenteDefAbastAuto() != null){
            	jTextFieldPobResDefAbastAuto.setText(elemento.getPoblacionResidenteDefAbastAuto().toString());
        	} else{
        		jTextFieldPobResDefAbastAuto.setText("");
        	}
            if (elemento.getPoblacionEstacionalDefAbastAuto() != null){
            	jTextFieldPobEstDefAbastAuto.setText(elemento.getPoblacionEstacionalDefAbastAuto().toString());
        	} else{
        		jTextFieldPobEstDefAbastAuto.setText("");
        	}
            if (elemento.getFuentesNoControladas() != null){
            	jTextFieldFuentesNo.setText(elemento.getFuentesNoControladas().toString());
        	} else{
        		jTextFieldFuentesNo.setText("");
        	}
            if (elemento.getLongitudSaneamiento() != null){
            	jTextFieldLongSaneam.setText(elemento.getLongitudSaneamiento().toString());
        	} else{
        		jTextFieldLongSaneam.setText("");
        	}
            if (elemento.getViviendasConSaneamiento() != null){
            	jTextFieldVivConSaneam.setText(elemento.getViviendasConSaneamiento().toString());
        	} else{
        		jTextFieldVivConSaneam.setText("");
        	}
            if (elemento.getViviendasSinSaneamiento() != null){
            	jTextFieldVivSinSaneam.setText(elemento.getViviendasSinSaneamiento().toString());
        	} else{
        		jTextFieldVivSinSaneam.setText("");
        	}
            if (elemento.getLongDeficitariaSaneam() != null){
            	jTextFieldLongDefSaneam.setText(elemento.getLongDeficitariaSaneam().toString());
        	} else{
        		jTextFieldLongDefSaneam.setText("");
        	}
            if (elemento.getViviendasDefSaneam() != null){
            	jTextFieldVivDefSaneam.setText(elemento.getViviendasDefSaneam().toString());
        	} else{
        		jTextFieldVivDefSaneam.setText("");
        	}
            if (elemento.getPoblacionResidenteDefSaneam() != null){
            	jTextFieldPobResDefSaneam.setText(elemento.getPoblacionResidenteDefSaneam().toString());
        	} else{
        		jTextFieldPobResDefSaneam.setText("");
        	}
            if (elemento.getPoblacionEstacionalDefSaneam() != null){
            	jTextFieldPobEstDefSaneam.setText(elemento.getPoblacionEstacionalDefSaneam().toString());
        	} else{
        		jTextFieldPobEstDefSaneam.setText("");
        	}
            if (elemento.getCaudalDesaguado() != null){
            	jTextFieldCaudalDes.setText(elemento.getCaudalDesaguado().toString());
        	} else{
        		jTextFieldCaudalDes.setText("");
        	}
            if (elemento.getCaudalTratado() != null){
            	jTextFieldCaudalTrat.setText(elemento.getCaudalTratado().toString());
        	} else{
        		jTextFieldCaudalTrat.setText("");
        	}
            if (elemento.getViviendasSaneamientoAuto() != null){
            	jTextFieldVivSaneamAuto.setText(elemento.getViviendasSaneamientoAuto().toString());
        	} else{
        		jTextFieldVivSaneamAuto.setText("");
        	}
            if (elemento.getPoblacionEstacionalSaneamAuto() != null){
            	jTextFieldPobEstSaneamAuto.setText(elemento.getPoblacionEstacionalSaneamAuto().toString());
        	} else{
        		jTextFieldPobEstSaneamAuto.setText("");
        	}
            if (elemento.getViviendasDeficitSaneamAuto() != null){
            	jTextFieldVivDefSaneamAuto.setText(elemento.getViviendasDeficitSaneamAuto().toString());
        	} else{
        		jTextFieldVivDefSaneamAuto.setText("");
        	}
            if (elemento.getPoblacionResidenteDefSaneamAuto() != null){
            	jTextFieldPobResDefSaneamAuto.setText(elemento.getPoblacionResidenteDefSaneamAuto().toString());
        	} else{
        		jTextFieldPobResDefSaneamAuto.setText("");
        	}
            if (elemento.getPoblacionEstacionalDefSaneamAuto() != null){
            	jTextFieldPobEstDefSaneamAuto.setText(elemento.getPoblacionEstacionalDefSaneamAuto().toString());
        	} else{
        		jTextFieldPobEstDefSaneamAuto.setText("");
        	}
            if (elemento.getTmBasura() != null){
            	jTextFieldTmBasura.setText(elemento.getTmBasura().toString());
        	} else{
        		jTextFieldTmBasura.setText("");
        	}
            if (elemento.getContenedores() != null){
            	jTextFieldContenedores.setText(elemento.getContenedores().toString());
        	} else{
        		jTextFieldContenedores.setText("");
        	}
            if (elemento.getViviendasSinBasura() != null){
            	jTextFieldVivSinBasura.setText(elemento.getViviendasSinBasura().toString());
        	} else{
        		jTextFieldVivSinBasura.setText("");
        	}
            if (elemento.getPoblacionResidenteSinBasura() != null){
            	jTextFieldPobResSinBasura.setText(elemento.getPoblacionResidenteSinBasura().toString());
        	} else{
        		jTextFieldPobResSinBasura.setText("");
        	}
            if (elemento.getPoblacionEstacionalSinBasura() != null){
            	jTextFieldPobEstSinBasura.setText(elemento.getPoblacionEstacionalSinBasura().toString());
        	} else{
        		jTextFieldPobEstSinBasura.setText("");
        	}
            if (elemento.getPlantillaLimpieza() != null){
            	jTextFieldPlanLimpieza.setText(elemento.getPlantillaLimpieza().toString());
        	} else{
        		jTextFieldPlanLimpieza.setText("");
        	}
            if (elemento.getPuntosLuz() != null){
            	jTextFieldPtosLuz.setText(elemento.getPuntosLuz().toString());
        	} else{
        		jTextFieldPtosLuz.setText("");
        	}
            if (elemento.getViviendasSinAlumbrado() != null){
            	jTextFieldVivSinAlumbrado.setText(elemento.getViviendasSinAlumbrado().toString());
        	} else{
        		jTextFieldVivSinAlumbrado.setText("");
        	}
            if (elemento.getLongDeficitariaAlumbrado() != null){
            	jTextFieldLongDefAlumbrado.setText(elemento.getLongDeficitariaAlumbrado().toString());
        	} else{
        		jTextFieldLongDefAlumbrado.setText("");
        	}
            if (elemento.getVivendasAbastecimientoAuto() != null){
            	jTextFieldVivAbastAuto.setText(elemento.getVivendasAbastecimientoAuto().toString());
        	} else{
        		jTextFieldVivAbastAuto.setText("");
        	}
            if (elemento.getFuentesControladas() != null){
            	jTextFieldFuentesCtrl.setText(elemento.getFuentesControladas().toString());
        	} else{
        		jTextFieldFuentesCtrl.setText("");
        	}
            if (elemento.getPoblacionResidenteSaneamAuto() != null){
            	jTextFieldPobResSaneamAuto.setText(elemento.getPoblacionResidenteSaneamAuto().toString());
        	} else{
        		jTextFieldPobResSaneamAuto.setText("");
        	} 
            
            if (elemento.getFecha() != null && elemento.getFecha().equals( new java.util.Date()) ){
            	jTextFieldFechaRev.setText(elemento.getFecha().toString());
        	} else{
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = new java.util.Date();
                String datetime = dateFormat.format(date);
                
        		jTextFieldFechaRev.setText(datetime);
        	} 
            if (elemento.getEstado() != null){
            	jComboBoxEstadoRev.setSelectedPatron(elemento.getEstado().toString());
        	}
        	else{
        		jComboBoxEstadoRev.setSelectedIndex(0);
        	} 
        }
    }
    
    
    public void loadData(){
    	
    	Object object = AppContext.getApplicationContext().getBlackboard().get("diseminados_panel");    	
    	if (object != null && object instanceof DiseminadosEIEL){    		
    		DiseminadosEIEL elemento = (DiseminadosEIEL)object;
        	/* Campos Clave */
            if (elemento.getCodINEProvincia()!=null){
            	jComboBoxProvincia.setSelectedItem(elemento.getCodINEProvincia());
            } else{
            	jComboBoxProvincia.setSelectedIndex(0);
            }
            if (elemento.getCodINEMunicipio() != null){            	
            	jComboBoxMunicipio.setSelectedItem(elemento.getCodINEMunicipio());
            } else{
            	jComboBoxMunicipio.setSelectedIndex(0);
            }
            /* Otros Campos */
            if (elemento.getPadron() != null){
            	jTextFieldPadron.setText(elemento.getPadron().toString());
        	} else{
        		jTextFieldPadron.setText("");
        	}
            if (elemento.getPoblacionEstacional() != null){
            	jTextFieldPobEst.setText(elemento.getPoblacionEstacional().toString());
        	} else{
        		jTextFieldPobEst.setText("");
        	}
            if (elemento.getViviendasTotales() != null){
            	jTextFieldVivTot.setText(elemento.getViviendasTotales().toString());
        	} else{
        		jTextFieldVivTot.setText("");
        	}
            if (elemento.getPlazasHoteleras() != null){
            	jTextFieldHotel.setText(elemento.getPlazasHoteleras().toString());
        	} else{
        		jTextFieldHotel.setText("");
        	}
            if (elemento.getPlazasCasasRurales() != null){
            	jTextFieldCasaRural.setText(elemento.getPlazasCasasRurales().toString());
        	} else{
        		jTextFieldCasaRural.setText("");
        	}
            if (elemento.getLongitudAbastecimiento() != null){
            	jTextFieldLongAbast.setText(elemento.getLongitudAbastecimiento().toString());
        	} else{
        		jTextFieldLongAbast.setText("");
        	}
            if (elemento.getViviendasConAbastecimiento() != null){
            	jTextFieldVivConAbast.setText(elemento.getViviendasConAbastecimiento().toString());
        	} else{
        		jTextFieldVivConAbast.setText("");
        	}
            if (elemento.getViviendasSinAbastecimiento() != null){
            	jTextFieldVivSinAbast.setText(elemento.getViviendasSinAbastecimiento().toString());
        	} else{
        		jTextFieldVivSinAbast.setText("");
        	}
            if (elemento.getConsumoInvierno() != null){
            	jTextFieldConsInvierno.setText(elemento.getConsumoInvierno().toString());
        	} else{
        		jTextFieldConsInvierno.setText("");
        	}
            if (elemento.getConsumoVerano() != null){
            	jTextFieldConsVerano.setText(elemento.getConsumoVerano().toString());
        	} else{
        		jTextFieldConsVerano.setText("");
        	}
            if (elemento.getViviendasExcesoPresion() != null){
            	jTextFieldVivExcPresion.setText(elemento.getViviendasExcesoPresion().toString());
        	} else{
        		jTextFieldVivExcPresion.setText("");
        	}
            if (elemento.getViviendasDefectoPresion() != null){
            	jTextFieldVivDefPresion.setText(elemento.getViviendasDefectoPresion().toString());
        	} else{
        		jTextFieldVivDefPresion.setText("");
        	}
            if (elemento.getLongDeficitariaAbast() != null){
            	jTextFieldLongDefAbast.setText(elemento.getLongDeficitariaAbast().toString());
        	} else{
        		jTextFieldLongDefAbast.setText("");
        	}
            if (elemento.getViviendasDeficitAbast() != null){
            	jTextFieldVivDefAbast.setText(elemento.getViviendasDeficitAbast().toString());
        	} else{
        		jTextFieldVivDefAbast.setText("");
        	}
            if (elemento.getPoblacionResidenteDefAbast() != null){
            	jTextFieldPobResDefAbast.setText(elemento.getPoblacionResidenteDefAbast().toString());
        	} else{
        		jTextFieldPobResDefAbast.setText("");
        	}
            if (elemento.getPoblacionEstacionalDefAbast() != null){
            	jTextFieldPobEstDefAbast.setText(elemento.getPoblacionEstacionalDefAbast().toString());
        	} else{
        		jTextFieldPobEstDefAbast.setText("");
        	}
            if (elemento.getPoblacionResidenteAbastAuto() != null){
            	jTextFieldPobResAbastAuto.setText(elemento.getPoblacionResidenteAbastAuto().toString());
        	} else{
        		jTextFieldPobResAbastAuto.setText("");
        	}
            if (elemento.getPoblacionEstacionalAbastAuto() != null){
            	jTextFieldPobEstAbastAuto.setText(elemento.getPoblacionEstacionalAbastAuto().toString());
        	} else{
        		jTextFieldPobEstAbastAuto.setText("");
        	}
            if (elemento.getViviendasDefAbastAuto() != null){
            	jTextFieldVivDefAbastAuto.setText(elemento.getViviendasDefAbastAuto().toString());
        	} else{
        		jTextFieldVivDefAbastAuto.setText("");
        	}
            if (elemento.getPoblacionResidenteDefAbastAuto() != null){
            	jTextFieldPobResDefAbastAuto.setText(elemento.getPoblacionResidenteDefAbastAuto().toString());
        	} else{
        		jTextFieldPobResDefAbastAuto.setText("");
        	}
            if (elemento.getPoblacionEstacionalDefAbastAuto() != null){
            	jTextFieldPobEstDefAbastAuto.setText(elemento.getPoblacionEstacionalDefAbastAuto().toString());
        	} else{
        		jTextFieldPobEstDefAbastAuto.setText("");
        	}
            if (elemento.getFuentesNoControladas() != null){
            	jTextFieldFuentesNo.setText(elemento.getFuentesNoControladas().toString());
        	} else{
        		jTextFieldFuentesNo.setText("");
        	}
            if (elemento.getLongitudSaneamiento() != null){
            	jTextFieldLongSaneam.setText(elemento.getLongitudSaneamiento().toString());
        	} else{
        		jTextFieldLongSaneam.setText("");
        	}
            if (elemento.getViviendasConSaneamiento() != null){
            	jTextFieldVivConSaneam.setText(elemento.getViviendasConSaneamiento().toString());
        	} else{
        		jTextFieldVivConSaneam.setText("");
        	}
            if (elemento.getViviendasSinSaneamiento() != null){
            	jTextFieldVivSinSaneam.setText(elemento.getViviendasSinSaneamiento().toString());
        	} else{
        		jTextFieldVivSinSaneam.setText("");
        	}
            if (elemento.getLongDeficitariaSaneam() != null){
            	jTextFieldLongDefSaneam.setText(elemento.getLongDeficitariaSaneam().toString());
        	} else{
        		jTextFieldLongDefSaneam.setText("");
        	}
            if (elemento.getViviendasDefSaneam() != null){
            	jTextFieldVivDefSaneam.setText(elemento.getViviendasDefSaneam().toString());
        	} else{
        		jTextFieldVivDefSaneam.setText("");
        	}
            if (elemento.getPoblacionResidenteDefSaneam() != null){
            	jTextFieldPobResDefSaneam.setText(elemento.getPoblacionResidenteDefSaneam().toString());
        	} else{
        		jTextFieldPobResDefSaneam.setText("");
        	}
            if (elemento.getPoblacionEstacionalDefSaneam() != null){
            	jTextFieldPobEstDefSaneam.setText(elemento.getPoblacionEstacionalDefSaneam().toString());
        	} else{
        		jTextFieldPobEstDefSaneam.setText("");
        	}
            if (elemento.getCaudalDesaguado() != null){
            	jTextFieldCaudalDes.setText(elemento.getCaudalDesaguado().toString());
        	} else{
        		jTextFieldCaudalDes.setText("");
        	}
            if (elemento.getCaudalTratado() != null){
            	jTextFieldCaudalTrat.setText(elemento.getCaudalTratado().toString());
        	} else{
        		jTextFieldCaudalTrat.setText("");
        	}
            if (elemento.getViviendasSaneamientoAuto() != null){
            	jTextFieldVivSaneamAuto.setText(elemento.getViviendasSaneamientoAuto().toString());
        	} else{
        		jTextFieldVivSaneamAuto.setText("");
        	}
            if (elemento.getPoblacionEstacionalSaneamAuto() != null){
            	jTextFieldPobEstSaneamAuto.setText(elemento.getPoblacionEstacionalSaneamAuto().toString());
        	} else{
        		jTextFieldPobEstSaneamAuto.setText("");
        	}
            if (elemento.getViviendasDeficitSaneamAuto() != null){
            	jTextFieldVivDefSaneamAuto.setText(elemento.getViviendasDeficitSaneamAuto().toString());
        	} else{
        		jTextFieldVivDefSaneamAuto.setText("");
        	}
            if (elemento.getPoblacionResidenteDefSaneamAuto() != null){
            	jTextFieldPobResDefSaneamAuto.setText(elemento.getPoblacionResidenteDefSaneamAuto().toString());
        	} else{
        		jTextFieldPobResDefSaneamAuto.setText("");
        	}
            if (elemento.getPoblacionEstacionalDefSaneamAuto() != null){
            	jTextFieldPobEstDefSaneamAuto.setText(elemento.getPoblacionEstacionalDefSaneamAuto().toString());
        	} else{
        		jTextFieldPobEstDefSaneamAuto.setText("");
        	}
            if (elemento.getTmBasura() != null){
            	jTextFieldTmBasura.setText(elemento.getTmBasura().toString());
        	} else{
        		jTextFieldTmBasura.setText("");
        	}
            if (elemento.getContenedores() != null){
            	jTextFieldContenedores.setText(elemento.getContenedores().toString());
        	} else{
        		jTextFieldContenedores.setText("");
        	}
            if (elemento.getViviendasSinBasura() != null){
            	jTextFieldVivSinBasura.setText(elemento.getViviendasSinBasura().toString());
        	} else{
        		jTextFieldVivSinBasura.setText("");
        	}
            if (elemento.getPoblacionResidenteSinBasura() != null){
            	jTextFieldPobResSinBasura.setText(elemento.getPoblacionResidenteSinBasura().toString());
        	} else{
        		jTextFieldPobResSinBasura.setText("");
        	}
            if (elemento.getPoblacionEstacionalSinBasura() != null){
            	jTextFieldPobEstSinBasura.setText(elemento.getPoblacionEstacionalSinBasura().toString());
        	} else{
        		jTextFieldPobEstSinBasura.setText("");
        	}
            if (elemento.getPlantillaLimpieza() != null){
            	jTextFieldPlanLimpieza.setText(elemento.getPlantillaLimpieza().toString());
        	} else{
        		jTextFieldPlanLimpieza.setText("");
        	}
            if (elemento.getPuntosLuz() != null){
            	jTextFieldPtosLuz.setText(elemento.getPuntosLuz().toString());
        	} else{
        		jTextFieldPtosLuz.setText("");
        	}
            if (elemento.getViviendasSinAlumbrado() != null){
            	jTextFieldVivSinAlumbrado.setText(elemento.getViviendasSinAlumbrado().toString());
        	} else{
        		jTextFieldVivSinAlumbrado.setText("");
        	}
            if (elemento.getLongDeficitariaAlumbrado() != null){
            	jTextFieldLongDefAlumbrado.setText(elemento.getLongDeficitariaAlumbrado().toString());
        	} else{
        		jTextFieldLongDefAlumbrado.setText("");
        	}
            if (elemento.getVivendasAbastecimientoAuto() != null){
            	jTextFieldVivAbastAuto.setText(elemento.getVivendasAbastecimientoAuto().toString());
        	} else{
        		jTextFieldVivAbastAuto.setText("");
        	}
            if (elemento.getFuentesControladas() != null){
            	jTextFieldFuentesCtrl.setText(elemento.getFuentesControladas().toString());
        	} else{
        		jTextFieldFuentesCtrl.setText("");
        	}
            if (elemento.getPoblacionResidenteSaneamAuto() != null){
            	jTextFieldPobResSaneamAuto.setText(elemento.getPoblacionResidenteSaneamAuto().toString());
        	} else{
        		jTextFieldPobResSaneamAuto.setText("");
        	} 
            
            if (elemento.getFecha() != null && elemento.getFecha().equals( new java.util.Date()) ){
            	jTextFieldFechaRev.setText(elemento.getFecha().toString());
        	} else{
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
                java.util.Date date = new java.util.Date();
                String datetime = dateFormat.format(date);
                
                jTextFieldFechaRev.setText(datetime);        		
        	} 
            if (elemento.getEstado() != null){
            	jComboBoxEstadoRev.setSelectedPatron(elemento.getEstado().toString());
        	}
        	else{
        		jComboBoxEstadoRev.setSelectedIndex(0);
        	} 
        }
    }
    
    
    public DiseminadosEIEL getDiseminados (DiseminadosEIEL elemento){
        if (okPressed){
            if(elemento==null){
            	elemento = new DiseminadosEIEL();
            }
            /* Claves: COMBOBOX */
    		elemento.setCodINEProvincia(((Provincia) jComboBoxProvincia
					.getSelectedItem()).getIdProvincia());
			elemento.setCodINEMunicipio(((Municipio) jComboBoxMunicipio
					.getSelectedItem()).getIdIne());
			/* JTEXT - Integer y FLOAT */
            if (jTextFieldPadron.getText()!=null && !jTextFieldPadron.getText().equals("")){
            	elemento.setPadron(new Integer(jTextFieldPadron.getText()));
            } else if (jTextFieldPadron.getText().equals("")){
            	elemento.setPadron(new Integer(0));
            }
            if (jTextFieldPobEst.getText()!=null && !jTextFieldPobEst.getText().equals("")){
            	elemento.setPoblacionEstacional(new Integer(jTextFieldPobEst.getText()));
            } else if (jTextFieldPobEst.getText().equals("")){
            	elemento.setPoblacionEstacional(new Integer(0));
            }
            if (jTextFieldVivTot.getText()!=null && !jTextFieldVivTot.getText().equals("")){
            	elemento.setViviendasTotales(new Integer(jTextFieldVivTot.getText()));
            } else if (jTextFieldVivTot.getText().equals("")){
            	elemento.setViviendasTotales(new Integer(0));
            }
            if (jTextFieldHotel.getText()!=null && !jTextFieldHotel.getText().equals("")){
            	elemento.setPlazasHoteleras(new Integer(jTextFieldHotel.getText()));
            } else if (jTextFieldHotel.getText().equals("")){
            	elemento.setPlazasHoteleras(new Integer(0));
            }
            if (jTextFieldCasaRural.getText()!=null && !jTextFieldCasaRural.getText().equals("")){
            	elemento.setPlazasCasasRurales(new Integer(jTextFieldCasaRural.getText()));
            } else if (jTextFieldCasaRural.getText().equals("")){
            	elemento.setPlazasCasasRurales(new Integer(0));
            }
            if (jTextFieldLongAbast.getText()!=null && !jTextFieldLongAbast.getText().equals("")){
            	elemento.setLongitudAbastecimiento(new Integer(jTextFieldLongAbast.getText()));
            } else if (jTextFieldLongAbast.getText().equals("")){
            	elemento.setLongitudAbastecimiento(new Integer(0));
            }
            if (jTextFieldVivConAbast.getText()!=null && !jTextFieldVivConAbast.getText().equals("")){
            	elemento.setViviendasConAbastecimiento(new Integer(jTextFieldVivConAbast.getText()));
            } else if (jTextFieldVivConAbast.getText().equals("")){
            	elemento.setViviendasConAbastecimiento(new Integer(0));
            }
            if (jTextFieldVivSinAbast.getText()!=null && !jTextFieldVivSinAbast.getText().equals("")){
            	elemento.setViviendasSinAbastecimiento(new Integer(jTextFieldVivSinAbast.getText()));
            } else if (jTextFieldVivSinAbast.getText().equals("")){
            	elemento.setViviendasSinAbastecimiento(new Integer(0));
            }
            if (jTextFieldConsInvierno.getText()!=null && !jTextFieldConsInvierno.getText().equals("")){
            	elemento.setConsumoInvierno(new Integer(jTextFieldConsInvierno.getText()));
            } else if (jTextFieldConsInvierno.getText().equals("")){
            	elemento.setConsumoInvierno(new Integer(0));
            }
            if (jTextFieldConsVerano.getText()!=null && !jTextFieldConsVerano.getText().equals("")){
            	elemento.setConsumoVerano(new Integer(jTextFieldConsVerano.getText()));
            } else if (jTextFieldConsVerano.getText().equals("")){
            	elemento.setConsumoVerano(new Integer(0));
            }
            if (jTextFieldVivExcPresion.getText()!=null && !jTextFieldVivExcPresion.getText().equals("")){
            	elemento.setViviendasExcesoPresion(new Integer(jTextFieldVivExcPresion.getText()));
            } else if (jTextFieldVivExcPresion.getText().equals("")){
            	elemento.setViviendasExcesoPresion(new Integer(0));
            }
            if (jTextFieldVivDefPresion.getText()!=null && !jTextFieldVivDefPresion.getText().equals("")){
            	elemento.setViviendasDefectoPresion(new Integer(jTextFieldVivDefPresion.getText()));
            } else if (jTextFieldVivDefPresion.getText().equals("")){
            	elemento.setViviendasDefectoPresion(new Integer(0));
            }
            if (jTextFieldLongDefAbast.getText()!=null && !jTextFieldLongDefAbast.getText().equals("")){
            	elemento.setLongDeficitariaAbast(new Integer(jTextFieldLongDefAbast.getText()));
            } else if (jTextFieldLongDefAbast.getText().equals("")){
            	elemento.setLongDeficitariaAbast(new Integer(0));
            }
            if (jTextFieldVivDefAbast.getText()!=null && !jTextFieldVivDefAbast.getText().equals("")){
            	elemento.setViviendasDeficitAbast(new Integer(jTextFieldVivDefAbast.getText()));
            } else if (jTextFieldVivDefAbast.getText().equals("")){
            	elemento.setViviendasDeficitAbast(new Integer(0));
            }
            if (jTextFieldPobResDefAbast.getText()!=null && !jTextFieldPobResDefAbast.getText().equals("")){
            	elemento.setPoblacionResidenteDefAbast(new Integer(jTextFieldPobResDefAbast.getText()));
            } else if (jTextFieldPobResDefAbast.getText().equals("")){
            	elemento.setPoblacionResidenteDefAbast(new Integer(0));
            }
            if (jTextFieldPobEstDefAbast.getText()!=null && !jTextFieldPobEstDefAbast.getText().equals("")){
            	elemento.setPoblacionEstacionalDefAbast(new Integer(jTextFieldPobEstDefAbast.getText()));
            } else if (jTextFieldPobEstDefAbast.getText().equals("")){
            	elemento.setPoblacionEstacionalDefAbast(new Integer(0));
            }
            if (jTextFieldPobResAbastAuto.getText()!=null && !jTextFieldPobResAbastAuto.getText().equals("")){
            	elemento.setPoblacionResidenteAbastAuto(new Integer(jTextFieldPobResAbastAuto.getText()));
            } else if (jTextFieldPobResAbastAuto.getText().equals("")){
            	elemento.setPoblacionResidenteAbastAuto(new Integer(0));
            }
            if (jTextFieldPobEstAbastAuto.getText()!=null && !jTextFieldPobEstAbastAuto.getText().equals("")){
            	elemento.setPoblacionEstacionalAbastAuto(new Integer(jTextFieldPobEstAbastAuto.getText()));
            } else if (jTextFieldPobEstAbastAuto.getText().equals("")){
            	elemento.setPoblacionEstacionalAbastAuto(new Integer(0));
            }
            if (jTextFieldVivDefAbastAuto.getText()!=null && !jTextFieldVivDefAbastAuto.getText().equals("")){
            	elemento.setViviendasDefAbastAuto(new Integer(jTextFieldVivDefAbastAuto.getText()));
            } else if (jTextFieldVivDefAbastAuto.getText().equals("")){
            	elemento.setViviendasDefAbastAuto(new Integer(0));
            }
            if (jTextFieldPobResDefAbastAuto.getText()!=null && !jTextFieldPobResDefAbastAuto.getText().equals("")){
            	elemento.setPoblacionResidenteDefAbastAuto(new Integer(jTextFieldPobResDefAbastAuto.getText()));
            } else if (jTextFieldPobResDefAbastAuto.getText().equals("")){
            	elemento.setPoblacionResidenteDefAbastAuto(new Integer(0));
            }
            if (jTextFieldPobEstDefAbastAuto.getText()!=null && !jTextFieldPobEstDefAbastAuto.getText().equals("")){
            	elemento.setPoblacionEstacionalDefAbastAuto(new Integer(jTextFieldPobEstDefAbastAuto.getText()));
            } else if (jTextFieldPobEstDefAbastAuto.getText().equals("")){
            	elemento.setPoblacionEstacionalDefAbastAuto(new Integer(0));
            }
            if (jTextFieldFuentesNo.getText()!=null && !jTextFieldFuentesNo.getText().equals("")){
            	elemento.setFuentesNoControladas(new Integer(jTextFieldFuentesNo.getText()));
            } else if (jTextFieldFuentesNo.getText().equals("")){
            	elemento.setFuentesNoControladas(new Integer(0));
            }
            if (jTextFieldLongSaneam.getText()!=null && !jTextFieldLongSaneam.getText().equals("")){
            	elemento.setLongitudSaneamiento(new Integer(jTextFieldLongSaneam.getText()));
            } else if (jTextFieldLongSaneam.getText().equals("")){
            	elemento.setLongitudSaneamiento(new Integer(0));
            }
            if (jTextFieldVivConSaneam.getText()!=null && !jTextFieldVivConSaneam.getText().equals("")){
            	elemento.setViviendasConSaneamiento(new Integer(jTextFieldVivConSaneam.getText()));
            } else if (jTextFieldVivConSaneam.getText().equals("")){
            	elemento.setViviendasConSaneamiento(new Integer(0));
            }
            if (jTextFieldVivSinSaneam.getText()!=null && !jTextFieldVivSinSaneam.getText().equals("")){
            	elemento.setViviendasSinSaneamiento(new Integer(jTextFieldVivSinSaneam.getText()));
            } else if (jTextFieldVivSinSaneam.getText().equals("")){
            	elemento.setViviendasSinSaneamiento(new Integer(0));
            }
            if (jTextFieldLongDefSaneam.getText()!=null && !jTextFieldLongDefSaneam.getText().equals("")){
            	elemento.setLongDeficitariaSaneam(new Integer(jTextFieldLongDefSaneam.getText()));
            } else if (jTextFieldLongDefSaneam.getText().equals("")){
            	elemento.setLongDeficitariaSaneam(new Integer(0));
            }
            if (jTextFieldVivDefSaneam.getText()!=null && !jTextFieldVivDefSaneam.getText().equals("")){
            	elemento.setViviendasDefSaneam(new Integer(jTextFieldVivDefSaneam.getText()));
            } else if (jTextFieldVivDefSaneam.getText().equals("")){
            	elemento.setViviendasDefSaneam(new Integer(0));
            }
            if (jTextFieldPobResDefSaneam.getText()!=null && !jTextFieldPobResDefSaneam.getText().equals("")){
            	elemento.setPoblacionResidenteDefSaneam(new Integer(jTextFieldPobResDefSaneam.getText()));
            } else if (jTextFieldPobResDefSaneam.getText().equals("")){
            	elemento.setPoblacionResidenteDefSaneam(new Integer(0));
            }
            if (jTextFieldPobEstDefSaneam.getText()!=null && !jTextFieldPobEstDefSaneam.getText().equals("")){
            	elemento.setPoblacionEstacionalDefSaneam(new Integer(jTextFieldPobEstDefSaneam.getText()));
            } else if (jTextFieldPobEstDefSaneam.getText().equals("")){
            	elemento.setPoblacionEstacionalDefSaneam(new Integer(0));
            }
            if (jTextFieldCaudalDes.getText()!=null && !jTextFieldCaudalDes.getText().equals("")){
            	elemento.setCaudalDesaguado(new Integer(jTextFieldCaudalDes.getText()));
            } else if (jTextFieldCaudalDes.getText().equals("")){
            	elemento.setCaudalDesaguado(new Integer(0));
            }
            if (jTextFieldCaudalTrat.getText()!=null && !jTextFieldCaudalTrat.getText().equals("")){
            	elemento.setCaudalTratado(new Integer(jTextFieldCaudalTrat.getText()));
            } else if (jTextFieldCaudalTrat.getText().equals("")){
            	elemento.setCaudalTratado(new Integer(0));
            }
            if (jTextFieldVivSaneamAuto.getText()!=null && !jTextFieldVivSaneamAuto.getText().equals("")){
            	elemento.setViviendasSaneamientoAuto(new Integer(jTextFieldVivSaneamAuto.getText()));
            } else if (jTextFieldVivSaneamAuto.getText().equals("")){
            	elemento.setViviendasSaneamientoAuto(new Integer(0));
            }
            if (jTextFieldPobEstSaneamAuto.getText()!=null && !jTextFieldPobEstSaneamAuto.getText().equals("")){
            	elemento.setPoblacionEstacionalSaneamAuto(new Integer(jTextFieldPobEstSaneamAuto.getText()));
            } else if (jTextFieldPobEstSaneamAuto.getText().equals("")){
            	elemento.setPoblacionEstacionalSaneamAuto(new Integer(0));
            }
            if (jTextFieldVivDefSaneamAuto.getText()!=null && !jTextFieldVivDefSaneamAuto.getText().equals("")){
            	elemento.setViviendasDeficitSaneamAuto(new Integer(jTextFieldVivDefSaneamAuto.getText()));
            } else if (jTextFieldVivDefSaneamAuto.getText().equals("")){
            	elemento.setViviendasDeficitSaneamAuto(new Integer(0));
            }
            if (jTextFieldPobResDefSaneamAuto.getText()!=null && !jTextFieldPobResDefSaneamAuto.getText().equals("")){
            	elemento.setPoblacionResidenteDefSaneamAuto(new Integer(jTextFieldPobResDefSaneamAuto.getText()));
            } else if (jTextFieldPobResDefSaneamAuto.getText().equals("")){
            	elemento.setPoblacionResidenteDefSaneamAuto(new Integer(0));
            }
            if (jTextFieldPobEstDefSaneamAuto.getText()!=null && !jTextFieldPobEstDefSaneamAuto.getText().equals("")){
            	elemento.setPoblacionEstacionalDefSaneamAuto(new Integer(jTextFieldPobEstDefSaneamAuto.getText()));
            } else if (jTextFieldPobEstDefSaneamAuto.getText().equals("")){
            	elemento.setPoblacionEstacionalDefSaneamAuto(new Integer(0));
            }
            if (jTextFieldTmBasura.getText()!=null && !jTextFieldTmBasura.getText().equals("")){
            	elemento.setTmBasura(new Float(jTextFieldTmBasura.getText()));
            } else if (jTextFieldTmBasura.getText().equals("")){
            	elemento.setTmBasura(new Float(0));
            }
            if (jTextFieldContenedores.getText()!=null && !jTextFieldContenedores.getText().equals("")){
            	elemento.setContenedores(new Integer(jTextFieldContenedores.getText()));
            } else if (jTextFieldContenedores.getText().equals("")){
            	elemento.setContenedores(new Integer(0));
            }
            if (jTextFieldVivSinBasura.getText()!=null && !jTextFieldVivSinBasura.getText().equals("")){
            	elemento.setViviendasSinBasura(new Integer(jTextFieldVivSinBasura.getText()));
            } else if (jTextFieldVivSinBasura.getText().equals("")){
            	elemento.setViviendasSinBasura(new Integer(0));
            }
            if (jTextFieldPobResSinBasura.getText()!=null && !jTextFieldPobResSinBasura.getText().equals("")){
            	elemento.setPoblacionResidenteSinBasura(new Integer(jTextFieldPobResSinBasura.getText()));
            } else if (jTextFieldPobResSinBasura.getText().equals("")){
            	elemento.setPoblacionResidenteSinBasura(new Integer(0));
            }
            if (jTextFieldPobEstSinBasura.getText()!=null && !jTextFieldPobEstSinBasura.getText().equals("")){
            	elemento.setPoblacionEstacionalSinBasura(new Integer(jTextFieldPobEstSinBasura.getText()));
            } else if (jTextFieldPobEstSinBasura.getText().equals("")){
            	elemento.setPoblacionEstacionalSinBasura(new Integer(0));
            }
            if (jTextFieldPlanLimpieza.getText()!=null && !jTextFieldPlanLimpieza.getText().equals("")){
            	elemento.setPlantillaLimpieza(new Integer(jTextFieldPlanLimpieza.getText()));
            } else if (jTextFieldPlanLimpieza.getText().equals("")){
            	elemento.setPlantillaLimpieza(new Integer(0));
            }
            if (jTextFieldPtosLuz.getText()!=null && !jTextFieldPtosLuz.getText().equals("")){
            	elemento.setPuntosLuz(new Integer(jTextFieldPtosLuz.getText()));
            } else if (jTextFieldPtosLuz.getText().equals("")){
            	elemento.setPuntosLuz(new Integer(0));
            }
            if (jTextFieldVivSinAlumbrado.getText()!=null && !jTextFieldVivSinAlumbrado.getText().equals("")){
            	elemento.setViviendasSinAlumbrado(new Integer(jTextFieldVivSinAlumbrado.getText()));
            } else if (jTextFieldVivSinAlumbrado.getText().equals("")){
            	elemento.setViviendasSinAlumbrado(new Integer(0));
            }
            if (jTextFieldLongDefAlumbrado.getText()!=null && !jTextFieldLongDefAlumbrado.getText().equals("")){
            	elemento.setLongDeficitariaAlumbrado(new Integer(jTextFieldLongDefAlumbrado.getText()));
            } else if (jTextFieldLongDefAlumbrado.getText().equals("")){
            	elemento.setLongDeficitariaAlumbrado(new Integer(0));
            }
            if (jTextFieldVivAbastAuto.getText()!=null && !jTextFieldVivAbastAuto.getText().equals("")){
            	elemento.setVivendasAbastecimientoAuto(new Integer(jTextFieldVivAbastAuto.getText()));
            } else if (jTextFieldVivAbastAuto.getText().equals("")){
            	elemento.setVivendasAbastecimientoAuto(new Integer(0));
            }
            if (jTextFieldFuentesCtrl.getText()!=null && !jTextFieldFuentesCtrl.getText().equals("")){
            	elemento.setFuentesControladas(new Integer(jTextFieldFuentesCtrl.getText()));
            } else if (jTextFieldFuentesCtrl.getText().equals("")){
            	elemento.setFuentesControladas(new Integer(0));
            }
            if (jTextFieldPobResSaneamAuto.getText()!=null && !jTextFieldPobResSaneamAuto.getText().equals("")){
            	elemento.setPoblacionResidenteSaneamAuto(new Integer(jTextFieldPobResSaneamAuto.getText()));
            } else if (jTextFieldPobResSaneamAuto.getText().equals("")){
            	elemento.setPoblacionResidenteSaneamAuto(new Integer(0));
            }
            if (jTextFieldFechaRev.getText()!=null && !jTextFieldFechaRev.getText().equals("")){
            	String fechas=jTextFieldFechaRev.getText();
            	String anio=fechas.substring(0,4);
            	String mes=fechas.substring(5,7);
            	String dia=fechas.substring(8,10);            	
            	java.util.Date fecha = new java.util.Date(Integer.parseInt(anio)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia));            	
            	elemento.setFecha(new Date(fecha.getYear(),fecha.getMonth(), fecha.getDate())); 
            }  
            else{
            	elemento.setFecha(null);
            }
            if (jComboBoxEstadoRev.getSelectedPatron()!=null)
            	elemento.setEstado(Integer.parseInt(jComboBoxEstadoRev.getSelectedPatron()));
        }
        return elemento;
    }
    
    private void initialize() {
		Locale loc = I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle(
				"com.geopista.app.eiel.language.LocalGISEIELi18n", loc, this
						.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("LocalGISEIEL", bundle);
		
		this.setName(I18N.get("LocalGISEIEL","localgiseiel.diseminados.panel.title"));
		
		this.setLayout(new GridBagLayout());
		this.setSize(DiseminadosDialog.DIM_X, DiseminadosDialog.DIM_Y);
		this.add(getJPanelDatosIdentificacion(), new
				 GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
				 GridBagConstraints.CENTER , GridBagConstraints.BOTH,
				 new Insets(5, 5, 5, 5), 0, 1));
		if (jScroll == null) {
			jScroll = new JScrollPane(getJPanelDatosInformacion());
			jScroll = new JScrollPane();
			jScroll.setViewportView(jPanelInformacion);
			jScroll.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.info"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
			this.add(jScroll, new GridBagConstraints(0, 1, 1, 1, 0.1, 1,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(5, 5, 5, 5), 0, 200));

			EdicionOperations oper = new EdicionOperations();

			// Inicializa los desplegables
			if (Identificadores.get("ListaProvincias") == null) {
				ArrayList lst = oper.obtenerProvincias();
				Identificadores.put("ListaProvincias", lst);
				EdicionUtils.cargarLista(getJComboBoxProvincia(),oper.obtenerProvinciasConNombre());
				Provincia p = new Provincia();
				p.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
				p.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
				getJComboBoxProvincia().setSelectedItem(p);
			} else {
				EdicionUtils.cargarLista(getJComboBoxProvincia(),
						(ArrayList) Identificadores.get("ListaProvincias"));
				 EdicionUtils.cargarLista(getJComboBoxProvincia(),
				 oper.obtenerProvinciasConNombre());
				 Provincia p = new Provincia();
				p.setIdProvincia(ConstantesLocalGISEIEL.idProvincia);
				p.setNombreOficial(ConstantesLocalGISEIEL.Provincia);
				getJComboBoxProvincia().setSelectedItem(p);
			}
			loadData();
		}
		
		this.add(getJPanelDatosRevision(), new
				 GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
				 GridBagConstraints.CENTER , GridBagConstraints.BOTH,
				 new Insets(5, 5, 5, 5), 0, 1));
		
		
	}
    
/* Metodos que devuelven CAMPOS CLAVE */
    
    private Component getJPanelDatosRevision() {
		
    	 if (jPanelRevision == null)
         {   
         	jPanelRevision = new JPanel(new GridBagLayout());
         	jPanelRevision.setBorder(BorderFactory.createTitledBorder
                     (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.revision"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
         	
             jLabelFechaRev  = new JLabel("", JLabel.CENTER); 
             jLabelFechaRev.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.fecha")); 
             
             jLabelEstadoRev = new JLabel("", JLabel.CENTER); 
             jLabelEstadoRev.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.estado")); 
                         
             jPanelRevision.add(jLabelFechaRev,
                     new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                             GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                             new Insets(5, 5, 5, 5), 0, 0));
             
             jPanelRevision.add(getJTextFieldFechaRev(), 
                     new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                             GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                             new Insets(5, 5, 5, 5), 0, 0));
             
             jPanelRevision.add(jLabelEstadoRev, 
                     new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                             GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                             new Insets(5, 5, 5, 5), 0, 0));
             
             
             jPanelRevision.add(getJComboBoxEstado(), 
                     new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                             GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                             new Insets(5, 5, 5, 5), 130, 0));
             
         }
         return jPanelRevision;
	}

	public JComboBox getJComboBoxProvincia(){
		if (jComboBoxProvincia == null) {
			EdicionOperations oper = new EdicionOperations();
			ArrayList<Provincia> listaProvincias = oper.obtenerProvinciasConNombre();
			jComboBoxProvincia = new JComboBox(listaProvincias.toArray());
			jComboBoxProvincia
					.setSelectedIndex(this
							.provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));
			jComboBoxProvincia.setRenderer(new UbicacionListCellRenderer());
			jComboBoxProvincia.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getJComboBoxMunicipio() != null) {
						if (jComboBoxProvincia.getSelectedIndex() == 0) {
							jComboBoxMunicipio.removeAllItems();
							Municipio municipio = new Municipio();
							municipio.setIdIne("");
							municipio.setNombreOficial("");
							jComboBoxMunicipio.addItem(municipio);
						} else {
							EdicionOperations oper = new EdicionOperations();
							jComboBoxProvincia
									.setSelectedIndex(provinciaIndexSeleccionar(ConstantesLocalGISEIEL.idProvincia));

							if (jComboBoxProvincia.getSelectedItem() != null) {
								EdicionUtils.cargarLista(
										getJComboBoxMunicipio(),
										oper.obtenerTodosMunicipios(((Provincia) jComboBoxProvincia
												.getSelectedItem())
												.getIdProvincia()));
								jComboBoxMunicipio
										.setSelectedIndex(municipioIndexSeleccionar(ConstantesLocalGISEIEL.idMunicipio));
							}
						}
					}

				}
			});

		}

		return jComboBoxProvincia;
    }
    
	public JComboBox getJComboBoxMunicipio(){
    	if (jComboBoxMunicipio == null) {
			jComboBoxMunicipio = new JComboBox();
			jComboBoxMunicipio.setRenderer(new UbicacionListCellRenderer());
			jComboBoxMunicipio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (jComboBoxMunicipio.getSelectedIndex() != 0) {
						MunicipioEIEL municipio = new MunicipioEIEL();
						if (jComboBoxProvincia.getSelectedItem() != null) {
							municipio
									.setCodProvincia(((Provincia) jComboBoxProvincia
											.getSelectedItem())
											.getIdProvincia());
						}
						if (jComboBoxMunicipio.getSelectedItem() != null) {
							municipio
									.setCodMunicipio(((Municipio) jComboBoxMunicipio
											.getSelectedItem()).getIdIne());
						}

					}
				}
			});
		}
		return jComboBoxMunicipio;
    }

    
    /* Metodos que devuelven el resto de CAMPOS */
    
    private JTextField getjTextFieldPadron(){
    	if (jTextFieldPadron == null){
    		jTextFieldPadron = new TextField(6);
    		jTextFieldPadron.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPadron, 6, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPadron;
    }
    
    private JTextField getjTextFieldPobEst(){
    	if (jTextFieldPobEst == null){
    		jTextFieldPobEst = new TextField(6);
    		jTextFieldPobEst.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobEst, 6, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobEst;
    }
    
    private JTextField getjTextFieldVivTot(){
    	if (jTextFieldVivTot == null){
    		jTextFieldVivTot = new TextField(5);
    		jTextFieldVivTot.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivTot, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivTot;
    }
    
    private JTextField getjTextFieldHotel(){
    	if (jTextFieldHotel == null){
    		jTextFieldHotel = new TextField(5);
    		jTextFieldHotel.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldHotel, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldHotel;
    }
    
    private JTextField getjTextFieldCasaRural(){
    	if (jTextFieldCasaRural == null){
    		jTextFieldCasaRural = new TextField(5);
    		jTextFieldCasaRural.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldCasaRural, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldCasaRural;
    }
    
    private JTextField getjTextFieldLongAbast(){
    	if (jTextFieldLongAbast == null){
    		jTextFieldLongAbast = new TextField(7);
    		jTextFieldLongAbast.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLongAbast, 7, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldLongAbast;
    }
    
    private JTextField getjTextFieldVivConAbast(){
    	if (jTextFieldVivConAbast == null){
    		jTextFieldVivConAbast = new TextField(5);
    		jTextFieldVivConAbast.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivConAbast, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivConAbast;
    }
    
    private JTextField getjTextFieldVivSinAbast(){
    	if (jTextFieldVivSinAbast == null){
    		jTextFieldVivSinAbast = new TextField(5);
    		jTextFieldVivSinAbast.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivSinAbast, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivSinAbast;
    }
    
    private JTextField getjTextFieldConsInvierno(){
    	if (jTextFieldConsInvierno == null){
    		jTextFieldConsInvierno = new TextField(5);
    		jTextFieldConsInvierno.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldConsInvierno, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldConsInvierno;
    }
    
    private JTextField getjTextFieldConsVerano(){
    	if (jTextFieldConsVerano == null){
    		jTextFieldConsVerano = new TextField(5);
    		jTextFieldConsVerano.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldConsVerano, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldConsVerano;
    }
    
    private JTextField getjTextFieldVivExcPresion(){
    	if (jTextFieldVivExcPresion == null){
    		jTextFieldVivExcPresion = new TextField(5);
    		jTextFieldVivExcPresion.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivExcPresion, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivExcPresion;
    }
    
    private JTextField getjTextFieldVivDefPresion(){
    	if (jTextFieldVivDefPresion == null){
    		jTextFieldVivDefPresion = new TextField(5);
    		jTextFieldVivDefPresion.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivDefPresion, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivDefPresion;
    }
    
    private JTextField getjTextFieldLongDefAbast(){
    	if (jTextFieldLongDefAbast == null){
    		jTextFieldLongDefAbast = new TextField(5);
    		jTextFieldLongDefAbast.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLongDefAbast, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldLongDefAbast;
    }
    
    private JTextField getjTextFieldVivDefAbast(){
    	if (jTextFieldVivDefAbast == null){
    		jTextFieldVivDefAbast = new TextField(5);
    		jTextFieldVivDefAbast.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivDefAbast, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivDefAbast;
    }
    
    private JTextField getjTextFieldPobResDefAbast(){
    	if (jTextFieldPobResDefAbast == null){
    		jTextFieldPobResDefAbast = new TextField(5);
    		jTextFieldPobResDefAbast.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobResDefAbast, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobResDefAbast;
    }
    
    private JTextField getjTextFieldPobEstDefAbast(){
    	if (jTextFieldPobEstDefAbast == null){
    		jTextFieldPobEstDefAbast = new TextField(5);
    		jTextFieldPobEstDefAbast.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobEstDefAbast, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobEstDefAbast;
    }
    
    private JTextField getjTextFieldPobResAbastAuto(){
    	if (jTextFieldPobResAbastAuto == null){
    		jTextFieldPobResAbastAuto = new TextField(5);
    		jTextFieldPobResAbastAuto.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobResAbastAuto, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobResAbastAuto;
    }
    
    private JTextField getjTextFieldPobEstAbastAuto(){
    	if (jTextFieldPobEstAbastAuto == null){
    		jTextFieldPobEstAbastAuto = new TextField(5);
    		jTextFieldPobEstAbastAuto.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobEstAbastAuto, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobEstAbastAuto;
    }
    
    private JTextField getjTextFieldVivDefAbastAuto(){
    	if (jTextFieldVivDefAbastAuto == null){
    		jTextFieldVivDefAbastAuto = new TextField(5);
    		jTextFieldVivDefAbastAuto.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivDefAbastAuto, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivDefAbastAuto;
    }
    
    private JTextField getjTextFieldPobResDefAbastAuto(){
    	if (jTextFieldPobResDefAbastAuto == null){
    		jTextFieldPobResDefAbastAuto = new TextField(5);
    		jTextFieldPobResDefAbastAuto.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobResDefAbastAuto, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobResDefAbastAuto;
    }
    
    private JTextField getjTextFieldPobEstDefAbastAuto(){
    	if (jTextFieldPobEstDefAbastAuto == null){
    		jTextFieldPobEstDefAbastAuto = new TextField(5);
    		jTextFieldPobEstDefAbastAuto.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobEstDefAbastAuto, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobEstDefAbastAuto;
    }
    
    private JTextField getjTextFieldFuentesNo(){
    	if (jTextFieldFuentesNo == null){
    		jTextFieldFuentesNo = new TextField(5);
    		jTextFieldFuentesNo.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldFuentesNo, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldFuentesNo;
    }
    
    private JTextField getjTextFieldLongSaneam(){
    	if (jTextFieldLongSaneam == null){
    		jTextFieldLongSaneam = new TextField(5);
    		jTextFieldLongSaneam.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLongSaneam, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldLongSaneam;
    }
    
    private JTextField getjTextFieldVivConSaneam(){
    	if (jTextFieldVivConSaneam == null){
    		jTextFieldVivConSaneam = new TextField(5);
    		jTextFieldVivConSaneam.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivConSaneam, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivConSaneam;
    }
    
    private JTextField getjTextFieldVivSinSaneam(){
    	if (jTextFieldVivSinSaneam == null){
    		jTextFieldVivSinSaneam = new TextField(5);
    		jTextFieldVivSinSaneam.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivSinSaneam, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivSinSaneam;
    }
    
    private JTextField getjTextFieldLongDefSaneam(){
    	if (jTextFieldLongDefSaneam == null){
    		jTextFieldLongDefSaneam = new TextField(5);
    		jTextFieldLongDefSaneam.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLongDefSaneam, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldLongDefSaneam;
    }
    
    private JTextField getjTextFieldVivDefSaneam(){
    	if (jTextFieldVivDefSaneam == null){
    		jTextFieldVivDefSaneam = new TextField(5);
    		jTextFieldVivDefSaneam.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivDefSaneam, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivDefSaneam;
    }
    
    private JTextField getjTextFieldPobResDefSaneam(){
    	if (jTextFieldPobResDefSaneam == null){
    		jTextFieldPobResDefSaneam = new TextField(5);
    		jTextFieldPobResDefSaneam.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobResDefSaneam, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobResDefSaneam;
    }
    
    private JTextField getjTextFieldPobEstDefSaneam(){
    	if (jTextFieldPobEstDefSaneam == null){
    		jTextFieldPobEstDefSaneam = new TextField(5);
    		jTextFieldPobEstDefSaneam.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobEstDefSaneam, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobEstDefSaneam;
    }
    
    private JTextField getjTextFieldCaudalDes(){
    	if (jTextFieldCaudalDes == null){
    		jTextFieldCaudalDes = new TextField(8);
    		jTextFieldCaudalDes.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldCaudalDes, 8, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldCaudalDes;
    }
    
    private JTextField getjTextFieldCaudalTrat(){
    	if (jTextFieldCaudalTrat == null){
    		jTextFieldCaudalTrat = new TextField(8);
    		jTextFieldCaudalTrat.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldCaudalTrat,8, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldCaudalTrat;
    }
    
    private JTextField getjTextFieldVivSaneamAuto(){
    	if (jTextFieldVivSaneamAuto == null){
    		jTextFieldVivSaneamAuto = new TextField(5);
    		jTextFieldVivSaneamAuto.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivSaneamAuto, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivSaneamAuto;
    }
    
    private JTextField getjTextFieldPobEstSaneamAuto(){
    	if (jTextFieldPobEstSaneamAuto == null){
    		jTextFieldPobEstSaneamAuto = new TextField(5);
    		jTextFieldPobEstSaneamAuto.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobEstSaneamAuto, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobEstSaneamAuto;
    }
    
    private JTextField getjTextFieldVivDefSaneamAuto(){
    	if (jTextFieldVivDefSaneamAuto == null){
    		jTextFieldVivDefSaneamAuto = new TextField(5);
    		jTextFieldVivDefSaneamAuto.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivDefSaneamAuto, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivDefSaneamAuto;
    }
    
    private JTextField getjTextFieldPobResDefSaneamAuto(){
    	if (jTextFieldPobResDefSaneamAuto == null){
    		jTextFieldPobResDefSaneamAuto = new TextField(5);
    		jTextFieldPobResDefSaneamAuto.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobResDefSaneamAuto, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobResDefSaneamAuto;
    }
    
    private JTextField getjTextFieldPobEstDefSaneamAuto(){
    	if (jTextFieldPobEstDefSaneamAuto == null){
    		jTextFieldPobEstDefSaneamAuto = new TextField(5);
    		jTextFieldPobEstDefSaneamAuto.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobEstDefSaneamAuto, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobEstDefSaneamAuto;
    }
    
    private JTextField getjTextFieldTmBasura(){
    	if (jTextFieldTmBasura == null){
    		jTextFieldTmBasura = new TextField(5);
    		jTextFieldTmBasura.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldTmBasura, 12, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldTmBasura;
    }
    
    private JTextField getjTextFieldContenedores(){
    	if (jTextFieldContenedores == null){
    		jTextFieldContenedores = new TextField(5);
    		jTextFieldContenedores.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldContenedores, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldContenedores;
    }

    private JTextField getjTextFieldVivSinBasura(){
    	if (jTextFieldVivSinBasura == null){
    		jTextFieldVivSinBasura = new TextField(5);
    		jTextFieldVivSinBasura.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivSinBasura, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivSinBasura;
    }
    
    private JTextField getjTextFieldPobResSinBasura(){
    	if (jTextFieldPobResSinBasura == null){
    		jTextFieldPobResSinBasura = new TextField(5);
    		jTextFieldPobResSinBasura.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobResSinBasura, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobResSinBasura;
    }
    
    private JTextField getjTextFieldPobEstSinBasura(){
    	if (jTextFieldPobEstSinBasura == null){
    		jTextFieldPobEstSinBasura = new TextField(5);
    		jTextFieldPobEstSinBasura.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobEstSinBasura, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobEstSinBasura;
    }
    
    private JTextField getjTextFieldPlanLimpieza(){
    	if (jTextFieldPlanLimpieza == null){
    		jTextFieldPlanLimpieza = new TextField(5);
    		jTextFieldPlanLimpieza.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPlanLimpieza, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPlanLimpieza;
    }
    
    private JTextField getjTextFieldPtosLuz(){
    	if (jTextFieldPtosLuz == null){
    		jTextFieldPtosLuz = new TextField(5);
    		jTextFieldPtosLuz.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPtosLuz, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPtosLuz;
    }
    
    private JTextField getjTextFieldVivSinAlumbrado(){
    	if (jTextFieldVivSinAlumbrado == null){
    		jTextFieldVivSinAlumbrado = new TextField(5);
    		jTextFieldVivSinAlumbrado.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivSinAlumbrado, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivSinAlumbrado;
    }
    
    private JTextField getjTextFieldLongDefAlumbrado(){
    	if (jTextFieldLongDefAlumbrado == null){
    		jTextFieldLongDefAlumbrado = new TextField(8);
    		jTextFieldLongDefAlumbrado.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldLongDefAlumbrado, 8, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldLongDefAlumbrado;
    }
    
    private JTextField getjTextFieldVivAbastAuto(){
    	if (jTextFieldVivAbastAuto == null){
    		jTextFieldVivAbastAuto = new TextField(5);
    		jTextFieldVivAbastAuto.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldVivAbastAuto, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldVivAbastAuto;
    }
    
    private JTextField getjTextFieldFuentesCtrl(){
    	if (jTextFieldFuentesCtrl == null){
    		jTextFieldFuentesCtrl = new TextField(5);
    		jTextFieldFuentesCtrl.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldFuentesCtrl, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldFuentesCtrl;
    }
    
    private JTextField getJTextFieldFechaRev(){
    	if (jTextFieldFechaRev == null){
    		jTextFieldFechaRev  = new TextField();
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            String datetime = dateFormat.format(date);            
            jTextFieldFechaRev.setText(datetime);
    	}
    	return jTextFieldFechaRev;
    }
    
    private ComboBoxEstructuras getJComboBoxEstado(){ 
        if (jComboBoxEstadoRev == null){
            Estructuras.cargarEstructura("eiel_Estado de revisión");
            jComboBoxEstadoRev = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        
            jComboBoxEstadoRev.setPreferredSize(new Dimension(100, 20));
        }
        return jComboBoxEstadoRev;        
    }
    
    private JTextField getjTextFieldPobResSaneamAuto(){
    	if (jTextFieldPobResSaneamAuto == null){
    		jTextFieldPobResSaneamAuto = new TextField(5);
    		jTextFieldPobResSaneamAuto.addCaretListener(new CaretListener(){
    			public void caretUpdate(CaretEvent evt){
    				EdicionUtils.chequeaLongYCharCampoEdit(jTextFieldPobResSaneamAuto, 5, aplicacion.getMainFrame());
    			}
    		});
    	}
    	return jTextFieldPobResSaneamAuto;
    }
    
    public DiseminadosPanel(GridBagLayout layout){
        super(layout);
        initialize();
    }
       
    public String validateInput(){
        return null; 
    }
    
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getJPanelDatosIdentificacion(){
        if (jPanelIdentificacion == null){   
        	jPanelIdentificacion = new JPanel(new GridBagLayout());
            jPanelIdentificacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("LocalGISEIEL", "localgiseiel.panels.identity"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            /* Definicion de LABEL'S */
            jLabelCodProv = new JLabel("", JLabel.CENTER); 
            jLabelCodProv.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codprov"))); 
            jLabelCodMunic = new JLabel("", JLabel.CENTER); 
            jLabelCodMunic.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.codmunic"))); 
            /* Agregamos las Labels al JPANELIDENTIFICATION */
            jPanelIdentificacion.add(jLabelCodProv, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            jPanelIdentificacion.add(getJComboBoxProvincia(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelIdentificacion.add(jLabelCodMunic, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));            
            jPanelIdentificacion.add(getJComboBoxMunicipio(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            
            for (int i=0; i < jPanelIdentificacion.getComponentCount(); i++){
            	jPanelIdentificacion.getComponent(i).setEnabled(false);
            }
            
        }
        return jPanelIdentificacion;
    }
    
    /**
     * This method initializes jPanelDatosInformacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosInformacion(){
        if (jPanelInformacion == null){   
        	jPanelInformacion  = new JPanel(new GridBagLayout());
        	jPanelInformacion.setBorder(BorderFactory.createEmptyBorder());

        	/* Definicion de LABEL'S */
        	jLabelPadron  = new JLabel("", JLabel.CENTER);
            jLabelPadron.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.padron_dis"));
            jLabelPobEst  = new JLabel("", JLabel.CENTER);
            jLabelPobEst.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.pob_estaci"));
            jLabelVivTot  = new JLabel("", JLabel.CENTER);
            jLabelVivTot.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.viv_total"));
            jLabelHotel  = new JLabel("", JLabel.CENTER);
            jLabelHotel.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.hoteles"));
            jLabelCasaRural  = new JLabel("", JLabel.CENTER);
            jLabelCasaRural.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.casas_rural"));
            jLabelLongAbast  = new JLabel("", JLabel.CENTER);
            jLabelLongAbast.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.long_ms"));
            jLabelVivConAbast  = new JLabel("", JLabel.CENTER);
            jLabelVivConAbast.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aag_v_cone"));
            jLabelVivSinAbast  = new JLabel("", JLabel.CENTER);
            jLabelVivSinAbast.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aag_v_ncon"));
            jLabelConsInvierno  = new JLabel("", JLabel.CENTER);
            jLabelConsInvierno.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.consumo_inv"));
            jLabelConsVerano  = new JLabel("", JLabel.CENTER);
            jLabelConsVerano.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.consumo_verano"));
            jLabelVivExcPresion  = new JLabel("", JLabel.CENTER);
            jLabelVivExcPresion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aag_v_expr"));
            jLabelVivDefPresion  = new JLabel("", JLabel.CENTER);
            jLabelVivDefPresion.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aag_v_depr"));
            jLabelLongDefAbast  = new JLabel("", JLabel.CENTER);
            jLabelLongDefAbast.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aag_l_defi"));
            jLabelVivDefAbast  = new JLabel("", JLabel.CENTER);
            jLabelVivDefAbast.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aag_v_defi"));
            jLabelPobResDefAbast  = new JLabel("", JLabel.CENTER);
            jLabelPobResDefAbast.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aag_pr_def"));
            jLabelPobEstDefAbast  = new JLabel("", JLabel.CENTER);
            jLabelPobEstDefAbast.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aag_pe_def"));
            jLabelPobResAbastAuto  = new JLabel("", JLabel.CENTER);
            jLabelPobResAbastAuto.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aau_pob_re"));
            jLabelPobEstAbastAuto  = new JLabel("", JLabel.CENTER);
            jLabelPobEstAbastAuto.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aau_pob_es"));
            jLabelVivDefAbastAuto  = new JLabel("", JLabel.CENTER);
            jLabelVivDefAbastAuto.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aau_def_vi"));
            jLabelPobResDefAbastAuto  = new JLabel("", JLabel.CENTER);
            jLabelPobResDefAbastAuto.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aau_def_re"));
            jLabelPobEstDefAbastAuto  = new JLabel("", JLabel.CENTER);
            jLabelPobEstDefAbastAuto.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aau_def_es"));
            jLabelFuentesNo  = new JLabel("", JLabel.CENTER);
            jLabelFuentesNo.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aau_fencon"));
            jLabelLongSaneam  = new JLabel("", JLabel.CENTER);
            jLabelLongSaneam.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.longi_ramal"));
            jLabelVivConSaneam  = new JLabel("", JLabel.CENTER);
            jLabelVivConSaneam.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.syd_v_cone"));
            jLabelVivSinSaneam  = new JLabel("", JLabel.CENTER);
            jLabelVivSinSaneam.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.syd_v_ncon"));
            jLabelLongDefSaneam  = new JLabel("", JLabel.CENTER);
            jLabelLongDefSaneam.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.syd_l_defi"));
            jLabelVivDefSaneam  = new JLabel("", JLabel.CENTER);
            jLabelVivDefSaneam.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.syd_v_defi"));
            jLabelPobResDefSaneam  = new JLabel("", JLabel.CENTER);
            jLabelPobResDefSaneam.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.syd_pr_def"));
            jLabelPobEstDefSaneam  = new JLabel("", JLabel.CENTER);
            jLabelPobEstDefSaneam.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.syd_pe_def"));
            jLabelCaudalDes  = new JLabel("", JLabel.CENTER);
            jLabelCaudalDes.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.caudTot"));
            jLabelCaudalTrat  = new JLabel("", JLabel.CENTER);
            jLabelCaudalTrat.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.caudTrt"));
            jLabelVivSaneamAuto  = new JLabel("", JLabel.CENTER);
            jLabelVivSaneamAuto.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.sau_vivien"));
            jLabelPobEstSaneamAuto  = new JLabel("", JLabel.CENTER);
            jLabelPobEstSaneamAuto.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.sau_pob_es"));
            jLabelVivDefSaneamAuto  = new JLabel("", JLabel.CENTER);
            jLabelVivDefSaneamAuto.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.sau_vi_def"));
            jLabelPobResDefSaneamAuto  = new JLabel("", JLabel.CENTER);
            jLabelPobResDefSaneamAuto.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.sau_pob_re_def"));
            jLabelPobEstDefSaneamAuto  = new JLabel("", JLabel.CENTER);
            jLabelPobEstDefSaneamAuto.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.sau_pob_es_def"));
            jLabelTmBasura  = new JLabel("", JLabel.CENTER);
            jLabelTmBasura.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.produ_basu"));
            jLabelContenedores  = new JLabel("", JLabel.CENTER);
            jLabelContenedores.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.contenedores"));
            jLabelVivSinBasura  = new JLabel("", JLabel.CENTER);
            jLabelVivSinBasura.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.rba_v_sser"));
            jLabelPobResSinBasura  = new JLabel("", JLabel.CENTER);
            jLabelPobResSinBasura.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.rba_pr_sse"));
            jLabelPobEstSinBasura  = new JLabel("", JLabel.CENTER);
            jLabelPobEstSinBasura.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.rba_pe_sse"));
            jLabelPlanLimpieza  = new JLabel("", JLabel.CENTER);
            jLabelPlanLimpieza.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.rba_plalim"));
            jLabelPtosLuz  = new JLabel("", JLabel.CENTER);
            jLabelPtosLuz.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.puntos_luz"));
            jLabelVivSinAlumbrado  = new JLabel("", JLabel.CENTER);
            jLabelVivSinAlumbrado.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.alu_v_sin"));
            jLabelLongDefAlumbrado  = new JLabel("", JLabel.CENTER);
            jLabelLongDefAlumbrado.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.alu_l_sin"));
            jLabelVivAbastAuto  = new JLabel("", JLabel.CENTER);
            jLabelVivAbastAuto.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aau_vivien"));
            jLabelFuentesCtrl  = new JLabel("", JLabel.CENTER);
            jLabelFuentesCtrl.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.aau_fecont"));
            jLabelPobResSaneamAuto  = new JLabel("", JLabel.CENTER);
            jLabelPobResSaneamAuto.setText(I18N.get("LocalGISEIEL", "localgiseiel.panels.label.sau_pob_re"));

            /* Agregamos los JLabels y los JTextFieldPanels al JPANELINFORMATION */
            jPanelInformacion.add(jLabelPadron,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPadron(), 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobEst,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobEst(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivTot,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivTot(), 
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelHotel,
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldHotel(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelCasaRural,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldCasaRural(), 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelLongAbast,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldLongAbast(), 
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivConAbast,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivConAbast(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivSinAbast,
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivSinAbast(), 
                    new GridBagConstraints(3, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelConsInvierno,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldConsInvierno(), 
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelConsVerano,
                    new GridBagConstraints(2, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldConsVerano(), 
                    new GridBagConstraints(3, 4, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivExcPresion,
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivExcPresion(), 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivDefPresion,
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivDefPresion(), 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelLongDefAbast,
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldLongDefAbast(), 
                    new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivDefAbast,
                    new GridBagConstraints(2, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivDefAbast(), 
                    new GridBagConstraints(3, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobResDefAbast,
                    new GridBagConstraints(0, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobResDefAbast(), 
                    new GridBagConstraints(1, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobEstDefAbast,
                    new GridBagConstraints(2, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobEstDefAbast(), 
                    new GridBagConstraints(3, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobResAbastAuto,
                    new GridBagConstraints(0, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobResAbastAuto(), 
                    new GridBagConstraints(1, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobEstAbastAuto,
                    new GridBagConstraints(2, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobEstAbastAuto(), 
                    new GridBagConstraints(3, 8, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivDefAbastAuto,
                    new GridBagConstraints(0, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivDefAbastAuto(), 
                    new GridBagConstraints(1, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobResDefAbastAuto,
                    new GridBagConstraints(2, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobResDefAbastAuto(), 
                    new GridBagConstraints(3, 9, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobEstDefAbastAuto,
                    new GridBagConstraints(0, 10, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobEstDefAbastAuto(), 
                    new GridBagConstraints(1, 10, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelFuentesNo,
                    new GridBagConstraints(2, 10, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldFuentesNo(), 
                    new GridBagConstraints(3, 10, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelLongSaneam,
                    new GridBagConstraints(0, 11, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldLongSaneam(), 
                    new GridBagConstraints(1, 11, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivConSaneam,
                    new GridBagConstraints(2, 11, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivConSaneam(), 
                    new GridBagConstraints(3, 11, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivSinSaneam,
                    new GridBagConstraints(0, 12, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivSinSaneam(), 
                    new GridBagConstraints(1, 12, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelLongDefSaneam,
                    new GridBagConstraints(2, 12, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldLongDefSaneam(), 
                    new GridBagConstraints(3, 12, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivDefSaneam,
                    new GridBagConstraints(0, 13, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivDefSaneam(), 
                    new GridBagConstraints(1, 13, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobResDefSaneam,
                    new GridBagConstraints(2, 13, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobResDefSaneam(), 
                    new GridBagConstraints(3, 13, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobResSaneamAuto,
                    new GridBagConstraints(0, 14, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobResSaneamAuto(), 
                    new GridBagConstraints(1, 14, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobEstDefSaneam,
                    new GridBagConstraints(2, 14, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobEstDefSaneam(), 
                    new GridBagConstraints(3, 14, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelCaudalDes,
                    new GridBagConstraints(0, 15, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldCaudalDes(), 
                    new GridBagConstraints(1, 15, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelCaudalTrat,
                    new GridBagConstraints(2, 15, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldCaudalTrat(), 
                    new GridBagConstraints(3, 15, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivSaneamAuto,
                    new GridBagConstraints(0, 16, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivSaneamAuto(), 
                    new GridBagConstraints(1, 16, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobEstSaneamAuto,
                    new GridBagConstraints(2, 16, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobEstSaneamAuto(), 
                    new GridBagConstraints(3, 16, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivDefSaneamAuto,
                    new GridBagConstraints(0, 17, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivDefSaneamAuto(), 
                    new GridBagConstraints(1, 17, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobResDefSaneamAuto,
                    new GridBagConstraints(2, 17, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobResDefSaneamAuto(), 
                    new GridBagConstraints(3, 17, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobEstDefSaneamAuto,
                    new GridBagConstraints(0, 18, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobEstDefSaneamAuto(), 
                    new GridBagConstraints(1, 18, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelTmBasura,
                    new GridBagConstraints(2, 18, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldTmBasura(), 
                    new GridBagConstraints(3, 18, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelContenedores,
                    new GridBagConstraints(0, 19, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldContenedores(), 
                    new GridBagConstraints(1, 19, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivSinBasura,
                    new GridBagConstraints(2, 19, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivSinBasura(), 
                    new GridBagConstraints(3, 19, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobResSinBasura,
                    new GridBagConstraints(0, 20, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobResSinBasura(), 
                    new GridBagConstraints(1, 20, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPobEstSinBasura,
                    new GridBagConstraints(2, 20, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPobEstSinBasura(), 
                    new GridBagConstraints(3, 20, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPlanLimpieza,
                    new GridBagConstraints(0, 21, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPlanLimpieza(), 
                    new GridBagConstraints(1, 21, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelPtosLuz,
                    new GridBagConstraints(2, 21, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldPtosLuz(), 
                    new GridBagConstraints(3, 21, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivSinAlumbrado,
                    new GridBagConstraints(0, 22, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivSinAlumbrado(), 
                    new GridBagConstraints(1, 22, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelLongDefAlumbrado,
                    new GridBagConstraints(2, 22, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldLongDefAlumbrado(), 
                    new GridBagConstraints(3, 22, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelVivAbastAuto,
                    new GridBagConstraints(0, 23, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldVivAbastAuto(), 
                    new GridBagConstraints(1, 23, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
            jPanelInformacion.add(jLabelFuentesCtrl,
                    new GridBagConstraints(2, 23, 1, 1, 0.1, 0.1,
                            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 5, 0));
            jPanelInformacion.add(getjTextFieldFuentesCtrl(), 
                    new GridBagConstraints(3, 23, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));

        }
        return jPanelInformacion;
    }

    public void okPressed(){
        okPressed = true;
    }
    
    public boolean getOkPressed(){
        return okPressed;
    }

    public boolean datosMinimosYCorrectos(){
        return (jComboBoxProvincia!=null && jComboBoxProvincia.getSelectedItem()!=null && jComboBoxProvincia.getSelectedIndex()>0) &&
        (jComboBoxMunicipio!=null && jComboBoxMunicipio.getSelectedItem()!=null && jComboBoxMunicipio.getSelectedIndex()>0);
    }

	public void enter() {
		loadData();
		loadDataIdentificacion();
	}

	public void exit() {
		
	}
	
	
	public void loadDataIdentificacion(){

		Object obj = AppContext.getApplicationContext().getBlackboard().get("featureDialog");
		if (obj != null && obj instanceof FeatureDialog){
			FeatureDialog featureDialog = (FeatureDialog) obj;
			Feature feature = featureDialog.get_fieldPanel().getModifiedFeature();
										
				GeopistaSchema esquema = (GeopistaSchema)feature.getSchema();
				feature.getAttribute(esquema.getAttributeByColumn("id"));
	        	
	        	String codprov = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("codprov"))!=null){
	        		codprov=(feature.getAttribute(esquema.getAttributeByColumn("codprov"))).toString();
	        	}
	        	
	        	String codmunic = null;
	        	if (feature.getAttribute(esquema.getAttributeByColumn("codmunic"))!=null){
	        		codmunic=(feature.getAttribute(esquema.getAttributeByColumn("codmunic"))).toString();
	        	}
	        	
	        	EdicionOperations operations = new EdicionOperations();
	        	loadData(operations.getPanelDiseminadosEIEL(codprov, codmunic));
	        	
	        	loadDataIdentificacion(codprov, codmunic);       	
			}
		}
	
	

	public void loadDataIdentificacion(String codprov, String codmunic) {
		
		//Datos identificacion
        
		if (codprov != null) {
			// jComboBoxProvincia.setSelectedItem(codprov);
			jComboBoxProvincia
					.setSelectedIndex(provinciaIndexSeleccionar(codprov));
		} else {
			jComboBoxProvincia.setSelectedIndex(-1);
		}

		if (codmunic != null) {
			jComboBoxMunicipio
					.setSelectedIndex(municipioIndexSeleccionar(codmunic));
		} else {
			jComboBoxMunicipio.setSelectedIndex(-1);
		}
		
	}
	
	public int provinciaIndexSeleccionar(String provinciaId) {
		for (int i = 0; i < jComboBoxProvincia.getItemCount(); i++) {
			if ((!jComboBoxProvincia.getItemAt(i).equals(""))
					&& (jComboBoxProvincia.getItemAt(i) instanceof Provincia)
					&& ((Provincia) jComboBoxProvincia.getItemAt(i))
							.getIdProvincia().equals(provinciaId)) {
				return i;
			}
		}

		return -1;
	}

	public int municipioIndexSeleccionar(String municipioId) {
		if (!municipioId.equals("")) {
			for (int i = 0; i < jComboBoxMunicipio.getItemCount(); i++) {
				try {
					if (((Municipio) jComboBoxMunicipio.getItemAt(i))
							.getIdIne().equals(municipioId.substring(2, 5))) {
						// jComboBoxMunicipioNucleo.setEnabled(false);
						return i;
					}
				} catch (StringIndexOutOfBoundsException e) {
					if (((Municipio) jComboBoxMunicipio.getItemAt(i))
							.getIdIne().equals(municipioId)) {
						// jComboBoxMunicipio.setEnabled(false);
						return i;
					}
				}
			}
		}

		return -1;
	}
    
}
