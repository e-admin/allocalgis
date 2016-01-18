package com.geopista.bean;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


import org.omg.CORBA.StringSeqHelper;

import com.geopista.feature.GeopistaSchema;

import junit.framework.TestCase;
/*
 * 
 * Created on 12-may-2005 by juacas
 *
 * 
 */

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class testBean extends TestCase
{
GeopistaBeanProxy bean;
private   String	TEST_SCHEMA;
	public static void main(String[] args)
	{
	junit.swingui.TestRunner.run(testBean.class);
//	testBean tst=new testBean("test");
//	try
//		{
//		tst.setUp();
//		tst.testSetAttribute();
//		}
//	catch (Exception e)
//		{
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		}
	
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
	super.setUp();
	InputStream is=testBean.class.getResourceAsStream("parcelas.sch");
	BufferedReader rd=new BufferedReader(new InputStreamReader(is));
	StringWriter wr=new StringWriter();
	int ch;
	while( (ch=rd.read()) != -1)
		{
		wr.write(ch);
		}
	TEST_SCHEMA=wr.toString();
	rd.close();
	wr.close();
	
	bean = new GeopistaBeanProxy();
	bean.reset();
	bean.selectMap();
	bean.setLayer("test");
	bean.setSchema(TEST_SCHEMA);
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
	super.tearDown();
	}

	/**
	 * Constructor for testBean.
	 * @param arg0
	 */
	public testBean(String arg0)
	{
	super(arg0);
	}

	public final void testSetLayer()
	{
	assertTrue(bean.setLayer("TESTLayer"));
	
	}

	public final void testSetAttribute()
	{
	
	Object val= new Double(32);
	
	assertTrue(bean.setAttribute("Superficie_Solar",val));
	Object expected=bean.getAttribute("Superficie_Solar");
	assertEquals("valor atributo",val, expected);
	String val2="32";
	assertTrue(bean.setAttribute("Superficie_Solar",val2));
	bean.setAttribute("Fecha_Alta","19/04/2005");
	bean.setAttribute("ID_Parcela",new Integer(23));
	Integer val3=(Integer)bean.getAttribute("ID_Parcela");
	assertEquals(23,val3.intValue());
	}

	public final void testGetAttribute()
	{
	testSetAttribute();
	}

	public final void testEdit()
	{
	
	
	bean.setAttribute("Fecha_Alta","19/04/2005");
	bean.edit();
	 Object val=bean.getAttribute("Fecha_Alta");
	Date fec=(Date)val;
	try
		{
		assertFalse(fec.equals(DateFormat.getDateInstance(DateFormat.SHORT).parse("19/04/2005")));
		}
	catch (ParseException e)
		{
		
		e.printStackTrace();
		assertTrue(false);
		}
		
	}

	public final void testReset()
	{
	//TODO Implement reset().
	}

	public final void testGetMapName()
	{
	//TODO Implement getMapName().
	}

	public final void testGetMapPath()
	{
	//TODO Implement getMapPath().
	}

	public final void testGetSchema()
	{
	
	GeopistaSchema sch=bean.getSchema();
	assertTrue("Modalidad_Reparto".equals(sch.getAttributeByColumn("Indicador de modalidad de reparto")));
	}

	public final void testExtractMap()
	{
	//TODO Implement extractMap().
	}

	public final void testGetMap()
	{
	//TODO Implement getMap().
	}

	public final void testGetFeature()
	{
	//TODO Implement getFeature().
	}

	public final void testRemoveFeature()
	{
	    assertTrue(bean.removeFeature("1234"));
	}

	public final void testModifiedFeature()
	{
	   
	    bean.modifiedFeature("12345");
	    
	}

	public final void testCheckFeature()
	{
	//TODO Implement checkFeature().
	}

	public final void testGetMapFormat()
	{
	//TODO Implement getMapFormat().
	}

	public final void testSyncMap()
	{
	//TODO Implement syncMap().
	}

	public final void testGetGeometry()
	{
	//TODO Implement getGeometry().
	}

	public final void testSetGeometry()
	{
	//TODO Implement setGeometry().
	}

	public final void testGetLayers()
	{
	//TODO Implement getLayers().
	}

	public final void testSelectMap()
	{
	//TODO Implement selectMap().
	}

	public final void testSetSchema()
	{
	bean.reset();
	bean.setLayer("testLayer2");
	assertTrue(bean.setSchema(TEST_SCHEMA));
	}

	public final void testNewFeature()
	{
	    assertFalse(bean.newFeature("NoInicializado"));
	}

	/*
	 * Class under test for boolean checkGeometry(Geometry)
	 */
	public final void testCheckGeometry()
	{
	    assertTrue(bean.checkGeometry("POINT(214 248)"));
	    assertTrue(bean.checkGeometry("LINESTRING(193 127,364 169)"));
	    assertTrue(bean.checkGeometry("POLYGON((302 255,302 280,335 280,335 255,302 255))"));
	}

	public final void testGetErrorMessages()
	{
	//TODO Implement getErrorMessages().
	}

}
