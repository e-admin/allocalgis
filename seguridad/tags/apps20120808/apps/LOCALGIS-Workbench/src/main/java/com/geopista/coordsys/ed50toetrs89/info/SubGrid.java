package com.geopista.coordsys.ed50toetrs89.info;

import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * This class is used to read a subgrid from a grid file.
 * 
 * @author javieraragon
 */
public class SubGrid {

//	private Logger log4j = Logger.getLogger(this.getClass().getName());

	private String subName;
	private String parent;
	private String created;
	private String update;
	private Double sLat;
	private double nLat;
	private double eLong;
	private double wLong;
	private double latInc;
	private double longInc;
	private long gsCount;
	private long posicion;
	
	private Node[] puntos;
	private double latA;
	private double lonA;
	
	/**
	 * Creates an empty Subgrid.
	 */
	public SubGrid() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates a SubGrid from the specified SubGrid name, parent file name, created date, updated date, south latitude, north latitude, 
	 * east longitude, west longitude, latitude increase, longitude increase, subgrid's nodes count. 
	 * @param subName the sub grid name. 
	 * @param parent the grid name if it exists.
	 * @param created the date when the grid file was created.
	 * @param update the date when the grid file was updated for the last time.
	 * @param lat1 the south (lower) latitude of the grid.
	 * @param lat2 the north (upper) latitude of the grid.
	 * @param long1 the east (more eastern) longitude of the grid.
	 * @param long2 the west (more western) longitude of the grid.
	 * @param latInc the latitude increase of the grid.
	 * @param longInc the longitude increase of the grid.
	 * @param gsCount number of nodes in the grid.
	 */
	public SubGrid(String subName, String parent, String created,
			String update, Double lat1, double lat2, double long1, double long2,
			double latInc, double longInc, long gsCount) {
		super();
		this.subName = subName;
		this.parent = parent;
		this.created = created;
		this.update = update;
		sLat = lat1;
		nLat = lat2;
		eLong = long1;
		wLong = long2;
		this.latInc = latInc;
		this.longInc = longInc;
		this.gsCount = gsCount;
	}


	
	/**
	 * Gets the position in bytes to read in to the grid file.
	 * @return the position file pointer in bytes.
	 */
	public long getPosicion() {
		return posicion;
	}

	
	
	/**
	 * Sets the position in bytes to read into the grid file. 
	 * @param l the position in bytes to read in to the grid file.
	 */
	public void setPosicion(long l) {
		this.posicion = l;
	}

	/**
	 * Sets the start latitude of the node to make the conversion.
	 * @param lata latitude of the node.
	 */
	public void setLatA(double lata) {
		this.latA=lata;		
	}


	/**
	 * Sets the start longitude of the node to make the conversion.
	 * @param lona longitude of the node.
	 */
	public void setLonA(double lona) {
		this.lonA=lona;
		
	}


	/**
	 * Gets the start latitude of the node to make the conversion 
	 * @return the start latitude of the node.
	 */
	public double getLatA() {
		return latA;
	}


	/**
	 * Gets the start longitude of the node to make the conversion.
	 * @return the start longitude of the node.
	 */
	public double getLonA() {
		return lonA;
	}


	
	/**
	 * Gets the 4 nodes with the error to make the conversion.
	 * @return 4 nodes with the errors. 
	 */
	public Node[] getNodes() {
		return puntos;
	}


	/**
	 * Sets the 4 nodes with the error to make the conversion.
	 * @param nodes 4 nodes to make the conversion.
	 */
	public void setNodes(Node[] nodes) {
		this.puntos = nodes;
	}

	
	
	/**
	 * Gets the name of the sub grid.
	 * @return name of the sub grid.
	 */
	public String getSubName() {
		return subName;
	}


	/**
	 * Sets the name of the sub grid.
	 * @param subName sub grid's name.
	 */
	public void setSubName(String subName) {
		this.subName = subName;
	}


	/**
	 * Gets the parent grid name.
	 * @return parent grid's name
	 */
	public String getParent() {
		return parent;
	}


	/**
	 * Sets the parent grid name
	 * @param parent grid's name.
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}


	/**
	 * Gets the date when the grid file was created.
	 * @return grid file creation date.
	 */
	public String getCreated() {
		return created;
	}


	/**
	 * Sets the date when the grid file was created.
	 * @param created grid file creation date.
	 */
	public void setCreated(String created) {
		this.created = created;
	}


	/**
	 * Gets the date when the grid file was updated for the last time.
	 * @return grid file updated date.
	 */
	public String getUpdate() {
		return update;
	}


	/**
	 * Sets the date when the grid file was updated for the last time.
	 * @param update grid file updated date.
	 */
	public void setUpdate(String update) {
		this.update = update;
	}


	/**
	 * Gets the south (lower) latitude of the sub grid.
	 * @return sub grid's south latitude.
	 */
	public Double getSLat() {
		return sLat;
	}


	/**
	 * Sets the south (lower) latitude of the sub grid.
	 * @param lat sub grid's south latitude.
	 */
	public void setSLat(Double lat) {
		sLat = lat;
	}


	/**
	 * Gets the north (upper) latitude of the sub grid.
	 * @return sub grid's north latitude.
	 */
	public double getNLat() {
		return nLat;
	}

	/**
	 * Sets the north (upper) latitude of the sub grid.
	 * @param lat sub grid's north latitude.
	 */
	public void setNLat(double lat) {
		nLat = lat;
	}


	/**
	 * Gets the east (more easter) longitude of the sub grid.
	 * @return sub grid's east longitude.
	 */
	public double getELong() {
		return eLong;
	}


