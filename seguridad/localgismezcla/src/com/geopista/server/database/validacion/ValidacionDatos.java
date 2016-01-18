package com.geopista.server.database.validacion;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.geopista.protocol.control.Sesion;
import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.CPoolDatabase;
import com.localgis.web.core.coordsys.CoordinateSystem;

public class ValidacionDatos {
	
	private static Connection conn;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	
	public static void validacion(StringBuffer str, Sesion userSesion, String fase, 
			Integer idMunicipio,int cuadro, ArrayList lstValCuadros){
		
		try {
			
			Connection conn = CPoolDatabase.getConnection();
			if (conn != null) {
				if(cuadro==0){
//					System.out.println((new StringBuilder("0--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_general.validacion(conn, str, lstValCuadros);
				}
	            else if(cuadro==1){
//	                System.out.println((new StringBuilder("A--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadroA.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==2){
//	                System.out.println((new StringBuilder("C--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadroC.validacion(conn, str, idMunicipio, lstValCuadros);
	            }
	            else if(cuadro==3){
//	                System.out.println((new StringBuilder("G--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadroG.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==4){
//	                System.out.println((new StringBuilder("H--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadroH.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==5){
//	                System.out.println((new StringBuilder("I--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadroI.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==6){
//	                System.out.println((new StringBuilder("J--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadroJ.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==7){
//	                System.out.println((new StringBuilder("K--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadroK.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==8){
//	                System.out.println((new StringBuilder("L--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadroL.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==9){
//	                System.out.println((new StringBuilder("M--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadroM.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==10){
//	                System.out.println((new StringBuilder("N--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadroN.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==11){
//	                System.out.println((new StringBuilder("O--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadroO.validacion(conn, str, lstValCuadros);
				}
				else if(cuadro==12){
//	                System.out.println((new StringBuilder("1--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro01.validacion(conn, str, lstValCuadros);
				}
	            else if(cuadro==13){
//	                System.out.println((new StringBuilder("2--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro02.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==14){
//	                System.out.println((new StringBuilder("3--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro03.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==15){
//	                System.out.println((new StringBuilder("4--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro04.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==16){
//	                System.out.println((new StringBuilder("5--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro05.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==17){
//	                System.out.println((new StringBuilder("6--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro06.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==18){
//	                System.out.println((new StringBuilder("7--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro07.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==19){
//	                System.out.println((new StringBuilder("8--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro08.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==20){
//	                System.out.println((new StringBuilder("9--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro09.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==21){
//	                System.out.println((new StringBuilder("10--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro10.validacion(conn, str, lstValCuadros);
				}
				else if(cuadro==22){ 
//	                System.out.println((new StringBuilder("11--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro11.validacion(conn, str, lstValCuadros);
				}
	            else if(cuadro==23){
//	                System.out.println((new StringBuilder("12--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro12.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==24){
//	                System.out.println((new StringBuilder("13--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro13.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==25){
//	                System.out.println((new StringBuilder("14--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro14.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==26){
//	                System.out.println((new StringBuilder("15--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro15.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==27){
//	                System.out.println((new StringBuilder("16--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro16.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==28){
//	                System.out.println((new StringBuilder("17--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro17.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==29){
//	                System.out.println((new StringBuilder("18--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro18.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==30){
//	                System.out.println((new StringBuilder("19--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro19.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==31){
//	                System.out.println((new StringBuilder("20--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro20.validacion(conn, str, lstValCuadros);
				}
				else if(cuadro==32){
//	                System.out.println((new StringBuilder("21--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro21.validacion(conn, str, (new Integer(fase)).intValue(), lstValCuadros);
				}
	            else if(cuadro==33){
//	                System.out.println((new StringBuilder("22--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro22.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==34){
//	                System.out.println((new StringBuilder("23--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro23.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==35){
//	                System.out.println((new StringBuilder("24--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro24.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==36){
//	                System.out.println((new StringBuilder("25--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro25.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==37){
//	                System.out.println((new StringBuilder("26--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro26.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==38){
//	                System.out.println((new StringBuilder("27--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro27.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==39){
//	                System.out.println((new StringBuilder("28--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro28.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==40){
//	                System.out.println((new StringBuilder("29--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro29.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==41){
//	                System.out.println((new StringBuilder("30--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro30.validacion(conn, str, lstValCuadros);
				}
				else if(cuadro==42){
//	                System.out.println((new StringBuilder("31--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro31.validacion(conn, str, lstValCuadros);
				}
	            else if(cuadro==43){
//	                System.out.println((new StringBuilder("32--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro32.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==44){
//	                System.out.println((new StringBuilder("33--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro33.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==45){
//	                System.out.println((new StringBuilder("34--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro34.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==46){
//	                System.out.println((new StringBuilder("35--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro35.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==47){
//	                System.out.println((new StringBuilder("36--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro36.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==48){
//	                System.out.println((new StringBuilder("37--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro37.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==49){
//	                System.out.println((new StringBuilder("38--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro38.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==50){
//	                System.out.println((new StringBuilder("39--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro39.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==51){
//	                System.out.println((new StringBuilder("40--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro40.validacion(conn, str, lstValCuadros);
				}
				else if(cuadro==52){
//	                System.out.println((new StringBuilder("41--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro41.validacion(conn, str, lstValCuadros);
				}
	            else if(cuadro==53){
//	                System.out.println((new StringBuilder("42--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro42.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==54){
//	                System.out.println((new StringBuilder("43--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro43.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==55){
//	                System.out.println((new StringBuilder("44--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro44.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==56){
//	                System.out.println((new StringBuilder("45--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro45.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==57){
//	                System.out.println((new StringBuilder("46--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro46.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==58){
//	                System.out.println((new StringBuilder("47--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro47.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==59){
//	                System.out.println((new StringBuilder("48--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro48.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==60){
//	                System.out.println((new StringBuilder("49--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro49.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==61){
//	                System.out.println((new StringBuilder("51--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro51.validacion(conn, str, lstValCuadros);
				}
				else if(cuadro==62){
//	                System.out.println((new StringBuilder("52--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro52.validacion(conn, str, lstValCuadros);
				}
	            else if(cuadro==63){
//	                System.out.println((new StringBuilder("53--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro53.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==64){
//	                System.out.println((new StringBuilder("54--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro54.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==65){
//	                System.out.println((new StringBuilder("55--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro55.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==66){
//	                System.out.println((new StringBuilder("56--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro56.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==67){
//	                System.out.println((new StringBuilder("57--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro57.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==68){
//	                System.out.println((new StringBuilder("58--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro58.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==69){
//	                System.out.println((new StringBuilder("59--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro59.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==70){
//	                System.out.println((new StringBuilder("60--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro60.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==71){
//	                System.out.println((new StringBuilder("61--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro61.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==72){
//	                System.out.println((new StringBuilder("62--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro62.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==73){
//	                System.out.println((new StringBuilder("63--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_cuadro63.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==74){
//	                System.out.println((new StringBuilder("65--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());              
					Validacion_cuadro65.validacion(conn, str, lstValCuadros);
	            }
	            else if(cuadro==75){
//					System.out.println((new StringBuilder("66--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	            	Validacion_cuadro66.validacion(conn, str, (new Integer(fase)).intValue(), lstValCuadros);
	            }
	            else if(cuadro==76){
//					System.out.println((new StringBuilder("Listados--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	            	Validacion_listados.validacion(conn, str);
	            }
	            else if(cuadro==77){
//	                System.out.println((new StringBuilder("Munc_enc--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                Validacion_mun_enc_dis.validacion(conn, str);
	            }
	            else if(cuadro==78){
//	                System.out.println((new StringBuilder("Integridad_referencial--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
	                IntegridadReferencial.validacion(conn, str,fase);
	            }
//	             System.out.println((new StringBuilder("fin--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
			}
		} catch (Exception ex) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);

		}
		finally
		{
			COperacionesEIEL.safeClose(rs, ps, conn);
		}
		
		
		
	}

}
