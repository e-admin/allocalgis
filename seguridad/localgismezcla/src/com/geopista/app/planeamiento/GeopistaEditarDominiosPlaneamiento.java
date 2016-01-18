package com.geopista.app.planeamiento;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.PropertyConfigurator;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.dominios.CDominiosFrame;
import com.geopista.app.administrador.dominios.JPanelDominios;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.administrador.usuarios.CUsuariosFrame;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;


public class GeopistaEditarDominiosPlaneamiento  extends JPanel implements WizardPanel{
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  	//public static String ficheroConfiguracion = "configAdministracion.ini";
    public static final String idApp="Administracion";
    
    private JLabel lblImagen = new JLabel();
    //private puzzled.IniFile fichConfig;
    private CUsuariosFrame usuariosFrame;
    private CDominiosFrame dominiosFrame;
    private ResourceBundle messages=null;
    private JPanel pnlGeneral = new JPanel();
  	public GeopistaEditarDominiosPlaneamiento () {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

    
    }
  

 public void enteredFromLeft(Map dataMap)
  {
    
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
     //System.out.println("saliendo de panel 1"); 
    }

    /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {
      
    }

    public void remove(InputChangedListener listener)
    {
      
    }

    public String getTitle()
    {
      return "";
    }

    public String getID()
    {
      return "1";
    }

    public String getInstructions()
    {
     return "";
    }

    public boolean isInputValid()
    {
      return true;
    }

