// jecw.cpp : Defines the initialization routines for the DLL.
//

#include "stdafx.h"
#include "jecw.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

//
//TODO: If this DLL is dynamically linked against the MFC DLLs,
//		any functions exported from this DLL which call into
//		MFC must have the AFX_MANAGE_STATE macro added at the
//		very beginning of the function.
//
//		For example:
//
//		extern "C" BOOL PASCAL EXPORT ExportedFunction()
//		{
//			AFX_MANAGE_STATE(AfxGetStaticModuleState());
//			// normal function body here
//		}
//
//		It is very important that this macro appear in each
//		function, prior to any calls into MFC.  This means that
//		it must appear as the first statement within the 
//		function, even before any object variable declarations
//		as their constructors may generate calls into the MFC
//		DLL.
//
//		Please see MFC Technical Notes 33 and 58 for additional
//		details.
//


// CjecwApp

BEGIN_MESSAGE_MAP(CjecwApp, CWinApp)
END_MESSAGE_MAP()


// CjecwApp construction

CjecwApp::CjecwApp()
{
	// TODO: add construction code here,
	// Place all significant initialization in InitInstance
}


// The one and only CjecwApp object

CjecwApp theApp;


// CjecwApp initialization

BOOL CjecwApp::InitInstance()
{
	CWinApp::InitInstance();

	return TRUE;
}

/* Funciones JNI para la lectura de imágenes en formato ECW */

#include <NCSECWClient.h>
#include "JNCSFile.h"

static JavaVM *pJavaVirtualMachineInst = NULL;
struct NCSJNIFieldIDs *pGlobalJNCSFieldIDs = NULL;

typedef struct NCSJNIFieldIDs {
	jfieldID jIDNativeDataPointer;
	jfieldID jIDWidth;
	jfieldID jIDHeight;	
	jfieldID jIDNumberOfBands;
	jfieldID jIDCompressionRate;
	jfieldID jIDCellIncrementX;
	jfieldID jIDCellIncrementY;
	jfieldID jIDCellSizeUnits;
	jfieldID jIDOriginX;
	jfieldID jIDOriginY;
	jfieldID jIDDatum;
	jfieldID jIDProjection;
	jfieldID jIDFilename;
	jfieldID jIDIsOpen;
	jmethodID jIDRefreshUpdateMethod;
	jfieldID jIDFileType;
	jfieldID jIDFileMimeType;
} NCSJNIFieldIDs;

// This is the object specific data structure, its cached.
typedef struct NCSJNIInfo {
	//jobject  ECWFile;
	NCSFileView *pFileView;
} NCSJNIInfo;

/*void NCSJNIThrowException(JNIEnv *pEnv, const char *pSignature, const char *pMessage)
{
	jclass jExceptionClass = NULL;

	if (pEnv->ExceptionCheck()) {
		pEnv->ExceptionDescribe();
		pEnv->ExceptionClear();
	}

	jExceptionClass = pEnv->FindClass(pSignature);

	if (jExceptionClass != NULL) {
		pEnv->ThrowNew(jExceptionClass, pMessage); 
	}
	pEnv->DeleteLocalRef(jExceptionClass);
}*/

NCSError NCSCreateJNIInfoStruct(JNIEnv *pEnv, jobject ECWFile, NCSFileView *pNCSFileView, NCSJNIInfo **pReturn)
{
	NCSJNIInfo *pJNIInfo;
	//char *pErrorString = NULL;
	
	//pJNIInfo = (NCSJNIInfo *)NCSMalloc(sizeof(NCSJNIInfo), TRUE);
	pJNIInfo = (NCSJNIInfo *) malloc(sizeof(NCSJNIInfo));
	if (pJNIInfo != NULL)
	{
		pJNIInfo->pFileView = pNCSFileView;
		//pJNIInfo->ECWFile = (void *)(*pEnv)->NewGlobalRef(pEnv, ECWFile);
		*pReturn = pJNIInfo;
	}
	else
	{
		return NCS_COULDNT_ALLOC_MEMORY;
	}
	return NCS_SUCCESS;
}


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *pJVM, void *reserved)
{
	pJavaVirtualMachineInst = pJVM;
	NCSecwInit();

	return 1;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *pJVM, void *reserved)
{
	NCSecwShutdown();
//	NCSFree((void *)pGlobalJNCSFieldIDs);
	if (pGlobalJNCSFieldIDs!=NULL) free(pGlobalJNCSFieldIDs);
	return;
}

