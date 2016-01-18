package com.geopista.app.image;

import com.geopista.util.FeatureExtendedForm;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.DocumentDialog;
import com.geopista.ui.dialogs.ImageDialog;
import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.vividsolutions.jump.util.Blackboard;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 08-may-2006
 * Time: 11:44:55
 */

public class ImageExtendedForm implements FeatureExtendedForm
{
      private InfoImagePanel infoImage;
	private AppContext aplicacion;
	private FeatureDialogHome fd;
     public ImageExtendedForm()
  {


  }

    public void setApplicationContext(ApplicationContext context)
  {

  }

  public void flush()
  {
  }

  public boolean checkPanels()
  {
    return true;
  }


	  public AbstractValidator getValidator()
  {
    return null;
  }
		public InfoImagePanel getInfoImagePanel(){
			return infoImage;
		}
    public void disableAll() {
        if (infoImage!=null) infoImage.setEnabled(false);
   }

    public void initialize(FeatureDialogHome fd)
    {
    	this.fd=fd;
        if (!AppContext.getApplicationContext().isOnline()) return;
		      
        Object[] features;
        if (fd instanceof ImageDialog)
            features=(((ImageDialog)fd).getFeatures()).toArray();
        else{
            features= new Object[1];
            features[0]= (GeopistaFeature)fd.getFeature();
            
            if (features[0] == null){
				return;
			}
			else if (features[0] instanceof GeopistaFeature){
				GeopistaFeature geopistaFeature = (GeopistaFeature)features[0];
				if (((GeopistaFeature)features[0]).getLayer()!= null && 
				((GeopistaFeature)features[0]).getLayer() instanceof GeopistaLayer ){
					GeopistaLayer layer = (GeopistaLayer)((GeopistaFeature)features[0]).getLayer();
					if (layer.isLocal() || layer.isExtracted()){
						return;
					}
				}
				else{
					return;
				}
			}
             try{ //Si se esta insertando no dejamos meter documentos.
               new Long(((GeopistaFeature)fd.getFeature()).getSystemId());
            }catch(Exception e){return;}//Si es una inserción no mostramos la pantalla
        }
        AppContext app =(AppContext) AppContext.getApplicationContext();
        this.aplicacion = (AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
        Identificadores.put ("feature", features);

        infoImage = new InfoImagePanel();
        infoImage.setPreferredSize(new Dimension(600,450));
        infoImage.setMinimumSize(new Dimension(600,450));
        infoImage.setFd(fd);
        fd.addPanel(infoImage);
        addAyudaOnline();
    }
  	/**
  	 * Ayuda Online
  	 * 
  	 */
  	private void addAyudaOnline() {
  		JDialog fdDialog;
  		if (fd instanceof ImageDialog){
  			fdDialog = (ImageDialog) fd;
  			
  			fdDialog.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
	  				.put(KeyStroke.getKeyStroke("F1"), "action F1");
	
  			fdDialog.getRootPane().getActionMap().put("action F1", new AbstractAction() {
	  			public void actionPerformed(ActionEvent ae) {
	  	 			String uriRelativa = "/Geocuenca:Georreferenciaci%C3%B3n_de_im%C3%A1genes";
	  				GeopistaBrowser.openURL(aplicacion.getString("ayuda.geopista.web")
	  						+ uriRelativa);
	  			}
	  		});
  		}
  	}
    
    public GeopistaFeature getSelectedFeature()
    {
         if (infoImage==null) return null;
         return infoImage.getSelectedFeature();
    }
  }
