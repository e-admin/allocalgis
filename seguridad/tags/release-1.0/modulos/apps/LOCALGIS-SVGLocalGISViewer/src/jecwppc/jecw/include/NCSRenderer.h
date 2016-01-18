/********************************************************** 
** Copyright 1998 Earth Resource Mapping Pty Ltd.
** This document contains unpublished source code of
** Earth Resource Mapping Pty Ltd. This notice does
** not indicate any intention to publish the source
** code contained herein.
** 
** FILE:   	NCSRenderer.c
** CREATED:	12 Jan 2000
** AUTHOR: 	Mark Sheridan
** PURPOSE:	C++ class wrappers for the ECW library
** EDITS:
**
** [01] 07Dec00 jmp 
** [02] 12Feb01 jmp 
*******************************************************/


//#if !defined(NCSRENDERER_H)
#ifndef NCSRENDERER_H
#define NCSRENDERER_H

#define NCS_HIST_AND_LUT_SUPPORT
//#define NCS_MOSAIC_TOOL_SUPPORT

#if defined(WIN32) || defined(_WIN32_WCE)
	#define DIB_FIX		//only define for Release with IWS 2.0
#endif //WIN32

#include "NCSFile.h"
#include "NCSMutex.h"

#ifdef MACINTOSH
	#include <Quickdraw.h>
	#define COLORREF DWORD
	#define RECT Rect
	#define LPRECT Rect *
	#define HDC CGrafPtr
#endif

class NCS_EXPORT CNCSRenderer : public CNCSFile  
{
public:
	CNCSRenderer();
	virtual ~CNCSRenderer();
	NCSError SetView(  INT32 nBands, INT32 *pBandList, 
					   INT32 nWidth, INT32 nHeight,
					   IEEE8 dWorldTLX, IEEE8 dWorldTLY,
					   IEEE8 dWorldBRX, IEEE8 dWorldBRY );
	NCSError SetView(  INT32 nBands, INT32 *pBandList, 
					   INT32 nWidth, INT32 nHeight,
					   INT32 nDatasetTLX, INT32 nDatasetTLY,
					   INT32 nDatasetBRX, INT32 nDatasetBRY );
	virtual NCSEcwReadStatus ReadLineBIL (UINT8 **ppOutputLine);
	virtual NCSEcwReadStatus ReadLineRGB (UINT8 *pRGBTriplet);
	virtual NCSEcwReadStatus ReadLineBGR (UINT8 *pRGBTriplet);
	NCSError ReadImage( NCSFileViewSetInfo *pViewSetInfo);
	NCSError ReadImage( INT32 nWidth, INT32 nHeight );
	NCSError ReadImage( IEEE8 dWorldTLX, IEEE8 dWorldTLY, IEEE8 dWorldBRX, IEEE8 dWorldBRY, INT32 nDatasetTLX, INT32 nDatasetTLY, INT32 nDatasetBRX, INT32 nDatasetBRY, INT32 nWidth, INT32 nHeight);
//#ifdef WIN32
	NCSError DrawImage( HDC DeviceContext, LPRECT pClipRect, IEEE8 dWorldTLX, IEEE8 dWorldTLY, IEEE8 dWorldBRX, IEEE8 dWorldBRY );
//#elif defined(MACINTOSH)
//	NCSError DrawImage( GrafPtr gpWindow, RectPtr pClipRect, IEEE8 dWorldTLX, IEEE8 dWorldTLY, IEEE8 dWorldBRX, IEEE8 dWorldBRY );
//#else
//	#error unknown machine type
//#endif

	void SetBackgroundColor( COLORREF nBackgroundColor );
	void SetTransparent( BOOLEAN bTrasnparent );
	void GetTransparent( BOOLEAN *pbTransparent );
	NCSError WriteJPEG(char *pFilename, INT32 nQuality);
	NCSError WriteJPEG(UINT8 **ppBuffer, UINT32 *pBufferLength, INT32 nQuality);
	static void FreeJPEGBuffer(UINT8 *pBuffer);
	NCSError WriteWorldFile(char *pFilename);
	static void ShutDown( void );
#ifdef NCS_HIST_AND_LUT_SUPPORT
	BOOLEAN CalcHistograms(BOOLEAN bEnable);
	BOOLEAN	GetHistogram(INT32 nBand, UINT32 Histogram[256]);
	
