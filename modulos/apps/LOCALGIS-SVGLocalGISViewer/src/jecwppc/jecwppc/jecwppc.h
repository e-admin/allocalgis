// jecwppc.h : main header file for the jecwppc DLL
//

#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resourceppc.h"

// CjecwppcApp
// See jecwppc.cpp for the implementation of this class
//

class CjecwppcApp : public CWinApp
{
public:
	CjecwppcApp();

// Overrides
public:
	virtual BOOL InitInstance();

	DECLARE_MESSAGE_MAP()
};

