package com.geopista.server.administradorCartografia;

public class ACUtil {

	public static void initSizeOf() {
		
		String mostrarSizeOf=System.getProperty("mostrarSizeOf");
		
		if ((mostrarSizeOf!=null) && (mostrarSizeOf.equals("true"))){
			net.sourceforge.sizeof.SizeOf.skipStaticField(true);
			net.sourceforge.sizeof.SizeOf.setMinSizeToLog(0); //0 turn off logging
		}
	}

	public static long deepSizeOf(Object object) {

		String mostrarSizeOf=System.getProperty("mostrarSizeOf");
		
		if ((mostrarSizeOf!=null) && (mostrarSizeOf.equals("true"))){
			return net.sourceforge.sizeof.SizeOf.deepSizeOf(object);
		}
		else
			return 0;
	}
}
