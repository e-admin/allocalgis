package com.geopista.coordsys.ed50toetrs89.info;

import java.io.RandomAccessFile;

/**
 * This class is used to read a header file information from a grid file.
 * 
 * @author javieraragon
 */
public class Header {

	private long numOREC;
	private long numSREC;
	private long numFile;
	private char[] gsType = new char[8];
	private char[] version = new char[8];
	private char[] systemF = new char[8];
	private char[] systemT = new char[8];
	private double majorF;
	private double minorF;
	private double majorT;
	private double minorT;

	/**
	 * Constructor.
	 */
	public Header() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates a Header grid file from the specified number of header's records
	 * (numorec), the number of grid's records, the number of grids, the nodes
	 * value's units, the file version, the system start ellipsoid (systemf),
	 * the system finish ellipsoid (systemt), the bigger semiaxis start
	 * ellipsoid, the lower semiaxis start ellipsoid, the bigger semiaxis finish
	 * ellipsoid and the lower semiaxis finish ellipsoid.
	 * 
	 * @param numorec
	 *            number of header's records.
	 * @param numsrec
	 *            number of grid's records.
	 * @param numfile
	 *            number of grids.
	 * @param gstype
	 *            nodes value's units.
	 * @param version
	 *            file version .
	 * @param systemf
	 *            system start ellipsoid.
	 * @param systemt
	 *            system finish ellipsoid.
	 * @param majorf
	 *            bigger semiaxis start ellipsoid.
	 * @param minorf
	 *            lower semiaxis start ellipsoid.
	 * @param majort
	 *            bigger semiaxis finish ellipsoid.
	 * @param minort
	 *            lower semiaxis finish ellipsoid.
	 */
	public Header(long numorec, long numsrec, long numfile, char[] gstype,
			char[] version, char[] systemf, char[] systemt, double majorf,
			double minorf, double majort, double minort) {
		super();
		numOREC = numorec;
		numSREC = numsrec;
		numFile = numfile;
		gsType = gstype;
		this.version = version;
		systemF = systemf;
		systemT = systemt;
		majorF = majorf;
		minorF = minorf;
		majorT = majort;
		minorT = minort;
	}

	/**
	 * Gets the number of header's records.
	 * 
	 * @return the number of header's records.
	 */
	public long getNUMOREC() {
		return numOREC;
	}

	/**
	 * Sets the number of header's records.
	 * 
	 * @param num_orec
	 *            number of header's records.
	 */
	public void setNUMOREC(long num_orec) {
		numOREC = num_orec;
	}

	/**
	 * Gets the number of grid's records.
	 * 
	 * @return the number of grid's records.
	 */
	public long getNUMSREC() {
		return numSREC;
	}

	/**
	 * Sets the number of grid's records.
	 * 
	 * @param num_srec
	 *            number of header's records.
	 */
	public void setNUMSREC(long num_srec) {
		numSREC = num_srec;
	}

	/**
	 * Gets the number of grids on a grid file.
	 * 
	 * @return the number of grids.
	 */
	public long getNUMFILE() {
		return numFile;
	}

	/**
	 * Sets the number of grids on a grid file.
	 * 
	 * @param num_file
	 *            number of grids.
	 */
	public void setNUMFILE(long num_file) {
		numFile = num_file;
	}

	/**
	 * Gets the units of the nodes's values.
	 * 
	 * @return the nodes value's units.
	 */
	public char[] getGSTYPE() {
		return gsType;
	}

	/**
	 * Sets the units of the node's values.
	 * 
	 * @param gs_type
	 *            node's values units.
	 */
	public void setGSTYPE(char[] gs_type) {
		gsType = gs_type;
	}

	/**
	 * Gets the version number of the grid file.
	 * 
	 * @return the version number.
	 */
	public char[] getVERSION() {
		return version;
	}

	/**
	 * Sets the version number of the grid file.
	 * 
	 * @param version
	 *            version number.
	 */
	public void setVERSION(char[] version) {
		this.version = version;
	}

	/**
	 * Gets the system start ellipsoid.
	 * 
	 * @return the system start ellipsoid.
	 */
	public char[] getSYSTEMF() {
		return systemF;
	}

	/**
	 * Sets the system start ellipsoid.
	 * 
	 * @param system_f
	 *            system start ellipsoid.
	 */
	public void setSYSTEMF(char[] system_f) {
		systemF = system_f;
	}

	/**
	 * Gets the system finish ellipsoid.
	 * 
	 * @return the system finish ellipsoid.
	 */
	public char[] getSYSTEMT() {
		return systemT;
	}

