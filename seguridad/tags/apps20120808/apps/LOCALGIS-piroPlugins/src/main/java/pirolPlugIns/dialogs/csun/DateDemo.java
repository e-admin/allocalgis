/*
 * Found on 12.04.2005 for PIROL
 * 
 * CVS header information:
 * $RCSfile: DateDemo.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:32:07 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/csun/DateDemo.java,v $
 */
package pirolPlugIns.dialogs.csun;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
* DateDemo  Java example program for CS 585.
* Uses student provided DateChooser.java dialog 
*
* Mike Barnes 9/19/01
*/
public class DateDemo extends JFrame {
  private JButton getDate;
  private JLabel dateLabel;
  private GregorianCalendar date;
  private DateChooser dc;

   //All applications must have a main method.
   public static void main(String args[]) {
      DateDemo app = new DateDemo("Date Demo");
      app.setBounds(100,100,300, 100);
      app.show();
      // need to be able to close the window.
      app.addWindowListener( new WindowAdapter() {
         public void windowClosing(WindowEvent e)
            {System.exit(0);}   });
      }

   // the constructor of the frame window
   public DateDemo(String frameTitle) {
      super(frameTitle);
      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout(5,5));
      getDate = new JButton("Get a date");
      // listen for JButton events
      getDate.addActionListener(new JButtonListener());
      contentPane.add(getDate, BorderLayout.CENTER);
      dateLabel = new JLabel("date is displayed here",JLabel.CENTER);
      contentPane.add(dateLabel, BorderLayout.SOUTH);
   }

   // the "callback function" to handle button press events
   class JButtonListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {  
        date = new GregorianCalendar();
        dc = new DateChooser(DateDemo.this, date);         
        String dateString;
        // get date and if valid format a display.
        if (dc.showDateChooser() == DateChooser.OK_OPTION) {
          date = dc.getDate();
          dateString = new String(
                       Integer.toString(date.get(Calendar.MONTH) + 1) + " / ");
          dateString = dateString.concat(
                       Integer.toString(date.get(Calendar.DATE)) + " / ");
          dateString = dateString.concat(
                       Integer.toString(date.get(Calendar.YEAR)));
          dateLabel.setText(dateString); }
      }
   }
}