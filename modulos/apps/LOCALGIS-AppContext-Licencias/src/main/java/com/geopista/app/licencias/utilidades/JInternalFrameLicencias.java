/**
 * JInternalFrameLicencias.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias.utilidades;

import java.awt.Cursor;
import java.awt.HeadlessException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.geopista.app.licencias.CConstantesLicencias;
import com.geopista.app.licencias.CUtilidadesComponentes;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.vividsolutions.jump.feature.Feature;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 21-jun-2005
 * Time: 20:04:48
 */
public class JInternalFrameLicencias extends JInternalFrame{
    private Logger logger = Logger.getLogger(JInternalFrameLicencias.class);

    protected GeopistaEditor geopistaEditor;
    protected javax.swing.JTable referenciasCatastralesJTable;
    protected DefaultTableModel referenciasCatastralesJTableModel;
    protected JFrame desktop;
    protected javax.swing.JPanel editorMapaJPanel;
    protected ResourceBundle literales;

    protected void loadMapa(int idMapa){
        CUtilidadesComponentes.showGeopistaMap(desktop,editorMapaJPanel, geopistaEditor,idMapa, false);
    }

    protected void loadMapa(int idMapa, String layernamevisible, String layernamenovisible)
    {
        try {
			if (CUtilidadesComponentes.showGeopistaMap(desktop,editorMapaJPanel, geopistaEditor,idMapa, false, layernamevisible, layernamenovisible))
			{
			    GeopistaLayer layer=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer("parcelas");
			    if (layer!=null)
			    {
			        layer.setEditable(true);
			        layer.setActiva(true);
			    }
			} else
			{
			    new JOptionPane("No existe el mapa "+idMapa+" en el sistema. \nContacte con el administrador."
			            , JOptionPane.ERROR_MESSAGE).createDialog(this.desktop, "ERROR").show();
			}
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    protected boolean refreshFeatureSelection() {

		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			geopistaEditor.getSelectionManager().clear();

			GeopistaLayer geopistaLayer = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("parcelas");

            for (int i=0; i<referenciasCatastralesJTable.getModel().getRowCount(); i++){
                String refCatastral= (String) referenciasCatastralesJTable.getValueAt(i,0);
             	Collection collection = CUtilidadesComponentes.searchByAttribute(geopistaLayer, "Referencia catastral", refCatastral);
				Iterator it = collection.iterator();
				if (it.hasNext()) {
					Feature feature = (Feature) it.next();
					geopistaEditor.select(geopistaLayer, feature);
				}
			}

			geopistaEditor.zoomToSelected();

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return true;
		} catch (Exception ex) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			logger.error("Exception: " ,ex);
			return false;
		}

	}
    public boolean refreshFeatureSelection(Vector referenciasCatastrales) {

		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			geopistaEditor.getSelectionManager().clear();
            if (referenciasCatastrales ==null) return true;
			GeopistaLayer geopistaLayer = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("parcelas");
            java.util.List featureList = geopistaLayer.getFeatureCollectionWrapper().getFeatures();
            if (featureList.size()<=0) return true;
            Feature feature = (Feature) featureList.get(0);
            String attributeName = feature.getSchema().getAttributeName(2);
            for (Enumeration e= referenciasCatastrales.elements(); e.hasMoreElements();)
            {
                CReferenciaCatastral ref=(CReferenciaCatastral) e.nextElement();
                Collection collection = CUtilidadesComponentes.searchByAttribute(geopistaLayer, attributeName, ref.getReferenciaCatastral());
				Iterator it = collection.iterator();
				if (it.hasNext()) {
					geopistaEditor.select(geopistaLayer, (Feature) it.next());
				}
            }
			geopistaEditor.zoomToSelected();
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return true;
		} catch (Exception ex) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			logger.error("Exception: " ,ex);
			return false;
		}
	}
    protected void deleteParcelaJButtonActionPerformed()
    {
            int selectedRow = referenciasCatastralesJTable.getSelectedRow();
            if (selectedRow != -1) {
                referenciasCatastralesJTableModel.removeRow(selectedRow);
                refreshFeatureSelection();
            }
    }//GEN-LAST:event_deleteParcelaJButtonActionPerformed

    protected void mapToTableJButtonActionPerformed() {
      	Object[] options = {literales.getString("Licencias.mensaje3"), literales.getString("Licencias.mensaje4")};
	    if (JOptionPane.showOptionDialog(this,
				literales.getString("Licencias.mensaje1"),
				literales.getString("Licencias.mensaje2"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, //don't use a custom Icon
				options, //the titles of buttons
				options[1])!=JOptionPane.OK_OPTION) return;

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
		Collection collection = geopistaEditor.getSelection();
		Iterator it = collection.iterator();
		CConstantesLicencias.referenciasCatastrales.clear();
		while (it.hasNext()) {
			Feature feature = (Feature) it.next();
			if (feature == null) {
				logger.error("feature: " + feature);
				continue;
			}

            CReferenciaCatastral referenciaCatastral = CUtilidadesComponentes.getReferenciaCatastral(feature);
            Object[] features= new Object[1];
            features[0]= feature;
			referenciasCatastralesJTableModel.addRow(new Object[]{referenciaCatastral.getReferenciaCatastral(),
																  referenciaCatastral.getTipoVia(),
																  referenciaCatastral.getNombreVia(),
																  referenciaCatastral.getPrimerNumero(),
																  referenciaCatastral.getPrimeraLetra(),
																  referenciaCatastral.getBloque(),
																  referenciaCatastral.getEscalera(),
																  referenciaCatastral.getPlanta(),
																  referenciaCatastral.getPuerta(),
                                                                  referenciaCatastral.getCPostal(),
                                                                  features});
		}

        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

    protected void nombreViaJButtonActionPerformed() {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            CUtilidadesComponentes.showAddressDialog(desktop,literales);
            try {
                  Enumeration enumerationElement = CConstantesLicencias.referenciasCatastrales.elements();
                  while (enumerationElement.hasMoreElements())
                  {
                        CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) enumerationElement.nextElement();
                        referenciasCatastralesJTableModel.addRow(new Object[]{referenciaCatastral.getReferenciaCatastral(),
                                                                          referenciaCatastral.getTipoVia(),
                                                                          referenciaCatastral.getNombreVia(),
                                                                          referenciaCatastral.getPrimerNumero(),
                                                                          referenciaCatastral.getPrimeraLetra(),
                                                                          referenciaCatastral.getBloque(),
                                                                          referenciaCatastral.getEscalera(),
                                                                          referenciaCatastral.getPlanta(),
                                                                          referenciaCatastral.getPuerta(),
                                                                          referenciaCatastral.getCPostal(), CUtilidadesComponentes.getFeatureSearched(geopistaEditor, referenciaCatastral.getReferenciaCatastral())});
                  }
                  refreshFeatureSelection();
            } catch (Exception ex) {
                logger.error("Exception: " , ex);
            }
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        protected void refCatastralJButtonActionPerformed() {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            CUtilidadesComponentes.showReferenciaCatastralDialog(desktop,literales);
            try {
                   Enumeration enumerationElement = CConstantesLicencias.referenciasCatastrales.elements();
                  while (enumerationElement.hasMoreElements()) {
                    CReferenciaCatastral referenciaCatastral= (CReferenciaCatastral) enumerationElement.nextElement();
                    referenciasCatastralesJTableModel.addRow(new Object[]{referenciaCatastral.getReferenciaCatastral(),
                                                                          referenciaCatastral.getTipoVia(),
                                                                          referenciaCatastral.getNombreVia(),
                                                                          referenciaCatastral.getPrimerNumero(),
                                                                          referenciaCatastral.getPrimeraLetra(),
                                                                          referenciaCatastral.getBloque(),
                                                                          referenciaCatastral.getEscalera(),
                                                                          referenciaCatastral.getPlanta(),
                                                                          referenciaCatastral.getPuerta(),
                                                                          referenciaCatastral.getCPostal(), CUtilidadesComponentes.getFeatureSearched(geopistaEditor, referenciaCatastral.getReferenciaCatastral())});
                }
                refreshFeatureSelection();

            } catch (Exception ex) {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                logger.error("Exception: " , ex);
            }

            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        }//GEN-LAST:event_refCatastralJButtonActionPerformed

        /**
	 * Referencias Catastrales
	 */
	public void cargarReferenciasCatastralesJTable(CSolicitudLicencia solicitud) {
		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			CUtilidadesComponentes.clearTable(referenciasCatastralesJTableModel);
			Vector referenciasCatastrales = solicitud.getReferenciasCatastrales();

			if ((referenciasCatastrales == null) || (referenciasCatastrales.isEmpty())) {
				logger.info("No hay referenciasCatastrales para la licencia.");
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}


			for (int i = 0; i < referenciasCatastrales.size(); i++) {
				CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) referenciasCatastrales.elementAt(i);
               	referenciasCatastralesJTableModel.addRow(new Object[]{referenciaCatastral.getReferenciaCatastral(),
																	  referenciaCatastral.getTipoVia()==null?"":referenciaCatastral.getTipoVia(),
																	  referenciaCatastral.getNombreVia(),
																	  referenciaCatastral.getPrimerNumero(),
																	  referenciaCatastral.getPrimeraLetra(),
																	  referenciaCatastral.getBloque(),
																	  referenciaCatastral.getEscalera(),
																	  referenciaCatastral.getPlanta(),
																	  referenciaCatastral.getPuerta(),
                                                                      referenciaCatastral.getCPostal()});


			}
			refreshFeatureSelection();
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} catch (Exception ex) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			logger.error("Exception: ",ex);
		}

	}

    public void setGeopistaEditor(GeopistaEditor geoeditor){
        this.geopistaEditor= geoeditor;
    }

}