	/**
	 * Sets the system finish ellipsoid.
	 * 
	 * @param system_t
	 *            system finish ellipsoid
	 */
	public void setSYSTEMT(char[] system_t) {
		systemT = system_t;
	}

	/**
	 * Gets the system start ellipsoid's bigger semiaxis.
	 * 
	 * @return the system start ellipsoid's bigger semiaxis.
	 */
	public double getMAJORF() {
		return majorF;
	}

	/**
	 * Sets the system start ellipsoid's bigger semiaxis.
	 * 
	 * @param major_f
	 *            system start ellipsoid's bigger semiaxis.
	 */
	public void setMAJORF(double major_f) {
		majorF = major_f;
	}

	/**
	 * Gets the system start ellipsoid's lower semiaxis.
	 * 
	 * @return the system start ellipsoid's lower semiaxis.
	 */
	public double getMINORF() {
		return minorF;
	}

	/**
	 * Sets the system start ellipsoid's lower semiaxis.
	 * 
	 * @param minor_f
	 *            system start ellipsoid's lower semiaxis.
	 */
	public void setMINORF(double minor_f) {
		minorF = minor_f;
	}

	/**
	 * Gets the system finish ellipsoid's bigger semiaxis.
	 * 
	 * @return the system finish ellipsoid's bigger semiaxis.
	 */
	public double getMAJORT() {
		return majorT;
	}

	/**
	 * Sets the system finish ellipsoid's bigger semiaxis.
	 * 
	 * @param major_t
	 *            system finish ellipsoid's bigger semiaxis.
	 */
	public void setMAJORT(double major_t) {
		majorT = major_t;
	}

	/**
	 * Gets the system finish ellipsoid's lower semiaxis.
	 * 
	 * @return the system finish ellipsoid's lower semiaxis.
	 */
	public double getMINORT() {
		return minorT;
	}

	/**
	 * Sets the system finish ellipsoid's lower semiaxis.
	 * 
	 * @param minort
	 *            system finish ellipsoid's lower semiaxis.
	 */
	public void setMINORT(double minort) {
		minorT = minort;
	}

	/**
	 * Reads the file header information from a file.
	 * 
	 * @param buf
	 *            {@link RandomAccessFile} to read the header.
	 */
	public void readHeader(RandomAccessFile bufferIn) {

		try {

			UtilsToReadGridFile.readId(bufferIn);
			this.numOREC = UtilsToReadGridFile.readIntegerBigEndian(bufferIn);

			UtilsToReadGridFile.readId(bufferIn);
			this.numSREC = UtilsToReadGridFile.readIntegerBigEndian(bufferIn);

			UtilsToReadGridFile.readId(bufferIn);
			this.numFile = UtilsToReadGridFile.readIntegerBigEndian(bufferIn);

			UtilsToReadGridFile.readId(bufferIn);
			this.gsType = UtilsToReadGridFile.readChain(bufferIn);

			UtilsToReadGridFile.readId(bufferIn);
			this.version = UtilsToReadGridFile.readChain(bufferIn);

			UtilsToReadGridFile.readId(bufferIn);
			this.systemF = UtilsToReadGridFile.readChain(bufferIn);

			UtilsToReadGridFile.readId(bufferIn);
			this.systemT = UtilsToReadGridFile.readChain(bufferIn);

			UtilsToReadGridFile.readId(bufferIn);
			this.majorF = UtilsToReadGridFile.readDouble(bufferIn);

			UtilsToReadGridFile.readId(bufferIn);
			this.minorF = UtilsToReadGridFile.readDouble(bufferIn);

			UtilsToReadGridFile.readId(bufferIn);
			this.majorT = UtilsToReadGridFile.readDouble(bufferIn);

			UtilsToReadGridFile.readId(bufferIn);
			this.minorT = UtilsToReadGridFile.readDouble(bufferIn);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		String a = "";

		a = a + "NUMOREC: " + this.getNUMOREC() + "\n";
		a = a + "NUMSREC: " + this.getNUMSREC() + "\n";
		a = a + "NUMFILE: " + this.getNUMFILE() + "\n";
		a = a + "GSType : " + String.valueOf(this.getGSTYPE()) + "\n";
		a = a + "version : " + String.valueOf(this.getVERSION()) + "\n";
		a = a + "Systemf: " + String.valueOf(this.getSYSTEMF()) + "\n";
		a = a + "Systemt: " + String.valueOf(this.getSYSTEMT()) + "\n";
		a = a + "Majorf : " + String.valueOf(this.getMAJORF()) + "\n";
		a = a + "Minorf : " + this.getMINORF() + "\n";
		a = a + "Majort : " + this.getMAJORT() + "\n";
		a = a + "minort : " + this.getMINORT() + "\n";

		return a;
	}

}
