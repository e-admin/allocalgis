package com.geopista.ui.plugin.edicionalfanum;


import com.geopista.app.AppContext;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.autoforms.FeatureFieldPanel;
import com.geopista.ui.dialogs.TextDialog;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;




public class DatosAlfanumericosExtendedForm implements FeatureExtendedForm
{
    //private DatosAlfanumericosDocumentPanel textDocument;
    
    FeatureFieldPanel	fieldPanel= null;
	private AppContext aplicacion;
	private FeatureDialogHome fd;
    public DatosAlfanumericosExtendedForm()
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

        /*textDocument = new DatosAlfanumericosDocumentPanel();
        textDocument.setPreferredSize(new Dimension(600,450));
        textDocument.setMinimumSize(new Dimension(600,450));
        textDocument.setFd(fd);
        fd.addPanel(textDocument);*/
        
        fieldPanel = new FeatureFieldPanel(fd.getFeature());
        
        fieldPanel.buildDialogByGroupingTables();
    }

	@Override
	public void disableAll() {
		// TODO Auto-generated method stub
		
	}
  	
    
    /*public GeopistaFeature getSelectedFeature()
    {
         if (textDocument==null) return null;
         return textDocument.getSelectedFeature();
    }*/
  }
