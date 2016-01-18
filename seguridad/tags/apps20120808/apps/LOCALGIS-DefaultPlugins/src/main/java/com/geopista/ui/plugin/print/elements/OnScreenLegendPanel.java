/*
 * 
 * Created on 16-jul-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.print.elements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.legendeFrame.LegendFrame;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.legendeFrame.Police;

import com.geopista.ui.legend.LegendPanel;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class OnScreenLegendPanel extends JPanel
{
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		// Draw handlers for resizing
		if (legendFrame.isSelected())
		{
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, GraphicElements._RESIZE_HANDLER_SIZE, GraphicElements._RESIZE_HANDLER_SIZE);
			g.drawRect(this.getWidth()-GraphicElements._RESIZE_HANDLER_SIZE-1, 0, GraphicElements._RESIZE_HANDLER_SIZE, GraphicElements._RESIZE_HANDLER_SIZE);
			g.drawRect(0, this.getHeight()-GraphicElements._RESIZE_HANDLER_SIZE-1, GraphicElements._RESIZE_HANDLER_SIZE, GraphicElements._RESIZE_HANDLER_SIZE);
			g.drawRect(this.getWidth()-GraphicElements._RESIZE_HANDLER_SIZE-1, this.getHeight()-GraphicElements._RESIZE_HANDLER_SIZE-1, GraphicElements._RESIZE_HANDLER_SIZE, GraphicElements._RESIZE_HANDLER_SIZE);
		}
	}
	public int getSymbolHeight()
	{
		return legendPanel.getSymbolHeight();
	}
	public void setSymbolHeight(int h)
	{
		legendPanel.setSymbolHeight(h);
	}
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
	.getLog(OnScreenLegendPanel.class);

	private LegendPanel legendPanel = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	protected LegendFrame	legendFrame;
	/**
	 * This is the default constructor
	 */
	public OnScreenLegendPanel() {
		super();
		initialize();


		setOpaque(false);
	}
	/**
	 * @param frame
	 */
	public OnScreenLegendPanel(LegendFrame frame)
	{
		super();
		legendFrame=frame;
		setLabelMapLegendFont();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	protected  void initialize() {

		getLegendPanel().setOpaque(false);
		getLegendPanel().setEditingMode(false);
		getLegendPanel().setAutoLayout(true);

		BorderLayout borderLayout9 = new BorderLayout();
		this.setLayout(borderLayout9);  // Generated
		this.setSize(300,200);

		this.setBackground(java.awt.SystemColor.text);  // Generated
		this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.controlShadow,1));  // Generated
		borderLayout9.setHgap(3);  // Generated
		borderLayout9.setVgap(3);  // Generated
		this.add(getJPanel(), java.awt.BorderLayout.SOUTH);  // Generated
		this.add(getLegendPanel(), java.awt.BorderLayout.CENTER);  // Generated
		this.add(getJPanel1(), java.awt.BorderLayout.EAST);  // Generated
		this.add(getJPanel2(), java.awt.BorderLayout.WEST);  // Generated
		this.add(getJPanel3(), java.awt.BorderLayout.NORTH);  // Generated
	}
	/**
	 * 
	 */
	public void setTitleMapLegendFont()
	{
		Police pol=legendFrame.getTitleMapLegendFont();
		getLegendPanel().setSymbolHeight(pol.getFontSize());
		getLegendPanel().repaint();
	}

	/**
	 * @param heightOnScreen
	 * @param newForPrintHeight
	 */
	public void setLabelMapLegendFont(int heightOnScreen, int newForPrintHeight)
	{
//		if (legendFrame!=null)
//		getLegendPanel().setSymbolSize(legendFrame.getLabelMapLegendFont().getFontSize()*heightOnScreen/newForPrintHeight*2,legendFrame.getLabelMapLegendFont().getFontSize()*heightOnScreen/newForPrintHeight);


//		logger.debug("setLabelMapLegendFont(heightOnScreen = "
//		+ heightOnScreen + ", newForPrintHeight = "
//		+ newForPrintHeight + ") - Cambio con parametros:");

//		validate();
	}
	/**
	 * @param heightOnScreen
	 * @param newForPrintHeight
	 */
	public void setTitleMapLegendFont(int heightOnScreen, int newForPrintHeight)
	{
		if (legendFrame!=null)
			getLegendPanel().setSymbolSize(getLegendPanel().getSymbolWidth()*heightOnScreen/newForPrintHeight,getLegendPanel().getSymbolHeight()*heightOnScreen/newForPrintHeight);
		validate();
	}
	
	/**
	 * 
	 */
	public void setValeursSpec()
	{
		setValeursSpec(0);
		/*
		List layers=legendFrame.getLayers();
		if (layers!=null)
			getLegendPanel().initializeFromLayers(layers);	
		setTitleMapLegendFont();
		*/
	}	
	
	public void setValeursSpec(double escala)
	{	
		List layers=legendFrame.getLayers();
		if (layers!=null)
			getLegendPanel().initializeFromLayers(layers, escala);	
		setTitleMapLegendFont();
	}
	
	/**
	 * 
	 */
	public void fixerDimensions()
	{
		// TODO Auto-generated method stub
	}
	/**
	 * 
	 */
	public void setLabelMapLegendFont()
	{

////		Police pol=legendFrame.getLabelMapLegendFont();
////		getLegendPanel().setSymbolSize(pol.getFontSize()*2,pol.getFontSize());


//		logger
//		.debug("setLabelMapLegendFont() - Cambio sin parametros: : pol = "
//		+ pol.getFontSize());


//		getLegendPanel().repaint();
	}
	/**
	 * This method initializes legendPanel	
	 * 	
	 * @return com.geopista.ui.legend.LegendPanel	
	 */    
	public LegendPanel getLegendPanel() {
		if (legendPanel == null) {
			legendPanel = new LegendPanel();
			legendPanel.setPreferredSize(new java.awt.Dimension(4000,4000));  // Generated
		}
		return legendPanel;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setPreferredSize(new java.awt.Dimension(2,2));  // Generated
		}
		return jPanel;
	}
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setPreferredSize(new java.awt.Dimension(2,2));  // Generated
		}
		return jPanel1;
	}
	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setPreferredSize(new java.awt.Dimension(2,2));  // Generated
		}
		return jPanel2;
	}
	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			FlowLayout flowLayout10 = new FlowLayout();
			jPanel3 = new JPanel();
			jPanel3.setLayout(flowLayout10);  // Generated
			jPanel3.setOpaque(false);  // Generated
			jPanel3.setPreferredSize(new java.awt.Dimension(2,2));  // Generated
			flowLayout10.setAlignment(java.awt.FlowLayout.LEFT);  // Generated
		}
		return jPanel3;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */  
	int w=8;
	/**
	 * @return
	 */
	public LegendPanel borrowLegendPanel()
	{
		this.remove(getLegendPanel());

		return getLegendPanel();
	}
	public void restoreLegendPanel()
	{
		this.add(getLegendPanel(),BorderLayout.NORTH);
	}
	
	public void cleanLegendPanel()
	{
		getLegendPanel().reset();		
	}
	
}
