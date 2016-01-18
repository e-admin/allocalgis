package com.tinyline.app;

import com.tinyline.svg.SVG;
import com.tinyline.svg.SVGDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.EventObject;

// Referenced classes of package com.tinyline.app:
//            PPSVGCanvas, MouseHandler, PlayerListener, StatusBar

public class TinyLine extends Frame
    implements ActionListener, ItemListener, StatusBar
{

    Dimension dim;
    Rectangle toolbarBounds;
    Rectangle statusBounds;
    Rectangle canvasBounds;
    MouseHandler currentMouseHandler;
    Button fileButton;
    Button goButton;
    TextField documentUrlField;
    int m_x;
    int m_y;
    PopupMenu m_menu;
    MenuItem openButton;
    CheckboxMenuItem linkButton;
    CheckboxMenuItem panButton;
    CheckboxMenuItem zoomInButton;
    CheckboxMenuItem zoomOutButton;
    CheckboxMenuItem current;
    MenuItem pauseButton;
    MenuItem refreshButton;
    MenuItem exitButton;
    Label alertLabel;
    PPSVGCanvas canvas;
    Runtime r;

    public TinyLine()
    {
        m_menu = new PopupMenu();
    }

    public void init()
    {
        r = Runtime.getRuntime();
        setLayout(null);
        setTitle("TinyLine 1.10");
        setBackground(new Color(184, 200, 216));
        setResizable(false);
        dim = getSize();
        Insets insets = getInsets();
        int i = insets.top;
        int j = dim.height - insets.bottom;
        int k = insets.left;
        int l = dim.width - insets.right;
        toolbarBounds = new Rectangle(k, i, l - k, 30);
        i += 30;
        statusBounds = new Rectangle(k, j - 30, l - k, 30);
        j -= 30;
        canvasBounds = new Rectangle(k, i, l - k, j - i);
        i = toolbarBounds.y + 3;
        k = toolbarBounds.x + 3;
        fileButton = new Button("File");
        fileButton.setSize(40, 24);
        fileButton.setBounds(k, i, fileButton.getPreferredSize().width, 24);
        fileButton.addActionListener(this);
        add(fileButton);
        m_x = fileButton.getBounds().x;
        m_y = fileButton.getBounds().y + fileButton.getBounds().height;
        k += fileButton.getSize().width + 3;
        goButton = new Button("Go");
        goButton.setSize(30, 24);
        l -= goButton.getPreferredSize().width + 3;
        goButton.setBounds(l, i, goButton.getPreferredSize().width, 24);
        goButton.addActionListener(this);
        add(goButton);
        documentUrlField = new TextField(20);
        documentUrlField.setBounds(k, i, l - k, 24);
        add(documentUrlField);
        documentUrlField.setText("http://");
        openButton = new MenuItem("Open");
        openButton.addActionListener(this);
        m_menu.add(openButton);
        m_menu.addSeparator();
        linkButton = new CheckboxMenuItem("Link", true);
        linkButton.addItemListener(this);
        m_menu.add(linkButton);
        current = linkButton;
        panButton = new CheckboxMenuItem("Pan");
        panButton.addItemListener(this);
        m_menu.add(panButton);
        zoomInButton = new CheckboxMenuItem("Zoom In");
        zoomInButton.addItemListener(this);
        m_menu.add(zoomInButton);
        zoomOutButton = new CheckboxMenuItem("Zoom Out");
        zoomOutButton.addItemListener(this);
        m_menu.add(zoomOutButton);
        m_menu.addSeparator();
        pauseButton = new MenuItem("Pause");
        pauseButton.addActionListener(this);
        m_menu.add(pauseButton);
        refreshButton = new MenuItem("Refresh");
        refreshButton.addActionListener(this);
        m_menu.add(refreshButton);
        m_menu.addSeparator();
        exitButton = new MenuItem("Exit");
        exitButton.addActionListener(this);
        m_menu.add(exitButton);
        add(m_menu);
        canvas = new PPSVGCanvas(canvasBounds.width, canvasBounds.height);
        canvas.setBounds(canvasBounds.x, canvasBounds.y, canvasBounds.width, canvasBounds.height);
        add(canvas);
        canvas.setStatusBar(this);
        alertLabel = new Label("");
        alertLabel.setBounds(statusBounds.x + 10, statusBounds.y, statusBounds.width - 20, statusBounds.height);
        add(alertLabel);
        alertLabel.setText("www.tinyline.com");
        currentMouseHandler = new MouseHandler(canvas);
        currentMouseHandler.type = 0;
        canvas.addMouseListener(currentMouseHandler);
        canvas.addMouseMotionListener(currentMouseHandler);
        SVGDocument svgdocument = canvas.loadSVG(getClass().getResourceAsStream("/com/tinyline/app/images/helvetica_svg"));
        com.tinyline.svg.SVGFontElem svgfontelem = SVGDocument.getFont(svgdocument, SVG.VAL_DEFAULT_FONTFAMILY);
        SVGDocument.defaultFont = svgfontelem;
        PlayerListener playerlistener = new PlayerListener(canvas);
        canvas.addEventListener("default", playerlistener, false);
        canvas.start();
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
        CheckboxMenuItem checkboxmenuitem = (CheckboxMenuItem)itemevent.getSource();
        if(current != null && current != checkboxmenuitem)
        {
            current.setState(false);
        }
        current = checkboxmenuitem;
        current.setState(true);
        if(checkboxmenuitem == panButton)
        {
            currentMouseHandler.type = 1;
        } else
        if(checkboxmenuitem == zoomInButton)
        {
            currentMouseHandler.type = 2;
        } else
        if(checkboxmenuitem == zoomOutButton)
        {
            currentMouseHandler.type = 3;
        } else
        if(checkboxmenuitem == linkButton)
        {
            currentMouseHandler.type = 0;
        }
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void alertError(String s)
    {
        alertLabel.setText(s);
        repaint(statusBounds.x, statusBounds.y, statusBounds.width, statusBounds.height);
    }

    public void alertWait(String s)
    {
        alertLabel.setText(s);
        repaint(statusBounds.x, statusBounds.y, statusBounds.width, statusBounds.height);
    }

    public void alertInit(String s)
    {
        alertLabel.setText(s);
        repaint(statusBounds.x, statusBounds.y, statusBounds.width, statusBounds.height);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj == fileButton)
        {
            m_menu.show(this, m_x, m_y);
        } else
        if(obj == openButton)
        {
            fileOpen();
        } else
        if(obj == pauseButton)
        {
            canvas.pauseResumeAnimations();
        } else
        if(obj == refreshButton)
        {
            canvas.origView();
        } else
        if(obj == goButton)
        {
            String s = documentUrlField.getText();
            if(s == null || s.equals(""))
            {
                return;
            }
            try
            {
                r.gc();
                URL url = new URL(s);
                canvas.goURL(s);
            }
            catch(Exception exception)
            {
                return;
            }
        } else
        if(obj == exitButton)
        {
            System.exit(0);
        }
    }

    private void fileOpen()
    {
        FileDialog filedialog = new FileDialog(this, "Load file", 0);
        if(filedialog == null)
        {
            return;
        }
        filedialog.setModal(true);
        filedialog.setVisible(true);
        if(filedialog.getFile() != null)
        {
            File file = new File(filedialog.getDirectory(), filedialog.getFile());
            try
            {
                URL url = new URL("file", "", slashify(file.getAbsolutePath(), file.isDirectory()));
                String s = url.toExternalForm();
                documentUrlField.setText(s);
                canvas.goURL(s);
                r.gc();
            }
            catch(Exception exception)
            {
                return;
            }
        }
        filedialog = null;
    }

    private static String slashify(String s, boolean flag)
    {
        String s1 = s;
        if(File.separatorChar != '/')
        {
            s1 = s1.replace(File.separatorChar, '/');
        }
        if(!s1.startsWith("/"))
        {
            s1 = "/" + s1;
        }
        if(!s1.endsWith("/") && flag)
        {
            s1 = s1 + "/";
        }
        return s1;
    }

    public static void main(String args[])
    {
        TinyLine tinyline = new TinyLine();
        tinyline.setBounds(0, 0, 240, 320);
        tinyline.setVisible(true);
        tinyline.init();
    }
}