/* Inicializa la librería de decompresión de imágenes ECW */
JNIEXPORT jint JNICALL Java_com_ermapper_ecw_JNCSFile_NCSJNIInit
  (JNIEnv *pEnv, jclass ECWFileClass)
{
	NCSJNIFieldIDs *pJNIInfo = NULL;
	char *pErrorString = NULL;

	if (!pGlobalJNCSFieldIDs) {

		//pJNIInfo = (NCSJNIFieldIDs *)NCSMalloc(sizeof(NCSJNIFieldIDs), TRUE);
		pJNIInfo = (NCSJNIFieldIDs *) malloc(sizeof(NCSJNIFieldIDs));

		// Get all the field ids of the ECWFile object
		pJNIInfo->jIDNativeDataPointer	= pEnv->GetFieldID(ECWFileClass, "nativeDataPointer", "J" );
		pJNIInfo->jIDWidth				= pEnv->GetFieldID(ECWFileClass, "width", "I" );
		pJNIInfo->jIDHeight				= pEnv->GetFieldID(ECWFileClass, "height", "I" );
		pJNIInfo->jIDNumberOfBands		= pEnv->GetFieldID(ECWFileClass, "numBands", "I" );
		pJNIInfo->jIDCompressionRate	= pEnv->GetFieldID(ECWFileClass, "compressionRate", "D" );
		pJNIInfo->jIDCellIncrementX		= pEnv->GetFieldID(ECWFileClass, "cellIncrementX", "D" );
		pJNIInfo->jIDCellIncrementY		= pEnv->GetFieldID(ECWFileClass, "cellIncrementY", "D" );
		pJNIInfo->jIDCellSizeUnits		= pEnv->GetFieldID(ECWFileClass, "cellSizeUnits", "I" );
		pJNIInfo->jIDOriginX			= pEnv->GetFieldID(ECWFileClass, "originX", "D" );
		pJNIInfo->jIDOriginY			= pEnv->GetFieldID(ECWFileClass, "originY", "D" );
		pJNIInfo->jIDDatum				= pEnv->GetFieldID(ECWFileClass, "datum", "Ljava/lang/String;" );
		pJNIInfo->jIDProjection			= pEnv->GetFieldID(ECWFileClass, "projection", "Ljava/lang/String;" );
		pJNIInfo->jIDFilename			= pEnv->GetFieldID(ECWFileClass, "fileName", "Ljava/lang/String;" );
		pJNIInfo->jIDIsOpen				= pEnv->GetFieldID(ECWFileClass, "bIsOpen", "Z" );
		pJNIInfo->jIDRefreshUpdateMethod= pEnv->GetMethodID(ECWFileClass, "refreshUpdate", "(IIDDDD)V");

		//pJNIInfo->jIDFileType			= (*pEnv)->GetFieldID(pEnv, ECWFileClass, "fileType", "I" );
		//pJNIInfo->jIDFileMimeType		= (*pEnv)->GetFieldID(pEnv, ECWFileClass, "mimeType", "Ljava/lang/String;" );


		// Do some error checking
		if (!pJNIInfo->jIDNativeDataPointer)
			pErrorString = "Could not determine fieldID for 'nativeDataPointer' in ECWFile object.";
		if (!pJNIInfo->jIDWidth)
			pErrorString = "Could not determine fieldID for 'width' in ECWFile object.";
		if (!pJNIInfo->jIDHeight)
			pErrorString = "Could not determine fieldID for 'height' in ECWFile object.";
		if (!pJNIInfo->jIDNumberOfBands)
			pErrorString = "Could not determine fieldID for 'numBands' in ECWFile object.";
		if (!pJNIInfo->jIDCompressionRate)
			pErrorString = "Could not determine fieldID for 'compressionRate' in ECWFile object.";
		if (!pJNIInfo->jIDCellIncrementX)
			pErrorString = "Could not determine fieldID for 'cellIncrementX' in ECWFile object.";
		if (!pJNIInfo->jIDCellIncrementY)
			pErrorString = "Could not determine fieldID for 'cellIncrementY' in ECWFile object.";
		if (!pJNIInfo->jIDCellSizeUnits)
			pErrorString = "Could not determine fieldID for 'cellSizeUnits' in ECWFile object.";
		if (!pJNIInfo->jIDOriginX)
			pErrorString = "Could not determine fieldID for 'originX' in ECWFile object.";
		if (!pJNIInfo->jIDOriginY)
			pErrorString = "Could not determine fieldID for 'originY' in ECWFile object.";
		if (!pJNIInfo->jIDDatum)
			pErrorString = "Could not determine fieldID for 'datum' in ECWFile object.";
		if (!pJNIInfo->jIDProjection)
			pErrorString = "Could not determine fieldID for 'projection' in ECWFile object.";
		if (!pJNIInfo->jIDFilename)
			pErrorString = "Could not determine fieldID for 'fileName' in ECWFile object.";
		if (!pJNIInfo->jIDIsOpen)
			pErrorString = "Could not determine fieldID for 'bIsOpen' in ECWFile object.";
		/*if (!pJNIInfo->jIDFileType)
			pErrorString = "Could not determine fieldID for 'fileType' in ECWFile object.";
		if (!pJNIInfo->jIDFileMimeType)
			pErrorString = "Could not determine fieldID for 'mimeType' in ECWFile object.";*/
			
		if (pErrorString) {
//#ifdef WIN32
//			MessageBox(NULL, OS_STRING(pErrorString), TEXT("JNCSClass Library (JNI)"), MB_OK);
//#else
			fprintf(stderr, "JNCSClass Library (JNI) : %s\n", pErrorString);
//#endif
			NCSFormatErrorText(NCS_JNI_ERROR, pErrorString);
			return NCS_JNI_ERROR;
		}
		else {
			pGlobalJNCSFieldIDs = pJNIInfo;
			return NCS_SUCCESS;
		}
	}
	else {
		return NCS_SUCCESS;
	}
}

