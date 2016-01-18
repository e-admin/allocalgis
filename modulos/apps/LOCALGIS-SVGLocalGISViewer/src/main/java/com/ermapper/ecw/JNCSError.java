/**
 * JNCSError.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.ermapper.ecw;

/**
 * Representación de errores como cadenas de texto  
 */
public class JNCSError{

	public static String getError(int e){
		
		switch(e){
			case 0: return new String("No error");												/* NCS_SUCCESS											*/
			/* NCS Raster Errors */
			case 1: return new String("Queue node creation failed");							/* NCS_QUEUE_NODE_CREATE_FAILED							*/
			case 2: return new String("Could not open file");						/* NCS_FILE_OPEN_FAILED									*/
			case 3: return new String("The Image Web Server's licensed file limit has been reached ");							/* NCS_FILE_LIMIT_REACHED (license name eg office) */
			case 4: return new String("The requested file is larger than is permitted by the license on this Image Web Server ");/* NCS_FILE_SIZE_LIMIT_REACHED (license name eg office)	*/
			case 5: return new String("Not enough memory for new file");															/* NCS_FILE_NO_MEMORY				*/
			case 6: return new String("The Image Web Server's licensed client limit has been reached ");							/* NCS_CLIENT_LIMIT_REACHED	(license name eg enterprise) */
			case 7: return new String("Detected duplicate open from net layer");				/* NCS_DUPLICATE_OPEN									*/
			case 8: return new String("Packet request type not yet implemented");			/* NCS_PACKET_REQUEST_NYI (packet type num)				*/
			case 9: return new String("Packet type is illegal");							/* NCS_PACKET_TYPE_ILLEGAL (packet type num)			*/
			case 10: return new String("Client closed while requests outstanding");				/* NCS_DESTROY_CLIENT_DANGLING_REQUESTS					*/
	
			/* NCS Network Errors */
			case 11: return new String("Client UID unknown");									/* NCS_UNKNOWN_CLIENT_UID								*/
			case 12: return new String("Could not create new client ");						/* NCS_COULDNT_CREATE_CLIENT (reason)					*/
			case 13: return new String("Could not resolve address of Image Web Server ");		/* NCS_NET_COULDNT_RESOLVE_HOST	(ip or hostname)		*/
			case 14: return new String("Could not connect to host ");							/* NCS_NET_COULDNT_CONNECT (reason)						*/
			case 15: return new String("Receive timeout");										/* NCS_NET_RECV_TIMEOUT									*/
			case 16: return new String("Error sending header ");								/* NCS_NET_HEADER_SEND_FAILURE (reason)					*/
			case 17: return new String("Error receiving header ");							/* NCS_NET_HEADER_RECV_FAILURE (reason)					*/
			case 18: return new String("Error sending packet");									/* NCS_NET_PACKET_SEND_FAILURE							*/
			case 19: return new String("Error receiving packet");								/* NCS_NET_PACKET_RECV_FAILURE							*/
			case 20: return new String("401 Unauthorised");										/* NCS_NET_401_UNAUTHORISED								*/
			case 21: return new String("403 Forbidden");										/* NCS_NET_403_FORBIDDEN								*/
			case 22: return new String("Is the host an Image Web Server?");						/* NCS_NET_404_NOT_FOUND								*/
			case 23: return new String("Your HTTP proxy requires authentication,\nthis is presently unsupported by the Image Web Server control");	/*	NCS_NET_407_PROXYAUTH */
			case 24: return new String("Unexpected HTTP response ");								/* NCS_NET_UNEXPECTED_RESPONSE (resonse # or string)*/
			case 25: return new String("Bad HTTP response ");									/* NCS_NET_BAD_RESPONSE	(resonse # or string)			*/
			case 26: return new String("Already connected");									/* NCS_NET_ALREADY_CONNECTED							*/
			case 27: return new String("The connection is invalid");							/* NCS_INVALID_CONNECTION								*/
			case 28: return new String("Windows sockets failure ");							/* NCS_WINSOCK_FAILURE (reason (GetLastError()) or wininet version)	*/
	
			/* NCS Symbol Errors */
			case 29: return new String("Symbology error");										/* NCS_SYMBOL_ERROR										*/
			case 30: return new String("Could not open database");								/* NCS_OPEN_DB_ERROR									*/
			case 31: return new String("Could not execute the requested query on database");	/* NCS_DB_QUERY_FAILED									*/
			case 32: return new String("SQL statement could not be executed");					/* NCS_DB_SQL_ERROR										*/
			case 33: return new String("Open symbol layer failed");								/* NCS_GET_LAYER_FAILED									*/
			case 34: return new String("The database is not open");								/* NCS_DB_NOT_OPEN										*/
			case 35: return new String("This type of quad tree is not supported");				/* NCS_QT_TYPE_UNSUPPORTED								*/
	
			/* Preference errors */
			case 36: return new String("Invalid local user key name specified ");				/* NCS_PREF_INVALID_USER_KEY (key name)					*/
			case 37: return new String("Invalid local machine key name specified ");			/* NCS_PREF_INVALID_MACHINE_KEY	(local machine key)		*/
			case 38: return new String("Failed to open registry key ");						/* NCS_REGKEY_OPENEX_FAILED	(key name)					*/
			case 39: return new String("Registry query failed ");								/* NCS_REGQUERY_VALUE_FAILED (reason)					*/
			case 40: return new String("Type mismatch in registry variable");					/* NCS_INVALID_REG_TYPE									*/
	
			/* Misc errors */
			case 41: return new String("Invalid arguments passed to function ");				/* NCS_INVALID_ARGUMENTS (function name)				*/
			case 42: return new String("ECW error ");											/* NCS_ECW_ERROR (reason)								*/
			case 43: return new String("Server error ");										/* NCS_SERVER_ERROR (reason)							*/
			case 44: return new String("Unknown error ");										/* NCS_UNKNOWN_ERROR (reason)							*/
			case 45: return new String("Extent conversion failed");								/* NCS_EXTENT_ERROR										*/
			case 46: return new String("Could not allocate enough memory ");					/* NCS_COULDNT_ALLOC_MEMORY	(what trying to malloc)	[12]*/
			case 47: return new String("An invalid parameter was used ");						/* NCS_INVALID_PARAMETER (reason)						*/
			
			/* Compress errors */
			case 48: return new String("Could not perform Read/Write on file ");				/* NCS_FILEIO_ERROR	(filename)							*/
			case 49: return new String("Could not open compression task ");					/* NCS_COULDNT_OPEN_COMPRESSION	(reason)				*/
			case 50: return new String("Could not perform compression ");						/* NCS_COULDNT_PERFORM_COMPRESSION	(reason)			*/
			case 51: return new String("Trying to generate too many output lines");				/* NCS_GENERATED_TOO_MANY_OUTPUT_LINES					*/
			case 52: return new String("User cancelled compression");							/* NCS_USER_CANCELLED_COMPRESSION						*/
			case 53: return new String("Could not read line from input image file");			/* NCS_COULDNT_READ_INPUT_LINE							*/
			case 54: return new String("Input image size exceeded for this version");			/* NCS_INPUT_SIZE_EXCEEDED								*/
	
			/* Decompression Errors */
			case 55: return new String("Specified image region is outside image area");			/* NCS_REGION_OUTSIDE_FILE								*/
			case 56: return new String("Supersampling not supported");							/* NCS_NO_SUPERSAMPLE									*/
			case 57: return new String("Specified image region has a zero width or height");	/* NCS_ZERO_SIZE										*/
			case 58: return new String("More bands specified than exist in this file ");/* NCS_TOO_MANY_BANDS	(bands passed); bands in file)	*/
			case 59: return new String("An invalid band number has been specified ");			/* NCS_INVALID_BAND_NR (bandnr)							*/
			
			/* New Compression Error */
			case 60: return new String("Input image size is too small to compress");			/* NCS_INPUT_SIZE_TOO_SMALL								*/
			/* NEW Network error */
			case 61: return new String("The ECWP client version is incompatible with this server "); /* NCS_INCOMPATIBLE_PROTOCOL_VERSION (clientver, server ver) */
			case 62: return new String("Windows Internet Client error ");						/* NCS_WININET_FAILURE	(reason)						*/
			case 63: return new String("Could not load wininet.dll ");						/* NCS_COULDNT_LOAD_WININET	(reason)					*/
	
			/* NCSFile && NCSRenderer class errors */
			case 64: return new String("Invalid SetView parameters or SetView not called.");	/* NCS_FILE_INVALID_SETVIEW								*/
			case 65: return new String("There is no open ECW file.");							/* NCS_FILE_NOT_OPEN									*/
	
			/* NEW JNI Java Errors */
			case 66: return new String("Class does not implement ECWProgressiveDisplay interface.");/* NCS_JNI_REFRESH_NOT_IMPLEMENTED */
	
			/* NEW Coordinate Errors */
			case 67: return new String("Incompatible coordinate systems");						/* NCS_INCOMPATIBLE_COORDINATE_SYSTEMS					*/
			case 68: return new String("Incompatible coordinate datum types");					/* NCS_INCOMPATIBLE_COORDINATE_DATUM					*/
			case 69: return new String("Incompatible coordinate projection types");				/* NCS_INCOMPATIBLE_COORDINATE_PROJECTION				*/
			case 70: return new String("Incompatible coordinate units types");					/* NCS_INCOMPATIBLE_COORDINATE_UNITS					*/
			case 71: return new String("Non-linear coordinate systems not supported");			/* NCS_COORDINATE_CANNOT_BE_TRANSFORMED					*/
			case 72: return new String("GDT Error  ");										/* NCS_GDT_ERROR (reason)								*/
			case 73: return new String("Zero length packet : ");								/* NCS_NET_PACKET_RECV_ZERO_LENGTH (reason)             */ /**[01]**/
			case 74: return new String("Must use Japanese version of the ECW SDK");				/* NCS_UNSUPPORTEDLANGUAGE								*/ /**[02]**/
			case 75: return new String("Lost of connection to server  ");					/* NCS_CONNECTION_LOST									*/ /**[03]**/
			case 76: return new String("NCSGdt coordinate conversion failed  ");				/* NCS_COORD_CONVERT_ERROR */
			case 77: return new String("Failed to open metabase  ");							/* NCS_METABASE_OPEN_FAILED*/				/**[04]**/
			case 78: return new String("Failed to get value from metabase  ");				/* NCS_METABASE_GET_FAILED*/				/**[04]**/
			case 79: return new String("Timeout sending header  ");							/* NCS_NET_HEADER_SEND_TIMEOUT*/			/**[05]**/
			case 80: return new String("Java JNI error  ");									/* NCS_JNI_ERROR */						/**[06]**/
			case 81: return new String("No data source passed");								/* NCS_DB_INVALID_NAME									*/
			case 82: return new String("Could not resolve address of Image Web Server Symbol Server Extension");		/* NCS_SYMBOL_COULDNT_RESOLVE_HOST		*/
			case 83: return new String("Invalid NCSError value!");							/* NCS_INVALID_ERROR_ENUM */			/**[08]**/
			/* NCSFileIO errors [10] */
			case 84: return new String("End Of File reached  ");									/* NCS_FILE_EOF					[10]	*/
			case 85: return new String("File not found ");										/* NCS_FILE_NOT_FOUND			[10]	*/
			case 86: return new String("File is invalid or corrupt  ");							/* NCS_FILE_INVALID				[10]	*/
			case 87: return new String("Attempted to read, write or seek past file limits  ");	/* NCS_FILE_SEEK_ERROR			[10]	*/
			case 88: return new String("Permissions not available to access file  ");			/* NCS_FILE_NO_PERMISSIONS		[10]	*/
			case 89: return new String("File open error ");										/* NCS_FILE_OPEN_ERROR			[10]	*/
			case 90: return new String("File close error  ");									/* NCS_FILE_CLOSE_ERROR			[10]	*/
			case 91: return new String("File IO error  ");										/* NCS_FILE_IO_ERROR			[10]	*/
			
			case 92: return new String("Illegal World Coordinates  ");							/* NCS_SET_EXTENTS_ERROR		[13]	*/
	
			case 93: return new String("Image projection doesn't match controlling layer  ");	/* NCS_FILE_PROJECTION_MISMATCH		[14]	*/
	
			/** 1.65 gdt errors [15]**/
			case 94: return new String("Unknown map projection ");							/*NCS_GDT_UNKNOWN_PROJECTION,*/
			case 95: return new String("Unknown datum ");									/*NCS_GDT_UNKNOWN_DATUM,*/
			case 96: return new String("User specified Geographic Projection Database data server failed while loading .  Please check your network connection and if the problem persists contact the website administrator.");			/*NCS_GDT_USER_SERVER_FAILED*/
			case 97: return new String("Remote Geographic Projection Database file downloading has been disable and no Geographic Projection Database data is locally available");	/*NCS_GDT_REMOTE_PATH_DISABLED*/
			case 98: return new String("Invalid transform mode ");							/*NCS_GDT_BAD_TRANSFORM_MODE,			[17]**/
	
			case 99: return new String("coordinate to be transformed is out of bounds ");	/*NCS_GDT_TRANSFORM_OUT_OF_BOUNDS*/
	
			case 100: return new String("Layer already exists with this name  ");				/*NCS_LAYER_DUPLICATE_LAYER_NAME*/	/**[17]**/
			case 101: return new String("Layer does not contain this parameter  ");			/*NCS_LAYER_INVALID_PARAMETER*/		/**[18]**/
	
			case 102: return new String("Failed to create pipe ");							/*NCS_PIPE_CREATE_FAILED*/ /**[19]**/
			/* Directory creation errors */
			case 103: return new String("Directory already exists  ");						/*[20] NCS_FILE_MKDIR_EXISTS, Directory already exists */
			case 104: return new String("The path was not found  ");							/*[20] NCS_FILE_MKDIR_PATH_NOT_FOUND, The path for the location of the new dir does not exist */
	
			case 105: return new String("The read was cancelled");
			case 106: return new String("Error reading georeferencing data from JPEG 2000 file "); /*[21] NCS_JP2_GEODATA_READ_ERROR*/
			case 107: return new String("Error writing georeferencing data to JPEG 2000 file"); /*[21] NCS_JP2_GEODATA_READ_ERROR*/
			case 108: return new String("JPEG 2000 file is not or should not be georeferenced"); /*[21] NCS_JP2_GEODATA_NOT_GEOREFERENCED*/
	
			// Insert new errors before here!
			case 109: return new String("Max NCSError enum value!");							/* NCS_MAX_ERROR_NUMBER */				/**[08]**/
			
		}
		return new String("");
	}
	
}