/********************************************************
** Copyright 1999 Earth Resource Mapping Pty Ltd.
** This document contains unpublished source code of
** Earth Resource Mapping Pty Ltd. This notice does
** not indicate any intention to publish the source
** code contained herein.
**
** FILE:   	NCSDefs.h
** CREATED:	Tue Mar 2 09:19:00 WST 1999
** AUTHOR: 	Simon Cope
** PURPOSE:	General NCS defines
** EDITS:
** [01] sjc 30Apr00 Merged Mac SDK port
** [02]  ny 03Nov00 Merge WinCE/PALM SDK changes
 *******************************************************/

#ifndef NCSDEFS_H
#define NCSDEFS_H

#ifdef __cplusplus
extern "C" {
#endif

#ifndef NCSTYPES_H
#include "NCSTypes.h"
#endif

#include <float.h>

#ifdef LINUX
#ifndef NULL
#define NULL 0
#endif
#endif

#ifdef HPUX
// This should be defined as "inline", but the compiler doenst 
// like our "static inline type function()" prototypes, probably 
// have to change them all to static type inline function()"
// Use the next line when this is sorted.
//#define __inline inline
#define __inline
#endif

#if defined(MACINTOSH)||defined(SOLARIS)||defined(IRIX)||defined(PALM)||defined(HPUX)
#define NCSBO_MSBFIRST
#else	// WIN32, LINUX (i386)
#define NCSBO_LSBFIRST
#endif

#ifndef _WIN32_WCE
#define UNALIGNED
#endif	/* !_WIN32_WCE */

#ifdef PALM
// FIXME
#define NCS_PALM_CREATOR_ID 'NCS1'
#endif

#ifdef HPUX
// The system headers clash, so use their versions of MAX/MIN instead.
#include <sys/param.h>

#else

#ifndef MAX
#define MAX(a, b) ((a) > (b) ? (a) : (b))
#endif
#ifndef MIN
#define MIN(a, b) ((a) > (b) ? (b) : (a))
#endif

#endif

#ifndef TRUE
#define TRUE 1
#endif
#ifndef FALSE
#define FALSE 0
#endif

#ifdef WIN32

#ifndef MAXDOUBLE
#define MAXDOUBLE   DBL_MAX			//1.7976931348623158e+308
#define NCS_FQNAN	_FPCLASS_QNAN	//0x0002
#define NCS_NAN		_FPCLASS_SNAN
#endif	/* !MAXDOUBLE */

#if defined(_WIN32_WCE)
#define NCS_NO_UNALIGNED_ACCESS
#endif // _WIN32_WCE

#elif defined PALM

#define NCS_FQNAN	0x0002

#elif defined MACINTOSH

#define NCS_FQNAN	0x0002
#define NCS_NAN		NAN
#ifndef MAXDOUBLE
#define MAXDOUBLE   DBL_MAX
#endif

#elif defined SOLARIS || defined LINUX || defined HPUX

#include <values.h>
#include <limits.h>
#include <ctype.h>

// Solaris can't access types on unaligned addressed
#define NCS_NO_UNALIGNED_ACCESS

#if defined(SOLARIS) || defined(HPUX)
// SPARC has slow BYTE bit ops
#define NCS_SLOW_CPU_BYTE_OPS
#endif

#else

#error DEFINE SYSTEM INCLUDES FOR TYPES

#endif	/* WIN32 */

typedef enum {
	NCSCS_RAW	= 0,
	NCSCS_UTM	= 1,
	NCSCS_LL	= 2
} NCSCoordSys;

/*Coodinate system defines*/
#define NCS_LINEAR_COORD_SYS	"linear"
#define	NCS_FEET_FACTOR			0.30480061
#ifdef WIN32
#define NCS_EXPORT __declspec(dllexport)
#define NCS_IMPORT __declspec(dllimport)
#ifdef _WIN32_WCE
#define NCS_CALL
#else
#define NCS_CALL __cdecl
#endif
#define NCS_CB_CALL __cdecl
#else
#define NCS_EXPORT
#define NCS_IMPORT
#define NCS_CALL
#define NCS_CB_CALL
#endif

#ifndef MAX_PATH
#ifdef MACINTOSH

	//	Note: Verify that this is OK for all MAC/OS Platform
#define MAX_PATH	256

#elif defined PALM

#define MAX_PATH 	1024

#elif defined SOLARIS || defined LINUX || defined HPUX

#define MAX_PATH	PATH_MAX

#else	/* PALM */

#define MAX_PATH	PATHNAMELEN

#endif
#endif	/* !MAX_PATH */

#define NCSIsNullString(s) ((s) == (char *)0 || (*(s)) == '\0')

#ifdef __cplusplus
}
#endif

#endif /* NCSDEFS_H */