/* Abre un fichero ECW */
int openFile(JNIEnv *pEnv, jobject *JNCSFile, jstring Filename, char *pFilename, jboolean bProgressive, NCSFileViewSetInfo *pNCSFileViewSetInfo, NCSFileView *pNCSFileView){
	NCSError nError;
		
//	if (bProgressive) {
//		nError = NCScbmOpenFileView((char *)pFilename, &pNCSFileView, NCSJNIRefreshCallback);
//	}
//	else {
		nError = NCScbmOpenFileView((char *)pFilename, &pNCSFileView, NULL);
//	}


	if (NCS_FAILED(nError)) {
		// Return the short filename, since people dont like diplaying the full ecwp url
		char	*pProtocol, *pHost, *pECWFilename, *pShortFileName;
		int		nProtocolLength, nHostLength, nFilenameLength;

		NCSecwNetBreakdownUrl((char *)pFilename, &pProtocol, &nProtocolLength,
									 &pHost, &nHostLength,
									 &pECWFilename, &nFilenameLength);

		if (pECWFilename && nFilenameLength > 0 && (strstr(pECWFilename, "/") || strstr(pECWFilename, "\\")))
		{
			int len = strlen(pECWFilename), index = 0;
			for (index=len; index > 0; index--)
			{
				pShortFileName = &pECWFilename[index];
				if (pECWFilename[index] == '\\' || pECWFilename[index] == '/')
				{
					pShortFileName ++;
					break;
				}
			}
		}
		else 
		{
			pShortFileName = (char *)pFilename;
		}
		
		NCSFormatErrorText(NCS_FILE_OPEN_FAILED, pShortFileName, NCSGetLastErrorText(nError));
		return NCS_FILE_OPEN_FAILED;
	}
	else {
		NCSJNIInfo *pJNIInfo = NULL;
		char *pMimeType = NULL;
		NCSFileViewFileInfo	*pNCSFileInfo = NULL;
		
		nError = NCSCreateJNIInfoStruct(pEnv, (*JNCSFile), pNCSFileView, &pJNIInfo);
		NCScbmGetViewFileInfo(pNCSFileView, &pNCSFileInfo);

		// Set the properties in the actual Java object
		pEnv->SetIntField((*JNCSFile), pGlobalJNCSFieldIDs->jIDWidth, (jint)pNCSFileInfo->nSizeX	);
		pEnv->SetIntField((*JNCSFile), pGlobalJNCSFieldIDs->jIDHeight, (jint)pNCSFileInfo->nSizeY );
		pEnv->SetIntField((*JNCSFile), pGlobalJNCSFieldIDs->jIDNumberOfBands, (jint)pNCSFileInfo->nBands);
		pEnv->SetDoubleField((*JNCSFile), pGlobalJNCSFieldIDs->jIDCompressionRate, (jdouble)pNCSFileInfo->nCompressionRate );
		pEnv->SetDoubleField((*JNCSFile), pGlobalJNCSFieldIDs->jIDCellIncrementX, (jdouble)pNCSFileInfo->fCellIncrementX );
		pEnv->SetDoubleField((*JNCSFile), pGlobalJNCSFieldIDs->jIDCellIncrementY, (jdouble)pNCSFileInfo->fCellIncrementY );
		pEnv->SetIntField((*JNCSFile), pGlobalJNCSFieldIDs->jIDCellSizeUnits, (jint)pNCSFileInfo->eCellSizeUnits);
		pEnv->SetDoubleField((*JNCSFile), pGlobalJNCSFieldIDs->jIDOriginX, (jdouble)pNCSFileInfo->fOriginX );
		pEnv->SetDoubleField((*JNCSFile), pGlobalJNCSFieldIDs->jIDOriginY, (jdouble)pNCSFileInfo->fOriginY );
		pEnv->SetObjectField((*JNCSFile), pGlobalJNCSFieldIDs->jIDDatum, pEnv->NewStringUTF(pNCSFileInfo->szDatum));
		pEnv->SetObjectField((*JNCSFile), pGlobalJNCSFieldIDs->jIDProjection, pEnv->NewStringUTF(pNCSFileInfo->szProjection));
		pEnv->SetObjectField((*JNCSFile), pGlobalJNCSFieldIDs->jIDFilename, Filename);
		pEnv->SetBooleanField((*JNCSFile), pGlobalJNCSFieldIDs->jIDIsOpen, JNI_TRUE);
		pEnv->SetLongField((*JNCSFile), pGlobalJNCSFieldIDs->jIDNativeDataPointer, (jlong)pJNIInfo);

	}
	return NCS_SUCCESS;
}