	BOOLEAN ApplyLUTs(BOOLEAN bEnable);
	BOOLEAN	SetLUT(INT32 nBand, UINT8 Lut[256]);
#endif // NCS_HIST_AND_LUT_SUPPORT

#ifdef NCS_MOSAIC_TOOL_SUPPORT
	void SelectImage(BOOLEAN bSelected);
#endif	//NCS_MOSAIC_TOOL_SUPPORT

protected:
	void DrawingExtents(LPRECT pClipRect,	
					    IEEE8 dWorldTLX, IEEE8 dWorldTLY, IEEE8 dWorldBRX, IEEE8 dWorldBRY,
						LPRECT pNewClipRect);	/**[01]**/
#if defined( DIB_FIX ) || defined( MACINTOSH )
	BOOLEAN	m_bCreateNewDIB;	// need to recreate DIB	/**[02]**/
#endif
private:
	
#ifdef WIN32	
	BOOLEAN CreateDIBAndPallete( HDC hDeviceContext, INT32 nWidth, INT32 nHeight );
	BOOLEAN DestroyDibAndPalette( void );
#elif defined(MACINTOSH)
	BOOLEAN CreatePixMapAndPallete( GrafPtr pGPtr, INT32 nWidth, INT32 nHeight );
	BOOLEAN DestroyPixMapAndPallete( void );
	GDHandle GetWindowDevice (WindowPtr pWindow);
#else
	#error unknown machine type
#endif

	NCSError AdjustExtents( INT32 nWidth, INT32 nHeight,
						    INT32 nDatasetTLX, INT32 nDatasetTLY, INT32 nDatatasetBRX, INT32 DatasetBRY,
						    INT32 *pnAdjustedDatasetTLX, INT32 *pnAdjustedDatasetTLY, INT32 *pnAdjustedDatatasetBRX, INT32 *pnAdjustedDatasetBRY,
						    INT32 *pnAdjustedDeviceTLX, INT32 *pnAdjustedDeviceTLY, INT32 *pnAdjustedDeviceBRX, INT32 *pnAdjustedDeviceBRY );
	NCSError AdjustExtents( INT32 nWidth, INT32 nHeight, 
						    IEEE8 dWorldTLX, IEEE8 dWorldTLY, IEEE8 dWorldBRX, IEEE8 dWorldBRY, 
						    IEEE8 *pdAdjustedWorldTLX, IEEE8 *pdAdjustedWorldTLY, IEEE8 *pdAdjustedWorldBRX, IEEE8 *pdAdjustedWorldBRY,
						    INT32 *pnAdjustedDeviceTLX, INT32 *pnAdjustedDeviceTLY, INT32 *pnAdjustedDeviceBRX, INT32 *pnAdjustedDeviceBRY );

	void CalculateDeviceCoords(INT32 nTopLeftX, INT32 nTopLeftY,INT32 nBottomRightX, INT32 nBottomRightY,
						IEEE8 dWorldTLX, IEEE8 dWorldTLY, IEEE8 dWorldBRX, IEEE8 dWorldBRY,
						IEEE8 dNewWorldTLX, IEEE8 dNewWorldTLY, IEEE8 dNewWorldBRX, IEEE8 dNewWorldBRY,
						INT32 *pnDeviceTLX, INT32 *pnDeviceTLY,	INT32 *pnDeviceBRX, INT32 *pnDeviceBRY);

