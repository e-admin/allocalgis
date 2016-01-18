/**
 * DateChooser.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Found on 12.04.2005 for PIROL
 * 
 * CVS header information:
 * $RCSfile: DateChooser.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:32:07 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/dialogs/csun/DateChooser.java,v $
 */

/*********************  DateChooser.java **************************/

package pirolPlugIns.dialogs.csun;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pirolPlugIns.i18n.PirolPlugInMessages;


/**
 * Hovanes Gambaryan
 * Henry Demirchian
 * CSUN, CS 585
 * Professor Mike Barnes
 * December 06, 2000
 * --
 * Additional work by Stefan Ostermann, 2005/11/30
 * I18n for the Pirol-Project and Jump
 * www.projekt-pirol.de
 * --
 * DateChooser class is a general GUI based date chooser. It allows
 * the user to select an instance of GregorianCalendar defined in
 * java.util package.
 *
 * Programming API is similar to JFC's JColorChooser or JFileChooser. 
 * This class can be used in any application to enable the user to 
 * select a date from a visually displayed calendar.
 *
 * There is a lot of improvements that can be done over this class
 * in areas of functionality, usability, and appearance. But as is,
 * the class can be easily used from within any Java program.
 *
 * Typical usage is like:
 *
 *  // initial date
 *  GregorianCalendar date = new GregorianCalendar()
 *
 *      // The  owner is the JFrame of the application ("AppClass.this")
 *
 *  // show the date chooser
 *  DateChooser dc = new DateChooser(owner, date);
 *
 *  // user can eiter choose a date or cancel by closing
 *  if (dc.showDateChooser() == DateChooser.OK_OPTION) {
 *    date = dc.getDate();
 *  }
 */
 
public class DateChooser extends JDialog implements ActionListener {

  public static final int OK_OPTION = 1;
  public static final int CANCEL_OPTION = 2;

  private static final ArrayList monthNames;
  static {
    monthNames = new ArrayList(12);
    monthNames.add(
    		PirolPlugInMessages.getString("january"));
    monthNames.add(
    		PirolPlugInMessages.getString("february"));
    monthNames.add(
    		PirolPlugInMessages.getString("march"));
    monthNames.add(
    		PirolPlugInMessages.getString("april"));
    monthNames.add(
    		PirolPlugInMessages.getString("may"));
    monthNames.add(
    		PirolPlugInMessages.getString("june"));
    monthNames.add(
    		PirolPlugInMessages.getString("july"));
    monthNames.add(
    		PirolPlugInMessages.getString("august"));
    monthNames.add(
    		PirolPlugInMessages.getString("september"));
    monthNames.add(
    		PirolPlugInMessages.getString("october"));
    monthNames.add(
    		PirolPlugInMessages.getString("november"));
    monthNames.add(
    		PirolPlugInMessages.getString("december"));
  };

  private GregorianCalendar date;
  private JLabel monthLabel;
  private JLabel yearLabel;
  private JPanel dayGrid;
  private boolean ready;


