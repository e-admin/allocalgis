package jimm.datavision.gui;
import jimm.datavision.*;
import jimm.datavision.layout.*;
import jimm.datavision.layout.pdf.PDFLE;

import java.io.*;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * This dialog lets the user export report results using one of the
 * layout engines.
 *
 * @author Jim Menard, <a href="mailto:jimm@io.com">jimm@io.com</a>
 */
public class ExportWin extends JDialog implements ActionListener {


protected Report report;
protected JComboBox combo;

/**
 * Constructor.
 *
 * @param owner the parent window to which this dialog belongs
 * @param initialText the initial text to edit
 * @param title the window title
 */
public ExportWin(Frame owner, Report report) {
    super(owner, I18N.get("ExportWin.title"));
    this.report = report;
    buildWindow();
    pack();
    setVisible(true);
}

/**
 * Builds the window contents.
 *
 * @param whichTab the index of the tab to display when opened
 */
protected void buildWindow() {
    // Panel containing list of layout engines
    combo = new JComboBox(layoutNames());
    JPanel comboPanel = new JPanel();
    comboPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    comboPanel.add(combo);

    // OK and Cancel Buttons
    JPanel buttonPanel = buildButtonPanel();

    // Add list, cards, and buttons to window
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(comboPanel, BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
}

/**
 * Builds and returns a panel containing the OK and Cancel
 *
 * @return a panel
 */
protected JPanel buildButtonPanel() {
    JPanel buttonPanel = new JPanel();
    JButton button;

    buttonPanel.add(button = new JButton(I18N.get("GUI.ok")));
    button.addActionListener(this);
    button.setDefaultCapable(true);

    buttonPanel.add(button = new JButton(I18N.get("GUI.cancel")));
    button.addActionListener(this);

    return buttonPanel;
}

/**
 * Handles the OK and Cancel buttons.
 *
 * @param e action event
 */
public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    if (I18N.get("GUI.ok").equals(cmd)) {
	String choice = (String)combo.getSelectedItem();
	dispose();
	runReport(choice);
    }
    else if (I18N.get("GUI.cancel").equals(cmd)) {
	dispose();
    }
}

protected String[] layoutNames() {
    /** Satec */
    //String[] names = new String[7];
    String[] names = new String[2];

    int i = 0;
    //names[i++] = I18N.get("ExportWin.le_comma");
    names[i++] = I18N.get("ExportWin.le_tab");
    //names[i++] = I18N.get("ExportWin.le_docbook");
    //names[i++] = I18N.get("ExportWin.le_html");
    //names[i++] = I18N.get("ExportWin.le_latex");
    names[i++] = I18N.get("ExportWin.le_pdf");
    //names[i++] = I18N.get("ExportWin.le_xml");
    return names;
}

/**
 * Given the user's choice of layout engine, ask user for output file
 * and run the report.
 *
 * @param choice the combo box string that the user selected
 */
protected void runReport(String choice) {
    String extension = null;
    if (I18N.get("ExportWin.le_comma").equals(choice))
	extension = ".csv";
    else if (I18N.get("ExportWin.le_tab").equals(choice))
	//extension = ".tab";
    extension= ".txt";
    else if (I18N.get("ExportWin.le_docbook").equals(choice))
	extension = ".sgml";
    else if (I18N.get("ExportWin.le_html").equals(choice))
	extension = ".html";
    else if (I18N.get("ExportWin.le_latex").equals(choice))
	extension = ".tex";
    else if (I18N.get("ExportWin.le_pdf").equals(choice))
	extension = ".pdf";
    else if (I18N.get("ExportWin.le_xml").equals(choice))
	extension = ".xml";

    String path = selectFile(extension);
    if (path == null)		// Cancelled by user
	return;

    try {
	LayoutEngine le = null;
	if (I18N.get("ExportWin.le_comma").equals(choice))
	    le = new CharSepLE(new PrintWriter(new FileWriter(path)), ',');
	else if (I18N.get("ExportWin.le_tab").equals(choice)){
        /** satec */        
        if (!path.endsWith(".txt"))
            le = new CharSepLE(new PrintWriter(new FileWriter(path+extension)), '\t');
        else
    	    le = new CharSepLE(new PrintWriter(new FileWriter(path)), '\t');
    }
	else if (I18N.get("ExportWin.le_docbook").equals(choice))
	    le = new DocBookLE(new PrintWriter(new FileWriter(path)));
	else if (I18N.get("ExportWin.le_html").equals(choice)){
        /** satec */
        if ((!path.endsWith(".html")) && (!path.endsWith(".htm")))
	        le = new HTMLLE(new PrintWriter(new FileWriter(path+extension)));
        else
            le = new HTMLLE(new PrintWriter(new FileWriter(path)));
    }
	else if (I18N.get("ExportWin.le_latex").equals(choice))
	    le = new LaTeXLE(new PrintWriter(new FileWriter(path)));
	else if (I18N.get("ExportWin.le_pdf").equals(choice)){
        /** satec */
        if (!path.endsWith(".pdf"))
	        le = new PDFLE(new FileOutputStream(path+extension));
        else
            le = new PDFLE(new FileOutputStream(path));
    }
	else if (I18N.get("ExportWin.le_xml").equals(choice))
	    le = new XMLLE(new XMLWriter(new FileOutputStream(path)));

	if (le != null) {
	    report.setLayoutEngine(le);
	    report.run();
	}
    }
    catch (IOException e) {
	ErrorHandler.error(I18N.get("ExportWin.err_msg"),
			   e, I18N.get("ExportWin.err_title"));
    }
}

/**
 * Lets user select output file. I'd like to be able to give a default
 * file extension (thus the argument), but that will have to come later.
 *
 * @param extension default file name extension (unused)
 * @return the path to the selected file
 */
protected String selectFile(String extension) {
    JFileChooser chooser = Designer.getChooser();
    if (chooser.showSaveDialog(this.getOwner()) == JFileChooser.APPROVE_OPTION)
	return chooser.getSelectedFile().getPath();
    else
	return null;
}

}