JNIEXPORT jint JNICALL Java_com_ermapper_ecw_JNCSFile_ECWOpen
  (JNIEnv *pEnv, jobject JNCSFile, jstring Filename, jboolean bProgressive){
	const char *pFilename = pEnv->GetStringUTFChars(Filename, (jboolean *)NULL);
	NCSFileView *pNCSFileView = NULL;
	NCSFileViewSetInfo *pNCSFileViewSetInfo = NULL;
	int ret;

	ret = openFile(pEnv, &JNCSFile, Filename, (char *)pFilename, bProgressive, pNCSFileViewSetInfo, pNCSFileView);
	
	pEnv->ReleaseStringUTFChars(Filename, (const char *)pFilename);

	NCScbmGetViewInfo(pNCSFileView, &pNCSFileViewSetInfo);

	pNCSFileViewSetInfo->pClientData = (void *)pEnv->NewGlobalRef(JNCSFile);

	return ret;
}

JNIEXPORT jint JNICALL Java_com_ermapper_ecw_JNCSFile_ECWOpenArray
  (JNIEnv *pEnv, jobject JNCSFile, jstring Filename, jboolean bProgressive, jbyteArray pszF){
  	
	NCSFileView *pNCSFileView = NULL;
	NCSFileViewSetInfo *pNCSFileViewSetInfo = NULL;
	jbyte *pFilename, *aux;
	int longitud = 0;
	NCSError nError;
  	
  	longitud = pEnv->GetArrayLength(pszF); 
  	aux = pEnv->GetByteArrayElements(pszF, 0);
  	pFilename = (jbyte *)malloc(sizeof(jbyte)*(longitud + 1));
  	strncpy((char *) pFilename, (char *) aux, longitud);
 	//pFilename = (jbyte *)realloc(pFilename, longitud + 1);
  	pFilename[longitud] = '\0';
  	
//  	if (bProgressive) {
//		nError = NCScbmOpenFileView((char *)pFilename, &pNCSFileView, NCSJNIRefreshCallback);
//	}else {
		nError = NCScbmOpenFileView((char *)pFilename, &pNCSFileView, NULL);
//	}


	if (NCS_FAILED(nError)) {
		// Return the short filename, since people dont like diplaying the full ecwp url
		char	*pProtocol, *pHost, *pECWFilename, *pShortFileName;
		int		nProtocolLength, nHostLength, nFilenameLength;

		NCSecwNetBreakdownUrl((char *)pFilename, &pProtocol, &nProtocolLength,
									 &pHost, &nHostLength,
									 &pECWFilename, &nFilenameLength);

		if (pECWFilename && nFilenameLength > 0 && (strstr(pECWFilename, "/") || strstr(pECWFilename, "\\")))
		{
			int len = strlen(pECWFilename), index = 0;
			for (index=len; index > 0; index--)
			{
				pShortFileName = &pECWFilename[index];
				if (pECWFilename[index] == '\\' || pECWFilename[index] == '/')
				{
					pShortFileName ++;
					break;
				}
			}
		}
		else 
		{
			pShortFileName = (char *)pFilename;
		}
		
		NCSFormatErrorText(NCS_FILE_OPEN_FAILED, pShortFileName, NCSGetLastErrorText(nError));
		return NCS_FILE_OPEN_FAILED;
	}
	else {
		NCSJNIInfo *pJNIInfo = NULL;
		char *pMimeType = NULL;
		NCSFileViewFileInfo	*pNCSFileInfo = NULL;
		
		nError = NCSCreateJNIInfoStruct(pEnv, JNCSFile, pNCSFileView, &pJNIInfo);
		NCScbmGetViewFileInfo(pNCSFileView, &pNCSFileInfo);

		// Set the properties in the actual Java object
		pEnv->SetIntField(JNCSFile, pGlobalJNCSFieldIDs->jIDWidth, (jint)pNCSFileInfo->nSizeX	);
		pEnv->SetIntField(JNCSFile, pGlobalJNCSFieldIDs->jIDHeight, (jint)pNCSFileInfo->nSizeY );
		pEnv->SetIntField(JNCSFile, pGlobalJNCSFieldIDs->jIDNumberOfBands, (jint)pNCSFileInfo->nBands);
		pEnv->SetDoubleField(JNCSFile, pGlobalJNCSFieldIDs->jIDCompressionRate, (jdouble)pNCSFileInfo->nCompressionRate );
		pEnv->SetDoubleField(JNCSFile, pGlobalJNCSFieldIDs->jIDCellIncrementX, (jdouble)pNCSFileInfo->fCellIncrementX );
		pEnv->SetDoubleField(JNCSFile, pGlobalJNCSFieldIDs->jIDCellIncrementY, (jdouble)pNCSFileInfo->fCellIncrementY );
		pEnv->SetIntField(JNCSFile, pGlobalJNCSFieldIDs->jIDCellSizeUnits, (jint)pNCSFileInfo->eCellSizeUnits);
		pEnv->SetDoubleField(JNCSFile, pGlobalJNCSFieldIDs->jIDOriginX, (jdouble)pNCSFileInfo->fOriginX );
		pEnv->SetDoubleField(JNCSFile, pGlobalJNCSFieldIDs->jIDOriginY, (jdouble)pNCSFileInfo->fOriginY );
		pEnv->SetObjectField(JNCSFile, pGlobalJNCSFieldIDs->jIDDatum, pEnv->NewStringUTF(pNCSFileInfo->szDatum));
		pEnv->SetObjectField(JNCSFile, pGlobalJNCSFieldIDs->jIDProjection, pEnv->NewStringUTF(pNCSFileInfo->szProjection));
		pEnv->SetObjectField(JNCSFile, pGlobalJNCSFieldIDs->jIDFilename, Filename);
		pEnv->SetBooleanField(JNCSFile, pGlobalJNCSFieldIDs->jIDIsOpen, JNI_TRUE);
		pEnv->SetLongField(JNCSFile, pGlobalJNCSFieldIDs->jIDNativeDataPointer, (jlong)pJNIInfo);

	}
  	
  	
	//ret = openFile(pEnv, &JNCSFile, Filename, (char *)pszFilename, bProgressive, pNCSFileViewSetInfo, pNCSFileView);
	
	pEnv->ReleaseByteArrayElements(pszF,(jbyte *)pFilename, 0);

	NCScbmGetViewInfo(pNCSFileView, &pNCSFileViewSetInfo);

	pNCSFileViewSetInfo->pClientData = (void *)pEnv->NewGlobalRef(JNCSFile);

	return NCS_SUCCESS;
}