  /**
   * Constructor for DateChooser
   *
   * @param owner JFrame instance, owner of DateChooser dialog
   * @param d GregorianCalendar instance that will be the initial date
   *          for this dialog
   */
  public DateChooser (JFrame owner, GregorianCalendar d) {
    super(owner, 
    		PirolPlugInMessages.getString("date-chooser"), true);
    date = d;

    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    JPanel yearPane = new JPanel();
    JPanel monthPane = new JPanel();
    yearPane.setLayout(new BoxLayout(yearPane, BoxLayout.X_AXIS));
    monthPane.setLayout(new BoxLayout(monthPane, BoxLayout.X_AXIS));

    JButton[] navButton = new JButton[4];

    // build the panel with month name and navigation buttons
    monthPane.add(navButton[0] = new JButton("<"));
    monthPane.add(monthLabel =
        new JLabel(String.valueOf(monthNames.get(
        date.get(GregorianCalendar.MONTH))), JLabel.CENTER));
    monthLabel.setMinimumSize(new Dimension(80, 17));
    monthLabel.setMaximumSize(new Dimension(80, 17));
    monthLabel.setPreferredSize(new Dimension(80, 17));
    monthPane.add(navButton[1] = new JButton(">"));

    // build the panel with year and navigation buttons
    yearPane.add(navButton[2] = new JButton("<<"));
    yearPane.add(yearLabel =
        new JLabel(String.valueOf(
        date.get(GregorianCalendar.YEAR)),
        JLabel.CENTER), BorderLayout.CENTER);
    yearLabel.setMinimumSize(new Dimension(50, 17));
    yearLabel.setMaximumSize(new Dimension(50, 17));
    yearLabel.setPreferredSize(new Dimension(50, 17));
    yearPane.add(navButton[3] = new JButton(">>"));

    // register a listener on the navigation buttons
    for (int i=0; i<4; i++) {
      navButton[i].addActionListener(this);
    }

    // set the tool tip text on the navigation buttons
    navButton[0].setToolTipText(
    		PirolPlugInMessages.getString("go-to-the-previous-month"));
    navButton[1].setToolTipText(
    		PirolPlugInMessages.getString("go-to-the-next-month"));
    navButton[2].setToolTipText(
    		PirolPlugInMessages.getString("go-to-the-previous-year"));
    navButton[3].setToolTipText(
    		PirolPlugInMessages.getString("go-to-the-next-year"));

    // put the panel for months and years together and add some formatting
    JPanel topPane = new JPanel();
    topPane.setLayout(new BoxLayout(topPane, BoxLayout.X_AXIS));
    topPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
    topPane.add(monthPane);
    topPane.add(Box.createRigidArea(new Dimension(20, 0)));
    topPane.add(yearPane);

    // create the panel that will hold the days of the months
    dayGrid = new JPanel(new GridLayout(7, 7));
    updateDayGrid();

    contentPane.add(topPane, BorderLayout.NORTH);
    contentPane.add(dayGrid, BorderLayout.CENTER);

    setResizable(false);
    ready = false;
    pack();
    
    // center this dialog over the owner
    int xPos = (int) (owner.getLocation().getX() + 
      (owner.getWidth() / 2) - (getWidth() / 2));
    int yPos =  (int) (owner.getLocation().getY() +
      (owner.getHeight() / 2) - (getHeight() / 2));
    setLocation(xPos, yPos);
  }


  /**
   * Return the last selected date for this instance of DateChooser
   */
  public GregorianCalendar getDate () {
    return date;
  }


  /**
   * Displays a DateChooser dialog on the screen. If a new date is selected
   * returnsor OK_OPTION. If the action is canceled returns CANCEL_OPTION.
   * Both of the returned values are defined as static constants.
   */
  public int showDateChooser () {
    ready = false;
    setVisible(true);
    if (ready) {
      return(OK_OPTION);
    }
    else {
      return(CANCEL_OPTION);
    }
  }


  /**
   * Action handler for this dialog, which handles all the button presses.
   *
   * @param evt ActionEvent
   */
  public void actionPerformed (ActionEvent evt) {
    String label = ((JButton) evt.getSource()).getText();

    if (label.equals("<")) {
      int m = monthNames.indexOf(monthLabel.getText());
      m = prevMonth(m);
      monthLabel.setText((String) monthNames.get(m));
      updateDayGrid();
    }
    else if (label.equals(">")) {
      int m = monthNames.indexOf(monthLabel.getText());
      m = nextMonth(m);
      monthLabel.setText((String) monthNames.get(m));
      updateDayGrid();
    }
    else if (label.equals("<<")) {
      int y = 0;
      try {
        y = Integer.parseInt(yearLabel.getText());
      }
      catch (NumberFormatException e) {
        System.err.println(e.toString());
      }
      yearLabel.setText(String.valueOf(--y));
      updateDayGrid();
    }
    else if (label.equals(">>")) {
      int y = 0;
      try {
        y = Integer.parseInt(yearLabel.getText());
      }
      catch (NumberFormatException e) {
        System.err.println(e.toString());
      }
      yearLabel.setText(String.valueOf(++y));
      updateDayGrid();
    }
    else {
      int m = monthNames.indexOf(monthLabel.getText());
      int y = 0;
      int d = 0;
      try {
        y = Integer.parseInt(yearLabel.getText());
        d = Integer.parseInt(label);
      }
      catch (NumberFormatException e) {
        System.err.println(e.toString());
      }
      date = new GregorianCalendar(y, m, d);
      date.setLenient(false);
      ready = true;
      dispose();
    }
  }


