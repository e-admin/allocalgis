
package com.geopista.app.catastro;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.geopista.app.inicio.CatastroInicioExtended;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class GeopistaValidarReferenciaPanel extends JPanel implements WizardPanel
{
  private JLabel lblImagen = new JLabel();
  private JLabel lblReferencia = new JLabel();
  private JTextField txtReferencia = new JTextField();
  private JLabel lblDigitos = new JLabel();
  private JTextField txtDigito1 = new JTextField();
  private JTextField txtDigito2 = new JTextField();
  private JPanel pnlOpciones = new JPanel();
  private JRadioButton rdbUrbana = new JRadioButton();
  private JRadioButton rdbRustica = new JRadioButton();
  private JLabel lblUrbana = new JLabel();
  private JLabel lblRustica = new JLabel();
  private JLabel lblReferencia1 = new JLabel();
  

  public GeopistaValidarReferenciaPanel()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
    
/*  public static void main(String[] args)
  {
    //JFrame frame1 = new JFrame("Validar Referencia Catastral");    
    JDialog dlgVentana = new JDialog();
    GeopistaValidarReferenciaPanel iniciar = new GeopistaValidarReferenciaPanel();
   // dlgVentana.setTitle("Validar Referencia Catastral");
    dlgVentana.getContentPane().add(iniciar);
    dlgVentana.setSize(750, 600);
    dlgVentana.setVisible(true); 
    dlgVentana.setLocation(150, 90);
    
  }*/

  private void jbInit() throws Exception
  {
    this.setLayout(null);
    
  

    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));  
    lblImagen.setIcon(IconLoader.icon("catastro.png"));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    
    lblReferencia.setText("Introduzca la referencia catastral a validar:");
    lblReferencia.setBounds(new Rectangle(135, 35, 355, 25));
    txtReferencia.setBounds(new Rectangle(145, 85, 225, 20));
    lblDigitos.setText("Dígitos de Control:");
    lblDigitos.setBounds(new Rectangle(135, 140, 115, 20));
    txtDigito1.setBounds(new Rectangle(255, 140, 30, 20));
    txtDigito2.setBounds(new Rectangle(295, 140, 30, 20));
    pnlOpciones.setBounds(new Rectangle(335, 130, 80, 80));
    
    
    // se ocupa un contenedor o grupo especial para los radiobuttons

    ButtonGroup grupo= new ButtonGroup(); 
   /*rdbUrbana.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          rdbUrbana_actionPerformed(e);
        }
      });*/
    lblUrbana.setText("Urbana");
    lblRustica.setText("Rústica");
    lblReferencia1.setText("(18 Caracteres)");
    lblReferencia1.setBounds(new Rectangle(395, 85, 105, 25));

    this.setSize(750,600);
  
  grupo.add(rdbUrbana);
  grupo.add(rdbRustica); 

    pnlOpciones.add(rdbUrbana, null);
    pnlOpciones.add(lblUrbana, null);
    pnlOpciones.add(rdbRustica, null);
    pnlOpciones.add(lblRustica, null);

    this.add(lblReferencia1, null);
    this.add(pnlOpciones, null);
    this.add(txtDigito2, null);
    this.add(txtDigito1, null);
    this.add(lblDigitos, null);
    this.add(txtReferencia, null);
    this.add(lblReferencia, null);
    this.add(lblImagen,null);
    //this.add(jScrollPane1, null);

   //CuadroDialogo.showMessageDialog(this,"Vd. no tiene acceso"); 
   
  }//jbinit


  public void enteredFromLeft(Map dataMap)
  {
    
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
     System.out.println("saliendo de panel 1"); 
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
      return " ";
    }

    public String getID()
    {
      return "1";
    }

    public String getInstructions()
    {
     return " ";
    }

    public boolean isInputValid()
    {
      return true;
    }

    private String nextID="2";
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
  
private void rdbUrbana_actionPerformed(ActionEvent e)
  {
/*jb1.addMouseListener( new MouseAdapter()

{ public void mousePressed(MouseEvent e){*/

    if(rdbUrbana.isSelected()== true )
    {
      System.out.println("Ha seleccionado urbana");
    }
    if(rdbRustica.isSelected()== true )
    {
      System.out.println("Ha seleccionado rustica");
    }
    
}
 

  private void btnSiguiente_actionPerformed(ActionEvent e)
  {
/*jb1.addMouseListener( new MouseAdapter()

{ public void mousePressed(MouseEvent e){*/

    if(rdbUrbana.isSelected()== true )
    {
      System.out.println("Ha seleccionado urbana");
    }
    if(rdbRustica.isSelected()== true )
    {
      System.out.println("Ha seleccionado rustica");
    }
    
}

  private void btnCancelar_actionPerformed(ActionEvent e)
  {
    CatastroInicioExtended Ini = new CatastroInicioExtended();
    this.setVisible(false);
    JDialog Dialog = (JDialog) SwingUtilities.getWindowAncestor(this );
    Dialog.setVisible(false);
    Ini.setVisible(true);
    //Ini.displayPage("E:/Trabajos/SIT FEMP/Desarrollo/geopista/Browser/Catastro.htm");
  }

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}

/*private void btnAnterior_actionPerformed(ActionEvent e)
  {
     //CREA UNA VENTANA NUEVA, NO CIERRA LA ACTUAL*******          
         // JFrame dlgVentana = new JFrame("Consultar Catastro");
	JDialog dlgVentana = new JDialog();
          GeopistaCatastroPrincipalPanel geopistaCatastroPrincipalPanel = new GeopistaCatastroPrincipalPanel();
          dlgVentana.setTitle("Consultar Catastro");
          dlgVentana.getContentPane().add(geopistaCatastroPrincipalPanel);
          dlgVentana.setSize(750, 600);
          dlgVentana.setVisible(true); 
          dlgVentana.setLocation(150, 90);
          this.setVisible(false);
  }
  */

 }
