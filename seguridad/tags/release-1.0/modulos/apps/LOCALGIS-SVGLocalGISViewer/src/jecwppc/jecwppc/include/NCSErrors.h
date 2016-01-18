/********************************************************
** Copyright 1999 Earth Resource Mapping Pty Ltd.
** This document contains unpublished source code of
** Earth Resource Mapping Pty Ltd. This notice does
** not indicate any intention to publish the source
** code contained herein.
**
** FILE:   	NCSError.h
** CREATED:	Thu Feb 25 09:19:00 WST 1999
** AUTHOR: 	Simon Cope
** PURPOSE:	NCS Error handling enum/routines.
** EDITS:
   [01] ny	26Apr00 Added 	NCS_NET_PACKET_RECV_ZERO_LENGTH error 
   [02] sjc 30May00 Merge Mac SDK port in to main source stream
   [03] ny  12May00 Added lost of connection error
   [04] ny  07Jul00 Added Metabase query errors
   [05] ny  19Jul00 Added Timeout errors
   [06] mjs 25Aug00 Added errors for Java JNI functionality
   [07] jmp 31Aug00 Moved extra symbol server errors to end of enum
   [08] sjc 12Oct00 Added NCS_MAX_ERROR_NUMBER to enable range check
   [10]  ny	30Oct00	Merge WinCE/Palm SDK
   [11] rar	19Jan01 Mac port changes
   [12] sjc 06Mar01 Strings were swapped
   [13] jmp 17May01 Added SetExtents error
   [14] jmp 25Sep01 Layer errors
   [15] ajd 22Nov01 added 1.65 gdt errors
   [16] ajd 15Jan02 added NCS_GDT_TRANSFORM_OUT_OF_BOUNDS
   [17]	jmp 17Jan02 Layer errors
   [18] jmp 24Apr02 Layer Parameter Errors
   [19] mjs 15Nov02 Added PIPE errors for CGI server
   [20] rar 20Nov02	Added errors for NCSMakeDir
 *******************************************************/

#ifndef NCSERRORS_H
#define NCSERRORS_H