  /**
   * This method is used by DateChooser to calculate and display days of
   * the month in correct format for the month currently displayed.
   * Days of the months are displayed as JButtons that the user can select.
   * DateChooser's current day is higlighted in red color.
   */
  private void updateDayGrid () {
    dayGrid.removeAll();

    // get the currently selected month and year
    int m = monthNames.indexOf(monthLabel.getText());
    int y = 0;
    try {
      y = Integer.parseInt(yearLabel.getText());
    }
    catch (NumberFormatException e) {
      System.err.println(e.toString());
    }

    // look at the first day of the month for this month
    GregorianCalendar temp = new GregorianCalendar(y, m, 1);
    temp.setLenient(false);
    int offset = 0;

    // decide what day of the week is the first day of this month
    switch(temp.get(GregorianCalendar.DAY_OF_WEEK)) {
      case GregorianCalendar.MONDAY  : offset = 0; break;
      case GregorianCalendar.TUESDAY   : offset = 1; break;
      case GregorianCalendar.WEDNESDAY : offset = 2; break;
      case GregorianCalendar.THURSDAY  : offset = 3; break;
      case GregorianCalendar.FRIDAY  : offset = 4; break;
      case GregorianCalendar.SATURDAY  : offset = 5; break;
      case GregorianCalendar.SUNDAY  : offset = 6; break;
    }

    // display 7 days of the week across the top
    dayGrid.add(new JLabel(
    		PirolPlugInMessages.getString("mon"), JLabel.CENTER));
    dayGrid.add(new JLabel(
    		PirolPlugInMessages.getString("tue"), JLabel.CENTER));
    dayGrid.add(new JLabel(
    		PirolPlugInMessages.getString("wed"), JLabel.CENTER));
    dayGrid.add(new JLabel(
    		PirolPlugInMessages.getString("thu"), JLabel.CENTER));
    dayGrid.add(new JLabel(
    		PirolPlugInMessages.getString("fri"), JLabel.CENTER));
    dayGrid.add(new JLabel(
    		PirolPlugInMessages.getString("sat"), JLabel.CENTER));
    dayGrid.add(new JLabel(
    		PirolPlugInMessages.getString("sun"), JLabel.CENTER));

    // skip to the correct first day of the week for this month
    for (int i=1; i<=offset; i++) {
      dayGrid.add(new JLabel(""));
    }

    // display days of the month for this month
    JButton day;
    for (int i=1; i<=getLastDay(); i++) {
      dayGrid.add(day = new JButton(String.valueOf(i)));
      day.setToolTipText(
    		  	PirolPlugInMessages.getString("click-on-a-day-to-choose-it"));
      day.addActionListener(this);

      // show the current day in bright red.
      if (i == date.get(Calendar.DATE) &&
        m == date.get(Calendar.MONTH) &&
        y == date.get(Calendar.YEAR)) {
          day.setForeground(Color.red);
      }
    }

    // display the remaining empty slots to preserve the structure
    for (int i=(offset+getLastDay()+1); i<=42; i++) {
      dayGrid.add(new JLabel(""));
    }

    repaint();
    validate();
  }


  /**
   * Return the month following the one passed in as an argument.
   * If the argument is the las month of the year, return the first month.
   *
   * @param month Current month expressed as an integer (0 to 11).
   */
  private int nextMonth (int month) {
    if (month == 11) {
      return(0);
    }
    return(++month);
  }


  /**
   * Return the month preceding the one passed in as an argument.
   * If the argument is the first month of the year, return the last month.
   *
   * @param month Current month expressed as an integer (0 to 11).
   */
  private int prevMonth (int month) {
    if (month == 0) {
      return(11);
    }
    return(--month);
  }


  /**
   * Return the value of the last day in the currently selected month
   */
  private int getLastDay () {
    int m = (monthNames.indexOf(monthLabel.getText()) + 1);
    int y = 0;
    try {
      y = Integer.parseInt(yearLabel.getText());
    }
    catch (NumberFormatException e) {
      System.err.println(e.toString());
    }

    if ((m==4) || (m==6) || (m==9) || (m==11)) {
      return(30);
    }
    else if (m==2) {
      if (date.isLeapYear(y)) {
        return(29);
      }
      return(28);
    }
    return(31);
  }
}