/* Cierra el fichero */
JNIEXPORT void JNICALL Java_com_ermapper_ecw_JNCSFile_ECWClose
  (JNIEnv *pEnv , jobject JNCSFile, jboolean bFreeCache)
{
	NCSError nError;
	NCSJNIInfo *pJNIInfo = NULL;
	
	pJNIInfo = (NCSJNIInfo *)pEnv->GetLongField(JNCSFile, pGlobalJNCSFieldIDs->jIDNativeDataPointer);
	
	if (pJNIInfo != NULL) {
		NCSFileViewSetInfo	*pViewInfo;

		NCScbmGetViewInfo(pJNIInfo->pFileView, &pViewInfo);
	
		// Clear the Java object members.
		pEnv->SetIntField   (JNCSFile, pGlobalJNCSFieldIDs->jIDWidth, (jint)0	);
		pEnv->SetIntField   (JNCSFile, pGlobalJNCSFieldIDs->jIDHeight, (jint)0 );
		pEnv->SetIntField   (JNCSFile, pGlobalJNCSFieldIDs->jIDNumberOfBands, (jint)0);
		pEnv->SetDoubleField(JNCSFile, pGlobalJNCSFieldIDs->jIDCompressionRate, (jdouble)0.0 );
		pEnv->SetDoubleField(JNCSFile, pGlobalJNCSFieldIDs->jIDCellIncrementX, (jdouble)0.0 );
		pEnv->SetDoubleField(JNCSFile, pGlobalJNCSFieldIDs->jIDCellIncrementY, (jdouble)0.0 );
		pEnv->SetIntField   (JNCSFile, pGlobalJNCSFieldIDs->jIDCellSizeUnits, (jint)0 );
		pEnv->SetDoubleField(JNCSFile, pGlobalJNCSFieldIDs->jIDOriginX, (jdouble)0.0 );
		pEnv->SetDoubleField(JNCSFile, pGlobalJNCSFieldIDs->jIDOriginY, (jdouble)0.0 );
		pEnv->SetObjectField(JNCSFile, pGlobalJNCSFieldIDs->jIDDatum, pEnv->NewStringUTF(""));
		pEnv->SetObjectField(JNCSFile, pGlobalJNCSFieldIDs->jIDProjection, pEnv->NewStringUTF(""));
		pEnv->SetObjectField(JNCSFile, pGlobalJNCSFieldIDs->jIDFilename, NULL);
		pEnv->SetBooleanField(JNCSFile, pGlobalJNCSFieldIDs->jIDIsOpen, JNI_FALSE);
		pEnv->SetLongField  (JNCSFile, pGlobalJNCSFieldIDs->jIDNativeDataPointer, (jlong)0);

// Estas dos funciones estaban sin comentar y dan problemas, pues estos campos no los tratan en otras funciones
//		pEnv->SetIntField(JNCSFile, pGlobalJNCSFieldIDs->jIDFileType, (jint)0 );
//		pEnv->SetObjectField(JNCSFile, pGlobalJNCSFieldIDs->jIDFileMimeType, NULL );

		// Clean up any global refs
		//(*pEnv)->DeleteGlobalRef(pEnv, pJNIInfo->ECWFile);
		pEnv->DeleteGlobalRef((jobject) pViewInfo->pClientData);

		nError = NCScbmCloseFileViewEx(pJNIInfo->pFileView, bFreeCache);

		pJNIInfo->pFileView = NULL;
		//NCSFree(pJNIInfo);
		if (pJNIInfo!=NULL) free(pJNIInfo);
	}
	return;
}

