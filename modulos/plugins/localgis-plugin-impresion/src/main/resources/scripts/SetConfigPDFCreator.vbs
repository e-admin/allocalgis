' Convert2PDF.vbs script
' Part of PDFCreator
' License: GPL
' Homepage: http://www.pdfforge.org/products/pdfcreator
' Windows Scripting Host version: 5.1
' Version: 1.1.0.0
' Date: December, 24. 2007
' Author: Frank Heindörfer
' Comments: This script convert a printable file in a pdf-file using 
'           the com interface of PDFCreator.

Option Explicit

Const maxTime = 30    ' in seconds
Const sleepTime = 250 ' in milliseconds

Dim objArgs, fso, PDFCreator, ReadyState, AppTitle

Set objArgs = WScript.Arguments

Set fso = CreateObject("Scripting.FileSystemObject")

AppTitle = "PDFCreator - Configuracion Impresion"

Set PDFCreator = Wscript.CreateObject("PDFCreator.clsPDFCreator", "PDFCreator_")
PDFCreator.cStart "/NoProcessingAtStartup"

With PDFCreator
  .cOption("UseAutosave") = 1
  .cOption("UseAutosaveDirectory") = 1
  .cOption("AutosaveFormat") = 0
  .cClearcache
  .cPrinterStop = false
  .cOption("AutosaveDirectory") = objArgs(0)
  .cOption("AutosaveFilename") = objArgs(1)
End With

 'Podemos crear array con valores modificados para restaurar ...
Wscript.Quit(0)

'--- PDFCreator events ---

Public Sub PDFCreator_eReady()
 ReadyState = 1
End Sub

Public Sub PDFCreator_eError()
	If PDFCreator.cErrorDetail("Number") <> 2 Then
	 MsgBox "An error is occured!" & vbcrlf & vbcrlf & _
	  "Error [" & PDFCreator.cErrorDetail("Number") & "]: " & PDFcreator.cErrorDetail("Description"), vbCritical + vbSystemModal, AppTitle
	 Wscript.Quit
	End if
End Sub
