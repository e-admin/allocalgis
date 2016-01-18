/********************************************************** 
** Copyright 1999 Earth Resource Mapping Pty Ltd.
** This document contains unpublished source code of
** Earth Resource Mapping Pty Ltd. This notice does
** not indicate any intention to publish the source
** code contained herein.
** 
** FILE:   	NCSECWClient.wc
** CREATED:	08 May 1999
** AUTHOR: 	TIS
** PURPOSE:	Public Client Interface library to Image Web Server ECW image decompression library
** EDITS:
** [01] tis	01-May-99 Created file
** [02] sjc 09-Jul-99 added SetFileViewEx() call.
** [03] sjc 30-Sep-99 added NCScbmCloseFileViewEx() call.
** [04] sjc 26-Oct-00 Added NCSecwSetIOCallbacks()
** [05] ddi 14-Nov-00 Added NCScbmReadViewLineIEEE4() and NCScbmReadViewLineUINT16()
** [06] sjc 22-Jan-02 New config get/set routines and enum
** [07] sjc 04-Feb-02 Added MAXOPEN
** [08] sjc 20-Feb-02 Added a bunch of tuneable parameters
** [09] sjc 30-Apr-03 Added low-mem compression config option
** [10] rar 02-Sep-03 Added option to try ecwp re-connection if connection lost
**
** NOTES
**
**	(1)	This file must be kept in synch with NCSECW.H
** 
**
********************************************************/

#ifndef NCSECWCLIENT_H
#define NCSECWCLIENT_H

#ifndef NCSDEFS_H
#include "NCSDefs.h"
#endif
#ifndef NCSERRORS_H
#include "NCSErrors.h"
#endif

#ifdef __cplusplus
extern "C" {
#endif

/*
**	Return status from read line routines.
**	The application should treat CANCELLED operations as non-fatal,
**	in that they most likely mean this view read was cancelled for
**	performance reasons.
*/
typedef enum {
	NCSECW_READ_OK			= 0,	// read was successful
	NCSECW_READ_FAILED		= 1,	// read failed due to an error
	NCSECW_READ_CANCELLED	= 2		// read was cancelled, due to:
									// a) application has moved to a new SetView
									// b) due to library shutdown in progress 
} NCSEcwReadStatus;

/*
**	Public and visible NCSECW structures
*/

typedef enum {
	ECW_CELL_UNITS_INVALID	=	0,
	ECW_CELL_UNITS_METERS	=	1,	/* will be set to 1 for RAW type files */
	ECW_CELL_UNITS_DEGREES	=	2,
	ECW_CELL_UNITS_FEET		=	3,
	ECW_CELL_UNITS_UNKNOWN	=	4
} CellSizeUnits;

typedef enum {						/**[05]**/
	NCSCT_UINT8				=	0,	/**[05]**/
	NCSCT_UINT16			=	1,	/**[05]**/
	NCSCT_UINT32			=	2,	/**[05]**/
	NCSCT_UINT64			=	3,	/**[05]**/
	NCSCT_INT8				=	4,	/**[05]**/
	NCSCT_INT16				=	5,	/**[05]**/
	NCSCT_INT32				=	6,	/**[05]**/
	NCSCT_INT64				=	7,	/**[05]**/
	NCSCT_IEEE4				=	8,	/**[05]**/
	NCSCT_IEEE8				=	9	/**[05]**/
} NCSEcwCellType;					/**[05]**/

typedef enum {						/**[06]**/
	NCSCFG_TEXTURE_DITHER			= 0,/** BOOLEAN - TRUE/FALSE texture image [06]**/
	NCSCFG_FORCE_FILE_REOPEN		= 1,/** BOOLEAN - TRUE/FALSE open new file each view opened [06]**/
	NCSCFG_CACHE_MAXMEM				= 2,/** UINT32 - Target MAX memory to use for cache [06]**/
	NCSCFG_CACHE_MAXOPEN			= 3,/** UINT32 - Target MAX # files to have open to use for cache [07]**/
	NCSCFG_BLOCKING_TIME_MS			= 4,/** NCSTimeStampMs - Time an ECWP: blocking read waits before returning - default 10000ms [08]**/
	NCSCFG_REFRESH_TIME_MS			= 5,/** NCSTimeStampMs - Time delay between blocks arriving and next Refresh callback - default 500ms [08]**/
	NCSCFG_PURGE_DELAY_MS			= 6,/** NCSTimeStampMs - Min Time delay between last cache purge and the next one - default 1000ms [08]**/
	NCSCFG_FILE_PURGE_DELAY_MS		= 7,/** NCSTimeStampMs - Time delay between last view closing and file being purged from cache - default 1800000ms [08]**/
	NCSCFG_MIN_FILE_PURGE_DELAY_MS	= 8,/** NCSTimeStampMs - Min Time delay between last view closing and file being purged from cache - default 30000ms [08]**/
	NCSCFG_ECWP_PROXY				= 9,/** char * - "server" - name of ECWP Proxy server **/
	NCSCFG_FORCE_LOWMEM_COMPRESS	= 10,/** BOOLEAN - TRUE/FALSE force a low-memory compression [09]**/
	NCSCFG_TRY_ECWP_RECONNECT		= 11/** BOOLEAN - TRUE/FALSE Try to reconnect if ecwp connection lost [10]**/
} NCSEcwConfigType;					/**[06]**/

typedef struct {		// NCScbmGetViewFileInfo() returns a pointer to this file info structure for a view
	UINT32	nSizeX, nSizeY;			// Size in X and Y directions, e.g. number of cells in each direction
	UINT16	nBands;					// number of bands (not subbands) in the file, e.g. 3 for a RGB file
	UINT16	nCompressionRate;		// Approximate compression rate. May be zero.  E.G. 20 = 20:1 compression
	CellSizeUnits	eCellSizeUnits;	// Units used for pixel size
	IEEE8	fCellIncrementX;		// Increment in CellSizeUnits in X direction. May be negative. Will never be zero
	IEEE8	fCellIncrementY;		// Increment in CellSizeUnits in Y direction. May be negative. Will never be zero
	IEEE8	fOriginX;				// World X origin for top-left corner of top-left cell, in CellSizeUnits
	IEEE8	fOriginY;				// World Y origin for top-left corner of top-left cell, in CellSizeUnits
	char	*szDatum;				// ER Mapper style Datum name string, e.g. "RAW" or "NAD27". Will never be NULL
	char	*szProjection;			// ER Mapper style Projection name string, e.g. "RAW" or "WGS84". Will never be NULL
} NCSFileViewFileInfo;

typedef struct {		// NCScbmGetViewSetInfo() returns a pointer to this set info structure for a view
	void	*pClientData;			// Client data
	UINT32 nBands;					// number of bands to read
	UINT32 *pBandList;				// Array of band numbers being read
	UINT32 nTopX, nLeftY;			// Top-Left in image coordinates
	UINT32 nBottomX, nRightY;		// Bottom-Left in image coordinates
	UINT32 nSizeX, nSizeY;			// Size of window
	UINT32 nBlocksInView;			// total number of blocks that cover the view area
	UINT32 nBlocksAvailable;		// current number of blocks available at this instant
	UINT32 nBlocksAvailableAtSetView;	// Blocks that were available at the time of the SetView
	UINT32 nMissedBlocksDuringRead;	// number of blocks that were not present during a view read
	IEEE8  fTopX, fLeftY;			// Top-Left in world coordinates from SetViewEx	[02]
	IEEE8  fBottomX, fRightY;		// Bottom-RIght in world coordinates from SetViewEx [02]
} NCSFileViewSetInfo;

/*
** The public library is included into the private library to pick up the public
** structures. If being included, we don't use the public function call definitions
*/

#ifndef ERS_WAVELET_DATASET_EXT
#define ERS_WAVELET_DATASET_EXT	".ecw"		/* compressed wavelet format file extension */
#endif
#ifndef ERSWAVE_VERSION_STRING
#define ERSWAVE_VERSION_STRING	"2.0"		/* should be in sync with the above */
#endif
#ifndef NCS_ECW_PROXY_KEY
#define NCS_ECW_PROXY_KEY		"ECWP Proxy"
#endif // NCS_ECW_PROXY_KEY

typedef struct NCSFileStruct NCSFile;
typedef struct NCSFileViewStruct NCSFileView;

/*
**	Note:  routines return non-zero if there was an error
*/


/*
**	Do not call these unless directly including the NCSECW code rather than linking against the DLL
*/
extern void NCS_CALL NCSecwInit(void);		// DO NOT call if linking against the DLL
extern void NCS_CALL NCSecwShutdown(void);	// DO NOT call if linking against the DLL

/* [04] NCSecwSetIOCallbacks() - see SDK samples for details
*/
extern NCSError NCSecwSetIOCallbacks(NCSError (NCS_CALL *pOpenCB)(char *szFileName, void **ppClientData),
									  NCSError (NCS_CALL *pCloseCB)(void *pClientData),
									  NCSError (NCS_CALL *pReadCB)(void *pClientData, void *pBuffer, UINT32 nLength),
									  NCSError (NCS_CALL *pSeekCB)(void *pClientData, UINT64 nOffset),
									  NCSError (NCS_CALL *pTellCB)(void *pClientData, UINT64 *pOffset));
/*
**	This is used for multi-client load testing and texture management. Never call this routine.
*/
extern void NCS_CALL NCSecwConfig(BOOLEAN bNoTextureDither, BOOLEAN bForceFileReopen);

/*
** Reports if this is a local or remote file, and breaks URL down into sections
*/
extern BOOLEAN NCS_CALL NCSecwNetBreakdownUrl( char *szUrlPath,
						   char **ppProtocol,	int *pnProtocolLength,
						   char **ppHost,		int *pnHostLength,
						   char **ppFilename,	int *pnFilenameLength);
/*
** Opens a File View.  After doing this, you can do a GetViewFileInfo to get the file details
*/
extern NCSError NCS_CALL NCScbmOpenFileView(char *szUrlPath, NCSFileView **ppNCSFileView,
					   NCSEcwReadStatus (*pRefreshCallback)(NCSFileView *pNCSFileView));
/*
** Closes a File View. You can do this at any time after an OpenFileView
*/
extern NCSError NCS_CALL NCScbmCloseFileView(NCSFileView *pNCSFileView);
extern NCSError NCS_CALL NCScbmCloseFileViewEx(NCSFileView *pNCSFileView, BOOLEAN bFreeCachedFile);	/**[03]**/
/*
** Gets view File info - generic information about the file opened by an OpenView.
** No SetView specific information is available via this call - use NCScbmGetViewInfo() instead.
*/
extern NCSError NCS_CALL NCScbmGetViewFileInfo(NCSFileView *pNCSFileView, NCSFileViewFileInfo **ppNCSFileViewFileInfo);
/*
** Gets view info - information about the Setview currently being processed
*/
extern NCSError NCS_CALL NCScbmGetViewInfo(NCSFileView *pNCSFileView, NCSFileViewSetInfo **ppNCSFileViewSetInfo);
/*
** Sets the File View. You can do this at any time, after an OpenFileView.
** Multiple SetFileViews can be done, even if previous SetViews have not finished
** processing yet.  After the Setview call is made, you can free the pBandList if
** you wih - it is only used during the SetFileView, not afterwards
*/
extern NCSError NCS_CALL NCScbmSetFileView(NCSFileView *pNCSFileView,
				UINT32 nBands,					// number of bands to read
				UINT32 *pBandList,				// index into actual band numbers from source file
			    UINT32 nTopX, UINT32 nLeftY,	// Top-Left in image coordinates
				UINT32 nBottomX, UINT32 nRightY,// Bottom-Left in image coordinates
				UINT32 nSizeX, UINT32 nSizeY);	// Output view size in window pixels

extern NCSError NCS_CALL NCScbmSetFileViewEx(NCSFileView *pNCSFileView,		/*[02]*/
				UINT32 nBands,					// number of bands to read
				UINT32 *pBandList,				// index into actual band numbers from source file
			    UINT32 nTopX, UINT32 nLeftY,	// Top-Left in image coordinates
				UINT32 nBottomX, UINT32 nRightY,// Bottom-Right in image coordinates
				UINT32 nSizeX, UINT32 nSizeY,	// Output view size in window pixels
				IEEE8 fTopX, IEEE8 fLeftY,		// Top-Left in world coordinates
				IEEE8 fBottomX, IEEE8 fRightY);	// Bottom-Right in world coordinates
/*
** Read line by line in BIL format to different data types
*/
NCSEcwReadStatus NCS_CALL NCScbmReadViewLineBIL( NCSFileView *pNCSFileView, UINT8 **p_p_output_line);
NCSEcwReadStatus NCS_CALL NCScbmReadViewLineBILEx( NCSFileView *pNCSFileView, NCSEcwCellType eType, void **p_p_output_line);	/**[05]**/

/*
** Read line by line in RGB format
*/
NCSEcwReadStatus NCS_CALL NCScbmReadViewLineRGB( NCSFileView *pNCSFileView, UINT8 *pRGBTriplets);
/*
** Read line by line in BGR format
*/
NCSEcwReadStatus NCS_CALL NCScbmReadViewLineBGR( NCSFileView *pNCSFileView, UINT8 *pRGBTriplets);
/*
** Read line by line in RGB format
*/
NCSEcwReadStatus NCS_CALL NCScbmReadViewLineRGBA( NCSFileView *pNCSFileView, UINT32 *pRGBA);
/*
** Read line by line in BGR format
*/
NCSEcwReadStatus NCS_CALL NCScbmReadViewLineBGRA( NCSFileView *pNCSFileView, UINT32 *pBGRA);

/*
** Used for testing purposes, to simulate the Inverse DWT process
*/
NCSEcwReadStatus NCS_CALL NCScbmReadViewFake( NCSFileView *pNCSFileView);	// a fake reader, for performance testing

/*
** Return the major and minor versions of the library.
*/
void NCSGetLibVersion( INT32 *nMajor, INT32 *nMinor );

/*
** Get/Set config parameters [06]
*/
NCSError NCSecwSetConfig(NCSEcwConfigType eType, ...);
NCSError NCSecwGetConfig(NCSEcwConfigType eType, ...);

/*
** Purge the ECW cache, based on current config [08]
*/
extern void NCScbmPurgeCache(NCSFileView *pView);

#ifdef __cplusplus
}
#endif

#endif	// NCSECWCLIENT_H