/* Establece la view actual para la imagen */
JNIEXPORT jint JNICALL Java_com_ermapper_ecw_JNCSFile_ECWSetView
  (JNIEnv *pEnv, jobject ECWFile, jint nBands, jintArray nBandList, jint nDatasetTLX, jint nDatasetTLY, jint nDatasetBRX, jint nDatasetBRY, jdouble dWorldTLX, jdouble dWorldTLY, jdouble dWorldBRX, jdouble dWorldBRY, jint nWidth, jint nHeight)
{
	NCSError nError = NCS_SUCCESS;
	NCSJNIInfo *pJNIInfo = NULL;

	// Sanity check
	if (pEnv->IsInstanceOf(ECWFile, pEnv->FindClass("com/ermapper/ecw/JNCSFile")) == JNI_FALSE)
	{
//#ifdef WIN32
//		MessageBox(NULL, OS_STRING("ECWSetView() error : object is not a JNCSFile instance"), TEXT("JNCSClass Library (JNI)"), MB_OK);
//#endif //WIN32
		return NCS_JNI_ERROR;
	}

	pJNIInfo = (NCSJNIInfo *)pEnv->GetLongField(ECWFile, pGlobalJNCSFieldIDs->jIDNativeDataPointer);

	if (pJNIInfo) {
		
		//jint *pBandBuffer = (jint *)NCSMalloc(sizeof(UINT32)*nBands+1, TRUE);
		jint *pBandBuffer = (jint *) malloc(sizeof(UINT32)*nBands+1);
		pEnv->GetIntArrayRegion(nBandList, 0, nBands, pBandBuffer);
		
		nError = NCScbmSetFileViewEx(   pJNIInfo->pFileView, 
										nBands, 
										(UINT32*)pBandBuffer,
										nDatasetTLX, nDatasetTLY, nDatasetBRX, nDatasetBRY,
										nWidth,
										nHeight,
										dWorldTLX,
										dWorldTLY,
										dWorldBRX,
										dWorldBRY);
		//NCSFree(pBandBuffer);
		if (pBandBuffer!=NULL) free(pBandBuffer);

	} else {
		NCSFormatErrorText(NCS_JNI_ERROR, "method SetView() could not get native data from JNCSFile object.");
		nError = NCS_JNI_ERROR;
	}
		
	return nError;
}

