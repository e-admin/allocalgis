/********************************************************
** Copyright 1999 Earth Resource Mapping Pty Ltd.
** This document contains unpublished source code of
** Earth Resource Mapping Pty Ltd. This notice does
** not indicate any intention to publish the source
** code contained herein.
**
** FILE:   	NCSTimeStamp.h
** CREATED:	Thu Feb 25 09:19:00 WST 1999
** AUTHOR: 	Simon Cope
** PURPOSE:	NCS Memory handling routines.
** EDITS:
 *******************************************************/

#ifndef NCSTIMESTAMP_H
#define NCSTIMESTAMP_H

#ifdef __cplusplus
extern "C" {
#endif

#ifndef NCSTYPES_H
#include "NCSTypes.h"
#endif

#ifdef WIN32

#include <mmsystem.h>

#endif /* WIN32 */

extern NCSTimeStampMs NCSGetTimeStampMs(void);

#ifdef __cplusplus
}
#endif

#endif /* NCSTIMESTAMP_H */
