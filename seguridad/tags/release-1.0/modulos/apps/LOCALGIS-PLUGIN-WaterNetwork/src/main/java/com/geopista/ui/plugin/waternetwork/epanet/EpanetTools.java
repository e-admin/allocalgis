package com.geopista.ui.plugin.waternetwork.epanet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;

public class EpanetTools{

	private GeometryFactory factory = new GeometryFactory();
	
	//Genera el fichero compatible con Epanet.
	public void newEpanetFile(String path, ArrayList<Feature> features) throws IOException{		
		try{			
			File epanet = new File(path);			 
			PrintWriter pw = new PrintWriter(epanet);
			pw.println(epanetString(features));
			pw.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}		
	}
	
	//Genera un string que se mostrará en el frame de la herramiento con el contenido del fichero
	//compatible con Epanet.
	public String epanetString(ArrayList<Feature> features){
		ArrayList<Point> cs = addCoordinates(features);
		ArrayList<Point> js = addJunctions(idsEx(features),features);
		ArrayList<Feature> ps = addPipes(features);
		ArrayList<Feature> vs = addValves(features);
		ArrayList<Feature> rs = addReservoirs(features);
		ArrayList<Point> vr = addVertices(features);
		String epanet= "";
		epanet = epanet + "[TITLE]\n" + 
						  I18N.get("WaterNetworkPlugIn","Epanet.FileTitle")+ "\n\n" +
						  junctions(js) + "\n" +
						  reservoirs(rs) + "\n" +
						  tanks() + "\n" +
						  pipes(ps) + "\n" +
						  pump() + "\n" +
						  valves(vs) + "\n" +
						  tags() + "\n" +
						  demands() + "\n" +
						  status() + "\n" +
						  patterns() + "\n" +
						  curves() + "\n" +
						  controls() + "\n" +
						  rules() + "\n" +
						  energy() + "\n" +
						  emitters() + "\n" +
						  quality(cs) + "\n" +
						  sources() + "\n" +
						  reactions() + "\n" +
						  mixing() + "\n" +
						  times() + "\n" +
						  report() + "\n" +
						  options()+ "\n"+
						  coordinates(cs) + "\n" +
						  vertices(vr) + "\n" +
						  labels() + "\n" +
						  backdrop() + "\n" +
						  "[END]";		
		return epanet;
	}
	
	public ArrayList<Integer> reservoirsNode(ArrayList<Feature> features){
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		for(Feature feature : features){
			if(feature.getAttribute("tipo").equals("r"))
				nodes.add((Integer)feature.getAttribute("node1"));
		}
		return nodes;
	}
	

	//Extrae las uniones de los elementos de la red a representar en Epanet.
	public ArrayList<Point> addCoordinates(ArrayList<Feature> features){
		ArrayList<Point> points = new ArrayList<Point>();
		for(Feature feature : features){
			Coordinate[] coordinates = feature.getGeometry().getCoordinates();
			for(int i=0;i<coordinates.length;i++){
				if(i==0){
					Point p = factory.createPoint(coordinates[i]);
					p.setSRID((Integer) feature.getAttribute("node1"));
					points.add(p);				
				}
				if(i==coordinates.length-1){
					Point p = factory.createPoint(coordinates[i]);
					p.setSRID((Integer) feature.getAttribute("node2"));
					points.add(p);			
				}
			}
		}
		points = deleteDuplicatePoint(points);
		return points;
	}
	
	//Extrae las uniones de los elementos de la red a representar en Epanet.
	@SuppressWarnings("unchecked")
	public ArrayList<Point> addJunctions(ArrayList<Integer> ids, ArrayList<Feature> features){
		ArrayList<Point> points = new ArrayList<Point>();
		for(Feature feature : features){
			if(!ids.contains((Integer)feature.getAttribute("node1")) ){
				Coordinate[] coordinates = feature.getGeometry().getCoordinates();
				for(int i=0;i<coordinates.length;i++){
					if(i==0){
						Point p = factory.createPoint(coordinates[i]);
						p.setSRID((Integer) feature.getAttribute("node1"));
						points.add(p);
					}
					if(i==coordinates.length-1){
						Point p = factory.createPoint(coordinates[i]);
						p.setSRID((Integer) feature.getAttribute("node2"));
						points.add(p);
					}
				}
			}
		}
		points = deleteDuplicatePoint(points);
		ArrayList<Point> aux = (ArrayList<Point>) points.clone();
		for(Point p : points)
			if(reservoirsNode(features).contains(p.getSRID())) aux.remove(p);				
		return aux;
	}
	
