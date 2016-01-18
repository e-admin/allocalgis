/*
*	(c) Copyright IBM Corp. 1991, 2006 All Rights Reserved
*/

#if defined(WIN32) || defined(J9WINCE)
/* windows.h defined UDATA.  Ignore its definition */
#define UDATA UDATA_win32_
#include <windows.h>
#undef UDATA	/* this is safe because our UDATA is a typedef, not a macro */
#endif

#include "j9comp.h"
#include "j9port.h"
#include "libhlp.h"

#if defined(J9ZOS390)
#include <stdlib.h> /* for malloc for atoe */
#include "atoe.h"
#endif

extern UDATA VMCALL gpProtectedMain(void *arg);

#ifdef J9VM_PORT_SIGNAL_SUPPORT
static UDATA VMCALL 
genericSignalHandler(struct J9PortLibrary* portLibrary, U_32 gpType, void* gpInfo, void* userData)
{
	PORT_ACCESS_FROM_PORT(portLibrary);
	U_32 category;

	j9tty_printf(PORTLIB, "\nAn unhandled error (%d) has occurred.\n", gpType);

	for (category=0 ; category<J9PORT_SIG_NUM_CATEGORIES ; category++) {
		U_32 infoCount = j9sig_info_count(gpInfo, category) ;
		U_32 infoKind, index;
		void *value;
		const char* name;

		for (index=0 ; index < infoCount ; index++) {
			infoKind = j9sig_info(gpInfo, category, index, &name, &value);

			switch (infoKind) {
				case J9PORT_SIG_VALUE_32:
					j9tty_printf(PORTLIB, "%s=%08.8x\n", name, *(U_32 *)value);
					break;
				case J9PORT_SIG_VALUE_64:
				case J9PORT_SIG_VALUE_FLOAT_64:
					j9tty_printf(PORTLIB, "%s=%016.16llx\n", name, *(U_64 *)value);
					break;
				case J9PORT_SIG_VALUE_STRING:
					j9tty_printf(PORTLIB, "%s=%s\n", name, (const char *)value);
					break;
				case J9PORT_SIG_VALUE_ADDRESS:
					j9tty_printf(PORTLIB, "%s=%p\n", name, *(void**)value);
					break;
			}
		}
	}

	abort();

	/* UNREACHABLE */
	return 0;
}

static UDATA VMCALL
signalProtectedMain(J9PortLibrary* portLibrary, void* arg)
{
	return gpProtectedMain(arg);
}
#endif

#if defined(WIN32) || defined(J9WINCE)
int 
translated_main(int argc, char ** argv, char ** envp)
#else
int 
main(int argc, char ** argv, char ** envp)
#endif
{
	J9PortLibrary j9portLibrary;
	J9PortLibraryVersion portLibraryVersion;
	struct j9cmdlineOptions options;
	int rc = 257;
#ifdef J9VM_PORT_SIGNAL_SUPPORT
	UDATA result;
#endif

#if defined(J9ZOS390)
	iconv_init();
	{  /* translate argv strings to ascii */
		int i;
		for (i = 0; i < argc; i++) {
			argv[i] = e2a_string(argv[i]);
		}
	}
#endif

	/* Use portlibrary version which we compiled against, and have allocated space
	 * for on the stack.  This version may be different from the one in the linked DLL.
	 */
	J9PORT_SET_VERSION(&portLibraryVersion, J9PORT_CAPABILITY_MASK);
	if (0 == j9port_init_library(&j9portLibrary, &portLibraryVersion, sizeof(J9PortLibrary))) {
		options.argc = argc;
		options.argv = argv;
		options.envp = envp;
		options.portLibrary = &j9portLibrary;

#ifdef J9VM_PORT_SIGNAL_SUPPORT
		if (j9portLibrary.sig_protect(&j9portLibrary, 
			signalProtectedMain, &options,
			genericSignalHandler, NULL,
			J9PORT_SIG_FLAG_SIGALLSYNC, 
			&result) == 0
		) {
			rc = result;
		}
#else
		rc = j9portLibrary.gp_protect(&j9portLibrary, gpProtectedMain, &options);
#endif

		j9portLibrary.port_shutdown_library(&j9portLibrary);
	}

	return rc;
}

#if defined(WIN32) || defined(J9WINCE)
int 
wmain(int argc, wchar_t ** argv, wchar_t ** envp)
{
	char **translated_argv = NULL;
	char **translated_envp = NULL;
	char* cursor;
	int i, length, envc;
	int rc;

	/* Translate argv to UTF-8 */
	length = argc;	/* 1 null terminator per string */
	for(i = 0; i < argc; i++) {
		length += wcslen(argv[i]) * 3;
	}
	translated_argv = (char**)malloc(length + ((argc + 1) * sizeof(char*))); /* + array entries */
	cursor = (char*)&translated_argv[argc + 1];
	for(i = 0; i < argc; i++) {
		int utf8Length;
		
		translated_argv[i] = cursor;
		if(0 == (utf8Length = WideCharToMultiByte(OS_ENCODING_CODE_PAGE, OS_ENCODING_WC_FLAGS, argv[i], -1, cursor, length, NULL, NULL))) {
			return -1;
		}
		cursor += utf8Length;
		*cursor++ = '\0';
		length -= utf8Length;
	}
	translated_argv[argc] = NULL;	/* NULL terminated the new argv */

	/* Translate argv to UTF-8 */
	if(envp) {
		envc = 0;
		while(NULL != envp[envc]) {
			envc++;
		}
		length = envc;	/* 1 null terminator per string */
		for(i = 0; i < envc; i++) {
			length += wcslen(envp[i]) * 3;
		}
		translated_envp = (char**)malloc(length + ((envc + 1) * sizeof(char*))); /* + array entries */
		cursor = (char*)&translated_envp[envc + 1];
		for(i = 0; i < envc; i++) {
			int utf8Length;

			translated_envp[i] = cursor;
			if(0 == (utf8Length = WideCharToMultiByte(OS_ENCODING_CODE_PAGE, OS_ENCODING_WC_FLAGS, envp[i], -1, cursor, length, NULL, NULL))) {
				return -1;
			}
			cursor += utf8Length;
			*cursor++ = '\0';
			length -= utf8Length;
		}
		translated_envp[envc] = NULL;	/* NULL terminated the new envp */
	}
		
	rc = translated_main(argc, translated_argv, translated_envp);

	/* Free the translated strings */
	free(translated_argv);
	free(translated_envp);

	return rc;
}
#endif