	BOOLEAN CalcStretchBltCoordinates(INT32 nViewWidth, INT32 nViewHeight,
									  IEEE8 dTLX, IEEE8 dTLY, IEEE8 dBRX, IEEE8 dBRY,
									  IEEE8 outputDeviceCoords[4], IEEE8 outputImageCoords[4]);
////
	void calculateDeviceCoords(int nDeviceTLX, int nDeviceTLY,
							   int nDeviceBRX, int nDeviceBRY,
							   double dWorldTLX, double dWorldTLY,
							   double dWorldBRX, double dWorldBRY,
							   double outputDeviceCoords[4],
							   double dRendererWorldTLX, double dRendererWorldTLY,
							   double dRendererWorldBRX, double dRendererWorldBRY);
	void calculateImageCoords(double dDevice1TLX, double dDevice1TLY, double dDevice1BRX, double dDevice1BRY,
                              double dImageWidth, double dImageHeight,
                              double dDevice2TLX, double dDevice2TLY, double dDevice2BRX, double dDevice2BRY,
							  double outputImageCoords[4]);
////

	NCSError WriteJPEG(char *pFilename, UINT8 **ppBuffer, UINT32 *pBufferLength, INT32 nQuality);
#ifdef NCS_HIST_AND_LUT_SUPPORT
	BOOLEAN SetupHistograms(void);
#endif // NCS_HIST_AND_LUT_SUPPORT
	// These are the adjusted extents for when the set view is outside the range of the data
	INT32 m_nAdjustedViewWidth;
	INT32 m_nAdjustedViewHeight;
	INT32 m_nAdjustedXOffset;
	INT32 m_nAdjustedYOffset;
	IEEE8 m_dAdjustedWorldTLX;
	IEEE8 m_dAdjustedWorldTLY;
	IEEE8 m_dAdjustedWorldBRX;
	IEEE8 m_dAdjustedWorldBRY;

	// This is the actual extents of the renderer.
	INT32 m_nRendererWidth;
	INT32 m_nRendererHeight;
	INT32 m_nRendererXOffset;
	INT32 m_nRendererYOffset;	
	INT32 m_nRendererDatasetTLX;
	INT32 m_nRendererDatasetTLY;
	INT32 m_nRendererDatasetBRX;
	INT32 m_nRendererDatasetBRY;
	IEEE8 m_dRendererWorldTLX;
	IEEE8 m_dRendererWorldTLY;
	IEEE8 m_dRendererWorldBRX;
	IEEE8 m_dRendererWorldBRY;

#ifdef NCS_MOSAIC_TOOL_SUPPORT
	BOOLEAN m_bSelected;
#endif //NCS_MOSAIC_TOOL_SUPPORT

#ifdef WIN32
	HBITMAP m_hBitmap;
	BITMAPINFO *m_pbmInfo;
	UINT8 *m_pColorTable;
	PALETTEENTRY *m_pPaletteEntries;
	HINSTANCE	m_hOpenGLDLL;	
	UINT8 *m_pBitmapImage;
#elif defined(MACINTOSH)
	//PixMapHandle m_hPixMap;
	GWorldPtr m_hLocalMemDC;
	//CTabHandle m_hCTable;
#endif
	UINT8 *m_pRGBTriplets;
	UINT8 *m_pRGBTripletsLocal;
	INT32 m_nDCWidth;
	INT32 m_nDCHeight;
	INT32 m_nDCBitDepth;
	INT32 m_nBytesPerLine;
	BOOLEAN m_bHaveInit;
	COLORREF m_nBackgroundColor;
	BOOLEAN  m_bIsTransparent;
	NCSMutex m_DrawMutex;
	NCSFileViewSetInfo *pCurrentViewSetInfo;
	BOOLEAN m_bHaveReadImage;
	BOOLEAN m_bAlternateDraw;
#ifdef NCS_HIST_AND_LUT_SUPPORT
	BOOLEAN m_bCalcHistograms;
	INT32	m_nReadLine;
	typedef UINT32 Histogram[256];
	Histogram *m_pHistograms;
	NCSMutex m_HistogramMutex;
	
	BOOLEAN m_bApplyLUTs;
	BOOLEAN m_bLutChanged;
	UINT8 m_LUTs[3][256];
#endif //NCS_HIST_AND_LUT_SUPPORT
};

#endif // !defined(NCSRENDERER_H)