	//
	public ArrayList<Integer> idsEx(ArrayList<Feature> features){
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(Feature feature : features)
			if(feature.getAttribute("tipo").equals("t"))
				ids.add((Integer) feature.getAttribute("node1"));
		return ids;
	}
	
	//Elimina los nodos duplicados.
	public ArrayList<Point> deleteDuplicatePoint(ArrayList<Point> points){		
		ArrayList<Point> auxPoints = new ArrayList<Point>();
		ArrayList<Integer> pointsSrid = new ArrayList<Integer>();
		ArrayList<Integer> auxPointsSrid = new ArrayList<Integer>();
		for(Point point : points)
			pointsSrid.add(point.getSRID());
		for(Integer pointSrid : pointsSrid)
			if(!auxPointsSrid.contains(pointSrid))auxPointsSrid.add(pointSrid);
		for(Integer aux : auxPointsSrid)
			for(Point point : points)
				if(aux==point.getSRID()){
					auxPoints.add(point);
					break;
				}
		return auxPoints;		
	}
	
	//Extrae las tuberias de la capa "red_abastecimiento".
	public ArrayList<Feature> addPipes(ArrayList<Feature> features){
		ArrayList<Feature> lines = new ArrayList<Feature>();
		for(Feature feature : features){
			if(feature.getAttribute("tipo").equals("p")||feature.getAttribute("tipo").equals("a") || feature.getAttribute("tipo").equals("h"))
				lines.add(feature);	
		}
		return lines;
	}
	
	//Extrae las valvulas de la capa "red_abastecimiento".
	public ArrayList<Feature> addValves(ArrayList<Feature> features){
		ArrayList<Feature> valves = new ArrayList<Feature>();
		for(Feature feature : features){
			if(feature.getAttribute("tipo").equals("v"))
				valves.add(feature);	
		}
		return valves;
	}
	
	//Extrae los embalses de la capa "red_abastecimiento".
	public ArrayList<Feature> addReservoirs(ArrayList<Feature> features){
		ArrayList<Feature> reservoirs = new ArrayList<Feature>();
		for(Feature feature : features){
			if(feature.getAttribute("tipo").equals("r"))
				reservoirs.add(feature);	
		}
		return reservoirs;
	}
	
	//
	public ArrayList<Point> addVertices(ArrayList<Feature> features){
		ArrayList<Point> points = new ArrayList<Point>();
		for(Feature feature : features){
			Coordinate[] coordinates = feature.getGeometry().getCoordinates();
			for(int i=0;i<coordinates.length;i++){
				if(i!=0 && i!=coordinates.length-1){
					Point p = factory.createPoint(coordinates[i]);
					int id = Integer.parseInt(feature.getAttribute("id").toString());
					p.setSRID(id);
					points.add(p);
				}
			}
		}
		return points;
	}

	/* ************************************************************************************************** *
	 * Los siguientes metodos rellenan el fichero compatible con Epanet con cada uno de los apartados que *
	 * lo configuraran para su correcta importacion en dicha plataforma 								  *
	 * ************************************************************************************************** */
	
	public String junctions(ArrayList<Point> js){
		String junctions = "[JUNCTIONS]\n";
		junctions = junctions + ";ID\t\tElev\t\tDemand\t\tPattern \n";
		for(Point j : js)
			junctions = junctions + " " + j.getSRID() + "\t\t 700 \t\t 150;\n";
		return junctions;
	}

	public String reservoirs(ArrayList<Feature> features){
		String reservoirs = "[RESERVOIRS]\n";
		reservoirs = reservoirs + ";ID\t\tHead\t\tPattern \n";
		for(Feature feature : features){
			reservoirs = reservoirs + " " + feature.getAttribute("node1")+"\t\t ; \n";
		}
		return reservoirs;
	}

