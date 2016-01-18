/*
 * 
 * Created on 29-mar-2005 by juacas
 *
 * 
 */
package com.geopista.bean;
import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import com.geopista.ui.images.IconLoader;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class GEOPISTABeanBeanInfo extends SimpleBeanInfo
{

	private static boolean	BEANINFOUSED=true;
	/**
	 * 
	 */
	public BeanDescriptor getBeanDescriptor()
	{
	BeanDescriptor bd= new BeanDescriptor(GEOPISTABean.class);
	bd.setDisplayName("GEOPISTA ActiveX bridge");
	return bd;
	}
	public MethodDescriptor[] getMethodDescriptors()
	{
	if (!GEOPISTABeanBeanInfo.BEANINFOUSED)
		return super.getMethodDescriptors();
	
	try
		{
		MethodDescriptor[] md= new MethodDescriptor[]
					{
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("createTestConfig",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("edit",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("checkGeometry",new Class[]{String.class})),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("reset",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("extractMap",new Class[]{String.class, Double.class, Double.class, Double.class, Double.class, String.class})),				
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("syncMap",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("modifiedFeature",new Class[]{String.class})),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("removeFeature",new Class[]{String.class})),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("getAttribute",new Class[]{String.class})),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("getErrorMessages",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("getFeature",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("getGeometry",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("getLayers",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("getMap",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("getMapFormat",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("getMapName",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("getMapPath",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("getSchema",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("getWKTGeometry",new Class[0])),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("setAttribute",new Class[]{String.class,Object.class})), 
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("setAttributeAsString",new Class[]{String.class,String.class})),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("modifiedFeature",new Class[]{String.class})),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("newFeature",new Class[]{String.class})),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("setGeometry",new Class[]{String.class})),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("setLayer",new Class[]{String.class})),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("setSchema",new Class[]{String.class})),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("removeFeature",new Class[]{String.class})),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("getAttribute",new Class[]{String.class})),
					new MethodDescriptor(GEOPISTABean.class.getDeclaredMethod("checkFeature", new Class[]{Boolean.class}))
					
					
					
					};
		return md;
		}
	catch (SecurityException e)
		{
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	catch (NoSuchMethodException e)
		{
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	System.out.println("Informando de métodos por defecto.");
	return super.getMethodDescriptors();
	}
	public PropertyDescriptor[] getPropertyDescriptors()
	{
	if (!GEOPISTABeanBeanInfo.BEANINFOUSED)
		return super.getPropertyDescriptors();
	
	
//	try
//		{
		PropertyDescriptor[] result=null;
		
//			result = new PropertyDescriptor[]{
//					 new PropertyDescriptor("NotificationAreaVisible",null,GEOPISTABean.class.getDeclaredMethod("setNotificationAreaVisible",new Class[]{Boolean.class})),
//					 new PropertyDescriptor("map",GEOPISTABean.class.getDeclaredMethod("getMap",new Class[0]),null),
//					 new PropertyDescriptor("mapName",GEOPISTABean.class.getDeclaredMethod("getMapName",new Class[0]),null),
//					 new PropertyDescriptor("mapPath",GEOPISTABean.class.getDeclaredMethod("getMapPath",new Class[0]),null),
//					 new PropertyDescriptor("geometry",GEOPISTABean.class.getDeclaredMethod("getGeometry",new Class[0]),null),
//					 new PropertyDescriptor("mapFormat",GEOPISTABean.class.getDeclaredMethod("getMapFormat",new Class[0]),null),
//					 new PropertyDescriptor("mapPath",GEOPISTABean.class.getDeclaredMethod("getMapPath",new Class[0]),null)
//		};
//	return result;
//		}
//	catch (IntrospectionException e)
//		{
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		}
//	catch (SecurityException e)
//		{
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		}
//	catch (NoSuchMethodException e1)
//		{
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//		}

	return result;
	

	}
//	 public Image loadImage(String imgUrl) {
//     try {
//         super.loadImage(imgUrl);
//         URL url = getClass().getClassLoader().getResource(imgUrl);
//         if(url != null)
//             return new ImageIcon(url).getImage();
//     } catch (Exception e) {
//         e.printStackTrace();
//     }
//     return null;
// }
	public Image getIcon(int iconKind)
	{
	  if (iconKind == BeanInfo.ICON_MONO_16x16 ||
            iconKind == BeanInfo.ICON_COLOR_16x16 ) {
            java.awt.Image img = IconLoader.icon("app-icon.gif").getImage();
            return img;
        }
        if (iconKind == BeanInfo.ICON_MONO_32x32 ||
            iconKind == BeanInfo.ICON_COLOR_32x32 ) {
            java.awt.Image img = IconLoader.icon("app-icon.gif").getImage();
            return img;
        }
        return null;
	}
}