    private String nextID=null;
    private BorderLayout borderLayout1 = new BorderLayout();
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }


    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {

        return nextID;
    } 
    
      public void setWizardContext(WizardContext wd)
    {
      
    }

  private void jbInit() throws Exception
  {
  	
  	this.setSize(900,1600);
    this.setLayout(null);
    pnlGeneral.setLayout(null);





  
  if(!aplicacion.isLogged())
                  {
                       aplicacion.setProfile("Geopista");
                       aplicacion.login();
                  }
 if(aplicacion.isLogged())
 {
    lblImagen.setBounds(new Rectangle(2,10, 120, 550));
//    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black,1));
    lblImagen.setIcon(IconLoader.icon("planeamiento.png"));
//    pnlGeneral.setBounds(new Rectangle(15, 20, 110, 490));
    configureApp(); 
    Locale currentLocale = new Locale(Constantes.Locale);
   
    ResourceBundle messages = ResourceBundle.getBundle("config.administrador", currentLocale);
    //dominiosFrame=new CDominiosFrame(messages,aplicacion.getMainFrame());
    JPanelDominios a = new JPanelDominios(messages,aplicacion.getMainFrame(),"2");
    a.changeScreenLang(messages);
    a.setBounds(new Rectangle(135, 20, 700, 500));
    a.setBorder(BorderFactory.createTitledBorder(""));
    a.hideLeyenda();
    
    pnlGeneral.setBounds(new Rectangle(2,10, 120, 550));
    pnlGeneral.setBorder(BorderFactory.createTitledBorder(""));
    pnlGeneral.setLayout(null);
    pnlGeneral.add(lblImagen,null);
    this.add(pnlGeneral,null);
    this.add(a,null);
    
  }
  }
    // Variables declaration - do not modify//GEN-BEGIN:variables

	private boolean showAuth() {
    
		CAuthDialog auth = new CAuthDialog(aplicacion.getMainFrame(), true,Constantes.url,
                            GeopistaEditarDominiosPlaneamiento.idApp, Constantes.idMunicipio,
                            messages);

		auth.setBounds(30,60,315,155);
		auth.show();
		return true;
	}

	private boolean configureApp() {

		try {

			//****************************************************************
			//** Inicializamos el log4j
			//*******************************************************
			try{PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.ini", 3000);}catch(Exception e){};
			

			//**************PropertyConfigurator**************************************************
			//** Cargamos la configuracion de config.ini
			//*******************************************************
			/*try{fichConfig = new IniFile(ficheroConfiguracion);}catch(Exception e){fichConfig=null;}
            if (fichConfig!=null)
            {
			    Constantes.url= fichConfig.getValue("GENERAL", "SERVER_URL");
			    Constantes.timeout= new Integer(fichConfig.getValue("GENERAL", "SERVER_TIMEOUT")).intValue();
                Constantes.country = fichConfig.getValue("IDIOMA", "COUNTRY");
                Constantes.language = fichConfig.getValue("IDIOMA", "LANGUAGE");
                Constantes.Locale = Constantes.language+"_"+Constantes.country;
                Constantes.Municipio= fichConfig.getValue("AYUNTAMIENTO", "MUNICIPIO");
                try
                {
                     Constantes.idMunicipio= new Integer(fichConfig.getValue("AYUNTAMIENTO", "IDMUNICIPIO")).intValue();
                }catch (Exception e){
                    logger.error("Valor de id municipio no valido:"+e.toString()+fichConfig.getValue("AYUNTAMIENTO", "IDMUNICIPIO"));
                }
                Constantes.Provincia= fichConfig.getValue("AYUNTAMIENTO", "PROVINCIA");
            }
            else
            {
                //JOptionPane.showMessageDialog(this, "Fichero no valido, lo obtengo por un stream");

                Properties aprops = new Properties();
                aprops.load((getClass().getClassLoader().getResourceAsStream("config/configAdministracion.ini")));
                Constantes.url= (String) aprops.get("SERVER_URL");
			    Constantes.timeout= new Integer((String) aprops.get("SERVER_TIMEOUT")).intValue();
                Constantes.country =(String) aprops.get("COUNTRY");
                Constantes.language = (String) aprops.get("LANGUAGE");
                Constantes.Locale = Constantes.language+"_"+Constantes.country;
                Constantes.Municipio= (String) aprops.get("MUNICIPIO");
                try
                {
                     Constantes.idMunicipio=new Integer((String)aprops.get("IDMUNICIPIO")).intValue();
                }catch (Exception e){
                    logger.error("Valor de id municipio no valido:"+e.toString()+fichConfig.getValue("AYUNTAMIENTO", "IDMUNICIPIO"));
                }
                Constantes.Provincia= (String) aprops.get("PROVINCIA");
            } */
            try{
                   ApplicationContext app= AppContext.getApplicationContext();
                   Constantes.url= app.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+"administracion";
                   Constantes.Locale = app.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY,"es_ES",false);
                   try{Constantes.idMunicipio=new Integer(app.getUserPreference("geopista.DefaultCityId","0",false)).intValue();
                   }catch (Exception e){
                              JOptionPane.showMessageDialog(this, "Valor de id municipio no valido:"+e.toString()+app.getUserPreference("geopista.DefaultCityId","0",false));
                              //System.out.println("Valor de id municipio no valido:"+e.toString()+app.getUserPreference("geopista.DefaultCityId","0",false));
                              //logger.error("Valor de id municipio no valido:"+e.toString()+app.getUserPreference("geopista.DefaultCityId","0",false));
                              System.exit(-1);
                   }


            }catch(Exception e){
                	StringWriter sw = new StringWriter();
			        PrintWriter pw = new PrintWriter(sw);
			        //e.printStackTrace(pw);
                    JOptionPane.showMessageDialog(this, "Excepcion al cargar el fichero de configuración:\n"+sw.toString());
			        //logger.error("Exception: " + sw.toString());
                    System.exit(-1);
			        return false;
            }

            //System.out.println("administrador.Constantes.url: " + Constantes.url);
            //System.out.println("administrador.Constantes.timeout: " + Constantes.timeout);

			//logger.debug("administrador.Constantes.url: " + Constantes.url);
			//logger.debug("administrador.Constantes.timeout: " + Constantes.timeout);
			//****************************************************************
			//** Establecemos el idioma especificado en la configuracion
			//*******************************************************
			setLang(Constantes.Locale);
            return true;

		} catch (Exception ex) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			//ex.printStackTrace(pw);
            JOptionPane.showMessageDialog(this, "Excepcion al cargar el fichero de configuración:\n"+sw.toString());
//			logger.error("Exception: " + sw.toString());
			return false;

		}

	}

	private boolean setLang(String locale) {

		try {
            //System.out.println("Cambiando el idioma a "+locale);
			//logger.debug("Cambiando idioma a: " + locale);
			/*if (fichConfig!=null)
            {
                fichConfig.setValue("IDIOMA", "COUNTRY",country);
                fichConfig.setValue("IDIOMA", "LANGUAGE",language);
                fichConfig.saveFile();
            } */
            try
            {
                ApplicationContext app = AppContext.getApplicationContext();
                app.setUserPreference(AppContext.GEOPISTA_LOCALE_KEY,locale);
            }catch(Exception e)
            {
                //logger.error("Exception: " + e.toString());
            }
			Locale currentLocale = new Locale(locale);
			messages = ResourceBundle.getBundle("config.administrador", currentLocale);
            changeScreenLang(messages);
            if (usuariosFrame!=null) usuariosFrame.changeScreenLang(messages);
            //if (dominiosFrame!=null) dominiosFrame.changeScreenLang(messages);
            Constantes.Locale = locale;
			return true;

		} catch (Exception ex) {
			//logger.error("Exception: " + ex.toString());
			return false;
		}

	}


  	private boolean changeScreenLang(ResourceBundle messages) {
        //setTitle(messages.getString("CMainAdministrador.title"));
      	return true;

	}


    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }
}







