package com.geopista.app.text;


import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.TextDialog;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;




public class TextExtendedForm implements FeatureExtendedForm
{
    private TextDocumentPanel textDocument;
	private AppContext aplicacion;
	private FeatureDialogHome fd;
    public TextExtendedForm()
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

    public void disableAll() {
        if (textDocument!=null) textDocument.setEnabled(false);

    }

	public TextDocumentPanel getInfoTextPanel(){
		return textDocument;
	}
    public void initialize(FeatureDialogHome fd)
    {
    	this.fd=fd;
        if (!AppContext.getApplicationContext().isOnline()) return;
		        
        //GeopistaSchema esquema = (GeopistaSchema)fd.getFeature().getSchema();
        Object[] features;
        if (fd instanceof TextDialog)
            features=(((TextDialog)fd).getFeatures()).toArray();
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

        textDocument = new TextDocumentPanel();
        textDocument.setPreferredSize(new Dimension(600,450));
        textDocument.setMinimumSize(new Dimension(600,450));
        textDocument.setFd(fd);
        fd.addPanel(textDocument);
        addAyudaOnline();
    }
  	/**
  	 * Ayuda Online
  	 * 
  	 */
  	private void addAyudaOnline() {
  		JDialog fdDialog;
  		if (fd instanceof TextDialog){
  			fdDialog = (TextDialog) fd;
  			
  			fdDialog.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
	  				.put(KeyStroke.getKeyStroke("F1"), "action F1");
	
  			fdDialog.getRootPane().getActionMap().put("action F1", new AbstractAction() {
	  			public void actionPerformed(ActionEvent ae) {
	  	 			String uriRelativa = "/Geocuenca:Georreferenciaci%C3%B3n_de_textos";
	  				GeopistaBrowser.openURL(aplicacion.getString("ayuda.geopista.web")
	  						+ uriRelativa);
	  			}
	  		});
  		}
  	}
    
    public GeopistaFeature getSelectedFeature()
    {
         if (textDocument==null) return null;
         return textDocument.getSelectedFeature();
    }
  }