	public String tanks(){
		String tanks = "[TANKS]\n";
		tanks = tanks + ";ID\t\tElevation\t\tInitLevel\t\tMinLevel\t\tMaxLevel\t\tDiameter\t\tMinVol\t\tVolCurve \n";		
		return tanks;
	}

	public String pipes(ArrayList<Feature> features){
		String pipes = "[PIPES]\n";
		pipes = pipes + ";ID\t\tNode1\t\tNode2\t\tLength\t\tDiameter\t\tRoughness\t\tMinorLoss\t\tStatus \n";
		for(Feature feature : features){
			double length = 0.0;
			int l = (int)(Double.valueOf(feature.getAttribute("length").toString()).doubleValue() * 10);
			length = l/10.00;
			if(length<=1.0)length=1.0;
			pipes = pipes + " " + feature.getAttribute("id")+"\t\t"+
								  feature.getAttribute("node1")+"\t\t"+
								  feature.getAttribute("node2")+"\t\t"+
								  length+"\t\t"+
								  feature.getAttribute("diameter")+"\t\t\t"+
								  feature.getAttribute("roughness")+"\t\t\t"+
								  feature.getAttribute("minorloss")+"\t\t\t"+
								  feature.getAttribute("status")+"\t; \n";
		}
		return pipes;
	}

	public String pump(){
		String pump = "[PUMP]\n";
		pump = pump + ";ID\t\tNode1\t\tNode2\t\tParameters \n";
		return pump;
	}

	public String valves(ArrayList<Feature> features){
		String valves = "[VALVES]\n";
		valves = valves + ";ID\t\tNode1\t\tNode2\t\tDiameter\t\tType\t\tSetting\t\tMinorLoss\t\tStatus \n";
		for(Feature feature : features)
			valves = valves + " " + feature.getAttribute("id") + "\t\t" +
								  	feature.getAttribute("node1") + "\t\t" +
								  	feature.getAttribute("node2") + "\t\t" +
								  	feature.getAttribute("diameter") + "\t\t\t" +
								  	feature.getAttribute("type") + "\t\t" +
								  	feature.getAttribute("setting") + "\t\t" +
								  	feature.getAttribute("minorloss") + "\t\t" +
									feature.getAttribute("status")+"\t; \n";
		return valves;
	}

	public String tags(){
		String tags = "[TAGS]\n";
		return tags;
	}

	public String demands(){
		String demands = "[DEMANDS]\n";
		demands = demands + ";Junction\t\tDemand\t\tPattern\t\tCategory \n";
		return demands;
	}

	public String status(){
		String status = "[STATUS]\n";
		status = status + ";ID\t\tStatus/Setting \n";
		return status;
	}

	public String patterns(){
		String patterns = "[PATTERNS]\n";
		patterns = patterns + ";ID\t\tMultipliers \n";
		patterns = patterns + "1 \t 1.0 \t 1.2 \t 1.4 \t 1.6 \t 1.4 \t 1.2 \n";
		patterns = patterns + "1 \t 1.0 \t 0.8 \t 0.6 \t 0.4 \t 0.6 \t 0.8 \n";
		return patterns;
	}

	public String curves(){
		String curves = "[CURVES]\n";
		curves = curves + ";ID\t\tX-Value\t\tY-Value \n";
		return curves;
	}

	public String controls(){
		String controls = "[CONTROLS]\n";
		return controls;
	}

	public String rules(){
		String rules = "[RULES]\n";
		return rules;
	}

	public String energy(){
		String energy = "[ENERGY]\n";
		energy = energy + "Global Efficiency \t 75 \n";
		energy = energy + "Global Price \t 0.0 \n";
		energy = energy + "Demand Charge \t 0.0 \n";
		return energy;
	}

	public String emitters(){
		String emitters = "[EMITTERS]\n";
		emitters = emitters + ";Junction\t\tCoefficient \n";
		return emitters;
	}