/* Lee vista actual de la imagen completa y la devuelve en formato RGBA */
JNIEXPORT jint JNICALL Java_com_ermapper_ecw_JNCSFile_ECWReadImageRGBA
  (JNIEnv *pEnv, jobject JNCSFile, jintArray pRGBArray, jint width, jint height)
{
	jboolean bIsCopy;
	NCSEcwReadStatus eStatus;
	NCSJNIInfo *pJNIInfo = NULL;
	NCSError nError = NCS_SUCCESS;
	jint *pRGBAPixels;
	jint *pRGBLineArrayPtr = NULL;
	int nIndex;

	pJNIInfo = (NCSJNIInfo *)pEnv->GetLongField(JNCSFile, pGlobalJNCSFieldIDs->jIDNativeDataPointer);
	
	if (!pJNIInfo) {
		NCSFormatErrorText(NCS_JNI_ERROR, "method readLineRGB() could not get native data from JNCSFile object.");
		nError = NCS_JNI_ERROR;
	}
	else {
		// Lock the primitive array and get a pointer to the memory.
		pRGBAPixels = (jint *)pEnv->GetIntArrayElements(pRGBArray, &bIsCopy);//GetPrimitiveArrayCritical(pRGBArray, &bIsCopy);

		if (pRGBAPixels) {

			pRGBLineArrayPtr = pRGBAPixels;
			for (nIndex = 0; nIndex < height; nIndex ++) {
#if defined(NCSBO_LSBFIRST)
				eStatus = NCScbmReadViewLineBGRA( pJNIInfo->pFileView, (UINT32 *)pRGBLineArrayPtr);
#elif defined(NCSBO_MSBFIRST)
				eStatus = NCScbmReadViewLineBGRA( pJNIInfo->pFileView, (UINT32 *)pRGBLineArrayPtr);
				//eStatus = NCScbmReadViewLineRGBA( pJNIInfo->pFileView, (UINT32 *)pRGBLineArrayPtr);
#endif
				pRGBLineArrayPtr += width;

				if (eStatus == NCSECW_READ_CANCELLED) {
					nError = NCS_SUCCESS;
					break;
				}
				if (eStatus == NCSECW_READ_FAILED) {
					NCSFormatErrorText(NCS_JNI_ERROR, "method readLineRGB() failed (internal error).");
					nError = NCS_JNI_ERROR;
					break;
				}
#ifndef WIN32
				NCSThreadYield();
#endif
			}
		}
		else {
			NCSFormatErrorText(NCS_JNI_ERROR, "method readLineRGB() could not allocate memory for RGB Array.");
			nError = NCS_JNI_ERROR;
		}
		// Copy the array back to the object and free the memory
		pEnv->ReleaseIntArrayElements(pRGBArray, pRGBAPixels, 0); //ReleasePrimitiveArrayCritical(pRGBArray, pRGBAPixels, 0);
	}
	return nError;
}