	/**
	 * Sets the east (more easter) longitude of the sub grid.
	 * @param long1 sub grid's east longitude.
	 */
	public void setELong(double long1) {
		eLong = long1;
	}

	/**
	 * Gets the west (more western) longitude of the sub grid.
	 * @return sub grid's west longitude.
	 */
	public double getWLong() {
		return wLong;
	}

	/**
	 * Sets the east (more western) longitude of the sub grid.
	 * @param long1 sub grid's west longitude.
	 */
	public void setWLong(double long1) {
		wLong = long1;
	}


	/**
	 * Gets the latitude increase for the nodes in the sub grid.
	 * @return latitude increase.
	 */
	public double getLatInc() {
		return latInc;
	}


	/**
	 * Sets the latitude increase for the nodes in the sub grid.
	 * @param latInc latitude increase.
	 */
	public void setLatInc(double latInc) {
		this.latInc = latInc;
	}

	/**
	 * Gets the longitude increase for the nodes in the sub grid.
	 * @return longitude increase.
	 */
	public double getLongInc() {
		return longInc;
	}

	/**
	 * Sets the longitude increase for the nodes in the sub grid.
	 * @param latInc longitude increase.
	 */
	public void setLongInc(double longInc) {
		this.longInc = longInc;
	}


	/**
	 * Gets the number of nodes in the sub grid.
	 * @return number of nodes.
	 */
	public long getGsCount() {
		return gsCount;
	}


	/**
	 * Sets the number of nodes in the sub grid.
	 * @param gsCount number of nodes.
	 */
	public void setGsCount(long gsCount) {
		this.gsCount = gsCount;
	}


	/**
	 * Reads the sub grid header information from a file.
	 * @param buf {@link RandomAccessFile} to read the sub grid.
	 */
	public void readSubGrid(RandomAccessFile buf){

		try {

			while(true){
				String r = UtilsToReadGridFile.readId(buf);
				this.subName = String.valueOf(UtilsToReadGridFile.readChain(buf));
				if(r.equals("SUB_NAME")){
					break;
				}
			}

			UtilsToReadGridFile.readId(buf);
			this.parent = String.valueOf(UtilsToReadGridFile.readChain(buf));

			UtilsToReadGridFile.readId(buf);
			this.created = String.valueOf(UtilsToReadGridFile.readChain(buf));

			UtilsToReadGridFile.readId(buf);
			this.update = String.valueOf(UtilsToReadGridFile.readChain(buf));

			UtilsToReadGridFile.readId(buf);
			this.sLat = UtilsToReadGridFile.readDouble(buf);

			UtilsToReadGridFile.readId(buf);
			this.nLat = UtilsToReadGridFile.readDouble(buf);

			UtilsToReadGridFile.readId(buf);
			this.eLong= UtilsToReadGridFile.readDouble(buf);

			UtilsToReadGridFile.readId(buf);
			this.wLong = UtilsToReadGridFile.readDouble(buf);

	
			UtilsToReadGridFile.readId(buf);
			this.latInc = UtilsToReadGridFile.readDouble(buf);


			UtilsToReadGridFile.readId(buf);
			this.longInc = UtilsToReadGridFile.readDouble(buf);

			UtilsToReadGridFile.readId(buf);
			this.gsCount = UtilsToReadGridFile.readLong(buf);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * pass to string all the sub grid's information.
	 */
	public String toString(){
		String result = "";

		result = result + "SubName  :" +  this.subName + "\n";
		result = result + "Parent   :" +  this.parent + "\n";
		result = result + "Created  :" +  this.created + "\n";
		result = result + "Updated  :" +  this.update + "\n";
		result = result + "S_Lat    :" +  this.sLat + "\n";
		result = result + "N_Lat    :" +  this.nLat + "\n";
		result = result + "E_Long   :" +  this.eLong + "\n";
		result = result + "W_Long   :" +  this.wLong + "\n";
		result = result + "Lat_inc  :" +  this.latInc + "\n";
		result = result + "Long_inc :" +  this.longInc + "\n";
		result = result + "GS_Count :" +  this.gsCount + "\n";


		return result;
	}


	/**
	 * Reads the nodes for make the transformation, using the posicion of this class.
	 * @param bufin file to read the nodes
	 * @param p nodes to store the read data
	 * @param lonlat longitude and latitude to read nodes.
	 */
	public void readNodes (RandomAccessFile bufin, float p[], LonLat lonlat){
		
		
		puntos = new Node[4];
		byte[] readed = new byte[4];

	    
		for (int ij = 1; ij <= 4; ij++){
			try {
				bufin.seek((long) (this.posicion + (p[ij-1] - 1) * 16) - 4);
				
				bufin.read(readed);
		
				this.puntos[ij-1] = new Node();
				for (int j=0; j<4; j++){
					try {
						bufin.read(readed);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					float a = UtilsToReadGridFile.byteArrayToFloat(readed, 0);

					if (a==-1){
						a = 0;
					}

					if (j==0){
						this.puntos[ij-1].setIlat(a);
					} else if (j==1){
						this.puntos[ij-1].setIlon(a);
					} else if (j==2){
						this.puntos[ij-1].setPlat(a);
					} else if (j==3){
						this.puntos[ij-1].setPlon(a);
					}
				}
	
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}


	/**
	 * See if the coordinate given as a parameter is in the dub grid.
	 * @param lon longitude of the coordinate.
	 * @param lat latitude of the coordinate.
	 * @return true if the coordinate matches with the sub grid and false in any other case.
	 */
	public boolean isInside(Double lon, Double lat) {
		return (lon<=eLong && lon>=wLong && lat<=nLat && lat>=sLat)?true:false;
	}



}