#ifdef __cplusplus
extern "C" {
#endif

#ifndef NCSDEFS_H
#include "NCSDefs.h"
#endif

#if (defined(WIN32) && !defined(_WIN32_WCE))/*||defined(MACINTOSH)*/ /**[10]**/
#include <crtdbg.h>
#else
#define _CRT_WARN 1
#if !defined(_DEBUG) && !defined(DEBUG)
#ifndef _ASSERT
#define _ASSERT(a)
#endif
#define _RPT0(a, b)
#define _RPT1(a, b, a1)
#define _RPT2(a, b, a1, a2)
#define _RPT3(a, b, a1, a2, a3)	
#define _RPT4(a, b, a1, a2, a3, a4)
#else	/* _DEBUG */
#ifndef _ASSERT
#define _ASSERT(a)
#endif
#define _RPT0(a, b)					fprintf(stderr, b)
#define _RPT1(a, b, a1)				fprintf(stderr, b, a1)
#define _RPT2(a, b, a1, a2)			fprintf(stderr, b, a1, a2)
#define _RPT3(a, b, a1, a2, a3)		fprintf(stderr, b, a1, a2, a3)
#define _RPT4(a, b, a1, a2, a3, a4)	fprintf(stderr, b, a1, a2, a3, a4)
#endif	/* _DEBUG */
#endif	/* WIN32 || MACINTOSH */

#ifdef PALM
#include <PalmTypes.h>
#include <FileStream.h>
#endif

/*
** Error Enum.
*/

/*
** IMPORTANT: Add new errors to the end of this list so we can retain some 
**			  backwards binary compatibilty. Also, don't forget to add the 
**			  error text!
*/

typedef enum {
	/* NCS Raster Errors */
	NCS_SUCCESS					= 0,
	NCS_QUEUE_NODE_CREATE_FAILED,
	NCS_FILE_OPEN_FAILED,
	NCS_FILE_LIMIT_REACHED,
	NCS_FILE_SIZE_LIMIT_REACHED,
	NCS_FILE_NO_MEMORY,
	NCS_CLIENT_LIMIT_REACHED,
	NCS_DUPLICATE_OPEN,
	NCS_PACKET_REQUEST_NYI,
	NCS_PACKET_TYPE_ILLEGAL,
	NCS_DESTROY_CLIENT_DANGLING_REQUESTS,

	/* NCS Network Errors */
	NCS_UNKNOWN_CLIENT_UID,
	NCS_COULDNT_CREATE_CLIENT,
	NCS_NET_COULDNT_RESOLVE_HOST,
	NCS_NET_COULDNT_CONNECT,
	NCS_NET_RECV_TIMEOUT,
	NCS_NET_HEADER_SEND_FAILURE,
	NCS_NET_HEADER_RECV_FAILURE,
	NCS_NET_PACKET_SEND_FAILURE,
	NCS_NET_PACKET_RECV_FAILURE,
	NCS_NET_401_UNAUTHORISED,			/* we don't do authentication - suggests a misconfigured server */
	NCS_NET_403_FORBIDDEN,				/* could be a 403.9 from IIS/PWS meaning max simultaneous request limit reached */
	NCS_NET_404_NOT_FOUND,				/* suggests server hasn't got IWS installed */
	NCS_NET_407_PROXYAUTH,				/* we don't do proxy authentication yet */
	NCS_NET_UNEXPECTED_RESPONSE,		/* http response code we can't handle */
	NCS_NET_BAD_RESPONSE,				/* http response not to spec */
	NCS_NET_ALREADY_CONNECTED,
	NCS_INVALID_CONNECTION,
	NCS_WINSOCK_FAILURE,

	/* NCS Symbol Errors */
	NCS_SYMBOL_ERROR,
	NCS_OPEN_DB_ERROR,
	NCS_DB_QUERY_FAILED,
	NCS_DB_SQL_ERROR,
	NCS_GET_LAYER_FAILED,
	NCS_DB_NOT_OPEN,
	NCS_QT_TYPE_UNSUPPORTED,

	/* Preference errors */
	NCS_PREF_INVALID_USER_KEY,
	NCS_PREF_INVALID_MACHINE_KEY,
	NCS_REGKEY_OPENEX_FAILED,
	NCS_REGQUERY_VALUE_FAILED,
	NCS_INVALID_REG_TYPE,

	/* Misc Errors */
	NCS_INVALID_ARGUMENTS,
	NCS_ECW_ERROR,				/* unspecified, but coming out of ecw */
	NCS_SERVER_ERROR,			/* unspecified server error */
	NCS_UNKNOWN_ERROR,
	NCS_EXTENT_ERROR,
	NCS_COULDNT_ALLOC_MEMORY,
	NCS_INVALID_PARAMETER,

	/* Compression Errors */
	NCS_FILEIO_ERROR,
	NCS_COULDNT_OPEN_COMPRESSION,
	NCS_COULDNT_PERFORM_COMPRESSION,
	NCS_GENERATED_TOO_MANY_OUTPUT_LINES,
	NCS_USER_CANCELLED_COMPRESSION,
	NCS_COULDNT_READ_INPUT_LINE,
	NCS_INPUT_SIZE_EXCEEDED,

	/* Decompression Errors */
	NCS_REGION_OUTSIDE_FILE,
	NCS_NO_SUPERSAMPLE,
	NCS_ZERO_SIZE,
	NCS_TOO_MANY_BANDS,
	NCS_INVALID_BAND_NR,

	/* NEW Compression Error */
	NCS_INPUT_SIZE_TOO_SMALL,

	/* NEW Network error */
	NCS_INCOMPATIBLE_PROTOCOL_VERSION,
	NCS_WININET_FAILURE,
	NCS_COULDNT_LOAD_WININET,

	/* NCSFile && NCSRenderer class errors */
	NCS_FILE_INVALID_SETVIEW,
	NCS_FILE_NOT_OPEN,

	/* NEW JNI Java Errors */
	NCS_JNI_REFRESH_NOT_IMPLEMENTED,	 // A class is trying to use RefreshUpdate() method, but has not implemented ECWProgressiveDisplay

	/* NEW Coordinate Errors*/
	NCS_INCOMPATIBLE_COORDINATE_SYSTEMS,
	NCS_INCOMPATIBLE_COORDINATE_DATUM,
	NCS_INCOMPATIBLE_COORDINATE_PROJECTION,
	NCS_INCOMPATIBLE_COORDINATE_UNITS,
	NCS_COORDINATE_CANNOT_BE_TRANSFORMED,
	NCS_GDT_ERROR,
	
	/* NEW NCScnet error */
	NCS_NET_PACKET_RECV_ZERO_LENGTH,	/**[01]**/
	
	/* Macintosh SDK specific errors */
	NCS_UNSUPPORTEDLANGUAGE,			/**[02]**/

	/* Lost of connection  */
	NCS_CONNECTION_LOST,				/**[03]**/

	NCS_COORD_CONVERT_ERROR,

	/* Metabase Stuff  */
	NCS_METABASE_OPEN_FAILED,				/**[04]**/
	NCS_METABASE_GET_FAILED,				/**[04]**/

	NCS_NET_HEADER_SEND_TIMEOUT,			/**[05]**/
	
	NCS_JNI_ERROR,							/**[06]**/

	NCS_DB_INVALID_NAME,					/**[07]**/
	NCS_SYMBOL_COULDNT_RESOLVE_HOST,		/**[07]**/
	
	NCS_INVALID_ERROR_ENUM,					/**[08]**/
	
	/* NCSFileIO errors [10] */
	NCS_FILE_EOF,						/* EOF 					*/
	NCS_FILE_NOT_FOUND,					/* File not found 		*/
	NCS_FILE_INVALID,					/* Invalid file			*/
	NCS_FILE_SEEK_ERROR,				/* Seek, read or write out of range	*/
	NCS_FILE_NO_PERMISSIONS,			/* File is read only, can't write */
	NCS_FILE_OPEN_ERROR,				/* Open failed 			*/
	NCS_FILE_CLOSE_ERROR,				/* Close failed 		*/
	NCS_FILE_IO_ERROR,					/* Misc IO error		*/

	NCS_SET_EXTENTS_ERROR,					/**[09]**/ 

	NCS_FILE_PROJECTION_MISMATCH,		/* Image projection doesn't match controlling layer. [14] */

	/** 1.65 gdt errors [15]**/
	NCS_GDT_UNKNOWN_PROJECTION,
	NCS_GDT_UNKNOWN_DATUM,
	NCS_GDT_USER_SERVER_FAILED,
	NCS_GDT_REMOTE_PATH_DISABLED,
	NCS_GDT_BAD_TRANSFORM_MODE,

	NCS_GDT_TRANSFORM_OUT_OF_BOUNDS,

	NCS_LAYER_DUPLICATE_LAYER_NAME,				/**[17]**/
	NCS_LAYER_INVALID_PARAMETER,				/**[18]**/

	NCS_PIPE_CREATE_FAILED,						/**[19]**/

	/* Directory creation errors */
	NCS_FILE_MKDIR_EXISTS,				/*[20] Directory already exists */
	NCS_FILE_MKDIR_PATH_NOT_FOUND,		/*[20] The path for the location of the new dir does not exist */

	// Insert new errors before here!
	NCS_MAX_ERROR_NUMBER					/**[08] NOTE: THOS SHOULD ALWAYS BE LAST IN ENUM! **/
} NCSError;


#ifdef NCSERROR_DEFINE_GLOBALS

static char *NCSErrorTextArray[] = {"No Error",							/* NCS_SUCCESS						*/
	/* NCS Raster Errors */
	"Queue node creation failed",						/* NCS_QUEUE_NODE_CREATE_FAILED		*/
	"File open failed",									/* NCS_FILE_OPEN_FAILED				*/
	"The Image Web Server's licensed file limit has been reached",								/* NCS_FILE_LIMIT_REACHED			*/
	"The requested file is larger than is permitted by the license on this Image Web Server",	/* NCS_FILE_SIZE_LIMIT_REACHED		*/
	"Not enough memory for new file",															/* NCS_FILE_NO_MEMORY				*/
	"The Image Web Server's licensed client limit has been reached",							/* NCS_CLIENT_LIMIT_REACHED			*/
	"Detected duplicate open from net layer",			/* NCS_DUPLICATE_OPEN				*/
	"Packet request type not yet implemented",			/* NCS_PACKET_REQUEST_NYI			*/
	"Packet type is illegal",							/* NCS_PACKET_TYPE_ILLEGAL			*/
	"Client closed while requests outstanding",			/* NCS_DESTROY_CLIENT_DANGLING_REQUESTS */

	/* NCS Network Errors */
	"Client UID unknown",								/* NCS_UNKNOWN_CLIENT_UID			*/
	"Could not create new client",						/* NCS_COULDNT_CREATE_CLIENT		*/
	"Could not resolve address of Image Web Server",		/* NCS_NET_COULDNT_RESOLVE_HOST		*/
	"Could not connect to host",							/* NCS_NET_COULDNT_CONNECT			*/
	"Receive timeout",									/* NCS_NET_RECV_TIMEOUT				*/
	"Error sending header",								/* NCS_NET_HEADER_SEND_FAILURE		*/
	"Error receiving header",							/* NCS_NET_HEADER_RECV_FAILURE		*/
	"Error sending packet",								/* NCS_NET_PACKET_SEND_FAILURE		*/
	"Error receiving packet",							/* NCS_NET_PACKET_RECV_FAILURE		*/
	"401 Unauthorised",									/* NCS_NET_401_UNAUTHORISED			*/
	"403 Forbidden",									/* NCS_NET_403_FORBIDDEN			*/
	"Is the host an Image Web Server?",					/* NCS_NET_404_NOT_FOUND			*/
	"Your HTTP proxy requires authentication,\nthis is presently unsupported by the Image Web Server control",	/*	NCS_NET_407_PROXYAUTH */
	"Unexpected HTTP response",							/* NCS_NET_UNEXPECTED_RESPONSE		*/
	"Bad HTTP response",								/* NCS_NET_BAD_RESPONSE				*/
	"Already connected",								/* NCS_NET_ALREADY_CONNECTED		*/
	"The connection is invalid",						/* NCS_INVALID_CONNECTION			*/
	"Windows sockets failure",							/* NCS_WINSOCK_FAILURE				*/

	/* NCS Symbol Errors */
	"Symbology error",									/* NCS_SYMBOL_ERROR					*/
	"Could not open database",							/* NCS_OPEN_DB_ERROR				*/
	"Could not execute the requested query on database",/* NCS_DB_QUERY_FAILED				*/
	"SQL statement could not be executed",				/* NCS_DB_SQL_ERROR					*/
	"Open symbol layer failed",							/* NCS_GET_LAYER_FAILED				*/
	"The database is not open",							/* NCS_DB_NOT_OPEN					*/
	"This type of quad tree is not supported",			/* NCS_QT_TYPE_UNSUPPORTED			*/
	/* Preference errors */
	"Invalid local user key name specified",			/* NCS_PREF_INVALID_USER_KEY		*/
	"Invalid local machine key name specified",			/* NCS_PREF_INVALID_MACHINE_KEY		*/
	"Failed to open registry key",						/* NCS_REGKEY_OPENEX_FAILED			*/
	"Registry query failed",							/* NCS_REGQUERY_VALUE_FAILED		*/
	"Type mismatch in registry variable",				/* NCS_INVALID_REG_TYPE				*/
	/* Misc errors */
	"Invalid arguments passed to function",				/* NCS_INVALID_ARGUMENTS			*/
	"ECW error",										/* NCS_ECW_ERROR					*/
	"Server error",										/* NCS_SERVER_ERROR					*/
	"Unknown error",									/* NCS_UNKNOWN_ERROR				*/
	"Extent conversion failed",							/* NCS_EXTENT_ERROR					*/
	"Could not allocate enough memory",					/* NCS_COULDNT_ALLOC_MEMORY	[12]	*/
	"An invalid parameter was used",					/* NCS_INVALID_PARAMETER			*/
	/* Compress errors */
	"Could not perform Read/Write on file",				/* NCS_FILEIO_ERROR					*/
	"Could not open compression task",					/* NCS_COULDNT_OPEN_COMPRESSION		*/
	"Could not perform compression",					/* NCS_COULDNT_PERFORM_COMPRESSION	*/
	"Trying to generate too many output lines",			/* NCS_GENERATED_TOO_MANY_OUTPUT_LINES */
	"User cancelled compression",						/* NCS_USER_CANCELLED_COMPRESSION	*/
	"Could not read line from input image file",		/* NCS_COULDNT_READ_INPUT_LINE		*/
	"Input image size exceeded for this version",		/* NCS_INPUT_SIZE_EXCEEDED			*/
		/* Decompression Errors */
	"Specified image region is outside image area",		/* NCS_REGION_OUTSIDE_FILE			*/
	"Supersampling not supported",						/* NCS_NO_SUPERSAMPLE				*/
	"Specified image region has a zero width or height",/* NCS_ZERO_SIZE					*/
	"More bands specified than exist in this file",		/* NCS_TOO_MANY_BANDS				*/
	"An invalid band number has been specified",		/* NCS_INVALID_BAND_NR				*/
	/* New Compression Error */
	"Input image size is too small to compress",		/* NCS_INPUT_SIZE_TOO_SMALL			*/
	/* NEW Network error */
	"The ECWP client version is incompatible with this server", /* NCS_INCOMPATIBLE_PROTOCOL_VERSION */
	"Windows Internet Client error",					/* NCS_WININET_FAILURE				*/
	"Could not load wininet.dll",						/* NCS_COULDNT_LOAD_WININET			*/

	/* NCSFile && NCSRenderer class errors */
	"Invalid SetView parameters or SetView not called.",/* NCS_FILE_INVALID_SETVIEW */
	"There is no open ECW file.",						/* NCS_FILE_NOT_OPEN */
	
	/* NEW JNI Java Errors */
	"Class does not implement ECWProgressiveDisplay interface.",/* NCS_JNI_REFRESH_NOT_IMPLEMENTED */

	/* NEW Coordinate Errors */
	"Incompatible coordinate systems",					/* NCS_INCOMPATIBLE_COORDINATE_SYSTEMS	*/
	"Incompatible coordinate datum types",				/* NCS_INCOMPATIBLE_COORDINATE_DATUM	*/
	"Incompatible coordinate projection types",			/* NCS_INCOMPATIBLE_COORDINATE_PROJECTION*/
	"Incompatible coordinate units types",				/* NCS_INCOMPATIBLE_COORDINATE_UNITS	*/
	"Non-linear coordinate systems not supported",		/* NCS_COORDINATE_CANNOT_BE_TRANSFORMED	*/
	"GDT Error",										/* NCS_GDT_ERROR */
	"Zero length packet",								/* NCS_NET_PACKET_RECV_ZERO_LENGTH */	/**[01]**/
	"Must use Japanese version of the ECW SDK",			/* NCS_UNSUPPORTEDLANGUAGE */			/**[02]**/
	"Lost of connection to server",						/* NCS_CONNECTION_LOST */				/**[03]**/
	"NCSGdt coordinate conversion failed",				/* NCS_COORD_CONVERT_ERROR */
	"Failed to open metabase",							/* NCS_METABASE_OPEN_FAILED*/				/**[04]**/
	"Failed to get value from metabase",				/* NCS_METABASE_GET_FAILED*/				/**[04]**/
	"Timeout sending header",							/* NCS_NET_HEADER_SEND_TIMEOUT*/			/**[05]**/
	"Java JNI error",									/* NCS_JNI_ERROR */						/**[06]**/
	"No data source passed",							/* NCS_DB_INVALID_NAME				*/	/**[07]**/
	"Could not resolve address of Image Web Server Symbol Server Extension",		/* NCS_SYMBOL_COULDNT_RESOLVE_HOST*/  /**[07]**/
	"Invalid NCSError value!",							/* NCS_INVALID_ERROR_ENUM */			/**[08]**/
	/* NCSFileIO errors [10] */
	"End Of File reached",								/* NCS_FILE_EOF							[10]	*/
	"File not found",									/* NCS_FILE_NOT_FOUND					[10]	*/
	"File is invalid or corrupt",						/* NCS_FILE_INVALID						[10]	*/
	"Attempted to read, write or seek past file limits",/* NCS_FILE_SEEK_ERROR					[10]	*/
	"Permissions not available to access file",			/* NCS_FILE_NO_PERMISSIONS				[10]	*/
	"File open error",									/* NCS_FILE_OPEN_ERROR					[10]	*/
	"File close error",									/* NCS_FILE_CLOSE_ERROR					[10]	*/
	"File IO error",									/* NCS_FILE_IO_ERROR					[10]	*/
	
	"Illegal World Coordinates",						/* NCS_SET_EXTENTS_ERROR				[13]	*/

	"Image projection doesn't match controlling layer", /* NCS_FILE_PROJECTION_MISMATCH		[14]	*/

	/** 1.65 gdt errors [15]**/
	"Unknown map projection",							/*NCS_GDT_UNKNOWN_PROJECTION,*/
	"Unknown datum",									/*NCS_GDT_UNKNOWN_DATUM,*/
	"User specified Geographic Projection Database data server failed",	/*NCS_GDT_USER_SERVER_FAILED*/
	"Remote Geographic Projection Database file downloading has been disable and no Geographic Projection Database data is locally available",	/*NCS_GDT_REMOTE_PATH_DISABLED*/
	"Invalid transform mode",							/*NCS_GDT_BAD_TRANSFORM_MODE,*/

	"coordinate to be transformed is out of bounds",	/*NCS_GDT_TRANSFORM_OUT_OF_BOUNDS*/

	"Layer already exists with this layer name",		/*NCS_LAYER_DUPLICATE_LAYER_NAME*/	/**[17]**/
	"Layer does not contain this parameter",			/*NCS_LAYER_INVALID_PARAMETER*/		/**[18]**/

	"Failed to create pipe",							/*NCS_PIPE_CREATE_FAILED*/ /**[19]**/

	/* Directory creation errors */
	"Directory already exists",							/*[20] NCS_FILE_MKDIR_EXISTS, Directory already exists */
	"The path was not found",							/*[20] NCS_FILE_MKDIR_PATH_NOT_FOUND, The path for the location of the new dir does not exist */

	// Insert new errors before here!
	"Max NCSError enum value!",							/* NCS_MAX_ERROR_NUMBER */				/**[08]**/
	""};

static char *NCSErrorTextArrayEx[] = {
	"No error",												/* NCS_SUCCESS											*/
	/* NCS Raster Errors */
	"Queue node creation failed",							/* NCS_QUEUE_NODE_CREATE_FAILED							*/
	"Could not open file \"%s\" %s",						/* NCS_FILE_OPEN_FAILED									*/
	"The Image Web Server's licensed file limit has been reached %s",							/* NCS_FILE_LIMIT_REACHED (license name eg office) */
	"The requested file is larger than is permitted by the license on this Image Web Server %s",/* NCS_FILE_SIZE_LIMIT_REACHED (license name eg office)	*/
	"Not enough memory for new file",															/* NCS_FILE_NO_MEMORY				*/
	"The Image Web Server's licensed client limit has been reached %s",							/* NCS_CLIENT_LIMIT_REACHED	(license name eg enterprise) */
	"Detected duplicate open from net layer",				/* NCS_DUPLICATE_OPEN									*/
	"Packet request type not yet implemented %d",			/* NCS_PACKET_REQUEST_NYI (packet type num)				*/
	"Packet type is illegal %d",							/* NCS_PACKET_TYPE_ILLEGAL (packet type num)			*/
	"Client closed while requests outstanding",				/* NCS_DESTROY_CLIENT_DANGLING_REQUESTS					*/

	/* NCS Network Errors */
	"Client UID unknown",									/* NCS_UNKNOWN_CLIENT_UID								*/
	"Could not create new client %s",						/* NCS_COULDNT_CREATE_CLIENT (reason)					*/
	"Could not resolve address of Image Web Server %s",		/* NCS_NET_COULDNT_RESOLVE_HOST	(ip or hostname)		*/
	"Could not connect to host %s",							/* NCS_NET_COULDNT_CONNECT (reason)						*/
	"Receive timeout",										/* NCS_NET_RECV_TIMEOUT									*/
	"Error sending header %s",								/* NCS_NET_HEADER_SEND_FAILURE (reason)					*/
	"Error receiving header %s",							/* NCS_NET_HEADER_RECV_FAILURE (reason)					*/
	"Error sending packet",									/* NCS_NET_PACKET_SEND_FAILURE							*/
	"Error receiving packet",								/* NCS_NET_PACKET_RECV_FAILURE							*/
	"401 Unauthorised",										/* NCS_NET_401_UNAUTHORISED								*/
	"403 Forbidden",										/* NCS_NET_403_FORBIDDEN								*/
	"Is the host an Image Web Server?",						/* NCS_NET_404_NOT_FOUND								*/
	"Your HTTP proxy requires authentication,\nthis is presently unsupported by the Image Web Server control",	/*	NCS_NET_407_PROXYAUTH */
	"Unexpected HTTP response %s",								/* NCS_NET_UNEXPECTED_RESPONSE (resonse # or string)*/
	"Bad HTTP response %s",									/* NCS_NET_BAD_RESPONSE	(resonse # or string)			*/
	"Already connected",									/* NCS_NET_ALREADY_CONNECTED							*/
	"The connection is invalid",							/* NCS_INVALID_CONNECTION								*/
	"Windows sockets failure %s",							/* NCS_WINSOCK_FAILURE (reason (GetLastError()) or wininet version)	*/

	/* NCS Symbol Errors */
	"Symbology error",										/* NCS_SYMBOL_ERROR										*/
	"Could not open database",								/* NCS_OPEN_DB_ERROR									*/
	"Could not execute the requested query on database",	/* NCS_DB_QUERY_FAILED									*/
	"SQL statement could not be executed",					/* NCS_DB_SQL_ERROR										*/
	"Open symbol layer failed",								/* NCS_GET_LAYER_FAILED									*/
	"The database is not open",								/* NCS_DB_NOT_OPEN										*/
	"This type of quad tree is not supported",				/* NCS_QT_TYPE_UNSUPPORTED								*/

	/* Preference errors */
	"Invalid local user key name specified %s",				/* NCS_PREF_INVALID_USER_KEY (key name)					*/
	"Invalid local machine key name specified %s",			/* NCS_PREF_INVALID_MACHINE_KEY	(local machine key)		*/
	"Failed to open registry key %s",						/* NCS_REGKEY_OPENEX_FAILED	(key name)					*/
	"Registry query failed %s",								/* NCS_REGQUERY_VALUE_FAILED (reason)					*/
	"Type mismatch in registry variable",					/* NCS_INVALID_REG_TYPE									*/
	
	/* Misc errors */
	"Invalid arguments passed to function %s",				/* NCS_INVALID_ARGUMENTS (function name)				*/
	"ECW error %s",											/* NCS_ECW_ERROR (reason)								*/
	"Server error %s",										/* NCS_SERVER_ERROR (reason)							*/
	"Unknown error %s",										/* NCS_UNKNOWN_ERROR (reason)							*/
	"Extent conversion failed",								/* NCS_EXTENT_ERROR										*/
	"Could not allocate enough memory %s",					/* NCS_COULDNT_ALLOC_MEMORY	(what trying to malloc)	[12]*/
	"An invalid parameter was used %s",						/* NCS_INVALID_PARAMETER (reason)						*/
	
	/* Compress errors */
	"Could not perform Read/Write on file %s",				/* NCS_FILEIO_ERROR	(filename)							*/
	"Could not open compression task %s",					/* NCS_COULDNT_OPEN_COMPRESSION	(reason)				*/
	"Could not perform compression %s",						/* NCS_COULDNT_PERFORM_COMPRESSION	(reason)			*/
	"Trying to generate too many output lines",				/* NCS_GENERATED_TOO_MANY_OUTPUT_LINES					*/
	"User cancelled compression",							/* NCS_USER_CANCELLED_COMPRESSION						*/
	"Could not read line from input image file",			/* NCS_COULDNT_READ_INPUT_LINE							*/
	"Input image size exceeded for this version",			/* NCS_INPUT_SIZE_EXCEEDED								*/
	
	/* Decompression Errors */
	"Specified image region is outside image area",			/* NCS_REGION_OUTSIDE_FILE								*/
	"Supersampling not supported",							/* NCS_NO_SUPERSAMPLE									*/
	"Specified image region has a zero width or height",	/* NCS_ZERO_SIZE										*/
	"More bands specified than exist (%d) in this file (%d)",/* NCS_TOO_MANY_BANDS	(bands passed, bands in file)	*/
	"An invalid band number has been specified %s",			/* NCS_INVALID_BAND_NR (bandnr)							*/
	
	/* New Compression Error */
	"Input image size is too small to compress",			/* NCS_INPUT_SIZE_TOO_SMALL								*/
	/* NEW Network error */
	"The ECWP client version (%s) is incompatible with this server (%s)", /* NCS_INCOMPATIBLE_PROTOCOL_VERSION (clientver, server ver) */
	"Windows Internet Client error %s",						/* NCS_WININET_FAILURE	(reason)						*/
	"Could not load wininet.dll %s",						/* NCS_COULDNT_LOAD_WININET	(reason)					*/

	/* NCSFile && NCSRenderer class errors */
	"Invalid SetView parameters or SetView not called.",	/* NCS_FILE_INVALID_SETVIEW								*/
	"There is no open ECW file.",							/* NCS_FILE_NOT_OPEN									*/
	
	/* NEW JNI Java Errors */
	"Class does not implement ECWProgressiveDisplay interface.",/* NCS_JNI_REFRESH_NOT_IMPLEMENTED */

	/* NEW Coordinate Errors */
	"Incompatible coordinate systems",						/* NCS_INCOMPATIBLE_COORDINATE_SYSTEMS					*/
	"Incompatible coordinate datum types",					/* NCS_INCOMPATIBLE_COORDINATE_DATUM					*/
	"Incompatible coordinate projection types",				/* NCS_INCOMPATIBLE_COORDINATE_PROJECTION				*/
	"Incompatible coordinate units types",					/* NCS_INCOMPATIBLE_COORDINATE_UNITS					*/
	"Non-linear coordinate systems not supported",			/* NCS_COORDINATE_CANNOT_BE_TRANSFORMED					*/
	"GDT Error : %s",										/* NCS_GDT_ERROR (reason)								*/
	"Zero length packet : %s",								/* NCS_NET_PACKET_RECV_ZERO_LENGTH (reason)             */ /**[01]**/
	"Must use Japanese version of the ECW SDK",				/* NCS_UNSUPPORTEDLANGUAGE								*/ /**[02]**/
	"Lost of connection to server : %s",					/* NCS_CONNECTION_LOST									*/ /**[03]**/
	"NCSGdt coordinate conversion failed : %s",				/* NCS_COORD_CONVERT_ERROR */
	"Failed to open metabase : %s",							/* NCS_METABASE_OPEN_FAILED*/				/**[04]**/
	"Failed to get value from metabase : %s",				/* NCS_METABASE_GET_FAILED*/				/**[04]**/
	"Timeout sending header : %s",							/* NCS_NET_HEADER_SEND_TIMEOUT*/			/**[05]**/
	"Java JNI error : %s",									/* NCS_JNI_ERROR */						/**[06]**/
	"No data source passed",								/* NCS_DB_INVALID_NAME									*/
	"Could not resolve address of Image Web Server Symbol Server Extension",		/* NCS_SYMBOL_COULDNT_RESOLVE_HOST		*/
	"Invalid NCSError value!",							/* NCS_INVALID_ERROR_ENUM */			/**[08]**/
	/* NCSFileIO errors [10] */
	"End Of File reached : %s",									/* NCS_FILE_EOF					[10]	*/
	"File not found : %s",										/* NCS_FILE_NOT_FOUND			[10]	*/
	"File is invalid or corrupt : %s",							/* NCS_FILE_INVALID				[10]	*/
	"Attempted to read, write or seek past file limits : %s",	/* NCS_FILE_SEEK_ERROR			[10]	*/
	"Permissions not available to access file : %s",			/* NCS_FILE_NO_PERMISSIONS		[10]	*/
	"File open error : %s",										/* NCS_FILE_OPEN_ERROR			[10]	*/
	"File close error : %s",									/* NCS_FILE_CLOSE_ERROR			[10]	*/
	"File IO error : %s",										/* NCS_FILE_IO_ERROR			[10]	*/
	
	"Illegal World Coordinates : %s",							/* NCS_SET_EXTENTS_ERROR		[13]	*/

	"Image projection doesn't match controlling layer : %s",	/* NCS_FILE_PROJECTION_MISMATCH		[14]	*/

	/** 1.65 gdt errors [15]**/
	"Unknown map projection: %s",							/*NCS_GDT_UNKNOWN_PROJECTION,*/
	"Unknown datum: %s",									/*NCS_GDT_UNKNOWN_DATUM,*/
	"User specified Geographic Projection Database data server failed while loading %s.  Please check your network connection and if the problem persists contact the website administrator.",			/*NCS_GDT_USER_SERVER_FAILED*/
	"Remote Geographic Projection Database file downloading has been disable and no Geographic Projection Database data is locally available",	/*NCS_GDT_REMOTE_PATH_DISABLED*/
	"Invalid transform mode: %s",							/*NCS_GDT_BAD_TRANSFORM_MODE,			[17]**/

	"coordinate to be transformed is out of bounds: %s",	/*NCS_GDT_TRANSFORM_OUT_OF_BOUNDS*/

	"Layer already exists with this name : %s",				/*NCS_LAYER_DUPLICATE_LAYER_NAME*/	/**[17]**/
	"Layer does not contain this parameter : %s",			/*NCS_LAYER_INVALID_PARAMETER*/		/**[18]**/

	"Failed to create pipe : %s",							/*NCS_PIPE_CREATE_FAILED*/ /**[19]**/

	/* Directory creation errors */
	"Directory already exists : %s",						/*[20] NCS_FILE_MKDIR_EXISTS, Directory already exists */
	"The path was not found : %s",							/*[20] NCS_FILE_MKDIR_PATH_NOT_FOUND, The path for the location of the new dir does not exist */

	// Insert new errors before here!
	"Max NCSError enum value!",							/* NCS_MAX_ERROR_NUMBER */				/**[08]**/
	""};

#endif /* NCSERROR_DEFINE_GLOBALS */

#define NCS_SUCCEEDED(nError) (nError == NCS_SUCCESS)
#define NCS_FAILED(nError) (nError != NCS_SUCCESS)

void NCSErrorInit(void);
void NCSErrorFini(void);
void NCSGetLastErrorTextMsgBox(NCSError nError, void *pWindow);
const char * NCSFormatErrorText(NCSError nError, ...);
const char * NCSGetLastErrorText(NCSError nError);
NCSError NCSGetLastErrorNum(void);

#ifdef WIN32 // [11]

DWORD NCSDbgGetExceptionInfoMsg(EXCEPTION_POINTERS *pExceptionPtr, char *pMessage);

#endif	/* MACINTOSH */

// Deprecated
extern const char * NCSGetErrorText(NCSError eError);

#ifdef WIN32
#ifdef _WIN32_WCE
#define MSGBOX_WARNING_FLAGS	(MB_OK|MB_ICONINFORMATION)
#define MSGBOX_ERROR_FLAGS		(MB_OK|MB_ICONERROR)
#else
#define MSGBOX_WARNING_FLAGS	(MB_OK|MB_ICONINFORMATION|MB_TASKMODAL)
#define MSGBOX_ERROR_FLAGS		(MB_OK|MB_ICONERROR|MB_TASKMODAL)
#endif
#elif defined PALM
NCSError NCSPalmGetNCSError(Err eErr);
#endif

#ifdef __cplusplus
}
#endif

#endif /* NCSERRORS_H */