	public String quality(ArrayList<Point> cs){
		String quality = "[QUALITY]\n";
		quality = quality + ";Node\t\tInitQual \n";
		for(Point c : cs){
			quality = quality + " " + c.getSRID() + "\t\t 0.5 \t; \n" ;
		}
		return quality;
	}

	public String sources(){
		String sources = "[SOURCES]\n";
		sources = sources + ";Node\t\tType\t\tQuality\t\tPattern \n";
		return sources;
	}

	public String reactions(){
		String reactions = "[REACTIONS]\n";
		reactions = reactions + ";Type\t\tPipe/Tank\t\tCoefficient \n";
		return reactions;
	}

	public String mixing(){
		String mixing = "[MIXING]\n";
		mixing = mixing + ";Tank\t\tModel \n";
		return mixing;
	}

	public String times(){
		String times = "[TIMES]\n";
		times = times + "Duration \t 24:00 \n"; 
		times = times + "Hydraulic Timestep \t 1:00 \n"; 
		times = times + "Quality Timestep \t 0:05 \n"; 
		times = times + "Pattern Timestep \t 2:00 \n"; 
		times = times + "Pattern Start \t 0:00 \n"; 
		times = times + "Report Timestep \t 1:00 \n"; 
		times = times + "Report Start \t 0:00 \n"; 
		times = times + "Start ClockTime \t 12 am \n";
		times = times + "Statistic \t None \n";
		return times;
	}

	public String report(){
		String report = "[REPORT]\n";
		report = report + "Status \t No \n";
		report = report + "Summary \t No \n";
		report = report + "Page \t 0 \n";
		return report;
	}

	public String options(){
		String options = "[OPTIONS]\n";
		options = options + "Units \t GPM \n";
		options = options + "Headloss \t H-W\n";
		options = options + "Specific Gravity \t 1.0 \n";
		options = options + "Viscosity \t 1.0 \n";
		options = options + "Trials \t 40 \n";
		options = options + "Accuracy \t 0.001 \n";
		options = options + "CHECKFREQ \t 2 \n";
		options = options + "MAXCHECK \t 10 \n";
		options = options + "DAMPLIMIT \t 0 \n";
		options = options + "Unbalanced \t Continue 10 \n";
		options = options + "Pattern \t 1 \n";
		options = options + "Demand Multiplier \t 1.0 \n";
		options = options + "Emitter Exponent \t 0.5 \n";
		options = options + "Quality \t Chlorine mg/L \n";
		options = options + "Diffusivity \t 1.0 \n";
		options = options + "Tolerance \t 0.01 \n";
		return options;
	}

	public String coordinates(ArrayList<Point> cs){
		String coordinates = "[COORDINATES]\n";
		coordinates = coordinates + ";Node\t\tX-Coord\t\tY-Coord \n";
		for(Point c : cs){
			double cX,cY = 0.0;
			int numerox = (int)(c.getCoordinate().x * 10);
			int numeroy = (int)(c.getCoordinate().y * 10);
			cX = numerox/10.0; 
			cY = numeroy/10.0;
			coordinates = coordinates + " " + c.getSRID() + "\t\t" + cX + "\t\t" + cY + "\t; \n";			
		}
		return coordinates;
	}

	public String vertices(ArrayList<Point> vr){
		String vertices = "[VERTICES]\n";
		vertices = vertices + ";Link\t\tX-Coord\t\tY-Coord \n";
		for(Point v : vr){
			double cX,cY = 0.0;
			int numerox = (int)(v.getCoordinate().x * 10);
			int numeroy = (int)(v.getCoordinate().y * 10);
			cX = numerox/10.0; 
			cY = numeroy/10.0;
			vertices = vertices + " " + v.getSRID() + "\t\t" + cX + "\t\t" + cY + "\t; \n";
		}
			
		return vertices;
	}

	public String labels(){
		String labels = "[LABELS]\n";
		labels = labels + ";X-Coord\t\tY-Coord\t\tLabel & Anchor Node \n";
		return labels;
	}

	public String backdrop(){
		String backdrop = "[BACKDROP]\n";
		return backdrop;
	}
}