/* Lee la vista actual linea a linea devolviendola en formato RGBA */
JNIEXPORT jint JNICALL Java_com_ermapper_ecw_JNCSFile_ECWReadLineRGBA
  (JNIEnv *pEnv, jobject JNCSFile, jintArray pRGBArray)
{
	jboolean bIsCopy;
	NCSEcwReadStatus eStatus;
	NCSJNIInfo *pJNIInfo = NULL;
	NCSError nError = NCS_SUCCESS;
	jint *pRGBAPixels;

	pJNIInfo = (NCSJNIInfo *)pEnv->GetLongField(JNCSFile, pGlobalJNCSFieldIDs->jIDNativeDataPointer);
	
	if (!pJNIInfo) {
		NCSFormatErrorText(NCS_JNI_ERROR, "method readLineRGB() could not get native data from JNCSFile object.");
		nError = NCS_JNI_ERROR;
	}
	else {
		// Lock the primitive array and get a pointer to the memory.
		pRGBAPixels = (jint *)pEnv->GetIntArrayElements(pRGBArray, &bIsCopy); //GetPrimitiveArrayCritical(pRGBArray, &bIsCopy);

		if (pRGBAPixels) {
			// Read into a RGBA Java scaneline (which is byte reversed, so go the BGRA here (???)
			eStatus = NCScbmReadViewLineBGRA( pJNIInfo->pFileView, (UINT32 *)pRGBAPixels);

			if (eStatus == NCSECW_READ_FAILED) {
				NCSFormatErrorText(NCS_JNI_ERROR, "method readLineRGB() failed (internal error).");
				nError = NCS_JNI_ERROR;
			}	
		}
		else {
			NCSFormatErrorText(NCS_JNI_ERROR, "method readLineRGB() could not allocate memory for RGB Array.");
			nError = NCS_JNI_ERROR;
		}
		// Copy the array back to the object and free the memory
		pEnv->ReleaseIntArrayElements(pRGBArray, pRGBAPixels, 0); //ReleasePrimitiveArrayCritical(pRGBArray, pRGBAPixels, 0);
	}
	return nError;
}

/* Devuelve el texto asociado al error que se ha producido */
JNIEXPORT jstring JNICALL Java_com_ermapper_ecw_JNCSFile_ECWGetErrorString
  (JNIEnv *pEnv, jobject JNCSFile, jint nErrorNumber)
{
	//return (*pEnv)->NewStringUTF(pEnv, NCSGetLastErrorText(nErrorNumber));
	
	return pEnv->NewStringUTF("....");
}
