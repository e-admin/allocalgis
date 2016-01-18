package com.geopista.util;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.GeopistaPrintPlugIn;
import com.geopista.ui.plugin.print.PrintLayoutFrame;
import com.geopista.ui.wizard.WizardContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.OrderedMap;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
public class printPanel02 extends javax.swing.JPanel implements  com.geopista.ui.wizard.WizardPanel
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(printPanel02.class);

	private JCheckBox jCheckBox1;  //  @jve:decl-index=0:visual-constraint="106,388"
	private JLabel jLabel1;
	private JComboBox jComboBox1;
AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

  private Blackboard blackboardInformes  = aplicacion.getBlackboard();
  private WizardContext wizardContext;
  private OrderedMap escalas = new OrderedMap();
  private OrderedMap escalasInverso = new OrderedMap();  
  private String localId = null;

private PlugInContext	context;
  
  
  
	public printPanel02(String id, String nextId, PlugInContext context) {
	    
	    this.nextID = nextId;
	    this.localId = id;
	    this.context=context;
		setName(I18N.get("printPanel02.Name"));
		initialize();
		escalas.put("1:2500","2500");
	    escalas.put("1:5000","5000");
	   	escalas.put("1:10000","10000");
	    escalas.put("1:25000","25000");            
	    escalas.put("1:40000","40000");            
	    escalas.put("1:50000","50000");
	    escalas.put(I18N.get("EscalaVistaActual"),"-1");

      //matriz con el valor de las escalas
      escalasInverso.put("2500","1:2500");
      escalasInverso.put("5000","1:5000");
   	  escalasInverso.put("10000","1:10000");
      escalasInverso.put("25000","1:25000");            
      escalasInverso.put("40000","1:40000");            
      escalasInverso.put("50000","1:50000");
      escalasInverso.put("-1",I18N.get("EscalaVistaActual"));

      
      List nombresEscalas = escalas.keyList();
      Iterator nombresEscalasIter = nombresEscalas.iterator();
      while(nombresEscalasIter.hasNext())
      {
        jComboBox1.addItem(nombresEscalasIter.next());
      }
	}		


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
			
	   
		try {
			preInitGUI();
	
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			jCheckBox1 = new JCheckBox();
			jLabel1 = new JLabel();
			jComboBox1 = new JComboBox();
			jComboBox1.addItemListener(new ItemListener(){

				
				
				
				public void itemStateChanged(ItemEvent arg0)
				{
				if (wizardContext!=null)
				wizardContext.inputChanged();
				
				}});
			this.setLayout(new GridBagLayout());  // Generated
	
			this.setPreferredSize(new java.awt.Dimension(556,365));
	
			jCheckBox1.setText(getExternalizedString("PrintWizard.Leyenda"));
			jCheckBox1.setVisible(true);
			
			jLabel1.setText(getExternalizedString("PrintWizard.Escala")); //$NON-NLS-1$
			jLabel1.setEnabled(true);
			jLabel1.setVisible(true);
			jLabel1.setPreferredSize(new java.awt.Dimension(120,20));
		
			jComboBox1.setVisible(true);
			jComboBox1.setPreferredSize(new java.awt.Dimension(320,20));
			jComboBox1.setEditable(true);  // Generated
			gridBagConstraints1.gridx = 0;  // Generated
			gridBagConstraints1.gridy = 3;  // Generated
			gridBagConstraints1.insets = new java.awt.Insets(0,0,0,0);  // Generated
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;  // Generated
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints1.gridwidth = 3;  // Generated
			
			gridBagConstraints2.gridx = 0;  // Generated
			gridBagConstraints2.gridy = 2;  // Generated
			gridBagConstraints2.insets = new java.awt.Insets(0,0,0,0);  // Generated
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;  // Generated
			gridBagConstraints3.gridx = 1;  // Generated
			gridBagConstraints3.gridy = 2;  // Generated
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints3.insets = new java.awt.Insets(0,5,40,5);  // Generated
			gridBagConstraints3.gridwidth = 2;  // Generated
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;  // Generated
			this.add(jComboBox1, gridBagConstraints3);  // Generated
			this.add(jCheckBox1, gridBagConstraints1);  // Generated
			this.add(jLabel1, gridBagConstraints2);  // Generated
			postInitGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** Add your pre-init code in here 	*/
	public void preInitGUI(){
	}

	/** Add your post-init code in here 	*/
	public void postInitGUI(){
	}

	/** Auto-generated main method */
	public static void main(String[] args){
		showGUI();
	}

	/**
	* This static method creates a new instance of this class and shows
	* it inside a new JFrame, (unless it is already a JFrame).
	*
	* It is a convenience method for showing the GUI, but it can be
	* copied and used as a basis for your own code.	*
	* It is auto-generated code - the body of this method will be
	* re-generated after any changes are made to the GUI.
	* However, if you delete this method it will not be re-created.	*/
	public static void showGUI(){
		try {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			printPanel02 inst = new printPanel02(null,null,null);
			frame.setContentPane(inst);
			frame.getContentPane().setSize(inst.getSize());
			frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	* 
	*/
	public String getExternalizedString(String key){
		return I18N.get(key);
	}
	 public void enteredFromLeft(Map dataMap)
  {
	     
	   
          //Iniciamos la ayuda
            try {
                String helpHS="ayuda.hs";
                HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
                HelpBroker hb = hs.createHelpBroker();
                hb.enableHelpKey(this,"planeamientoGenerarMapaPlaneamiento03",hs); 
          }catch (Exception excp){
          }                        
 //fin de la ayuda

    String escalaSeleccionada = (String) blackboardInformes.get(GeopistaPrintPlugIn.ESCALAIMPRIMIRMAPA);
    if(escalaSeleccionada!=null)
    {

//      jComboBox1.setSelectedItem((String)escalasInverso.get(escalaSeleccionada));
    jComboBox1.setSelectedItem("-1".equals(escalaSeleccionada)?I18N.get("EscalaVistaActual"):escalaSeleccionada);
    }
    Boolean mostrarLeyendaBoolean = (Boolean) blackboardInformes.get(GeopistaPrintPlugIn.MOSTRARLEYENDAIMPRIMIR);
    if(mostrarLeyendaBoolean!=null)
    { 
      boolean mostrarLeyenda = mostrarLeyendaBoolean.booleanValue();
      jCheckBox1.setSelected(mostrarLeyenda);
    }
    
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
      String escalaSeleccionada = (String) jComboBox1.getSelectedItem();
      if (escalaSeleccionada.equals(I18N.get("EscalaVistaActual")))
      	blackboardInformes.put(GeopistaPrintPlugIn.ESCALAIMPRIMIRMAPA,"-1");
      else
      	{
      	String escalaReal[] =  escalaSeleccionada.split(":");
      	blackboardInformes.put(GeopistaPrintPlugIn.ESCALAIMPRIMIRMAPA,escalaReal[1]);
      	}
      blackboardInformes.put(GeopistaPrintPlugIn.ESCALASELECCIONADAIMPRIMIR,escalaSeleccionada);
      Boolean  showLeyend = new Boolean(jCheckBox1.isSelected());
      blackboardInformes.put(GeopistaPrintPlugIn.MOSTRARLEYENDAIMPRIMIR,showLeyend);
      
      
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
      return this.getName();
    }

    public String getID()
    {
      return localId;
    }

    public String getInstructions()
    {
    	   return I18N.get("PrintJPanel02Instructions");
    	   
    }

    public boolean isInputValid()
    {
    try
		{
			String escalaSeleccionada = (String) jComboBox1.getSelectedItem();
			if (escalaSeleccionada.equals(I18N.get("EscalaVistaActual"))) return true;
			String escalaReal[] =  escalaSeleccionada.split(":");
			if ("1".equals(escalaReal[0]) && Double.parseDouble(escalaReal[1])>0 )
				return true;
			else
			  return false;
		}
	catch (NumberFormatException e)
		{
		logger.debug("isInputValid() - Formato incorrecto en la escala.");

		return false;
		}
    }

    public void setWizardContext(WizardContext wd)
    {
      wizardContext = wd;
    }

    /**
     * @return null to turn the Next button into a Finish button
     */
    private String nextID=null;
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }
    public String getNextID()
    {
      return nextID;
    }


    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }
}  //  @jve:decl-index=0:
