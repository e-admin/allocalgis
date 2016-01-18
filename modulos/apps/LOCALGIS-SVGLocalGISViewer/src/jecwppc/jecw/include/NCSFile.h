/********************************************************** 
** Copyright 1998 Earth Resource Mapping Pty Ltd.
** This document contains unpublished source code of
** Earth Resource Mapping Pty Ltd. This notice does
** not indicate any intention to publish the source
** code contained herein.
** 
** FILE:   	NCSFile.c
** CREATED:	12 Jan 2000
** AUTHOR: 	Mark Sheridan
** PURPOSE:	C++ class wrappers for the ECW library
** EDITS:
** [01] sjc 10Dec02 Temporary fix for bug #1219
*******************************************************/

#ifndef NCSFILE_H
#define NCSFILE_H

#include "NCSECWClient.h"
#include "NCSErrors.h"
#include "NCSDefs.h"

typedef NCSEcwReadStatus (*NCSRefreshCallback)( class CNCSFile *pFile);

class NCS_EXPORT CNCSFile 
{
public:
	CNCSFile();
	virtual ~CNCSFile();

	INT32 m_nWidth;				/*!< The number of cells across the dataset */
	INT32 m_nHeight;			/*!< The number of lines down the dataset */
	INT32 m_nNumberOfBands;		/*!< The number of bands in the dataset */
	IEEE8 m_dCompressionRate;	/*!< The actual compression ratio */
	IEEE8 m_dCellIncrementX;	/*!< The X cell size in m_CellSizeUnits */
	IEEE8 m_dCellIncrementY;	/*!< The Y cell size in m_CellSizeUnits */
	IEEE8 m_dOriginX;			/*!< The top left X origin of the dataset (world) */
	IEEE8 m_dOriginY;			/*!< The top left Y origin of the dataset (world) */
	char *m_pDatum;				/*!< The GDT datum of the dataset */
	char *m_pProjection;		/*!< The GDT projection of the dataset */
	char *m_pFilename;			/*!< A pointer to the currently open filename */
	CellSizeUnits m_CellSizeUnits;/*!< Cell units, meters, degrees or feet */
	
	// Methods on file
	NCSError Open ( char * pURLPath, BOOLEAN bProgressiveDisplay );
	NCSError Close ( BOOLEAN bFreeCache );
	NCSError SetView ( INT32 nBands, INT32 *pBandList, 
					   INT32 nWidth, INT32 nHeight,
					   IEEE8 dWorldTLX, IEEE8 dWorldTLY,
					   IEEE8 dWorldBRX, IEEE8 dWorldBRY );
	NCSError SetView ( INT32 nBands, INT32 *pBandList, 
					   INT32 nWidth, INT32 nHeight,
					   INT32 dDatasetTLX, INT32 dDatasetTLY,
					   INT32 dDatasetBRX, INT32 dDatasetBRY );
	virtual NCSEcwReadStatus ReadLineBIL (UINT8 **ppOutputLine);
	virtual NCSEcwReadStatus ReadLineRGB (UINT8 *pRGBTriplet);
	virtual NCSEcwReadStatus ReadLineBGR (UINT8 *pRGBTriplet);
	NCSError ConvertWorldToDataset(IEEE8 dWorldX, IEEE8 dWorldY, INT32 *pnWorldX, INT32 *pnWorldY); // Rectilinear conversion from world to dataset coord
	NCSError ConvertDatasetToWorld(INT32 nWorldX, INT32 nWorldY, IEEE8 *pdWorldX, IEEE8 *pdWorldY); // Rectilinear conversion from dataset to world coord
	void SetClientData(void *pClientData);
	void *GetClientData ();
	INT32 GetPercentComplete ();
	INT32 GetPercentCompleteTotalBlocksInView();

	// Misc util functions
	static BOOLEAN BreakdownURL(  char *pURLPath, 
							char **ppProtocol,
							char **ppHost, 
							char **ppFilename);
	static const char *FormatErrorText ( NCSError nErrorNum );

	virtual void RefreshUpdate(NCSFileViewSetInfo *pViewSetInfo){};

	NCSFileViewSetInfo * GetFileViewSetInfo();

public:
	INT32 m_nSetViewNrBands;		/*!< The number of bands in the current view */
	INT32 *m_pnSetViewBandList;		/*!< A pointer to the band list for the current view */
	INT32 m_nSetViewWidth;			/*!< The current view width */
	INT32 m_nSetViewHeight;			/*!< The current view height */
	IEEE8 m_dSetViewWorldTLX;		/*!< The current view world top left X */
	IEEE8 m_dSetViewWorldTLY;		/*!< The current view world top left Y */
	IEEE8 m_dSetViewWorldBRX;		/*!< The current view world bottom right X */
	IEEE8 m_dSetViewWorldBRY;		/*!< The current view world bottom right Y */
	INT32 m_nSetViewDatasetTLX;		/*!< The current view dataset top left X */
	INT32 m_nSetViewDatasetTLY;		/*!< The current view dataset top left Y */
	INT32 m_nSetViewDatasetBRX;		/*!< The current view dataset bottom right X */
	INT32 m_nSetViewDatasetBRY;		/*!< The current view dataset bottom right Y */

protected:
	BOOLEAN m_bIsProgressive;
	BOOLEAN m_bHaveValidSetView;
	BOOLEAN m_bSetViewModeIsWorld;
	BOOLEAN m_bIsOpen;

private:
	NCSFileView *m_pNCSFileView;
	NCSFileViewFileInfo *m_pFileViewInfo;
	NCSFileViewSetInfo *m_pFileViewSetInfo;
	void *m_pClientData;

	static NCSEcwReadStatus InternalRefreshCallback(NCSFileView *pNCSFileView);//[01]
};

#endif /* NCSFILE_H */
